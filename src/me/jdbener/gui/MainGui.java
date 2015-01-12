package me.jdbener.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import me.jdbener.Bennerbot;
import me.jdbener.apis.APIManager;

import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;

public class MainGui extends JFrame {
	private static final long serialVersionUID = 3242478898346354359L;
	/**The information/update button, shove random status updates here*/
	public JButton infoButton;
	/**weather or not the bot should update*/ 
	private boolean updated = false,
	/**weather or not the bot should update and restart*/
					restart = false;
	private int thingsAdded;
	//the tabed pane
	public JTabbedPane tabbedPane;
	//settings stuff
	public static ArrayList<JComponent> settings = new ArrayList<JComponent>();
	public static ArrayList<String> settingsNames = new ArrayList<String>();
	public static ArrayList<String> settingRestarts = new ArrayList<String>();
	//configurarion
	private Map<String, Object> OConf = new HashMap<String, Object>();
	
	//random variables
	private JPanel contentPane;
	private ChatDisplayConfigurationPanel CDC;
	private AutoMessagePanel autoMessagePanel;
	private DisplayPanel displayFrameDisplay;
	public static JFrame displayFrame;

	/**
	 * Create the frame.
	 */
	public MainGui() {	
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage(MainGui.class.getResource("/me/jdbener/gui/Lion.png")));
		setTitle(Bennerbot.name+" v"+Bennerbot.version);
		/*
		 * Define the inclosing frame
		 */
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e){
				if(updated){
					if(JOptionPane.showConfirmDialog(null, "Some setting have been changed... would you like to save them?", "Settings Changed ~ "+Bennerbot.name+" v"+Bennerbot.version, JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION){
						setVisible(false);
						map2DB(true);
					}
				}
				setVisible(false);
				System.exit(0);
			}
		});
		setBounds(100, 100, 929, 537);
		contentPane = new JPanel();
		contentPane.setBorder(null);
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		/*
		 * The tabs
		 */
		Bennerbot.logger.info("Loading Beggining Tab Loading Sequence");
		tabbedPane = new JTabbedPane(JTabbedPane.LEFT);
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		tabbedPane.setBackground(Color.WHITE);
		tabbedPane.setBorder(null);
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		
		/* ============================
		 * General Configuration Tab
		 * ============================*/
		Bennerbot.logger.info("Loading the \"General Configuration Tab\"");
		GeneralConfigurationPanel generalConfigPanel = new GeneralConfigurationPanel();
		generalConfigPanel.setLocation(-250, -162);
		tabbedPane.addTab("<html><body><center><table width='100'>General Configuration</table></body></html>", null, generalConfigPanel, "General settings, Connections, Databases, ETC...");
		tabbedPane.setForegroundAt(0, Color.GRAY);
		tabbedPane.setBackgroundAt(0, Color.BLACK);
		tabbedPane.setEnabledAt(0, true);
		generalConfigPanel.setLayout(null);
		generalConfigPanel.setBorder(null);
		
		
		/* ============================
		 * Chat Display Tab
		 * ============================*/
		Bennerbot.logger.info("Loading the \"Chat Display Tab\"");
		CDC = new ChatDisplayConfigurationPanel(); 
		tabbedPane.addTab("<html><body><center><table width='100'>Chat Display</table></body></html>", null, CDC, null);
		tabbedPane.setBackgroundAt(1, Color.BLACK);
		
		/* ============================
		 * Chat Popout Display Frame
		 * ============================*/
		Bennerbot.logger.info("Loading the \"Chat Display Window\"");
		displayFrameDisplay = new DisplayPanel();
		displayFrame = DisplayPanel.createJFrame(displayFrameDisplay);
		displayFrame.setBounds(1029, 100, 400, 537);
		displayFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		displayFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e){
				displayFrame.setVisible(false);
			}
		});
		displayFrame.setIconImage(Toolkit.getDefaultToolkit().getImage(MainGui.class.getResource("/me/jdbener/gui/Lion.png")));
		displayFrame.setTitle("Chat ~ "+Bennerbot.name+" v"+Bennerbot.version);
//		displayFrame.setAlwaysOnTop(true);
		
		/* ============================
		 * Moderation Tab
		 * ============================*/
		Bennerbot.logger.info("Loading the \"Chat Moderation Settings Tab\"");
		ModerationSettingsPanel ModerationPanel = new ModerationSettingsPanel(); 
		tabbedPane.addTab("<html><body><center><table width='100'>Chat Moderation</table></body></html>", null, ModerationPanel, null);
		tabbedPane.setBackgroundAt(2, Color.BLACK);
		tabbedPane.setBackgroundAt(1, Color.BLACK);
		
		/* ============================
		 * Commands Tab
		 * ============================*/
		Bennerbot.logger.info("Loading the \"Custom Commands Tab\"");
		CustomCommandsPanel CommandPanel = new CustomCommandsPanel(); 
		tabbedPane.addTab("<html><body><center><table width='100'>Commands & Variables</table></body></html>", null, CommandPanel, null);
		tabbedPane.setForegroundAt(3, Color.WHITE);
		tabbedPane.setBackgroundAt(3, Color.BLACK);
		tabbedPane.setBackgroundAt(2, Color.BLACK);
		tabbedPane.setBackgroundAt(1, Color.BLACK);
		
		/* ============================
		 * AutoMessage Tab
		 * ============================*/
		Bennerbot.logger.info("Loading the \"AutoMessage Tab\"");
		autoMessagePanel = new AutoMessagePanel(); 
		tabbedPane.addTab("<html><body><center><table width='100'>AutoMessage Settings</table></body></html>", null, autoMessagePanel, null);
		tabbedPane.setBackgroundAt(4, Color.BLACK);
		tabbedPane.setForegroundAt(3, Color.WHITE);
		tabbedPane.setBackgroundAt(3, Color.BLACK);
		tabbedPane.setBackgroundAt(2, Color.BLACK);
		tabbedPane.setBackgroundAt(1, Color.BLACK);
		
		/* ============================
		 * Miscellaneous Tab
		 * ============================*/
		Bennerbot.logger.info("Loading the \"Miscellaneous Settings Tab\"");
		miscSettingsPanel miscSettings = new miscSettingsPanel(); 
		tabbedPane.addTab("<html><body><center><table width='100'>Miscellaneous Settings</table></body></html>", null, miscSettings, null);
		tabbedPane.setBackgroundAt(5, Color.BLACK);
		tabbedPane.setBackgroundAt(4, Color.BLACK);
		tabbedPane.setForegroundAt(3, Color.WHITE);
		tabbedPane.setBackgroundAt(3, Color.BLACK);
		tabbedPane.setBackgroundAt(2, Color.BLACK);
		tabbedPane.setBackgroundAt(1, Color.BLACK);
		
		/* ============================
		 * Information Button
		 * ============================*/
		Bennerbot.logger.info("Loading the \"Info Button\"");
		infoButton = new JButton("Close this Window to stop the Bot");
		infoButton.setEnabled(false);
		infoButton.setForeground(Color.WHITE);
		infoButton.setBackground(Color.BLACK);
		infoButton.setToolTipText("This button provides lots of usefull infromation as well as being the tool for updating the bot");
		infoButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				new Thread(new Runnable(){
					@Override
					public void run() {
						if(updated){
							infoButton.setEnabled(false);
							infoButton.setText("Working...");
							if(restart){
								infoButton.setText("Restarting...");
								map2DB(true);
								try {
									Restart.restartApplication(null);
								} catch (IOException e) {e.printStackTrace();}
							} else {
								map2DB();
							}
							for(Entry<String, Object> e: Bennerbot.conf.entrySet())
								OConf.put(e.getKey(), e.getValue());
							infoButton.setText("Done!");
							try {
								Thread.sleep(3000);
							} catch (InterruptedException e1) {e1.printStackTrace();}
							updateMap();
							//Bennerbot.name = Bennerbot.conf.get("botName").toString().trim();
						} else {}
					}
				}).start();	
			}
		});
		contentPane.add(infoButton, BorderLayout.SOUTH);
		/* ============================
		 * Bot Settings Window
		 * ============================*/
		
		/* ============================
		 * Backend Code that Makes it all run!
		 * ============================*/
		Bennerbot.logger.info("Finished Tab Initiation Squence... Loading Backend");
		setupMapTable();		
		db2Map();
		for(Entry<String, Object> e: Bennerbot.conf.entrySet()){
			OConf.put(e.getKey(), e.getValue());
		}
		setValuesfromMap();
		setupMapListeners();
		Bennerbot.guiLoaded = true;
		Bennerbot.logger.info("Finished Loading GUI Backend");
		//Bennerbot.name = Bennerbot.conf.get("botName").toString().trim();
	}
	public void writeDisplay(String write){
		displayFrameDisplay.append(write);
		CDC.displayPanel.append(write);
	}
	public void refreshAutoMessages(){
		autoMessagePanel.dB2Map();
	}
	private void updateMap(){
		//update current values table
		for(int i = 0; i < settings.size(); i++){
			String value = "";
			JComponent j = settings.get(i);
			if(j.getClass().getName().equalsIgnoreCase("javax.swing.JCheckBox"))
				value = (((JCheckBox)j).isSelected())+"";
			else if(j.getClass().getName().equalsIgnoreCase("javax.swing.JTextField"))
				value = (((JTextField)j).getText());
			else if(j.getClass().getName().equalsIgnoreCase("javax.swing.JPasswordField"))
				value = new String((((JPasswordField)j).getPassword()));
			
			Bennerbot.conf.put(settingsNames.get(i), value);
		}
		updated = updated();
		restart = shouldRestart();
		if(updated){
			infoButton.setEnabled(true);
			if(restart){
				infoButton.setText("Update Settings and Restart (Restart is required for some of your changes to take effect)");
			} else {
				infoButton.setText("Update Settings");
			}
		} else {
			infoButton.setEnabled(false);
			infoButton.setText("Close This Window to Stop the Bot");
		}
	}
	public void setValuesfromMap(){
		for(java.util.Map.Entry<String, Object> e: Bennerbot.conf.entrySet()){
			for(int i2 = 0; i2 < settingsNames.size(); i2++){
				if(settingsNames.get(i2).equalsIgnoreCase(e.getKey())){
						if(e.getValue() != null)
							if(settings.get(i2).getClass().getName().equalsIgnoreCase("javax.swing.JCheckBox"))
								((JCheckBox)settings.get(i2)).setSelected(e.getValue().toString().equalsIgnoreCase("true"));
							else if(settings.get(i2).getClass().getName().equalsIgnoreCase("javax.swing.JTextField"))
								((JTextField)settings.get(i2)).setText(e.getValue().toString());
							else if(settings.get(i2).getClass().getName().equalsIgnoreCase("javax.swing.JPasswordField"))
								((JPasswordField)settings.get(i2)).setText(e.getValue().toString());
				}
			}
		}
	}
	private void setupMapListeners(){
		ActionListener checkboxActions = new ActionListener() {@Override public void actionPerformed(ActionEvent e) {updateMap();}};
		DocumentListener textareaActions = new DocumentListener(){@Override public void changedUpdate(DocumentEvent arg0) {updateMap();}@Override public void insertUpdate(DocumentEvent arg0) {updateMap();}@Override public void removeUpdate(DocumentEvent arg0) {updateMap();}};
		for(int i = 0; i < settings.size(); i++){
			JComponent j = settings.get(i);
			if(j.getClass().getName().equalsIgnoreCase("javax.swing.JCheckBox"))
				((JCheckBox)settings.get(i)).addActionListener(checkboxActions);
			else if(j.getClass().getName().equalsIgnoreCase("javax.swing.JTextField"))
				((JTextField)settings.get(i)).getDocument().addDocumentListener(textareaActions);
			else if(j.getClass().getName().equalsIgnoreCase("javax.swing.JPasswordField"))
				((JPasswordField)settings.get(i)).getDocument().addDocumentListener(textareaActions);
			updateMap();
		}
	}
	private boolean updated(){
		MapDifference<String, String> diff = Maps.difference(convertMapToString(OConf), convertMapToString(Bennerbot.conf));
		boolean update = false;
		if(!diff.entriesDiffering().toString().equalsIgnoreCase("{}")){
			update = true;
		}
		return update;
	}
	private boolean shouldRestart(){
		MapDifference<String, String> diff = Maps.difference(convertMapToString(OConf), convertMapToString(Bennerbot.conf));
		boolean restart = false;
		for(String s: settingRestarts)
			if(diff.entriesDiffering().containsKey(s))
				restart = true;
		return restart;
	}
	private void setupMapTable(){
		try{
			Connection c = APIManager.getConnection();
			Statement stmt = c.createStatement();
			String query = "CREATE TABLE IF NOT EXISTS SETTINGS (BID	INT, FIELD	TEXT, VALUE TEXT);";
			stmt.execute(query);
			stmt.close();
			c.close();
		} catch (SQLException e){
			e.printStackTrace();
		}
	}
	private void map2DB(){
		try{
			Connection c = APIManager.getConnection();
			Statement stmt = c.createStatement();
			stmt.execute("DELETE FROM SETTINGS WHERE BID = "+me.jdbener.lib.botId.getBotID());
			thingsAdded = filterMap(convertMapToString(Bennerbot.conf), settingsNames).size();
			for(final Entry<String, String> e: filterMap(convertMapToString(Bennerbot.conf), settingsNames).entrySet()){
				new Thread(new Runnable(){
					@Override
					public void run() {
						try {
							Connection c = APIManager.getConnection();
							Statement stmt = c.createStatement();
							stmt.execute("INSERT INTO SETTINGS VALUES ("+me.jdbener.lib.botId.getBotID()+", '"+e.getKey()+"', '"+e.getValue()+"')");
							stmt.close();
							c.close();
						} catch (SQLException e1) {
							e1.printStackTrace();
						}
						thingsAdded--;
						return;
					}
				}).start();
			}
			stmt.close();
			c.close();
		} catch (SQLException e){
			e.printStackTrace();
		}
	}
	private void map2DB(boolean shouldWait){
		map2DB();
		while(thingsAdded != 0){System.out.print(thingsAdded);}System.out.println("");
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	public void db2Map() {
		try{
			Connection c = APIManager.getConnection();
			Statement stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM SETTINGS WHERE BID = "+me.jdbener.lib.botId.getBotID());
			while(rs.next()){
				Bennerbot.conf.put(rs.getString("FIELD"), rs.getString("VALUE"));
			}
			stmt.close();
			c.close();
		} catch (SQLException e){
			e.printStackTrace();
			map2DB();
			db2Map();
		}
	}
	private static Map<String, String> convertMapToString(Map<String, Object> in){
		Map<String, String> out = new HashMap<String, String>();
		for(Entry<String, Object> e: in.entrySet()){
			String key = e.getKey().toString();
			String outValue = e.getValue()+"";
			
			out.put(key, outValue);
		}
		return out;
	}
	private static Map<String, String> filterMap(Map<String, String> m, ArrayList<String> f){
		Map<String, String> out = new HashMap<String, String>();
		for(Entry<String, String> e: m.entrySet()){
			for(String s: f){
				if(e.getKey().contains(s)){
					out.put(e.getKey(), e.getValue());
				}
			}
		}
		return out;
	}
}
class Restart {
	/** 
	 * Sun property pointing the main class and its arguments. 
	 * Might not be defined on non Hotspot VM implementations.
	 */
	public static final String SUN_JAVA_COMMAND = "sun.java.command";

	/**
	 * Restart the current Java application
	 * @param runBeforeRestart some custom code to be run before restarting
	 * @throws IOException
	 */
	public static void restartApplication(Runnable runBeforeRestart) throws IOException{
		try {
			// java binary
			String java = System.getProperty("java.home") + "/bin/java";
			// vm arguments
			List<String> vmArguments = ManagementFactory.getRuntimeMXBean().getInputArguments();
			StringBuffer vmArgsOneLine = new StringBuffer();
			for (String arg : vmArguments) {
				// if it's the agent argument : we ignore it otherwise the
				// address of the old application and the new one will be in conflict
				if (!arg.contains("-agentlib")) {
					vmArgsOneLine.append(arg);
					vmArgsOneLine.append(" ");
				}
			}
			// init the command to execute, add the vm args
			final StringBuffer cmd = new StringBuffer("\"" + java + "\" " + vmArgsOneLine);

			// program main and program arguments
			String[] mainCommand = System.getProperty(SUN_JAVA_COMMAND).split(" ");
			// program main is a jar
			if (mainCommand[0].endsWith(".jar")) {
				// if it's a jar, add -jar mainJar
				cmd.append("-jar " + new File(mainCommand[0]).getPath());
			} else {
				// else it's a .class, add the classpath and mainClass
				cmd.append("-cp \"" + System.getProperty("java.class.path") + "\" " + mainCommand[0]);
			}
			// finally add program arguments
			for (int i = 1; i < mainCommand.length; i++) {
				cmd.append(" ");
				cmd.append(mainCommand[i]);
			}
			// execute the command in a shutdown hook, to be sure that all the
			// resources have been disposed before restarting the application
			Runtime.getRuntime().addShutdownHook(new Thread() {
				@Override
				public void run() {
					try {
						Runtime.getRuntime().exec(cmd.toString());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
			// execute some custom code before restarting
			if (runBeforeRestart!= null) {
				runBeforeRestart.run();
			}
			// exit
			System.exit(0);
		} catch (Exception e) {
			// something went wrong
			throw new IOException("Error while trying to restart the application", e);
		}
	}
}
