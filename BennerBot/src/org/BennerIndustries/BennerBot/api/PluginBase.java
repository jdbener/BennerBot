package org.BennerIndustries.BennerBot.api;

import java.util.ArrayList;
import java.util.Arrays;

import org.BennerIndustries.BennerBot.api.datatypes.MessageReference;
import org.BennerIndustries.BennerBot.api.datatypes.PluginReference;
import org.BennerIndustries.BennerBot.api.datatypes.PluginReference.PluginTags;
import org.BennerIndustries.BennerBot.api.datatypes.UserReference;
import org.BennerIndustries.BennerBot.core.BennerBot;
/**
 * The class that all plugins will be based off of, provides ample integration between the bot and the developer for a hassle free experience.
 * It can also be somewhat bypassed by experienced plugin developers, the only functions that are mandatory are the message and loading functions. 
 * @author Joshua Dahl (Jdbener)
 */
public abstract class PluginBase {
	private PluginReference ref;
	private boolean loaded = false;
	
	/**
	 * This must be included in every plugin as it provides the basic information that the bot needs to process this plugin correctly
	 * @param name the name of the plugin
	 * @param identifier a unique identifier for this plugin
	 * @param description a short description of what the plugin does
	 * @param tags the tags that are associated with this plugin, these are used to determine which plugins to send a targeted message to.
	 */
	public PluginBase(String name, String identifier, String description, PluginTags[] tags){
		ref = new PluginReference(identifier, name, description, tags);
	}
	
	/*
	 * This section provides the functions that are used to get infromation about this plugin
	 */
	
	/**
	 * This function is used by the bot to identify this listener's identifier
	 * @return a unique identifier which can be used to identify this listener from others
	 */
	public String getPluginIdentifier(){
		return ref.getIdentifier();
	}
	/**
	 * @return the name of this plugin
	 */
	public String getPluginName(){
		return ref.getName();
	}
	/**
	 * @return the description of this plugin 
	 */
	public String getPluginDescription(){
		return ref.getDescription();
	}
	/**
	 * @return the tags that this plugin is tagged with, as an array
	 */
	public PluginTags[] getPluginTags(){
		return ref.getTags();
	}
	/**
	 * @return the tags that this plugin is tagged with, as a list
	 */
	public ArrayList<PluginTags> getPluginTags(Boolean useless){
		return new ArrayList<PluginTags>(Arrays.asList(ref.getTags()));
	}
	/**
	 * @return wheather or not the plugin is currently loaded
	 */
	public boolean isLoaded(){
		return loaded;
	}
	
	/*
	 * This section contains references to other classes
	 */
	
	/**
	 * Prints a message to the console, this is used for official messages,
	 * use System.out.println for unofficial messages.
	 * @param message the message to be printed
	 */
	public void log(String message){
		BennerBot.log(message);
	}
	/**
	 * Translates the plugin identifier of a plugin, into its string name 
	 * @param pInit the plugin identifier to search for
	 * @return the name of the plugin, if found
	 */
	public String getListenerName(String pInit){
		return getListenerName(BennerBot.listener.getListenerID(pInit));
	}
	/**
	 * Translates the plugin reference ID of a plugin into its string name
	 * @param ID the plugin reference ID to search for
	 * @return the name of the plugin, if found
	 */
	public String getListenerName(int ID){
		return BennerBot.listener.getListenerName(ID);
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
	
	/*
	 * This section contains all of the abstract functions that plugin developers will have to create for themselves.
	 */
	
	/**
	 * This function will be called upon by the bot whenever it wants to send a message to listener
	 * @param message a reference to the message to be sent
	 * @return this should return weather or not the call was successful
	 */
	public abstract boolean sendMessage(MessageReference message);
	public void trueLoad(){
		loaded = true;
		loadPlugin();
	}
	/**
	 * This function will be called whenever the bot wants to load this plugin,
	 * this should do things like setup the Settings to be used and any other code that needs to be run on bot load.
	 */
	public abstract void loadPlugin();
	public void trueUnload(){
		loaded = false;
		unloadPlugin();
	}
	/**
	 * This function will be called whenever the bot want to unload the plugin,
	 * this will happen when the plugin is disabled or when the bot is shutting down and should remove anything that was added to other storage mechanisms
	 */
	public abstract void unloadPlugin();
}
