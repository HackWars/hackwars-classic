package Hacktendo.Functions;


import java.util.ArrayList;
import Hackscript.Model.*;
import Hacktendo.*;

public class CreateSprite extends LinkerFunctions{

	private RenderEngine RE;
	private HacktendoLinker HL;
	public CreateSprite(RenderEngine RE,HacktendoLinker HL){
		this.RE=RE;
		this.HL=HL;
	}
	
	public Object execute(ArrayList parameters){
		if(!(RE instanceof ClientRenderEngine)){
		
			int image = (int)(Integer)((TypeInteger)parameters.get(0)).getRawValue();
			int script = (int)(Integer)((TypeInteger)parameters.get(1)).getRawValue();
			int x = (int)(Integer)((TypeInteger)parameters.get(2)).getRawValue();
			int y = (int)(Integer)((TypeInteger)parameters.get(3)).getRawValue();
			int z = (int)(Integer)((TypeInteger)parameters.get(4)).getRawValue();
			
			Sprite S=new Sprite(RE);
			S.setImageID(image);
			S.setScriptID(script);
			S.setX(x);
			S.setY(y);
			S.setZ(z);
					
			return(new TypeInteger(S.getSpriteID()));
		}
		return(new TypeInteger(0));
	}
}
