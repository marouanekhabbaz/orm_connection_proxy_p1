package com.revature.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.revature.SQL.DDL;
import com.revature.SQL.DQL;
import com.revature.exception.DdlException;
import com.revature.introspection.Inspector;

/**
 * 
 * @author marouanekhabbaz
 *
 */


public class DataBase {
	
	private static final Logger log = LoggerFactory.getLogger(DataBase.class);
	
	public static BasicDataSource connPool = null;
	
	public static HashMap<String, Object> cache = new HashMap<>();
	

	/**
	 * 
	 * @param clazz 
	 * @return
	 * @throws DdlException
	 * 
	 */
	
	public boolean addMappedClass(Class<?>... clazzs) throws DdlException {
		
		DDL ddl = new DDL();
		
		for(Class<?> c : clazzs) {
			ddl.create(c);
		}
		
		log.info("  Classes have been mapped into the database successfully ");
		
		return true;
		
	}
	

	
	 public static BasicDataSource getDataSource()
	    {
	 
	        if (connPool == null)
	        {
	        	Properties prop = new Properties(); // imported from java.util
				
				String url = "";
				String username = "";
				String password = "";
				String minIdle = "";
				String maxIdle = "";
				String maxOpenPreparedStatements = "";
	        	
	      	String path = new File("src\\\\main\\\\resources\\\\application.properties").getAbsolutePath();
				// File path = new File("C:\\Users\\marouanekhabbaz\\Desktop\\demos\\DemoForOCP\\src\\main\\resources\\application.properties");
				try {
					prop.load(new FileReader(path));
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				url = prop.getProperty("DEV_url"); // this is retrieving the value of the "url" key in application.properties file
				username =  prop.getProperty("DEV_username");
				password = prop.getProperty("DEV_password");
				minIdle = prop.getProperty("minIdle");
				maxIdle = prop.getProperty("maxIdle");
				maxOpenPreparedStatements = prop.getProperty("maxOpenPreparedStatements");
				

	
	        	
	            BasicDataSource dataSource = new BasicDataSource();
	            dataSource.setUrl(url);
	            dataSource.setUsername(username);
	            dataSource.setPassword(password);
	 
	            dataSource.setMinIdle(Integer.parseInt(minIdle));
	            dataSource.setMaxIdle(Integer.parseInt(maxIdle));
	            dataSource.setMaxOpenPreparedStatements(Integer.parseInt(maxOpenPreparedStatements));
	 
	            connPool = dataSource;
	            log.info("Currently connected into development environment");
	            log.info("Pool of Connection established successfully");
	        }
	        return connPool;
	    }
	
	 
	 
	 
	 public static BasicDataSource getDataSource(Environment env)
	    {
	 
	        if (connPool == null)
	        {
	        	Properties prop = new Properties(); // imported from java.util
				
				String url = "";
				String username = "";
				String password = "";
				String minIdle = "";
				String maxIdle = "";
				String maxOpenPreparedStatements = "";
	        	
	      	String path = new File("src\\\\main\\\\resources\\\\application.properties").getAbsolutePath();
				// File path = new File("C:\\Users\\marouanekhabbaz\\Desktop\\demos\\DemoForOCP\\src\\main\\resources\\application.properties");
				try {
					prop.load(new FileReader(path));
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				

				
			
			
				url = prop.getProperty(env+"_url"); // this is retrieving the value of the "url" key in application.properties file
				username =  prop.getProperty(env+"_username");
				password = prop.getProperty(env+"_password");	
				minIdle = prop.getProperty("minIdle");
				maxIdle = prop.getProperty("maxIdle");
				maxOpenPreparedStatements = prop.getProperty("maxOpenPreparedStatements");
				
	        	
	            BasicDataSource dataSource = new BasicDataSource();
	            dataSource.setUrl(url);
	            dataSource.setUsername(username);
	            dataSource.setPassword(password);
	 
	            dataSource.setMinIdle(Integer.parseInt(minIdle));
	            dataSource.setMaxIdle(Integer.parseInt(maxIdle));
	            dataSource.setMaxOpenPreparedStatements(Integer.parseInt(maxOpenPreparedStatements));
	 
	            connPool = dataSource;
	            
	            log.info("Currently connected into "  + env + " environment");
	            log.info("Pool of Connection established successfully");
	        }
	        return connPool;
	    }
	 
	 
	 
	 
	 
	 
	 
	 
	
	 public DataBase getConnection() {
					 
					 DataBase.getDataSource();
					 
				return this;
			
		}
	 
	 
	 
	 public DataBase getConnection(Environment env) {
		 
		 DataBase.getDataSource(env);
		 
	return this;

}
	 
	 
	 
	 
	 
	 

	 /**
	  * 
	  * @param <T>
	 * @param clazz
	  * 
	  * add the table from database to the cache.
	  * key -> table name
	  * value -> linkedList of all row of the that table
	  */

	public <T> void addTableToCach (Class<T> clazz) {
		Inspector<?> inspector = Inspector.of(clazz);
		
		DQL dql = new DQL();
		
		LinkedList<T> result;
		try {
			result =  dql.getAll(clazz);
			cache.put(inspector.getTableName(), result );
			
			log.info(inspector.getTableName() + " has been add to the cache ");
			
			
		} catch (SQLException e) {
			log.error("An SQL exception has been thrown check the stack trace to debug");
			e.printStackTrace();
		}
	}
	 
	/**
	 * 
	 * @param cachName -> name of the cache.
	 * @param obj -> could be anything example when you retrieve something from the database
	 */
	public void addNewCach(String cachName , Object obj) {
		cache.put(cachName, obj);
		log.info(cachName + " has been added to cache");
	}
	 
	 
}
