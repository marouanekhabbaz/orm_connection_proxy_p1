package com.revatrure.demo;

import java.beans.ConstructorProperties;

import com.revature.annontation.Column;
import com.revature.annontation.Entity;
import com.revature.annontation.Id;
@Entity(tableName="persons")
public class Person {
	
	@Id(columnName="person_id") // this has been marked as a Primary Key
	private int id;
	
	@Column(columnName="person", dataType ="varchar(50)", defaultValue = " 'blue' ")
	private String model;
	
	@Column(columnName="age", dataType = "varchar(50)", nullable = false)
	private String color;
	
	@Column(columnName = "doors", dataType = "INTEGER", defaultValue= "1")
	private int doors ;
	
	@Column(columnName = "valid", dataType = "boolean")
	private boolean isValid ;

	
	
	@ConstructorProperties(value = { "person_id",  "person" , "age" , "doors",  "valid" })
	public Person(int id, String model, String color, int doors, boolean isValid) {
		super();
		this.id = id;
		this.model = model;
		this.color = color;
		this.doors = doors;
		this.isValid = isValid;
	}

	@Override
	public String toString() {
		return "Person [id=" + id + ", model=" + model + ", color=" + color + ", doors=" + doors + ", isValid="
				+ isValid + "]";
	}
	
	
	
	
	
	
}
