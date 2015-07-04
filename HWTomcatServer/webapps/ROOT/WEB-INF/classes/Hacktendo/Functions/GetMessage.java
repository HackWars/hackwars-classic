package Hacktendo.Functions;


import java.util.ArrayList;
import Hackscript.Model.*;
import Hacktendo.*;

public class GetMessage extends LinkerFunctions{

	private RenderEngine RE;
	private HacktendoLinker HL;
	public GetMessage(RenderEngine RE,HacktendoLinker HL){
		this.RE=RE;
		this.HL=HL;
	}
	
	public Object execute(ArrayList parameters){
		int spriteID = HL.getSpriteID();
		Sprite MySprite=RE.getSprite(spriteID);
		if(MySprite==null)
			return(new TypeString(""));
		
		Object O=MySprite.getMessage();
		return O;
	}
}
