package GUI;
/**
*
*the dialog box for asking user if they are sure they want to pay for an attack.
*@author Cameron McGuinness
*/

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.text.*;



public class AttackDialog extends JDialog implements ActionListener{
	
	private JPanel panel=new JPanel();
	private JTextArea ta=new JTextArea();
	private AttackPane MyAttackPane;
	private JButton ok=new JButton("Attack"), cancel=new JButton("Cancel");
	private String id;
	/**
	* constructor
	* @param MyAttackPane the attack pane that calls the dialog.
	**/
	public AttackDialog(AttackPane MyAttackPane){
		this.MyAttackPane = MyAttackPane;
		//SpringLayout layout = new SpringLayout();
		panel.setLayout(null);
		int width=2;
		int height=2;
		NumberFormat nf= NumberFormat.getCurrencyInstance();
		JLabel label = new JLabel("<html>Are You Sure?<br>It would cost you "+nf.format(10.0f)+" to attack this port.</html>");
		panel.add(label);
		label.setBounds(width,height,label.getPreferredSize().width,label.getPreferredSize().height);
		height+=label.getPreferredSize().height+5;
		int finalWidth = width+label.getPreferredSize().width;
		panel.add(ok);
		ok.addActionListener(this);
		ok.setBounds(width,height,ok.getPreferredSize().width,ok.getPreferredSize().height);
		width+=15+ok.getPreferredSize().width;
		
		panel.add(cancel);
		cancel.addActionListener(this);
		cancel.setBounds(width,height,cancel.getPreferredSize().width,cancel.getPreferredSize().height);
		height+=15+ok.getPreferredSize().width;
		width+=15+cancel.getPreferredSize().width;
		ok.setActionMap(null);

		getContentPane().add(panel);
		setTitle("Attack Post");
		setSize(finalWidth+10,height);
		int x = MyAttackPane.getBounds().x+MyAttackPane.getBounds().width;
		int y = MyAttackPane.getBounds().y+MyAttackPane.getBounds().height;
		//setLocationRelativeTo(MyForumPanel);
		double random = Math.random();
		int xL = (int)((double)x*random)-MyAttackPane.getBounds().x;
		int yL = (int)((double)y*random)-MyAttackPane.getBounds().y;
		setLocation(xL,yL);
		setModal(false);
		setVisible(true);
	}
	
	/**
	 * called when a button is pushed
	 * @param e action event.
	 * */
	public void actionPerformed(ActionEvent e){
		if(e.getSource()==cancel)
			setVisible(false);
		if(e.getSource()==ok){
			MyAttackPane.attack();
			setVisible(false);
		}
		
		
		
	}	
}
