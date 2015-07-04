package Hacktendo.Functions;


import java.util.ArrayList;
import Hackscript.Model.*;
import Hacktendo.*;
import Game.MMO.*;

public class invalidate extends LinkerFunctions{

	private MMOEngine RE;
	private HacktendoLinker HL;
	private boolean initialized=false;
	
	public invalidate(RenderEngine RE,HacktendoLinker HL){
		if(RE instanceof MMOEngine){
			this.RE=(MMOEngine)RE;
			this.HL=HL;
			initialized=true;
		}
	}
	
	public Object execute(ArrayList parameters){
		if(initialized){
			RE.invalidate(HL.getSpriteID());
		}
					
		return null;
	}
}
