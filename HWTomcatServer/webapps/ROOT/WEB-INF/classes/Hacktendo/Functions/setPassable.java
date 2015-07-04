package Hacktendo.Functions;


import java.util.ArrayList;
import Hackscript.Model.*;
import Hacktendo.*;

public class setPassable extends LinkerFunctions{

	private RenderEngine RE;
	private HacktendoLinker HL;
	public setPassable(RenderEngine RE,HacktendoLinker HL){
		this.RE=RE;
		this.HL=HL;
	}
	
	public Object execute(ArrayList parameters){
		int x = (int)(Integer)((TypeInteger)parameters.get(0)).getRawValue();
		int y = (int)(Integer)((TypeInteger)parameters.get(1)).getRawValue();
		int index=x+y*45;
		boolean passable = (boolean)(Boolean)((TypeBoolean)parameters.get(2)).getRawValue();
		RE.getCurrentMap().setPassable(index,passable);
		return(null);
	}
}
