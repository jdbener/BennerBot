package me.jdbener.lib;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Scanner;

import me.jdbener.apis.APIManager;

public class botId {
	private static File f = new File("../.botid");
	/*public static void main (String[] args){
		setupBotIDTable();
		System.out.println(getBotID("jdbener"));
	}*/
	public botId(){
		setupBotIDTable();
	}
	private static String encrypt(String s){
		try {
			return new String(MessageDigest.getInstance("MD5").digest(s.getBytes()));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return s;
	}
	public static void setupBotIDTable(){
		 try {
			 Connection c = APIManager.getConnection(); 
			 Statement stmt = c.createStatement();
			 String sql = "CREATE TABLE IF NOT EXISTS BOTS" +
		                   "(ID INT PRIMARY KEY     NOT NULL, " + 
		                   " HASH         TEXT      NOT NULL," +
		                   "UNIQUE (ID))"; 
		      stmt.executeUpdate(sql);
		      stmt.close();
		      c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static int getBotID(String hash){
		try{
			Connection c = APIManager.getConnection();
			Statement stmt = c.createStatement();
			//File f = new File("config/.botid");
			if(!f.exists()){
				String sql = "SELECT * FROM BOTS;"; 
				ResultSet rs = stmt.executeQuery(sql);
				rs.last();

				FileWriter fw = new FileWriter(f);
				fw.append(hash);
				fw.close();
				
				sql = "INSERT INTO BOTS VALUES ("+(rs.getRow()+1)+",'"+encrypt(hash)+"')";
				stmt.execute(sql);
			}
	
			String sql = "SELECT * FROM BOTS;"; 
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()){
				if(rs.getString("HASH").equalsIgnoreCase(encrypt(hash))){
					return rs.getInt("ID");
				}
			}
		} catch (SQLException | IOException e){
			e.printStackTrace();
		}
		return 0;
	}
	public static int getBotID(){
		return getBotID(getHash());
	}
	public static void updateFile(String hash){
		try{
			Connection c = APIManager.getConnection();
			Statement stmt = c.createStatement();
			//File f = new File("config/.botid");
			
			if(!f.exists()){
				String sql = "SELECT * FROM BOTS;"; 
				ResultSet rs = stmt.executeQuery(sql);
				rs.last();

				FileWriter fw = new FileWriter(f);
				fw.append(hash);
				fw.close();
							
				sql = "INSERT INTO BOTS VALUES ("+(rs.getRow()+1)+",'"+encrypt(hash)+"')";
				stmt.execute(sql);
			} 
			
			String sql = "SELECT * FROM BOTS;"; 
			ResultSet rs = stmt.executeQuery(sql);
			
			String oldhash = getHash();
			
			int id = 0;
			while(rs.next()){
				if(rs.getString("HASH").equalsIgnoreCase(encoder.encode("@"+oldhash, 10000, 1))){
					id = rs.getInt("ID")+1;
				}
			}
			if(id == 0){
				id = rs.getRow()+1;
			}
			
			f.delete();
			
			FileWriter fw = new FileWriter(f);
			fw.append(hash);
			fw.close();
			
			rs = stmt.executeQuery("SELECT * FROM BOTS WHERE HASH = '"+encrypt(hash)+"'");
			int r1 = rs.getRow();
			rs.last();
			int r2 = rs.getRow();
			
			if(r1 == r2){
				sql = "INSERT INTO BOTS VALUES ("+id+",'"+encrypt(hash)+"')";
				stmt.execute(sql);
			}
		} catch (SQLException | IOException e){
			e.printStackTrace();
		}
	}
	public static String getHash(){
		try {
			//File f = new File("config/.botid");
			if(!f.exists()){
				updateFile(new Date().getTime()+"");
			}
			@SuppressWarnings("resource")
			Scanner s = new Scanner(f.toURI().toURL().openStream()).useDelimiter("\\A");
			return s.hasNext() ? s.next() : "";
		} catch (IOException e) {e.printStackTrace();}
		return "";
	}
}
