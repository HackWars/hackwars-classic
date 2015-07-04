package GUI;
/**

MessageWindow.java
this is the message window.
*/

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import javax.imageio.*;
import java.awt.*;
import java.awt.event.*;
import Assignments.*;

public class NetworkInfoPanel extends JPanel{

		private NetworkPanel networkPanel;
		private JLabel nameLabel = new JLabel(),ipLabel = new JLabel(), questLabel = new JLabel();
		private JLabel questCountLabel = new JLabel(),shippingCountLabel = new JLabel(),regularCountLabel = new JLabel(),storeCountLabel= new JLabel();
		private JTree treePane;
		private JScrollPane sp;
		public NetworkInfoPanel(NetworkPanel networkPanel){
			this.networkPanel=networkPanel;
			setLayout(new GridBagLayout());
			setBackground(MapPanel.NETWORK_INFO_BACKGROUND);
			//setPreferredSize(new Dimension(200,200));
			setOpaque(false);
			populate();
		}
		
		private void populate(){
			nameLabel.setText(networkPanel.getName());
			nameLabel.setFont(MapPanel.NETWORK_NAME_FONT);
			nameLabel.setForeground(MapPanel.NETWORK_NAME_COLOR);
			GridBagConstraints c = new GridBagConstraints();
			c.anchor = GridBagConstraints.FIRST_LINE_START;
			c.weightx=0.0;
			c.weighty=0.0;
			c.insets = new Insets(5,5,5,5);
			c.fill=GridBagConstraints.BOTH;
			add(nameLabel,c);
			
			/*ipLabel.setText(networkPanel.getIP());
			ipLabel.setFont(MapPanel.NETWORK_IP_FONT);
			ipLabel.setForeground(MapPanel.NETWORK_IP_COLOR);
			c.gridy=1;
			add(ipLabel,c);
			
			questCountLabel.setText(networkPanel.getQuestNPCs().length+" quest NPCs");
			questCountLabel.setFont(MapPanel.NETWORK_QUESTS_FONT);
			questCountLabel.setForeground(MapPanel.NETWORK_QUESTS_COLOR);
			c.gridy=2;
			add(questCountLabel,c);
			
			shippingCountLabel.setText(networkPanel.getShippingNPCs().length+" shipping NPCs");
			shippingCountLabel.setFont(MapPanel.NETWORK_QUESTS_FONT);
			shippingCountLabel.setForeground(MapPanel.NETWORK_QUESTS_COLOR);
			c.gridy=3;
			add(shippingCountLabel,c);
			
			regularCountLabel.setText(networkPanel.getRegularNPCs().length+" regular NPCs");
			regularCountLabel.setFont(MapPanel.NETWORK_QUESTS_FONT);
			regularCountLabel.setForeground(MapPanel.NETWORK_QUESTS_COLOR);
			c.gridy=4;
			add(regularCountLabel,c);*/
			
			c.gridx=0;
			/*c.gridy=1;
			c.weightx=1.0;
			c.weighty=1.0;
			c.fill=GridBagConstraints.BOTH;
			DefaultMutableTreeNode help = new DefaultMutableTreeNode("NPCs");
		
		DefaultMutableTreeNode tutorials = new DefaultMutableTreeNode("Quests"); 
		
		DefaultMutableTreeNode apis = new DefaultMutableTreeNode("Shipping");
		
		DefaultMutableTreeNode challenges = new DefaultMutableTreeNode("Regular");
		
		//createTutorialNodes(tutorials);
		//createAPINodes(apis);
		//createChallengesNodes(challenges);
		help.add(tutorials);
		help.add(apis);
		help.add(challenges);
		NetworkTreeCellRenderer renderer = new NetworkTreeCellRenderer();
		renderer.setLeafIcon(null);
		renderer.setOpenIcon(null);
		renderer.setClosedIcon(null);
		renderer.setBackgroundSelectionColor(Color.white);
		renderer.setBackgroundNonSelectionColor(new Color(0,0,0,0));
		renderer.setTextSelectionColor(Color.black);
		renderer.setTextNonSelectionColor(Color.white);
		renderer.setBorderSelectionColor(Color.gray);
		treePane = new JTree(help);
		treePane.setCellRenderer(renderer);
		treePane.setBackground(Color.black);
		treePane.setOpaque(false);
		treePane.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		//treePane.addTreeSelectionListener(this);

		
		sp = new JScrollPane(treePane);
		sp.setOpaque(false);
		sp.getViewport().setOpaque(false);
		sp.setBorder(null);
		add(sp,c);*/
			
			questCountLabel = new JLabel("Quests (0)");
			questCountLabel.setForeground(MapPanel.NETWORK_QUEST_COLOR);
			questCountLabel.setFont(MapPanel.NETWORK_QUESTS_FONT);
			c.gridy=2;
			add(questCountLabel,c);
			
			shippingCountLabel = new JLabel("Shipping (0)");
			shippingCountLabel.setForeground(MapPanel.NETWORK_SHIPPING_COLOR);
			shippingCountLabel.setFont(MapPanel.NETWORK_QUESTS_FONT);
			c.gridy=3;
			add(shippingCountLabel,c);
			
			regularCountLabel = new JLabel("Regular (0)");
			regularCountLabel.setForeground(MapPanel.NETWORK_REGULAR_COLOR);
			regularCountLabel.setFont(MapPanel.NETWORK_QUESTS_FONT);
			c.gridy=4;
			add(regularCountLabel,c);
			
			storeCountLabel = new JLabel("Store (0)");
			storeCountLabel.setForeground(MapPanel.NETWORK_STORE_COLOR);
			storeCountLabel.setFont(MapPanel.NETWORK_QUESTS_FONT);
			c.gridy=5;
			add(storeCountLabel,c);
			
			c.gridx++;
			JLabel l = new JLabel("");
			c.weightx=1.0;
			add(l,c);
			
			l = new JLabel("");
			c.gridy=6;
			c.insets = new Insets(0,0,20,0);
			c.weighty=1.0;
			add(l,c);
			
		}
		
		/*public void createTree(){
				DefaultMutableTreeNode NPCs = new DefaultMutableTreeNode("NPCs");
		
		DefaultMutableTreeNode quests = new DefaultMutableTreeNode("Quests ("+networkPanel.getQuestNPCs().length+")"); 
		
		DefaultMutableTreeNode shipping = new DefaultMutableTreeNode("Shipping ("+networkPanel.getShippingNPCs().length+")");
		
		DefaultMutableTreeNode regular = new DefaultMutableTreeNode("Regular ("+networkPanel.getRegularNPCs().length+")");
		NPCLabel l = new NPCLabel("JOE","555.555.5.555","","TITLE",NPCLabel.QUEST,null);
		DefaultMutableTreeNode npc = new DefaultMutableTreeNode(l); 
		
		//createTutorialNodes(tutorials);
		//createAPINodes(apis);
		//createChallengesNodes(challenges);
		NPCs.add(quests);
		NPCs.add(shipping);
		NPCs.add(regular);
		quests.add(npc);
		NetworkTreeCellRenderer renderer = new NetworkTreeCellRenderer();
		renderer.setLeafIcon(null);
		renderer.setOpenIcon(null);
		renderer.setClosedIcon(null);
		renderer.setBackgroundSelectionColor(Color.white);
		renderer.setBackgroundNonSelectionColor(new Color(0,0,0,0));
		renderer.setTextSelectionColor(Color.black);
		renderer.setTextNonSelectionColor(Color.white);
		renderer.setBorderSelectionColor(Color.gray);
		treePane = new JTree(NPCs);
		treePane.setCellRenderer(renderer);
		treePane.setBackground(Color.black);
		treePane.setOpaque(false);
		treePane.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		remove(sp);
		sp = new JScrollPane(treePane);
		sp.setOpaque(false);
		sp.getViewport().setOpaque(false);
		sp.setBorder(null);
		GridBagConstraints c = new GridBagConstraints();
		c.gridx=0;
		c.gridy=1;
		c.weightx=1.0;
		c.weighty=1.0;
		c.fill=GridBagConstraints.BOTH;
		add(sp,c);
			
		}*/


		public void setNetwork(String network){
			nameLabel.setText(network);
			//createTree();
			questCountLabel.setText("Quests ("+networkPanel.getQuestNPCs().length+")");
			shippingCountLabel.setText("Shipping ("+networkPanel.getShippingNPCs().length+")");
			regularCountLabel.setText("Regular ("+networkPanel.getRegularNPCs().length+")");
            storeCountLabel.setText("Store (" + networkPanel.getStoreNPCs().length + ")");
		}

}
