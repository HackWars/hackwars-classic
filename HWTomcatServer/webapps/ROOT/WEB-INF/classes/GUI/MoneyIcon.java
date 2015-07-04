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

public class MoneyIcon extends JPanel implements MouseListener{
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
	private ToolTip toolTip;
	public MoneyIcon(Hacker MyHacker,String text,String image,int x,int y,Color back,String name){
		this.MyHacker=MyHacker;
		this.image=image;
		this.x=x;
		this.y=y;
		this.text = text;
		//setPreferredSize(new Dimension(125,25));
		setBackground(StatsPanel.BACKGROUND);
		setBorder(StatsPanel.BORDER);
		this.setLayout(new GridBagLayout());
		icon = ImageLoader.getImage(image);
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(0,0,0,0);
		c.gridx=0;
		c.fill=GridBagConstraints.NONE;
		IconPanel iconPanel = new IconPanel(icon);
		add(iconPanel,c);
		c.gridx=1;
		JPanel panel = new JPanel();
		panel.setBackground(back);
		panel.setLayout(null);
		
		label = new JLabel(text);
		panel.setPreferredSize(new Dimension(110,25));
		label.setPreferredSize(new Dimension(110,25));
		label.setHorizontalAlignment(SwingConstants.RIGHT);
		label.setVerticalAlignment(SwingConstants.CENTER);
		label.setBounds(0,0,105,25);
		label.setForeground(Color.white);
		label.setFont(StatsPanel.TEXT);
		panel.add(label);
		add(panel,c);
		
		String ttt = "<html><h4>"+name+"</h4></html>";
		//setToolTipText(ttt);
		toolTip = new ToolTip(ttt,MyHacker);
		//MyHacker.getPanel().add(toolTip);
		addMouseListener(this);

	}
	
	/*public float getAmount(){
		return(amount);
	}*/
	
	public void setText(String text){
		this.text=text;
		label.setText(text);
		
		
	}
	
	public void paintComponent(Graphics g){
		g.setColor(StatsPanel.STAT_BORDER);
		/*g.fillRect(0,0,getWidth(),5);
		g.fillRect(0,0,5,getHeight());
		g.fillRect(25,0,5,getHeight());
		g.fillRect(0,getHeight()-5,getWidth(),5);*/
	}
		
	public void mouseEntered(MouseEvent e){
		toolTip.show(e.getX()+getLocationOnScreen().x-MyHacker.getPanel().getLocationOnScreen().x,e.getY()+getLocationOnScreen().y-MyHacker.getPanel().getLocationOnScreen().y);
	}

	public void mouseExited(MouseEvent e){
		toolTip.setVisible(false);
	}

	public void mousePressed(MouseEvent e){

	}

	public void mouseReleased(MouseEvent e){

	}

	public void mouseClicked(MouseEvent e){

	}
	
}
