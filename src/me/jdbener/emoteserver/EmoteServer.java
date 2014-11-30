package me.jdbener.emoteserver;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import me.jdbener.lib.EscapeChars;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmoteServer {
	private static Logger logger = LoggerFactory.getLogger(EmoteServer.class);
	private static JSONParser parser = new JSONParser();

	public static void main (String[] args){
		setupEmoteTable();
		Runnable run = new Runnable() {
			public void run() {
				logger.info("Loading Emoticons");
				insertEmotes(setupEmotes());
				logger.info("Finished Loading Emoticons");
			}
		};
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
		executor.scheduleAtFixedRate(run, 0, 1, TimeUnit.DAYS);
}
	/**
	 * This function will convert an input stream into a string
	 * @param is ~ the input steam
	 * @return ~ the string
	 */
	private static String StreamToString(java.io.InputStream is) {
		@SuppressWarnings("resource")
		Scanner s = new Scanner(is).useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
	}
	/**
	 * This function sets up all of the emotions
	 */
	private static Map<String, String> setupEmotes(){
		Map<String, String> temp = new HashMap<String, String>();
		//TODO implement getting all the hitbox emotions
		try {
			//HttpURLConnection conn = (HttpURLConnection) new URL("https://api.twitch.tv/kraken/chat/emoticons").openConnection();

			JSONObject jobj = (JSONObject) parser.parse(StreamToString(new URL("https://api.twitch.tv/kraken/chat/emoticons").openStream()));
			JSONArray emotes = (JSONArray) jobj.get("emoticons");
			
			JSONObject emote;
			String regex, url;
			int i=0;while(i < emotes.toArray().length){
				emote = (JSONObject) emotes.get(i);
			
				regex = EscapeChars.forRegex(((String) emote.get("regex")).replace("~", ""));
				url = (String)((JSONObject)((JSONArray)emote.get("images")).get(0)).get("url");
			
				logger.info("Adding Emoticon: "+regex+"~"+url);
				temp.put(regex, url);
				i++;
			}	
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		return temp;
	}
	//Database Name	x32ce8ff7x_BB
	//Database Username	x32ce8ff7x_BB
	//Database Password	RCJEQpiESS
	//Host bennerbot.pagebit.net

	//dbhost 198.91.81.2
	//dbpass bennerbot
	//dbuser/name bennerbo_t
	private static Connection getConnection(){
		Connection c = null;
		try {
			c = DriverManager.getConnection("jdbc:mysql://85.10.205.173:3306/bennerbot?" + "user=bennerbot&password=bennerbot");
		} catch ( Exception e ) {
			e.printStackTrace();
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
		}
		return c;
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
	private static void insertEmotes(Map <String, String> in){
		logger.info(in.size()+" emoticons to load");
		try{
			Connection c = getConnection();
			Statement stmt = c.createStatement();
			int i = 1;for(Map.Entry<String, String> entry : in.entrySet()){
				String sql = "DELETE FROM EMOTE WHERE REGEX = '"+entry.getKey()+"'";
				stmt.executeUpdate(sql);
				sql = "INSERT INTO EMOTE (REGEX, LINK) " +
						"VALUES ('"+entry.getKey()+"','"+entry.getValue()+"');"; 
				stmt.executeUpdate(sql);
				logger.info("inserted emoicon #"+i+": "+entry.getKey());
				i++;
			}
			stmt.close();
			c.close();
		} catch (SQLException e){
			e.printStackTrace();
		}
	}
}