package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import util.*;
import java.io.*;
import java.awt.geom.*;
import java.awt.event.*;
import javax.imageio.*;
import java.util.*;
import javax.swing.border.*;


public class LeftRoundedBorder extends AbstractBorder{
	private final int INSET = 4;
	private final BufferedImage icon = ImageLoader.getImage("images/new.png");
	private boolean showIcon=true;

	public LeftRoundedBorder(boolean showIcon){
		this.showIcon=showIcon;
	}
	
	public Insets getBorderInsets(Component c){
		return(new Insets(INSET,INSET*2,INSET,INSET));
	}
	
	public Insets getBorderInsets(Component c,Insets insets){
		return(new Insets(INSET,INSET*2,INSET,INSET));
	}

	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height){
		//y+=2;
		Graphics2D graphics = (Graphics2D)g;
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY );
		graphics.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_DISABLE);
		graphics.setColor(Color.black);
		
		/*g.drawLine(x,y,x,height-1);
		g.drawLine(x,y,x+10,y);
		//g.setFont(new Font("monospace",Font.PLAIN,10));
		Font f = g.getFont();
		int size = f.getSize();
		//System.out.println(size);
		g.drawString("Parameters",x+10,y+size/2+1);
		g.drawLine(x+6*size,y,width-1,y);
		g.drawLine(x,height-1,width-1,height-1);*/
		graphics.setColor(Color.white);
		graphics.fillRect(x+5,y,width-1,height-1);
		graphics.fillArc(x,y,10,height-1,90,180);
		graphics.setColor(Color.black);
		graphics.drawLine(width-1,y,width-1,height-1);
		graphics.drawLine(x+5,y,width-1,y);
		graphics.drawLine(x+5,height-1,width-1,height-1);
		graphics.drawArc(x,y,10,height-1,90,180);
		if(showIcon){
			graphics.drawLine(x+20,y,x+20,height-1);
			graphics.drawImage(icon,x+4,y+3,null);
		}
		
		
		//g.drawRect(x,y,width-1,height-1);
	
	
	}

}