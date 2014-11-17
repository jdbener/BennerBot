package me.jdbener.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.ho.yaml.Yaml;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

import me.jdbener.Bennerbot;

public class CustomCommandHandeler extends ListenerAdapter<PircBotX>{
	public static Map<String, String> commands = new HashMap<String, String>();
	
	public CustomCommandHandeler(){
		setupReplacementTable();
		update();
	}
	
	public void update(){
		try {
			@SuppressWarnings("unchecked")
			Map<String, String> temp = (Map<String, String>) Yaml.load(new FileInputStream(new File("config/commands.yml")));
			
			for (Entry<String, String> entry : temp.entrySet()){
				if(entry.getKey().toString().startsWith("~")){
					Bennerbot.variableMap.put("<"+entry.getKey().replace("~", "")+">", entry.getValue());
				} else {
					commands.put(entry.getKey(), entry.getValue());
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private void setupReplacementTable(){
		Bennerbot.variableMap.put("<botname>", Bennerbot.name);
		Bennerbot.variableMap.put("<botversion>", Bennerbot.version);
		Bennerbot.variableMap.put("<twitchhost>", Bennerbot.conf.get("twitchChannel").toString().toLowerCase().trim());
		Bennerbot.variableMap.put("<hitboxhost>", Bennerbot.conf.get("hitboxChannel").toString().toLowerCase().trim());
	}

	public void onMessage(MessageEvent<PircBotX> e) throws Exception {
		update();
		Bennerbot.variableMap.put("<username>", e.getUser().getNick());
		for (Entry<String, String> entry : commands.entrySet()){
			if(e.getMessage().equalsIgnoreCase("!"+entry.getKey())){
				e.getChannel().send().message(Bennerbot.replaceVariables(entry.getValue()));
			}
		}
		
		
	}
}
