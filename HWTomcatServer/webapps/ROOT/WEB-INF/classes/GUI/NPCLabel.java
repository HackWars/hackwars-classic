package GUI;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import Assignments.*;
import View.*;

public class NPCLabel extends JLabel implements ActionListener,MouseListener{
	public static final int QUEST=0;
	public static final int SHIPPING=1;
	public static final int REGULAR=2;
	public static final int STORE=3;
	private String name,ip,commodity="",title="";
	private int type=0;
	private Hacker hacker;
	private ToolTip toolTip;
	public NPCLabel(String name,String ip,String commodity,String title,int type,Hacker hacker,NetworkPanel networkPanel){
		
		
		this.name=name;
		this.ip=ip;
		this.commodity=commodity;
		this.title=title;
		this.type=type;
		this.hacker=hacker;
		addMouseListener(this);
		//setActionCommand(name);
		//setIcon(ImageLoader.getImageIcon("images/off32.png"));
		setText(name);
		Color c=Color.white;
		String tooltiptext="";
		if(type==QUEST){
			c=MapPanel.NETWORK_QUEST_COLOR;
			tooltiptext = "<html><b>Name</b>: "+name+"<br><b>Title</b>: "+title+"<br><b>IP</b>: "+ip+"</html>";
		}
		else if(type==SHIPPING){
			c=MapPanel.NETWORK_SHIPPING_COLOR;
			tooltiptext = "<html><b>Name</b>: "+name+"<br><b>Title</b>: "+title+"<br><b>IP</b>: "+ip+"<br><b>Commodity</b>: "+commodity+"</html>";
		}
		else if(type==REGULAR){
			c=MapPanel.NETWORK_REGULAR_COLOR;
			tooltiptext = "<html><b>Name</b>: "+name+"<br><b>Title</b>: "+title+"<br><b>IP</b>: "+ip+"</html>";
		}
		else if(type==STORE){
			c=MapPanel.NETWORK_STORE_COLOR;
			tooltiptext = "<html><b>Name</b>: "+name+"<br><b>Title</b>: "+title+"<br><b>IP</b>: "+ip+"</html>";
		
		}
		setForeground(c);
		setFont(MapPanel.NETWORK_QUESTS_FONT);
		
		//setToolTipText(tooltiptext);
		toolTip = new ToolTip(tooltiptext,hacker);
		//hacker.getPanel().setComponentZOrder(toolTip,0);
	}
	
	public void mouseEntered(MouseEvent e){
		toolTip.show(e.getX()+getLocationOnScreen().x-hacker.getPanel().getLocationOnScreen().x,e.getY()+getLocationOnScreen().y-hacker.getPanel().getLocationOnScreen().y);

	}

	public void mouseExited(MouseEvent e){
		toolTip.setVisible(false);
	}

	public void mousePressed(MouseEvent e){

	}

	public void mouseReleased(MouseEvent e){

	}

	public void mouseClicked(MouseEvent e){
		JPopupMenu menu = new JPopupMenu();
		JMenuItem menuItem;
		
		
		menuItem = new JMenuItem("Visit Website");
		menuItem.addActionListener(this);
		menu.add(menuItem);
		
		menuItem = new JMenuItem("Scan");
		menuItem.addActionListener(this);
		menu.add(menuItem);
		
		menu.show(this,e.getX(),e.getY());

	}
	
	public void actionPerformed(ActionEvent e){
			if(e.getActionCommand().equals("Scan")){
				hacker.startScan(ip);
			}
			else if(e.getActionCommand().equals("Visit Website")){
				hacker.showWebBrowser(ip);
			}
	}
	
	public String toString(){
		return getText();
	}
}

