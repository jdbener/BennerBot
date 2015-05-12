package me.jdbener.moderataion;

import me.jdbener.Bennerbot;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

public class GeneralFilter extends ListenerAdapter<PircBotX>{
	int MaxMessageLength = 150;
	public void onMessage(MessageEvent<PircBotX> e){
		MaxMessageLength = Integer.parseInt(Bennerbot.getConfigString("MaxMessageLength"));
		boolean filter = false;
		
		if(e.getMessage().length() > MaxMessageLength && filter == false && Bennerbot.getConfigBoolean("LengthFilter")){
			Bennerbot.sendMessage(Bennerbot.capitalize(e.getUser().getNick())+" has sent a message that is longer than "+MaxMessageLength+" charecters long!", e.getBot().getBotId());
			FilterManager.punish(e.getUser().getNick());
			filter = true;
		}

	}
}
