package org.BennerIndustries.BennerBot.api.datatypes;

public class PluginReference {
	/**
	 * A list of every tag that plugins can have
	 */
	public enum PluginTags {CONNECTIONPROVIDER, FILTER, UI, UTILITY, UNTAGGED, ALL};
	
	private String pIdent, name, desc = "";
	private PluginTags[] tags;
	
	/**
	 * Creates a reference that identifies a plugin, providing important infromation about it that can be easily accessed.
	 * @param identifier the identifier that is unique to this plugin.
	 * @param name the name of the plugin
	 * @param tags a list of tags that identify the plugin
	 */
	public PluginReference(String identifier, String name, PluginTags[] tags){
		pIdent = identifier;
		this.name = name;
		this.tags = tags;
	}
	/**
	 * Creates a reference that identifies a plugin, providing important infromation about it that can be easily accessed.
	 * @param identifier the identifier that is unique to this plugin.
	 * @param name the name of the plugin.
	 * @param description provides some infromation about what the plugin does.
	 * @param tags a list of tags that identify the plugin
	 */
	public PluginReference(String identifier, String name, String description, PluginTags[] tags){
		pIdent = identifier;
		this.name = name;
		this.desc = description;
		this.tags = tags;
	}
	public String getIdentifier(){
		return pIdent;
	}
	public String getName(){
		return name;
	}
	public String getDescription(){
		return desc;
	}
	public PluginTags[] getTags(){
		return tags;
	}
	
}
