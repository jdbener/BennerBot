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

public class BennerBitManager extends ListenerAdapter<PircBotX>{
	public BennerBitManager(){
		setupUserBitsTable();
	}
	public void onMessage(MessageEvent<PircBotX> e){
		if(e.getMessage().startsWith("!bits") && !Bennerbot.configBoolean("optoutoftheBennerBitProgram")){
			if(e.getMessage().split(" ").length == 1){
				try{
					Bennerbot.sendMessage(Bennerbot.capitalize(e.getUser().getNick())+" you have ฿"+(int)getBits(e.getUser().getNick())+" BennerBits");
				} catch (Exception ex){
					ex.printStackTrace();
					Bennerbot.sendMessage("Sorry but it seams that something has gone wrong");
				}
			} else if(e.getMessage().split(" ").length == 2){
				try{
					String user = e.getMessage().split(" ")[1];
					Bennerbot.sendMessage(Bennerbot.capitalize(e.getUser().getNick())+" "+Bennerbot.capitalize(user)+" has ฿"+(int)getBits(user)+" BennerBits");
				} catch(Exception ex){
					ex.printStackTrace();
					Bennerbot.sendMessage("Sorry but it seams that something has gone wrong");
				}
			} else {
				Bennerbot.sendMessage("Sorry wrong format, try: !bits <username>");
			}
		}
		if(e.getMessage().startsWith("!givebits") && !Bennerbot.configBoolean("optoutoftheBennerBitProgram")){
			if(e.getMessage().split(" ").length == 3){
				try{
					String user = e.getMessage().split(" ")[1];
					double bits = Double.parseDouble(e.getMessage().split(" ")[2]);
					
					if(!(getBits(e.getUser().getNick()) < bits)){
						tradeBits(new String[]{e.getUser().getNick(), user}, new double[]{(-1*bits), bits});
						Bennerbot.sendMessage(Bennerbot.capitalize(e.getUser().getNick())+", you have successfully transfered ฿"+bits+" BennerBits to: "+Bennerbot.capitalize(user));
					} else {
						Bennerbot.sendMessage(Bennerbot.capitalize(e.getUser().getNick())+", you dont have enouph bits to compleat this transaction");
					}
					
					
					Bennerbot.sendMessage(Bennerbot.capitalize(e.getUser().getNick())+" "+Bennerbot.capitalize(user)+" has ฿"+(int)getBits(user)+" BennerBits");
				} catch(Exception ex){
					ex.printStackTrace();
					Bennerbot.sendMessage("Sorry but it seams that something has gone wrong");
				}
			} else {
				Bennerbot.sendMessage("Sorry wrong format, try: !bits <username> <bits>");
			}
		}
	}
	public static void setupUserBitsTable(){
		 try {
			 Connection c = APIManager.getConnection(); 
			 Statement stmt = c.createStatement();
			 String sql = "CREATE TABLE IF NOT EXISTS BITS" +
		                   "(USER          TEXT    NOT NULL, " + 
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
		  String sql = "SELECT * FROM BITS;"; 
		  
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
		
	}
	public static void tradeBits(String[] user, double[] d){
		for(int i = 0; i < user.length; i++)
			try{
				setBits(user[i], getBits(user[i])+d[i]);
			} catch (Exception e){
				e.printStackTrace();
			}
	}
	public static int bits2XP(double bits){
		return (int) bits * 10;
	}
}
