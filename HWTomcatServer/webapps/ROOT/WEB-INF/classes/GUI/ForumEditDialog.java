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

public class ForumEditDialog extends JDialog implements ActionListener{
	
	private JPanel panel=new JPanel();
	private JTextArea ta=new JTextArea();
	private ForumPanel MyForumPanel;
	private JButton ok=new JButton("Post"), cancel=new JButton("Cancel");
	private String id;
	public ForumEditDialog(ForumPanel MyForumPanel,String message,String id){
		this.id=id;
		this.MyForumPanel = MyForumPanel;
		//SpringLayout layout = new SpringLayout();
		panel.setLayout(null);
		int width=2;
		int height=2;
		JLabel label = new JLabel("Message: ");
		panel.add(label);
		label.setBounds(width,height,label.getPreferredSize().width,label.getPreferredSize().height);
		height+=label.getPreferredSize().height+5;
		JScrollPane sp = new JScrollPane(ta);
		ta.setColumns(50);
		ta.setRows(8);
		ta.setLineWrap(true);
		ta.setWrapStyleWord(true);
		ta.setText(message);
		panel.add(sp);
		sp.setBounds(width,height,ta.getPreferredSize().width,ta.getPreferredSize().height);
		height+=ta.getPreferredSize().height+10;
		
		panel.add(ok);
		ok.addActionListener(this);
		ok.setBounds(width,height,ok.getPreferredSize().width,ok.getPreferredSize().height);
		width+=15+ok.getPreferredSize().width;
		
		panel.add(cancel);
		cancel.addActionListener(this);
		cancel.setBounds(width,height,cancel.getPreferredSize().width,cancel.getPreferredSize().height);
		
		getContentPane().add(panel);
		setTitle("Edit Post");
		setSize(600,220);
		//setLocationRelativeTo(MyForumPanel);
		setModal(false);
		setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e){
		if(e.getSource()==cancel)
			setVisible(false);
		if(e.getSource()==ok){
			MyForumPanel.editMessage(ta.getText(),id);
			setVisible(false);
		}
		
		
		
	}	
}
