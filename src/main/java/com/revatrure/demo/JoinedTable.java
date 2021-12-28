package com.revatrure.demo;

import com.revature.annontation.Column;
import com.revature.annontation.Entity;
import com.revature.annontation.ForeignKey;
import com.revature.annontation.JoinedColumn;


@Entity(tableName = "join_table")
public class JoinedTable {
	
	
	@ForeignKey(columnName = "car_id", joinedColumn = "car_id", joinedTable = "cars")
	@Column(columnName = "car_id", dataType = "INTEGER" , refrences = "cars(car_id)")
	int carId;
	
	
	
	@ForeignKey(columnName = "person_id", joinedColumn = "person_id", joinedTable = "persons")
	@Column(columnName = "person_id", dataType = "INTEGER",  refrences = "persons(person_id)")
	int personId;
	
	
}
