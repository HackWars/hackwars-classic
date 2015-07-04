package Hacktendo.Functions;


import java.util.ArrayList;
import Hackscript.Model.*;
import Hacktendo.*;

public class IndexOf extends LinkerFunctions{

	private RenderEngine RE;
	private HacktendoLinker HL;
	public IndexOf(RenderEngine RE,HacktendoLinker HL){
		this.RE=RE;
		this.HL=HL;
	}
	
	public Object execute(ArrayList parameters){
		String dataString=((TypeString)parameters.get(0)).getStringValue();
		String searchString=((TypeString)parameters.get(1)).getStringValue();
		int index=((TypeInteger)parameters.get(2)).getIntValue();
		return(new TypeInteger(dataString.indexOf(searchString,index)));
	}
}
