package GUI;

import java.util.ArrayList;
import Hackscript.Model.*;
import java.util.*;


public class BashLinker extends Linker{
	
	private Object theLinker = null;
	private Hacker MyHacker;

	public BashLinker(Hacker MyHacker){
		this.MyHacker=MyHacker;
		
	}
	
	public Variable runFunction(String name, ArrayList parameters){
		
		if(name.equals("openAttackWindow")){
			try{
				int port = (Integer)((TypeInteger)parameters.get(0)).getRawValue();
				int x = (Integer)((TypeInteger)parameters.get(1)).getRawValue();
				int y = (Integer)((TypeInteger)parameters.get(2)).getRawValue();
				if(parameters.size()==3){
					MyHacker.showAttack(port,x,y);
				}
				else if(parameters.size()==5){
					String ip = ((TypeString)parameters.get(3)).getStringValue();
					int targetPort = (Integer)((TypeInteger)parameters.get(4)).getRawValue();
					MyHacker.showAttack(port,x,y,ip,targetPort);
				}
				else{
					MyHacker.showMessage("openAttackWindow() used incorrectly");
				}
			}catch(Exception e){
				e.printStackTrace();
				MyHacker.showMessage("Exception in openAttackWindow()");
			}
		}
		else if(name.equals("openPortManagement")){
			try{
				int x = (Integer)((TypeInteger)parameters.get(0)).getRawValue();
				int y = (Integer)((TypeInteger)parameters.get(1)).getRawValue();
				MyHacker.startPortManagement(x,y);
			}catch(Exception e){
				MyHacker.showMessage("Exception in openPortManagement()");
			}
		}
		else if(name.equals("startAttack")){
			try{
				int port = (Integer)((TypeInteger)parameters.get(0)).getRawValue();
				MyHacker.beginAttack(port);
			}catch(Exception e){
				e.printStackTrace();
				MyHacker.showMessage("Exception in startAttack()");
			}
		}
		else if(name.equals("openRedirectWindow")){
			try{
				int port = (Integer)((TypeInteger)parameters.get(0)).getRawValue();
				int x = (Integer)((TypeInteger)parameters.get(1)).getRawValue();
				int y = (Integer)((TypeInteger)parameters.get(2)).getRawValue();
				if(parameters.size()==3){
					MyHacker.showRedirect(port,x,y);
				}
				else if(parameters.size()==5){
					String ip = ((TypeString)parameters.get(3)).getStringValue();
					int targetPort = (Integer)((TypeInteger)parameters.get(4)).getRawValue();
					MyHacker.showRedirect(port,x,y,ip,targetPort);
				}
				else{
					MyHacker.showMessage("openRedirectWindow() used incorrectly");
				}
			}catch(Exception e){
				e.printStackTrace();
				MyHacker.showMessage("Exception in openRedirectWindow()");
			}
		}
		else if(name.equals("startRedirect")){
			try{
				int port = (Integer)((TypeInteger)parameters.get(0)).getRawValue();
				MyHacker.beginRedirect(port);
			}catch(Exception e){
				e.printStackTrace();
				MyHacker.showMessage("Exception in startRedirect()");
			}
		}
		else if(name.equals("turnOnPort")){
			int port = (Integer)((TypeInteger)parameters.get(0)).getRawValue();
			MyHacker.portOnOff(port,true);
		}
		else if(name.equals("turnOffPort")){
			int port = (Integer)((TypeInteger)parameters.get(0)).getRawValue();
			MyHacker.portOnOff(port,false);
		}
		else if(name.equals("turnOnWatch")){
			int watch = (Integer)((TypeInteger)parameters.get(0)).getRawValue();
			MyHacker.watchOnOff(watch,true);
		}
		else if(name.equals("turnOffWatch")){
			int watch = (Integer)((TypeInteger)parameters.get(0)).getRawValue();
			MyHacker.watchOnOff(watch,false);
		}
		else if(name.equals("length")){
			if(parameters.get(0)!=null){
				if(parameters.get(0) instanceof TypeArray)
					return( new TypeInteger(( (ArrayList) ( (TypeArray) parameters.get(0) ).getRawValue()).size()));
				ArrayList Temp=(ArrayList)parameters.get(0);
				return(new TypeInteger(Temp.size()));
			}else
				return(new TypeInteger(0));
		}
		else if(name.equals("message")){
			String message = ((TypeString)parameters.get(0)).getStringValue();
			MyHacker.showMessage(message);
		}
		
		return null;
	}


}
