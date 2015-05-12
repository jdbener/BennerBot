package org.BennerIndustries.BennerBot.core;

import org.BennerIndustries.BennerBot.api.PluginBase;
import org.BennerIndustries.BennerBot.api.datatypes.MessageReference;
import org.BennerIndustries.BennerBot.api.datatypes.PluginReference.PluginTags;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BennerBot {
	
	public static Logger logger = LoggerFactory.getLogger(BennerBot.class);	
	
	/**
	 * Starts the bot
	 * @param args command line arguments. The ones avaliable are:
	 * 
	 */
	public static void main (String[] args){
		new BennerBot(args);
	}
	
	public static ListenerManager manager = new ListenerManager();
	
	public BennerBot(String[] args){
		PluginBase a = new PluginBase("Dummy Plugin 1", "random things", new PluginTags[] {PluginTags.ALL}){
			@Override
			public boolean sendMessage(MessageReference message) {
				logger.info("Plugin: "+message.getMessage()+"~"+manager.getListenerID(message.getSourcePlugin()));
				try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
				reciveMessage(message.getMessage(), message.getUser(), message.getSourceChannel());
				return true;
			}
		}, b = new PluginBase("Dummy Interpeter 1", "random interpitation things", new PluginTags[] {PluginTags.ALL}){
			@Override
			public boolean sendMessage(MessageReference message) {
				logger.info("Interpeter: "+message.getMessage()+"~"+manager.getListenerID(message.getSourcePlugin()));
				return true;
			}
		};
		
		manager.addListener(a);
		manager.addListener(b);
		
		a.reciveMessage("this is my message", "jdbener", "jdbener");
		b.reciveMessage("this is a new message", "ziddy", "jdbener");
		
		//System.exit(0);
	}
}
