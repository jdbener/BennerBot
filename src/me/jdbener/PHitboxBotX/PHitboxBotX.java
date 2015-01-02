package me.jdbener.PHitboxBotX;

import java.net.InetAddress;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;

import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.Utils;
import org.pircbotx.hooks.events.ConnectEvent;
import org.pircbotx.hooks.events.DisconnectEvent;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.managers.ListenerManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PHitboxBotX extends PircBotX{
	String user, pass, channel;
	HitboxWebsocket socket;
	public static Logger log = LoggerFactory.getLogger(PHitboxBotX.class);
	HitboxSocketOutput stream = new HitboxSocketOutput(){
		@Override
		public void write(String in) {
			//chat message
			if(in.startsWith("5::") && in.contains("\"method\":\"chatMsg\"") && !in.contains("\"extra-info\":\"from-bot\"")){
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
			//output message
			}else if(in.startsWith("5::") && in.contains("\"method\":\"chatMsg\"") && in.contains("\"extra-info\":\"from-bot\"")){
				log.info("message sent");
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
		
		// Recover all the information to launch the connection to the chat
		String IP = HitboxWebsocket.getServerIP();
		log.info("Getting Hitbox server IP: "+IP);
		String ID = HitboxWebsocket.getConnectionID(IP);
		log.info("Getting connection ID: "+ID);
		log.info("Getting your token");
		HitboxWebsocket.getToken(user, pass);
		
		try {
			socket = new HitboxWebsocket(new URI("ws://" + IP + "/socket.io/1/websocket/" + ID), user, pass, channel);
			socket.addOutput(stream);
			log.info("Sucessfully Connected... Joined Channel "+channel);
		} catch (URISyntaxException e) {e.printStackTrace();}
		
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