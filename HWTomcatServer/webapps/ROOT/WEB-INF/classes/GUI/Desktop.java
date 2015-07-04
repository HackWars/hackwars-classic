package GUI;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

public class Desktop extends JDesktopPane{
	private BufferedImage map,logo;
	public void setMap(BufferedImage map){
		this.map=map;
	}
	
	public void setLogo(BufferedImage logo){
		this.logo=logo;
	}


	public void paintComponent(Graphics g){
		int x = (getWidth()-logo.getWidth())/2-100;
		int y = (getHeight()-logo.getHeight())/2-50;
		g.drawImage(logo, x,y,null);
		//g.drawImage(map, 0, 1, null);
	}

}
