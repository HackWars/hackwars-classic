package Game.ComputerFunctions;
import Game.*;
import Assignments.*;
/**
A function that is run within Computer.java.

Indicate that a port is to be treated as a dummy port.
*/

import java.util.*;
import Assignments.*;
import Hackscript.Model.*;

public class setDummyPort extends function{
	public setDummyPort(Computer MyComputer){
		super(MyComputer);
	}
	
	public void execute(ApplicationData MyApplicationData){
		HashMap Ports=super.getComputer().getPorts();
		int port=MyApplicationData.getPort();
	
		boolean on=(Boolean)(MyApplicationData.getParameters());
		if(Ports.get(new Integer(port))!=null){
			Port tempport=(Port)Ports.get(new Integer(port));
			if(!tempport.getAttacking()&&tempport.getAccessing().length()==0){
			
				if(!tempport.getOverHeated()){
					if(on==false&&tempport.getDummy()&&tempport.getOn()){
						float cpuCheck=super.getComputer().getCPULoad()+tempport.getActualCPUCost();
						float maxCPU=Computer.CPU_CHART[super.getComputer().getCPUType()]+super.getComputer().getEquipmentSheet().getCPUBonus();
						if(cpuCheck<=maxCPU){
							tempport.setDummy(on);
						}
					}else
						tempport.setDummy(on);
				}
			
			}
		}
		super.getComputer().getComputerHandler().addData(new ApplicationData("fetchports",null,0,super.getComputer().getIP()),super.getComputer().getIP());
	}
}
