package Game.ComputerFunctions;
import Game.*;
import Assignments.*;
/**
A function that is run within Computer.java.

This function is sent to a player's computer to request that the zombie attack handshake begin taking place.
*/

import java.util.*;
import Assignments.*;
import Hackscript.Model.*;

public class requestZombieAttack extends function{
	public requestZombieAttack(Computer MyComputer){
		super(MyComputer);
	}
	
	public void execute(ApplicationData MyApplicationData){
		if(super.getComputer().checkBank()){
			if(super.getComputer().getPettyCash()>=20.0f){
				NetworkSwitch MyComputerHandler=super.getComputer().getComputerHandler();
				MyComputerHandler.addData(new ApplicationData("pettycash",new Float(-20.0f),0,super.getComputer().getIP()),super.getComputer().getIP());
				String targetIP=(String)((Object[])MyApplicationData.getParameters())[5];
				MyComputerHandler.addData(new ApplicationData("zombieattack",MyApplicationData.getParameters(),MyApplicationData.getPort(),super.getComputer().getIP()),targetIP);
			}else{
				super.getComputer().addMessage(MessageHandler.ZOMBIE_FAIL_NOT_ENOUGH_MONEY);
				super.getComputer().sendPacket();
			}
		}
	}
}
