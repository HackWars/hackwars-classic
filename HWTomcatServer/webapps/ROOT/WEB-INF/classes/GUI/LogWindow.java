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

public class LogWindow extends Application{
	private JDesktopPane mainPanel=null;
	private JTextArea textPane;
	private Hacker MyHacker;
	private JScrollPane scrollPane;
	public LogWindow(Hacker MyHacker){
		this.MyHacker=MyHacker;
		this.setTitle("Log Window");
		this.setResizable(false);
		this.setMaximizable(false);
		this.setClosable(true);
		this.setIconifiable(true);
		this.addInternalFrameListener(this);
		setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
		setBounds(50,50,500,325);
		setLayout(null);
		populate();
		//System.out.println(MyHacker.getPanel().getComponentZOrder(this));
	}

	public void populate(){
		textPane = new JTextArea();
		Font f = new Font("Monoserif",Font.PLAIN,10);
		scrollPane = new JScrollPane(textPane);
		add(scrollPane);
		scrollPane.setBounds(0,0,475,250);
		
		JButton empty = new JButton("Empty Log");
		add(empty);
		empty.setBounds(5,255,empty.getPreferredSize().width,empty.getPreferredSize().height);
		empty.addActionListener(this);
	}
	
	public void addLine(String line){
		textPane.setText(line);
		SwingUtilities.invokeLater(new Runnable()
		{
		public void run()
		{
			scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
		}
		}); 
	}
	

	public void internalFrameClosed(InternalFrameEvent e) {
		//MyHacker.setMessageWindowOpen(false);
	}
	
	public void actionPerformed(ActionEvent e){
		//System.out.println("Deleting Log");
		Object objects[] = new Object[]{MyHacker.getEncryptedIP()};
		View MyView=MyHacker.getView();
		MyView.setFunction("deletelogs");
		MyView.addFunctionCall(new RemoteFunctionCall(0,"deletelogs",objects));
	}

}
