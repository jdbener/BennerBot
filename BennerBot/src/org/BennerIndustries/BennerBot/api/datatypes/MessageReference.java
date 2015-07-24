package org.BennerIndustries.BennerBot.api.datatypes;

/**
 * This class defines a reference to a message for cross plugin communication
 * @author Joshua Dahl (Jdbener)
 */
public class MessageReference {
	private String message, source = "";
	private UserReference user;
	/**
	 * @param message the message to be sent
	 * @param user a user reference object associating this message with a particular person
	 * @param source the source plugin of this message, this is useful for backtracking errors
	 */
	public MessageReference(String message, UserReference user, String source){
		this.message = message;
		this.user = user;
		this.source = source;
	}
	/**
	 * @param message the message to be sent
	 * @param user a user reference object associating this message with a particular person
	 */
	public MessageReference(String message, UserReference user){
		this.message = message;
		this.user = user;
	}
	/**
	 * @return the message associated with this reference
	 */
	public String getMessage(){
		return message;
	}
	/**
	 * @return the message associated with this reference
	 */
	public UserReference getUser(){
		return user;
	}
	/**
	 * @return the source plugin (if specified) that this plugin is from
	 */
	public String getSourcePlugin(){
		return source;
	}
}
