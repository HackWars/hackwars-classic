package Game.ComputerFunctions;
import Game.*;
import Assignments.*;
/**
A function that is run within Computer.java.

Turns a port on a player's computer on or off.
*/

import java.util.*;
import Assignments.*;
import Hackscript.Model.*;

public class portOnOff extends function{
	public portOnOff(Computer MyComputer){
		super(MyComputer);
	}
	
	public void execute(ApplicationData MyApplicationData){
		HashMap Ports=super.getComputer().getPorts();
		int port=MyApplicationData.getPort();
	
		Object[] message=null;
		boolean on=(Boolean)(MyApplicationData.getParameters());
		
		Iterator PortIterator=Ports.entrySet().iterator();
		while(PortIterator.hasNext()){
			Port TempPort=(Port)(((Map.Entry)PortIterator.next()).getValue());
			if(TempPort.getNumber()==port){
				boolean allow=true;
				if(on&&!TempPort.getOn()){
					TempPort.setOn(true);

					float cpuCheck=super.getComputer().getCPULoad()+TempPort.getCPUCost();
					float maxCPU=Computer.CPU_CHART[super.getComputer().getCPUType()]+super.getComputer().getEquipmentSheet().getCPUBonus();
					
					if(cpuCheck>maxCPU){
						allow=false;
						message=MessageHandler.PORT_ON_FAIL_EXCEED_CPU;
					}
					
					TempPort.setOn(false);
				}
				
				if(!on&&TempPort.getOn()){//Make srue we are allowed to turn off the port at this time.
					if(!TempPort.getAccessing().equals("")){
						allow=false;
						message=MessageHandler.PORT_OFF_FAIL_UNDER_ATTACK;
					}else if(TempPort.getAttacking()){
						allow=false;
						message=MessageHandler.PORT_OFF_FAIL_ATTACKING;
					}else if(TempPort.getOverHeated()){
						allow=false;
						message = MessageHandler.PORT_OFF_FAIL_OVERHEATED;
					}
					else if(TempPort.getHealth()!=100.0f){
						allow = false;
						message = MessageHandler.PORT_OFF_FAIL_DAMAGED;
					}
				}
				
				if(allow){
					TempPort.setOn(on);
				}else
					super.getComputer().addMessage(message);
					
				allow=false;
			}
		}

		super.getComputer().getComputerHandler().addData(new ApplicationData("fetchports",null,0,super.getComputer().getIP()),super.getComputer().getIP());
	}
}
