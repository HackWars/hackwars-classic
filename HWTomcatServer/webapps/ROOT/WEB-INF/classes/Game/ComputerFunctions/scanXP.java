package Game.ComputerFunctions;
import Game.*;
import Assignments.*;
/**
A function that is run within Computer.java.

Reward a player scan experience.
*/

import java.util.*;
import Assignments.*;
import Hackscript.Model.*;

public class scanXP extends function{
	public scanXP(Computer MyComputer){
		super(MyComputer);
	}
	
	public void execute(ApplicationData MyApplicationData){
		super.getComputer().sendDamagePacket();
		Float amount=(Float)MyApplicationData.getParameters();
		float mult = 1.0f;
		amount *= mult;
		amount+=(Float)super.getComputer().getStats().get("Scanning");
		if(amount < 300.0f && mult < 0) amount = 300.0f;
		super.getComputer().getStats().put("Scanning",amount);
	}
}
