package DBAdmin;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

public class NameDialog extends JDialog implements ActionListener{
	
	private JPanel panel=new JPanel();
	private JTextField nameField;
	private JTextField titleField;
	private JButton ok=new JButton("Go");//, cancel=new JButton("Cancel");
    private NetworkAdmin na;
	public NameDialog(NetworkAdmin myNetworkAdmin){
		
        na = myNetworkAdmin;
        
		panel.setLayout(null);
		int width=2;
		int height=2;
		JLabel label = new JLabel("Name: ");
		panel.add(label);
		label.setBounds(width,height,label.getPreferredSize().width,label.getPreferredSize().height);
		width+=label.getPreferredSize().width+5;
		
		nameField=new JTextField(20);
		panel.add(nameField);
		nameField.setBounds(width,height,nameField.getPreferredSize().width,nameField.getPreferredSize().height);
		height+=nameField.getPreferredSize().height+5;
		width=2;
		
		label = new JLabel("Title: ");
		panel.add(label);
		label.setBounds(width,height,label.getPreferredSize().width,label.getPreferredSize().height);
		width+=label.getPreferredSize().width+5;
		
		titleField=new JTextField(20);
		panel.add(titleField);
		titleField.setBounds(width,height,titleField.getPreferredSize().width,titleField.getPreferredSize().height);
		height+=titleField.getPreferredSize().height+5;
		
		width=2;
		panel.add(ok);
		ok.addActionListener(this);
		ok.setBounds(width,height,ok.getPreferredSize().width,ok.getPreferredSize().height);
		width+=15+ok.getPreferredSize().width;
		
		//panel.add(cancel);
		//cancel.addActionListener(this);
		//cancel.setBounds(width,height,cancel.getPreferredSize().width,cancel.getPreferredSize().height);

		height+=35+ok.getPreferredSize().height;
		//width=label.getPreferredSize().width+passwordField.getPreferredSize().width+30;
        width=350;
		
		getContentPane().add(panel);
		setTitle("Set Name And Title");
		setSize(width,height);
		setModal(true);
		setVisible(true);
	}
	
    public String getName() {
        return nameField.getText();
    }
    
    public String getTitle() {
        return titleField.getText();
    }
    
	public void actionPerformed(ActionEvent e){
		//if(e.getSource()==cancel)
			//setVisible(false);
		if(e.getSource()==ok){
            //na.npcName.setText(nameField.getText());
            //na.npcTitle.setText(titleField.getText());
			setVisible(false);
		}
		
		
		
	}	
}
