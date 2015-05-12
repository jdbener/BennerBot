package me.jdbener.events;

import me.jdbener.Bennerbot;
import net.xeoh.plugins.base.annotations.PluginImplementation;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.JoinEvent;
import org.pircbotx.hooks.events.PartEvent;

@PluginImplementation
public class JoinLeaveMessages extends ListenerAdapter<PircBotX> {
	
	public void onJoin(JoinEvent<PircBotX> e){
		if(Bennerbot.getConfigBoolean("enableJoinMessages"))
			if(!e.getUser().getNick().equalsIgnoreCase(e.getBot().getNick())){
				e.getChannel().send().message("Welcome "+e.getUser().getNick()+" to the channel!");
			}
	}
	public void onPart(PartEvent<PircBotX> e){
		if(Bennerbot.getConfigBoolean("enableLeaveMessages"))
		e.getChannel().send().message(e.getUser().getNick()+" has left the channel!");
	}

}
