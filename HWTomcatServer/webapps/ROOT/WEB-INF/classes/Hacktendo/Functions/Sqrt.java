package Hacktendo.Functions;


import java.util.ArrayList;
import Hackscript.Model.*;
import Hacktendo.*;

public class Sqrt extends LinkerFunctions{

	private RenderEngine RE;
	private HacktendoLinker HL;
	public Sqrt(RenderEngine RE,HacktendoLinker HL){
		this.RE=RE;
		this.HL=HL;
	}
	
	public Object execute(ArrayList parameters){
		float val = (Float)((TypeFloat)parameters.get(0)).getRawValue();
		return new TypeFloat((float)Math.sqrt(val));
	}
}
