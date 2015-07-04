package Game.ComputerFunctions;
import Game.*;
import Assignments.*;
/**
A function that is run within Computer.java.

This function requests that a watch be fired explicitly (usually they fire when some event takes place).
*/

import java.util.*;
import Assignments.*;
import Hackscript.Model.*;

public class requestTriggerNote extends function{
	public requestTriggerNote(Computer MyComputer){
		super(MyComputer);
	}
	
	public void execute(ApplicationData MyApplicationData){
		String watchNote=(String)((Object[])MyApplicationData.getParameters())[0];
		HashMap TriggerParam=(HashMap)((Object[])MyApplicationData.getParameters())[1];
		String targetIP=(String)((Object[])MyApplicationData.getParameters())[2];
		
		//System.out.println("Trying to trigger watch with targetIP " + targetIP + " My IP is " + super.getComputer().getIP());
		
		super.getComputer().getWatchHandler().triggerWatch(watchNote,targetIP,TriggerParam);
	}
}
