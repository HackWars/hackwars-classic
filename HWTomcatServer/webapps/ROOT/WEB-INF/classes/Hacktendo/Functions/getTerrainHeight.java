package Hacktendo.Functions;


import java.util.ArrayList;
import Hackscript.Model.*;
import Hacktendo.*;

public class getTerrainHeight extends LinkerFunctions{

	private RenderEngine RE;
	private HacktendoLinker HL;
	public getTerrainHeight(RenderEngine RE,HacktendoLinker HL){
		this.RE=RE;
		this.HL=HL;
	}
	
	public Object execute(ArrayList parameters){
		if(parameters.size()>0){
			int id = (int)(Integer)((TypeInteger)parameters.get(0)).getRawValue();
			Sprite MySprite=RE.getSprite(id);
			return(new TypeInteger(MySprite.getTerrainHeight()));
		}else{
			boolean val=false;
			int spriteID = HL.getSpriteID();
			Sprite MySprite=RE.getSprite(spriteID);
			return(new TypeInteger(MySprite.getTerrainHeight()));
		}
	}
}
