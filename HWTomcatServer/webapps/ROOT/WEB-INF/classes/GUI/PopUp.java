package GUI;
/**

MessageWindow.java
this is the message window.
*/

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import Assignments.*;
import View.*;
import java.text.*;
import java.math.*;
import Browser.*;
import Game.*;

public class PopUp extends Application{
	private HtmlHandler textPane;
	private Hacker MyHacker;
	private String message;
	public PopUp(Hacker MyHacker,String message){
		this.MyHacker=MyHacker;
		this.message=message;
		this.setTitle("Pop Up");
		this.setResizable(false);
		this.setMaximizable(false);
		this.setClosable(true);
		this.setIconifiable(true);
		this.addInternalFrameListener(this);
		setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
		setBounds(50,50,400,400);
		setLayout(null);
		populate();
	}

	public void populate(){
		textPane = new HtmlHandler();
		//textPane.createView();
		add(textPane.getView());
		try{
				textPane.parseDocument(message,this);
		}catch(Exception e){
			e.printStackTrace();
		}
		textPane.getView().setBounds(0,0,400,400);
		
	}

	public void internalFrameClosed(InternalFrameEvent e) {
		//MyHacker.setMessageWindowOpen(false);
	}
	
}
