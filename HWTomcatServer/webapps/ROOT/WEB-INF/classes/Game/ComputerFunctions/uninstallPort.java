package Game.ComputerFunctions;
import Game.*;
import Assignments.*;
/**
A function that is run within Computer.java.

Uninstalls a port on the player's computer.
*/

import java.util.*;
import Assignments.*;
import Hackscript.Model.*;

public class uninstallPort extends function{
	public uninstallPort(Computer MyComputer){
		super(MyComputer);
	}
	
	public void execute(ApplicationData MyApplicationData){
		float maxCPU=Computer.CPU_CHART[super.getComputer().getCPUType()]+super.getComputer().getEquipmentSheet().getCPUBonus();
		if(super.getComputer().getCPULoad()<=maxCPU){

			int delete_port=(Integer)(MyApplicationData.getParameters());
			
			Iterator PortIterator=super.getComputer().getPorts().entrySet().iterator();
			while(PortIterator.hasNext()){
				Port TempPort=(Port)(((Map.Entry)PortIterator.next()).getValue());
				if(TempPort.getNumber()==delete_port){
					
					boolean allow=true;
					Object[] message=null;
					if(!TempPort.getAccessing().equals("")){
						allow=false;
						message=MessageHandler.UNINSTALL_PORT_FAIL_UNDER_ATTACK;
					}if(TempPort.getAttacking()){
						allow=false;
						message=MessageHandler.UNINSTALL_PORT_FAIL_ATTACKING;
					}if(TempPort.getOverHeated()){
						allow=false;
						message=MessageHandler.UNINSTALL_PORT_FAIL_OVERHEATED;
					}
				
					if(allow)
						PortIterator.remove();
					else{
						super.getComputer().addMessage(message);
					}
				}
			}

			super.getComputer().getComputerHandler().addData(new ApplicationData("fetchports",null,0,super.getComputer().getIP()),super.getComputer().getIP());
		}
	}
}
