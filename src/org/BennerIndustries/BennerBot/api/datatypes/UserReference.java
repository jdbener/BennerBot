package org.BennerIndustries.BennerBot.api.datatypes;

import java.awt.Color;

import org.BennerIndustries.BennerBot.utility.BennerCore;
/**
 * A reference to a specific user
 * @author Joshua Dahl (Jdbener)
 */
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
	
	/** the default colors for a reference that dosent have its color specified */
	private Color[] defaultColors = new Color[]{
		BennerCore.hex2Rgb("#FF0000"),	//Red
		BennerCore.hex2Rgb("#0000FF"),	//Blue
		BennerCore.hex2Rgb("#00FF00"),	//Green
		BennerCore.hex2Rgb("#B22222"),	//Fire Brick
		BennerCore.hex2Rgb("#FF7F50"),	//Coral
		BennerCore.hex2Rgb("#9ACD32"),	//Yellow Green
		BennerCore.hex2Rgb("#FF4500"),	//Orange Red
		BennerCore.hex2Rgb("#2E8B57"),	//Sea Green
		BennerCore.hex2Rgb("#DAA520"),	//Golden Rod
		BennerCore.hex2Rgb("#D2691E"),	//Chocolate
		BennerCore.hex2Rgb("#5F9EA0"),	//Cadet Blue
		BennerCore.hex2Rgb("#1E90FF"),	//Dodger Blue
		BennerCore.hex2Rgb("#FF69B4"),	//HotPink
		BennerCore.hex2Rgb("#8A2BE2"),	//Blue Violet
		BennerCore.hex2Rgb("#00FF7F")	//Spring Green
	};
	
	private PermissionLevels permLevel;
	private String name, pIdent;
	private int banned;
	private Color color;
	
	/**
	 * @param name the name of the user
	 * @param color the color that this user will have in GUI based chat displays
	 * @param level the permission level of the user according to the bot
	 * @param pIdent the source plugin that this user is from
	 */
	public UserReference(String name, Color color, PermissionLevels level, String pIdent){
		this.name = name;
		this.color = color;
		this.pIdent = pIdent;
		permLevel = level;
		banned = 0;
	}
	/**
	 * @param name the name of the user
	 * @param level the permission level of the user according to the bot
	 * @param pIdent the source plugin that this user is from
	 */
	public UserReference(String name, PermissionLevels level, String pIdent){
		this.name = name;
		this.pIdent = pIdent;
		permLevel = level;
		this.banned = 0;
        color = defaultColors[(name.charAt(0) + name.charAt(name.length() - 1)) % defaultColors.length];
	}
	/**
	 * @return the name of the user that this object represents
	 */
	public String getName(){
		return name;
	}
	/**
	 * @return the Plugin Identifier of the plugin to which the user is associated
	 */
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
	public Integer isBanned(){
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
	 * Sets the banned status of a user
	 * @param banned the new banned status
	 */
	public boolean setBannedTime(int milli){
		this.banned = milli;
		return true;
	}
}
