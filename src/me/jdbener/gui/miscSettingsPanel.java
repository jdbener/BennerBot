package me.jdbener.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;

import me.jdbener.Bennerbot;
import me.jdbener.apis.APIManager;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

public class miscSettingsPanel extends JPanel {
	private static final long serialVersionUID = 3270562443564784442L;
	private JCheckBox chckbxEnableRellay, chckbxShowMessageSource;
	private JTextField streamTitle;
	private JTextField streamGame;
	private JButton btnUpdate;
	private String lastGame = "blank", lastTitle = "blank";

	/**
	 * Create the panel.
	 */
	public miscSettingsPanel() {
		//TODO make a way to generate a twitch access token
		setLayout(null);
		
		JPanel relayPanel = new JPanel();
		relayPanel.setBorder(new TitledBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null), "Relay Settings", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		relayPanel.setBounds(314, 12, 188, 87);
		add(relayPanel);
		relayPanel.setLayout(null);
		
		chckbxEnableRellay = new JCheckBox("Enable Relay?");
		chckbxEnableRellay.setToolTipText("Weather or not messages should be sent from one server to another");
		chckbxEnableRellay.setEnabled(true);
		chckbxEnableRellay.setSelected(true);
		chckbxEnableRellay.setBounds(8, 21, 172, 24);
		chckbxEnableRellay.addItemListener(new ItemListener(){
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				if(chckbxEnableRellay.isSelected()){
					chckbxShowMessageSource.setEnabled(true);
				} else 
					chckbxShowMessageSource.setEnabled(false);
			}
		});
		relayPanel.add(chckbxEnableRellay);
		MainGui.settings.add(chckbxEnableRellay);
		MainGui.settingsNames.add("activateRelay");
		
		chckbxShowMessageSource = new JCheckBox("Show Message Source?");
		chckbxShowMessageSource.setToolTipText("Weather or not the server the message is from should be shown (this is helpful if you are managing alot of servers)");
		chckbxShowMessageSource.setBounds(8, 54, 172, 24);
		relayPanel.add(chckbxShowMessageSource);
		MainGui.settings.add(chckbxShowMessageSource);
		MainGui.settingsNames.add("showSource");
		
		JPanel apiPanel = new JPanel();
		apiPanel.setBorder(new TitledBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null), "API Settings", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		apiPanel.setBounds(12, 12, 290, 49);
		add(apiPanel);
		apiPanel.setLayout(null);
		JCheckBox chckbxEnableCheckBox = new JCheckBox("Enable Follower Chat Notificatoins");
		chckbxEnableCheckBox.setToolTipText("Weather or not to show a message in chat some someone follows");
		chckbxEnableCheckBox.setBounds(6, 17, 270, 24);
		apiPanel.add(chckbxEnableCheckBox);
		MainGui.settings.add(chckbxEnableCheckBox);
		MainGui.settingsNames.add("enableFollowerNotifications");
		
		JPanel streamInfromationPanel = new JPanel();
		streamInfromationPanel.setBorder(new TitledBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null), "Change Stream Infromation", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		streamInfromationPanel.setBounds(12, 72, 290, 112);
		add(streamInfromationPanel);
		streamInfromationPanel.setLayout(null);
		
		JLabel lblStreamTitle = new JLabel("Stream Title:");
		lblStreamTitle.setBounds(12, 23, 86, 16);
		streamInfromationPanel.add(lblStreamTitle);
		
		streamTitle = new JTextField();
		streamTitle.setToolTipText("The title of your stream (what people will see in the game directory)(requires a Twitch Access Token)");
		streamTitle.setHorizontalAlignment(SwingConstants.LEFT);
		streamTitle.setBounds(103, 21, 175, 20);
		streamInfromationPanel.add(streamTitle);
		streamTitle.setColumns(10);
		
		JLabel lblStreamGame = new JLabel("Stream Game:");
		lblStreamGame.setBounds(12, 51, 86, 16);
		streamInfromationPanel.add(lblStreamGame);
		
		streamGame = new JTextField();
		streamGame.setToolTipText("The game that you are playing (requires a Twitch Access Token)");
		streamGame.setBounds(103, 49, 175, 20);
		streamInfromationPanel.add(streamGame);
		streamGame.setColumns(10);
		
		btnUpdate = new JButton("Update!");
		btnUpdate.setForeground(Color.WHITE);
		btnUpdate.setBackground(Color.BLACK);
		btnUpdate.setBounds(12, 78, 128, 24);
		btnUpdate.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				gui2TwitchAPI();
			}
		});
		streamInfromationPanel.add(btnUpdate);
		
		final Runnable apply = new Runnable(){
			@Override
			public void run() {
				/*
				 * Twitch Backend
				 */
				try {
					String title = Bennerbot.filterUTF8((""+((JSONObject) APIManager.parser.parse(Bennerbot.StreamToString(new URL("https://api.twitch.tv/kraken/channels/"+Bennerbot.configGetString("twitchChannel")).openStream()))).get("status")).replace(" ", " "));
					String game = Bennerbot.filterUTF8((""+((JSONObject)((JSONObject) APIManager.parser.parse(Bennerbot.StreamToString(new URL("https://api.twitch.tv/kraken/streams/"+Bennerbot.configGetString("twitchChannel")).openStream()))).get("stream")).get("game")).replace(" ", " "));
					
					if(!(streamTitle.getText().equalsIgnoreCase(lastTitle) || streamTitle.getText().equalsIgnoreCase(title)) && !streamTitle.isFocusOwner()){
						streamTitle.setText(title);
					}
					if(!(streamGame.getText().equalsIgnoreCase(lastGame) || streamGame.getText().equalsIgnoreCase(game)) && !streamGame.isFocusOwner()){
						streamGame.setText(game);
					}
				} catch (ParseException | IOException e) {
					e.printStackTrace();
				}
			}
			
		};
		
		Executors.newScheduledThreadPool(1).scheduleAtFixedRate(apply ,0, 30, TimeUnit.SECONDS);
		
		JButton refreshButton = new JButton("Refresh");
		refreshButton.setToolTipText("This will update the text fields with the most recent infromation avaliable");
		refreshButton.setForeground(Color.WHITE);
		refreshButton.setBackground(Color.BLACK);
		refreshButton.setBounds(151, 78, 128, 24);
		refreshButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				apply.run();
			}
		});
		streamInfromationPanel.add(refreshButton);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null), "Bot Settings", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(314, 110, 201, 49);
		add(panel);
		
		JComboBox<String> comboBox = new JComboBox<String>();
		comboBox.setModel(new DefaultComboBoxModel<String>(new String[] {"Update Bot's Theme", "Work in Progress"}));
		panel.add(comboBox);
	}
	public void gui2TwitchAPI(){
		new Thread(new Runnable(){
			@Override
			public void run() {
				boolean success = true;
				try{
					String url = "https://api.twitch.tv/kraken/channels/"+Bennerbot.conf.get("twitchChannel").toString().toLowerCase()+"?oauth_token="+Bennerbot.getAccessToken();
					URL obj = new URL(url);
					HttpURLConnection conn = (HttpURLConnection) obj.openConnection();

					conn.setRequestProperty("Accept", "application/vnd.twitchtv.v2+json");
					conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
					conn.setRequestMethod("PUT");
					conn.setDoOutput(true);
				
					String data = "channel[status]="+streamTitle.getText()+"&channel[game]="+streamGame.getText();
					System.out.println(data);
					
					lastGame = streamGame.getText();
					lastTitle = streamGame.getText();
		
					OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
					out.write(data);
					out.flush();
					
					for (Entry<String, List<String>> header : conn.getHeaderFields().entrySet())
						System.out.println(header.getKey() + "=" + header.getValue());
				} catch (Exception e){
					e.printStackTrace();
					success = false;
				}
				
				//TODO add hitbox updating
				
				if(success == true)
					JOptionPane.showMessageDialog(null, "Successfully updated your Stream's Title and/or Game!");
				else
					JOptionPane.showMessageDialog(null, "It apperes something has gone wrong, try again in a few seconds");
					
			}
		}).start();
	}
}
