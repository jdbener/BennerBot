package me.jdbener.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.JButton;

import me.jdbener.Bennerbot;

import java.awt.Color;

public class miscSettingsPanel extends JPanel {
	private static final long serialVersionUID = 3270562443564784442L;
	private JCheckBox chckbxEnableRellay, chckbxShowMessageSource;

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
		MainGui.addComponent(chckbxEnableRellay, "activateRelay", false);
		
		chckbxShowMessageSource = new JCheckBox("Show Message Source?");
		chckbxShowMessageSource.setToolTipText("Weather or not the server the message is from should be shown (this is helpful if you are managing alot of servers)");
		chckbxShowMessageSource.setBounds(8, 54, 172, 24);
		relayPanel.add(chckbxShowMessageSource);
		MainGui.addComponent(chckbxShowMessageSource, "showSource", false);
		
		JPanel apiPanel = new JPanel();
		apiPanel.setBorder(new TitledBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null), "API Settings", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		apiPanel.setBounds(12, 12, 290, 49);
		add(apiPanel);
		apiPanel.setLayout(null);
		JCheckBox chckbxEnableCheckBox = new JCheckBox("Enable Follower Chat Notificatoins");
		chckbxEnableCheckBox.setToolTipText("Weather or not to show a message in chat some someone follows");
		chckbxEnableCheckBox.setBounds(6, 17, 270, 24);
		apiPanel.add(chckbxEnableCheckBox);
		MainGui.addComponent(chckbxEnableCheckBox, "enableFollowerNotifications", false);
		
		JButton btnResetTwitchAccess = new JButton("Reset Twitch Access Token");
		btnResetTwitchAccess.setForeground(Color.WHITE);
		btnResetTwitchAccess.setBackground(Color.BLACK);
		btnResetTwitchAccess.setBounds(12, 64, 290, 23);
		btnResetTwitchAccess.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Bennerbot.regenerateAccessToken();
			}
		});
		add(btnResetTwitchAccess);
	}
}
