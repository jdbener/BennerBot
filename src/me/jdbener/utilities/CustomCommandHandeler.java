package me.jdbener.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.ho.yaml.Yaml;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

import me.jdbener.Bennerbot;
import me.jdbener.moderataion.FilterManager;

@SuppressWarnings("unused")
public class CustomCommandHandeler extends ListenerAdapter<PircBotX>{
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
					if(Bennerbot.commandMap.containsKey(Bennerbot.removeLastChar(entry.getKey()))){
						Bennerbot.commandMap.put(Bennerbot.removeLastChar(entry.getKey()), Bennerbot.commandMap.get(Bennerbot.removeLastChar(entry.getKey())).split(":")[0]+"~"+entry.getValue()+":"+Bennerbot.commandMap.get(Bennerbot.removeLastChar(entry.getKey())).split(":")[1]);
					} else {
						Bennerbot.commandMap.put(entry.getKey(), entry.getValue()+":0");
					}
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
		for (Entry<String, String> entry : Bennerbot.commandMap.entrySet()){
			if(e.getMessage().startsWith("!"+entry.getKey())){
				int which = 0;
				if(e.getMessage().split(" ").length>1)
					which = Integer.parseInt(e.getMessage().split(" ")[1]);
				if(which<0)which=0;if(which>entry.getValue().split("~").length)which=entry.getValue().split("~").length;
				System.out.println(which);
				e.getChannel().send().message(replaceVariables(entry.getValue().split("~")[which]).split(":")[0]);
			}
		}
		if(e.getMessage().equalsIgnoreCase("!commands")){
			Runnable run = new Runnable(){
				@Override
				public void run() {
					String out = "";
					int i = 0;
					for(Entry<String, String> entry: Bennerbot.commandMap.entrySet()){
						if(i != 0){
							out+=(i+") !"+entry.getKey()+"   ");
						}
						i++;
					}
					Bennerbot.sendMessage(out);
				}
			};
			
			Executors.newScheduledThreadPool(2).execute(run);
		}
		/*if(e.getMessage().startsWith("!switch")){
			//if(Bennerbot.isMod(e.getUser(), e.getChannel())){
				System.out.println(e.getMessage().split(" ").length);
				if(e.getMessage().split(" ").length == 2){
					try{
						String key = e.getMessage().split(" ")[1];
						String msg = Bennerbot.commandMap.get(key).split(":")[0];
						int which = Integer.parseInt(Bennerbot.commandMap.get(key).split(":")[1]);
						int count = Bennerbot.commandMap.get(key).split("~").length-1;
						
						if(which+1>count)which=0;else which++;
						
						System.out.println(count);
						
						Bennerbot.commandMap.put(key, msg+":"+which);
						
						Bennerbot.sendMessage("Command: \""+key+"\" has successfully been updated to entry #"+which);
						
						System.out.println(Bennerbot.commandMap.get(key));
					} catch (Exception ex) {
						ex.printStackTrace();
						Bennerbot.sendMessage("It looks like something has gone wrong :(");
					}
				} else if(e.getMessage().split(" ").length == 3){
					try{
						String key = e.getMessage().split(" ")[1];
						String msg = Bennerbot.commandMap.get(key).split(":")[0];
						int which = Integer.parseInt(e.getMessage().split(" ")[2]);
						int count = Bennerbot.commandMap.get(key).split("~").length-1;
					
						if(which>count)which=count;if(which<0)which=0;
			
						Bennerbot.commandMap.put(key, msg+":"+which);
						
						Bennerbot.sendMessage("Command: \""+key+"\" has successfully been updated to entry #"+which);
					} catch (Exception ex) {
						ex.printStackTrace();
						Bennerbot.sendMessage("It looks like something has gone wrong :(");
					}
				} else {
					Bennerbot.sendMessage("It look like you are using the wrong format for this message, try using !song <command> or !song <command> <entry>");
				}
			//} else {
			//	Bennerbot.sendMessage(Bennerbot.capitalize(e.getUser().getNick())+" has tried to use a command they dont have permsision to");
			//	FilterManager.punish(e.getUser().getNick());
			//}
		}*/
	}
	/**
	 * This function will replace any variables in the string that is passed to it, with there appropriate values.
	 */
	public static String replaceVariables(String in){
		//irate through all of the variables
		for (Map.Entry<String, String> entry : Bennerbot.variableMap.entrySet()) {
	        //replace any occurrences of the variable
			in = in.replaceAll(entry.getKey(), entry.getValue());
	    }
		return in;
	}
}
