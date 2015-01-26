package me.jdbener.gui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import me.jdbener.Bennerbot;
import me.jdbener.apis.APIManager;

import java.awt.Color;

public class CustomCommandsPanel extends JPanel {
	private static final long serialVersionUID = -5697654528947385438L;
	private DefaultListModel<String> commandModel,
									 variableModel;
	private JList<String> CommandsList,
						  VariableList;
	private JTextField commandKeyField, variableKeyField;
	private JTextField commandValueField, variableValueField;
	private JPanel commandPanel,
				   variablePanel;
	
	private JButton commandRefresh, variableRefresh;
	
	/**
	 * Create the panel.
	 */
	public CustomCommandsPanel() {

		setLayout(null);
		
		/*=====================================
		 * Commands List
		 *=====================================*/
		commandModel = new DefaultListModel<String>();
		CommandsList = new JList<String>(commandModel);
		CommandsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		CommandsList.setBounds(10, 11, 252, 454);
		CommandsList.setVisibleRowCount(20);
		CommandsList.setModel(commandModel);
		
		JScrollPane commandsScrollPane = new JScrollPane(CommandsList);
		commandsScrollPane.setBounds(10, 45, 776, 185);
		add(commandsScrollPane);
		
		/*=====================================
		 * Commands Buttons
		 *=====================================*/
		JButton addCommandButton = new JButton("Add");
		addCommandButton.setForeground(Color.WHITE);
		addCommandButton.setBackground(Color.BLACK);
		addCommandButton.setBounds(595, 23, 52, 23);
		addCommandButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				new Thread(new Runnable(){
					@Override
					public void run() {
						JOptionPane.showMessageDialog(null, commandPanel, "Add Command ~ "+Bennerbot.name+" v"+Bennerbot.version, JOptionPane.PLAIN_MESSAGE);
						if(!commandKeyField.getText().equalsIgnoreCase("") && !commandValueField.getText().equalsIgnoreCase("")){
							commandModel.addElement(commandKeyField.getText().replace("!", "").replace(" ", "").toLowerCase()+": "+commandValueField.getText());
							commandKeyField.setText("");
							commandValueField.setText("");
							commandsGui2Map();
							commandsSetValuesFromMap();
							commandsMap2DB();
						}
						return;
					}
				}).start();
			}
		});
		add(addCommandButton);
		
		commandKeyField = new JTextField(20);
		commandKeyField.setToolTipText("The name of the command that you want people to put an exlamation point in front of to use");
	    commandValueField = new JTextField(20);
	    commandValueField.setToolTipText("What the bot will return after the command is sent");

	    commandPanel = new JPanel();
	    commandPanel.setSize(500, 200);
	    commandPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
	    JLabel label = new JLabel("Command Name:");
	    commandPanel.add(label);
	    commandPanel.add(commandKeyField);
	    
	    commandPanel.add(new JLabel("Bot Return:"));
	    commandPanel.add(commandValueField);
		
		JButton removeCommandButton = new JButton("Remove");
		removeCommandButton.setForeground(Color.WHITE);
		removeCommandButton.setBackground(Color.BLACK);
		removeCommandButton.setBounds(694, 23, 92, 23);
		removeCommandButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				new Thread(new Runnable(){
					@Override
					public void run() {
						for(int i: CommandsList.getSelectedIndices()){
							commandModel.remove(i);
						}
						commandsGui2Map();
						commandsSetValuesFromMap();
						commandsMap2DB();
					}
				}).start();
			}
		});
		add(removeCommandButton);
		
		JButton button = new JButton("Edit");
		button.setForeground(Color.WHITE);
		button.setBackground(Color.BLACK);
		button.setBounds(644, 23, 52, 23);
		button.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				new Thread(new Runnable(){
					@Override
					public void run() {
						int selected = CommandsList.getSelectedIndex();
						String key = commandModel.get(selected).split(":")[0];
						String value = commandModel.get(selected).split(":")[1];
						commandKeyField.setText(key);
						commandValueField.setText(value);
						JOptionPane.showMessageDialog(null, commandPanel, "Update Command ~ "+Bennerbot.name+" v"+Bennerbot.version, JOptionPane.PLAIN_MESSAGE);
						commandModel.setElementAt(commandKeyField.getText().replace(" ", "").replace("<", "").replace(">", "").toLowerCase()+": "+commandValueField.getText(), selected);
						commandKeyField.setText("");
						commandValueField.setText("");
						commandsGui2Map();
						commandsSetValuesFromMap();
						commandsMap2DB();
					}
				}).start();
			}
		});
		add(button);
		
		/*=====================================
		 * Variables List
		 *=====================================*/
		variableModel = new DefaultListModel<String>();
		VariableList = new JList<String>(variableModel);
		VariableList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		VariableList.setBounds(10, 11, 252, 454);
		VariableList.setVisibleRowCount(20);
		VariableList.setModel(variableModel);
		
		JScrollPane variableScrollPane = new JScrollPane(VariableList);
		variableScrollPane.setBounds(10, 276, 776, 185);
		add(variableScrollPane);
		
		/*=====================================
		 * Variable Buttons
		 *=====================================*/
		JButton addVariableButton = new JButton("Add");
		addVariableButton.setForeground(Color.WHITE);
		addVariableButton.setBackground(Color.BLACK);
		addVariableButton.setBounds(595, 254, 52, 23);
		addVariableButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				new Thread(new Runnable(){
					@Override
					public void run() {
						JOptionPane.showMessageDialog(null, variablePanel, "Add Variable ~ "+Bennerbot.name+" v"+Bennerbot.version, JOptionPane.PLAIN_MESSAGE);
						if(!variableKeyField.getText().equalsIgnoreCase("") && !variableValueField.getText().equalsIgnoreCase("")){
							variableModel.addElement("<"+variableKeyField.getText().replace(" ", "").replace("<", "").replace(">", "")+">".toLowerCase()+": "+variableValueField.getText());
							variableKeyField.setText("");
							variableValueField.setText("");
							varibleGui2Map();
							variablesSetValuesFromMap();
							variableMap2DB();
						}
						return;
					}
				}).start();
			}
		});
		add(addVariableButton);
		
		variableKeyField = new JTextField(20);
		variableKeyField.setToolTipText("The name of the command that you want people to put an exlamation point in front of to use");
	    variableValueField = new JTextField(20);
	    variableValueField.setToolTipText("What the bot will return after the command is sent");

	    variablePanel = new JPanel();
	    variablePanel.setSize(500, 200);
	    variablePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
	    variablePanel.add(new JLabel("Variable Name:"));
	    variablePanel.add(variableKeyField);
	    
	    variablePanel.add(new JLabel("Variable Value:"));
	    variablePanel.add(variableValueField);
		
		JButton removeVaribleButton = new JButton("Remove");
		removeVaribleButton.setForeground(Color.WHITE);
		removeVaribleButton.setBackground(Color.BLACK);
		removeVaribleButton.setBounds(694, 254, 92, 23);
		removeVaribleButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				new Thread(new Runnable(){
					@Override
					public void run() {
						for(int i: VariableList.getSelectedIndices()){
							variableModel.remove(i);
						}
						varibleGui2Map();
						variablesSetValuesFromMap();
						variableMap2DB();
					}
				}).start();
			}
		});
		add(removeVaribleButton);
		
		JLabel lblCommands = new JLabel("Commands");
		lblCommands.setBounds(12, 27, 148, 16);
		add(lblCommands);
		
		JLabel lblVariables = new JLabel("Variables");
		lblVariables.setBounds(12, 257, 129, 16);
		add(lblVariables);
		
		JButton btnEdit = new JButton("Edit");
		btnEdit.setForeground(Color.WHITE);
		btnEdit.setBackground(Color.BLACK);
		btnEdit.setBounds(644, 254, 52, 23);
		btnEdit.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				new Thread(new Runnable(){
					@Override
					public void run() {
						int selected = VariableList.getSelectedIndex();
						String key = variableModel.get(selected).split(":")[0];
						String value = variableModel.get(selected).split(":")[1];
						variableKeyField.setText(key);
						variableValueField.setText(value);
						JOptionPane.showMessageDialog(null, variablePanel, "Update Variable ~ "+Bennerbot.name+" v"+Bennerbot.version, JOptionPane.PLAIN_MESSAGE);
						variableModel.setElementAt("<"+variableKeyField.getText().replace(" ", "").replace("<", "").replace(">", "")+">".toLowerCase()+": "+variableValueField.getText(), selected);
						variableKeyField.setText("");
						variableValueField.setText("");
						varibleGui2Map();
						variablesSetValuesFromMap();
						variableMap2DB();
					}
				}).start();
			}
		});
		add(btnEdit);
		
		JLabel lblThisWindowAlso = new JLabel("<html><body>This window also loads commands and variables from the flat file,<br> if it is from the flat file it can not be modified or removed.</body></html>");
		lblThisWindowAlso.setBounds(153, 230, 385, 47);
		add(lblThisWindowAlso);
		
		/*=====================================
		 * Refresh Buttons
		 *=====================================*/
		commandRefresh = new JButton("Refresh");
		commandRefresh.setForeground(Color.WHITE);
		commandRefresh.setBackground(Color.BLACK);
		commandRefresh.setBounds(706, 23, 80, 23);
		commandRefresh.setVisible(false);
		commandRefresh.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				new Thread(new Runnable(){
					@Override
					public void run() {
						commandsDB2Map();
						commandsSetValuesFromMap();
					}
				}).start();
			}
		});
		add(commandRefresh);
		
		variableRefresh = new JButton("Refresh");
		variableRefresh.setForeground(Color.WHITE);
		variableRefresh.setBackground(Color.BLACK);
		variableRefresh.setBounds(706, 254, 80, 23);
		variableRefresh.setVisible(false);
		variableRefresh.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				new Thread(new Runnable(){
					@Override
					public void run() {
						variableDB2Map();
						variablesSetValuesFromMap();
					}
				}).start();
			}
		});
		add(variableRefresh);
		
		
		
		/*=====================================
		 * Backend Code
		 *=====================================*/
		setupCommandsMapTable();
		setupVariableMapTable();
		//command thread
		new Thread(new Runnable(){
			@Override
			public void run() {
				while(commandModel.size() == 0){
					commandsSetValuesFromMap();
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				commandsDB2Map();
				commandsSetValuesFromMap();
				return;
			}
		}).start();
		//variable thread
		new Thread(new Runnable(){
			@Override
			public void run() {
				while(variableModel.size() == 0){
					variablesSetValuesFromMap();
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				variableDB2Map();
				variablesSetValuesFromMap();
				return;
			}
		}).start();
		
		new Thread(new Runnable(){
			@Override
			public void run() {
				try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
				commandRefresh.getActionListeners()[0].actionPerformed(null);
				variableRefresh.getActionListeners()[0].actionPerformed(null);
			}
		}).start();
	}
	/*
	 * Command List Functions
	 */
	public void commandsSetValuesFromMap(){
		commandModel.clear();
		for(Entry<String, String> e: Bennerbot.commandMap.entrySet()){
			try{
				String key = e.getKey().trim();
				key = key.startsWith(" ") ? key.substring(1) : key;
				String value = e.getValue().trim();
				value = value.startsWith(" ") ? value.substring(1) : value;
				String text = (key+": "+value).trim();
				text = text.startsWith(" ") ? text.substring(1) : text;
				key = text.split(":")[0];
				value = text.split(":")[1];
				text = key+": "+value;
				if(!commandModel.contains(text))
					commandModel.addElement(text);
			} catch (Exception ex){
				ex.printStackTrace();
			}
		}
	}
	public void commandsGui2Map(){
		Map<String, String> temp = new HashMap<String, String>();
		for(int i = 0; i < commandModel.size(); i++){
			String key = commandModel.get(i).split(":")[0].trim();
			key = key.startsWith(" ") ? key.substring(1) : key;
			String value = commandModel.get(i).split(":")[1].trim();
			value = value.startsWith(" ") ? value.substring(1) : value;
			temp.put(key, value);
		}
		if(!temp.equals(Bennerbot.commandMap))
			Bennerbot.commandMap = temp;
	}
	private void setupCommandsMapTable(){
		try{
			Connection c = APIManager.getConnection();
			Statement stmt = c.createStatement();
			String query = "CREATE TABLE IF NOT EXISTS COMMANDS (BID	INT, FIELD	TEXT, VALUE TEXT);";
			stmt.execute(query);
			stmt.close();
			c.close();
		} catch (SQLException e){
			e.printStackTrace();
		}
	}
	private void commandsMap2DB(){
		try{
			Connection c = APIManager.getConnection();
			Statement stmt = c.createStatement();
			stmt.execute("DELETE FROM COMMANDS WHERE BID = "+me.jdbener.utill.botId.getBotID());
			for(Entry<String, String> e: Bennerbot.commandMap.entrySet()){
				stmt.execute("INSERT INTO COMMANDS VALUES ("+me.jdbener.utill.botId.getBotID()+", '"+e.getKey()+"', '"+e.getValue()+"')");
			}
			stmt.close();
			c.close();
		} catch (SQLException e){
			e.printStackTrace();
		}
	}
	public void commandsDB2Map() {
		try{
			Connection c = APIManager.getConnection();
			Statement stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM COMMANDS WHERE BID = "+me.jdbener.utill.botId.getBotID());
			while(rs.next()){
				Bennerbot.commandMap.put(rs.getString("FIELD"), rs.getString("VALUE"));
			}
			stmt.close();
			c.close();
		} catch (SQLException e){
			e.printStackTrace();
			commandsMap2DB();
			commandsDB2Map();
		}
	}
	
	/*
	 * Variable List Functions
	 */
	public void variablesSetValuesFromMap(){
		for(Entry<String, String> e: Bennerbot.variableMap.entrySet()){
			try{
				for(int i = 0; i < variableModel.size(); i++)
					variableModel.removeElement(e.getKey()+": "+e.getValue());
			
				String key = e.getKey().trim();
				key = key.startsWith(" ") ? key.substring(1) : key;
				String value = e.getValue().trim();
				value = value.startsWith(" ") ? value.substring(1) : value;
				String text = (key+": "+value).trim();
				text = text.startsWith(" ") ? text.substring(1) : text;
				key = text.split(":")[0];
				value = text.split(":")[1];
				text = key+": "+value;
				if(!variableModel.contains(text))
					variableModel.addElement(text);
				
			} catch (Exception ex){
				ex.printStackTrace();
			}
		}
	}
	public void varibleGui2Map(){
		Map<String, String> temp = new HashMap<String, String>();
		for(int i = 0; i < variableModel.size(); i++){
			String key = variableModel.get(i).split(":")[0].trim();
			key = key.startsWith(" ") ? key.substring(1) : key;
			String value = variableModel.get(i).split(":")[1].trim();
			value = value.startsWith(" ") ? value.substring(1) : value;
			temp.put(key, value);
		}
		if(!temp.equals(Bennerbot.variableMap))
			Bennerbot.variableMap = temp;
	}
	private void setupVariableMapTable(){
		try{
			Connection c = APIManager.getConnection();
			Statement stmt = c.createStatement();
			String query = "CREATE TABLE IF NOT EXISTS VARIABLES (BID	INT, FIELD	TEXT, VALUE TEXT);";
			stmt.execute(query);
			stmt.close();
			c.close();
		} catch (SQLException e){
			e.printStackTrace();
		}
	}
	private void variableMap2DB(){
		try{
			Connection c = APIManager.getConnection();
			Statement stmt = c.createStatement();
			stmt.execute("DELETE FROM VARIABLES WHERE BID = "+me.jdbener.utill.botId.getBotID());
			for(Entry<String, String> e: Bennerbot.variableMap.entrySet()){
				stmt.execute("INSERT INTO VARIABLES VALUES ("+me.jdbener.utill.botId.getBotID()+", '"+e.getKey()+"', '"+e.getValue()+"')");
			}
			stmt.close();
			c.close();
		} catch (SQLException e){
			e.printStackTrace();
		}
	}
	public void variableDB2Map() {
		try{
			Connection c = APIManager.getConnection();
			Statement stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM VARIABLES WHERE BID = "+me.jdbener.utill.botId.getBotID());
			while(rs.next()){
				Bennerbot.variableMap.put(rs.getString("FIELD"), rs.getString("VALUE"));
			}
			stmt.close();
			c.close();
		} catch (SQLException e){
			e.printStackTrace();
			variableMap2DB();
			variableDB2Map();
		}
	}
}
