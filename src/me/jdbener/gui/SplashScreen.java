package me.jdbener.gui; 

import javax.swing.JWindow;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

public class SplashScreen extends JWindow {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4181665667244408191L;
	Image img = Toolkit.getDefaultToolkit().getImage(SplashScreen.class.getResource("/me/jdbener/gui/splash.png"));
	public SplashScreen() {
		setSize(675,420); 
		setLocationRelativeTo(null);
	}
	public void paint(Graphics g) { 
		g.drawImage(img,0,0,this); 
	} 
	public void start(){
		setVisible(true);
	}
	public void end(){
		setVisible(false);
		dispose();
	}
}
