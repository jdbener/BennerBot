package org.BennerIndustries.BennerBot.core.Managers;

import java.util.ArrayList;

import org.BennerIndustries.BennerBot.api.PluginBase;
import org.BennerIndustries.BennerBot.api.datatypes.MessageReference;
import org.BennerIndustries.BennerBot.api.datatypes.PluginReference.PluginTags;

/**
 * This class keeps track of the currently loaded plugins, as well as the interactions between them.
 * @author Joshua Dahl (Jdbener)
 */
public class ListenerManager {
	private ArrayList<PluginBase> listeners = new ArrayList<PluginBase>();
	
	/**
	 * Adds a plugin/listener to the index
	 * @param listener the plugin/listener to be added
	 */
	public boolean addListener(PluginBase listener) {
		listener.trueLoad();
		return listeners.add(listener);
	}
	/**
	 * Removes a plugin/listener from the index
	 * @param lIdent the identifier of the plugin/listener to be removed
	 */
	public boolean removeListener(String lIdent){
		for(int i = 0; i < listeners.size(); i++){
			if(listeners.get(i).getPluginIdentifier().equals(lIdent)){
				listeners.get(i).trueUnload();
				listeners.remove(i);
				return true;
			}
		}
		return false;
	}
	/**
	 * Removes a plugin/listener from the index
	 * @param lIdent the index ID of the plugin/listener to be removed
	 */
	public boolean removeListener(int lIdent){
		if(lIdent > listeners.size())
			return false;
		if(lIdent < 0)
			return false;
		
		listeners.get(lIdent).trueUnload();
		listeners.remove(lIdent);
		return true;
	}
	/**
	 * Checks if the index has the specified plugin/listener
	 * @param lIdent the identifier of the plugin/listener to check for
	 * @return weather or not the plugin/listener is in the index
	 */
	public boolean hasListener(String lIdent){
		for(int i = 0; i < listeners.size(); i++){
			if(listeners.get(i).getPluginIdentifier().equals(lIdent)){
				return true;
			}
		}
		return false;
	}
	/**
	 * Changes the identifier of a plugin/listener into an index ID
	 * @param lIdent the identifier of the plugin/listener to search for
	 * @return the index ID of the plugin/listener (if in the index, otherwise -1)
	 */
	public int getListenerID(String lIdent){
		for(int i = 0; i < listeners.size(); i++){
			if(listeners.get(i).getPluginIdentifier().equals(lIdent)){
				return i;
			}
		}
		return -1;
	}
	/**
	 * Fetches the String name of a plugin/listener based on its index ID
	 * @param lIdent the index Id of the plugin/listener to search for
	 * @return the String name of the plugin/listener (or 'blank' if the index is not found)
	 */
	public String getListenerName(int lIdent){
		if(lIdent > listeners.size())
			return "";
		if(lIdent < 0)
			return "";
		
		return listeners.get(lIdent).getPluginName();
	}
	/**
	 * This function will send a message reference to a specific plugin/listener
	 * @param lIdent the identifier of the targeted plugin/listener
	 * @param reference the message reference
	 */
	public boolean sendMessage(String lIdent, MessageReference reference){
		for(int i = 0; i < listeners.size(); i++){
			if(listeners.get(i).getPluginIdentifier().equals(lIdent)){
				return listeners.get(i).sendMessage(reference);
			}
		}
		return false;
	}
	/**
	 * This function will send a message reference to a specific plugin/listener
	 * @param lIdent the index ID of the targeted plugin/listener
	 * @param reference the message reference
	 */
	public boolean sendMessage(int lIdent, MessageReference reference){
		if(lIdent > listeners.size())
			return false;
		if(lIdent < 0)
			return false;
		
		return listeners.get(lIdent).sendMessage(reference);
	}
	/**
	 * this function will send a message reference to every plugin/listener
	 * @param reference the message reference
	 */
	public boolean sendMessage(MessageReference reference){
		for(int i = 0; i < listeners.size(); i++){
			if(!sendMessage(i, reference)){
				return false;
			}
		}
		return true;
	}
	/**
	 * This function processes an incoming message from a plugin/listener and sends it to every other plugin/listener
	 * @param lIdent the identifier of the plugin/listener which sent the message
	 * @param reference the message reference itself
	 */
	public boolean receiveMessage(final String lIdent, final MessageReference reference){
		//These two blocks of code check if the Listener Identifier passed represents a listener in the system or not.
		//If not then it will stop the function and return an error
		boolean has = false;
		for(PluginBase l: listeners){
			if(l.getPluginIdentifier().equals(lIdent)){
				has = true;
				break;
			}
		}
		if(has == false)
			return false;
			
		//This block of code sends a copy of the message to every other listener/plugin so that it can be processed by all of them accordingly
		for(int i = 0; i < listeners.size(); i++){
			if(!listeners.get(i).getPluginIdentifier().equals(lIdent)){
				if(!sendMessage(i, reference))
					return false;
			}
		}
		return true;
	}
	/**
	 * This function processes an incomeing message from a plugin/listener and sends it to a specific group of plugins/listeners
	 * @param target the target group
	 * @param lIdent the identifier of the plugin/listener which sent the message
	 * @param reference the message reference itself
	 */
	public boolean reciveTargetedMessage(final PluginTags[] target, final String lIdent, final MessageReference reference){
		//These two blocks of code check if the Listener Identifier passed represents a listener in the system or not.
		//If not then it will stop the function and return an error
		boolean has = false;
		for(PluginBase l: listeners){
			if(l.getPluginIdentifier().equals(lIdent)){
				has = true;
				break;
			}
		}
		if(has == false)
			return false;
					
		//This block of code sends a copy of the message to every other listener/plugin so that it can be processed by all of them accordingly
		for(int i = 0; i < listeners.size(); i++)
			if(!listeners.get(i).getPluginIdentifier().equals(lIdent))
				for(PluginTags tag: listeners.get(i).getPluginTags())
					for(PluginTags tar: target)
						if(tag.equals(tar) || tar.equals(PluginTags.ALL))
							if(!sendMessage(i, reference))
								return false;
							else
								break;
		return true;
	}
}
