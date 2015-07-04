package Game.ComputerFunctions;
import Game.*;
import Assignments.*;
/**
A function that is run within Computer.java.

Reward a player repair experience.
*/

import java.util.*;
import Assignments.*;
import Hackscript.Model.*;

public class repairXP extends function{
	public repairXP(Computer MyComputer){
		super(MyComputer);
	}
	
	public void execute(ApplicationData MyApplicationData){
		float amount=(Float)MyApplicationData.getParameters();
		float mult = 1.0f;
		amount *= mult;
		amount+=(Float)super.getComputer().getStats().get("Repair");
		if(amount < 300.0f && mult < 0) amount = 300.0f;
		super.getComputer().getStats().put("Repair",amount);
		super.getComputer().sendDamagePacket();
	}
}
