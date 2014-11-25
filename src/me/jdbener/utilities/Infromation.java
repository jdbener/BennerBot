package me.jdbener.utilities;

import me.jdbener.Bennerbot;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

public class Infromation extends ListenerAdapter<PircBotX> {
	public void onMessage(MessageEvent<PircBotX> e){
		if(e.getMessage().equalsIgnoreCase("!purpose"))
			Bennerbot.sendMessage("I was designed by my creater, Jdbener, to be the first and only chat bot deisgned to manage both twitch and hitbox channels, i also have a display that allows you to view said chats in one centrefeid location.");
		
		if(e.getMessage().equalsIgnoreCase("!test")){
			if(e.getUser().getNick().equalsIgnoreCase("jdbener") || e.getUser().getNick().equalsIgnoreCase(Bennerbot.conf.get("twitchChannel").toString()) ||  e.getUser().getNick().equalsIgnoreCase(Bennerbot.conf.get("hitboxChannel").toString())){
				try{
					Bennerbot.sendMessage("Starting test event");
					Thread.sleep(3000);
					Bennerbot.sendMessage(Bennerbot.class.toString());
					Thread.sleep(3000);
					Bennerbot.sendMessage(Bennerbot.gui.toString());
					Thread.sleep(3000);
					Bennerbot.sendMessage(Bennerbot.manager.toString());
					Thread.sleep(3000);
					Bennerbot.sendMessage(Bennerbot.plugins.toString());
					Thread.sleep(3000);
					Bennerbot.sendMessage("Twitch: "+Bennerbot.conf.get("twitchChannel").toString()+" Hitbox: "+Bennerbot.conf.get("hitboxChannel").toString());
				} catch(InterruptedException ex){
					ex.printStackTrace();
				}
			} else {
				Bennerbot.sendMessage("Sorry this command is for developers only");
			}
		}
		
	}
}
