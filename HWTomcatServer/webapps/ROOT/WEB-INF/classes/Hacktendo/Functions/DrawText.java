package Hacktendo.Functions;


import java.util.ArrayList;
import Hackscript.Model.*;
import Hacktendo.*;

public class DrawText extends LinkerFunctions{

	private RenderEngine RE;
	private HacktendoLinker HL;
	public DrawText(RenderEngine RE,HacktendoLinker HL){
		this.RE=RE;
		this.HL=HL;
	}
	
	public Object execute(ArrayList parameters){
		String text = (String)((TypeString)parameters.get(0)).getStringValue();
		String face = (String)((TypeString)parameters.get(1)).getStringValue();
		int size = (int)(Integer)((TypeInteger)parameters.get(2)).getRawValue();
		int x = (int)(Integer)((TypeInteger)parameters.get(3)).getRawValue();
		int y = (int)(Integer)((TypeInteger)parameters.get(4)).getRawValue();
		int r = (int)(Integer)((TypeInteger)parameters.get(5)).getRawValue();
		int g = (int)(Integer)((TypeInteger)parameters.get(6)).getRawValue();
		int b = (int)(Integer)((TypeInteger)parameters.get(7)).getRawValue();
		int a = (int)(Integer)((TypeInteger)parameters.get(8)).getRawValue();
		if(a<0)
			a=0;
		Object O[] = new Object[]{RenderEngine.TEXT,text,face,new Integer(size),new Integer(x),new Integer(y),new Integer(r),new Integer(g),new Integer(b),new Integer(a)};
		RE.addGraphics(O);
		return null;
	}
}
