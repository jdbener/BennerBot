package org.BennerIndustries.BennerBot.api.datatypes;

import java.awt.Color;

public class UserReference {
	public enum PERMISSION_LEVELS {base, regular, regularPlus, moderator, admin};
	
	private PERMISSION_LEVELS permLevel;
	private String name;
	private Color color;
}
