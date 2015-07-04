package Hacktendo.Functions;


import java.util.ArrayList;
import Hackscript.Model.*;
import Hacktendo.*;

public class setOffscreenProcessing extends LinkerFunctions{

	private RenderEngine RE;
	private HacktendoLinker HL;
	public setOffscreenProcessing(RenderEngine RE,HacktendoLinker HL){
		this.RE=RE;
		this.HL=HL;
	}
	
	public Object execute(ArrayList parameters){

		if(parameters.size()>1){
			int id = (int)(Integer)((TypeInteger)parameters.get(0)).getRawValue();
			boolean val=true;
			val = (boolean)(Boolean)((TypeBoolean)parameters.get(1)).getRawValue();
			Sprite MySprite=RE.getSprite(id);
						
			if(MySprite!=null)
				MySprite.setOffscreenProcessing(val);
		}else{
			
			boolean val=true;
			int spriteID = HL.getSpriteID();
			val = (boolean)(Boolean)((TypeBoolean)parameters.get(0)).getRawValue();
			Sprite MySprite=RE.getSprite(spriteID);
			MySprite.setOffscreenProcessing(val);

		}
	
		return null;
	}
}
