package com.mininet.helper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import com.mininet.exceptions.NoSuchAgeException;
import com.mininet.modal.Adult;
import com.mininet.modal.Child;
import com.mininet.modal.Person;
import com.mininet.modal.Relation;
import com.mininet.modal.RelationType;
import com.mininet.modal.Sex;
import com.mininet.modal.State;
import com.mininet.modal.YoungChild;

/**
 * 
 * @author 
 * Helper class for our application
 */
public class MiniNetHelper {

	/**
	 *  Connection string from Sqlite
	 */
	private static final String SQLITE_CONNECTION_STRING = "jdbc:sqlite:persondb1.db";

	/**
	 *  Connection string from HSQL
	 */
	private static final String HSQLDB_CONNECTION_STRING = "jdbc:hsqldb:hsqldb1.db";

	/**
	 * name of the people file
	 */
	public static String PEOPLE_FILENAME="people.txt";

	/**
	 * name of the relations file
	 */
	public static String RELATION_FILENAME="relations.txt";

	/**
	 * conection object
	 */
	public static Connection conn;

	/**
	 * 
	 * @return list of persons read
	 * @throws NoSuchAgeException if invalid age is present database 
	 */
	public static List<Person> getPersons() throws NoSuchAgeException
	{
		List<Person> persons=null;

		/**
		 * read from text file
		 */
		persons=readPersonFile();

		/**
		 * if text file is not found
		 * try to read from SQLite DB
		 */
		if(persons==null)
		{
			System.out.println("People.txt not found . ");
			persons=getSQLITEConnection();
		}
		else
			return persons;

		/**
		 * If unable to read from SQLite DB
		 * read from HSQL database
		 */
		if(persons==null)
		{
			System.out.println("SQlite Connection not exist not found . ");
			persons=getHSQLDBConnection();
		}
		else
			return persons;
		if(persons==null)
			System.out.println("HSQLDB Connection not exist not found . ");
		/**
		 * list of persons
		 */
		return persons;
	}

	/**
	 * 
	 * @return the list of persons after reading SQL db
	 * @throws NoSuchAgeException if invalid age is there
	 */
	private static List<Person> getSQLITEConnection() throws NoSuchAgeException {
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection(SQLITE_CONNECTION_STRING);
			//createTable();
			//insertData();
			return readPersons();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * insert data into table
	 */
	/*
	private static void insertData() {
		java.sql.PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement("insert into person (name,image,status,age,state,sex"
					+ ") values(?,?,?,?,?,?)" );
			pstmt.setString(1, "Alex Smith");
	         pstmt.setString(2, "");
	         pstmt.setString(3, "student at RMIT");
	         pstmt.setInt(4, 21);
	         pstmt.setString(5, "WA");
	         pstmt.setString(6, "M");
	         pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}


	}
/**
	 * create table 
	 */
	/*
	private static void createTable() {
	String createSQL = "create table person ("
			+ "name varchar(500) primary key ,"
			+ " image varchar(500),   "
			+ "status varchar(100) , "
			+"  age integer,"
			+ "state varchar(20) , "

			+ "sex varchar(30)) ";
		java.sql.Statement stmt = null;
		try {
			stmt = conn.createStatement();
			 stmt.execute(createSQL);

		} catch (SQLException e) {
			e.printStackTrace();
		}


	}
	 */

	/**
	 * 
	 * @return the list of persons after reading HSQL db
	 * @throws NoSuchAgeException if invalid age is there
	 */
	private static List<Person> getHSQLDBConnection() throws NoSuchAgeException {
		try {
			Class.forName("org.hsqldb.jdbcDriver");
			conn = DriverManager.getConnection(HSQLDB_CONNECTION_STRING);
			//createTable();
			//insertData();
			return readPersons();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @return list of read persons from the connection object 
	 * @throws NoSuchAgeException if age is invalid
	 */
	private static List<Person> readPersons() throws NoSuchAgeException {
		List<Person> persons=new ArrayList<>();
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt=conn.createStatement();
			rs = stmt.executeQuery("select * from person");
			/**
			 * traverse the whole resultset
			 */
			while (rs.next()) {

				String name;
				String image;
				String status;
				int age;
				State state;
				Sex sex;
				name=rs.getString(1);
				image=rs.getString(2);
				status=rs.getString(3);
				age=rs.getInt(4);
				state=getState(rs.getString(5).trim());
				sex=rs.getString(6).trim().equals("M")?Sex.Male:Sex.Female;

				/**
				 * See the class of The person
				 */
				if(age <= 2)
					persons.add( new YoungChild(name, image, status, sex, age, state));
				else if (age > 2 && age <= 16)
					persons.add(  new Child(name, image, status, sex, age, state));
				else
					persons.add(  new Adult(name, image, status, sex, age, state));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

		return persons;
	}

	/**
	 * 
	 * @return the list of read person from database
	 * @throws NoSuchAgeException invalid age
	 */
	private static List<Person> readPersonFile() throws NoSuchAgeException {
		List<Person> persons=new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(PEOPLE_FILENAME))) {
			String line;

			while ((line = br.readLine()) != null) {
				Person person=getPerson(line);
				persons.add(person);
			}

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		return persons;
	}

	/**
	 * 
	 * @param line CSV record of person
	 * @return the Person object constructed from this Line
	 * @throws NoSuchAgeException Invalid age is there
	 */
	private static Person getPerson(String line) throws NoSuchAgeException {
		String name;
		String image;
		String status;
		int age;
		State state;
		Sex sex;
		String[] arr=line.split(",");
		name=arr[0].trim();
		image=arr[1].trim();
		status=arr[2].trim();
		status=status.substring(1, status.length()-1);
		image=image.substring(1, image.length()-1);
		sex=arr[3].trim().equals("M")?Sex.Male:Sex.Female;
		age=Integer.parseInt(arr[4].trim());
		state=getState(arr[5].trim());

		if(age <= 2)
			return new YoungChild(name, image, status, sex, age, state);
		else if (age > 2 && age <= 16)
			return new Child(name, image, status, sex, age, state);
		else
			return new Adult(name, image, status, sex, age, state);
	}

	/**
	 * 
	 * @param state State String
	 * @return Equivalent State enum of the Person
	 */
	private static State getState(String state) {
		if(state.equals("ACT"))
			return State.ACT;
		else if(state.equals("NSW"))
			return State.NSW;
		else if(state.equals("NT"))
			return State.NT;
		else if(state.equals("QLD"))
			return State.QLD;
		else if(state.equals("SA"))
			return State.SA;
		else if(state.equals("TAS"))
			return State.TAS;
		else if(state.equals("VIC"))
			return State.VIC;
		else if(state.equals("WA"))
			return State.WA;
		return null;
	}

	/**
	 * 
	 * @param persons list of persons in application
	 * @return ist of relations read
	 */
	public static List<Relation> getRelations(List<Person> persons) {
		List<Relation> relations=new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(RELATION_FILENAME))) {
			String line;

			while ((line = br.readLine()) != null) {
				Relation relation=getRelation(persons,line);
				if(relation!=null)
					relations.add(relation);
			}

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		return relations;
	}

	/**
	 * 
	 * @param persons list of persons
	 * @param name name of person
	 * @return Equivalent Person object of the passed name
	 */
	public static Person getPerson(List<Person> persons,String name)
	{
		for(Person p:persons)
		{
			if(p.getName().equalsIgnoreCase(name))
				return p;
		}
		return null;
	}

	/**
	 * 
	 * @param persons list of persons
	 * @param line Relation csv STring
	 * @return Relation object constructed from this String
	 */
	private static Relation getRelation(List<Person> persons, String line) {
		String[] arr=line.split(",");

		Person from=getPerson(persons,arr[0].trim());
		if(from==null)
			return null;
		Person to=getPerson(persons,arr[1].trim());
		if(to==null)
			return null;
		RelationType relation=getRelationType(arr[2].trim());
		return new Relation(from, to, relation);
	}


	/**
	 * 
	 * @param r Relation STring
	 * @return Equivalent RelationType enum for passed String
	 */
	private static RelationType getRelationType(String r) {
		if(r.equalsIgnoreCase("Parent"))
			return RelationType.Parent;
		else if(r.equalsIgnoreCase("Colleague"))
			return RelationType.Colleague;
		else if(r.equalsIgnoreCase("Classmate"))
			return RelationType.Classmate;
		else if(r.equalsIgnoreCase("Couple"))
			return RelationType.Couple;
		else if(r.equalsIgnoreCase("Friends"))
			return RelationType.Friends;
		return null;
	}

	/**
	 * 
	 * @return Connection object of this class
	 */
	public static Connection getConnection() {
		return conn;
	}


}
