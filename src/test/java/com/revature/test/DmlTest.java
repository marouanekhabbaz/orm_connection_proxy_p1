package com.revature.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.revatrure.demo.Car;
import com.revatrure.demo.Client;
import com.revatrure.demo.Service;
import com.revature.SQL.DDL;
import com.revature.SQL.DML;
import com.revature.SQL.DQL;
import com.revature.exception.DdlException;
import com.revature.exception.IdDontExistException;
import com.revature.exception.VarArgsHasDiffrentException;
import com.revature.util.DataBase;
import com.revature.util.Environment;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DmlTest {
	static public 	DataBase db = new DataBase().getConnection(Environment.TEST);
	
	static public DDL ddl = new DDL();
	
	static public DML dml = new DML();

	
	static Client client1 = new Client("marwane", "khab", 27, true);
	static Client client2 = new Client("adam", "jhon", 18, false);
	static Client client3 = new Client("jhon", "adam", 30, true);
	static Client client4 = new Client("messi", "leo", 37,true);
	static Client client5 = new Client("ref", "nadal", 32);
	
	static Car[] cars = {
		new Car("bmw", "white", 25),
		new Car("audi", "black", 27),
		new Car("ford", "blue", 25),
		new Car("benz", "white", 30),
		new Car("chevy", "grey", 24),
		new Car("toyota", "black", 22),
		new Car("honda", "white", 24)
	} ;
	
	
	@BeforeClass
	public static void setup()  {
		//, Client.class , Service.class
		try {
			db.addMappedClass( Client.class  , Car.class, Service.class);
			ddl.truncateCascade(Car.class);
			ddl.truncateCascade(Client.class);
			ddl.truncateCascade(Service.class);
		
			
		
		} catch (DdlException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	@Test(expected = VarArgsHasDiffrentException.class )
	public void test1() throws IllegalArgumentException, IllegalAccessException, SQLException {
		dml.insert(cars[0], client1);
	}
	
	
	@Test
	public void test2() throws IllegalArgumentException, IllegalAccessException, SQLException {
	List<Object> returned =	 dml.insert(cars);
	assertEquals(7, returned.size());
	}
	

	

	
	
	// update valid
	@Test
	public void test5() throws SQLException {
		Object row = dml.update(Car.class, "color = 'white' ", 2);
		assertTrue(row.toString().contains("white"));
	}
	
	
	// update already deleted
		@Test(expected = IdDontExistException.class)
		public void test6() throws SQLException {
			Object row = dml.update(Car.class, "color = 'white' ", 10000);
			
		}
	
		// update with condition
	
		public void test7() throws SQLException {
			List<Object> rows = dml.update(Car.class, "color = 'white' ", " price = 24  ");
			
			assertEquals(2, rows.size());
			
		}
		
		
		@Test
		public void test8() throws SQLException {
			int id = dml.delete(Car.class , 1);
			assertEquals(1, id);
		}
		
		// not found
		@Test
		public void test9() throws SQLException {
			int id = dml.delete(Car.class , 10000000);
			assertEquals(0, id);
		}
		
		
	
		
		
}
