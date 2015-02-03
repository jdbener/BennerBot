package me.jdbener.gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

import me.jdbener.Bennerbot;
import me.jdbener.apis.APIManager;

import org.ho.yaml.Yaml;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

@SuppressWarnings("serial")
public class GeneralConfigurationPanel extends JPanel {
	private JLabel hitboxStatusLabel,
	hitboxViewerLabel,
	hitboxFollowerLabel,
	hitboxSubscriberLabel,
	hitboxTitleLabel,
	hitboxGameLabel,
		
	//Twitch Metrics
	twitchStatusLabel,
	twitchViewerLabel,
	twitchFollowerLabel,
	twitchSubscriber,
	twitchTitleLabel,
	twitchGameLabel;
	
	private String lastGame = "blank", lastTitle = "blank";
	
	private JButton botIDUpdate;
	
	//random variables
	private JTextField twitchUsername;
	private JPasswordField twitchOAuth;
	private JTextField twitchChannel;
	private JTextField hitboxUsername;
	private JPasswordField hitboxPassword;
	private JTextField hitboxChannel;
	private JTextField txtBotName;
	private JCheckBox enableTwitch;
	private JCheckBox enableHitbox;
	private JCheckBox enableLastfmIntegration;
	private JLabel botIDNumLabel;
	private JTextField lfmUsername;
	private JTextField lastfmMSGFormat;
	private JTextField lastfmCommandName;
	private JTextField botID;
	private JTextField streamTitle;
	private JTextField streamGame;
	/**
	 * Create the panel.
	 */
	public GeneralConfigurationPanel() {
		//JPanel generalConfigPanel = new JPanel();
		setLocation(-250, -162);
		//tabbedPane.addTab("<html><body><center><table width='100'>General Configuration</table></body></html>", null, generalConfigPanel, "General settings, Connections, Databases, ETC...");
		//tabbedPane.setForegroundAt(0, Color.GRAY);
		//tabbedPane.setBackgroundAt(0, Color.BLACK);
		//tabbedPane.setEnabledAt(0, true);
		setLayout(null);
		setBorder(null);
		
		//Bennerbot.logger.info("\tLoading Central Tutorial");
		/*
		 * Central Tutorial
		 */
		JLabel generalConfigurationTutorial = new JLabel("<html><body><center>Bennerbot has been designed differently than other chatbots, it has been designed to manage both twitch and hitbox simultaneously. Use this tab to setup basic connection infromation, as well as to view important infromatin about your channel.</center></body></html>");
		generalConfigurationTutorial.setHorizontalAlignment(SwingConstants.CENTER);
		generalConfigurationTutorial.setBounds(215, 149, 368, 176);
		add(generalConfigurationTutorial);
		JLabel twitchSettingsLabel = new JLabel("Twitch Settings");
		twitchSettingsLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
		twitchSettingsLabel.setBounds(10, 13, 275, 14);
		add(twitchSettingsLabel);
		
		//Bennerbot.logger.info("\tLoading Twitch Settings Panel");
		/*
		 * Twitch Settings Sub-Panel
		 */
		JPanel Twitch = new JPanel();
		Twitch.setBounds(10, 28, 275, 109);
		add(Twitch);
		Twitch.setLayout(null);
		Twitch.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		
		enableTwitch = new JCheckBox("Connect to Twitch?");
		enableTwitch.setHorizontalAlignment(SwingConstants.CENTER);
		enableTwitch.setToolTipText("Check this if you want to connect to a twitch account");
		enableTwitch.setBounds(10, 7, 255, 23);
		enableTwitch.addItemListener(new ItemListener(){
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				if(!enableTwitch.isSelected()){
					twitchUsername.setEnabled(false);
					twitchOAuth.setEnabled(false);
					twitchChannel.setEnabled(false);
				} else {
					twitchUsername.setEnabled(true);
					twitchOAuth.setEnabled(true);
					twitchChannel.setEnabled(true);
				}
			}
		});
		Twitch.add(enableTwitch);
		MainGui.addComponent(enableTwitch, "connectToTwitch", true);
		
		twitchUsername = new JTextField();
		twitchUsername.setEnabled(false);
		twitchUsername.setToolTipText("The username of the the twitch account you want the bot to use");
		twitchUsername.setBounds(72, 30, 193, 20);
		Twitch.add(twitchUsername);
		twitchUsername.setColumns(10);
		MainGui.addComponent(twitchUsername, "twitchUsername", true);
		
		twitchOAuth = new JPasswordField();
		twitchOAuth.setEnabled(false);
		twitchOAuth.setToolTipText("Enter the OAuth password (for lack of a better word) for your twitch account. This can be found here: http://www.twitchapps.com/tmi/");
		twitchOAuth.setBounds(72, 54, 193, 20);
		Twitch.add(twitchOAuth);
		twitchOAuth.setColumns(10);
		MainGui.addComponent(twitchOAuth, "twitchAccessToken", true);
		
		twitchChannel = new JTextField();
		twitchChannel.setEnabled(false);
		twitchChannel.setToolTipText("The Twitch channel to moderate");
		twitchChannel.setText("BennerBot");
		twitchChannel.setBounds(72, 78, 193, 20);
		Twitch.add(twitchChannel);
		twitchChannel.setColumns(10);
		MainGui.addComponent(twitchChannel, "twitchChannel", true);
		
		JLabel twitchUsernameL = new JLabel("Username:");
		twitchUsernameL.setHorizontalAlignment(SwingConstants.LEFT);
		twitchUsernameL.setBounds(7, 33, 62, 14);
		Twitch.add(twitchUsernameL);
		
		JLabel twitchPasswordL = new JLabel("OAuth:");
		twitchPasswordL.setHorizontalAlignment(SwingConstants.LEFT);
		twitchPasswordL.setBounds(7, 57, 46, 14);
		twitchPasswordL.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		twitchPasswordL.setToolTipText("Click this to generate a new Oauth Token, you need to be signed into the account above for this to work");
		twitchPasswordL.setForeground(Color.red);
		twitchPasswordL.addMouseListener(new MouseAdapter()   {   
	        public void mouseClicked(MouseEvent e){   
	        	twitchOAuth.setText(Bennerbot.getAccessToken().replace("oauth:", ""));
	        	JOptionPane.showMessageDialog(null, "Successfully updated your Access/OAuth Token");
	        }   
		});
		Twitch.add(twitchPasswordL);
		
		JLabel twitchChannelL = new JLabel("Channel:");
		twitchChannelL.setHorizontalAlignment(SwingConstants.LEFT);
		twitchChannelL.setBounds(7, 81, 56, 14);
		Twitch.add(twitchChannelL);
		
		//Bennerbot.logger.info("\tLoading Hitbox Settings Panel");
		/*
		 * Hitbox Settings Sub-Label
		 */
		JLabel hitboxSettingsLabel = new JLabel("Hitbox Settings");
		hitboxSettingsLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
		hitboxSettingsLabel.setBounds(513, 13, 275, 14);
		add(hitboxSettingsLabel);
		
		JPanel Hitbox = new JPanel();
		Hitbox.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		Hitbox.setLayout(null);
		Hitbox.setBounds(513, 27, 275, 110);
		add(Hitbox);
		
		enableHitbox = new JCheckBox("Connect to Hitbox?");
		enableHitbox.setToolTipText("Check this if you want to connect to a hitbox account");
		enableHitbox.setHorizontalAlignment(SwingConstants.CENTER);
		enableHitbox.setBounds(10, 7, 255, 23);
		enableHitbox.addItemListener(new ItemListener(){
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(!enableHitbox.isSelected()){
					hitboxUsername.setEnabled(false);
					hitboxPassword.setEnabled(false);
					hitboxChannel.setEnabled(false);
				} else {
					hitboxUsername.setEnabled(true);
					hitboxPassword.setEnabled(true);
					hitboxChannel.setEnabled(true);
				}
			}
		});
		Hitbox.add(enableHitbox);
		MainGui.addComponent(enableHitbox, "connectToHitbox", true);
		
		hitboxUsername = new JTextField();
		hitboxUsername.setEnabled(false);
		hitboxUsername.setToolTipText("The username of the the hitbox account you want the bot to use");
		hitboxUsername.setColumns(10);
		hitboxUsername.setBounds(72, 30, 193, 20);
		Hitbox.add(hitboxUsername);
		MainGui.addComponent(hitboxUsername, "hitboxUsername", true);
		
		hitboxPassword = new JPasswordField();
		hitboxPassword.setEnabled(false);
		hitboxPassword.setToolTipText("Enter the password for your hitbox account. Dont worry this wont be scene by anybody but yourself.");
		hitboxPassword.setColumns(10);
		hitboxPassword.setBounds(72, 54, 193, 20);
		Hitbox.add(hitboxPassword);
		MainGui.addComponent(hitboxPassword, "hitboxPassword", true);
		
		hitboxChannel = new JTextField();
		hitboxChannel.setEnabled(false);
		hitboxChannel.setToolTipText("The name of the hitbox account to moderate");
		hitboxChannel.setText("BennerBot");
		hitboxChannel.setColumns(10);
		hitboxChannel.setBounds(72, 78, 193, 20);
		Hitbox.add(hitboxChannel);
		MainGui.addComponent(hitboxChannel, "hitboxChannel", true);
		
		JLabel hitboxUsernameL = new JLabel("Username:");
		hitboxUsernameL.setHorizontalAlignment(SwingConstants.LEFT);
		hitboxUsernameL.setBounds(7, 33, 62, 14);
		Hitbox.add(hitboxUsernameL);
		
		JLabel hitboxPasswordL = new JLabel("Password:");
		hitboxPasswordL.setHorizontalAlignment(SwingConstants.LEFT);
		hitboxPasswordL.setBounds(7, 57, 62, 14);
		Hitbox.add(hitboxPasswordL);
		
		JLabel hitboxChannelL = new JLabel("Channel:");
		hitboxChannelL.setHorizontalAlignment(SwingConstants.LEFT);
		hitboxChannelL.setBounds(7, 81, 59, 14);
		Hitbox.add(hitboxChannelL);
		
		//Bennerbot.logger.info("\tLoading General Settings Panel");
		/*
		 * General Settings Sub-Panel
		 */
		JLabel generalLabel = new JLabel("General Settings");
		generalLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
		generalLabel.setBounds(293, 12, 208, 14);
		add(generalLabel);
		
		JPanel General = new JPanel();
		General.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		General.setBounds(293, 28, 208, 125);
		add(General);
		General.setLayout(null);
		
		JLabel lblBotName = new JLabel("Bot name:");
		lblBotName.setBounds(5, 10, 62, 14);
		lblBotName.setHorizontalAlignment(SwingConstants.LEFT);
		General.add(lblBotName);
		
		txtBotName = new JTextField();
		txtBotName.setBounds(65, 7, 138, 20);
		txtBotName.setToolTipText("The aesthetic way that the bot's name is to be displayed.");
		txtBotName.setText("BennerBot");
		txtBotName.setColumns(10);
		General.add(txtBotName);
		MainGui.addComponent(txtBotName, "botName");
		
		JCheckBox respondOperatorCommands = new JCheckBox("Respond to your Commands?");
		respondOperatorCommands.setHorizontalAlignment(SwingConstants.CENTER);
		respondOperatorCommands.setToolTipText("Weather or not commands sent through this GUI should be phrased and responded to");
		respondOperatorCommands.setSelected(true);
		respondOperatorCommands.setVerticalAlignment(SwingConstants.TOP);
		respondOperatorCommands.setBounds(0, 100, 210, 23);
		General.add(respondOperatorCommands);
		MainGui.addComponent(respondOperatorCommands, "respondOperatorCommands");
		
		/*
		 * Stream Title/Game section
		 */
		JLabel lblStreamTitle = new JLabel("Stream Title:");
		lblStreamTitle.setBounds(5, 32, 79, 16);
		General.add(lblStreamTitle);
		
		JLabel lblStreamGame = new JLabel("Stream Game:");
		lblStreamGame.setBounds(5, 54, 89, 16);
		General.add(lblStreamGame);
		
		streamTitle = new JTextField();
		streamTitle.setBounds(77, 30, 126, 20);
		General.add(streamTitle);
		streamTitle.setColumns(10);
		
		streamGame = new JTextField();
		streamGame.setBounds(87, 52, 116, 20);
		General.add(streamGame);
		streamGame.setColumns(10);
		
		JButton btnUpdate = new JButton("Update");
		btnUpdate.setToolTipText("This button will update the stream title/game");
		btnUpdate.setForeground(Color.WHITE);
		btnUpdate.setBackground(Color.BLACK);
		btnUpdate.setBounds(10, 74, 92, 24);
		btnUpdate.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				gui2API();
			}
		});
		General.add(btnUpdate);
		
		final Runnable refresh = new Runnable(){
			@Override
			public void run(){
				/*
				 * Twitch backend
				 */
				if(Bennerbot.configBoolean("connectToTwitch"))
					try {
						String title = Bennerbot.filterUTF8((""+((JSONObject) APIManager.parser.parse(Bennerbot.StreamToString(new URL("https://api.twitch.tv/kraken/channels/"+Bennerbot.configGetString("twitchChannel")).openStream()))).get("status")).replace(" ", " "));
						String game = Bennerbot.filterUTF8((""+((JSONObject) APIManager.parser.parse(Bennerbot.StreamToString(new URL("https://api.twitch.tv/kraken/channels/"+Bennerbot.configGetString("twitchChannel")).openStream()))).get("game")).replace(" ", " "));
						
						if(!(streamTitle.getText().equalsIgnoreCase(lastTitle) || streamTitle.getText().equalsIgnoreCase(title)) && !streamTitle.isFocusOwner()){
							streamTitle.setText(title);
							streamTitle.setToolTipText(title);
						}
						if(!(streamGame.getText().equalsIgnoreCase(lastGame) || streamGame.getText().equalsIgnoreCase(game)) && !streamGame.isFocusOwner()){
							streamGame.setText(game);
							streamGame.setToolTipText(game);
						}
					} catch (ParseException | IOException e) {
						e.printStackTrace();
					}
				else if(Bennerbot.configBoolean("connectToHitbox"))
					try{
						JSONArray channels = (JSONArray) ((JSONObject) APIManager.parser.parse(Bennerbot.StreamToString(new URL("http://www.hitbox.tv/api/media/live/"+Bennerbot.configGetString("hitboxChannel")+"/list").openStream()))).get("livestream");
						JSONObject channel = new JSONObject();
						for(int i = 0; i < channels.size(); i++){
							if(((JSONObject)channels.get(i)).get("media_file").toString().equalsIgnoreCase(Bennerbot.configGetString("hitboxChannel"))){
								channel = (JSONObject)channels.get(i);
							}
						}
						String title = Bennerbot.filterUTF8((channel.get("media_status").toString()));
						String game = Bennerbot.filterUTF8((channel.get("category_name").toString()));
						
						if(!(streamTitle.getText().equalsIgnoreCase(lastTitle) || streamTitle.getText().equalsIgnoreCase(title)) && !streamTitle.isFocusOwner()){
							streamTitle.setText(title);
							streamTitle.setToolTipText(title);
						}
						if(!(streamGame.getText().equalsIgnoreCase(lastGame) || streamGame.getText().equalsIgnoreCase(game)) && !streamGame.isFocusOwner()){
							streamGame.setText(game);
							streamGame.setToolTipText(game);
						}
					} catch (ParseException | IOException e) {
						e.printStackTrace();
					}
				else {
					streamTitle.setText("No Connections");
					streamGame.setText("No Connections");
				}
			}
		};
		
		JButton btnRefresh = new JButton("Refresh");
		btnRefresh.setToolTipText("This Button will refresh the stream title/game with what the servers are displaying");
		btnRefresh.setForeground(Color.WHITE);
		btnRefresh.setBackground(Color.BLACK);
		btnRefresh.setBounds(101, 74, 97, 24);
		btnRefresh.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				new Thread(refresh).start();
			}
		});
		General.add(btnRefresh);
		Executors.newScheduledThreadPool(1).scheduleAtFixedRate(refresh, 0, 10, TimeUnit.SECONDS);
		
		//Bennerbot.logger.info("\tLoading Hitbox Metrics");
		/*
		 * Hitbox Metric Sub-Panel
		 */
		JLabel HitboxmetricsLabel = new JLabel("Hitbox Channel Metrics");
		HitboxmetricsLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
		HitboxmetricsLabel.setBounds(601, 149, 187, 14);
		add(HitboxmetricsLabel);
		
		JPanel HitboxStatusPanel = new JPanel();
		HitboxStatusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		HitboxStatusPanel.setBounds(601, 168, 187, 157);
		add(HitboxStatusPanel);
		HitboxStatusPanel.setLayout(null);
		
		hitboxStatusLabel = new JLabel("<html><body>Status:&nbsp;<span style='color:yellow'>Connection</span></body></html>");
		hitboxStatusLabel.setToolTipText("This displays infromation about your channel");
		hitboxStatusLabel.setFont(new Font("Trajan Pro", Font.PLAIN, 11));
		hitboxStatusLabel.setBounds(10, 5, 172, 20);
		HitboxStatusPanel.add(hitboxStatusLabel);
		
		hitboxViewerLabel = new JLabel("<html><body>Viewers:&nbsp;<span style='color:yellow'>Connection</span></body></html>");
		hitboxViewerLabel.setFont(new Font("Trajan Pro", Font.PLAIN, 11));
		hitboxViewerLabel.setBounds(10, 30, 172, 20);
		HitboxStatusPanel.add(hitboxViewerLabel);
		
		hitboxFollowerLabel = new JLabel("<html><body>Followers:&nbsp;<span style='color:yellow'>Connection</span></body></html>");
		hitboxFollowerLabel.setFont(new Font("Trajan Pro", Font.PLAIN, 11));
		hitboxFollowerLabel.setBounds(10, 55, 172, 20);
		HitboxStatusPanel.add(hitboxFollowerLabel);
		
		hitboxSubscriberLabel = new JLabel("<html><body>Subscribers:&nbsp;<span style='color:yellow'>Connection</span></body></html>");
		hitboxSubscriberLabel.setFont(new Font("Trajan Pro", Font.PLAIN, 11));
		hitboxSubscriberLabel.setBounds(10, 83, 172, 20);
		HitboxStatusPanel.add(hitboxSubscriberLabel);
		
		hitboxTitleLabel = new JLabel("<html><body>Title: <span style='color:yellow'>Connection</span></body></html>");
		hitboxTitleLabel.setVerticalAlignment(SwingConstants.TOP);
		hitboxTitleLabel.setFont(new Font("Trajan Pro", Font.PLAIN, 11));
		hitboxTitleLabel.setBounds(10, 111, 172, 20);
		HitboxStatusPanel.add(hitboxTitleLabel);
		
		hitboxGameLabel = new JLabel("<html><body>Game: <span style='color:yellow'>Connection</span></body></html>");
		hitboxGameLabel.setVerticalAlignment(SwingConstants.TOP);
		hitboxGameLabel.setFont(new Font("Trajan Pro", Font.PLAIN, 11));
		hitboxGameLabel.setBounds(10, 137, 172, 20);
		HitboxStatusPanel.add(hitboxGameLabel);
		
		//Bennerbot.logger.info("\tLoading Twitch Metrics");
		/*
		 * Twitch Metrics Sub-Panel
		 */
		//TODO twitch metrics
		JLabel TwitchMetricsLabel = new JLabel("Twitch Channel Metrics");
		TwitchMetricsLabel.setHorizontalAlignment(SwingConstants.TRAILING);
		TwitchMetricsLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
		TwitchMetricsLabel.setBounds(10, 149, 187, 14);
		add(TwitchMetricsLabel);
		
		JPanel TwitchStatusPanel = new JPanel();
		TwitchStatusPanel.setLayout(null);
		TwitchStatusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		TwitchStatusPanel.setBounds(10, 168, 187, 157);
		add(TwitchStatusPanel);
		
		twitchStatusLabel = new JLabel("<html><body>Status:&nbsp;<span style='color:yellow'>Connection</span></body></html>");
		twitchStatusLabel.setHorizontalAlignment(SwingConstants.TRAILING);
		twitchStatusLabel.setToolTipText("This displays infromation about your channel");
		twitchStatusLabel.setFont(new Font("Trajan Pro", Font.PLAIN, 11));
		twitchStatusLabel.setBounds(10, 5, 172, 20);
		TwitchStatusPanel.add(twitchStatusLabel);
		
		twitchViewerLabel = new JLabel("<html><body>Viewers:&nbsp;<span style='color:yellow'>Connection</span></body></html>");
		twitchViewerLabel.setHorizontalAlignment(SwingConstants.TRAILING);
		twitchViewerLabel.setFont(new Font("Trajan Pro", Font.PLAIN, 11));
		twitchViewerLabel.setBounds(10, 30, 172, 20);
		TwitchStatusPanel.add(twitchViewerLabel);
		
		twitchFollowerLabel = new JLabel("<html><body>Followers:&nbsp;<span style='color:yellow'>Connection</span></body></html>");
		twitchFollowerLabel.setHorizontalAlignment(SwingConstants.TRAILING);
		twitchFollowerLabel.setFont(new Font("Trajan Pro", Font.PLAIN, 11));
		twitchFollowerLabel.setBounds(10, 55, 172, 20);
		TwitchStatusPanel.add(twitchFollowerLabel);
		
		twitchSubscriber = new JLabel("<html><body>Subscribers:&nbsp;<span style='color:yellow'>Connection</span></body></html>");
		twitchSubscriber.setHorizontalAlignment(SwingConstants.TRAILING);
		twitchSubscriber.setFont(new Font("Trajan Pro", Font.PLAIN, 11));
		twitchSubscriber.setBounds(10, 83, 172, 20);
		TwitchStatusPanel.add(twitchSubscriber);
		
		twitchTitleLabel = new JLabel("<html><body>Title: <span style='color:yellow'>Connection</span></body></html>");
		twitchTitleLabel.setVerticalAlignment(SwingConstants.TOP);
		twitchTitleLabel.setHorizontalAlignment(SwingConstants.TRAILING);
		twitchTitleLabel.setFont(new Font("Trajan Pro", Font.PLAIN, 11));
		twitchTitleLabel.setBounds(10, 111, 172, 20);
		TwitchStatusPanel.add(twitchTitleLabel);
		
		twitchGameLabel = new JLabel("<html><body>Game: <span style='color:yellow'>Connection</span></body></html>");
		twitchGameLabel.setVerticalAlignment(SwingConstants.TOP);
		twitchGameLabel.setHorizontalAlignment(SwingConstants.TRAILING);
		twitchGameLabel.setFont(new Font("Trajan Pro", Font.PLAIN, 11));
		twitchGameLabel.setBounds(10, 137, 172, 20);
		TwitchStatusPanel.add(twitchGameLabel);
		
		//Bennerbot.logger.info("\tLoading Metrcis Backend");
		/*
		 * Backend for the metrics code
		 */
		//TODO metrics backend
		Executors.newScheduledThreadPool(22).scheduleAtFixedRate(new Runnable(){
			@Override
			public void run() {
				if(Bennerbot.configBoolean("connectToTwitch")){
					try{
						if(((JSONObject) APIManager.parser.parse(Bennerbot.StreamToString(new URL("https://api.twitch.tv/kraken/streams/"+Bennerbot.configGetString("twitchChannel")).openStream()))).get("stream") != null)
							twitchStatusLabel.setText("<html><body>Status: <span style='color:green'>Online</span></body></html>");
						else
							twitchStatusLabel.setText("<html><body>Status: <span style='color:red'>Offline</span></body></html>");
					} catch (Exception e){twitchStatusLabel.setText("<html><body>Status: <span style='color:red'>Unknown</span></body></html>");}
					try{
						twitchViewerLabel.setText("Viewers: "+Integer.parseInt(""+((JSONObject)((JSONObject) APIManager.parser.parse(Bennerbot.StreamToString(new URL("https://api.twitch.tv/kraken/streams/"+Bennerbot.configGetString("twitchChannel")).openStream()))).get("stream")).get("viewers")));
					} catch (Exception e){twitchViewerLabel.setText("<html><body>Viewers: <span style='color:red'>Offline</span></body></html>");}
					try{
						twitchFollowerLabel.setText("Folowers: "+Integer.parseInt(""+((JSONObject) APIManager.parser.parse(Bennerbot.StreamToString(new URL("https://api.twitch.tv/kraken/channels/"+Bennerbot.configGetString("twitchChannel")+"/follows/?limit=1").openStream()))).get("_total")));
					} catch (Exception e){twitchFollowerLabel.setText("Folowers: 0");}
					try{
						twitchSubscriber.setText("Subscribers: "+Integer.parseInt(""+((JSONObject) APIManager.parser.parse(Bennerbot.StreamToString(new URL("https://api.twitch.tv/kraken/channels/"+Bennerbot.configGetString("twitchChannel")+"/subscriptions?oauth_token="+Bennerbot.getAccessToken()).openStream()))).get("_total")));
					} catch (Exception e){twitchSubscriber.setText("Subscribers: 0");}
					try{
						String title = Bennerbot.filterUTF8(("Title: "+((JSONObject) APIManager.parser.parse(Bennerbot.StreamToString(new URL("https://api.twitch.tv/kraken/channels/"+Bennerbot.configGetString("twitchChannel")).openStream()))).get("status")).replace(" ", " "));
						twitchTitleLabel.setText(title);
						twitchTitleLabel.setToolTipText(title);
					} catch (Exception e){twitchTitleLabel.setText("<html><body>Title: <span style='color:red'>Offline</span></body></html>");twitchTitleLabel.setToolTipText("");}
					try{
						String game = Bennerbot.filterUTF8((""+((JSONObject) APIManager.parser.parse(Bennerbot.StreamToString(new URL("https://api.twitch.tv/kraken/channels/"+Bennerbot.configGetString("twitchChannel")).openStream()))).get("game")).replace(" ", " "));
						twitchGameLabel.setText(game);
						twitchGameLabel.setToolTipText(game);
					} catch (Exception e){twitchGameLabel.setText("<html><body>Game: <span style='color:red'>Offline</span></body></html>");twitchGameLabel.setToolTipText("");}
				} else {
					twitchStatusLabel.setText("<html><body>Status:&nbsp;<span style='color:yellow'>Connection</span></body></html>");
					twitchStatusLabel.setToolTipText("");
					twitchViewerLabel.setText("<html><body>Viewers:&nbsp;<span style='color:yellow'>Connection</span></body></html>");
					twitchViewerLabel.setToolTipText("");
					twitchFollowerLabel.setText("<html><body>Followers:&nbsp;<span style='color:yellow'>Connection</span></body></html>");
					twitchFollowerLabel.setToolTipText("");
					twitchSubscriber.setText("<html><body>Subscribers:&nbsp;<span style='color:yellow'>Connection</span></body></html>");
					twitchSubscriber.setToolTipText("");
					twitchTitleLabel.setText("<html><body>Title:&nbsp;<span style='color:yellow'>Connection</span></body></html>");
					twitchTitleLabel.setToolTipText("");
					twitchGameLabel.setText("<html><body>Game:&nbsp;<span style='color:yellow'>Connection</span></body></html>");
					twitchGameLabel.setToolTipText("");
				}
				if(Bennerbot.configBoolean("connectToHitbox")){
					try{
						JSONArray channels = (JSONArray) ((JSONObject) APIManager.parser.parse(Bennerbot.StreamToString(new URL("http://www.hitbox.tv/api/media/live/"+Bennerbot.configGetString("hitboxChannel")+"/list").openStream()))).get("livestream");
						JSONObject channel = new JSONObject();
						for(int i = 0; i < channels.size(); i++){
							if(((JSONObject)channels.get(i)).get("media_file").toString().equalsIgnoreCase(Bennerbot.configGetString("hitboxChannel"))){
								channel = (JSONObject)channels.get(i);
							}
						}
						try{
							if(channel.get("media_is_live").toString().equalsIgnoreCase("1"))
								hitboxStatusLabel.setText("<html><body>Status: <span style='color:green'>Online</span></body></html>");
							else
								hitboxStatusLabel.setText("<html><body>Status: <span style='color:red'>Offline</span></body></html>");
						} catch (Exception e){hitboxStatusLabel.setText("<html><body>Status: <span style='color:red'>Unknown</span></body></html>");}
						try{
							hitboxViewerLabel.setText("Viewers: "+Integer.parseInt(channel.get("media_views").toString()));
						} catch (Exception e){hitboxViewerLabel.setText("<html><body>Viewers: <span style='color:red'>Offline</span></body></html>");}
						try{
							hitboxFollowerLabel.setText("Folowers: "+Integer.parseInt(((JSONObject)channel.get("channel")).get("followers").toString()));
						} catch (Exception e){hitboxFollowerLabel.setText("Folowers: 0");}
						try{
							hitboxSubscriberLabel.setText("Subscribers: "+0);
						} catch (Exception e){hitboxSubscriberLabel.setText("Subscribers: 0");}
						try{
							String title = Bennerbot.filterUTF8(("Title: "+channel.get("media_status").toString()));
							hitboxTitleLabel.setText(title);
							hitboxTitleLabel.setToolTipText(title);
						} catch (Exception e){hitboxTitleLabel.setText("<html><body>Title: <span style='color:red'>Offline</span></body></html>");twitchTitleLabel.setToolTipText("");}
						try{
							String game = Bennerbot.filterUTF8(("Game: "+channel.get("category_name").toString()));
							hitboxGameLabel.setText(game);
							hitboxGameLabel.setToolTipText(game);
						} catch (Exception e){hitboxGameLabel.setText("<html><body>Game: <span style='color:red'>Offline</span></body></html>");twitchGameLabel.setToolTipText("");}
					} catch (Exception e){
						//e.printStackTrace();
					}
				} else {
					hitboxStatusLabel.setText("<html><body>Status:&nbsp;<span style='color:yellow'>Connection</span></body></html>");
					hitboxStatusLabel.setToolTipText("");
					hitboxViewerLabel.setText("<html><body>Viewers:&nbsp;<span style='color:yellow'>Connection</span></body></html>");
					hitboxViewerLabel.setToolTipText("");
					hitboxFollowerLabel.setText("<html><body>Followers:&nbsp;<span style='color:yellow'>Connection</span></body></html>");
					hitboxFollowerLabel.setToolTipText("");
					hitboxSubscriberLabel.setText("<html><body>Subscribers:&nbsp;<span style='color:yellow'>Connection</span></body></html>");
					hitboxSubscriberLabel.setToolTipText("");
					hitboxTitleLabel.setText("<html><body>Title:&nbsp;<span style='color:yellow'>Connection</span></body></html>");
					hitboxTitleLabel.setToolTipText("");
					hitboxGameLabel.setText("<html><body>Game:&nbsp;<span style='color:yellow'>Connection</span></body></html>");
					hitboxGameLabel.setToolTipText("");
				}
			}
		}, 0, 1, TimeUnit.SECONDS);	
		
		//Bennerbot.logger.info("\tLoading Last.fm Settings Panel");
		/*
		 * Last.fm Sub-Panel
		 */
		JPanel LastFMConfigPanel = new JPanel();
		LastFMConfigPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		LastFMConfigPanel.setBounds(10, 357, 600, 116);
		add(LastFMConfigPanel);
		LastFMConfigPanel.setLayout(null);
		
		enableLastfmIntegration = new JCheckBox("Enable Last.fm Integration");
		enableLastfmIntegration.setToolTipText("Wethor or not to get currently playing song infromation from last.fm");
		enableLastfmIntegration.setHorizontalAlignment(SwingConstants.CENTER);
		enableLastfmIntegration.setBounds(8, 5, 748, 24);
		enableLastfmIntegration.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent e) {
				if(!enableLastfmIntegration.isSelected()){
					lfmUsername.setEnabled(false);
					lastfmMSGFormat.setEnabled(false);
					lastfmCommandName.setEnabled(false);
				} else {
					lfmUsername.setEnabled(true);
					lastfmMSGFormat.setEnabled(true);
					lastfmCommandName.setEnabled(true);
				}
			}
		});
		LastFMConfigPanel.add(enableLastfmIntegration);
		MainGui.addComponent(enableLastfmIntegration, "enableLastfmIntegration");
		
		JLabel lfmUsernameLabel = new JLabel("Last.fm Account Name:");
		lfmUsernameLabel.setBounds(8, 35, 149, 16);
		LastFMConfigPanel.add(lfmUsernameLabel);
		
		lfmUsername = new JTextField();
		lfmUsername.setEnabled(false);
		lfmUsername.setToolTipText("The last.fm account to monitor");
		lfmUsername.setBounds(165, 35, 422, 20);
		LastFMConfigPanel.add(lfmUsername);
		lfmUsername.setColumns(10);
		MainGui.addComponent(lfmUsername, "lastfmName");
		
		JLabel lblLastfmMessageFromat = new JLabel("Last.fm Message Fromat:");
		lblLastfmMessageFromat.setBounds(8, 65, 149, 16);
		LastFMConfigPanel.add(lblLastfmMessageFromat);
		
		lastfmMSGFormat = new JTextField();
		lastfmMSGFormat.setEnabled(false);
		lastfmMSGFormat.setToolTipText("<html><body>the format for how messages are responded to"
				+ "<br>&lt;user&gt;: the user who sent the command"
				+ "<br>&lt;title&gt;: the title of the song thats currently playing"+
				"<br>&lt;artist&gt;: the artist who performs said song"+
				"<br>&lt;album&gt;: the album that the song is realised on"+
				"<br>&lt;url&gt;: a link to find out more information about the song at"+
				"<br>Add a number in front of these to represent previous tracks (number must be between 1 and 20)"+
				"<br>for example, &lt;1title&gt; would show the title of the previous song</html></body>");
		lastfmMSGFormat.setText("Hay <user> thats, <title> ~ <artist> on <album>");
		lastfmMSGFormat.setBounds(165, 63, 422, 20);
		LastFMConfigPanel.add(lastfmMSGFormat);
		lastfmMSGFormat.setColumns(10);
		MainGui.addComponent(lastfmMSGFormat, "songCommandMessageFormat");
		
		JLabel lblLastfmCommandName = new JLabel("Last.fm Command Name:");
		lblLastfmCommandName.setBounds(8, 90, 149, 16);
		LastFMConfigPanel.add(lblLastfmCommandName);
		
		lastfmCommandName = new JTextField();
		lastfmCommandName.setEnabled(false);
		lastfmCommandName.setToolTipText("The name for the !song command. Prefaced with a exlamation point (!)");
		lastfmCommandName.setText("!song");
		lastfmCommandName.setBounds(165, 88, 422, 20);
		LastFMConfigPanel.add(lastfmCommandName);
		lastfmCommandName.setColumns(10);
		MainGui.addComponent(lastfmCommandName, "lastfmCommandName");
		
		JLabel lblLastfmSettings = new JLabel("Last.fm Settings");
		lblLastfmSettings.setHorizontalAlignment(SwingConstants.CENTER);
		lblLastfmSettings.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblLastfmSettings.setBounds(10, 337, 600, 14);
		add(lblLastfmSettings);
		
		JButton openChatDisplayBTN = new JButton("Open Chat Display");
		openChatDisplayBTN.setForeground(Color.WHITE);
		openChatDisplayBTN.setBounds(214, 299, 375, 24);
		openChatDisplayBTN.setBackground(new Color(24, 26, 28));
		openChatDisplayBTN.setToolTipText("Use this if you want to have your chat display in a seperate window. This can be usefull if for example you need to have it for a stream overlay while editing MainGui.settings.");
		openChatDisplayBTN.addActionListener(new ActionListener() {
			 public void actionPerformed(ActionEvent arg0) {
				 MainGui.displayFrame.setVisible(true);
			}
		});
		add(openChatDisplayBTN);
		
		//Bennerbot.logger.info("\tLoading BotID Panel");
		/*
		 * Bot ID Panel
		 */
		JPanel botIDPanel = new JPanel();
		botIDPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		botIDPanel.setBounds(624, 357, 164, 116);
		add(botIDPanel);
		botIDPanel.setLayout(null);
		
		botID = new JTextField();
		botID.setHorizontalAlignment(SwingConstants.CENTER);
		botID.setBounds(12, 12, 140, 24);
		botIDPanel.add(botID);
		botID.setToolTipText("This determins the ID of the bot, bots using the same ID share settings, set this to something specific to you or write this down somewhere");
		botID.setColumns(10);
		botID.setText(me.jdbener.utill.botId.getHash());
		//make sure that the hash is in the database
		
		botIDUpdate = new JButton("Update");
		botIDUpdate.setToolTipText("Update your bot ID");
		botIDUpdate.setForeground(Color.WHITE);
		botIDUpdate.setBackground(Color.BLACK);
		botIDUpdate.setBounds(12, 40, 140, 24);
		botIDUpdate.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				new Thread(new Runnable(){
					@Override
					public void run() {
						botIDUpdate.setText("Working...");
						me.jdbener.utill.botId.updateFile(botID.getText());
						botIDNumLabel.setText("Bot #"+me.jdbener.utill.botId.getBotID(me.jdbener.utill.botId.getHash()));
						botIDUpdate.setText("Update");
					}
				}).start();
			}
		});
		botIDPanel.add(botIDUpdate);
		
		botIDNumLabel = new JLabel("Bot #"+me.jdbener.utill.botId.getBotID(me.jdbener.utill.botId.getHash()));
		botIDNumLabel.setHorizontalAlignment(SwingConstants.CENTER);
		botIDNumLabel.setBounds(12, 65, 140, 16);
		botIDPanel.add(botIDNumLabel);
		
		JButton btnConfig = new JButton("Config");
		btnConfig.setToolTipText("Click this button to reset your settings to those found in the config file");
		btnConfig.setForeground(Color.WHITE);
		btnConfig.setBackground(Color.BLACK);
		btnConfig.setBounds(12, 81, 62, 24);
		btnConfig.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					@SuppressWarnings("unchecked")
					Map<String, Object> temp = (Map<String, Object>) Yaml.load(new FileInputStream(new File("config/config.yml")));
					for(Entry<String, Object> e: temp.entrySet()){
						Bennerbot.conf.put(e.getKey(), e.getValue());
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				Bennerbot.gui.setValuesfromMap();
			}
		});
		botIDPanel.add(btnConfig);
		
		JButton btnNewButton = new JButton("Database");
		btnNewButton.setToolTipText("Click this button to rest your settings to those found in the Database");
		btnNewButton.setForeground(Color.WHITE);
		btnNewButton.setBackground(Color.BLACK);
		btnNewButton.setBounds(72, 81, 80, 24);
		btnNewButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Bennerbot.gui.db2Map();
				Bennerbot.gui.setValuesfromMap();
			}
		});
		botIDPanel.add(btnNewButton);
		
		JLabel lblBotId = new JLabel("Bot Settings");
		lblBotId.setBounds(624, 336, 164, 16);
		add(lblBotId);
		lblBotId.setHorizontalAlignment(SwingConstants.CENTER);
	}
	private void gui2API(){
		new Thread(new Runnable(){
			@SuppressWarnings("unchecked")
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

					OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
					out.write(data);
					out.flush();
					
					for (Entry<String, List<String>> header : conn.getHeaderFields().entrySet())
						System.out.println(header.getKey() + "=" + header.getValue());
				} catch (Exception e){
					e.printStackTrace();
					success = false;
				}
				try{
					JSONObject obj = (JSONObject) APIManager.parser.parse(Bennerbot.StreamToString(new URL("http://www.hitbox.tv/api/media/live/"+Bennerbot.conf.get("hitboxChannel")+"/list").openStream()));
					System.out.println(obj.toJSONString());
					String lives = ((JSONArray) obj.get("livestream")).toJSONString();
					lives = lives.replace("[", "");
					lives = lives.replace("]", "");
					
					JSONObject live = new JSONObject();
					
					live.put("media_user_name", Bennerbot.conf.get("hitboxChannel"));
					live.put("media_status", streamTitle.getText());
					live.put("category_name", streamGame.getText());
					
					lives = "{ \"livestream\":["+live.toJSONString()+"]}";
					
					JSONObject add = (JSONObject) APIManager.parser.parse(lives);
					
					System.out.println();
					System.out.println(add.toJSONString());
					
					String url = "http://www.hitbox.tv/api/media/live/"+Bennerbot.conf.get("hitboxChannel")+"/list?authToken="+APIManager.GetHitboxAuth("jdbener", "il0venV!");
					URL con = new URL(url);
					HttpURLConnection conn = (HttpURLConnection) con.openConnection();
					
					conn.setRequestMethod("PUT");
					conn.setDoOutput(true);
		
					OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
					out.write(add.toJSONString());
					out.flush();
				} catch (Exception e){
					e.printStackTrace();
					success = false;
				}
				
				lastGame = streamGame.getText();
				lastTitle = streamTitle.getText();
				//TODO add hitbox updating
				
				if(success == true)
					JOptionPane.showMessageDialog(null, "Successfully updated your Stream's Title and/or Game!");
				else
					JOptionPane.showMessageDialog(null, "It apperes something has gone wrong, try again in a few seconds");
					
			}
		}).start();
	}
}
