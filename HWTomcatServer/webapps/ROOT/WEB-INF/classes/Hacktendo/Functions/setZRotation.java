package Hacktendo.Functions;


import java.util.ArrayList;
import Hackscript.Model.*;
import Hacktendo.*;

public class setZRotation extends LinkerFunctions{

	private RenderEngine RE;
	private HacktendoLinker HL;
	public setZRotation(RenderEngine RE,HacktendoLinker HL){
		this.RE=RE;
		this.HL=HL;
	}
	
	public Object execute(ArrayList parameters){
		if(parameters.size()>1){
			int id = (int)(Integer)((TypeInteger)parameters.get(0)).getRawValue();
			float val=0;

			if(parameters.get(1) instanceof TypeFloat)
				val = (float)(Float)((TypeFloat)parameters.get(1)).getRawValue();
			else if(parameters.get(1) instanceof TypeInteger)
				val = (float)(int)(Integer)((TypeInteger)parameters.get(1)).getRawValue();

			Sprite MySprite=RE.getSprite(id);
			MySprite.setZRotation(val);
		}else{
			float val=0;
			int spriteID = HL.getSpriteID();

			if(parameters.get(0) instanceof TypeFloat)
				val = (float)(Float)((TypeFloat)parameters.get(0)).getRawValue();
			else if(parameters.get(0) instanceof TypeInteger)
				val = (float)(int)(Integer)((TypeInteger)parameters.get(0)).getRawValue();

			Sprite MySprite=RE.getSprite(spriteID);
			MySprite.setZRotation(val);
		}
		return null;
	}
}
