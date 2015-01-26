package me.jdbener;
/**
 * This class is what creates the output files
 * Author: Jdbener (Joshua Dahl)
 * Date: 11/8/14
 */


//TODO add pretty scrolling to the output file
//TODO change the TTS system so that the speak method is called when a message is displayed, so that there isent a delay between when the message is spoken and when it is displayed.

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.jdbener.apis.APIManager;
import me.jdbener.speech.TextToSpeachManager;
import me.jdbener.utill.Server;
import me.jdbener.utill.UserColors;
import me.jdbener.utill.user;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.ConnectEvent;
import org.pircbotx.hooks.events.JoinEvent;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.PartEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;

public class Cleaner extends ListenerAdapter<PircBotX>{
	//this variable is a reference to some of the color utitlites used by the code
	public static UserColors UC = new UserColors();
	
	/**
	 * this function sets up the beginning of the files
	 */
	public Cleaner(){
		//Deletes both file which clears them.
		new File("output.html").delete();
		new File("output.txt").delete();

		try {
			//if new file creation didnt fail
			if(new File("output.html").createNewFile())
				//if the write type is turned on
				if(Bennerbot.conf.get("WriteClean").toString().equalsIgnoreCase("true"))
					//write the beggining html
					Bennerbot.write("<html>\n<head>\n<title>"+Bennerbot.name+" v"+Bennerbot.version+" ~ Chat</title><link rel=\"stylesheet\" type=\"text/css\" href=\"resource/layout.css\" media=\"screen\" /><script src=\"//ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js\" ></script>\n</head><body onLoad=\"setInterval('window.scrollTo(0,document.body.scrollHeight);location.reload();', 1000);\">\n<center><h1>Welcome to "+Bennerbot.name+"</h1>Version "+me.jdbener.Bennerbot.version+"</center>\n", new File("output.html"), false);
			//if new creation didnt fail
			if(new File("output.txt").createNewFile())
				//if the write type is turned on
				if(Bennerbot.conf.get("WriteDirty").toString().equalsIgnoreCase("true"))
					//write the welcome message
					Bennerbot.write("Welcome to "+Bennerbot.name+"\nVersion "+me.jdbener.Bennerbot.version+"\n", new File("output.txt"));
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * This function is called when the bot connect to the server
	 */
	public void onConnect(ConnectEvent<PircBotX> e){
		//the name of the bot
		/*String user = Bennerbot.servers.get(e.getBot().getBotId()).getUser();
		//the bots color
		Color color = Color.red;
		//check if the bots name is in the color file 
		if(!UC.inFile(user)){
			// add it if its not
			UC.addUserColor(user, Color.red);
		}
		//sets the color to the color in the file 
		for(user uc: UC.getUserColors()){
			if(uc.getUser().equalsIgnoreCase(user))
				color=uc.getColor();
		}
		//set the color on the server to the color set in the file
		//Bennerbot.sendMessage("/color "+UserColors.rgb2Hex(color), new String());*/
		
		if(e.getBot().getBotId() == Bennerbot.getBotIDbyName("Twitch"))
			e.getBot().sendRaw().rawLine("TWITCHCLIENT 1");
	
		//display connect message
		onOutput("Sucessfully Connected to "+Bennerbot.servers.get(e.getBot().getBotId()).getName());
	}
	
	/**
	 * This function is called whenever somebody says something
	 */
	public void onMessage(MessageEvent<PircBotX> e){
		//initialize variables
		String clean = "", dirty, server, serverd, mod="", user, message;
		Color color = Color.gray;
		
		Date date = new Date();
		
		//set the server text/image
		int id = e.getBot().getBotId()-1; if(id<0)id=0;if(id>Bennerbot.servers.size())id=Bennerbot.servers.size();
		Server s = Bennerbot.servers.get(id);
		server = "<img class='server' src=\'"+s.getLogo()+"\'>";
		serverd = s.getName();
		
		//the user is the user who sent the message
		user = e.getUser().getNick();
		//gets the message that was sent
		message = APIManager.filterEmotes(filterURLS(Bennerbot.filterUTF8(e.getMessage())));
	
		color = UC.getColorForUser(user);
		//check if the user sending the message is a moderator or not
		if(Bennerbot.isMod(e.getUser(), e.getChannel()))
			mod = "<img class='badge' src=\'http://help.twitch.tv/customer/portal/attachments/349943'>";
		//check if the user sending the message is a broadcaster or not
		else if(e.getUser().getNick().equalsIgnoreCase(Bennerbot.conf.get("twitchChannel").toString()) || e.getUser().getNick().equalsIgnoreCase(Bennerbot.conf.get("hitboxChannel").toString()))
			mod = "<img class='badge' src=\'http://help.twitch.tv/customer/portal/attachments/349942'>";
		//capitalize the user's name
		user=Bennerbot.capitalize(user);
		//this long block of code will shorten the user's name, or not depending
		if(Bennerbot.conf.get("nameShortener").toString().equalsIgnoreCase("true")){
			if(e.getUser().getNick().length() > 9){
				user=user.substring(0,9)+"... :";
			}
		} else {
			if (user.length() >= 32){
				user=user.substring(0,32)+":";
			} else { 
				user+=":";
			}
		}
		//weather to write to the file by default
		boolean write = true;
		//if the message was sent by a bot dont display it
		if(user.endsWith("bot") && Bennerbot.conf.get("filterBots").toString().equalsIgnoreCase("true"))
			write = false;
		//if the message is a command, dont display it
		if(message.startsWith("!") && Bennerbot.conf.get("filterCommands").toString().equalsIgnoreCase("true"))
			write = false;
		
		clean = Bennerbot.conf.get("DisplayMessageFormat").toString()
					.replace("<server>", server)
					.replace("<badge>", mod)
					.replace("<timestamp>", "<span class='timestamp'>"+Bennerbot.dateFormat.format(date)+"</span>")
					.replace("<color>", "<span class='color'>"+UserColors.rgb2Hex(color)+"</span>")
					.replace("<user>", "<span class='username' style='color:"+UserColors.rgb2Hex(color)+"'>"+user.replace(":", "")+"</span>")
					.replace("<noformatuser>", "<span class='username'>"+user.replace(":", "")+"</span>")
					.replace("<message>", "<span class='message'>"+message+"</span>")
					.replace("<br>", "");
		if(((message.contains(Bennerbot.conf.get("twitchChannel").toString()) || message.contains(Bennerbot.conf.get("hitboxChannel").toString())) && Bennerbot.conf.get("HighlighMessages").toString().equalsIgnoreCase("true")))
			clean = "<span class=\'message highlight\'>"+clean+"</div>";
		else
			clean = "<span class=\'message\'>"+clean+"</div>";
		clean = clean+"<br>\n";
		
		//dirty output
		dirty = serverd+" ["+Bennerbot.dateFormat.format(date)+"] "+e.getUser().getNick()+": "+message+"\n";
		
		//write the cleaned file if allowed
		if(Bennerbot.conf.get("WriteClean").toString().equalsIgnoreCase("true") && write)
			Bennerbot.write(clean, new File("output.html"));
		//write the dirty file if allowed
		if(Bennerbot.conf.get("WriteDirty").toString().equalsIgnoreCase("true") && write)
			Bennerbot.write(dirty, new File("output.txt"));
		
		if(Bennerbot.configBoolean("enableTTS"))
			TextToSpeachManager.read(e);
	}
	/**
	 * this function is called whenever somebody joins the channel
	 */
	public void onJoin(JoinEvent<PircBotX> e){
		//if the message type is enabled in the config folder
		if(Bennerbot.conf.get("enableJoinMessages").toString().equalsIgnoreCase("true") && Bennerbot.conf.get("enableBotMessages").toString().equalsIgnoreCase("true")){
			//initialize variables
			String clean, dirty, server, user, message;
			Color color = Color.gray;
			
			Date date = new Date();
			
			//this information dose not have to be determined
			server = "<img class='server' src=\'"+Bennerbot.getPath(new File("resource/OutputLogo.png"))+"\'>";
			user = Bennerbot.name;
			message = "Welcome "+e.getUser().getNick()+" to the channel!";
			
			//color information
			if(!UC.inFile(user)){
				 UC.addUserColor(user, Color.red);
			 }
			for(user uc: UC.getUserColors()){
				if(uc.getUser().equalsIgnoreCase(user))
					color=uc.getColor();
			}
			
			//the clean format
			//clean = server+Bennerbot.dateFormat.format(date)+" <span style=\'color:"+UserColors.rgb2Hex(color)+"\'>"+user+"</span>: "+filterURLS(APIManager.filterEmotes(message))+"<br>\n";
			//clean = "<span class='server'>"+server+"</span> <span class='timestamp'>"+Bennerbot.dateFormat.format(date)+"</span> <span class='username' style=\'color:"+UserColors.rgb2Hex(color)+"\'>"+user+"</span> <span class='message'>"+APIManager.filterEmotes(filterURLS(message))+"</span><br>\n";
			clean = Bennerbot.conf.get("DisplayMessageFormat").toString()
					.replace("<server>", server)
					.replace("<badge>", "")
					.replace("<timestamp>", "<span class='timestamp'>"+Bennerbot.dateFormat.format(date)+"</span>")
					.replace("<color>", "<span class='color'>"+UserColors.rgb2Hex(color)+"</span>")
					.replace("<user>", "<span class='username' style='color:"+UserColors.rgb2Hex(color)+"'>"+user+"</span>")
					.replace("<noformatuser>", "<span class='username'>"+user+"</span>")
					.replace("<message>", "<span class='message'>"+message+"</span>")
					.replace("<br>", "");
			if(((message.contains(Bennerbot.conf.get("twitchChannel").toString()) || message.contains(Bennerbot.conf.get("hitboxChannel").toString())) && Bennerbot.conf.get("HighlighMessages").toString().equalsIgnoreCase("true")))
				clean = "<span class=\'message highlight\'>"+clean+"</div>";
			else
				clean = "<span class=\'message\'>"+clean+"</div>";
			clean = clean+"<br>\n";
			
			//the dirty format
			dirty = "["+Bennerbot.dateFormat.format(date)+"] "+message+"\n";
			
			//write the output to the appropriate files
			if(Bennerbot.conf.get("WriteClean").toString().equalsIgnoreCase("true") || Bennerbot.conf.get("OutputGUI").toString().equalsIgnoreCase("true"))
				Bennerbot.write(clean, new File("output.html"));
			if(Bennerbot.conf.get("WriteDirty").toString().equalsIgnoreCase("true"))
				Bennerbot.write(dirty, new File("output.txt"));
		}
	}
	/**
	 * this function is called whenever a user leaves the channel
	 */
	public void onPart(PartEvent<PircBotX> e ){
		//if leave messages are enabled in the config
		if(Bennerbot.conf.get("enableLeaveMessages").toString().equalsIgnoreCase("true") && Bennerbot.conf.get("enableBotMessages").toString().equalsIgnoreCase("true")){
			//Initialize variables
			String clean, dirty, server, user, message;
			Color color = Color.gray;
			
			Date date = new Date();
		
			server = "<img class='server' src=\'"+Bennerbot.getPath(new File("resource/OutputLogo.png"))+"\'>";
			user = Bennerbot.name;
			message = e.getUser().getNick()+" has left the channel!";
			
			//Initialize colors
			if(!UC.inFile(user)){
				 UC.addUserColor(user, Color.red);
			 }
			for(user uc: UC.getUserColors()){
				if(uc.getUser().equalsIgnoreCase(user))
					color=uc.getColor();
			}
			
			//output
			//clean = server+Bennerbot.dateFormat.format(date)+" <span style=\'color:"+UserColors.rgb2Hex(color)+"\'>"+user+"</span>: "+filterURLS(APIManager.filterEmotes(message))+"<br>\n";
			clean = Bennerbot.conf.get("DisplayMessageFormat").toString()
					.replace("<server>", server)
					.replace("<badge>", "")
					.replace("<timestamp>", "<span class='timestamp'>"+Bennerbot.dateFormat.format(date)+"</span>")
					.replace("<color>", "<span class='color'>"+UserColors.rgb2Hex(color)+"</span>")
					.replace("<user>", "<span class='username' style='color:"+UserColors.rgb2Hex(color)+"'>"+user+"</span>")
					.replace("<noformatuser>", "<span class='username'>"+user+"</span>")
					.replace("<message>", "<span class='message'>"+message+"</span>")
					.replace("<br>", "");
			if(((message.contains(Bennerbot.conf.get("twitchChannel").toString()) || message.contains(Bennerbot.conf.get("hitboxChannel").toString())) && Bennerbot.conf.get("HighlighMessages").toString().equalsIgnoreCase("true")))
				clean = "<span id=\'highlight\'>"+clean+"</div>";
			clean = clean+"<br>\n";
			dirty = "["+Bennerbot.dateFormat.format(date)+"] "+message+"\n";
			
			if(Bennerbot.conf.get("WriteClean").toString().equalsIgnoreCase("true") || Bennerbot.conf.get("OutputGUI").toString().equalsIgnoreCase("true"))
				Bennerbot.write(clean, new File("output.html"));
			if(Bennerbot.conf.get("WriteDirty").toString().equalsIgnoreCase("true"))
				Bennerbot.write(dirty, new File("output.txt"));
		}
	}
	/**
	 * this function is called whenever a private message is sent to the bot
	 */
	public void onPrivateMessage(PrivateMessageEvent<PircBotX> e){
		//if the message is from twitch
		if(e.getUser().getNick().equalsIgnoreCase("JTV")){
			//is the message worth showing?
			boolean trash = (e.getMessage().contains("HISTORYEND") ||
							e.getMessage().contains("USERCOLOR") || 
							e.getMessage().contains("Your color has been changed") || 
							e.getMessage().contains("HOSTTARGET")||
							e.getMessage().contains("EMOTESET"));
			if(!trash){
				//Initialize variables
				String clean, dirty;
				
				//display
				clean = "<span class='message error'>"+e.getMessage()+"</div><br>\n";
				dirty = e.getMessage();
			
				if(Bennerbot.conf.get("WriteClean").toString().equalsIgnoreCase("true") || Bennerbot.conf.get("OutputGUI").toString().equalsIgnoreCase("true"))
					Bennerbot.write(clean, new File("output.html"));
				if(Bennerbot.conf.get("WriteDirty").toString().equalsIgnoreCase("true"))
					Bennerbot.write(dirty, new File("output.txt"));
			}
			if(e.getMessage().startsWith("USERCOLOR")){
				//TODO expanding here
				String user = e.getMessage().split(" ")[1];
				String color = e.getMessage().split(" ")[2];
				
				UC.UpdateUserColor(user, UserColors.hex2Rgb(color));
			}
		} else {
			e.respond("Please dont private messgae me");
		}
	}
	/**
	 * this function is called whenever the bot sends a message
	 * @param txt ~ the message that was sent
	 */
	public static void onOutput(String txt){
		//standard
		if(Bennerbot.conf.get("enablePluginMessages").toString().equalsIgnoreCase("true") && Bennerbot.conf.get("enableBotMessages").toString().equalsIgnoreCase("true")){
			String clean, dirty, server, user, message;
			boolean display = true;
			Color color = Color.gray;
			
			Date date = new Date();
		
			server = "<img class='server' src=\'"+Bennerbot.getPath(new File("resource/OutputLogo.png"))+"'>";
			user = Bennerbot.name;
			message = txt;
			
			if(!UC.inFile(user)){
				 UC.addUserColor(user, Color.red);
			 }
			for(user uc: UC.getUserColors()){
				if(uc.getUser().equalsIgnoreCase(user))
					color=uc.getColor();
			}
		
			//clean = server+Bennerbot.dateFormat.format(date)+" <span style=\'color:"+UserColors.rgb2Hex(color)+"\'>"+user+"</span>: "+filterURLS(APIManager.filterEmotes(message))+"<br>\n";
			//clean = "<span class='server'>"+server+"</span> <span class='timestamp'>"+Bennerbot.dateFormat.format(date)+"</span> <span class='username' style=\'color:"+UserColors.rgb2Hex(color)+"\'>"+user+"</span> <span class='message'>"+APIManager.filterEmotes(filterURLS(message))+"</span><br>\n";
			clean = Bennerbot.conf.get("DisplayMessageFormat").toString()
					.replace("<server>", server)
					.replace("<badge>", "")
					.replace("<timestamp>", "<span class='timestamp'>"+Bennerbot.dateFormat.format(date)+"</span>")
					.replace("<color>", "<span class='color'>"+UserColors.rgb2Hex(color)+"</span>")
					.replace("<user>", "<span class='username' style='color:"+UserColors.rgb2Hex(color)+"'>"+user+"</span>")
					.replace("<noformatuser>", "<span class='username'>"+user+"</span>")
					.replace("<message>", "<span class='message'>"+message+"</span>")
					.replace("<br>", "");
			if(((message.contains(Bennerbot.conf.get("twitchChannel").toString()) || message.contains(Bennerbot.conf.get("hitboxChannel").toString())) && Bennerbot.conf.get("HighlighMessages").toString().equalsIgnoreCase("true")))
				clean = "<span class=\'message highlight\'>"+clean+"</div>";
			else
				clean = "<span class=\'message\'>"+clean+"</div>";
			clean = clean+"<br>\n";
			dirty = "["+Bennerbot.dateFormat.format(date)+"] "+message+"\n";
			
			if((Bennerbot.conf.get("WriteClean").toString().equalsIgnoreCase("true") || Bennerbot.conf.get("OutputGUI").toString().equalsIgnoreCase("true")) && display)
				Bennerbot.write(clean, new File("output.html"));
			if(Bennerbot.conf.get("WriteDirty").toString().equalsIgnoreCase("true"))
				Bennerbot.write(dirty, new File("output.txt"));
		}
	}
	/**
	 * this function is called whenever the bot operator sends a message
	 * @param txt ~ the message that was sent
	 */
	public static void onOutput(String txt, boolean fromuser){
		//standard
		String clean, dirty, server, user, message;
		Color color = Color.gray;
		
		Date date = new Date();
		
		server = "<img class='server' src=\'"+Bennerbot.getPath(new File("resource/OutputLogo.png"))+"\'>";
		user = Bennerbot.name;
		message = txt;
		
		if(!UC.inFile(user)){
			 UC.addUserColor(user, Color.red);
		 }
		for(user uc: UC.getUserColors()){
			if(uc.getUser().equalsIgnoreCase(user))
				color=uc.getColor();
		}
		
		//clean = server+Bennerbot.dateFormat.format(date)+" <span style=\'color:"+UserColors.rgb2Hex(color)+"\'>"+user+"</span>: "+filterURLS(APIManager.filterEmotes(message))+"<br>\n";
		//clean = "<span class='server'>"+server+"</span> <span class='timestamp'>"+Bennerbot.dateFormat.format(date)+"</span> <span class='username' style=\'color:"+UserColors.rgb2Hex(color)+"\'>"+user+"</span> <span class='message'>"+APIManager.filterEmotes(filterURLS(message))+"</span><br>\n";
		clean = Bennerbot.conf.get("DisplayMessageFormat").toString()
				.replace("<server>", server)
				.replace("<badge>", "")
				.replace("<timestamp>", "<span class='timestamp'>"+Bennerbot.dateFormat.format(date)+"</span>")
				.replace("<color>", "<span class='color'>"+UserColors.rgb2Hex(color)+"</span>")
				.replace("<user>", "<span class='username' style='color:"+UserColors.rgb2Hex(color)+"'>"+user+"</span>")
				.replace("<noformatuser>", "<span class='username'>"+user+"</span>")
				.replace("<message>", "<span class='message'>"+message+"</span>")
				.replace("<br>", "");
		if(((message.contains(Bennerbot.conf.get("twitchChannel").toString()) || message.contains(Bennerbot.conf.get("hitboxChannel").toString())) && Bennerbot.conf.get("HighlighMessages").toString().equalsIgnoreCase("true")))
			clean = "<span class=\'message highlight\'>"+clean+"</div>";
		else
			clean = "<span class=\'message\'>"+clean+"</div>";
		clean = clean+"<br>\n";
		dirty = "["+Bennerbot.dateFormat.format(date)+"] "+message+"\n";
			
		if(Bennerbot.conf.get("WriteClean").toString().equalsIgnoreCase("true"))
			Bennerbot.write(clean, new File("output.html"));
		if(Bennerbot.conf.get("WriteDirty").toString().equalsIgnoreCase("true"))
			Bennerbot.write(dirty, new File("output.txt"));
	}
	public static String filterURLS(String s) {
		Matcher m = Pattern.compile(
				new StringBuilder()
				.append("((?:(http|https|Http|Https|rtsp|Rtsp):")
				.append("\\/\\/(?:(?:[a-zA-Z0-9\\$\\-\\_\\.\\+\\!\\*\\'\\(\\)")
					.append("\\,\\;\\?\\&\\=]|(?:\\%[a-fA-F0-9]{2})){1,64}(?:\\:(?:[a-zA-Z0-9\\$\\-\\_")
					.append("\\.\\+\\!\\*\\'\\(\\)\\,\\;\\?\\&\\=]|(?:\\%[a-fA-F0-9]{2})){1,25})?\\@)?)?")
					.append("((?:(?:[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}\\.)+")   // named host
					.append("(?:")   // plus top level domain
					.append("(?:aero|arpa|asia|a[cdefgilmnoqrstuwxz])")
					.append("|(?:biz|b[abdefghijmnorstvwyz])")
					.append("|(?:cat|com|coop|c[acdfghiklmnoruvxyz])")
					.append("|d[ejkmoz]")
					.append("|(?:edu|e[cegrstu])")
					.append("|f[ijkmor]")
					.append("|(?:gov|g[abdefghilmnpqrstuwy])")
					.append("|h[kmnrtu]")
					.append("|(?:info|int|i[delmnoqrst])")
					.append("|(?:jobs|j[emop])")
					.append("|k[eghimnrwyz]")
					.append("|l[abcikrstuvy]")
					.append("|(?:mil|mobi|museum|m[acdghklmnopqrstuvwxyz])")
					.append("|(?:name|net|n[acefgilopruz])")
					.append("|(?:org|om)")
					.append("|(?:pro|p[aefghklmnrstwy])")
					.append("|qa")
					.append("|r[eouw]")
					.append("|s[abcdeghijklmnortuvyz]")
					.append("|(?:tel|travel|t[cdfghjklmnoprtvwz])")
					.append("|u[agkmsyz]")
					.append("|v[aceginu]")
					.append("|w[fs]")
					.append("|y[etu]")
					.append("|z[amw]))")
					.append("|(?:(?:25[0-5]|2[0-4]") // or ip address
					.append("[0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9])\\.(?:25[0-5]|2[0-4][0-9]")
					.append("|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(?:25[0-5]|2[0-4][0-9]|[0-1]")
					.append("[0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(?:25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}")
					.append("|[1-9][0-9]|[0-9])))")
					.append("(?:\\:\\d{1,5})?)") // plus option port number
					.append("(\\/(?:(?:[a-zA-Z0-9\\;\\/\\?\\:\\@\\&\\=\\#\\~")  // plus option query params
					.append("\\-\\.\\+\\!\\*\\'\\(\\)\\,\\_])|(?:\\%[a-fA-F0-9]{2}))*)?")
					.append("(?:\\b|$)").toString()
				).matcher(s);;
		StringBuffer sb = new StringBuffer();
		while(m.find()){
		    String found =m.group(0); 
		    m.appendReplacement(sb, "<a href='"+found+"'>"+found+"</a>"); 
		}
		m.appendTail(sb);
		return sb.toString();
    }
	
}

