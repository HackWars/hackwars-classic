package Hacktendo.Functions;


import java.util.ArrayList;
import Hackscript.Model.*;
import Hacktendo.*;

public class DrawTextWorld extends LinkerFunctions{

	private OpenGLRenderEngine RE;
	private HacktendoLinker HL;
	public DrawTextWorld(RenderEngine RE,HacktendoLinker HL){
		this.RE=(OpenGLRenderEngine)RE;
		this.HL=HL;
	}
	
	public Object execute(ArrayList parameters){
		String text,face;
		int size,x,y,z,r,g,b,a;
	
		if(parameters.size()==9){
			text = (String)((TypeString)parameters.get(0)).getStringValue();
			face = (String)((TypeString)parameters.get(1)).getStringValue();
			size = (int)(Integer)((TypeInteger)parameters.get(2)).getRawValue();
			x = (int)(Integer)((TypeInteger)parameters.get(3)).getRawValue();
			y = (int)(Integer)((TypeInteger)parameters.get(4)).getRawValue();
			r = (int)(Integer)((TypeInteger)parameters.get(5)).getRawValue();
			g = (int)(Integer)((TypeInteger)parameters.get(6)).getRawValue();
			b = (int)(Integer)((TypeInteger)parameters.get(7)).getRawValue();
			a = (int)(Integer)((TypeInteger)parameters.get(8)).getRawValue();
			z=-48;
		}else{
			text = (String)((TypeString)parameters.get(0)).getStringValue();
			face = (String)((TypeString)parameters.get(1)).getStringValue();
			size = (int)(Integer)((TypeInteger)parameters.get(2)).getRawValue();
			x = (int)(Integer)((TypeInteger)parameters.get(3)).getRawValue();
			y = (int)(Integer)((TypeInteger)parameters.get(4)).getRawValue();
			z=(int)(Integer)((TypeInteger)parameters.get(5)).getRawValue();
			r = (int)(Integer)((TypeInteger)parameters.get(6)).getRawValue();
			g = (int)(Integer)((TypeInteger)parameters.get(7)).getRawValue();
			b = (int)(Integer)((TypeInteger)parameters.get(8)).getRawValue();
			a = (int)(Integer)((TypeInteger)parameters.get(9)).getRawValue();
			z=z*-1;
		}
				
		if(a<0)
			a=0;
			
		Object O[] = new Object[]{OpenGLRenderEngine.TEXT_WORLD,text,face,new Integer(size),new Integer(x),new Integer(y),new Integer(r),new Integer(g),new Integer(b),new Integer(a),new Integer(z)};
		RE.addGraphics(O);
		
		return null;
	}
}
