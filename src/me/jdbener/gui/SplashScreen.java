package me.jdbener.gui; 

import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;

import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class SplashScreen extends JWindow {
	private static final long serialVersionUID = 4181665667244408191L;
	private Image img = Toolkit.getDefaultToolkit().getImage(SplashScreen.class.getResource("/me/jdbener/gui/splash.png"));
	private JProgressBar bar = new JProgressBar(0, 100);
	public SplashScreen() {
		SwingUtilities.invokeLater(new Runnable(){
			@Override
			public void run() {
				setSize(675,420); 
				setLocationRelativeTo(null);
				setContentPane(new JLabel(new ImageIcon(img)));
				bar.setForeground(Color.RED);
				bar.setStringPainted(true);
				bar.setBounds(5, 395, 665, 20);
				bar.addPropertyChangeListener(new PropertyChangeListener(){
					@Override
					public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent) {
						if(bar.getValue() == 0)
							bar.setVisible(false);
						else
							bar.setVisible(true);
					}
				});
				getContentPane().add(bar);
				setAlwaysOnTop(true);
			}
		});
	}
	public void setProgress(int p){
		//System.out.println("changing progress to: "+p);
		bar.setValue(p);
		//Bennerbot.logger.info(bar.getValue()+"");
		bar.repaint();
	}
	public void start(){
		setVisible(true);
	}
	public void end(){
		setVisible(false);
		dispose();
	}
}
