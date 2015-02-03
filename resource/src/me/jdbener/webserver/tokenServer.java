package me.jdbener.webserver;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import me.jdbener.Bennerbot;
import me.jdbener.webserver.webserver.WebserverListener;

public class tokenServer {
	File f = new File("../.token");
	public tokenServer() {
		webserver s = new webserver(new WebserverListener() {
            @Override
            public void webserverStarted() {
                Bennerbot.logger.info("Webserver Started");
            }
 
            @Override
            public void webserverStopped() {
            	Bennerbot.logger.info("Webserver Stoped");
            }
 
            @Override
            public void webserverError(String error) {
            	Bennerbot.logger.info("Webserver Stoped with Error Message: "+error);
            }
 
            @Override
            public void webserverTokenReceived(String token) {
                System.out.println("Webserver recived new token");
                updateToken(token);
            }
        });
        new Thread(s).start();
    }
	public void updateToken(String token){
		try{
			if(!f.exists()){
				f.createNewFile();
			} 
			
			f.delete();
			
			FileWriter fw = new FileWriter(f);
			fw.append(token);
			fw.close();
		} catch (IOException e){
			e.printStackTrace();
		}
	}
	public String getToken(){
		try {
			if(!f.exists())
				f.createNewFile();
			@SuppressWarnings("resource")
			Scanner s = new Scanner(f.toURI().toURL().openStream()).useDelimiter("\\A");
			return s.hasNext() ? s.next() : "";
		} catch (IOException e) {e.printStackTrace();}
		return "";
	}
}
