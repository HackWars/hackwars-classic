package Hacktendo.Functions;


import java.util.ArrayList;
import Hackscript.Model.*;
import Hacktendo.*;

public class getMouseWorldY extends LinkerFunctions{

	private OpenGLRenderEngine RE;
	private HacktendoLinker HL;
	public getMouseWorldY(RenderEngine RE,HacktendoLinker HL){
		this.RE=(OpenGLRenderEngine)RE;
		this.HL=HL;
	}
	
	public Object execute(ArrayList parameters){
		return(new TypeInteger(RE.getMouseWorldY()));
	}
}
