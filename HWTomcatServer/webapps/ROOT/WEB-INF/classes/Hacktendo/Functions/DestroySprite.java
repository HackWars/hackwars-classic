package Hacktendo.Functions;


import java.util.ArrayList;
import Hackscript.Model.*;
import Hacktendo.*;

public class DestroySprite extends LinkerFunctions{

	private RenderEngine RE;
	private HacktendoLinker HL;
	public DestroySprite(RenderEngine RE,HacktendoLinker HL){
		this.RE=RE;
		this.HL=HL;
	}
	
	public Object execute(ArrayList parameters){
		if(parameters.size()==1){
			int id = (int)(Integer)((TypeInteger)parameters.get(0)).getRawValue();
			Sprite S=RE.getSprite(id);
			S.setInactive(true);
			RE.deleteSprite(S,id,S.getZ(),S.getRenderType());
		}else{ 
			int spriteID = HL.getSpriteID();
			Sprite S=RE.getSprite(spriteID);
			S.setInactive(true);
			RE.deleteSprite(S,spriteID,S.getZ(),S.getRenderType());
		}
		return null;
	}
}
