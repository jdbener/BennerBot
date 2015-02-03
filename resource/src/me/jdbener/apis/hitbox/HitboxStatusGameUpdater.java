package me.jdbener.apis.hitbox;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import me.jdbener.Bennerbot;
import me.jdbener.apis.APIManager;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

public class HitboxStatusGameUpdater extends ListenerAdapter<PircBotX> {
	@SuppressWarnings("unchecked")
	public void onMessage(MessageEvent<PircBotX> e){
		boolean ismod = e.getUser().getChannelsHalfOpIn().contains(e.getChannel()) || e.getUser().getChannelsOpIn().contains(e.getChannel()) || e.getUser().getChannelsOwnerIn().contains(e.getChannel()) || e.getUser().getChannelsSuperOpIn().contains(e.getChannel()) || e.getUser().getChannelsVoiceIn().contains(e.getChannel());
		
		if(e.getMessage().startsWith("!title"))
			if(ismod){
				try{
					JSONObject obj = getJSON();
					System.out.println(obj.toJSONString());
					String lives = ((JSONArray) obj.get("livestream")).toJSONString();
					lives = lives.replace("[", "");
					lives = lives.replace("]", "");
					
					JSONObject live = new JSONObject();
					
					String title = "";
					
					for(int i = 1; i<e.getMessage().split(" ").length; i++)
						title+=e.getMessage().split(" ")[i]+" ";
					
					live.put("media_user_name", Bennerbot.conf.get("hitboxChannel"));
					live.put("media_status", title);
					
					
					lives = "{ \"livestream\":["+live.toJSONString()+"]}";
					
					JSONObject add = (JSONObject) APIManager.parser.parse(lives);
					
					System.out.println();
					System.out.println(add.toJSONString());
					
					String url = "http://www.hitbox.tv/api/media/live/"+Bennerbot.conf.get("hitboxChannel")+"/list?authToken="+APIManager.GetHitboxAuth("jdbener", "il0venV!");
					URL con = new URL(url);
					HttpURLConnection conn = (HttpURLConnection) con.openConnection();
					
					conn.setRequestMethod("PUT");
					conn.setDoOutput(true);
					
					//String data = "login="+user+"&pass="+pass+"&app=desktop";
		
					OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
					out.write(add.toJSONString());
					out.flush();
		
					//JSONObject jobj = (JSONObject) APIManager.parser.parse(APIManager.StreamToString(conn.getInputStream()));
		
					System.out.println(conn.getResponseCode());
					
					Bennerbot.sendMessage("Stream's title was set to: "+title, 1);
		
				}catch(ParseException | IOException ex){
					ex.printStackTrace();
				}
			} else {
				//Bennerbot.sendMessage(Bennerbot.capitalize(e.getUser().getNick())+" has just tried to use a command they dont have permision to", 1);
				//FilterManager.punish(e.getUser().getNick());
			}
		if(e.getMessage().startsWith("!game"))
			if(ismod){
				try{
					JSONObject obj = getJSON();
					System.out.println(obj.toJSONString());
					String lives = ((JSONArray) obj.get("livestream")).toJSONString();
					lives = lives.replace("[", "");
					lives = lives.replace("]", "");
					
					JSONObject live = new JSONObject();
					
					String game = "";
					
					for(int i = 1; i<e.getMessage().split(" ").length; i++)
						game+=e.getMessage().split(" ")[i]+" ";
					
					live.put("media_user_name", Bennerbot.conf.get("hitboxChannel"));
					live.put("category_name", game);
					
					
					lives = "{ \"livestream\":["+live.toJSONString()+"]}";
					
					JSONObject add = (JSONObject) APIManager.parser.parse(lives);
					
					System.out.println();
					System.out.println(add.toJSONString());
					
					String url = "http://www.hitbox.tv/api/media/live/"+Bennerbot.conf.get("hitboxChannel")+"/list?authToken="+APIManager.GetHitboxAuth(Bennerbot.hitboxu, Bennerbot.hitboxpw);
					URL con = new URL(url);
					HttpURLConnection conn = (HttpURLConnection) con.openConnection();
					
					conn.setRequestMethod("PUT");
					conn.setDoOutput(true);
					
					//String data = "login="+user+"&pass="+pass+"&app=desktop";
		
					OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
					out.write(add.toJSONString());
					out.flush();
		
					//JSONObject jobj = (JSONObject) APIManager.parser.parse(APIManager.StreamToString(conn.getInputStream()));
		
					System.out.println(conn.getResponseCode());
					
					Bennerbot.sendMessage("Stream's game was set to: "+game, 1);
		
				}catch(ParseException | IOException ex){
					ex.printStackTrace();
				}
			} else {
				//Bennerbot.sendMessage(Bennerbot.capitalize(e.getUser().getNick())+" has just tried to use a command they dont have permision to", 1);
				//FilterManager.punish(e.getUser().getNick());
			}
	}
	
	public static JSONObject getJSON(){
		try{
			String url = "http://www.hitbox.tv/api/media/live/"+Bennerbot.conf.get("hitboxChannel")+"/list";
			URL obj = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
		
			return (JSONObject) APIManager.parser.parse(Bennerbot.StreamToString(conn.getInputStream()));
		}catch(ParseException | IOException e){
			e.printStackTrace();
		}
		return null;
	}
}
