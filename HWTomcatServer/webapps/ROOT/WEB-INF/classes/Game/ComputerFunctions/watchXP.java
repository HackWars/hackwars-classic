package Game.ComputerFunctions;
import Game.*;
import Assignments.*;
/**
A function that is run within Computer.java.

Reward a player watch experience.
*/

import java.util.*;
import Assignments.*;
import Hackscript.Model.*;

public class watchXP extends function{
	public watchXP(Computer MyComputer){
		super(MyComputer);
	}
	
	public void execute(ApplicationData MyApplicationData){
		Float amount=(Float)MyApplicationData.getParameters();
		float mult = 1.0f;
		amount *= mult;
		amount+=(Float)super.getComputer().getStats().get("Watch");
		if(amount < 300.0f && mult < 0) amount = 300.0f;
		super.getComputer().getStats().put("Watch",amount);
		super.getComputer().sendDamagePacket();
	}
}
