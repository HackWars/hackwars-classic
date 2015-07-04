package GUI;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.undo.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.zip.*;
import org.apache.axis.encoding.*;
import java.io.*;

public class MapMaker extends JInternalFrame implements InternalFrameListener,ActionListener{

	private HacktendoCreator hacktendoCreator = null;
	private TilePicker tilePicker = null;
	private MapDrawer mapDrawer = null;
	//private Tile[] tiles;
	private Object[] maps = new Object[9];
	private JLabel positionLabel=null;
	private BigMapView bigMapView=null;
	private int[] scripts = new int[9];
	private boolean[][] passable = new boolean[9][1080];
	//private Tile[] map;
	public MapMaker(HacktendoCreator hacktendoCreator){
		this.hacktendoCreator=hacktendoCreator;
		setMaximizable(false);
		setResizable(false);
		setIconifiable(true);
		setClosable(false);
		setLayout(null);
		setTitle("Map Maker");
		addInternalFrameListener(this);
		tilePicker=new TilePicker(this);
		
		tilePicker.setPreferredSize(new Dimension(130,1025));
		JScrollPane sp = new JScrollPane(tilePicker);
		add(sp);
		sp.setBounds(2,5,150,321);
		for(int i=0;i<passable.length;i++){
			for(int ii=0;ii<passable[i].length;ii++){
				passable[i][ii]=false;
			}
		}
		//tiles = hacktendoCreator.getTiles();
		/*
		*
		*  This is only here for testing
		*
		*/
		/*tiles = new Tile[128];
		tiles[0] = new Tile(new int[1024]);
		for(int x=1;x<128;x++){
			int[] image = new int[1024];
			for(int i=0;i<1024;i++){
				//double rand = Math.random()*111000+num;
				double red = Math.random()*256.0;
				double blue = Math.random()*256.0;
				double green = Math.random()*256.0;
				int val = (int)(red*1000000+blue*1000+green);
				int mult = 1;
				if(i%2==0) mult=-1;
				image[i] = -val;
			}
			tiles[x] = new Tile(image);
		}*/
		mapDrawer = new MapDrawer(this);
		add(mapDrawer);
		mapDrawer.setBounds(sp.getWidth()+10,5,353,321);
		setBounds(10,10,sp.getWidth()+mapDrawer.getWidth()+40,400);
		positionLabel = new JLabel("(0,0)");
		add(positionLabel);
		positionLabel.setBounds(getWidth()-positionLabel.getPreferredSize().width-10,getHeight()-positionLabel.getPreferredSize().height-55,positionLabel.getPreferredSize().width,positionLabel.getPreferredSize().height);
		bigMapView = new BigMapView(this,0);
		hacktendoCreator.getPanel().add(bigMapView);
		bigMapView.addComponentListener(hacktendoCreator);
		try{
			bigMapView.setIcon(true);
		}catch(Exception e){}
		createMenu();
		
		setVisible(true);
		
	}
	
	public void createMenu(){
		JMenuBar menuBar = new JMenuBar();
		JMenu menu;
		JMenuItem menuItem;
	
		menu = new JMenu("Map");
		ButtonGroup group = new ButtonGroup();
		JRadioButtonMenuItem radiomenuItem = new JRadioButtonMenuItem("0");
		radiomenuItem.addActionListener(this);
		radiomenuItem.setSelected(true);
		group.add(radiomenuItem);
		menu.add(radiomenuItem);
		
		radiomenuItem = new JRadioButtonMenuItem("1");
		radiomenuItem.addActionListener(this);
		group.add(radiomenuItem);
		menu.add(radiomenuItem);
		
		radiomenuItem = new JRadioButtonMenuItem("2");
		radiomenuItem.addActionListener(this);
		group.add(radiomenuItem);
		menu.add(radiomenuItem);
		
		radiomenuItem = new JRadioButtonMenuItem("3");
		radiomenuItem.addActionListener(this);
		group.add(radiomenuItem);
		menu.add(radiomenuItem);
		
		radiomenuItem = new JRadioButtonMenuItem("4");
		radiomenuItem.addActionListener(this);
		group.add(radiomenuItem);
		menu.add(radiomenuItem);
		
		radiomenuItem = new JRadioButtonMenuItem("5");
		radiomenuItem.addActionListener(this);
		group.add(radiomenuItem);
		menu.add(radiomenuItem);
		
		radiomenuItem = new JRadioButtonMenuItem("6");
		radiomenuItem.addActionListener(this);
		group.add(radiomenuItem);
		menu.add(radiomenuItem);
		
		radiomenuItem = new JRadioButtonMenuItem("7");
		radiomenuItem.addActionListener(this);
		group.add(radiomenuItem);
		menu.add(radiomenuItem);
		
		radiomenuItem = new JRadioButtonMenuItem("8");
		radiomenuItem.addActionListener(this);
		group.add(radiomenuItem);
		menu.add(radiomenuItem);
		
		menuBar.add(menu);
		
		menu = new JMenu("Options");
		JCheckBoxMenuItem checkmenuItem = new JCheckBoxMenuItem("Grid",true);
		checkmenuItem.addActionListener(this);
		menu.add(checkmenuItem);
		
		menuItem = new JMenuItem("Script");
		menuItem.addActionListener(this);
		menu.add(menuItem);
		
		menuBar.add(menu);
		
		setJMenuBar(menuBar);	
	}
	
	public Tile[] getTiles(){
		return hacktendoCreator.getTiles();
	}
	
	public void drawTile(int index,int map){
		int[] tiles = (int[])maps[map];
		if(tiles==null){
			tiles = new int[1080];
			for(int i=0;i<1080;i++){
				tiles[i]= tilePicker.getChosen();;
			}
		}
		if(index>=0)
			tiles[index] = tilePicker.getChosen();
		maps[map]=tiles;
		bigMapView.repaint();
		
	}
	public void drawTile(int index,int map,int tileID){
		int[] tiles = (int[])maps[map];
		if(tiles==null){
			tiles = new int[1080];
			for(int i=0;i<1080;i++){
				tiles[i]=tileID;
			}
		}
		if(index>=0)
			tiles[index] = tileID;
		maps[map]=tiles;
		mapDrawer.repaint();
		bigMapView.repaint();
	}
	/**
	Reset the passability values.
	*/
	public void resetPassable(){
		for(int i=0;i<passable.length;i++){
			for(int ii=0;ii<passable[i].length;ii++){
				passable[i][ii]=false;
			}
		}
	}
	
	/**
	Set whether this tile on the map is passable.
	*/
	public void setPassable(int index,int map){
		if(passable[map][index]){
			passable[map][index]=false;
		}else{
			passable[map][index]=true;
		}
		mapDrawer.repaint();
		bigMapView.repaint();
	}
	
	/**
	Return whether or not the tile indicated is passable.
	*/
	public boolean getPassable(int index,int map){
		return(passable[map][index]);
	}
	
	public int[] getMap(int map){
		return (int[])maps[map];
	}
	
	public void setPosition(int x,int y){
		//System.out.println("Setting Position");
		positionLabel.setText("("+x+","+y+")");
		positionLabel.setBounds(getWidth()-positionLabel.getPreferredSize().width-10,getHeight()-positionLabel.getPreferredSize().height-55,positionLabel.getPreferredSize().width,positionLabel.getPreferredSize().height);
		//positionLabel.setBounds(135,330,positionLabel.getPreferredSize().width,positionLabel.getPreferredSize().height);
		//repaint();
		
	}
	
	public void setScreen(int x,int y){
		mapDrawer.setScreen(x,y);
		bigMapView.setScreen(x,y);
	}
	
	public void setScript(int map,int scriptID){
		scripts[map]=scriptID;
	}
	
	public String getXML(){
		String xml="<maps>";
		for(int i=0;i<9;i++){
			int[] map = (int[])maps[i];
			if(map!=null){
				xml+="<map><id>"+i+"</id><script>"+scripts[i]+"</script>";
				String tiles="<tiles>";
				for(int ii=0;ii<map.length;ii++){
					int id = map[ii];
					boolean passable = this.passable[i][ii];
					if(passable)
						id=id|0x80;
					tiles+="<tile>"+id+"</tile>";
				}
				tiles+="</tiles>";
				try{
					byte[] b = tiles.getBytes();
					ByteArrayOutputStream bout=new ByteArrayOutputStream();
					ZipOutputStream zout=new ZipOutputStream(bout);
				
					zout.setLevel(9);
					zout.putNextEntry(new ZipEntry("map"));
					zout.write(b,0,b.length);
					zout.closeEntry();
					zout.close();
					String info=Base64.encode(bout.toByteArray());
					xml+="<data><![CDATA["+info+"]]></data></map>";
			
					
				}catch(Exception e){
					e.printStackTrace();
				}
	
				
			}
		}
		
		xml+="</maps>";
		return xml;
	}
	
	
	public void internalFrameIconified(InternalFrameEvent e) {
		JDesktopIcon DI = getDesktopIcon();
		DI.setBackground(new Color(41,42,41));
		
		JButton b = (JButton)DI.getUI().getAccessibleChild(DI,0);
		JLabel l = (JLabel)DI.getUI().getAccessibleChild(DI,1);
		//b.setFocusPainted(false);
		b.setContentAreaFilled(false);
		b.setForeground(Color.WHITE);
		
		//b.setBorderPainted(false);
		
		DI.getUI().update(DI.getGraphics(),DI);
		//getDesktopIcon().setLocation(162,182);
		//getDesktopIcon().moveToFront();
	}
	
	public void internalFrameClosing(InternalFrameEvent e) {
	}

	public void internalFrameClosed(InternalFrameEvent e) {
	}

	public void internalFrameOpened(InternalFrameEvent e) {
	}

	public void internalFrameDeiconified(InternalFrameEvent e) {

	}

	public void internalFrameActivated(InternalFrameEvent e) {

	}

	public void internalFrameDeactivated(InternalFrameEvent e) {

	}
	
	public void actionPerformed(ActionEvent e){
		if(e.getActionCommand().equals("Grid")){
			JCheckBoxMenuItem check =(JCheckBoxMenuItem)e.getSource(); 
			mapDrawer.setGrid(check.getState());
		}
		if(e.getSource() instanceof JRadioButtonMenuItem){
			int map=Integer.parseInt(e.getActionCommand());
			mapDrawer.setMap(map);
			bigMapView.setMap(map);
		}
		if(e.getActionCommand().equals("Script")){
			Script[] scripts = hacktendoCreator.getScripts();
			String[] options = new String[scripts.length];
			for(int i=0;i<options.length;i++){
				options[i]=""+i;//scripts[i].getName();
			}
			String answer = (String)JOptionPane.showInputDialog(this,
				"Script:",
				"Set Script",
				JOptionPane.PLAIN_MESSAGE,
				null,
				options,
				"0");
			if(answer!=null){
				this.scripts[mapDrawer.getMap()]=Integer.parseInt(answer);
			}
		}
	}

}
