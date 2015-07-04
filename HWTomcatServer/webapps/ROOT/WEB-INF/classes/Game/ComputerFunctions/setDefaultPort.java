package Game.ComputerFunctions;
import Game.*;
import Assignments.*;
/**
A function that is run within Computer.java.

Set the default port associated with a certain operation on a player's computer.
*/

import java.util.*;
import Assignments.*;
import Hackscript.Model.*;

public class setDefaultPort extends function{
	public setDefaultPort(Computer MyComputer){
		super(MyComputer);
	}
	
	public void execute(ApplicationData MyApplicationData){
		int type=(Integer)MyApplicationData.getParameters();
		int port=MyApplicationData.getPort();
		
		if(type==PacketPort.BANKING)
			super.getComputer().setDefaultBank(port);
		if(type==PacketPort.ATTACK)
			super.getComputer().setDefaultAttack(port);
		if(type==PacketPort.FTP)
			super.getComputer().setDefaultFTP(port);
		if(type==PacketPort.HTTP)
			super.getComputer().setDefaultHTTP(port);
		if(type==PacketPort.SHIPPING){
			super.getComputer().setDefaultShipping(port);
		}
			
		super.getComputer().getComputerHandler().addData(new ApplicationData("fetchports",null,0,super.getComputer().getIP()),super.getComputer().getIP());
	}
}
