package Hacktendo.Functions;


import java.util.ArrayList;
import Hackscript.Model.*;
import Hacktendo.*;

public class Pow extends LinkerFunctions{

	private RenderEngine RE;
	private HacktendoLinker HL;
	public Pow(RenderEngine RE,HacktendoLinker HL){
		this.RE=RE;
		this.HL=HL;
	}
	
	public Object execute(ArrayList parameters){
		float val = (float)(Float)((TypeFloat)parameters.get(0)).getRawValue();
		float pow = (float)(Float)((TypeFloat)parameters.get(1)).getRawValue();
		return(new TypeFloat((float)Math.pow((double)val,(double)pow)));
	}
}
