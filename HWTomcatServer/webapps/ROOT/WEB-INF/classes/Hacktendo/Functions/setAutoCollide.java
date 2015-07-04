package Hacktendo.Functions;


import java.util.ArrayList;
import Hackscript.Model.*;
import Hacktendo.*;

public class setAutoCollide extends LinkerFunctions{

	private RenderEngine RE;
	private HacktendoLinker HL;
	public setAutoCollide(RenderEngine RE,HacktendoLinker HL){
		this.RE=RE;
		this.HL=HL;
	}
	
	public Object execute(ArrayList parameters){
		if(parameters.size()>1){
			int id = (int)(Integer)((TypeInteger)parameters.get(0)).getRawValue();
			boolean val=false;
			val = (boolean)(Boolean)((TypeBoolean)parameters.get(1)).getRawValue();
			Sprite MySprite=RE.getSprite(id);
			MySprite.setAutoCollide(val);
		}else{
			boolean val=false;
			int spriteID = HL.getSpriteID();
			val = (boolean)(Boolean)((TypeBoolean)parameters.get(0)).getRawValue();
			Sprite MySprite=RE.getSprite(spriteID);
			MySprite.setAutoCollide(val);
		}
		return null;
	}
}
