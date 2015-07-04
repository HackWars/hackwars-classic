package GUI;


import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import Game.*;
import View.*;
import Assignments.*;

public class HomeActionListener implements ActionListener{

	private String name;
	private Home MyHome;
	public HomeActionListener(String name,Home MyHome){
		this.name=name;
		this.MyHome=MyHome;
	}
	public void actionPerformed(ActionEvent e){
		MyHome.callDirectory(name);
	}
}
