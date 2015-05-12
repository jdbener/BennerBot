package org.BennerIndustries.BennerBot.core;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.BennerIndustries.BennerBot.api.PluginBase;
import org.BennerIndustries.BennerBot.api.datatypes.MessageReference;
import org.BennerIndustries.BennerBot.api.datatypes.PluginReference.PluginTags;

public class ListenerManager {
	private ArrayList<PluginBase> listeners = new ArrayList<PluginBase>();
	
	public ListenerManager(){
		Executors.newScheduledThreadPool(0).scheduleAtFixedRate(new Runnable(){
			@Override
			public void run() {
				try {Thread.sleep(10);} catch (InterruptedException e) {e.printStackTrace();}
			}
		}, 0, 20, TimeUnit.SECONDS);
	}
	/**
	 * Adds a plugin/listener to the index
	 * @param listener the plugin/listener to be added
	 * @return weather or not the addition was successful
	 */
	public boolean addListener(PluginBase listener) {
		return listeners.add(listener);
	}
	public boolean removeListener(String lIdent){
		for(int i = 0; i < listeners.size(); i++){
			if(listeners.get(i).getPluginIdentifier().equals(lIdent)){
				listeners.remove(i);
				return true;
			}
		}
		return false;
	}
	public boolean removeListener(int lIdent){
		if(lIdent > listeners.size())
			return false;
		if(lIdent < 0)
			return false;
		
		listeners.remove(lIdent);
		return true;
	}
	public boolean hasListener(String lIdent){
		for(int i = 0; i < listeners.size(); i++){
			if(listeners.get(i).getPluginIdentifier().equals(lIdent)){
				return true;
			}
		}
		return false;
	}
	public int getListenerID(String lIdent){
		for(int i = 0; i < listeners.size(); i++){
			if(listeners.get(i).getPluginIdentifier().equals(lIdent)){
				return i;
			}
		}
		return -1;
	}
	public boolean sendMessage(String lIdent, MessageReference reference){
		for(int i = 0; i < listeners.size(); i++){
			if(listeners.get(i).getPluginIdentifier().equals(lIdent)){
				return listeners.get(i).sendMessage(reference);
			}
		}
		return false;
	}
	public boolean sendMessage(int lIdent, MessageReference reference){
		if(lIdent > listeners.size())
			return false;
		if(lIdent < 0)
			return false;
		
		return listeners.get(lIdent).sendMessage(reference);
	}
	public boolean sendMessage(MessageReference reference){
		for(int i = 0; i < listeners.size(); i++){
			if(!sendMessage(i, reference)){
				return false;
			}
		}
		return true;
	}
	public boolean sendConncetion(String lIdent, String channel){
		for(int i = 0; i < listeners.size(); i++){
			if(listeners.get(i).getPluginIdentifier().equals(lIdent)){
				return listeners.get(i).sendConnction(channel);
			}
		}
		return false;
	}
	public boolean sendConnection(int lIdent, String channel){
		if(lIdent > listeners.size())
			return false;
		if(lIdent < 0)
			return false;
		
		return listeners.get(lIdent).sendConnction(channel);
	}
	public boolean sendDisconnect(String lIdent){
		for(int i = 0; i < listeners.size(); i++){
			if(listeners.get(i).getPluginIdentifier().equals(lIdent)){
				return listeners.get(i).sendDisconect();
			}
		}
		return false;
	}
	public boolean sendDisconnect(int lIdent){
		if(lIdent > listeners.size())
			return false;
		if(lIdent < 0)
			return false;
		
		return listeners.get(lIdent).sendDisconect();
	}
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
				
		//Generates a list of targets
		//ArrayList<String> targets = new ArrayList<String>(Arrays.asList(target.replace(" ", "").split(",")));
					
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
