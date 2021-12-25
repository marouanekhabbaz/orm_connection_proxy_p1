package com.revature.introspection;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import com.revature.annontation.Column;
import com.revature.annontation.Entity;
import com.revature.annontation.Id;

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
	
	private HashMap<Object, Object> columnsAndValues = new HashMap<>();
	
	
	
	
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
	

	public HashMap<Object, Object>  getColumnsAndValues(Object obj) throws IllegalArgumentException, IllegalAccessException {
	
		this.getColumns() ;
		
		Field[] fields = obj.getClass().getDeclaredFields();
	
		for (Field field : fields) {	

		Optional<ColumnField> isColumn =	columnFields.stream().filter(c-> c.getName().equals(field.getName())).findAny();
			
			if(isColumn.isPresent()) {
				
				field.setAccessible(true);
				Object value = field.get(obj); 				
            columnsAndValues.put(field.getName(), value);

			}
				
		}
		
		
		if (columnsAndValues.isEmpty()) {
			throw new RuntimeException("No columns found in this object " + obj.toString() );
		}
		
		return columnsAndValues;
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
