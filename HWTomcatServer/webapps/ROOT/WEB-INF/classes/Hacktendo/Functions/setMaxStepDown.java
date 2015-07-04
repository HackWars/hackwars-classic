package Hacktendo.Functions;


import java.util.ArrayList;
import Hackscript.Model.*;
import Hacktendo.*;

public class setMaxStepDown extends LinkerFunctions{

	private RenderEngine RE;
	private HacktendoLinker HL;
	public setMaxStepDown(RenderEngine RE,HacktendoLinker HL){
		this.RE=RE;
		this.HL=HL;
	}
	
	public Object execute(ArrayList parameters){
		if(parameters.size()>1){
			int id = (int)(Integer)((TypeInteger)parameters.get(0)).getRawValue();
			int val=0;
			
			if(parameters.get(1) instanceof TypeFloat)
				val = (int)(float)(Float)((TypeFloat)parameters.get(1)).getRawValue();
			else if(parameters.get(1) instanceof TypeInteger)
				val = (Integer)((TypeInteger)parameters.get(1)).getRawValue();

			Sprite MySprite=RE.getSprite(id);
			MySprite.setMaxStepDown(val);
		}else{
			int val=0;
			int spriteID = HL.getSpriteID();

			if(parameters.get(0) instanceof TypeFloat)
				val = (int)(float)(Float)((TypeFloat)parameters.get(0)).getRawValue();
			else if(parameters.get(0) instanceof TypeInteger)
				val = (Integer)((TypeInteger)parameters.get(0)).getRawValue();

			Sprite MySprite=RE.getSprite(spriteID);
			MySprite.setMaxStepDown(val);
		}
		return null;
	}
}
