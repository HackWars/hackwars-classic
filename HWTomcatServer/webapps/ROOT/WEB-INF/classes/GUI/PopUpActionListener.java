package GUI;
/**
PopUpActionListener.java

the action listener for the right click menu.
*/
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.util.*;

public class PopUpActionListener implements ActionListener{
	//data
	private JDesktopPane mainPanel;
	private Hacker MyHacker;
	
	public PopUpActionListener(JDesktopPane mainPanel,Hacker MyHacker){
		this.mainPanel=mainPanel;
		this.MyHacker=MyHacker;
	}
	
	public void actionPerformed(ActionEvent e){
	}
}
