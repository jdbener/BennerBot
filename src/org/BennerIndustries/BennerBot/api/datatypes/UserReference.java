package org.BennerIndustries.BennerBot.api.datatypes;

import java.awt.Color;

public class UserReference {
	/** A set of values that define the possible permission levels for users */
	public enum PermissionLevels {base, regular, regularPlus, moderator, admin;
		/**
		 * Changes a enum value into a string value
		 * @param level the enum value
		 * @return the string value
		 */
		public static String toString(PermissionLevels level){
			switch(level){
			case regular: return "regular";
			case regularPlus: return "regular+";
			case moderator: return "moderator";
			case admin: return "administrator";
			default: return "default";
			}
		}
	};
	
	private PermissionLevels permLevel;
	private String name, channel, pIdent;
	private Boolean banned;
	private Color color;
	
	public UserReference(String name, Color color, String channel, PermissionLevels level, String pIdent){
		this.name = name;
		this.channel = channel;
		this.color = color;
		this.pIdent = pIdent;
		permLevel = level;
		banned = false;
	}
	public UserReference(String name, Color color, String channel, PermissionLevels level, String pIdent, boolean banned){
		this.name = name;
		this.channel = channel;
		this.color = color;
		this.pIdent = pIdent;
		permLevel = level;
		this.banned = banned;
	}
	/**
	 * @return the name of the user that this object represents
	 */
	public String getName(){
		return name;
	}
	/**
	 * @return the channel that this user is from
	 */
	public String getChannel(){
		return channel;
	}
	public String getSourceIdentifier(){
		return pIdent;
	}
	/**
	 * @return the permission level that this user has
	 */
	public PermissionLevels getPermissionLevel(){
		return permLevel;
	}
	/**
	 * @return the permission level that this user has in String form
	 */
	public String getPermissionLevel(boolean useless){
		return PermissionLevels.toString(permLevel);
	}
	/**
	 * @return the color that this user will show up in chats
	 */
	public Color getChatColor(){
		return color;
	}
	/**
	 * @return weather or not this user is banned
	 */
	public boolean isBanned(){
		return banned;
	}
	/**
	 * Changes the permission level of this user
	 * @param level the new level
	 */
	public boolean setPermisionLevel(PermissionLevels level){
		permLevel = level;
		return true;
	}
	/**
	 * Changes the chat color of this user
	 * @param color the new color
	 */
	public boolean setChatColor(Color color){
		this.color = color;
		return true;
	}
	/**
	 * Toggles the banned status of a user
	 */
	public boolean toggleBanned(){
		if(banned == false)
			banned = true;
		else
			banned = false;
		return true;
	}
	/**
	 * Sets the banned status of a user
	 * @param banned the new banned status
	 */
	public boolean toggleBanned(boolean banned){
		this.banned = banned;
		return true;
	}
}
