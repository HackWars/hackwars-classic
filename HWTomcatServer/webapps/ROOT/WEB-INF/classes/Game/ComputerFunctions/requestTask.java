package Game.ComputerFunctions;
import Game.*;
import Assignments.*;
/**
A function that is run within Computer.java.

Some Hacktendo games can be part of a quest. This function allows a hacktendo game to indicate it has finished a step in a quest.
*/

import java.util.*;
import Assignments.*;
import Hackscript.Model.*;

public class requestTask extends function{
	public requestTask(Computer MyComputer){
		super(MyComputer);
	}
	
	public void execute(ApplicationData MyApplicationData){
		String fileName=(String)((Object[])MyApplicationData.getParameters())[0];
		Integer questID=(Integer)((Object[])MyApplicationData.getParameters())[1];
		String taskName=(String)((Object[])MyApplicationData.getParameters())[2];
									
		if(!super.getComputer().checkQuest(questID)){//Make sure the quest isn't already complete.							
			HashMap CurrentQuest=(HashMap)((Object[])super.getComputer().getCurrentQuests().get(questID))[0];
			String label=(String)((Object[])super.getComputer().getCurrentQuests().get(questID))[1];
			if(CurrentQuest==null){
				CurrentQuest=new HashMap();
				CurrentQuest.put(taskName,new Object[]{new Boolean(true),""});
				super.getComputer().getCurrentQuests().put(questID,new Object[]{CurrentQuest,label});
			}else{
				CurrentQuest.put(taskName,new Object[]{new Boolean(true),""});
			}
		}
	}
}
