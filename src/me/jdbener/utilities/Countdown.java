package me.jdbener.utilities;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import me.jdbener.Bennerbot;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

public class Countdown extends ListenerAdapter<PircBotX> {
	private MessageEvent<PircBotX> ev;
	private static int running = 0;
	public void onMessage(MessageEvent<PircBotX> e){
		if(e.getMessage().startsWith("!countdown")){
			if(e.getMessage().split(" ").length == 2){
				ev = e;
				Runnable runnable = new Runnable() {
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
				};

				ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);
				executor.execute(runnable);
			} else if(e.getMessage().split(" ").length == 3){
				ev = e;
				Runnable runnable = new Runnable() {
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
				};

				ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);
				executor.execute(runnable);
			} else if(e.getMessage().split(" ").length >= 4){
				ev = e;
				Runnable runnable = new Runnable() {
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
				};

				ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);
				executor.execute(runnable);
			}
		}
	}
}
