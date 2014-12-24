package me.jdbener.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JCheckBox;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JSlider;
import javax.swing.JLabel;

public class ModerationSettingsPanel extends JPanel {
	public static void main (String[] args){
		try {
	    	UIManager.setLookAndFeel("com.jtattoo.plaf.noire.NoireLookAndFeel");
   		} catch (Exception e) {
   			e.printStackTrace();
   		}
		JFrame f = new JFrame("yolo");
		f.setContentPane(new ModerationSettingsPanel());
		f.setSize(1000, 1000);
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -926952150229837706L;
	private JSlider warnings2BanSlider, warnings2TimeoutSlider, maxCapsSlider, maxLengthSlider;
	private JTextField warnings2TimeoutLabel, warnings2BanLabel, maxCapsLabel, maxLengthLabel;
	private JCheckBox chckbxEnableCapsFilter, chckbxEnable, enableModeration;
	private JLabel lblWarningsSettings, lblNewLabel, lblMessageLengthFilter; 
	private JPanel kickSettingsPanel, panel, panel_1;

	/**
	 * Create the panel.
	 */
	public ModerationSettingsPanel() {
		setLayout(null);
		
		enableModeration = new JCheckBox("Enable Moderation?");
		enableModeration.setToolTipText("Weather or not to enable chat monitoring for your channel");
		enableModeration.setHorizontalAlignment(SwingConstants.CENTER);
		enableModeration.setBounds(8, 8, 652, 24);
		enableModeration.addItemListener(new ItemListener(){
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(enableModeration.isSelected()){
					lblWarningsSettings.setVisible(true); 
					lblNewLabel.setVisible(true); 
					lblMessageLengthFilter.setVisible(true); 
					kickSettingsPanel.setVisible(true); 
					panel.setVisible(true); 
					panel_1.setVisible(true);
				} else {
					lblWarningsSettings.setVisible(false); 
					lblNewLabel.setVisible(false); 
					lblMessageLengthFilter.setVisible(false); 
					kickSettingsPanel.setVisible(false); 
					panel.setVisible(false); 
					panel_1.setVisible(false);
				}
			}
		});
		add(enableModeration);
		MainGui.settings.add(enableModeration);
		MainGui.settingsNames.add("enableModeration");
		
		kickSettingsPanel = new JPanel();
		kickSettingsPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		kickSettingsPanel.setBounds(8, 58, 350, 78);
		add(kickSettingsPanel);
		kickSettingsPanel.setLayout(null);
		
		warnings2BanSlider = new JSlider();
		warnings2BanSlider.setValue(20);
		warnings2BanSlider.setBounds(146, 12, 163, 22);
		warnings2BanSlider.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent paramChangeEvent) {
				warnings2BanLabel.setText(warnings2BanSlider.getValue()+"");
				warnings2TimeoutSlider.setMaximum(warnings2BanSlider.getValue()-1);
			}
		});
		kickSettingsPanel.add(warnings2BanSlider);
		
		warnings2BanLabel = new JTextField(warnings2BanSlider.getValue()+"");
		warnings2BanLabel.setBounds(308, 12, 30, 16);
		warnings2BanLabel.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try{
					warnings2BanSlider.setValue(Integer.parseInt(warnings2BanLabel.getText()));
				} catch (Exception e){
					e.printStackTrace();
				}
			}
		});
		kickSettingsPanel.add(warnings2BanLabel);
		MainGui.settings.add(warnings2BanLabel);
		MainGui.settingsNames.add("banWarnings");
		
		warnings2TimeoutSlider = new JSlider();
		warnings2TimeoutSlider.setMinimum(1);
		warnings2TimeoutSlider.setMaximum(warnings2BanSlider.getMaximum());
		warnings2TimeoutSlider.setValue(5);
		warnings2TimeoutSlider.setBounds(146, 46, 163, 22);
		warnings2TimeoutSlider.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent paramChangeEvent) {
				warnings2TimeoutLabel.setText(warnings2TimeoutSlider.getValue()+"");
				warnings2BanSlider.setMinimum(warnings2TimeoutSlider.getValue()+1);
			}
		});
		kickSettingsPanel.add(warnings2TimeoutSlider);
		
		warnings2TimeoutLabel = new JTextField(warnings2TimeoutSlider.getValue()+"");
		warnings2TimeoutLabel.setBounds(308, 46, 30, 16);
		warnings2TimeoutLabel.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try{
					warnings2TimeoutSlider.setValue(Integer.parseInt(warnings2TimeoutLabel.getText()));
				} catch (Exception e){
					e.printStackTrace();
				}
			}
		});
		kickSettingsPanel.add(warnings2TimeoutLabel);
		MainGui.settings.add(warnings2TimeoutLabel);
		MainGui.settingsNames.add("kickWarnings");
		
		JLabel lblWarningsTillBan = new JLabel("Warnings till Ban:");
		lblWarningsTillBan.setBounds(12, 15, 138, 16);
		kickSettingsPanel.add(lblWarningsTillBan);
		
		JLabel lblWarningsTillTimeout = new JLabel("Warnings till Timeout:");
		lblWarningsTillTimeout.setBounds(12, 49, 138, 16);
		kickSettingsPanel.add(lblWarningsTillTimeout);
		
		lblWarningsSettings = new JLabel("Warnings Settings");
		lblWarningsSettings.setBounds(8, 40, 105, 16);
		add(lblWarningsSettings);
		
		/*=====================
		 * Caps Filter Panel
		 ====================*/
		panel = new JPanel();
		panel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel.setBounds(8, 162, 350, 62);
		add(panel);
		panel.setLayout(null);
		
		chckbxEnableCapsFilter = new JCheckBox("Enable Caps Filter?");
		chckbxEnableCapsFilter.setHorizontalAlignment(SwingConstants.CENTER);
		chckbxEnableCapsFilter.setBounds(8, 8, 334, 24);
		chckbxEnableCapsFilter.addItemListener(new ItemListener(){
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				if(chckbxEnableCapsFilter.isSelected()){
					maxCapsSlider.setEnabled(true);
					maxCapsLabel.setEnabled(true);
				} else {
					maxCapsSlider.setEnabled(false);
					maxCapsLabel.setEnabled(false);
				}
			}
		});
		panel.add(chckbxEnableCapsFilter);
		MainGui.settings.add(chckbxEnableCapsFilter);
		MainGui.settingsNames.add("CapsFilter");
		
		maxCapsSlider = new JSlider();
		maxCapsSlider.setMinorTickSpacing(1);
		maxCapsSlider.setEnabled(false);
		maxCapsSlider.setMinimum(1);
		maxCapsSlider.setValue(80);
		maxCapsSlider.setBounds(142, 33, 163, 22);
		maxCapsSlider.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent arg0) {
				maxCapsLabel.setText((((double)maxCapsSlider.getValue())/100)+"");
			}
		});
		panel.add(maxCapsSlider);
		
		JLabel lblPercentageOfCaps = new JLabel("Max Caps Percentage:");
		lblPercentageOfCaps.setBounds(8, 35, 138, 16);
		panel.add(lblPercentageOfCaps);
		
		maxCapsLabel = new JTextField("0.80");
		maxCapsLabel.setEnabled(false);
		maxCapsLabel.setBounds(304, 33, 38, 16);
		maxCapsLabel.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				maxCapsSlider.setValue((int) (Double.parseDouble(maxCapsLabel.getText())*100));
			}
		});
		panel.add(maxCapsLabel);
		//TODO look at this interaction
		MainGui.settings.add(maxCapsLabel);
		MainGui.settingsNames.add("MaxCapsPercentage");
		
		lblNewLabel = new JLabel("Capitalazation Filter Settings");
		lblNewLabel.setBounds(8, 148, 161, 16);
		add(lblNewLabel);
		
		/* ====================
		 * Length Filter Panel
		 ====================*/
		panel_1 = new JPanel();
		panel_1.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel_1.setBounds(8, 256, 350, 78);
		add(panel_1);
		panel_1.setLayout(null);
		
		chckbxEnable = new JCheckBox("Enable Length Filter?");
		chckbxEnable.setHorizontalAlignment(SwingConstants.CENTER);
		chckbxEnable.setBounds(8, 8, 334, 24);
		chckbxEnable.addItemListener(new ItemListener(){
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				if(chckbxEnable.isSelected()){
					maxLengthSlider.setEnabled(true);
					maxLengthLabel.setEnabled(true);
				} else {
					maxLengthSlider.setEnabled(false);
					maxLengthLabel.setEnabled(false);
				}
			}
		});
		panel_1.add(chckbxEnable);
		MainGui.settings.add(chckbxEnable);
		MainGui.settingsNames.add("LengthFilter");
		
		maxLengthLabel = new JTextField("200");
		maxLengthLabel.setEnabled(false);
		maxLengthLabel.setBounds(291, 50, 51, 16);
		maxLengthLabel.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				maxLengthSlider.setValue(Integer.parseInt(maxLengthLabel.getText()));
			}
		});
		panel_1.add(maxLengthLabel);
		
		maxLengthSlider = new JSlider();
		maxLengthSlider.setEnabled(false);
		maxLengthSlider.setMaximum(1000);
		maxLengthSlider.setValue(200);
		maxLengthSlider.setMinimum(1);
		maxLengthSlider.setBounds(142, 44, 148, 22);
		maxLengthSlider.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent arg0) {
				maxLengthLabel.setText(""+maxLengthSlider.getValue());
			}
		});
		panel_1.add(maxLengthSlider);
		
		JLabel lblMaxMessageLength = new JLabel("Max Message Length");
		lblMaxMessageLength.setBounds(8, 46, 138, 16);
		panel_1.add(lblMaxMessageLength);
		
		lblMessageLengthFilter = new JLabel("Message Length Filter Settings");
		lblMessageLengthFilter.setBounds(8, 236, 161, 16);
		add(lblMessageLengthFilter);
		MainGui.settings.add(lblMessageLengthFilter);
		MainGui.settingsNames.add("MaxMessageLength");
		
		/*=====================
		 * Backend Code
		 =====================*/
		enableModeration.setSelected(true);
	}
}
