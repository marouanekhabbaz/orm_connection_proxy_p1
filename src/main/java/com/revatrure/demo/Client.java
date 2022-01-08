package com.revatrure.demo;

import java.beans.ConstructorProperties;

import com.revature.annontation.Column;
import com.revature.annontation.Entity;
import com.revature.annontation.Id;
@Entity(tableName="clients")
public class Client {
	
	@Id(columnName="client_id") // this has been marked as a Primary Key
	private int id;
	
	@Column(columnName="fisrt_name", dataType ="varchar(50)")
	private String firstName;
	
	@Column(columnName = "last_name", dataType = "varchar(50)", defaultValue= "1")
	private String lastName ;
	
	@Column(columnName="age", dataType = "INTEGER", nullable = false)
	private int age;
	
	
	
	@Column(columnName = "approved", dataType = "boolean" , defaultValue = " true ")
	private boolean isApproved ;


	@ConstructorProperties(value = { "client_id",  "fisrt_name" , "last_name" , "age",  "approved" })
	public Client(int id, String firstName, String lastName, int age, boolean isApproved) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.age = age;
		this.isApproved = isApproved;
	}

	
	public Client( String firstName, String lastName, int age, boolean isApproved) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.age = age;
		this.isApproved = isApproved;
	}
	
	

	public Client( String firstName, String lastName, int age ) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.age = age;
	
	}
	
	public int getId() {
		return id;
	}




	public void setId(int id) {
		this.id = id;
	}




	public String getFirstName() {
		return firstName;
	}




	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	public String getLastName() {
		return lastName;
	}


	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	public int getAge() {
		return age;
	}


	public void setAge(int age) {
		this.age = age;
	}


	public boolean isApproved() {
		return isApproved;
	}




	public void setApproved(boolean isApproved) {
		this.isApproved = isApproved;
	}




	@Override
	public String toString() {
		return "Client [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", age=" + age
				+ ", isApproved=" + isApproved + "]";
	}

	
	
	
	
	
	
	
}
