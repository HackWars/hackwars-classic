package Hacktendo.Functions;


import java.util.ArrayList;
import Hackscript.Model.*;
import Hacktendo.*;

public class getPassable extends LinkerFunctions{

	private RenderEngine RE;
	private HacktendoLinker HL;
	public getPassable(RenderEngine RE,HacktendoLinker HL){
		this.RE=RE;
		this.HL=HL;
	}
	
	public Object execute(ArrayList parameters){
		int x = (int)(Integer)((TypeInteger)parameters.get(0)).getRawValue();
		int y = (int)(Integer)((TypeInteger)parameters.get(1)).getRawValue();
		int id = x+y*45;
		return(new TypeBoolean(RE.isPassable(id)));
	}
}
