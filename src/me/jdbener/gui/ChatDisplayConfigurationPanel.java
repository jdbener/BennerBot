package me.jdbener.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class ChatDisplayConfigurationPanel extends JPanel {
	private static final long serialVersionUID = 8655094463955877291L;
	private JTextField messageFormatBox;
	DisplayPanel displayPanel;
	
	/**
	 * Create the panel.
	 */
	public ChatDisplayConfigurationPanel() {
		JButton displayPopoutButton = new JButton("Popout Display");
		displayPopoutButton.setBounds(0, 0, 799, 23);
		displayPopoutButton.setForeground(Color.WHITE);
		displayPopoutButton.setBackground(Color.BLACK);
		displayPopoutButton.setToolTipText("Use this if you want to have your chat display in a seperate window. This can be usefull if for example you need to have it for a stream overlay while editing settings.");
		displayPopoutButton.addActionListener(new ActionListener() {
			 @Override
			public void actionPerformed(ActionEvent arg0) {
				 MainGui.displayFrame.setVisible(true);
			}
		});
		setLayout(null);
		add(displayPopoutButton);
		
		JCheckBox nameShortenerCheckbox = new JCheckBox("Shorten Usernames?");
		nameShortenerCheckbox.setToolTipText("Weather or not to shorten usernames, long names can cause issues and users names longer than 32 characters will be trimed no matter what");
		nameShortenerCheckbox.setBounds(10, 34, 150, 23);
		add(nameShortenerCheckbox);
		MainGui.settings.add(nameShortenerCheckbox);
		MainGui.settingsNames.add("nameShortener");
		
		JCheckBox highlightMessagesCheckbox = new JCheckBox("Highlight Messages?");
		highlightMessagesCheckbox.setSelected(true);
		highlightMessagesCheckbox.setToolTipText("Weather or not to highlight messages that include your name in them");
		highlightMessagesCheckbox.setBounds(10, 60, 150, 23);
		add(highlightMessagesCheckbox);
		MainGui.settings.add(highlightMessagesCheckbox);
		MainGui.settingsNames.add("HighlighMessages");
		
		JCheckBox enableBotMessages = new JCheckBox("Display Bot Messages?");
		enableBotMessages.setSelected(true);
		enableBotMessages.setToolTipText("Weather or not to display messages from \"bennerbot\"");
		enableBotMessages.setBounds(293, 34, 189, 23);
		add(enableBotMessages);
		MainGui.settings.add(enableBotMessages);
		MainGui.settingsNames.add("enableBotMessages");
		
		JCheckBox chckbxEnableUserJoin = new JCheckBox("Display User Join Messages?");
		chckbxEnableUserJoin.setToolTipText("Weather or not a message will be sent when a user joins the channel");
		chckbxEnableUserJoin.setBounds(293, 60, 189, 23);
		add(chckbxEnableUserJoin);
		MainGui.settings.add(chckbxEnableUserJoin);
		MainGui.settingsNames.add("enableJoinMessages");
		
		JCheckBox chckbxEnableUserLeave = new JCheckBox("Display User Leave Messages?");
		chckbxEnableUserLeave.setToolTipText("Weather or not to send a message when a user leaves the channel");
		chckbxEnableUserLeave.setBounds(486, 60, 199, 23);
		add(chckbxEnableUserLeave);
		MainGui.settings.add(chckbxEnableUserLeave);
		MainGui.settingsNames.add("enableLeaveMessages");
		
		JCheckBox chckbxDisplayPluginMessages = new JCheckBox("Display Plugin Messages?");
		chckbxDisplayPluginMessages.setSelected(true);
		chckbxDisplayPluginMessages.setToolTipText("Weather or not messages that plugins create will be displayed, some of theis messages will be a little spammy");
		chckbxDisplayPluginMessages.setBounds(486, 34, 199, 23);
		add(chckbxDisplayPluginMessages);
		MainGui.settings.add(chckbxDisplayPluginMessages);
		MainGui.settingsNames.add("enablePluginMessages");
		
		JCheckBox chckbxFilterbots = new JCheckBox("Filter Bots?");
		chckbxFilterbots.setToolTipText("Weather or not to filter users whos names end in \"bot\"");
		chckbxFilterbots.setBounds(164, 34, 125, 23);
		add(chckbxFilterbots);
		MainGui.settings.add(chckbxFilterbots);
		MainGui.settingsNames.add("filterBots");
		
		JCheckBox chckbxFilterCommands = new JCheckBox("Filter Commands?");
		chckbxFilterCommands.setToolTipText("Weather or not to display messages that start with an exlamation point (!)");
		chckbxFilterCommands.setBounds(164, 60, 125, 23);
		add(chckbxFilterCommands);
		MainGui.settings.add(chckbxFilterCommands);
		MainGui.settingsNames.add("filterCommands");
		
		messageFormatBox = new JTextField();
		messageFormatBox.setToolTipText(" ");
		messageFormatBox.setHorizontalAlignment(SwingConstants.CENTER);
		messageFormatBox.setText("<server> <badge><timestamp> <user>: <message>");
		messageFormatBox.setToolTipText("<html><body>The format for messages that are displayed in the GUI or Written to a file<br><ol>"
				+ "<li>&lt;server&gt;: the server icon that this message is from</li>"
				+ "<li>&lt;badge&gt;: the moderator/streamer badge</li>"
				+ "<li>&lt;timestamp&gt;: the current time</li>"
				+ "<li>&lt;color&gt;: the color for a specific user</li>"
				+ "<li>&lt;user&gt;: the username of the user who sent the message, with his color added</li>"
				+ "<li>&lt;noformateuser&gt;: the username of the user who sent the message</li>"
				+ "<li>&lt;message&gt;: the message itself</li></ol></body></html>");
		messageFormatBox.setBounds(10, 93, 779, 20);
		add(messageFormatBox);
		messageFormatBox.setColumns(10);
		MainGui.settings.add(messageFormatBox);
		MainGui.settingsNames.add("DisplayMessageFormat");
		
		displayPanel = new DisplayPanel();
		displayPanel.setBounds(0, 116, 799, 365);
		add(displayPanel);
	}
}
