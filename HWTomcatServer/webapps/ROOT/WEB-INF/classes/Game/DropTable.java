/*
 * DropTable.java
 *
 * Created on March 10, 2007, 10:40 AM
 *
 * The main linker for Hack Wars.
 *
 */

package Game;
import java.util.ArrayList;
import Hackscript.Model.*;
import java.util.HashMap;
import util.*;
import java.util.*;
import org.w3c.dom.Node;
import org.w3c.dom.NamedNodeMap;

/**
 * By Alexander Morrison
 */
public class DropTable{
	
	private int totalWeight=0;
	private ArrayList Drops=new ArrayList();

	private int clueLevel=0;
	private Computer MyComputer=null;
	
	private String Connection="localhost";
	private String DB="hackwars";
	private String Username="root";
	private String Password="";
	
	public DropTable(int dropTable,Computer MyComputer){
		//Add the low items to the drop table.
		this.MyComputer=MyComputer;
		try{
			String Q="SELECT item_id,weight FROM drop_table where drop_id="+dropTable;
			sql C=new sql(Connection,DB,Username,Password);
			ArrayList result=C.process(Q);
			if(result!=null&&result.size()>0){
				for(int i=0;i<result.size();i+=2){
					Q="SELECT data FROM items WHERE id="+result.get(i);
					ArrayList result2=C.process(Q);
					
					int weight=new Integer((String)result.get(i+1));
					String data="";
					if(result2!=null&&result2.size()>0){
						data=(String)result2.get(0);
					}
					
					totalWeight+=weight;
					Drops.add(new Object[]{new Integer(totalWeight),data});
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	HackerFile parseData(String data,boolean parseHardware){
		HackerFile HF=null;
		try{
	
			LoadXML LX = new LoadXML();
			LX.loadByteArray(data.getBytes());
		
			Node N=LX.findNodeRecursive("file",0);
		
			//Get the type.
			Node temp=LX.findNodeRecursive(N,"type",0);
			temp=LX.findNodeRecursive(temp,"#text",0);
			int type=new Integer(temp.getNodeValue());
			
			HF=new HackerFile(type);
			
			//Get the name of this file.
			temp=LX.findNodeRecursive(N,"name",0);
			temp= LX.findNodeRecursive(temp,"#text",0);
			String name="CURRUPT(DELETE)";
			if(temp!=null)
				name=temp.getNodeValue();
			HF.setName(name);
																							
			//Get the location of this file in the directory structure.
			temp=LX.findNodeRecursive(N,"location",0);
			temp= LX.findNodeRecursive(temp,"#text",0);
			if(temp!=null){
				String location=temp.getNodeValue();
				HF.setLocation(location);
			}
			
			//Get the description associated with this file.
			temp=LX.findNodeRecursive(N,"description",0);
			temp= LX.findNodeRecursive(temp,"#text",0);
			if(temp!=null){
				String description=temp.getNodeValue();
				HF.setDescription(description);
			}
			
			//Get the price of this file.
			temp=LX.findNodeRecursive(N,"price",0);
			temp= LX.findNodeRecursive(temp,"#text",0);
			float price=new Float(temp.getNodeValue());
			HF.setPrice(price);
			
			//Get the price of this file.
			temp=LX.findNodeRecursive(N,"quantity",0);
			temp= LX.findNodeRecursive(temp,"#text",0);
			int quantity=new Integer(temp.getNodeValue());
			HF.setQuantity(quantity);
			
			//Get the CPU gost of this file.
			temp=LX.findNodeRecursive(N,"cpu",0);
			temp= LX.findNodeRecursive(temp,"#text",0);
			float cpu=new Float(temp.getNodeValue());
			HF.setCPUCost(cpu);
			
			//Get the maker of this file.
			temp=LX.findNodeRecursive(N,"maker",0);
			temp= LX.findNodeRecursive(temp,"#text",0);
			if(temp!=null){
				String maker=temp.getNodeValue();
				HF.setMaker(maker);
			}
			
			HashMap Script=new HashMap();
			Node N2=LX.findNodeRecursive(N,"content",0);
			if(N2!=null){
			
				String Keys[]=HF.getTypeKeys();
				for(int ii=0;ii<Keys.length;ii++){														
					temp=LX.findNodeRecursive(N2,Keys[ii],0);
					if(temp!=null){
						temp= LX.findNodeRecursive(temp,"#text",0);
						if(temp!=null){
							String script=temp.getNodeValue();
							Script.put(Keys[ii],script);
						}else{
							Script.put(Keys[ii],"");
						}
					}else{
						Script.put(Keys[ii],"");
					}
				}
				HF.setContent(Script);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		HF=parseDrop(HF,parseHardware);
		
		return(HF);
	}
	
	public HackerFile generateDrop(){	
		int drop=(int)(Math.random()*totalWeight);
		for(int i = 0; i < Drops.size(); i++){
        	Object O[]=(Object[])Drops.get(i);
			int currentRange=(Integer)O[0];
            // if the random number between 0 and totalWeight is less than the range up to that point in the drop table, then parse the data for that drop.
			if(drop < currentRange){
				HackerFile HF=parseData("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?>"+(String)O[1],true);
				return(HF);
			}
		}

		return(null);
	}
	
	/**
	This function searches through the drop table and returns a 
	quest item based on the name of the quest item.
	*/
	public HackerFile getQuestItem(String name){
		for(int i=0;i<Drops.size();i++){
			Object O[]=(Object[])Drops.get(i);
			
			HackerFile HF=parseData("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?>"+(String)O[1],false);
			HashMap HM=HF.getContent();
			String itemname=(String)HF.getName();
									
																					
			if(itemname!=null)
				if(itemname.equals(name)){
										
					if(HF.getName().equals("Equipment") || HF.getName().equals("LowEquipment") || HF.getName().equals("MediumEquipment")  || HF.getName().equals("HighEquipment")  || HF.getName().equals("RareEquipment") ){
						
						String quality = HF.getMaker();
						
						if(quality.equals("Low"))
							return(MyComputer.getEquipmentSheet().generateHardware(EquipmentSheet.LOW));
						else if(quality.equals("Medium"))
							return(MyComputer.getEquipmentSheet().generateHardware(EquipmentSheet.MEDIUM));
						else if(quality.equals("High"))
							return(MyComputer.getEquipmentSheet().generateHardware(EquipmentSheet.HIGH));
						else if(quality.equals("Rare"))
							return(MyComputer.getEquipmentSheet().generateHardware(EquipmentSheet.RARE));
					}
				
					return(HF);
				}
		}
		return(null);
	}
		
	/**
	Return the data parsed from a packet as a Hacker File. 
	*/
	public HackerFile parseDrop(HackerFile HF,boolean parseHardware){
		
		if(parseHardware){ //Should we parse the hardware data?
        
            // the drop is a card
			if(HF.getName().equals("Equipment") || HF.getName().equals("LowEquipment") || HF.getName().equals("MediumEquipment")  || HF.getName().equals("HighEquipment")  || HF.getName().equals("RareEquipment") ){
				String quality = HF.getMaker();
				if(quality.equals("Low"))
					return(MyComputer.getEquipmentSheet().generateHardware(EquipmentSheet.LOW));
				else if(quality.equals("Medium"))
					return(MyComputer.getEquipmentSheet().generateHardware(EquipmentSheet.MEDIUM));
				else if(quality.equals("High"))
					return(MyComputer.getEquipmentSheet().generateHardware(EquipmentSheet.HIGH));
				else if(quality.equals("Rare"))
					return(MyComputer.getEquipmentSheet().generateHardware(EquipmentSheet.RARE));
			}
            
            // the drop is a firewall
			if(HF.getType()==HackerFile.NEW_FIREWALL){
				return (MyComputer.getNewFireWall().generateFirewall(HF.getName()));
			}
            
		}
        
        
		return(HF);
	}
	
}
