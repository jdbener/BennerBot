package org.BennerIndustries.BennerBot.api.datatypes;

public class MessageReference {
	String message, source = "";
	UserReference user;
	
	public MessageReference(String message, UserReference user, String source){
		this.message = message;
		this.user = user;
		this.source = source;
	}
	public MessageReference(String message, UserReference user){
		this.message = message;
		this.user = user;
	}
	public String getMessage(){
		return message;
	}
	public UserReference getUser(){
		return user;
	}
	public String getSourcePlugin(){
		return source;
	}
}
