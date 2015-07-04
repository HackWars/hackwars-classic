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
import java.awt.image.*;
import java.text.*;

public class MeterIcon extends JPanel{
	//data
	private String stat,image;
	private Hacker MyHacker;
	private JDesktopPane mainPanel;
	private float xp;
	private int x,y;
	private Color desktopColour;
	private int xpTable[];
	private JLabel textField;
	private BufferedImage icon;
	private BarPanel barPanel;
	private LevelPanel levelPanel;
	private String text="$0.00";
	private JLabel label;
	private int amount=0;
	private JButton billion,hundredMillion,tenMillion,million,hundredThousand,tenThousand,thousand,hundred,ten,one;
	public MeterIcon(Hacker MyHacker,int amount,String image,int x,int y,Color back,String name){
		this.MyHacker=MyHacker;
		this.image=image;
		this.x=x;
		this.y=y;
		this.amount=amount;
		//setPreferredSize(new Dimension(125,25));
		setBackground(StatsPanel.BACKGROUND);
		this.setLayout(new GridBagLayout());
		icon = ImageLoader.getImage(image);
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(0,0,0,5);
		c.gridx=0;
		c.fill=GridBagConstraints.NONE;
		IconPanel iconPanel = new IconPanel(icon);
		add(iconPanel,c);
		JPanel panel = new JPanel();
		panel.setBackground(back);
		label = new JLabel(""+amount);
		label.setForeground(Color.white);
		label.setFont(StatsPanel.TEXT);
		label.setHorizontalAlignment(SwingConstants.RIGHT);
		label.setVerticalAlignment(SwingConstants.CENTER);
		label.setPreferredSize(new Dimension(110,25));
		panel.add(label);
		c.gridx=1;
		add(panel,c);
		panel.setPreferredSize(new Dimension(120,25));
		String ttt = "<html><h4>"+name+"</h4></html>";
		setToolTipText(ttt);
		
		/*hundredThousand = new JButton(ImageLoader.getImageIcon("images/0.png"));
		hundredThousand.setBorderPainted(false);
		hundredThousand.setContentAreaFilled(false);
		hundredThousand.setMargin(new Insets(0,0,0,0));
		hundredThousand.setHorizontalAlignment(SwingConstants.LEFT);
		hundredThousand.setVerticalAlignment(SwingConstants.TOP);
		c.gridx=1;
		add(hundredThousand,c);
		tenThousand = new JButton(ImageLoader.getImageIcon("images/0.png"));
		tenThousand.setBorderPainted(false);
		tenThousand.setContentAreaFilled(false);
		tenThousand.setMargin(new Insets(0,0,0,0));
		tenThousand.setHorizontalAlignment(SwingConstants.LEFT);
		tenThousand.setVerticalAlignment(SwingConstants.TOP);
		c.gridx=2;
		add(tenThousand,c);
		thousand = new JButton(ImageLoader.getImageIcon("images/0.png"));
		c.gridx=3;
		add(thousand,c);
		hundred = new JButton(ImageLoader.getImageIcon("images/0.png"));
		c.gridx=4;
		add(hundred,c);
		ten = new JButton(ImageLoader.getImageIcon("images/0.png"));
		c.gridx=5;
		add(ten,c);
		one = new JButton(ImageLoader.getImageIcon("images/0.png"));
		c.gridx=6;
		add(one,c);
		
		
		
		
		
		thousand.setBorderPainted(false);
		thousand.setContentAreaFilled(false);
		thousand.setMargin(new Insets(0,0,0,0));
		thousand.setHorizontalAlignment(SwingConstants.LEFT);
		thousand.setVerticalAlignment(SwingConstants.TOP);
		
		hundred.setBorderPainted(false);
		hundred.setContentAreaFilled(false);
		hundred.setMargin(new Insets(0,0,0,0));
		hundred.setHorizontalAlignment(SwingConstants.LEFT);
		hundred.setVerticalAlignment(SwingConstants.TOP);
		
		ten.setBorderPainted(false);
		ten.setContentAreaFilled(false);
		ten.setMargin(new Insets(0,0,0,0));
		ten.setHorizontalAlignment(SwingConstants.LEFT);
		ten.setVerticalAlignment(SwingConstants.TOP);
	
		one.setBorderPainted(false);
		one.setContentAreaFilled(false);
		one.setMargin(new Insets(0,0,0,0));
		one.setHorizontalAlignment(SwingConstants.LEFT);
		one.setVerticalAlignment(SwingConstants.TOP);*/
		
		

	}
	
	public void redrawMeter(){
		int count=amount;
		/*int billionc = count/1000000000;
		billionh.setIcon(ImageLoader.getImageIcon("images/"+billionc+".png"));
		count-=billionc*100000000;
		int hundredMillionc = count/100000000;
		hundredMillion.setIcon(ImageLoader.getImageIcon("images/"+hundredMillionc+".png"));
		count-=hundredMillionc*100000000;
		int tenMillionc = count/10000000;
		tenMillionh.setIcon(ImageLoader.getImageIcon("images/"+tenMillionc+".png"));
		count-=tenMillionc*10000000;
		int millionc = count/1000000;
		millionh.setIcon(ImageLoader.getImageIcon("images/"+millionc+".png"));
		count-=millionc*1000000;*/
		int hundredThousandc = count/100000;
		hundredThousand.setIcon(ImageLoader.getImageIcon("images/"+hundredThousandc+".png"));
		count-=hundredThousandc*100000;
		int tenThousandc = count/10000;
		tenThousand.setIcon(ImageLoader.getImageIcon("images/"+tenThousandc+".png"));
		count-=tenThousandc*10000;
		int thousandc = count/1000;
		thousand.setIcon(ImageLoader.getImageIcon("images/"+thousandc+".png"));
		count-=thousandc*1000;
		int hundredc = count/100;
		hundred.setIcon(ImageLoader.getImageIcon("images/"+hundredc+".png"));
		count-=hundredc*100;
		int tenc = count/10;
		ten.setIcon(ImageLoader.getImageIcon("images/"+tenc+".png"));
		count-=tenc*10;
		one.setIcon(ImageLoader.getImageIcon("images/"+count+".png"));
	}
	
	public int getAmount(){
		return(amount);
	}
	
	public void setAmount(int amount){
		this.amount=amount;
		NumberFormat nf = NumberFormat.getNumberInstance();
		label.setText(nf.format(amount));
		//redrawMeter();
		//label.setText(text);
		
		
	}
	
}
