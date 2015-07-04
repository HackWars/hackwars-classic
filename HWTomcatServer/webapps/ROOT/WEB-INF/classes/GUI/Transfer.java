package GUI;
/**

Transfer.java
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
import java.util.Vector;
import java.math.*;
import net.miginfocom.swing.*;
import java.util.HashMap;

public class Transfer extends Application implements UndoableEditListener,FocusListener{
	private JDesktopPane mainPanel=null;
	private Hacker MyHacker=null;
	private JSpinner spinner;
	private JTextField ipField1,ipField2,ipField3,ipField4;
	private IPPanel ipPanel;
	private JButton transferButton=new JButton("Transfer");
	private JFormattedTextField formattedTextField;
	private int usePort;
    private HashMap bankPortInts = new HashMap();
    private Vector bankPorts = new Vector();
    private JComboBox bankPortsCbo;
	
	public Transfer(String name,boolean resize,boolean max,boolean close,boolean iconify,JDesktopPane mainPanel,Hacker MyHacker,int port){
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
		
		JLabel depositWithLabel = new JLabel("Transfer using port: ");
        this.add(depositWithLabel);
		
        bankPortsCbo = new JComboBox();
        bankPortsCbo.setEditable(false);
        populateBankPorts();
        setSelectedBankPort();
        
        this.add(bankPortsCbo,"growx");
		
		//get amount in petty cash here.
		float bankMoney = MyHacker.getPettyCash();
		bankMoney*=100;
		bankMoney=(float)Math.floor(bankMoney);
		bankMoney/=100;
		NumberFormat nf = NumberFormat.getCurrencyInstance();
		JLabel bankMoneyLabel = new JLabel("Petty Cash:");
		this.add(bankMoneyLabel);
		
		JLabel label = new JLabel(nf.format(bankMoney));
		add(label,"align trailing");
		
		SpinnerModel model = new SpinnerNumberModel(0,0,bankMoney,1);
		spinner = new JSpinner(model);
		//this.add(spinner);
		
		label = new JLabel("Amount:");
        add(label);
		DecimalFormat formatter = new DecimalFormat();
		formatter.setMaximumFractionDigits(2);
		formattedTextField = new JFormattedTextField(formatter);//(JFormattedTextField)spinner.getEditor().getComponents()[0];
		formattedTextField.setHorizontalAlignment(JTextField.RIGHT);
		add(formattedTextField,"growx");
		
		JLabel ipLabel = new JLabel("IP to transfer to:");
		this.add(ipLabel);
		
		ipPanel = new IPPanel(MyHacker);
		add(ipPanel,"align trailing");

        
		this.add(transferButton,"skip 1,growx");
		transferButton.addActionListener(this);
	
		formattedTextField.setBorder(BorderFactory.createLineBorder(Color.black));
		formattedTextField.requestFocusInWindow();
		formattedTextField.addActionListener(this);
		formattedTextField.setActionCommand("Transfer");
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
		transferButton.setEnabled(true);
        if (bankPortInts.get("" + usePort) != null) {
            bankPortsCbo.setSelectedIndex((Integer)bankPortInts.get("" + usePort));
        } else if (bankPortInts.get("" + MyHacker.getDefaultBank()) != null) {
            bankPortsCbo.setSelectedIndex((Integer)bankPortInts.get("" + MyHacker.getDefaultBank()));
        } else if (!bankPorts.isEmpty()) {
            bankPortsCbo.setSelectedIndex(0);
        } else {
            bankPortsCbo.setEnabled(false);
			transferButton.setEnabled(false);
        }
        bankPortsCbo.validate();
        bankPortsCbo.repaint();
    }
	
	
	public void internalFrameClosed(InternalFrameEvent e) {
		MyHacker.setTransferOpen(false);
	}
	
	public void actionPerformed(ActionEvent e){
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
		String targetIP=ipPanel.getIP();//ipField1.getText()+"."+ipField2.getText()+"."+ipField3.getText()+"."+ipField4.getText();
		Object objects[] = {new Float(amount),ip,targetIP,new Integer(port)};
		MyView.setFunction("transfer");
		MyView.addFunctionCall(new RemoteFunctionCall(0,"transfer",objects));
		this.hide();
		mainPanel.repaint();
		MyHacker.setTransferOpen(false);
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
