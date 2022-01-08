package com.revature.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.booleanThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.SQLException;
import java.util.Random;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.revatrure.demo.Car;
import com.revatrure.demo.Client;

import com.revatrure.demo.Service;
import com.revature.SQL.DDL;
import com.revature.exception.DdlException;
import com.revature.util.DataBase;
import com.revature.util.Environment;
//(MethodSorters.NAME_ASCENDING)


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DdlTest {
	
	
	static public 	DataBase db = new DataBase().getConnection(Environment.TEST);
	static public DDL ddl = new DDL();
	

	
	@BeforeClass
	public static void setup() {
		//, Client.class , Service.class
		try {
			db.addMappedClass( Client.class  , Car.class, Service.class);
			
		
		} catch (DdlException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
	@Test
	public void test1() throws DdlException {
			
		boolean actual =	ddl.drop(Client.class);
		
		assertTrue(actual);

	}
	
	//DropCascadeClassHasReffrenceToOtherClass() 
	@Test(expected = DdlException.class )
	public void test2() throws DdlException {
		ddl.drop(Client.class);
	}
	//addOneClass() 
	@Test
	public void test3() throws DdlException {
	boolean actual =	ddl.create(Client.class);
	assertTrue(actual);
	System.out.println("2");
	}
	
	//addMultipleClass() 
	@Test
	public void test4() throws DdlException {
	boolean actual =	db.addMappedClass(Car.class, Service.class);
	System.out.println("3");
	assertTrue(actual);
	}
	
	// alter an existing class 
	@Test
	public void test5() throws DdlException {
	boolean actual =	ddl.alter( Car.class, "ADD email VARCHAR(255) ");
	
	assertTrue(actual);
	}
	
	// Drooping the added column
	@Test
	public void test6() throws DdlException {
	boolean actual =	ddl.alter( Car.class, "DROP COLUMN  email ");
	System.out.println(actual);
	assertTrue(actual);
	}
	
	// Drooping a non existing column 
	@Test(expected = DdlException.class )
	public void test7() throws DdlException {
	boolean actual =	ddl.alter( Car.class, " DROP COLUMN  email ");
	
	}
	
	
	
	// truncate CASCADE a table that is refrenced in other classes
	@Test
	public void test9() throws DdlException {
		

	boolean actual =	ddl.truncateCascade(Client.class);
	

	assertTrue(actual);
	
	
	}
	
	// DROP CASCADE a table that is refrenced in other classes
//	@Test
//	public void test10() throws DdlException {
//		
//		System.out.println("======================");
//	boolean actual =ddl.dropCascade(Client.class);
//	System.out.println(actual);
//	assertTrue(actual);
//	System.out.println("======================");
//	
//	}
//	
}
