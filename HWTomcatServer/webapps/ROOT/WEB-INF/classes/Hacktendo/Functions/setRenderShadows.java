package Hacktendo.Functions;


import java.util.ArrayList;
import Hackscript.Model.*;
import Hacktendo.*;

public class setRenderShadows extends LinkerFunctions{

	private OpenGLRenderEngine RE;
	private HacktendoLinker HL;
	public setRenderShadows(RenderEngine RE,HacktendoLinker HL){
		this.RE=(OpenGLRenderEngine)RE;
		this.HL=HL;
	}
	
	public Object execute(ArrayList parameters){
		boolean val=false;
		
		if(parameters.get(0) instanceof TypeBoolean)
			val = (Boolean)((TypeBoolean)parameters.get(0)).getRawValue();
			
		RE.setRenderShadows(val);
		return null;
	}
}
