package com.revature.SQL;

import java.beans.ConstructorProperties;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.dbcp2.BasicDataSource;

import com.revature.introspection.ColumnField;
import com.revature.introspection.ForeignKeyField;
import com.revature.introspection.Inspector;
import com.revature.util.DataBase;

/**
 * 
 * @author marouanekhabbaz
 * 
 * - get(Class<?> clazz, int id)
 *	 	- clazz -> class annotated with @Entity and have a Constructor annotated with @ConstructorProperties
 *   	- id -> represent the primary key 
 *  	- @return -> an instance of the class passed , instantiated using the constructor annotated with  @ConstructorProperties
 *  
 *  
 *  -getAll(Class<?> clazz )
 *  	 - clazz -> class annotated with @Entity and have a Constructor annotated with @ConstructorProperties
 * 		 - @return -> A linkedList of object of all rows inside the table in database
 * 
 *  
 *  -getWhere(Class<?> clazz , String condition ) 
 *           - clazz -> class annotated with @Entity and have a Constructor annotated with @ConstructorProperties
 *		 	 - condition -> custom condition example : you need to select all rows that color is green -->  column_color = 'green' 
 * 						In general the condition look like this =>  column_name = 'value' 
 * 			 - @return -  A linkedList of object of all rows inside the table in database
 * 
 *
 *  
 */

public class DQL {
	BasicDataSource connPool = DataBase.connPool;
	
	
	/**
	 * 
	 * @param clazz -> class annotated with @Entity and have a Constructor annotated with @ConstructorProperties
	 * @param id -> represent the primary key 
	 * @return -> an instance of the class passed , instantiated using the constructor annotated with  @ConstructorProperties
	 * 
	 */
	
	
	public Object get(Class<?> clazz, int id) throws SQLException{
		Inspector<Class<?>> inspector = Inspector.of(clazz);
		Constructor constructor = inspector.findAnnotatedConstructor(clazz);
		
		String sql = "SELECT * FROM " + inspector.getTableName() + " WHERE " + inspector.getPrimaryKey().getColumnName() 
				+" =  " + id ;
		
		System.out.println(sql);
		
		String[]  columns = ((ConstructorProperties) constructor.getAnnotation(ConstructorProperties.class)).value();  

		try
		(Connection conn = connPool.getConnection()  ;
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);)
		{
			List<Object> args = new ArrayList<>();
			
		if(rs.next()) {
			
			for(String column: columns) {
				args.add(rs.getObject(column));
			}
	
		return 	constructor.newInstance(args.toArray());
			
		}else {
			System.out.println("No result found ");
			return null;
		}	
	} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		return null;
		
		
	}
	
	
	/**
	 * 
	 * @param clazz -> class annotated with @Entity and have a Constructor annotated with @ConstructorProperties
	 * @return -> A linkedList of object of all rows inside the table in database
	 * @throws SQLException
	 * 
	 */
	
	public LinkedList<Object> getAll(Class<?> clazz ) throws SQLException{
		Inspector<Class<?>> inspector = Inspector.of(clazz);
		Constructor constructor = inspector.findAnnotatedConstructor(clazz);
		LinkedList<Object> resultList = new LinkedList<>();
		String sql = "SELECT * FROM " + inspector.getTableName() ;
		
		System.out.println(sql);
		
		String[]  columns = ((ConstructorProperties) constructor.getAnnotation(ConstructorProperties.class)).value();  

		try
		(Connection conn = connPool.getConnection()  ;
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);)
		{
			
			
		while(rs.next()) {
			List<Object> args = new ArrayList<>();
			for(String column: columns) {
				args.add(rs.getObject(column));
			}
			
		resultList.add(constructor.newInstance(args.toArray()));
			
		}	
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		if(resultList.size()==0) {
			System.out.println( inspector.getTableName() + " is empty ");
		}
		
		return resultList;
		
	}
	
	
	/**
	 * 
	 * @param clazz -> class annotated with @Entity and have a Constructor annotated with @ConstructorProperties
	 * @param condition -> custom condition example : you need to select all rows that color is green -->  column_color = 'green' 
	 * 						In general the condition look like this =>  column_name = 'value' 
	 * @return -  A linkedList of object of all rows inside the table in database
	 * @throws SQLException
	 */
	
	public LinkedList<Object> getWhere(Class<?> clazz , String condition ) throws SQLException{
		Inspector<Class<?>> inspector = Inspector.of(clazz);
		Constructor constructor = inspector.findAnnotatedConstructor(clazz);
		LinkedList<Object> resultList = new LinkedList<>();
		
		String sql = "SELECT * FROM " + inspector.getTableName() + "  where " + condition ;
		
		System.out.println(sql);
		
		String[]  columns = ((ConstructorProperties) constructor.getAnnotation(ConstructorProperties.class)).value();  

		try
		(Connection conn = connPool.getConnection()  ;
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);)
		{
			
			
		while(rs.next()) {
			List<Object> args = new ArrayList<>();
			for(String column: columns) {
				args.add(rs.getObject(column));
			}
			
		resultList.add(constructor.newInstance(args.toArray()));
			
		}	
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		if(resultList.size()==0) {
			System.out.println( inspector.getTableName() + " is empty ");
		}
		
		return resultList;
		
	}
	
	
	/**
	 * 
	 * @param querry -> represent SQL statement 
	 * @return -> LinkedList of Hashmaps representing each row returned in the result
	 * 			- The hashmap has key = column_name , value = the value of that column in each row 
	 * @throws SQLException
	 */
	
	public LinkedList<HashMap<String, Object>> nativeQuerry( String querry ) throws SQLException{
		LinkedList <HashMap<String, Object>> returnedRow = new LinkedList<>();
		try 
		(Connection conn = connPool.getConnection()  ;
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(querry ); )
		{	
		
		if(rs != null) {
			   ResultSetMetaData rsMetaData = rs.getMetaData();
			   int countOfColumns = rsMetaData.getColumnCount();
			   ArrayList<String> columnsNames = new ArrayList<>();
			   for(int i = 1; i<=countOfColumns; i++) {
				   columnsNames.add(rsMetaData.getColumnName(i));
			   }
			 while(rs.next()) {		
			HashMap<String, Object> row = new HashMap<>();	 
			// returnedRow.add( rs.getObject(1));
			 
			columnsNames.forEach(column-> {
				try {
					row.put(column, rs.getObject(column));
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} );
			
			returnedRow.add(row);
			
				
			 
			 }
		}
		returnedRow.forEach(r-> System.out.println("row " + r));
		
		}	
		return  returnedRow;
	}
	
	
	/**
	 * 
	 * @param clazzA -> first class -> has the a foreign key referencing to the primary key of the second class
	 * @param clazzB -> second class -> has a primary key that is used as foreign key in clazzA
	 *  @return -> LinkedList of Hashmaps representing each row returned in the result
	 * 			- The hashmap has key = column_name , value = the value of that column in each row 
	 * @throws SQLException
	 * 
	 */
	
	
	public LinkedList<HashMap<String, Object>> joinQuerry( Class<?> clazzA , Class<?> clazzB ) throws SQLException{
		Inspector<Class<?>> inspectorA = Inspector.of(clazzA);
		Inspector<Class<?>> inspectorB = Inspector.of(clazzB);
		System.out.println(clazzA.getName());
		
		ForeignKeyField foreignKey = inspectorA.getForeignKey(clazzB);
		
		
		System.out.println(foreignKey.getForeignKeyName()  + " from here ===========");
		System.out.println(foreignKey.getJoinedColumn()  + " from here ===========");
		
		
		String sql = "SELECT * "
				+ "FROM  "   + inspectorA.getTableName()  + " AS a   \n"
				+ " JOIN "  + inspectorB.getTableName() +  " AS b  \n "
				+  "  On a." + foreignKey.getForeignKeyName()  + " = b." +
				foreignKey.getJoinedColumn() 
				
			;
		
		System.out.println(sql);
		
		LinkedList <HashMap<String, Object>> returnedRow = new LinkedList<>();
		try 
		(Connection conn = connPool.getConnection()  ;
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery( sql ); )
		{	
		
		if(rs != null) {
			   ResultSetMetaData rsMetaData = rs.getMetaData();
			   int countOfColumns = rsMetaData.getColumnCount();
			   ArrayList<String> columnsNames = new ArrayList<>();
			   for(int i = 1; i<=countOfColumns; i++) {
				   columnsNames.add(rsMetaData.getColumnName(i));
			   }
			 while(rs.next()) {		
			HashMap<String, Object> row = new HashMap<>();	 
			// returnedRow.add( rs.getObject(1));
			 
			columnsNames.forEach(column-> {
				try {
					row.put(column, rs.getObject(column));
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} );
			
			returnedRow.add(row);
			
				
			 
			 }
		}
		returnedRow.forEach(r-> System.out.println("row " + r));
		
		}	
		return  returnedRow;
	}
	
	
	
	
}

	

