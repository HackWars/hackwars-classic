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

public class FTPQuantityDialog extends JDialog implements ActionListener{
	
	private FTP MyFTP;
	private JPanel panel=new JPanel();
	private int maxQuantity;
	private String name;
	private JSpinner spinner;
	private SpinnerModel model;
	private JButton ok=new JButton("Go"),cancel = new JButton("Cancel");
	
	public FTPQuantityDialog(FTP MyFTP, int maxQuantity,String name){
		if(maxQuantity<0)
			maxQuantity=1;
		this.MyFTP=MyFTP;
		this.maxQuantity=maxQuantity;
		this.name=name;
		SpringLayout layout = new SpringLayout();
		panel.setLayout(layout);
		int width=2;
		int height=2;
		JLabel label = new JLabel("Quantity");
		panel.add(label);
		layout.putConstraint(SpringLayout.NORTH,label,height,SpringLayout.NORTH,panel);
		layout.putConstraint(SpringLayout.WEST,label,width,SpringLayout.WEST,panel);
		width+=label.getPreferredSize().width+5;
		model = new SpinnerNumberModel(1,1,maxQuantity,1);	
		spinner = new JSpinner(model);
		panel.add(spinner);
		layout.putConstraint(SpringLayout.NORTH,spinner,height,SpringLayout.NORTH,panel);
		layout.putConstraint(SpringLayout.WEST,spinner,width,SpringLayout.WEST,panel);
		height+=spinner.getPreferredSize().height+5;
		
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
		setTitle("Set Quantity");
		setSize(300,100);
		setLocationRelativeTo(MyFTP);
		//setFrameIcon(ImageLoader.getImageIcon("images/functioncall.png"));
		setModal(false);
		setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e){
		
		if(e.getSource()==ok){
			MyFTP.setQuantity(name,(int)((Integer)spinner.getValue()).intValue());
			setVisible(false);
		}
		if(e.getSource()==cancel){
			//System.out.println("Canceled");
			setVisible(false);
		}
		
	}	
}
