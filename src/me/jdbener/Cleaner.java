/**
 * This class is what creates the output files
 * Author: Jdbener (Joshua Dahl)
 * Date: 11/8/14
 */

package me.jdbener;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.jdbener.apis.APIManager;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.ConnectEvent;
import org.pircbotx.hooks.events.JoinEvent;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.PartEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;

public class Cleaner extends ListenerAdapter<PircBotX>{
	//this variable is a reference to some of the color utitlites used by the code
	static UserColors UC = new UserColors();
	
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
					Bennerbot.write("<html>\n<head>\n<title>"+Bennerbot.name+" v"+Bennerbot.version+" ~ Chat</title><link rel=\"stylesheet\" type=\"text/css\" href=\"resource/layout.css\" media=\"screen\" />\n</head><body onLoad=\"setInterval('window.scrollTo(0,document.body.scrollHeight);location.reload();', 1000);\">\n<center><h1>Welcome to "+Bennerbot.name+"</h1>Version "+me.jdbener.Bennerbot.version+"</center>\n", new File("output.html"), false);
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
		String user = Bennerbot.name;
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
		Bennerbot.sendMessage("/color "+UserColors.rgb2Hex(color), new String());
		
		//twitch
		if(e.getBot().getBotId() == 0)
			onOutput("Sucessfully Connected to Twitch");
		//hitbox
		if(e.getBot().getBotId() == 1)
			onOutput("Sucessfully Connected to Hitbox");
	}
	
	/**
	 * This function is called whenever somebody says something
	 */
	public void onMessage(MessageEvent<PircBotX> e){
		//initialize variables
		String clean, dirty, server, serverd, mod, user, message;
		Color color = Color.gray;
		DateFormat dateFormat = new SimpleDateFormat("HH:mm");
		Date date = new Date();
		
		//set the server text/image
		if (e.getBot().getBotId() == 1){
			 server = "<img src=\'"+Bennerbot.getPath(new File("resource/HitboxLogo.png"))+"\'>";
			 serverd = "hitbox";
		 } else  if (e.getBot().getBotId() == 0){
			 server = "<img src=\'"+Bennerbot.getPath(new File("resource/TwitchLogo.png"))+"\'>";
			 serverd = "twitch";
		 } else {
			 server = "";
			 serverd = "";
		 }
		
		//the user is the user who sent the message
		user = e.getUser().getNick();
		//gets the message that was sent
		message = APIManager.filterEmotes(filterURLS(e.getMessage()));
		
		//is the user in the file?
		if(!UC.inFile(user)){
			//create a new color until the color isent in the file
			 Color tempC = new Color(new Random().nextFloat(),new Random().nextFloat(),new Random().nextFloat());
			 while (UC.inFile(tempC)){
				 tempC = new Color(new Random().nextFloat(),new Random().nextFloat(),new Random().nextFloat());
			 }
			 //add the color to the file
			 UC.addUserColor(user, tempC);
		 }
		 //sets the color from the file
		 for(user uc: UC.getUserColors()){
			 if(uc.getUser().equalsIgnoreCase(user))
				 color=uc.getColor();
		 }
		 //check if the user sending the message is a moderator or not
		 if(Bennerbot.isMod(e.getUser(), e.getChannel()))
			 mod = "<img src=\'http://help.twitch.tv/customer/portal/attachments/349943'>";
		 else 
			 mod = "";
		 //check if the user sending the message is a broadcaster or not
		 if(e.getUser().getNick().equalsIgnoreCase(Bennerbot.conf.get("twitchChannel").toString()) || e.getUser().getNick().equalsIgnoreCase(Bennerbot.conf.get("hitboxChannel").toString()))
			 mod = "<img src=\'http://help.twitch.tv/customer/portal/attachments/349942'>";
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
		//support for highlighting messages 
		if((message.contains(Bennerbot.conf.get("twitchChannel").toString()) || message.contains(Bennerbot.conf.get("hitboxChannel").toString())) && Bennerbot.conf.get("HighlighMessages").toString().equalsIgnoreCase("true"))
			clean = "<div id=\'highlight\'>"+server+" "+mod+dateFormat.format(date)+" <span style=\'color:"+UserColors.rgb2Hex(color)+"\'>"+user+"</span> "+message+"</div><br>\n";		
		else
			clean = server+" "+mod+dateFormat.format(date)+" <span style=\'color:"+UserColors.rgb2Hex(color)+"\'>"+user+"</span> "+message+"<br>\n";
		
		//dirty output
		dirty = serverd+" ["+dateFormat.format(date)+"] "+e.getUser().getNick()+": "+message+"\n";
		
		//write the cleaned file if allowed
		if(Bennerbot.conf.get("WriteClean").toString().equalsIgnoreCase("true"))
			Bennerbot.write(clean, new File("output.html"));
		//write the dirty file if allowed
		if(Bennerbot.conf.get("WriteDirty").toString().equalsIgnoreCase("true"))
			Bennerbot.write(dirty, new File("output.txt"));
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
			DateFormat dateFormat = new SimpleDateFormat("HH:mm");
			Date date = new Date();
			
			//this information dose not have to be determined
			server = "<img src=\'"+Bennerbot.getPath(new File("resource/OutputLogo.png"))+"\'>";
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
			clean = server+dateFormat.format(date)+" <span style=\'color:"+UserColors.rgb2Hex(color)+"\'>"+user+"</span>: "+filterURLS(APIManager.filterEmotes(message))+"<br>\n";
			//the dirty format
			dirty = "["+dateFormat.format(date)+"] "+message+"\n";
			
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
			DateFormat dateFormat = new SimpleDateFormat("HH:mm");
			Date date = new Date();
		
			server = "<img src=\'"+Bennerbot.getPath(new File("resource/OutputLogo.png"))+"\'>";
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
			clean = server+dateFormat.format(date)+" <span style=\'color:"+UserColors.rgb2Hex(color)+"\'>"+user+"</span>: "+filterURLS(APIManager.filterEmotes(message))+"<br>\n";
			dirty = "["+dateFormat.format(date)+"] "+message+"\n";
			
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
							e.getMessage().contains("HOSTTARGET"));
			if(!trash){
				//Initialize variables
				String clean, dirty;
				
				//display
				clean = "<span id='error'>"+e.getMessage()+"</span><br>\n";
				dirty = e.getMessage();
			
				if(Bennerbot.conf.get("WriteClean").toString().equalsIgnoreCase("true") || Bennerbot.conf.get("OutputGUI").toString().equalsIgnoreCase("true"))
					Bennerbot.write(clean, new File("output.html"));
				if(Bennerbot.conf.get("WriteDirty").toString().equalsIgnoreCase("true"))
					Bennerbot.write(dirty, new File("output.txt"));
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
			DateFormat dateFormat = new SimpleDateFormat("HH:mm");
			Date date = new Date();
		
			server = "<img src=\'"+Bennerbot.getPath(new File("resource/OutputLogo.png"))+"'>";
			user = Bennerbot.name;
			message = txt;
			
			if(!UC.inFile(user)){
				 UC.addUserColor(user, Color.red);
			 }
			for(user uc: UC.getUserColors()){
				if(uc.getUser().equalsIgnoreCase(user))
					color=uc.getColor();
			}
		
			clean = server+dateFormat.format(date)+" <span style=\'color:"+UserColors.rgb2Hex(color)+"\'>"+user+"</span>: "+filterURLS(APIManager.filterEmotes(message))+"<br>\n";
			dirty = "["+dateFormat.format(date)+"] "+message+"\n";
			
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
		boolean display = true;
		Color color = Color.gray;
		DateFormat dateFormat = new SimpleDateFormat("HH:mm");
		Date date = new Date();
		
		server = "<img src=\'"+Bennerbot.getPath(new File("resource/OutputLogo.png"))+"\'>";
		user = Bennerbot.name;
		message = txt;
		
		for(String msg: message.split(" ")){
			if(msg.length()>15 && Bennerbot.conf.get("OutputGUI").toString().equalsIgnoreCase("true")){
				display = false;
			}
		}
		
		if(!UC.inFile(user)){
			 UC.addUserColor(user, Color.red);
		 }
		for(user uc: UC.getUserColors()){
			if(uc.getUser().equalsIgnoreCase(user))
				color=uc.getColor();
		}
		
		clean = server+dateFormat.format(date)+" <span style=\'color:"+UserColors.rgb2Hex(color)+"\'>"+user+"</span>: "+filterURLS(APIManager.filterEmotes(message))+"<br>\n";
		dirty = "["+dateFormat.format(date)+"] "+message+"\n";
			
		if((Bennerbot.conf.get("WriteClean").toString().equalsIgnoreCase("true") || Bennerbot.conf.get("OutputGUI").toString().equalsIgnoreCase("true")) && display)
			Bennerbot.write(clean, new File("output.html"));
		if(Bennerbot.conf.get("WriteDirty").toString().equalsIgnoreCase("true"))
			Bennerbot.write(dirty, new File("output.txt"));
	}
	public static String filterURLS(String s) {
		//String urlValidationRegex = "(https?|ftp)://(www\\d?|[a-zA-Z0-9]+)?.[a-zA-Z0-9-]+(\\:|.)([a-zA-Z0-9.]+|(\\d+)?)([/?:].*)?";
		//String urlValidationRegex = Pattern.quote("~(?:\b[a-z\d.-]+://[^<>\s]+|\b(?:(?:(?:[^\s!@#$%^&*()_=+[\]{}\|;:'",.<>/?]+)\.)+(?:ac|ad|aero|ae|af|ag|ai|al|am|an|ao|aq|arpa|ar|asia|as|at|au|aw|ax|az|ba|bb|bd|be|bf|bg|bh|biz|bi|bj|bm|bn|bo|br|bs|bt|bv|bw|by|bz|cat|ca|cc|cd|cf|cg|ch|ci|ck|cl|cm|cn|coop|com|co|cr|cu|cv|cx|cy|cz|de|dj|dk|dm|do|dz|ec|edu|ee|eg|er|es|et|eu|fi|fj|fk|fm|fo|fr|ga|gb|gd|ge|gf|gg|gh|gi|gl|gm|gn|gov|gp|gq|gr|gs|gt|gu|gw|gy|hk|hm|hn|hr|ht|hu|id|ie|il|im|info|int|in|io|iq|ir|is|it|je|jm|jobs|jo|jp|ke|kg|kh|ki|km|kn|kp|kr|kw|ky|kz|la|lb|lc|li|lk|lr|ls|lt|lu|lv|ly|ma|mc|md|me|mg|mh|mil|mk|ml|mm|mn|mobi|mo|mp|mq|mr|ms|mt|museum|mu|mv|mw|mx|my|mz|name|na|nc|net|ne|nf|ng|ni|nl|no|np|nr|nu|nz|om|org|pa|pe|pf|pg|ph|pk|pl|pm|pn|pro|pr|ps|pt|pw|py|qa|re|ro|rs|ru|rw|sa|sb|sc|sd|se|sg|sh|si|sj|sk|sl|sm|sn|so|sr|st|su|sv|sy|sz|tc|td|tel|tf|tg|th|tj|tk|tl|tm|tn|to|tp|travel|tr|tt|tv|tw|tz|ua|ug|uk|um|us|uy|uz|va|vc|ve|vg|vi|vn|vu|wf|ws|xn--0zwm56d|xn--11b5bs3a9aj6g|xn--80akhbyknj4f|xn--9t4b11yi5a|xn--deba0ad|xn--g6w251d|xn--hgbk6aj7f53bba|xn--hlcj6aya9esc7a|xn--jxalpdlp|xn--kgbechtv|xn--zckzah|ye|yt|yu|za|zm|zw)|(?:(?:[0-9]|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])\.){3}(?:[0-9]|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5]))(?:[;/][^#?<>\s]*)?(?:\?[^#<>\s]*)?(?:#[^<>\s]*)?(?!\w))~iS");
		//Pattern p = Pattern.compile(urlValidationRegex);
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
/*
 * This class is used in the manegment of user colors.
 */
class UserColors {
	//Initialize variables
	static File f = new File("config/usercolors.txt");
	//BufferedReader in;
	
	public UserColors(){
		
		setupUserColorTable();
		//assign the variables
		/*try {
			in = new BufferedReader(new FileReader(f));
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		
	}
	public static void setupUserColorTable(){
		 try {
			 Connection c = APIManager.getConnection(); 
			 Statement stmt = c.createStatement();
			 String sql;
			 if(Bennerbot.conf.get("resetColorDatabase").toString().equalsIgnoreCase("true")){
				 sql = "DROP TABLE IF EXISTS UC;"; 
				 stmt.executeUpdate(sql);
			 }
			  sql = "CREATE TABLE IF NOT EXISTS UC " +
		                   "(USER          TEXT    NOT NULL, " + 
		                   " COLOR         TEXT)"; 
		      stmt.executeUpdate(sql);
		      stmt.close();
		      c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	//this function adds a user's color to the database
	public void addUserColor(String user, Color color){
		try{
			  Connection c = APIManager.getConnection();
			  Statement stmt = c.createStatement();
			  String sql = "INSERT INTO UC (USER, COLOR) " +
	                   "VALUES ('"+user+"','"+rgb2Hex(color)+"');"; 
			  
			  stmt.execute(sql);
		} catch (SQLException e){
			 e.printStackTrace();
		}
			  
		//write(user+":"+rgb2Hex(color));
	}
	//this function returns an array of user objects from the database
	public user[] getUserColors(){
		ArrayList<user> temp = new ArrayList<user>();
		try{
		  Connection c = APIManager.getConnection();
		  Statement stmt = c.createStatement();
		  String sql = "SELECT * FROM UC;"; 
		  
		  ResultSet rs = stmt.executeQuery(sql);
		  
		  while(rs.next()){
			  user tempu = new user();
			  tempu.setUser(rs.getString("USER"));
			  tempu.setColor(hex2Rgb(rs.getString("COLOR")));
			  temp.add(tempu);
		  }
		  
		} catch (SQLException e){
			e.printStackTrace();
		}
		/*//reads the file
		String temp = readFile(f.toString());
		//splits the string by line
		String[] tempa = temp.split("[\\r\\n]+");
		user[] out = new user[tempa.length];
		//creates the array
		for(int i = 0; i < out.length; i=i+1){
			String user = tempa[i].split(":")[0];
			String color = tempa[i].split(":")[1];
			out[i] = new user();
			out[i].user = user;
			out[i].color = hex2Rgb(color);
		}
		//returns the array*/
		return temp.toArray(new user[0]);
	}
	//checks if a user is in the file
	public boolean inFile(String user){
		user[] temp = getUserColors();
		boolean out=false;
		for(user t: temp){
			if(t.getUser().equalsIgnoreCase(user))
				out=true;
		}
		return out;
	}
	//checks if a color is in the file
	public boolean inFile(Color color){
		user[] temp = getUserColors();
		boolean out=false;
		for(user t: temp){
			if(rgb2Hex(t.getColor()).equalsIgnoreCase(rgb2Hex(color)))
				out=true;
		}
		return out;
	}
	//converts a color object into a string hexadecimal
	public final static String rgb2Hex(Color colour) throws NullPointerException {
		  String hexColour = Integer.toHexString(colour.getRGB() & 0xffffff);
		  if (hexColour.length() < 6) {
		    hexColour = "000000".substring(0, 6 - hexColour.length()) + hexColour;
		  }
		  return "#" + hexColour;
		}
	//converts a string hexadecimal into a color object
	public static Color hex2Rgb(String colorStr) {
	    return new Color(
	            Integer.valueOf( colorStr.substring( 1, 3 ), 16 ),
	            Integer.valueOf( colorStr.substring( 3, 5 ), 16 ),
	            Integer.valueOf( colorStr.substring( 5, 7 ), 16 ) );
	}
	//reads the file
	/*static String readFile(String path) {
		try {
			//gets all of the bytes in the file
			byte[] encoded = Files.readAllBytes(Paths.get(path));
			//turns those bytes into a string
			return new String(encoded, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	//writes to the file
	static void write(String out){
		try {
			FileWriter writer = new FileWriter(f, true);
			writer.append(out+"\n");
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}*/
}
/*
 * This class is a simple object that stores a username and a color
 */
class user{
	public String user;
	public Color color;
	
	public String getUser(){
		return user;
	}
	public void setUser(String u){
		user = u;
	}
	public Color getColor(){
		return color;
	}
	public void setColor(Color c){
		color = c;
	}
}