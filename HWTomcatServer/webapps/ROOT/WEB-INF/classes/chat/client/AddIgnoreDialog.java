package chat.client;
/**
FunctionCallDialog.java

the dialog box for function calls.

*/

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

public class AddIgnoreDialog extends JDialog implements ActionListener{
	
	private JPanel panel=new JPanel();
	private JTextField nameField;
	private JTextField groupField;
	private ChatController controller;
	private JButton ok=new JButton("Add"), cancel=new JButton("Cancel");
	public AddIgnoreDialog(String name,ChatController controller){
		this.controller=controller;
		panel.setLayout(null);
		int width=2;
		int height=2;
		JLabel label = new JLabel("Name: ");
		panel.add(label);
		label.setBounds(width,height,label.getPreferredSize().width,label.getPreferredSize().height);
		width+=label.getPreferredSize().width+5;
		
		nameField=new JTextField(name,20);
		panel.add(nameField);
		nameField.setBounds(width,height,nameField.getPreferredSize().width,nameField.getPreferredSize().height);
		height+=nameField.getPreferredSize().height+5;
		width=2;
		
		label = new JLabel("Group: ");
		panel.add(label);
		label.setBounds(width,height,label.getPreferredSize().width,label.getPreferredSize().height);
		width+=label.getPreferredSize().width+5;
		
		groupField=new JTextField(20);
		panel.add(groupField);
		groupField.setBounds(width,height,groupField.getPreferredSize().width,groupField.getPreferredSize().height);
		height+=groupField.getPreferredSize().height+5;
		
		width=2;
		panel.add(ok);
		ok.addActionListener(this);
		ok.setBounds(width,height,ok.getPreferredSize().width,ok.getPreferredSize().height);
		width+=15+ok.getPreferredSize().width;
		
		panel.add(cancel);
		cancel.addActionListener(this);
		cancel.setBounds(width,height,cancel.getPreferredSize().width,cancel.getPreferredSize().height);
		height+=15+cancel.getPreferredSize().width;
		width=label.getPreferredSize().width+groupField.getPreferredSize().width+30;
		
		getContentPane().add(panel);
		setTitle("Add Ignore");
		setSize(width,height);
		setModal(false);
		setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e){
		if(e.getSource()==cancel)
			setVisible(false);
		if(e.getSource()==ok){
			//MyHome.changePrice((float)((Double)spinner.getValue()).floatValue());
			try{
				System.out.println(groupField.getText());
				controller.sendRelationAdd(nameField.getText(),groupField.getText(),true,true);
			}catch(Exception ex){}
			//VRL.addFriend(nameField.getText(),groupField.getText());
			setVisible(false);
		}
		
		
		
	}	
}
