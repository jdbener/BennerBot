package me.jdbener.lib;

import java.awt.Color;

/*
 * This class is a simple object that stores a username and a color
 */
public class user{
	public String user;
	public Color color;
	
	public user(){
		
	}
	
	public user(String u, Color c){
		user = u;
		color = c;
	}
	public user(String u, String c){
		user = u;
		color = UserColors.hex2Rgb(c);
	}
	
	public String getUser(){
		return user;
	}
	public void setUser(String u){
		user = u;
	}
	public Color getColor(){
		return color;
	}
	public void setColor(Color c){
		color = c;
	}
}

