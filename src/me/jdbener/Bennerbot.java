/**
 * This class is the heart and soul of BennerBot, it controls and decides everything.
 * Author: Jdbener (Joshua Dahl)
 * Date: 11/8/14
 **/

package me.jdbener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;

import me.jdbener.apis.APIManager;
import me.jdbener.gui.GUIContainer;
import me.jdbener.moderataion.FilterManager;
import me.jdbener.utilities.BotJoinHandeler;
import me.jdbener.utilities.ChatRelayHandeler;
import me.jdbener.utilities.CustomCommandHandeler;
import me.jdbener.utilities.Infromation;
import me.jdbener.utilities.JoinLeaveMessages;
import net.xeoh.plugins.base.PluginManager;
import net.xeoh.plugins.base.impl.PluginManagerFactory;
import net.xeoh.plugins.base.util.PluginManagerUtil;

import org.ho.yaml.Yaml;
import org.pircbotx.Channel;
import org.pircbotx.Configuration;
import org.pircbotx.Configuration.Builder;
import org.pircbotx.MultiBotManager;
import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Bennerbot {
	//bot infromation
	public static String name = "BennerBot";												//the name of the bot
	public final static String version = "0.9";												//the version ID of the bot
	public static String twitchu = "BennerBot",												//twitch username used to connect
			twitchpw = "oauth:hrr2wpqq0knt6sb0spzd3d1mugpdezf",								//twitch OAuth used to connect
			hitboxu = "bennerbot",															//hitbox username used to connect
			hitboxpw = "bennerbot";															//hitbox password used to connect
	
	//initialize the variables
	public static Logger logger = LoggerFactory.getLogger(Bennerbot.class);					//the bot logger, this is used to write messages to the console
	public static Collection<BennerBotPlugin> plugins;										//the collection of plugins that the bot loads
	public static Map<String, Object> conf = null;											//all of the input from the configuration file
	public static Map<String, String> variableMap = new HashMap<String, String>();			//all of the variables bot, plugin, or custom
	public static Map<String, String> emoteMap = new HashMap<String, String>();				//all the emoteicons that have been custom added and those generated from the twitch api
	public static GUIContainer gui;															//the GUI object
	
	//initialize the servers
	//bot managaer
	public static MultiBotManager<PircBotX> manager = new MultiBotManager<PircBotX>();
	//twitch
	public static Builder<PircBotX> twitch;
	//hitbox
	public static Builder<PircBotX> hitbox;
	
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
	 * This function will replace any variables in the string that is passed to it, with there appropriate values.
	 */
	public static String replaceVariables(String in){
		//irate through all of the variables
		for (Map.Entry<String, String> entry : variableMap.entrySet()) {
	        //replace any occurrences of the variable
			in = in.replaceAll(entry.getKey(), entry.getValue());
	    }
		return in;
	}
	
	/**
	 * This function will run whenever the bot is started
	 */
	public static void main (String[] args){
		try {
		    // Try to get LOCK //
		    if (!AppLock.setLock("BennerBotLockKey")) {
		    	JOptionPane.showMessageDialog(null, "Only one application instance may run at the same time!");
		        throw new RuntimeException("Only one application instance may run at the same time!");
		    }
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
		    Runnable runnable = new Runnable() {
			    @Override
				public void run() {
			    	setupConfig();
			    }
			};
			//add the update to the execution thread
			ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
			executor.scheduleAtFixedRate(runnable, 0, 1, TimeUnit.SECONDS);
		    //create a new instance of the bot
		    new Bennerbot();
		} finally {
		    AppLock.releaseLock(); // Release lock
		}
		
	}
	/**
	 * This function sets up the bot's server connections
	 */
	public Bennerbot (){
		//set the twitch username
		if(!conf.get("twitchUsername").toString().equalsIgnoreCase("default"))
			twitchu = conf.get("twitchUsername").toString();
		//set the twitch password
		if(!conf.get("twitchOAuth").toString().equalsIgnoreCase("default"))
			twitchpw = conf.get("twitchOAuth").toString();
		//set the hitbox username
		if(!conf.get("hitboxUsername").toString().equalsIgnoreCase("default"))
			hitboxu = conf.get("hitboxUsername").toString();
		//set the hitbox username
		if(!conf.get("hitboxPassword").toString().equalsIgnoreCase("default"))
			hitboxpw = conf.get("hitboxPassword").toString();
		
		//Initializes all of the settings for the bots!
		//twitch
		twitch = new Configuration.Builder<PircBotX>()
				.setName(twitchu)
				.setLogin(name)
				.setServerHostname("irc.twitch.tv")
				.setServerPort(6667)
				.setServerPassword(twitchpw)
				.addAutoJoinChannel("#"+conf.get("twitchChannel").toString().toLowerCase())
				.setAutoReconnect(true)
				.addListener(new ChatRelayHandeler())
				.addListener(new CustomCommandHandeler())
				.addListener(new BotJoinHandeler())
				.addListener(new Infromation())
				.addListener(new JoinLeaveMessages());
				
		//hitbox
		hitbox = new Configuration.Builder<PircBotX>()
				.setName(hitboxu)
				.setLogin(name)
				.setServerHostname("irc.glados.tv")
				.setServerPort(6667)
				.setServerPassword(hitboxpw)
				.addAutoJoinChannel("#"+conf.get("hitboxChannel").toString().toLowerCase())
				.setAutoReconnect(true)
				.addListener(new ChatRelayHandeler())
				.addListener(new CustomCommandHandeler())
				.addListener(new BotJoinHandeler())
				.addListener(new Infromation())
				.addListener(new JoinLeaveMessages());
		
		//implement the plugin loader
		if(conf.get("enablePluginSystem").toString().equalsIgnoreCase("true"))
				pluginLoader();
		
		if(conf.get("enableModeration").toString().equalsIgnoreCase("true"))
			new FilterManager();
		
		//this activates !title and !game commands, as well as follower notifications
		try{
			new APIManager();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//output to a file?
		if(conf.get("enableOutput").toString().equalsIgnoreCase("true")){
			twitch.addListener(new Cleaner());
			hitbox.addListener(new Cleaner());
		}
		
		//add the bots to the manager and start them
		manager.addBot(twitch.buildConfiguration());
		manager.addBot(hitbox.buildConfiguration());
		manager.start();
		
		//set up the GUI class
		gui = new GUIContainer();
		
		if(Bennerbot.conf.get("enableOutput").toString().equalsIgnoreCase("true")){
			@SuppressWarnings("resource")
			Scanner scan = new Scanner(System.in);
			while(true)
				if(scan.hasNext())
					sendMessage(scan.nextLine(), true);
		}
		
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
	 * @param txt ~ the message to be sent
	 */
	public static void sendMessage(String txt){
		//Twitch
		manager.getBotById(0).sendRaw().rawLine("PRIVMSG #"+Bennerbot.conf.get("twitchChannel").toString().toLowerCase()+" :"+txt);
		//Hitbox
		manager.getBotById(1).sendRaw().rawLine("PRIVMSG #"+Bennerbot.conf.get("hitboxChannel").toString().toLowerCase()+" :"+txt);
		
		Cleaner.onOutput(txt);
	}
	/**
	 * used to send a message to the server that wont be writen to the output file
	 * @param txt ~ the message to be sent
	 */
	public static void sendMessage(String txt, String dontShow){
		//Twitch
		manager.getBotById(0).sendRaw().rawLine("PRIVMSG #"+Bennerbot.conf.get("twitchChannel").toString().toLowerCase()+" :"+txt);
		//Hitbox
		manager.getBotById(1).sendRaw().rawLine("PRIVMSG #"+Bennerbot.conf.get("hitboxChannel").toString().toLowerCase()+" :"+txt);
	}
	/**
	 * used to send a message to the server that is from the bot operator
	 * @param txt ~ the message to be sent
	 */
	public static void sendMessage(String txt, boolean fromuser){
		String server = "", user = "";
		//Twitch
		if(Bennerbot.conf.get("showSource").toString().equalsIgnoreCase("true")){server = " [twitch]";}
		if(Bennerbot.conf.get("showSendName").toString().equalsIgnoreCase("true")){user = Bennerbot.capitalize(Bennerbot.conf.get("twitchChannel").toString().toLowerCase())+": ";}
		manager.getBotById(0).sendRaw().rawLine("PRIVMSG #"+Bennerbot.conf.get("twitchChannel").toString().toLowerCase()+" :"+user+server+txt);
		//Hitbox
		if(Bennerbot.conf.get("showSource").toString().equalsIgnoreCase("true")){server = " [hitbox]";}
		if(Bennerbot.conf.get("showSendName").toString().equalsIgnoreCase("true")){user = Bennerbot.capitalize(Bennerbot.conf.get("htiboxChannel").toString().toLowerCase())+": ";}
		manager.getBotById(1).sendRaw().rawLine("PRIVMSG #"+Bennerbot.conf.get("hitboxChannel").toString().toLowerCase()+" :"+user+txt);
		
		Cleaner.onOutput(txt, true);
	}
	/**
	 * used to send a message to a specific bot
	 * @param txt ~ the message to be sent
	 * @param bot ~ the id of the bot, 0) twitch 1) hitbox
	 */
	public static void sendMessage(String txt, int bot){
		//Twitch
		if(bot == 0)
			manager.getBotById(0).sendRaw().rawLine("PRIVMSG #"+Bennerbot.conf.get("twitchChannel").toString().toLowerCase()+" :"+txt);
		//Hitbox
		else
			manager.getBotById(1).sendRaw().rawLine("PRIVMSG #"+Bennerbot.conf.get("hitboxChannel").toString().toLowerCase()+" :"+txt);
		
		Cleaner.onOutput(txt);
	}
	/**
	 * A simple function that capitalizes the first letter of the string pasted to it
	 * @param line ~ the string to be capitalized
	 * @return ~ the capitalized string
	 */
	public static String capitalize(String line){
	  return Character.toUpperCase(line.charAt(0)) + line.substring(1);
	}
	/**
	 * returns the content of the path specified
	 * @param path ~ the path to the file
	 * @return ~ the contents of the file
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
	 * @param path ~ the file object of the file you want to read
	 * @return ~ the contents of the file
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
	 * @param f ~ the file to be translated
	 * @return ~ the path to the file
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
	 * @param out ~ the string to be appended
	 * @param f ~ the file to append the string to
	 */
	public static void write(String out, File f){
		try {
			FileWriter writer = new FileWriter(f, true);
			writer.append(out);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(f.getPath().contains("output.html")){
			gui.display.update(out);
		}
	}
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
	 * @param u ~ the user
	 * @param c ~ the channel
	 * @return ~ weather or not the user is a moderator
	 */
	public static boolean isMod(User u, Channel c){
		return u.getChannelsHalfOpIn().contains(c) || u.getChannelsOpIn().contains(c) || u.getChannelsOwnerIn().contains(c) || u.getChannelsSuperOpIn().contains(c);
	}
	
	public static void deleteOldFiles(int daysBack, String dirWay) {    
        File directory = new File(dirWay);  
        if(directory.exists()){  
  
            File[] listFiles = directory.listFiles();              
            long purgeTime = System.currentTimeMillis() - (daysBack * 24 * 60 * 60 * 1000);  
            for(File listFile : listFiles) {  
                if(listFile.lastModified() < purgeTime) {  
                    if(!listFile.delete()) {  
                        System.err.println("Unable to delete file: " + listFile);  
                    }  
                }  
            }  
        } else {  
            logger.warn("Files were not deleted, directory " + dirWay + " does'nt exist!");  
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