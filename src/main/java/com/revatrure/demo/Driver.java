package com.revatrure.demo;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

import com.revature.SQL.DDL;
import com.revature.SQL.DML;
import com.revature.SQL.DQL;
import com.revature.exception.DdlException;
import com.revature.introspection.ColumnField;
import com.revature.introspection.Inspector;
import com.revature.introspection.PrimaryKeyField;
import com.revature.util.DataBase;


public class Driver {


	public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException {
		System.out.println("befoooore ");
		
		DataBase db = new DataBase().getConnection();
		
		
		
		Car bmw = new Car(1, "bmw", "blue" );
		Car renault = new Car(2, "renault", "red");
		Person p = new Person(0, "marouane", "pass", 0, false);
		DDL d = new DDL();
		
		DML dml = new DML();
		
		DQL  dql = new DQL();
		
		try {
	//	d.(Car.class, Person.class);
			
		//	db.addMappedClass( Person.class , Car.class );
			
		dml.insert(p)	;	
		dml.insert(bmw, renault);
		
		System.out.println(dql.getWhere(Car.class, "color= 'blue' "));
		
//		Car c =	(Car) dql.get(Car.class, 1);
//		Person p1 = (Person) dql.get(Person.class, 1);
		
//		System.out.println(p1);
//		
//	System.out.println( "my color is : " +	c.getColor() );
//	System.out.println("my id is " + c.getId());
//	System.out.println("my model is " + c.getModel());
	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		try {
//			dml.insert(bmw, renault);
//		} catch (IllegalArgumentException | IllegalAccessException | SQLException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		
//		Inspector<Car> i = new Inspector<Car>(Car.class);
//		
//		i.findAnnotatedConstructor(Car.class);
		
		
//		
//		try {
////			dml.delete(Car.class, 1000);
//			System.out.println(	dml.update(Car.class,  " color = 'red' ", " color = 'green' "));
//			
//			System.out.println(	dml.update(Car.class,  " color = 'red' ", 2));
//			
//			System.out.println(dml.delete(Car.class, 4));
//			
//			System.out.println(dml.delete(Car.class, " color = 'red' "));
//			
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
//		try {
//			d.create(Car.class);
//			dml.insert(bmw, renault );
//		} catch (IllegalArgumentException | IllegalAccessException | SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
//		try {
//			
//		} catch (DdlException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
//  try {
//	System.out.println(d.create(Car.class));
//} catch (DdlException e) {
//	// TODO Auto-generated catch block
//	e.printStackTrace();
//}
		
		
		
//		try {
//			System.out.println(d.alter(Car.class, "ADD Email33h varchar(255)"));
//		} catch (DdlException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		try {
//			 System.out.println(d.drop(Car.class));
//			} catch (DdlException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		
//		try {
//			System.out.println(d.truncate(Car.class));
//		} catch (DdlException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
  
		
		

	//	System.out.println(bmw);  
	
		

        // Get the all field objects of User class
//        Field[] fields = bmw.getClass().getDeclaredFields();
//        System.out.println(fields);
//        
//  
//        for (int i = 0; i < fields.length; i++) {
//        	
//        	System.out.println(i);
//        
//            // get value of the fields
//        	fields[i].setAccessible(true);
//            Object value = fields[i].get(bmw);
//  
//            // print result
//            System.out.println("Value of Field "
//                               + fields[i].getName()
//                               + " is " + value);
//        }
		
		
		
//		Inspector<Class<?>> i = Inspector.of(Car.class); 
//		
//System.out.println(i.getColumnsAndValues(bmw) + "===========================.");
//		
	//	System.out.println(i.getClass());
		
		
//		List<ColumnField> = MetaModel<>(clazz)
//		List<ColumnField> l = i.getColumns();
//		
//		
//		
//		 PrimaryKeyField  p = i.getPrimaryKey();
		 
		
	//	System.out.println(p.getName());
		
	
	
		
//		for (ColumnField c : l) {
//	//		System.out.println(c.getName());
//			
//		}
		
		System.out.println("aftererr ");
	}
	

	
	
	
	
		
		
	
}
