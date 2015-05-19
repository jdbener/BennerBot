package org.BennerIndustries.BennerBot.api.datatypes;

public class SettingReference {
	public enum SettingTypes {String, Integer, Double, Boolean};
	private String name;
	private Object value, defaultValue;
	private SettingTypes type;
	
	public SettingReference(String name, Object value, Object defaultValue, SettingTypes type){
		this.name = name;
		this.value = value;
		this.defaultValue = defaultValue;
		this.type = type;
	}
	public SettingReference(String name, Object defaultValue, SettingTypes type){
		this.name = name;
		this.value = defaultValue;
		this.defaultValue = defaultValue;
		this.type = type;
	}
	
	public String getName(){
		return name;
	}
	public Object getValue(){
		if(type == SettingTypes.String)
			return value.toString();
		else if(type == SettingTypes.Integer)
			return Integer.parseInt(value+"");
		else if(type == SettingTypes.Double)
			return Double.parseDouble(value+"");
		else if(type == SettingTypes.Boolean)
			return value.equals(true);
		return value;
	}
	public String getValueString(){
		return value+"";
	}
	public Object getDefaultValue(){
		if(type == SettingTypes.String)
			return defaultValue.toString();
		else if(type == SettingTypes.Integer)
			return Integer.parseInt(defaultValue+"");
		else if(type == SettingTypes.Double)
			return Double.parseDouble(defaultValue+"");
		else if(type == SettingTypes.Boolean)
			return defaultValue.equals(true);
		return defaultValue;
	}
	public String getDefaultValueString(){
		return defaultValue+"";
	}
	public boolean setValue(Object newValue){
		value = newValue;
		return true;
	}
}
