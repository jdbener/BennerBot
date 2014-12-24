package me.jdbener.utilities;

import java.io.File;
import java.io.FileInputStream;
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
	public int i = 0, posInList = 0;
	@SuppressWarnings("unchecked")
	public AutoMessageHandeler(){
		try {
			Map<Object, String> temp = (Map<Object, String>) Yaml.load(new FileInputStream(new File("config/automessages.yml")));
			for (Entry<Object, String> e: temp.entrySet()){
				Bennerbot.messagesMap.put(e.getKey().toString(), e.getValue());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Runnable runnable = new Runnable() {
		    @Override
			public void run() {
		    	if(Bennerbot.configBoolean("enableAutoMessages")){
		    		i=0;for(Entry<String, String> entry: Bennerbot.messagesMap.entrySet()){
		    			if(i == posInList){
		    				System.out.println(Bennerbot.messagesMap);
		    				Bennerbot.sendMessage(entry.getValue(), "");
		    				posInList++;
		    				break;
		    			}
		    			i++;
		    		}
		    		if(posInList >= Bennerbot.messagesMap.size())
		    			posInList=0;
		    	}
		    }
		};
		
		//add the update to the execution thread
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
		executor.scheduleAtFixedRate(runnable, 0, Integer.parseInt(Bennerbot.conf.get("autoMessageInterval").toString()), TimeUnit.SECONDS);
	}
	
}
