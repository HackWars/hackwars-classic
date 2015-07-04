package Hacktendo.Functions;


import java.util.ArrayList;
import Hackscript.Model.*;
import Hacktendo.*;
import java.util.*;

public class TriggerWatch extends LinkerFunctions{

	private RenderEngine RE;
	private HacktendoLinker HL;
	public TriggerWatch(RenderEngine RE,HacktendoLinker HL){
		this.RE=RE;
		this.HL=HL;
	}
	
	public Object execute(ArrayList parameters){
		String watchNote="";
		if(parameters.get(0) instanceof TypeString)
			watchNote=((TypeString)parameters.get(0)).getStringValue();
		HashMap TriggerParam=new HashMap();
		for(int i=1;i<parameters.size();i+=2){
			String key=((TypeString)parameters.get(i)).getStringValue();
			TriggerParam.put(key,parameters.get(i+1));
		}
		
		Object O[]=new Object[]{watchNote,TriggerParam};
		RE.setFireWatch(O);
		return null;
	}
}
