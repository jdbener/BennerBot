package me.jdbener.PHitboxBotX;

import java.net.URL;

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
