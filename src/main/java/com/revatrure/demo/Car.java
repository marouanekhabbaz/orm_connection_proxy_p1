package com.revatrure.demo;

import java.beans.ConstructorProperties;

import java.util.Objects;

import com.revature.annontation.Column;
import com.revature.annontation.Entity;
import com.revature.annontation.ForeignKey;
import com.revature.annontation.Id;


@Entity(tableName="cars")
public class Car {

	@Id(columnName="car_id") // this has been marked as a Primary Key
	private int id;
	
	@Column(columnName="car_model", dataType ="varchar(50)", defaultValue = " 'blue' ")
	private String model;
	
	@Column(columnName="color", dataType = "varchar(50)", nullable = false)
	private String color;
	
	@Column(columnName = "doors", dataType = "INTEGER", defaultValue= "1")
	private int doors ;
	
	@ForeignKey(columnName = "forignKey", joinedColumn = "person_id" , joinedTable = "persons")
	@Column(columnName="forignKey", dataType = "INTEGER" , refrences = "persons(person_id)")
	private String forignKey;
	
	public String marouane;
	
	
	public String anas ;
	
	
	
	
	public Car() {
	
	}
	
	
	
	



	@ConstructorProperties(value = { "car_id",  "car_model" , "color" })
	public Car(int id, String firstName, String color) {
		super();
		this.id = id;
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

	@Override
	public int hashCode() {
		return Objects.hash(color, id, model);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Car other = (Car) obj;
		return Objects.equals(color, other.color) && id == other.id && Objects.equals(model, other.model);
	}

	@Override
	public String toString() {
		return "Car [id=" + id + ", model=" + model + ", color=" + color + "]";
	}
	
	
	


	
	
}
