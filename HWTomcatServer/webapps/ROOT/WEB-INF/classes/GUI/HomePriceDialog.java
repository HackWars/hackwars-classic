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

public class HomePriceDialog extends JDialog implements ActionListener{
	
	private JPanel panel=new JPanel();
	private JSpinner spinner;
	private SpinnerNumberModel model;
	private Home MyHome;
	private JButton ok=new JButton("Set"), cancel=new JButton("Cancel");
	public HomePriceDialog(Home MyHome){
		
		this.MyHome=MyHome;
		//SpringLayout layout = new SpringLayout();
		panel.setLayout(null);
		int width=2;
		int height=2;
		JLabel label = new JLabel("Price: ");
		panel.add(label);
		label.setBounds(width,height,label.getPreferredSize().width,label.getPreferredSize().height);
		width+=label.getPreferredSize().width+5;
		
		
		
		model = new SpinnerNumberModel(0,0,100000000000.0f,1);
		spinner = new JSpinner(model);
		panel.add(spinner);
		spinner.setBounds(width,height,spinner.getPreferredSize().width,label.getPreferredSize().height);
		height+=spinner.getPreferredSize().height+5;
		
		panel.add(ok);
		ok.addActionListener(this);
		ok.setBounds(width,height,ok.getPreferredSize().width,ok.getPreferredSize().height);
		width+=15+ok.getPreferredSize().width;
		
		panel.add(cancel);
		cancel.addActionListener(this);
		cancel.setBounds(width,height,cancel.getPreferredSize().width,cancel.getPreferredSize().height);
		height+=15+cancel.getPreferredSize().width;
		width=label.getPreferredSize().width+spinner.getPreferredSize().width+30;
		
		getContentPane().add(panel);
		setTitle("Set Price");
		setSize(width,height);
		setLocationRelativeTo(MyHome);
		setModal(false);
		setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e){
		if(e.getSource()==cancel)
			setVisible(false);
		if(e.getSource()==ok){
			MyHome.changePrice("",(float)((Double)spinner.getValue()).floatValue());
			setVisible(false);
		}
		
		
		
	}	
}
