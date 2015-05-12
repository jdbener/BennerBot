package me.jdbener.apis.twitch;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import me.jdbener.Bennerbot;
import me.jdbener.apis.APIManager;

import org.ho.yaml.Yaml;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

public class TwitchAutoHostingManager {
	String currentlyHosting = null;
	
	@SuppressWarnings("unchecked")
	public TwitchAutoHostingManager(){
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
					if(((JSONObject) APIManager.parser.parse(Bennerbot.StreamToString(new URL("https://api.twitch.tv/kraken/streams/"+Bennerbot.getConfigString("twitchChannel")).openStream()))).get("stream") != null){
						Bennerbot.sendMessage("/unhost", Bennerbot.getBotIDbyName("twitch"), "");
						currentlyHosting=null;
					}
					//Override for user selected hosts
					else if(!((JSONObject) APIManager.parser.parse(Bennerbot.StreamToString(new URL("http://chatdepot.twitch.tv/rooms/"+Bennerbot.getConfigString("twitchChannel")+"/host_target").openStream()))).get("host_target").toString().equalsIgnoreCase("") && !((JSONObject) APIManager.parser.parse(Bennerbot.StreamToString(new URL("http://chatdepot.twitch.tv/rooms/"+Bennerbot.getConfigString("twitchChannel")+"/host_target").openStream()))).get("host_target").toString().equalsIgnoreCase(currentlyHosting)){
						currentlyHosting=((JSONObject) APIManager.parser.parse(Bennerbot.StreamToString(new URL("http://chatdepot.twitch.tv/rooms/"+Bennerbot.getConfigString("twitchChannel")+"/host_target").openStream()))).get("host_target").toString();
					}
					//Otherwise host someone from the list
					else {
						if(((JSONObject) APIManager.parser.parse(Bennerbot.StreamToString(new URL("https://api.twitch.tv/kraken/streams/"+currentlyHosting).openStream()))).get("stream") != null)
							Bennerbot.logger.info("Still Hosting "+currentlyHosting);
							//Bennerbot.sendMessage("/host "+currentlyHosting, Bennerbot.getBotIDbyName("twitch"), "");
						else
							for(Entry<String, Object> e: APIManager.hosttargets.entrySet())
								if(e.getValue().toString().equalsIgnoreCase("twitch"))
									if(((JSONObject) APIManager.parser.parse(Bennerbot.StreamToString(new URL("https://api.twitch.tv/kraken/streams/"+e.getKey()).openStream()))).get("stream") != null){
										currentlyHosting=e.getKey();
										Bennerbot.sendMessage("/host "+currentlyHosting, Bennerbot.getBotIDbyName("twitch"), "");
									}
								
					}
				} catch (ParseException | IOException e) {
					e.printStackTrace();
				}
					
			}
			
		}, 0, 1, TimeUnit.MINUTES);
	}
}
