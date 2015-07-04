package DBAdmin;

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.font.TextLayout;
import java.awt.font.FontRenderContext;
import util.*;
import java.io.*;
import java.awt.geom.*;
import java.awt.event.*;
import javax.imageio.*;
import java.util.*;
import javax.swing.border.*;


public class FunctionBorder extends AbstractBorder{
	private final int INSET = 4;
	private String text = "";
	
	public FunctionBorder(String text){
		this.text = text;
	}
	public Insets getBorderInsets(Component c){
		return(new Insets(INSET,INSET,INSET,INSET));
	}
	
	public Insets getBorderInsets(Component c,Insets insets){
		return(new Insets(INSET,INSET,INSET,INSET));
	}

	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height){
		y+=2;
		g.setColor(Color.black);
		g.drawLine(x,y,x,height-1);
		g.drawLine(x,y,x+10,y);
		//g.setFont(new Font("monospace",Font.PLAIN,12));
		Font f = g.getFont();
		FontMetrics metric = g.getFontMetrics();
		int fontWidth = metric.stringWidth(text);
		int size = f.getSize();
		//System.out.println(size);
		g.drawString(text,x+11,y+size/2+1);
		g.drawLine(x+fontWidth+12,y,width-1,y);
		g.drawLine(x,height-1,width-1,height-1);
		g.drawLine(width-1,y,width-1,height-1);
		//g.drawRect(x,y,width-1,height-1);
	
	
	}

}