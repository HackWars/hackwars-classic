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
import java.util.*;
import net.miginfocom.swing.*;

public class SecondaryPortsDialog extends JDialog implements ActionListener{

	//private int watchID;
    //private int maxPorts;
    int ports[];
    JCheckBox cBoxes[];
	private JButton ok=new JButton("Okay"), cancel = new JButton("Cancel");
	boolean curSecPorts[] = new boolean[Constants.MAX_PORTS];
	Integer[] newSecondaryPorts;
	Integer[] currentSecondaryPorts;
	boolean changed = false;

    public Integer[] getSecondaryPorts(Component parent, Integer[] currentSecondaryPorts){
		this.currentSecondaryPorts = currentSecondaryPorts;
        ports = new int[Constants.MAX_PORTS];
        cBoxes = new JCheckBox[Constants.MAX_PORTS];

        // create a GridLayout for the checkboxes
		setLayout(new MigLayout("fill,wrap 4,align leading"));
		
        // get the currently observed ports
        for (int i = 0; i < currentSecondaryPorts.length; i++) {
            curSecPorts[(int)currentSecondaryPorts[i]] = true;
        }
        
		getContentPane().add(new JLabel("Select Secondary Ports:"), "span,wrap");
        
        //add all the checkboxes to the grid layout
        JCheckBox cBox;
        for (int i = 0; i < Constants.MAX_PORTS; i++) {
            cBox = new JCheckBox("Port " + i, curSecPorts[i]);
            add(cBox);
            cBoxes[i] = cBox;
        }
        
        //add the buttons (OK/Cancel) to the dialog
        getContentPane().add(ok);
		ok.addActionListener(this);
		
		getContentPane().add(cancel);
		cancel.addActionListener(this);
		
        // set the title and positioning of the dialog
		setTitle("Set Selected Ports");
		setLocationRelativeTo(parent);
		setModal(true);
		setResizable(false);
        pack();
		setVisible(true);
		
		if(changed){
			return(newSecondaryPorts);
		}
		else{
			return(currentSecondaryPorts);
		}
	}
	
	public void actionPerformed(ActionEvent e){
    
		if(e.getSource()==ok){
            ArrayList oPorts = new ArrayList();
			//Integer[] ports;
			for(int i=0;i<Constants.MAX_PORTS;i++){
                if (cBoxes[i].isSelected() == true) {
                    oPorts.add(new Integer(i));
                }
			}
            newSecondaryPorts = new Integer[oPorts.size()];
            oPorts.toArray(newSecondaryPorts);
			changed = true;
			dispose();
		}
        
		if(e.getSource()==cancel){
			dispose();
		}
        
	}	
        
}