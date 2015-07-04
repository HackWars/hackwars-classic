package DBAdmin.Quest;

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import util.*;
import java.io.*;
import java.awt.geom.*;
import java.awt.event.*;
import javax.imageio.*;
import java.util.*;
import javax.swing.text.*;

public class Dialog extends JFrame{

	private DialogBack back;
	public Dialog(){
		pack();
		setBounds(0,0,1024,768);
		setResizable(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		back = new DialogBack(this);
		setTitle("Dialog Making Tool");
		setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		
		c.fill=GridBagConstraints.BOTH;
		c.weightx=1.0;
		c.weighty=1.0;
		JTabbedPane tb = new JTabbedPane();
		add(tb,c);
		tb.addTab("NPC",new JPanel());
		tb.addTab("Dialog",back);
		tb.addTab("HTTP Script",new JPanel());
		tb.addTab("Website",new JPanel());
		tb.setSelectedIndex(1);
		
		setExtendedState(Frame.MAXIMIZED_BOTH);
		setVisible(true);
		
		DialogWidget widget = new DialogWidget(this,10,10,600,600,"Title");
		int index = back.addDialog(widget);
		back.setCurrentDialog(index);
		
		/*widget = new DialogWidget(this,820,10,200,45,"Option 1-1");
		back.addDialog(widget);
		
		widget = new DialogWidget(this,820,60,200,45,"Option 1-2");
		back.addDialog(widget);
		
		widget = new DialogWidget(this,820,110,200,45,"Option 1-3");
		back.addDialog(widget);*/
		
		
		
		
	}
	
	public void newPanel(){
		DialogWidget widget = new DialogWidget(this,10,10,600,600,"Title");
		int index = back.addDialog(widget);
		widget.setTitle(""+index);
		back.setCurrentDialog(index);
		
	}
	public DialogBack getBack(){
		return(back);
	}
	
	public String[] getTitles(){
		return back.getTitles();
	}
	
	public String getTitle(int index){
		return back.getTitle(index);
	}


	public static void main(String args[]){
		try {
			
	    // Set System L&F
		//UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		    
		//System.out.println(UIManager.getCrossPlatformLookAndFeelClassName());
	    }
	    catch (UnsupportedLookAndFeelException e) {
	       System.out.println("Unsupported");
	    }
	    catch (ClassNotFoundException e) {
		    System.out.println("Class not found");
	    }
	    catch (InstantiationException e) {
		System.out.println("Instantiation Exception");
	    }
	    catch (IllegalAccessException e) {
		System.out.println("Illegal Access Exception");
	    }
		Dialog d = new Dialog();
		
	}

}
