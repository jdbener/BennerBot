package org.BennerIndustries.BennerBot.api.datatypes;
/**
 * A reference to an entry in the settings registry. If instantiated and tracked by a plugin, it can be used to keep easy track of the current status of the specified setting.
 * @author Joshua Dahl (Jdbener)
 */
public class SettingReference {
	private String name;
	private Object value, defaultValue;
	
	/**
	 * @param name the name of the setting
	 * @param value the setting's current value
	 * @param defaultValue the setting's default value
	 */
	public SettingReference(String name, Object value, Object defaultValue){
		this.name = name;
		this.value = value;
		this.defaultValue = defaultValue;
	}
	/**
	 * @param name the name of the setting
	 * @param defaultValue the setting's default value
	 */
	public SettingReference(String name, Object defaultValue){
		this.name = name;
		this.value = defaultValue;
		this.defaultValue = defaultValue;
	}
	/**
	 * @return the name of the setting
	 */
	public String getName(){
		return name;
	}
	/**
	 * @return the setting's current value, in its base form
	 */
	public Object getValue(){
		String name = value.getClass().getName();
		if(name.contains("String"))
			return value.toString();
		else if(name.contains("int") || name.contains("Integer"))
			return Integer.parseInt(value+"");
		else if(name.contains("double") || name.contains("Double"))
			return Double.parseDouble(value+"");
		else if(name.contains("bool") || name.contains("Bool"))
			return value.equals(true);
		return value;
	}
	/**
	 * @return the setting's current value, as a String
	 */
	public String getValueString(){
		return value+"";
	}
	/**
	 * @return the setting's default value, in its base form
	 */
	public Object getDefaultValue(){
		String name = defaultValue.getClass().getName();
		if(name.contains("String"))
			return defaultValue.toString();
		else if(name.contains("int") || name.contains("Integer"))
			return Integer.parseInt(defaultValue+"");
		else if(name.contains("double") || name.contains("Double"))
			return Double.parseDouble(defaultValue+"");
		else if(name.contains("bool") || name.contains("Bool"))
			return defaultValue.equals(true);
		return defaultValue;
	}
	/**
	 * @return the setting's default value, as a String
	 */
	public String getDefaultValueString(){
		return defaultValue+"";
	}
	/**
	 * Updates the setting's value
	 * @param newValue the new value of the setting
	 */
	public boolean setValue(Object newValue){
		value = newValue;
		return true;
	}
}
