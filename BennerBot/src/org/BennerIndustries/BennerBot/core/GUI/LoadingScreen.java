package org.BennerIndustries.BennerBot.core.GUI;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.alee.laf.panel.WebPanel;
import com.alee.laf.progressbar.WebProgressBar;
import com.alee.laf.rootpane.WebFrame;

@SuppressWarnings("serial")
public class LoadingScreen extends WebFrame {

	private WebPanel contentPane = new WebPanel();
	private WebProgressBar progressBar = new WebProgressBar(0);

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					final LoadingScreen frame = new LoadingScreen();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public LoadingScreen() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(677,423); 
		setContentPane(contentPane);
		setUndecorated(true);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		contentPane.setLayout(null);
		
		//Sets up the progress bar and then hides it
		progressBar.setValue(0);
		progressBar.setBounds(10, 395, 655, 20);
		progressBar.setStringPainted(true);
		progressBar.setString("Something went wrong! Please restart me.");
		contentPane.add(progressBar);
	
		//Applies the Splash Screen Image to the GUI
		JLabel ImageLabel = new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().getImage(LoadingScreen.class.getResource("/org/BennerIndustries/BennerBot/core/GUI/splash.png"))));
		ImageLabel.setBounds(0, 0, 678, 424);
		contentPane.add(ImageLabel);
	}
	/**
	 * Changes the progress of the load.
	 * @param message The message to display over the loading bar.
	 * @param percential The percentage loaded, 1 = 100% 0 = 0%.
	 */
	public void setProgress(String message, double percential){
		progressBar.setString(message);
		progressBar.setValue((int)(percential*100));
	}
}
