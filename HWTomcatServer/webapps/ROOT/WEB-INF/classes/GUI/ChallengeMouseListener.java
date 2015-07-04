package GUI;
/**
PortManagementMouseListener.java
this is the mouse listener for the port management window.
*/

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import Assignments.*;
import View.*;
import java.text.*;

public class ChallengeMouseListener implements MouseListener{
	//data
	private Hacker MyHacker;
	private int id;

	public ChallengeMouseListener(Hacker MyHacker,int id){
		this.MyHacker=MyHacker;
		this.id=id;
	}
	
	public void mouseEntered(MouseEvent e){

	}

	public void mouseExited(MouseEvent e){

	}

	public void mousePressed(MouseEvent e){
		
	}

	public void mouseReleased(MouseEvent e){
		
	}

	public void mouseClicked(MouseEvent e){
		//System.out.println("Clicked id "+id);
		
		String link="http://"+MyHacker.getView().getIP()+"/help/challenges.php?id="+id;
		ChallengeDetails CD = new ChallengeDetails(MyHacker,link);
		MyHacker.getPanel().add(CD);
		CD.setVisible(true);
		CD.moveToFront();		
	
	}
	
}

