package me.jdbener.moderataion;

import java.util.HashMap;
import java.util.Map;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

import me.jdbener.Bennerbot;

public class FilterManager extends ListenerAdapter<PircBotX>{
	public static Map<String, String> userMap = new HashMap<String, String>();
	public static int warnings = 3, maxWarnings = 20;
	
	@SuppressWarnings("unused")
	public FilterManager(){
		warnings = Integer.parseInt(Bennerbot.conf.get("kickWarnings").toString());
		maxWarnings = Integer.parseInt(Bennerbot.conf.get("banWarnings").toString());
		
		//caps filter
		if(Bennerbot.conf.get("CapsFilter").toString().equalsIgnoreCase("true")){
			Bennerbot.listener.addListener(new CapsFilter());
		}
		
		//url filter
		//if(Bennerbot.conf.get("UrlFilter").toString().equalsIgnoreCase("true")){
		if(false){
			Bennerbot.listener.addListener(new URLFilter());
		}
		
		//general filter
		Bennerbot.listener.addListener(new GeneralFilter());
		
		//commands
		Bennerbot.listener.addListener(this);
	}
	public static void punish(String user){
		if(!user.equalsIgnoreCase("bennerbot")){
			if(!userMap.containsKey(user))
				userMap.put(user, 1+"");
			else
				userMap.put(user, (Integer.parseInt(userMap.get(user))+1)+"");
		
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			if(Integer.parseInt(userMap.get(user)) >= maxWarnings){
				Bennerbot.sendMessage("/ban "+user);
				Bennerbot.sendMessage(Bennerbot.capitalize(user)+" has been BANNED!");
			}else if(Integer.parseInt(userMap.get(user)) >= warnings){
				Bennerbot.sendMessage("/timeout "+user);
				Bennerbot.sendMessage(Bennerbot.capitalize(user)+" has been timedout!");
			} else {
				Bennerbot.sendMessage(Bennerbot.capitalize(user)+" you have: "+(warnings-Integer.parseInt(userMap.get(user)))+" warnings left!");
			}
		}
	}
	
	public void onMessage(MessageEvent<PircBotX> e){
		if(e.getMessage().equalsIgnoreCase("!clearwarnings")){
			if(e.getUser().getChannelsHalfOpIn().contains(e.getChannel()) || e.getUser().getChannelsOpIn().contains(e.getChannel()) || e.getUser().getChannelsOwnerIn().contains(e.getChannel()) || e.getUser().getChannelsSuperOpIn().contains(e.getChannel()) || e.getUser().getChannelsVoiceIn().contains(e.getChannel())){
				FilterManager.userMap = new HashMap<String, String>();
				Bennerbot.sendMessage("User warnings have been cleared!");
			} else {
				Bennerbot.sendMessage(Bennerbot.capitalize(e.getUser().getNick())+" has tried to use a command they don't have permision to use");
				FilterManager.punish(e.getUser().getNick());
			}
		}
		if(e.getMessage().startsWith("!warnings")){
			if(e.getMessage().split(" ").length == 1){
				if(FilterManager.userMap.containsKey(e.getUser().getNick()))
					Bennerbot.sendMessage(Bennerbot.capitalize(e.getUser().getNick())+" you have: "+(FilterManager.warnings-Integer.parseInt(FilterManager.userMap.get(e.getUser().getNick())))+" warnings left!");
				else
					Bennerbot.sendMessage(Bennerbot.capitalize(e.getUser().getNick())+" you have: 5 warnings left!");
			} else {
				if(FilterManager.userMap.containsKey(e.getMessage().split(" ")[1].toLowerCase()))
					Bennerbot.sendMessage(Bennerbot.capitalize(e.getMessage().split(" ")[1])+" has: "+(FilterManager.warnings-Integer.parseInt(FilterManager.userMap.get(e.getMessage().split(" ")[1])))+" warnings left!");
				else
					Bennerbot.sendMessage(Bennerbot.capitalize(e.getMessage().split(" ")[1])+" has: 5 warnings left!");
			}
		}
		
	}
}
