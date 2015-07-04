package Hacktendo.Functions;

import java.util.ArrayList;
import Hackscript.Model.*;
import Hacktendo.*;
import java.util.*;

public class Load extends LinkerFunctions{

	private RenderEngine RE;
	private HacktendoLinker HL;
	public Load(RenderEngine RE,HacktendoLinker HL){
		this.RE=RE;
		this.HL=HL;
	}
	
	public Object execute(ArrayList parameters){
		String key = (String)((TypeString)parameters.get(0)).getStringValue();
		HashMap LoadFile=RE.getLoadFile();
		if(LoadFile!=null)
			return(LoadFile.get(key));
		return(new TypeInteger(0));
	}
}
