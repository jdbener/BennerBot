package me.jdbener.moderataion;

import java.util.HashMap;
import java.util.Map;

import me.jdbener.Bennerbot;

public class FilterManager{
	public static Map<String, String> userMap = new HashMap<String, String>();
	public static int warnings = 3, maxWarnings = 20;
	
	public FilterManager(){
		warnings = Integer.parseInt(Bennerbot.conf.get("kickWarnings").toString());
		maxWarnings = Integer.parseInt(Bennerbot.conf.get("banWarnings").toString());
		
		//caps filter
		if(Bennerbot.conf.get("CapsFilter").toString().equalsIgnoreCase("true")){
			Bennerbot.twitch.addListener(new CapsFilter());
			Bennerbot.hitbox.addListener(new CapsFilter());
		}
		
		if(Bennerbot.conf.get("UrlFilter").toString().equalsIgnoreCase("true")){
			Bennerbot.twitch.addListener(new URLFilter());
			Bennerbot.hitbox.addListener(new URLFilter());
		}
		
		/*if(Bennerbot.conf.get("BadWordFilter").toString().equalsIgnoreCase("true")){
			Bennerbot.twitch.addListener(new BadWordsFilter());
			Bennerbot.hitbox.addListener(new BadWordsFilter());
		}*/
		
		Bennerbot.twitch.addListener(new GeneralFilter());
		Bennerbot.hitbox.addListener(new GeneralFilter());
	}
	public static void punish(String user){
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
