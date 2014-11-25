package me.jdbener.apis.hitbox;

import java.io.IOException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import me.jdbener.Bennerbot;
import me.jdbener.apis.APIManager;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

public class HitboxFollowerHandeler{
	HFollowerTimer f = new HFollowerTimer();
	public HitboxFollowerHandeler(){
		
		TimerTask t = (TimerTask) f;
		new Timer().schedule(t, 0, 5000);
	}
	
}

class HFollowerTimer extends TimerTask {
	private URL path;
	
	public HFollowerTimer(){
		try {
			path = new URL("http://api.hitbox.tv/followers/user/"+Bennerbot.conf.get("hitboxChannel")+"/?limit=10000000000000000000000");
			
			JSONObject obj = (JSONObject) APIManager.parser.parse(APIManager.StreamToString(path.openStream()));
			JSONArray followers = (JSONArray) obj.get("followers");
			
			for(int i = 0; i < followers.toArray().length; i++){
				JSONObject follower = (JSONObject) followers.get(i);
				String name = (String) follower.get("user_name");
				if(!APIManager.followers.contains(name))
					APIManager.followers.add(name);
				
			}
		} catch (ParseException | IOException e) {
			e.printStackTrace();
		}
		
	}
	public void run(){
		try {
			String list = "";
			JSONObject obj = (JSONObject) APIManager.parser.parse(APIManager.StreamToString(path.openStream()));
			JSONArray followers = (JSONArray) obj.get("followers");
			for(int i=0; i<followers.size(); i++){
				JSONObject follower = (JSONObject) followers.get(i);
				String name = (String) follower.get("user_name");
				if(!APIManager.followers.contains(name)){
					//Bennerbot.sendMessage(Bennerbot.capitalize(name)+" has just followed");
					list = list+", "+Bennerbot.capitalize(name);
					APIManager.followers.add(name);
				}
			}
			if(!list.equalsIgnoreCase("")){
				Bennerbot.sendMessage(list+" have followed on hitbox");
			}
		} catch (IOException | ParseException e1) {
			e1.printStackTrace();
		}
	}
}
