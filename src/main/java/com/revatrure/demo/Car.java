package com.revatrure.demo;

import java.beans.ConstructorProperties;

import java.util.Objects;

import com.revature.annontation.Column;
import com.revature.annontation.Entity;
import com.revature.annontation.ForeignKey;
import com.revature.annontation.Id;


@Entity(tableName="cars")
public class Car {

	@Id(columnName="car_id")
	private int id;
	
	@Column(columnName="car_model", dataType ="varchar(50)")
	private String model;
	
	@Column(columnName="color", dataType = "varchar(50)" , defaultValue = " 'blue' ")
	private String color;
	
	@Column(columnName = "price", dataType = "INTEGER",  nullable = false)
	private int price ;
	
	@ForeignKey(columnName = "owner", joinedColumn = "client_id" , joinedTable = "clients")
	@Column(columnName="owner", dataType = "INTEGER" , refrences = "clients(client_id)" , defaultValue = "null")
	private Integer owner;
	
	
	public String notMapped1;
	
	
	public String notMapped2 ;
	
	
	
	
	public Car() {
	
	}
		
	
	
	@ConstructorProperties(value = { "car_id",  "car_model" , "color" , "price", "owner" })
	public Car(int id, String model, String color, int price, Integer owner) {
		this.id = id;
		this.model = model;
		this.color = color;
		this.price = price;
		this.owner = owner;
	}



	public Car(String firstName, String color ,  int price) {
		super();
		this.price = price;
		this.model = firstName;
		this.color = color;
	}
	
	
	public Car(String firstName, String color) {
		super();
		this.model = firstName;
		this.color = color;
	}



	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}



	public String getModel() {
		return model;
	}



	public void setModel(String model) {
		this.model = model;
	}



	public String getColor() {
		return color;
	}



	public void setColor(String color) {
		this.color = color;
	}



	public int getPrice() {
		return price;
	}



	public void setPrice(int price) {
		this.price = price;
	}



	public int getOwner() {
		return owner;
	}



	public void setOwner(int owner) {
		this.owner = owner;
	}



	public String getNotMapped1() {
		return notMapped1;
	}



	public void setNotMapped1(String notMapped1) {
		this.notMapped1 = notMapped1;
	}



	@Override
	public String toString() {
		return "Car [id=" + id + ", model=" + model + ", color=" + color + ", price=" + price + ", owner=" + owner
				+ "]";
	}

	
	
	


	
	
}
