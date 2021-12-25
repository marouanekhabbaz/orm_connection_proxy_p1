package com.revatrure.demo;

import java.lang.reflect.Field;
import java.util.List;

import com.revature.SQL.DDL;
import com.revature.introspection.ColumnField;
import com.revature.introspection.Inspector;
import com.revature.introspection.PrimaryKeyField;
import com.revature.util.DataBase;


public class Driver {


	public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException {
		System.out.println("befoooore ");
		
		DataBase db = new DataBase().getConnection();
		
		Car bmw = new Car(1, "bmw", "blue");
		
		DDL d = new DDL();
		
	//	System.out.println(d.create(Car.class));
		System.out.println(d.alter(Car.class, "ADD Email33 varchar(255)"));
		
		
	//	System.out.println(d.truncate(Car.class));
		
	//	System.out.println(bmw);
		
		

        // Get the all field objects of User class
//        Field[] fields = bmw.getClass().getDeclaredFields();
//        System.out.println(fields);
        
  
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
//		
		
		
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
