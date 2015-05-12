package me.jdbener.plugins.BennerDJ;

import java.awt.BorderLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import chrriis.dj.nativeswing.swtimpl.NativeInterface;
import chrriis.dj.nativeswing.swtimpl.components.JWebBrowser;

public class BennerDJ extends JPanel{
	public Map<String, String> playlist = new HashMap();	
	JWebBrowser webBrowser;

	public static void main(String[] args) {
		NativeInterface.open();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame frame = new JFrame("YouTube Viewer");
				frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
				frame.getContentPane().add(new BennerDJ(), BorderLayout.CENTER);
				frame.setSize(300, 300);
				frame.setLocationByPlatform(true);
				frame.setVisible(true);
			}
		});
		NativeInterface.runEventPump();
		// don't forget to properly close native components
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run() {
				NativeInterface.close();
			}
		}));
	}

	public BennerDJ() {
		setLayout(new BorderLayout());
		webBrowser = new JWebBrowser();
		add(webBrowser, BorderLayout.CENTER);
		webBrowser.setBarsVisible(false);
		changeVideo("e-ORhEE9VVg");
	}
	private void changeVideo(String id){
		//webBrowser.navigate("http://www.youtube.com/embed/"+id+"?html5=1&autoplay=1&controls=1&disablekb=1&fs=0&modestbranding=1&rel=0");
		webBrowser.navigate("http://bennerbot.x10host.com/youtube/?v="+id);
	}
}

