package Game.ComputerFunctions;
import Game.*;
import Assignments.*;
/**
A function that is run within Computer.java.

Reward a player redirect experience.
*/

import java.util.*;
import Assignments.*;
import Hackscript.Model.*;

public class redirectXP extends function{
	public redirectXP(Computer MyComputer){
		super(MyComputer);
	}
	
	public void execute(ApplicationData MyApplicationData){
		if(MyApplicationData.getParameters() instanceof Integer){
			int type=(Integer)MyApplicationData.getParameters();
			float amount=Computer.commodityXP[type];
			float mult = 1.0f;
			amount *= mult;
			amount+=(Float)super.getComputer().getStats().get("Redirecting");
			if(amount < 300.0f && mult < 0) amount = 300.0f;
			super.getComputer().getStats().put("Redirecting",amount);
		}else{
			float amount=(Float)MyApplicationData.getParameters();
			float mult = 1.0f;
			amount *= mult;
			amount+=(Float)super.getComputer().getStats().get("Redirecting");
			if(amount < 300.0f && mult < 0) amount = 300.0f;
			super.getComputer().getStats().put("Redirecting",amount);
		}
		super.getComputer().sendDamagePacket();
	}
}
