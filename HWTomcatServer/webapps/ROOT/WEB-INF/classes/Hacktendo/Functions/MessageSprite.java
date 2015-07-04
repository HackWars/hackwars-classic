package Hacktendo.Functions;


import java.util.ArrayList;
import Hackscript.Model.*;
import Hacktendo.*;

public class MessageSprite extends LinkerFunctions{

	private RenderEngine RE;
	private HacktendoLinker HL;
	public MessageSprite(RenderEngine RE,HacktendoLinker HL){
		this.RE=RE;
		this.HL=HL;
	}
	
	public Object execute(ArrayList parameters){
		int id = (int)(Integer)((TypeInteger)parameters.get(0)).getRawValue();
		Sprite MySprite=RE.getSprite(id);
		if(MySprite!=null)
		for(int i=1;i<parameters.size();i+=2){
			String flag=(String)((TypeString)parameters.get(i)).getStringValue();
			Object O=parameters.get(i+1);
			MySprite.setMessage(flag,O);
		}
		return null;
	}
}
