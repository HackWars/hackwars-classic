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
import java.util.*;

public class MessageWindow extends JPanel implements ActionListener,Runnable{
	public static final int UP=0;
	public static final int DOWN=1;
	public static final int MOVINGDOWN=2;
	public static final int MOVINGUP=3;
	private JDesktopPane mainPanel=null;
	private Hacker MyHacker=null;
	private JTextArea messagePane = new JTextArea();
	private JScrollPane scrollPane;
	private String message="";
	private int width,height;
	private Thread MyThread;
	private Color c;
	private JButton minMessage;
	private boolean flashing=false;
	private int status;
	public MessageWindow(JDesktopPane mainPanel,Hacker MyHacker,int width,int height,Color c){
		this.mainPanel=mainPanel;
		this.MyHacker=MyHacker;
		this.width=width;
		this.height=height;
		this.c=c;
		setLayout(null);
		setBackground(new Color(91,91,91));
		populate();
		//windowUp();
		//this.setFrameIcon(ImageLoader.getImageIcon("images/calc.png"));
	}
	
	public void populate(){
		removeAll();
		/*JSeparator sep = new JSeparator();
		sep.setForeground(new Color(0,0,0));
		add(sep);
		sep.setBounds(0,0,width,1);
		
		sep = new JSeparator();
		sep.setForeground(new Color(0,0,0));
		sep.setOrientation(SwingConstants.VERTICAL);
		add(sep);
		sep.setBounds(0,0,1,17);*/
		
		JLabel mLabel = new JLabel("Messages");
		Font f = new Font("dialog",Font.BOLD,12);
		mLabel.setFont(f);
		mLabel.setForeground(Color.WHITE);
		add(mLabel);
		mLabel.setBounds(2,2,mLabel.getPreferredSize().width,mLabel.getPreferredSize().height);
		
		minMessage = new JButton(ImageLoader.getImageIcon("images/down.png"));
		minMessage.setBorderPainted(false);
		minMessage.setContentAreaFilled(false);
		minMessage.setOpaque(false);
		minMessage.setToolTipText("Minimize Message Window");
		add(minMessage);
		minMessage.setBounds(width-30,2,16,16);
		minMessage.setActionCommand("MinMessage");
		minMessage.addActionListener(this);
		
		/*sep = new JSeparator();
		sep.setForeground(new Color(0,0,0));
		sep.setOrientation(SwingConstants.VERTICAL);
		add(sep);
		sep.setBounds(width-11,0,1,17);
		
		sep = new JSeparator();
		sep.setForeground(new Color(0,0,0));
		sep.setOrientation(SwingConstants.VERTICAL);
		add(sep);
		sep.setBounds(width-32,0,1,17);
		
		JSeparator sep2 = new JSeparator();
		sep2.setForeground(new Color(0,0,0));
		add(sep2);
		sep2.setBounds(0,17,width,1);*/
		messagePane.setEditable(false);
		messagePane.setBackground(Color.BLACK);
		Font f1 = new Font("monoserif",Font.PLAIN,10);
		messagePane.setFont(f1);
		messagePane.setForeground(Color.WHITE);
		scrollPane = new JScrollPane(messagePane);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		add(scrollPane);
		scrollPane.setBounds(0,20,width-10,100);
		setBounds(0,height-170,width,170);
		status=UP;
	}
	public void repopulate(){
		removeAll();
		setBackground(new Color(91,91,91));
		JSeparator sep = new JSeparator();
		sep.setForeground(new Color(0,0,0));
		add(sep);
		sep.setBounds(0,0,width,1);
		
		sep = new JSeparator();
		sep.setForeground(new Color(0,0,0));
		sep.setOrientation(SwingConstants.VERTICAL);
		add(sep);
		sep.setBounds(0,0,1,17);
		
		JLabel mLabel = new JLabel("Messages");
		mLabel.setForeground(Color.WHITE);
		Font f = new Font("dialog",Font.BOLD,12);
		mLabel.setFont(f);
		add(mLabel);
		mLabel.setBounds(2,2,mLabel.getPreferredSize().width,mLabel.getPreferredSize().height);
		
		minMessage = new JButton(ImageLoader.getImageIcon("images/down.png"));
		minMessage.setBorderPainted(false);
		minMessage.setContentAreaFilled(false);
		minMessage.setOpaque(false);
		if(status==UP){
			minMessage.setToolTipText("Minimize Message Window");
			minMessage.setActionCommand("MinMessage");
		}
		else{
			minMessage.setIcon(ImageLoader.getImageIcon("images/messageup.png"));
			minMessage.setToolTipText("Maximize Message Window");
			minMessage.setActionCommand("MaxMessage");
		}
			add(minMessage);
			minMessage.setBounds(width-30,2,16,16);
		minMessage.addActionListener(this);
		
		sep = new JSeparator();
		sep.setForeground(new Color(0,0,0));
		sep.setOrientation(SwingConstants.VERTICAL);
		add(sep);
		sep.setBounds(width-11,0,1,17);
		
		sep = new JSeparator();
		sep.setForeground(new Color(0,0,0));
		sep.setOrientation(SwingConstants.VERTICAL);
		add(sep);
		sep.setBounds(width-32,0,1,17);
		
		JSeparator sep2 = new JSeparator();
		sep2.setForeground(new Color(0,0,0));
		add(sep2);
		sep2.setBounds(0,17,width,1);
		
		//scrollPane = new JScrollPane(messagePane);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		add(scrollPane);
		scrollPane.setBounds(0,20,width-10,100);
		if(status==UP)
			setBounds(0,height-170,width,170);
		else if(status==DOWN)
			setBounds(0,height-70,width,170);
		revalidate();
	}


	public void windowUp(){
		setBackground(new Color(91,91,91));
		minMessage.setIcon(ImageLoader.getImageIcon("images/down.png"));
		minMessage.setToolTipText("Minimize Message Window");
		minMessage.setActionCommand("MinMessage");
		
		MyThread = new Thread(this);
		status=MOVINGUP;
		MyThread.start();
	}
	
	public void windowDown(){
		//status=DOWN;
		minMessage.setIcon(ImageLoader.getImageIcon("images/messageup.png"));
		minMessage.setToolTipText("Maximize Message Window");
		minMessage.setActionCommand("MaxMessage");

		MyThread = new Thread(this);
		status=MOVINGDOWN;
		MyThread.start();
		
	}

	public void setMessage(String message){
		Calendar c = Calendar.getInstance();
		int hour=c.get(Calendar.HOUR);
		int minute=c.get(Calendar.MINUTE);
		int seconds=c.get(Calendar.SECOND);
		int ampm=c.get(Calendar.AM_PM);
		String pm="PM";
		String second;
		String minutes;
		if(seconds<10)
			second="0"+seconds;
		else
			second=""+seconds;
		if(minute<10)
			minutes="0"+minute;
		else
			minutes=""+minute;
		if(ampm==0)
			pm="AM";
		if(hour==0)
			hour=12;
		messagePane.append("("+hour+":"+minutes+":"+second+" "+pm+") "+message+"\n");
		messagePane.setCaretPosition(messagePane.getText().length());
		if(status==DOWN&&!flashing){
			MyThread=new Thread(this);
			MyThread.start();
		}
	}
	
	public void move(Rectangle r){
		width=r.width;
		height=r.height;
		repopulate();
		/*if(status==DOWN){
			windowDown();
		}
		if(status==UP){
			windowUp();
		}*/
	}
	
	public void run(){
		if(status==DOWN){
			flashing=true;
			int color=0;
			int change=1;
			Color c = new Color(91,91,91);
			int c1=c.getRed(),c2=c.getGreen(),c3=c.getBlue();
			int ce1=255,ce2=174,ce3=0;
			int col1,col2,col3;
			while(status==DOWN){
				col1=(ce1-c1)*color/100+c1;
				col2=(ce2-c2)*color/100+c2;
				col3=(ce3-c3)*color/100+c3;
				setBackground(new Color(col1,col2,col3));
				color+=change;
				if(color==100||color==0)
					change*=-1;
				try{
					MyThread.sleep(15);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			setBackground(c);
			flashing=false;
		}
		if(status==MOVINGDOWN){
			for(int i=height-170;i<height-65;i+=5){
				setBounds(0,i,width,170);
				try{
					MyThread.sleep(15);
				}catch(Exception e){}
			}
			status=DOWN;
		}
		if(status==MOVINGUP){
			for(int i=height-65;i>height-170;i-=5){
				//System.out.println("Moving up one step: "+i);
				setBounds(0,i,width,170);
				try{
					MyThread.sleep(15);
				}catch(Exception e){}
			}
			status=UP;
		}
		
	}
	
	public void actionPerformed(ActionEvent e){
		message = messagePane.getText();
		if(e.getActionCommand().equals("MinMessage")){
			//messagePane.append("Minimize Window\n");
			windowDown();
		}
		if(e.getActionCommand().equals("MaxMessage")){
			//messagePane.append("Minimize Window\n");
			windowUp();
		}
	}
	
}
