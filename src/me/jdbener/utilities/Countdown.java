package me.jdbener.utilities;

import java.util.ArrayList;

import me.jdbener.Bennerbot;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

public class Countdown extends ListenerAdapter<PircBotX> {
	private MessageEvent<PircBotX> ev;
	private static int running = 0;
	ArrayList<Thread> activeCountdowns = new ArrayList<Thread>();
	@SuppressWarnings("deprecation")
	public void onMessage(MessageEvent<PircBotX> e){
		if(e.getMessage().startsWith("!countdown")){
			if(e.getMessage().split(" ").length == 2){
				ev = e;
				Thread t = new Thread(new Runnable() {
				    public void run() {
				    	int length = Integer.parseInt(ev.getMessage().split(" ")[1]);
						int time = length/5;
						int name = running+1;
						running++;
						
						for(int i = 0; i< time; i++) {
						    try{
						        Bennerbot.sendMessage("Countdown #"+name+", "+length+"...");
						        
						        length = length-5;
						        
						        Thread.sleep(5000);
						    } catch(InterruptedException ie) {}
						}
						Bennerbot.sendMessage("DONE!");
						running--;
				    }
				});
				t.start();
				activeCountdowns.add(t);
			} else if(e.getMessage().split(" ").length == 3){
				ev = e;
				Thread t = new Thread(new Runnable() {
				    public void run() {
				    	int length = Integer.parseInt(ev.getMessage().split(" ")[1]);
				    	int interval = Integer.parseInt(ev.getMessage().split(" ")[2]);
				    	if(interval<1)interval = 1;
				    	
						int time = length/interval;
						int name = running+1;
						running++;
						
						for(int i = 0; i< time; i++) {
						    try{
						        Bennerbot.sendMessage("Countdown #"+name+", "+length+"...");
						        
						        length = length-interval;
						        
						        Thread.sleep(interval*1000);
						    } catch(InterruptedException ie) {}
						}
						Bennerbot.sendMessage("DONE!");
						running--;
				    }
				});

				t.start();
				activeCountdowns.add(t);
			} else if(e.getMessage().split(" ").length >= 4){
				ev = e;
				Thread t = new Thread(new Runnable() {
				    public void run() {
				    	int length = Integer.parseInt(ev.getMessage().split(" ")[1]);
				    	int interval = Integer.parseInt(ev.getMessage().split(" ")[2]);
				    	if(interval<1)interval = 1;
				    	
						int time = length/interval;
						String name = "";
						for(int i=3; i<ev.getMessage().split(" ").length; i++)
							name+=" "+ev.getMessage().split(" ")[i];
								
						running++;
						
						for(int i = 0; i< time; i++) {
						    try{
						        Bennerbot.sendMessage(name+", "+length+"...");
						        
						        length = length-interval;
						        
						        Thread.sleep(interval*1000);
						    } catch(InterruptedException ie) {}
						}
						Bennerbot.sendMessage("DONE!");
						running--;
				    }
				});

				t.start();
				activeCountdowns.add(t);
			}
		}
		if(e.getMessage().startsWith("!stopcountdown")){
			if(e.getMessage().split(" ").length == 2){
				try{
					int cid = Integer.parseInt(e.getMessage().split(" ")[1]) - 1;
					activeCountdowns.get(cid).stop();
					activeCountdowns.remove(cid);
					Bennerbot.sendMessage("Successfully stopped the countdown!", "");
				} catch (Exception ex){
					ex.printStackTrace();
					Bennerbot.sendMessage("An error has occurred", "");
				}
			} else {
				Bennerbot.sendMessage("Please use the correct format, !stopcountdown <countdown id>", "");
			}
		}
	}
}
