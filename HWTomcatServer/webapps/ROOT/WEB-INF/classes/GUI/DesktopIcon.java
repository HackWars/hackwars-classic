package GUI;
/**

DesktopIcon.java

this represents and icon on the desktop.

*/

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;

public class DesktopIcon extends JPanel{
	//data
	public static final int USER=0;
	public static final int SYSTEM=1;
	private String function,name,image;
	private int x,y;
	private int type;
	
	public DesktopIcon(String function,String name,int x,int y,String image,Color c,Hacker MyHacker,int type,JDesktopPane panel){
		this.function=function;
		this.name=name;
		this.x=x;
		this.y=y;
		this.image=image;
		this.type=type;
		this.setLayout(new BorderLayout());
		JButton imageIcon= new JButton(new ImageIcon(image));
		imageIcon.setBorderPainted(false);
		imageIcon.setContentAreaFilled(false);
		imageIcon.setOpaque(false);
		imageIcon.setDisabledIcon(new ImageIcon(image));
		imageIcon.setEnabled(false);
		MainMouseListener mL=new MainMouseListener(MyHacker.getPopUp(),true,MyHacker.getIconList(),MyHacker,panel);
		imageIcon.addMouseListener(mL);
		this.add(imageIcon,BorderLayout.PAGE_START);
		JLabel nameLabel=new JLabel(name);
		this.add(nameLabel,BorderLayout.PAGE_END);
		this.setBackground(c);
		this.addMouseListener(mL);
	}
	
	public Point getPoint(){
		return(new Point(x,y));
	}
	
	public String getName(){
		return(name);
	}
	
	public int getType(){
		return(type);
	}

}
