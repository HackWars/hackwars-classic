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

public class WatchQuantityDialog extends JDialog implements ActionListener{
	
	private WatchManager MyWatchManager;
	private JPanel panel=new JPanel();
	private int type,watchID;
	private JSpinner spinner;
	private SpinnerModel model;
	private JButton ok=new JButton("Set"),cancel = new JButton("Cancel");
	
	public WatchQuantityDialog(WatchManager MyWatchManager, int type,int watchID){
		this.type=type;
		this.MyWatchManager=MyWatchManager;
		this.watchID=watchID;
		SpringLayout layout = new SpringLayout();
		panel.setLayout(layout);
		int width=2;
		int height=2;
		JLabel label = new JLabel();
		if(type==0)
			label.setText("Health Limit: ");
		else
			label.setText("Petty Cash Value: ");
		panel.add(label);
		layout.putConstraint(SpringLayout.NORTH,label,height,SpringLayout.NORTH,panel);
		layout.putConstraint(SpringLayout.WEST,label,width,SpringLayout.WEST,panel);
		width+=label.getPreferredSize().width+5;
		
		if(type==0)
			model = new SpinnerNumberModel(0,0,100.0f,1);
		else
			model = new SpinnerNumberModel(0,0,100000000000.0f,1);
			
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
		if(type==0)
			setTitle("Health Limit");
		else
			setTitle("Petty Cash Value");
		setSize(300,100);
		setLocationRelativeTo(MyWatchManager);
		//setFrameIcon(ImageLoader.getImageIcon("images/functioncall.png"));
		setModal(false);
		setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e){
		
		if(e.getSource()==ok){
			MyWatchManager.setQuantity(watchID,(float)((Double)spinner.getValue()).floatValue());
			setVisible(false);
		}
		if(e.getSource()==cancel){
			//System.out.println("Canceled");
			setVisible(false);
		}
		
	}	
}
