package Hacktendo.Functions;


import java.util.ArrayList;
import Hackscript.Model.*;
import Hacktendo.*;

public class DeleteTimeStamp extends LinkerFunctions{

	private RenderEngine RE;
	private HacktendoLinker HL;
	public DeleteTimeStamp(RenderEngine RE,HacktendoLinker HL){
		this.RE=RE;
		this.HL=HL;
	}
	
	public Object execute(ArrayList parameters){
	String key = (String)((TypeString)parameters.get(0)).getStringValue();
	HL.removeTimer(key);
	return(new TypeFloat(0));
	}
}
