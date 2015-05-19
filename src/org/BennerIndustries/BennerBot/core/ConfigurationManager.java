package org.BennerIndustries.BennerBot.core;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.BennerIndustries.BennerBot.api.datatypes.SettingReference;

/**
 * This class manages the setting system. It keeps the local storage and database in sync so that settings can be used across runs.
 * @author Joshua Dahl (Jdbener)
 */
public class ConfigurationManager {
	
	/** A List of the settings that are to be actively managed*/
	private ArrayList<SettingReference> settings = new ArrayList<SettingReference>();
	/** The SQL statement that is used to run all of the connections to the database */
	private Statement s = SQLConnection.createStatement();
	
	public ConfigurationManager(){
		try {
			SQLConnection.createStatement().execute("CREATE TABLE IF NOT EXISTS 'Configuration' (name text, value text)");
		} catch (SQLException e) {e.printStackTrace();}
	    finally {try {
				s.close();
		} catch (SQLException e) {e.printStackTrace();}}
	}
	/**
	 * Starts managing a setting and loads the correct value from the database
	 * @param ref a reference to the setting to start managing
	 */
	public boolean addSetting(SettingReference ref){
		settings.add(ref);
		try{
			if(!hasSQL(ref.getName())){
				s.execute("DELETE FROM 'Configuration' WHERE name='"+ref.getName()+"'");
				s.execute("INSERT INTO 'Configuration' VALUES ('"+ref.getName()+"', '"+ref.getValueString()+"')");
			} else
				ref.setValue(hasSQL(ref.getName()));
			return true;
		} catch(SQLException e){e.printStackTrace();}
		return false;
	}
	/**
	 * Updates the specified setting with the specified value
	 * @param name the name of the setting to update
	 * @param value the new value
	 */
	public boolean updateSetting(String name, Object value){
		try{
			s.execute("DELETE FROM 'Configuration' WHERE name='"+name+"'");
			s.execute("INSERT INTO 'Configuration' VALUES ('"+name+"', '"+value+"')");
			return true;
		} catch(SQLException e){e.printStackTrace();}
		return false;
	}
	/**
	 * Weather or not a setting is being managed
	 * @param name the setting to check for
	 */
	public boolean hasSetting(String name){
		for(SettingReference ref: settings){
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
			ResultSet set = s.executeQuery("SELECT * FROM 'Configuration'");
			while(set.next()){
				if(set.getString("name").equalsIgnoreCase(name))
					return true;
			}
		} catch(SQLException e){e.printStackTrace();}
		return false;
	}
	/**
	 * Returns a reference to a setting
	 * @param name the name of the setting to search for
	 */
	public SettingReference getReference(String name){
		refresh();
		for(SettingReference ref: settings){
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
			ResultSet set = s.executeQuery("SELECT * FROM 'Configuration'");
			while(set.next()){
				for(SettingReference ref: settings){
					if(ref.getName().equalsIgnoreCase(set.getString("name")))
						ref.setValue(set.getString("value"));
				}
			}
		} catch(SQLException e){e.printStackTrace();}
	}
}
