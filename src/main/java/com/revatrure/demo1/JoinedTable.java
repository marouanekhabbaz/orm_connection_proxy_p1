package com.revatrure.demo1;

import com.revature.annontation.Column;
import com.revature.annontation.Entity;
import com.revature.annontation.ForeignKey;
import com.revature.annontation.Id;
import com.revature.annontation.JoinedColumn;


@Entity(tableName = "join_table")
public class JoinedTable {
	
	
	
	public JoinedTable(int carId, int personId) {
		super();
		this.carId = carId;
		this.personId = personId;
	}



	@Id(columnName = "id")
	int id;
	
	@ForeignKey(columnName = "car", joinedColumn = "car_id", joinedTable = "cars")
	@Column(columnName = "car", dataType = "INTEGER" , refrences = "cars(car_id)")
	int carId;
	
	
	
	@ForeignKey(columnName = "person", joinedColumn = "person_id", joinedTable = "persons")
	@Column(columnName = "person", dataType = "INTEGER",  refrences = "persons(person_id)")
	int personId;
	
	
}
