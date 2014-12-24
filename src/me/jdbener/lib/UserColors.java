/*
 * This class is used in the manegment of user colors.
 */
package me.jdbener.lib;

import java.awt.Color;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import me.jdbener.apis.APIManager;

public class UserColors {
	//twitch api colors
	public static user[] defaultcolors = new user[]{
		new user("Red", "#FF0000"),
		new user("Blue", "#0000FF"),
		new user("Green", "#00FF00"),
		new user("FireBrick", "#B22222"),
		new user("Coral", "#FF7F50"),
		new user("YellowGreen", "#9ACD32"),
		new user("OrangeRed", "#FF4500"),
		new user("SeaGreen", "#2E8B57"),
		new user("GoldenRod", "#DAA520"),
		new user("Chocolate", "#D2691E"),
		new user("CadetBlue", "#5F9EA0"),
		new user("DodgerBlue", "#1E90FF"),
		new user("HotPink", "#FF69B4"),
		new user("BlueViolet", "#8A2BE2"),
		new user("SpringGreen", "#00FF7F")
	};

	
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
	public Color getColorForUser(String name) {
        Color color = new Color(0);
        if (inFile(name)){ //Cached from USERCOLOR notices
        	for(user uc: getUserColors()){
   			 if(uc.getUser().equalsIgnoreCase(name))
   				 color=uc.getColor();
   		 	}
        }else {
            int n = name.charAt(0) + name.charAt(name.length() - 1);
            color = defaultcolors[n % defaultcolors.length].getColor();
            addUserColor(name, color);
        }
        return color;
	}
	
	public void UpdateUserColor(String name, Color color){
		try{
			  Connection c = APIManager.getConnection();
			  Statement stmt = c.createStatement();
			  String sql = "DELETE FROM UC WHERE USER = '"+name+"'";
			  stmt.execute(sql);
			  sql = "INSERT INTO UC VALUES ('"+name+"','"+rgb2Hex(color)+"');";
			  
			  stmt.execute(sql);
		} catch (SQLException e){
			 e.printStackTrace();
		}
	}
	
	public static void setupUserColorTable(){
		 try {
			 Connection c = APIManager.getConnection(); 
			 Statement stmt = c.createStatement();
			 String sql;
			 /*if(Bennerbot.conf.get("resetColorDatabase").toString().equalsIgnoreCase("true")){
				 sql = "DROP TABLE IF EXISTS UC;"; 
				 stmt.executeUpdate(sql);
			 }*/
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