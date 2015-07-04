package GUI;
/**

MessageWindow.java
this is the message window.
*/

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.imageio.*;
import java.awt.*;
import java.awt.event.*;
import Assignments.*;
import View.*;
import java.text.*;
import java.math.*;
import java.net.*;
import java.io.*;
import java.awt.image.*;

public class MapPanel extends Application{
	public static final Color NETWORK_INFO_BACKGROUND = new Color(0, 0, 0, 175);
	public static final Color NETWORK_NAME_COLOR = Color.white;
	public static final Color NETWORK_IP_COLOR = Color.white;
	public static final Color NETWORK_QUEST_COLOR = new Color(255, 255, 153);
	public static final Color NETWORK_SHIPPING_COLOR = new Color(153, 255, 153);
	public static final Color NETWORK_REGULAR_COLOR = new Color(255, 255, 255);
	public static final Color NETWORK_STORE_COLOR = new Color(153, 204, 255);
	public static final Color HEADER_COLOR = new Color(255, 255, 255);
	public static final Font NETWORK_NAME_FONT=new Font("monospace",Font.BOLD,18);
	//individual NPCs and counts
	public static final Font NETWORK_QUESTS_FONT = new Font("monospace",Font.BOLD,12);
	//NPC List 
	public static final Font NETWORK_NPC_FONT = new Font("monospace",Font.BOLD,14);
	public static final Color NETWORK_MAP_BACKGROUND = Color.white;

	private Hacker MyHacker=null;
	private int width,height,x,y;
	public NetworkPanel networkPanel;
	private NetworkMapPanel networkMapPanel;
	public MapPanel(Hacker MyHacker,int width,int height,int x,int y){
		setTitle("Network");
		setResizable(true);
		setMaximizable(true);
		setClosable(true);
		setIconifiable(true);
		addInternalFrameListener(this);
		setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
		this.MyHacker=MyHacker;
		this.width=width;
		this.height=height;
		this.x=x;
		this.y=y;
		setLayout(new GridBagLayout());
		populate();
		
	}
	
	public void populate(){
		GridBagConstraints c = new GridBagConstraints();
		c.fill=GridBagConstraints.BOTH;
		c.weightx=1.0;
		c.weighty=1.0;
		JTabbedPane tb = new JTabbedPane();
		networkPanel = new NetworkPanel(MyHacker,this);
		tb.addTab("Network",networkPanel);
		networkMapPanel = new NetworkMapPanel(MyHacker,this);
		JScrollPane sp = new JScrollPane(networkMapPanel);
		sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		sp.setBackground(NETWORK_MAP_BACKGROUND);
		tb.add("Map",networkMapPanel);
		
		add(tb,c);
		setBounds(x,y,width,height);	
		
	}
	
	public void setNetwork(PacketNetwork network){
		networkPanel.setNetwork(network);
	}
		
}
