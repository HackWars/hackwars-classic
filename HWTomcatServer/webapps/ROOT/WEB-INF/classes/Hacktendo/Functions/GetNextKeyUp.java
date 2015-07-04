package Hacktendo.Functions;


import java.util.ArrayList;
import Hackscript.Model.*;
import Hacktendo.*;

public class GetNextKeyUp extends LinkerFunctions{

	private RenderEngine RE;
	private HacktendoLinker HL;
	public GetNextKeyUp(RenderEngine RE,HacktendoLinker HL){
		this.RE=RE;
		this.HL=HL;
	}
	
	public Object execute(ArrayList parameters){
		return(new TypeInteger(RE.getNextKeyUp()));
	}
}
