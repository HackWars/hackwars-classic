package Hacktendo.Functions;


import java.util.ArrayList;
import Hackscript.Model.*;
import Hacktendo.*;

public class Strlen extends LinkerFunctions{

	private RenderEngine RE;
	private HacktendoLinker HL;
	public Strlen(RenderEngine RE,HacktendoLinker HL){
		this.RE=RE;
		this.HL=HL;
	}
	
	public Object execute(ArrayList parameters){
		String val = ((TypeString)parameters.get(0)).getStringValue();
		
		return(new TypeInteger((int)val.length()));
	}
}
