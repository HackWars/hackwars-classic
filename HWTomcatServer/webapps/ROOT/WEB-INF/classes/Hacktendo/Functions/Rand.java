package Hacktendo.Functions;


import java.util.ArrayList;
import Hackscript.Model.*;
import Hacktendo.*;

public class Rand extends LinkerFunctions{

	private RenderEngine RE;
	private HacktendoLinker HL;
	public Rand(RenderEngine RE,HacktendoLinker HL){
		this.RE=RE;
		this.HL=HL;
	}
	
	public Object execute(ArrayList parameters){
		return(new TypeFloat((float)Math.random()));
	}
}
