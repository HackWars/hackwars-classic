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
import java.awt.image.*;
import Assignments.*;
import java.util.*;

public class NetworkPanel extends JPanel implements AdjustmentListener{

		private Hacker hacker;
		private MapPanel mapPanel;
		private String name="Unable to connect to UGoPNet",ip="Please Login Again";
		private String[] questNPCs=new String[]{"you","are"},shippingNPCs=new String[]{"not", "logged", "in"},regularNPCs = new String[]{"please","reconnect"},storeNPCs=new String[]{};
		private JLabel nameLabel = new JLabel();
		private NetworkInfoPanel networkInfoPanel;
		private int questsCompleted=3,totalQuests=15;
		private BufferedImage back=null;
		private JPanel innerScroll;
		public NetworkPanel(Hacker hacker,MapPanel mapPanel){
			try{
                //load in the default network background image - UGOP
				back = ImageLoader.getImage("images/UGOPNet.png");
			}catch(Exception e){}
			this.hacker=hacker;
			this.mapPanel=mapPanel;
			setLayout(new GridBagLayout());
			populate();
		}
		
		private void populate(){
			networkInfoPanel = new NetworkInfoPanel(this);
			GridBagConstraints c = new GridBagConstraints();
			c.fill=GridBagConstraints.BOTH;
			c.weightx=1.0;
			c.weighty=1.0;
			c.anchor=GridBagConstraints.FIRST_LINE_START;
			add(networkInfoPanel,c);
			
			innerScroll = new JPanel();
			innerScroll.setLayout(new GridBagLayout());
			innerScroll.setBackground(MapPanel.NETWORK_INFO_BACKGROUND);
			GridBagConstraints cp = new GridBagConstraints();
			cp.anchor = GridBagConstraints.NORTHWEST;
			cp.weightx=0.2;
			cp.weighty=0.0;
			cp.insets = new Insets(0,5,0,0);
			cp.fill = GridBagConstraints.NONE;
			int y =2;
			JLabel l = new JLabel("NPC List");
			//l.setForeground(MapPanel.NETWORK_QUESTS_FONT);
			l.setBackground(MapPanel.HEADER_COLOR);
			cp.gridy = 0;
			innerScroll.add(l,cp);
			cp.weighty=0.0;
			cp.gridheight=1;
			
			
			//String[] regularNPCs = networkPanel.getRegularNPCs();
			for(int i=0;i<regularNPCs.length;i++){
					l = new JLabel(regularNPCs[i]);
					l.setForeground(MapPanel.NETWORK_REGULAR_COLOR);
					l.setFont(MapPanel.NETWORK_QUESTS_FONT);
					l.setHorizontalAlignment(SwingConstants.LEFT);
					cp.gridy=y;
					y++;
					innerScroll.add(l,cp);
			}
			
			//String[] questNPCs = networkPanel.getQuestNPCs();
			for(int i=0;i<questNPCs.length;i++){
					l = new JLabel(questNPCs[i]);
					l.setForeground(MapPanel.NETWORK_QUEST_COLOR);
					l.setFont(MapPanel.NETWORK_QUESTS_FONT);
					cp.gridy=y;
					y++;
					innerScroll.add(l,cp);
			}
			
			//String[] shippingNPCs = networkPanel.getShippingNPCs();
			for(int i=0;i<shippingNPCs.length;i++){
					l = new JLabel(shippingNPCs[i]);
					l.setForeground(MapPanel.NETWORK_SHIPPING_COLOR);
					l.setFont(MapPanel.NETWORK_QUESTS_FONT);
					cp.gridy=y;
					y++;
					innerScroll.add(l,cp);
			}
			
			l = new JLabel("");
			cp.gridy=y;
			cp.weighty=1.0;
			innerScroll.add(l,cp);
			
			l = new JLabel("");
			cp.gridx=1;
			cp.weightx=1.0;
			innerScroll.add(l,cp);
			//innerScroll.setOpaque(false);
			
			JScrollPane sp = new JScrollPane(innerScroll);
			sp.getViewport().setOpaque(false);
			sp.setOpaque(false);
			sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			sp.getVerticalScrollBar().addAdjustmentListener(this);
			c.gridx = 1;
			c.gridy = 0;
			c.gridheight=2;
			c.weightx=1.0;
			c.weighty=1.0;
			c.fill=GridBagConstraints.BOTH;
			add(sp,c);
		}
		
		public String getName(){
			return name;
		}
		
		public void setNetwork(PacketNetwork network){
			name=network.getName();
			//networkInfoPanel.setNetwork(network);
			innerScroll.removeAll();
			GridBagConstraints cp = new GridBagConstraints();
			cp.anchor = GridBagConstraints.FIRST_LINE_START;
			cp.weightx=1.0;
			cp.weighty=0.0;
			cp.insets = new Insets(0,5,0,0);
			cp.fill = GridBagConstraints.NONE;
			//cp.ipadx = 30;
			int y =3;
			JLabel label = new JLabel("NPC List");
			label.setFont(MapPanel.NETWORK_NPC_FONT);
			label.setForeground(MapPanel.NETWORK_NAME_COLOR);
			cp.gridy = 0;
			innerScroll.add(label,cp);
			cp.weightx=0.0;
			cp.weighty=0.0;
			//cp.ipady
			cp.gridheight=1;
			cp.anchor = GridBagConstraints.FIRST_LINE_START;
			NPCLabel l;
			cp.gridy++;

            // Add Ali as the store -- this no longer will work because we have multiple nodes.
			//l = new NPCLabel("Ali",hacker.getStoreIP(),"","Store",NPCLabel.STORE,hacker);
			//innerScroll.add(l,cp);
			
			Object[] regular = network.getRegularNPCs().toArray();
			Object[] quest = network.getQuestNPCs().toArray();
			Object[] mining = network.getMiningNPCs().toArray();
            Object[] store = network.getStoreNPCs().toArray();
			questNPCs = new String[quest.length];
			shippingNPCs = new String[mining.length];
            regularNPCs = new String[regular.length];
            storeNPCs = new String[store.length];

            for (int i=0; i < store.length; i++) {
				l = new NPCLabel((String)((HashMap)store[i]).get("name"),(String)((HashMap)store[i]).get("ip"),"",(String)((HashMap)store[i]).get("title"),NPCLabel.STORE,hacker,this);
				cp.gridy=y;
				y++;
				innerScroll.add(l,cp);
            }
            
			for(int i=0;i<quest.length;i++){
				l = new NPCLabel((String)((HashMap)quest[i]).get("name"),(String)((HashMap)quest[i]).get("ip"),"",(String)((HashMap)quest[i]).get("title"),NPCLabel.QUEST,hacker,this);
				cp.gridy=y;
				y++;
				innerScroll.add(l,cp);
			}
			
			for(int i=0;i<mining.length;i++){
				l = new NPCLabel((String)((HashMap)mining[i]).get("name"),(String)((HashMap)mining[i]).get("ip"),(String)((HashMap)mining[i]).get("commodity"),(String)((HashMap)mining[i]).get("title"),NPCLabel.SHIPPING,hacker,this);
				cp.gridy=y;
				y++;
				innerScroll.add(l,cp);
			}
			
			for(int i=0;i<regular.length;i++){
				l = new NPCLabel((String)((HashMap)regular[i]).get("name"),(String)((HashMap)regular[i]).get("ip"),"",(String)((HashMap)regular[i]).get("title"),NPCLabel.REGULAR,hacker,this);
				cp.gridy=y;
				y++;
				innerScroll.add(l,cp);
			}
			
			label = new JLabel("");
			cp.gridy=y;
			cp.weighty=1.0;
			innerScroll.add(label,cp);
            
            // change the background image 
// Temp fix to get rid of errors when in other networks
// besides UGOPNet
//            back = ImageLoader.getImage("images/" + name + ".png");
            back = ImageLoader.getImage("images/UGOPNet.png");
            
			networkInfoPanel.setNetwork(name);
		}
		
		
		public String[] getQuestNPCs(){
			return questNPCs;
		}
		
		public String[] getShippingNPCs(){
			return shippingNPCs;
		}
		
		public String[] getRegularNPCs(){
			return regularNPCs;
		}
        
        public String[] getStoreNPCs() {
            return storeNPCs;
        }
		
		public void paintComponent(Graphics g){
			g.setColor(new Color(0,0,0));
			g.fillRect(0,0,getWidth(),getHeight());
			g.drawImage(back,0,0,null);
			
		}
		
		public void adjustmentValueChanged(AdjustmentEvent e){
			repaint();
		}

}
