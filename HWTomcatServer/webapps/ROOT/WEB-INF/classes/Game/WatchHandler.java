package Game;
/**
Handles all the watches currently installed.
*/

import java.util.*;

public class WatchHandler{
	//Types of watches.
	public static final int HEALTH=0;
	public static final int PETTY_CASH=1;
	public static final int SCAN=2;
	
	private ArrayList Watches=new ArrayList();//Array of watches currently installed.
	private Computer MyComputer=null;//The parent computer with these watches installed.
	private ComputerHandler MyComputerHandler=null;//The watch handler for message dispatching.
	
	/**
	Check for a watch at the given port number.
	*/
	public boolean checkForWatch(int port){
		for(int i=0;i<Watches.size();i++){
			if(((Watch)Watches.get(i)).getPort()==port)
				return(true);
		}
		return(false);
	}
	
	/**
	Automatically trigger a watch.
	*/
	public void triggerWatch(int index,String sourceIP,HashMap TriggerParam){
		if(index<Watches.size()){
			Watch TempWatch=(Watch)Watches.get(index);
			if(TempWatch!=null&&TempWatch.getOn()){
				TempWatch.setTargetIP(sourceIP);
				TempWatch.setTargetPort(0);
				TempWatch.setExternal(true);
				Port TempPort=(Port)MyComputer.getPorts().get(new Integer(TempWatch.getPort()));
				TempWatch.setPort(TempPort);
				Object O[]=new Object[]{new Float(0.0f),sourceIP,0,new Boolean(false),sourceIP,0,-1};
				ApplicationData AD=new ApplicationData("damage",O,0,sourceIP);
				TempWatch.setTriggerParam(TriggerParam);
				TempWatch.setTriggered(true);
				TempWatch.execute(AD);
			}
		}
	}
	
	
	/**
	Automatically trigger a watch.
	*/
	public void triggerWatch(String note,String sourceIP,HashMap TriggerParam){
		Watch TempWatch=null;
		for(int i=0;i<Watches.size();i++){
			Watch Temp=(Watch)Watches.get(i);
			if(Temp!=null&&Temp.getNote().equals(note)){
				TempWatch=Temp;
				break;
			}
		}
		
		if(TempWatch!=null&&TempWatch.getOn()){
			TempWatch.setTargetIP(sourceIP);
			TempWatch.setTargetPort(0);
			Port TempPort=(Port)MyComputer.getPorts().get(new Integer(TempWatch.getPort()));
			TempWatch.setPort(TempPort);
			Object O[]=new Object[]{new Float(0.0f),sourceIP,0,new Boolean(false),sourceIP,0,-1};
			ApplicationData AD=new ApplicationData("damage",O,0,sourceIP);
			TempWatch.setTriggerParam(TriggerParam);
			TempWatch.setTriggered(true);
			
			//System.out.println("About to execute watch based on the note found.");
			
			TempWatch.execute(AD);
		}
	}
	

	/**
	Constructor.
	*/
	public WatchHandler(ComputerHandler MyComputerHandler,Computer MyComputer){
		this.MyComputer=MyComputer;
		this.MyComputerHandler=MyComputerHandler;
	}
	
	/**
	Add a new watch to the list of watches.
	*/
	public void addWatch(Watch W){
		Watches.add(W);
	}
	
	/**
	Return the array list of watches.
	*/
	public ArrayList getWatches(){
		return(Watches);
	}
	
	/**
	Remove a watch from the Watches list.
	*/
	public void removeWatch(int index){
		Watches.remove(index);
	}
	
	/**
	Get a watch.
	*/
	public Watch getWatch(int index){
		return((Watch)Watches.get(index));
	}
	
	/**
	Destroy all the watches installed on the given port number.
	*/
	public void destroyWatches(int port){
		Iterator MyIterator=Watches.iterator();
		while(MyIterator.hasNext()){
			Watch twatch=(Watch)MyIterator.next();
			if(twatch.getPort()==port&&twatch.getType()!=SCAN&&twatch.getOn())
				MyIterator.remove();
		}
	}

	/**
	Update the initial health quanity for a given port.
	*/
	public void updateInitialHealthQuanity(int port,float quantity){
		Iterator MyIterator=Watches.iterator();
		while(MyIterator.hasNext()){
			Watch twatch=(Watch)MyIterator.next();
			if(twatch.getPort()==port&&twatch.getType()==HEALTH)
				twatch.setInitialQuantity(quantity);
		}
	}
	
	/**
	Get the number of watches that are currently on.
	*/
	public int getWatchCount(){
		int count=0;
		for(int i=0;i<Watches.size();i++){
			if(((Watch)Watches.get(i)).getOn())
				count++;
		}
		return(count);
	}
	
	/**
	Check the watches, and return the current CPU load.
	*/
	public float checkWatches(ApplicationData MyApplicationData,HashMap Ports,float pettyCash){
		boolean gainedXP=false;
		float watchCost=0.0f;
		Iterator MyIterator=Watches.iterator();
		Watch TempWatch=null;
		while(MyIterator.hasNext()){
			TempWatch=(Watch)MyIterator.next();
			if(TempWatch.getOn()){

				Port TempPort=(Port)Ports.get(new Integer(TempWatch.getPort()));
				boolean overheated=false;
				if(TempPort!=null)
					overheated=TempPort.getOverHeated();
					
				if(!overheated||MyApplicationData.getFunction().equals("scansuccess")){//Make sure our port isn't overheated.
							
					if(MyApplicationData.getFunction().equals("damage")){
						Object[] parameters = (Object[])MyApplicationData.getParameters();
						boolean zombieDamage=false;//Is the damge being dealt by a port that has been maliciously taken over?
						String zombieSource=(String)parameters[4];
						if(zombieSource!=null){
							zombieDamage=true;
						}
						
						String targetIP=(String)parameters[1];
						if(zombieDamage){
							targetIP=zombieSource;
						}
						int targetPort=(Integer)parameters[2];
						
						if(TempWatch.getType()==HEALTH){//Fired when health reaches a certain quanity.
												
							if(TempPort!=null&&MyApplicationData.getPort()==TempPort.getNumber()){
							
								float value=TempPort.getHealth();
								if((TempWatch.getInitialQuantity()>=TempWatch.getQuantity())&&(value<TempWatch.getQuantity())){
									//Set the source of the watch to internal or external.
									if(MyApplicationData.getSource()==ApplicationData.INSIDE)
										TempWatch.setExternal(false);
									else
										TempWatch.setExternal(true);
										
									TempWatch.setTargetIP(targetIP);
									TempWatch.setTargetPort(targetPort);
									TempWatch.setPort(TempPort);
									TempWatch.execute(MyApplicationData);
									if(!gainedXP){
										MyComputerHandler.addData(new ApplicationData("watchxp",new Float((float)MyComputer.getWatchLevel()),0,MyComputer.getIP()),MyComputer.getIP());
										gainedXP=true;
									}
								}
								TempWatch.setInitialQuantity(value);
							}
						}
					}
					
					//Make sure the initial quantity value remains valid.
					if(MyApplicationData.getFunction().equals("bank")){
						if(TempWatch.getType()==PETTY_CASH){
							TempWatch.setInitialQuantity(pettyCash);
						}
					}
					
					//Fired when petty cash reaches a certain amount.
					if(MyApplicationData.getFunction().equals("pettycash")){
						
						float value=pettyCash;
						
						if(TempWatch.getType()==PETTY_CASH){
							if(((TempWatch.getInitialQuantity()<TempWatch.getQuantity())&&(value>=TempWatch.getQuantity()))){
								if(TempPort!=null&&TempPort.getOn()&&!TempPort.getDummy()){
									if(TempPort.getType()==Port.BANKING){//Make sure this is installed on banking.
									
										//Set the source of the watch to internal or external.
										if(MyApplicationData.getSource()==ApplicationData.INSIDE)
											TempWatch.setExternal(false);
										else
											TempWatch.setExternal(true);
									
										float amount=value-TempWatch.getInitialQuantity();
										//float deposit=(Float)MyApplicationData.getParameters();
										float deposit=0.0f;
										if(MyApplicationData.getParameters() instanceof Float)
											deposit=(Float)MyApplicationData.getParameters();
										else {
											Object[] oD = (Object[]) MyApplicationData.getParameters();
											deposit = (Float) oD[0];
										}

										TempWatch.setDepositAmount(deposit);
										TempWatch.setTargetIP(MyApplicationData.getSourceIP());
										TempWatch.setTargetPort(TempPort.getNumber());
										TempWatch.setPort(TempPort);
										TempWatch.execute(MyApplicationData);
										if(!gainedXP){
											MyComputerHandler.addData(new ApplicationData("watchxp",new Float((float)amount/50.0f),0,MyComputer.getIP()),MyComputer.getIP());
											gainedXP=true;
										}
									}
								}
							}
							TempWatch.setInitialQuantity(value);
						}
					}
					
					//Fired when petty cash reaches a certain amount.
					if(MyApplicationData.getFunction().equals("scansuccess")){
						float value=pettyCash;
						if(TempWatch.getType()==SCAN){
						
							//Set the source of the watch to internal or external.
							if(MyApplicationData.getSource()==ApplicationData.INSIDE)
								TempWatch.setExternal(false);
							else
								TempWatch.setExternal(true);
							
							TempWatch.setTargetIP(MyApplicationData.getSourceIP());
							TempWatch.setTargetPort(0);
							TempWatch.execute(MyApplicationData);
							if(!gainedXP){
								MyComputerHandler.addData(new ApplicationData("watchxp",new Float((float)MyComputer.getWatchLevel()/4.0f),0,MyComputer.getIP()),MyComputer.getIP());
								gainedXP=true;
							}
						}
					}
				}
				watchCost+=TempWatch.getCPUCost();
			}
		}
		return(watchCost);
	}
	
	/**
	Output the contents of this class as an XML string.
	*/
	public String outputXML(){
		String returnMe="<watches>\n";
		for(int i=0;i<Watches.size();i++){
			returnMe+=((Watch)Watches.get(i)).outputXML();
		}
		returnMe+="</watches>\n";
		return(returnMe);
	}
}
