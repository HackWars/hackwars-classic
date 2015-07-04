package Hacktendo.Functions;


import java.util.ArrayList;
import Hackscript.Model.*;
import Hacktendo.*;

public class setViewportYRotation extends LinkerFunctions{

	private OpenGLRenderEngine RE;
	private HacktendoLinker HL;
	public setViewportYRotation(RenderEngine RE,HacktendoLinker HL){
		this.RE=(OpenGLRenderEngine)RE;
		this.HL=HL;
	}
	
	public Object execute(ArrayList parameters){
		try{
			float val=0.0f;

			if(parameters.get(0) instanceof TypeFloat)
				val = (float)(Float)((TypeFloat)parameters.get(0)).getRawValue();
			else if(parameters.get(0) instanceof TypeInteger)
				val = (float)(int)(Integer)((TypeInteger)parameters.get(0)).getRawValue();

			RE.setYRotation(val);
		}catch(Exception e){
		}
		return null;
	}
}
