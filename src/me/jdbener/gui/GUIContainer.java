/**
 * This class creates a simple GUI with a button to open an output display
 * Author: Jdbener (Joshua Dahl)
 * Date: 11/11/14
 */

package me.jdbener.gui;

import java.awt.FlowLayout;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import me.jdbener.Bennerbot;

public class GUIContainer{
	JButton open,								//the button used to open the display
			rsz;
	JFrame f;									//the container object
	JLabel l;									//the label that display basic information or an error message
	public GUIChatDisplay display;				//an instance of the chat display class

	public GUIContainer() {
		if(!GraphicsEnvironment.isHeadless()){
			Bennerbot.logger.info("Started Loading the GUI");
			//creates the container
			f = new JFrame(Bennerbot.name+" v"+Bennerbot.version);
			//creates the label
			l = new JLabel("Please close this window to stop the bot!");
			//creates the button
			open = new JButton("Open Display");
			//makes sure that clicking the button will do something
			open.addActionListener(new ActionListener() {
				 @Override
				public void actionPerformed(ActionEvent arg0) {
					//if the display is visible
					 if(display.d.isVisible()){
						//hide the display
						display.d.setVisible(false);
						//and change the text to represent accordingly
						open.setText("Open Display");
					//if the display is not visible
					 } else {
						 //show the display
						display.d.setVisible(true);
						//change the text to represent the correct action
						open.setText("Hide Display");
					}
				}
	        }); 
			rsz = new JButton("Resize Display");
			rsz.addActionListener(new ActionListener() {
				 @Override
				public void actionPerformed(ActionEvent arg0) {
					 display.d.setSize(Integer.parseInt(Bennerbot.conf.get("defaultGUIDisplaySize").toString().split(",")[0]), Integer.parseInt(Bennerbot.conf.get("defaultGUIDisplaySize").toString().split(",")[1]));
				 }
			});
			//set the layout
			f.setLayout(new FlowLayout());
			//add the label
			f.add(l);
			//this variable will determine how tall the GUI is
			int size = 60;
			//if output is enabled
			if(Bennerbot.conf.get("enableOutput").toString().equalsIgnoreCase("true")){
				//add the button
				f.add(open);
				f.add(rsz);
				size = 85;
			}
			//set the size
			f.setSize(250, size);
			//creates an instance of the chat display class
			display = new GUIChatDisplay(size);
			//add an icon
	        try {
				f.setIconImage(ImageIO.read(new File("resource/BennerBotLogo.png")));
			} catch (IOException e) {
				e.printStackTrace();
			}
			//make the container visible
			f.setVisible(true);
			//makes the GUI non resizable
			f.setResizable(false);
			//close the bot when the GUI is closed
			f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			//this will update the button every tenth of a second
			Runnable runnable = new Runnable() {
			    @Override
				public void run() {
					if(display.d.isVisible()){
						open.setText("Hide Display");
					} else {
						open.setText("Open Display");
					}
			    }
			};
			//add the update to the execution thread
			ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
			executor.scheduleAtFixedRate(runnable, 0, 1, TimeUnit.SECONDS);
		} else {
			Bennerbot.logger.info("Environment is headless, continuing bot without GUI");
		}
	}
}
