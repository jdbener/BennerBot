package me.jdbener.utilities;

import java.util.Map;
import java.util.Random;

import me.jdbener.Bennerbot;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

public class Infromation extends ListenerAdapter<PircBotX> {
	public void onMessage(MessageEvent<PircBotX> e){
		if(e.getMessage().equalsIgnoreCase("!purpose"))
			Bennerbot.sendMessage("I was designed by my creater, Jdbener, to be the first and only chat bot deisgned to manage both twitch and hitbox channels, i also have a display that allows you to view said chats in one centrefeid location.");
		
		if(e.getMessage().equalsIgnoreCase("!test")){
			if(e.getUser().getNick().equalsIgnoreCase("jdbener") || e.getUser().getNick().equalsIgnoreCase(Bennerbot.twitchu) || e.getUser().getNick().equalsIgnoreCase(Bennerbot.hitboxu) || e.getUser().getNick().equalsIgnoreCase(Bennerbot.conf.get("twitchChannel").toString()) ||  e.getUser().getNick().equalsIgnoreCase(Bennerbot.conf.get("hitboxChannel").toString())){
				try{
					Bennerbot.sendMessage("Starting test event");
					Thread.sleep(3000);
					Bennerbot.sendMessage(Bennerbot.name+" "+Bennerbot.version);
					//Thread.sleep(3000);
					//Bennerbot.sendMessage(Bennerbot.manager.getBots().toString());
					//Thread.sleep(3000);
					//Bennerbot.sendMessage(Bennerbot.plugins.toString());
					Thread.sleep(3000);
					Map<String, Object> temp = Bennerbot.conf;
					temp.remove("hitboxPassword");
					temp.remove("twitchOauth");
					Bennerbot.sendMessage(temp.toString());
					Thread.sleep(3600);
					Bennerbot.sendMessage("Twitch: "+Bennerbot.conf.get("twitchChannel").toString()+" Hitbox: "+Bennerbot.conf.get("hitboxChannel").toString());
				} catch(InterruptedException ex){
					ex.printStackTrace();
				}
			} else {
				Bennerbot.sendMessage("Sorry this command is for developers only");
			}
		}
		
		if(e.getMessage().startsWith("!roll")){
			if(e.getMessage().split(" ").length == 2){
				Random rng = new Random();
				int num = rng.nextInt(Integer.parseInt(e.getMessage().split(" ")[1])+1);
				Bennerbot.sendMessage(Bennerbot.capitalize(e.getUser().getNick())+" rolled a "+num+"!");
			} else {
				Random rng = new Random();
				int num = rng.nextInt(100);
				Bennerbot.sendMessage(Bennerbot.capitalize(e.getUser().getNick())+" rolled a "+num+"!");
			}
		}
	}
}
