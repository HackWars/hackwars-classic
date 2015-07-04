package Hacktendo.Functions;


import java.util.ArrayList;
import Hackscript.Model.*;
import Hacktendo.*;

public class ParseFloat extends LinkerFunctions{

	private RenderEngine RE;
	private HacktendoLinker HL;
	public ParseFloat(RenderEngine RE,HacktendoLinker HL){
		this.RE=RE;
		this.HL=HL;
	}
	
	public Object execute(ArrayList parameters){
		String s1=((TypeString)parameters.get(0)).getStringValue();
		return(new TypeFloat((float)(new Float(s1))));
	}
}
