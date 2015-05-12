package me.jdbener.levels;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.JoinEvent;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.PartEvent;

import me.jdbener.Bennerbot;
import me.jdbener.apis.APIManager;
import me.jdbener.utill.botId;

public class LevelManager extends ListenerAdapter<PircBotX>{
	public static Map<String, String> timeMap = new HashMap<String, String>();
	private static double modifer = 1.0; 
	
	public LevelManager(){
		setupUserLevelsTable();
		modifer = Double.parseDouble(Bennerbot.getConfigString("levelModifer"));
		if(modifer < 0.1) modifer = 0.1;
		
		Bennerbot.listener.addListener(new BennerBitManager());
	}
	public void onJoin(JoinEvent<PircBotX> e){
		timeMap.put(e.getUser().getNick(), new Date().getTime()+"");
	}
	public void onPart(PartEvent<PircBotX> e){
		long old = new Date().getTime();
		try{
			old = Long.valueOf(timeMap.get(e.getUser().getNick())).longValue();
		} catch (Exception ex){
			ex.printStackTrace();
		}
		long now = new Date().getTime();
		long dif = now - old;
		int xp = (int) ((dif / 1000)/Integer.parseInt(Bennerbot.getConfigString("oneXpEveryXSecond")));
		
		setXP(e.getUser().getNick(), getXP(e.getUser().getNick())+xp);
		BennerBitManager.setBits(e.getUser().getNick(), BennerBitManager.getBits(e.getUser().getNick())+(double)((int)(xp/(Integer.parseInt(Bennerbot.getConfigString("xpPerMessaged"))/Integer.parseInt(Bennerbot.getConfigString("currencyPerMessage"))))));
		
		timeMap.remove(e.getUser().getNick());
	}
	public void onMessage(MessageEvent<PircBotX> e){
		if(!e.getMessage().startsWith("!")){
			try{
				setXP(e.getUser().getNick(), getXP(e.getUser().getNick())+Integer.parseInt(Bennerbot.getConfigString("xpPerMessage")));
				BennerBitManager.setBits(e.getUser().getNick(), BennerBitManager.getBits(e.getUser().getNick())+Integer.parseInt(Bennerbot.getConfigString("currencyPerMessage")));
			} catch (Exception ex){
				ex.printStackTrace();
			}
		} 		
		if(e.getMessage().startsWith("!level") || e.getMessage().startsWith("!lvl")){
			if(e.getMessage().split(" ").length == 1){
				try{
					updateUser(e.getUser().getNick());
					Bennerbot.sendMessage(format(getXP(e.getUser().getNick())).replaceAll("<user>", Bennerbot.capitalize(e.getUser().getNick())));
				} catch (Exception ex){
					ex.printStackTrace();
					Bennerbot.sendMessage("Sorry something went wrong");
				}
			} else if(e.getMessage().split(" ").length == 2){
				if(Bennerbot.getConfigBoolean("levelCommandAllowOtherUserLookup"))
					try{
						String name = e.getMessage().split(" ")[1];
						updateUser(name);
						Bennerbot.sendMessage(format(getXP(name)).replaceAll("<user>", Bennerbot.capitalize(name)));
					} catch (Exception ex) {
						ex.printStackTrace();
						Bennerbot.sendMessage("Sorry something went wrong");
					}
				else
					Bennerbot.sendMessage("Sorry wrong format, try: "+Bennerbot.getConfigString("levelCommandName")+" <username>");
			} else {
				Bennerbot.sendMessage("Sorry wrong format, try: "+Bennerbot.getConfigString("levelCommandName")+" <username>");
			}
		}
	}
	public static void setupUserLevelsTable(){
		 try {
			 Connection c = APIManager.getConnection(); 
			 Statement stmt = c.createStatement();
			 String sql = "CREATE TABLE IF NOT EXISTS LEVELS " +
		                   "(BOTID		   INT,"
		                   +"USER          TEXT    NOT NULL, " + 
		                   " LEVEL         TEXT)"; 
		      stmt.executeUpdate(sql);
		      stmt.close();
		      c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	private static Map<String, String> getUserExperience(){
		Map<String, String> temp = new HashMap<String, String>();
		try{
		  Connection c = APIManager.getConnection();
		  Statement stmt = c.createStatement();
		  String sql = "SELECT * FROM LEVELS WHERE BOTID = "+botId.getBotID()+";"; 
		  
		  ResultSet rs = stmt.executeQuery(sql);
		  
		  while(rs.next()){
			  String user = rs.getString("USER");
			  String level = rs.getString("LEVEL");
			  temp.put(user, level);
		  }
		} catch (SQLException e){
			e.printStackTrace();
		}
		return temp;
	}
	public static int getXP(String user){
		try{
			Map<String, String> temp = getUserExperience();
			for(Entry<String, String> e: temp.entrySet()){
				if(e.getKey().equalsIgnoreCase(user))
					return Integer.parseInt(e.getValue());
			}
		} catch (Exception e){
			e.printStackTrace();
		}
		return 0;
	}
	public static void setXP(String user, int xp){
		try{
			  Connection c = APIManager.getConnection();
			  Statement stmt = c.createStatement();
			  String sql = "DELETE FROM LEVELS WHERE USER = '"+user+"' && BOTID = "+botId.getBotID()+"";
			  stmt.execute(sql);
			  sql = "INSERT INTO LEVELS VALUES ("+botId.getBotID()+",'"+user+"','"+xp+"');";
			  
			  stmt.execute(sql);
		} catch (SQLException e){
			 e.printStackTrace();
		}
	}
	public static int xp2Level(int xp){
		//(25 + (
		return (int) ((25 + Math.sqrt(625 + 100 * (xp * modifer))) / 50);
	}
	public static int level2XP(int level){
		return (int) ((25 * Math.pow(level, 2)-25*level)*modifer);
	}
	public static int xp2Percentage(int xp){
		int level = xp2Level(xp);
		int levelxp = level2XP(level);
		int nextlevel = level + 1;
		int nextxp = level2XP(nextlevel);
		return (int)(((double)(xp-levelxp)/(nextxp-levelxp))*100);
	}
	public static double xp2Decimal(int xp){
		int level = xp2Level(xp);
		int levelxp = level2XP(level);
		int nextlevel = level + 1;
		int nextxp = level2XP(nextlevel);
		return (double)(xp-levelxp)/(nextxp-levelxp);
	}
	public static String format(int xp){
		int level = xp2Level(xp);
		int nextlevel = level + 1;
		int nextxp = level2XP(nextlevel);
		int percentage = xp2Percentage(xp);
		double decimal = xp2Decimal(xp);
		
		String out = Bennerbot.getConfigString("levelCommandFormat")
				.replaceAll("<level>", level+"")
				.replaceAll("<currentxp>", xp+"")
				.replaceAll("<nextlevelxp>", nextxp+"")
				.replaceAll("<xptillnextlevel>", (nextxp-xp)+"")
				.replaceAll("<nextlevel>", nextlevel+"")
				.replaceAll("<percentage>", percentage+"")
				.replaceAll("<percentagenoformat>", decimal+"");
		
		return out;
	}
	private void updateUser(final String user){
		new Thread(new Runnable(){
			@Override
			public void run() {
				long old = new Date().getTime();
				try{
					old = Long.valueOf(timeMap.get(user)).longValue();
				} catch (Exception ex){
					ex.printStackTrace();
					timeMap.put(user, new Date().getTime()+"");
				}
				long now = new Date().getTime();
				long dif = now - old;
				int xp = (int) ((dif / 1000)/Integer.parseInt(Bennerbot.getConfigString("oneXpEveryXSecond")));
				
				setXP(user, getXP(user)+xp);
				
				timeMap.put(user, new Date().getTime()+"");
			}
			
		}).start();
	}
}