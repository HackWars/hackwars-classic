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

public class StatsPanel extends JPanel implements ActionListener{
	public static final int UP=0;
	public static final int DOWN=1;
	public static final int MOVINGDOWN=2;
	public static final int MOVINGUP=3;
	public static final Color BACKGROUND = new Color(50,50,50);
	public static final int PADDING = 5;
	public static final Font TEXT = new Font("monospace",Font.BOLD,12);
	public static final Font IP_TEXT = new Font("monospace",Font.BOLD,20);
	public static final Font CPU_TEXT = new Font("monospace",Font.BOLD,16);
	public static final Color STAT_BACKGROUND = new Color(0,0,0);
	public static final Color STAT_BORDER = new Color(0,156,0);
	public static final Color TEXT_COLOR = new Color(255, 255, 255);
	public static final Color CPU_BACKGROUND = new Color(50,50,50,0);
	public static final Border BORDER = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
	public static final Border PANEL_BORDER = BorderFactory.createLineBorder(BACKGROUND);
	public static final BufferedImage BACKGROUND_TILE = ImageLoader.getImage("images/BlackTile.png");
	//private JDesktopPane panel=null;
	private Hacker MyHacker=null;
	private int width,height,x,y;
	//private JTextArea messagePane = new JTextArea();
	//private String message;
	//private Color c;
	private JButton minMessage;
	private int status;
	private StatIcon MI,AI,WI,FWI,SI,HI,RI,redirectIcon,repairIcon, TI;
	//private StatIcon TI;
	private CPULoadIcon CPUI;
	//private JLabel cashLabel,hackoMeter,pettyCash,bank;
	//private boolean flashing=false;
	//private int count,hcount;
	
	private MoneyIcon pettyIcon,bankIcon,ductTapeIcon,siliconIcon,germaniumIcon,ybcoIcon,plutoniumIcon, hackOMeterIcon,voteOMeterIcon;
	public StatsPanel(JDesktopPane mainPanel,Hacker MyHacker,int width,int height,int x,int y,Color c,StatIcon MI,StatIcon AI,StatIcon WI,StatIcon SI,StatIcon FWI,TotalLevelIcon TI,CPULoadIcon CPUI,JLabel cashLabel,StatIcon HI){
		//this.panel=mainPanel;
		this.MyHacker=MyHacker;
		this.width=width;
		this.height=height;
		//this.c=c;
		this.MI=MI;
		this.AI=AI;
		this.WI=WI;
		this.SI=SI;
		this.FWI=FWI;
		//this.TI=TI;
		this.CPUI=CPUI;
		//this.cashLabel=cashLabel;
		this.x=x;
		this.y=y;
		this.HI=HI;
		
		setBorder(PANEL_BORDER);
		setLayout(new GridBagLayout());
		//setBackground(c);
		populate();
		//windowUp();
		//this.setFrameIcon(ImageLoader.getImageIcon("images/calc.png"));
	}
	
	public void populate(){
		GridBagConstraints c = new GridBagConstraints();
		c.anchor=GridBagConstraints.FIRST_LINE_START;
		c.fill=GridBagConstraints.NONE;
		c.insets = new Insets(0,PADDING,0,PADDING);
		c.gridx=1;
		c.gridy=1;
		//c.ipady=5;
		pettyIcon = new MoneyIcon(MyHacker,"$0.00","images/pettycash.png",x+10,280,STAT_BACKGROUND,"Petty Cash");
		add(pettyIcon,c);
		c.gridy=2;
		c.ipady=0;
		bankIcon = new MoneyIcon(MyHacker,"$0.00","images/bank.png",x+10,310,STAT_BACKGROUND,"Bank");
		add(bankIcon,c);
		
		
		c.ipadx=0;
		c.ipady=0;
		hackOMeterIcon = new MoneyIcon(MyHacker,"0","images/hackometer.png",x+10,35,STAT_BACKGROUND,"Hack-O-Meter");
		c.gridy=3;
		add(hackOMeterIcon,c);
		voteOMeterIcon = new MoneyIcon(MyHacker,"0","images/voteometer.png",x+10,35,STAT_BACKGROUND,"Vote-O-Meter");
		c.gridy=4;
		add(voteOMeterIcon,c);
		
		//c.gridheight=1;
		c.gridy=5;
		ductTapeIcon = new MoneyIcon(MyHacker,"0","images/ducttape.png",x+10,35,STAT_BACKGROUND,"Duct Tape");
		add(ductTapeIcon,c);
		c.gridy=6;
		germaniumIcon = new MoneyIcon(MyHacker,"0","images/germanium.png",x+10,35,STAT_BACKGROUND,"Germanium");
		add(germaniumIcon,c);
		c.gridy=7;
		siliconIcon = new MoneyIcon(MyHacker,"0","images/silicon.png",x+10,35,STAT_BACKGROUND,"Silicon");
		add(siliconIcon,c);
		c.gridy=8;
		ybcoIcon = new MoneyIcon(MyHacker,"0","images/YBCO.png",x+10,35,STAT_BACKGROUND,"YBCO");
		add(ybcoIcon,c);
		c.gridy=9;
		plutoniumIcon = new MoneyIcon(MyHacker,"0","images/plutonium.png",x+10,35,STAT_BACKGROUND,"Plutonium");
		add(plutoniumIcon,c);
		c.gridy=10;
		c.gridx=0;
		c.gridwidth=2;
		c.anchor=GridBagConstraints.CENTER;
		JLabel label = new JLabel(MyHacker.getIP());
		label.setForeground(Color.white);
		label.setFont(IP_TEXT);
		//c.gridheight=3;
		c.insets=new Insets(0,0,PADDING,0);
		add(label,c);
		c.insets = new Insets(0,PADDING,0,PADDING);
		c.gridwidth=1;
		c.gridheight=1;
		c.ipadx=0;
		c.gridx=0;
		c.gridy=1;
		MI = new StatIcon(MyHacker,0.0f,"images/merchanting.png",10,0,100,"Merchanting");
		add(MI,c);
		
		c.gridy=2;
		AI = new StatIcon(MyHacker,0.0f,"images/attackIcon.png",x+10,35,100,"Attack");
		add(AI,c);
		c.gridy=3;
		WI = new StatIcon(MyHacker,0.0f,"images/watchIcon.png",x+10,70,100,"Watch");
		add(WI,c);
		c.gridy=4;
		SI = new StatIcon(MyHacker,0.0f,"images/scan.png",x+10,105,100,"Scan");
		add(SI,c);
		c.gridy=5;
		FWI = new StatIcon(MyHacker,0.0f,"images/firewallIcon.png",x+10,140,100,"Firewall");
		add(FWI,c);
		c.gridy=6;
		HI = new StatIcon(MyHacker,0.0f,"images/http.png",x+10,175,100,"HTTP");
		add(HI,c);
		c.gridy=7;
		redirectIcon = new StatIcon(MyHacker,0.0f,"images/redirect.png",x+10,210,100,"Redirect");
		add(redirectIcon,c);
		c.gridy=8;
		repairIcon = new StatIcon(MyHacker,0.0f,"images/repair.png",x+10,245,100,"Repair");
		add(repairIcon,c);
		c.gridy=9;
		TI = new StatIcon(MyHacker,0.0f,"images/totallevel.png",x+10,280,800,"Total Level");
		add(TI,c);
		c.gridx=0;
		c.gridwidth=2;
		c.gridy=0;
		c.gridheight=1;
		c.anchor=GridBagConstraints.FIRST_LINE_START;
		c.fill=GridBagConstraints.BOTH;
		CPUI = new CPULoadIcon(MyHacker,MyHacker.getPanel(),"CPU Load",x+10,100);
		add(CPUI,c);
		
		setBounds(x,y,width,height);
	}
		
	
	public void move(Dimension r){
		x=r.width-335;
		setBounds(x,y,width,height);
		//y=r.height;
		/*if(status==UP)
			repopulate();
		else if(status==DOWN)
			setDown();*/
	}
	
	public void setOrder(){
		//setComponentZOrder(minMessage,0);
	}
	
	public StatIcon getMerchantingIcon(){
		return(MI);
	}
	public StatIcon getAttackIcon(){
		return(AI);
	}
	public StatIcon getWatchIcon(){
		return(WI);
	}
	public StatIcon getScanIcon(){
		return(SI);
	}
	public StatIcon getFireWallIcon(){
		return(FWI);
	}
	public StatIcon getHTTPIcon(){
		return(HI);
	}
	public StatIcon getRedirectIcon(){
		return(redirectIcon);
	}
	public StatIcon getRepairIcon(){
		return(repairIcon);
	}
	public StatIcon getTotalLevelIcon(){
		return(TI);
	}
	public CPULoadIcon getCPULoadIcon(){
		return(CPUI);
	}
	
	public MoneyIcon getPettyCash(){
		return(pettyIcon);
	}
	
	public MoneyIcon getBank(){
		return(bankIcon);
	}
	
	
	public void setCommodities(float[] commodities){
		ductTapeIcon.setText(""+(int)commodities[0]);
		germaniumIcon.setText(""+(int)commodities[1]);
		siliconIcon.setText(""+(int)commodities[2]);
		ybcoIcon.setText(""+(int)commodities[3]);
		plutoniumIcon.setText(""+(int)commodities[4]);
		
	}
	
	public void setHackOMeter(int count){
		hackOMeterIcon.setText("" +(int)count);
			
			
	}
	public void setVoteOMeter(int count){
		voteOMeterIcon.setText(""+(int)count);
		
			
			
	}
	
	public int getStatus(){
		return status;
	}
	
	public void setDown(){
		/*removeAll();
		add(minMessage);
		minMessage.setIcon(ImageLoader.getImageIcon("images/maxstats.png"));
		minMessage.setToolTipText("Maximize Stats Panel");
		minMessage.setBounds(CPUI.getPreferredSize().width+(new JLabel(pettyCash.getText())).getPreferredSize().width+27,1,16,16);
		setComponentZOrder(minMessage,0);
		
		add(pettyCash);
		pettyCash.setBounds(1,1,(new JLabel(pettyCash.getText()+"000")).getPreferredSize().width,pettyCash.getHeight());
		
		add(CPUI);
		CPUI.setBounds(20+(new JLabel(pettyCash.getText())).getPreferredSize().width,1,(new JLabel("100.000%")).getPreferredSize().width,CPUI.getPreferredSize().height);
				CPUI.setBackground(new Color(41,42,41));
		
				
		setBounds(panel.getBounds().width-CPUI.getPreferredSize().width-(new JLabel(pettyCash.getText())).getPreferredSize().width-60,getBounds().y,(new JLabel(pettyCash.getText())).getPreferredSize().width+CPUI.getPreferredSize().width+60,17);
		status=DOWN;*/
	}
	
	public int getTotalLevel(){
		int level=0;
		level+=MI.getLevel();
		level+=AI.getLevel();
		level+=WI.getLevel();
		level+=SI.getLevel();
		level+=FWI.getLevel();
		level+=HI.getLevel();
		level+=repairIcon.getLevel();
		level+=redirectIcon.getLevel();
		return level;
	}
	
	public void paintComponent(Graphics g){
		g.setColor(BACKGROUND);
		g.fillRect(	0,0,getWidth(),getHeight());
		for(int i=0;i<getWidth()/BACKGROUND_TILE.getWidth()+1;i++){
			for(int y=0;y<getHeight()/BACKGROUND_TILE.getHeight()+1;y++){
				g.drawImage(BACKGROUND_TILE,i*BACKGROUND_TILE.getWidth(),y*BACKGROUND_TILE.getHeight(),null);
			}
		}
		
		
	}
	
	public void actionPerformed(ActionEvent e){
		if(e.getActionCommand().equals("Move")){
			if(status==UP){
				//System.out.println("Move Down");
				/*setBounds(panel.getBounds().width-25,getBounds().y,17,17);
				minMessage.setIcon(ImageLoader.getImageIcon("images/maxstats.png"));
				minMessage.setToolTipText("Maximize Stats Panel");
				status=DOWN;*/
				setDown();
			}
			else if(status==DOWN){
				//System.out.println("Move Up");
				/*setBounds(x,getBounds().y,width,height);
				minMessage.setIcon(ImageLoader.getImageIcon("images/minstats.png"));
				minMessage.setToolTipText("Minimize Stats Panel");
				panel.setComponentZOrder(this,0);
				status=UP;*/
				//repopulate();
			}
		}
	}
}
