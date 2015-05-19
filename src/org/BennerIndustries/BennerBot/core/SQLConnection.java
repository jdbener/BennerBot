package org.BennerIndustries.BennerBot.core;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLConnection {
	public static Connection createConnection(){
		try {
		      Class.forName("org.sqlite.JDBC");
		      return DriverManager.getConnection("jdbc:sqlite:"+new File(".db").getAbsolutePath());
		    } catch (Exception e) {
		      e.printStackTrace();
		}
		return null;
	}
	public static Statement createStatement(){
		try {
			return createConnection().createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
