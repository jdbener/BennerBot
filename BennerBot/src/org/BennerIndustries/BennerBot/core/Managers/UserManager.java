package org.BennerIndustries.BennerBot.core.Managers;

import java.awt.Color;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map.Entry;

import org.BennerIndustries.BennerBot.api.datatypes.UserReference;
import org.BennerIndustries.BennerBot.core.SQLConnection;
import org.BennerIndustries.BennerBot.utility.BennerCore;

public class UserManager {
	/** A list of the user objects to be managed */
	ArrayList<UserReference> users = new ArrayList<UserReference>();
	/** The SQL statement that is used to run all of the connections to the database */
	private Statement s = SQLConnection.createStatement();
	
	public UserManager(){
		try {
			SQLConnection.createStatement().execute("CREATE TABLE IF NOT EXISTS 'Users' (PID text, user text, color text)");
		} catch (SQLException e) {e.printStackTrace();}
	    finally {try {
				s.close();
		} catch (SQLException e) {e.printStackTrace();}}
	}
	/**
	 * Starts managing a user and loads the correct values from the database
	 * @param ref a reference to the user to start managing
	 */
	public boolean addUser(UserReference ref){
		users.add(ref);
		if(!hasSQL(ref.getName())){
		for(Entry<String, Color> e: ref.getIdentifierColorPairs().entrySet())
			try{
				s.execute("DELETE FROM 'Users' WHERE user='"+ref.getName()+"' AND PID='"+e.getKey()+"'");
				s.execute("INSERT INTO 'Users' VALUES ('"+e.getKey()+"', '"+ref.getName()+"', '"+BennerCore.rgb2Hex(e.getValue())+"')");
			} catch(SQLException ex){ex.printStackTrace();}
		} else {
			for(Entry<String, Color> e: getReference(ref.getName()).getIdentifierColorPairs().entrySet()){
				ref.setIdentiferColorPair(e.getKey(), e.getValue());
			}
		}
		return false;
	}
	/**
	 * Updates the specified user with the specified value
	 * @param name the name of the user
	 * @param pIdent the plugin identifier of the plugin to associate a color to
	 * @param value the color to associate
	 */
	public boolean updateUser(String name, String pIdent, Color value){
		try{
			s.execute("DELETE FROM 'Users' WHERE user='"+name+"' AND PID='"+pIdent+"'");
			s.execute("INSERT INTO 'Users' VALUES ('"+pIdent+"', '"+name+"', '"+BennerCore.rgb2Hex(value)+"')");
			refresh();
			return true;
		} catch(SQLException e){e.printStackTrace();}
		return false;
	}
	/**
	 * Stops managing a user
	 * @param ref a reference to the user to stop managing
	 */
	public boolean removeSetting(UserReference ref){
		for(int i = 0; i < users.size(); i++){
			if(users.get(i).equals(ref)){
				users.remove(i);
				return true;
			}
		}
		return false;
	}
	/**
	 * Stops managing a user
	 * @param i the index id of the user to stop managing
	 */
	public boolean removeSetting(int i){
		users.remove(i);
		return true;
	}
	/**
	 * Checks if a user is being managed by a specific plugin
	 * @param name the name of the user to search for
	 * @param pIdent the plugin identifier to search for
	 */
	public boolean hasUser(String name, String pIdent){
		for(UserReference ref: users){
			if(ref.getName().equalsIgnoreCase(name) && ref.getIdentifierColorPairs().containsKey(pIdent)){
				return true;
			}
		}
		return false;
	}
	/**
	 * Checks if a user is being managed by a specific plugin
	 * @param name the name of the user to search for
	 */
	public boolean hasUser(String name){
		for(UserReference ref: users){
			if(ref.getName().equalsIgnoreCase(name)){
				return true;
			}
		}
		return false;
	}
	/**
	 * Weather or not a setting is in the database
	 * @param name the setting to check for
	 */
	private boolean hasSQL(String name){
		try{
			ResultSet set = s.executeQuery("SELECT * FROM 'Users'");
			while(set.next()){
				if(set.getString("user").equalsIgnoreCase(name))
					return true;
			}
		} catch(SQLException e){e.printStackTrace();}
		return false;
	}
	/**
	 * Returns a reference to a user
	 * @param name the name of the user to search for
	 */
	public UserReference getReference(String name){
		refresh();
		for(UserReference ref: users){
			if(ref.getName().equalsIgnoreCase(name)){
				return ref;
			}
		}
		return null;
	}
	/**
	 * Makes sure that the database and in-memory information are in sync.
	 */
	private void refresh(){
		try{
			ResultSet set = s.executeQuery("SELECT * FROM 'Users'");
			while(set.next()){
				for(UserReference ref: users){
					if(ref.getName().equalsIgnoreCase(set.getString("user")))
						try{
							ref.setIdentiferColorPair(set.getString("PID"), BennerCore.hex2Rgb(set.getString("color")));
						} catch (Exception e){e.printStackTrace();}
				}
			}
		} catch(SQLException e){e.printStackTrace();}
	}
}
