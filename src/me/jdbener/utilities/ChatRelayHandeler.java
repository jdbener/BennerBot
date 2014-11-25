/*
 * This class handles the chat relay system
 * Author: Jdbener
 * Date: 10/6/14
 */
package me.jdbener.utilities;
import me.jdbener.Bennerbot;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

public class ChatRelayHandeler extends ListenerAdapter<PircBotX> {
	//The relay code
	public void onMessage(MessageEvent<PircBotX> e) throws Exception {
		if(!(e.getUser().getNick().equalsIgnoreCase(Bennerbot.twitchu) || e.getUser().toString().equalsIgnoreCase(Bennerbot.hitboxu)))
			//Determine weather or not it is turned on in the code
			if(Bennerbot.conf.get("activateRelay").toString().equalsIgnoreCase("true")){
				//check which server is the opposite server to the one that is sending the code
				String server = "";
				int i=0; while(i < Bennerbot.servers.toArray().length){
					if(e.getBot().getBotId() != i){
						if(Bennerbot.conf.get("showSource").toString().equalsIgnoreCase("true")){server = " ["+Bennerbot.servers.get(i).getName()+"]";}
						Bennerbot.sendMessage(Bennerbot.capitalize(e.getUser().getNick())+server+": "+ e.getMessage().toString(), i, "dont show");
					}
					i++;
				}
			}
	}
}
