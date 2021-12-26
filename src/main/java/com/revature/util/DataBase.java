package com.revature.util;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import com.revature.SQL.DDL;
import com.revature.exception.DdlException;
import com.revature.introspection.Inspector;


public class DataBase {
	
	public static Connection conn = null;
	
	private String dbUrl;
	private String dbUsername;
	private String dbPassword;
	// this is the list of classes that the user wants our ORM to "scan" aka introspect and build 
	// as DB objects
	private List<Inspector<Class<?>>> InspectedList;
	
	// This method doesn't technically follow SRP Single Responsibility Principle
	public DataBase addAnnotatedClass(Class annotatedClass) {

		
		// first check if a linked List has been instantiated...
		// if not, instantiate it!
		if (InspectedList == null) {
			InspectedList = new LinkedList<>();
		}
		
		// we need to call the method that transforms a class into an appropriate
		// data model that our ORM can introspect (a MetaModel)
		InspectedList.add(Inspector.of(annotatedClass));
		
		return this; // returns the configuration object on which the addAnnotatedClass() method is being called
	}
	
	public List<Inspector<Class<?>>> getMetaModels() {
		
		// in the case that the metaModelList of the Configuration object is empty, return an empty list.
		// otherwise, reutrn the metaModelList.
		return (InspectedList == null) ? Collections.emptyList() : InspectedList;
	}
	
	
	
	public boolean addMappedClass(Class<?>... clazzs) throws DdlException {
		
		DDL ddl = new DDL();
		
		for(Class<?> c : clazzs) {
			ddl.create(c);
		}
		
		return true;
		
	}
	
	
	// return a Connection object OR call on a separate class like Connection Util
	public DataBase getConnection() {
		
		try {
			if(conn != null && !conn.isClosed()) {
				
			}
			}catch (SQLException e) {
				
				e.printStackTrace();
				return null;
				// TODO: handle exception
			}
			
			// this class is instantiated to read from a properties file 
			Properties prop = new Properties(); // imported from java.util
			
			String url = "";
			String username = "";
			String password = "";
			
			
			try {
				String path = new File("src\\\\main\\\\resources\\\\application.properties").getAbsolutePath();
				prop.load(new FileReader(path));
				
				url = prop.getProperty("url"); // this is retrieving the value of the "url" key in application.properties file
				username =  prop.getProperty("username");
				password = prop.getProperty("password");
				
				
				 conn = DriverManager.getConnection(url, username, password );
				 System.out.println("Connection established successfully");
				 
				 
		
			} catch (SQLException e) {
		
				System.out.println("Cannot establish DB connection");	
				e.printStackTrace();
			}catch (IOException e) {
				e.printStackTrace();
			}
			return this;
		
	}

}
