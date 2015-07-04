package Hacktendo.Functions;


import java.util.ArrayList;
import Hackscript.Model.*;
import Hacktendo.*;

public class DrawLine extends LinkerFunctions{

	private RenderEngine RE;
	private HacktendoLinker HL;
	public DrawLine(RenderEngine RE,HacktendoLinker HL){
		this.RE=RE;
		this.HL=HL;
	}
	
	public Object execute(ArrayList parameters){
		int x1 = (int)(Integer)((TypeInteger)parameters.get(0)).getRawValue();
		int y1 = (int)(Integer)((TypeInteger)parameters.get(1)).getRawValue();
		int x2 = (int)(Integer)((TypeInteger)parameters.get(2)).getRawValue();
		int y2 = (int)(Integer)((TypeInteger)parameters.get(3)).getRawValue();
		int r = (int)(Integer)((TypeInteger)parameters.get(4)).getRawValue();
		int g = (int)(Integer)((TypeInteger)parameters.get(5)).getRawValue();
		int b = (int)(Integer)((TypeInteger)parameters.get(6)).getRawValue();
		int a = (int)(Integer)((TypeInteger)parameters.get(7)).getRawValue();
		if(a<0)
			a=0;
		Object O[] = new Object[]{RenderEngine.LINE,new Integer(x1),new Integer(y1),new Integer(x2),new Integer(y2),new Integer(r),new Integer(g),new Integer(b),new Integer(a)};
		RE.addGraphics(O);
		return null;
	}
}
