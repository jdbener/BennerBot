package me.jdbener.gui;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map.Entry;

import javax.swing.JLabel;

import me.jdbener.Bennerbot;
import me.jdbener.apis.APIManager;

import javax.swing.JCheckBox;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class AutoMessagePanel extends JPanel {
	private static final long serialVersionUID = -528938451643829635L;
	private DefaultListModel<String> model;
	private JList<String> list;
	private JPanel Panel;
	private JTextField Field;
	private JTextField textField;
	private JSlider slider;
	/**
	 * Create the panel.
	 */
	public AutoMessagePanel() {
		setToolTipText("How Often should a message be sent? (Measured in Seconds)");
		/*=====================================
		 * s List
		 *=====================================*/
		model = new DefaultListModel<String>();
		setLayout(null);
		list = new JList<String>(model);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setBounds(10, 11, 252, 454);
		list.setVisibleRowCount(20);
		list.setModel(model);
		
		JScrollPane sScrollPane = new JScrollPane(list);
		sScrollPane.setBounds(12, 60, 770, 398);
		add(sScrollPane);
		
		/*=====================================
		 * s Buttons
		 *=====================================*/
		JButton addButton = new JButton("Add");
		addButton.setForeground(Color.WHITE);
		addButton.setBackground(Color.BLACK);
		addButton.setBounds(652, 39, 38, 24);
		addButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				new Thread(new Runnable(){
					@Override
					public void run() {
						JOptionPane.showMessageDialog(null, Panel, "Add AutoMessage  ~ "+Bennerbot.name+" v"+Bennerbot.version, JOptionPane.PLAIN_MESSAGE);
						if(!Field.getText().equalsIgnoreCase("")){
							model.addElement(Field.getText());
							Field.setText("");
						}
						gui2Map();
						setValuesFromMap();
						map2DB();
					}
				}).start();
			}
		});
		add(addButton);
		
		Field = new JTextField(20);
		Field.setToolTipText("The message you want the bot to send");

	    Panel = new JPanel();
	    Panel.setSize(500, 200);
	    Panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
	    JLabel label = new JLabel("Message:");
	    Panel.add(label);
	    Panel.add(Field);
		
		JButton removeButton = new JButton("Remove");
		removeButton.setForeground(Color.WHITE);
		removeButton.setBackground(Color.BLACK);
		removeButton.setBounds(720, 39, 62, 24);
		removeButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				new Thread(new Runnable(){
					@Override
					public void run() {
						for(int i: list.getSelectedIndices()){
							model.remove(i);
						}
						gui2Map();
						setValuesFromMap();
						map2DB();
					}
				}).start();
			}
		});
		add(removeButton);
		
		JButton button = new JButton("Edit");
		button.setForeground(Color.WHITE);
		button.setBackground(Color.BLACK);
		button.setBounds(685, 39, 37, 24);
		button.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				new Thread(new Runnable(){
					@Override
					public void run() {
						int selected = list.getSelectedIndex();
						String key = model.get(selected);
						Field.setText(key);
						JOptionPane.showMessageDialog(null, Panel, "Update AutoMessage  ~ "+Bennerbot.name+" v"+Bennerbot.version, JOptionPane.PLAIN_MESSAGE);
						model.setElementAt(Field.getText(), selected);
						Field.setText("");
						gui2Map();
						setValuesFromMap();
						map2DB();
					}
				}).start();
			}
		});
		add(button);
		
		JLabel lblMessages = new JLabel("Messages:");
		lblMessages.setBounds(12, 43, 100, 16);
		add(lblMessages);
		
		JCheckBox chckbxEnableAutoMessages = new JCheckBox("Enable Auto Messages?");
		chckbxEnableAutoMessages.setBounds(8, 8, 165, 24);
		chckbxEnableAutoMessages.addItemListener(new ItemListener(){
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == 1){
					slider.setEnabled(true);
					textField.setEnabled(true);
				} else {
					slider.setEnabled(false);
					textField.setEnabled(false);
				}
			}	
		});
		add(chckbxEnableAutoMessages);
		MainGui.settings.add(chckbxEnableAutoMessages);
		MainGui.settingsNames.add("enableAutoMessages");
		
		JLabel lblTimeBetweenMessages = new JLabel("Time Between Messages:");
		lblTimeBetweenMessages.setToolTipText("");
		lblTimeBetweenMessages.setBounds(187, 12, 153, 16);
		add(lblTimeBetweenMessages);
		
		slider = new JSlider();
		slider.setToolTipText("How often you want a message to be sent (measured in seconds)");
		slider.setEnabled(false);
		slider.setSnapToTicks(true);
		slider.setMaximum(6000);
		slider.setValue(300);
		slider.setMinorTickSpacing(5);
		slider.setMinimum(10);
		slider.setBounds(332, 8, 400, 24);
		slider.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent arg0) {
				textField.setText(slider.getValue()+"");
			}
		});
		add(slider);
		
		textField = new JTextField("300");
		textField.setEnabled(false);
		textField.setBounds(744, 12, 38, 16);
		textField.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				slider.setValue(Integer.parseInt(textField.getText()));
			}
		});
		add(textField);
		MainGui.settings.add(textField);
		MainGui.settingsNames.add("autoMessageInterval");
		MainGui.settingRestarts.add("autoMessageInterval");
		
		/*=====================================
		 * Backend Code
		 *=====================================*/
		setupMapTable();
		new Thread(new Runnable(){
			@Override
			public void run() {
				while(model.size() == 0){
					setValuesFromMap();
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				dB2Map();
				setValuesFromMap();
				return;
			}
		}).start();

	}
	public void setValuesFromMap(){
		for(Entry<String, String> e: Bennerbot.messagesMap.entrySet()){
			try{
				for(int i = 0; i < model.size(); i++)
					model.removeElement(e.getValue());
			
				String value = e.getValue().trim();
				value = value.startsWith(" ") ? value.substring(1) : value;
				if(!model.contains(value))
					model.addElement(value);
				
			} catch (Exception ex){
				ex.printStackTrace();
			}
		}
	}
	public void gui2Map(){
		for(int i = 0; i < model.size(); i++){
			String value = model.get(i).trim();
			value = value.startsWith(" ") ? value.substring(1) : value;
			Bennerbot.messagesMap.put(getKeyFromValue(value), value);
		}
	}
	private String getKeyFromValue(String value){
		if(Bennerbot.messagesMap.containsValue(value)){
			try{
			for(Entry<String, String> e: Bennerbot.messagesMap.entrySet()){
				if(e.getValue().equalsIgnoreCase(value)){
					return e.getKey().toString();
				}
			}
			} catch (Exception e){
				e.printStackTrace();
				return (Bennerbot.messagesMap.size()+1)+"";
			}
		} else {
			return (Bennerbot.messagesMap.size()+1)+"";
		}
		return "";
	}
	private void setupMapTable(){
		try{
			Connection c = APIManager.getConnection();
			Statement stmt = c.createStatement();
			String query = "CREATE TABLE IF NOT EXISTS MESSAGES (BID	INT, FIELD	TEXT, VALUE TEXT);";
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
			stmt.execute("DELETE FROM MESSAGES WHERE BID = "+me.jdbener.lib.botId.getBotID());
			for(Entry<String, String> e: Bennerbot.messagesMap.entrySet()){
				stmt.execute("INSERT INTO MESSAGES VALUES ("+me.jdbener.lib.botId.getBotID()+", '"+e.getKey()+"', '"+e.getValue()+"')");
			}
			stmt.close();
			c.close();
		} catch (SQLException e){
			e.printStackTrace();
		}
	}
	public void dB2Map() {
		try{
			Connection c = APIManager.getConnection();
			Statement stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM MESSAGES WHERE BID = "+me.jdbener.lib.botId.getBotID());
			while(rs.next()){
				Bennerbot.messagesMap.put(rs.getString("FIELD"), rs.getString("VALUE"));
			}
			stmt.close();
			c.close();
		} catch (SQLException e){
			e.printStackTrace();
			map2DB();
			dB2Map();
		}
	}
}
