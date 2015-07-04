package GUI;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import Assignments.*;
import View.*;
import java.awt.image.*;
import java.awt.event.*;
import javax.swing.event.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.HashMap;
import java.util.Collection;
/**

MessageWindow.java
this is the message window.
*/

public class NetworkMapPanel extends JPanel implements ActionListener{

		private MapPanel mapPanel;
		private Hacker hacker;
		private BufferedImage back;
		private HashMap<String, NetworkButton> nodeList;
		
		public final static String[][] shortcuts = {
			{"UND", "LunarLabs"},
			{"DoSC", "DarkNet"},
			{"SubNet", "Wastelands"}
		
		};
		
		public final static String[][] connections = {
			{"UGOPNet","SubNet"},
			{"UGOPNet","ProgNet"},
			{"UGOPNet","LunarMicrosystems"},
			{"UGOPNet","DoSC"},
			{"UGOPNet","ArenaNet"},
			{"UGOPNet","UGoPIntranet"},
			{"UGOPNet","LawNet"},
			{"SubNet","UGOPNet"},
			{"SubNet","DarkNet"},
			{"ProgNet","UGOPNet"},
			{"ProgNet","UND"},
			{"ProgNet","UniversityNet"},
			{"DarkNet","SubNet"},
			{"DarkNet","UND"},
			{"DarkNet","SpyNet"},
			{"DarkNet","TerrorNet"},
			{"LunarMicrosystems","UGOPNet"},
			{"LunarMicrosystems","LunarCreditUnion"},
			{"LunarMicrosystems","LunarDatabank"},
			{"LunarMicrosystems","LunarCorporate"},
			{"LunarMicrosystems","LunarLabs"},
			{"DoSC","UGOPNet"},
			{"DoSC","DarkNet"},
			{"DoSC","DTNet"},
			{"DoSC","SiNet"},
			{"DoSC","DoSCDataBank"},
			{"DoSC","DoSCBank"},
			{"DTNet","DoSC"},
			{"DTNet","GeNet"},
			{"GeNet","DTNet"},
			{"GeNet","SiNet"},
			{"SiNet","DoSC"},
			{"SiNet","GeNet"},
			{"SiNet","YBCONet"},
			{"YBCONet","SiNet"},
			{"YBCONet","PuNet"},
			{"PuNet","YBCONet"},
			{"UND","ProgNet"},
			{"UND","DarkNet"},
			{"UniversityNet","ProgNet"},
			{"DoSCDataBank","DoSC"},
			{"DoSCBank","DoSC"},
			{"ArenaNet","UGOPNet"},
			{"ArenaNet","TheArena"},
			{"TheArena","ArenaNet"},
			{"LunarCreditUnion","LunarMicrosystems"},
			{"SpyNet","DarkNet"},
			{"LunarDatabank","LunarMicrosystems"},
			{"LunarCorporate","LunarMicrosystems"},
			{"LunarCorporate","LunarLabs"},
			{"LunarCorporate","LunarSpecOps"},
			{"LunarCorporate","LunarSat"},
			{"LunarCorporate","LunarColonies"},
			{"LunarLabs","LunarMicrosystems"},
			{"LunarLabs","UND"},
			{"LunarLabs","LunarCorporate"},
			{"LunarLabs","LunarSpecOps"},
			{"LunarLabs","LunarSat"},
			{"LunarLabs","LunarColonies"},
			{"LunarSpecOps","LunarCorporate"},
			{"LunarSpecOps","LunarLabs"},
			{"LunarSat","LunarCorporate"},
			{"LunarSat","LunarLabs"},
			{"LunarSat","LunarColonies"},
			{"LunarColonies","LunarCorporate"},
			{"LunarColonies","LunarLabs"},
			{"LunarColonies","LunarSat"},
			{"UGoPIntranet","UGOPNet"},
			{"UGoPIntranet","UGoPCorporate"},
			{"UGoPIntranet","UGoPDatabank"},
			{"UGoPIntranet","UGoPVault"},
			{"UGoPCorporate","UGoPIntranet"},
			{"UGoPCorporate","InnerCircle"},
			{"UGoPCorporate","LawNet"},
			{"UGoPDatabank","UGoPIntranet"},
			{"UGoPVault","UGoPIntranet"},
			{"TerrorNet","DarkNet"},
			{"TerrorNet","TerrorStash"},
			{"TerrorNet","TerrorWeaponsNet"},
			{"TerrorNet","TerrorLeaders"},
			{"TerrorStash","TerrorNet"},
			{"TerrorWeaponsNet","TerrorNet"},
			{"TerrorLeaders","TerrorNet"},
			{"TerrorLeaders","InnerCircle"},
			{"TerrorLeaders","Wastelands"},
			{"InnerCircle","UGoPCorporate"},
			{"InnerCircle","TerrorLeaders"},
			{"LawNet","UGOPNet"},
			{"LawNet","UGoPCorporate"},
			{"Wastelands","TerrorLeaders"}};
		  
		public NetworkMapPanel(Hacker hacker,MapPanel mapPanel){
			
			Dimension mapsize = mapPanel.networkPanel.getSize();
			double h = mapsize.getHeight();
			System.out.println(h);
			float multi = 400;
			HashMap<String, NetworkButton> networks = new HashMap<String, NetworkButton>();
			networks.put("UGOPNet",new NetworkButton("UGOPNet",3.0f,2.25f,1.1f,new Color(0, 0, 204),"images/browserhome.png",hacker, multi));
			networks.put("SubNet",new NetworkButton("SubNet",3.0f,3.5f,1,new Color(0, 0, 150),"images/down.png",hacker,multi));
			networks.put("ProgNet",new NetworkButton("ProgNet",3.5f,3.0f,1,new Color(0, 0, 150),"images/script.png",hacker,multi));
			networks.put("DarkNet",new NetworkButton("DarkNet",3.0f,4.0f,0.9f,new Color(50, 50, 50),"images/exit.png",hacker,multi));
			networks.put("LunarMicrosystems",new NetworkButton("LunarMicrosystems",2,4.5f,1,new Color(0, 153, 255),"images/cpu.png",hacker,multi));
			networks.put("DoSC",new NetworkButton("DoSC",2.25f,3.25f,1,new Color(0, 0, 204),"images/firewall.png",hacker,multi));
			networks.put("DTNet",new NetworkButton("DTNet",2.0f,2.0f,1,new Color(100, 100, 100),"images/ducttape.png",hacker,multi));
			networks.put("GeNet",new NetworkButton("GeNet",1.0f,2.0f,1,new Color(100, 100, 100),"images/germanium.png",hacker,multi));
			networks.put("SiNet",new NetworkButton("SiNet",1.0f,3.0f,1,new Color(100, 100, 100),"images/silicon.png",hacker,multi));
			networks.put("YBCONet",new NetworkButton("YBCONet",0.5f,2.5f,1,new Color(100, 100, 100),"images/YBCO.png",hacker,multi));
			networks.put("PuNet",new NetworkButton("PuNet",0,2.0f,0.8f,new Color(100, 100, 100),"images/plutonium.png",hacker,multi));
			networks.put("UND",new NetworkButton("UND",3.5f,3.5f,1,new Color(0, 0, 204),"images/firewall.png",hacker,multi));
			networks.put("UniversityNet",new NetworkButton("UniversityNet",4.5f,2.0f,1.0f,new Color(0, 0, 204),"images/browser.png",hacker,multi));
			networks.put("DoSCDataBank",new NetworkButton("DoSCDatabank",1.75f,2.5f,1.3f,new Color(0, 0, 204),"images/compile.png",hacker,multi));
			networks.put("DoSCBank",new NetworkButton("DoSCBank",2.5f,2.0f,1,new Color(0, 0, 204),"images/bank.png",hacker,multi));
			networks.put("ArenaNet",new NetworkButton("ArenaNet",4.5f,3.0f,1,new Color(204, 0, 0),"images/attack.png",hacker,multi));
			networks.put("TheArena",new NetworkButton("TheArena",4.0f,4.0f,1,new Color(204, 0, 0),"images/attack.png",hacker,multi));
			networks.put("LunarCreditUnion",new NetworkButton("LunarCreditUnion",2.5f,4,1,new Color(0, 153, 255),"images/pettycash.png",hacker,multi));
			networks.put("SpyNet",new NetworkButton("SpyNet",3.0f,4.0f,1,new Color(200, 200, 200),"images/watchIcon.png",hacker,multi));
			networks.put("LunarDatabank",new NetworkButton("LunarDatabank",1.0f,4.5f,1.4f,new Color(0, 153, 255),"images/bank.png",hacker,multi));
			networks.put("LunarCorporate",new NetworkButton("LunarCorporate",0,6.0f,1,new Color(0, 153, 255),"images/hd.png",hacker,multi));
			networks.put("LunarLabs",new NetworkButton("LunarLabs",2.5f,5.0f,1,new Color(0, 153, 255),"images/repair.png",hacker,multi));
			networks.put("LunarSpecOps",new NetworkButton("LunarSpecOps",1f,6.0f,1,new Color(0, 153, 255),"images/watchIcon.png",hacker,multi));
			networks.put("LunarSat",new NetworkButton("LunarSat",0.25f,5.0f,1,new Color(0, 153, 255),"images/scan.png",hacker,multi));
			networks.put("LunarColonies",new NetworkButton("LunarColonies",1.0f,5.5f,1,new Color(0, 153, 255),"images/new.png",hacker,multi));
			networks.put("UGoPIntranet",new NetworkButton("UGoPIntranet",2.5f,1,1,new Color(0, 0, 204),"images/http.png",hacker,multi));
			networks.put("UGoPCorporate",new NetworkButton("UGoPCorporate",3.0f,0,1,new Color(0, 0, 204),"images/hd.png",hacker,multi));
			networks.put("UGoPDatabank",new NetworkButton("UGoPDatabank",2.75f,1.5f,1.2f,new Color(0, 0, 204),"images/compile.png",hacker,multi));
			networks.put("UGoPVault",new NetworkButton("UGoPVault",3.0f,1.0f,1,new Color(0, 0, 204),"images/bank.png",hacker,multi));
			networks.put("TerrorNet",new NetworkButton("TerrorNet",5.5f,5.0f,0.9f,new Color(204, 0, 0),"images/refresh.png",hacker,multi));
			networks.put("TerrorStash",new NetworkButton("TerrorStash",4.5f,4.5f,1,new Color(204, 0, 0),"images/bank.png",hacker,multi));
			networks.put("TerrorWeaponsNet",new NetworkButton("TerrorWeaponsNet",5.0f,4.0f,1,new Color(204, 0, 0),"images/attack.png",hacker,multi));
			networks.put("TerrorLeaders",new NetworkButton("TerrorLeaders",6,6.0f,0.75f,new Color(204, 0, 0),"images/firewall.png",hacker,multi));
			networks.put("InnerCircle",new NetworkButton("InnerCircle",6,0,0.5f,new Color(150, 0, 100),"images/decompile.png",hacker,multi));
			networks.put("LawNet",new NetworkButton("LawNet",4.0f,1.5f,0.75f,new Color(150, 0, 100),"images/redirect.png",hacker,multi));
			networks.put("GroundZero",new NetworkButton("GroundZero",3.0f,5.5f,1,new Color(200, 200, 200),"images/ports.png",hacker,multi));
			networks.put("Wastelands",new NetworkButton("Wastelands",3.5f,4.5f,1.2f,new Color(204, 0, 0),"images/attack.png",hacker,multi));
			networks.put("JuniperPenetentiary",new NetworkButton("JuniperPenetentiary",7,0,0.75f,new Color(150, 0, 100),"images/firewall.png",hacker,multi));
			
			
			this.nodeList = networks;
			this.hacker=hacker;
			this.mapPanel=mapPanel;
			try{
				back = ImageLoader.getImage("images/NetMapFull.png");
			}catch(Exception e){}
			setLayout(null);
			setBackground(MapPanel.NETWORK_INFO_BACKGROUND);
			setPreferredSize(new Dimension(back.getWidth(),back.getHeight()));
		
			populate();
		}
		
		private void populate(){
			 //then draw the nodes
		    Collection<String> c = this.nodeList.keySet();
			Iterator<String> itr = c.iterator();
			
			while(itr.hasNext()) {
				String key = itr.next();
				add(this.nodeList.get(key));
			}

		}
		
		
		
		public void paintComponent(Graphics g){
			//clear(g);
			Graphics2D g2d = (Graphics2D)g;

			RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			
			g2d.setRenderingHints(rh);
			    
			g.setColor(new Color(0,0,0));
			g.fillRect(0,0,getWidth(),getHeight());
			
			//draw the connectors
			for (int i=1; i<connections.length; i++) {
			
				NetworkButton b1 = this.nodeList.get(connections[i][0]);
				NetworkButton b2 = this.nodeList.get(connections[i][1]);
				
				g2d.setStroke(new BasicStroke(2));

				//grey line
				g2d.setPaint(new Color(50, 50, 50));
				g2d.drawLine(b1.x + b1.width/2, b1.y + b1.height/2, b2.x + b2.width/2, b2.y + b2.height/2);

				g2d.setStroke(new BasicStroke(1));
				
				//b1 line
				g2d.setPaint(b1.baseColor);
				g2d.drawLine(b1.x + b1.width/2 - 2, b1.y - 2 + b1.height/2, b2.x - 2 + b2.width/2, b2.y - 2 + b2.height/2);
				//b2 line
				g2d.setPaint(b2.baseColor);
				g2d.drawLine(b1.x+2 + b1.width/2,  b1.y+2 + b1.height/2, b2.x+2 + b2.width/2, b2.y+2 + b2.height/2);

				//draw crosshairs around current
				
				String currentNode = mapPanel.networkPanel.getName();

				NetworkButton current = this.nodeList.get(currentNode);
				int x = current.x;
				int y = current.y;
				int height = current.height;
				int width = current.width;
				int l = (int)(height * 0.2);
				g2d.setStroke(new BasicStroke(6));
				g2d.setPaint(new Color(200, 200, 200));
				g2d.drawLine((int)(x + width * 0.5), y, (int)(x  + width * 0.5), y - l);
				g2d.drawLine(x, (int)(y + height * 0.5), x - l, (int)(y + height * 0.5));
				g2d.drawLine((int)(x + width * 0.5), y + height, (int)(x + width * 0.5), y + height + l);
				g2d.drawLine(x + width, (int)(y + height * 0.5), x + width + l, (int)(y + height * 0.5));

				g2d.setStroke(new BasicStroke(4));
				g2d.setPaint(current.baseColor);
				g2d.drawLine((int)(x + width * 0.5), y, (int)(x  + width * 0.5), y - l);
				g2d.drawLine(x, (int)(y + height * 0.5), x - l, (int)(y + height * 0.5));
				g2d.drawLine((int)(x + width * 0.5), y + height, (int)(x + width * 0.5), y + height + l);
				g2d.drawLine(x + width, (int)(y + height * 0.5), x + width + l, (int)(y + height * 0.5));

				
				
			}
			
		}
		
		public void actionPerformed(ActionEvent e){
			Object objects[] = {hacker.getEncryptedIP(), "GroundZero", "UGOPNet", "SubNet", "ProgNet", "UniversityNet", "UND", "DarkNet", "SpyNet", "ArenaNet", "The Arena", "LunarMicrosystems", "LunarDatabank", "LunarCreditUnion", "LunarCorporate", "LunarLabs", "LunarSat", "LunarColonies", "LunarSpecOps", "DoSC", "DoSCDatabank", "DoSCBank", "DTNet", "GeNet", "SiNet", "YBCONet", "PuNet", "LawNet", "UGoPIntranet", "UGoPDatabank", "UGoPVault", "UGoPCorporate", "InnerCircle", "TerrorLeaders", "TerrorNet", "TerrorWeaponsNet", "TerrorStash", "Wastelands"};
            hacker.getView().setFunction("changenetwork");
            hacker.getView().addFunctionCall(new RemoteFunctionCall(0,"changenetwork",objects));

		}
		


}
