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

public class requestTrigger extends function{
	public requestTrigger(Computer MyComputer){
		super(MyComputer);
	}
	
	public void execute(ApplicationData MyApplicationData){
		int watchNumber=(Integer)((Object[])MyApplicationData.getParameters())[0];
		HashMap TriggerParam=(HashMap)((Object[])MyApplicationData.getParameters())[1];
		String targetIP=(String)((Object[])MyApplicationData.getParameters())[2];
		super.getComputer().getWatchHandler().triggerWatch(watchNumber,targetIP,TriggerParam);
	}
}
