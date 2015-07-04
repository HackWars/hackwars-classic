package Game.ComputerFunctions;
import Game.*;
import Assignments.*;
/**
A function that is run within Computer.java.

This function causes an attack to be launched against one of an opponents default ports.
*/

import java.util.*;
import Assignments.*;
import Hackscript.Model.*;

public class requestAttackDefault extends function{
	public requestAttackDefault(Computer MyComputer){
		super(MyComputer);
	}
	
	public void execute(ApplicationData MyApplicationData){
		String target=(String)((Object[])MyApplicationData.getParameters())[1];
		Integer I[]=new Integer[]{new Integer(0)};
		String S[][]={null,null,null};
		Object O=null;
		if(target.equals("Bank"))
			O=new Object[]{super.getComputer().getIP(),super.getComputer().getDefaultBank(),I,S,null,0};
		if(target.equals("Attack"))
			O=new Object[]{super.getComputer().getIP(),super.getComputer().getDefaultAttack(),I,S,null,0};
		ApplicationData Test=new ApplicationData("requestattack",O,MyApplicationData.getPort(),MyApplicationData.getSourceIP());
		super.getComputer().getComputerHandler().addData(Test,MyApplicationData.getSourceIP());
	}
}
