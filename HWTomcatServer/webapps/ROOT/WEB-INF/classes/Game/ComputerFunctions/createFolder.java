package Game.ComputerFunctions;
import Game.*;
import Assignments.*;
/**
A function that is run within Computer.java.

Used to create a folder in the player's file system.
*/

import java.util.*;
import Assignments.*;
import Hackscript.Model.*;

public class createFolder extends function{
	public createFolder(Computer MyComputer){
		super(MyComputer);
	}
	
	public void execute(ApplicationData MyApplicationData){
		super.getComputer().sendPacket();
		String directory=(String)MyApplicationData.getParameters();
		if(!super.getComputer().getFileSystem().addDirectory(directory))
			super.getComputer().addMessage(MessageHandler.SAVE_FAIL_HD_FULL);
		super.getComputer().getPacketAssignment().setRequestPrimary(true,1);
	}
}
