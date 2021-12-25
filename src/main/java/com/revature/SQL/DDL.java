package com.revature.SQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import com.revatrure.demo.Car;
import com.revature.introspection.ColumnField;
import com.revature.introspection.Inspector;
import com.revature.introspection.PrimaryKeyField;
import com.revature.util.DataBase;

public class DDL {
	
	Connection conn = DataBase.conn;
	
	
		public boolean create(Class<?> clazz) {
			
			
			Inspector<Class<?>> inspector = Inspector.of(clazz); 
			
			PrimaryKeyField id = inspector.getPrimaryKey();
			
			List<ColumnField> columns = inspector.getColumns();
			

		
			try {
				
				
				Statement stmt = conn.createStatement();
				
				String  sql = "CREATE TABLE "  + inspector.getTableName() + " (\r\n"
						+ id.getColumnName() + " SERIAL PRIMARY KEY,\r\n";
					
					
		
		
				
				for(int i=0 ; i< columns.size(); i++) {
					
					if(i == columns.size()-1) {
						sql += columns.get(i).getColumnName() + " VARCHAR(50) \n";
					}else {
					sql += columns.get(i).getColumnName() + " VARCHAR(50) , \n";
					}
					
				}
				
				sql +=  ")";
				
				System.out.println(sql);

				stmt.executeQuery(sql);

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
			
			return false;
			
		}
		
		
		public boolean alter(Class<?> clazz , String sql) {
			Inspector<Class<?>> inspector = Inspector.of(clazz); 
			
			Statement stmt;
			try {
				stmt = conn.createStatement();
				
				String  sql1 = "alter table "  + inspector.getTableName() + " " + sql ;
						
		
				stmt.executeQuery(sql1);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			
			return false;
		}
		
		
		
		
		
		public boolean truncate(Class<?> clazz) {
			Inspector<Class<?>> inspector = Inspector.of(clazz); 
			
			Statement stmt;
			try {
				stmt = conn.createStatement();
				
				String  sql = "truncate table "  + inspector.getTableName() ;
						
		
				stmt.executeQuery(sql);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			
			return false;
		}
		
		
		public boolean drop(Class<?> clazz) {
			Inspector<Class<?>> inspector = Inspector.of(clazz); 
			
			//truncate cars 
			Statement stmt;
			try {
				stmt = conn.createStatement();
				
				String  sql = "drop table if exists "  + inspector.getTableName() ;
						
		
				stmt.executeQuery(sql);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			
			return false;
			
		}
		
	
		
		
}
