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

public class ForumCreateThreadDialog extends JDialog implements ActionListener{
	
	private JPanel panel=new JPanel();
	private JTextArea ta=new JTextArea();
	private JTextField tf=new JTextField(40);
	private ForumPanel MyForumPanel;
	private JButton ok=new JButton("Post"), cancel=new JButton("Cancel");
	public ForumCreateThreadDialog(ForumPanel MyForumPanel,WebBrowser MyWebBrowser){
		
		this.MyForumPanel = MyForumPanel;
		panel.setLayout(null);
		int width=2;
		int height=2;
		JLabel label = new JLabel("Subject: ");
		panel.add(label);
		label.setBounds(width,height,label.getPreferredSize().width,label.getPreferredSize().height);
		width+=label.getPreferredSize().width+5;
		panel.add(tf);
		tf.setBounds(width,height,tf.getPreferredSize().width,tf.getPreferredSize().height);
		height+=tf.getPreferredSize().height+5;
		width=2;
		label = new JLabel("Message: ");
		panel.add(label);
		label.setBounds(width,height,label.getPreferredSize().width,label.getPreferredSize().height);
		height+=label.getPreferredSize().height+5;
		JScrollPane sp = new JScrollPane(ta);
		ta.setColumns(50);
		ta.setRows(8);
		ta.setLineWrap(true);
		ta.setWrapStyleWord(true);
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
		setTitle("Create New Thread");
		setSize(600,250);
		setLocationRelativeTo(MyWebBrowser);
		setModal(false);
		setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e){
		if(e.getSource()==cancel)
			setVisible(false);
		if(e.getSource()==ok){
			MyForumPanel.createNewThread(ta.getText(),tf.getText());
			setVisible(false);
		}
		
		
		
	}	
}
