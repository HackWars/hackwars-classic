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
import java.util.HashMap;

public class TutorialWindow extends Application {
	private final String messages[] = {"<p>Welcome to Hack Wars! This tutorial will help you get started.</p><p>To get started, you will need some basic scripts.</p><p>In order to do this, go to Applications>Internet>Store</p>",
					   "<p>Now Click on \"Buy\" under basicBank.bin to buy a bank script</p>",
					   "<p>Now we need to Install the script you just got.</p><p>Go to System>Administration>Port Management</p>"
					  };
	private HtmlHandler textPane;
	private Hacker MyHacker;
	private int step=0;
	private String title="Tutorial";
    private JCheckBox checkbox;
	
	public TutorialWindow(Hacker MyHacker){
		this.MyHacker=MyHacker;
		this.setTitle("Tutorial");
		this.setResizable(true);
		this.setMaximizable(true);
		this.setClosable(true);
		this.setIconifiable(true);
		this.addInternalFrameListener(this);
        //this.addWindowListener(this);
		//setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
		setBounds(250,50,600,500);
		setLayout(new GridBagLayout());
		Object[] message = (Object[])XMLRPCCall.execute("http://www.hackwars.net/xmlrpc/tutorials.php","getTutorial",new Object[]{"HI"});
		messages[0] = (String)message[0];
		title = (String)message[1];
		createToolBar();
		populate();
	}
	
	private void createToolBar(){
		JToolBar toolBar = new JToolBar("Tools");
		JButton button;
		
		button = new JButton(ImageLoader.getImageIcon("images/back.png"));
		button.setActionCommand("Back");
		button.setToolTipText("Back");
		button.addActionListener(this);
		toolBar.add(button);
		
		button = new JButton(ImageLoader.getImageIcon("images/forward.png"));
		button.setActionCommand("Next");
		button.setToolTipText("Next");
		button.addActionListener(this);
		toolBar.add(button);
		GridBagConstraints c = new GridBagConstraints();
	//	add(toolBar);
		toolBar.setBounds(0,0,400,toolBar.getPreferredSize().height);
		//System.out.println(toolBar.getPreferredSize().height);
	}
	
	public void populate(){
		GridBagConstraints c = new GridBagConstraints();
		textPane = new HtmlHandler();
		textPane.setParent(this);
		//textPane.createView();
		c.gridx=0;
		c.fill=GridBagConstraints.BOTH;
		c.weighty=1.0;
		c.weightx=1.0;
		c.gridwidth=2;
		add(textPane.getView(),c);
		//textPane.getView().setBounds(0,0,388,375);
		showMessage();
		
		c.gridy=1;
		c.gridwidth=1;
		c.weightx=0.0;
		c.weighty=0.0;
		c.fill=GridBagConstraints.HORIZONTAL;
		checkbox = new JCheckBox();
        // is the "Show this on startup." checkbox ticked?
        HashMap prefs = MyHacker.getPreferences();
        boolean showTutorial = true;
        if (prefs != null) {
            Object o = prefs.get(OptionPanel.ATTACK_TUTORIAL_KEY);
            if (o != null) {
                showTutorial = new Boolean(""+o);
            }
        }
		checkbox.setSelected(showTutorial);
		checkbox.addActionListener(this);
		add(checkbox,c);
		
		c.gridx=1;
		c.weightx=1.0;
		JLabel label = new JLabel("Show this tutorial on startup.");
		add(label,c);
	}
	
	private void showMessage(){
		try{
				textPane.parseDocument("<html>"+messages[step]+"</html>",this);
		}catch(Exception e){
			e.printStackTrace();
		}	
	}
	
	public void nextStep(){
		step++;
		System.out.println("Moving to step "+step);
		showMessage();
		moveToFront();
	}
	
	public int getStep(){
		return step;
	}
	
	public void linkGo(URL link){
		MyHacker.getView().getLoad().linkGo(link);
	}


	public void internalFrameClosed(InternalFrameEvent e) {
        MyHacker.setPreference(OptionPanel.ATTACK_TUTORIAL_KEY, ((Boolean)checkbox.isSelected()).toString());
	}
	
	public void actionPerformed(ActionEvent e){
	}
    
}
