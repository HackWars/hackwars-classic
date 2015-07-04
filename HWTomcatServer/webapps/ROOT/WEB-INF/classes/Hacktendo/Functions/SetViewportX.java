package Hacktendo.Functions;


import java.util.ArrayList;
import Hackscript.Model.*;
import Hacktendo.*;

public class SetViewportX extends LinkerFunctions{

	private RenderEngine RE;
	private HacktendoLinker HL;
	public SetViewportX(RenderEngine RE,HacktendoLinker HL){
		this.RE=RE;
		this.HL=HL;
	}
	
	public Object execute(ArrayList parameters){
		int val;
		if(parameters.get(0) instanceof TypeInteger)
			val = (int)(Integer)((TypeInteger)parameters.get(0)).getRawValue();
		else
			val = (int)(float)(Float)((TypeFloat)parameters.get(0)).getRawValue();
					
		RE.setViewX(val);
		return null;
	}
}
