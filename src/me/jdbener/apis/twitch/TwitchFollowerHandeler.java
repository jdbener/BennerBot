package me.jdbener.apis.twitch;

import java.io.IOException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import me.jdbener.Bennerbot;
import me.jdbener.apis.APIManager;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

public class TwitchFollowerHandeler{
	public TwitchFollowerHandeler(){
		TFollowerTimer f = new TFollowerTimer();
		TimerTask t = (TimerTask) f;
		new Timer().schedule(t, 0, 5000);
	}
	
}

class TFollowerTimer extends TimerTask {
	private URL path;
	
	public TFollowerTimer(){
		try {
			path = new URL("https://api.twitch.tv/kraken/channels/"+Bennerbot.getConfigString("twitchChannel")+"/follows/?limit=1000000");
			
			JSONObject obj = (JSONObject) APIManager.parser.parse(Bennerbot.StreamToString(path.openStream()));
			JSONArray followers = (JSONArray) obj.get("follows");
			
			for(int i = 0; i < followers.toArray().length; i++){
				JSONObject follower = (JSONObject) followers.get(i);
				JSONObject user = (JSONObject) follower.get("user");
				if(!APIManager.followers.contains((String) user.get("name")))
					APIManager.followers.add((String) user.get("name"));
				
			}
		} catch (ParseException | IOException e) {}
		
	}
	public void run(){
		try {
			String list = "";
			JSONObject obj = (JSONObject) APIManager.parser.parse(Bennerbot.StreamToString(path.openStream()));
			JSONArray followers = (JSONArray) obj.get("follows");
			for(int i=0; i<((JSONObject) followers.get(0)).size(); i++){
				JSONObject follower = (JSONObject) followers.get(i);
				JSONObject user = (JSONObject) follower.get("user");
				String name = (String) user.get("name");
				if(!APIManager.followers.contains((String) user.get("name"))){
					//Bennerbot.sendMessage(Bennerbot.capitalize(name)+" has just followed");
					list = list+Bennerbot.capitalize(name)+", ";
					APIManager.followers.add(name);
				}
			}
			if(!list.equalsIgnoreCase("")){
				if(list.split(",").length>2)
					Bennerbot.sendMessage(list+" have followed on twitch");
				else
					Bennerbot.sendMessage(list+" has followed on twitch");
			}
		} catch (IOException | ParseException e1) {}
	}
}
