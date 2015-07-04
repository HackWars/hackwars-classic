package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.*;
import javax.swing.border.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.HashMap;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import net.miginfocom.swing.*;


public class OptionDialog extends JDialog implements ActionListener{
	private  int selectedButton = 1;
	private JCheckBox checkbox;
    
    public static final int OPTION_OK = 0;
    public static final int OPTION_YES = 0;
    public static final int OPTION_NO = 1;
	
	public Object[] showOKDialog(Component frame,String title,String message){
		getContentPane().setLayout(new MigLayout("wrap 2,ins 10"));
		setTitle(title);
        message = "<html>" + message.replaceAll("\\\n", "<br/>") + "</html>";
		JLabel label = new JLabel(message);
		getContentPane().add(label,"span,wrap");
		checkbox = new JCheckBox("Do not show this dialog again.");
		getContentPane().add(checkbox,"growx");
		JButton yes = new JButton("OK");
		yes.addActionListener(this);
		getContentPane().add(yes);
		
		setModal(true);
		pack();
		setLocationRelativeTo(frame);
		setVisible(true);
		return(new Object[]{selectedButton,checkbox.isSelected()});
	}
	
	public Object[] showYesNoDialog(Component frame,String title,String message, String checkBoxText){
		getContentPane().setLayout(new MigLayout("fill,wrap 3,ins 10"));
		setTitle(title);
        message = "<html>" + message.replaceAll("\\\n", "<br/>") + "</html>";
		JLabel label = new JLabel(message);
		getContentPane().add(label,"span,wrap");
		checkbox = new JCheckBox(checkBoxText);
		getContentPane().add(checkbox,"growx");
		JButton yes = new JButton("Yes");
		yes.addActionListener(this);
		getContentPane().add(yes);
		JButton no = new JButton("No");
		no.addActionListener(this);
		getContentPane().add(no);
		
		setModal(true);
		pack();
		setLocationRelativeTo(frame);
		setVisible(true);
		
		return(new Object[]{selectedButton,checkbox.isSelected()});
	
	}
	
	
	public void actionPerformed(ActionEvent e){
		if(e.getActionCommand().equals("OK")){
			selectedButton = OPTION_OK;
		}
		else if(e.getActionCommand().equals("Yes")){
			selectedButton = OPTION_YES;
		}
		else if(e.getActionCommand().equals("No")){
			selectedButton = OPTION_NO;
		}
		dispose();
	}



}
