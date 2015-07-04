package Game.ComputerFunctions;
import Game.*;
import Assignments.*;
/**
A function that is run within Computer.java.

Changes the port that a watch is pointing at.
*/

import java.util.*;
import Assignments.*;
import Hackscript.Model.*;

public class changeWatchPort extends function{
	public changeWatchPort(Computer MyComputer){
		super(MyComputer);
	}
	
	public void execute(ApplicationData MyApplicationData){
		int target_watch=(Integer)((Integer[])MyApplicationData.getParameters())[0];
		int new_port=(Integer)((Integer[])MyApplicationData.getParameters())[1];
		WatchHandler MyWatchHandler=super.getComputer().getWatchHandler();
			
		if(target_watch<MyWatchHandler.getWatches().size()){
			Watch MyWatch=(Watch)MyWatchHandler.getWatch(target_watch);
			MyWatch.setPort(new_port);
			super.getComputer().getComputerHandler().addData(new ApplicationData("fetchwatches",null,0,super.getComputer().getIP()),super.getComputer().getIP());
		}
	}
}
