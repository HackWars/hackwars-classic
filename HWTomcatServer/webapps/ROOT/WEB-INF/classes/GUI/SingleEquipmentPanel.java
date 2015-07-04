package GUI;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import Assignments.*;
import View.*;
import java.lang.*;
import Game.*;
import java.text.*;
import java.io.*;
import javax.imageio.*;
import java.awt.image.*;
import java.util.*;
import net.miginfocom.swing.*;


public class SingleEquipmentPanel extends JPanel {

	public static final BufferedImage LABEL_BACK = ImageLoader.getImage("images/equipLabel.png");
	public static final BufferedImage EQUIP_BACK = ImageLoader.getImage("images/equipBack.png");
	private String name = "";
	private Inventory bu = null;
	public SingleEquipmentPanel(String name,Hacker MyHacker,EquipmentPopUp EPU){
		this.name = name;
		setBackground(Color.white);
		setPreferredSize(new Dimension(100,100));
		setLayout(new MigLayout());
		if(name.equals("Hard Drive")){
			bu = new Inventory(MyHacker,EPU,Equipment.hdNames[MyHacker.getHDType()],Equipment.hdValues[MyHacker.getHDType()],"",Inventory.VALUE,Inventory.HD,"",-1,"",new int[]{0,0,0,0,0});
		}
		else if(name.equals("CPU")){
			bu = new Inventory(MyHacker,EPU,Equipment.cpuNames[MyHacker.getCPUType()],Equipment.cpuValues[MyHacker.getCPUType()],"",Inventory.VALUE,Inventory.CPU,"",-1,"",new int[]{0,0,0,0,0});
		}
		else if(name.equals("Memory")){
			bu = new Inventory(MyHacker,EPU,Equipment.memNames[MyHacker.getMemoryType()],Equipment.memPortValues[MyHacker.getMemoryType()],Equipment.memWatchValues[MyHacker.getMemoryType()],Inventory.VALUE,Inventory.MEMORY,"",-1,"",new int[]{0,0,0,0,0});
		}
		if(bu!=null){
			add(bu,"pos (visual.x2/2 - pref/2) (visual.y2/2 - pref.y2/2)");
		}
	
	}
	
	public void paintComponent(Graphics g){
		g.setColor(Color.black);
		g.fillRect(0,0,getWidth(),getHeight());
		int imageWidth = LABEL_BACK.getWidth();
		int imageHeight = LABEL_BACK.getHeight();
		int panelWidth = getWidth();
		int backWidth = EQUIP_BACK.getWidth();
		int backHeight = EQUIP_BACK.getHeight();
		
		for(int i=0;i<panelWidth/imageWidth+1;i++){
			g.drawImage(LABEL_BACK,i*imageWidth,0,imageWidth,imageHeight,null);
		}
		for(int i=0;i<panelWidth/backWidth+1;i++){
			g.drawImage(EQUIP_BACK,i*backWidth,imageHeight+1,backWidth,backHeight,null);
		
		}
		g.setColor(Color.white);
		g.drawString(name,0,25);
	}
	
	public void reset(Inventory inv){
		remove(inv);
		bu = null;
	
	}
	
	
	public void setInventory(Inventory inv){
		//System.out.println("Adding Inventory Card");
		if(bu!=null){
			reset(bu);
		}
		if(bu==null){
			inv.setEquipmentPanel(this);
			add(inv,"pos (visual.x2/2 - pref/2) (visual.y2/2 - pref.y2/2)");
		}
		bu=inv;
		validate();
	}
}