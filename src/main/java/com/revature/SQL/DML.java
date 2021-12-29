package com.revature.SQL;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.dbcp2.BasicDataSource;
import org.postgresql.util.PGobject;

import com.revature.exception.IdDontExistException;
import com.revature.exception.VarArgsHasDiffrentException;
import com.revature.introspection.ColumnField;
import com.revature.introspection.Inspector;

import com.revature.util.DataBase;


	/**
	 * 
	 * @author marouanekhabbaz
	 * 
	 * 
	 * -DML provide methods to execute data manipulation statement against the database
	 * - All operations against the database using this class are auto committed.
	 * 
	 * -insert(Object... objs) ->
	 * 		- Variable Arguments (Varargs)  
	 * 		- Developer can add any number of objects in single invocation of insert() method 
	 * 		- Objs passed should be annotated with @Entity and should all be from the same class
	 * 		- Returns a list of created rows in the DB  
	 * 
	 * 
	 * -delete(Class<?> clazz ,int id) ->
	 * 	 		-clazz -> represent a class annotated with @Entity 
	 * 			- condition -> custom condition example : you need to delete all rows that color is green -->  column_color = 'green' 
	 * 					In general the condition look like this =>  column_name = 'value' 
	 * 			- @return List of the primary key of rows that been deleted 
	 *			- @throws SQLException
	 *
	 *
	 *- delete(Class<?> clazz , String condition)
	 *			- clazz -> represent a class annotated with @Entity 
	 * 			-condition -> custom condition example : you need to delete all rows that color 
	 * 			is green -->  column_color = 'green' 
	 * 			In general the condition look like this =>  column_name = 'value' 
	 *			- @return List of the primary key of rows that been deleted 
	 * 			- @throws SQLException
	 * 
	 * -update(Class<?> clazz , String statement , String  condition ) ->
	 * 			-  clazz -> represent a class annotated with @Entity 
	 * 			- statement -> update statement 
	 * 			- condition -> rows to be updated should have this condition ex: color = 'green'
	 * 			- @return list of objects representing the rows updated in the database 
	 * 			- @throws SQLException
	 * 
	 * -update(Class<?> clazz, String statement  ,int id )
	 *  		- clazz -> represent a class annotated with @Entity 
	 * 			- statement -> update statement 
	 * 			- id -> primary key of the row to update 
	 * 			- @return an object representing the row updated in the database 
	 *			- @throws SQLException
	 *			- @throws IdDontExistException
	 * 			
	 * 
	 *
	 */

public class DML {
	BasicDataSource connPool = DataBase.connPool;
	
	
	/**
	 * 
	 * @param columns representing a list of columns present in the class annotated with @Entity
	 * @return a String representing the columns that will be used in the insert statement against the DB
	 */
	
	private String columnsStatement(List< ColumnField> columns ) {
		
		String sql = " ( ";
		
		for(int i=0 ; i < columns.size() ; i++) {
			if(i == columns.size() -1 ) {
				sql += columns.get(i).getColumnName() + " ) ";
			}else {
				sql += columns.get(i).getColumnName() + " , ";
			}
			
		}
		
		return sql;
		
	}
	
	
	/**
	 * 
	 * @param columns representing a list of columns present in the class annotated with @Entity
	 * @param columnsAndValues  a hashMap that has key = column_name and a Value is the value of the field associate with column name in the object 
	 * @return a String representing a line of Values(value_of_column1 ,value_of_column2 , value_of_column3  )
	 * 			 that will be passed in the insert statement against the the DB.
	 */
	private String values(List< ColumnField> columns , HashMap<String, Object> columnsAndValues) {
		
		String sql = " ( ";

		
		for(int i=0 ; i < columns.size() ; i++) {	
			String colmun = columns.get(i).getColumnName();	
			
			Object value = ( columnsAndValues.get(colmun)  instanceof String) ? "'" + columnsAndValues.get(colmun)  + "'" :columnsAndValues.get(colmun) ;
			
			
			if(i == columns.size()-1 ) {
				sql +=	value    + " ) ";
			}else {
				sql += value +" , ";
			}	
			
		}
		
		return sql;
		
	}
	
	
	
	/**
	 * 
	 * @param objs takes Variable Arguments (Varargs)  , developer can add any number of objects in single invocation of insert() method 
	 * @param objs passed in this method should be annotated with @Entity and the should all be from the same class
	 * @return List<Object> representing the list of created rows in the DB  
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws SQLException
	 * @throws  VarArgsHasDiffrentException
	 * 
	 * 
	 */
	
	
	public 	List<Object> insert(Object... objs) throws IllegalArgumentException, IllegalAccessException, SQLException {
		
		Class<?> clazz = objs[0].getClass();
		
		Inspector<Class<?>> inspector1 = Inspector.of(clazz);
		List< ColumnField> columns =	inspector1.getColumns();
		String columnsStatement =  columnsStatement(columns);
		
		String sql = "INSERT INTO " + inspector1.getTableName() + columnsStatement + " \n"
				+ "VALUES "
				;
		for( int i=0 ; i < objs.length; i++ )  {
			Object obj = objs[i];
	     	
			if(!obj.getClass().equals(clazz)) {
				throw new VarArgsHasDiffrentException("Var arguments passed has diffrent type");
			}
			
		Inspector<Class<?>> inspector = Inspector.of(obj.getClass());
		HashMap<String, Object> columnsAndValues =	inspector.getColumnsAndValues(obj);
		
		if(i == objs.length -1) {
			sql += values(columns, columnsAndValues) + " ";
		}else {
			sql += values(columns, columnsAndValues) + " , \n";
		}
	
		}
				
		// return rows that been created 
		
		sql += " returning  " + inspector1.getTableName() ;
		
		System.out.println(sql);
		
		
		 
		try 
		(Connection conn = connPool.getConnection()  ;
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql); )
		{	
		List<Object> returnedRow = new ArrayList();
		if(rs != null) {
			 while(rs.next()) {		
			 returnedRow.add( rs.getObject(1));
			 }
		}
		System.out.println(returnedRow);

		return  returnedRow;
		
		}	
	}
	
	/**
	 * 
	 * @param clazz -> represent a class annotated with @Entity 
	 * @param id -> represent the primary key of the row you want to delete in the data base 
	 * @return  the id of the row deleted or 0 if the id passed does not exists in the database
	 * @throws SQLException
	 */
	
	public int delete(Class<?> clazz ,int id) throws SQLException {
		
		Inspector<Class<?>> inspector = Inspector.of(clazz);
		
		String sql = "DELETE FROM " + inspector.getTableName() + " WHERE " + inspector.getPrimaryKey().getColumnName() +" =  " + id + " returning " + inspector.getPrimaryKey().getColumnName()  ;
		
		System.out.println(sql);
		
		try
		(Connection conn = connPool.getConnection()  ;
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);)
		{
		if(rs.next()) {
			return rs.getInt(inspector.getPrimaryKey().getColumnName()) ;	
			
		}else {
			System.out.println("row with id " + id + " don't exist in the table " + inspector.getTableName());
			return 0;
		}
		}
	}
	
	
	
	
	/**
	 * 
	 * @param clazz -> represent a class annotated with @Entity 
	 * @param condition -> custom condition example : you need to delete all rows that color is green -->  column_color = 'green' 
	 * 					In general the condition look like this =>  column_name = 'value' 
	 * @return List of the primary key of rows that been deleted 
	 * @throws SQLException
	 * 
	 */
	
	
	public List<Object>  delete(Class<?> clazz , String condition) throws SQLException {
		
		Inspector<Class<?>> inspector = Inspector.of(clazz);
		
		String sql = "DELETE FROM " + inspector.getTableName() + " WHERE " +  condition 
				 + " returning " + inspector.getPrimaryKey().getColumnName() 
				;
		
		System.out.println(sql);
		try
		(Connection conn = connPool.getConnection()  ;
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);)
		{
		List<Object> deletedRow = new ArrayList();
		if(rs != null) {
			
			 while(rs.next()) {		
				 deletedRow.add( rs.getObject(1));
			 }
			 
			 System.out.println(deletedRow.size() + " rows was deleted from table " + inspector.getTableName());
			
		}
		else {
			
			System.out.println("0 row was deleted form table " + inspector.getTableName());
			return deletedRow;
			
		}
		return deletedRow;
		}
	}
	
	/**
	 * 
	 * @param clazz -> represent a class annotated with @Entity 
	 * @param statement -> update statement 
	 * @param id -> primary key of the row to update 
	 * @return an object representing the row updated in the database 
	 * @throws SQLException
	 * @throws IdDontExistException
	 */
	
	
	public Object update(Class<?> clazz, String statement  ,int id ) throws SQLException , IdDontExistException {
		
		Inspector<Class<?>> inspector = Inspector.of(clazz);
		
		//update  cars   set color = 'white' where  car_id = 208 returning cars
		
		String sql = "update " + inspector.getTableName() + " set " + statement  + " WHERE " + inspector.getPrimaryKey().getColumnName() +" =  " + id + " returning " + inspector.getTableName() ;
		
		System.out.println(sql);
		try(
		Connection conn = connPool.getConnection()  ;
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);){
		if(rs.next()) {
			return rs.getObject(1) ;	
			
		}else {

			throw new IdDontExistException("row with id " + id + " don't exist in the table " + inspector.getTableName());
		}
		}
	}
	
	
	/**
	 * @param clazz -> represent a class annotated with @Entity 
	 * @param statement -> update statement 
	 * @param condition -> rows to be updated should have this condition ex: color = 'green'
	 * @return list of objects representing the rows updated in the database 
	 * @throws SQLException
	 * 
	 * 
	 */
	
	public List<Object> update(Class<?> clazz , String statement , String  condition ) throws SQLException  {
		
		Inspector<Class<?>> inspector = Inspector.of(clazz);
		
		//update  cars   set color = 'white' where  car_id = 208 returning cars
		
		String sql = "update " + inspector.getTableName() + " set " + statement  + " WHERE " 
		+  condition + " returning " + inspector.getTableName() ;
		
		System.out.println(sql);
		try
		(Connection conn = connPool.getConnection()  ;
		
		Statement stmt = conn.createStatement();
		
		ResultSet rs = stmt.executeQuery(sql);)
		{
		
		List<Object> updatedRow = new ArrayList<>();
		
		if(rs != null) {
			while(rs.next()) {
				updatedRow.add(rs.getObject(1)) ;	
			}
		}
		
		System.out.println( updatedRow.size() + " rows in the table were updated ");
		
		return updatedRow;
		
	}
		
		
	}

}
