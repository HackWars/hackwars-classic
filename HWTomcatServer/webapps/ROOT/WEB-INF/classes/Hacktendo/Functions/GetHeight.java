package Hacktendo.Functions;


import java.util.ArrayList;
import Hackscript.Model.*;
import Hacktendo.*;

public class GetHeight extends LinkerFunctions{

	private RenderEngine RE;
	private HacktendoLinker HL;
	public GetHeight(RenderEngine RE,HacktendoLinker HL){
		this.RE=RE;
		this.HL=HL;
	}
	
	public Object execute(ArrayList parameters){
		if(parameters.size()>0){
			int id = (int)(Integer)((TypeInteger)parameters.get(0)).getRawValue();
			Sprite MySprite=RE.getSprite(id);
			int height = MySprite.getHeight();
			return(new TypeInteger(height));
			
		}else{
			int spriteID = HL.getSpriteID();
			Sprite MySprite=RE.getSprite(spriteID);
			int height = MySprite.getHeight();
			return(new TypeInteger(height));
		}
	}
}
