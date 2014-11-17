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
}
