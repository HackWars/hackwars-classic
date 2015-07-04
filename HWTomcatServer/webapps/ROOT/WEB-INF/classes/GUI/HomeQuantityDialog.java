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
import java.text.*;

public class HomeQuantityDialog extends JInternalFrame implements ActionListener{
	
	private Home MyHome;
	private JPanel panel=new JPanel();
	private int maxQuantity;
	private String name;
	private JSpinner spinner;
	private SpinnerModel model;
	private JLabel moneyLabel;
	private float price;
	private int index;
	private JButton ok=new JButton("Sell"),cancel = new JButton("Cancel"), sellAll = new JButton("Sell All");
	
	public HomeQuantityDialog(Home MyHome, Hacker MyHacker,int maxQuantity,String name,float price,int index){
		this.MyHome=MyHome;
		this.maxQuantity=maxQuantity;
		this.name=name;
		this.price = price;
		this.index = index;
		SpringLayout layout = new SpringLayout();
		panel.setLayout(layout);
		int width=2;
		int height=2;
		NumberFormat nf = NumberFormat.getCurrencyInstance();
		moneyLabel = new JLabel("<html>Are You Sure you want to sell "+name+"?<br>Buy Price:  "+nf.format(price)+"</html>");
		panel.add(moneyLabel);
		layout.putConstraint(SpringLayout.NORTH,moneyLabel,height,SpringLayout.NORTH,panel);
		layout.putConstraint(SpringLayout.WEST,moneyLabel,width,SpringLayout.WEST,panel);
		height+=moneyLabel.getPreferredSize().height+10;
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

		panel.add(sellAll);
		sellAll.addActionListener(this);
		layout.putConstraint(SpringLayout.NORTH,sellAll,height,SpringLayout.NORTH,panel);
		layout.putConstraint(SpringLayout.WEST,sellAll,5+width,SpringLayout.WEST,panel);
		width+=5+sellAll.getPreferredSize().width+5;
		
		panel.add(cancel);
		cancel.addActionListener(this);
		layout.putConstraint(SpringLayout.NORTH,cancel,height,SpringLayout.NORTH,panel);
		layout.putConstraint(SpringLayout.WEST,cancel,width,SpringLayout.WEST,panel);
		width+=5+cancel.getPreferredSize().width;
		height+=ok.getPreferredSize().height+5;
		
		getContentPane().add(panel);
		setTitle("Sell File");
		MyHacker.getPanel().add(this);
		setBounds(100,100,350,height+50);
		//setLocationRelativeTo(MyHome);
		//setFrameIcon(ImageLoader.getImageIcon("images/functioncall.png"));
		//setModal(false);
		setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e){
		
		if(e.getSource()==ok){
			MyHome.sellFile(index,(int)((Integer)spinner.getValue()).intValue());
			setVisible(false);
		}
        if (e.getSource()==sellAll) {
            MyHome.sellFile(index,maxQuantity);
            setVisible(false);
        }
		if(e.getSource()==cancel){
			//System.out.println("Canceled");
			setVisible(false);
		}
		
	}	
}
