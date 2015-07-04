package Hacktendo.Functions;


import java.util.ArrayList;
import Hackscript.Model.*;
import Hacktendo.*;

public class setBeat extends LinkerFunctions{

	private RenderEngine RE;
	private HacktendoLinker HL;
	public setBeat(RenderEngine RE,HacktendoLinker HL){
		this.RE=RE;
		this.HL=HL;
	}
	
	public Object execute(ArrayList parameters){
		RE.setBeat(true);
		return(null);
	}
}
