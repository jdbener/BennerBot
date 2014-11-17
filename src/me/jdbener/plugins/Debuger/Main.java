package me.jdbener.plugins.Debuger;

import java.io.File;
import java.io.IOException;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.BadLocationException;

import me.jdbener.Bennerbot;
import me.jdbener.utilities.SmartScroller;

public class Main{
	JEditorPane editor;
	JScrollPane pane;
	JFrame f;
	String print = "";
	
	
	public static void main(String[] args){
		Main main = new Main();
		
		while(true){
			main.update();
		}
	}
	public Main(){
		editor = new JEditorPane();
		editor.setEditable(true);
		editor.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		pane = new JScrollPane(editor);
		pane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		pane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		pane.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		f = new JFrame(Bennerbot.name+" v"+Bennerbot.version+" ~ Console");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.getContentPane().add(pane);
		f.setSize(400, 400);
		f.setVisible(true);
		new SmartScroller(pane);
	}
	
	public void update(){
		String file = "ERROR";
		try {
			file = Bennerbot.readFile(new File("resource/output-dirty.txt").getCanonicalFile().getPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		file = file.replace(print, "");
		print+=file;
		try {
			editor.getDocument().insertString(editor.getDocument().getLength(), file, null);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

}
