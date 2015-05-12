package me.jdbener.apis.hitbox;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import me.jdbener.Bennerbot;
import me.jdbener.apis.APIManager;

import org.ho.yaml.Yaml;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

public class HitboxAutoHostingManager {
	String currentlyHosting = null;
	
	@SuppressWarnings("unchecked")
	public HitboxAutoHostingManager(){
		try{
			APIManager.hosttargets = (Map<String, Object>) Yaml.load(new FileInputStream(new File("config/autohost.yml")));
			APIManager.hosttargets.remove("dummy");
		} catch (FileNotFoundException e){
			e.printStackTrace();
		}
		Executors.newScheduledThreadPool(1).scheduleAtFixedRate(new Runnable(){
			@Override
			public void run() {
				if(Bennerbot.getConfigBoolean("enableAutoHoster"))
				try {
					//Stream is online
					if(checkStreamOnline(Bennerbot.getConfigString("hitboxChannel"))){
						unhost(Bennerbot.getConfigString("hitboxChannel"));
						currentlyHosting=null;
					}
					//Override for user selected hosts
					else if(!getHostTarget(Bennerbot.getConfigString("hitboxChannel")).equalsIgnoreCase("") && !getHostTarget(Bennerbot.getConfigString("hitboxChannel")).equalsIgnoreCase(currentlyHosting)){
						currentlyHosting=getHostTarget(Bennerbot.getConfigString("hitboxChannel"));
					}
					//Otherwise host someone from the list
					else {
						if(checkStreamOnline(currentlyHosting))
							Bennerbot.logger.info("Still Hosting "+currentlyHosting);
							//Bennerbot.sendMessage("/host "+currentlyHosting, Bennerbot.getBotIDbyName("twitch"), "");
						else
							for(Entry<String, Object> e: APIManager.hosttargets.entrySet())
								if(e.getValue().toString().equalsIgnoreCase("hitbox"))
									if(checkStreamOnline(e.getKey())){
										currentlyHosting=e.getKey();
										host(Bennerbot.getConfigString("hitboxChannel"), currentlyHosting);
									}
								
					}
				} catch (Exception e) {
					e.printStackTrace();
				}	
			}
		}, 0, 1, TimeUnit.MINUTES);
	}
	private String getHostTarget(String stream){
		try {
			JSONArray channels = (JSONArray) ((JSONObject) APIManager.parser.parse(Bennerbot.StreamToString(new URL("http://www.hitbox.tv/api/media/live/"+stream+"/list").openStream()))).get("livestream");
			JSONObject channel = new JSONObject();
			for(int i = 0; i < channels.size(); i++){
				if(((JSONObject)channels.get(i)).get("media_file").toString().equalsIgnoreCase(Bennerbot.getConfigString("stream"))){
					channel = (JSONObject)channels.get(i);
				}
			}
			return ((JSONObject)((JSONObject) channel.get("media_hosted_media")).get("livestream")).get("media_name").toString();
		} catch (ParseException | IOException e) {e.printStackTrace();}
		return null;
	}
	private boolean checkStreamOnline(String stream){
		try {
			JSONArray channels = (JSONArray) ((JSONObject) APIManager.parser.parse(Bennerbot.StreamToString(new URL("http://www.hitbox.tv/api/media/live/"+stream+"/list").openStream()))).get("livestream");
			JSONObject channel = new JSONObject();
			for(int i = 0; i < channels.size(); i++){
				if(((JSONObject)channels.get(i)).get("media_file").toString().equalsIgnoreCase(Bennerbot.getConfigString("stream"))){
					channel = (JSONObject)channels.get(i);
				}
			}
			try{
				if(channel.get("media_is_live").toString().equalsIgnoreCase("1"))
					return true;
			} catch (Exception e){}
			
		} catch (ParseException | IOException e1) {e1.printStackTrace();}
		return false;
	}
	@SuppressWarnings("unchecked")
	private void unhost(String stream){
		try{
			JSONObject obj = (JSONObject) APIManager.parser.parse(Bennerbot.StreamToString(new URL("http://www.hitbox.tv/api/media/live/"+stream+"/list").openStream()));
			System.out.println(obj.toJSONString());
			String lives = ((JSONArray) obj.get("livestream")).toJSONString();
			lives = lives.replace("[", "");
			lives = lives.replace("]", "");
			
			JSONObject live = new JSONObject();
			
			live.put("media_user_name", stream);
			live.put("media_hosted_id", "null");
			
			lives = "{ \"livestream\":["+live.toJSONString()+"]}";
			
			JSONObject add = (JSONObject) APIManager.parser.parse(lives);
			
			System.out.println();
			System.out.println(add.toJSONString());
			
			String url = "http://www.hitbox.tv/api/media/live/"+stream+"/list?authToken="+APIManager.GetHitboxAuth(Bennerbot.hitboxu, Bennerbot.hitboxpw);
			URL con = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) con.openConnection();
			
			conn.setRequestMethod("PUT");
			conn.setDoOutput(true);

			OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
			out.write(add.toJSONString());
			out.flush();
			} catch (Exception e){
				e.printStackTrace();
			}
	}
	@SuppressWarnings("unchecked")
	private void host(String stream, String target){
		try{
		JSONObject obj = (JSONObject) APIManager.parser.parse(Bennerbot.StreamToString(new URL("http://www.hitbox.tv/api/media/live/"+stream+"/list").openStream()));
		System.out.println(obj.toJSONString());
		String lives = ((JSONArray) obj.get("livestream")).toJSONString();
		lives = lives.replace("[", "");
		lives = lives.replace("]", "");
		
		JSONObject live = new JSONObject();
		
		live.put("media_user_name", stream);
		live.put("media_hosted_id", ((JSONObject)((JSONObject) APIManager.parser.parse(Bennerbot.StreamToString(new URL("http://www.hitbox.tv/api/media/live/"+target+"/list").openStream()))).get("livestream")).get("media_id")).toString();
		
		lives = "{ \"livestream\":["+live.toJSONString()+"]}";
		
		JSONObject add = (JSONObject) APIManager.parser.parse(lives);
		
		System.out.println();
		System.out.println(add.toJSONString());
		
		String url = "http://www.hitbox.tv/api/media/live/"+stream+"/list?authToken="+APIManager.GetHitboxAuth(Bennerbot.hitboxu, Bennerbot.hitboxpw);
		URL con = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) con.openConnection();
		
		conn.setRequestMethod("PUT");
		conn.setDoOutput(true);

		OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
		out.write(add.toJSONString());
		out.flush();
		} catch (Exception e){
			e.printStackTrace();
		}
	}
}
