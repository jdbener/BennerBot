package org.BennerIndustries.BennerBot.api;

import java.util.ArrayList;
import java.util.Arrays;

import org.BennerIndustries.BennerBot.api.datatypes.MessageReference;
import org.BennerIndustries.BennerBot.api.datatypes.PluginReference;
import org.BennerIndustries.BennerBot.api.datatypes.PluginReference.PluginTags;
import org.BennerIndustries.BennerBot.api.datatypes.UserReference;
import org.BennerIndustries.BennerBot.core.BennerBot;

public abstract class PluginBase {
	private PluginReference ref;
	
	public PluginBase(String name, String identifier, String description, PluginTags[] tags){
		ref = new PluginReference(identifier, name, description, tags);
	}
	
	/**
	 * This function is used by the bot to identify this listener's identifier
	 * @return a unique identifier which can be used to identify this listener from others
	 */
	public String getPluginIdentifier(){
		return ref.getIdentifier();
	}
	public String getPluginName(){
		return ref.getName();
	}
	public PluginTags[] getPluginTags(){
		return ref.getTags();
	}
	public ArrayList<PluginTags> getPluginTags(Boolean useless){
		return new ArrayList<PluginTags>(Arrays.asList(ref.getTags()));
	}
	
	/**
	 * This function is called by the plugin developer whenever there is a message that must be sent to the bot.
	 * @param message the message to be processed
	 * @param user the user that sent the message
	 * @param channel the channel the message is sent too
	 * @return weather or not the message was successfully sent.
	 */
	public boolean reciveMessage(String message, UserReference user){
		if(BennerBot.listener.hasListener(getPluginIdentifier())){
			BennerBot.listener.receiveMessage(getPluginIdentifier(), new MessageReference(message, user, getPluginIdentifier()));
			return true;
		}
		return false;
	}
	/**
	 * This function is called by the plugin developer whenever there is a message that must be sent to the bot.
	 * @param target the tag to use, i recommend using the tags in org.BennerIndustries.BennerBot.api.datatypes.PluginTags but it is not mandated.
	 * @param message the message to be processed
	 * @param user the user that sent the message
	 * @param channel the channel the message is sent too
	 * @return weather or not the message was successfully sent.
	 */
	public boolean reciveTargetedMessage(PluginTags[] target, String message, UserReference user){
		if(BennerBot.listener.hasListener(getPluginIdentifier()))
			BennerBot.listener.reciveTargetedMessage(target, getPluginIdentifier(), new MessageReference(message, user, getPluginIdentifier()));
		return false;
	}
	/**
	 * This function is called by the plugin developer whenever there is a message that must be sent to the bot.
	 * @param target the tag to use, i recommend using the tags in org.BennerIndustries.BennerBot.api.datatypes.PluginTags but it is not mandated.
	 * @param message the message to be processed
	 * @param user the user that sent the message
	 * @param channel the channel the message is sent too
	 * @return weather or not the message was successfully sent.
	 */
	public boolean reciveTargetedMessage(PluginTags target, String message, UserReference user){
		if(BennerBot.listener.hasListener(getPluginIdentifier()))
			BennerBot.listener.reciveTargetedMessage(new PluginTags[] {target}, getPluginIdentifier(), new MessageReference(message, user, getPluginIdentifier()));
		return false;
	}
	/**
	 * This function will be called upon by the bot whenever it wants to send a message to listener
	 * @param message a reference to the message to be sent
	 * @return this should return weather or not the call was successful
	 */
	public abstract boolean sendMessage(MessageReference message);
	/**
	 * This function will be called by the bot whenever it wants to connect to a new channel on the server.
	 * It is a decided fact that the bot will only try to connect too one server at a time, therefore this is should override any existing connections
	 * @param channel the channel that the bot wants to connect too
	 * @return this should return weather or not the call was successful
	 */
	public boolean sendConnction(String channel) {
		return false;
	}
	/**
	 * This function will be called by the bot whenever it wants to disconnect from a channel
	 * @return this should return weather or not the call was successful
	 */
	public boolean sendDisconect(){
		return false;
	}
}
