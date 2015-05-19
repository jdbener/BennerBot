package org.BennerIndustries.BennerBot.core;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
/**
 * This class provides a connection to the database file
 * @author Joshua Dahl (Jdbener)
 */
public class SQLConnection {

	/**
	 * Creates a connection to the database file
	 * @return the established connection
	 */
	public static Connection createConnection(){
		try {
		      Class.forName("org.sqlite.JDBC");
		      return DriverManager.getConnection("jdbc:sqlite:"+new File(".db").getAbsolutePath());
		    } catch (Exception e) {
		      e.printStackTrace();
		}
		return null;
	}
	/**
	 * Creates a statement that classes can use to interact with the database file
	 * @return the statement
	 */
	public static Statement createStatement(){
		try {
			return createConnection().createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
