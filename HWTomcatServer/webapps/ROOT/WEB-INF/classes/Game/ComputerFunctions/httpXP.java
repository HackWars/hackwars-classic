package Game.ComputerFunctions;
import Game.*;
import Assignments.*;
/**
A function that is run within Computer.java.

Reward a player HTTP experience.
*/

import java.util.*;
import Assignments.*;
import Hackscript.Model.*;

public class httpXP extends function{
	public httpXP(Computer MyComputer){
		super(MyComputer);
	}
	
	public void execute(ApplicationData MyApplicationData){
		boolean vote=true;
		if(super.getComputer().getType()!=Computer.NPC){
			Float amount=(Float)MyApplicationData.getParameters();
			
			if(amount==500.7337f){//This value counts as a vote.
				if(super.getComputer().checkHTTP()){
					amount=500.0f;
					super.getComputer().setVoteCount(super.getComputer().getVoteCount()+1);
				}else{
					vote=false;
					super.getComputer().addMessage(MessageHandler.VOTE_FAIL_HTTP_NOT_ON);
				}
			}
			
			if(vote){//Should we get XP?
				float mult = 1.0f;
				amount *= mult;
				amount+=(Float)super.getComputer().getStats().get("Webdesign");
				if(amount < 300.0f && mult < 0) amount = 300.0f;
				super.getComputer().getStats().put("Webdesign",amount);
			}
			
			super.getComputer().sendDamagePacket();
		}
	}
}
