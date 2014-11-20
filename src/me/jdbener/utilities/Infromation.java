package me.jdbener.utilities;

import java.util.HashMap;

import me.jdbener.Bennerbot;
import me.jdbener.moderataion.FilterManager;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

public class Infromation extends ListenerAdapter<PircBotX> {
	public void onMessage(MessageEvent<PircBotX> e){
		if(e.getMessage().equalsIgnoreCase("!purpose"))
			e.getChannel().send().message("I was designed by my creater, Jdbener, to be the first and only chat bot deisgned to manage both twitch and hitbox channels, i also have a display that allows you to view said chats in one centrefeid location.");
		
		if(e.getMessage().equalsIgnoreCase("!clearwarnings")){
			if(e.getUser().getChannelsHalfOpIn().contains(e.getChannel()) || e.getUser().getChannelsOpIn().contains(e.getChannel()) || e.getUser().getChannelsOwnerIn().contains(e.getChannel()) || e.getUser().getChannelsSuperOpIn().contains(e.getChannel()) || e.getUser().getChannelsVoiceIn().contains(e.getChannel())){
				FilterManager.userMap = new HashMap<String, String>();
				e.getChannel().send().message("User warnings have been cleared!");
			} else {
				e.getChannel().send().message(Bennerbot.capitalize(e.getUser().getNick())+" has tried to use a command they don't have permision to use");
				FilterManager.punish(e.getUser().getNick());
			}
		}
		
		if(e.getMessage().equalsIgnoreCase("!warnings")){
			if(FilterManager.userMap.containsKey(e.getUser().getNick()))
				e.getChannel().send().message(Bennerbot.capitalize(e.getUser().getNick())+" you have: "+(FilterManager.warnings-Integer.parseInt(FilterManager.userMap.get(e.getUser().getNick())))+" warnings left!");
			else
				e.getChannel().send().message(Bennerbot.capitalize(e.getUser().getNick())+" you have: 5 warnings left!");
		}
	}
}
