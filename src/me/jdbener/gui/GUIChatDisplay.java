/**
 * This class creates a display for chat
 * Author: Jdbener (Joshua Dahl)
 * 11/13/14
 */

package me.jdbener.gui;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

import me.jdbener.Bennerbot;
import me.jdbener.utilities.SmartScroller;

@SuppressWarnings("unused")
public class GUIChatDisplay implements ActionListener{
	public JFrame d;										//the frame for displaying content
	private JEditorPane e; 									//displays html
	private JButton send;									//the button used to send messages
	private JTextField t;									//where users can enter text to send to the server
	private HTMLEditorKit kit = new HTMLEditorKit();		//this is the editor kit used by the display
	private HTMLDocument doc = new HTMLDocument();			//the document used by the display
	private StyleSheet styleSheet = doc.getStyleSheet();	//the CSS used by the system
	
	/**
	 * this function sets up everything that needs to be set up
	 */
	@SuppressWarnings("deprecation")
	public GUIChatDisplay(int movey)  {
		//load the stylesheat
		try {
			styleSheet.importStyleSheet(new File("resource/layout.css").toURI().toURL());
		} catch (MalformedURLException e2) {
			e2.printStackTrace();
		}
		
		//create the frame
		d = new JFrame(Bennerbot.name+" v"+Bennerbot.version+" ~ Chat");
		//creates a new text area
		t = new JTextField();
		//set the size
		t.setPreferredSize(new Dimension(230, 25));
		//add the action
		t.addActionListener(this);
		//creates the display
		e = new JEditorPane("text/html", "<center><h1>Loading...</h1>Please Wait!</center>");
		//makes it so that you cant edit the contents of the display
		e.setEditable(false);
		//apply the kit
		e.setEditorKit(kit);
		//apply the document
		e.setDocument(doc);
		//remove its border
		e.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		e.addHyperlinkListener(new HyperlinkListener() {
		    public void hyperlinkUpdate(HyperlinkEvent e) {
		        if(e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
		            if(Desktop.isDesktopSupported()) {
		                try {
		                    Desktop.getDesktop().browse(e.getURL().toURI());
		                }
		                catch (IOException | URISyntaxException ex) {
		                    ex.printStackTrace();
		                }
		            }
		        }
		    }
		}
		);
		//create a scroll area
		JScrollPane pane = new JScrollPane(e);
		//remove the horizontal scroll bars
		pane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		//make the vertical scroll bars always visible
		pane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		//remove the border arround the pane
		pane.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		//set the layout that the frame uses
		d.setLayout(new BorderLayout());
		//add the display
		d.add(pane, BorderLayout.CENTER);
		//if user output is enabled
		if(Bennerbot.conf.get("enableUserOutput").toString().equalsIgnoreCase("true"))
			//add the the text area
			d.add(t, BorderLayout.PAGE_END);
		//set the size
		d.setSize(Integer.parseInt(Bennerbot.conf.get("defaultGUIDisplaySize").toString().split(",")[0]), Integer.parseInt(Bennerbot.conf.get("defaultGUIDisplaySize").toString().split(",")[1]));
		//move to just bellow the previous GUI
		d.move(0, movey);
		//add an icon
        try {
			d.setIconImage(ImageIO.read(new File("resource/BennerBotLogo.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
        //set up the scrolling system
		new SmartScroller(pane);
		
		//set the style sheet
		kit.setStyleSheet(styleSheet);
		//display a welcome message
		try {
			kit.insertHTML(doc, doc.getLength(), "Welcome to "+Bennerbot.name+" v"+Bennerbot.version, 0, 0, null);
		} catch (BadLocationException | IOException e1) {
			e1.printStackTrace();
		}
		
		//this sets up the "ghost" text in the text area
		Runnable runnable = new Runnable() {
		    public void run() {
		    	//if the text area is focused
		    	if(!t.isFocusOwner()){
		    		//if there is no message in the text area
		    		if(t.getText().equalsIgnoreCase("")){
		    			//enter the tutorial message
		    			t.setText("Enter messages here, press enter to send them");
		    		}
		    	//if the text area is not focused
		    	} else {
		    		//if the tutorial messaged is entered
		    		if(t.getText().equalsIgnoreCase("Enter messages here, press enter to send them")){
		    			//clear the text area
		    			t.setText("");
		    		}
		    	}
		    }
		};
		
		//run the ghost text function every 100 milliseconds
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
		executor.scheduleAtFixedRate(runnable, 0, 100, TimeUnit.MILLISECONDS);
	}
	
	public GUIChatDisplay(){
		new GUIChatDisplay(85);
	}
	
	/**
	 * This function is called whenever something that should be displayed is written to the file
	 * @param write ~ the value to be written to the display
	 */
	public void update(String write){
		try {
			kit.setStyleSheet(styleSheet);
			kit.insertHTML(doc, doc.getLength(), write, 0, 0, null);
		} catch (IOException | BadLocationException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * this function will be run whenever the button is clicked, it checks which OS is being used and then launches the appropriate browser
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		//gets the text of the text area
		String out = t.getText();
		//clear the text area
		t.setText("");
		
		//text?
		if(!out.equalsIgnoreCase(""))
			//send the text to the server
			Bennerbot.sendMessage(out, true);
	}
}
