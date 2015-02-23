package me.jdbener.plugins.BennerDJ;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import me.jdbener.BennerCore;

import com.github.axet.vget.vhs.YouTubeParser;
import com.github.axet.vget.vhs.YouTubeParser.VideoDownload;
import com.github.axet.vget.vhs.YoutubeInfo;

public class testgetytvideourl {
	public static void main(String[] args){
		//http://g11.youtubeinmp3.com/download/?video=https://www.youtube.com/watch?v=3y2gp5SEOUA&n=1
		URL url = getYTURL("https://www.youtube.com/watch?v=4a_zuVYYknk");
		System.out.println(url);


		/*
			try {
		    	Player player;

				//player = Manager.createPlayer(new File("nyancat.mp3").toURI().toURL());
				player = Manager.createRealizedPlayer(new File("nyancat.mp3").toURI().toURL());

				System.out.println(player.getDuration().getSeconds());
			    player.start(); // start player
			    
			    //player.close();

			    JFrame f = new JFrame();
			    f.setSize(300, 50);
			    f.setResizable(false);
			    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			    //f.add(player.getVisualComponent());
			    f.add(player.getControlPanelComponent());
			    f.setVisible(true);
			} catch (NoPlayerException | IOException | CannotRealizeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//		   */
	}
	/**
	 * Loads youtube video using the specified id and format. The available formats are:
	 * 1 = mp4/720p
	 * 2 = webm/480p
	 * 3 = mp4/480p
	 * 4 = flv/320p
	 * 5 = 3gpp/320p
	 * 6 = 3gpp/320p
	 * @param id the youtube video's id
	 * @param format the format to use
	 * @return the url to find said video
	 */
	public static String getYTVideoURL(String id, int format){
		if(format < 1) format = 1; if(format > 6) format = 6; if(format == 3)format = 1;
		ArrayList<String> l = new ArrayList<String>(Arrays.asList(BennerCore.readURL("http://youtube.com/get_video_info?video_id="+id).split("&")));
		int counter = 1;
		for(String s: l){
			if(s.contains("url_encoded_fmt_stream_map"))
				try {
					for(String st: new ArrayList<String>(Arrays.asList(java.net.URLDecoder.decode(s, "UTF-8").split("&"))))
						if(st.contains("url=http")){
							if(counter == format)
								return java.net.URLDecoder.decode(st.split("=")[1], "UTF-8");
							counter++;
						}
				} catch (UnsupportedEncodingException e) {e.printStackTrace();}
		}
		return "";
	}
	public static URL getYTURL(String url){
    	try {      	
            YoutubeInfo info = new YoutubeInfo(new URL(url));

            YouTubeParser parser = new YouTubeParser();
            parser.extract(info, new AtomicBoolean(), null);

            List<VideoDownload> list = parser.extractLinks(info);
            
            System.out.println(info.getTitle());
            URL videoURL = null;
            boolean done = false;
            for (VideoDownload d : list) {
            	if(d.stream.toString().contains("mp4"))
            		if(!done){
            			//System.out.println(d.stream + "\t" + java.net.URLDecoder.decode(d.url+"", "UTF-8"));
            			videoURL = new URL(java.net.URLDecoder.decode(d.url+"", "UTF-8"));
            			done = true;
            		}
            }
            HttpURLConnection c = (HttpURLConnection) videoURL.openConnection();
            if(!c.getContentType().equalsIgnoreCase("text/plain"))
            	return videoURL;
        } catch (Exception e) {
            e.printStackTrace();
        }
		return null;
    }
	/*public static URL getVimoURL(String url){
    	try {      	
            VimeoInfo info = new VimeoInfo(new URL(url));

            VimeoParser parser = new VimeoParser();
            parser.extract(info, new AtomicBoolean(), null);

            List<com.github.axet.vget.vhs.VimeoParser.VideoDownload> list = parser.extractLinks(info, new AtomicBoolean(), null);
            
            System.out.println(info.getTitle());
            URL videoURL = new URL(url);
            boolean done = false;
            for (com.github.axet.vget.vhs.VimeoParser.VideoDownload d : list) {
            	///System.out.println(d.url);
            	if(d.vq.ordinal())
            		if(!done){
            			//System.out.println(d.stream + "\t" + java.net.URLDecoder.decode(d.url+"", "UTF-8"));
            			videoURL = new URL(java.net.URLDecoder.decode(d.url+"", "UTF-8"));
            			done = true;
            		}
            }
            HttpURLConnection c = (HttpURLConnection) videoURL.openConnection();
            if(!c.getContentType().equalsIgnoreCase("text/plain"))
            	return videoURL;
        } catch (Exception e) {
            e.printStackTrace();
        }
		return null;
    }*/
}
//1 = mp4/720
//2 = webm/medium
//3 = mp4/medium
//4 = flv/small
//5 = 3gpp/small
//6 = 3gpp/small
//java.net.URLDecoder.decode(s, "UTF-8")