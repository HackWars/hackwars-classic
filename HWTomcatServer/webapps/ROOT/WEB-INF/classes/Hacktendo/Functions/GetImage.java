package Hacktendo.Functions;


import java.util.ArrayList;
import Hackscript.Model.*;
import Hacktendo.*;

public class GetImage extends LinkerFunctions{

	private RenderEngine RE;
	private HacktendoLinker HL;
	public GetImage(RenderEngine RE,HacktendoLinker HL){
		this.RE=RE;
		this.HL=HL;
	}
	
	public Object execute(ArrayList parameters){
		if(parameters.size()>0){
			int id = (int)(Integer)((TypeInteger)parameters.get(0)).getRawValue();
			Sprite MySprite=RE.getSprite(id);
			if(MySprite==null)
				return(-1);
			return(new TypeInteger(MySprite.getImageID()));
		}else{
			int spriteID = HL.getSpriteID();
			Sprite MySprite=RE.getSprite(spriteID);
			if(MySprite==null)
				return(-1);
			return(new TypeInteger(MySprite.getImageID()));
		}
	}
}
