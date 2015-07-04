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

public class ZombieAttackDialog extends JDialog implements ActionListener,UndoableEditListener,FocusListener{
	
	private JPanel panel=new JPanel();
	private JTextField ipField1,ipField2,ipField3,ipField4;
	private JSpinner portSpinner;
	private JButton ok=new JButton("Continue"), cancel=new JButton("Cancel");
	private Hacker MyHacker;
	private IPPanel ipPanel;
	public ZombieAttackDialog(Hacker MyHacker){
		Insets insets = this.getInsets();
		this.MyHacker=MyHacker;
		//SpringLayout layout = new SpringLayout();
		panel.setLayout(null);
		int width=2;
		int height=2;
		JLabel label = new JLabel("Zombie IP: ");
		panel.add(label);
		label.setBounds(width,height,label.getPreferredSize().width,label.getPreferredSize().height);
		width+=5+label.getPreferredSize().width;
		
		int position=width;
		/*ipField1 = new JTextField(3);
		this.add(ipField1);
		Dimension d = ipField1.getPreferredSize();
		//ipField1.setText(ipValue);
		ipField1.setBounds(position,insets.top+5,d.width,d.height);
		position+=d.width;
		
		JLabel dot = new JLabel(".");
		d = dot.getPreferredSize();
		add(dot);
		dot.setBounds(position,insets.top+5,d.width,d.height);
		position+=d.width;
		
		ipField2 = new JTextField(3);
		this.add(ipField2);
		d = ipField2.getPreferredSize();
		//ipField2.setText(ipValue);
		ipField2.setBounds(position,insets.top+5,d.width,d.height);
		position+=d.width;
		
		dot = new JLabel(".");
		d = dot.getPreferredSize();
		add(dot);
		dot.setBounds(position,insets.top+5,d.width,d.height);
		position+=d.width;
		
		ipField3 = new JTextField(2);
		this.add(ipField3);
		d = ipField3.getPreferredSize();
		//ipField3.setText(ipValue);
		ipField3.setBounds(position,insets.top+5,d.width,d.height);
		position+=d.width;
		
		dot = new JLabel(".");
		d = dot.getPreferredSize();
		add(dot);
		dot.setBounds(position,insets.top+5,d.width,d.height);
		position+=d.width;
		
		ipField4 = new JTextField(3);
		this.add(ipField4);
		d = ipField4.getPreferredSize();
		//ipField4.setText(ipValue);
		ipField4.setBounds(position,insets.top+5,d.width,d.height);
		
		ipField1.getDocument().addUndoableEditListener(this);
		ipField2.getDocument().addUndoableEditListener(this);
		ipField3.getDocument().addUndoableEditListener(this);
		ipField4.getDocument().addUndoableEditListener(this);
		ipField1.addFocusListener(this);
		ipField2.addFocusListener(this);
		ipField3.addFocusListener(this);
		ipField4.addFocusListener(this);*/
		
		ipPanel = new IPPanel(MyHacker);
		add(ipPanel);
		Dimension d = ipPanel.getPreferredSize();
		ipPanel.setBounds(position,insets.top+5,d.width,d.height);

		height+=label.getPreferredSize().height+15;
		width = 2;
		label = new JLabel("Port: ");
		panel.add(label);
		label.setBounds(width,height,label.getPreferredSize().width,label.getPreferredSize().height);
		width+=5+label.getPreferredSize().width;
		SpinnerModel model = new SpinnerNumberModel(0,0,31,1);
		portSpinner = new JSpinner(model);
		this.add(portSpinner);
		d = portSpinner.getPreferredSize();
		portSpinner.setBounds(width,height,d.width,d.height);
		
		height+=15+label.getPreferredSize().height;
		width=2;
		panel.add(ok);
		ok.addActionListener(this);
		ok.setBounds(width,height,ok.getPreferredSize().width,ok.getPreferredSize().height);
		width+=15+ok.getPreferredSize().width;
		
		panel.add(cancel);
		cancel.addActionListener(this);
		cancel.setBounds(width,height,cancel.getPreferredSize().width,cancel.getPreferredSize().height);
		
		getContentPane().add(panel);
		setTitle("Start Zombie Attack");
		setSize(400,150);
		setLocationRelativeTo(MyHacker.getPanel());
		setModal(true);
		setVisible(true);
		
	}
	
	public void actionPerformed(ActionEvent e){
		if(e.getSource()==cancel)
			dispose();
		if(e.getSource()==ok){
			String ip=ipPanel.getIP();//ipField1.getText()+"."+ipField2.getText()+"."+ipField3.getText()+"."+ipField4.getText();
			MyHacker.startZombieAttack(ip,(int)(Integer)portSpinner.getValue());
			dispose();
		}
	}
	public void undoableEditHappened(UndoableEditEvent e){
		//System.out.println("Undoable Edit Happened");
		if(e.getSource()==ipField1.getDocument()){
			if(ipField1.getText().length()==3){
				ipField2.grabFocus();
			}
		}
		if(e.getSource()==ipField2.getDocument()){
			if(ipField2.getText().length()==3){
				ipField3.grabFocus();
			}
		}
		if(e.getSource()==ipField3.getDocument()){
			if(ipField3.getText().length()==1){
				ipField4.grabFocus();
			}
		}
	}
	
	public void focusGained(FocusEvent e){
		((JTextField)e.getSource()).selectAll();
	}

	public void focusLost(FocusEvent e){
	}	
}
