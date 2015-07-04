package GUI;
/**

AttackPane.java
this is the attack window.
*/

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import Assignments.*;
import View.*;
import java.lang.*;
import Game.*;
import java.text.*;

public class ZombieAttackPane extends Application implements ChangeListener,UndoableEditListener,FocusListener{
	private JDesktopPane mainPanel=null;
	private Hacker MyHacker=null;
	private JTextField ipField1,ipField2,ipField3,ipField4;
	private JSpinner portSpinner;
	private String ipValue;
	private int portValue;
	private JButton button;
	private JTextPane textPane;
	private JTabbedPane tabbedPane;
	private int port;
	private JTextField bankScript,attackScript,FTPScript;
	private JComboBox ports;
	private String bankFolder,attackFolder,FTPFolder;
	private String bankMaliciousIP,FTPMaliciousIP;
	private float bankPettyCashTarget,pettyCashTarget;
	private JSpinner pettyCashSpinner;
	private JTextField addPort;
	private JLabel damageDone;
	private float damage=0.0f;
	private JCheckBox confirm,details;
	private IPPanel ipPanel;
	public ZombieAttackPane(String name,boolean resize,boolean max,boolean close,boolean iconify,JDesktopPane mainPanel,Hacker MyHacker,String ipValue,int portValue,int port){
		this.setTitle(name);
		this.setResizable(resize);
		this.setMaximizable(max);
		this.setClosable(close);
		this.setIconifiable(iconify);
		this.addInternalFrameListener(this);
		setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
		this.mainPanel=mainPanel;
		this.MyHacker=MyHacker;
		this.ipValue=ipValue;
		this.portValue=portValue;
		this.port=port;
		//this.setFrameIcon(ImageLoader.getImageIcon("images/calc.png"));
	}

	public void populate(){
		Insets insets = this.getInsets();
		JLabel label = new JLabel("IP: ");
		this.add(label);
		Dimension dL = label.getPreferredSize();
		label.setBounds(insets.left+5,insets.top+5,dL.width,dL.height);
		
		
		label = new JLabel("Port: ");
		this.add(label);
		dL = label.getPreferredSize();
		label.setBounds(insets.left+5,insets.top+25,dL.width,dL.height);
		int position=insets.left+dL.width+10;
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
		
		SpinnerModel model = new SpinnerNumberModel(portValue,0,31,1);
		portSpinner = new JSpinner(model);
		this.add(portSpinner);
		d = portSpinner.getPreferredSize();
		portSpinner.setBounds(insets.left+dL.width+10,insets.top+25,d.width,d.height);
		
		JLabel ipLabel = new JLabel("Zombie IP: "+ipValue);
		add(ipLabel);
		ipLabel.setBounds(insets.left+5,insets.top+45,ipLabel.getPreferredSize().width,ipLabel.getPreferredSize().height);
		
		JLabel portLabel = new JLabel("Port: "+portValue);
		add(portLabel);
		portLabel.setBounds(insets.left+15+ipLabel.getPreferredSize().width,insets.top+45,portLabel.getPreferredSize().width,portLabel.getPreferredSize().height);
		
		button=new JButton("Attack");
		this.add(button);
		d = button.getPreferredSize();
		button.setBounds(insets.left+5,insets.top+65,d.width,d.height);
		button.addActionListener(this);
		
		damageDone = new JLabel("Damage Done: 199");
		add(damageDone);
		d = damageDone.getPreferredSize();
		damageDone.setText("Damage Done: 0");
		damageDone.setBounds(insets.left+330-d.width,insets.top+25,d.width,d.height);
		
		
		confirm = new JCheckBox();
		
		JLabel clabel = new JLabel("Show confirm dialog");
		add(clabel);
		int cwidth = clabel.getPreferredSize().width;
		clabel.setBounds(insets.left+330-cwidth,insets.top+62,cwidth,clabel.getPreferredSize().height);
		add(confirm);
		d = confirm.getPreferredSize();
		confirm.setBounds(insets.left+330-cwidth-d.width-5,insets.top+60,d.width,d.height);
		confirm.setSelected(MyHacker.getShowAttack());
		confirm.addActionListener(this);
		confirm.setActionCommand("Confirm");
		
		
		details = new JCheckBox();
		
		clabel = new JLabel("Show details");
		add(clabel);
		cwidth = clabel.getPreferredSize().width;
		clabel.setBounds(insets.left+330-cwidth,insets.top+77,cwidth,clabel.getPreferredSize().height);
		add(details);
		d = confirm.getPreferredSize();
		details.setBounds(insets.left+330-cwidth-d.width-5,insets.top+75,d.width,d.height);
		details.setSelected(MyHacker.getShowAttackDetails());
		details.addActionListener(this);
		details.setActionCommand("Details");
		
		tabbedPane = new JTabbedPane();
		this.add(tabbedPane);
		tabbedPane.setBounds(insets.left+15,insets.top+85+d.height,325,240);
		textPane = new JTextPane();
		textPane.setEditable(false);
		JScrollPane scrollPanel = new JScrollPane(textPane);
		
		JPanel optionsPanel = new JPanel();
		SpringLayout layout = new SpringLayout();
		optionsPanel.setLayout(layout);
		optionsPanel.setBackground(new Color(255,255,255));
		JButton cancelButton = new JButton("Cancel Attack");
		cancelButton.setActionCommand("Cancel");
		cancelButton.addActionListener(this);
		optionsPanel.add(cancelButton);
		layout.putConstraint(SpringLayout.NORTH,cancelButton,2,SpringLayout.NORTH,optionsPanel);
		layout.putConstraint(SpringLayout.WEST,cancelButton,2,SpringLayout.WEST,optionsPanel);
	
		label = new JLabel("Secondary Ports:");
		optionsPanel.add(label);
		layout.putConstraint(SpringLayout.NORTH,label,7+cancelButton.getPreferredSize().height,SpringLayout.NORTH,cancelButton);
		layout.putConstraint(SpringLayout.WEST,label,2,SpringLayout.WEST,optionsPanel);
		
		addPort = new JTextField(2);
		optionsPanel.add(addPort);
		layout.putConstraint(SpringLayout.NORTH,addPort,5+cancelButton.getPreferredSize().height,SpringLayout.NORTH,cancelButton);
		layout.putConstraint(SpringLayout.WEST,addPort,label.getPreferredSize().width+5,SpringLayout.WEST,label);
		
		JButton button = new JButton(ImageLoader.getImageIcon("images/add.png"));
		button.setMargin(new Insets(0,0,0,0));
		button.setHorizontalAlignment(SwingConstants.LEFT);
		button.setVerticalAlignment(SwingConstants.TOP);
		button.setActionCommand("add");
		button.addActionListener(this);
		optionsPanel.add(button);
		layout.putConstraint(SpringLayout.NORTH,button,5+cancelButton.getPreferredSize().height,SpringLayout.NORTH,cancelButton);
		layout.putConstraint(SpringLayout.WEST,button,addPort.getPreferredSize().width+5,SpringLayout.WEST,addPort);
		
		ports = new JComboBox();
		ports.setEditable(false);
		ports.setMaximumRowCount(5);
		ports.addActionListener(this);
		ports.setActionCommand("Port");
		optionsPanel.add(ports);
		layout.putConstraint(SpringLayout.NORTH,ports,5+addPort.getPreferredSize().height,SpringLayout.NORTH,addPort);
		layout.putConstraint(SpringLayout.WEST,ports,label.getPreferredSize().width+5,SpringLayout.WEST,label);
		
		button = new JButton(ImageLoader.getImageIcon("images/remove.png"));
		button.setMargin(new Insets(0,0,0,0));
		button.setHorizontalAlignment(SwingConstants.LEFT);
		button.setVerticalAlignment(SwingConstants.TOP);
		button.setActionCommand("remove");
		button.addActionListener(this);
		optionsPanel.add(button);
		layout.putConstraint(SpringLayout.NORTH,button,5+addPort.getPreferredSize().height,SpringLayout.NORTH,addPort);
		layout.putConstraint(SpringLayout.WEST,button,ports.getPreferredSize().width+15,SpringLayout.WEST,ports);
		
		label = new JLabel("Petty Cash Target:");
		optionsPanel.add(label);
		layout.putConstraint(SpringLayout.NORTH,label,5+ports.getPreferredSize().height,SpringLayout.NORTH,ports);
		layout.putConstraint(SpringLayout.WEST,label,2,SpringLayout.WEST,optionsPanel);
		model = new SpinnerNumberModel(0,0,50000000.0f,1);
		pettyCashSpinner = new JSpinner(model);
		pettyCashSpinner.addChangeListener(this);
		optionsPanel.add(pettyCashSpinner);
		layout.putConstraint(SpringLayout.NORTH,pettyCashSpinner,5+ports.getPreferredSize().height,SpringLayout.NORTH,ports);
		layout.putConstraint(SpringLayout.WEST,pettyCashSpinner,5+label.getPreferredSize().width,SpringLayout.WEST,label);
		tabbedPane.addTab("Terminal",scrollPanel);
		tabbedPane.addTab("Options",optionsPanel);
		
		if(!MyHacker.getShowAttackDetails())
			setBounds(getBounds().x,getBounds().y,getBounds().width,135);
		
	}

	public void addMessage(float damage){
		this.damage+=damage;
		damageDone.setText("Damage Done: "+this.damage);
		textPane.setText(textPane.getText()+"You have hit the port for "+damage+" damage!\n");
		String text=textPane.getText();
		textPane.setCaretPosition(text.length());
	}

	public void internalFrameClosed(InternalFrameEvent e) {
		MyHacker.setAttackOpen(false);
	}
	
	public void setSecondaryPorts(Integer[] add){
		ports.removeAllItems();
		if(add.length>0){
			portSpinner.setValue(add[0]);
			for(int i=1;i<add.length;i++){
				ports.addItem(add[i]);
			}
		}
	}
	
	public void setIP(String ip1,String ip2,String ip3,String ip4){
		ipPanel.setIP(ip1+"."+ip2+"."+ip3+"."+ip4);
	}

	public void actionPerformed(ActionEvent e){
		if(e.getActionCommand().equals("Attack")){
			int n=0;
			if(MyHacker.getShowAttack()){
				NumberFormat nf= NumberFormat.getCurrencyInstance();
				Object[] options = {"Yes","Cancel"};
				n = JOptionPane.showOptionDialog(this,
				    "Are You Sure?\nIt would cost you "+nf.format(20.0f)+" to attack this port.",
				    "Attack",
				    JOptionPane.YES_NO_OPTION,
				    JOptionPane.QUESTION_MESSAGE,
				    null,
				    options,
				    options[1]);
			}
			if(n==0){
				this.damage=0.0f;
				damageDone.setText("Damage Done: "+this.damage);
				//button.setEnabled(false);
				String targetIP = ipPanel.getIP();//ipField1.getText()+"."+ipField2.getText()+"."+ipField3.getText()+"."+ipField4.getText();
				int targetPort = (int)(Integer)portSpinner.getValue();
				textPane.setText("Attack Commencing on "+targetIP+" at port "+targetPort+"...\n");
				View MyView = MyHacker.getView();
				//get ip from main class.
				String ip=MyHacker.getEncryptedIP();
				Integer secondaryPorts[] = new Integer[ports.getItemCount()];
				for(int i=0;i<ports.getItemCount();i++){
					secondaryPorts[i]=(Integer)ports.getItemAt(i);
				}
				String scripts[][] = new String[3][3];
				//scripts[0] = new String[]{bankFolder,bankScript.getText()};
				//scripts[1] = new String[]{FTPFolder,FTPScript.getText()};
				//scripts[2] = new String[]{attackFolder,attackScript.getText()};
				
				Object otherInfo[] = new Object[5];
				otherInfo[0]=bankMaliciousIP;
				otherInfo[1]=(Float)bankPettyCashTarget;
				otherInfo[2]=FTPMaliciousIP;
				otherInfo[3]=(Float)pettyCashTarget;
				otherInfo[4]="";
				//System.out.println(ipValue);
				Object objects[] = {targetIP,targetPort,ipValue,new Integer(portValue),secondaryPorts,null,otherInfo,ip};
				MyView.setFunction("requestzombieattack");
				MyView.addFunctionCall(new RemoteFunctionCall(0,"requestzombieattack",objects));
				//this.hide();
				//MyHacker.setAttackOpen(false);
			}
		}
		if(e.getActionCommand().equals("Cancel")){
			textPane.setText(textPane.getText()+"Attack Canceled by you.\n--------------------------------------\n");
			String ip=MyHacker.getEncryptedIP();
			View MyView = MyHacker.getView();
			Object objects[] = {ipValue,new Integer(portValue),ip};
			MyView.setFunction("requestzombiecancelattack");
			MyView.addFunctionCall(new RemoteFunctionCall(0,"requestzombiecancelattack",objects));
			tabbedPane.setSelectedIndex(0);
			button.setEnabled(true);
		}
		if(e.getActionCommand().equals("add")){
			try{
				int newport = Integer.parseInt(addPort.getText());
				if(newport<0||newport>31)
					throw new NumberFormatException();
				ports.addItem(newport);
			}catch(NumberFormatException ex){
				MyHacker.showMessage("Secondary ports must be a number between 0 and 31");
			}
		}
		if(e.getActionCommand().equals("remove")){
			int index = ports.getSelectedIndex();
			ports.removeItemAt(index);
			
		}
		if(e.getActionCommand().equals("Confirm")){
			boolean c = confirm.isSelected();
			MyHacker.setShowAttack(c);
		}
		if(e.getActionCommand().equals("Details")){
			if(!details.isSelected())
				setBounds(getBounds().x,getBounds().y,getBounds().width,135);
			else
				setBounds(getBounds().x,getBounds().y,getBounds().width,400);
			MyHacker.setShowAttackDetails(details.isSelected());
		}
		
	}
	public void stateChanged(ChangeEvent e) {
			pettyCashTarget=(float)((Double)pettyCashSpinner.getValue()).floatValue();
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

