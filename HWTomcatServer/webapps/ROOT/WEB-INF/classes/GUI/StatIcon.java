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

public class StatIcon extends JPanel implements MouseListener{
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
	private int maxLevel;
	private String name;
	private ToolTip toolTip;
	public StatIcon(Hacker MyHacker,float xp,String image,int x,int y,int maxLevel,String name){
		this.MyHacker=MyHacker;
		//this.mainPanel=mainPanel;
		this.stat=stat;
		this.xp=xp;
		this.image=image;
		this.maxLevel=maxLevel;
		this.name=name;
		setBackground(StatsPanel.BACKGROUND);
		setBorder(StatsPanel.BORDER);
		xpTable = MyHacker.getXPTable();
		this.setLayout(new GridBagLayout());
		icon = ImageLoader.getImage(image);
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(0,0,0,0);
		c.gridx=0;
		c.fill=GridBagConstraints.NONE;
		IconPanel iconPanel = new IconPanel(icon);
		add(iconPanel,c);
		int i=8;
		if(maxLevel==100)
			i=getLevel();
		int[] xpTable = MyHacker.getXPTable();
		float fromXP=0.0f;
		float toXP;
		if(maxLevel==100){
			if(i!=1){
				fromXP = xpTable[i-2];
				toXP = xpTable[i-1];
			}
			else{
				fromXP=0;
				toXP=83;
			}
		}
		else{
			toXP=0;
		}
		//System.out.println(fromXP+"  "+xp+"  "+toXP);
		float percent = (xp-fromXP)/(toXP-fromXP)*100.0f;
		barPanel = new BarPanel(i,percent,maxLevel);
		c.gridx=1;
		c.gridwidth=4;
		add(barPanel,c);
		
		c.gridx=5;
		levelPanel = new LevelPanel(i);
		levelPanel.setBackground(StatsPanel.STAT_BACKGROUND);
		add(levelPanel,c);
		String text =""+i;
		NumberFormat nf=NumberFormat.getNumberInstance();
		String ttt = "<html><h4>"+name+"</h4>";
		if(!name.equals("Total Level")){
			ttt +="<b>Experience</b>:   "+(nf.format((int)getXP()));
			ttt += "<br /><b>To Next Level</b>:   "+nf.format(getToNextLevel());
		}
		ttt +="</html>";
		toolTip = new ToolTip(ttt,MyHacker);//setToolTipText(ttt);
		//MyHacker.getPanel().add(toolTip);
		toolTip.setBounds(x,y,toolTip.getPreferredSize().width,toolTip.getPreferredSize().height);
		//MyHacker.getPanel().setComponentZOrder(toolTip,0);
		addMouseListener(this);
		//setPreferredSize(new Dimension(165,50));
	
	}
	public void redraw(){
		//int i=getLevel();
		if(maxLevel==100)
			MyHacker.getStatsPanel().getTotalLevelIcon().redraw();
		else{
			levelPanel.setLevel(MyHacker.getStatsPanel().getTotalLevel());
			barPanel.setLevel(MyHacker.getStatsPanel().getTotalLevel());
		}
	}
	public Point getPoint(){
		return(new Point(x,y));
	}
	
	public String getStat(){
		return(stat);
	}
	
	public float getXP(){
		return(xp);
	}
	
	public void setXP(float xp){
		if(xp>this.xp){
			this.xp=xp;
			redraw();
			int level = getLevel();
			barPanel.setLevel(level);
			int[] xpTable = MyHacker.getXPTable();
			float fromXP;
			float toXP;
			if(level!=1){
				fromXP = xpTable[level-2];
				toXP = xpTable[level-1];
			}
			else{
				fromXP=0;
				toXP=83;
			}
			//System.out.println(fromXP+"  "+xp+"  "+toXP);
			float percent = (xp-fromXP)/(toXP-fromXP)*100.0f;
			barPanel.setToNext(percent);
			levelPanel.setLevel(level);
			NumberFormat nf=NumberFormat.getNumberInstance();
			String ttt = "<html><h4>"+name+"</h4>";
			if(!name.equals("Total Level")){
				ttt +="<b>Experience</b>:   "+(nf.format((int)getXP()));
				ttt += "<br /><b>To Next Level</b>:   "+nf.format(getToNextLevel());
			}
			ttt +="</html>";
			toolTip.setText(ttt);//setToolTipText(ttt);
			MyHacker.statChanged();
		}
		
	}
	
	public int getLevel(){
		int i=0;
		try{
			while(((int)xp>xpTable[i])&&i<99){
				i++;
			}
		}catch(ArrayIndexOutOfBoundsException e){}
		return(i+1);
	}
	
	public int getToNextLevel(){
		int toNext=0;
		if(getLevel()<100)
			toNext = xpTable[getLevel()-1]-(int)xp;
		else if(getLevel()==100)
			toNext=-1;
		return(toNext+1);
	}
	
	public void setPoint(int x){
		this.x=x;
		//this.y=y;
		//toolTip.move(x);
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
