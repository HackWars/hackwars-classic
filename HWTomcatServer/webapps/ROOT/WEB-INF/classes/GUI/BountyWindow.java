package GUI;


import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import View.*;
import Assignments.*;


public class BountyWindow extends JDialog implements ActionListener,UndoableEditListener,FocusListener,ItemListener{
 
	private final String[] types = {"Scan","Attack","Install Script","Vote","Change HTTP Target","Destroy Watches"};
	private Hacker MyHacker=null;
	private JCheckBox anonymous,anyPlayer;
	private JTextField ipField1,ipField2,ipField3,ipField4;
	private JTextField fileField;
	private JButton ok=new JButton("Create"),cancel=new JButton("Cancel"),browse=new JButton("Browse");
	private JSpinner rewardSpinner,iterationSpinner;
	private JComboBox type;
	private String folder;
	
	public BountyWindow(Hacker MyHacker){
		this.MyHacker=MyHacker;
		
		JPanel panel = new JPanel();
		panel.setLayout(null);
		
		int width=2;
		int height=2;
		
		JLabel label = new JLabel("Anonymous: ");
		int fieldX = new JLabel("# of iterations: ").getPreferredSize().width+10;
		panel.add(label);
		label.setBounds(width,height,label.getPreferredSize().width,label.getPreferredSize().height);
		
		anonymous = new JCheckBox();
		panel.add(anonymous);
		anonymous.setBounds(fieldX,height,anonymous.getPreferredSize().width,anonymous.getPreferredSize().height);
		height+=5+anonymous.getPreferredSize().height;
		
		label = new JLabel("Target: ");
		panel.add(label);
		label.setBounds(width,height,label.getPreferredSize().width,label.getPreferredSize().height);
		
		int position=fieldX;
		ipField1 = new JTextField(3);
		this.add(ipField1);
		Dimension d = ipField1.getPreferredSize();
		ipField1.setBounds(position,height,d.width,d.height);
		position+=d.width;
		
		JLabel dot = new JLabel(".");
		d = dot.getPreferredSize();
		add(dot);
		dot.setBounds(position,height,d.width,d.height);
		position+=d.width;
		
		ipField2 = new JTextField(3);
		this.add(ipField2);
		d = ipField2.getPreferredSize();
		ipField2.setBounds(position,height,d.width,d.height);
		position+=d.width;
		
		dot = new JLabel(".");
		d = dot.getPreferredSize();
		add(dot);
		dot.setBounds(position,height,d.width,d.height);
		position+=d.width;
		
		ipField3 = new JTextField(2);
		this.add(ipField3);
		d = ipField3.getPreferredSize();
		ipField3.setBounds(position,height,d.width,d.height);
		position+=d.width;
		
		dot = new JLabel(".");
		d = dot.getPreferredSize();
		add(dot);
		dot.setBounds(position,height,d.width,d.height);
		position+=d.width;
		
		ipField4 = new JTextField(3);
		this.add(ipField4);
		d = ipField4.getPreferredSize();
		ipField4.setBounds(position,height,d.width,d.height);
		position+=d.width;
		
		ipField1.getDocument().addUndoableEditListener(this);
		ipField2.getDocument().addUndoableEditListener(this);
		ipField3.getDocument().addUndoableEditListener(this);
		ipField4.getDocument().addUndoableEditListener(this);
		ipField1.addFocusListener(this);
		ipField2.addFocusListener(this);
		ipField3.addFocusListener(this);
		ipField4.addFocusListener(this);
		
		anyPlayer = new JCheckBox("Any Player");
		panel.add(anyPlayer);
		anyPlayer.setBounds(position+2,height,anyPlayer.getPreferredSize().width,anyPlayer.getPreferredSize().height);
		anyPlayer.addItemListener(this);
		//anyPlayer.setText("Any Player");
		position+=4+anyPlayer.getPreferredSize().width;
		label = new JLabel("Any Player");
		panel.add(label);
		//label.setBounds(position,height,label.getPreferredSize().width,label.getPreferredSize().height);
		height+=d.height+5;
		label = new JLabel("Type: ");
		panel.add(label);
		label.setBounds(width,height,label.getPreferredSize().width,label.getPreferredSize().height);
		type = new JComboBox(types);
		type.addActionListener(this);
		panel.add(type);
		type.setBounds(fieldX,height,type.getPreferredSize().width,type.getPreferredSize().height);
		height+=type.getPreferredSize().height+5;
		
		label = new JLabel("File: ");
		panel.add(label);
		label.setBounds(width,height,label.getPreferredSize().width,label.getPreferredSize().height);
		fileField = new JTextField(10);
		panel.add(fileField);
		fileField.setEditable(false);
		fileField.setBounds(fieldX,height,fileField.getPreferredSize().width,fileField.getPreferredSize().height);
		
		panel.add(browse);
		browse.addActionListener(this);
		browse.setEnabled(false);
		browse.setBounds(fieldX+5+fileField.getPreferredSize().width,height-2,browse.getPreferredSize().width,browse.getPreferredSize().height);
		height+=fileField.getPreferredSize().height+5;
		
		label = new JLabel("# of iterations: ");
		panel.add(label);
		label.setBounds(width,height,label.getPreferredSize().width,label.getPreferredSize().height);
			
		SpinnerNumberModel model = new SpinnerNumberModel(1,1,1000,1);
		iterationSpinner = new JSpinner(model);
		panel.add(iterationSpinner);
		iterationSpinner.setBounds(fieldX,height,iterationSpinner.getPreferredSize().width,iterationSpinner.getPreferredSize().height);
		height+=15+label.getPreferredSize().height;
		
		label = new JLabel("Reward: ");
		panel.add(label);
		label.setBounds(width,height,label.getPreferredSize().width,label.getPreferredSize().height);
		model = new SpinnerNumberModel(0,0,50000000.0f,1);
		rewardSpinner = new JSpinner(model);
		panel.add(rewardSpinner);
		rewardSpinner.setBounds(fieldX,height,rewardSpinner.getPreferredSize().width,rewardSpinner.getPreferredSize().height);
		height+=15+label.getPreferredSize().height;
		
		panel.add(ok);
		ok.addActionListener(this);
		ok.setBounds(width,height,ok.getPreferredSize().width,ok.getPreferredSize().height);
		width+=15+ok.getPreferredSize().width;
		
		panel.add(cancel);
		cancel.addActionListener(this);
		cancel.setBounds(width,height,cancel.getPreferredSize().width,cancel.getPreferredSize().height);
		height+=cancel.getPreferredSize().height+15;
		
		getContentPane().add(panel);
		setTitle("Create Bounty");
		setSize(350,height+20);
		setLocationRelativeTo(MyHacker.getFrame());
		setModal(false);
		setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e){
		if(e.getSource()==cancel)
			setVisible(false);
		if(e.getSource()==ok){
			View MyView = MyHacker.getView();
			String target="*";
			if(!anyPlayer.isSelected()){
				target=ipField1.getText()+"."+ipField2.getText()+"."+ipField3.getText()+"."+ipField4.getText();
			}
			boolean anon = anonymous.isSelected();
			int types = type.getSelectedIndex();
			String file = fileField.getText();
			int iterations = (int)(Integer)iterationSpinner.getValue();
			float reward = (float)((Double)rewardSpinner.getValue()).floatValue();
			Object objects[] = {MyHacker.getEncryptedIP(),anon,target,types,file,folder,iterations,reward};
			MyView.setFunction("makebounty");
			MyView.addFunctionCall(new RemoteFunctionCall(0,"makebounty",objects));
			setVisible(false);
		}
		if(e.getSource()==type){
			String selected = (String)type.getSelectedItem();
			if(selected.equals("Install Script")){
				browse.setEnabled(true);
				//fileField.setEditable(true);
				fileField.setBackground(new Color(255,255,255));
			}else{
				browse.setEnabled(false);
				//fileField.setEditable(false);
				fileField.setBackground(new Color(238,238,238));
			}
		}
		if(e.getSource()==browse){
			BountyFileChooser BFC = new BountyFileChooser(MyHacker,this);
			MyHacker.getPanel().add(BFC);
			BFC.setVisible(true);
			BFC.moveToFront();
		}
		
	}
	
	public void itemStateChanged(ItemEvent e){
		if(e.getStateChange()==ItemEvent.SELECTED){
			ipField1.setEditable(false);
			ipField2.setEditable(false);
			ipField3.setEditable(false);
			ipField4.setEditable(false);
		}
		else if(e.getStateChange()==ItemEvent.DESELECTED){
			ipField1.setEditable(true);
			ipField2.setEditable(true);
			ipField3.setEditable(true);
			ipField4.setEditable(true);
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
	
	public void fileSelected(String name,String folder){
		fileField.setText(name);
		//requestFocusInWindow();
		this.folder=folder;
		toFront();
	}
}
