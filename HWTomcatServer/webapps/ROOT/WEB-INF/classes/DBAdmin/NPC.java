package DBAdmin;
/*
HackWars (2008)

Description: A slapped together tool for editing drop tables in HackWars.

*/

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import util.*;
import java.io.*;
import java.awt.geom.*;
import java.awt.event.*;
import javax.imageio.*;
import java.util.*;
import javax.swing.text.*;

public class NPC extends JFrame implements MouseListener,ActionListener{
	//MYSQL INFO.
	private String Connection="70.49.0.26";
	private String DB="hackwars";
	private String Username="hackserver1";
	private String Password="l99fd4ew";
	
	private String Connection1="70.49.0.26";
	private String DB1="hackerforum";
	
	private String npcIP="";
	
	private JTextArea NPCEditArea=new JTextArea();
	private JTextField ip=new JTextField();
	private JTextField quantity=new JTextField();
	private java.awt.List allNPCList = new java.awt.List();	
	private JTextArea NPCFile = new JTextArea();
	private JScrollPane MyJScrollPane;
	/**
	Builds the list of items.
	*/
	public void buildNPCTable(){
	
		allNPCList.clear();
	
		try{
			sql C=new sql(Connection1,DB1,Username,Password);
			ArrayList result=null;
			String Q="SELECT ip FROM users WHERE npc=\"Y\" ORDER BY ip ASC";
			result=C.process(Q);				
			
			if(result!=null)
			for(int i=0;i<result.size();i++){
				allNPCList.add((String)result.get(i));
			}
			Q="SELECT ip FROM users WHERE name LIKE '%funnyman%' ORDER BY ip ASC";
			result=C.process(Q);				
			
			if(result!=null)
			for(int i=0;i<result.size();i++){
				allNPCList.add((String)result.get(i));
			}
			
			C.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	Intialize the viewport with the given width/height background-color and graphics configuration.
	*/
	public NPC(String connection,String user,String pass){
		this.Connection = connection;
		this.Connection1 = connection;
		Username=user;
		Password=pass;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx=0;
		c.gridy=0;
		c.fill=GridBagConstraints.VERTICAL;
		c.anchor=GridBagConstraints.FIRST_LINE_START;
		c.insets = new Insets(0,5,5,0);
		/* All NPC List */
		
		
		JLabel label = new JLabel("All NPCs");
		label.setBounds(5,5,200,25);
		allNPCList.setBounds(5, 30, 200, 530);
		allNPCList.setName("loadNPCs");
		allNPCList.addActionListener(this);
		this.add(label,c);
		c.gridy=1;
		c.gridheight=3;
		this.add(allNPCList,c);
		
		/* IP and Quantity fields */
		c.gridheight=1;
		c.gridy=0;
		c.gridx=1;
		label = new JLabel("IP");
		label.setBounds(215, 5, 150, 25);
		
		this.add(label,c);
		c.gridy=1;
		ip.setBounds(215, 30, 150, 25);
		ip.setPreferredSize(new Dimension(150,25));
		ip.setMinimumSize(new Dimension(150,25));
		this.add(ip,c);
		c.gridx=2;
		c.gridy=0;
		label = new JLabel("Quantity");
		label.setBounds(375, 5, 150, 25);
		quantity.setBounds(375, 30, 150, 25);
		quantity.setPreferredSize(new Dimension(150,25));
		quantity.setMinimumSize(new Dimension(150,25));
		this.add(label,c);
		c.gridy=1;
		this.add(quantity,c);
		
		/* NPC File */
		c.gridx=1;
		c.gridwidth=3;
		c.weightx=1.0;
		c.weighty=1.0;
		c.gridy=2;
		c.fill=GridBagConstraints.BOTH;
		MyJScrollPane=new JScrollPane();
		MyJScrollPane.setBounds(215, 65, 500, 450);
		NPCFile.setBounds(215, 65, 500, 450);
		MyJScrollPane.getViewport().add(NPCFile);
		this.add(MyJScrollPane,c);
		
		/* Save and Save as New buttons! */
		c.gridy=3;
		c.fill=GridBagConstraints.VERTICAL;
		c.gridwidth=1;
		c.weightx=0.0;
		c.weighty=0.0;
		JButton save = new JButton("Save");
		save.setActionCommand("save");
		save.addActionListener(this);
		this.add(save,c);
		c.gridx=2;
		JButton saveAsNew = new JButton("Save As New");
		saveAsNew.addActionListener(this);
		saveAsNew.setActionCommand("saveAsNew");
		
		save.setBounds(215, 525, 100, 30);
		saveAsNew.setBounds(325, 525, 100, 30);
		
		
		this.add(saveAsNew,c);
		
		c.gridx=3;
		JButton delete = new JButton("Delete");
		delete.addActionListener(this);
		delete.setActionCommand("delete");
		
		this.add(delete,c);
		
		this.pack();
		this.setBounds(0,0,800,600);
		this.repaint();
		
		this.setTitle("NPC  Editor");

	}
		
	public void actionPerformed(ActionEvent AE){
		String name="";
		if(AE.getSource() instanceof java.awt.List){
			name=((java.awt.List)AE.getSource()).getName();
		}
		
		if(AE.getActionCommand().equals("save")) {
			try{				
				sql C=new sql(Connection,DB,Username,Password);
				ArrayList result=null;
				
				String data=NPCFile.getText();
				data = data.replaceAll("\\\\","\\\\\\\\");
				data = data.replaceAll("\"","\\\\\"");
				String Q = "UPDATE user SET stats=\""+data+"\" WHERE ip = \"" + npcIP + "\"";				
				result=C.process(Q);
				C.close();
			}catch(Exception e){
				e.printStackTrace();
			}



		} else if(name.equals("loadNPCs")) {
		
			npcIP = (String)allNPCList.getItem(allNPCList.getSelectedIndex());
			ip.setText(npcIP);
			quantity.setText("1");
			
			
			try {
				sql C=new sql(Connection,DB,Username,Password);
				ArrayList result=null;
				String Q="SELECT stats FROM user WHERE ip = \"" + npcIP + "\"";
				System.out.println(Q);
				result=C.process(Q);
				
				if ((result!=null) && (result.size() > 0)) {
					NPCFile.setText((String)result.get(0));
					NPCFile.setCaretPosition(0);
					
				}
				
			} catch (Exception e) {

			}
			
			
			
		} else if (AE.getActionCommand().equals("saveAsNew") ){
			
			// get the ip
			String ipValue = ip.getText();
			
			// get the quantity
			String quantityValue = quantity.getText();
			
			if (checkRange(ipValue, quantityValue) == false) {
				JOptionPane.showMessageDialog(this,"IP Range unavailable.  Try again sucka foo!");
			} else {
			
				createNPC(ipValue,quantityValue);
				buildNPCTable();

			}
			
		}
		else if(AE.getActionCommand().equals("delete")){
			Object[] options = {"Yes",
                    "No"};
			int n = JOptionPane.showOptionDialog(this,
				"Are you sure you want to delete "
				+ npcIP,
				"Are you sure?",
				JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null,
				options,
				options[1]);

			if(n==0){
				try {
				sql C=new sql(Connection,DB,Username,Password);
				ArrayList result=null;
				String Q="DELETE FROM user WHERE ip = \"" + npcIP + "\"";
				System.out.println(Q);
				result=C.process(Q);
				Q="DELETE FROM hackerforum.users WHERE ip = \"" + npcIP + "\"";
				System.out.println(Q);
				result=C.process(Q);
				Q="DELETE FROM network_npc WHERE npc_ip = \"" + npcIP + "\"";
				System.out.println(Q);
				result=C.process(Q);
				
				C.close();
				allNPCList.remove(npcIP);	
				ip.setText("");
				NPCFile.setText("");
				} catch (Exception e) {

				}
				
			}
		}
	}
	
	public boolean createNPC(String ip, String quantity) {
		String replaceIP=ip;
		String currentIP = ip;
		String baseIP = "";
		String splitIP[]=ip.split("\\.");
		int lastOctet=new Integer(splitIP[3]);
		currentIP="";
		for(int i=0;i<splitIP.length-1;i++){
			currentIP+=splitIP[i];
			if(i!=splitIP.length-2)
				currentIP+=".";
		}
		baseIP = currentIP;
		
		int quant = new Integer(quantity);
		boolean isFree = true;
		String Q = "";
		
		sql C=new sql(Connection1,DB1,Username,Password);
		sql C2=new sql(Connection,DB,Username,Password);
		
		ArrayList result=null;
		
		for (int i = 0; i < quant; i++) {
			String lastOctetString="";
			if(lastOctet<10){
				lastOctetString="00"+lastOctet;
			}else if(lastOctet<100){
				lastOctetString="0"+lastOctet;
			}else
				lastOctetString=""+lastOctet;
				
			currentIP = baseIP + "." + lastOctetString;
		
			String stats=NPCFile.getText();
			System.out.println(npcIP.replaceAll("\\.","\\\\."));
			if(!npcIP.equals(""))
				stats=stats.replaceAll(npcIP.replaceAll("\\.","\\\\."),currentIP);
			else
				stats=stats.replaceAll(this.ip.getText().replaceAll("\\.","\\\\."),currentIP);

			stats = stats.replaceAll("\\\\","\\\\\\\\");
			stats = stats.replaceAll("\"","\\\\\"");
		
			try{
			Q = "INSERT INTO user(ip,stats) VALUES(\""+currentIP+"\",\"" + stats+"\")";
			C2.process(Q);
			Q = "INSERT INTO users(name,password,email,picture,location,ip,description,npc,last_logged_ip) VALUES(\""+currentIP+"\",\"NOT_NULL342342342342343\",\"EMAIL\",\"PICTURE\",\"LOCATION\",\""+currentIP+"\",\"description\",\"Y\",\"YOUR_MOM\")";
			C.process(Q);
			}catch(Exception e){
				e.printStackTrace();
			}
			
			lastOctet++;
		}
		
		C.close();
		return(isFree);
	}
	
	
	public boolean checkRange(String ip, String quantity) {
		String currentIP = ip;
		String baseIP = "";
		String splitIP[]=ip.split("\\.");
		int lastOctet=new Integer(splitIP[3]);
		currentIP="";
		for(int i=0;i<splitIP.length-1;i++){
			currentIP+=splitIP[i];
			if(i!=splitIP.length-2)
				currentIP+=".";
		}
		baseIP = currentIP;
		
		int quant = new Integer(quantity);
		boolean isFree = true;
		String Q = "";
		
		sql C=new sql(Connection1,DB1,Username,Password);
		ArrayList result=null;
		
		for (int i = 0; i < quant; i++) {
			String lastOctetString="";
			if(lastOctet<10){
				lastOctetString="00"+lastOctet;
			}else if(lastOctet<100){
				lastOctetString="0"+lastOctet;
			}else
				lastOctetString=""+lastOctet;
				
			currentIP = baseIP + "." + lastOctetString;
		
			Q = "SELECT ip FROM users WHERE ip = \"" + currentIP+"\"";
			result=C.process(Q);
			if (result!=null) {
				if (result.size() > 0) {
					isFree=false;
					break;
				}
			}
			
			lastOctet++;
		}
		
		C.close();
		return(isFree);
	}
	
	/**
	DANGER THIS FUNCTION DELETES ALL THE NPCS.
	*/
	public void deleteNPCs(){
		String Q="SELECT ip FROM users WHERE npc=\"Y\"";
		System.out.println(Q);
		sql C=new sql(Connection1,DB1,Username,Password);
		sql C2=new sql(Connection,DB,Username,Password);
		ArrayList result=null;
		try{				
			result=C.process(Q);
			if(result!=null&&result.size()>0){
				for(int i=0;i<result.size();i++){
					String deleteIP=(String)result.get(i);

					Q="DELETE FROM users WHERE ip=\""+deleteIP+"\"";
					C.process(Q);
					Q="DELETE FROM user WHERE ip=\""+deleteIP+"\"";
					C2.process(Q);
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		C.close();
		C2.close();
	}
	
	public void mouseExited(MouseEvent me){}
	public void mouseEntered(MouseEvent me){}
	public void mouseReleased(MouseEvent me){}
	public void mousePressed(MouseEvent me){}
	public void mouseClicked(MouseEvent me){
		super.requestFocus();
		super.requestFocusInWindow();
	}
	
	public static void main(String args[]){
		NPC x = new NPC(args[0],args[1],args[2]);
		x.buildNPCTable();
	}

}//END.
