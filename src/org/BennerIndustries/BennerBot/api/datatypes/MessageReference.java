package org.BennerIndustries.BennerBot.api.datatypes;

public class MessageReference {
	String message, user, channel, source = "";
	
	public MessageReference(String message, String user, String channel, String source){
		this.message = message;
		this.user = user;
		this.channel = channel;
		this.source = source;
	}
	public MessageReference(String message, String user, String channel){
		this.message = message;
		this.user = user;
		this.channel = channel;
	}
	public String getMessage(){
		return message;
	}
	public String getUser(){
		return user;
	}
	public String getSourceChannel(){
		return channel;
	}
	public String getSourcePlugin(){
		return source;
	}
}
