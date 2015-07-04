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

public class DropTable extends JFrame implements MouseListener,ActionListener{
	//MYSQL INFO.
	private String Connection="localhost";
	private String DB="hackwars";
	private String Username="root";
	private String Password="";
	
	private String dropTableId = "";
	private String itemId="";

	private java.awt.List DropTableList=new java.awt.List(15,true);
	private ArrayList DropTableListKeys=new ArrayList();
	
	private java.awt.List CurrentDropList=new java.awt.List(15,true);
	private ArrayList CurrentDropListKeys=new ArrayList();
	
	
	private java.awt.List AllItems=new java.awt.List(15,true);
	private ArrayList AllItemsKeys = new ArrayList();
	
	private JTextArea ItemEditArea=new JTextArea();
	private JTextField ItemDescription=new JTextField();
	
	/**
	Load in the drop table list.
	*/
	public void buildDropTableList(){
		try{
			System.out.println("Trying to build drop table list.");
		
			sql C=new sql(Connection,DB,Username,Password);
			ArrayList result=null;			
			String Q="select DISTINCT drop_id from drop_table order by drop_id ASC";
			result=C.process(Q);	
			
			System.out.println(result);
			
			if(result!=null) {
				for(int i=0;i<result.size();i++){
					String id=(String)result.get(i);
					DropTableList.add(id);
					DropTableListKeys.add(id);
				}
			}
			
			C.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	/**
	Builds the list of items.
	*/
	public void buildItemTable(){
		AllItems.clear();
		AllItemsKeys.clear();
		try{
			sql C=new sql(Connection,DB,Username,Password);
			ArrayList result=null;
			String Q="select id,description from items order by id ASC";
			result=C.process(Q);	
			
			if(result!=null)
			for(int i=0;i<result.size();i+=2){
				String id=(String)result.get(i);
				String description=(String)result.get(i+1);
				AllItems.add("Item ID: "+id+", Description:"+description);
				AllItemsKeys.add(id);
			}
			
			C.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	Intialize the viewport with the given width/height background-color and graphics configuration.
	*/
	public DropTable(String host,String user,String pass){
		
		Connection = host;
		Username = user;
		Password = pass;
	
		setVisible(true);
		setLayout(null);
		JLabel Label=new JLabel("Drop Tables");
		Label.setBounds(5,5,200,20);
		this.add(Label);
		Label = new JLabel("Current Drop Table");
		Label.setBounds(210,5, 200, 20);
		this.add(Label);
		Label = new JLabel("All Items");
		Label.setBounds(415, 5, 200, 20);
		this.add(Label);
		DropTableList.setBounds(5,25,200,200);
		DropTableList.addActionListener(this);
		DropTableList.setName("Load_Current_Drop_Table");
		DropTableList.setMultipleMode(false);
		
		JButton Button=new JButton("Create");
		Button.addActionListener(this);
		Button.setActionCommand("New_Drop_Table");
		Button.setBounds(5,230,200,25);
		this.add(Button);
		
		CurrentDropList.setMultipleMode(false);
		CurrentDropList.setBounds(210, 25, 200, 200);
		Button=new JButton("Remove From Drop Table");
		Button.addActionListener(this);
		Button.setActionCommand("Remove_Drop_Item");
		Button.setBounds(210,230,200,25);
		this.add(Button);
		
		
		AllItems.setBounds(415, 25, 500, 200);
		AllItems.setName("Load_Item");
		AllItems.setMultipleMode(false);
		AllItems.addActionListener(this);
		Button=new JButton("Add To Drop Table");
		Button.addActionListener(this);
		Button.setActionCommand("Add_Item_To_Drop_Table");
		Button.setBounds(415,230,200,25);
		this.add(Button);
		
		this.add(DropTableList);
		this.add(CurrentDropList);
		this.add(AllItems);
		
		
		Label = new JLabel("Edit Item");
		Label.setBounds(5,280,200, 20);
		this.add(Label);

		Label = new JLabel("Description:");
		Label.setBounds(5,310,100, 20);
		this.add(Label);
		
		ItemDescription.setBounds(100,310,200,20);
		this.add(ItemDescription);
		
		JScrollPane MyScrollPane=new JScrollPane();
		MyScrollPane.setBounds(5,350,615,200);
		ItemEditArea.setBounds(5,350,615,200);
		MyScrollPane.getViewport().add(ItemEditArea);
		this.add(MyScrollPane);
		
		Button=new JButton("Save");
		Button.addActionListener(this);
		Button.setActionCommand("Save_Item");
		Button.setBounds(5,555,307,25);
		this.add(Button);

		Button=new JButton("Save as New");
		Button.addActionListener(this);
		Button.setActionCommand("Save_Item_New");
		Button.setBounds(308,555,307,25);
		this.add(Button);
		
		this.pack();
		this.setBounds(0,0,920,605);
		this.repaint();
		
		this.setTitle("Items and Drop Table Editor");

	}
		
	public void actionPerformed(ActionEvent AE){
		String name="";
		if(AE.getSource() instanceof java.awt.List){
			name=((java.awt.List)AE.getSource()).getName();
		}
		if(AE.getActionCommand().equals("New_Drop_Table")){
			String maxDropIdQuery = "SELECT max(drop_id) from drop_table";
			
			//SQL STUFF:
			try{
				sql C=new sql(Connection,DB,Username,Password);
				ArrayList result=null;
				result=C.process(maxDropIdQuery);
				
				if(result!=null) {
					String highestDropId = "";
										
					if (result.size() == 0) {
						highestDropId = "0";
					} else {
						highestDropId=(String)result.get(0);
					}
					
					int x=0;
					try{
						x = (int)(new Integer(highestDropId));
					}catch(Exception e){}
					
					x = x + 1;
					
					// insert the new drop table
					String insertNewDropTableQuery = "INSERT into drop_table(drop_id, item_id, weight) values(" + x + ", 1, 100)";
					result = C.process(insertNewDropTableQuery);
					
					// add it to the list in the GUI
					DropTableList.add("" + x);
					DropTableListKeys.add("" + x);
					
				}
				
			}catch(Exception e){
				e.printStackTrace();
			}
			
			
			
		}else
		
		if(AE.getActionCommand().equals("Add_Item_To_Drop_Table")){
				// get the item that was clicked on	
				String item_id=(String)AllItemsKeys.get(AllItems.getSelectedIndex());
				
				//pop up a window to enter the weight of the item
				String weight = (String)JOptionPane.showInputDialog(this,"Enter weight:",null);
				
				//SQL STUFF:
				try{
					sql C=new sql(Connection,DB,Username,Password);
					ArrayList result=null;			
					String Q="INSERT into drop_table (drop_id, item_id, weight) VALUES(" + dropTableId + ", " + item_id + ", " + weight + ")";
					result=C.process(Q);
					
					CurrentDropList.add("Item: "+item_id+", Weight: "+ weight);
					CurrentDropListKeys.add(new String[]{dropTableId,item_id});

				}catch(Exception e){
					e.printStackTrace();
				}
				
				CurrentDropList.repaint();

				
				
		}else
		
		if(AE.getActionCommand().equals("Remove_Drop_Item")){
			int currentIndex=CurrentDropList.getSelectedIndex();
			String dropID=((String[])CurrentDropListKeys.get(currentIndex))[1];
			try{
				sql C=new sql(Connection,DB,Username,Password);
				ArrayList result=null;			
				String Q="DELETE FROM drop_table WHERE drop_id="+dropTableId+" AND item_id="+dropID;
				result=C.process(Q);
			}catch(Exception e){
				e.printStackTrace();
			}
			CurrentDropList.remove(currentIndex);
		}else
		
		if(AE.getActionCommand().equals("Save_Item")){

			try{
				sql C=new sql(Connection,DB,Username,Password);
				ArrayList result=null;
							
				String data=ItemEditArea.getText();
				data = data.replaceAll("\\\\","\\\\\\\\");
				data = data.replaceAll("\"","\\\\\"");
				String description=ItemDescription.getText();
				String Q="UPDATE items SET description=\""+description+"\",data=\""+data+"\" WHERE id="+itemId;
				C.process(Q);
				buildItemTable();

			}catch(Exception e){
			}
		}else
		
		if(AE.getActionCommand().equals("Save_Item_New")){

			try{
				sql C=new sql(Connection,DB,Username,Password);
				ArrayList result=null;
							
				String data=ItemEditArea.getText();
				data = data.replaceAll("\\\\","\\\\\\\\");
				data = data.replaceAll("\"","\\\\\"");
				String description=ItemDescription.getText();
				String Q="INSERT INTO items(description,data) VALUES(\""+description+"\",\""+data+"\")";
				C.process(Q);
				buildItemTable();

			}catch(Exception e){
			}
		}else
		
		if(name.equals("Load_Item")){
				
			int currentIndex=AllItems.getSelectedIndex();
			itemId=(String)AllItemsKeys.get(currentIndex);

			try{
				sql C=new sql(Connection,DB,Username,Password);
				ArrayList result=null;			
				String Q="SELECT id,data,description FROM items where id="+itemId;
				result=C.process(Q);
				if(result!=null){
					ItemEditArea.setText((String)result.get(1));
					ItemDescription.setText((String)result.get(2));
				}
			}catch(Exception e){
			}
		}else
		
		if(name.equals("Load_Current_Drop_Table")){
			CurrentDropList.clear();
			CurrentDropListKeys.clear();
		
			String id=(String)DropTableListKeys.get(DropTableList.getSelectedIndex());
			dropTableId = id;
			
			//SQL STUFF:
			try{
				sql C=new sql(Connection,DB,Username,Password);
				ArrayList result=null;			
				String Q="SELECT item_id,weight FROM drop_table where drop_id="+id;
				result=C.process(Q);
				
				if(result!=null)
				for(int i=0;i<result.size();i+=2){
					String itemID=(String)result.get(i);
					String weight=(String)result.get(i+1);
					CurrentDropList.add("Item: "+itemID+", Weight: "+weight);
					CurrentDropListKeys.add(new String[]{id,itemID});
				}
		

			}catch(Exception e){
				e.printStackTrace();
			}
			
			CurrentDropList.repaint();
		}
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
		DropTable DT=new DropTable(args[0],args[1],args[2]);
		DT.buildDropTableList();
		DT.buildItemTable();
	}

}//END.
