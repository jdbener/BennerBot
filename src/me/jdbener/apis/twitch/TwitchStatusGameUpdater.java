package me.jdbener.apis.twitch;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map.Entry;

import me.jdbener.Bennerbot;
import me.jdbener.moderataion.FilterManager;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

public class TwitchStatusGameUpdater extends ListenerAdapter<PircBotX>{
	public void onMessage(MessageEvent<PircBotX> e){
		if(Bennerbot.conf.get("twitchAccessToken") != null){
			if(e.getMessage().startsWith("!game"))
				if(Bennerbot.isMod(e.getUser(), e.getChannel())){
				try {
					String url = "https://api.twitch.tv/kraken/channels/"+Bennerbot.conf.get("twitchChannel").toString().toLowerCase()+"?oauth_token="+Bennerbot.getAccessToken();
					URL obj = new URL(url);
					HttpURLConnection conn = (HttpURLConnection) obj.openConnection();

					conn.setRequestProperty("Accept", "application/vnd.twitchtv.v2+json");
					conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
					conn.setRequestMethod("PUT");
					conn.setDoOutput(true);
			
					String game = "";
			
					for(int i = 1; i<e.getMessage().split(" ").length; i++)
						game+=e.getMessage().split(" ")[i]+" ";
					
					String data = "channel[game]="+game;
			
					OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
					out.write(data);
					out.flush();
			
					for (Entry<String, List<String>> header : conn.getHeaderFields().entrySet())
						System.out.println(header.getKey() + "=" + header.getValue());
			
					Bennerbot.sendMessage("Stream's game was set to: "+game);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				} else {
					Bennerbot.sendMessage(Bennerbot.capitalize(e.getUser().getNick())+" has just tried to use a command they dont have permision to", 0);
					FilterManager.punish(e.getUser().getNick());
				}
			if(e.getMessage().startsWith("!title")){
				if(Bennerbot.isMod(e.getUser(), e.getChannel())){
				try {
					Bennerbot.logger.info("changing title");
					String url = "https://api.twitch.tv/kraken/channels/"+Bennerbot.conf.get("twitchChannel").toString().toLowerCase()+"?oauth_token="+Bennerbot.configGetString("twitchAccessToken");
					URL obj = new URL(url);
					HttpURLConnection conn = (HttpURLConnection) obj.openConnection();

					conn.setRequestProperty("Accept", "application/vnd.twitchtv.v2+json");
					conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
					conn.setRequestMethod("PUT");
					conn.setDoOutput(true);
				
					String title = "";
				
					for(int i = 1; i<e.getMessage().split(" ").length; i++)
						title+=e.getMessage().split(" ")[i]+" ";

					String data = "channel[status]="+title;
				
					OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
					out.write(data);
					out.flush();
				
					for (Entry<String, List<String>> header : conn.getHeaderFields().entrySet())
						System.out.println(header.getKey() + "=" + header.getValue());
				
					Bennerbot.sendMessage("Stream's title was set to: "+title);
				} catch (IOException e1){
					e1.printStackTrace();
				}
				} else {
					Bennerbot.sendMessage(Bennerbot.capitalize(e.getUser().getNick())+" has just tried to use a command they dont have permision to", 0);
					FilterManager.punish(e.getUser().getNick());
				}
			}
		}
	}
	
}
