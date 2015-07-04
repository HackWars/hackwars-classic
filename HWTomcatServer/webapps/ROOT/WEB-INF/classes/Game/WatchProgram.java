package Game;
/**
WatchProgram.java
A watch program is executed by the watch handler when a specific event has fired.
*/

import Hackscript.Model.*;
import java.io.*;
import java.util.HashMap;

public class WatchProgram extends Program{

	private String fireScript="";//Script that runs when watch fires.
	private Computer MyComputer=null;//Computer associated with this program.
	private Watch ParentWatch=null;//Watch housing this program.
	private boolean triggered=false;
	
	//Constructor.
	public WatchProgram(Computer MyComputer,NetworkSwitch MyComputerHandler,Watch ParentWatch){
		super.setComputerHandler(MyComputerHandler);
		super.setComputer(MyComputer);
		this.MyComputer=MyComputer;
		this.ParentWatch=ParentWatch;
	}
	
	public boolean getTriggered(){
		return(triggered);
	}
	
	public void setTriggered(boolean triggered){
		this.triggered=triggered;
	}
	
	//Get the parent watch associated with this program.
	public Watch getParentWatch(){
		return(ParentWatch);
	}
	
	/**
	Shut down all observed ports.
	*/
	public void shutDownPorts(){
		ParentWatch.shutDownPorts();
	}
	
	/**
	Shut down a specific port.
	*/
	public void shutDownPort(int port){
		ParentWatch.shutDownPort(port);
	}
	
	/**
	Shut down all observed ports.
	*/
	public void turnOnPorts(){
		ParentWatch.turnOnPorts();
	}
	
	/**
	Shut down a specific port.
	*/
	public void turnOnPort(int port){
		ParentWatch.turnOnPort(port);
	}
	
	/**
	Set the amount currently in the player's petty cash.
	*/
	public void setPettyCash(float pettyCash){
		MyComputer.setPettyCash(pettyCash);
	}
	
	/**
	Return the amount currently in the player's petty cash.
	*/
	public float getPettyCash(){
		return(MyComputer.getPettyCash());
	}
	
	/**
	Switch the fire wall from the port provided to this port.
	*/
	public void switchFireWall(int port){
		ParentWatch.switchFireWall(port);
	}
	
	/**
	Switch any fire wall from a port in the observed ports array.
	*/
	public void switchAnyFireWall(){
		ParentWatch.switchAnyFireWall();
	}
	
	/**
	Check the array of observed fire walls for the fire wall with the given name.
	*/
	public int checkForFireWall(String FireWallName){
		return(ParentWatch.checkForFireWall(FireWallName));
	}
	
	/**
	Check whether the given fire wall is present on the port this program is installed on.
	*/
	public boolean checkFireWall(String FireWallName){
		return(ParentWatch.checkFireWall(FireWallName));
	}
	
	/**
	Get the banking application associated with the computer.
	*/
	public int getDefaultBank(){
		return(MyComputer.getDefaultBank());
	}
	
	/**
	Get the default attacking application associated with the computer.
	*/
	public int getDefaultAttack(){
		return(MyComputer.getDefaultAttack());
	}
	
	/**
	Get the ftp application associated with the computer.
	*/
	public int getDefaultFTP(){
		return(MyComputer.getDefaultBank());
	}
	
	/**
	Get the default HTTP application associated with the computer.
	*/
	public int getDefaultHTTP(){
		return(MyComputer.getDefaultHTTP());
	}
	
	/**
	Get the IP that triggered this watch (Used if this happens to be a damage or health watch).
	*/
	public String getTargetIP(){
		return(ParentWatch.getTargetIP());
	}
	
	/**
	Get the port that triggered this watch.
	*/
	public int getTargetPort(){
		return(ParentWatch.getTargetPort());
	}
	
	/**
	Get the IP of the computer associated with this program.
	*/
	public String getIP(){
		return(MyComputer.getIP());
	}
	
	/**
	Get the port number associated with this program.
	*/
	public int getNumber(){
		return(ParentWatch.getNumber());
	}
	
	/**
	Get the fire wall that should be searched for based on initial setup.
	*/
	public String getSearchFireWall(){
		return(NewFireWall.FireWallNames[ParentWatch.getSearchFireWall()]);
	}
	
	/**
	installScript(HashMap Script);
	Installs a script on the various entrance points on this program.
	*/
	public void installScript(HashMap Script){
		fireScript=(String)Script.get("fire");
	}
		
	/**
	Return a hash map representation of the program currently installed on this port.
	*/
	public HashMap getContent(){
		HashMap returnMe=new HashMap();
		returnMe.put("fire",fireScript);
		return(returnMe);
	}
	
	/**
	Execute the script associated with this watch.
	*/
	public void execute(ApplicationData MyApplicationData){
		try{
			HackerLinker HL=new HackerLinker(this,super.getComputerHandler());
			RunFactory.runCode(fireScript,HL,MyComputer.MAX_OPS);
		}catch(Exception e){
			e.printStackTrace();
		}
		triggered=false;
	}

	/**
	Returns the keys associated with this program type.
	*/
	public String[] getTypeKeys(){
		String returnMe[]=new String[]{"fire"};
		return(returnMe);
	}
	
	/**
	Output the class data in XML format.
	*/
	public String outputXML(){
		String returnMe="";
		
		if(fireScript!=null)
			returnMe+="<fire><![CDATA["+fireScript.replaceAll("]]>","]]&gt;")+"]]></fire>\n";
		else
			returnMe+="<fire><![CDATA["+fireScript+"]]></fire>\n";

		return(returnMe);
	}
}
