package me.jdbener.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

import me.jdbener.Bennerbot;

public class StartupDialog extends JDialog {
	private static final long serialVersionUID = 6450492467328019466L;
	private final JPanel contentPanel = new JPanel();
	private int clicked = 0;
	
	public StartupDialog(){setAlwaysOnTop(true);}
	
	/**
	 * Create the dialog.
	 */
	public int getOption(){
		setTitle(Bennerbot.name+" "+Bennerbot.version);
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e){
				setVisible(false);
				System.exit(0);
			}
		});
		setVisible(true);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			JLabel lblWhichGuiFormat = new JLabel("Which GUI Format would you like to use?");
			contentPanel.add(lblWhichGuiFormat);
		}
		{
			JPanel buttonPane = new JPanel();
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			FlowLayout fl_buttonPane = new FlowLayout(FlowLayout.CENTER, 5, 5);
			buttonPane.setLayout(fl_buttonPane);
			{
				JButton okButton = new JButton("Configuation GUI");
				okButton.setToolTipText("The Configuation GUI is a more basic GUI with more features, what you would expect from a another bot such as Nightbot or Meowbot");
				okButton.setActionCommand("Yes");
				okButton.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent arg0) {
						clicked = 1;
					}
				});
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Lite GUI");
				cancelButton.setToolTipText("The \"Lite GUI\" is a more basic gui for those running on lower end computers or who want to primarily use the configuration file");
				cancelButton.setActionCommand("No");
				cancelButton.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent arg0) {
						clicked = 2;
					}
				});
				buttonPane.add(cancelButton);
			}
		}
		setSize(250, 100);
		setResizable(false);
		setLocationRelativeTo(null);
		setAlwaysOnTop(false);
		setAlwaysOnTop(true);
		while(clicked == 0){try {
			setAlwaysOnTop(true);
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}}
		this.dispose();
		return clicked;
	}
}
