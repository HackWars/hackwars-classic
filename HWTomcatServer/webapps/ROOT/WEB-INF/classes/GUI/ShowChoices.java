package GUI;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import Assignments.*;
import View.*;

public class ShowChoices extends Application {
	public final static int BANK=0;
	public final static int FTP=1;
	public final static int ATTACK=2;
	public final static int HTTP=3;
	public final static int LOGS=5;
	public final static int SHIPPING=4;
	
	private final String[] bankChoices={"Empty Petty Cash","Peek At Code","Peek At Logs"};
	private final String[] ftpChoices={"Open Public FTP","Peek At Code","Peek At Logs"};
	private final String[] attackChoices={"Peek At Code","Peek At Logs"};
	private final String[] httpChoices={"Change Daily Pay Target","Peek At Logs"};
	private final String[] shippingChoices={"Peek At Code","Peek At Logs"};
	private int type,port;
	private String ip;
	private JComboBox comboBox;
	private ChoicesProgressBar progress;
	private JLabel timeLabel;
	private Hacker MyHacker;
	private int windowHandle = 0;
	
	public ShowChoices(int type,String ip,int port,int windowHandle,Hacker MyHacker){
		this.MyHacker=MyHacker;
		this.type=type;
		this.ip=ip;
		this.port=port;
		this.windowHandle = windowHandle;
		//System.out.println("Attack Port: "+attackPort);
		this.setTitle("Choices - "+ip+":"+port);
		this.setResizable(false);
		this.setMaximizable(false);
		this.setClosable(false);
		this.setIconifiable(true);
		addInternalFrameListener(this);
		JPanel panel=new JPanel();
		panel.setLayout(null);
		
		Object[] choices={bankChoices,ftpChoices,attackChoices,httpChoices,shippingChoices};
		
		timeLabel = new JLabel("30 seconds left");
		panel.add(timeLabel);
		timeLabel.setBounds(5,5,timeLabel.getPreferredSize().width,timeLabel.getPreferredSize().height);
		int height=7+timeLabel.getPreferredSize().height;
		
		comboBox = new JComboBox((String[])choices[type]);
		panel.add(comboBox);
		comboBox.setBounds(140-comboBox.getPreferredSize().width/2,height,comboBox.getPreferredSize().width,comboBox.getPreferredSize().height);
		height+=30+comboBox.getPreferredSize().height;
		
		JButton ok = new JButton("Go");
		panel.add(ok);
		ok.addActionListener(this);
		JButton cancel = new JButton("Cancel");
		panel.add(cancel);
		cancel.addActionListener(this);
		ok.setBounds(140-(ok.getPreferredSize().width+cancel.getPreferredSize().width)/2,height,ok.getPreferredSize().width,ok.getPreferredSize().height);
		
		
		cancel.setBounds(140-(ok.getPreferredSize().width+cancel.getPreferredSize().width)/2+ok.getPreferredSize().width+5,height,cancel.getPreferredSize().width,cancel.getPreferredSize().height);
		
		progress = new ChoicesProgressBar(this,ip);
		progress.setMinimum(0);
		progress.setMaximum(30);
		progress.setValue(0);
		progress.startBar();
		
		
		add(panel);
	}
	
	public void setTime(String label){
		timeLabel.setText(label);
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("Cancel")){
			Object[] objects = {MyHacker.getEncryptedIP(), ip, new Integer(port)};
			View MyView = MyHacker.getView();
			MyView.setFunction("finalizecancelled");
			MyView.addFunctionCall(new RemoteFunctionCall(0,"finalizecancelled",objects));
			dispose();
			return;
		}
		if(((String)comboBox.getSelectedItem()).equals("Empty Petty Cash")){
			Object[] objects = {MyHacker.getEncryptedIP(),ip,new Integer(port),windowHandle};
			View MyView = MyHacker.getView();
			MyView.setFunction("emptypettycash");
			MyView.addFunctionCall(new RemoteFunctionCall(0,"emptypettycash",objects));
		}
		if(((String)comboBox.getSelectedItem()).equals("Install New Script")){
			ShowChoicesFileChooser SCFC = new ShowChoicesFileChooser(MyHacker,type,ip,port,windowHandle);
			MyHacker.getPanel().add(SCFC);
			SCFC.moveToFront();
			
		}
		//System.out.println(comboBox.getSelectedItem());
		if(((String)comboBox.getSelectedItem()).equals("Peek At Code")){
			//System.out.println("Starting Up Peeking at Code");
			Object[] objects = {MyHacker.getEncryptedIP(),ip,new Integer(port)};
			View MyView = MyHacker.getView();
			MyView.setFunction("peekcode");
			MyView.addFunctionCall(new RemoteFunctionCall(0,"peekcode",objects));
			PeekAtCode PAC = new PeekAtCode(MyHacker,type,ip,port);
			MyHacker.getPanel().add(PAC);
			PAC.moveToFront();
		}
		if(((String)comboBox.getSelectedItem()).equals("Peek At Logs")){
			//System.out.println("Starting Up Peeking at Logs");
			
			Object[] objects = {MyHacker.getEncryptedIP(),ip,new Integer(port)};
			View MyView = MyHacker.getView();
			MyView.setFunction("peeklogs");
			MyView.addFunctionCall(new RemoteFunctionCall(0,"peeklogs",objects));
			PeekAtCode PAC = new PeekAtCode(MyHacker,LOGS,ip,port);
			MyHacker.getPanel().add(PAC);
			PAC.moveToFront();
		}
		if(((String)comboBox.getSelectedItem()).equals("Change Daily Pay Target")){
			String answer = (String)JOptionPane.showInputDialog(
                    MyHacker.getPanel(),
                    "IP:",
                    "Change Daily Pay Target",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    MyHacker.getIP());
		    if(answer!=null){
			    Object[] objects = {ip,new Integer(port),answer,MyHacker.getEncryptedIP(),windowHandle};
			View MyView = MyHacker.getView();
			MyView.setFunction("changedailypay");
			MyView.addFunctionCall(new RemoteFunctionCall(0,"changedailypay",objects));
		    }
		}
		if(((String)comboBox.getSelectedItem()).equals("Open Public FTP")){
			ShowChoicesFileChooser SCFC = new ShowChoicesFileChooser(MyHacker,1,ip,port,windowHandle);
			MyHacker.getPanel().add(SCFC);
			SCFC.moveToFront();
		}
		if(((String)comboBox.getSelectedItem()).equals("Open Store FTP")){
			ShowChoicesFileChooser SCFC = new ShowChoicesFileChooser(MyHacker,0,ip,port,windowHandle);
			MyHacker.getPanel().add(SCFC);
			SCFC.moveToFront();
		}
		dispose();
	}
    
    public void internalFrameClosed(InternalFrameEvent e) {
    }
		
}

