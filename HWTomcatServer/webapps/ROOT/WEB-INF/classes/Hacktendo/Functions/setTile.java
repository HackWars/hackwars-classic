package Hacktendo.Functions;


import java.util.ArrayList;
import Hackscript.Model.*;
import Hacktendo.*;

public class setTile extends LinkerFunctions{

	private OpenGLRenderEngine RE;
	private HacktendoLinker HL;
	public setTile(RenderEngine RE,HacktendoLinker HL){
		this.RE=(OpenGLRenderEngine)RE;
		this.HL=HL;
	}
	
	public Object execute(ArrayList parameters){		

		int x = (int)(Integer)((TypeInteger)parameters.get(0)).getRawValue();
		int y = (int)(Integer)((TypeInteger)parameters.get(1)).getRawValue();
		int index=x+y*45;
		int tile = (int)(Integer)((TypeInteger)parameters.get(2)).getRawValue();
		RE.getCurrentMap().setTile(index,tile,RE.getCurrentMap().isPassable(index));
		RE.regenerateLists();
		return(null);
	}
}
