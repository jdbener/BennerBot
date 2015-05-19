package org.BennerIndustries.BennerBot.core;

import java.awt.Color;

import org.BennerIndustries.BennerBot.api.PluginBase;
import org.BennerIndustries.BennerBot.api.datatypes.MessageReference;
import org.BennerIndustries.BennerBot.api.datatypes.PluginReference.PluginTags;
import org.BennerIndustries.BennerBot.api.datatypes.UserReference;
import org.BennerIndustries.BennerBot.api.datatypes.UserReference.PermissionLevels;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BennerBot {
	
	public static Logger logger = LoggerFactory.getLogger(BennerBot.class);	
	
	/**
	 * Starts the bot
	 * @param args command line arguments. The ones available are:
	 * 
	 */ 
	public static void main (String[] args){
		new BennerBot(args);
	}
	
	public static ListenerManager listener = new ListenerManager();
	public static ConfigurationManager configuration = new ConfigurationManager();
	
	public BennerBot(String[] args){
		PluginBase a = new PluginBase("Dummy Plugin 1", "dp1", "random things", new PluginTags[] {PluginTags.ALL}){
			@Override
			public boolean sendMessage(MessageReference message) {
				logger.info("Plugin: "+message.getMessage()+"~"+message.getSourcePlugin());
				try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
				reciveMessage(message.getMessage(), message.getUser());
				return true;
			}
		}, b = new PluginBase("Dummy Interpeter 1", "di1", "random interpitation things", new PluginTags[] {PluginTags.ALL}){
			@Override
			public boolean sendMessage(MessageReference message) {
				logger.info("Interpeter: "+message.getMessage()+"~"+message.getSourcePlugin());
				return true;
			}
		};
		
		listener.addListener(a);
		listener.addListener(b);
		
		a.reciveMessage("this is my message", new UserReference("jdbener", Color.red, "jdbener", PermissionLevels.admin, a.getPluginIdentifier()));
		b.reciveMessage("this is a new message", new UserReference("zidnar", Color.black, "jdbener", PermissionLevels.regular, b.getPluginIdentifier()));
		
		//System.exit(0);
	}
}
