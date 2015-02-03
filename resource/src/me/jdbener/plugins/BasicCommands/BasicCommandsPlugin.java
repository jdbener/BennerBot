package me.jdbener.plugins.BasicCommands;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import me.jdbener.BennerBotPlugin;
import me.jdbener.Bennerbot;
import net.xeoh.plugins.base.annotations.PluginImplementation;
import net.xeoh.plugins.base.annotations.Timer;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

//this annotation is necessary for this file to be considered a plugin
@PluginImplementation
public class BasicCommandsPlugin extends ListenerAdapter<PircBotX> implements BennerBotPlugin {
	// this is a basic system for returning a message to a user 
	public void onMessage(MessageEvent<PircBotX> e) throws Exception {
		/*
		 * !hello, responds with hello to however sent the commands
		 */
		if(e.getMessage().equalsIgnoreCase("!hello")){
			e.getChannel().send().message("Hey, "+e.getUser().getNick()+"!");
		}
		/*
		 * !time, returns the current date and time of the host
		 */
		if(e.getMessage().equalsIgnoreCase("!time")){
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
			Calendar cal = Calendar.getInstance();
			e.getChannel().send().message("The date and time is "+dateFormat.format(cal.getTime()));
		}
		/*
		 * !welcome <username>, will send a welcome message to the specified user
		 */
		if(e.getMessage().split(" ")[0].equalsIgnoreCase("!welcome")){
			if(e.getMessage().split(" ").length > 1){
				e.getChannel().send().message("Welcome "+e.getMessage().split(" ")[1]+"!");
			} else {
				e.getChannel().send().message(e.getUser().getNick()+": Please use the correct format next time: !welcome <username>");
			}
		}
	}
	
	/*
	 * This determines the name of this particular plugin
	 */
	@Override
	public String getName() {
		return "Basic Commands";
	}
	/*
	 * This function is run every second and will update the variable Time with the current date.
	 * It would be better to do this in an onMessage event but, i needed a timer example
	 */
	@Timer(period=1000)
	public void updateVariable(){
		//updates the variable time with the current time
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		Calendar cal = Calendar.getInstance();
		Bennerbot.variableMap.put("<time>", dateFormat.format(cal.getTime()));
	}
	/*
	 * This is where you will run any code that needs to be run when the plugin loads
	 */
	@Override
	public void inititate() {
		//These two lines of code (ignoring the logger) will add
		Bennerbot.listener.addListener(this);
		Bennerbot.logger.info("Added listener");
		
		//adds a variable named time and gives it the time value
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		Calendar cal = Calendar.getInstance();
		Bennerbot.variableMap.put("<time>", dateFormat.format(cal.getTime()));
	}
	
	/**
	 * We have no need for user output in the file
	 */
	@Override
	public void onOperatorOuput(String txt) {
		try {
			onMessage(Bennerbot.GenerateMessageEvent(txt));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
