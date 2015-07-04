package GUI;


import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;


public class InventoryList extends JPanel{
	public final static int EQUIPMENT = 3;
	public final static int STORE = 13;
	public final static int HOME = 6;
	public final static ImageIcon PCI = ImageLoader.getImageIcon("images/pci.png");
	public final static ImageIcon FOLDER = ImageLoader.getImageIcon("images/folderBig.png");
	public final static ImageIcon TEXT = ImageLoader.getImageIcon("images/textfile.png");
	public final static ImageIcon FIREWALL = ImageLoader.getImageIcon("images/firewallHome.png");
	public final static ImageIcon SCRIPT = ImageLoader.getImageIcon("images/scriptHome.png");
	private int x=2,y=5,last=0,count=EQUIPMENT;
	private boolean paint=true;
	public InventoryList(){
		setLayout(null);
		
	}
	
	//public void setInventory(
	
	public void addInventory(Inventory inv){
		add(inv);
		inv.setBounds(x,y,47,42);
		if(last!=count){
			x+=50;
			last+=1;
		}else{
			x=2;
			y+=50;
			last=0;
		}
		setPreferredSize(new Dimension(82*count,y+30));
	}
	
	public void setPaint(boolean paint){
		this.paint=paint;
	}
	public void setCount(int count){
		this.count=count;
	}

	public void reset(){
		removeAll();
		x=2;
		y=5;
		last=0;
		repaint();
	}
	
	
	public void paintComponent(Graphics g){
		if(paint){
			try{
				Rectangle vis = getVisibleRect();
				File fi = ImageLoader.getFile("images/inventoryback.png");
				BufferedImage bg = ImageIO.read(fi.toURL());
				g.drawImage(bg, vis.x, vis.y, null);
			}catch(Exception e){}
		}
	}
}
