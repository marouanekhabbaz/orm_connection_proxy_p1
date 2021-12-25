package com.revatrure.demo;

import java.util.Objects;

import com.revature.annontation.Column;
import com.revature.annontation.Entity;
import com.revature.annontation.Id;


@Entity(tableName="cars")
public class Car {

	@Id(columnName="car_id") // this has been marked as a Primary Key
	private int id;
	
	@Column(columnName="car_model", nullable = false, unique = false, dataType = "VARCHAR(50)", defaultValue = "")
	private String model;
	
	@Column(columnName="color", nullable = false, unique = false, dataType = "VARCHAR(50)",defaultValue = "")
	private String color;
	
	
	public Car() {
	
	}

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
