/*
 * This class handles the chat relay system
 * Author: Jdbener
 * Date: 10/6/14
 */
package me.jdbener.events;

import me.jdbener.Bennerbot;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

public class ChatRelayHandeler extends ListenerAdapter<PircBotX> {
	//The relay code
	public void onMessage(MessageEvent<PircBotX> e) throws Exception {
		boolean bot = false;
		if(e.getUser().getNick().toString().contains("bot"))bot = true;
		if(!bot)
			//Determine weather or not it is turned on in the code
			if(Bennerbot.conf.get("activateRelay").toString().equalsIgnoreCase("true") /*&& Bennerbot.servers.size() > 1*/){
				//check which server is the opposite server to the one that is sending the code
				String server = "";
				int i=0; while(i < Bennerbot.servers.toArray().length){
					if(e.getBot().getBotId() != i || i == Bennerbot.getBotIDbyName("Hitbox")){
						//Determine weather or not to show the message's source
						System.out.println(e.getBot().getConfiguration().getServerHostname());
						server = " ["+Bennerbot.servers.get(Bennerbot.getBotIDbyURL(e.getBot().getConfiguration().getServerHostname())).getName()+"]";
						//send the message
						Bennerbot.sendMessage(Bennerbot.capitalize(e.getUser().getNick())+server+": "+ e.getMessage().toString(), i, "dont show");
					}
					i++;
				}
			}
	}
}
