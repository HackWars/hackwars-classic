package Game;
/**
Represents a watch which will fire given an appropriate event in the computer.
*/

import java.util.*;
import Assignments.*;
import Hackscript.Model.*;

public class Watch{
	//Watch constants.
	public static final int HEALTH=0;
	public static final int PETTY_CASH=1;
	public static final int SCAN=2;

	private Program MyProgram=null;//Program attached to this watch.
	private Computer MyComputer=null;//The computer structure this port is attached to.
	private ArrayList ObservedPorts=new ArrayList();//The ports that are observed for purposes of switching a firewall.
	
	private float cpuCost=0.0f;//The CPU Cost of this watch.
	private int type=0;//Type of watch.
	private int searchFireWall=0;//Type of fire wall to search for.
	private int port=0;//What port is this watch on if applicable.
	private float initialQuantity=0;//The quantity that this watch starts at.
	private float quantity=0;//What quanity of 'X' should this fire on if applicable.
	private boolean on=false;//Is this watch on?
	private String note="";//Helpful note associated with port.
	private Port MyPort=null;//Port associated with this watch.
	private float depositAmount=0.0f;
	private HashMap TriggerParams=null;//Parameters can be passed in by trigger watch.
	private boolean external=false;//Is this watch being run from an external process or an internal one?
	
	/**
	Fetch whether this watch is being triggered by an external process or internal one.
	*/
	public boolean getExternal(){
		return(external);
	}
	
	public void setExternal(boolean external){
		this.external=external;
	}
	
	//Attack specific.
	private String targetIP="";//IP that triggered watch.
	private int targetPort=-1;//Port that triggered watch.
	
	//The IP of the port that caused this to fire.

	public void setTriggered(boolean triggered){
		((WatchProgram)MyProgram).setTriggered(triggered);
	}

	/**
	Constructor.
	*/
	public Watch(Computer MyComputer){
		this.MyComputer=MyComputer;
	}
	
	public void setDepositAmount(float depositAmount){
		this.depositAmount=depositAmount;
	}
	
	public float getDepositAmount(){
		return(depositAmount);
	}
	
	public void setTriggerParam(HashMap TriggerParams){
		this.TriggerParams=TriggerParams;
	}
	
	public Variable getTriggerParameter(String key){
		if(TriggerParams==null)
			return(new TypeString(""));
		Variable ReturnMe=(Variable)TriggerParams.get(key);
		if(ReturnMe==null)
			return(new TypeString(""));
		return(ReturnMe);
	}
	
	/**
	Check the array of observed fire walls for the fire wall with the given name.
	*/
	public int checkForFireWall(String FireWallName){
		FireWallName=FireWallName.toLowerCase();
		FireWallName=FireWallName.replaceAll(" ","");
		HashMap Ports=MyComputer.getPorts();
		for(int i=0;i<ObservedPorts.size();i++){
			Port tport=(Port)Ports.get((Integer)ObservedPorts.get(i));
			if(tport!=null){
				if(tport.getFireWall().getName().toLowerCase().equals(FireWallName)){
					
					return(tport.getNumber());
				}
			}
		}
		return(-1);
	}
	
	/**
	Check whether the given fire wall exisits on the port this program is installed on.
	*/
	public boolean checkFireWall(String FireWallName){
		if(MyPort==null)
			return(false);
	
		FireWallName=FireWallName.toLowerCase();
		FireWallName=FireWallName.replaceAll(" ","");
		if(MyPort.getFireWall().getName().equals(FireWallName)){
			return(true);
		}
		return(false);
	}
	
	/**
	Switch the fire wall from the port provided to this port.
	*/
	public void switchFireWall(int port){
		if(MyPort==null)
			return;
	
		NewFireWall CurrentFireWall=MyPort.getFireWall();
		HashMap Ports=MyComputer.getPorts();
		Port SwitchPort=(Port)Ports.get(new Integer(port));
		
		if(SwitchPort==null)
			return;
			
		if(!SwitchPort.getOn())
			return;
		
		NewFireWall SwitchFireWall=SwitchPort.getFireWall();
		
		if(switchPossible(SwitchPort)){
            HackerFile F1=MyPort.getFireWall().getHackerFile();
            HackerFile F2=SwitchFireWall.getHackerFile();
            MyPort.getFireWall().loadHackerFile(F2);
            SwitchPort.getFireWall().loadHackerFile(F1);
		}
		
		MyComputer.sendDamagePacket();
	}
	
	/**
	Return whether or not this action will cause things to overheat.
	*/
	public boolean switchPossible(Port TempPort){
		//Check to see if this will cause an overheat.
		float cpuLoad=MyComputer.getCPULoad();
		if(TempPort.getDummy())
			cpuLoad-=TempPort.getFireWall().getCPUCost()/2.0f;
		else
			cpuLoad-=TempPort.getFireWall().getCPUCost();
			
		if(MyPort.getDummy())
			cpuLoad-=MyPort.getFireWall().getCPUCost()/2.0f;
		else
			cpuLoad-=MyPort.getFireWall().getCPUCost();
			
		if(TempPort.getDummy())
			cpuLoad+=MyPort.getFireWall().getCPUCost()/2.0f;
		else
			cpuLoad+=MyPort.getFireWall().getCPUCost();
			
		if(MyPort.getDummy())
			cpuLoad+=TempPort.getFireWall().getCPUCost()/2.0f;
		else
			cpuLoad+=TempPort.getFireWall().getCPUCost();
			
		if(cpuLoad>MyComputer.getMaximumCPULoad()){
			return(false);
		}

		return(true);
	}
	
	/**
	Switch any fire wall from the observed ports to this port.
	*/
	public void switchAnyFireWall(){	
			
		if(MyPort==null)
			return;
		
		NewFireWall CurrentFireWall=MyPort.getFireWall();
		HashMap Ports=MyComputer.getPorts();
		Port SwitchPort=null;
		
		Iterator PortIterator=Ports.entrySet().iterator();
		int ii=0;

		for(int i=0;i<ObservedPorts.size();i++){
			Port TempPort=(Port)Ports.get((Integer)ObservedPorts.get(i));
			if(!(TempPort.getFireWall().getName().equals("None"))){
				if(switchPossible(TempPort)){
					SwitchPort=TempPort;
					break;
				}
			}
			ii++;
		}
		
		if(SwitchPort==null)
			return;
			
		if(!SwitchPort.getOn())
			return;
						
		NewFireWall SwitchFireWall=SwitchPort.getFireWall();
		
        HackerFile F1=MyPort.getFireWall().getHackerFile();
        HackerFile F2=SwitchFireWall.getHackerFile();
        MyPort.getFireWall().loadHackerFile(F2);
        SwitchPort.getFireWall().loadHackerFile(F1);

		MyComputer.sendDamagePacket();
	}
	
	/**
	Shut down all the ports being observed by this watch.
	*/
	public void shutDownPorts(){
		HashMap Ports=MyComputer.getPorts();
		Iterator PortIterator=Ports.entrySet().iterator();
		int ii=0;
		for(int i=0;i<ObservedPorts.size();i++){
			Port TempPort=(Port)Ports.get((Integer)ObservedPorts.get(i));
			if(TempPort!=null){
				String ip=(String)MyComputer.getIP();
				int port=(Integer)TempPort.getNumber();
				Boolean on=new Boolean(false);
				MyComputer.getComputerHandler().addData(new ApplicationData("portonoff",on,port,ip),ip);
			}
			ii++;
		}
	}
	
	/**
	Shut down the port number provided.
	*/
	public void shutDownPort(int port){
		HashMap Ports=MyComputer.getPorts();
		Port TempPort=(Port)Ports.get(new Integer(port));
		if(TempPort==null)
			return;
		if(TempPort!=null){
			String ip=(String)MyComputer.getIP();
			Boolean on=new Boolean(false);
			MyComputer.getComputerHandler().addData(new ApplicationData("portonoff",on,port,ip),ip);
		}
	}
	
	/**
	Shut down all the ports being observed by this watch.
	*/
	public void turnOnPorts(){
		HashMap Ports=MyComputer.getPorts();
		Iterator PortIterator=Ports.entrySet().iterator();
		int ii=0;
		for(int i=0;i<ObservedPorts.size();i++){
			Port TempPort=(Port)Ports.get((Integer)ObservedPorts.get(i));
			if(TempPort!=null){
				String ip=(String)MyComputer.getIP();
				int port=(Integer)TempPort.getNumber();
				Boolean on=new Boolean(true);
				MyComputer.getComputerHandler().addData(new ApplicationData("portonoff",on,port,ip),ip);
			}
			ii++;
		}
	}
	
	/**
	Shut down the port number provided.
	*/
	public void turnOnPort(int port){
		HashMap Ports=MyComputer.getPorts();
		Port TempPort=(Port)Ports.get(new Integer(port));
		if(TempPort==null)
			return;
		if(TempPort!=null){
			String ip=(String)MyComputer.getIP();
			Boolean on=new Boolean(true);
			MyComputer.getComputerHandler().addData(new ApplicationData("portonoff",on,port,ip),ip);
		}
	}
	
	/**
	Get the port number that attacked this port.
	*/
	public int getTargetPort(){
		return(targetPort);
	}
	
	/**
	Set the port that triggered this watch.
	*/
	public void setTargetPort(int targetPort){
		this.targetPort=targetPort;
	}
	
	/**
	Get the IP that triggered this port.
	*/
	public String getTargetIP(){
		return(targetIP);
	}
	
	/**
	Set the IP that triggered this watch.
	*/
	public void setTargetIP(String targetIP){
		this.targetIP=targetIP;
	}
	
	/**
	Get the IP of the computer associated with this watch.
	*/
	public String getIP(){
		return(MyComputer.getIP());
	}
	
	/**
	Get the port number associated with this watch.
	*/
	public int getNumber(){
		if(MyPort==null)
			return(0);
	
		return(MyPort.getNumber());
	}
	
	/**
	Set the port that this program is installed on.
	*/
	public void setPort(Port MyPort){
		this.MyPort=MyPort;
	}
	
	/**
	Set the CPU cost associated with this watch.
	*/
	public void setCPUCost(float cpuCost){
		this.cpuCost=cpuCost;
	}
	
	/**
	Return the CPU cost associated with this watch.
	*/
	public float getCPUCost(){
		if(!on)
			return(0.0f);
		return(cpuCost);
	}
	
	/**
	Return get actual CPU cost.
	*/
	public float getActualCPUCost(){
		return(cpuCost);
	}
	
	/**
	Set the type of watch that this is.
	*/
	public void setType(int type){
		this.type=type;
	}
	
	/**
	Get the type of watch.
	*/
	public int getType(){
		return(type);
	}
	
	/**
	Set the quanity that this watch will fire on.
	What this quantity actually is varies depending on the type of watch.*/
	public void setQuantity(float quantity){
		this.quantity=quantity;
	}
	
	/**
	Get the quantity.
	*/
	public float getQuantity(){
		return(quantity);
	}
	
	/**
	Set the quanity that this watch will fire on.
	What this quantity actually is varies depending on the type of watch.*/
	public void setInitialQuantity(float initialQuantity){
		this.initialQuantity=initialQuantity;
	}
	
	/**
	Get the intial quanity that this watch is currently set to.
	*/
	public float getInitialQuantity(){
		return(initialQuantity);
	}
	
	/**
	Set the program attached to this watch.
	*/
	public void setProgram(Program MyProgram){
		this.MyProgram=MyProgram;
	}
	
	/**
	Get the program attached to this watch.
	*/
	public Program getProgram(){
		return(MyProgram);
	}
	
	/**
	Exectue the program attached to this port.
	*/
	public void execute(ApplicationData MyApplicationData){
		MyProgram.execute(MyApplicationData);
	}
	
	/**
	Set whether this watch is on or off.
	*/
	public void setOn(boolean on){
		this.on=on;
	}
	
	/**
	Get whether this watch is on or off.
	*/
	public boolean getOn(){
		return(on);
	}
	
	/**
	Set the port this watch is installed on.
	*/
	public void setPort(int port){
		this.port=port;
	}
	
	/**
	Get the port this watch is installed on.
	*/
	public int getPort(){
		return(port);
	}
	
	/**
	Set the note associated with this watch.
	*/
	public void setNote(String note){
		this.note=note;
	}
	
	/**
	Get the note associated with this watch.
	*/
	public String getNote(){
		return(note);
	}
	
	/**
	Get the ArrayList of observed ports.
	*/
	public ArrayList getObservedPorts(){
		return(ObservedPorts);
	}
	
	/**
	Add an observerd port.
	*/
	public void addObservedPort(int observedPort){
		ObservedPorts.add(new Integer(observedPort));
	}
	
	/**
	Set the list of observed ports.
	*/
	public void setObservedPorts(Integer PortArray[]){
		ObservedPorts=new ArrayList();
		for(int i=0;i<PortArray.length;i++){
			ObservedPorts.add(PortArray[i]);
		}
	}
	
	/**
	Set the fire wall that this watch should search for on observed ports.
	*/
	public void setSearchFireWall(int searchFireWall){
		this.searchFireWall=searchFireWall;
	}
	
	/**
	Get the fire wall that this watch should search for.
	*/
	public int getSearchFireWall(){
		return(searchFireWall);
	}
		
	/**
	Get the packet version of the watch.
	*/
	public PacketWatch getPacketWatch(){
		PacketWatch returnMe=new PacketWatch();
		returnMe.setType(type);
		returnMe.setOn(on);
		returnMe.setPort(port);
		returnMe.setNote(note);
		returnMe.setCPUCost(cpuCost);
		returnMe.setSearchFireWall(searchFireWall);
		returnMe.setQuantity(quantity);
		returnMe.setObservedPorts(ObservedPorts);
		return(returnMe);
	}
	
	/**
	Output this watch in XML format for saving.
	*/
	public String outputXML(){
		String returnMe="<watch>\n";
		returnMe+="<cpu>"+cpuCost+"</cpu>\n";
		if(on)
			returnMe+="<on>1</on>\n";
		else
			returnMe+="<on>0</on>\n";
		returnMe+="<type>"+type+"</type>\n";
		
		if(note!=null)
			returnMe+="<note><![CDATA["+note.replaceAll("]]>","]]&gt;")+"]]></note>\n";
		else
			returnMe+="<note><![CDATA["+note+"]]></note>\n";

	
		for(int i=0;i<ObservedPorts.size();i++){
			returnMe+="<observedport>"+(Integer)ObservedPorts.get(i)+"</observedport>\n";
		}
		returnMe+="<installport>"+port+"</installport>\n";

		returnMe+="<searchfirewall>"+searchFireWall+"</searchfirewall>\n";
		returnMe+="<quantity>"+quantity+"</quantity>\n";
		returnMe+=MyProgram.outputXML();
		returnMe+="</watch>\n";
		return(returnMe);
	}
}
