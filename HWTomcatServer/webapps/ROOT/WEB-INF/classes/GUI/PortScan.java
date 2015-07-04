package GUI;
/**

PortScan.java
this is the attack window.
*/

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import Assignments.*;
import View.*;
import java.text.*;

public class PortScan extends Application implements UndoableEditListener,FocusListener,TableModelListener{ 
	private final String[] columns = {"Port","Type","FireWall","Default","Attack"};
	private final String[] firewalls = {"No FireWall","Basic FireWall","Medium FireWall","Greater FireWall","Basic Attacking FireWall","Medium Attacking FireWall","Greater Attacking FireWall","Ultimate Attacking FireWall"};
	private final String[] types = {"Banking","FTP","Attack","HTTP","Redirect"};
	private JDesktopPane mainPanel=null;
	private Hacker MyHacker=null;
	private JTextField ipField1,ipField2,ipField3,ipField4;
	private String ipValue;
	private int portValue;
	private JButton button;
	private JTextPane textPane;
	private JTabbedPane tabbedPane;
	private JPanel optionsPanel;
	private DefaultListModel listModel = new DefaultListModel();
	private JList list;
	private JTable table;
	private JScrollPane scrollPane;
	private PortScanTableModel tableModel;
	private JButton redirectButton;
	private IPPanel ipPanel;
	
	public PortScan(String name,boolean resize,boolean max,boolean close,boolean iconify,JDesktopPane mainPanel,Hacker MyHacker,String ipValue,int portValue){
		this.setTitle(name);
		this.setResizable(resize);
		this.setMaximizable(max);
		this.setClosable(close);
		this.setIconifiable(iconify);
		this.addInternalFrameListener(this);
		this.mainPanel=mainPanel;
		this.MyHacker=MyHacker;
		this.ipValue=ipValue;
		populate();
		if(ipValue!="")
			setIP(ipValue);
		this.portValue=portValue;
		//this.setFrameIcon(ImageLoader.getImageIcon("images/calc.png"));
	}

	public void populate(){
		Insets insets = this.getInsets();
		JLabel label = new JLabel("IP: ");
		this.add(label);
		Dimension dL = label.getPreferredSize();
		label.setBounds(insets.left+15,insets.top+5,dL.width,dL.height);
		int position = insets.left+dL.width+10;
		
		ipPanel = new IPPanel(MyHacker);
		add(ipPanel);
		Dimension d = ipPanel.getPreferredSize();
		ipPanel.setBounds(position,insets.top+5,d.width,d.height);
		
		button=new JButton("Scan");
		this.add(button);
		d = button.getPreferredSize();
		button.setBounds(insets.left+15,insets.top+25,d.width,d.height);
		button.addActionListener(this);
		
		JLabel results = new JLabel("<html><h3>Results</h3></html>");
		this.add(results);
		results.setBounds(insets.left+15,insets.top+d.height+65-results.getPreferredSize().height,results.getPreferredSize().width,results.getPreferredSize().height);
		
		Object[][] data = {};
		tableModel = new PortScanTableModel(data,columns);
		table = new JTable(tableModel);
		//table.setTableHeader(table.getTableHeader());
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.getModel().addTableModelListener(this);
		table.getColumnModel().getColumn(0).setPreferredWidth(40);
		table.getColumnModel().getColumn(1).setPreferredWidth(80);
		table.getColumnModel().getColumn(2).setPreferredWidth(180);
		table.getColumnModel().getColumn(3).setPreferredWidth(60);
		table.getColumnModel().getColumn(4).setPreferredWidth(60);
		
		table.getColumnModel().getColumn(0).setCellRenderer(new PortScanTableCellRenderer(this));
		table.getColumnModel().getColumn(1).setCellRenderer(new PortScanTableCellRenderer(this));
		table.getColumnModel().getColumn(2).setCellRenderer(new PortScanTableCellRenderer(this));
		table.getColumnModel().getColumn(3).setCellRenderer(new PortScanTableCellRenderer(this));
		table.getColumnModel().getColumn(4).setCellRenderer(new PortScanTableCellRenderer(this));
		table.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(new JCheckBox()));
		scrollPane = new JScrollPane(table);
		this.add(scrollPane);
		scrollPane.setBounds(insets.left+15,insets.top+65+d.height,423,200);
		
		button=new JButton("Attack");
		this.add(button);
		d = button.getPreferredSize();
		button.setBounds(insets.left+15,insets.top+270+d.height,d.width,d.height);
		button.addActionListener(this);
		
		redirectButton=new JButton("Redirect");
		this.add(redirectButton);
		Dimension rd = redirectButton.getPreferredSize();
		redirectButton.setBounds(insets.left+25+d.width,insets.top+270+rd.height,rd.width,rd.height);
		redirectButton.addActionListener(this);
		redirectButton.setEnabled(false);
		
		
		button=new JButton("Wont work");
		this.add(button);
		
		
	}

	public void receivedScan(PacketPort scan[]){
		//System.out.println("received in Port Scan");
		Object[] data=new Object[5];
		tableModel.resetData();
		for(int i=0;i<scan.length;i++){
			if(scan[i]!=null){
				data[0] = new Integer(scan[i].getNumber());
				data[1] = types[scan[i].getType()];
				if(scan[i].getFireWall()!=null)
					data[2] = scan[i].getFireWall().get("name");//firewalls[scan[i].getFireWall()];
				else
					data[2] = "Not Available";
				data[3] = new Integer(scan[i].getDefault());
				data[4] = new Boolean(false);
				tableModel.addRow(data);
			}
		}
	}
	
	public void setIPValue(String ipValue){
        ipPanel.setIP(ipValue);
	}
	
	public void setIP(String ip){
		ipPanel.setIP(ip);
	}
	

	public void internalFrameClosed(InternalFrameEvent e) {
		MyHacker.setScanOpen(false);
	}
    
    private void doScan() {
        tableModel.resetData();
        //String targetIP = ipField.getText();
        String targetIP = ipPanel.getIP();//ipField1.getText()+"."+ipField2.getText()+"."+ipField3.getText()+"."+ipField4.getText();
        //get ip from main class.
        String ip=MyHacker.getEncryptedIP();
        View MyView = MyHacker.getView();
        Object objects[] = {ip,targetIP};
        MyView.setFunction("requestscan");
        MyView.addFunctionCall(new RemoteFunctionCall(0,"requestscan",objects));
    }

	public void actionPerformed(ActionEvent e){
		if(e.getActionCommand().equals("Scan")){
			if(MyHacker.getShowScan()){
				NumberFormat nf= NumberFormat.getCurrencyInstance();
				Object[] options = {"Yes","Cancel"};
                Object[] check = ConfirmationPanel.showYesNoDialog(this, "Port Scan", "Are You Sure?\n\nIt will cost you " + nf.format(10.0f) + " to scan this ip.", "Always scan, don't ask again.");
                if ((int)(Integer)check[0] == OptionDialog.OPTION_YES) {
                    doScan();
                }
                if ((boolean)(Boolean)check[1] == true) {
                    MyHacker.setShowScan(false);
                }
            } else {
                doScan();
            }
		}
		if(e.getActionCommand().equals("Attack")){
			//String targetIP = ipField1.getText()+"."+ipField2.getText()+"."+ipField3.getText()+"."+ipField4.getText();
			int count=0;
			boolean attack;
			for(int i=0;i<tableModel.getRowCount();i++){
				attack =(boolean)(Boolean)tableModel.getValueAt(i,4); 
				//System.out.println(attack);
				if(attack)
					count++;
				
			}
			Integer[] attacks = new Integer[count];
			count=0;
			for(int i=0;i<tableModel.getRowCount();i++){
				attack =(boolean)(Boolean)tableModel.getValueAt(i,4);
				if(attack){
					attacks[count]=(Integer)tableModel.getValueAt(i,0);
					//System.out.println(attacks[count]);
					count++;
				}
			}
			AttackPane AP = MyHacker.showAttack(MyHacker.getDefaultAttack());
			AP.setSecondaryPorts(attacks);
			AP.setIP(ipPanel.getIP());
			AP.setVisible(true);
		}
		
		if(e.getActionCommand().equals("Redirect")){
			//String targetIP = ipField1.getText()+"."+ipField2.getText()+"."+ipField3.getText()+"."+ipField4.getText();
			int count=0;
			boolean attack;
			for(int i=0;i<tableModel.getRowCount();i++){
				attack =(boolean)(Boolean)tableModel.getValueAt(i,4); 
				//System.out.println(attack);
				if(attack)
					count++;
				
			}
			AttackPane AP = MyHacker.showRedirect(MyHacker.getDefaultRedirect());
			//AP.setSecondaryPorts(attacks);
			AP.setIP(ipPanel.getIP());
			AP.setTargetPort((Integer)tableModel.getValueAt(table.getSelectedRow(),0));
			AP.setVisible(true);
		}
	}
	
	/*public void caretUpdate(CaretEvent e){
		if(e.getSource()==ipField1){
			if(e.getDot()==3){
				ipField2.grabFocus();
			}
		}
		else if(e.getSource()==ipField2){
			if(e.getDot()==3){
				ipField3.grabFocus();
			}
		}
		else if(e.getSource()==ipField3){
			if(e.getDot()==1){
				ipField4.grabFocus();
			}
		}
		
	}*/
	
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
	
	public void tableChanged(TableModelEvent e) {
		if(e.getColumn()==4){
			int row = e.getFirstRow();
			String type = (String)tableModel.getValueAt(row,1);
			boolean check = (Boolean)tableModel.getValueAt(row,4);
			if(type.equals("Redirect")){
				if(check)
					redirectButton.setEnabled(true);
				else
					redirectButton.setEnabled(false);
				
			}
			
			
		}
	}

	
}
