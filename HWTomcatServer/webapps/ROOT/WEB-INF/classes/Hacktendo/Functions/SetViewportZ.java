package Hacktendo.Functions;


import java.util.ArrayList;
import Hackscript.Model.*;
import Hacktendo.*;

public class SetViewportZ extends LinkerFunctions{

	private OpenGLRenderEngine RE;
	private HacktendoLinker HL;
	public SetViewportZ(RenderEngine RE,HacktendoLinker HL){
		this.RE=(OpenGLRenderEngine)RE;
		this.HL=HL;
	}
	
	public Object execute(ArrayList parameters){
		int val;
		
		if(parameters.get(0) instanceof TypeInteger)
			val = (int)(Integer)((TypeInteger)parameters.get(0)).getRawValue();
		else
			val = (int)(float)(Float)((TypeFloat)parameters.get(0)).getRawValue();
					
		RE.setViewZ(val*-1);
		return null;
	}
}
