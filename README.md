# orm connection proxy OCP


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
  git clone https://github.com/marouanekhabbaz/orm_connection_proxy.git
  cd orm_connection_proxy
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
url= your database url
username= your  database username
password= your database password  

 ``` 

## Usage  
  ### Annotating classes  
  All classes which represent objects in database must be annotated.


   - #### @Entity(tableName="table_name")

      - Indicates that this class is associated with table 'table_name'  
      - Each mapped class must should have @Entity annotation .

   ### @ConstructorProperties(value = { "person_id",  "person" , "age" , "doors",  "valid" }) (imported from java.beans package )

      - Developer can have multiple constructor in a  class. but must have one constructor annotated with  @ConstructorProperties
      - Pass the column_name associted with each parameter into value attribute.
      - This contructor will be use to create an instance of this class at runtime (When retiriving data from the database). 

   - #### 	@Id(columnName="name_id")

      - Indicates that the annotated field is the primary key for the table.
      - Each mapped class must should have a @Id field representing the primary key of the table.


   - #### @Column(columnName="person", dataType ="varchar(50)", defaultValue = " 'blue' " , 	unique = true , check = "condition" ,  refrences = "table_name(column_name)" , nullable = true)

      - Column annotation has six attributes representing the constraints of the column 
      -**columnName**  -> name of the column.
      -**dataType**     -> defines what value this column can hold ex: varchar(50), INTEGER , NUMERIC ...
      -**defaultValue**-> optional , set the default value of a column if no value provided (if the value is string add 'value'). 
      -**unique** -> optional by default is set to false , don't allow duplicate value.
      -**nullabe** ->  optional by default is set to true. if you set it to false, it won't allow null value.
      -**check** ->optional ,  allow values that meet the condition into the column ex:   CHECK (salary>=15)
      -**refrences** -> to create a foreign key that refrence to a column in another table ex: refrences = "table_name(column_name)" .
    

    #### @ForeignKey(columnName = "person", joinedColumn = "person_id", joinedTable = "persons")
    
      - Add this annotation the column that refrences column in another table.
      - This annotation has 3 attributes.
      -**columnName** -> the name of this column should be exactly the same as the one used in @Column(columnName="same name").
      -**joinedColumn**->  the name of the column that foreign key depends on,  must be the same used in @@Column(refrences = "table_name(**same_column_name**)").
      -**joinedTable** ->  the name of the table that foreign key refrencing to. must be the same used in @@Column(refrences = "**same_table_name**(column_name)"). 
      -Use this annotation to enable the use of Join Queries (simple or multiple join like many to many relationship )






  

  ### User API  
  
  - #### `public static Something getInstance()`  
     - returns the singleton instance of the class. It is the starting point to calling any of the below methods.  
  - #### `public HashMap<Class<?>, HashSet<Object>> getCache()`  
     - returns the cache as a HashMap.  
  - #### `public boolean addClass(final Class<?> clazz)`  
     - Adds a class to the ORM. This is the method to use to declare a Class is an object inside of the database.  
  - #### `public boolean UpdateObjectInDB(final Object obj,final String update_columns)`  
     - Updates the given object in the databse. Update columns is a comma seperated lsit fo all columns in the onject which need to be updated  
  - #### `public boolean removeObjectFromDB(final Object obj)`  
     - Removes the given object from the database.  
  - #### `public boolean addObjectToDB(final Object obj)`  
     - Adds the given object to the database.  
  - #### `public Optional<List<Object>> getListObjectFromDB(final Class <?> clazz, final String columns, final String conditions)`  
  - #### `public Optional<List<Object>> getListObjectFromDB(final Class <?> clazz, final String columns, final String conditions,final String operators)`  
  - #### `public Optional<List<Object>> getListObjectFromDB(final Class<?> clazz)`  
     - Gets a list of all objects in the database which match the included search criteria  
        - columns - comma seperated list of columns to search by.  
        - conditions - coma seperated list the values the columns should match to.  
        - operators - comma seperated list of operators to apply to columns (AND/OR) in order that they should be applied.  
  - #### `public void beginCommit()`  
     - begin databse commit.  
  - #### `public void Rollback()`  
     - Rollback to previous commit.  
  - #### `public void Rollback(final String name)`  
     - Rollback to previous commit with given name.  
  - #### `public void setSavepoint(final String name)`  
     - Set a savepoint with the given name.  
  - #### `public void ReleaseSavepoint(final String name)`  
     - Release the savepoint with the given name.  
  - #### `public void enableAutoCommit()`  
     - Enable auto commits on the database.  
  - #### `public void setTransaction()`  
     - Start a transaction block.  
  - #### `public void addAllFromDBToCache(final Class<?> clazz)`  
     - Adds all objects currently in the databse of the given clas type to the cache.  



## License

This project uses the following license: [GNU Public License 3.0](https://www.gnu.org/licenses/gpl-3.0.en.html).
# orm_connection_proxy_p1
