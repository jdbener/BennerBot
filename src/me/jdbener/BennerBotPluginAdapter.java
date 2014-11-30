package me.jdbener;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
/**
 * This class creates a new instance of the BennerbotPlugin, all that needs to be done is for this to be extended and everything should work
 * @author Joshua Dahl (Jdbener)
 */
public abstract class BennerBotPluginAdapter extends ListenerAdapter<PircBotX> implements BennerBotPlugin {
	
	@Override
	public void onOperatorOuput(String txt) {}

}
