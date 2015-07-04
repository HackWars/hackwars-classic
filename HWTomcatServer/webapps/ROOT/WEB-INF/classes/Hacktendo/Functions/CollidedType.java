package Hacktendo.Functions;


import java.util.ArrayList;
import Hackscript.Model.*;
import Hacktendo.*;

public class CollidedType extends LinkerFunctions{

	private RenderEngine RE;
	private HacktendoLinker HL;
	public CollidedType(RenderEngine RE,HacktendoLinker HL){
		this.RE=RE;
		this.HL=HL;
	}
	
	public Object execute(ArrayList parameters){
		int spriteID = HL.getSpriteID();
		Sprite S=RE.getSprite(spriteID);
		if(S==null)
			return(new TypeInteger(-1));
		return(new TypeInteger(S.getCollidedType()));
	}
}
