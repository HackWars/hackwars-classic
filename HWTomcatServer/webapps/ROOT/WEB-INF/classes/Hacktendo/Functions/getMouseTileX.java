package Hacktendo.Functions;


import java.util.ArrayList;
import Hackscript.Model.*;
import Hacktendo.*;

public class getMouseTileX extends LinkerFunctions{

	private OpenGLRenderEngine RE;
	private HacktendoLinker HL;
	public getMouseTileX(RenderEngine RE,HacktendoLinker HL){
		this.RE=(OpenGLRenderEngine)RE;
		this.HL=HL;
	}
	
	public Object execute(ArrayList parameters){
		return(new TypeInteger(RE.getMouseTileX()));
	}
}
