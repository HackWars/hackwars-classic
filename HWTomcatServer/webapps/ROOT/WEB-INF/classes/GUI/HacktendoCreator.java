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
import java.util.*;
import java.awt.image.*;
import java.util.zip.*;
import org.apache.axis.encoding.*;
import java.io.*;
import org.w3c.dom.Node;
import org.w3c.dom.NamedNodeMap;
import util.*;

public class HacktendoCreator extends Application implements ComponentListener{
	public static final int X_SCREENS=5;
	public static final int Y_SCREENS=3;
	private JDesktopPane mainPanel = new JDesktopPane();
	private Hacker MyHacker = null;
	private HacktendoScriptEditor MyScriptEditor = null;
	private ArrayList scripts = new ArrayList();
	private Tile[] tiles = new Tile[128];
	private Sprite[] sprites = new Sprite[64];
	//private ArrayList sprites = new ArrayList();
	private MapMaker mapMaker=null;
	private ResourceHandler resourceHandler=null;
	
	public HacktendoCreator(Hacker MyHacker){
		this.MyHacker=MyHacker;
		setBounds(25,25,800,600);
		MyHacker.getPanel().add(this);
		setTitle("Hacktendo Game Creator");
		setMaximizable(false);
		setResizable(false);
		setClosable(true);
		setIconifiable(true);
		setVisible(true);
		setLayout(null);
		addInternalFrameListener(this);
		addComponentListener(this);

		/*Component[] C = getComponents();
		for(int i=0;i<C.length;i++)
			System.out.println(i+": "+C[i]);
		setBorder(BorderFactory.createLineBorder(Color.black,4));*/
		setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
		populate();
		
	}
	
	public void populate(){
		createMenu();
		add(mainPanel);
		mainPanel.setBounds(0,0,getWidth(),548);
		mainPanel.setBackground(new Color(41,42,41));
		mainPanel.setLayout(null);
		MyScriptEditor = new HacktendoScriptEditor("Script Editor",false,false,false,true,mainPanel,this);
		MyScriptEditor.addComponentListener(this);
		mainPanel.add(MyScriptEditor);
		try{
			MyScriptEditor.setIcon(true);
		}catch(Exception e){}
		mapMaker = new MapMaker(this);
		mapMaker.addComponentListener(this);
		mainPanel.add(mapMaker);
		try{
			mapMaker.setIcon(true);
		}catch(Exception e){}
		
		resourceHandler = new ResourceHandler(this);
		resourceHandler.addComponentListener(this);
		mainPanel.add(resourceHandler);
		
		
	}
	
	private void createMenu(){
	JMenuBar menuBar = new JMenuBar();
	JMenu menu;
	JMenuItem menuItem;
	
	menu = new JMenu("File");
	menuItem =new JMenuItem("New Project");
	menu.add(menuItem);
	menuItem.addActionListener(this);
	
	menuItem =new JMenuItem("Open Project");
	menu.add(menuItem);
	menuItem.addActionListener(this);

	menuItem =new JMenuItem("Save Project");
	menu.add(menuItem);
	menuItem.addActionListener(this);
	
	menuItem =new JMenuItem("Exit");
	menu.add(menuItem);
	menuItem.addActionListener(this);
	
	menuBar.add(menu);
	
	menu = new JMenu("Tools");
	
	menuItem =new JMenuItem("Run Game");
	menu.add(menuItem);
	menuItem.addActionListener(this);
	
	menuItem =new JMenuItem("Pack Game");
	menu.add(menuItem);
	menuItem.addActionListener(this);
	
	menuBar.add(menu);
	
	setJMenuBar(menuBar);
		
	}
	
	public void addScript(Script script){
		scripts.add(script);
	}
	
	public void setScript(int id,Script script){
		Object[] obj = scripts.toArray();
		scripts.clear();
		for(int i=0;i<obj.length;i++){
			if(id!=i)
				scripts.add(obj[i]);
			else
				scripts.add(script);
		}
	}
	
	public Script getScript(int id){
		if(id<scripts.size())
			return (Script)scripts.get(id);
		else
			return null;
	}
	
	public void newScript(int type){
			
		HashMap HM = new HashMap();
		if(type==Script.MAP){
			HM.put("initialize","int main(){\n\n}");
			HM.put("continue","int main(){\n\n}");
			HM.put("finalize","int main(){\n\n}");
		}
		else{
			HM.put("fire","int main(){\n\n}");
		}
		scripts.add(new Script("Untitled",HM,type,scripts.size()));
		editScript(scripts.size()-1);
	}
	
	public void editScript(int id){
		MyScriptEditor.openFile(""+id);
		if(MyScriptEditor.isIcon()){
			try{
				MyScriptEditor.setIcon(false);
			}catch(Exception e){}
		}
		MyScriptEditor.moveToFront();
	}
	
	public Script[] getScripts(){
		Object[] obj = scripts.toArray();
		Script[] returnMe = new Script[obj.length];
		for(int i=0;i<obj.length;i++)
			returnMe[i]=(Script)obj[i];
		return returnMe;
	}
	
	public JDesktopPane getPanel(){
		return mainPanel;
	}
	
	public Sprite getSprite(int index){
		return sprites[index];
	}
	public void setSprite(int index,BufferedImage image,int trans){
		sprites[index] = new Sprite(image,trans,index);
	}
	
	public Tile[] getTiles(){
		return tiles;
	}
	
	public void importTiles(BufferedImage image,int index){
		for(int i=0;i<image.getWidth()/64;i++){
			for(int ii=0;ii<image.getHeight()/64;ii++){
				int tIndex=index+i+ii*image.getWidth()/64;
				if(tIndex>=128)
					break;
				BufferedImage img = new BufferedImage(64,64,BufferedImage.TYPE_BYTE_INDEXED);
				Graphics g = img.createGraphics();
				g.drawImage(image.getSubimage(i*64,ii*64,64,64),0,0,64,64,null);
				g.dispose();
				
				IndexColorModel MyIndexColorModel=(IndexColorModel)img.getColorModel();
				
				byte ri[]=new byte[256];
				byte gi[]=new byte[256];
				byte bi[]=new byte[256];
				for(int p=0;p<256;p++){
					int c[]=MyIndexColorModel.getComponents(p,null,0);
					ri[p]=(byte)c[0];
					gi[p]=(byte)c[1];
					bi[p]=(byte)c[2];
				}
				
				tiles[tIndex]=new Tile(img,tIndex,ri,gi,bi);
			}
		}
	}
	
	public String getXML(String filename){
		
		String xml = "<rawgame>";
		
		xml+=mapMaker.getXML();
		xml+="<tiles>";
		for(int i=0;i<tiles.length;i++){
			Tile tile = tiles[i];
			if(tile!=null){
				xml+=tile.getXML();
			}
		}
		xml+="</tiles><sprites>";
		for(int i=0;i<sprites.length;i++){
			Sprite sprite = sprites[i];
			if(sprite!=null){
				xml+=sprite.getXML();
			}
		}
		xml+="</sprites><scripts>";
		for(int i=0;i<scripts.size();i++){
			Script script = (Script)scripts.get(i);
			if(script!=null){
				xml+=script.getXML();
			}
		}
		
		xml+="</scripts>";
		xml+="</rawgame>";
		try{
			ByteArrayOutputStream bout=new ByteArrayOutputStream();
			ZipOutputStream zout=new ZipOutputStream(bout);
			byte b[] = xml.getBytes();
			zout.setLevel(9);
			zout.putNextEntry(new ZipEntry("game"));
			zout.write(5);
			zout.write(b,0,b.length);
			zout.closeEntry();
			zout.close();
			String info=Base64.encode(bout.toByteArray());
			String ip=MyHacker.getIP();
			xml = "<game><name><![CDATA["+filename+"]]></name><ip>"+ip+"</ip><licensed>false</licensed><data><![CDATA["+info+"]]></data></game>";
		}catch(Exception e){
			e.printStackTrace();
		}
		return xml;
	}
	
	public void receivedGame(HackerFile HF){
		HashMap HM = HF.getContent();
		String xml = (String)HM.get("data");
		loadGame(xml);
	
	}
	
	public void openGame(String filename,String folder){
		String encryptedIP = MyHacker.getEncryptedIP();
		View MyView = MyHacker.getView();
		MyHacker.setRequestedFile(Hacker.HACKTENDO_CREATOR);
		Object[] objects = {encryptedIP,folder,filename};
		MyView.setFunction("requestgame");
		MyView.addFunctionCall(new RemoteFunctionCall(0,"requestgame",objects));
		
	}
	
	
	public void loadGame(String xml){
		mapMaker.resetPassable();
		scripts.clear();
		Node N = null;
		LoadXML LX = new LoadXML();
		xml=xml.replaceAll("&gt;",">");
		
		try{
			if(!xml.equals("game.xml"))
				LX.loadString(xml);
			else
				LX.loadFile(xml);
				
			
			N = LX.findNodeRecursive("data",0);
			N = LX.findNodeRecursive(N,"#text",0);
			String info = N.getNodeValue();
			int size=0;
			byte buffer[]=new byte[512];
			byte B[]=Base64.decode(info);			
			
			ByteArrayOutputStream bout=new ByteArrayOutputStream();
			ZipInputStream zin=new ZipInputStream(new ByteArrayInputStream(B));
			zin.getNextEntry();
			int trans=zin.read();
			while((size=zin.read(buffer))!=-1){
				bout.write(buffer,0,size);
			}
			zin.close();
			info = bout.toString();
			//System.out.println(info);
			LX.loadString(info);
			//System.out.println("Starting Tiles");
			/*
			*
			*	TILES
			*
			*
			*/
			this.tiles = new Tile[128];
			Node tiles = LX.findNodeRecursive("tiles",0);
			int i=0;
			while((N=LX.findNodeRecursive(tiles,"tile",i))!=null){
				//Load the palette.
				Node pal=LX.findNodeRecursive(N,"palette",0);
				byte r[]=new byte[256];
				byte g[]=new byte[256];
				byte b[]=new byte[256];
				
				if(pal!=null){
					pal = LX.findNodeRecursive(pal,"#text",0);
					String data=pal.getNodeValue();
					B=Base64.decode(data);
					bout=new ByteArrayOutputStream();
					zin=new ZipInputStream(new ByteArrayInputStream(B));
					zin.getNextEntry();
					//trans=zin.read();
					while((size=zin.read(buffer))!=-1){
						bout.write(buffer,0,size);
					}
					zin.close();
					B = bout.toByteArray();
					for(int index=0;index<256;index++){
						r[index]=B[index];
						g[index]=B[index+256];
						b[index]=B[index+512];
					}
				}else{
					r=SpriteHandler.r;
					g=SpriteHandler.g;
					b=SpriteHandler.b;
				}
				
			
				Node temp = LX.findNodeRecursive(N,"id",0);
				temp=LX.findNodeRecursive(temp,"#text",0);
				int id = (int)Integer.parseInt(temp.getNodeValue());
				temp = LX.findNodeRecursive(N,"data",0);
				temp=LX.findNodeRecursive(temp,"#text",0);
				String data = temp.getNodeValue();
				//System.out.println(data);
				B=Base64.decode(data);	
				bout=new ByteArrayOutputStream();
				zin=new ZipInputStream(new ByteArrayInputStream(B));
				zin.getNextEntry();
				//trans=zin.read();
				while((size=zin.read(buffer))!=-1){
					bout.write(buffer,0,size);
				}
				zin.close();
				B = bout.toByteArray();
				
				if(B.length==1024){
					byte tb[]=new byte[4096];
					tb[0]=B[0];
					for(int x=0;x<32;x++){
						for(int y=0;y<32;y++){
							tb[x*2+y*128]=B[x+y*32];
							tb[x*2+1+y*128]=B[x+y*32];
							tb[x*2+y*128+64]=B[x+y*32];
							tb[x*2+1+y*128+64]=B[x+y*32];
						}
					}
					B=tb;
				}
												
				this.tiles[id]=new Tile(B,id,r,g,b);
				i++;
			}
			//System.out.println("Done Tiles");
			//System.out.println("Starting Sprites");
			/*
			*
			*	SPRITES
			*
			*
			*/
			tiles = LX.findNodeRecursive("sprites",0);
			buffer=new byte[1025];
			size=0;
			i=0;
			this.sprites = new Sprite[64];
			while((N=LX.findNodeRecursive(tiles,"sprite",i))!=null){
				//Load the palette.
				Node pal=LX.findNodeRecursive(N,"palette",0);
				byte r[]=new byte[256];
				byte g[]=new byte[256];
				byte b[]=new byte[256];
				
				if(pal!=null){
					pal = LX.findNodeRecursive(pal,"#text",0);
					String data=pal.getNodeValue();
					B=Base64.decode(data);
					bout=new ByteArrayOutputStream();
					zin=new ZipInputStream(new ByteArrayInputStream(B));
					zin.getNextEntry();
					//trans=zin.read();
					while((size=zin.read(buffer))!=-1){
						bout.write(buffer,0,size);
					}
					zin.close();
					B = bout.toByteArray();
					for(int index=0;index<256;index++){
						r[index]=B[index];
						g[index]=B[index+256];
						b[index]=B[index+512];
					}
				}else{
					r=SpriteHandler.r;
					g=SpriteHandler.g;
					b=SpriteHandler.b;
				}
			
				Node temp = LX.findNodeRecursive(N,"id",0);
				temp=LX.findNodeRecursive(temp,"#text",0);
				int id = (int)Integer.parseInt(temp.getNodeValue());
				Node frames = LX.findNodeRecursive(N,"frames",0);
				int ii=0;
				this.sprites[id]=new Sprite(id);
				
				this.sprites[id].setRGB(r,g,b);//Set the pallette for this sprite.
				
				while((temp=LX.findNodeRecursive(frames,"frame",ii))!=null){
				
					Node temp2 = LX.findNodeRecursive(temp,"data",0);
					temp2 = LX.findNodeRecursive(temp,"#text",0);
					String data = temp2.getNodeValue();
					//System.out.println(data);
					B=Base64.decode(data);	
					bout=new ByteArrayOutputStream();
					zin=new ZipInputStream(new ByteArrayInputStream(B));
					zin.getNextEntry();
					//trans=zin.read();
					while((size=zin.read(buffer))!=-1){
						bout.write(buffer,0,size);
					}
					zin.close();
					B = bout.toByteArray();
					
					if(B.length==1025){
						byte tb[]=new byte[4097];
						tb[0]=B[0];
						for(int x=0;x<32;x++){
							for(int y=0;y<32;y++){
								tb[x*2+1+y*128]=B[x+1+y*32];
								tb[x*2+2+y*128]=B[x+1+y*32];
								tb[x*2+1+y*128+64]=B[x+1+y*32];
								tb[x*2+2+y*128+64]=B[x+1+y*32];
							}
						}
						B=tb;
					}
					
					if(B.length>0)
						this.sprites[id].setFrame(B,ii);
					ii++;
				}
				//System.out.println(i+": "+id);
				i++;
			}
			//System.out.println("Done Sprites");
			//System.out.println("Starting Scripts");
			/*
			*
			*	SCRIPTS
			*
			*/
			tiles = LX.findNodeRecursive("scripts",0);
			i=0;
			while((N=LX.findNodeRecursive(tiles,"script",i))!=null){
				Node temp = LX.findNodeRecursive(N,"id",0);
				temp = LX.findNodeRecursive(temp,"#text",0);
				int id = Integer.parseInt(temp.getNodeValue());
				temp = LX.findNodeRecursive(N,"name",0);
				temp = LX.findNodeRecursive(temp,"#text",0);
				String name = temp.getNodeValue();
				temp = LX.findNodeRecursive(N,"type",0);
				temp = LX.findNodeRecursive(temp,"#text",0);
				int type=Integer.parseInt(temp.getNodeValue());
				HashMap HM = new HashMap();
				if(type==Script.MAP){
					temp = LX.findNodeRecursive(N,"initialize",0);
					temp = LX.findNodeRecursive(temp,"#text",0);
					String data = temp.getNodeValue();
					B=Base64.decode(data);	
					bout=new ByteArrayOutputStream();
					zin=new ZipInputStream(new ByteArrayInputStream(B));
					zin.getNextEntry();
					//trans=zin.read();
					while((size=zin.read(buffer))!=-1){
						bout.write(buffer,0,size);
					}
					zin.close();
					data = bout.toString();
					HM.put("initialize",data);
					temp = LX.findNodeRecursive(N,"continue",0);
					temp = LX.findNodeRecursive(temp,"#text",0);
					data = temp.getNodeValue();
					B=Base64.decode(data);	
					bout=new ByteArrayOutputStream();
					zin=new ZipInputStream(new ByteArrayInputStream(B));
					zin.getNextEntry();
					//trans=zin.read();
					while((size=zin.read(buffer))!=-1){
						bout.write(buffer,0,size);
					}
					zin.close();
					data = bout.toString();
					HM.put("continue",data);
					temp = LX.findNodeRecursive(N,"finalize",0);
					temp = LX.findNodeRecursive(temp,"#text",0);
					data = temp.getNodeValue();
					B=Base64.decode(data);	
					bout=new ByteArrayOutputStream();
					zin=new ZipInputStream(new ByteArrayInputStream(B));
					zin.getNextEntry();
					//trans=zin.read();
					while((size=zin.read(buffer))!=-1){
						bout.write(buffer,0,size);
					}
					zin.close();
					data = bout.toString();
					HM.put("finalize",data);
				}
				else{
					temp = LX.findNodeRecursive(N,"fire",0);
					temp = LX.findNodeRecursive(temp,"#text",0);
					String data = temp.getNodeValue();
					B=Base64.decode(data);	
					bout=new ByteArrayOutputStream();
					zin=new ZipInputStream(new ByteArrayInputStream(B));
					zin.getNextEntry();
					//trans=zin.read();
					while((size=zin.read(buffer))!=-1){
						bout.write(buffer,0,size);
					}
					zin.close();
					data = bout.toString();
					HM.put("fire",data);
					
				}
				Script script = new Script(name,HM,type,id);
				addScript(script);
				i++;
			}
			resourceHandler.repopulateScripts();
			//System.out.println("Done Scripts");
			//System.out.println("Starting Map");
			/*
			*
			*	MAP
			*
			*/
			tiles = LX.findNodeRecursive("maps",0);
			i=0;
			LoadXML LX2 = new LoadXML();
			while((N=LX.findNodeRecursive(tiles,"map",i))!=null){
				Node temp = LX.findNodeRecursive(N,"id",0);
				temp = LX.findNodeRecursive(temp,"#text",0);
				int id = Integer.parseInt(temp.getNodeValue());
				temp = LX.findNodeRecursive(N,"script",0);
				temp = LX.findNodeRecursive(temp,"#text",0);
				int scriptID=Integer.parseInt(temp.getNodeValue());
				mapMaker.setScript(id,scriptID);
				temp = LX.findNodeRecursive(N,"data",0);
				temp = LX.findNodeRecursive(temp,"#text",0);
				String data = temp.getNodeValue();
				B=Base64.decode(data);	
				bout=new ByteArrayOutputStream();
				zin=new ZipInputStream(new ByteArrayInputStream(B));
				zin.getNextEntry();
				//trans=zin.read();
				while((size=zin.read(buffer))!=-1){
					bout.write(buffer,0,size);
				}
				zin.close();
				data = bout.toString();
				//System.out.println(data);
				LX2.loadString(data);
				Node tiles2 =  LX2.findNodeRecursive("tiles",0);
				int ii=0;
				Node N2=null;
				while((N2=LX2.findNodeRecursive(tiles2,"tile",ii))!=null){
					Node temp2 = LX2.findNodeRecursive(N2,"#text",0);
					int tileID = Integer.parseInt(temp2.getNodeValue());
					
					boolean passable=true;
					if(tileID>=128){
						tileID-=128;
						passable=false;
					}
					
					mapMaker.drawTile(ii,id,tileID);
					if(!passable)
						mapMaker.setPassable(ii,id);
					ii++;
				}
				i++;
			}
			
			//System.out.println("Done Map");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void saveGame(String filename,String folder){
		
		HackerFile HF = new HackerFile(HackerFile.GAME_PROJECT);
		HF.setMaker(MyHacker.getUsername());
		HF.setName(filename);
		String xml = getXML(filename);
		HashMap HM = new HashMap();
		HM.put("data",xml);
		HM.put("level","1");
		HF.setQuantity(1);
		HF.setContent(HM);
		String encryptedIP = MyHacker.getEncryptedIP();
		View MyView = MyHacker.getView();
		Object[] objects = {encryptedIP,folder,HF};
		MyView.setFunction("savefile");
		MyView.addFunctionCall(new RemoteFunctionCall(0,"savefile",objects));
	}
	
	public void packGame(String filename,String folder){
		HackerFile HF = new HackerFile(HackerFile.GAME);
		HF.setMaker(MyHacker.getUsername());
		HF.setName(filename);
		String xml = getXML(filename);
		HashMap HM = new HashMap();
		HM.put("data",xml);
		HM.put("level","1");
		HF.setQuantity(30);
		HF.setContent(HM);
		String encryptedIP = MyHacker.getEncryptedIP();
		View MyView = MyHacker.getView();
		Object[] objects = {encryptedIP,folder,HF};
		MyView.setFunction("savefile");
		MyView.addFunctionCall(new RemoteFunctionCall(0,"savefile",objects));
	}
	
	
	public void actionPerformed(ActionEvent e){
		String ac=e.getActionCommand();
		if(ac.equals("Run Game")){
			String xml = getXML("");
			try{
				FileOutputStream FOS=new FileOutputStream("game.xml");
				FOS.write(xml.getBytes());
				FOS.close();
			}catch(Exception ex){
				ex.printStackTrace();
			}
			//System.out.println(xml);
			//System.out.println("File Made");
			MyHacker.startHacktendoPlayerDebug(xml);
		}
		if(ac.equals("Pack Game")){
			/*String answer = (String)JOptionPane.showInputDialog(
                    MyHacker.getPanel(),
                    "File Name:",
                    "Pack Project",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    "");
		    if(answer!=null){
				packGame(answer);
			
			}*/
			HacktendoFileChooser HFC = new HacktendoFileChooser(MyHacker,HacktendoFileChooser.PACK,"",null,this);
			mainPanel.add(HFC);
			HFC.moveToFront(); 
		}
		if(ac.equals("Open Project")){
		/*String answer = (String)JOptionPane.showInputDialog(
                    MyHacker.getPanel(),
                    "File Name:",
                    "Open Project",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    "");
		    if(answer!=null){
				//loadGame(answer);	
				if(answer.equals("game.xml")){
					loadGame("game.xml");
				}
				else{
					String encryptedIP = MyHacker.getEncryptedIP();
					View MyView = MyHacker.getView();
					MyHacker.setRequestedFile(Hacker.HACKTENDO_CREATOR);
					Object[] objects = {encryptedIP,"",answer};
					MyView.setFunction("requestgame");
					MyView.addFunctionCall(new RemoteFunctionCall(0,"requestgame",objects));
				}
			}*/
			HacktendoFileChooser HFC = new HacktendoFileChooser(MyHacker,HacktendoFileChooser.OPEN,"",null,this);
			mainPanel.add(HFC);
			HFC.moveToFront(); 
		}
		if(ac.equals("Save Project")){
			/*String answer = (String)JOptionPane.showInputDialog(
                    MyHacker.getPanel(),
                    "File Name:",
                    "Save Project",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    "");
		    if(answer!=null){
				saveGame(answer,"");
			
			}*/
			HacktendoFileChooser HFC = new HacktendoFileChooser(MyHacker,HacktendoFileChooser.SAVE,"",null,this);
			mainPanel.add(HFC);
			HFC.moveToFront();
		}
	}
	
	//Methods for component listener.
	public void componentShown(ComponentEvent e){

	}
	
	public void componentResized(ComponentEvent e){

	}
	
	public void componentHidden(ComponentEvent e){

	}
	
	public void componentMoved(ComponentEvent e){
		e.getComponent().repaint();
	}
}

