package Hacktendo.Functions;


import java.util.ArrayList;
import Hackscript.Model.*;
import Hacktendo.*;

public class Substr extends LinkerFunctions{

	private RenderEngine RE;
	private HacktendoLinker HL;
	public Substr(RenderEngine RE,HacktendoLinker HL){
		this.RE=RE;
		this.HL=HL;
	}
	
	public Object execute(ArrayList parameters){
		//Compare two strings which are passed in.
		String s1="";
		int start=0;
		int finish=0;
		if(parameters.get(0) instanceof TypeString)
			s1=((TypeString)parameters.get(0)).getStringValue();
		if(parameters.get(1) instanceof TypeInteger)
			start=((TypeInteger)parameters.get(1)).getIntValue();
		if(parameters.get(2) instanceof TypeInteger)
			finish=((TypeInteger)parameters.get(2)).getIntValue();
		return(new TypeString(s1.substring(start,finish)));
	}
}
