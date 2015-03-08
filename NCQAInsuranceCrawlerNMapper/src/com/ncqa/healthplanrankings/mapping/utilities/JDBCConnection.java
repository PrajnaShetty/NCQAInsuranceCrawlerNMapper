package com.ncqa.healthplanrankings.mapping.utilities;
 
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
 
/*
 * @author - Prajna Shetty (pshet001@ucr.edu)
 */
public class JDBCConnection {
 
  public static Connection  getConnectionNCQA(){
 
	System.out.println("-------- MySQL JDBC Connection Testing ------------");
 
	try {
		Class.forName("com.mysql.jdbc.Driver");
	} catch (ClassNotFoundException e) {
		System.out.println("MySQL JDBC Driver not found");
		e.printStackTrace();
	}
 
	System.out.println("MySQL JDBC Driver Registered!");
	Connection connection = null;
 
	try {
		connection = DriverManager
		.getConnection("jdbc:mysql://localhost:3306/msprojectdb_ncqa","root", "root");
 
	} catch (SQLException e) {
		System.out.println("Connection Failed! Check output console");
		e.printStackTrace();
	}
 
	if (connection != null) {
		System.out.println("Connection Successful!");
	} else {
		System.out.println("Failed to make connection!");
	}
	
	return connection;
  }
  
  public static Connection  getConnectionVitals(){
	  
		System.out.println("-------- MySQL JDBC Connection Testing ------------");
	 
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("MySQL JDBC Driver not found");
			e.printStackTrace();
		}
	 
		System.out.println("MySQL JDBC Driver Registered!");
		Connection connection = null;
	 
		try {
			connection = DriverManager
			.getConnection("jdbc:mysql://localhost:3306/msprojectdb","root", "root");
	 
		} catch (SQLException e) {
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
		}
	 
		if (connection != null) {
			System.out.println("Connection Successful!");
		} else {
			System.out.println("Failed to make connection!");
		}
		
		return connection;
	  }
  
  public static Connection  getConnectionHealthGrades(){
	  
		System.out.println("-------- MySQL JDBC Connection Testing ------------");
	 
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("MySQL JDBC Driver not found");
			e.printStackTrace();
		}
	 
		System.out.println("MySQL JDBC Driver Registered!");
		Connection connection = null;
	 
		try {
			connection = DriverManager
			.getConnection("jdbc:mysql://localhost:3306/msprojectdb_ncqa","root", "root");
	 
		} catch (SQLException e) {
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
		}
	 
		if (connection != null) {
			System.out.println("Connection Successful!");
		} else {
			System.out.println("Failed to make connection!");
		}
		
		return connection;
	  }
}
