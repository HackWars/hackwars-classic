package Game.ComputerFunctions;
import Game.*;
/**
Represents a watch which will fire given an appropriate event in the computer.
*/

import java.util.*;
import Assignments.*;
import Hackscript.Model.*;
import Assignments.*;

public abstract class function{
	private Computer MyComputer=null;
	
	public function(Computer MyComputer){
		this.MyComputer=MyComputer;
	}
	
	public Computer getComputer(){
		return(MyComputer);
	}
	
	public abstract void execute(ApplicationData MyApplicationData);
}
