package me.jdbener.speech;

import java.awt.Color;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sun.speech.freetts.*;
import com.sun.speech.freetts.audio.*;

import javax.sound.sampled.AudioFormat;

import me.jdbener.Bennerbot;
import me.jdbener.utill.UserColors;
import me.jdbener.utill.user;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

@SuppressWarnings("unused")
public class TextToSpeachManager{
	private static Voice voice;
	private static String lastuser = "";
	
	public static void read(MessageEvent<PircBotX> e){
		//String voiceName = "kevin16";
        VoiceManager voiceManager = VoiceManager.getInstance();
        
        voice = voiceManager.getVoice("kevin16");

        if (voice == null) {
            System.out.println("Voice not found.");
            System.exit(1);
        }
        
        String user = Bennerbot.capitalize(e.getUser().getNick());
        String message = e.getMessage().trim();
        
        double variablity = 1;
        
        if(Bennerbot.conf.get("enableVariability").toString().equalsIgnoreCase("true")){
        	variablity = getVaribilityForUser(user);
        }
        
        voice.setPitch(getPitchForUser(user, variablity));
        int speed = getTempoForUser(user);
        if(!Bennerbot.configEqualsString("TTSSpeed", "default")){
        	speed = Integer.parseInt(Bennerbot.configGetString("TTSSpeed"));
        }
        voice.setRate(speed);
        voice.setVolume(Integer.parseInt(Bennerbot.configGetString("TTSVolume")));
        
        voice.allocate();

        message = replaceEmotes(message).trim().replaceAll("[^ -~]", "?");
        
        if(!user.equalsIgnoreCase(lastuser) || !Bennerbot.conf.get("enableTTSMinimalisticMode").toString().equalsIgnoreCase("true")){
        	message = (user + " says: " + message).trim();
        	lastuser = user;
        }
        
        if(!e.getMessage().startsWith("!"))
        	voice.speak(message);
	}
	private static int getPitchForUser(String name, double variablity) {
        int n = name.charAt(0) + name.charAt(name.length() - 1);
        Color color = UserColors.defaultcolors[n % UserColors.defaultcolors.length].getColor();
        int r = (int) (((color.getRed()+color.getBlue()+color.getGreen())/3.835)*variablity);
        if(r>200)r=200;if(r<0)r=0;
        return r;
	}
	private static int getTempoForUser(String name){
		return 200-(getPitchForUser(name, 1)/2);
	}
	private static double getVaribilityForUser(String name){
		double var = (((double)getTempoForUser(name)/100)/50);
		return 1+var;
	}
	private static String replaceEmotes(String msg){
		return filterURLS(msg.replaceAll("\\;-?(p|P)", "ToungFace")
				.replaceAll("\\:\\&gt\\;", "MonkeyLove")
				.replaceAll("\\:-?/", "What")
				.replaceAll("\\:-?\\(", "SadFace")
				.replaceAll("\\:-?[z|Z|\\|]", "TiredFace")
				.replaceAll("\\:-?D", "BigSmileyFace")
				.replaceAll("\\:-?(S|s)", "ZipperFace")
				.replaceAll("\\:-?(p|P)", "TongueFace")
				.replaceAll("\\:-?(o|O)", "JawDrops")
				.replaceAll("\\:-?(?:\\/|\\\\)(?!\\/)", "Eh")
				.replaceAll("\\&lt\\;\\]", "BirthdayMonkey")
				.replaceAll("\\&gt\\;\\(", "ScreamyFace")
				.replaceAll("[o|O](_|\\.)[o|O]", "OH")
				.replaceAll("\\:-?\\)", "SmileyFace")
				.replace("lmao", "Lauphing My Ass Off")
				.replace("w0t", "what")
				.replace("***", "Censored")
				.replace("*", ""));
	}
	public static String filterURLS(String s) {
		Matcher m = Pattern.compile(
				new StringBuilder()
				.append("((?:(http|https|Http|Https|rtsp|Rtsp):")
				.append("\\/\\/(?:(?:[a-zA-Z0-9\\$\\-\\_\\.\\+\\!\\*\\'\\(\\)")
					.append("\\,\\;\\?\\&\\=]|(?:\\%[a-fA-F0-9]{2})){1,64}(?:\\:(?:[a-zA-Z0-9\\$\\-\\_")
					.append("\\.\\+\\!\\*\\'\\(\\)\\,\\;\\?\\&\\=]|(?:\\%[a-fA-F0-9]{2})){1,25})?\\@)?)?")
					.append("((?:(?:[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}\\.)+")   // named host
					.append("(?:")   // plus top level domain
					.append("(?:aero|arpa|asia|a[cdefgilmnoqrstuwxz])")
					.append("|(?:biz|b[abdefghijmnorstvwyz])")
					.append("|(?:cat|com|coop|c[acdfghiklmnoruvxyz])")
					.append("|d[ejkmoz]")
					.append("|(?:edu|e[cegrstu])")
					.append("|f[ijkmor]")
					.append("|(?:gov|g[abdefghilmnpqrstuwy])")
					.append("|h[kmnrtu]")
					.append("|(?:info|int|i[delmnoqrst])")
					.append("|(?:jobs|j[emop])")
					.append("|k[eghimnrwyz]")
					.append("|l[abcikrstuvy]")
					.append("|(?:mil|mobi|museum|m[acdghklmnopqrstuvwxyz])")
					.append("|(?:name|net|n[acefgilopruz])")
					.append("|(?:org|om)")
					.append("|(?:pro|p[aefghklmnrstwy])")
					.append("|qa")
					.append("|r[eouw]")
					.append("|s[abcdeghijklmnortuvyz]")
					.append("|(?:tel|travel|t[cdfghjklmnoprtvwz])")
					.append("|u[agkmsyz]")
					.append("|v[aceginu]")
					.append("|w[fs]")
					.append("|y[etu]")
					.append("|z[amw]))")
					.append("|(?:(?:25[0-5]|2[0-4]") // or ip address
					.append("[0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9])\\.(?:25[0-5]|2[0-4][0-9]")
					.append("|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(?:25[0-5]|2[0-4][0-9]|[0-1]")
					.append("[0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(?:25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}")
					.append("|[1-9][0-9]|[0-9])))")
					.append("(?:\\:\\d{1,5})?)") // plus option port number
					.append("(\\/(?:(?:[a-zA-Z0-9\\;\\/\\?\\:\\@\\&\\=\\#\\~")  // plus option query params
					.append("\\-\\.\\+\\!\\*\\'\\(\\)\\,\\_])|(?:\\%[a-fA-F0-9]{2}))*)?")
					.append("(?:\\b|$)").toString()
				).matcher(s);;
		StringBuffer sb = new StringBuffer();
		while(m.find()){
		    //String found =m.group(0); 
		    m.appendReplacement(sb, ""); 
		}
		m.appendTail(sb);
		return sb.toString();
    }
}
