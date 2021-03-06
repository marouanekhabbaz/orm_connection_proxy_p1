package com.revature.introspection;

import java.beans.ConstructorProperties;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.revature.annontation.Column;

import com.revature.annontation.Entity;
import com.revature.annontation.ForeignKey;
import com.revature.annontation.Id;
import com.revature.exception.EntityClassWithNoAnnotatedConstructorException;
import com.revature.exception.NoForeignKeyFoundException;

/*
 * 
 * 
 * 
 */

public class Inspector<T> { // we're inferring that the MetaModel class can only be a metamodel of another class
	
	private Class<T> clazz;
	private PrimaryKeyField primaryKeyField; // we created this "type" in our com.revature.util package
	private List<ColumnField> columnFields;
//	private List<ForeignKeyField> foreignKeyFields;
	
	private HashMap<String, Object> columnsAndValues = new HashMap<>();
	
	
	
	
	/**
	 * 
	 * @param clazz
	 * @return Inspector
	 *  @throws IllegalStateException
	 *  
	 *  Check the class if it's annotated with @Entity 
	 *  
	 *  If not it throw an IllegalStateException
	 *  
	 *  If annotated return an new Inspector object 
	 *  
	 */
	
	
	public static Inspector<Class<?>> of(Class<?> clazz) {
	
		if (clazz.getAnnotation(Entity.class) == null) {
			throw new IllegalStateException("Cannot create Inspector object! Provided class "
					+ clazz.getName() + " is not annotated with @Entity");
			
		}

		return new Inspector<>(clazz);
	}

	
	
	// we only call the constructor when we invoke the MetaModel.of(MyClass.class);
	public Inspector(Class<?> clazz) {
		this.clazz = (Class<T>) clazz;
		this.columnFields = new LinkedList<>();
	
		
	}
	
	// this method will return all the column fields of a metamodel class
	public List<ColumnField> getColumns()  {
		// this method reutrns all the properties of the class that are marked with @Column annotation
		
		Field[] fields = clazz.getDeclaredFields();
		
		// for each field within the above Field[], check if it has the Column annotation
		// if it DOES have the @Column annotation add it to the metamodels's columnFields LinkedList
		for (Field field : fields) {
			
		
			
			// The column reference variable will NOT be null, if the field is annotated with @Column
			Column column = field.getAnnotation(Column.class);
			
			if (column != null) {
				// if it is indeed marked with @Column, instantiate a new ColumnField object with its data
				field.setAccessible(true);
	         
				columnFields.add(new ColumnField(field));
			}
			
		}
		
		// add some extra logic in the case that the class doens't have any column fields
		if (columnFields.isEmpty()) {
			throw new RuntimeException("No columns found in: " + clazz.getName());
		}
		
		return columnFields;
	}
	
	/**
	 * 
	 * @param obj
	 * @return HashMap that has all Field as key and  the Value of that field as a Key
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * 
	 * Start by invoking this.getColumns() to create a list of fields that is annotated  with @Column 
	 * 
	 * Getting the all declared field of the object passed as a parameter  
	 * 
	 * For each field, we check if a Column (if it's in the list created by this.getColumns() 
	 * 
	 * If it's a list we make its private value accessible by invoking .setAccessible(true)
	 * 
	 * Finally, we add  it to the hashMap
	 * 
	 * If the HashMap is empty that means the object doesn't have any column and we throw an error
	 * 
	 * Return the the HashMap
	 * 
	 */
	

	public HashMap<String, Object>  getColumnsAndValues(Object obj) throws IllegalArgumentException, IllegalAccessException {
	
		List< ColumnField> columns =	this.getColumns() ;
		
		Field[] fields = obj.getClass().getDeclaredFields();
	
		for (Field field : fields) {	

	List<ColumnField > isColumn =	 columnFields.stream().filter(c-> c.getName().equals(field.getName())).collect(Collectors.toList());
			
			if(! isColumn.isEmpty()) {
				
				field.setAccessible(true);
				Object value = field.get(obj); 				
            columnsAndValues.put(isColumn.get(0).getColumnName(), value);

			}
				
		}
		
		
		if (columnsAndValues.isEmpty()) {
			throw new RuntimeException("No columns found in this object " + obj.toString() );
		}
		
		return columnsAndValues;
	}
	
	/**
	 * 
	 * @param clazzB is an table annotated with @Entity and that have a joined column to the instance of this class
	 * @return the column that reference to the given primary key in the current class
	 */
	
	
	public ForeignKeyField getForeignKey(Class<?> clazzB) {
		
		Field[] fields = clazz.getDeclaredFields();

		for (Field field : fields) {
			
			if (field.getAnnotation(ForeignKey.class)  != null) {
				
				ForeignKeyField	foreignKey = new 	ForeignKeyField(field);
				String foreignKeyOfJoinedColumn = foreignKey.getJoinedColumn();
				String joindColumn = getColumnUsingColumnName(clazzB ,foreignKeyOfJoinedColumn);
	
				
			if(foreignKeyOfJoinedColumn.equals(joindColumn)) {
				return foreignKey ;
			}
			}
			
		}

		
		throw new  NoForeignKeyFoundException("Did not find a column refrencing to  " + clazzB.getName());
	
		}

	/**
	 * 
	 * @param clazz
	 * @param columnName
	 * @return column name of the join column in this table
	 */
	private String getColumnUsingColumnName(Class<?> clazz ,String columnName) {
		Inspector<?> i = of(clazz);
		
		List<ColumnField> columns =  i.getColumns() ;
		
		PrimaryKeyField primaryKey = i.getPrimaryKey();
		if(primaryKey.getColumnName().equals(columnName)) {
			return primaryKey.getColumnName();
		}
		
		for(ColumnField column : columns) {
			if(column.getColumnName().equals(columnName)) {
				return column.getColumnName();
			}
		}
		
		
		
		 new RuntimeException(columnName + " doesn't exist in " + clazz.getName());
		return null;
		
	}
	
	
	
	
	// As of right now I have a way to extract the primary key of a MetaModel object
	public PrimaryKeyField getPrimaryKey() {
		// capture an array of its fields
		Field[] fields = clazz.getDeclaredFields();
		// check if the Id comes back as NOT null
		for (Field field : fields) {
			Id primaryKey = field.getAnnotation(Id.class);
			
			// IF primaryKey is NOT null, then generate a PrimaryKeyField object from that field
			if (primaryKey != null) {
				// this will capture the first and (hopefully) only primary key that exists
				return new PrimaryKeyField(field);
			}
		}
		throw new RuntimeException("Did not find a field annotated with @Id in " + clazz.getName());
	}
	
	
	/**
	 * 
	 * @param clazz
	 * @return the Constructor annotated with @ConstructorProperties
	 * @throw EntityClassWithNoAnnotatedConstructorException if the class don't have 
	 * 		  a constructor annotated with  @ConstructorProperties
	 */
	
	public static  Constructor findAnnotatedConstructor(Class<?> clazz) {
		
		System.out.println("Printing the public constructors of class " + clazz.getName());
		
		 Constructor[] constructors = clazz.getConstructors();
		
		for (Constructor constructor : constructors) {

			  Annotation annotation = constructor.getAnnotation(ConstructorProperties.class);
			  
			 
			  if(annotation instanceof ConstructorProperties) {
				String[]  s = ((ConstructorProperties) constructor.getAnnotation(ConstructorProperties.class)).value();  
				return constructor; 

			  }
		}
		
		throw new EntityClassWithNoAnnotatedConstructorException("Entity class must have a constructor annotated with @ConstructorProperties");
	}
	
	
	public String getTableName() {
		return  clazz.getAnnotation(Entity.class).tableName();
	}
	
	public String getSimpleClassName() {
		return clazz.getSimpleName();
	}
	
	public String getClassName() {
		return clazz.getName();
	}
	

}
