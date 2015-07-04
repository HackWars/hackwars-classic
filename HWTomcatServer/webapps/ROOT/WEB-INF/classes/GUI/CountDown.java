package GUI;


import java.awt.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import Assignments.*;
import java.util.*;
import util.*;
import java.text.*;

public class CountDown extends JLabel implements Runnable{

	private ShowChoices MyChoices;
	private Thread MyThread=null;
	private String ip;
	private int max;
	private int value;
	private Hacker MyHacker;
	
	public CountDown(Hacker MyHacker){
		this.MyHacker=MyHacker;
		MyThread=new Thread(this);
	}
	
	public void startCount(int max){
		//System.out.println("Starting Count");
		this.max=this.value=max;
		MyThread.start();
	}
	
	public void run(){
		setVisible(true);
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMinimumIntegerDigits(2);
		while(value>0){
			try{
				MyThread.sleep(1000);
			}catch(Exception e){}
			//setValue(getValue()+1);
			int minutes = value/60;
			int seconds = value-minutes*60;
			value--;
			setText("<html><font color=\"#FFFFFF\">Server Shutdown in "+minutes+":"+nf.format(seconds)+"</font></html>");
			MyHacker.setFrameTitle("Server Shutdown in "+minutes+":"+nf.format(seconds));
		}
		//System.out.println("Countdown Over");
		setText("<html><font color=\"#FFFFFF\">Disconnected</font></html>");
		MyHacker.setFrameTitle("Disconnected");
		
	}
		
	
}
