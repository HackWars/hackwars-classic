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

public class ObservedPortsDialog extends JDialog implements ActionListener{

	private WatchManager MyWatchManager;
	private int watchID;
    private int maxPorts;
    int ports[];
    JCheckBox cBoxes[];
	private JButton ok=new JButton("Okay"), cancel = new JButton("Cancel");

    public ObservedPortsDialog(WatchManager MyWatchManager, int watchID, int maxPorts, int[] currentObservedPorts){
		this.MyWatchManager=MyWatchManager;
		this.watchID=watchID;
        this.maxPorts = maxPorts;
        ports = new int[maxPorts];
        cBoxes = new JCheckBox[maxPorts];

        // create a GridLayout for the checkboxes
		GridBagLayout layout = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
		setLayout(layout);
		int width=2;
		int height=2;
        
        // get the currently observed ports
        boolean[] curObsPorts = new boolean[maxPorts];
        for (int i = 0; i < currentObservedPorts.length; i++) {
            curObsPorts[currentObservedPorts[i]] = true;
        }
        
        //Observed Ports label
        c.gridx = 0;
        c.gridy = 0;
		c.gridwidth=4;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.insets = new Insets(2,2,2,2);
		getContentPane().add(new JLabel("Select Observed Ports:"), c);
        
        //add all the checkboxes to the grid layout
        int x = 0, y = 1;
		c.gridwidth=1;
		
        int count = 0;
        JCheckBox cBox;
        for (int i = 0; i < maxPorts; i++) {
            c.gridx = x;
            c.gridy = y;
            cBox = new JCheckBox("Port " + i, curObsPorts[i]);
            getContentPane().add(cBox, c);
            cBoxes[i] = cBox;
            count++;
            if (count == 8) {
                count = 0;
                y = 1;
                x++;//= cBox.getPreferredSize().width+5;
            } else {
                y ++;//= 5;
            }
        }
        
        //add the buttons (OK/Cancel) to the dialog
        c.gridy++;// = 250;
        c.gridx = 0;
		getContentPane().add(ok, c);
		ok.addActionListener(this);
		
        c.gridx ++;//= ok.getPreferredSize().width + 5;
		getContentPane().add(cancel, c);
		cancel.addActionListener(this);
		
        // set the title and positioning of the dialog
		setTitle("Set Observed Ports");
		setLocationRelativeTo(MyWatchManager);
		setModal(true);
		setResizable(false);
        pack();
		setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e){
    
		if(e.getSource()==ok){
            ArrayList oPorts = new ArrayList();
			Integer[] ports;
			for(int i=0;i<maxPorts;i++){
                if (cBoxes[i].isSelected() == true) {
                    oPorts.add(new Integer(i));
                }
			}
            ports = new Integer[oPorts.size()];
            oPorts.toArray(ports);
			MyWatchManager.setObservedPorts(watchID,ports);
			setVisible(false);
		}
        
		if(e.getSource()==cancel){
			setVisible(false);
		}
        
	}	
        
}