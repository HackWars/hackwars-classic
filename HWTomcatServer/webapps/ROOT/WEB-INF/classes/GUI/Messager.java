package GUI;
/**

Deposit.java
this is the deposit window.
*/

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

public class Messager extends Application{
	private JDesktopPane mainPanel=null;
	private Hacker MyHacker=null;

	public Messager(String name,boolean resize,boolean max,boolean close,boolean iconify,JDesktopPane mainPanel,Hacker MyHacker){
		this.setTitle(name);
		this.setResizable(resize);
		this.setMaximizable(max);
		this.setClosable(close);
		this.setIconifiable(iconify);
		this.addInternalFrameListener(this);
		this.mainPanel=mainPanel;
		this.MyHacker=MyHacker;
		this.setFrameIcon(ImageLoader.getImageIcon("images/im.png"));
	}

	public void populate(){
		Rectangle r = this.getBounds();
		String[] statusStrings = {"Online","Offline","Away"};
		JComboBox status = new JComboBox(statusStrings);
		this.add(status);
		Dimension d = status.getPreferredSize();
		status.setBounds(0,r.height-d.height-32,r.width-10,d.height);

	}

	public void internalFrameIconified(InternalFrameEvent e) {
	    mainPanel.repaint();
	}

	public void internalFrameClosed(InternalFrameEvent e) {
		MyHacker.setMessagerOpen(false);
	}
}
