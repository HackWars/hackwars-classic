package Hacktendo.Functions;


import java.util.ArrayList;
import Hackscript.Model.*;
import Hacktendo.*;
import util.*;

public class CheckTimeStamp extends LinkerFunctions{

	private RenderEngine RE;
	private HacktendoLinker HL;
	public CheckTimeStamp(RenderEngine RE,HacktendoLinker HL){
		this.RE=RE;
		this.HL=HL;
	}
	
	public Object execute(ArrayList parameters){
	String key = (String)((TypeString)parameters.get(0)).getStringValue();
	long timerValue=HL.checkTimer(key);
	return(new TypeInteger((int)(Time.getInstance().getCurrentTime()-timerValue)));
	}
}
