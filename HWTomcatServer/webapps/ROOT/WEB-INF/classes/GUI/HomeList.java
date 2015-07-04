package GUI;


import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import java.util.*;
import Assignments.*;
import View.*;


public class HomeList extends JPanel{
	public final static int COUNT = 9;
	private int x=2,y=5,last=0;
	private ArrayList icons = new ArrayList();
	private Home MyHome;
	private Hacker MyHacker;
	public HomeList(Home MyHome,Hacker MyHacker){
		this.MyHome=MyHome;
		this.MyHacker=MyHacker;
		setLayout(null);
		
	}
	
	//public void setInventory(
	
	public void changeDirectory(String name){
		MyHome.changeDirectory(name);
	}
	
	public void addInventory(HomeIcon inv){
		icons.add(inv);
		add(inv);
		inv.setBounds(x,y,60,65);
		if(last!=(int)(getBounds().width/65)-1){
			x+=65;
			last+=1;
		}else{
			x=2;
			y+=70;
			last=0;
		}
		setPreferredSize(new Dimension(65*(int)(getBounds().width/65),y+75));
	}
	
	public void reset(){
		removeAll();
		icons = new ArrayList();
		x=2;
		y=5;
		last=0;
		repaint();
		System.gc();
	}
	
	public void resize(int x,int y){
		setBounds(0,0,x,y);
	}
	
	public void resetBackgrounds(){
		Object[] icons = this.icons.toArray();
		for(int i=0;i<icons.length;i++){
			((HomeIcon)icons[i]).setSelected(false);
			((HomeIcon)icons[i]).setBackground(null);
		}
	}
	
	public HomeIcon getSelected(){
		Object[] icons = this.icons.toArray();
		for(int i=0;i<icons.length;i++){
			if(((HomeIcon)icons[i]).isSelected())
				return (HomeIcon)icons[i];
		}
		return null;
	}
	
	public Home getHome(){
		return MyHome;
	}
	

}

