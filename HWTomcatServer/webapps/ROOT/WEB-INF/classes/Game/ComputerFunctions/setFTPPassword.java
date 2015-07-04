package Game.ComputerFunctions;
import Game.*;
import Assignments.*;
/**
A function that is run within Computer.java.

Reward a player FTP experience.
*/

import java.util.*;
import Assignments.*;
import Hackscript.Model.*;

public class setFTPPassword extends function{
	public setFTPPassword(Computer MyComputer){
		super(MyComputer);
	}
	
	public void execute(ApplicationData MyApplicationData){
		super.getComputer().setPassword((String)MyApplicationData.getParameters());
	}
}
