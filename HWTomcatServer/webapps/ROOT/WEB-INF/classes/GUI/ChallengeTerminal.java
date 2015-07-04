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

public class ChallengeTerminal extends Application{
	private JDesktopPane mainPanel=null;
	private JTextArea textPane;
	public ChallengeTerminal(){
		this.setTitle("Terminal");
		this.setResizable(false);
		this.setMaximizable(false);
		this.setClosable(true);
		this.setIconifiable(true);
		this.addInternalFrameListener(this);
		setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
		setBounds(50,50,500,300);
		populate();
	}

	public void populate(){
		textPane = new JTextArea();
		Font f = new Font("Monoserif",Font.PLAIN,10);
		JScrollPane sp = new JScrollPane(textPane);
		add(sp);
		sp.setBounds(0,0,100,100);
		
	}
	
	public void addLine(String line){
		textPane.append(line);
	}
	

	public void internalFrameIconified(InternalFrameEvent e) {
		JDesktopIcon DI = getDesktopIcon();
		DI.setBackground(new Color(41,42,41));
		
		JButton b = (JButton)DI.getUI().getAccessibleChild(DI,0);
		JLabel l = (JLabel)DI.getUI().getAccessibleChild(DI,1);
		//b.setFocusPainted(false);
		b.setContentAreaFilled(false);
		b.setForeground(Color.WHITE);
		
		//b.setBorderPainted(false);
		
		DI.getUI().update(DI.getGraphics(),DI);
	    getDesktopIcon().setLocation(2,302);
	}

	public void internalFrameClosed(InternalFrameEvent e) {
		//MyHacker.setMessageWindowOpen(false);
	}

}
