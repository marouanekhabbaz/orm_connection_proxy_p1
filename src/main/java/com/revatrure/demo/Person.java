package com.revatrure.demo;

import com.revature.annontation.Column;
import com.revature.annontation.Entity;
import com.revature.annontation.Id;
@Entity(tableName="persons")
public class Person {
	@Id(columnName="person_id") // this has been marked as a Primary Key
	private int id;
	
	@Column(columnName="person", dataType ="varchar(50)", defaultValue = " 'blue' ")
	private String model;
	
	@Column(columnName="ager", dataType = "varchar(50)", nullable = false)
	private String color;
	
	@Column(columnName = "doors", dataType = "INTEGER", defaultValue= "1")
	private int doors ;
	
}
