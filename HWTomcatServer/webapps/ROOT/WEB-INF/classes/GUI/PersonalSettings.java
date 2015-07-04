package GUI;
/**

MessageWindow.java
this is the message window.
*/

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import Assignments.*;
import View.*;
import java.text.*;
import java.math.*;
import Browser.*;
import Game.*;
import java.net.URL;
import java.util.*;
import javax.imageio.*;
import java.io.*;

public class PersonalSettings extends Application implements ComponentListener{
	private final String[] images={"images/nopic.png","images/Snow_001.png","images/Bill_001.png","images/Necro_001.png","images/Johan_001.png","images/Butch_001.png","images/Jansen_001.png","images/Gunner001.png","images/N00b001.png","images/GothGirl_001.png","images/Rep_001.png"};
	private final String[] cpuNames={"Polonium 500MHz","Polonium 800Mhz","Polonium 1.4Ghz","Polonium 2.2Ghz","Polonium 3Ghz","Polonium Dual 2.4 Ghz","Polonium 600MHz"};
	private final int[] cpuValues={50,100,150,200,250,300,75};
	private final int[] cpuLevels={5,100,200,300,400,500,50};
	private final String[] hdNames={"Ocean Gate 200MB","Ocean Gate 500MB","Ocean Gate 1GB","Ocean Gate 2GB","Ocean Gate 4GB","Ocean Gate 250MB"};
	private final int[] hdValues={15,30,60,90,120,20};
	private final int[] hdLevels={5,150,250,350,450,60};
	private final String[] memNames={"Kansas 16MB","Kansas 64MB","Kansas 128MB","Kansas 256MB","Kansas 32MB"}; 
	private final int[] memPortValues={8,16,24,32,8};
	private final int[] memWatchValues={4,6,8,12,5};
	private final int[] memLevels={5,275,375,475,75};
	private Hacker MyHacker=null;
	private JTabbedPane tb;
	private String username;
	private String ip;
	private int imageIndex=0;
	private JPanel imagePanel = new JPanel();
	private JButton prev,next;
	private JTextArea ta;
	private JTextField tf;
	
	public PersonalSettings(String username,Hacker MyHacker){
		this.MyHacker=MyHacker;
		this.username=username;
		this.setTitle("Personal Settings - "+username);
		this.setResizable(false);
		this.setMaximizable(false);
		this.setClosable(true);
		this.setIconifiable(true);
		this.addInternalFrameListener(this);
		setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
		addComponentListener(this);
		setBounds(50,50,500,500);
		setLayout(null);
		populate();
	}

	public void populate(){
		
		tb = new JTabbedPane();
		
		JPanel profilePanel = new JPanel();
		JPanel challengesPanel = new JPanel();
		tb.addTab("Profile",profilePanel);
		/*if(username.equalsIgnoreCase(MyHacker.getUsername())){
			tb.addTab("Challenges",challengesPanel);
			getChallengePanel(challengesPanel);
		}*/
		
		add(tb);
		//setBackground(Color.white);
		tb.setBounds(2,0,getBounds().width-15,getBounds().height-50);
		getProfilePanel(profilePanel);
		
		
		
	}
	
	public void getProfilePanel(JPanel p){
		String image="";
		int width=0;
		int height=0;
		String description="";
		String location="";
		String ip="";
		try{
			Object[] params = new Object[]{username};
			HashMap result = (HashMap)XMLRPCCall.execute("http://"+MyHacker.getView().getIP()+"/xmlrpc/profile.php","getProfile",params);
			image = "http://www.team-captin.com/"+result.get("image");
			getImageIndex((String)result.get("image"));
			width=(int)(Integer)result.get("width");
			height=(int)(Integer)result.get("height");
			height=160;
			description=(String)result.get("description");
			location=(String)result.get("location");
			ip=(String)result.get("ip");
			//System.out.println(description);
		}catch(Exception e){e.printStackTrace();}
		//System.out.println(image);
		this.ip=ip;
		//ImageViewerPanel imagePanel = new ImageViewerPanel();
		//aImage aI = new aImage(image,imagePanel);
		//imagePanel.setAImage(aI);
		try{
			imagePanel.setBorder(new CentredBackgroundBorder(ImageIO.read(new URL(image)),imagePanel));
		}catch(Exception e){e.printStackTrace();}
		p.setLayout(null);
		p.add(imagePanel);
		imagePanel.setBounds(5,5,width,height);
		
		
		JLabel label = new JLabel("Description: ");
		p.add(label);
		label.setBounds(15+width,5,label.getPreferredSize().width,label.getPreferredSize().height);
		ta = new JTextArea(description);
		p.add(ta);
		ta.setBounds(20+width+label.getPreferredSize().width,5,getBounds().width-(50+width+label.getPreferredSize().width),100);
		
		label = new JLabel("Location: ");
		p.add(label);
		label.setBounds(15+width,120,label.getPreferredSize().width,label.getPreferredSize().height);
		
		tf = new JTextField(location);
		p.add(tf);
		tf.setBounds(ta.getBounds().x,120,250,tf.getPreferredSize().height);
		
		ta.setBorder(tf.getBorder());
		/*if(!username.equalsIgnoreCase(MyHacker.getUsername())){
			JButton scan = new JButton("Scan");
			p.add(scan);
			scan.setBounds(5,height+30,scan.getPreferredSize().width,scan.getPreferredSize().height);
			scan.addActionListener(this);
			
			JButton attack = new JButton("Attack");
			p.add(attack);
			attack.setBounds(5,height+35+scan.getPreferredSize().height,attack.getPreferredSize().width,attack.getPreferredSize().height);
			attack.addActionListener(this);
		}*/
		if(username.equals(MyHacker.getUsername())){
			prev = new JButton(ImageLoader.getImageIcon("images/left.png"));
			prev.setActionCommand("prev");
			prev.setBorderPainted(false);
			prev.setContentAreaFilled(false);
			if(imageIndex==0)
				prev.setEnabled(false);
			prev.addActionListener(this);
			p.add(prev);
			prev.setBounds(5,10+height,16,16);
			next = new JButton(ImageLoader.getImageIcon("images/right.png"));
			next.setActionCommand("next");
			next.setBorderPainted(false);
			next.setContentAreaFilled(false);
			if(imageIndex==images.length)
				next.setEnabled(false);
			next.addActionListener(this);
			p.add(next);
			next.setBounds(width-9,height+10,16,16);
			JButton save = new JButton(ImageLoader.getImageIcon("images/save.png"));
			save.setActionCommand("saveImage");
			save.setBorderPainted(false);
			save.setContentAreaFilled(false);
			save.addActionListener(this);
			p.add(save);
			save.setBounds(width/2-3,height+10,16,16);
			
			save = new JButton(ImageLoader.getImageIcon("images/save.png"));
			save.setActionCommand("saveDescription");
			save.setBorderPainted(false);
			save.setContentAreaFilled(false);
			save.addActionListener(this);
			p.add(save);
			save.setBounds(ta.getBounds().x-20,ta.getBounds().y+15,16,16);
			
			save = new JButton(ImageLoader.getImageIcon("images/save.png"));
			save.setActionCommand("saveLocation");
			save.setBorderPainted(false);
			save.setContentAreaFilled(false);
			save.addActionListener(this);
			p.add(save);
			save.setBounds(tf.getBounds().x+tf.getBounds().width+5,tf.getBounds().y,16,16);
			
			JLabel hardware = new JLabel("Hardware");
			hardware.setFont(new Font("dialog",Font.BOLD,16));
			p.add(hardware);
			hardware.setBounds(label.getBounds().x,tf.getBounds().y+60,hardware.getPreferredSize().width,hardware.getPreferredSize().height);
			
			JButton CPU = new JButton(ImageLoader.getImageIcon("images/cpu.png"));
			CPU.setBorderPainted(false);
			CPU.setContentAreaFilled(false);
			p.add(CPU);
			CPU.setBounds(hardware.getBounds().x,hardware.getBounds().y+60,16,16);
			
			JButton HD = new JButton(ImageLoader.getImageIcon("images/hd.png"));
			HD.setBorderPainted(false);
			HD.setContentAreaFilled(false);
			p.add(HD);
			HD.setBounds(hardware.getBounds().x,CPU.getBounds().y+20,16,16);
			
			JButton mem = new JButton(ImageLoader.getImageIcon("images/memory.png"));
			mem.setBorderPainted(false);
			mem.setContentAreaFilled(false);
			p.add(mem);
			mem.setBounds(hardware.getBounds().x,HD.getBounds().y+20,16,16);
			
			JLabel current = new JLabel("Current");
			current.setFont(new Font("dialog",Font.BOLD,14));
			p.add(current);
			current.setBounds(CPU.getBounds().x+CPU.getBounds().width+5,hardware.getBounds().y+hardware.getBounds().height+5,current.getPreferredSize().width,current.getPreferredSize().height);
			
			JLabel nextUpgrade = new JLabel("Next Upgrade");
			nextUpgrade.setFont(new Font("dialog",Font.BOLD,14));
			p.add(nextUpgrade);
			nextUpgrade.setBounds(CPU.getBounds().x+CPU.getBounds().width+200,hardware.getBounds().y+hardware.getBounds().height+5,nextUpgrade.getPreferredSize().width,nextUpgrade.getPreferredSize().height);
			int up = MyHacker.getCPUType()+1;
			if(MyHacker.getCPUType()==0)
				up=6;
			if(MyHacker.getCPUType()==6)
				up=1;
			
			int hdup = MyHacker.getHDType()+1;
			if(MyHacker.getHDType()==0)
				hdup=5;
			if(MyHacker.getHDType()==5)
				hdup=1;
			
			int mup = MyHacker.getMemoryType()+1;
			if(MyHacker.getMemoryType()==0)
				mup=4;
			if(MyHacker.getMemoryType()==4)
				mup=1;
			
			JLabel cpuLabel = new JLabel(cpuNames[MyHacker.getCPUType()]);
			cpuLabel.setFont(new Font("dialog",Font.PLAIN,12));
			cpuLabel.setToolTipText("<html>CPU Points: "+cpuValues[MyHacker.getCPUType()]+"<br> Total Level Required: "+cpuLevels[MyHacker.getCPUType()]+"</html>"); 
			p.add(cpuLabel);
			cpuLabel.setBounds(current.getBounds().x,CPU.getBounds().y,cpuLabel.getPreferredSize().width,cpuLabel.getPreferredSize().height);
			
			JLabel nextCPULabel = new JLabel(cpuNames[up]);
			nextCPULabel.setFont(new Font("dialog",Font.PLAIN,12));
			nextCPULabel.setToolTipText("<html>CPU Points: "+cpuValues[up]+"<br> Total Level Required: "+cpuLevels[up]+"</html>");
			p.add(nextCPULabel);
			nextCPULabel.setBounds(nextUpgrade.getBounds().x,CPU.getBounds().y,nextCPULabel.getPreferredSize().width,nextCPULabel.getPreferredSize().height);
			
			JLabel hdLabel = new JLabel(hdNames[MyHacker.getHDType()]);
			hdLabel.setFont(new Font("dialog",Font.PLAIN,12));
			hdLabel.setToolTipText("<html>HD Size: "+hdValues[MyHacker.getHDType()]+"<br> Total Level Required: "+hdLevels[MyHacker.getHDType()]+"</html>"); 
			p.add(hdLabel);
			hdLabel.setBounds(current.getBounds().x,HD.getBounds().y,hdLabel.getPreferredSize().width,hdLabel.getPreferredSize().height);
			
			JLabel nextHDLabel = new JLabel(hdNames[hdup]);
			nextHDLabel.setFont(new Font("dialog",Font.PLAIN,12));
			nextHDLabel.setToolTipText("<html>HD Size: "+hdValues[hdup]+"<br> Total Level Required: "+hdLevels[hdup]+"</html>");
			p.add(nextHDLabel);
			nextHDLabel.setBounds(nextUpgrade.getBounds().x,HD.getBounds().y,nextHDLabel.getPreferredSize().width,nextHDLabel.getPreferredSize().height);
			
			JLabel memLabel = new JLabel(memNames[MyHacker.getMemoryType()]);
			memLabel.setFont(new Font("dialog",Font.PLAIN,12));
			memLabel.setToolTipText("<html>Ports: "+memPortValues[MyHacker.getMemoryType()]+"<br>Watches: "+memWatchValues[MyHacker.getMemoryType()]+"<br> Total Level Required: "+hdLevels[MyHacker.getHDType()]+"</html>"); 
			p.add(memLabel);
			memLabel.setBounds(current.getBounds().x,mem.getBounds().y,memLabel.getPreferredSize().width,memLabel.getPreferredSize().height);
			
			JLabel nextmemLabel = new JLabel(memNames[mup]);
			nextmemLabel.setFont(new Font("dialog",Font.PLAIN,12));
			nextmemLabel.setToolTipText("<html>Ports: "+memPortValues[mup]+"<br>Watches: "+memWatchValues[mup]+"<br> Total Level Required: "+memLevels[mup]+"</html>");
			p.add(nextmemLabel);
			nextmemLabel.setBounds(nextUpgrade.getBounds().x,mem.getBounds().y,nextmemLabel.getPreferredSize().width,nextmemLabel.getPreferredSize().height);
		}
	}
	
	public void getChallengePanel(JPanel p){
		JPanel cp = new JPanel();
		cp.setLayout(null);
		JScrollPane sp = new JScrollPane(cp);
		int y=10;
		try{
			Object[] params = new Object[]{MyHacker.getIP()};
			Object[] result = (Object[])XMLRPCCall.execute("http://"+MyHacker.getView().getIP()+"/help/apilist.php","listChallenges", params);
			//for(int j=0;j<5;j++){
			for(int i=0;i<result.length;i++){
				HashMap HM = (HashMap)result[i];
				JLabel challenge = new JLabel("<html><u>"+(String)HM.get("name")+"</u></html>");
				cp.add(challenge);
				challenge.setForeground(Color.blue);
				challenge.addMouseListener(new ChallengeMouseListener(MyHacker,(int)(Integer)HM.get("id")));
				challenge.setBounds(5,y,challenge.getPreferredSize().width,challenge.getPreferredSize().height);
				JButton button = new JButton(ImageLoader.getImageIcon("images/dummyoff.png"));
				button.setBorderPainted(false);
				button.setEnabled(false);
				button.setContentAreaFilled(false);
				cp.add(button);
				button.setBounds(getBounds().width-62,y,16,16);
				if((boolean)(Boolean)HM.get("done")){
					button.setIcon(ImageLoader.getImageIcon("images/dummyon.png"));
				}
				y+=challenge.getPreferredSize().height+5;
			}
			//}
			//System.out.println(description);
		}catch(Exception e){e.printStackTrace();}
		cp.setPreferredSize(new Dimension(getBounds().width-30,y+10));
		cp.setBackground(Color.white);
		p.add(sp);
		//sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		sp.setBounds(5,5,getBounds().width-25,getBounds().height-60);
	}
	
	public void getImageIndex(String image){
		int imageIndex=0;
		for(imageIndex=0;imageIndex<images.length;imageIndex++){
			if(images[imageIndex].equals(image))
				break;
		}
		this.imageIndex=imageIndex;
	}
	

	public void internalFrameClosed(InternalFrameEvent e) {
		//MyHacker.setMessageWindowOpen(false);
	}
	
	 public void componentHidden(ComponentEvent e) {
    }

    public void componentMoved(ComponentEvent e) {
        
    }

    public void componentResized(ComponentEvent e) {
	   tb.setBounds(2,0,getBounds().width-15,getBounds().height-50);
    }

    public void componentShown(ComponentEvent e) {

    }
    
    public void actionPerformed(ActionEvent e){
	 if(e.getActionCommand().equals("Scan")){
		 MyHacker.startScan(ip);
	 }
	 if(e.getActionCommand().equals("Attack")){
		 AttackPane AP = MyHacker.getDefaultAttackPane();
		 String[] ips = ip.split("\\.");
		//AP.setSecondaryPorts(attacks);
		AP.setIP(ips[0],ips[1],ips[2],ips[3]);
		AP.setVisible(true);
	 }
	 if(e.getActionCommand().equals("prev")){
		 //System.out.println("Previous Image");
		 String image="http://www.team-captin.com/"+images[imageIndex-1];
		 try{
			imagePanel.setBorder(new CentredBackgroundBorder(ImageIO.read(new URL(image)),imagePanel));
		}catch(Exception ex){ex.printStackTrace();}
		 imageIndex--;
		 next.setEnabled(true);
		 if(imageIndex==0)
			 prev.setEnabled(false);
	 }
	  if(e.getActionCommand().equals("next")){
		 //System.out.println("Next Image");
		 String image="http://www.team-captin.com/"+images[imageIndex+1];
		 try{
			imagePanel.setBorder(new CentredBackgroundBorder(ImageIO.read(new URL(image)),imagePanel));
		 }catch(Exception ex){ex.printStackTrace();}
		 imageIndex++;
		 prev.setEnabled(true);
		 if(imageIndex==images.length-1)
			 next.setEnabled(false);
	 }
	 if(e.getActionCommand().equals("saveImage")){
		 try{
			Object[] params = new Object[]{username,images[imageIndex]};
			XMLRPCCall.execute("http://"+MyHacker.getView().getIP()+"/xmlrpc/profile.php","saveImage", params);
		 }catch(Exception ex){ex.printStackTrace();}
	 }
	 if(e.getActionCommand().equals("saveDescription")){
		 try{
			Object[] params = new Object[]{username,ta.getText()};
			XMLRPCCall.execute("http://"+MyHacker.getView().getIP()+"/xmlrpc/profile.php","saveDescription", params);
		 }catch(Exception ex){ex.printStackTrace();}
	 }
	 if(e.getActionCommand().equals("saveLocation")){
		 try{
			Object[] params = new Object[]{username,tf.getText()};
			XMLRPCCall.execute("http://"+MyHacker.getView().getIP()+"/xmlrpc/profile.php","saveLocation", params);
		 }catch(Exception ex){ex.printStackTrace();}
	 }
		    
    }

}
