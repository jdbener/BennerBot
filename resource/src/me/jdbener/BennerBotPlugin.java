/**
 * This interface is what plugins have to implement to be included in the bot
 * Author: Jdbener (Joshua Dahl)
 * Date: 11/8/14
 */
package me.jdbener;

import net.xeoh.plugins.base.Plugin;

public interface BennerBotPlugin extends Plugin {
	/**
	 * This function returns a string that declares the name of the plugin, this is used for beautification and logging purposes
	 * @return ~ the name of the plugin
	 */
	public String getName();
	/**
	 * This function is run when the plugin is load, this function is basically a main method.
	 */
	public void inititate();
	/**
	 * This function is "optional" meaning that it can be left blank without any filler,
	 * however if you want your plugin to do something when a user sends a message, you can use this function.
	 * You can also link this back into your onMessage function if needed.
	 */
	public void onOperatorOuput(String txt);
}
