package Hacktendo.Functions;


import java.util.ArrayList;
import Hackscript.Model.*;
import Hacktendo.*;

public class setCollideWithWater extends LinkerFunctions{

	private RenderEngine RE;
	private HacktendoLinker HL;
	public setCollideWithWater(RenderEngine RE,HacktendoLinker HL){
		this.RE=RE;
		this.HL=HL;
	}
	
	public Object execute(ArrayList parameters){
		if(parameters.size()>1){
			int id = (int)(Integer)((TypeInteger)parameters.get(0)).getRawValue();
			boolean val=false;

			if(parameters.get(1) instanceof TypeBoolean)
				val = (Boolean)((TypeBoolean)parameters.get(1)).getRawValue();
			Sprite MySprite=RE.getSprite(id);
			MySprite.setCollideWithWater(val);
		}else{
			boolean val=false;
			int spriteID = HL.getSpriteID();

			if(parameters.get(0) instanceof TypeBoolean)
				val = (Boolean)((TypeBoolean)parameters.get(0)).getRawValue();
				
			Sprite MySprite=RE.getSprite(spriteID);
			MySprite.setCollideWithWater(val);
		}
		return null;
	}
}
