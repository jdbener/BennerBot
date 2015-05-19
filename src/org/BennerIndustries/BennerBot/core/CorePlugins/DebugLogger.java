package org.BennerIndustries.BennerBot.core.CorePlugins;

import org.BennerIndustries.BennerBot.api.PluginBase;
import org.BennerIndustries.BennerBot.api.datatypes.MessageReference;
import org.BennerIndustries.BennerBot.api.datatypes.PluginReference.PluginTags;
import org.BennerIndustries.BennerBot.core.BennerBot;
/**
 * A simple class that does basic printing and log file saving for the bot
 * @author Joshua Dahl (Jdbener)
 */
public class DebugLogger extends PluginBase{
	
	public DebugLogger() {
		super("BennerBot Logger", "BBL", "A small utility logger that prints and saves everything that happens", new PluginTags[]{PluginTags.ALL});
	}

	@Override
	public boolean sendMessage(MessageReference message) {
		log(message.getMessage()+" was sent from, \'"+BennerBot.listener.getListenerName(BennerBot.listener.getListenerID(message.getSourcePlugin()))
				+"\'/"+message.getSourcePlugin()+" by \'"+message.getUser().getName()+"\'");
		return true;
	}

	@Override
	public void loadPlugin() {
		//do nothing
	}

	@Override
	public void unloadPlugin() {
		//do nothing
	}

}
