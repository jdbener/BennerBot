package me.jdbener.gui;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

import me.jdbener.Bennerbot;
import me.jdbener.lib.SmartScroller;

public class DisplayPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2718171538512735809L;
	private JTextField displayInput;
	private HTMLEditorKit kit = new HTMLEditorKit();		//this is the editor kit used by the display
	private HTMLDocument doc = new HTMLDocument();			//the document used by the display
	private StyleSheet styleSheet = doc.getStyleSheet();	//the CSS used by the system
	
	/**
	 * Create the panel.
	 */
	public DisplayPanel() {
		//load the stylesheat
		try {
			styleSheet.importStyleSheet(new File("resource/layout.css").toURI().toURL());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportBorder(null);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		add(scrollPane, BorderLayout.CENTER);
		
		JEditorPane displayEditorPane = new JEditorPane("text/html", "<center><h1>Loading...</h1>Please Wait!</center>");
		displayEditorPane.setEditable(false);
		displayEditorPane.setEditorKit(kit);
		displayEditorPane.setDocument(doc);
		displayEditorPane.addHyperlinkListener(new HyperlinkListener() {
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
		displayEditorPane.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		scrollPane.setViewportView(displayEditorPane);
		
		new SmartScroller(scrollPane);
		
		displayInput = new JTextField();
		displayInput.setToolTipText("Enter messages here, press enter to send them");
		displayInput.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				new Thread(new Runnable(){
					@Override
					public void run() {
						//gets the text of the text area
						String out = displayInput.getText();
						//clear the text area
						displayInput.setText("");
						//text?
						if(!out.equalsIgnoreCase("")){
							if(out.startsWith("~"))
								try{
									Bennerbot.sendMessage(out.replace(out.split(" ")[0], ""), Bennerbot.getBotIDbyName(out.split(" ")[0]), true);
								} catch(Exception ex){
									Bennerbot.sendMessage(out, true);
								}
							else
								//send the text to the server
								Bennerbot.sendMessage(out, true);
						}
					}
				}).start();;
			}
		});
		displayInput.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		add(displayInput, BorderLayout.SOUTH);
		
		//this sets up the "ghost" text in the text area
		Runnable runnable = new Runnable() {
			public void run() {
				//if the text area is focused
				if(!displayInput.isFocusOwner()){
					//if there is no message in the text area
					if(displayInput.getText().equalsIgnoreCase("")){
						//enter the tutorial message
						displayInput.setText("Enter messages here, press enter to send them");
					}
					//if the text area is not focused
				} else {
					//if the tutorial messaged is entered
					if(displayInput.getText().equalsIgnoreCase("Enter messages here, press enter to send them")){
						//clear the text area
						displayInput.setText("");
					}
				}
			}
		};
				
		//run the ghost text function every 100 milliseconds
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
		executor.scheduleAtFixedRate(runnable, 0, 100, TimeUnit.MILLISECONDS);
		
		//set the style sheet
		kit.setStyleSheet(styleSheet);
		//display a welcome message
		try {
			kit.insertHTML(doc, doc.getLength(), "Welcome to "+Bennerbot.name+" v"+Bennerbot.version, 0, 0, null);
		} catch (BadLocationException | IOException e1) {
			e1.printStackTrace();
		}
	}
	public void append(String write){
		try {
			kit.setStyleSheet(styleSheet);
			kit.insertHTML(doc, doc.getLength(), write, 0, 0, null);
		} catch (IOException | BadLocationException e) {
			e.printStackTrace();
		}
	}
	public static JFrame createJFrame(DisplayPanel p){
		JFrame out = new JFrame();
		out.setContentPane(p);
		return out;
	}
}
