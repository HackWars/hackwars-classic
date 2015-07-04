package GUI;
/**
FunctionCallDialog.java

the dialog box for function calls.

*/

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

public class LinkDialog extends JDialog implements ActionListener{
	
	private WebsiteEditor MyWebsiteEditor;
	private JPanel panel=new JPanel();
	private JButton ok=new JButton("Insert"),cancel = new JButton("Cancel");
	private JTextField link=new JTextField(12),name=new JTextField(12);
	
	public LinkDialog(WebsiteEditor MyWebsiteEditor){
		int height=2;
		int width=2;
		this.MyWebsiteEditor=MyWebsiteEditor;
		SpringLayout layout = new SpringLayout();
		panel.setLayout(layout);
		JLabel label = new JLabel("Name:");
		int b = label.getPreferredSize().width;
		label = new JLabel("Link:");
		panel.add(label);
		layout.putConstraint(SpringLayout.NORTH,label,height,SpringLayout.NORTH,panel);
		layout.putConstraint(SpringLayout.WEST,label,width,SpringLayout.WEST,panel);
		width+=b+5;
		
		panel.add(link);
		layout.putConstraint(SpringLayout.NORTH,link,height,SpringLayout.NORTH,panel);
		layout.putConstraint(SpringLayout.WEST,link,width,SpringLayout.WEST,panel);
		width+=5+link.getPreferredSize().width;
		height+=link.getPreferredSize().height+5;
	
		width=2;
		label = new JLabel("Name:");
		panel.add(label);
		layout.putConstraint(SpringLayout.NORTH,label,height,SpringLayout.NORTH,panel);
		layout.putConstraint(SpringLayout.WEST,label,width,SpringLayout.WEST,panel);
		width+=5+label.getPreferredSize().width;
		
		panel.add(name);
		layout.putConstraint(SpringLayout.NORTH,name,height,SpringLayout.NORTH,panel);
		layout.putConstraint(SpringLayout.WEST,name,width,SpringLayout.WEST,panel);
		height+=5+name.getPreferredSize().height;
		
		width=2;
		panel.add(ok);
		ok.addActionListener(this);
		layout.putConstraint(SpringLayout.NORTH,ok,height,SpringLayout.NORTH,panel);
		layout.putConstraint(SpringLayout.WEST,ok,5+width,SpringLayout.WEST,panel);
		width+=5+ok.getPreferredSize().width;
		
		
		panel.add(cancel);
		cancel.addActionListener(this);
		layout.putConstraint(SpringLayout.NORTH,cancel,height,SpringLayout.NORTH,panel);
		layout.putConstraint(SpringLayout.WEST,cancel,width,SpringLayout.WEST,panel);
		width+=5+cancel.getPreferredSize().width;
		height+=ok.getPreferredSize().height+5;
		
		getContentPane().add(panel);
		setTitle("Insert Link");
		setSize(200,110);
		setLocationRelativeTo(MyWebsiteEditor);
		//setFrameIcon(ImageLoader.getImageIcon("images/functioncall.png"));
		setModal(false);
		setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e){
					
		if(e.getSource()==ok){
			MyWebsiteEditor.insertLink(link.getText(),name.getText());
			setVisible(false);
		}
		if(e.getSource()==cancel){
			setVisible(false);
		}
		
	}	
}
