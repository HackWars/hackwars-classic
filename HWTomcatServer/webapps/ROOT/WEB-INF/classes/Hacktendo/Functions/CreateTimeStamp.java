package Hacktendo.Functions;


import java.util.ArrayList;
import Hackscript.Model.*;
import Hacktendo.*;
import util.*;

public class CreateTimeStamp extends LinkerFunctions{

	private RenderEngine RE;
	private HacktendoLinker HL;
	public CreateTimeStamp(RenderEngine RE,HacktendoLinker HL){
		this.RE=RE;
		this.HL=HL;
	}
	
	public Object execute(ArrayList parameters){
	String key = (String)((TypeString)parameters.get(0)).getStringValue();
	HL.addTimer(key,new Long(Time.getInstance().getCurrentTime()));
	return(new TypeFloat(0));
	}
}
