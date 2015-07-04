package DBAdmin;
/*
HackWars (2008)

Description: A slapped together tool for editing Networks in HackWars.

*/

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.image.*;
import util.*;
import java.io.*;
import java.awt.geom.*;
import java.awt.event.*;
import javax.imageio.*;
import java.util.*;

public class NetworkAdmin extends JFrame implements MouseListener,ActionListener, FocusListener, ItemListener{
	//MYSQL INFO.
	private String Connection="70.49.0.26";
	private String DB="hackwars";
	private String Username="hackserver1";
	private String Password="l99fd4ew";
	
	private String Connection1="70.49.0.26";
	private String DB1="hackerforum";

    private String networkId = "";
    private String networkName = "";
    private String attachedNetworkId = "";
    // "listName", "integer selected index"
    private String[] oldListSelection =  new String[4];
    
    
	private JTextField networkNameField = new JTextField();
	private java.awt.List allNetworksList = new java.awt.List();
    private java.awt.List attachedNetworksList = new java.awt.List();
    private java.awt.List allNPCList = new java.awt.List();	
    private java.awt.List miningNPCs = new java.awt.List();
    private java.awt.List attackNPCs = new java.awt.List();
    private java.awt.List questNPCs = new java.awt.List();
    private JTextField storeNPC = new JTextField();
    public JTextField npcName = new JTextField();
    public JTextField npcTitle = new JTextField();
    private JTextField attackProbability = new JTextField();
    private java.awt.List gameList = new java.awt.List();
    private JTextArea entranceMessage = new JTextArea();
    private JComboBox resources = new JComboBox(new String[] {"Duct Tape", "Germanium", "Silicon", "YBCO", "Plutonium"});
    
	/**
	Builds the list of NPCs.
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
			
			C.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
    
    
    /**
            Builds the list of Networks
            */
    public void buildNetworkList() {
    
        try {
            sql C = new sql(Connection, DB, Username, Password);
            ArrayList result = null;
            String Q = "SELECT id, name FROM network";
            result = C.process(Q);
            
            if (result != null && result.size() != 0) {
                for (int i = 0; i < result.size(); i+=2) {
                    allNetworksList.add((String)result.get(i) + ": " + (String)result.get(i+1));
                }
            }
            
            C.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	/**
	Intialize the viewport with the given width/height background-color and graphics configuration.
	*/
	public NetworkAdmin(String connection,String user,String pass){
		this.Connection = connection;
		this.Connection1 = connection;
		Username=user;
		Password=pass;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
		setLayout(null);
		
        /* Set up two tabs: Network, and NPCs */
        
        /* First tab: Network Info */
        
        javax.swing.JTabbedPane tabby = new JTabbedPane();
        tabby.setBounds(5,5,780,560);

        JPanel networkTabPanel = new JPanel();
        networkTabPanel.setLayout(null);
        networkTabPanel.setBounds(5,5,780,560);
        
        JLabel label = new JLabel("Name");
        label.setBounds(5,5,50,25);
        networkNameField.setBounds(65,5, 200, 25);
        networkNameField.setEditable(false);
        networkTabPanel.add(label);
        networkTabPanel.add(networkNameField);
        
        JButton newNetwork = new JButton("New Network");
        newNetwork.setBounds(270, 5, 145, 25);
        newNetwork.setActionCommand("newNetwork");
        newNetwork.addActionListener(this);
        networkTabPanel.add(newNetwork);
        
        label = new JLabel("Entrance Message");
        label.setBounds(420, 35, 175, 25);
        networkTabPanel.add(label);
        
        entranceMessage.setBounds(420, 60, 300, 200);
        entranceMessage.addFocusListener(this);
        entranceMessage.setName("leaveEntranceCriteria");
        networkTabPanel.add(entranceMessage);
        
        label = new JLabel("Probability of NPC attack (between 0 and 1):");
        label.setBounds(420, 270, 250, 25);
        networkTabPanel.add(label);
        attackProbability.setBounds(670, 270, 50, 25);
        attackProbability.addFocusListener(this);
        attackProbability.setName("leaveAttackProbability");
        networkTabPanel.add(attackProbability);
        
        label = new JLabel("All Networks");
        label.setBounds(5, 35, 200, 25);
        allNetworksList.setBounds(5, 60, 200, 400);
        allNetworksList.setName("selectNetwork");
        allNetworksList.addActionListener(this);
        networkTabPanel.add(label);
        networkTabPanel.add(allNetworksList);
        
        JButton addAttachedNetwork = new JButton("Attach...");
        addAttachedNetwork.setActionCommand("addAttachedNetwork");
        addAttachedNetwork.addActionListener(this);
        addAttachedNetwork.setBounds(5, 465, 200, 25);
        networkTabPanel.add(addAttachedNetwork);
        
        label = new JLabel("Attached Networks");
        label.setBounds(215, 35, 200, 25);
        attachedNetworksList.setBounds(215, 60, 200, 400);
        attachedNetworksList.setName("attachedNetworksList");
        attachedNetworksList.addActionListener(this);
        networkTabPanel.add(label);
        networkTabPanel.add(attachedNetworksList);
        
        JButton removeAttachedNetwork = new JButton("Remove...");
        removeAttachedNetwork.setActionCommand("removeAttachedNetwork");
        removeAttachedNetwork.addActionListener(this);
        removeAttachedNetwork.setBounds(215, 465, 200, 25);
        networkTabPanel.add(removeAttachedNetwork);
        
        
        /* Second Tab: NPCs */

        JPanel NPCTabPanel = new JPanel();
        NPCTabPanel.setLayout(null);
        NPCTabPanel.setBounds(5,5,780,560);
        
        int labelHeight = 25;
        int spacer = 5;
        
        label = new JLabel("NPC Name: ");
        label.setBounds(5,5,70, 25);
        NPCTabPanel.add(label);
        
        npcName.setBounds(75,5,150,25);
        npcName.setName("leaveNpcName");
        npcName.addActionListener(this);
        NPCTabPanel.add(npcName);
        
        label = new JLabel("NPC Title: ");
        label.setBounds(250,5, 60, 25);
        NPCTabPanel.add(label);

        npcTitle.setBounds(310,5,150,25);
        npcTitle.addActionListener(this);
        npcTitle.setName("leaveNpcTitle");
        NPCTabPanel.add(npcTitle);
        
        label = new JLabel("All NPCs");
        label.setBounds(5,5 + labelHeight + spacer,100,25);
        NPCTabPanel.add(label);
        
        allNPCList.setBounds(5,35 + labelHeight + spacer,175,350 - (labelHeight + spacer));
        allNPCList.setName("allNPCList");
        //allNPCList.addItemListener(this);
        allNPCList.setMultipleMode(true);
		NPCTabPanel.add(allNPCList);
        
        label = new JLabel("Mining NPCs");
        label.setBounds(185,5 + labelHeight + spacer,100,25);
        NPCTabPanel.add(label);
        
        miningNPCs.setBounds(185,35 + labelHeight + spacer,175,350- (labelHeight + spacer));
        miningNPCs.setName("miningNPCs");
        miningNPCs.addActionListener(this);
        miningNPCs.addItemListener(this);
        NPCTabPanel.add(miningNPCs);
        
        label = new JLabel("Resource");
        label.setBounds(185, 390, 175, 25);
        NPCTabPanel.add(label);
        
        resources.setBounds(185, 410, 175, 25);
        resources.addActionListener(this);
        resources.setActionCommand("selectResource");
        NPCTabPanel.add(resources);
        
        label = new JLabel("Attack NPCs");
        label.setBounds(370,5 + labelHeight + spacer,100,25);
        NPCTabPanel.add(label);
        
        attackNPCs.setBounds(370,35 + labelHeight + spacer,175,400 - (labelHeight + spacer));
        attackNPCs.setName("attackNPCs");
        attackNPCs.addItemListener(this);
        NPCTabPanel.add(attackNPCs);
        
        label = new JLabel("Quest NPCs");
        label.setBounds(555,5 + labelHeight + spacer,100,25);
        NPCTabPanel.add(label);
        
        questNPCs.setBounds(555,35 + labelHeight + spacer,175,400- (labelHeight + spacer));
        questNPCs.setName("questNPCs");
        questNPCs.addItemListener(this);
        NPCTabPanel.add(questNPCs);
        
        label = new JLabel("Store NPC");
        label.setBounds(190, 480, 100, 25);
        storeNPC.setBounds(255, 480, 175,25);
        NPCTabPanel.add(label);
        NPCTabPanel.add(storeNPC);
        
        /* Add the Add buttons */
        JButton addMiningNPC = new JButton("Add as Mining");
        addMiningNPC.setActionCommand("addMiningNPC");
        addMiningNPC.addActionListener(this);
        addMiningNPC.setBounds(5, 390, 175, 25);
        NPCTabPanel.add(addMiningNPC);
        
        JButton addAttackNPC = new JButton("Add as Attack");
        addAttackNPC.setActionCommand("addAttackNPC");
        addAttackNPC.addActionListener(this);
        addAttackNPC.setBounds(5,420,175,25);
        NPCTabPanel.add(addAttackNPC);
        
        JButton addQuestNPC = new JButton("Add as Quest");
        addQuestNPC.setActionCommand("addQuestNPC");
        addQuestNPC.addActionListener(this);
        addQuestNPC.setBounds(5,450,175,25);
        NPCTabPanel.add(addQuestNPC);
        
        JButton addStoreNPC = new JButton("Add as Store");
        addStoreNPC.setActionCommand("addStoreNPC");
        addStoreNPC.addActionListener(this);
        addStoreNPC.setBounds(5,480,175,25);
        NPCTabPanel.add(addStoreNPC);
        
        /* Add the remove buttons */
        
        JButton removeMiningNPC = new JButton("Remove...");
        removeMiningNPC.setActionCommand("removeMiningNPC");
        removeMiningNPC.addActionListener(this);
        removeMiningNPC.setBounds(185, 440, 175, 25);
        NPCTabPanel.add(removeMiningNPC);        

        JButton removeAttackNPC = new JButton("Remove...");
        removeAttackNPC.setActionCommand("removeAttackNPC");
        removeAttackNPC.addActionListener(this);
        removeAttackNPC.setBounds(370, 440, 175, 25);
        NPCTabPanel.add(removeAttackNPC);        

        JButton removeQuestNPC = new JButton("Remove...");
        removeQuestNPC.setActionCommand("removeQuestNPC");
        removeQuestNPC.addActionListener(this);
        removeQuestNPC.setBounds(555, 440, 175, 25);
        NPCTabPanel.add(removeQuestNPC);        
        
        
        tabby.addTab("Network", networkTabPanel);
        tabby.addTab("NPCs", NPCTabPanel);
        this.add(tabby);
        
        
		this.pack();
		this.setBounds(0,0,800,600);
		this.repaint();
		this.setTitle("Networks Editor");
        
	}
	
    
    
    /** clears all the fields related to a network */
    private void clearFields() {
        attachedNetworksList.clear();
        miningNPCs.clear();
        attackNPCs.clear();
        questNPCs.clear();
        storeNPC.setText("");
        entranceMessage.setText("");
        attackProbability.setText("");
        npcName.setText("");
        npcTitle.setText("");
        resources.setSelectedIndex(0);
    }
    
    /** loads the selected network information */
    public void loadNetwork() {

        clearFields();
        
        try{
            // set the store NPC & attack probability
            sql C = new sql(Connection, DB, Username, Password);
            ArrayList result = null;
            String Q = "SELECT storeNPC, attack_probability FROM network WHERE id = " + networkId;
            result = C.process(Q);
            if (result != null && result.size() != 0) {
                for (int i = 0; i < result.size(); i+=2) {
                    storeNPC.setText((String)result.get(i));
                    attackProbability.setText((String)result.get(i+1));
                }
            }
            
            //set the network name
            networkNameField.setText(networkName);
            
            
            // set the attached networks
            Q = "SELECT n.id,n.name FROM network n INNER JOIN attached_networks an ON n.id = an.attached_network_id WHERE an.network_id = " + networkId;
            result = C.process(Q);
            if (result != null && result.size() != 0) {
                for (int i = 0; i < result.size(); i+=2) {
                    attachedNetworksList.add((String)result.get(i) + ": " + (String)result.get(i+1));
                }
            }

            //set the NPCs on this network
            Q = "SELECT npc_ip, npc_type FROM network_npc WHERE network_id = " + networkId;
            result = C.process(Q);
            if (result != null && result.size() != 0) {
                String npcType = "";
                for (int i = 0; i < result.size(); i+=2) {
                    npcType = (String)result.get(i+1);
                    if (npcType.equals("attack")) {
                        attackNPCs.add((String)result.get(i));
                    }
                    else if (npcType.equals("quest")) {
                        questNPCs.add((String)result.get(i));
                    }
                    else if (npcType.equals("mining")) {
                        miningNPCs.add((String)result.get(i));
                    }
                    else {
                        System.out.println("Damn, unrecongnized Network_NPC type!");
                    }
                }
            }
            
            C.close();

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /* we implemenet FocusListener */
    public void focusLost(FocusEvent fe) {
        if (networkId == "") {
            return;
        }
        
        if (((String)(fe.getComponent().getName())).equals("leaveEntranceCriteria") && attachedNetworkId != "") {
            try {
                sql C = new sql(Connection, DB, Username, Password);
                ArrayList result = null;
                String Q = "UPDATE attached_networks SET entranceMessage = '" + entranceMessage.getText() + "' WHERE network_id = " + networkId + " AND attached_network_id = " + attachedNetworkId;
				System.out.println(Q);
				result = C.process(Q);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        else if (((String)(fe.getComponent().getName())).equals("leaveAttackProbability") && attackProbability.getText().length() > 0) {
            try {
                float attackProb = (float)Float.parseFloat(attackProbability.getText());
                sql C = new sql(Connection, DB, Username, Password);
                ArrayList result = null;
                String Q = "UPDATE network SET attack_probability = " + attackProb + " WHERE id = " + networkId;
                result = C.process(Q);                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void focusGained(FocusEvent e) {}

    
    // implement ItemListener
    public void itemStateChanged(ItemEvent IE) {
        
        sql C = new sql(Connection, DB, Username, Password);
        ArrayList result = null;
        
        String selected = "";
        String listName = ((java.awt.List)IE.getItemSelectable()).getName();

        updatePreviousSelection();
        setNameAndTitle(listName);
        deselectPreviousSelection(listName);
        
        // store the newly selected item
        oldListSelection[0] = listName;
        oldListSelection[1] = ((java.awt.List)IE.getItemSelectable()).getSelectedItem();
        oldListSelection[2] = npcName.getText();
        oldListSelection[3] = npcTitle.getText();

    }
    
    private void updatePreviousSelection() {
        sql C = new sql(Connection, DB, Username, Password);
        ArrayList result = null;
        String Qname  = "";

        // update the old selected item
        if (oldListSelection[0] != null) {
            if (oldListSelection[0].equals("miningNPCs")) {
                Qname = "UPDATE network_npc SET name = '" + npcName.getText() + "', title = '" + npcTitle.getText() + "' WHERE network_id = " + networkId + " AND npc_ip = '" + oldListSelection[1] + "' AND npc_type = 'mining'";
            } else if (oldListSelection[0].equals("attackNPCs")) {
                Qname = "UPDATE network_npc SET name = '" + npcName.getText() + "', title = '" + npcTitle.getText() + "' WHERE network_id = " + networkId + " AND npc_ip = '" + oldListSelection[1] + "' AND npc_type = 'attack'";
            } else if (oldListSelection[0].equals("questNPCs")) {
                Qname = "UPDATE network_npc SET name = '" + npcName.getText() + "', title = '" + npcTitle.getText() + "' WHERE network_id = " + networkId + " AND npc_ip = '" + oldListSelection[1] + "' AND npc_type = 'quest'";
            } else {
                //System.out.println("You have entered a parallel universe.  All is chaos.  Repeat, all is chaos.");
            }
            
            try {
                result = C.process(Qname);
            } catch (Exception e) {
                e.printStackTrace();
            }        
        }
    }
    
    private void setNameAndTitle(String listName) {
        String selected = "";
        String Q = "";
        sql C = new sql(Connection, DB, Username, Password);
        ArrayList result = null;

        if (listName.equals("miningNPCs")) {
            selected = miningNPCs.getSelectedItem();
            Q = "SELECT name, title FROM network_npc WHERE network_id = " + networkId + " AND npc_ip = '" + selected + "' AND npc_type = 'mining'";
        } else if (listName.equals("attackNPCs")) {
            selected = attackNPCs.getSelectedItem();
            Q = "SELECT name, title FROM network_npc WHERE network_id = " + networkId + " AND npc_ip = '" + selected + "' AND npc_type = 'attack'";
        } else if (listName.equals("questNPCs")) {
            selected = questNPCs.getSelectedItem();
            Q = "SELECT name, title FROM network_npc WHERE network_id = " + networkId + " AND npc_ip = '" + selected + "' AND npc_type = 'quest'";
        }
        
        try {
            // load in the name/title for the newly selected item
            result = C.process(Q);
            if (result != null && result.size() > 0) {
                npcName.setText((String)result.get(0));
                npcTitle.setText((String)result.get(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void deselectPreviousSelection(String listName) {
    
        // deselect the previously selected item
        if (listName.equals("miningNPCs")) {
            if (oldListSelection[0] != null) {
                if (oldListSelection[0].equals("attackNPCs")) {
                    attackNPCs.deselect(attackNPCs.getSelectedIndex());
                } else if (oldListSelection[0].equals("questNPCs")) {
                    questNPCs.deselect(questNPCs.getSelectedIndex());
                }
            }
            
        } else if (listName.equals("attackNPCs")) {
            if (oldListSelection[0] != null) {
                if (oldListSelection[0].equals("miningNPCs")) {
                    miningNPCs.deselect(miningNPCs.getSelectedIndex());
                } else if (oldListSelection[0].equals("questNPCs")) {
                    questNPCs.deselect(questNPCs.getSelectedIndex());
                }
            }

            
        } else if (listName.equals("questNPCs")) {
            if (oldListSelection[0] != null) {
                if (oldListSelection[0].equals("attackNPCs")) {
                    attackNPCs.deselect(attackNPCs.getSelectedIndex());
                } else if (oldListSelection[0].equals("miningNPCs")) {
                    miningNPCs.deselect(miningNPCs.getSelectedIndex());
                }
            }
        }
    }
    
    // implement actionlistener
	public void actionPerformed(ActionEvent AE){
    
        try {
            
            sql C = new sql(Connection, DB, Username, Password);
            ArrayList result = null;
    		String name="";
            
    		if(AE.getSource() instanceof java.awt.List){
    			name=((java.awt.List)AE.getSource()).getName();
    		}
            
            if (AE.getActionCommand().equals("newNetwork")) {
                // pop up a window to enter the new network name
				String newNetworkName = (String)JOptionPane.showInputDialog(this,"Enter new network name:",null);
                String Q = "INSERT INTO network (name) VALUES ('" + newNetworkName + "')";
                result = C.process(Q);
                Q = "SELECT max(id) FROM network";
                result = C.process(Q);
                if (result != null && result.size() > 0) {
                    networkId = (String)result.get(0);
                    networkName = newNetworkName;
                }
                allNetworksList.add(networkId + ": " + networkName);
                loadNetwork();
    		} 
            
            else if (AE.getActionCommand().equals("selectResource")) {
                // if something's selected in the mining NPC list, then update the database when the resource is changed
                if (miningNPCs.getSelectedIndex() != -1) {
                    String selectedNPC = (String)miningNPCs.getItem(miningNPCs.getSelectedIndex());
                    String resourceName = (String)resources.getSelectedItem();
                    String Q = "UPDATE network_npc SET resource = '" + resourceName + "' WHERE network_id = " + networkId + " AND npc_ip = '" + selectedNPC + "' AND npc_type = 'mining'";
                    C.process(Q);
                }
            }
            
            else if(AE.getActionCommand().equals("addAttachedNetwork")) {
                //split apart the ids of the network to attach
                String attachedNetwork = (String)allNetworksList.getItem(allNetworksList.getSelectedIndex());
                String[] networkSplit = attachedNetwork.split(": ");
                attachedNetworkId = (String)networkSplit[0];
                
                //add the attached network that's selected
                
                /********* I need to be able to catch the exception thrown when there's a duplicate key, which isn't thrown by util.sql.process() 
                                                    Right now, it will update the GUI to be wrong, because I have no way of knowing that the UPDATE failed
                                            *******/
                
                //update both sides of the many to many...
                String Q = "INSERT INTO attached_networks (network_id, attached_network_id) VALUES(" +  networkId + ", " + attachedNetworkId + ")";
                String Q1 = "INSERT INTO attached_networks (network_id, attached_network_id) VALUES(" + attachedNetworkId + ", " + networkId + ")";
                
                result = C.process(Q);
                result = C.process(Q1);
                
                // update the attached networks list
                loadNetwork();
    		} 
            
    		else if(AE.getActionCommand().equals("removeAttachedNetwork")) {
                //split apart the ids of the networks
                String attachedNetwork =(String)attachedNetworksList.getItem(attachedNetworksList.getSelectedIndex());
                String[] networkSplit = attachedNetwork.split(": ");
                attachedNetworkId = (String)networkSplit[0];
                
                //delete the attached network that's selected
                String Q = "DELETE FROM attached_networks WHERE network_id = " + networkId + " AND attached_network_id = " + attachedNetworkId;
                String Q1 = "DELETE FROM attached_networks WHERE network_id = " + attachedNetworkId + " AND attached_network_id = " + networkId;
                result = C.process(Q);
                result = C.process(Q1);
                
                // update the attached networks list
                attachedNetworksList.remove(attachedNetworksList.getSelectedIndex());
                entranceMessage.setText("");
    		}
            
            else if (name.equals("attachedNetworksList")) {
                // clicked on the attached networks list
                String attachedNetwork =(String)attachedNetworksList.getItem(attachedNetworksList.getSelectedIndex());
                String[] networkSplit = attachedNetwork.split(": ");
                attachedNetworkId = (String)networkSplit[0];
                
                //load the entrance message
                String Q = "SELECT entranceMessage FROM attached_networks WHERE network_id = " + networkId + " AND attached_network_id = " + attachedNetworkId;
                result = C.process(Q);
                if (result != null && result.size() > 0) {
                    entranceMessage.setText((String)result.get(0));
                } else {
                    entranceMessage.setText("");
                }
            }

            else if(name.equals("selectNetwork")) {
                String network = (String)allNetworksList.getItem(allNetworksList.getSelectedIndex());
                String[] networkSplit = network.split(": ");
                networkId = (String)networkSplit[0];
                networkName = (String)networkSplit[1];
                loadNetwork();
    		} 
            else if (name.equals("miningNPCs")) {
                String selectedNPC = (String)miningNPCs.getItem(miningNPCs.getSelectedIndex());
                String Q = "SELECT resource FROM network_npc WHERE network_id = " + networkId + " AND npc_ip = '" + selectedNPC + "' AND npc_type = 'mining'";
                result = C.process(Q);
                if (result != null && result.size() > 0) {
                    String dbResource = (String)result.get(0);
                    // set the combo box to the right value
                    resources.setSelectedItem(dbResource);
                } else { System.out.println("SHIT");}
            }
            
            else if (AE.getActionCommand().equals("addMiningNPC") ){
            
                String[] selected = allNPCList.getSelectedItems();
                int listLength = selected.length;
                if (listLength > 0) {
                    NameDialog nameAndTitle = new NameDialog(this);
                    String npcNameValue = nameAndTitle.getName();//npcName.getText();
                    String npcTitleValue = nameAndTitle.getTitle();//npcTitle.getText();
                    for (int i=0; i < listLength; i++) {
                        //update the database
                        String Q = "INSERT INTO network_npc (network_id, npc_ip, npc_type, resource, name, title) VALUES (" + networkId + ", '" + selected[i] + "', 'mining', 'Duct Tape', '" + npcNameValue + "', '" + npcTitleValue + "')";
                        result = C.process(Q);
                        
                        //update the screen
                        miningNPCs.add(selected[i]);
                    }
                }
            }
            
            else if (AE.getActionCommand().equals("addAttackNPC") ){
                String[] selected = allNPCList.getSelectedItems();
                int listLength = selected.length;
                if (listLength > 0) {
                    NameDialog nameAndTitle = new NameDialog(this);
                    String npcNameValue = nameAndTitle.getName();//npcName.getText();
                    String npcTitleValue = nameAndTitle.getTitle();//npcTitle.getText();
                    for (int i=0; i < listLength; i++) {
                        //update the database
                        String Q = "INSERT INTO network_npc (network_id, npc_ip, npc_type, name, title) VALUES (" + networkId + ", '" + selected[i] + "', 'attack', '" + npcNameValue + "', '" + npcTitleValue + "')";
                        result = C.process(Q);
                        
                        //update the screen
                        attackNPCs.add(selected[i]);
                    }
                }
    		} 
            
            else if (AE.getActionCommand().equals("addQuestNPC") ){
                String[] selected = allNPCList.getSelectedItems();
                int listLength = selected.length;
                if (listLength > 0) {
                    NameDialog nameAndTitle = new NameDialog(this);
                    String npcNameValue = nameAndTitle.getName();//npcName.getText();
                    String npcTitleValue = nameAndTitle.getTitle();//npcTitle.getText();
                    for (int i=0; i < listLength; i++) {
                        //update the database
                        String Q = "INSERT INTO network_npc (network_id, npc_ip, npc_type, name, title) VALUES (" + networkId + ", '" + selected[i] + "', 'quest', '" + npcNameValue + "', '" + npcTitleValue + "')";
                        result = C.process(Q);
                        
                        //update the screen
                        questNPCs.add(selected[i]);
                    }
                }

    		} 
            
            else if (AE.getActionCommand().equals("addStoreNPC") ){
    			String selectedNPC = (String)allNPCList.getItem(allNPCList.getSelectedIndex());
                String Q = "UPDATE network SET storeNPC = '" + selectedNPC + "' WHERE id = " + networkId;
                result = C.process(Q);
                
                storeNPC.setText(selectedNPC);
    		} 
            
            else if (AE.getActionCommand().equals("removeMiningNPC") ){
    			//delete the NPC from the database
                int selectedIndex = miningNPCs.getSelectedIndex();
                if (selectedIndex >= 0) {
                    String selectedNPC = (String)miningNPCs.getItem(selectedIndex);
                    String Q = "DELETE FROM network_npc WHERE npc_ip = '" + selectedNPC + "' AND network_id = " + networkId + " AND npc_type = 'mining'";
                    result = C.process(Q);
                    
                    //delete them from the on-screen list
                    miningNPCs.remove(miningNPCs.getSelectedIndex());
                    npcName.setText("");
                    npcTitle.setText("");
                    resources.setSelectedIndex(0);
                }
    		} 
            
            else if (AE.getActionCommand().equals("removeAttackNPC") ){
                int selectedIndex = attackNPCs.getSelectedIndex();
                if (selectedIndex >= 0) {
        			String selectedNPC = (String)attackNPCs.getItem(selectedIndex);
                    String Q = "DELETE FROM network_npc WHERE npc_ip = '" + selectedNPC + "' AND network_id = " + networkId + " AND npc_type = 'attack'";
                    result = C.process(Q);
                    attackNPCs.remove(attackNPCs.getSelectedIndex());
                    npcName.setText("");
                    npcTitle.setText("");
                }
    		}
            
            else if (AE.getActionCommand().equals("removeQuestNPC") ){
                int selectedIndex = questNPCs.getSelectedIndex();
                if (selectedIndex >= 0) {
        			String selectedNPC = (String)questNPCs.getItem(selectedIndex);
                    String Q = "DELETE FROM network_npc WHERE npc_ip = '" + selectedNPC + "' AND network_id = " + networkId + " AND npc_type = 'quest'";
                    result = C.process(Q);
                    questNPCs.remove(questNPCs.getSelectedIndex());
                    npcName.setText("");
                    npcTitle.setText("");
                }
    		}
            
            C.close();
        
        } catch (Exception e) {
            e.printStackTrace();
        }
        
	}
    
    private void populateNameAndTitle() {
        npcName.setText("");
        npcTitle.setText("");
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
		NetworkAdmin x = new NetworkAdmin(args[0],args[1],args[2]);
		x.buildNPCTable();
        x.buildNetworkList();
	}

}//END.
