package Hacktendo.Functions;


import java.util.ArrayList;
import Hackscript.Model.*;
import Hacktendo.*;

public class Abs extends LinkerFunctions{

	private RenderEngine RE;
	private HacktendoLinker HL;
	public Abs(RenderEngine RE,HacktendoLinker HL){
		this.RE=RE;
		this.HL=HL;
	}
	
	public Object execute(ArrayList parameters){
		Object O = parameters.get(0);
		if(O instanceof TypeInteger){
			int number = (int)(Integer)((TypeInteger)O).getRawValue();
			return new TypeInteger(Math.abs(number));
		}else
		
		if(O instanceof TypeFloat){
			float number=(Float)((TypeFloat)O).getRawValue();
			return new TypeFloat(Math.abs(number));
		}
		return null;
	}
}
