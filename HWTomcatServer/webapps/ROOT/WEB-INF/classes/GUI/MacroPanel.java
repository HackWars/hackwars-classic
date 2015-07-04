package GUI;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

public class MacroPanel extends JPanel{
	private BufferedImage image;
	private int h;
	public void setImage(BufferedImage image,int h){
		this.image=image;
		this.h=h;
		repaint();
	}
	
	public void paintComponent(Graphics g){		
		int x = (getWidth()-image.getWidth())/2;
		int y = h;
		g.drawImage(image, x,y,null);
			
	}

}
