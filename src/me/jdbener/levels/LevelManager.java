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
import me.jdbener.lib.botId;

public class LevelManager extends ListenerAdapter<PircBotX>{
	public static Map<String, String> timeMap = new HashMap<String, String>();
	
	public LevelManager(){
		setupUserLevelsTable();
		
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
		int xp = (int) ((dif / 1000)/30);
		
		setXP(e.getUser().getNick(), getXP(e.getUser().getNick())+xp);
		
		BennerBitManager.setBits(e.getUser().getNick(), BennerBitManager.getBits(e.getUser().getNick())+(double)((int)(xp/10)));
		
		timeMap.remove(e.getUser().getNick());
	}
	public void onMessage(MessageEvent<PircBotX> e){
		if(!e.getMessage().startsWith("!")){
			setXP(e.getUser().getNick(), getXP(e.getUser().getNick())+10);
			BennerBitManager.setBits(e.getUser().getNick(), BennerBitManager.getBits(e.getUser().getNick())+1);
		} 
		
		if(e.getMessage().startsWith("!level") || e.getMessage().startsWith("!lvl")){
			long old = new Date().getTime();
			try{
				old = Long.valueOf(timeMap.get(e.getUser().getNick())).longValue();
			} catch (Exception ex){
				ex.printStackTrace();
				timeMap.put(e.getUser().getNick(), new Date().getTime()+"");
			}
			long now = new Date().getTime();
			long dif = now - old;
			int xp = (int) ((dif / 1000)/30);
			
			setXP(e.getUser().getNick(), getXP(e.getUser().getNick())+xp);
			
			timeMap.put(e.getUser().getNick(), new Date().getTime()+"");
			
			if(e.getMessage().split(" ").length == 1){
				try{
					Bennerbot.sendMessage(Bennerbot.capitalize(e.getUser().getNick())+" your level information is: "+format(getXP(e.getUser().getNick())));
				} catch (Exception ex){
					ex.printStackTrace();
					Bennerbot.sendMessage("Sorry something went wrong");
				}
			} else if(e.getMessage().split(" ").length == 2){
					try{
						String name = e.getMessage().split(" ")[1];
						Bennerbot.sendMessage(Bennerbot.capitalize(e.getUser().getNick())+" "+Bennerbot.capitalize(name)+"'s level infromation is: "+format(getXP(name)));
					} catch (Exception ex) {
						ex.printStackTrace();
						Bennerbot.sendMessage("Sorry something went wrong");
					}
			} else {
				Bennerbot.sendMessage("Sorry wrong format, try: !level <username>");
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
		return (int) ((25 + Math.sqrt(625 + 100 * xp) ) / 50);
	}
	public static int level2XP(int level){
		return (int) (25 * Math.pow(level, 2)-25*level);
	}
	public static int xp2Percentage(int xp){
		int level = xp2Level(xp);
		int levelxp = level2XP(level);
		int nextlevel = level + 1;
		int nextxp = level2XP(nextlevel);
		return (int)(((double)(xp-levelxp)/(nextxp-levelxp))*100);
	}
	public static double xp2Percentage(int xp, boolean noFormat){
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
		
		return level+" ("+(xp)+"\\"+(nextxp)+") "+nextlevel;
	}
}