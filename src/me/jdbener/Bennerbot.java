/**
 * This class is the heart and soul of BennerBot, it controls and decides everything.
 * Author: Jdbener (Joshua Dahl)
 * Date: 11/8/14
 **/

package me.jdbener;

import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import me.jdbener.PHitboxBotX.HitboxServer;
import me.jdbener.apis.APIManager;
import me.jdbener.gui.MainGui;
import me.jdbener.gui.SplashScreen;
import me.jdbener.levels.LevelManager;
import me.jdbener.lib.Server;
import me.jdbener.moderataion.FilterManager;
import me.jdbener.utilities.AutoMessageHandeler;
import me.jdbener.utilities.ChatRelayHandeler;
import me.jdbener.utilities.Countdown;
import me.jdbener.utilities.CustomCommandHandeler;
import me.jdbener.utilities.Infromation;
import me.jdbener.utilities.JoinLeaveMessages;
import me.jdbener.webserver.tokenServer;
import net.xeoh.plugins.base.PluginManager;
import net.xeoh.plugins.base.impl.PluginManagerFactory;
import net.xeoh.plugins.base.util.PluginManagerUtil;

import org.ho.yaml.Yaml;
import org.pircbotx.Channel;
import org.pircbotx.MultiBotManager;
import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.managers.ListenerManager;
import org.pircbotx.hooks.managers.ThreadedListenerManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Bennerbot {
	//bot information
	public static String name = "BennerBot",												//the name of the bot
			version = "0.17", 															//the version ID of the bot
			twitchu = "",																	//twitch username used to connect
			twitchpw = "",																	//twitch OAuth used to connect
			hitboxu = "",																	//hitbox username used to connect
			hitboxpw = "";																	//hitbox password used to connect
	
	//initialize the variables
	public static Logger logger = LoggerFactory.getLogger(Bennerbot.class);					//the bot logger, this is used to write messages to the console
	public static Collection<BennerBotPlugin> plugins;										//the collection of plugins that the bot loads
	public static Map<String, Object> conf = new HashMap<String, Object>();					//all of the input from the configuration file
	public static Map<String, String> variableMap = new HashMap<String, String>();			//all of the variables bot, plugin, or custom
	public static Map<String, String> commandMap = new HashMap<String, String>();			//all the custom commands from the file, bot, or plugins
	public static Map<String, String> messagesMap = new HashMap<String, String>();			//all the automessages
	public static MainGui gui = null;														//the GUI object
	public static boolean guiLoaded = false;												//this value stores weather or not the gui has loaded yet or not
	public static SplashScreen sp = new SplashScreen();										//this variable contains a reference to our splash screen
	public static DateFormat dateFormat;													//this variable is contains an object that lets you format dates into text..
	public static Map<String, String> lastMessagePrinted = new HashMap<String, String>();	//this is used to prevent message loops in the chat display
	private static tokenServer webserver = new tokenServer();								//this is used to generate a twitch access token
	
	//initialize the servers
	//bot managaer
	public static MultiBotManager<PircBotX> manager = new MultiBotManager<PircBotX>();
	//Listener Manager
	public static ListenerManager<PircBotX> listener = new ThreadedListenerManager<PircBotX>();
	//Server Manager
	public static ArrayList<Server> servers = new ArrayList<Server>();
	
	/**
	 * This function will setup all of the required components for the configuration file.
	 * As well as the custom command file
	 */
	@SuppressWarnings("unchecked")
	private static void setupConfig(){
		try{
			//Initializes the configuration component
			conf = (Map<String, Object>) Yaml.load(new FileInputStream(new File("config/config.yml")));
		} catch (FileNotFoundException e){
			e.printStackTrace();
		}
		name = conf.get("botName").toString().trim();
	}	
	/**
	 * This function will run whenever the bot is started
	 */
	public static void main (String[] args){
		try {
			//set the look and feel
		    //TODO add a way to change the look and feel
		    try {
		    	UIManager.setLookAndFeel("com.jtattoo.plaf.noire.NoireLookAndFeel");
       		} catch (Exception e) {
       			e.printStackTrace();
       		}
			// Try to get LOCK //
		    if (!AppLock.setLock("BennerBotLockKey")) {
		    	try{
		    		JOptionPane.showMessageDialog(null, "Only one application instance may run at the same time!");
		    	} catch (Exception e){
		    		e.printStackTrace();
		    	}
		        throw new RuntimeException("Only one application instance may run at the same time!");
		    }
		    //Determine weather or not the nogui argument was passed.
		    boolean nogui = true;
			for(int i = 0; i < args.length; i++) {
	           if(args[i].equalsIgnoreCase("nogui")){
	        	   nogui = false;
	           }
	        }
			//start the splash screenS
		    if(!GraphicsEnvironment.isHeadless() && nogui)
				sp.start();
		    //ask if they want to use the configuation gui
		    if(!GraphicsEnvironment.isHeadless())
		    	if(2 == new StartupDialog().getOption())
		    		nogui = false;
		    //make refresh the splash screen so that it is ontop
		    sp.setAlwaysOnTop(false);
    		sp.setAlwaysOnTop(true);	
		    //redirects a copy of the console output to a file
		    try {
		    	//creates the date used in the name of the log file
		    	String date = new SimpleDateFormat("MM.dd.HH.mm").format(new Date());
		    	new File("logs").mkdir();
		    	logger.info("Log file: "+date+".log created");
		    	deleteOldFiles(2, "logs");
		    	//the actual redirection
		    	System.setOut(new PrintStream(new Writer(System.out, new FileOutputStream(new File("Logs/"+date+".log")))));
		    } catch (IOException e) {
		    	e.printStackTrace();
		    }
        
		    //set up everything
		    setupConfig();
			//set the date format
			dateFormat = new SimpleDateFormat(conf.get("dateFormat").toString());
			//set up the GUI or the BOTID depending on if the environment is headless or not
			if(!GraphicsEnvironment.isHeadless() && nogui){
				logger.info("Doing some database stuff, give me a moment");
				me.jdbener.lib.botId.setupBotIDTable();
				me.jdbener.lib.botId.updateFile(me.jdbener.lib.botId.getHash());
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							gui = new MainGui();
							gui.setVisible(true);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});	
			} else {
				//if there is a graphics environment launch in light mode
				if (!GraphicsEnvironment.isHeadless() && !nogui){
					JFrame displayFrame = new JFrame();
					displayFrame.setSize(200, 60);
					displayFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					displayFrame.setIconImage(Toolkit.getDefaultToolkit().getImage(MainGui.class.getResource("/me/jdbener/gui/Lion.png")));
					displayFrame.setTitle(Bennerbot.name+" v"+Bennerbot.version);
					displayFrame.setResizable(false);
					displayFrame.add(new JLabel("Close this window to stop the bot"));
					displayFrame.setVisible(true);
				}
				//run the code that the gui would have already run
				//make sure the hash is in place
				new Thread(new Runnable(){
					@Override
					public void run() {
						me.jdbener.lib.botId.setupBotIDTable();
						String hash = configGetString("botID");
						if(hash.equalsIgnoreCase("default"))
							hash = new Date().getTime()+"";
						
						if(!hash.equalsIgnoreCase(me.jdbener.lib.botId.getHash()))
							me.jdbener.lib.botId.updateFile(hash);
					}
				}).start();
				//setup the config system (this lets changes to the config file take effect in real time)
				Runnable runnable = new Runnable() {
					@Override
					public void run() {
						setupConfig();
					}
				};
				//add the update to the execution thread
				Executors.newScheduledThreadPool(1).scheduleAtFixedRate(runnable, 0, 1, TimeUnit.SECONDS);
				//claim that the gui is loaded
				logger.info("Running in NoGUI mode ");
				guiLoaded = true;
				
			}
			//let the GUI catch up and update its settings
			logger.warn("Waiting for GUI to load");
			while(guiLoaded == false)
				try {
					Thread.sleep(100);
				} catch (InterruptedException e1) {}
			logger.info("GUI loaded... Continuing with launch sequence");
			//close the splash screen
			sp.end();
			//refresh the GUI so that is on top
			try{
				gui.setAlwaysOnTop(true);
				gui.setAlwaysOnTop(false);
			} catch (Exception e){
				e.printStackTrace();
			}
		    //create a new instance of the bot
		    new Bennerbot();
		    //command line messages
			if(Bennerbot.conf.get("enableOutput").toString().equalsIgnoreCase("true")){
				@SuppressWarnings("resource")
				Scanner scan = new Scanner(System.in);
				String out;
				while(true)
					if(scan.hasNext()){
						out = scan.nextLine();
						if(out.equalsIgnoreCase("stop")){
							System.out.println("Stoping Bot");
							System.exit(0);
						} else if(out.equals("tokenRegen")){
							Bennerbot.generateAccessToken();
						}
						if(out.startsWith("~"))
							try{
								Bennerbot.sendMessage(out.replace(out.split(" ")[0], ""), Bennerbot.getBotIDbyName(out.split(" ")[0].toLowerCase()), true);
							} catch(Exception e){
								Bennerbot.sendMessage(out, true);
							}
						else
							//send the text to the server
							Bennerbot.sendMessage(out, true);
					}
			}
		} finally {
		    AppLock.releaseLock(); // Release lock
		}
	}
	/**
	 * This function sets up the bot's server connections
	 */
	public Bennerbot (){
		//set the twitch username
		twitchu = conf.get("twitchUsername").toString();
		//set the twitch password
		twitchpw = conf.get("twitchOAuth") == null ? Bennerbot.getAccessToken() : conf.get("twitchOAuth").toString();
		
		//set the hitbox username
		hitboxu = conf.get("hitboxUsername").toString();
		//set the hitbox username
		hitboxpw = conf.get("hitboxPassword").toString();
		
		//output to a file?
		if(conf.get("enableOutput").toString().equalsIgnoreCase("true")){
			listener.addListener(new Cleaner());
		}
		
		listener.addListener(new ChatRelayHandeler());
		listener.addListener(new CustomCommandHandeler());
		listener.addListener(new Infromation());
		listener.addListener(new JoinLeaveMessages());
		listener.addListener(new Countdown());
		listener.addListener(new LevelManager());
		listener.addListener(new AutoMessageHandeler());
			
		if(conf.get("enableModeration").toString().equalsIgnoreCase("true"))
			new FilterManager();
				
		//this activates !title and !game commands, as well as follower notifications
		try{
			new APIManager();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//Initializes all of the settings for the bots!
		try {
			if(Bennerbot.conf.get("connectToTwitch").toString().equalsIgnoreCase("true"))
				servers.add(new Server("irc.twitch.tv", "Twitch", twitchu, twitchpw, conf.get("twitchChannel").toString().toLowerCase(), new File("resource/TwitchLogo.png").toURI().toURL()));
			if(Bennerbot.conf.get("connectToHitbox").toString().equalsIgnoreCase("true"))
				servers.add(new HitboxServer(hitboxu, hitboxpw, conf.get("hitboxChannel").toString().toLowerCase(), new File("resource/HitboxLogo.png").toURI().toURL()));
		} catch (MalformedURLException ex) {
			ex.printStackTrace();
		}

		//implement the plugin loader
		if(conf.get("enablePluginSystem").toString().equalsIgnoreCase("true"))
			pluginLoader();
		
		//add the bots to the manager and start them
		for(Server s: servers)
			manager.addBot(s.getBot());
		manager.start();
		
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		if(conf.get("enableAutoMessages").toString().equalsIgnoreCase("true"))
			new AutoMessageHandeler();
	}
	
	/**
	 * This function loads plugins in the /plugins folder
	 */
	private void pluginLoader(){
		logger.info("Begining plugin loading");
		//sets the file where the plugins are
		File pluginFile = new File("plugins/"), temp;
		//lists all of the files in that folder
		String[] names = pluginFile.list();
		//Initialize the pluginmanager
		PluginManager pm = PluginManagerFactory.createPluginManager();
		//add all the plugins in the /plugins
		pm.addPluginsFrom(pluginFile.toURI());
		logger.info("Finished looking for plugins in: plugins");
		//cycles through all of the subdirectories
		for(String name : names){
			//get the file object of the directory
			temp = new File(pluginFile.toString() + "/" + name);
		    //check if it is a file or directory
			if (temp.isDirectory()){
				//add all of the plugins
		    	pm.addPluginsFrom(temp.toURI());
		    	logger.info("Finished looking for plugins in: plugins/"+name);
		    }
		}
		
		//gets a list of all the plugins
		plugins = new PluginManagerUtil(pm).getPlugins(BennerBotPlugin.class);
		logger.info("Beggining pluging loading sequence");
		for(BennerBotPlugin plugin: plugins){
			logger.info("Beggining loading of: "+plugin.getName());
			//run the initiate method for each of the plugins
			plugin.inititate();
			logger.info(plugin.getName()+" has been sucessfully loaded!");
		}
		logger.info("Finished pluging loading sequence");
		logger.info("Proceading with "+name+" initiation sequence");
	}
	/**
	 * used to send a message to the server
	 * @param txt the message to be sent
	 */
	public static void sendMessage(String txt){
		sendMessage(txt, "");
		Cleaner.onOutput(txt);
	}
	/**
	 * used to send a message to the server that wont be writen to the output file
	 * @param txt the message to be sent
	 */
	public static void sendMessage(String txt, String dontShow){
		int i=0; while(i < servers.size()){
			try{
				sendMessage(txt, i, dontShow);
			} catch (Exception e){
				e.printStackTrace();
			}
			i++;
		}
	}
	/**
	 * used to send a message to the server that is from the bot operator
	 * @param txt the message to be sent
	 */
	@SuppressWarnings("unchecked")
	public static void sendMessage(String txt, boolean fromuser){
		String server = "", user = "";
		int i=0; while(i < servers.size()){
			Server s = servers.get(i);
			try{
				if(Bennerbot.conf.get("showSource").toString().equalsIgnoreCase("true")){server = " ["+s.getChannel().replace("#", "")+"]";}
				if(Bennerbot.conf.get("showSendName").toString().equalsIgnoreCase("true")){user = Bennerbot.capitalize(s.getChannel().replace("#", ""))+": ";}
				sendMessage(user+server+txt, i);
			} catch (Exception e){
				e.printStackTrace();
			}
			i++;
		}
		//Display the message
		Cleaner.onOutput(txt, true);
		//this code deals with message phrasing and responding
		if(Bennerbot.conf.get("respondToOperatorCommands").toString().equalsIgnoreCase("true")){
			//creates the event that is sent to the functions
			MessageEvent<PircBotX> e = GenerateMessageEvent(txt);
			//this code will cycle through the plugins and run the OnMessage function if it exists
			for(BennerBotPlugin obj: plugins.toArray(new BennerBotPlugin[0]))
				obj.onOperatorOuput(txt);
			//the code for hardcoded functions
			//bot ones
			for(Object obj: listener.getListeners().toArray())
				try{
					if(!obj.getClass().getName().contains("Cleaner")){
						((ListenerAdapter<PircBotX>) obj).onMessage(e);
					}
				} catch(Exception ex){
					ex.printStackTrace();
				}
		}
	}
	/**
	 * used to send a message to a specific bot
	 * @param txt the message to be sent
	 * @param bot the id of the bot, 0) twitch 1) hitbox
	 */
	public static void sendMessage(String txt, int bot){
		sendMessage(txt, bot, "");
		Cleaner.onOutput(txt);
	}
	/**
	 * used to send a message to a specific bot
	 * @param txt the message to be sent
	 * @param bot the id of the bot, 0  twitch 1 hitbox
	 */
	public static void sendMessage(String txt, int bot, String dontShow){
		try{
			if(bot<0) bot = 0; if(bot>servers.size())bot=servers.size();
			servers.get(bot).sendMessage("PRIVMSG "+servers.get(bot).getChannel()+" :"+txt);
		} catch (Exception e){
			e.printStackTrace();
		}	
	}
	/**
	 * used to send a message to a specific bot form a bot operator
	 * @param txt the message to be sent
	 * @param bot the id of the bot, 0) twitch 1) hitbox
	 */
	@SuppressWarnings("unchecked")
	public static void sendMessage(String txt, int bot, boolean fromUser){
		String server = "", user = "";
		try{
			Server s = servers.get(bot);
			if(Bennerbot.conf.get("showSource").toString().equalsIgnoreCase("true")){server = " ["+s.getChannel().replace("#", "")+"]";}
			if(Bennerbot.conf.get("showSendName").toString().equalsIgnoreCase("true")){user = Bennerbot.capitalize(s.getChannel().replace("#", ""))+": ";}
			sendMessage(user+server+txt, bot);
		} catch (Exception e){
			e.printStackTrace();
		}
		//this code deals with message phrasing and responding
		if(Bennerbot.conf.get("respondToOperatorCommands").toString().equalsIgnoreCase("true")){
			//creates the event that is sent to the functions
			MessageEvent<PircBotX> e = GenerateMessageEvent(txt);
			//this code will cycle through the plugins and run the OnMessage function if it exists
			for(BennerBotPlugin obj: plugins.toArray(new BennerBotPlugin[0]))
				obj.onOperatorOuput(txt);
			//the code for hardcoded functions
			//bot ones
			for(Object obj: listener.getListeners().toArray())
				try{
					if(!obj.getClass().getName().contains("Cleaner")){
						((ListenerAdapter<PircBotX>) obj).onMessage(e);
					}
				} catch(Exception ex){
					ex.printStackTrace();
				}
		}
	}
	/**
	 * A simple function that capitalizes the first letter of the string pasted to it
	 * @param line the string to be capitalized
	 * @return the capitalized string
	 */
	public static String capitalize(String line){
		if(conf.get("capitalizeNames").toString().equalsIgnoreCase("true"))
			return capitalize(line, true);
		return line;
	}
	/**
	 * A simple function that capitalizes the first letter of the string pasted to it
	 * @param line the string to be capitalized
	 * @return the capitalized string
	 */
	public static String capitalize(String line, boolean ignore){
		return Character.toUpperCase(line.charAt(0)) + line.substring(1);
	}
	/**
	 * returns the content of the path specified
	 * @param path the path to the file
	 * @return the contents of the file
	 */
	public static String readFile(String path) {
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
	/**
	 * returns the content of the file specified
	 * @param path the file object of the file you want to read
	 * @return the contents of the file
	 */
	public static String readFile(File f) {
		try {
			String path = f.toURI().toURL().toString();
			//gets all of the bytes in the file
			byte[] encoded = Files.readAllBytes(Paths.get(path));
			//turns those bytes into a string
			return new String(encoded, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * returns the path to the file object passed
	 * @param f the file to be translated
	 * @return the path to the file
	 */
	public static String getPath(File f){
		try {
			return f.toURI().toURL().toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * this function appends a string to a file
	 * @param out the string to be appended
	 * @param f the file to append the string to
	 */
	public static void write(String out, File f){
		if(!out.equalsIgnoreCase(lastMessagePrinted.get(f.getPath()))){
			try {
				FileWriter writer = new FileWriter(f, true);
				writer.append(out);
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		
			if(f.getPath().contains("output.html")){
				try{
					gui.writeDisplay(out);
				} catch (Exception e){e.printStackTrace();}
			}
		}
		lastMessagePrinted.put(f.getPath(), out);
	}
	/**
	 * this function appends a string to a file
	 * @param out the string to be appended
	 * @param f the file to append the string to
	 * @param display this makes sure not to display the text your are writing to a file
	 */
	public static void write(String out, File f, boolean display){
		try {
			FileWriter writer = new FileWriter(f, true);
			writer.append(out);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * this function check if a user is a moderator in a specific channel	
	 * @param u the user
	 * @param c the channel
	 * @return weather or not the user is a moderator
	 */
	public static boolean isMod(User u, Channel c){
		return u.getChannelsHalfOpIn().contains(c) || u.getChannelsOpIn().contains(c) || u.getChannelsOwnerIn().contains(c) || u.getChannelsSuperOpIn().contains(c);
	}
	/**
	 * This function will delete files that older than the date specified in the folder specifed
	 * @param daysBack the date specified
	 * @param dirWay the folder specified
	 */
	public static void deleteOldFiles(int daysBack, String dirWay) {    
        File directory = new File(dirWay);  
        if(directory.exists()){  
  
            File[] listFiles = directory.listFiles();              
            long purgeTime = System.currentTimeMillis() - (daysBack * 24 * 60 * 60 * 1000);  
            for(File listFile : listFiles) {  
                if(listFile.lastModified() < purgeTime) {  
                    if(!listFile.delete()) {  
                        Bennerbot.logger.error("Unable to delete file: " + listFile);  
                    } else {
                    	Bennerbot.logger.info("Deleted file: "+listFile);
                    }
                }  
            }  
        } else {  
            logger.warn("Files were not deleted, directory " + dirWay + " does'nt exist!");  
        }  
    }
	/**
	 * Creates a message event for the frame work, this is useful for plugins that want to use the onMessage function to handle user sent messages
	 * @param txt the message body
	 * @param bot the bot to get channels and name from
	 * @return the message event
	 */
	public static MessageEvent<PircBotX> GenerateMessageEvent(String txt, int bot){
		return new MessageEvent<PircBotX>(manager.getBotById(bot), manager.getBotById(bot).getUserChannelDao().getAllChannels().toArray(new Channel[0])[0], manager.getBotById(bot).getUserBot(), txt);
	}
	/**
	 * Creates a message event for the frame work, this is useful for plugins that want to use the onMessage function to handle user sent messages, used when the coder dosent care what bot is used
	 * @param txt the message body
	 * @return the message event
	 */
	public static MessageEvent<PircBotX> GenerateMessageEvent(String txt){
		return GenerateMessageEvent(txt, 0);
	}
	/**
	 * returns the bot ID of the server named, returns 0 if name = twitch or name isent found
	 * @param name the name to search for
	 * @return the id of the bot found
	 */
	public static int getBotIDbyName(String name){
		int i=0; while(i < servers.size()){
			if(servers.get(i).getName().equalsIgnoreCase(name))
				return i;
			i++;
		}
		return 0;
	}
	/**
	 * Removes the last character from a string
	 * @param str the string to have the last character removed from
	 * @return the string with the last character removed
	 */
	public static String removeLastChar(String str) {
        return str.substring(0,str.length()-1);
    }
	/**
	 * This function will convert an input stream into a string
	 * @param is the input steam
	 * @return the string
	 */
	public static String StreamToString(java.io.InputStream is) {
	    @SuppressWarnings("resource")
		Scanner s = new Scanner(is).useDelimiter("\\A");
	    return s.hasNext() ? s.next() : "";
	}
	/**
	 * This function checks if an item in the config has a specific value
	 * @param item the item
	 * @param value the value
	 * @return is that true or not
	 */
	public static boolean configEqualsString(String item, String value){
		return conf.get(item).toString().equalsIgnoreCase(value);
	}
	/**
	 * This function checks if an item in the config is true
	 * @param item the item
	 * @return is that the case
	 */
	public static boolean configBoolean(String item){
		return configEqualsString(item, "true");
	}
	/**
	 * returns the string if it is in the config file/database
	 * @param item the item to search for
	 * @return its value
	 */
	public static String configGetString(String item){
		return conf.get(item).toString();
	}
	/**
	 * This function filters all non English symbols with the specified replacement value
	 * @param utf the string to have it's values replaced
	 * @param replacement what to replace them with
	 * @return the replaced string
	 */
	public static String filterUTF8(String utf, String replacement){
		return utf.replaceAll("[^ -~]", replacement);
	}
	/**
	 * This function filters all non English symbols and replaces them with a question mark (?)
	 * @param utf the string to have its values replaced
	 * @return the replaced string
	 */
	public static String filterUTF8(String utf){
		return filterUTF8(utf, "?");
	}
	/**
	 * This function returns a twitch access token if one is generated
	 * @return
	 */
	public static String getAccessToken(){
		String token = webserver.getToken();
		if(token == ""){
			try{
				token = Bennerbot.configGetString("twitchAccessToken");
			} catch(Exception e){e.printStackTrace();}
		}
		if(token == ""){
			if(!GraphicsEnvironment.isHeadless()){
				try{JOptionPane.showMessageDialog(null, "<html><body><center>We see you dont have a Twitch Access Token generated.<br/>One will now be generated<center></body></html>", "Generate Token? ~ Bennerbot v0.17", JOptionPane.DEFAULT_OPTION);
				}catch(Exception e){System.out.println("We see you dont have a Twitch Access Token generated. One will now be generated");}
				generateAccessToken();
				while(webserver.getToken().equalsIgnoreCase("")){try {Thread.sleep(1);} catch (InterruptedException e) {e.printStackTrace();}/*do nothing but do enough to keep the system happy*/}
				token = webserver.getToken();
			}
		}
		return token;
	}
	private static void generateAccessToken(){
		String url = "https://api.twitch.tv/kraken/oauth2/authorize?response_type=token&client_id=9qxushp3sdeasixpxajz8pqlxdudfs6&redirect_uri=http://127.0.0.1:55555/token/&scope=channel_editor+channel_subscriptions+chat_login";
		try{
            Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
            if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE))
                desktop.browse(new URL(url).toURI());
        } catch (Exception e){
            e.printStackTrace();

            // Copy URL to the clipboard so the user can paste it into their browser
            StringSelection stringSelection = new StringSelection(url);
            Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
            clpbrd.setContents(stringSelection, null);
            // Notify the user of the failure
            try{
            	JOptionPane.showMessageDialog(null, "<html><body>We have unsucessfully tried to open a new page to generate an access token for you, <br>however the URL has been copied to your clipboard, simply paste into your browser to generate one.</body></html>");
            } catch(Exception ex){System.out.println("It apperes that the code for you to generate an access token wont work for you, dont worry this just means you will have to do it autonomusly. Please go to this url, \"https://api.twitch.tv/kraken/oauth2/authorize?response_type=token&client_id=9qxushp3sdeasixpxajz8pqlxdudfs6&redirect_uri=http://127.0.0.1:55555/token/&scope=channel_editor+channel_subscriptions+chat_login\" and copy the access token it responds into the config file");}
        }
	}
}

/**
 * The Class AppLock.
 *
 * @url http://nerdydevel.blogspot.com/2012/07/run-only-single-java-application-instance.html
 * @author rumatoest
 */
class AppLock {
 
        /**
         * Instantiates a new app lock.
         */
        private AppLock() {
        }
 
        /** The lock_file. */
        File lock_file = null;
 
        /** The lock. */
        FileLock lock = null;
 
        /** The lock_channel. */
        FileChannel lock_channel = null;
 
        /** The lock_stream. */
        FileOutputStream lock_stream = null;
 
        /**
         * Instantiates a new app lock.
         *
         * @param key Unique application key
         * @throws Exception The exception
         */
        private AppLock(String key) throws Exception {
                String tmp_dir = System.getProperty("java.io.tmpdir");
                if (!tmp_dir.endsWith(System.getProperty("file.separator"))) {
                        tmp_dir += System.getProperty("file.separator");
                }
 
                // Acquire MD5
                try {
                        java.security.MessageDigest md = java.security.MessageDigest
                                        .getInstance("MD5");
                        md.reset();
                        String hash_text = new java.math.BigInteger(1, md.digest(key
                                        .getBytes())).toString(16);
                        // Hash string has no leading zeros
                        // Adding zeros to the beginnig of has string
                        while (hash_text.length() < 32) {
                                hash_text = "0" + hash_text;
                        }
                        lock_file = new File(tmp_dir + hash_text + ".app_lock");
                } catch (Exception ex) {
                        System.out.println("AppLock.AppLock() file fail");
                }
 
                // MD5 acquire fail
                if (lock_file == null) {
                        lock_file = new File(tmp_dir + key + ".app_lock");
                }
 
                lock_stream = new FileOutputStream(lock_file);
 
                String f_content = "Java AppLock Object\r\nLocked by key: " + key
                                + "\r\n";
                lock_stream.write(f_content.getBytes());
 
                lock_channel = lock_stream.getChannel();
 
                lock = lock_channel.tryLock();
 
                if (lock == null) {
                        throw new Exception("Can't create Lock");
                }
        }
 
        /**
         * Release Lock.
         * Now another application instance can gain lock.
         *
         * @throws Throwable
         */
        private void release() throws Throwable {
                if (lock.isValid()) {
                        lock.release();
                }
                if (lock_stream != null) {
                        lock_stream.close();
                }
                if (lock_channel.isOpen()) {
                        lock_channel.close();
                }
                if (lock_file.exists()) {
                        lock_file.delete();
                }
        }
 
        @Override
        protected void finalize() throws Throwable {
                this.release();
                super.finalize();
        }
 
        /** The instance. */
        private static AppLock instance;
 
        /**
         * Set application lock.
         * Method can be run only one time per application.
         * All next calls will be ignored.
         *
         * @param key Unique application lock key
         * @return true, if successful
         */
        public static boolean setLock(String key) {
                if (instance != null) {
                        return true;
                }
 
                try {
                        instance = new AppLock(key);
                } catch (Exception ex) {
                        instance = null;
                        Bennerbot.logger.error("Fail to set AppLoc");
                        return false;
                }
 
                Runtime.getRuntime().addShutdownHook(new Thread() {
                        @Override
                        public void run() {
                                AppLock.releaseLock();
                        }
                });
                return true;
        }
 
        /**
         * Trying to release Lock.
         * After release you can not user AppLock again in this application.
         */
        public static void releaseLock() {
                try {
                    if (instance == null) {
                            throw new NoSuchFieldException("INSTATCE IS NULL");
                    }
                    instance.release();
                } catch (Throwable ex) {
                	Bennerbot.logger.error("Fail to release");
                }
        }
}