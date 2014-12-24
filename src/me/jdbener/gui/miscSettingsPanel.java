package me.jdbener.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JButton;

import me.jdbener.Bennerbot;

import java.awt.Color;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class miscSettingsPanel extends JPanel {
	private static final long serialVersionUID = 3270562443564784442L;
	private JTextField txtTwitchAccessToken;
	private JCheckBox chckbxEnableRellay, chckbxShowMessageSource;
	private JTextField streamTitle;
	private JTextField streamGame;
	private JButton btnUpdate;

	/**
	 * Create the panel.
	 */
	public miscSettingsPanel() {
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
		apiPanel.setBounds(12, 12, 290, 106);
		add(apiPanel);
		apiPanel.setLayout(null);
		
		txtTwitchAccessToken = new JTextField();
		txtTwitchAccessToken.setToolTipText("<html><body>This is used to allow twitch API integeration.<br>"
				+ "this can be gotten here: https://api.twitch.tv/kraken/oauth2/authorize?response_type=token&client_id=9qxushp3sdeasixpxajz8pqlxdudfs6&redirect_uri=http://localhost&scope=channel_editor+channel_subscriptions<br>"+
				"when you authorize the bot it will redirect you to a black page, copy and past the access token from the url and paste it here<br>(an automated way of getting this is in the works)</body></html>");
		txtTwitchAccessToken.setBounds(12, 50, 266, 20);
		apiPanel.add(txtTwitchAccessToken);
		txtTwitchAccessToken.setColumns(10);
		txtTwitchAccessToken.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(txtTwitchAccessToken.getText().equalsIgnoreCase("")){
					streamTitle.setEnabled(false);
					streamGame.setEnabled(false);
					btnUpdate.setEnabled(false);
				} else {
					streamTitle.setEnabled(true);
					streamGame.setEnabled(true);
					btnUpdate.setEnabled(true);
				}
			}
		});
		txtTwitchAccessToken.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				changed();
			}
			public void removeUpdate(DocumentEvent e) {
				changed();
			}
			public void insertUpdate(DocumentEvent e) {
				changed();
			}
   		    public void changed() {
   		    	if (txtTwitchAccessToken.getText().equals("")){
   		    		streamTitle.setEnabled(false);
   		    		streamGame.setEnabled(false);
   		    		btnUpdate.setEnabled(false);
   		    	} else {
   		    		streamTitle.setEnabled(true);
   		    		streamGame.setEnabled(true);
   		    		btnUpdate.setEnabled(true);
   		    	}
			}
		});
		MainGui.settings.add(txtTwitchAccessToken);
		MainGui.settingsNames.add("twitchAccessToken");
		
		JLabel lblTwitchAccessToken = new JLabel("Twitch Access Token:");
		lblTwitchAccessToken.setHorizontalAlignment(SwingConstants.CENTER);
		lblTwitchAccessToken.setBounds(12, 26, 266, 16);
		apiPanel.add(lblTwitchAccessToken);
		
		JCheckBox chckbxEnableCheckBox = new JCheckBox("Enable Follower Chat Notificatoins");
		chckbxEnableCheckBox.setToolTipText("Weather or not to show a message in chat some someone follows");
		chckbxEnableCheckBox.setBounds(8, 77, 270, 24);
		apiPanel.add(chckbxEnableCheckBox);
		MainGui.settings.add(chckbxEnableCheckBox);
		
		JPanel streamInfromationPanel = new JPanel();
		streamInfromationPanel.setBorder(new TitledBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null), "Change Stream Infromation", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		streamInfromationPanel.setBounds(12, 130, 290, 112);
		add(streamInfromationPanel);
		streamInfromationPanel.setLayout(null);
		
		JLabel lblStreamTitle = new JLabel("Stream Title:");
		lblStreamTitle.setBounds(12, 23, 86, 16);
		streamInfromationPanel.add(lblStreamTitle);
		
		streamTitle = new JTextField();
		streamTitle.setEnabled(false);
		streamTitle.setToolTipText("The title of your stream (what people will see in the game directory)(requires a Twitch Access Token)");
		streamTitle.setHorizontalAlignment(SwingConstants.LEFT);
		streamTitle.setBounds(103, 21, 175, 20);
		streamInfromationPanel.add(streamTitle);
		streamTitle.setColumns(10);
		
		JLabel lblStreamGame = new JLabel("Stream Game:");
		lblStreamGame.setBounds(12, 51, 86, 16);
		streamInfromationPanel.add(lblStreamGame);
		
		streamGame = new JTextField();
		streamGame.setEnabled(false);
		streamGame.setToolTipText("The game that you are playing (requires a Twitch Access Token)");
		streamGame.setBounds(103, 49, 175, 20);
		streamInfromationPanel.add(streamGame);
		streamGame.setColumns(10);
		
		btnUpdate = new JButton("Update!");
		btnUpdate.setEnabled(false);
		btnUpdate.setForeground(Color.WHITE);
		btnUpdate.setBackground(Color.BLACK);
		btnUpdate.setBounds(12, 78, 266, 24);
		btnUpdate.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
			}
		});
		streamInfromationPanel.add(btnUpdate);
		MainGui.settingsNames.add("enableFollowerNotifications");
	}
	public void gui2API(){
		new Thread(new Runnable(){
			@Override
			public void run() {
				try{
					String url = "https://api.twitch.tv/kraken/channels/"+Bennerbot.conf.get("twitchChannel").toString().toLowerCase()+"?oauth_token="+Bennerbot.configGetString("twitchAccessToken");
					URL obj = new URL(url);
					HttpURLConnection conn = (HttpURLConnection) obj.openConnection();

					conn.setRequestProperty("Accept", "application/vnd.twitchtv.v2+json");
					conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
					conn.setRequestMethod("PUT");
					conn.setDoOutput(true);
				
					String data = "channel[game]="+streamGame.getText();
		
					OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
					out.write(data);
					out.flush();
				} catch (Exception e){
					e.printStackTrace();
				}
			}
		}).start();
	}
}
