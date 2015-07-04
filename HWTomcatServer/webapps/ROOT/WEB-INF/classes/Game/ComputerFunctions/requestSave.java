package Game.ComputerFunctions;
import Game.*;
import Assignments.*;
/**
A function that is run within Computer.java.

This function allows a player of a Hacktendo game to save variables from the current state of the game they are playing.
*/

import java.util.*;
import Assignments.*;
import Hackscript.Model.*;

public class requestSave extends function{
	public requestSave(Computer MyComputer){
		super(MyComputer);
	}
	
	public void execute(ApplicationData MyApplicationData){
		String fileName=(String)((Object[])MyApplicationData.getParameters())[0];
		HashMap TriggerParam=(HashMap)((Object[])MyApplicationData.getParameters())[1];

		HackerFile NewHackerFile=new HackerFile(HackerFile.TEXT);
		NewHackerFile.setDescription("A save file for "+fileName+".");
		NewHackerFile.setMaker(fileName);
		NewHackerFile.setQuantity(0);
		HashMap attributes=new HashMap();
		
		String data="";
		if(TriggerParam!=null){
			Iterator MyIterator=TriggerParam.entrySet().iterator();
			while(MyIterator.hasNext()){
				Map.Entry AnEntry=(Map.Entry)MyIterator.next();
				Variable MyVariable=(Variable)AnEntry.getValue();
				String key=(String)AnEntry.getKey();
				
				if(MyVariable instanceof TypeString){
					data+=key+"\t";
					data+="string\t";
					data+=(MyVariable)+"\n";
				}else
				
				if(MyVariable instanceof TypeInteger){
					data+=key+"\t";
					data+="int\t";
					data+=(MyVariable)+"\n";
				}else
				
				if(MyVariable instanceof TypeFloat){
					data+=key+"\t";
					data+="float\t";
					data+=(MyVariable)+"\n";
				}else
				
				if(MyVariable instanceof TypeBoolean){
					data+=key+"\t";
					data+="bool\t";
					data+=(MyVariable)+"\n";
				}
				
			}
		}
		
		attributes.put("data",data);
		attributes.put("level","");
		NewHackerFile.setContent(attributes);
		NewHackerFile.setName(fileName+".save");
		Object Parameter[]=new Object[]{"",NewHackerFile};
		super.getComputer().getComputerHandler().addData(new ApplicationData("savefile",Parameter,0,super.getComputer().getIP()),super.getComputer().getIP());
	}
}
