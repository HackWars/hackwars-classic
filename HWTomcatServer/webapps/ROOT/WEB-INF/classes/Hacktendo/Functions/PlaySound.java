package Hacktendo.Functions;


import java.util.ArrayList;
import Hackscript.Model.*;
import Hacktendo.*;

public class PlaySound extends LinkerFunctions{

	private RenderEngine RE;
	private HacktendoLinker HL;
	public PlaySound(RenderEngine RE,HacktendoLinker HL){
		this.RE=RE;
		this.HL=HL;
	}
	
	public Object execute(ArrayList parameters){
		int id = (int)(Integer)((TypeInteger)parameters.get(0)).getRawValue();
		RE.play(id);
		return null;
	}
}
