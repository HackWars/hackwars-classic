package Hacktendo.Functions;


import java.util.ArrayList;
import Hackscript.Model.*;
import Hacktendo.*;

public class setMask extends LinkerFunctions{

	private RenderEngine RE;
	private HacktendoLinker HL;
	public setMask(RenderEngine RE,HacktendoLinker HL){
		this.RE=RE;
		this.HL=HL;
	}
	
	public Object execute(ArrayList parameters){
		if(parameters.size()>4){
			int id = (int)(Integer)((TypeInteger)parameters.get(0)).getRawValue();
			int r = (int)(Integer)((TypeInteger)parameters.get(1)).getRawValue();
			int g = (int)(Integer)((TypeInteger)parameters.get(2)).getRawValue();
			int b = (int)(Integer)((TypeInteger)parameters.get(3)).getRawValue();
			int a = (int)(Integer)((TypeInteger)parameters.get(4)).getRawValue();

			Sprite MySprite=RE.getSprite(id);
			MySprite.setMask(r,g,b,a);
		}else{
			int val=0;
			int spriteID = HL.getSpriteID();
			int r = (int)(Integer)((TypeInteger)parameters.get(0)).getRawValue();
			int g = (int)(Integer)((TypeInteger)parameters.get(1)).getRawValue();
			int b = (int)(Integer)((TypeInteger)parameters.get(2)).getRawValue();
			int a = (int)(Integer)((TypeInteger)parameters.get(3)).getRawValue();

			Sprite MySprite=RE.getSprite(spriteID);
			MySprite.setMask(r,g,b,a);
		}
		return null;
	}
}
