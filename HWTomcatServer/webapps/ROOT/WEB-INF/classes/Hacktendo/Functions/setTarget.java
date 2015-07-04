package Hacktendo.Functions;


import java.util.ArrayList;
import Hackscript.Model.*;
import Hacktendo.*;

public class setTarget extends LinkerFunctions{

	private ClientRenderEngine RE;
	private HacktendoLinker HL;
	private boolean initialized=false;
	
	public setTarget(RenderEngine RE,HacktendoLinker HL){
		if(RE instanceof ClientRenderEngine){
			this.RE=(ClientRenderEngine)RE;
			this.HL=HL;
			initialized=true;
		}
	}
	
	public Object execute(ArrayList parameters){
		if(initialized){
			int val;
			int val2;
			if(parameters.get(0) instanceof TypeInteger)
				val = (int)(Integer)((TypeInteger)parameters.get(0)).getRawValue();
			else
				val = (int)(float)(Float)((TypeFloat)parameters.get(0)).getRawValue();
				
			if(parameters.get(1) instanceof TypeInteger)
				val2 = (int)(Integer)((TypeInteger)parameters.get(1)).getRawValue();
			else
				val2 = (int)(float)(Float)((TypeFloat)parameters.get(1)).getRawValue();

			RE.target(val,val2);
		}
					
		return null;
	}
}
