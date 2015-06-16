package org.BennerIndustries.BennerBot.core.CorePlugins;

import java.util.Scanner;

import org.BennerIndustries.BennerBot.api.PluginBase;
import org.BennerIndustries.BennerBot.api.datatypes.MessageReference;
import org.BennerIndustries.BennerBot.api.datatypes.PluginReference.PluginTags;
import org.BennerIndustries.BennerBot.core.BennerBot;
/**
 * A simple class that does basic printing and log file saving for the bot
 * @author Joshua Dahl (Jdbener)
 */
public class DebugLogger extends PluginBase{
	
	private Scanner scan = new Scanner(System.in);
	private Thread thread = new Thread(new Runnable(){
		@Override
		public void run() {
			while(scan.hasNext()){
				String line = scan.nextLine();
				if(isLoaded()){
					log("\'"+line+"\' was recivied from the \'console\'");
					reciveTargetedMessage(new PluginTags[]{PluginTags.FILTER, PluginTags.UI, PluginTags.UTILITY, PluginTags.UNTAGGED}, line, BennerBot.users.getReference("BennerBot"));
				}
			}
		}
	});
	
	public DebugLogger() {
		super("BennerBot Logger", "DebugLoggerBBBASE", "A small utility logger that prints and saves everything that happens", new PluginTags[]{PluginTags.ALL});
	}

	@Override
	public boolean sendMessage(MessageReference message) {
		log("\'"+message.getMessage()+"\' was sent from, \'"+BennerBot.listener.getListenerName(BennerBot.listener.getListenerID(message.getSourcePlugin()))
				+"\'/"+message.getSourcePlugin()+" by \'"+message.getUser().getName()+"\'");
		return true;
	}

	@Override
	public void loadPlugin() {
		thread.start();
	}

	@Override
	public void unloadPlugin() {
	}

}
