package Hacktendo.Functions;


import java.util.ArrayList;
import Hackscript.Model.*;
import Hacktendo.*;

public class Tan extends LinkerFunctions{

	private RenderEngine RE;
	private HacktendoLinker HL;
	public Tan(RenderEngine RE,HacktendoLinker HL){
		this.RE=RE;
		this.HL=HL;
	}
	
	public Object execute(ArrayList parameters){
		try{
			
		float f1=0.0f;
		if(parameters.get(0) instanceof TypeFloat)
			f1=(Float)((TypeFloat)parameters.get(0)).getRawValue();
		f1=f1*HacktendoLinker.PI_OVER_180;
		return(new TypeFloat((float)Math.tan(f1)));
		}catch(Exception e){
		}
		return null;
	}
}
