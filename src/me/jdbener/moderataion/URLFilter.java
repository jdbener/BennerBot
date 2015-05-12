package me.jdbener.moderataion;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.jdbener.Bennerbot;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

public class URLFilter extends ListenerAdapter<PircBotX>{
	int whiteorblacklist = -1;
	public void onMessage(MessageEvent<PircBotX> e){
		whiteorblacklist = Integer.parseInt(Bennerbot.getConfigString("URLWhiteORBlackList"));
		if(whiteorblacklist != -1 || whiteorblacklist != 1)
			whiteorblacklist = -1;
			
		int filter = 0;
		final Pattern urlPattern = Pattern.compile(
		        "(?:^|[\\W])((ht|f)tp(s?):\\/\\/|www\\.)"
		                + "(([\\w\\-]+\\.){1,}?([\\w\\-.~]+\\/?)*"
		                + "[\\p{Alnum}.,%_=?&#\\-+()\\[\\]\\*$~@!:/{};']*)",
		        Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
		
		
		Matcher matcher = urlPattern.matcher(e.getMessage());
		while (matcher.find()) {
		    int matchStart = matcher.start(1);
		    int matchEnd = matcher.end();
		    String url = e.getMessage().substring(matchStart, matchEnd);

		    String allowed = Bennerbot.readFile(new File("config/urlwhite-blacklist.txt").toString());
				
			for(String allow: allowed.split("[\\r\\n]+")){
				if(filter == 0 && filter != -1){
					if(!url.contains(allow)){
						filter = -1;
					} else {
						filter = 1;
					}
				}
			}
		}


		if(filter == whiteorblacklist){
			Bennerbot.sendMessage(Bennerbot.capitalize(e.getUser().getNick())+" has sent a message that has a url in it!", e.getBot().getBotId());
			FilterManager.punish(e.getUser().getNick());
		}
		
	}
}
