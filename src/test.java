import java.awt.Desktop;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.net.URL;

import javax.swing.JOptionPane;

import me.jdbener.webserver.tokenServer;

public class test {
	private static tokenServer webserver = new tokenServer();
	public static void main (String[] args){
		System.out.println(getAccessToken());
	}
	public static String getAccessToken(){
		String token = webserver.getToken();
		if(token == ""){
			if(!GraphicsEnvironment.isHeadless()){
				JOptionPane.showMessageDialog(null, "<html><body><center>We see you dont have a Twitch Access Token generated.<br/>One will now be generated<center></body></html>", "Generate Token? ~ Bennerbot v0.17", JOptionPane.DEFAULT_OPTION);
				generateAccessToken();
				while(webserver.getToken().equalsIgnoreCase("")){try {Thread.sleep(1);} catch (InterruptedException e) {e.printStackTrace();}/*do nothing but do enough to keep the system happy*/}
				token = webserver.getToken();
			}
		}
		return token;
	}
	private static void generateAccessToken(){
		String url = "https://api.twitch.tv/kraken/oauth2/authorize?response_type=token&client_id=9qxushp3sdeasixpxajz8pqlxdudfs6&redirect_uri=http://127.0.0.1:55555/token/&scope=channel_editor+channel_subscriptions+chat_login";
		try{
            Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
            if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE))
                desktop.browse(new URL(url).toURI());
        } catch (Exception e){
            e.printStackTrace();

            // Copy URL to the clipboard so the user can paste it into their browser
            StringSelection stringSelection = new StringSelection(url);
            Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
            clpbrd.setContents(stringSelection, null);
            // Notify the user of the failure
            JOptionPane.showMessageDialog(null, "<html><body>We have unsucessfully tried to open a new page to generate an access token for you, <br>however the URL has been copied to your clipboard, simply paste into your browser to generate one.</body></html>");
        }
	}
}

