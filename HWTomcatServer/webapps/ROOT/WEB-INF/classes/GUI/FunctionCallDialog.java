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

public class FunctionCallDialog extends JDialog implements ActionListener{
	
	private ScriptEditor MyScriptEditor;
	private JPanel panel=new JPanel();
	private String[] functions,firsLabels,secondLabels;
	private float[] cpuCosts,compilingCosts;
	private int type;
	private JComboBox cb;
	private JLabel cpuCost=new JLabel("0.0"),compilingCost=new JLabel("$0.0");
	private JButton ok=new JButton("Insert"),cancel = new JButton("Cancel");
	private JTextField text1=new JTextField(12), text2=new JTextField(12);
	private JLabel label1=new JLabel("Compiling Cost:"),label2=new JLabel("Message:");
	private int fields=0;
	
	public FunctionCallDialog(String[] functions,String[] firstLabels,String[] secondLabels,float[] cpuCosts,float[] compilingCosts,int type,ScriptEditor MyScriptEditor){
		int height=2;
		int width=2;
		this.functions=functions;
		this.type=type;
		this.MyScriptEditor=MyScriptEditor;
		SpringLayout layout = new SpringLayout();
		panel.setLayout(layout);
		
		JLabel label = new JLabel("Function:");
		panel.add(label);
		layout.putConstraint(SpringLayout.NORTH,label,height,SpringLayout.NORTH,panel);
		layout.putConstraint(SpringLayout.WEST,label,width,SpringLayout.WEST,panel);
		width+=label1.getPreferredSize().width+5;
		
		cb=new JComboBox(functions);
		cb.addActionListener(this);
		panel.add(cb);
		layout.putConstraint(SpringLayout.NORTH,cb,height,SpringLayout.NORTH,panel);
		layout.putConstraint(SpringLayout.WEST,cb,width,SpringLayout.WEST,panel);
		width+=5+cb.getPreferredSize().width;
		height+=cb.getPreferredSize().height+5;
		
		width=2;
		label = new JLabel("CPU Cost:");
		panel.add(label);
		layout.putConstraint(SpringLayout.NORTH,label,height,SpringLayout.NORTH,panel);
		layout.putConstraint(SpringLayout.WEST,label,width,SpringLayout.WEST,panel);
		width+=label1.getPreferredSize().width+5;
		
		panel.add(cpuCost);
		layout.putConstraint(SpringLayout.NORTH,cpuCost,height,SpringLayout.NORTH,panel);
		layout.putConstraint(SpringLayout.WEST,cpuCost,width,SpringLayout.WEST,panel);
		height+=label.getPreferredSize().height+5;
		
		width=2;
		label = new JLabel("Compiling Cost:");
		panel.add(label);
		layout.putConstraint(SpringLayout.NORTH,label,height,SpringLayout.NORTH,panel);
		layout.putConstraint(SpringLayout.WEST,label,width,SpringLayout.WEST,panel);
		width+=label1.getPreferredSize().width+5;
		
		panel.add(compilingCost);
		layout.putConstraint(SpringLayout.NORTH,compilingCost,height,SpringLayout.NORTH,panel);
		layout.putConstraint(SpringLayout.WEST,compilingCost,width,SpringLayout.WEST,panel);
		height+=label.getPreferredSize().height+5;
		
		width=2;
		panel.add(label1);
		layout.putConstraint(SpringLayout.NORTH,label1,height,SpringLayout.NORTH,panel);
		layout.putConstraint(SpringLayout.WEST,label1,width,SpringLayout.WEST,panel);
		width+=5+label1.getPreferredSize().width;
		label1.setVisible(false);
		
		panel.add(text1);
		layout.putConstraint(SpringLayout.NORTH,text1,height,SpringLayout.NORTH,panel);
		layout.putConstraint(SpringLayout.WEST,text1,width,SpringLayout.WEST,panel);
		text1.setVisible(false);
		height+=5+text1.getPreferredSize().height;
		
		width=2;
		panel.add(label2);
		layout.putConstraint(SpringLayout.NORTH,label2,height,SpringLayout.NORTH,panel);
		layout.putConstraint(SpringLayout.WEST,label2,width,SpringLayout.WEST,panel);
		width+=5+label1.getPreferredSize().width;
		label2.setVisible(false);
		
		panel.add(text2);
		layout.putConstraint(SpringLayout.NORTH,text2,height,SpringLayout.NORTH,panel);
		layout.putConstraint(SpringLayout.WEST,text2,width,SpringLayout.WEST,panel);
		text2.setVisible(false);
		height+=5+text2.getPreferredSize().height;
		
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
		setTitle("Function Call");
		setSize(300,180);
		setLocationRelativeTo(MyScriptEditor);
		//setFrameIcon(ImageLoader.getImageIcon("images/functioncall.png"));
		setModal(false);
		setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e){
		if(e.getSource()==cb){
			String sI= (String)cb.getSelectedItem();
			
			//banking
			if(sI.equals("lowerDeposit")){
				label1.setText("IP:");
				label1.setVisible(true);
				text1.setVisible(true);
				label2.setText("Amount:");
				label2.setVisible(true);
				text2.setVisible(true);
				fields=2;
			}
			if(sI.equals("mediumDeposit")){
				label1.setText("IP:");
				label1.setVisible(true);
				text1.setVisible(true);
				label2.setText("Amount:");
				label2.setVisible(true);
				text2.setVisible(true);
				fields=2;
			}
			if(sI.equals("higherDeposit")){
				label1.setText("IP:");
				label1.setVisible(true);
				text1.setVisible(true);
				label2.setText("Amount:");
				label2.setVisible(true);
				text2.setVisible(true);
				fields=2;
			}
			if(sI.equals("greaterDeposit")){
				label1.setText("IP:");
				label1.setVisible(true);
				text1.setVisible(true);
				label2.setText("Amount:");
				label2.setVisible(true);
				text2.setVisible(true);
				fields=2;
			}
			if(sI.equals("withdraw")){
				label1.setText("IP:");
				label1.setVisible(true);
				text1.setVisible(true);
				label2.setText("Amount:");
				label2.setVisible(true);
				text2.setVisible(true);
				fields=2;
			}
			if(sI.equals("lowerTransfer")){
				label1.setText("IP:");
				label1.setVisible(true);
				text1.setVisible(true);
				label2.setText("Amount:");
				label2.setVisible(true);
				text2.setVisible(true);
				fields=2;
			}
			if(sI.equals("mediumTransfer")){
				label1.setText("IP:");
				label1.setVisible(true);
				text1.setVisible(true);
				label2.setText("Amount:");
				label2.setVisible(true);
				text2.setVisible(true);
				fields=2;
			}
			if(sI.equals("higherTransfer")){
				label1.setText("IP:");
				label1.setVisible(true);
				text1.setVisible(true);
				label2.setText("Amount:");
				label2.setVisible(true);
				text2.setVisible(true);
				fields=2;
			}
			if(sI.equals("greaterTransfer")){
				label1.setText("IP:");
				label1.setVisible(true);
				text1.setVisible(true);
				label2.setText("Amount:");
				label2.setVisible(true);
				text2.setVisible(true);
				fields=2;
			}
			if(sI.equals("message")){
				label1.setText("IP:");
				label1.setVisible(true);
				text1.setVisible(true);
				label2.setText("Message:");
				label2.setVisible(true);
				text2.setVisible(true);
				fields=2;
			}
			if(sI.equals("getAmount")){
				label1.setVisible(false);
				text1.setVisible(false);
				label2.setVisible(false);
				text2.setVisible(false);
				fields=0;
			}
			if(sI.equals("getSourceIP")){
				label1.setVisible(false);
				text1.setVisible(false);
				label2.setVisible(false);
				text2.setVisible(false);
				fields=0;
			}
			if(sI.equals("getTargetIP")){
				label1.setVisible(false);
				text1.setVisible(false);
				label2.setVisible(false);
				text2.setVisible(false);
				fields=0;
			}
			if(sI.equals("getPettyCash")){
				label1.setVisible(false);
				text1.setVisible(false);
				label2.setVisible(false);
				text2.setVisible(false);
				fields=0;
			}
			if(sI.equals("getMaliciousIP")){
				label1.setVisible(false);
				text1.setVisible(false);
				label2.setVisible(false);
				text2.setVisible(false);
				fields=0;
			}
			if(sI.equals("getPettyCashTarget")){
				label1.setVisible(false);
				text1.setVisible(false);
				label2.setVisible(false);
				text2.setVisible(false);
				fields=0;
			}
			
			//attack
			
		}
		if(e.getSource()==ok){
			MyScriptEditor.addFunctionCall((String)cb.getSelectedItem(),text1.getText(),text2.getText(),fields);
			setVisible(false);
		}
		if(e.getSource()==cancel){
			//System.out.println("Canceled");
			setVisible(false);
		}
		
	}	
}
