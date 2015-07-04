package Hacktendo.Functions;


import java.util.ArrayList;
import Hackscript.Model.*;
import Hacktendo.*;

public class GetFlag extends LinkerFunctions{

	private RenderEngine RE;
	private HacktendoLinker HL;
	public GetFlag(RenderEngine RE,HacktendoLinker HL){
		this.RE=RE;
		this.HL=HL;
	}
	
	public Object execute(ArrayList parameters){
		int spriteID = HL.getSpriteID();
		Sprite MySprite=RE.getSprite(spriteID);
		if(MySprite==null)
			return(new TypeString(""));
		return(new TypeString(MySprite.getFlag()));
	}
}
