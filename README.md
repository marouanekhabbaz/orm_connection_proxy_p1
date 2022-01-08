# ORM Connection Proxy OCP


## Project Description

A java based object-relational mapping tool for PostgreSQL, this framework handles the connection to the database. and retrieve data from classes or objects then generate SQL to persist the data into the database.
It also provides classes and methods that simplify the definition, manipulation, transaction, and querying actions from and to the database with little to no SQL or connection management.  

## Technologies Used

* PostgreSQL - version 42.2.23
* Java - version 8.0  
* Apache commons - version 2.1  
* JUnit
* Sonarcloud

## Features

* OCP handles the mapping of Java classes to database tables using a simple method without the need for complex configuration files.

* Provides simple APIs for storing and retrieving Java objects directly to and from the database.

* Easy to use and straightforward user API to store, retrieve or manipulate data using a Java class or object and OCP will handle the persistence of that into the database.  

* Little to no SQL, or any database-specific language, But it does provide API to write and execute native SQL statements to the database.

* OCP gives the developer 100% control over defining constraints for each column in the table such as  ( data type, Unique, primary key,    references, check, default, not null.. ) 

* Provide transaction functionality like commit, save point, and rollback. 

* Straightforward and simple Annotation-based for ease of use. 

* Mapping of join columns inside of entities. 

* All methods of DQL class returns each row from the result as a hashmap with the column name as the key, and the value is the value of that column in the table, this very similar to JSON, and make the retrieval and manipulation of the data much easier than working with ResultSet.  

* OCP uses connection pooling.  

* All SQL statements executed against the database are printed to the console for easier debugging.



## Getting Started  
Currently project must be included as local dependency. to do so:
```shell
  git clone https://github.com/marouanekhabbaz/orm_connection_proxy_p1
  cd orm_connection_proxy_p1
  mvn install
```
Next, place the following inside your project pom.xml file:
```XML
  <dependency>
  	<groupId>com.revature</groupId>
	<artifactId>OrmConnectionProxyP1</artifactId>
	<version>0.0.1-SNAPSHOT</version>
  </dependency>

```

Finally, inside your project structure create **application.proprties** file in **src/main/resources/**. 

In your application.proprties make sure to use the same keys below **url** , **username** , **password** .



 ```
 

DEV_url= your database url for dev
DEV_username=your  database username
DEV_password=your database password
 


TEST_url = your database url for tests
TEST_username=your  database username
TEST_password =your database password


PROD_url= your database url for production
PROD_username=your  database username
PROD_password=your database password

STAGE_url=your database url for staging
STAGE_username=your  database username
STAGE_password=your database password

#minimum number of connection object that are to be kept alive in the pool.
minIdle=5
#maximum number of  connections objects that can be alive in the pool.
maxIdle=15
#maximum number of PreparedStatements in the session
maxOpenPreparedStatements=50

 ``` 

## Usage  
  ### Annotating classes  
  All classes which represent objects in database must be annotated.

```Java

    @Entity(tableName="table_name")

```
   - Indicates that this class is associated with table 'table_name'  
   - Each mapped class must should have @Entity annotation .
```Java
   import java.beans.ConstructorProperties ; 

    @ConstructorProperties(value = { "person_id",  "person" , "age" , "doors",  "valid" }) 

```

   - Developer can have multiple constructor in a  class. but must have one constructor annotated with  @ConstructorProperties

   - Pass the column_name associted with each parameter into value attribute.

   - This contructor will be use to create an instance of this class at runtime (When retiriving data from the database). 

```Java  

     @Id(columnName="name_id")

```
   -  Indicates that the annotated field is the primary key for the table.

   -   Each mapped class must should have a @Id field representing the primary key of the table.


```Java 

    @Column(columnName="person", dataType ="varchar(50)", defaultValue = " 'blue' " , 	unique = true , check = "condition" ,  refrences = "table_name(column_name)" , nullable = true )
    
```

   - Column annotation has six attributes representing the constraints of the column 
   -  **columnName**  -> name of the column.

   -  **dataType**     -> defines what value this column can hold ex: `varchar(50)`, `INTEGER` , `NUMERIC`..

   -  **defaultValue**-> optional , set the default value of a column if no value provided (if the value is string add 'value'). 

   -  **unique** -> optional by default is set to `false` , don't allow duplicate value.

   -  **nullabe** ->  optional by default is set to `true`. if you set it to false, it won't allow null value.

   -  **check** ->optional ,  allow values that meet the condition into the column ex:   `CHECK (salary>=15)`

   -  **refrences** -> to create a foreign key that refrence to a column in another table ex: `refrences = "table_name(column_name)"` .


```Java 

    @ForeignKey(columnName = "person", joinedColumn = "person_id", joinedTable = "persons")

```
   - Add this annotation the column that refrences column in another table.

   - This annotation has 3 attributes.

   -  **columnName** -> the name of this column should be exactly the same as the one used in `@Column(columnName="same name")`.

   -  **joinedColumn**->  the name of the column that foreign key depends on,  must be the same used in `@Column(refrences = "table_name(**same_column_name**)")`.

   -  **joinedTable** ->  the name of the table that foreign key refrencing to. must be the same used in `@Column(refrences = "**same_table_name**(column_name)")`. 

   -  Use this annotation to enable the use of Join Queries (simple or multiple join like many to many relationship )



   ###  Connect to database and create tables : 

   1. Create an Entity class : 


   ````java

import java.beans.ConstructorProperties;
import com.revature.annontation.Column;
import com.revature.annontation.Entity;
import com.revature.annontation.ForeignKey;
import com.revature.annontation.Id;


@Entity(tableName="cars")
public class Car {

	@Id(columnName="car_id")
	private int id;
	
	@Column(columnName="car_model", dataType ="varchar(50)")
	private String model;
	
	@Column(columnName="color", dataType = "varchar(50)", nullable = false ,  defaultValue = " 'blue' ")
	private String color;
	
	@Column(columnName = "doors", dataType = "INTEGER", defaultValue= "4")
	private int doors ;
	
	@ForeignKey(columnName = "forignKey", joinedColumn = "person_id" , joinedTable = "persons")
	@Column(columnName="forignKey", dataType = "INTEGER" , refrences = "persons(person_id)")
	private String owner;
	
	public String otherField1;
	
	public String  otherField12 ;
	
	public Car() {
	
	}
	

	@ConstructorProperties(value = { "car_id",  "car_model" , "color" })
	public Car(int id, String firstName, String color) {
		super();
		this.id = id;
		this.model = firstName;
		this.color = color;
	}
}

   ````

2. Connect to the database 

````java

   import com.revature.util.DataBase;

   // if args passed it will connect by default to developement envirement
   DataBase db = new DataBase().getConnection();

   /*
   * To work on specific envirement pass it as a param to getConnection(Environment.TEST);
   Environment.TEST ,  Environment.DEV , Environment.PROD , Environment.STAGE
   *public 	DataBase db = new DataBase().getConnection(Environment.TEST);
   */
		

````

3. Create Entity class in database (make sure database dont have a table with the same name sql used is CREATE TABLE  IF NOT EXISTS.. )

````java
      /*
      * Using varagrs you can as many class as you need 
      */
   db.addMappedClass( Person.class , Car.class , ... );

````

  

  ## User API  
  
  - ### DataBase :
      #### `public DataBase getConnection()`   
     - returns the singleton instance of the class. and create a connetion pool to the data base. 

     ####  `public boolean addMappedClass(Class<?>... clazzs) `
     -  Create tables of classes passed as args into the database.

     #### `	public void addTableToCach (Class<?> clazz)`
      - Add the table from database to the cache.

      #### `public void addNewCach(String cachName , Object obj) `
      - Name of the cache.
	   - Could be anything example when you retrieve something from the database

   - ###  DDL
     -    Class used to execute Data definition language statements CREATE , ALTER , TRUNCATE , DROP.

     - #### public boolean create(Class<?> clazz)
     -   Create a table of the class passed into the database.

     - #### `public boolean alter(Class<?> clazz , String change)`

      - This method is used to modify , delete , add a column in an existing table also to add or drop or modify  a constraint of an existing column.

      ````java 
      import com.revature.SQL.DDL;

      	DDL ddl = new DDL();

		
		try {
			ddl.alter(Car.class, " ADD Email varchar(50) ");
		} catch (DdlException e) {
		
			e.printStackTrace();
		}


      ```` 

      - #### `public boolean truncate(Class<?> clazz)`

      - This method is used to delete every rows in an existing table 

      - #### 	`public boolean drop(Class<?> clazz)`

      - This method is used to delete an existing table.

      ### DML
      -   DML provide methods to execute data manipulation statement against the database
	   - All operations against the database using this class are auto committed.

      - #### `public 	List<Object> insert(Object... objs) `
      - This method will map the objects passed in insert their data into their table.
      - Objects passed in this method should be annotated with @Entity and the should all be from the same class.

      - #### 	`public int delete(Class<?> clazz ,int id)`
      - This method delete the row with primary key equal to id. 

      -  Returns the id of the row deleted or 0 if the id passed does not exists in the database 

      - #### `List<Object>  delete(Class<?> clazz , String condition)`

      - Delete all rows that meet the condition passed.
      - Return a list of the primary key of rows that been deleted  


      - #### `public Object update(Class<?> clazz, String statement  ,int id )`

      - This method update the row with primary key equal to id. 
      -  Return an object representing the row updated in the database.
 
      - ####  `public List<Object> update(Class<?> clazz , String statement , String  condition )`

      - Update all rows that meet the condition passed.
      - Return a list of objects representing the rows updated in the database.

      ````java

      import com.revature.SQL.DML;

      Car bmw = new Car(2, "bmw", "blue" );
      
      Car renault = new Car(4, "renault", "red");

      DML	dml = new DML();
		
	   try {
			List<Object> listInserted =	dml.insert(bmw, renault);
			
			int deleted = dml.delete(Car.class, 2);
			
			List<Object> deletedRows = dml.delete(Car.class, "WHERE color = 'red ");
			
			Object updated = dml.update(Car.class, "color = 'green' ", 1);
			
			List<Object> updatedRows = dml.update(Car.class,  "color = 'green' " , "WHERE color = 'blue' ");

		} catch (IllegalArgumentException e1) {
			
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
	
			e1.printStackTrace();
		} catch (SQLException e1) {
		
			e1.printStackTrace();
		}

      ````

 ###  Transaction
   *  Transaction provide methods to execute data manipulation statement against the database and gives the developer control to manage the transaction (commit , rollback ...)
 *  All operations against the database using this class are not committed and need to invoke .commit() method to persist change.

   - ####	`public void commit()`
   - Made change persistent  

   - #### `public Savepoint setSavePoint(String name)`
   -  To create a save point to rollback to it later down the road 

   - #### `public void rollBack(Savepoint savePoint)`
   - Cancel all change made to the database after creating a savepoint.

   - #### `public void rollBack()`
   -  Cancel all change made to the database since the last commit.

   - ####  `public void end()`
   - End the transaction and free the connection thread used for this transaction. 
   - #### `public 	List<Object> insert(Object... objs) `
   - Change is not committed automatically, need to invoke .commit() to persist the change. 
   - This method will map the objects passed in insert their data into their table.
   - Objects passed in this method should be annotated with @Entity and the should all be from the same class.

     #### 	`public int delete(Class<?> clazz ,int id)`
     - Change is not committed automatically, need to invoke .commit() to persist the change. 
      This method delete the row with primary key equal to id. 

   -  Returns the id of the row deleted or 0 if the id passed does not exists in the database 

- #### `List<Object>  delete(Class<?> clazz , String condition)`
- Change is not committed automatically, need to invoke .commit() to persist the change. 

- Delete all rows that meet the condition passed.
- Return a list of the primary key of rows that been deleted  

- #### `public Object update(Class<?> clazz, String statement  ,int id )`
- Change is not committed automatically, need to invoke .commit() to persist the change. 
- This method update the row with primary key equal to id. 
-  Return an object representing the row updated in the database.
 
- ####  `public List<Object> update(Class<?> clazz , String statement , String  condition )`
- Change is not committed automatically, need to invoke .commit() to persist the change  
- Update all rows that meet the condition passed.
- Return a list of objects representing the rows updated in the database.
  
      
````java
import com.revature.SQL.Transaction;

Transaction transaction = new Transaction();

Car bmw = new Car(2, "bmw", "blue" );
      
Car renault = new Car(4, "renault", "red");

		
		try {
			transaction.insert(bmw, renault);
			transaction.commit();
			transaction.insert(bmw, renault);
			Savepoint save =	transaction.setSavePoint("savepoint");
			transaction.delete(Car.class, 1);
			transaction.rollBack(save);
			transaction.commit();
			transaction.end();
		} catch (IllegalArgumentException e1) {
			
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
		
			e1.printStackTrace();
		} catch (SQLException e1) {
			
			e1.printStackTrace();
		}

````

- ### DQL


- #### `public <T> T get(Class<T> clazz, int id) throws SQLException`
- Return an instance of the class passed , instantiated using the constructor annotated with  `@ConstructorProperties`.

- #### `public <T> LinkedList<T> getAll(Class<T> clazz ) throws SQLException`
- Return a linkedList of object of all rows inside the table in database, the objects returned are instantiated using the constructor annotated with  `@ConstructorProperties`.

- #### `public <T> LinkedList<T> getWhere(Class<T> clazz , String condition ) throws SQLException`
- Return a linkedList of object of all rows inside the table in database that meet the condition passed as args, the objects returned are instantiated using the constructor annotated with  `@ConstructorProperties`.

- #### `public LinkedList<HashMap<String, Object>> nativeQuerry( String querry ) `
- Return a linkedList of Hashmaps representing each row returned in the result.


- #### 	`public LinkedList<HashMap<String, Object>> joinQuerry( Class<?> clazzA , Class<?> clazzB ) `

	 * clazzA -> first class -> has the a foreign key referencing to the primary key of the second class.
	 * clazzB -> second class -> has a primary key that is used as foreign key in clazzA.
	 * Return linkedList of Hashmaps representing each row returned in the result.
	 * The hashmap has key = column_name , value = the value of that column in each row. 
- #### `	public LinkedList<HashMap<String, Object>> joinQuerry( Class<?> clazzA , Class<?> clazzB , String condition )`
	 * Return linkedList of Hashmaps representing each row returned in  both tables that meet the condition.

- #### 	`public LinkedList<HashMap<String, Object>> joinQuerryManyToMany(Class<?> jointClazz ,Class<?> clazzA , Class<?> clazzB )` 
   - This method is used to write a join query with three table, In case you have a joint table that has foreign key refrenecing to columns in table A and in table B.
   - Return linkedList of Hashmaps representing each row returned in the result.   


- #### 	`public LinkedList<HashMap<String, Object>> joinQuerryManyToMany(Class<?> jointClazz ,Class<?> clazzA , Class<?> clazzB , String condition )` 

 - This method is used to write a join query with three table, In case you have a joint table that has foreign key refrenecing to columns in table A and in table B.
   - Return linkedList of Hashmaps representing each row returned in the result that meet the condition passed.

   ````java


	DQL  dql = new DQL();
		
		try {
			LinkedList<HashMap<String, Object>> result = dql.joinQuerry(Car.class, Person.class);
			
		
			LinkedList<HashMap<String, Object>> resultWithCondition = dql.joinQuerry(Car.class, Person.class, "color = 'red' ");
			
			LinkedList<HashMap<String, Object>> resultForm3tables = dql.joinQuerryManyToMany( JoinedTable.class ,Car.class, Person.class);
		
         /*
         param1 -> refer to as j in SQL statement if you want to add condition make sure to refer to it as j.name , j.age ...
         param2 -> refer to as a in SQL statement if you want to add condition make sure to refere to it as a.name , a.age ...

         param3 -> refer to as b in SQL statement if you want to add condition make sure to refer to it as b.name , b.age ...

         */
			LinkedList<HashMap<String, Object>> resultForm3tablesWithCondition = dql.joinQuerryManyToMany( JoinedTable.class ,Car.class, Person.class, "a.color ='red'");
			
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		


   ````  
  




 



## License

This project uses the following license: [GNU Public License 3.0](https://www.gnu.org/licenses/gpl-3.0.en.html).
# ORM Connection Proxy OCP
