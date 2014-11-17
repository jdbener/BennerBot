package me.jdbener.utilities;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.JoinEvent;

public class BotJoinHandeler extends ListenerAdapter<PircBotX>{
	public void onJoin(JoinEvent<PircBotX> e){
		if(e.getUser().getNick().equalsIgnoreCase(e.getBot().getNick())){
			if(!me.jdbener.Bennerbot.conf.get("incognitoMode").toString().equalsIgnoreCase("true")){
				String name = new String(e.getChannel().getName().replace('#', ' '));name = name.substring(1, 2).toUpperCase() + name.substring(2); 
				e.getChannel().send().message("Hello i am "+me.jdbener.Bennerbot.name+" "+me.jdbener.Bennerbot.version+", a ChatBot specially coded for "+name+", by Jdbener!");
			}
		}
	}
}
