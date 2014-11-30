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

import me.jdbener.Bennerbot;
import me.jdbener.apis.hitbox.HitboxFollowerHandeler;
import me.jdbener.apis.hitbox.HitboxStatusGameUpdater;
import me.jdbener.apis.lastfm.LastfmNowPlaying;
import me.jdbener.apis.twitch.TwitchFollowerHandeler;
import me.jdbener.apis.twitch.TwitchStatusGameUpdater;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class APIManager {
	public static ArrayList<String> followers = new ArrayList<String>();			//This array contains a list of everybody who has followed the bot operator
	public static JSONParser parser = new JSONParser();								//The parser that parses JSON data
	
	private static String dbName = "jdbc:mysql://85.10.205.173:3306/bennerbot?" + "user=bennerbot&password=bennerbot"; 
	
	/**
	 * This function loads all of the APIs
	 */
	public APIManager(){
		if(!Bennerbot.configEqualsString("databaseName", "default"))
			dbName = Bennerbot.configGetString("databaseName");
		
		dbName = "jdbc:mysql://"+dbName.replace("jdbc:mysql://", "");
		
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
			
			JSONObject jobj = (JSONObject) APIManager.parser.parse(Bennerbot.StreamToString(conn.getInputStream()));
			return (String) jobj.get("authToken");
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		return null;
		
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
	    if(Bennerbot.configBoolean("useRemoteDatabase"))
	    	try {
	    		//Class.forName("org.sqlite.JDBC");
	    		//c = DriverManager.getConnection("jdbc:mysql://bennerbot.pagebit.net:3306/x32ce8ff7x_BB", "x32ce8ff7x_BB", "RCJEQpiESS");
	    		c = DriverManager.getConnection(dbName);
	    	} catch ( Exception e ) {
	    		e.printStackTrace();
	    		c = getLocalConnection();
	    	}
	    else
	    	c = getLocalConnection();
	    return c;
	 }
	 public static Connection getLocalConnection(){
	    Connection c = null;
	    try {
	      Class.forName("org.sqlite.JDBC");
	      c = DriverManager.getConnection("jdbc:sqlite:"+new File("resource/bennerbot.db").getAbsolutePath());
	    } catch ( Exception e ) {
	      e.printStackTrace();
	    }
	    return c;
	 }
}
