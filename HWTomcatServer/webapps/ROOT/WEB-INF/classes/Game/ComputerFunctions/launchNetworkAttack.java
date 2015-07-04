package Game.ComputerFunctions;
import Game.*;
import Assignments.*;
/**
A function that is run within Computer.java.

Used to create a folder in the player's file system.
*/

import java.util.*;
import Assignments.*;
import Hackscript.Model.*;

public class launchNetworkAttack extends function{
	public launchNetworkAttack(Computer MyComputer){
		super(MyComputer);
	}
	
	public void execute(ApplicationData MyApplicationData){		
		Computer MyComputer = super.getComputer();
		String npcIP=(String)((Object[])MyApplicationData.getParameters())[0];
		//npcIP = "900.800.7.012";
		//Cause an NPC to attack this player.
		HashMap TriggerParam = new HashMap();
				
		TriggerParam.put("playerip", new TypeString(MyComputer.getIP()));
		TriggerParam.put("defaultattack",new TypeInteger(MyComputer.getDefaultAttack()));
		TriggerParam.put("defaultbank",new TypeInteger(MyComputer.getDefaultBank()));
		TriggerParam.put("defaulthttp",new TypeInteger(MyComputer.getDefaultHTTP()));
		TriggerParam.put("defaultredirecting",new TypeInteger(MyComputer.getDefaultShipping()));
				
		Object Parameter[] = new Object[]{"netbomb", TriggerParam, MyComputer.getIP()};
			
		System.out.println("Launching attack with NPC " + npcIP + " against " + Parameter[2] + ".");
		MyComputer.getComputerHandler().addData(new ApplicationData("requesttriggernote",Parameter,0, MyComputer.getIP()), npcIP);
	}
}
