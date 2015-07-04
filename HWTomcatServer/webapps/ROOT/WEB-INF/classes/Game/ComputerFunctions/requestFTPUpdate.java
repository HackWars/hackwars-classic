package Game.ComputerFunctions;
import Game.*;
import Assignments.*;
/**
A function that is run within Computer.java.

This function requests that the client side pulls an update of its FTP directories.
*/

import java.util.*;
import Assignments.*;
import Hackscript.Model.*;

public class requestFTPUpdate extends function{
	public requestFTPUpdate(Computer MyComputer){
		super(MyComputer);
	}
	
	public void execute(ApplicationData MyApplicationData){
		PacketAssignment PA=super.getComputer().getPacketAssignment();
		PA.setRequestPrimary(true,8);
		PA.setRequestSecondary(true,8);
		super.getComputer().sendPacket();
	}
}
