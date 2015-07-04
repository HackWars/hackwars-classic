package GUI;
/**

StatIcon.java

this represents a stat icon on the desktop.

*/

import javax.swing.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.awt.geom.AffineTransform;
import java.text.*;
import java.awt.image.*;
import java.math.*;

public class CPULoadIcon extends JPanel implements MouseListener{
	//data
	private String stat,image;
	private Hacker MyHacker;
	private JDesktopPane mainPanel;
	private float load,totalLoad;
	private Color desktopColour;
	private JLabel textField;
	private BufferedImage needle,back;
	private int degree=-10;
	private ToolTip toolTip;
	
	public CPULoadIcon(Hacker MyHacker,JDesktopPane mainPanel,String stat,float load,float totalLoad){
		this.MyHacker=MyHacker;
		this.mainPanel=mainPanel;
		this.stat=stat;
		this.load=load;
		this.totalLoad=totalLoad;
		this.setLayout(new BorderLayout());
		this.setBorder(StatsPanel.BORDER);
		this.setBackground(StatsPanel.BACKGROUND);
		needle = ImageLoader.getImage("images/cpuheat_needle.png");
		back = ImageLoader.getImage("images/cpuheat_scale.png");
		setOpaque(false);
		float i=getPercent();
		NumberFormat nf = new DecimalFormat("0.00");
		String text=nf.format(i)+"%";
		textField = new JLabel(text);
		textField.setBackground(Color.BLACK);
		textField.setForeground(Color.WHITE);
		textField.setFont(StatsPanel.TEXT);
		setPreferredSize(new Dimension(75,25));
		String ttt = "<html><h4>CPU Load</h4>0/50</html>";
		//setToolTipText(ttt);
		toolTip = new ToolTip(ttt,MyHacker);
		//MyHacker.getPanel().add(toolTip);
		//MyHacker.getPanel().setComponentZOrder(toolTip,0);
		addMouseListener(this);
	}
	
	public void paintComponent(Graphics g){
		Graphics2D graphics = (Graphics2D)g;
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY );
		graphics.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_DISABLE);
		g.setColor(StatsPanel.CPU_BACKGROUND);
		g.fillRect(0,0,getWidth(),getHeight());
		g.drawImage(back,0,0,null);
		float percent = getPercent();
		int x = (int)((percent*210.0f)/100.0f);

		if(percent>=100.0f)
			x=210;
		g.drawImage(needle,x,5,null);
				
		int level = MyHacker.getStatsPanel().getTotalLevel();
		g.setFont(StatsPanel.CPU_TEXT);
		g.setColor(StatsPanel.TEXT_COLOR);

		NumberFormat nf = new DecimalFormat("0.00");
		String text=nf.format(getPercent())+"%";
		int size = StatsPanel.CPU_TEXT.getSize();
		//System.out.println("Font Size: "+size);
		g.drawString(text,getWidth()/2-size*text.length()/4+105,18);
	}
	
		public void redraw(){
		float i=getPercent();
		NumberFormat nf = new DecimalFormat("0.00");
		String text=nf.format(i)+"%";
		textField.setText(text);
		MyHacker.getStatsPanel().setOrder();
	}
		
	public String getStat(){
		return(stat);
	}
	
	public float getLoad(){
		return(load);
	}
	
	public float getTotalLoad(){
		return(totalLoad);
	}
	
	public void setLoad(float load){
		if(load!=this.load){
			this.load=load;
			String ttt = "<html><h4>CPU Load</h4>";
			ttt += load+"/"+totalLoad+"</html>";
			//setToolTipText(ttt);
			toolTip.setText(ttt);
			repaint();
			MyHacker.statChanged();
		}
	}
	
	public void setTotalLoad(float totalLoad){
		if(totalLoad!=this.totalLoad){
			this.totalLoad=totalLoad;
			String ttt = "<html><h4>CPU Load</h4>";
			ttt += load+"/"+totalLoad+"</html>";
			//setToolTipText(ttt);
			toolTip.setText(ttt);
			repaint();
			MyHacker.statChanged();
		}
	}
	
	public float getPercent(){
		float percent = (load/totalLoad)*100;
		return(percent);
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
