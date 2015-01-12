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
	private String lastMessage = " ";
	//The relay code
	public void onMessage(MessageEvent<PircBotX> e) throws Exception {
		boolean bot = false;
		if(e.getUser().getNick().toString().contains("bot"))bot = true;
		//if(!(e.getUser().getNick().equalsIgnoreCase(Bennerbot.twitchu) || e.getUser().toString().equalsIgnoreCase(Bennerbot.hitboxu)))
		if(!bot)
			//Determine weather or not it is turned on in the code
			if(Bennerbot.conf.get("activateRelay").toString().equalsIgnoreCase("true")){
				//check which server is the opposite server to the one that is sending the code
				String server = "";
				if(!e.getMessage().endsWith(lastMessage)){
					int i=0; while(i < Bennerbot.servers.toArray().length){
						if(e.getBot().getBotId() != i){
							//Determine weather or not to show the message's source
							if(Bennerbot.conf.get("showSource").toString().equalsIgnoreCase("true")){server = " ["+Bennerbot.servers.get(e.getBot().getBotId()).getName()+"]";}
							//send the message
							Bennerbot.sendMessage(Bennerbot.capitalize(e.getUser().getNick())+server+": "+ e.getMessage().toString(), i, "dont show");
						}
						i++;
					}
				}
			//Assign the last message to prevent spam
			lastMessage = e.getMessage().toString();
		}
	}
}
