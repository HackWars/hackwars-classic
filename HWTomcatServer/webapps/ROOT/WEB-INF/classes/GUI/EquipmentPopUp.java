package GUI;


import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.text.*;


public class EquipmentPopUp extends JPanel{

	private String name="";
	private String type="";
	private String a1="",a2="";
	private Color textColor=null;
	private float price=-1.0f;
	private String durability;
	private int[] cost;
	
	public void setInfo(String name,String type,String a1,String a2,Color textColor,String durability,int[] cost){
		this.name=name;
		this.type=type;
		this.a1=a1;
		this.a2=a2;
		this.textColor=textColor;
		this.durability=durability;
		this.cost=cost;
	}
	
	public void setPrice(float price){
		this.price=price;
	}
	
	public void paintComponent(Graphics g){
		g.setColor(new Color(0.0f,0.0f,0.0f,0.80f));
		g.fillRect(0,0,400,100);
		g.setFont(new Font("dialog",Font.BOLD,14));
		g.setColor(textColor);
		g.drawString(name,20,15);
		
		g.setColor(Color.white);
		g.setFont(new Font("dialog",Font.BOLD,12));
		g.drawString(type+": ",20,30);
		
		g.setColor(Inventory.CONSUMER);
		g.drawString(a1,60,44);
		g.drawString(a2,60,58);
		
		g.setColor(Color.white);
		g.drawString(durability,20,72);
		if(cost[0]>0){
			g.drawString("Repair Cost: ",20,86);
			g.drawString(cost[0]+"x",95,86);
			g.drawImage(ImageLoader.DUCT_TAPE,115,70,null);
			if(cost[1]>0){
				g.drawString(cost[1]+"x",140,86);
				g.drawImage(ImageLoader.GERMANIUM,160,70,null);
			}
			if(cost[2]>0){
				g.drawString(cost[2]+"x",195,86);
				g.drawImage(ImageLoader.SILICON,215,70,null);
			}
			if(cost[3]>0){
				g.drawString(cost[3]+"x",250,86);
				g.drawImage(ImageLoader.YBCO,270,70,null);
			}
			if(cost[4]>0){
				g.drawString(cost[4]+"x",305,86);
				g.drawImage(ImageLoader.PLUTONIUM,325,70,null);
			}
		}
		
		if(price>0){
			g.setColor(Color.white);
			NumberFormat nf = NumberFormat.getCurrencyInstance();
			g.drawString("Price: "+nf.format(price),20,100);
		}
	}
}
