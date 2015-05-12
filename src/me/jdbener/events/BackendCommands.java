package me.jdbener.events;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import me.jdbener.Bennerbot;
import me.jdbener.apis.APIManager;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

public class BackendCommands extends ListenerAdapter<PircBotX> {
	public void onMessage(MessageEvent<PircBotX> e){
		String msg = e.getMessage();
		
		
		if(msg.startsWith("!roll")){
			if(e.getMessage().split(" ").length == 2){
				Random rng = new Random();
				int num = rng.nextInt(Integer.parseInt(e.getMessage().split(" ")[1])+1);
				Bennerbot.sendMessage(Bennerbot.capitalize(e.getUser().getNick())+" rolled a "+num+"!");
			} else {
				Random rng = new Random();
				int num = rng.nextInt(100);
				Bennerbot.sendMessage(Bennerbot.capitalize(e.getUser().getNick())+" rolled a "+num+"!");
			}
		} else if(msg.startsWith("!bennerbot") || msg.startsWith("!"+Bennerbot.getConfigString("botName")) || msg.startsWith("!bot")){
			if(msg.equalsIgnoreCase("!bennerbot") || msg.equalsIgnoreCase("!"+Bennerbot.getConfigString("botName")) || msg.equalsIgnoreCase("!bot"))
				Bennerbot.sendMessage("Welcome to "+Bennerbot.getConfigString("botName")+" version "+Bennerbot.version+"! Type !bot help, to for a more compleate list of commands Type !config, to access the Configuration System");
			else if(msg.endsWith("help"))
				Bennerbot.sendMessage("!bot purpose, !bot test, !bot sync");
			else if(msg.endsWith("purpose"))
				Bennerbot.sendMessage("I was designed by my creater, Jdbener, to be the first and only chat bot deisgned to manage both twitch and hitbox channels, i also have a display that allows you to view said chats in one centrefeid location.");
			else if(msg.endsWith("test")){
			if(e.getUser().getNick().equalsIgnoreCase("jdbener") || e.getUser().getNick().equalsIgnoreCase(Bennerbot.twitchu) || e.getUser().getNick().equalsIgnoreCase(Bennerbot.hitboxu) || e.getUser().getNick().equalsIgnoreCase(Bennerbot.getConfigString("twitchChannel").toString()) ||  e.getUser().getNick().equalsIgnoreCase(Bennerbot.getConfigString("hitboxChannel").toString())){
				try{
					Bennerbot.sendMessage("Starting test event");
					Thread.sleep(3000);
					Bennerbot.sendMessage(Bennerbot.name+" "+Bennerbot.version);
					//Thread.sleep(3000);
					//Bennerbot.sendMessage(Bennerbot.manager.getBots().toString());
					//Thread.sleep(3000);
					//Bennerbot.sendMessage(Bennerbot.plugins.toString());
					Thread.sleep(3000);
					Map<String, String> temp = Bennerbot.getConfigMap();
					temp.remove("hitboxPassword");
					temp.remove("twitchOauth");
					Bennerbot.sendMessage(temp.toString());
					Thread.sleep(3600);
					Bennerbot.sendMessage("Twitch: "+Bennerbot.getConfigString("twitchChannel")+" Hitbox: "+Bennerbot.getConfigString("hitboxChannel"));
				} catch(InterruptedException ex){
					ex.printStackTrace();
				}
			} else
				Bennerbot.sendMessage("Sorry this command is for developers only");
			} else if(msg.endsWith("sync")){
				try{
					Connection c = APIManager.getConnection();
					Statement stmt = c.createStatement();
					stmt.execute("DELETE FROM SETTINGS WHERE BID = "+me.jdbener.utill.botId.getBotID());
					for(final Entry<String, String> ex: Bennerbot.getConfigMap().entrySet()){
						new Thread(new Runnable(){
							@Override
							public void run() {
								try {
									Connection c = APIManager.getConnection();
									Statement stmt = c.createStatement();
									stmt.execute("INSERT INTO SETTINGS VALUES ("+me.jdbener.utill.botId.getBotID()+", '"+ex.getKey()+"', '"+ex.getValue()+"')");
									stmt.close();
									c.close();
								} catch (SQLException e1) {
									e1.printStackTrace();
								}
								return;
							}
						}).start();
					}
					stmt.close();
					c.close();
					Bennerbot.sendMessage("Bot Data Synced Successfully");
				} catch (SQLException ex){
					ex.printStackTrace();
				}
			}
		} else if(msg.startsWith("!config")){
			//The !config, and !config help, commands return some generalized information 
			if(msg.equalsIgnoreCase("!config") || msg.equalsIgnoreCase("!config help")){
				Bennerbot.sendMessage("Welcome to the "+Bennerbot.getConfigString("botName")+" Configuaration System. * Type !config list, to list all settings * Type !config help <seting name>, to view more infromation on it. * Type !config change <setting name> <value>, to change a setting.");
			} else if(msg.startsWith("!config list")){
				int page = 0;
				if(msg.split(" ").length == 3){
					page = Integer.parseInt(msg.split(" ")[2]);
				}
				Bennerbot.sendMessage(createList(Bennerbot.getConfigMap(), page, "Type !config list <npage>, to view the next page"));
			} else if(msg.startsWith("!config help")){
				String name = "";
				if(msg.split(" ").length == 3){
					name = msg.split(" ")[2];
				}
				try{
					if(!Bennerbot.getConfigHelp(name).equalsIgnoreCase("")){
						System.out.println(Bennerbot.getConfigHelp(name));
						Bennerbot.sendMessage(Bennerbot.getConfigHelp(name));
					}else
						Bennerbot.sendMessage("Setting not Found, or there is no Help Avaliable");
				} catch(Exception ex){
					Bennerbot.sendMessage("Setting not Found");
				}
			} else if(msg.startsWith("!config change")){
				if(msg.split(" ").length == 4){
					try{
						if(Bennerbot.getConfigMap().containsKey(msg.split(" ")[2])){
							Bennerbot.updateConfigEntry(msg.split(" ")[2], msg.split(" ")[3]);
							Bennerbot.sendMessage("Entry successfully updated");
						} else
							Bennerbot.sendMessage("Entry not Found");
					} catch (Exception ex){Bennerbot.sendMessage("Entry not Found");}
				} else {
					Bennerbot.sendMessage("Wrong format, try !config change <entry> <new value>");
				}
			}
		} else if(msg.startsWith("!perms")){
			if(msg.equalsIgnoreCase("!perms") || msg.equalsIgnoreCase("!perms help")){
				Bennerbot.sendMessage("Welcome to the "+Bennerbot.getConfigString("botName")+" Configuaration System. * Type !config list, to view a list of every permision * Type !perms help <seting name>, to view more infromation about it. * Type !perms update <permision name> <uservalue>, to change a setting.");
			}
		}
	}
	private String createList(Map<String, String> map, int page, String endMSG){
		int pages = 0;
		if((map.size()/10) == (map.size()/10.0))
			pages = (map.size()/10);
		else
			pages = (map.size()/10)-1;
		if(page > pages) page = pages; if(page < 0) page = 0;
		String out = "Page "+page+"/"+pages+"  ";
		if(page == pages){
			for(int i = page*10; i < pages; i++)
				if(!map.keySet().toArray()[i].toString().contains("password") && !map.keySet().toArray()[i].toString().contains("oauth"))
					out+="* "+map.keySet().toArray()[i].toString()+": "+map.get(map.keySet().toArray()[i].toString())+"  ";
		} else {
			for(int i = page*10; i < page*10+10; i++)
				if(!map.keySet().toArray()[i].toString().contains("password") && !map.keySet().toArray()[i].toString().contains("oauth"))
					out+="* "+map.keySet().toArray()[i].toString()+": "+map.get(map.keySet().toArray()[i].toString())+"  ";
			out+=" "+endMSG.replace("<npage>", (page+1)+"");
		}
		return out;
	}
}
