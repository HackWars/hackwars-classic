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
import java.net.*;

public class ChallengeDetails extends Application{
	private HtmlHandler textPane;
	private Hacker MyHacker;
	private String link;
	public ChallengeDetails(Hacker MyHacker,String link){
		this.MyHacker=MyHacker;
		this.link=link;
		this.setTitle("Challenge Details");
		this.setResizable(false);
		this.setMaximizable(false);
		this.setClosable(true);
		this.setIconifiable(true);
		this.addInternalFrameListener(this);
		setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
		setBounds(50,50,600,500);
		setLayout(null);
		populate();
	}

	public void populate(){
		textPane = new HtmlHandler();
		//textPane.createView();
		add(textPane.getView());
		try{
				textPane.parseDocument(new URL(link),this);
		}catch(Exception e){
			e.printStackTrace();
		}
		textPane.getView().setBounds(10,10,580,450);
		
	}

	public void internalFrameIconified(InternalFrameEvent e) {
	    getDesktopIcon().setLocation(2,302);
	}

	public void internalFrameClosed(InternalFrameEvent e) {
		//MyHacker.setMessageWindowOpen(false);
	}
	
}
