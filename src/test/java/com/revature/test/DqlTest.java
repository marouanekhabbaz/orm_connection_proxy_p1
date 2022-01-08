package com.revature.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.HashMap;
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
import com.revature.util.DataBase;
import com.revature.util.Environment;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DqlTest {
	static public 	DataBase db = new DataBase().getConnection(Environment.TEST);
	static public DQL dql = new DQL();
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
			try {
				dml.insert(cars);
				dml.insert(client5, client4, client3 , client2 , client1);
			} catch (IllegalArgumentException | IllegalAccessException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		
		} catch (DdlException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	@Test
	
	// return correct object
	public void test1() throws SQLException {
		
	Car car =	dql.get(Car.class, 1);
	
	assertEquals(1, car.getId());
		
		
	}
	
	// return all rows created in table
	@Test
	public void test2() throws SQLException {
		
	List<Car> carsReturned =	dql.getAll(Car.class);

	assertEquals(7, carsReturned.size());
	
	}
	
	@Test
	public void test3() throws SQLException {
		
	List<Car> carsReturned =	dql.getWhere(Car.class, " color = 'white' ");

	assertEquals(3, carsReturned.size());
	
	}
	

	@Test
	// join statement 
	public void test4() throws SQLException {
		Car newcar = new Car("honda", "white", 24);
		newcar.setOwner(1);
		try {
			dml.insert(newcar);
		} catch (IllegalArgumentException | IllegalAccessException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	List<HashMap<String, Object>> result =	dql.joinQuerry(Car.class, Client.class);
 
	assertEquals(1, result.size());
	
	
	}
	
	
	
	@Test
	// find result
	public void test5() throws SQLException {
		Car newcar = new Car("honda", "white", 24);
		newcar.setOwner(1);
		try {
			dml.insert(newcar);
		} catch (IllegalArgumentException | IllegalAccessException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	List<HashMap<String, Object>> result =	dql.joinQuerry(Car.class, Client.class , " a.color = 'white' ");
 
	
	System.out.println(result.size());
	assertEquals(2, result.size());
	
	
	}
	
	
	@Test
	// find no match
	public void test6() throws SQLException {
		
		
	List<HashMap<String, Object>> result =	dql.joinQuerry(Car.class, Client.class , " a.color = 'blue' ");
 
	assertEquals(0, result.size());
	
	
	}
	
	@Test
	// find no match in multiple join statement
	public void test7() throws SQLException {
	
		
	List<HashMap<String, Object>> result =	dql.joinQuerryManyToMany( Service.class , Car.class, Client.class , " a.color = 'blue' " );
 
	assertEquals(0, result.size());
	
	
	}
	
	
	

	
	
	
	@Test
	// find match in multiple join statement
	public void test8() throws SQLException {
	
	Service ser = new Service(1, 2);
	
	try {
		dml.insert(ser);
	} catch (IllegalArgumentException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IllegalAccessException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		
	List<HashMap<String, Object>> result =	dql.joinQuerryManyToMany( Service.class , Car.class, Client.class );
 
	assertEquals(1, result.size());
	
	
	}
	
	
	@Test
	// find match in multiple join statement with a condition
	public void test9() throws SQLException {
	
	Service ser = new Service(1, 2);
	
	try {
		dml.insert(ser);
	} catch (IllegalArgumentException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IllegalAccessException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		
	List<HashMap<String, Object>> result =	dql.joinQuerryManyToMany( Service.class , Car.class, Client.class ," a.color = 'white' " );
 
	assertEquals(2, result.size());
	
	
	}
	
	
	
	
}
