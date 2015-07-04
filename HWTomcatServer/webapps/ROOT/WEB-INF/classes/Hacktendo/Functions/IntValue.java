package Hacktendo.Functions;


import java.util.ArrayList;
import Hackscript.Model.*;
import Hacktendo.*;

public class IntValue extends LinkerFunctions{

	private RenderEngine RE;
	private HacktendoLinker HL;
	public IntValue(RenderEngine RE,HacktendoLinker HL){
		this.RE=RE;
		this.HL=HL;
	}
	
	public Object execute(ArrayList parameters){
		int val=0;
		try{
		
		if(parameters.get(0) instanceof TypeInteger){
			val = (int)(Integer)((TypeInteger)parameters.get(0)).getRawValue();
		}else{
			val = (int)(float)(Float)((TypeFloat)parameters.get(0)).getRawValue();
		}
			
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("An exception occured running int value.");
		}
			
		return(new TypeInteger(val));
	}
}
