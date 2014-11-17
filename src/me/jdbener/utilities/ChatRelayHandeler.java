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
		//Determine weather or not it is turned on in the code
		if(Bennerbot.conf.get("activateRelay").toString().equalsIgnoreCase("true") && !(Bennerbot.conf.get("twitchChannel").toString().equalsIgnoreCase("bennerbot") || Bennerbot.conf.get("hitboxChannel").toString().equalsIgnoreCase("bennerbot"))){
			//check which server is the opposite server to the one that is sending the code
			String server = "";
			//Hitbox
			if(e.getBot().getServerInfo().getNetwork() != null){
				if(Bennerbot.conf.get("showSource").toString().equalsIgnoreCase("true")){server = " [hitbox]";}
					Bennerbot.manager.getBotById(0).sendRaw().rawLine("PRIVMSG #"+Bennerbot.conf.get("twitchChannel").toString().toLowerCase()+" :"+Bennerbot.capitalize(e.getUser().getNick())+server+": "+e.getMessage().toString());
			//Twitch
			} else {
				if(Bennerbot.conf.get("showSource").toString().equalsIgnoreCase("true")){server = " [twitch]";}
					Bennerbot.manager.getBotById(1).sendRaw().rawLine("PRIVMSG #"+Bennerbot.conf.get("hitboxChannel").toString().toLowerCase()+" :" +Character.toUpperCase(e.getUser().getNick().charAt(0)) +e.getUser().getNick().substring(1)+server+": "+ e.getMessage().toString());
			}
		}
		
	
	}
}
