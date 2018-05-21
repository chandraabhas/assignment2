package com.mininet.dao;
import java.sql.Connection;
import java.sql.SQLException;

import com.mininet.modal.Person;
import com.mininet.modal.Sex;

/**
 * 
 * @author 
 * DAO class
 */
public class MininetDAO {
	
	/**
	 * connection object
	 */
	private Connection conn;
	
	/**
	 * 
	 * @param conn instantiate Connection object
	 */
	public MininetDAO(Connection conn)
	{
		this.conn=conn;
	}
	
	/**
	 * add the passed person to database
	 * @param person person to be added to database
	 * @return Response to be displayed to the user
	 */
	public String insertPerson(Person person) {
		java.sql.PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement("insert into person (name,image,status,age,state,sex"
					+ ") values(?,?,?,?,?,?)" );
			pstmt.setString(1, person.getName());
	         pstmt.setString(2, person.getImage());
	         pstmt.setString(3, person.getStatus());
	         pstmt.setInt(4, person.getAge());
	         pstmt.setString(5, person.getState().toString());
	         pstmt.setString(6, person.getSex()==Sex.Male?"M":"F");
	         pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return "Error occured while inserting person to db";
		}
		return "Person successfully added to DB";
	}
	
	/**
	 * 
	 * @param person person to be deleted from database
	 * @return Response to be displayed to the user
	 */
	public String deletePerson(Person person) {
		java.sql.PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement("delete from person where name = ? ");
			pstmt.setString(1, person.getName());
	        
	         pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return "Error occured while deleting person from db";
		}
		return "Person successfully deleted from DB";
		
	}
}
