package GUI;
/**

Withdraw.java
this is the deposit window.
*/

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import Assignments.*;
import View.*;
import java.text.*;
import java.math.*;
import java.util.Vector; // Vector
import net.miginfocom.swing.*;
import java.util.HashMap;

public class Withdraw extends Application{
	private JDesktopPane mainPanel=null;
	private Hacker MyHacker=null;
	private JSpinner spinner;
	private JFormattedTextField formattedTextField;
	private JButton withdrawButton = new JButton("Withdraw");
	private int usePort;
    private Vector bankPorts = new Vector();
    private HashMap bankPortInts = new HashMap();

    private JComboBox bankPortsCbo;
	
	public Withdraw(String name,boolean resize,boolean max,boolean close,boolean iconify,JDesktopPane mainPanel,Hacker MyHacker,int port){
		this.setTitle(name);
		this.setResizable(resize);
		this.setMaximizable(max);
		this.setClosable(close);
		this.setIconifiable(iconify);
		this.addInternalFrameListener(this);
		this.mainPanel=mainPanel;
		this.MyHacker=MyHacker;
		this.usePort=port;
		this.setFrameIcon(ImageLoader.getImageIcon("images/calc.png"));
	}

	public void populate(){
		setLayout(new MigLayout("wrap 2,fillx,ins 10"));
		
		JLabel depositWithLabel = new JLabel("Withdraw using port: ");
        this.add(depositWithLabel);
		
        bankPortsCbo = new JComboBox();
        bankPortsCbo.setEditable(false);
        populateBankPorts();
        setSelectedBankPort();
        
        this.add(bankPortsCbo,"growx");
		
		float bankMoney = MyHacker.getBankMoney();
		bankMoney*=100;
		bankMoney=(float)Math.floor(bankMoney);
		bankMoney/=100;
		NumberFormat nf = NumberFormat.getCurrencyInstance();
		JLabel bankMoneyLabel = new JLabel("Bank Money:");
		this.add(bankMoneyLabel);
		
		JLabel money = new JLabel(nf.format(bankMoney));
		add(money,"align trailing");
		Insets iFrameInsets = this.getInsets();
		Dimension dim = bankMoneyLabel.getPreferredSize();
		//bankMoneyLabel.setBounds(iFrameInsets.left+10,iFrameInsets.top+10,dim.width,dim.height);

		JLabel label = new JLabel("Amount:");
        add(label);
		
		SpinnerModel model = new SpinnerNumberModel(0,0,bankMoney,1);
		spinner = new JSpinner(model);
		//this.add(spinner);
		dim = spinner.getPreferredSize();
		//spinner.setBounds(iFrameInsets.left+10,iFrameInsets.top+30,dim.width,dim.height);
		DecimalFormat formatter = new DecimalFormat();
		formatter.setMaximumFractionDigits(2);
		formattedTextField = new JFormattedTextField(formatter);//(JFormattedTextField)spinner.getEditor().getComponents()[0];
		formattedTextField.setHorizontalAlignment(JTextField.RIGHT);
        formattedTextField.setBorder(BorderFactory.createLineBorder(Color.black));
		this.add(formattedTextField,"growx");
		
		/*spinner.getEditor().addActionListener(this);
		spinner.setActionCommand("Withdraw");*/

		this.add(withdrawButton,"growx,skip 1");
		/*Dimension dimB = button.getPreferredSize();
		button.setBounds(iFrameInsets.left+dim.width+20,iFrameInsets.top+30,dimB.width,dimB.height);*/
		withdrawButton.addActionListener(this);
		
		formattedTextField.setBorder(BorderFactory.createLineBorder(Color.black));
		formattedTextField.requestFocusInWindow();
		formattedTextField.addActionListener(this);
		formattedTextField.setActionCommand("Withdraw");
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
		withdrawButton.setEnabled(true);
        if (bankPortInts.get("" + usePort) != null) {
            bankPortsCbo.setSelectedIndex((Integer)bankPortInts.get("" + usePort));
        } else if (bankPortInts.get("" + MyHacker.getDefaultBank()) != null) {
            bankPortsCbo.setSelectedIndex((Integer)bankPortInts.get("" + MyHacker.getDefaultBank()));
        } else if (!bankPorts.isEmpty()) {
            bankPortsCbo.setSelectedIndex(0);
        } else {
            bankPortsCbo.setEnabled(false);
			withdrawButton.setEnabled(false);
        }
        bankPortsCbo.validate();
        bankPortsCbo.repaint();
    }

	
	public void internalFrameClosed(InternalFrameEvent e) {
		MyHacker.setWithdrawOpen(false);
	}

	public void actionPerformed(ActionEvent e){
		if(e.getActionCommand().equals("Withdraw")){
			float amount = 0.0f;
			Object value = formattedTextField.getValue();
			if(value instanceof Long){
				amount = (float)(long)(Long)value;//Float.parseFloat(formattedTextField.getText());//(float)((Double)spinner.getValue()).floatValue();
			}
			else if(value instanceof Double){
				amount = (float)(double)(Double)value;
			}
			View MyView = MyHacker.getView();
			//get ip from main class.
			//String ip="192.168.2.100";
			String ip = MyHacker.getEncryptedIP();
			int port = (new Integer((((String)bankPortsCbo.getSelectedItem()).split(":")[0]))).intValue();
			Object objects[] = {new Float(amount),ip,new Integer(port)};
			MyView.setFunction("withdraw");
			MyView.addFunctionCall(new RemoteFunctionCall(0,"withdraw",objects));
		}
		this.dispose();
		mainPanel.repaint();
		MyHacker.setWithdrawOpen(false);
	}
	
	
}
