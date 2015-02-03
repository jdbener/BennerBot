package me.jdbener.apis.lastfm;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import me.jdbener.Bennerbot;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

public class LastfmNowPlaying extends ListenerAdapter<PircBotX>{
	public static JSONParser parser;	
	public LastfmNowPlaying(){
		if(Bennerbot.configBoolean("enableCurrentlyPlayingSongFile"))
			Executors.newScheduledThreadPool(1).scheduleAtFixedRate(new Runnable(){
				@Override
				public void run() {
					try {
						String nowPlaying = getCurrentlyPlayingSong();
						if(!Bennerbot.StreamToString(new File("nowplaying.txt").toURI().toURL().openStream()).equalsIgnoreCase(nowPlaying)){
							FileWriter f = new FileWriter("nowplaying.txt");
							f.append(nowPlaying);
							f.close();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}, 0, 5, TimeUnit.SECONDS);
	}
	public void onMessage(MessageEvent<PircBotX> e){
		if(e.getMessage().equalsIgnoreCase("!song"))
			Bennerbot.sendMessage(getCurrentlyPlayingSong(Bennerbot.capitalize(e.getUser().getNick())));	

	}
	private String getCurrentlyPlayingSong(String user){
		try{
			JSONObject obj = (JSONObject) new JSONParser().parse(Bennerbot.StreamToString(new URL("http://ws.audioscrobbler.com/2.0/?method=user.getrecenttracks&limit=20&user="+Bennerbot.conf.get("lastfmName")+"&api_key=f3237219f4b0a18670d59ad9458acb91&format=json").openStream()));
			
			JSONObject recent = (JSONObject) obj.get("recenttracks");
			JSONObject track = (JSONObject)((JSONArray) recent.get("track")).get(0);
			
			String out = Bennerbot.conf.get("songCommandMessageFormat").toString()
				.replace("<user>", Bennerbot.capitalize(user))
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
			return out;
		} catch (ParseException | IOException e){
			e.printStackTrace();
		}
		return "error";
	}
	private String getCurrentlyPlayingSong(){
		try{
			JSONObject obj = (JSONObject) new JSONParser().parse(Bennerbot.StreamToString(new URL("http://ws.audioscrobbler.com/2.0/?method=user.getrecenttracks&limit=20&user="+Bennerbot.conf.get("lastfmName")+"&api_key=f3237219f4b0a18670d59ad9458acb91&format=json").openStream()));
			
			JSONObject recent = (JSONObject) obj.get("recenttracks");
			JSONObject track = (JSONObject)((JSONArray) recent.get("track")).get(0);
			
			String out = Bennerbot.conf.get("songFileFormat").toString();
				try{out = out.replace("<title>", (String) track.get("name"));}catch(Exception e){e.printStackTrace();}
				try{out = out.replace("<url>", (String) track.get("url"));}catch(Exception e){e.printStackTrace();}
				try{out = out.replace("<artist>", (String)((JSONObject) track.get("artist")).get("#text"));}catch(Exception e){e.printStackTrace();}
				try{out = out.replace("<album>", (String)((JSONObject) track.get("album")).get("#text"));}catch(Exception e){e.printStackTrace();}
			
			for(int i = 1; i < 20; i++){
				track = (JSONObject)((JSONArray) recent.get("track")).get(i);
			
				try{out = out.replace("<"+i+"title>", (String) track.get("name"));}catch(Exception e){e.printStackTrace();}
				try{out = out.replace("<"+i+"url>", (String) track.get("url"));}catch(Exception e){e.printStackTrace();}
				try{out = out.replace("<"+i+"artist>", (String)((JSONObject) track.get("artist")).get("#text"));}catch(Exception e){e.printStackTrace();}
				try{out = out.replace("<"+i+"album>", (String)((JSONObject) track.get("album")).get("#text"));}catch(Exception e){e.printStackTrace();}
			}
			return out;
		} catch (ParseException | IOException e){
			e.printStackTrace();
		}
		return "error";
	}
}
