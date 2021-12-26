package com.revature.SQL;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;

import com.revature.introspection.ColumnField;
import com.revature.introspection.Inspector;
import com.revature.util.DataBase;

public class DML {
	Connection conn = DataBase.conn;
	
	
	
	private String columnsStatement(List< ColumnField> columns ) {
		
		String sql = " ( ";
		
		for(int i=0 ; i < columns.size() ; i++) {
			if(i == columns.size() -1 ) {
				sql += columns.get(i).getColumnName() + " ) ";
			}else {
				sql += columns.get(i).getColumnName() + " , ";
			}
			
		}
		
		return sql;
		
	}
	
	
	
	private String values(List< ColumnField> columns , HashMap<String, Object> columnsAndValues) {
		
		String sql = " ( ";
	//	System.out.println(columnsAndValues);
		
		for(int i=0 ; i < columns.size() ; i++) {	
			String colmun = columns.get(i).getColumnName();	
			
			Object value = ( columnsAndValues.get(colmun)  instanceof String) ? "'" + columnsAndValues.get(colmun)  + "'" :columnsAndValues.get(colmun) ;
			
			
			if(i == columns.size()-1 ) {
				sql +=	value    + " ) ";
			}else {
				sql += value +" , ";
			}	
			
		}
		
		return sql;
		
	}
	
	
	
	public int insert(Object... objs) throws IllegalArgumentException, IllegalAccessException, SQLException {
		
		Class<?> clazz = objs[0].getClass();
		Inspector<Class<?>> inspector1 = Inspector.of(clazz);
		List< ColumnField> columns =	inspector1.getColumns();
		String columnsStatement =  columnsStatement(columns);
		
		String sql = "INSERT INTO " + inspector1.getTableName() + columnsStatement + " \n"
				+ "VALUES "
				;
		for( int i=0 ; i < objs.length; i++ )  {
			Object obj = objs[i];
			
	     	System.out.println(obj.getClass());
	     	
	     	
		Inspector<Class<?>> inspector = Inspector.of(obj.getClass());
		HashMap<String, Object> columnsAndValues =	inspector.getColumnsAndValues(obj);
		
		if(i == objs.length -1) {
			sql += values(columns, columnsAndValues) + " ; ";
		}else {
			sql += values(columns, columnsAndValues) + " , \n";
		}

	
		}
		
		System.out.println(sql);
		
		Statement stmt = conn.createStatement();
		
		ResultSet rs = stmt.executeQuery(sql);
		
		
		System.out.println(sql);
		return 0;	
	}
	
}
