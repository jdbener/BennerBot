package me.jdbener.moderataion;

import me.jdbener.Bennerbot;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

public class CapsFilter extends ListenerAdapter<PircBotX>{
	
	float capsCap = (float) 0.6;
	public void onMessage(MessageEvent<PircBotX> e){
		capsCap = (float)(Double.parseDouble(Bennerbot.getConfigString("MaxCapsPercentage")));
		String msg = e.getMessage();
		boolean filter = false;
		
		int ups = 0;
		int i=0;while(i < msg.length()){
			char c = msg.trim().charAt(i);
			String cs = new String(c+"");
			
			
			if(Character.isLetter(cs.toCharArray()[0])){
				if(cs.equals(cs.toUpperCase()))
					ups++;
			}
			i++;
		}
		if((float)ups/msg.length() > capsCap && msg.length() > 3){
			filter = true;
		}
		
		if(filter == true){
			Bennerbot.sendMessage(Bennerbot.capitalize(e.getUser().getNick())+" has sent a message that has an excessive amount of capitalazation!", e.getBot().getBotId());
			FilterManager.punish(e.getUser().getNick());
		}
	}
}
