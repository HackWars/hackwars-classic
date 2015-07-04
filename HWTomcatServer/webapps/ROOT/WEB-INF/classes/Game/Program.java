package Game;
/**
Program.java

An abstract program used to handle common functionality between the various programs.
(Banking,Attacking,FTP,etc.).
*/
import java.util.*;
public abstract class Program{
	private NetworkSwitch MyComputerHandler=null;//Central mechanism for contacting other computers.
	private Computer MyComputer=null;//The Computer this program is installed on.
	private String error="";
	
	/**
	Set the error string for this program which is used when compiling.
	*/
	public void setError(String error){
		this.error=error;
	}
	
	/**
	Get the error string associated with this program which is used when compiling.
	*/
	public String getError(){
		return(error);
	}
	
	/**
	setComputerHandler(ComputerHandler MyComputerHandler)
	sets the computer handler associated with this program.
	*/
	public void setComputerHandler(NetworkSwitch MyComputerHandler){
		this.MyComputerHandler=MyComputerHandler;
	}
	
	/**
	setComput(Computer MyComputer)
	sets the computerassociated with this program.
	*/
	public void setComputer(Computer MyComputer){
		this.MyComputer=MyComputer;
	}
	
	
	/**
	Get the computer setup to run with this application.
	*/
	public Computer getComputer(){
		return(MyComputer);
	}
	
	/**
	getComputerHandler()
	returns the computer handler associated with this program.
	*/
	public NetworkSwitch getComputerHandler(){
		return(MyComputerHandler);
	}
	
	/**
	installScript(HashMap Script);
	Installs a script on the various entrance points on this program.
	*/
	public abstract void installScript(HashMap Script);
	
	/**
	Execute the program with the RFC provided.
	*/
	public abstract void execute(ApplicationData MyApplicationData);
	
	/**
	Returns the keys that should be parsed from the save file given this program type.
	*/
	public abstract String[] getTypeKeys();
	
	/**
	Return a hashmap representation of the code.
	*/
	public abstract HashMap getContent();
	
	/**
	Output the class data in XML format.
	*/
	public abstract String outputXML();
}
