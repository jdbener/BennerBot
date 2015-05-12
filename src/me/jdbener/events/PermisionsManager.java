package me.jdbener.events;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import me.jdbener.apis.APIManager;
import me.jdbener.utill.botId;

public class PermisionsManager {
	public static final int disabled = 10;
	public static final int viewer = 0;
	public static final int follower = 1;
	public static final int regular = 2;
	public static final int subscriber = 3;
	public static final int moderator = 4;
	public static final int admin = 5;
	public static final int owner = 6;
	
	public static void main (String[] args){
		PermisionsManager p = new PermisionsManager();
		System.out.println(p.userHasPermisionLevel("jdbener", viewer));
	}
	public PermisionsManager(){
		setupPermTables();
	}
	public static String level2String(int pl){
		switch(pl){
		case disabled: return "Disabled";
		case viewer: return "Viewer";
		case follower: return "Follower";
		case regular: return "Regular";
		case subscriber: return "Subscriber";
		case moderator: return "Moderator";
		case admin: return "Administrator";
		case owner: return "Owner";
		}
		return "Unknown type";
	}
	public void setupPermTables(){
		 try {
			 Connection c = APIManager.getConnection(); 
			 Statement stmt = c.createStatement();
		     stmt.executeUpdate("CREATE TABLE IF NOT EXISTS PERMLEVELS " +
	                   "(USER          TEXT    NOT NULL, " + 
	                   " LEVEL         INT,"
	                   +"BID		   INT)");
		     stmt.close();
		     c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			Connection c = APIManager.getConnection(); 
			Statement stmt = c.createStatement();
			stmt.executeUpdate("CREATE TABLE IF NOT EXISTS PERMS " +
	                   "(PERM          TEXT    NOT NULL, " + 
	                   " LEVEL         INT,"
	                   +"BID		   INT)");
		    stmt.close();
		    c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void updateUserPermLevel(String name, int level){
		name = name.toLowerCase();
		try{
			Connection c = APIManager.getConnection();
			Statement stmt = c.createStatement();
			stmt.execute("DELETE FROM PERMLEVELS WHERE USER = '"+name+"' && BID = "+botId.getBotID());
			stmt.execute("INSERT INTO PERMLEVELS VALUES ('"+name+"',"+level+","+botId.getBotID()+");");
			stmt.close();
			c.close();
		} catch (SQLException e){e.printStackTrace();}
	}
	public void updatePermLevel(String perm, int level){
		perm = perm.toLowerCase();
		try{
			Connection c = APIManager.getConnection();
			Statement stmt = c.createStatement();
			stmt.execute("DELETE FROM PERMS WHERE PERM = '"+perm+"' && BID = "+botId.getBotID());
			stmt.execute("INSERT INTO PERMS VALUES ('"+perm+"',"+level+","+botId.getBotID()+");");
			stmt.close();
			c.close();
		} catch (SQLException e){e.printStackTrace();}
	}
	public int getUserPermLevel(String name){
		name = name.toLowerCase();
		try{
			Connection c = APIManager.getConnection();
			Statement stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM PERMLEVELS WHERE USER = '"+name+"' && BID = "+botId.getBotID());
			while(rs.next()){return(rs.getInt("LEVEL"));} 
		} catch (SQLException e){e.printStackTrace();}
		updateUserPermLevel(name, viewer);
		return viewer;
	}
	public int getPermLevel(String perm){
		perm = perm.toLowerCase();
		try{
			Connection c = APIManager.getConnection();
			Statement stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM PERMS WHERE PERM = '"+perm+"' && BID = "+botId.getBotID());
			while(rs.next()){return(rs.getInt("LEVEL"));} 
		} catch (SQLException e){e.printStackTrace();}
		updatePermLevel(perm, viewer);
		return viewer;
	}
	public String getUserPermLevelString(String name){
		return level2String(getUserPermLevel(name));
	}
	public String getPermLevelString(String perm){
		return level2String(getPermLevel(perm));
	}
	public boolean userHasPermisionLevel(String name, int level){
		if(getUserPermLevel(name) >= level)
			return true;
		return false;
	}
	public boolean userHasPermision(String name, String permision){
		return userHasPermisionLevel(name, getPermLevel(permision));
	}
	
}
