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


public class IPPanel extends JPanel implements FocusListener, KeyListener, DocumentListener {

	private JTextField ipField1,ipField2,ipField3,ipField4;
    
	private Hacker MyHacker;
	public IPPanel(Hacker MyHacker){
		this.MyHacker=MyHacker;
		setLayout(null);
		int position = 0;
		ipField1 = new JTextField(3);
		this.add(ipField1);
		Dimension d = ipField1.getPreferredSize();
		ipField1.setBounds(position,0,d.width,d.height);
		position+=d.width;
		
		JLabel dot = new JLabel(".");
		d = dot.getPreferredSize();
		add(dot);
		dot.setBounds(position,0,d.width,d.height);
		position+=d.width;
		
		ipField2 = new JTextField(3);
		this.add(ipField2);
		d = ipField2.getPreferredSize();
		ipField2.setBounds(position,0,d.width,d.height);
		position+=d.width;
		
		dot = new JLabel(".");
		d = dot.getPreferredSize();
		add(dot);
		dot.setBounds(position,0,d.width,d.height);
		position+=d.width;
		
		ipField3 = new JTextField(2);
		this.add(ipField3);
		d = ipField3.getPreferredSize();
		ipField3.setBounds(position,0,d.width,d.height);
		position+=d.width;
		
		dot = new JLabel(".");
		d = dot.getPreferredSize();
		add(dot);
		dot.setBounds(position,0,d.width,d.height);
		position+=d.width;
		
		ipField4 = new JTextField(3);
		this.add(ipField4);
		d = ipField4.getPreferredSize();
		ipField4.setBounds(position,0,d.width,d.height);
		position+=d.width;
        ipField1.addKeyListener(this);
        ipField2.addKeyListener(this);
        ipField3.addKeyListener(this);
        ipField4.addKeyListener(this);
        ipField1.getDocument().addDocumentListener(this);
        ipField2.getDocument().addDocumentListener(this);
        ipField3.getDocument().addDocumentListener(this);
        ipField4.getDocument().addDocumentListener(this);
		ipField1.addFocusListener(this);
		ipField2.addFocusListener(this);
		ipField3.addFocusListener(this);
		ipField4.addFocusListener(this);
		
		setPreferredSize(new Dimension(position+10,d.height));
		
	}
	
	public void setIP(String ip){
        String[] ipParts = ip.split("\\.");
        if (ipParts.length == 4) {
            // check to make sure that they didn't just put 123..4.2 or something stupid intended to break the system.
            if (ipParts[2].length() != 1) {
                ipError();
                return;
            }
            for (int i = 0; i < 4; i ++) {
                if (ipParts[i].length() == 0 || ipParts[i].length() > 3) {
                    ipError();
                    return;
                }
            }
            // set the IP
            setIP(ipParts);
        } else {
            ipError();
        }        
	}

    public void setIP(final String[] parts) {
        Thread worker = new Thread () {
            public void run() {
        		ipField1.setText(parts[0]);
        		ipField2.setText(parts[1]);
        		ipField3.setText(parts[2]);
        		ipField4.setText(parts[3]);
                ipField4.grabFocus();
            }
        };
        SwingUtilities.invokeLater(worker);
    }
    
    public void ipError() {
        MyHacker.showMessage("Wrong Format for IP");
    }
    
	
	public String getIP(){
		return(ipField1.getText()+"."+ipField2.getText()+"."+ipField3.getText()+"."+ipField4.getText());
	}
	
	public void setEnabled(boolean enabled){
		ipField1.setEnabled(enabled);
		ipField2.setEnabled(enabled);
		ipField3.setEnabled(enabled);
		ipField4.setEnabled(enabled);
	}
    
    // Implements DocumentListener
    public void changedUpdate(DocumentEvent e) {
    }
    
    public void insertUpdate(DocumentEvent e) {
        
        // if they paste, the change length is greater than 1
        if (e.getLength() > 3) {
            setIP(ipField1.getText());
        }
        
        // check length of each field and move cursor if necessary
        if (e.getDocument() == ipField1.getDocument()) {
            if (ipField1.getText().length() == 3) {
                ipField2.grabFocus();
            }
        } else if (e.getDocument() == ipField2.getDocument()) {
            if (ipField2.getText().length() == 3) {
                ipField3.grabFocus();
            }
        } else if (e.getDocument() == ipField3.getDocument()) {
            if (ipField3.getText().length() == 1) {
                ipField4.grabFocus();
            }
        }
    }
    
    public void removeUpdate(DocumentEvent e)  {
    }
    
    // Implements KeyListener
    // order of events == keyPressed, keyTyped, keyReleased
	public void keyPressed(KeyEvent e) {
        // use this to find if it's a copy/paste (action keys)
        //displayInfo(e, "KEY PRESSED: ");
    }
    
    public void keyReleased(KeyEvent e) {
        //displayInfo(e, "KEY RELEASED: ");
    }
    
    public void keyTyped(KeyEvent e) {
        // the input has not actually been added to the text field yet
        
        if (e.getKeyChar() == '.') {
            
            // eat the '.'
            e.consume();
            
            // if the '.' is the first character pressed, stay in that field
            // if the '.' is the 2nd or 3rd character, move to the next field
            if (e.getSource() == ipField1) {
                if (ipField1.getText().length() != 0) {
                    if (ipField1.getSelectedText() == null) {
                        ipField2.grabFocus();
                    }
                }
            } else if (e.getSource() == ipField2) {
                // if the first thing they enter in a box is a period, eat it.  Otherwise, move to the next box and eat it.
                if (ipField2.getText().length() != 0)  {
                    if (ipField2.getSelectedText() == null) {
                        ipField3.grabFocus();
                    }
                }
            } else if (e.getSource() == ipField3) {
                if (ipField3.getText().length() != 0) {
                    if (ipField3.getSelectedText() == null) {
                        ipField4.grabFocus();
                    }
                }
            }
        }
    }

	public void focusGained(FocusEvent e){
		((JTextField)e.getSource()).selectAll();
	}
	
	public void focusLost(FocusEvent e){
	}


}
