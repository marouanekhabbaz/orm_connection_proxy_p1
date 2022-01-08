package com.revature.SQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.revatrure.demo1.Car;
import com.revature.exception.DdlException;
import com.revature.introspection.ColumnField;
import com.revature.introspection.Inspector;
import com.revature.introspection.PrimaryKeyField;
import com.revature.util.DataBase;

/**
 * 
 * @author marouanekhabbaz
 * 
 * Class used to execute Data definition language statements CREATE , ALTER , TRUNCATE , DROP
 * 
 * Has the following method :
 * 
 *create( class) -> 
 *			-takes a class name of a class annotated with @Entity , 
 *			and create a table in the database with the name passed as tableName.
 *		 	- and columns of each field annotated with @Column . 
 * 
 *  
 * alter(Class<?> clazz , String sql) -> 
 * 		- takes a class name of a class annotated with @Entity
 * 		- sql -> statement to be passed Example : ADD username varchar(10);
 * 		 this method is used to modify , delete , add a column in an existing table also to add or
 * 		 drop or modify a constraint of an existing column 
 * 
 * 
 *  drop(Class<?> clazz) ->
 *  	- takes a class name of a class annotated with @Entity
 *  	- this method is dangerous use it only if you are sure that you want to delete an existing table 
 *		- this method is used to delete an existing table 
 *  	
 * truncate(Class<?> clazz)->
 * 		- takes a class name of a class annotated with @Entity
 * 		-this method is dangerous use it only if you are sure that you want to delete all data inside of an existing table 
   *		-this method is used to delete every rows in an existing table 
 * 
 *
 */

public class DDL {
	
	BasicDataSource connPool = DataBase.connPool;
	
	private static final Logger log = LoggerFactory.getLogger(DDL.class);
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	
	

		public DDL() {
		super();
	}


		/**
		 * 
		 * @param clazz
		 * @return boolean
		 * @throws DdlException
		 * 
		 * Check if class passed has @Entity annotation by invoking Inspector.of()
		 * 
		 * Get the primary key by invoking the .getPrimaryKey() of the inspector object 
		 * 
		 * Get list of columns by invoking .getColumns() of the inspector object
		 * 
		 * Retrieve the constraint like data type and default value .... from each column
		 * 
		 * Iterate thru the list and append each column to the SQL statement
		 * 
		 * Execute the statement 
		 * 
		 * return true or catch the exception if an error occurs 
		 * 
		 */
		
	
		public boolean create(Class<?> clazz) throws DdlException {
			
			
			Inspector<Class<?>> inspector = Inspector.of(clazz); 
			
			PrimaryKeyField id = inspector.getPrimaryKey();
			
			List<ColumnField> columns = inspector.getColumns();
		
			try( Connection conn = connPool.getConnection()  ;
				Statement stmt = conn.createStatement();) 	
			{
				String  sql = "CREATE TABLE  IF NOT EXISTS "  + inspector.getTableName() + " (\r\n"
						+ id.getColumnName() + " SERIAL PRIMARY KEY,\r\n";
						
				for(int i=0 ; i< columns.size(); i++) {		
					
					ColumnField column = columns.get(i);
					String columnName = " " +  column.getColumnName() + " ";
					String dataType = " " +  column.getDataType() + " ";
					String unique = (column.isUnique() ) ? " unique " : "";
					String nullable = (! column.isNullable())? " NOT NULL " : "";
					String defaultValue = (column.getDefaultValue().equals("")) ? "" : " default " +  column.getDefaultValue()  + " " ;
					String refrences = (column.getRefrences().equals("")) ? "" : " REFERENCES " + column.getRefrences() + " ";
					String check = (column.getCheck().equals("")) ? "" : " CHECK " + "(" + column.getCheck() + ") ";
					
					
					if(i == columns.size()-1) {
						sql += columnName + dataType  +  unique + nullable + refrences + defaultValue  +  check + "  \n";
					}else {
					sql += columnName + dataType  +  unique + nullable + refrences + defaultValue  + check + " , \n";
					}
					
				}
				
				sql +=  ")";
				
				 log.info(sql);
				
				try(ResultSet rs = stmt.executeQuery(sql) ){
					
				}

				

			} 
			catch (SQLException e) {
				if(e.getMessage().equals("No results were returned by the query.")) {
					log.info( inspector.getTableName() + " table created successfully");
					
					return true ;
				}else {
					log.error(ANSI_RED +  "An SQL exception has been thrown while creating " + inspector.getTableName() + " check the stack trace to debug" + ANSI_RESET);
					throw new DdlException(e.getMessage(),  e.getCause());
				}
				
			}
			
			return false;
			
		}
		
		/**
		 * 
		 * 
		 * @param clazz 
		 * @param change -> statement to be passed Example : ADD username varchar(10);
		 * @return boolean
		 * @throws DdlException
		 * 
		 * This method is used to modify , delete , add a column in an existing table also to add or drop or modify 
		 * a constraint of an existing column 
		 * 
		 * Check if class passed has @Entity annotation by invoking Inspector.of()
		 * 
		 * Append the SQL passed to alter the table 
		 * 
		 * Execute the statement 
		 * 
		 * return true or catch the exception if an error occurs 
		 * 
		 */
		public boolean alter(Class<?> clazz , String change) throws DdlException {
			Inspector<Class<?>> inspector = Inspector.of(clazz); 
			
			String  sql1 = "alter table "  + inspector.getTableName() + " " + change ;
		
			
			 try( Connection conn = connPool.getConnection();
				 Statement		 stmt = conn.createStatement();	 ) 
			 {
				 log.info(sql1);
				
				try(ResultSet rs = stmt.executeQuery(sql1) ){
					
				}

				
			}
			catch (SQLException e) {
				// TODO Auto-generated catch block
				
				if(e.getMessage().equals("No results were returned by the query.")) {
					log.info("Table altered successfully");
					return true ;
				}else {
					log.error(ANSI_RED + "An SQL exception has been thrown while altering " + inspector.getTableName() 
					+ " check the stack trace to debug" + ANSI_RESET);
					throw new DdlException(e.getMessage(),  e.getCause());
				}
				
			}
			
			return false;
		}
		
		
		
		/**
		 * 
		 * @param clazz
		 * @return boolean 
		 * @throws DdlException
		 * 
		 * This method is dangerous use it only if you are sure that you want to delete all data inside of an existing table 
		 * 
		 * This method is used to delete every rows in an existing table 
		 * 
		 * Check if class passed has @Entity annotation by invoking Inspector.of()
		 * 
		 * Execute the the truncate statement 
		 * 
		 * return true or catch the exception if an error occurs 
		 * 
		 * 
		 */
		
		public boolean truncate(Class<?> clazz)  throws DdlException  {
			Inspector<Class<?>> inspector = Inspector.of(clazz); 
			
			
			try( Connection conn = connPool.getConnection() ;
				Statement	stmt = conn.createStatement();	) 
			{	
				String  sql = "truncate table "  + inspector.getTableName() + " RESTART identity " ;
						log.info(sql);
				try(ResultSet rs = stmt.executeQuery(sql) ){
					
				}
			} 
			catch (SQLException e) {
		
				if(e.getMessage().equals("No results were returned by the query.")) {
					log.info("Table truncated successfully");
					return true ;
				}else {
					log.error(ANSI_RED+"An SQL exception has been thrown while truncating " + inspector.getTableName() 
					+ " check the stack trace to debug" + ANSI_RESET);
					throw new DdlException(e.getMessage(),  e.getCause());
				}
				
			}
		
			
			return false;
		}
		
		
		/**
		 * 
		 * @param clazz
		 * @return boolean 
		 * @throws DdlException
		 * 
		 * This method is dangerous use it only if you are sure that you want to delete all data inside of an existing table 
		 * 
		 * If another column depends on the data of this table it will be deleted . 
		 * 
		 * This method is used to delete every rows in an existing table 
		 * 
		 * Check if class passed has @Entity annotation by invoking Inspector.of()
		 * 
		 * Execute the the truncate statement 
		 * 
		 * return true or catch the exception if an error occurs 
		 */
		
		public boolean truncateCascade(Class<?> clazz)  throws DdlException  {
			Inspector<Class<?>> inspector = Inspector.of(clazz); 
			
			
			try( Connection conn = connPool.getConnection() ;
				Statement	stmt = conn.createStatement();	) 
			{	
				String  sql = "truncate table "  + inspector.getTableName() + " RESTART identity CASCADE ";
						log.info(sql);
				try(ResultSet rs = stmt.executeQuery(sql) ){
					
				}
			} 
			catch (SQLException e) {
		
				if(e.getMessage().equals("No results were returned by the query.")) {
				log.info("Table truncated successfully");
				
					return true ;
				}else {
					log.error(ANSI_RED + "An SQL exception has been thrown while truncating " + inspector.getTableName() 
					+ " check the stack trace to debug" + ANSI_RESET);
					throw new DdlException(e.getMessage(),  e.getCause());
				}
				
			}
		
			
			return false;
		}
		
		
		/**
		 * 
		 * @param clazz
		 * @return boolean
		 * @throws DdlException
		 * 
		 * This method is dangerous use it only if you are sure that you want to delete an existing table 
		 * 
		 * This method is used to delete an existing table 
		 * 
		 * Check if class passed has @Entity annotation by invoking Inspector.of()
		 * 
		 * Execute the the drop statement 
		 * 
		 * return true or catch the exception if an error occurs 
		 * 
		 */
		
		public boolean drop(Class<?> clazz) throws DdlException {
			Inspector<Class<?>> inspector = Inspector.of(clazz); 
			
			String  sql = "drop table "  + inspector.getTableName() ;
			
			try( Connection conn = connPool.getConnection()  ;
					Statement	stmt = conn.createStatement();
					
					) {
				log.info(sql);
				
				try(ResultSet rs = stmt.executeQuery(sql) ){
					
				}
		
			} 
			catch (SQLException e) {
				// TODO Auto-generated catch block	
				if(e.getMessage().equals("No results were returned by the query.")) {
					log.info("Table dropped successfully");
					return true ;
				}else {
					log.error(ANSI_RED +   "An SQL exception has been thrown while droping " + inspector.getTableName() 
					+ " check the stack trace to debug" + ANSI_RESET);
					throw new DdlException(e.getMessage(),  e.getCause());
				}
				
			}
			return false;
		}
		
	
		/**
		 * 
		 * @param clazz
		 * @return boolean
		 * @throws DdlException
		 * 
		 * This method is dangerous use it only if you are sure that you want to delete an existing table 
		 * 
		 * If another column depends on the data of this table it will be deleted . 
		 * 
		 * This method is used to delete an existing table 
		 * 
		 * Check if class passed has @Entity annotation by invoking Inspector.of()
		 * 
		 * Execute the the drop statement 
		 * 
		 * return true or catch the exception if an error occurs 
		 * 
		 */
		
		public boolean dropCascade(Class<?> clazz) throws DdlException {
			Inspector<Class<?>> inspector = Inspector.of(clazz); 
			
			String  sql = "drop table  "  + inspector.getTableName() + " CASCADE " ;
			
			try( Connection conn = connPool.getConnection()  ;
					Statement	stmt = conn.createStatement();
					
					) {
				log.info(sql);
				
				try(ResultSet rs = stmt.executeQuery(sql) ){
					
				}
		
			} 
			catch (SQLException e) {
				// TODO Auto-generated catch block	
				if(e.getMessage().equals("No results were returned by the query.")) {
					log.info("Table dropped successfully");
					return true ;
				}else {
					log.error(ANSI_RED + "An SQL exception has been thrown while droping " +
				inspector.getTableName() + " check the stack trace to debug" + ANSI_RESET);
					throw new DdlException(e.getMessage(),  e.getCause());
				}
				
			}
			return false;
		}
		
		
		/**
		 * 
		 * @param clazz
		 * @return
		 * @throws DdlException
		 */
		
		public boolean dropCascadeIfExist(Class<?> clazz) throws DdlException {
			Inspector<Class<?>> inspector = Inspector.of(clazz); 
			
			String  sql = "drop table if exists "  + inspector.getTableName() + " CASCADE " ;
			
			try( Connection conn = connPool.getConnection()  ;
					Statement	stmt = conn.createStatement();
					
					) {
				log.info(sql);
				
				try(ResultSet rs = stmt.executeQuery(sql) ){
					
				}
		
			} 
			catch (SQLException e) {
				// TODO Auto-generated catch block	
				if(e.getMessage().equals("No results were returned by the query.")) {
					log.info("Table dropped successfully");
					return true ;
				}else {
					log.error(ANSI_RED + "An SQL exception has been thrown while droping " +
				inspector.getTableName() + " check the stack trace to debug" + ANSI_RESET);
					throw new DdlException(e.getMessage(),  e.getCause());
				}
				
			}
			return false;
		}

		
		
}
