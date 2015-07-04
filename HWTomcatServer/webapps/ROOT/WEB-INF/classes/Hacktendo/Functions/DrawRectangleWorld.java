package Hacktendo.Functions;


import java.util.ArrayList;
import Hackscript.Model.*;
import Hacktendo.*;

public class DrawRectangleWorld extends LinkerFunctions{

	private OpenGLRenderEngine RE;
	private HacktendoLinker HL;
	public DrawRectangleWorld(RenderEngine RE,HacktendoLinker HL){
		this.RE=(OpenGLRenderEngine)RE;
		this.HL=HL;
	}
	
	public Object execute(ArrayList parameters){
		int x,y,z=-48,width,height,r,g,b,a;
	
		if(parameters.size()==8){
			x = (int)(Integer)((TypeInteger)parameters.get(0)).getRawValue();
			y = (int)(Integer)((TypeInteger)parameters.get(1)).getRawValue();
			width = (int)(Integer)((TypeInteger)parameters.get(2)).getRawValue();
			height = (int)(Integer)((TypeInteger)parameters.get(3)).getRawValue();
			r = (int)(Integer)((TypeInteger)parameters.get(4)).getRawValue();
			g = (int)(Integer)((TypeInteger)parameters.get(5)).getRawValue();
			b = (int)(Integer)((TypeInteger)parameters.get(6)).getRawValue();
			a = (int)(Integer)((TypeInteger)parameters.get(7)).getRawValue();
		}else{
			x = (int)(Integer)((TypeInteger)parameters.get(0)).getRawValue();
			y = (int)(Integer)((TypeInteger)parameters.get(1)).getRawValue();
			z = -1*(int)(Integer)((TypeInteger)parameters.get(2)).getRawValue();
			width = (int)(Integer)((TypeInteger)parameters.get(3)).getRawValue();
			height = (int)(Integer)((TypeInteger)parameters.get(4)).getRawValue();
			r = (int)(Integer)((TypeInteger)parameters.get(5)).getRawValue();
			g = (int)(Integer)((TypeInteger)parameters.get(6)).getRawValue();
			b = (int)(Integer)((TypeInteger)parameters.get(7)).getRawValue();
			a = (int)(Integer)((TypeInteger)parameters.get(8)).getRawValue();
		}
		
		if(a<0)
			a=0;
		Object O[] = new Object[]{OpenGLRenderEngine.RECTANGLE_WORLD,new Integer(x),new Integer(y),new Integer(width),new Integer(height),new Integer(r),new Integer(g),new Integer(b),new Integer(a),new Integer(z)};
		RE.addGraphics(O);
		return null;
	}
}
