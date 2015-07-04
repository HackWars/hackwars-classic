package GUI;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;



public class LoadingScreen extends JDesktopPane{
	
	private JProgressBar LoadingBar;
	public LoadingScreen(){
		/*frame = new JFrame("Loading");
		//SpringLayout layout = new SpringLayout();
		frame.setLayout(null);
		frame.pack();
		frame.setSize(250,100);
		frame.setResizable(false);
		frame.setLocation(200,200);
		frame.setVisible(true);*/
		LoadingBar = new JProgressBar(0,10);
		LoadingBar.setValue(0);
		LoadingBar.setString("Creating Menu");
		LoadingBar.setStringPainted(true);
		add(LoadingBar);
		LoadingBar.setBounds(100-LoadingBar.getPreferredSize().width/2,25-LoadingBar.getPreferredSize().height/2,LoadingBar.getPreferredSize().width+50,LoadingBar.getPreferredSize().height+10);
		//System.out.println("Loading Screen on");
	}
	
	public void dispose(){
		//frame.dispose();	
	}
	
	public void change(String string){
		//LoadingBar.setString(string);
		//LoadingBar.setValue(LoadingBar.getValue()+1);
		//frame.repaint();
	}
	public static void main(String args[]){
		JFrame frame = new JFrame("LOADING");
		LoadingScreen LS = new LoadingScreen();
		frame.add(LS);
		frame.pack();
		frame.setSize(250,100);
		frame.setVisible(true);
		LS.change("TESTING");
		
		
		
	}



}
