package Hacktendo.Functions;


import java.util.ArrayList;
import Hackscript.Model.*;
import Hacktendo.*;

public class Length extends LinkerFunctions{

	private RenderEngine RE;
	private HacktendoLinker HL;
	public Length(RenderEngine RE,HacktendoLinker HL){
		this.RE=RE;
		this.HL=HL;
	}
	
	public Object execute(ArrayList parameters){
		if(parameters.get(0)!=null){
			if(parameters.get(0) instanceof TypeArray)
				return( new TypeInteger(( (ArrayList) ( (TypeArray) parameters.get(0) ).getRawValue()).size()));
			ArrayList Temp=(ArrayList)parameters.get(0);
			return(new TypeInteger(Temp.size()));
		}else
			return(new TypeInteger(0));
	}
}
