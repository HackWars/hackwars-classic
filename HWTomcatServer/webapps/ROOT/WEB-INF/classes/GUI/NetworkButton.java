package GUI;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import Assignments.*;
import View.*;
import java.awt.image.*;
import java.awt.geom.*; 
import java.awt.event.*;
import javax.swing.event.*;

public class NetworkButton extends JButton implements ActionListener{
	
	public Color baseColor;
	public String interiorFile;   
	public int diameter;
	public int width;
	public int height;
	public int x;
	public int y;
	public Hacker hacker;
	public String name;
	//nonchanging colors
	private BufferedImage interior;

	public NetworkButton(String name, float xf, float yf, float df, Color c, String pic,Hacker hacker,float multi){
		this.setOpaque(false);
		interiorFile = pic;
		x = (int)((xf / 8) * multi);
		y= (int)((yf / 8) * multi);
		this.x=(int)((xf / 8) * multi);
		this.y=(int)((yf / 8) * multi);
		diameter = (int)((df / 16) * multi);
		this.name = name;
		this.width = diameter;
		this.height = diameter;

		setBounds(this.x,this.y,diameter,diameter);
		this.hacker=hacker;
		setBorderPainted(false);
		setContentAreaFilled(false);
		addActionListener(this);
		this.setToolTipText(name);
		this.baseColor = c;
		
	}
	

	public void paintComponent(Graphics g){
		Graphics2D g2d = (Graphics2D)g;
		
		RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		g2d.setRenderingHints(rh);
		
	    Color rimColor = new Color(50, 50, 50);
		Color c1 = new Color(150,150,150, 50); //light gray
		Color c2 = new Color(230,230,230, 50); //lighter gray

		//make it fit in the space
		int diameter = (int)(this.width * 0.8);
		int posx = (int)(this.width * 0.1);
		int posy = (int)(this.height * 0.1);
		
		Ellipse2D.Double base = new Ellipse2D.Double(posx, posy, diameter, diameter);
		Ellipse2D.Double rim = new Ellipse2D.Double(posy, posx, diameter, diameter);


		//draw base and interior
	    g2d.setPaint(this.baseColor);
	    g2d.fill(base);
			
	    //draw rim
	    g2d.setPaint(Color.black);
	    g2d.setStroke(new BasicStroke((int)(this.width * 0.1)));
	    g2d.draw(base);
	    g2d.setPaint(rimColor);
	    g2d.setStroke(new BasicStroke((int)(this.width * 0.05)));
	    g2d.setPaint(this.baseColor);
	    g2d.setStroke(new BasicStroke(1));
	    g2d.draw(base);
		
		//draw interior bitmap

	    BufferedImage interior = ImageLoader.getImage(interiorFile);
	    Image sInterior = interior.getScaledInstance(diameter, diameter, Image.SCALE_FAST);
	    
	    if (interior.getWidth() > diameter) {
	    
	    g2d.drawImage(sInterior, (this.width / 2) - (diameter / 2), (this.height / 2) - (diameter / 2), new Color(255, 255, 255, 0), null);
	    }
	    else {
	    	g2d.drawImage(interior, (this.width / 2) - (interior.getWidth() / 2), (this.height / 2) - (interior.getHeight() / 2), new Color(255, 255, 255, 0), null);
	    }
	    
	    //draw dome
		g2d.setPaint( new GradientPaint(0, (int)posy, c1, 0, (int) diameter, c2, false) );
		g2d.fill( new Ellipse2D.Double(posx,posy,diameter,diameter)); //draw a filled circle
		g2d.setPaint(c1);// change the paint color
		g2d.draw( new Ellipse2D.Double(posx,posy,diameter,diameter)); //draw the outline of the circle
		
		//start drawing top white ellipse
		double tempX = posx + diameter*0.12;
		double tempY = posy + diameter*0.06;
		double tempYDiam = diameter * 0.58;
		double tempXDiam = diameter * 0.76;
		
		//start drawing bottom white ellipse
		Color opaqueWhite = new Color(255,255,255,75); //here is where you control how transparent the bottom of the second white ellipse is by changing the alpha value

		g2d.setPaint(opaqueWhite);
		g2d.fill( new Ellipse2D.Double(tempX,tempY,tempXDiam, tempYDiam));

		tempX = posx + diameter*0.15;
		tempY = posy + diameter*0.17;
		tempYDiam = diameter*0.70;
		tempXDiam = diameter*0.70;
		
		g2d.setPaint( new GradientPaint(0, (int)(tempY+tempYDiam*0.80), opaqueWhite, 0, (int) (tempY + tempYDiam), opaqueWhite, false) );
		
		g2d.fill( new Ellipse2D.Double(tempX,tempY,tempXDiam, tempYDiam));
	    
	}
	
	public void actionPerformed(ActionEvent e){
		
		Object[] o = new Object[]{hacker.getEncryptedIP(),name};
		View MyView = hacker.getView();
		MyView.setFunction("changenetwork");
		MyView.addFunctionCall(new RemoteFunctionCall(0,"changenetwork",o));
	}
}

