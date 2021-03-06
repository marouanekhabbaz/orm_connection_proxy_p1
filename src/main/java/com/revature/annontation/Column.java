package com.revature.annontation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {
	String columnName();
	boolean unique() default false;
	boolean nullable() default true ;
	String dataType();
	String defaultValue() default "" ;
	String  check() default "";
	String refrences() default ""; //	INTEGER NOT NULL REFERENCES users(id),
}
