package Hacktendo.Functions;


import java.util.ArrayList;
import Hackscript.Model.*;
import Hacktendo.*;

public class NextMessage extends LinkerFunctions{

	private RenderEngine RE;
	private HacktendoLinker HL;
	public NextMessage(RenderEngine RE,HacktendoLinker HL){
		this.RE=RE;
		this.HL=HL;
	}
	
	public Object execute(ArrayList parameters){
		int spriteID = HL.getSpriteID();
		Sprite MySprite=RE.getSprite(spriteID);
		MySprite.nextMessage();
		return null;
	}
}
