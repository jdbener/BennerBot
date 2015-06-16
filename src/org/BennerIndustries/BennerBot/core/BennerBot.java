package org.BennerIndustries.BennerBot.core;

import java.awt.Color;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.bennerbot;
import org.BennerIndustries.BennerBot.api.datatypes.UserReference;
import org.BennerIndustries.BennerBot.api.datatypes.UserReference.PermissionLevels;
import org.BennerIndustries.BennerBot.core.CorePlugins.DebugLogger;
import org.BennerIndustries.BennerBot.core.Managers.ConfigurationManager;
import org.BennerIndustries.BennerBot.core.Managers.ListenerManager;
import org.BennerIndustries.BennerBot.core.Managers.UserManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BennerBot {
	
	private static Logger logger = LoggerFactory.getLogger(bennerbot.class);
	private static DebugLogger dl = new DebugLogger();
	
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
	public static UserManager users = new UserManager();
	
	public BennerBot(String[] args){
		/* Defines the bot user and adds it to the index */
		users.addUser(new UserReference("BennerBot", PermissionLevels.base, new HashMap<String, Color>()));
		listener.addListener(dl);

		Executors.newScheduledThreadPool(0).scheduleAtFixedRate(new Runnable(){
			@Override
			public void run() {
				try {Thread.sleep(10);} catch (InterruptedException e) {e.printStackTrace();}
			}
		}, 0, 20, TimeUnit.SECONDS);
	}
	public static void log(String message){
		logger.info(message);
	}
}
