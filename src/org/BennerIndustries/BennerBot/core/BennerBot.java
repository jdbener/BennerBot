package org.BennerIndustries.BennerBot.core;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.bennerbot;
import org.BennerIndustries.BennerBot.core.CorePlugins.DebugLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BennerBot {
	
	private static Logger logger = LoggerFactory.getLogger(bennerbot.class);	
	
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
		listener.addListener(new DebugLogger());
		
		
		
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
