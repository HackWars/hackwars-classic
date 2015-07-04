package Game;
/**
This singleton delivers files to a player when they purchase them via the Game's store. 
*/
import util.*;
import java.util.*;

public class GiveItemsSingleton{
	//MYSQL INFO.
	public static int SAVE_COUNTER = 0;
	private String Connection="localhost";
	private String DB="hackwars";
	private String Username="root";
	private String Password="";
	
	//The singleton class.
	private static GiveItemsSingleton instance=null;
	
	public static GiveItemsSingleton getInstance(){
		if(instance==null){
			instance=new GiveItemsSingleton();
		}
		
		return(instance);
	}
	
	//Make the constructor private.
	private GiveItemsSingleton(){
		
	}
	
	/**
	 The following functions provide the player with the items they have purchased,
	 this logic is specific enough it requires multiple functions.
	 
	 DataShield
	 DigitalFortress
	 */
	public boolean givePackage1(Computer PlayerComputer,ComputerHandler MyComputerHandler){
		try{
			ArrayList Items=new ArrayList();
			int basic=2+(int)(Math.random()*2.0);
			int medium=1+(int)(Math.random()*2.0);
			
			NewFireWall F=new NewFireWall();
			for(int i=0;i<basic;i++){
				Items.add(F.generateFirewall("DataShield"));
			}
			
			for(int i=0;i<medium;i++){
				Items.add(F.generateFirewall("DigitalFortress"));
			}
			
			if(PlayerComputer.getFileSystem().getSpaceLeft()<Items.size()){
				PlayerComputer.addMessage("You did not have enough HD space to receive the files you purchased from the HackWars online store. They will be given to you when you delete some files.");
				return(false);
			}else{
				for(int i=0;i<Items.size();i++){
					Object Parameter[]=new Object[]{"",(HackerFile)Items.get(i)};
					MyComputerHandler.addData(new ApplicationData("savefile",Parameter,0,PlayerComputer.getIP()),PlayerComputer.getIP());
				}
				
				PlayerComputer.addMessage("You have received files that you purchased using HackWars' online store. ("+basic+" x Attacking Data Shield, "+medium+" x Attacking Digital Fortress)");
				return(true);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return(false);
	}
	
	/**
	DigitalFortress
	RubyGuardian
	*/
	public boolean givePackage2(Computer PlayerComputer,ComputerHandler MyComputerHandler){
		try{
			ArrayList Items=new ArrayList();
			int basic=2+(int)(Math.random()*2.0);
			int medium=1+(int)(Math.random()*2.0);
			
			NewFireWall F=new NewFireWall();
			for(int i=0;i<basic;i++){
				Items.add(F.generateFirewall("DigitalFortress"));
			}
			
			for(int i=0;i<medium;i++){
				Items.add(F.generateFirewall("RubyGuardian"));
			}
			
			if(PlayerComputer.getFileSystem().getSpaceLeft()<Items.size()){
				PlayerComputer.addMessage("You did not have enough HD space to receive the files you purchased from the HackWars online store. They will be given to you when you delete some files.");
				return(false);
			}else{
				for(int i=0;i<Items.size();i++){
					Object Parameter[]=new Object[]{"",(HackerFile)Items.get(i)};
					MyComputerHandler.addData(new ApplicationData("savefile",Parameter,0,PlayerComputer.getIP()),PlayerComputer.getIP());
				}
				
				PlayerComputer.addMessage("You have received files that you purchased using HackWars' online store. ("+basic+" x Attacking Digital Fortress, "+medium+" x Attacking Ruby Guardian)");
				return(true);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return(false);
	}
	
	/**
	 RubyGuardian
	 DiamondDefender
	 */
	public boolean givePackage3(Computer PlayerComputer,ComputerHandler MyComputerHandler){
		try{
			ArrayList Items=new ArrayList();
			int basic=2+(int)(Math.random()*2.0);
			int medium=1+(int)(Math.random()*2.0);
			
			NewFireWall F=new NewFireWall();
			for(int i=0;i<basic;i++){
				Items.add(F.generateFirewall("RubyGuardian"));
			}
			
			for(int i=0;i<medium;i++){
				Items.add(F.generateFirewall("DiamondDefender"));
			}
			
			if(PlayerComputer.getFileSystem().getSpaceLeft()<Items.size()){
				PlayerComputer.addMessage("You did not have enough HD space to receive the files you purchased from the HackWars online store. They will be given to you when you delete some files.");
				return(false);
			}else{
				for(int i=0;i<Items.size();i++){
					Object Parameter[]=new Object[]{"",(HackerFile)Items.get(i)};
					MyComputerHandler.addData(new ApplicationData("savefile",Parameter,0,PlayerComputer.getIP()),PlayerComputer.getIP());
				}
				
				PlayerComputer.addMessage("You have received files that you purchased using HackWars' online store. ("+basic+" x Attacking Ruby Guardian, "+medium+" x Attacking Diamond Defender)");
				return(true);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return(false);
	}
	
	/**
	 LOW
	 MEDIUM
	 */
	public boolean givePackage4(Computer PlayerComputer,ComputerHandler MyComputerHandler){
		try{
			ArrayList Items=new ArrayList();
			int basic=2+(int)(Math.random()*2.0);
			int medium=1+(int)(Math.random()*2.0);
			
			NewFireWall F=new NewFireWall();
			for(int i=0;i<basic;i++){
				Items.add(PlayerComputer.getEquipmentSheet().generateHardware(EquipmentSheet.LOW));
			}
			
			for(int i=0;i<medium;i++){
				Items.add(PlayerComputer.getEquipmentSheet().generateHardware(EquipmentSheet.MEDIUM));
			}
			
			if(PlayerComputer.getFileSystem().getSpaceLeft()<Items.size()){
				PlayerComputer.addMessage("You did not have enough HD space to receive the files you purchased from the HackWars online store. They will be given to you when you delete some files.");
				return(false);
			}else{
				for(int i=0;i<Items.size();i++){
					Object Parameter[]=new Object[]{"",(HackerFile)Items.get(i)};
					MyComputerHandler.addData(new ApplicationData("savefile",Parameter,0,PlayerComputer.getIP()),PlayerComputer.getIP());
				}
				
				PlayerComputer.addMessage("You have received files that you purchased using HackWars' online store. ("+basic+" x Low Hardware Rolls, "+medium+" x Medium Hardware Rolls)");
				return(true);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return(false);
	}
	
	/**
	 MEDIUM
	 HIGH
	 */
	public boolean givePackage5(Computer PlayerComputer,ComputerHandler MyComputerHandler){
		try{
			ArrayList Items=new ArrayList();
			int basic=2+(int)(Math.random()*2.0);
			int medium=1+(int)(Math.random()*2.0);
			
			NewFireWall F=new NewFireWall();
			for(int i=0;i<basic;i++){
				Items.add(PlayerComputer.getEquipmentSheet().generateHardware(EquipmentSheet.MEDIUM));
			}
			
			for(int i=0;i<medium;i++){
				Items.add(PlayerComputer.getEquipmentSheet().generateHardware(EquipmentSheet.HIGH));
			}
			
			if(PlayerComputer.getFileSystem().getSpaceLeft()<Items.size()){
				PlayerComputer.addMessage("You did not have enough HD space to receive the files you purchased from the HackWars online store. They will be given to you when you delete some files.");
				return(false);
			}else{
				for(int i=0;i<Items.size();i++){
					Object Parameter[]=new Object[]{"",(HackerFile)Items.get(i)};
					MyComputerHandler.addData(new ApplicationData("savefile",Parameter,0,PlayerComputer.getIP()),PlayerComputer.getIP());
				}
				
				PlayerComputer.addMessage("You have received files that you purchased using HackWars' online store. ("+basic+" x Medium Hardware Rolls, "+medium+" x High Hardware Rolls)");
				return(true);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return(false);
	}
	
	
	/**
	 HIGH
	 RARE
	 */
	public boolean givePackage6(Computer PlayerComputer,ComputerHandler MyComputerHandler){
		try{
			ArrayList Items=new ArrayList();
			int basic=2+(int)(Math.random()*2.0);
			int medium=1+(int)(Math.random()*2.0);
			
			NewFireWall F=new NewFireWall();
			for(int i=0;i<basic;i++){
				Items.add(PlayerComputer.getEquipmentSheet().generateHardware(EquipmentSheet.HIGH));
			}
			
			for(int i=0;i<medium;i++){
				Items.add(PlayerComputer.getEquipmentSheet().generateHardware(EquipmentSheet.RARE));
			}
			
			if(PlayerComputer.getFileSystem().getSpaceLeft()<Items.size()){
				PlayerComputer.addMessage("You did not have enough HD space to receive the files you purchased from the HackWars online store. They will be given to you when you delete some files.");
				return(false);
			}else{
				for(int i=0;i<Items.size();i++){
					Object Parameter[]=new Object[]{"",(HackerFile)Items.get(i)};
					MyComputerHandler.addData(new ApplicationData("savefile",Parameter,0,PlayerComputer.getIP()),PlayerComputer.getIP());
				}
				
				PlayerComputer.addMessage("You have received files that you purchased using HackWars' online store. ("+basic+" x High Hardware Rolls, "+medium+" x Rare Hardware Rolls)");
				return(true);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return(false);
	}
	
	/**
	 This method gives a file to a player if there is enough room on their HD, 
	 and they have purchased a file.
	 
	 1                               FW1             2-3 Basic Firewalls 1-2 Medium Firewalls
	 2                               FW2             2-3 Medium Firewalls 1-2 Advanced Firewalls
	 3                               FW3             2-3 Advanced Firewalls 1-2 Ultimate Firewalls
	 4                               AGP1    2-3 1+ AGP cards
	 5                               AGP2    2-3 AGP 1+ 2-3 APG 2+
	 6                               AGP3    2-3 AGP 1+ 3-4 AGP 2+
	 */
	public synchronized void giveFiles(Computer PlayerComputer,ComputerHandler MyComputerHandler){
		try{
			sql C=new sql(Connection,DB,Username,Password);
			ArrayList result=null;
			String q="SELECT store_item_id,bought_item_id FROM bought_items WHERE ip=\""+PlayerComputer.getIP()+"\" AND given=0";
			result=C.process(q);
			
			ArrayList givenList=new ArrayList();//Purchases that have been successfully given.
			
			if(result!=null)
				for(int i=0;i<result.size();i+=2){
					String store_item_id=(String)result.get(i);
					String bought_item_id=(String)result.get(i+1);
					
					if(store_item_id.equals("1")){
						if(givePackage1(PlayerComputer,MyComputerHandler)){
							givenList.add(bought_item_id);
						}
					}else
						
					if(store_item_id.equals("2")){
						if(givePackage2(PlayerComputer,MyComputerHandler)){
							givenList.add(bought_item_id);
						}
					}else		
						
					if(store_item_id.equals("3")){
						if(givePackage3(PlayerComputer,MyComputerHandler)){
							givenList.add(bought_item_id);
						}
					}else
						
					if(store_item_id.equals("4")){
						if(givePackage4(PlayerComputer,MyComputerHandler)){
							givenList.add(bought_item_id);
						}
					}else

					if(store_item_id.equals("5")){
						if(givePackage5(PlayerComputer,MyComputerHandler)){
							givenList.add(bought_item_id);
						}
					}else		

					if(store_item_id.equals("6")){
						if(givePackage6(PlayerComputer,MyComputerHandler)){
							givenList.add(bought_item_id);
						}
					}
				}
		
			//Now update the items table to set that they have been given.
			for(int i=0;i<givenList.size();i++){
				q="UPDATE bought_items SET given=1 WHERE bought_item_id="+(String)givenList.get(i);
				C.process(q);
			}
			
			C.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
