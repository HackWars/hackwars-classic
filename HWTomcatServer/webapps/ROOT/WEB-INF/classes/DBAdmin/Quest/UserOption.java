package DBAdmin.Quest;

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import util.*;
import java.io.*;
import java.awt.geom.*;
import java.awt.event.*;
import javax.imageio.*;
import java.util.*;
import javax.swing.text.*;


public class UserOption extends JPanel implements MouseListener{

	private String dialog="";
	private int linkTo=-1;
	private DialogWidget widget;
	private JLabel dialogLabel,linkToLabel;
	
	
	public UserOption(DialogWidget widget,String dialog,int linkTo){
		this.widget=widget;
		this.dialog=dialog;
		this.linkTo=linkTo;
		populate();
		addMouseListener(this);
	}


	public void populate(){
		setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.8;
		c.insets = new Insets(0,0,2,10);
		dialogLabel = new JLabel(dialog);
		
		add(dialogLabel,c);
		
		c.gridx = 1;
		c.weightx = 0.2;
		c.anchor = GridBagConstraints.FIRST_LINE_END;
		linkToLabel = new JLabel(widget.getDialog().getTitle(linkTo));
		linkToLabel.setHorizontalAlignment(JLabel.RIGHT);
		add(linkToLabel,c);
		
		
	}
	
	public String getDialog(){
		return(dialog);
	}
	
	public int getLinkTo(){
		return(linkTo);
	}
	
	public void setDialog(String dialog){
		this.dialog=dialog;
		dialogLabel.setText(dialog);
	}
	
	public void setLinkTo(int linkTo){
		this.linkTo=linkTo;
		linkToLabel.setText(widget.getDialog().getTitle(linkTo));
		DialogBack back = widget.getDialog().getBack();
		back.setCurrentDialog(back.getCurrentIndex());
	}
	
	public void mouseClicked(MouseEvent e){
		if(e.getClickCount()==2){
			UserOptionDialog UOD = new UserOptionDialog(widget,this,widget.getDialog().getTitles());
		}
	}
	
	public void mouseEntered(MouseEvent e){
		
	}
	
	public void mousePressed(MouseEvent e){
		
	}
	
	public void mouseReleased(MouseEvent e){
		
	}
	
	public void mouseExited(MouseEvent e){
		
	}

}
