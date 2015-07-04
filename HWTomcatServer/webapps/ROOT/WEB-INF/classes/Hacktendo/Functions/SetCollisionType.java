package Hacktendo.Functions;


import java.util.ArrayList;
import Hackscript.Model.*;
import Hacktendo.*;

public class SetCollisionType extends LinkerFunctions{

	private RenderEngine RE;
	private HacktendoLinker HL;
	public SetCollisionType(RenderEngine RE,HacktendoLinker HL){
		this.RE=RE;
		this.HL=HL;
	}
	
	public Object execute(ArrayList parameters){
		if(parameters.size()>1){
			int id = (int)(Integer)((TypeInteger)parameters.get(0)).getRawValue();
			int val=0;
			val = (int)(Integer)((TypeInteger)parameters.get(1)).getRawValue();
			Sprite MySprite=RE.getSprite(id);
			if(MySprite!=null)
				MySprite.setCollisionType(val);
		}else{
			int val=0;
			int spriteID = HL.getSpriteID();
			val = (int)(Integer)((TypeInteger)parameters.get(0)).getRawValue();
			Sprite MySprite=RE.getSprite(spriteID);
			if(MySprite!=null)
				MySprite.setCollisionType(val);
		}
		return null;
	}
}
