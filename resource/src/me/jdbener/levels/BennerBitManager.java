package me.jdbener.levels;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

import me.jdbener.Bennerbot;
import me.jdbener.apis.APIManager;
import me.jdbener.moderataion.FilterManager;
import me.jdbener.utill.botId;

public class BennerBitManager extends ListenerAdapter<PircBotX>{
	public BennerBitManager(){
		setupUserBitsTable();
		
	}
	public void onMessage(MessageEvent<PircBotX> e){
		if(e.getMessage().startsWith(Bennerbot.configGetString("currencyLookupCommandName"))){
			if(e.getMessage().split(" ").length == 1){
				try{
					Bennerbot.sendMessage(Bennerbot.capitalize(e.getUser().getNick())+" you have "+(int)getBits(e.getUser().getNick())+" "+Bennerbot.configGetString("currencyName"));
				} catch (Exception ex){
					ex.printStackTrace();
					Bennerbot.sendMessage("Sorry but it seams that something has gone wrong");
				}
			} else if(e.getMessage().split(" ").length == 2){
				if(Bennerbot.configBoolean("currencyLookupCommandAllowOtherUserLookup"))
					try{
						if(Bennerbot.isMod(e.getUser(), e.getChannel())){
							String user = e.getMessage().split(" ")[1];
							Bennerbot.sendMessage(Bennerbot.capitalize(e.getUser().getNick())+" "+Bennerbot.capitalize(user)+" has "+(int)getBits(user)+" "+Bennerbot.configGetString("currencyName"));
						} else {
							Bennerbot.sendMessage(Bennerbot.capitalize(e.getUser().getNick())+" has tried to use a command they dont have permision to");
							FilterManager.punish(e.getUser().getNick());
						}
					} catch(Exception ex){
						ex.printStackTrace();
						Bennerbot.sendMessage("Sorry but it seams that something has gone wrong");
					}
				else
					Bennerbot.sendMessage("Sorry wrong format, try: "+Bennerbot.configGetString("currencyLookupCommandName"));
			} else {
				Bennerbot.sendMessage("Sorry wrong format, try: "+Bennerbot.configGetString("currencyLookupCommandName")+" <username>");
			}
		}
		if(e.getMessage().startsWith(Bennerbot.configGetString("currencyGiveCommandName"))){
			if(e.getMessage().split(" ").length == 3){
				try{
					String user = e.getMessage().split(" ")[1].toLowerCase();
					double bits = Double.parseDouble(e.getMessage().split(" ")[2]);
					
					if(!(getBits(e.getUser().getNick()) < bits)){
						setBits(e.getUser().getNick(), -1*bits);
						setBits(user, bits);
						Bennerbot.sendMessage(Bennerbot.capitalize(e.getUser().getNick())+", you have successfully transfered "+bits+" "+Bennerbot.configGetString("currencyName")+" to: "+Bennerbot.capitalize(user));
					} else {
						Bennerbot.sendMessage(Bennerbot.capitalize(e.getUser().getNick())+", you dont have enouph "+Bennerbot.configGetString("currencyName")+" to compleat this transaction");
					}
					
					
					Bennerbot.sendMessage(Bennerbot.capitalize(e.getUser().getNick())+" "+Bennerbot.capitalize(user)+" now has "+(int)getBits(user)+" "+Bennerbot.configGetString("currencyName"));
				} catch(Exception ex){
					ex.printStackTrace();
					Bennerbot.sendMessage("Sorry but it seams that something has gone wrong");
				}
			} else {
				Bennerbot.sendMessage("Sorry wrong format, try: "+Bennerbot.configGetString("currencyGiveCommandName")+" <username> <"+Bennerbot.configGetString("currencyName")+">");
			}
		}
	}
	public static void setupUserBitsTable(){
		 try {
			 Connection c = APIManager.getConnection(); 
			 Statement stmt = c.createStatement();
			 String sql = "CREATE TABLE IF NOT EXISTS BITS" +
		                   "(BOTID		  INT,"
		                   +"USER         TEXT    NOT NULL, " + 
		                   " BITS         TEXT)"; 
		      stmt.executeUpdate(sql);
		      stmt.close();
		      c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	private static Map<String, String> getUserBits(){
		Map<String, String> temp = new HashMap<String, String>();
		try{
		  Connection c = APIManager.getConnection();
		  Statement stmt = c.createStatement();
		  String sql = "SELECT * FROM BITS WHERE BOTID = "+botId.getBotID()+";"; 
		  
		  ResultSet rs = stmt.executeQuery(sql);
		  
		  while(rs.next()){
			  String user = rs.getString("USER");
			  String level = rs.getString("BITS");
			  temp.put(user, level);
		  }
		} catch (SQLException e){
			e.printStackTrace();
		}
		return temp;
	}
	public static double getBits(String user){
		try{
			Map<String, String> temp = getUserBits();
			for(Entry<String, String> e: temp.entrySet()){
				if(e.getKey().equalsIgnoreCase(user))
					return Double.parseDouble(e.getValue());
			}
		} catch (Exception e){
			e.printStackTrace();
		}
		return 0.0;
	}
	public static void setBits(String user, double d){
		try{
			  Connection c = APIManager.getConnection();
			  Statement stmt = c.createStatement();
			  String sql = "DELETE FROM BITS WHERE USER = '"+user+"' && BOTID = "+botId.getBotID();
			  stmt.execute(sql);
			  sql = "INSERT INTO BITS VALUES ("+botId.getBotID()+",'"+user+"','"+d+"');";
			  
			  stmt.execute(sql);
		} catch (SQLException e){
			 e.printStackTrace();
		}
	}
	public static void purgeBits(){
		try {
			 Connection c = APIManager.getConnection(); 
			 Statement stmt = c.createStatement();
			 String sql = "DELETE FROM BITS WHERE BOTID = "+botId.getBotID(); 
		     stmt.executeUpdate(sql);
		     stmt.close();
		     c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static int bits2XP(double bits){
		return (int) bits * 10;
	}
}
