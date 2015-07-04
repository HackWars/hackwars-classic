package Hacktendo.Functions;


import java.util.ArrayList;
import Hackscript.Model.*;
import Hacktendo.*;

public class DrawArc extends LinkerFunctions{

	private RenderEngine RE;
	private HacktendoLinker HL;
	public DrawArc(RenderEngine RE,HacktendoLinker HL){
		this.RE=RE;
		this.HL=HL;
	}
	
	public Object execute(ArrayList parameters){
		int x = (int)(Integer)((TypeInteger)parameters.get(0)).getRawValue();
		int y = (int)(Integer)((TypeInteger)parameters.get(1)).getRawValue();
		int width = (int)(Integer)((TypeInteger)parameters.get(2)).getRawValue();
		int height = (int)(Integer)((TypeInteger)parameters.get(3)).getRawValue();
		int startAngle = (int)(Integer)((TypeInteger)parameters.get(4)).getRawValue();
		int endAngle = (int)(Integer)((TypeInteger)parameters.get(5)).getRawValue();
		int r = (int)(Integer)((TypeInteger)parameters.get(6)).getRawValue();
		int g = (int)(Integer)((TypeInteger)parameters.get(7)).getRawValue();
		int b = (int)(Integer)((TypeInteger)parameters.get(8)).getRawValue();
		int a = (int)(Integer)((TypeInteger)parameters.get(9)).getRawValue();
		if(a<0)
			a=0;
		Object O[] = new Object[]{RenderEngine.ARC,new Integer(x),new Integer(y),new Integer(width),new Integer(height),new Integer(startAngle),new Integer(endAngle),new Integer(r),new Integer(g),new Integer(b),new Integer(a)};
		RE.addGraphics(O);
		return null;
	}
}
