package Hacktendo.Functions;


import java.util.ArrayList;
import Hackscript.Model.*;
import Hacktendo.*;

public class Asin extends LinkerFunctions{

	private RenderEngine RE;
	private HacktendoLinker HL;
	public Asin(RenderEngine RE,HacktendoLinker HL){
		this.RE=RE;
		this.HL=HL;
	}
	
	public Object execute(ArrayList parameters){
		float val = (Float)((TypeFloat)parameters.get(0)).getRawValue();
		float returnMe = (float)Math.asin(val)*HacktendoLinker.V180_OVER_PI;
		return(new TypeFloat(returnMe));
	}
}
