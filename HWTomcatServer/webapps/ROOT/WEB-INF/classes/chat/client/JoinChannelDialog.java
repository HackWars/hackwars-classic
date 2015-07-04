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

public class JoinChannelDialog extends JDialog implements ActionListener{
	
	private JPanel panel=new JPanel();
	private JTextField nameField;
	private JTextField passwordField;
	private ChatController controller;
	private JButton ok=new JButton("Join"), cancel=new JButton("Cancel");
	public JoinChannelDialog(ChatController controller){
		this.controller=controller;
		panel.setLayout(null);
		int width=2;
		int height=2;
		JLabel label = new JLabel("Name: ");
		panel.add(label);
		label.setBounds(width,height,label.getPreferredSize().width,label.getPreferredSize().height);
		width+=label.getPreferredSize().width+5;
		
		nameField=new JTextField(20);
		panel.add(nameField);
		nameField.setBounds(width,height,nameField.getPreferredSize().width,nameField.getPreferredSize().height);
		height+=nameField.getPreferredSize().height+5;
		width=2;
		
		label = new JLabel("Password: ");
		panel.add(label);
		label.setBounds(width,height,label.getPreferredSize().width,label.getPreferredSize().height);
		width+=label.getPreferredSize().width+5;
		
		passwordField=new JTextField(20);
		panel.add(passwordField);
		passwordField.setBounds(width,height,passwordField.getPreferredSize().width,passwordField.getPreferredSize().height);
		height+=passwordField.getPreferredSize().height+5;
		
		width=2;
		panel.add(ok);
		ok.addActionListener(this);
		ok.setBounds(width,height,ok.getPreferredSize().width,ok.getPreferredSize().height);
		width+=15+ok.getPreferredSize().width;
		
		panel.add(cancel);
		cancel.addActionListener(this);
		cancel.setBounds(width,height,cancel.getPreferredSize().width,cancel.getPreferredSize().height);
		height+=15+cancel.getPreferredSize().width;
		width=label.getPreferredSize().width+passwordField.getPreferredSize().width+30;
		
		getContentPane().add(panel);
		setTitle("Join Channel");
		setSize(width,height);
		setModal(false);
		setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e){
		if(e.getSource()==cancel)
			setVisible(false);
		if(e.getSource()==ok){
			try{
				controller.sendChannelJoin(nameField.getText(),passwordField.getText());
			}catch(Exception ex){}
			setVisible(false);
		}
		
		
		
	}	
}
