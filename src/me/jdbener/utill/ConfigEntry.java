package me.jdbener.utill;

public class ConfigEntry {
	String name;
	String value;
	String help;
	
	public ConfigEntry(String name, String value, String help){
		this.name = name;
		this.value = value;
		this.help = help;
	}
	public String getName(){
		return name;
	}
	public String getValue(){
		return value;
	}
	public void setValue(String value){
		this.value = value;
	}
	public String getHelp(){
		return help;
	}
	public void setHelp(String help){
		this.help = help;
	}
}
