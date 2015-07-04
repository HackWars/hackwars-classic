package GUI;


import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.text.*;
import Game.*;

public class HomePopUp extends JPanel{
	private final int FILE=0;
	private final int EQUIPMENT=1;
	private final int DESC_LENGTH=60;
	private String name="",a1="",a2="";
	private String type="";
	private String description="";
	private Color textColor=null;
	private float price=-1.0f;
	private int quantity=0;
	private int show=FILE;
	private String maker="";
	private String fileName="";
	public void setInfo(String name,String type,String description,Float price,int quantity,String maker){
		setOpaque(false);
		this.name=name;
		this.type=type;
		this.description=description;
		this.price=price;
		this.quantity=quantity;
		this.maker=maker;
		show=FILE;
		repaint();
		//setVisible(true);
		
	}
	
	public void setInfo(String name,String type,String a1,String a2,Color textColor,float price,int quantity,String maker,String fileName){
		this.name=name;
		this.type=type;
		this.a1=a1;
		this.a2=a2;
		this.textColor=textColor;
		this.price=price;
		this.quantity=quantity;
		this.maker=maker;
		this.fileName=fileName;
		show=EQUIPMENT;
		repaint();
		//setVisible(true);
		
	}
	
	public void paintComponent(Graphics g){
		if(show==FILE){
			int dy=44;
			if(description.length()>DESC_LENGTH){
				for(int i=0;i<description.length()/DESC_LENGTH+1;i++){
					dy+=12;
				}
			}
			g.setColor(new Color(0.0f,0.0f,0.0f,0.80f));
			g.fillRect(0,0,400,dy+70);
			g.setFont(new Font("dialog",Font.BOLD,14));
			g.setColor(Inventory.CONSUMER);
			g.drawString(name,20,15);
			
			g.setColor(Color.white);
			g.setFont(new Font("dialog",Font.BOLD,12));
			g.drawString(type,20,30);
			
			g.setColor(Inventory.CONSUMER);
			String showDesc="";
			dy=44;
			if(description.length()>DESC_LENGTH){
				//System.out.println("Description too long");
				for(int i=0;i<description.length()/DESC_LENGTH+1;i++){
					if(description.length()>(i+1)*DESC_LENGTH){
						showDesc=description.substring(i*DESC_LENGTH,(i+1)*DESC_LENGTH);
						g.drawString(showDesc,20,dy);
					}
					else{
						showDesc=description.substring(i*DESC_LENGTH,description.length());
						g.drawString(showDesc,20,dy);
					}
					dy+=12;
				}
			}
			else
				g.drawString(description,20,44);
			
			//g.drawString(a2,60,58);
			
			g.setColor(Color.white);
			NumberFormat nf = NumberFormat.getCurrencyInstance();
			g.drawString("Price: "+nf.format(price),20,dy+26);
			g.drawString("Quantity: "+quantity,20,dy+40);
			g.drawString("Maker: "+maker,20,dy+58);
			setBounds(getBounds().x,getBounds().y,getBounds().width,dy+70);
		}
		else if(show==EQUIPMENT){
			g.setColor(new Color(0.0f,0.0f,0.0f,0.80f));
			g.fillRect(0,0,400,130);
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
			NumberFormat nf = NumberFormat.getCurrencyInstance();
			g.drawString("Price: "+nf.format(price),20,70);
			g.drawString("Quantity: "+quantity,20,84);
			g.drawString("Maker: "+maker,20,98);
			g.drawString("Filename: "+fileName,20,112);
			setBounds(getBounds().x,getBounds().y,getBounds().width,130);
		}
	}
	
}
