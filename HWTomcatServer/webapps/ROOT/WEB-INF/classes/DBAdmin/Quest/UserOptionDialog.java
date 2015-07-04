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


public class UserOptionDialog extends JDialog implements ActionListener{

	private DialogWidget widget = null;
	private UserOption userOption = null;
	private JTextField dialogField = new JTextField();
	private JComboBox linkToComboBox;
	private String[] links;
	
	public UserOptionDialog(DialogWidget widget,UserOption userOption,String[] links){
		this.widget = widget;
		this.userOption = userOption;
		this.links=links;
		populate();
		
		
	}
	
	private void populate(){
		setResizable(true);
		setModal(true);
		setLocationRelativeTo(widget);
		//setMinimumSize(new Dimension(200,200));
		if(userOption==null){
			setTitle("New User Option");
		}
		else{
			setTitle("Edit User Option");
		}
		
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(10,10,2,0);
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		
		JLabel label = new JLabel("Text: ");
		getContentPane().add(label,c);
		
		c.gridx = 1;
		c.weightx = 1.0;
		c.gridwidth=2;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(10,2,2,10);
		if(userOption!=null){
			dialogField.setText(userOption.getDialog());
		}
		getContentPane().add(dialogField,c);
		
		c.gridx = 0;
		c.gridy = 1;
		c.fill = GridBagConstraints.NONE;
		c.weightx = 0.0;
		c.gridwidth=1;
		c.insets = new Insets(0,10,5,0);
		label = new JLabel("Destination: ");
		getContentPane().add(label,c);
		
		c.gridx = 1;
		c.weightx = 1.0;
		c.insets = new Insets(0,2,5,10);
		c.gridwidth = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		linkToComboBox = new JComboBox(links);
		if(userOption!=null){
			linkToComboBox.setSelectedIndex(userOption.getLinkTo()+1);
		}
		getContentPane().add(linkToComboBox,c);
		
		c.gridy = 2;
		c.gridwidth = 1;
		c.weightx = 0.0;
		c.fill = GridBagConstraints.NONE;
		c.insets = new Insets(0,0,10,0);
		
		JButton button = new JButton("OK");
		button.addActionListener(this);
		getContentPane().add(button,c);
		
		c.gridy = 2;
		c.gridx = 2;
		c.gridwidth = 1;
		c.weightx = 0.0;
		c.fill = GridBagConstraints.NONE;
		c.insets = new Insets(0,2,10,10);
		
		button = new JButton("Cancel");
		button.addActionListener(this);
		getContentPane().add(button,c);
		
		pack();
		setVisible(true);
		
	}
	
	
	public void actionPerformed(ActionEvent e){
		String command = e.getActionCommand();
		if(command.equals("Cancel")){
			setVisible(false);
		}
		else if(command.equals("OK")){
			if(userOption==null){
				widget.addUserOption(dialogField.getText(),linkToComboBox.getSelectedIndex()-1);
			}
			else{
				userOption.setDialog(dialogField.getText());
				userOption.setLinkTo(linkToComboBox.getSelectedIndex()-1);
			}
			setVisible(false);
		}
		
		
	}


}
