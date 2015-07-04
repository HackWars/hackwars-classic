package Game.ComputerFunctions;
import Game.*;
import Assignments.*;
/**
A function that is run within Computer.java.

Used to delete a folder from the player's file system.
*/

import java.util.*;
import Assignments.*;
import Hackscript.Model.*;

public class deleteFolder extends function{
	public deleteFolder(Computer MyComputer){
		super(MyComputer);
	}
	
	public void execute(ApplicationData MyApplicationData){
		super.getComputer().sendPacket();
		String directory=(String)MyApplicationData.getParameters();
		if(!super.getComputer().getFileSystem().deleteDirectory(directory)) {
			super.getComputer().addMessage(MessageHandler.DELETE_FAIL_NON_EMPTY_FOLDER);
        }
		super.getComputer().getPacketAssignment().setRequestPrimary(true,1);
	}
}
