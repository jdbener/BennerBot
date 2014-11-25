/**
 * This class manages all of the API integration for the bot
 * Author: Jdbener (Joshua Dahl)
 * Date: 11/8/14
 */
package me.jdbener.apis;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.swing.JOptionPane;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import me.jdbener.Bennerbot;
import me.jdbener.apis.hitbox.*;
import me.jdbener.apis.lastfm.*;
import me.jdbener.apis.twitch.*;

public class APIManager {
	public static ArrayList<String> followers = new ArrayList<String>();			//This array contains a list of everybody who has followed the bot operator
	public static JSONParser parser = new JSONParser();								//The parser that parses JSON data
	
	/**
	 * This function loads all of the APIs
	 */
	public APIManager(){
		//load follower notifications
		if(Bennerbot.conf.get("enableFollowerNotifications").toString().equalsIgnoreCase("true")){
			new TwitchFollowerHandeler();
			new HitboxFollowerHandeler();
		}
		
		//load the !title and !game commands
		if(Bennerbot.conf.get("enableStatusandGameUpdateing").toString().equalsIgnoreCase("true")){
			Bennerbot.listener.addListener(new TwitchStatusGameUpdater());
			Bennerbot.listener.addListener(new HitboxStatusGameUpdater());
		}
		
		if(Bennerbot.conf.get("enableLastfmIntegration").toString().equalsIgnoreCase("true"))
			Bennerbot.listener.addListener(new LastfmNowPlaying());
		
		//setupEmotes();
		if(Bennerbot.conf.get("enableAutoEmotes").toString().equalsIgnoreCase("true")) {
			Bennerbot.logger.info("Loading Emoticons");
			setupEmoteTable();
			ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
			executor.execute(new Runnable() {
				public void run() {
					insertEmotes(setupEmotes());
					Bennerbot.logger.info("Finished Loading Emoticons");
					try{
						JOptionPane.showMessageDialog(null, "Finished Loading Emoticons");
					} catch (Exception e){
						e.printStackTrace();
					}
				}
			});
		}
	}
	/**
	 * This function will convert an input stream into a string
	 * @param is ~ the input steam
	 * @return ~ the string
	 */
	public static String StreamToString(java.io.InputStream is) {
	    @SuppressWarnings("resource")
		Scanner s = new Scanner(is).useDelimiter("\\A");
	    return s.hasNext() ? s.next() : "";
	}
	/**
	 * returns the hitbox auth token used to update the 
	 */
	public static String GetHitboxAuth (String user, String pass){
		try {
			String url = "http://api.hitbox.tv/auth/token?";
			URL obj = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
			
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			
			String data = "login="+user+"&pass="+pass+"&app=desktop";
			
			OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
			out.write(data);
			out.flush();
			
			JSONObject jobj = (JSONObject) APIManager.parser.parse(APIManager.StreamToString(conn.getInputStream()));
			return (String) jobj.get("authToken");
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		return null;
		
	}
	/**
	 * This function sets up all of the emotions
	 */
	private static Map<String, String> setupEmotes(){
		Map<String, String> temp = new HashMap<String, String>();
		//TODO implement getting all the hitbox emotions
		try {
			HttpURLConnection conn = (HttpURLConnection) new URL("https://api.twitch.tv/kraken/chat/emoticons").openConnection();
			
			JSONObject jobj = (JSONObject) APIManager.parser.parse(APIManager.StreamToString(conn.getInputStream()));
			JSONArray emotes = (JSONArray) jobj.get("emoticons");
			
			JSONObject emote;
			String regex, url;
			int i=0;while(i < emotes.toArray().length){
				emote = (JSONObject) emotes.get(i);
				
				regex = (String) emote.get("regex");
				url = (String)((JSONObject)((JSONArray)emote.get("images")).get(0)).get("url");
				
				if(Bennerbot.conf.get("ShowEmoticonLoading").toString().equalsIgnoreCase("true"))
					Bennerbot.logger.info("Adding Emoticon: "+regex+"~"+url);
				temp.put(regex, url);
				i++;
			}
			
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		return temp;
	}
	/**
	 * this function filters all the emoticons in a string
	 * @param msg ~ the string to filter
	 * @return ~ the filtered string
	 */
	public static String filterEmotes(String msg){
		try {
	    	Connection c = getConnection();
	    	Statement stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery( "SELECT * FROM EMOTE;" );
			
			while(rs.next()){
				msg = msg.replaceAll(rs.getString("REGEX"), "<img src="+rs.getString("LINK")+">");
			}
			
			//message overrides
			msg = msg.replaceAll("<img src=http://static-cdn.jtvnw.net/jtv_user_pictures/chansub-global-emoticon-f8fe8c92a3dea<img src=http://static-cdn.jtvnw.net/jtv_user_pictures/chansub-global-emoticon-f8fe8c92a3dea0bb-23x30.png>bb-23x3<img src=http://static-cdn.jtvnw.net/jtv_user_pictures/chansub-global-emoticon-f8fe8c92a3dea0bb-23x30.png>.png><img src=http://static-cdn.jtvnw.net/jtv_user_pictures/chansub-global-emoticon-f8fe8c92a3dea<img src=http://static-cdn.jtvnw.net/jtv_user_pictures/chansub-global-emoticon-f8fe8c92a3dea0bb-23x30.png>bb-23x3<img src=http://static-cdn.jtvnw.net/jtv_user_pictures/chansub-global-emoticon-f8fe8c92a3dea0bb-23x30.png>.png><img src=http://static-cdn.jtvnw.net/jtv_user_pictures/chansub-global-emoticon-f8fe8c92a3dea<img src=http://static-cdn.jtvnw.net/jtv_user_pictures/chansub-global-emoticon-f8fe8c92a3dea0bb-23x30.png>bb-23x3<img src=http://static-cdn.jtvnw.net/jtv_user_pictures/chansub-global-emoticon-f8fe8c92a3dea0bb-23x30.png>.png>", "000");
			msg = msg.replaceAll("<img src=http://static-cdn.jtvnw.net/jtv_user_pictures/emoticon-12183-src-5e30313e517ae659-28x28.png>", "eve");
			msg = msg.replaceAll("<img src=http://static-cdn.jtvnw.net/jtv_user_pictures/chansub-global-emoticon-f8fe8c92a3dea0bb-23x30.png>", "0");
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	    return msg;
	}
	public static Connection getConnection(){
	    Connection c = null;
	    try {
	      Class.forName("org.sqlite.JDBC");
	      c = DriverManager.getConnection("jdbc:sqlite:"+new File("resource/bennerbot.db").getAbsolutePath());
	    } catch ( Exception e ) {
	      e.printStackTrace();
	    }
	    return c;
	 }
	 public static void setupEmoteTable(){
		 try {
			 Connection c = getConnection();
			  Statement stmt = c.createStatement();
			  String sql = "DROP TABLE IF EXISTS EMOTE;"; 
			  stmt.executeUpdate(sql);
			  sql = "CREATE TABLE IF NOT EXISTS EMOTE " +
		                   "(REGEX           TEXT    NOT NULL, " + 
		                   " LINK         	 VARCHAR(2083))"; 
		      stmt.executeUpdate(sql);
		      stmt.close();
		      c.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	 }
	 public static void insertEmotes(Map <String, String> in){
		 Bennerbot.logger.info(in.size()+" emoticons to load");
		 try{
			 Connection c = getConnection();
			 Statement stmt = c.createStatement();
			 int i = 1;
			 for(Map.Entry<String, String> entry : in.entrySet()){
				 String sql = "INSERT INTO EMOTE (REGEX, LINK) " +
	                   "VALUES ('"+entry.getKey()+"','"+entry.getValue()+"');"; 
				 stmt.executeUpdate(sql);
				 if(Bennerbot.conf.get("ShowEmoticonLoading").toString().equalsIgnoreCase("true"))
					 Bennerbot.logger.info("inserted emoicon #"+i+": "+entry.getKey());
				 i++;
			 }
			 stmt.close();
			 c.close();
		 } catch (SQLException e){
			 e.printStackTrace();
		 }
	 }
}
