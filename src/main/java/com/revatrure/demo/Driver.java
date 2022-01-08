package com.revatrure.demo;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.revature.SQL.DDL;
import com.revature.SQL.DML;
import com.revature.SQL.DQL;
import com.revature.SQL.Transaction;
import com.revature.exception.DdlException;
import com.revature.introspection.ColumnField;
import com.revature.introspection.Inspector;
import com.revature.introspection.PrimaryKeyField;
import com.revature.util.DataBase;
import com.revature.util.Environment;


public class Driver {


	public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException {
		System.out.println("befoooore ");
		
		DataBase db = new DataBase().getConnection(Environment.TEST);
		
		try {
			db.addMappedClass(Person.class,  Car.class , JoinedTable.class );
		} catch (DdlException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
		
		Car bmw = new Car(1, "bmw", "blue" );
		Car renault = new Car(2, "renault", "red");
		Person p = new Person(0, "marouane", "pass", 0, false);
		
//		JoinedTable j43 = new JoinedTable(2, 2);
//		JoinedTable j1 = new JoinedTable(1, 1);
//		JoinedTable j2= new JoinedTable(3, 3);
//		JoinedTable j3 = new JoinedTable(4, 4);
		
		DDL d = new DDL();
		
		// DML dml = new DML();
		
		
		
		DML	dml = new DML();
		
		
		DQL  dql = new DQL();
		
		
//		dml.insert(bmw, renault)
		
//		try {
//			LinkedList<HashMap<String, Object>> result = dql.joinQuerry(Car.class, Person.class);
//			
//		
//			LinkedList<HashMap<String, Object>> resultWithCondition = dql.joinQuerry(Car.class, Person.class, "color = 'red' ");
//			
//			LinkedList<HashMap<String, Object>> resultForm3tables = dql.joinQuerryManyToMany( JoinedTable.class ,Car.class, Person.class);
//		
//			LinkedList<HashMap<String, Object>> resultForm3tablesWithCondition = dql.joinQuerryManyToMany( JoinedTable.class ,Car.class, Person.class, "a.color ='red'");
//			
//		} catch (SQLException e2) {
//			// TODO Auto-generated catch block
//			e2.printStackTrace();
//		}
//		
		
//		try {
//		
//			List<Object> listInserted =	dml.insert(bmw, renault);
//			
//			int deleted = dml.delete(Car.class, 2);
//			
//			List<Object> deletedRows = dml.delete(Car.class, "color = 'red'");
//			
//			Object updated = dml.update(Car.class, "color = 'green' ", 67);
//			
//			
//			List<Object> updatedRows = dml.update(Car.class,  "color = 'green' " , " color = 'blue' ");
//			
//			
//		} catch (IllegalArgumentException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} catch (IllegalAccessException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} catch (SQLException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		
//		Inspector<Car> i = new Inspector<Car>(Car.class);
		
	//	Transaction t = new Transaction();
		
//		try {
//			
//		d.create(JoinedTable.class);
//	
//		dml.insert(j43, j1, j2 , j3);
//			
//	LinkedList<HashMap<String, Object>> rows	=	dql.joinQuerry(Car.class, Person.class , "color = 'red' ");
	
//	 dql.joinQuerryManyToMany(JoinedTable.class, Car.class, Person.class , "a.color ='blue' ");
//	
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		
		
		
//		Transaction transaction = new Transaction();
//		
//		try {
//			transaction.insert(bmw, renault);
//			transaction.commit();
//			transaction.insert(bmw, renault);
//			Savepoint save =	transaction.setSavePoint("savepoint");
//			transaction.delete(Car.class, 1);
//			transaction.rollBack(save);
//			transaction.commit();
//			transaction.end();
//		} catch (IllegalArgumentException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} catch (IllegalAccessException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} catch (SQLException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//	
		
//		try {
	//	d.(Car.class, Person.class);
			
		//	db.addMappedClass( Person.class , Car.class );
			
//		dml.insert(p)	;	
//		dml.insert(bmw, renault);
		
//			t.insert(bmw, renault);
//			t.commit();
//			Savepoint x =	t.setSavePoint("marouane");
//			t.insert(bmw, renault);
//			t.rollBack(x);
//			t.commit();
		
		
//		System.out.println(dql.getWhere(Car.class, "color= 'blue' "));
		
//		Car c =	(Car) dql.get(Car.class, 1);
//		Person p1 = (Person) dql.get(Person.class, 1);
		
//		System.out.println(p1);
//		
//	System.out.println( "my color is : " +	c.getColor() );
//	System.out.println("my id is " + c.getId());
//	System.out.println("my model is " + c.getModel());
	
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
//		try {
//			dml.insert(bmw, renault);
//		} catch (IllegalArgumentException | IllegalAccessException | SQLException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		

		
		// i.getForeignKey(null)
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
		
//		DDL ddl = new DDL();
//		
//		try {
//			ddl.alter(Car.class, " ADD Email varchar(255) ");
//		} catch (DdlException e) {
//			
//			e.printStackTrace();
//		}
//		
		
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
