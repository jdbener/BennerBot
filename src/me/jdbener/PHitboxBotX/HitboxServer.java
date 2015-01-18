package me.jdbener.PHitboxBotX;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_10;
import org.java_websocket.handshake.ServerHandshake;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.Utils;
import org.pircbotx.hooks.events.ConnectEvent;
import org.pircbotx.hooks.events.DisconnectEvent;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.managers.ListenerManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.jdbener.Bennerbot;
import me.jdbener.lib.Server;

public class HitboxServer extends Server{
	PHitboxBotX server;
	public HitboxServer(String username, String password, String channel, URL logo) {
		super("hitbox.tv", "Hitbox", username, password, channel, logo);
		server = new PHitboxBotX(getUser(), pass, getChannel(), Bennerbot.listener);
	}
	
	public void sendMessage(String msg){
		server.sendMessage(msg.replace("PRIVMSG "+getChannel()+" :", ""));
	}
}
@SuppressWarnings("unused")
class HitboxListener extends WebSocketClient {
	private String user,pass,channel;
	private static String token;
	public static String botName;
	private static boolean connected;
	private static boolean joiningChannel = false;
	private static ArrayList<WebSocketOutput> outputs = new ArrayList<WebSocketOutput>();
	/**
	 * Gets the list of available IPs for the connection and substring the first IP given
	 * @return The IP of the first server given by the server
	 */
	public static String getServerIP() {

		String serverList;

		// Recover the first server_ip given by the server
		serverList = url2String("http://api.hitbox.tv/chat/servers.json?redis=true");
		int serverIP1 = serverList.indexOf("server_ip\":\"");

		// First number of the IP
		int startIP = serverIP1 + 12;
//		PHitboxBotX.log.info(serverIP1); // DEBUG LINE
//		PHitboxBotX.log.info(serverList.charAt(startIP)); // DEBUG LINE - show the character at the position recovered
		// Recover the last IP
		int endIP = serverList.indexOf("\"", startIP);
//		PHitboxBotX.log.info(endIP); // DEBUG LINE
//		PHitboxBotX.log.info(serverList.charAt(endIP - 1)); DEBUG LINE

		// Recover the entire IP
		String IP = serverList.substring(startIP, endIP);
		// PHitboxBotX.log.info(IP); // DEBUG LINE - print the IP recovered

		return IP;
	}

	/**
	 * Gets the connection ID given by HitBox servers
	 * @param IP - the IP given by the getServerIP method
	 * @return - the connecion ID in order to connect the bot
	 */
	public static String getConnectionID(String IP) {

		// gets the connectionID of the server
		String connectionID = url2String("http://" + IP + "/socket.io/1/");

		// Gets the position of the first colon
		int colon = connectionID.indexOf(":");
		String ID = connectionID.substring(0, colon);

//		PHitboxBotX.log.info(ID); // DEBUG LINE - Prints the ID given by the server

		return ID;
	}

	/**
	 * Method used to connect to the given URL
	 * @param urlString - The URL to connect
	 * @return - the String returned by the URL
	 */
	public static String url2String(URL in){
		try {
			@SuppressWarnings("resource")
			Scanner s = new Scanner(in.openStream()).useDelimiter("\\A");
			return s.hasNext() ? s.next() : "";
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
	public static String url2String(String in){
		try {
			@SuppressWarnings("resource")
			Scanner s = new Scanner(new URL(in).openStream()).useDelimiter("\\A");
			return s.hasNext() ? s.next() : "";
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * Launch a connection to the HitBox server in order to retrieve the token to allow the bot to connect to the chat
	 * 
	 * @param user - the username given by the user
	 * @param password - the password of the account
	 * @return - The token if the connection is successful / Exit(0) if the credentials are wrong
	 */
	public static String getToken(String user, String password) {
		
		botName = user;
		
		HttpURLConnection connection = null;
		StringBuffer response = new StringBuffer();
		
		
		try {
			// Create connection
			URL url = new URL("http://api.hitbox.tv/auth/token");
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");

			// Parameters

			String urlParameters = "login="+botName+"&pass="+password+"&app=desktop";
//			PHitboxBotX.log.info(urlParameters);  // DEBUG LINE - SHOW THE PASSWORD GIVEN BY THE USER ON THE CONSOLE
			// Send post request
			connection.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(
					connection.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();
			// Get Response
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			String line;
			while ((line = rd.readLine()) != null) {
				response.append(line);
			}
			rd.close();
			String authToken = response.toString();
			
			// Find where is the token
			int token1 = authToken.indexOf("authToken\":\"");
			int startToken = token1 + 12;
//			PHitboxBotX.log.info(authToken.charAt(startToken)); // DEBUG LINE

			int endToken = authToken.indexOf("\"", startToken);
			// Store the token in the class
			token = authToken.substring(startToken, endToken);

			
		} catch (IOException e) {
			// If credentials are wrong
			PHitboxBotX.log.info("Wrong credentials. Try again");
		} catch (Exception e) {

			e.printStackTrace();

		} finally {

			if (connection != null) {
				connection.disconnect();
			}
		}
//		PHitboxBotX.log.info(token); // DEBUG LINE
		return token;
	}
	
	// Constructors
	public HitboxListener(URI serverUri, String user_, String password_, String channel_) {
		super(serverUri, new Draft_10());
		
		user = user_;
		pass = password_;
		channel = channel_;
		if(botName.equalsIgnoreCase(""))
			botName = user_;
		
		conection();
	}
	/**
	 * 
	 * If the connection is succesfull, this method is called in order to continue
	 * 
	 */
	/* (non-Javadoc)
	 * @see org.java_websocket.client.WebSocketClient#onOpen(org.java_websocket.handshake.ServerHandshake)
	 */
	@Override
	public void onOpen(ServerHandshake handshakedata) {
		sendOutput("6:::[{\"event\": \"Conection Established\"}]");
		connected = true;
	}

	/**
	 * Method called on each received message by the bot
	 */
	/* (non-Javadoc)
	 * @see org.java_websocket.client.WebSocketClient#onMessage(java.lang.String)
	 */
	@Override
	public void onMessage(String message) {
			// if ping
			if (message.equals("2::")) {
				// pong
				this.send("2::");
				sendOutput("2::[{\"event\": \"ping-pong\"}]");
			}
			// If this is another important message
			else if (message.startsWith("5:::")){
				// The bot will take it
				sendOutput(message);
			}
			else{
				sendOutput("other::"+message);
			}
	}

	/* (non-Javadoc)
	 * @see org.java_websocket.client.WebSocketClient#onClose(int, java.lang.String, boolean)
	 */
	@Override
	public void onClose(int code, String reason, boolean remote) {
		sendOutput("7:::[{\"event\": \"Conection Closed\",\"code\": \""+code+"\", \"reason\": \""+reason+"\"}]");
		connected = false;
	}

	/* (non-Javadoc)
	 * @see org.java_websocket.client.WebSocketClient#onError(java.lang.Exception)
	 */
	@Override
	public void onError(Exception ex) {
		sendOutput("8:::[{\"event\": \"Error Occured\", \"error\": \""+ex+"\"}]");
		ex.printStackTrace();
	}
	/**
	 * Joins the channel given by the user
	 * @param channel - the channel to join
	 */
	public void joinChannel(String channel_) {
		channel = channel_;
		joiningChannel = true;
		sendOutput("5:::{\"name\":\"message\",\"args\":[{\"method\":\"joinChannel\",\"params\":{\"channel\":\""+channel+"\",\"name\":\""+botName+"\",\"token\":\"" + token + "\",\"isAdmin\":false}}]}");
		this.send("5:::{\"name\":\"message\",\"args\":[{\"method\":\"joinChannel\",\"params\":{\"channel\":\""+channel+"\",\"name\":\""+botName+"\",\"token\":\"" + token + "\",\"isAdmin\":false}}]}");
		try {
			// Sleep to avoid the bot react to older messages
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		joiningChannel = false;

	}
	
	public void sendMessage(String message){
		sendOutput("5:::{\"name\":\"message\",\"args\":[{\"method\":\"chatMsg\",\"params\":{\"channel\":\""+channel.replace("#", "")+"\",\"name\":\""+botName+"\",\"nameColor\":\"FA5858\",\"text\":\""+message+"\"}}], \"extra-info\":\"from-bot\"}");
		this.send("5:::{\"name\":\"message\",\"args\":[{\"method\":\"chatMsg\",\"params\":{\"channel\":\""+channel.replace("#", "")+"\",\"name\":\""+botName+"\",\"nameColor\":\"FA5858\",\"text\":\""+message+"\"}}], \"extra-info\":\"from-bot\"}");
	}
	/**
	 * connection method Calls necessary methods to launch the websocket
	 * 
	 * @param password - The password given by the user
	 * @param botName - The name of the bot account
	 */
	private void conection() {
		try {
			PHitboxBotX.log.info("Connecting to hitbox servers...");
			connectBlocking();
			joinChannel(channel);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	private void sendOutput(String out){
		for(WebSocketOutput o: outputs){
			o.write(out);
		}
	}
	public void addOutput(WebSocketOutput... o){
		for(WebSocketOutput out: o)
			outputs.add(out);
	}
	public void removeOutput(int... index){
		for(int i: index)
			outputs.remove(i);
	}
	public boolean isConnected(){return connected;}
	public String getBotName(){return botName;}
	public String getChannel(){return channel;}
	public String getUser(){return user;}
}
class PHitboxBotX extends PircBotX{
	String user, pass, channel;
	HitboxListener socket;
	public static Logger log = LoggerFactory.getLogger(PHitboxBotX.class);
	WebSocketOutput stream = new WebSocketOutput(){
		@Override
		public void write(String in) {
			//chat message
			if(in.startsWith("5::") && in.contains("\\\"method\\\":\\\"chatMsg\\\"") && !in.contains("\"extra-info\":\"from-bot\"")){
				//sender's name
				int name1 = in.indexOf("name\\\":\\\"");
				int startname = name1 + 9;
				int endname = in.indexOf("\\\"", startname);
				String name = in.substring(startname, endname);
				//message sent
				int text1 = in.indexOf("text\\\":\\\"");
				int starttext = text1 + 9;
				int endtext = in.indexOf("\\\"", starttext);
				String text = in.substring(starttext, endtext);
				//create event
				log.info(":"+name+"!"+name+"@hitbox.tv PRIVMSG #"+channel+" :"+text);
				newMessageEvent(text, name);
			//Connection
			} else if(in.startsWith("6::")){
				log.info("Successfully connected to a Hitbox Server");
				newConnectionEvent();
			//Disconnect
			} else if(in.startsWith("7::")){
				log.info("Successfully disconnected from the Hitbox Server");
				newDisconnectionEvent();
			//Error
			} else if(in.startsWith("8::")){
				int error1 = in.indexOf("error\\\":\\\"");
				int starterror = error1 + 10;
				int enderror = in.indexOf("\\\"", starterror);
				Exception e = new Exception(in.substring(starterror, enderror));
				e.printStackTrace();
				newDisconnectionEvent(e);
			//Ping
			} else if(in.startsWith("2::")){
//				log.info("Pinged"); //This is disabled to prevent the super spam
			//Other message
			} else {
				System.err.println(in);
			}
		}
	};

	public PHitboxBotX(String username, String password, String channel_, ListenerManager<PircBotX> manager) {
		super(new Configuration.Builder<PircBotX>()
				.setName(username)
				.setLogin(username)
				.setServerPassword(password)
				.setServerHostname("socket.hitbox.tv")
				.addAutoJoinChannel("#"+channel_)
				.setListenerManager(manager)
				.buildConfiguration());
		
		user = username;
		pass = password;
		channel = channel_;
		
		try {
			// Recover all the information to launch the connection to the chat
			String IP = HitboxListener.getServerIP();
			log.info("Getting Hitbox server IP: "+IP);
			String ID = HitboxListener.getConnectionID(IP);
			log.info("Getting connection ID: "+ID);
			log.info("Getting your token");
			HitboxListener.getToken(user, pass);
			
			socket = new HitboxListener(new URI("ws://" + IP + "/socket.io/1/websocket/" + ID), user, pass, channel);
			socket.addOutput(stream);
			log.info("Sucessfully Connected... Joined Channel "+channel);
		} catch (Exception e) {e.printStackTrace();}
		
		 Utils.addBotToMDC(this);
	}
	private void newMessageEvent(String message, String user){
		this.configuration.getListenerManager().dispatchEvent(new MessageEvent<PircBotX>(this, getConfiguration().getBotFactory().createChannel(this, channel), getConfiguration().getBotFactory().createUser(this, user), message));
	}
	private void newConnectionEvent(){
		this.configuration.getListenerManager().dispatchEvent(new ConnectEvent<PircBotX>(this));
	}
	private void newDisconnectionEvent(){
		this.configuration.getListenerManager().dispatchEvent(new DisconnectEvent<PircBotX>(this, this.getUserChannelDao().createSnapshot(), null));
	}
	private void newDisconnectionEvent(Exception e){
		this.configuration.getListenerManager().dispatchEvent(new DisconnectEvent<PircBotX>(this, this.getUserChannelDao().createSnapshot(), e));
	}
	public void sendMessage(String message){
		socket.sendMessage(message);
	}
	public String getNick(){return socket.getBotName();}
	public boolean isConnected(){return socket.isConnected();}
	protected void shutdown(){socket.close();}
	protected void shutdown(boolean noReconnect){};
	/* 
	 * all the depreciated events
	 */
	@Deprecated
	public void startBot(){}
	@Deprecated
	protected void connect(){}
	@Deprecated
	public void stopBotReconnect(){}
	@Deprecated
	protected void changeSocket(Socket socket){}
	@Deprecated
	protected void startLineProcessing(){}
	@Deprecated
	protected void setNick(String nick){}
	@Deprecated
	public InetAddress getLocalAddress() {return null;}
}
interface WebSocketOutput {
	public void write(String in);
}
