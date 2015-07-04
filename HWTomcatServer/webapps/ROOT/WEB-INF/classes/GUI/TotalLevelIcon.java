package GUI;
/**

StatIcon.java

this represents a stat icon on the desktop.

*/

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;

public class TotalLevelIcon extends JPanel{
	//data
	private String stat,image;
	private Hacker MyHacker;
	private JDesktopPane mainPanel;
	private int level;
	private int x,y;
	private Color desktopColour;
	private JLabel textField;
	public TotalLevelIcon(Hacker MyHacker,JDesktopPane mainPanel,String stat,int level,int x,int y,Color desktopColour){
		this.MyHacker=MyHacker;
		this.mainPanel=mainPanel;
		this.stat=stat;
		this.x=x;
		this.y=y;
		this.level=level;
		this.setLayout(new BorderLayout());
		
		//StatMouseListener mL=new StatMouseListener(MyHacker,mainPanel,this);
		String text="LVL "+level;
		textField = new JLabel(text);
		//textField.setEditable(false);
		//textField.setBorder(null);
		textField.setBackground(Color.BLACK);
		//textField.setForeground(new Color(131,181,227));
		textField.setForeground(Color.WHITE);
		setBackground(Color.BLACK);
		//textField.setForeground(new Color(14,236,19));
		textField.setFont(new Font("dialog",Font.BOLD,14));
		
		this.add(textField,BorderLayout.CENTER);
		
		//this.addMouseListener(mL);
	}
	
	public void redraw(){
		level=0;
		level+=MyHacker.getStatsPanel().getMerchantingIcon().getLevel();
		level+=MyHacker.getStatsPanel().getAttackIcon().getLevel();
		level+=MyHacker.getStatsPanel().getWatchIcon().getLevel();
		level+=MyHacker.getStatsPanel().getScanIcon().getLevel();
		level+=MyHacker.getStatsPanel().getFireWallIcon().getLevel();
		level+=MyHacker.getStatsPanel().getHTTPIcon().getLevel();
		String text="LVL "+level;
		textField.setText(text);
		/*this.removeAll();
		
		mainPanel.remove(this);
		
		JLabel nameLabel=new JLabel(stat+":  ");
		this.add(nameLabel,BorderLayout.CENTER);
		nameLabel= new JLabel("Lvl "+level);
		this.add(nameLabel,BorderLayout.LINE_END);
		mainPanel.add(this);
		this.setBounds(x,y,this.getPreferredSize().width,this.getPreferredSize().height);*/
	}
	
	public int getLevel(){
		return(level);
	}
		
	public Point getPoint(){
		return(new Point(x,y));
	}
	
	public void setPoint(int x){
		this.x=x;
		//this.y=y;
	}
	
	
}
