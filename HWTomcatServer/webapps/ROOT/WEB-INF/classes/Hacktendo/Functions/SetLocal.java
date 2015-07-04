package Hacktendo.Functions;


import java.util.ArrayList;
import Hackscript.Model.*;
import Hacktendo.*;

public class SetLocal extends LinkerFunctions{

	private RenderEngine RE;
	private HacktendoLinker HL;
	public SetLocal(RenderEngine RE,HacktendoLinker HL){
		this.RE=RE;
		this.HL=HL;
	}
	
	public Object execute(ArrayList parameters){
		
		if(parameters.size()>2){
				
		
			int id = (int)(Integer)((TypeInteger)parameters.get(0)).getRawValue();
			String key = (String)((TypeString)parameters.get(1)).getStringValue();
			
			Object val = parameters.get(2);
			
			Sprite MySprite=RE.getSprite(id);
			MySprite.setParameter(key,val);
		}else{
			int spriteID = HL.getSpriteID();
						
			
			String key = (String)((TypeString)parameters.get(0)).getStringValue();
			
			//System.out.println(spriteID+" "+key);

			
			Object val = parameters.get(1);
			Sprite MySprite=RE.getSprite(spriteID);
			MySprite.setParameter(key,val);
		}

		return null;
	}
}
