package com.revature.SQL;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.revature.exception.IdDontExistException;
import com.revature.exception.VarArgsHasDiffrentException;
import com.revature.introspection.ColumnField;
import com.revature.introspection.Inspector;
import com.revature.util.DataBase;


/**
 * 
 * @author marouanekhabbaz
 * 
 *  Transaction provide methods to execute data manipulation (CRUD) statement against the database and
 *  gives the developer control to manage the transaction (commit , rollback ...)
 * - All operations against the database using this class are not committed and need to invoke .commit() method to persist change.
 * 
 * 
 * 
 *
 * 	- commit() -> To make change persistent  
 *	- setSavePoint(String name) -> To create a save point to roll back to it later down the road 
 * 								- @return Savepoint
 *	- rollBack(Savepoint savePoint) -> Cancel all change made to the database after creating a savepoint 
 *
 * 	- rollBack() -> Delete all change made to the database since the last commit
 * 
 *  -end()-> 
 *    		-close the connection 
 * 			-then finalize the transaction object.
 * 
 * 
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
*/
	


public class Transaction {
	private  Connection conn;
	private static final Logger log = LoggerFactory.getLogger(Transaction.class);
	
	// this block will be invoked each time the we instantiate  a new transaction 
	 {
	BasicDataSource connPool = DataBase.connPool;
	
	try {
		conn =connPool.getConnection();
		conn.setAutoCommit(false);
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	}

	 /**
	  * close the connection 
	  * then finalize the transaction object.
	  */
	 
	 public void end() {
		 try {
			conn.close();
			System.out.println("Transaction ended");
			this.finalize();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 }
	
	 /**
	  *To make change persistent  
	  */
	public void commit() {
		 try {
			conn.commit();
		log.info("Transaction  commited ");
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			
			log.error("Commit failed ");
			e.printStackTrace();
		}
	}
	
	
	/**
	 *  To create a save point to rollback to it later down the road 
	 * 
	 * @param name
	 * @return Savepoint
	 */
	public Savepoint setSavePoint(String name) {
		 Savepoint savepoint;
		try {
			savepoint = conn.setSavepoint(name);
		log.info("savepoint has been created ");
			 return savepoint;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.error("savepoint failed ");
			e.printStackTrace();
		}
		return null;
		
	}
	
	/**
	 * Cancel all change made to the database after creating a savepoint 
	 * 
	 * @param savePoint
	 * 
	 */
	public void rollBack(Savepoint savePoint) {
		try {
			conn.rollback(savePoint);
			log.info("rollback to a savepoint has been invoked ");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.error("rollBack failed ");
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Delete all change made to the database since the last commit
	 */
	public void rollBack() {
		try {
			conn.rollback();
		 log.info("rollback has been invoked ");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.error("rollBack failed ");
			e.printStackTrace();
		}
	}
	
	
	
	
	

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
	 * Not committed until invoking .commit();
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
	
		sql += " returning  " + inspector1.getTableName() ;
		
		System.out.println(sql);
		
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		
		List<Object> returnedRow = new ArrayList();
		if(rs != null) {
			 while(rs.next()) {		
			 returnedRow.add( rs.getObject(1));
			 }
		}
		System.out.println(returnedRow);
		
		stmt.close();
		rs.close();

		return  returnedRow;
		
		}	
	
	
	/**
	 *  Not committed until invoking .commit()
	 * @param clazz -> represent a class annotated with @Entity 
	 * @param id -> represent the primary key of the row you want to delete in the data base 
	 * @return  the id of the row deleted or 0 if the id passed does not exists in the database
	 * @throws SQLException
	 */
	
	public int delete(Class<?> clazz ,int id) throws SQLException {
		
		Inspector<Class<?>> inspector = Inspector.of(clazz);
		
		String sql = "DELETE FROM " + inspector.getTableName() + " WHERE " + inspector.getPrimaryKey().getColumnName() +" =  " + id + " returning " + inspector.getPrimaryKey().getColumnName()  ;
		
		System.out.println(sql);
		
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		
		if(rs.next()) {
			int returnedId = rs.getInt(inspector.getPrimaryKey().getColumnName()) ;
			stmt.close();
			rs.close();
			return returnedId;
			
		}else {
			System.out.println("row with id " + id + " don't exist in the table " + inspector.getTableName());
			stmt.close();
			rs.close();
			return 0;
		}
		
	}
	
	
	
	
	/**
	 * 
	 * 
	 * Not committed until invoking .commit()
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
	
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
	
		List<Object> deletedRow = new ArrayList();
		if(rs != null) {
			
			 while(rs.next()) {		
				 deletedRow.add( rs.getObject(1));
			 }
			 
			 System.out.println(deletedRow.size() + " rows was deleted from table " + inspector.getTableName());
				stmt.close();
				rs.close();
		}
		else {
			
			System.out.println("0 row was deleted form table " + inspector.getTableName());
			stmt.close();
			rs.close();
			return deletedRow;
			
		}
		return deletedRow;
		}
	
	
	
	
	/**
	 * 
	 * Not committed until invoking .commit()
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
		
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		if(rs.next()) {
			Object obj = rs.getObject(1) ;
			stmt.close();
			rs.close();
			
			return obj;
			
		}else {
			stmt.close();
			rs.close();
			throw new IdDontExistException("row with id " + id + " don't exist in the table " + inspector.getTableName());
		}
		
		
		}
	
	
	
	/**
	 * Not committed until invoking .commit()
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
		
		String sql = "update " + inspector.getTableName() + " set " + statement  + " WHERE " 
		+  condition + " returning " + inspector.getTableName() ;
		
		System.out.println(sql);
		
		
		
		Statement stmt = conn.createStatement();
		
		ResultSet rs = stmt.executeQuery(sql) ;
		
		
		List<Object> updatedRow = new ArrayList<>();
		
		if(rs != null) {
			while(rs.next()) {
				updatedRow.add(rs.getObject(1)) ;	
			}
		}
		
		System.out.println( updatedRow.size() + " rows in the table were updated in this transaction");
		
		stmt.close();
		rs.close();
		
		return updatedRow;
		
		}
		
		
	}
	
		
		
	

	

	
	

