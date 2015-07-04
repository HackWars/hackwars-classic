package GUI;
/**

Deposit.java
this is the deposit window.
*/

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.*;
import Assignments.*;
import View.*;
import Game.Port;
import java.text.*;
import java.math.*;
import java.util.Vector; // Vector
import java.util.HashMap;
import net.miginfocom.swing.*;

public class Deposit extends Application{
	private JDesktopPane mainPanel=null;
	private Hacker MyHacker=null;
	private JSpinner spinner;
	private int usePort;
    private JComboBox bankPortsCbo;
    private Vector bankPorts = new Vector();
    private HashMap bankPortInts = new HashMap();

    private JButton depositButton = new JButton("Deposit");
    private JButton depositAllButton = new JButton("Deposit All");
    private JFormattedTextField formattedTextField;
    
	public Deposit(String name,boolean resize,boolean max,boolean close,boolean iconify,JDesktopPane mainPanel,Hacker MyHacker,int port){
		this.setTitle(name);
		this.setResizable(false);
		this.setMaximizable(max);
		this.setClosable(close);
		this.setIconifiable(iconify);
		this.addInternalFrameListener(this);
		this.mainPanel=mainPanel;
		this.MyHacker=MyHacker;
		this.usePort=port;
		this.setFrameIcon(ImageLoader.getImageIcon("images/calc.png"));
		//setBackground(MyHacker.getColour());
	}

	public void populate(){
		//get amount in petty cash here.
        setLayout(new MigLayout("wrap 2,fillx,ins 10"));
        
        JLabel depositWithLabel = new JLabel("Deposit using port: ");
        this.add(depositWithLabel);
		
        bankPortsCbo = new JComboBox();
        bankPortsCbo.setEditable(false);
        populateBankPorts();
        setSelectedBankPort();
        
        this.add(bankPortsCbo,"growx");
        
		float pettyCash = MyHacker.getPettyCash();
		pettyCash*=100;
		pettyCash=(float)Math.floor(pettyCash);
		pettyCash/=100;
		NumberFormat nf = NumberFormat.getCurrencyInstance();
        JLabel label = new JLabel("Petty Cash:");
        add(label);
		JLabel pettyCashLabel = new JLabel(nf.format(pettyCash));
		//Font f = new Font("dialog",Font.PLAIN,12);
		//pettyCashLabel.setFont(f);
		//pettyCashLabel.setForeground(Color.WHITE);
		this.add(pettyCashLabel,"align trailing");
		Insets iFrameInsets = this.getInsets();
		Dimension dim = pettyCashLabel.getPreferredSize();
		//pettyCashLabel.setBounds(iFrameInsets.left+10,iFrameInsets.top+10,dim.width,dim.height);

        label = new JLabel("Amount:");
        add(label);
        
		SpinnerModel model = new SpinnerNumberModel(0,0,pettyCash,1);
		spinner = new JSpinner(model);
		//spinner.setBackground(MyHacker.getColour());
		//spinner.setForeground(Color.WHITE);
		DecimalFormat formatter = new DecimalFormat();
		formatter.setMaximumFractionDigits(2);
		formattedTextField = new JFormattedTextField(formatter);//(JFormattedTextField)spinner.getEditor().getComponents()[0];
		formattedTextField.setHorizontalAlignment(JTextField.RIGHT);
        formattedTextField.setBorder(BorderFactory.createLineBorder(Color.black));
		this.add(formattedTextField,"growx");
		dim = spinner.getPreferredSize();
		//spinner.setBounds(iFrameInsets.left+10,iFrameInsets.top+30,dim.width,dim.height);
        // show all the bank ports this person has in a combo box
        //Vector bankPorts = new Vector();
		iFrameInsets = this.getInsets();
		dim = bankPortsCbo.getPreferredSize();
		//bankPortsCbo.setBounds(iFrameInsets.left+10+depositWithLabel.getWidth()+10,iFrameInsets.top+50,47,dim.height);
        
		this.add(depositButton);
		Dimension dimB = depositButton.getPreferredSize();
		//depositButton.setBounds(iFrameInsets.left+dim.width+20,iFrameInsets.top+80,dimB.width,dimB.height);
		depositButton.addActionListener(this);
		
		this.add(depositAllButton,"growx");
		Dimension dimC = depositAllButton.getPreferredSize();
		//depositAllButton.setBounds(iFrameInsets.left+dim.width+20+dimB.width+20,iFrameInsets.top+80,dimC.width,dimC.height);
		depositAllButton.addActionListener(this);
		
		
		formattedTextField.requestFocusInWindow();
		formattedTextField.addActionListener(this);
		formattedTextField.setActionCommand("Deposit");
		formattedTextField.selectAll();
        pack();
	}

    public void setUsePort(int port) {
        this.usePort = port;
    }
    
    public void populateBankPorts() {
        bankPorts = MyHacker.getBankPorts();
        bankPortsCbo.removeAllItems();
        bankPortInts.clear();
        for (int i = 0; i < bankPorts.size(); i++) {
            String bp = (String)bankPorts.get(i);
            String bpInt = (bp.split(":"))[0];
            bankPortsCbo.addItem(bp);
            bankPortInts.put(bpInt, i);
        }
    }
    
    public void setSelectedBankPort() {
        // set the combo box either to (a) the port they selected (b) their default, if it's on or (c) the first port in the list that isn't default that is on.
        bankPortsCbo.setEnabled(true);
		depositButton.setEnabled(true);
		depositAllButton.setEnabled(true);
        if (bankPortInts.get("" + usePort) != null) {
            bankPortsCbo.setSelectedIndex((Integer)bankPortInts.get("" + usePort));
        } else if (bankPortInts.get("" + MyHacker.getDefaultBank()) != null) {
            bankPortsCbo.setSelectedIndex((Integer)bankPortInts.get("" + MyHacker.getDefaultBank()));
        } else if (!bankPorts.isEmpty()) {
            bankPortsCbo.setSelectedIndex(0);
        } else {
            bankPortsCbo.setEnabled(false);
            // should disable the Deposit/Deposit All buttons, but they aren't created yet, so ...fuck all y'all!
			depositButton.setEnabled(false);
			depositAllButton.setEnabled(false);
        }
        bankPortsCbo.validate();
        bankPortsCbo.repaint();
    }

	public void internalFrameClosed(InternalFrameEvent e) {
		MyHacker.setDepositOpen(false);
	}

	public void actionPerformed(ActionEvent e){
		float amount=0.0f;
		if(e.getActionCommand().equals("Deposit")){
			Object value = formattedTextField.getValue();
			if(value instanceof Long){
				amount = (float)(long)(Long)value;//Float.parseFloat(formattedTextField.getText());//(float)((Double)spinner.getValue()).floatValue();
			}
			else if(value instanceof Double){
				amount = (float)(double)(Double)value;
			}
		}else
			amount=MyHacker.getPettyCash();
		if(amount<0.0f)
			amount = 0.0f;
		View MyView = MyHacker.getView();
		//get ip from main class.
		//String ip="192.168.2.100";
		String ip = MyHacker.getEncryptedIP();
        int port = (new Integer((((String)bankPortsCbo.getSelectedItem()).split(":")[0]))).intValue();
		Object objects[] = {new Float(amount),ip,new Integer(port)};
		MyView.setFunction("deposit");
		MyView.addFunctionCall(new RemoteFunctionCall(0,"deposit",objects));
		this.hide();
		mainPanel.repaint();
		MyHacker.setDepositOpen(false);
	}
	
}
