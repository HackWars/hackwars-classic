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
import View.*;
import java.awt.image.*;
import Assignments.*;
//import java.awt.Dialog.ModalityType;

public class MacroDialog extends JInternalFrame implements ActionListener{
	
	private MacroPanel panel=new MacroPanel();
	private JButton ok=new JButton("Go");
	private BufferedImage image=null;
	private Hacker MyHacker=null;
	private JTextField tf=new JTextField(8);
	private int h;
	public MacroDialog(BufferedImage image,Hacker MyHacker){
		this.image=image;
		this.MyHacker=MyHacker;
		//SpringLayout layout = new SpringLayout();
		panel.setLayout(null);
		int width=2;
		int height=2;
		JLabel label = new JLabel("Please insert the given code: ");
		panel.add(label);
		label.setBounds(width,height,label.getPreferredSize().width,label.getPreferredSize().height);
		h=height+label.getPreferredSize().height+5;
		height+=label.getPreferredSize().height+35;
		int finalWidth = width+label.getPreferredSize().width;
		panel.add(tf);
		tf.addActionListener(this);
		tf.setBounds(width,height,tf.getPreferredSize().width,tf.getPreferredSize().height);
		height+=tf.getPreferredSize().height+5;
		panel.add(ok);
		ok.addActionListener(this);
		ok.setBounds(width,height,ok.getPreferredSize().width,ok.getPreferredSize().height);
		height+=15+ok.getPreferredSize().width;

		getContentPane().add(panel);
		setTitle("Insert Code");
		//setSize(finalWidth+10,height);
		//setLocation(200,200);
		panel.setImage(image,h);
		//setModal(true);
		setResizable(false);
		setClosable(false);
		MyHacker.getPanel().add(this);
		setBounds(200,200,finalWidth+10,height);
		//setModalityType(0);
		//setAlwaysOnTop(true);
		setIconifiable(false);
		setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e){
		if(e.getSource()==ok||e.getSource()==tf){
			//MyAttackPane.attack();
			Object[] objects = new Object[]{MyHacker.getEncryptedIP(),tf.getText()};
			MyHacker.getView().addFunctionCall(new RemoteFunctionCall(0,"unlock",objects));
			setVisible(false);
		}
		
		
		
	}

	 public void windowOpened(WindowEvent e){
    }
    
    public void windowClosing(WindowEvent e){
	   
    }
    
    public void windowClosed(WindowEvent e){
	   // System.out.println("Window Closed");
    }
    
    public void windowIconified(WindowEvent e){
    }
    
    public void windowDeiconified(WindowEvent e){
    }
    public void windowActivated(WindowEvent e){
    }
    public void windowDeactivated(WindowEvent e){
    }	
	
	
}
