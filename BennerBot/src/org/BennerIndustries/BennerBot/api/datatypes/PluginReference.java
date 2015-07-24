package org.BennerIndustries.BennerBot.api.datatypes;

import java.util.ArrayList;
/**
 * This class defines a reference to a plugin, and provides all the needed backend information for a plugin
 * @author Joshua Dahl (Jdbener)
 */
public class PluginReference {
	/**
	 * A list of every tag that plugins can have
	 */
	public enum PluginTags {CONNECTIONPROVIDER, FILTER, UI, UTILITY, UNTAGGED, ALL};
	
	private String pIdent, name, desc = "";
	private PluginTags[] tags;
	private ArrayList<UserReference> users = new ArrayList<UserReference>();
	
	/**
	 * Creates a reference that identifies a plugin, providing important infromation about it that can be easily accessed.
	 * @param identifier the identifier that is unique to this plugin.
	 * @param name the name of the plugin
	 * @param tags a list of tags that identify the plugin
	 */
	public PluginReference(String identifier, String name, PluginTags[] tags){
		pIdent = identifier;
		this.name = name;
		this.tags = tags;
	}
	/**
	 * Creates a reference that identifies a plugin, providing important infromation about it that can be easily accessed.
	 * @param identifier the identifier that is unique to this plugin.
	 * @param name the name of the plugin.
	 * @param description provides some infromation about what the plugin does.
	 * @param tags a list of tags that identify the plugin
	 */
	public PluginReference(String identifier, String name, String description, PluginTags[] tags){
		pIdent = identifier;
		this.name = name;
		this.desc = description;
		this.tags = tags;
	}
	/**
	 * @return the plugin identifier for this plugin
	 */
	public String getIdentifier(){
		return pIdent;
	}
	/**
	 * @return the name of this plugin
	 */
	public String getName(){
		return name;
	}
	/**
	 * @return the description of this plugin
	 */
	public String getDescription(){
		return desc;
	}
	/**
	 * @return the tags used to describe this plugin
	 */
	public PluginTags[] getTags(){
		return tags;
	}
	/**
	 * Adds a user to the reference list
	 * @param ref a reference to the user object to add
	 */
	public boolean addUser(UserReference ref){
		return users.add(new UserReference(ref.getName(), ref.getPermissionLevel(), ref.getIdentifierColorPairs()));
	}
	/**
	 * Removes a user from the reference list
	 * @param name the name of the user to remove
	 */
	public boolean removeUser(String name){
		for(int i = 0; i < users.size(); i++){
			if(users.get(i).getName().equalsIgnoreCase(name)){
				users.remove(i);
				return true;
			}
		}
		return false;
	}
	/**
	 * Removes a user from the reference list
	 * @param i the index at which the user is sitting in the list
	 */
	public boolean removeUser(int i){
		users.remove(i);
		return true;
	}
	/**
	 * Returns the UserReference stored in the reference list
	 * @param obj the name or index of the user to search for
	 * @return the UserReference specified, or null if it dosent exist
	 */
	public UserReference getUser(Object obj){
		if(obj.getClass().getName().contains("Integer"))
			return users.get(Integer.parseInt(obj+""));
		if(obj.getClass().getName().contains("String"))
			for(UserReference ref: users)
				if(ref.getName().equalsIgnoreCase(obj.toString()))
					return ref;
		return null;
	}
}
