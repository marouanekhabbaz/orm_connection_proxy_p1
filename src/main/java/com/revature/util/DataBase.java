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

import com.revatrure.demo.Car;
import com.revatrure.demo.Person;
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
		
		return true;
		
	}
	
	
	
	
	
	
	
	 private static BasicDataSource getDataSource()
	    {
	 
	        if (connPool == null)
	        {
	        	Properties prop = new Properties(); // imported from java.util
				
				String url = "";
				String username = "";
				String password = "";
	        	
	        	String path = new File("src\\\\main\\\\resources\\\\application.properties").getAbsolutePath();
				try {
					prop.load(new FileReader(path));
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				url = prop.getProperty("url"); // this is retrieving the value of the "url" key in application.properties file
				username =  prop.getProperty("username");
				password = prop.getProperty("password");
				
				

				 System.out.println("Pool of Connection established successfully");
	        	
	            BasicDataSource dataSource = new BasicDataSource();
	            dataSource.setUrl(url);
	            dataSource.setUsername(username);
	            dataSource.setPassword(password);
	 
	            dataSource.setMinIdle(5);
	            dataSource.setMaxIdle(10);
	            dataSource.setMaxOpenPreparedStatements(100);
	 
	            connPool = dataSource;
	        }
	        return connPool;
	    }
	
	
	 public DataBase getConnection() {
					 
					 DataBase.getDataSource();
					 
				return this;
			
		}

	 /**
	  * 
	  * @param clazz
	  * 
	  * add the table from database to the cache.
	  */

	public void addTableToCach (Class<?> clazz) {
		Inspector<?> inspector = Inspector.of(clazz);
		
		DQL dql = new DQL();
		
		LinkedList<Object> result;
		try {
			result = dql.getAll(clazz);
			cache.put(inspector.getTableName(), result );
		} catch (SQLException e) {
		
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
	}
	 
	 
}
