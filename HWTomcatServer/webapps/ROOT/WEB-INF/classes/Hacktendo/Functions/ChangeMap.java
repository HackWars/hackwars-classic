package Hacktendo.Functions;


import java.util.ArrayList;
import Hackscript.Model.*;
import Hacktendo.*;
import java.util.HashMap;

public class ChangeMap extends LinkerFunctions{

	private RenderEngine RE;
	private HacktendoLinker HL;
	public ChangeMap(RenderEngine RE,HacktendoLinker HL){
		this.RE=RE;
		this.HL=HL;
	}
	
	public Object execute(ArrayList parameters) throws Exception{
		int id = (int)(Integer)((TypeInteger)parameters.get(0)).getRawValue();
		if(parameters.size()>1){
			int script = (int)(Integer)((TypeInteger)parameters.get(1)).getRawValue();
			RE.setMap(id,script);
		}
		else{
			RE.setMap(id);
		}
		throw(new Exception("ChangeMap"));
	}
}
