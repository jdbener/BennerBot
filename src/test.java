import java.net.URL;
import java.util.Scanner;

import me.jdbener.apis.APIManager;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


public class test {
	static String channel = "slappy_tuna";
	static String hChannel = "sstak";
	public static void main (String[] args){
			String online = "offline", game = "unknown", title = "unknown";
			int viewers = 0, followers = 0, subscribers = 0;
			try{
				if(((JSONObject) APIManager.parser.parse(StreamToString(new URL("https://api.twitch.tv/kraken/streams/"+channel).openStream()))).get("stream") != null)
					online = "online";
				else
					online = "offline";
			} catch (Exception e){/*e.printStackTrace();*/}
			try{
				viewers = Integer.parseInt(""+((JSONObject)((JSONObject) APIManager.parser.parse(StreamToString(new URL("https://api.twitch.tv/kraken/streams/"+channel).openStream()))).get("stream")).get("viewers"));
			} catch (Exception e){/*e.printStackTrace();*/}
			try{
				followers = Integer.parseInt(""+((JSONObject) APIManager.parser.parse(StreamToString(new URL("https://api.twitch.tv/kraken/channels/"+channel+"/follows/?limit=1").openStream()))).get("_total"));
			} catch (Exception e){/*e.printStackTrace();*/}
			try{
				subscribers = Integer.parseInt(""+((JSONObject) APIManager.parser.parse(StreamToString(new URL("https://api.twitch.tv/kraken/channels/"+channel+"/subscriptions?oauth_token=nkrmqrbqexmply2v8ix6ksdxn1yxdv").openStream()))).get("_total"));
			} catch (Exception e){/*e.printStackTrace();*/}
			try{
				game = ""+((JSONObject)((JSONObject) APIManager.parser.parse(StreamToString(new URL("https://api.twitch.tv/kraken/streams/"+channel).openStream()))).get("stream")).get("game");
			} catch (Exception e){/*e.printStackTrace();*/}
			try{
				title = ""+((JSONObject) APIManager.parser.parse(StreamToString(new URL("https://api.twitch.tv/kraken/channels/"+channel).openStream()))).get("status");
			} catch (Exception e){/*e.printStackTrace();*/}
			System.out.println(channel+" is "+online+" ~ "+title+" playing "+game+" and has "+viewers+" viewers, "+followers+" followers, and "+subscribers+" subscribers");
			//Hitbox
			try{
				JSONArray channels = (JSONArray) ((JSONObject) APIManager.parser.parse(StreamToString(new URL("http://api.hitbox.tv/media").openStream()))).get("livestream");
				JSONObject channel = new JSONObject();
				for(int i = 0; i < channels.size(); i++){
					if(((JSONObject)channels.get(i)).get("media_file").toString().equalsIgnoreCase(hChannel)){
						channel = (JSONObject)channels.get(i);
					}
				}
				
				try{
					if(channel.get("media_is_live").toString().equalsIgnoreCase("1"))
						online = "online";
					else
						online = "offline";
				} catch (Exception e){}
				try{
					viewers = Integer.parseInt(channel.get("media_views").toString());
				} catch (Exception e){}
				try{
					followers = Integer.parseInt(((JSONObject)channel.get("channel")).get("followers").toString());
				} catch (Exception e){}
				try{
					subscribers =  0;
				} catch (Exception e){}
				try{
					game = channel.get("category_name").toString();
				} catch (Exception e){}
				try{
					title = channel.get("media_status").toString();
				} catch (Exception e){}
			} catch (Exception e){
				e.printStackTrace();
			}
			System.out.println(hChannel+" is "+online+" ~ "+title+" playing "+game+" and has "+viewers+" viewers, "+followers+" followers, and "+subscribers+" subscribers");
	}
	public static String StreamToString(java.io.InputStream is) {
	    @SuppressWarnings("resource")
		Scanner s = new Scanner(is).useDelimiter("\\A");
	    return s.hasNext() ? s.next() : "";
	}
}
