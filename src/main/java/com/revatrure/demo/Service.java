package com.revatrure.demo;

import com.revature.annontation.Column;
import com.revature.annontation.Entity;
import com.revature.annontation.ForeignKey;
import com.revature.annontation.Id;
import com.revature.annontation.JoinedColumn;


@Entity(tableName = "services")
public class Service {
	
	
	
	public Service(int carId, int personId) {
		super();
		this.carId = carId;
		this.clientId = personId;
	}



	@Id(columnName = "id")
	int id;
	
	@ForeignKey(columnName = "car", joinedColumn = "car_id", joinedTable = "cars")
	@Column(columnName = "car", dataType = "INTEGER" , refrences = "cars(car_id)")
	int carId;
	
	
	
	@ForeignKey(columnName = "client", joinedColumn = "client_id", joinedTable = "clients")
	@Column(columnName = "client", dataType = "INTEGER",  refrences = "clients(client_id)")
	int clientId;
	
	
}
