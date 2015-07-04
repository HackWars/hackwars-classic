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
import java.awt.image.*;

public class AttackAnimator extends JPanel{
	private final int DAMAGE_START = 100;
	private final int SPEED = 10;
	private final Color NORMAL_DAMAGE = Color.black;
	private final Color FIREWALL_DAMAGE = Color.white;
	private final BufferedImage ARROW = ImageLoader.getImage("images/arrow.jpg");
	private final BufferedImage FIREWALL = ImageLoader.getImage("images/firewall.png");
	private final BufferedImage COMPUTER = ImageLoader.getImage("images/HackerIcon.png");
	private int counter = 0;
	private String value = "";
	private DecimalFormat format = new DecimalFormat("#.##");
	private boolean firewall = false;
	public AttackAnimator(){
		setPreferredSize(new Dimension(300,300));
	
	}
	public void paintComponent(Graphics g){
		g.setColor(Color.white);
		int width = getWidth();
		int height = getHeight();
		g.fillRect(0,0,width,height);
		g.drawImage(ARROW,width/2-ARROW.getWidth()/2,height/2-ARROW.getHeight()/2,ARROW.getWidth(),ARROW.getHeight(),this);
		g.drawImage(COMPUTER,10,height/2-COMPUTER.getHeight()/2,COMPUTER.getWidth(),COMPUTER.getHeight(),this);
		g.drawImage(COMPUTER,width-COMPUTER.getWidth()-10,height/2-COMPUTER.getHeight()/2,COMPUTER.getWidth(),COMPUTER.getHeight(),this);
		g.drawImage(FIREWALL,COMPUTER.getWidth()+20,height/2-FIREWALL.getHeight()/2,FIREWALL.getWidth(),FIREWALL.getHeight(),this);
		g.drawImage(FIREWALL,width-FIREWALL.getWidth()-COMPUTER.getWidth()-20,height/2-FIREWALL.getHeight()/2,FIREWALL.getWidth(),FIREWALL.getHeight(),this);
		if(!firewall){
			g.setColor(NORMAL_DAMAGE);
		}
		else{
			g.setColor(FIREWALL_DAMAGE);
		}
		//System.out.println("Drawing ["+value+"] to ("+getWidth()/2+","+(getHeight()-20-counter*2)+")");
		g.drawString(value,DAMAGE_START+counter*SPEED,getHeight()/2);
		
	
	}
	
	public void increaseCounter(){
		counter++;
		repaint();
		//System.out.println("Counter: "+counter);
	}
	
	public void damage(float damage,boolean firewall){
		counter = 0;
		this.firewall = firewall;
		value = format.format(damage);
		repaint();
	}


}
