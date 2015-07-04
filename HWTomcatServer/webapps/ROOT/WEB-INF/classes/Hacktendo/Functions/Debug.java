package Hacktendo.Functions;


import java.util.ArrayList;
import Hackscript.Model.*;
import Hacktendo.*;

public class Debug extends LinkerFunctions{

	private RenderEngine RE;
	private HacktendoLinker HL;
	public Debug(RenderEngine RE,HacktendoLinker HL){
		this.RE=RE;
		this.HL=HL;
	}
	
	public Object execute(ArrayList parameters){
		String message = (String)((TypeString)parameters.get(0)).getStringValue();
		RE.setDebugMessage(message);
		return null;
	}
}
