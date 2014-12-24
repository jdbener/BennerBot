package me.jdbener.apis.lastfm;

import java.net.URL;

import me.jdbener.Bennerbot;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

public class LastfmNowPlaying extends ListenerAdapter<PircBotX>{
	public static JSONParser parser;	
	public void onMessage(MessageEvent<PircBotX> e){
		if(e.getMessage().equalsIgnoreCase("!song"))
			try {
				JSONObject obj = (JSONObject) new JSONParser().parse(Bennerbot.StreamToString(new URL("http://ws.audioscrobbler.com/2.0/?method=user.getrecenttracks&limit=20&user="+Bennerbot.conf.get("lastfmName")+"&api_key=f3237219f4b0a18670d59ad9458acb91&format=json").openStream()));
				
				JSONObject recent = (JSONObject) obj.get("recenttracks");
				JSONObject track = (JSONObject)((JSONArray) recent.get("track")).get(0);
				
				String out = Bennerbot.conf.get("songCommandMessageFormat").toString()
					.replace("<user>", Bennerbot.capitalize(e.getUser().getNick()))
					.replace("<title>", (String) track.get("name"))
					.replace("<url>", (String) track.get("url"))
					.replace("<artist>", (String)((JSONObject) track.get("artist")).get("#text"))
					.replace("<album>", (String)((JSONObject) track.get("album")).get("#text"))
					.replaceAll("[^ -~]", "?");
				
				for(int i = 1; i < 20; i++){
					track = (JSONObject)((JSONArray) recent.get("track")).get(i);
				
					out = out.replace("<"+i+"title>", (String) track.get("name"))
						.replace("<"+i+"url>", (String) track.get("url"))
						.replace("<"+i+"artist>", (String)((JSONObject) track.get("artist")).get("#text"))
						.replace("<"+i+"album>", (String)((JSONObject) track.get("album")).get("#text"));
				}
			
				Bennerbot.sendMessage(out);	
			} catch (Exception ex) {
				ex.printStackTrace();
				}
	}
	
}
