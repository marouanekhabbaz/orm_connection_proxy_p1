package com.revature.introspection;

import java.lang.reflect.Field;

import com.revature.annontation.Column;
import com.revature.annontation.ForeignKey;
import com.revature.annontation.Id;
/**
 * 
 * @author marouanekhabbaz
 *
 */
public class ForeignKeyField {
	

	Field field;
	public ForeignKeyField(Field field) {
		if(field.getAnnotation(ForeignKey.class) == null) {
			throw new IllegalStateException("Cannot create Foreignkey this field is not annotated with @ForeignKey");
		}
		
		this.field = field;
	
	}
	
	/**
	 * 
	 * @return name of joined class "table"
	 */
	public String getJoinedClass() {
	
		 return field.getAnnotation(ForeignKey.class).joinedTable();
	}
	
	
	/**
	 * 
	 * 
	 * @return name of column joined in the other class "table"
	 */
	public String getJoinedColumn() {
		//Field field
		 return field.getAnnotation(ForeignKey.class).joinedColumn();
	}
	
	
	/**
	 * 
	 * @return name of the field annotated with foreign key 
	 * 
	 */
	public String getForeignKeyName() {
		return field.getAnnotation(ForeignKey.class).columnName();
	} 
	
	

}
