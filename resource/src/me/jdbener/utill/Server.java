package me.jdbener.utill;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import me.jdbener.Bennerbot;

import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
 /**
  * This class represents a server object, which the bot uses to connect too and represent different servers
  * @author Joshua Dahl (Jdbener)
  * @date 11/23/14
  */
public class Server {
	private static int TOTALID = 1;
	private int id;
	/**The configuration builder that is used to identify the server*/
	private PircBotX server;
	/**The path to the logo file*/
	private URL logo;
	/**The url of the server*/
	private String url,
	/**The Channel to connect to on the server*/
				   chan,
	/**The name of the server*/
				   name,
				   user;
	protected String pass;
	
	/**
	 * This function creates the server object
	 * @param URL ~ the url of the server to connect to
	 * @param Name ~ the name of the server, how the servers name is going to be displayed
	 * @param username ~ the username used to connect to the server
	 * @param password ~ the password used to connect to the server
	 * @param channel ~ the channel to connect to
	 * @param Logo ~ the location of the logo for this server
	 */
	public Server(String URL, String Name, String username, String password, String channel, URL Logo){
		server = new PircBotX(new Configuration.Builder<PircBotX>()
				.setName(username)
				.setLogin(Bennerbot.name)
				.setServerPassword(password)
				.addAutoJoinChannel("#"+channel.toLowerCase().replace("#", ""))
				.setServerHostname(URL)
				.setServerPort(6667)
				.setAutoReconnect(true)
				.setListenerManager(Bennerbot.listener).buildConfiguration());
		
		logo = Logo;
		url = URL;
		chan = "#"+channel.toLowerCase().replace("#", "");
		name = Bennerbot.capitalize(Name.toLowerCase());
		user = username;
		pass = password;
		id = TOTALID;
		TOTALID++;
	}
	/**
	 * This function creates the server object
	 * @param URL ~ the url of the server to connect to
	 * @param username ~ the username used to connect to the server
	 * @param password ~ the password used to connect to the server
	 * @param channel ~ the channel to connect to
	 * @param Logo ~ the location of the logo for this server
	 */
	public Server(String URL, String username, String password, String channel, URL Logo){
		server = new PircBotX(new Configuration.Builder<PircBotX>()
				.setName(username)
				.setLogin(Bennerbot.name)
				.setServerPassword(password)
				.addAutoJoinChannel("#"+channel.toLowerCase().replace("#", ""))
				.setServerHostname(URL)
				.setServerPort(6667)
				.setAutoReconnect(true)
				.setListenerManager(Bennerbot.listener).buildConfiguration());
		
		logo = Logo;
		url = URL;
		chan = "#"+channel.toLowerCase().replace("#", "");
		name = Bennerbot.capitalize(URL.toLowerCase());
		id = TOTALID;
		TOTALID++;
	}
	/**
	 * This function creates the server object
	 * @param URL ~ the url of the server to connect to
	 * @param Name ~ the name of the server, how the servers name is going to be displayed
	 * @param username ~ the username used to connect to the server
	 * @param password ~ the password used to connect to the server
	 * @param channel ~ the channel to connect to
	 */
	public Server(String URL, String Name, String username, String password, String channel){
		server = new PircBotX(new Configuration.Builder<PircBotX>()
				.setName(username)
				.setLogin(Bennerbot.name)
				.setServerPassword(password)
				.addAutoJoinChannel("#"+channel.toLowerCase().replace("#", ""))
				.setServerHostname(URL)
				.setServerPort(6667)
				.setAutoReconnect(true)
				.setListenerManager(Bennerbot.listener).buildConfiguration());
		
		try {
			logo = new File("resource/OutputLogo.png").toURI().toURL();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		url = URL;
		chan = "#"+channel.toLowerCase().replace("#", "");
		name = Bennerbot.capitalize(Name.toLowerCase());
		id = TOTALID;
		TOTALID++;
	}
	/**
	 * This function creates the server object
	 * @param URL ~ the url of the server to connect to
	 * @param username ~ the username used to connect to the server
	 * @param password ~ the password used to connect to the server
	 * @param channel ~ the channel to connect to
	 */
	public Server(String URL, String username, String password, String channel){
		server = new PircBotX(new Configuration.Builder<PircBotX>()
				.setName(username)
				.setLogin(Bennerbot.name)
				.setServerPassword(password)
				.addAutoJoinChannel("#"+channel.toLowerCase().replace("#", ""))
				.setServerHostname(URL)
				.setServerPort(6667)
				.setAutoReconnect(true)
				.setListenerManager(Bennerbot.listener).buildConfiguration());
		
		try {
			logo = new File("resource/OutputLogo.png").toURI().toURL();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		url = URL;
		chan = "#"+channel.toLowerCase().replace("#", "");
		name = Bennerbot.capitalize(URL.toLowerCase());
		id = TOTALID;
		TOTALID++;
	}
	/**This function returns the path to the logo for the selected boss*/
	public URL getLogo(){return logo;}
	/**This function returns the configuration for the bot*/
	public PircBotX getBot(){return server;}
	/**This function returns the channel that the bot is connect to*/
	public String getChannel(){return chan;}
	/**This function returns the display name of the bot*/
	public String getName(){return name;}
	/**This function returns the URL of the server the bot is connected to*/
	public String getURL(){return url;}
	/**This function returns the username the bot is connects with*/
	public String getUser(){return user;}
	/**This function returns the URL of the server the bot is connected to as a URL object*/
	public URL getURL(boolean urlObject){try {return new URL(url);} catch (MalformedURLException e) {e.printStackTrace();}return null;}
	public int getID(){return id;}
	/**Returns a reference to the bot that was based off this server object*/
	public void sendMessage(String msg){
		server.sendRaw().rawLine(msg);
	}
}
