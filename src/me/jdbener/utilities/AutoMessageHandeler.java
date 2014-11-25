package me.jdbener.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import me.jdbener.Bennerbot;

import org.ho.yaml.Yaml;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;

public class AutoMessageHandeler extends ListenerAdapter<PircBotX>{
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map<String, String> messages = new HashMap();
	public int i = 0, posInList = 0;
	
	@SuppressWarnings({"unchecked"})
	public AutoMessageHandeler(){
		try {
			messages = (Map<String, String>) Yaml.load(new FileInputStream(new File("config/automessages.yml")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		Runnable runnable = new Runnable() {
		    @Override
			public void run() {
		    	i=0;for(Entry<String, String> entry: messages.entrySet()){
					if(i == posInList){
						Bennerbot.sendMessage(entry.getValue(), "");
						posInList++;
						break;
					}
					i++;
				}
		    	if(posInList >= messages.size())
		    		posInList=0;
		    }
		};
		
		//add the update to the execution thread
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
		executor.scheduleAtFixedRate(runnable, 0, Integer.parseInt(Bennerbot.conf.get("autoMessageInterval").toString()), TimeUnit.SECONDS);
	}
	
}
