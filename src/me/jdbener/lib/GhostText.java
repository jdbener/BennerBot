package me.jdbener.lib;

import javax.swing.JTextField;

public class GhostText implements Runnable{
	JTextField c;
	String d, g;
	public GhostText(JTextField component, String default_, String ghostText){
		c = component;
		d = default_;
		g = ghostText;
	}
	@Override
	public void run() {
		//if the text area is focused
		if(!c.isFocusOwner()){
			//if there is no message in the text area
			if(c.getText().equalsIgnoreCase(d)){
				//enter the tutorial message
				c.setText(g);
			}
			//if the text area is not focused
		} else {
			//if the tutorial messaged is entered
			if(c.getText().equalsIgnoreCase(g)){
				//clear the text area
				c.setText(d);
			}
		}
	}
}