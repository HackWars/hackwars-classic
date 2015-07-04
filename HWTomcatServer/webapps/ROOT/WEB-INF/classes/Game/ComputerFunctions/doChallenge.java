package Game.ComputerFunctions;
import Game.*;
import Assignments.*;
/**
A function that is run within Computer.java.

Runs a challenge.
*/

import java.util.*;
import Assignments.*;
import Hackscript.Model.*;

public class doChallenge extends function{
	public doChallenge(Computer MyComputer){
		super(MyComputer);
	}
	
	public void execute(ApplicationData MyApplicationData){
		super.getComputer().doChallengeRPC((String)((Object[])MyApplicationData.getParameters())[1],(String)((Object[])MyApplicationData.getParameters())[0]);
		super.getComputer().sendPacket();
	}
}
