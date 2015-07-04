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

public class ChoicesProgressBar extends JProgressBar implements Runnable{

	private ShowChoices MyChoices;
	private Thread MyThread=null;
	private String ip;
	
	public ChoicesProgressBar(ShowChoices MyChoices,String ip){
		this.MyChoices=MyChoices;
		this.ip=ip;
		
		MyThread=new Thread(this);
	}
	
	public void startBar(){
		MyThread.start();
	}
	
	public void run(){
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMinimumIntegerDigits(2);
		while(getValue()<getMaximum()){
			try{
				MyThread.sleep(1000);
			}catch(Exception e){}
			setValue(getValue()+1);
			int seconds = getMaximum()-getValue();
			MyChoices.setTime(nf.format(seconds)+" seconds left");
		}
		MyChoices.dispose();
		
	}
		
	
}
