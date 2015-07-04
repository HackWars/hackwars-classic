package Hacktendo.Functions;


import java.util.ArrayList;
import Hackscript.Model.*;
import Hacktendo.*;

public class GetMessageCount extends LinkerFunctions{

	private RenderEngine RE;
	private HacktendoLinker HL;
	public GetMessageCount(RenderEngine RE,HacktendoLinker HL){
		this.RE=RE;
		this.HL=HL;
	}
	
	public Object execute(ArrayList parameters){
		int spriteID = HL.getSpriteID();
		Sprite MySprite=RE.getSprite(spriteID);
		if(MySprite==null)
			return(0);
		return(new TypeInteger(MySprite.getMessageCount()));
	}
}
