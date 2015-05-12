/**
 * This class manages all of the API integration for the bot
 * Author: Jdbener (Joshua Dahl)
 * Date: 11/8/14
 */
package me.jdbener.apis;

import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.FileWriter;
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
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import me.jdbener.Bennerbot;
import me.jdbener.apis.hitbox.HitboxFollowerHandeler;
import me.jdbener.apis.hitbox.HitboxStatusGameUpdater;
import me.jdbener.apis.lastfm.LastfmNowPlaying;
import me.jdbener.apis.twitch.TwitchFollowerHandeler;
import me.jdbener.apis.twitch.TwitchStatusGameUpdater;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class APIManagerOld {
	public static ArrayList<String> followers = new ArrayList<String>();			//This array contains a list of everybody who has followed the bot operator
	public static JSONParser parser = new JSONParser();								//The parser that parses JSON data
	
	private static String TEMPTWITCHAUTH;
	
	private static Browser browser;
	private static Display display;
	private static Shell shell;
	/**
	 * This function loads all of the APIs
	 */
	public APIManagerOld(){		
		setupEmoteTable();
		
		//load follower notifications
		if(Bennerbot.getConfigBoolean("enableFollowerNotifications")){
			new TwitchFollowerHandeler();
			new HitboxFollowerHandeler();
		}
		
		//load the !title and !game commands
		if(Bennerbot.getConfigBoolean("enableStatusandGameUpdateing")){
			Bennerbot.listener.addListener(new TwitchStatusGameUpdater());
			Bennerbot.listener.addListener(new HitboxStatusGameUpdater());
		}
		
		if(Bennerbot.getConfigBoolean("enableLastfmIntegration"))
			Bennerbot.listener.addListener(new LastfmNowPlaying());
		
		if(Bennerbot.getConfigBoolean("enableLatestFollowerFile"))
			Executors.newScheduledThreadPool(1).scheduleAtFixedRate(new Runnable(){
				@Override
				public void run() {
					try {
						String nowPlaying = followers.get(0);
						if(!Bennerbot.StreamToString(new File("latestfollower.txt").toURI().toURL().openStream()).equalsIgnoreCase(nowPlaying)){
							FileWriter f = new FileWriter("latestfollower.txt");
							f.append(nowPlaying);
							f.close();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}, 0, 5, TimeUnit.SECONDS);
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
			
			JSONObject jobj = (JSONObject) APIManagerOld.parser.parse(Bennerbot.StreamToString(conn.getInputStream()));
			return (String) jobj.get("authToken");
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		return null;
		
	}
	public static String GetTwitchAuth(){
		if(!GraphicsEnvironment.isHeadless()){
			display = new Display();
	        shell = new Shell(display);
	        shell.setLayout(new FillLayout());
	        try {
	            browser = new Browser(shell, SWT.NONE);
	        } catch (SWTError e) {
	            System.out.println("Could not instantiate Browser: " + e.getMessage());
	            display.dispose();
	            return TEMPTWITCHAUTH;
	        }
	        
	        browser.setUrl("https://api.twitch.tv/kraken/oauth2/authorize?response_type=token&client_id=9qxushp3sdeasixpxajz8pqlxdudfs6&redirect_uri=http://localhost&scope=channel_editor+channel_subscriptions");
	        shell.setSize(400, 400);
	        shell.setText("Twitch Authentication ~ "+Bennerbot.name+" "+Bennerbot.version);
	        shell.open();
	       
	        browser.addLocationListener(new LocationListener() {
	            public void changing(LocationEvent event){
	                String loc = event.location;
	            	//System.out.println("Navigating to: " + event.location);
	                if(!loc.equalsIgnoreCase("https://api.twitch.tv/kraken/oauth2/authorize?response_type=token&client_id=9qxushp3sdeasixpxajz8pqlxdudfs6&redirect_uri=http://localhost&scope=channel_editor+channel_subscriptions")){
	                	//Bennerbot.conf.put("twitchAccessToken", loc.split("#")[1].split("&")[0].split("=")[1]);
	                	TEMPTWITCHAUTH = loc.split("#")[1].split("&")[0].split("=")[1];
	                	//System.out.println(Bennerbot.conf);
	                	display.dispose();
	                }
	            }

	            public void changed(LocationEvent event){}
	        });
	        
	        while (!shell.isDisposed()) {
	            if (!display.readAndDispatch())
	                display.sleep();
	        }
	        display.dispose();	        
		}
		return TEMPTWITCHAUTH;
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
				
			String search;
			String code;
				
			while(rs.next()){
				try{
					search = rs.getString("REGEX");
					code = search;
					if (!Pattern.compile("[^\\w]").matcher(code).find()) {
						search = "\\b" + code + "\\b";
					}
					//TODO add a way to change the size of emotions
					msg = msg.replaceAll(search, "<img src="+rs.getString("LINK")+">");
					//msg = msg.replaceAll("(?:^|\\s|\\b)"+rs.getString("REGEX")+"(?:^|\\b)", "<img src="+rs.getString("LINK")+">");
				} catch (Exception e){
					//e.printStackTrace();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		msg = msg.replaceAll("<img src=http://edge.vie.hitbox.tv//static/img/chat/default/thumbsdown.png>", "n");
		
	    return msg.replaceAll("[^ -~]", "?");
	}
	private static void setupEmoteTable(){
		try {
			Connection c = getConnection();
			Statement stmt = c.createStatement();
			String sql = "CREATE TABLE IF NOT EXISTS EMOTE " +
					"(REGEX           TEXT    NOT NULL, " + 
					"LINK         	 VARCHAR(2083))";
			stmt.executeUpdate(sql);
			stmt.close();
			c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static Connection getConnection(){
	    Connection c = null;
	    try {
	    	c = DriverManager.getConnection("jdbc:mysql://vps34796.vps.ovh.ca/BENNERBOT?user=BENNERBOT&password=BENNERBOT");
	    } catch ( Exception e ) {
//	    	e.printStackTrace();
//	    	Bennerbot.logger.warn("Connection to remote database failed, trying again with local connection");
	    	c = getLocalConnection();
	    }
	    return c;
	 }
	 private static Connection getLocalConnection(){
	    Connection c = null;
	    try {
	      Class.forName("org.sqlite.JDBC");
	      c = DriverManager.getConnection("jdbc:sqlite:"+new File("resource/bennerbot.db").getAbsolutePath());
	    } catch ( Exception e ) {
	      e.printStackTrace();
	      Bennerbot.logger.warn("Connection to local database failed");
	    }
	    return c;
	 }
}
