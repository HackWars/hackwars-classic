/*
 * structInfo.java
 *
 * Created on March 10, 2007, 10:40 AM
 *
 * By Alexander T Morrsion
 *
 */

package HackerLogin;
import Game.*;
import java.util.ArrayList;
import Hackscript.Model.*;
import java.util.*;

/**
 * By Alexander Morrison
 */
public class HackerLinker extends Linker{
    
    public int codeCounter = 0;
    private ComputerHandler MyComputerHandler=null;
	private Program MyProgram=null;
    private Object theLinker = null;
	private HashMap SpamTracker=new HashMap();
    private static final int MAX_USES_PER_SCRIPT = 36;
    
    /** Creates a new instance of structInfo */
    public HackerLinker(Program MyProgram,ComputerHandler MyComputerHandler) {
		this.MyProgram=MyProgram;
		this.MyComputerHandler=MyComputerHandler;
        codeCounter = 0;
    }
		
	//incrementValue the spamming counter.
	public int incrementValue(String key){
		return(0);
		/*
		Integer Counter=(Integer)SpamTracker.get(key);
		int value=0;
		if(Counter==null){
			SpamTracker.put(key,new Integer(1));
			value=1;
		}else{
			value=Counter.intValue()+1;
			SpamTracker.remove(key);
			SpamTracker.put(key,value);
		}
		return(value);*/
	}
    
    private void checkMaxUsage(String function) {
        if(incrementValue(function)>36)
            MyProgram.setError("You cannot use a single function more than 36 times in one script: " + function);
    }
    
    public Variable runFunction(String name, ArrayList parameters){
		Object result=null;
		try{
			
			/**
			Our Banking API.
			*/
			if(MyProgram instanceof Banking){
				boolean isDeposit=false;
				if(name.equals("lowerDeposit")){
					isDeposit=true;
				}
				if(name.equals("mediumDeposit")){
					isDeposit=true;
				}
				if(name.equals("higherDeposit")){
					isDeposit=true;
				}
				
				if(name.equals("greaterDeposit")){
					isDeposit=true;
				}
				
				//The scripter is using a function that deposits into a user's bank account.
				if(isDeposit){
                    checkMaxUsage("deposit");
				
					if(parameters.size()!=2)
						MyProgram.setError("Deposits take the IP address being targeted followed by a Float representing the amount to deposit.");
					else if(!(parameters.get(0) instanceof TypeString)||!(parameters.get(1) instanceof TypeFloat))
						MyProgram.setError("Deposits take the IP address being targeted followed by a Float representing the amount to deposit.");
				}
				
				//Withdraw money from your account.
				if(name.equals("withdraw")){
                    checkMaxUsage("withdraw");
	
					if(parameters.size()!=2)
						MyProgram.setError("Withdraws take the IP address being targeted followed by a Float representing the amount to deposit.");
					else if(!(parameters.get(0) instanceof TypeString)||!(parameters.get(1) instanceof TypeFloat))
						MyProgram.setError("Withdraws take the IP address being targeted followed by a Float representing the amount to deposit.");
				}
				
				boolean isTransfer=false;
				//Transfer function.
				if(name.equals("lowerTransfer")){
					isTransfer=true;
				}
				if(name.equals("mediumTransfer")){
					isTransfer=true;
				}
				if(name.equals("higherTransfer")){
					isTransfer=true;
				}
				if(name.equals("greaterTransfer")){
					isTransfer=true;
				}
								
				//User is using a function that transfers money to another user.
				if(isTransfer){
                    checkMaxUsage("transfer");
	
					if(parameters.size()!=2)
						MyProgram.setError("Transfers take the IP address being targeted followed by a Float representing the amount to deposit.");
					else if(!(parameters.get(0) instanceof TypeString)||!(parameters.get(1) instanceof TypeFloat))
						MyProgram.setError("Transfers take the IP address being targeted followed by a Float representing the amount to deposit.");
				}
				
				//Return the amount of money that has been requested to transfer.
				if(name.equals("getAmount")){
                    checkMaxUsage("getAmount");
				
					if(parameters.size()!=0)
						MyProgram.setError("\"getAmount\" takes no parameters and returns the current legitimate dollar amount that a player has provided for a given transaction.");
					return(new TypeFloat(2.0f));
				}
				
		/**
		Our Watch API.
		*/
		}else if(MyProgram instanceof WatchProgram){
				WatchProgram W=(WatchProgram)MyProgram;
				
				//Gets the default attack associated with this computer.
				//Initialize an attack on the default attack port.
				if(name.equals("getDefaultAttack")){
                    checkMaxUsage("getDefaultAttack");
				
					if(parameters.size()!=0)
						MyProgram.setError("\"getDefaultAttack()\" takes no inputs and returns the default attacking port associated with the computer.");
					return(new TypeInteger(0));
				}else
				
				if(name.equals("sendEmail")){
                    checkMaxUsage("sendEmail");
			
					if(parameters.size()!=1)
						MyProgram.setError("\"sendEmail\" takes a string message as input.");
					else if(!(parameters.get(0) instanceof TypeString))
						MyProgram.setError("\"sendEmail\" takes a string message as input.");
				}else
				
				if(name.equals("sendFacebookMessage")){
                    checkMaxUsage("sendFacebookMessage");
			
					if(parameters.size()!=1)
						MyProgram.setError("\"sendFacebookMessage\" takes a string message as input.");
					else if(!(parameters.get(0) instanceof TypeString))
						MyProgram.setError("\"sendFacebookMessage\" takes a string message as input.");
				}else
				
				//Gets the default attack associated with this computer.
				//Initialize an attack on the default attack port.
				if(name.equals("getDefaultFTP")){
                    checkMaxUsage("getDefaultFTP");

					if(parameters.size()!=0)
						MyProgram.setError("\"getDefaultFTP()\" takes no inputs and returns the default FTP port associated with the computer.");
					return(new TypeInteger(0));
				}else
				
				//Gets the default attack associated with this computer.
				//Initialize an attack on the default attack port.
				if(name.equals("getDefaultHTTP")){
                    checkMaxUsage("getDefaultHTTP");
                    
					if(parameters.size()!=0)
						MyProgram.setError("\"getDefaultHTTP()\" takes no inputs and returns the default HTTP port associated with the computer.");
					return(new TypeInteger(0));
				}else
				
				//Gets the default attack associated with this computer.
				//Initialize an attack on the default attack port.
				if(name.equals("getTransactionAmount")){

					if(incrementValue("getTransactionAmount")>36)
						MyProgram.setError("You used a single function more than 36 times in one script.");
				
					if(parameters.size()!=0)
						MyProgram.setError("\"getTransactionAmount()\" returns a float representing the amount that caused a petty cash watch to fire.");
						
					return(new TypeFloat(0.0f));
				}else
				
				//Initialize an attack on the default attack port.
				if(name.equals("getCPULoad")){

					if(incrementValue("getCPULoad")>36)
						MyProgram.setError("You used a single function more than 36 times in one script.");
				
					if(parameters.size()!=0)
						MyProgram.setError("\"getCPULoad()\" returns the current CPU load.");
						
					return(new TypeFloat(0.0f));
				}else
				
				//Initialize an attack on the default attack port.
				if(name.equals("getMaximumCPULoad")){

					if(incrementValue("getMaximumCPULoad")>36)
						MyProgram.setError("You used a single function more than 36 times in one script.");
				
					if(parameters.size()!=0)
						MyProgram.setError("\"getCPULoad()\" returns the maximum CPU load.");
						
					return(new TypeFloat(0.0f));
				}else
				
				//Initialize an attack on the default attack port.
				if(name.equals("isTriggered")){

					if(incrementValue("isTriggered")>36)
						MyProgram.setError("You used a single function more than 36 times in one script.");
				
					if(parameters.size()!=0)
						MyProgram.setError("\"isTriggered()\" returns whether this watch fired due to triggerWatch().");
						
					return(new TypeBoolean(false));
				}else
				
				//Gets the default attack associated with this computer.
				//Initialize an attack on the default attack port.
				if(name.equals("getPort")){

					if(incrementValue("getPort")>36)
						MyProgram.setError("You used a single function more than 36 times in one script.");
				
					if(parameters.size()!=0)
						MyProgram.setError("\"getPort()\" takes no input and returns the port that a watch is associated with.");
					return(new TypeInteger(0));
				}else
				
				//Cancel the current attack taking place.
				if(name.equals("cancelAttack")){

					if(incrementValue("cancelAttack")>36)
						MyProgram.setError("You used a single function more than 36 times in one script.");
		
					if(parameters.size()!=1)
						MyProgram.setError("\"cancelAttack(int)\" takes an integer as input representing a port to cancel an attack on.");
					else if(!(parameters.get(0) instanceof TypeInteger))
						MyProgram.setError("\"cancelAttack(int)\" takes an integer as input representing a port to cancel an attack on.");	
					
					return(new TypeInteger(0));
				}else
			
				//Initialize an attack on the default attack port.
				if(name.equals("counterattack")){

					if(incrementValue("counterattack")>36)
						MyProgram.setError("You used a single function more than 36 times in one script.");
				
					if(parameters.size()!=1)
						MyProgram.setError("\"counterattack(int)\" takes an int representing the port that should perform the counter attack.");
					else if(!(parameters.get(0) instanceof TypeInteger))
						MyProgram.setError("\"counterattack(int)\" takes an int representing the port that should perform the counter attack.");
				}else
				
				//Initialize an attack on the default attack port.
				if(name.equals("attack")){

					if(incrementValue("attack")>36)
						MyProgram.setError("You used a single function more than 36 times in one script.");
				
					if(parameters.size()!=3)
						MyProgram.setError("\"attack\" takes an int representing an attack port, an opponent's IP, and an opponent's port.");
					else if(!(parameters.get(0) instanceof TypeInteger)||!(parameters.get(1) instanceof TypeString)||!(parameters.get(2) instanceof TypeInteger))
						MyProgram.setError("\"attack\" takes an int representing an attack port, an opponent's IP, and an opponent's port.");
				}else
				
				//Initialize an attack on the default attack port.
				if(name.equals("zombieAttack")){

					if(incrementValue("zombieAttack")>36)
						MyProgram.setError("You used a single function more than 36 times in one script.");
				
					if(parameters.size()!=4)
						MyProgram.setError("\"zombieAttack\" takes the ip of a zombie, their attack port, an opponent's IP, and an opponent's port.");
					else if(!(parameters.get(0) instanceof TypeString)||!(parameters.get(1) instanceof TypeInteger)||!(parameters.get(2) instanceof TypeString)||!(parameters.get(3) instanceof TypeInteger))
						MyProgram.setError("\"zombieAttack\" takes the ip of a zombie, their attack port, an opponent's IP, and an opponent's port.");
				}else
				
				//Initialize an attack on the default attack port.
				if(name.equals("scan")){
				
					if(incrementValue("scan")>36)
						MyProgram.setError("You used a single function more than 36 times in one script.");
				
					if(parameters.size()!=1)
						MyProgram.setError("\"scan\" takes a string as input representing the IP of an opponent.");
					else if(!(parameters.get(0) instanceof TypeString))
						MyProgram.setError("\"scan\" takes a string as input representing the IP of an opponent.");
				}else
				
				//Initialize an attack on the default attack port.
				if(name.equals("getTriggerParameter")){
				
					if(incrementValue("getTriggerParameter")>36)
						MyProgram.setError("You used a single function more than 36 times in one script.");
				
					if(parameters.size()!=1)
						MyProgram.setError("\"getTriggerParameter\" takes a string represnting the key of a trigger parameter.");
					else if(!(parameters.get(0) instanceof TypeString))
						MyProgram.setError("\"getTriggerParameter\" takes a string represnting the key of a trigger parameter.");
					
					return(new TypeInteger(5));
				}else
				
				//Initialize an attack on the default attack port.
				if(name.equals("counterattackAttack")){
				
					if(incrementValue("counterattackAttack")>36)
						MyProgram.setError("You used a single function more than 36 times in one script.");
				
					if(parameters.size()!=1)
						MyProgram.setError("\"counterattackAttack(int)\" takes an int representing the port that should perform the counter attack.");
					else if(!(parameters.get(0) instanceof TypeInteger))
						MyProgram.setError("\"counterattackAttack(int)\" takes an int representing the port that should perform the counter attack.");
				}else
				
				//Initialize an attack on the default attack port.
				if(name.equals("counterattackBank")){

					if(incrementValue("counterattackBank")>36)
						MyProgram.setError("You used a single function more than 36 times in one script.");
				
					if(parameters.size()!=1)
						MyProgram.setError("\"counterattackBank(int)\" takes an int representing the port that should perform the counter attack.");
					else if(!(parameters.get(0) instanceof TypeInteger))
						MyProgram.setError("\"counterattackBank(int)\" takes an int representing the port that should perform the counter attack.");
				}else
				
				//Check whether or not a fire wall is installed on the port being watched.
				if(name.equals("checkForFireWall")){

					if(incrementValue("checkForFireWall")>36)
						MyProgram.setError("You used a single function more than 36 times in one script.");
				
					if(parameters.size()!=1)
						MyProgram.setError("\"checkForFireWall\" takes a string as input representing the name of the fire wall to search for.");
					else if(!(parameters.get(0) instanceof TypeString))
						MyProgram.setError("\"checkForFireWall\" takes a string as input representing the name of the fire wall to search for.");
					return(new TypeInteger(0));
				}else
				
				//Switch a fire wall from port x to the current port.
				if(name.equals("switchFireWall")){

					if(incrementValue("switchFireWall")>36)
						MyProgram.setError("You used a single function more than 36 times in one script.");
	
					if(parameters.size()!=1)
						MyProgram.setError("\"switchFireWall\" takes as input an integer representing the port to switch a fire wall from.");
					else if(!(parameters.get(0) instanceof TypeInteger))
						MyProgram.setError("\"switchFireWall\" takes as input an integer representing the port to switch a fire wall from.");
				}else
				
				//Search for any fire wall and place it on this port.
				if(name.equals("switchAnyFireWall")){

					if(incrementValue("switchAnyFireWall")>36)
						MyProgram.setError("You used a single function more than 36 times in one script.");
	
					if(parameters.size()!=0)
						MyProgram.setError("\"switchAnyFireWall\" takes no input and switches any available fire walls on the observed ports list.");
				}else
				
				//return's a player's default bank.
				if(name.equals("getDefaultBank")){

					if(incrementValue("getDefaultBank")>36)
						MyProgram.setError("You used a single function more than 36 times in one script.");
	
					if(parameters.size()!=0)
						MyProgram.setError("\"getDefaultBank()\" takes no input and returns an Integer representing a player's default bank.");
			
					return(new TypeInteger(0));
				}else
				
				//Deposit petty cash into bank as defense.
				if(name.equals("transferMoney")){

					if(incrementValue("transferMoney")>36)
						MyProgram.setError("You used a single function more than 36 times in one script.");
	
					if(parameters.size()!=2)
						MyProgram.setError("\"transferMoney\" takes a string ip to transfer to and a float amount.");
					else if(!(parameters.get(0) instanceof TypeString)||!(parameters.get(1) instanceof TypeFloat))
						MyProgram.setError("\"transferMoney\" takes a string ip to transfer to and a float amount.");

				}else
				
				//Deposit petty cash into bank as defense.
				if(name.equals("depositPettyCash")){

					if(incrementValue("depositPettyCash")>36)
						MyProgram.setError("You used a single function more than 36 times in one script.");
	
					if(parameters.size()!=1)
						MyProgram.setError("\"depositPettyCash\"takes an float as input representing the money to deposit.");
					else if(!(parameters.get(0) instanceof TypeFloat))
						MyProgram.setError("\"depositPettyCash\"takes an float as input representing the money to deposit.");

				}else
				
				//Check for a fire wall of the given name.
				if(name.equals("checkFireWall")){

					if(incrementValue("checkFireWall")>36)
						MyProgram.setError("You used a single function more than 36 times in one script.");
				
					if(parameters.size()!=1)
						MyProgram.setError("\"checkFireWall\" takes a string as input representing the name of the fire wall and checks the port associated with this watch for said fire wall.");
					else if(!(parameters.get(0) instanceof TypeString))
						MyProgram.setError("\"checkFireWall\" takes a string as input representing the name of the fire wall and checks the port associated with this watch for said fire wall.");
					return(new TypeBoolean(true));
				}else
				
				//Shut down all observed ports.
				if(name.equals("shutDownPorts")){
				
					if(incrementValue("shutDownPorts")>36)
						MyProgram.setError("You used a single function more than 36 times in one script.");
				
					if(parameters.size()!=0)
						MyProgram.setError("\"shutDownPorts\" takes no input and attempts to shut down all observed ports.");
				}else
				
				//Shut down the port at the number provided.
				if(name.equals("shutDownPort")){

					if(incrementValue("shutDownPort")>36)
						MyProgram.setError("You used a single function more than 36 times in one script.");
	
					if(parameters.size()!=1)
						MyProgram.setError("\"shutDownPort\" takes as input an integer representing the port to shut down.");
					else if(!(parameters.get(0) instanceof TypeInteger))
						MyProgram.setError("\"shutDownPort\" takes as input an integer representing the port to shut down.");
				}else
				
				//Shut down the port at the number provided.
				if(name.equals("shutDownWatch")){

					if(incrementValue("shutDownWatch")>36)
						MyProgram.setError("You used a single function more than 36 times in one script.");
	
					if(parameters.size()!=1)
						MyProgram.setError("\"shutDownWatch\" takes as input an integer representing the watch to shut down.");
					else if(!(parameters.get(0) instanceof TypeInteger))
						MyProgram.setError("\"shutDownWatch\" takes as input an integer representing the watch to shut down.");
				}else
				
				//Shut down all observed ports.
				if(name.equals("turnOnPorts")){

					if(incrementValue("turnOnPorts")>36)
						MyProgram.setError("You used a single function more than 36 times in one script.");
	
					if(parameters.size()!=0)
						MyProgram.setError("\"turnOnnPorts\" takes no input and attempts to turn on all observed ports.");
				}else
				
				//Shut down the port at the number provided.
				if(name.equals("turnOnPort")){

					if(incrementValue("turnOnPort")>36)
						MyProgram.setError("You used a single function more than 36 times in one script.");
	
					if(parameters.size()!=1)
						MyProgram.setError("\"turnOnPort\" takes as input an integer representing the port to turn on.");
					else if(!(parameters.get(0) instanceof TypeInteger))
						MyProgram.setError("\"turnOnPort\" takes as input an integer representing the port to turn on.");
				}else

				//Shut down the port at the number provided.
				if(name.equals("turnOnWatch")){

					if(incrementValue("turnOnWatch")>36)
						MyProgram.setError("You used a single function more than 36 times in one script.");
	
					if(parameters.size()!=1)
						MyProgram.setError("\"turnOnWatch\" takes as input an integer representing the watch to turn on.");
					else if(!(parameters.get(0) instanceof TypeInteger))
						MyProgram.setError("\"turnOnWatch\" takes as input an integer representing the watch to turn on.");
				}else
				
				//Heal this port.
				if(name.equals("heal")){

					if(incrementValue("heal")>36)
						MyProgram.setError("You used a single function more than 36 times in one script.");
	
					if(parameters.size()!=0)
						MyProgram.setError("\"heal\" takes no input and attempts to heal the given port.");
				}else 
				
				
				//Get the port number that fired this watch.
				if(name.equals("getTargetPort")){

					if(incrementValue("getTargetPort")>36)
						MyProgram.setError("You used a single function more than 36 times in one script.");
	
					if(parameters.size()!=0)
						MyProgram.setError("\"getTargetPort\" takes no input and returns the port that caused this watch to fire.");
					return(new TypeInteger(0));
				}else 
				
				//Return the type of fire-wall that should be searched for in 
				if(name.equals("getSearchFireWall")){

					if(incrementValue("getSearchFireWall")>36)
						MyProgram.setError("You used a single function more than 36 times in one script.");
	
					if(parameters.size()!=0)
						MyProgram.setError("\"getSearchFireWall\" takes no input and returns the string representing the fire wall that should be searched for.");
					return(new TypeString("NoFireWall"));
				}
				
		/**
		Our Attack API.
		*/
		}else if(MyProgram instanceof AttackProgram){
			AttackProgram A=(AttackProgram)MyProgram;
		
			
			//Switch the attack to one of the alternate targets.
			if(name.equals("switchAttack")){

				if(incrementValue("switchAttack")>36)
					MyProgram.setError("You used a single function more than 36 times in one script.");
	
				if(parameters.size()!=0)
					MyProgram.setError("\"switchAttack\" takes no input and switches the attack to an alternate port.");
			}else
			
			if(name.equals("getMaliciousIP")){

				if(incrementValue("getMaliciousIP")>36)
					MyProgram.setError("You used a single function more than 36 times in one script.");
		
				if(parameters.size()!=0)
					MyProgram.setError("\"getMaliciousIP\" takes no parameters, and returns a string representing the IP of the individual maliciously hooking into an application.");
				return(new TypeString("192"));
			}else
				
			//Switch the attack to one of the alternate targets.
			if(name.equals("zombie")){

				if(incrementValue("zombie")>36)
					MyProgram.setError("You used a single function more than 36 times in one script.");
	
				if(parameters.size()!=1)
					MyProgram.setError("\"zombie\" takes a string as input representing the player allowed to use the port.");
				else if(!(parameters.get(0) instanceof TypeString))
					MyProgram.setError("\"zombie\" takes a string as input representing the player allowed to use the port.");

			}else
			
			//Switch the attack to one of the alternate targets.
			if(name.equals("freeze")){

				if(incrementValue("freeze")>36)
					MyProgram.setError("You used a single function more than 36 times in one script.");
	
				if(parameters.size()!=0)
					MyProgram.setError("\"freeze\" takes no parameters and freezes an opponent's port.");
			
			}else
			
			
			if(name.equals("isZombie")){
				if(incrementValue("isZombie")>36)
					MyProgram.setError("You used a single function more than 36 times in one script.");
	
				if(parameters.size()!=0)
					MyProgram.setError("\"isZombie\" takes no input and returns whether or not a port is currently in zombie mode.");
					
				return(new TypeBoolean(false));
			}else
			
			if(name.equals("deleteLogs")){
				if(incrementValue("deleteLogs")>36)
					MyProgram.setError("You used a single function more than 36 times in one script.");
	
				if(parameters.size()!=1)
					MyProgram.setError("\"deleteLogs\" takes an ip as input representing the player who's log should be erased.");
				else if(!(parameters.get(0) instanceof TypeString))
					MyProgram.setError("\"deleteLogs\" takes an ip as input representing the player who's log should be erased.");
			}else
			
			if(name.equals("editLogs")){
				if(incrementValue("editLogs")>36)
					MyProgram.setError("You used a single function more than 36 times in one script.");
	
				if(parameters.size()!=2)
					MyProgram.setError("\"editLogs\" takes an initial string and a string to replace it with.");
				else if(!(parameters.get(0) instanceof TypeString)||!(parameters.get(1) instanceof TypeString))
					MyProgram.setError("\"editLogs\" takes an initial string and a string to replace it with.");
			}else
			
			//Peform a berserk attack.
			if(name.equals("berserk")){

				if(incrementValue("berserk")>36)
					MyProgram.setError("You used a single function more than 36 times in one script.");
	
				if(parameters.size()!=0)
					MyProgram.setError("\"berserk\" takes no input and performs a second berserk attack.");
			}
			
			//Install a script as a finalization step in an attack.
			if(name.equals("installScript")){

				if(incrementValue("installScript")>36)
					MyProgram.setError("You used a single function more than 36 times in one script.");
	
				if(parameters.size()!=0)
					MyProgram.setError("\"installScript\" takes no input and installs a script from the script list provided at run-time.");
			}else
			
			//Get the number of times that an attack has taken place.
			if(name.equals("getIterations")){

				if(incrementValue("getIterations")>36)
					MyProgram.setError("You used a single function more than 36 times in one script.");
	
				if(parameters.size()!=0)
					MyProgram.setError("\"getIterations\" takes no input and returns the number of iterarations that this attack has been running for.");
				return(new TypeInteger(50));
			}else
			
			//Get the CPU load of the computer that this attack program is running on.
			if(name.equals("getCPULoad")){

				if(incrementValue("getCPULoad")>36)
					MyProgram.setError("You used a single function more than 36 times in one script.");
	
				if(parameters.size()!=0)
					MyProgram.setError("\"getCPULoad\" takes no input and returns the current CPU load on the computer running the attack.");
				return(new TypeFloat(150.0f));
			}else
			
			//Deposit petty cash into bank as defense.
			if(name.equals("stealFile")){

				if(incrementValue("stealFile")>36)
					MyProgram.setError("You used a single function more than 36 times in one script.");

				if(parameters.size()!=0)
					MyProgram.setError("\"stealFile\" takes no input and steals a file from an opponent.");

			}else
			
			//End the attack by emptying the player's petty cash.
			if(name.equals("emptyPettyCash")){

				if(incrementValue("emptyPettyCash")>36)
					MyProgram.setError("You used a single function more than 36 times in one script.");
	
				if(parameters.size()!=0)
					MyProgram.setError("\"emptyPettyCash\" takes no input and empties a player's petty cash.");		
			}else
			
			//End the attack by emptying the player's petty cash.
			if(name.equals("changeDailyPay")){

				if(incrementValue("changeDailyPay")>36)
					MyProgram.setError("You used a single function more than 36 times in one script.");
	
				if(parameters.size()!=1)
					MyProgram.setError("\"changeDailyPay\" takes a string as input representing a player's IP to set daily pay to.");	
				else if(!(parameters.get(0) instanceof TypeString))
					MyProgram.setError("\"changeDailyPay\" takes a string as input representing a player's IP to set daily pay to.");		
			}else
			
			//End an attack by requesting that choices are shown on the front-end.
			if(name.equals("showChoices")){
				if(incrementValue("showChoices")>36)
					MyProgram.setError("You used a single function more than 36 times in one script.");
					
				if(parameters.size()!=0)
					MyProgram.setError("\"showChoices\" takes no input and presents the player with attack finilization options.");			
				
			}else
			
			//Cancel the current attack taking place.
			if(name.equals("cancelAttack")){

				if(incrementValue("cancelAttack")>36)
					MyProgram.setError("You used a single function more than 36 times in one script.");
	
				if(parameters.size()!=0)
					MyProgram.setError("\"cancelAttack\" takes no input and cancels the current attack.");			

			}else
			
			//Cancel the current attack taking place.
			if(name.equals("underAttack")){

				if(incrementValue("underAttack")>36)
					MyProgram.setError("You used a single function more than 36 times in one script.");
	
				if(parameters.size()!=0)
					MyProgram.setError("\"underAttack\" takes no input and returns a boolean representing whether the computer is under attack.");			

			}else
			
			//Get the HP of the port being targeted with this attack script.
			if(name.equals("getTargetHP")){

				if(incrementValue("getTargetHP")>36)
					MyProgram.setError("You used a single function more than 36 times in one script.");
	
				if(parameters.size()!=0)
					MyProgram.setError("\"getTargetHP\" takes no input and returns the HP of the port being attacked.");
				return(new TypeFloat(150.0f));
			}else
			
			//Get the port that is initializing this attack.
			if(name.equals("getSourcePort")){

				if(incrementValue("getSourcePort")>36)
					MyProgram.setError("You used a single function more than 36 times in one script.");
	
				if(parameters.size()!=0)
					MyProgram.setError("\"getSourcePort\" takes no input and returns the port that is performing an attack.");
				return(new TypeInteger(0));
			}else
			
			//Get the port that is initializing this attack.
			if(name.equals("getTargetPort")){

				if(incrementValue("getTargetPort")>36)
					MyProgram.setError("You used a single function more than 36 times in one script.");
	
				if(parameters.size()!=0)
					MyProgram.setError("\"getTargetPort\" takes no input and returns the port that is being targeted by an attack.");
				return(new TypeInteger(0));
			}else
			
			//Return the amount currently in this player's petty cash.
			if(name.equals("checkPettyCash")){

				if(incrementValue("checkPettyCash")>36)
					MyProgram.setError("You used a single function more than 36 times in one script.");
	
				if(parameters.size()!=0)
					MyProgram.setError("\"getPettyCash\" takes no parameters and returns the amount currently in an opponent's petty cash.");
				return(new TypeFloat(30.0f));
			}

			//Get the target amount for stealing from petty cash.
			if(name.equals("checkPettyCashTarget")){

				if(incrementValue("checkPettyCashTarget")>36)
					MyProgram.setError("You used a single function more than 36 times in one script.");
	
				if(parameters.size()!=0)
					MyProgram.setError("\"getPettyCashTarget\" takes no parameters and returns an amount which can be used to determine when a theft should take place.");
				return(new TypeFloat(2.0f));
			}
			
			//Return the hit points of the port doing the attacking.
			if(name.equals("getHP")){

				if(incrementValue("getHP")>36)
					MyProgram.setError("You used a single function more than 36 times in one script.");
	
				if(parameters.size()!=0)
					MyProgram.setError("\"getHP\" takes no input and returns the HP of the port undertaking the attack.");
				return(new TypeFloat(150.0f));
			}else
			
			//Get maximum CPU load of the computer performing this attack.
			if(name.equals("getMaximumCPULoad")){

				if(incrementValue("getMaximumCPULoad")>36)
					MyProgram.setError("You used a single function more than 36 times in one script.");
	
				if(parameters.size()!=0)
					MyProgram.setError("\"geMaximumCPULoad\" takes no input and returns the maximum CPU load of the port undertaking this attack.");
				return(new TypeFloat(150.0f));
			}else
			
			//Finalize the attack by destroying the ports attached to target.
			if(name.equals("destroyWatches")){

				if(incrementValue("destroyWatches")>36)
					MyProgram.setError("You used a single function more than 36 times in one script.");
	
				if(parameters.size()!=0)
					MyProgram.setError("\"destroyWatches\" takes no input and destroyes all watches attached to the port being attacked.");

			}else
			
			//Get the CPU cost of the port being attacked.
			if(name.equals("getTargetCPUCost")){

				if(incrementValue("getTargetCPUCost")>36)
					MyProgram.setError("You used a single function more than 36 times in one script.");

				if(parameters.size()!=0)
					MyProgram.setError("\"getTargetCPUCost\" takes no input and returns the CPU cost associated with the port being attacked.");
				return(new TypeFloat(150.0f));
			}else
			
			//Check whether port being attacked has a watch installed.
			if(name.equals("checkForWatch")){

				if(incrementValue("checkForWatch")>36)
					MyProgram.setError("You used a single function more than 36 times in one script.");
	
				if(parameters.size()!=0)
					MyProgram.setError("\"checkForWatch\" takes no input and returns whether a watch is associated with the port being attacked.");
				return(new TypeBoolean(false));
			}
		}	
		/**
		The Redirect/Mining API.
		*/
		else if(MyProgram instanceof ShippingProgram){		
			if(name.equals("redirectDuctTape")){
				if(incrementValue("redirectDuctTape")>36)
					MyProgram.setError("You used a single function more than 36 times in one script.");
	
				if(parameters.size()!=0)
					MyProgram.setError("\"redirectDuctTape\" Takes no parameters and attempts to redirect shipments of duct tape to your computer.");
			}else
			
			if(name.equals("redirectGermanium")){
				if(incrementValue("redirectGermanium")>36)
					MyProgram.setError("You used a single function more than 36 times in one script.");
	
				if(parameters.size()!=0)
					MyProgram.setError("\"redirectGermanium\" Takes no parameters and attempts to redirect shipments of germanium to your computer.");
			}else
			
			if(name.equals("redirectSilicon")){
				if(incrementValue("redirectSilicon")>36)
					MyProgram.setError("You used a single function more than 36 times in one script.");
	
				if(parameters.size()!=0)
					MyProgram.setError("\"redirectSilicon\" Takes no parameters and attempts to redirect shipments of silicon to your computer.");
			}else
			
			if(name.equals("redirectYBCO")){
				if(incrementValue("redirectYBCO")>36)
					MyProgram.setError("You used a single function more than 36 times in one script.");
	
				if(parameters.size()!=0)
					MyProgram.setError("\"redirectYBCO\" Takes no parameters and attempts to redirect shipments of YBCO to your computer.");
			}else
			
			if(name.equals("redirectPlutonium")){
				if(incrementValue("redirectYBCO")>36)
					MyProgram.setError("You used a single function more than 36 times in one script.");
	
				if(parameters.size()!=0)
					MyProgram.setError("\"redirectPlutonium\" Takes no parameters and attempts to redirect shipments of plutonium to your computer.");
			}
		
		}
		
		/**
		The FTP API
		*/		
		else if(MyProgram instanceof FTPProgram){		
		
			FTPProgram F=(FTPProgram)MyProgram;
			
			//Put method has been called.
			if(name.equals("put")){

				if(incrementValue("ftp")>1)
					MyProgram.setError("You can only use put or get once in a script.");
	
				if(parameters.size()!=1)
					F.setError("\"put\" takes one parameter, a string representing the IP to send the data to.");
				else if(!(parameters.get(0) instanceof TypeString))
					F.setError("\"put\" takes one parameter, a string representing the IP to send the data to.");
			}else
			
			if(name.equals("get")){

				if(incrementValue("ftp")>1)
					MyProgram.setError("You can only use put or get once in a script.");
	
				if(parameters.size()!=1)
					F.setError("\"get\" takes one parameter, a string representing the IP to send the data to.");
				else if(!(parameters.get(0) instanceof TypeString))
					F.setError("\"get\" takes one parameter, a string representing the IP to send the data to.");
			}else
			
			if(name.equals("getFileName")){

				if(incrementValue("getFileName")>36)
					MyProgram.setError("You used a single function more than 36 times in one script.");
	
				if(parameters.size()!=0)
					MyProgram.setError("\"getFileName\" takes no parameters, and returns a string representing the name of the file being transfered.");
				return(new TypeString("192"));
			}else

			if(name.equals("getFileType")){

				if(incrementValue("getFileType")>36)
					MyProgram.setError("You used a single function more than 36 times in one script.");
	
				if(parameters.size()!=0)
					MyProgram.setError("\"getFileType\" takes no parameters, and returns a string representing the type of the file being transfered.");
				return(new TypeString("192"));
			}else
			
			if(name.equals("getFilePrice")){

				if(incrementValue("getFilePrice")>36)
					MyProgram.setError("You used a single function more than 36 times in one script.");
	
				if(parameters.size()!=0)
					MyProgram.setError("\"getFilePrice\" takes no parameters, and returns a float representing the asking price of the file being transfered.");
				return(new TypeFloat(0.0f));
			}
		}else if(MyProgram instanceof HTTPProgram){//THE HTTP API.
			if(name.equals("getVisitorIP")){

				if(incrementValue("getVisitorIP")>36)
					MyProgram.setError("You used a single function more than 36 times in one script.");
		
				if(parameters.size()!=0)
					MyProgram.setError("\"getVisitorIP\" takes no parameters, and returns a string representing the IP of a visitor to your website.");
							
				return(new TypeString("192"));
			}else
			
			//Shut down the port at the number provided.
			if(name.equals("turnOnWatch")){

				if(incrementValue("turnOnWatch")>36)
					MyProgram.setError("You used a single function more than 36 times in one script.");

				if(parameters.size()!=1)
					MyProgram.setError("\"turnOnWatch\" takes as input an integer representing the watch to turn on.");
				else if(!(parameters.get(0) instanceof TypeInteger))
					MyProgram.setError("\"turnOnWatch\" takes as input an integer representing the watch to turn on.");
			}else
			
			if(name.equals("replaceContent")){
				if(incrementValue("replaceContent")>20)
					MyProgram.setError("You used a single function more than 20 times in one script.");
		
				if(parameters.size()!=2)
					MyProgram.setError("\"replaceContent\" takes a key representing a block of text on a page, and the content to replace it with.");
				else if(!(parameters.get(0) instanceof TypeString)||!(parameters.get(1) instanceof TypeString))
					MyProgram.setError("\"replaceContent\" takes a key representing a block of text on a page, and the content to replace it with.");
			}else
			
			if(name.equals("triggerWatch")){
	
				if(incrementValue("triggerWatch")>36)
					MyProgram.setError("You used a single function more than 36 times in one script.");
					
				else if(!(parameters.get(0) instanceof TypeInteger))
					MyProgram.setError("\"triggerWatch\" trigger watch takes an int as input and fires the associated watch.");
			}else
			
			if(name.equals("hideStore")){
				if(incrementValue("hideStore")>36)
					MyProgram.setError("You used a single function more than 36 times in one script.");
		
				if(parameters.size()!=0)
					MyProgram.setError("\"hideStore\" takes no input and stops your store from being displayed.");
			}else
			
			if(name.equals("isGetVariableSet")){
				if(incrementValue("isGetVariableSet")>36)
					MyProgram.setError("You used a single function more than 36 times in one script.");
		
				if(parameters.size()!=1)
					MyProgram.setError("\"isGetVariableSet\" takes a string as representing a variable in the HTTP request and returns whether it is set.");
					
				return(new TypeBoolean(true));
			}else
		
			if(name.equals("fetchGetVariable")){
				if(incrementValue("fetchGetVariable")>30)
					MyProgram.setError("You used a single function more than 36 times in one script.");
		
				if(parameters.size()!=1)
					MyProgram.setError("\"fetchGetVariable\" takes a string as representing a variable in the HTTP request.");
				return(new TypeString("192"));
			}else
			
			if(name.equals("getHostIP")){
			
				if(incrementValue("getTargetIP")>36)
					MyProgram.setError("You used a single function more than 36 times in one script.");
		
				if(parameters.size()!=0)
					MyProgram.setError("\"getHostIP\" takes no parameters, and returns a string representing the IP hosting this website.");
				return(new TypeString("192"));
			}else
			
			if(name.equals("popUp")){

				if(incrementValue("message")>36)
					MyProgram.setError("You used a single function more than 36 times in one script.");
		
				if(parameters.size()!=1)
					MyProgram.setError("\"popUp\" takes a string as input representing a popup to display in the web browser.");
				else if(!(parameters.get(0) instanceof TypeString))
					MyProgram.setError("\"popUp\" takes a string as input representing a popup to display in the web browser.");
			}else
			
			if(name.equals("isParameterSet")){
				if(incrementValue("getParameter")>30)
					MyProgram.setError("You used a single function more than 36 times in one script.");
		
				if(parameters.size()!=1)
					MyProgram.setError("\"isParameterSet\" takes a string as input representing a parameter name, and returns whether it is set.");
				else if(!(parameters.get(0) instanceof TypeString))
					MyProgram.setError("\"isParameterSet\" takes a string as input representing a parameter name, and returns whether it is set.");
				
				return(new TypeBoolean(true));
			}
			
			if(name.equals("getParameter")){

				if(incrementValue("getParameter")>36)
					MyProgram.setError("You used a single function more than 36 times in one script.");
		
				if(parameters.size()!=1)
					MyProgram.setError("\"getParameter\" takes a string as input representing a parameter name, and returns the parameter if it exists.");
				else if(!(parameters.get(0) instanceof TypeString))
					MyProgram.setError("\"getParameter\" takes a string as input representing a parameter name, and returns the parameter if it exists.");
				
				return(new TypeString("192"));
			}
			
		}

		
		//Compare two strings which are passed in.
		if(name.equals("equal")){

			if(incrementValue("equal")>36)
				MyProgram.setError("You used a single function more than 36 times in one script.");
	
			if(parameters.size()!=2)
				MyProgram.setError("\"equal\" takes two strings and returns whether they are equal.");
			else if(!(parameters.get(0) instanceof TypeString)||!(parameters.get(1) instanceof TypeString))
				MyProgram.setError("\"equal\" takes two strings and returns whether they are equal.");
								
			return(new TypeBoolean(true));
		}
		
		if(name.equals("getSourceIP")){

			if(incrementValue("getSourceIP")>36)
				MyProgram.setError("You used a single function more than 36 times in one script.");
	
			if(parameters.size()!=0)
				MyProgram.setError("\"getSourceIP\" takes no parameters, and returns a string representing the IP legitimately using this application.");
			return(new TypeString("192"));
		}
		
		if(name.equals("getTargetIP")){
		
			if(incrementValue("getTargetIP")>36)
				MyProgram.setError("You used a single function more than 36 times in one script.");
	
			if(parameters.size()!=0)
				MyProgram.setError("\"getTargetIP\" takes no parameters, and returns a string representing the IP legitimately being targeted by this application.");
			return(new TypeString("192"));
		}
				
		if(name.equals("getMaliciousIP")){

			if(incrementValue("getMaliciousIP")>36)
				MyProgram.setError("You used a single function more than 36 times in one script.");
	
			if(parameters.size()!=0)
				MyProgram.setError("\"getMaliciousIP\" takes no parameters, and returns a string representing the IP of the individual maliciously hooking into an application.");
			return(new TypeString("192"));
		}
				
		if(name.equals("message")){

			if(incrementValue("message")>36)
				MyProgram.setError("You used a single function more than 36 times in one script.");
	
			if(parameters.size()!=2)
				MyProgram.setError("\"message\" takes an ip indicating a computer to deliver a message to, followed by said message.");
			else if(!(parameters.get(0) instanceof TypeString)||!(parameters.get(1) instanceof TypeString))
				MyProgram.setError("\"message\"	takes an ip indicating a computer to deliver a message to, followed by said message");
		}
		
		if(name.equals("playSound")){

			if(incrementValue("playSound")>3)
				MyProgram.setError("You used a single function more than 3 times in one script.");
	
			if(parameters.size()!=2)
				MyProgram.setError("\"playSound\" takes an ip indicating a computer to deliver a message to, followed by an integer representing a sound to play.");
			else if(!(parameters.get(0) instanceof TypeString)||!(parameters.get(1) instanceof TypeInteger))
				MyProgram.setError("\"playSound\" takes an ip indicating a computer to deliver a message to, followed by an integer representing a sound to play.");
		}
		
		if(name.equals("logMessage")){
		
			if(incrementValue("logMessage")>36)
				MyProgram.setError("You used a single function more than 36 times in one script.");
	
			if(parameters.size()!=1)
				MyProgram.setError("\"logMessage\" takes a string message as input.");
			else if(!(parameters.get(0) instanceof TypeString))
				MyProgram.setError("\"logMessage\" takes a string message as input.");
		}
		
		if(name.equals("printf")){
		
			if(incrementValue("printf")>36)
				MyProgram.setError("You used a single function more than 36 times in one script.");
	
			if(!(parameters.get(0) instanceof TypeString))
				MyProgram.setError("\"printf\" takes a string as an input followed by a series of parameters.");
			return(new TypeString("192"));
		}
		
		
		//Return the amount currently in this player's petty cash.
		if(name.equals("checkPettyCash")){

			if(incrementValue("checkPettyCash")>36)
				MyProgram.setError("You used a single function more than 36 times in one script.");
	
			if(parameters.size()!=0)
				MyProgram.setError("\"getPettyCash\" takes no parameters and returns the amount currently in a player's petty cash.");
			return(new TypeFloat(30.0f));
		}

		//Get the target amount for stealing from petty cash.
		if(name.equals("checkPettyCashTarget")){

			if(incrementValue("checkPettyCashTarget")>36)
				MyProgram.setError("You used a single function more than 36 times in one script.");
	
			if(parameters.size()!=0)
				MyProgram.setError("\"getPettyCashTarget\" takes no parameters and returns an amount which can be used to determine when a theft should take place.");
			return(new TypeFloat(2.0f));
		}
		
		//Returns a random number.
		if(name.equals("rand")){

			if(incrementValue("rand")>36)
				MyProgram.setError("You used a single function more than 36 times in one script.");
	
			if(parameters.size()!=0)
				MyProgram.setError("\"rand\" takes no parameters and returns a random floating point number between 0 and 1.");
			return(new TypeFloat((float)Math.random()));
		}
		
		//Compare two strings which are passed in.
		if(name.equals("strcmp")){
			if(parameters.size()!=2)
				MyProgram.setError("\"strcmp(string,string)\" Returns an integer less than, equal to or greater than zero depending on whether the first string is less than, equal to or greater than the second string.");
			else if(!(parameters.get(0) instanceof TypeString)||!(parameters.get(1) instanceof TypeString))
				MyProgram.setError("\"strcmp(string,string)\" Returns an integer less than, equal to or greater than zero depending on whether the first string is less than, equal to or greater than the second string.");
			
			return(new TypeInteger(5));
		}
		
		//Return the length of a string that is passed in.
		if(name.equals("strlen")){
			if(parameters.size()!=1)
				MyProgram.setError("\"strlen(string)\" Returns an integer representing the length of a string.");
			else if(!(parameters.get(0) instanceof TypeString))
				MyProgram.setError("\"strlen(string)\" Returns an integer representing the length of a string.");
			
			return(new TypeInteger(5));
		}
		
		//A math function.
		if(name.equals("sqrt")){
			if(parameters.size()!=1)
				MyProgram.setError("\"sqrt(float)\" Returns the square root of the float provided.");
			else if(!(parameters.get(0) instanceof TypeFloat))
				MyProgram.setError("\"sqrt(float)\" Returns the square root of the float provided.");
			
			return(new TypeFloat(3.0f));

		}
		
		//A math function.
		if(name.equals("abs")){
			if(parameters.size()!=1)
				MyProgram.setError("\"abs(float)\" Returns the absolute value of the float provided.");
			else if(!(parameters.get(0) instanceof TypeFloat))
				MyProgram.setError("\"abs(float)\" Returns the absolute value of the float provided.");
			
			return(new TypeFloat(3.0f));

		}
		
		//A math function.
		if(name.equals("ln")){
			if(parameters.size()!=1)
				MyProgram.setError("\"ln(float)\" Returns the natural logarithm of the float provided.");
			else if(!(parameters.get(0) instanceof TypeFloat))
				MyProgram.setError("\"ln(float)\" Returns the natural logarithm of the float provided.");
			
			return(new TypeFloat(3.0f));

		}
		
		//A math function.
		if(name.equals("atan")){
			if(parameters.size()!=1)
				MyProgram.setError("\"atan(float)\" Returns the arc-tangent of the floating point number provided.");
			else if(!(parameters.get(0) instanceof TypeFloat))
				MyProgram.setError("\"atan(float)\" Returns the arc-tangent of the floating point number provided.");
			
			return(new TypeFloat(3.0f));

		}
		
		//A math function.
		if(name.equals("acos")){
			if(parameters.size()!=1)
				MyProgram.setError("\"acos(float)\" Returns the arc-cosine of the floating point number provided.");
			else if(!(parameters.get(0) instanceof TypeFloat))
				MyProgram.setError("\"acos(float)\" Returns the arc-cosine of the floating point number provided.");
			
			return(new TypeFloat(3.0f));

		}
		
		//A math function.
		if(name.equals("asin")){
			if(parameters.size()!=1)
				MyProgram.setError("\"asin(float)\" Returns the arc-sine of the floating point number provided.");
			else if(!(parameters.get(0) instanceof TypeFloat))
				MyProgram.setError("\"asin(float)\" Returns the arc-sine of the floating point number provided.");
			
			return(new TypeFloat(3.0f));
		}
		
		//A math function.
		if(name.equals("tan")){
			if(parameters.size()!=1)
				MyProgram.setError("\"tan(float)\" Returns the tangent of the floating point number provided.");
			else if(!(parameters.get(0) instanceof TypeFloat))
				MyProgram.setError("\"tan(float)\" Returns the tangent of the floating point number provided.");
			
			return(new TypeFloat(3.0f));
		}else
		
		//Get a global parameter.
		if(name.equals("isGlobalSet")){
			if(parameters.size()!=1)
				MyProgram.setError("\"isGlobalSet\"Takes an int and returns whether that global is set.");
			else if(!(parameters.get(0) instanceof TypeInteger))
				MyProgram.setError("\"isGlobalSet\"Takes an int and returns whether that global is set.");
			
			return(new TypeBoolean(false));
		}else
		
		//Get a global parameter.
		if(name.equals("setGlobal")){
			if(parameters.size()!=2)
				MyProgram.setError("\"setGlobal\"Takes an int and another variable as input, and sets the value of a global parameter.");
			else if(!(parameters.get(0) instanceof TypeInteger))
				MyProgram.setError("\"setGlobal\"Takes an int and another variable as input, and sets the value of a global parameter.");
			
			return(null);
		}else
		
		//Get a global parameter.
		if(name.equals("getGlobal")){
			if(parameters.size()!=1)
				MyProgram.setError("\"getGlobal\" Takes an index as input and returns the variable associated with it.");
			else if(!(parameters.get(0) instanceof TypeInteger))
				MyProgram.setError("\"getGlobal\" Takes an index as input and returns the variable associated with it.");
			
			return(new TypeFloat(30.0f));
		}
		
		//A math function.
		if(name.equals("cos")){
			if(parameters.size()!=1)
				MyProgram.setError("\"cos(float)\" Returns the cosine of the floating point number provided.");
			else if(!(parameters.get(0) instanceof TypeFloat))
				MyProgram.setError("\"cos(float)\" Returns the cosine of the floating point number provided.");
			
			return(new TypeFloat(3.0f));
		}
		
		//A math function.
		if(name.equals("sin")){
			if(parameters.size()!=1)
				MyProgram.setError("\"sin(float)\" Returns the sine of the floating point number provided.");
			else if(!(parameters.get(0) instanceof TypeFloat))
				MyProgram.setError("\"sin(float)\" Returns the sine of the floating point number provided.");
			
			return(new TypeFloat(3.0f));
		}
		
		//A math function.
		if(name.equals("getE")){
			if(parameters.size()!=0)
				MyProgram.setError("\"getE()\" Returns the mathematical constant e.");
			
			return(new TypeFloat((float)Math.E));
		}else
		
		//A math function.
		if(name.equals("getPI")){
			if(parameters.size()!=0)
				MyProgram.setError("\"getPI()\" Returns the mathematical constant PI.");
				
			return(new TypeFloat((float)Math.PI));
		}else
		
		//A math function.
		if(name.equals("clearFile")){
			if(parameters.size()!=1)
				MyProgram.setError("\"clearFile\" Takes a string as input and deletes the content of the corresponding file.");
			else if(!(parameters.get(0) instanceof TypeString))
				MyProgram.setError("\"clearFile\" Takes a string as input and deletes the content of the corresponding file.");
		}else
		
		//A math function.
		if(name.equals("fileExists")){
			if(parameters.size()!=1)
				MyProgram.setError("\"fileExists\" Takes a string as input and returns whether the corresponding file exists.");
			else if(!(parameters.get(0) instanceof TypeString))
				MyProgram.setError("\"fileExists\" Takes a string as input and returns whether the corresponding file exists.");
			
			return(new TypeBoolean(false));
		}else
		
		
		//A math function.
		if(name.equals("readFile")){
			if(parameters.size()!=1)
				MyProgram.setError("\"readFile\" Takes a string as input and returns the content of the corresponding file.");
			else if(!(parameters.get(0) instanceof TypeString))
				MyProgram.setError("\"readFile\" Takes a string as input and returns the content of the corresponding file.");
				
			return(new TypeString(""));
		}else
		
		//A math function.
		if(name.equals("readLine")){
			if(parameters.size()!=2)
				MyProgram.setError("\"readLine\" Takes a string representing a file and an integer representing a line number and returns a line of a file.");
			else if(!(parameters.get(0) instanceof TypeString)||!(parameters.get(1) instanceof TypeInteger))
				MyProgram.setError("\"readLine\" Takes a string representing a file and an integer representing a line number and returns a line of a file.");
				
			return(new TypeString(""));
		}else
		
		//A math function.
		if(name.equals("countLines")){
			if(parameters.size()!=1)
				MyProgram.setError("\"countLines\" returns the number of lines of text in a file.");
			else if(!(parameters.get(0) instanceof TypeString))
				MyProgram.setError("\"countLines\" returns the number of lines of text in a file.");
		
			return(new TypeInteger(0));
		}else
		
		//A math function.
		if(name.equals("writeLine")){
			if(parameters.size()!=2)
				MyProgram.setError("\"writeLine\" Takes a file and a string as input and outputs the string to a line of the file.");
			else if(!(parameters.get(0) instanceof TypeString)||!(parameters.get(1) instanceof TypeString))
				MyProgram.setError("\"writeLine\" Takes a file and a string as input and outputs the string to a line of the file.");
				
		}else
		
		//A math function.
		if(name.equals("writeFile")){
			if(parameters.size()!=2)
				MyProgram.setError("\"writeFile\" Takes a file and a string as input and replaces the contents of the file with the string.");
			else if(!(parameters.get(0) instanceof TypeString)||!(parameters.get(1) instanceof TypeString))
				MyProgram.setError("\"writeFile\" Takes a file and a string as input and replaces the contents of the file with the string.");
				
		}else
		
		//A math function.
		if(name.equals("indexOf")){
			if(parameters.size()!=3)
				MyProgram.setError("\"indexOf\" Takes two strings and an index as input and searches for the nth occurrence of s2 in s1.");
			else if(!(parameters.get(0) instanceof TypeString)||!(parameters.get(1) instanceof TypeString)||!(parameters.get(2) instanceof TypeInteger))
				MyProgram.setError("\"indexOf\" Takes two strings and an index as input and searches for the nth occurrence of s2 in s1.");
			return(new TypeInteger(-1));
		}else
		
		//A math function.
		if(name.equals("replaceAll")){
			if(parameters.size()!=3)
				MyProgram.setError("\"replaceAll(string s1,string s2,string s3)\" returns s1 with all instances of s2 replaced with s3.");
			else if(!(parameters.get(0) instanceof TypeString)||!(parameters.get(1) instanceof TypeString)||!(parameters.get(2) instanceof TypeString))
				MyProgram.setError("\"replaceAll(string s1,string s2,string s3)\" returns s1 with all instances of s2 replaced with s3.");
			return(new TypeString("Blah"));
		}else
				
		//A math function.
		if(name.equals("intValue")){
			if(parameters.size()!=1)
				MyProgram.setError("\"intValue\" takes a float as input and returns the integer value.");
			else if(!(parameters.get(0) instanceof TypeFloat))
				MyProgram.setError("\"intValue\" takes a float as input and returns the integer value.");
			return(new TypeInteger(2));
		}else
		
		//A math function.
		if(name.equals("floatValue")){
			if(parameters.size()!=1)
				MyProgram.setError("\"floatValue\" takes an integer as input and returns the float value.");
			else if(!(parameters.get(0) instanceof TypeFloat))
				MyProgram.setError("\"floatValue\" takes an integer as input and returns the float value.");
			return(new TypeFloat(2.0f));
		}else
		
		//A math function.
		if(name.equals("parseFloat")){
			if(parameters.size()!=1)
				MyProgram.setError("\"parseFloat\" Takes a string as input and attempts to parse it as a float.");
			else if(!(parameters.get(0) instanceof TypeString))
				MyProgram.setError("\"parseFloat\" Takes a string as input and attempts to parse it as a float.");
			return(new TypeFloat(2.0f));
		}else
		
		//A math function.
		if(name.equals("char")){
			if(parameters.size()!=1)
				MyProgram.setError("\"char\" takes an int as input and returns a string containing the corresponding character.");
			else if(!(parameters.get(0) instanceof TypeInteger))
				MyProgram.setError("\"char\" takes an int as input and returns a string containing the corresponding character.");
			return(new TypeString("a"));
		}else
		
		//A math function.
		if(name.equals("toUpper")){
			if(parameters.size()!=1)
				MyProgram.setError("\"toUpper\" Takes a string as input and returns it as upper-case.");
			else if(!(parameters.get(0) instanceof TypeString))
				MyProgram.setError("\"toUpper\" Takes a string as input and returns it as upper-case.");
			return(new TypeString(""));
		}else
		
		//A math function.
		if(name.equals("toLower")){
			if(parameters.size()!=1)
				MyProgram.setError("\"toLower\" Takes a string as input and returns it as upper-case.");
			else if(!(parameters.get(0) instanceof TypeString))
				MyProgram.setError("\"toLower\" Takes a string as input and returns it as upper-case.");
			return(new TypeString(""));
		}else
		
		//A math function.
		if(name.equals("parseInt")){
			if(parameters.size()!=1)
				MyProgram.setError("\"parseInt\" Takes a string as input and attempts to parse it as an int.");
			else if(!(parameters.get(0) instanceof TypeString))
				MyProgram.setError("\"parseInt\" Takes a string as input and attempts to parse it as an int.");
			return(new TypeInteger(2));
		}else
		
		//Compare two strings which are passed in.
		if(name.equals("substr")){
			if(parameters.size()!=3)
				MyProgram.setError("\"substr(string,int,int)\" returns a substring starting and ending with the two indexes provided.");
			else if(!(parameters.get(0) instanceof TypeString)||!(parameters.get(1) instanceof TypeInteger)||!(parameters.get(2) instanceof TypeInteger))
				MyProgram.setError("\"substr(string,int,int)\" returns a substring starting and ending with the two indexes provided.");
			
			return(new TypeString(""));
		}else
		
		//Split a string.
		if(name.equals("split")){
		
			if(parameters.size()!=2){
				MyProgram.setError("\"split(string,string)\" takes a string representing the data to split and a string representing the data to split on.");
				return(null);
			}if(!(parameters.get(0) instanceof TypeString)||!(parameters.get(1) instanceof TypeString)){
				MyProgram.setError("\"split(string,string)\" takes a string representing the data to split and a string representing the data to split on.");
				return(null);
			}			
			
			ArrayList A=new ArrayList();
			for(int i=0;i<128;i++)
				A.add(new TypeString(""));
						
			TypeArray TA=new TypeArray(A);
			return(TA);
		}else
		
		if(name.equals("join")){
			if(parameters.size()!=2){
				MyProgram.setError("\"join(Array,String)\" Takes an array and a string to join on, returns the joined array.");
			}if(!((parameters.get(0) instanceof ArrayList)|(parameters.get(0) instanceof TypeArray))||!(parameters.get(1) instanceof TypeString)){
				MyProgram.setError("\"join(Array,String)\" Takes an array and a string to join on, returns the joined array.");
			}	
			
			return(new TypeString(""));
		}else
	
		if(name.equals("length")){
			if(parameters.size()!=1){
				MyProgram.setError("\"length(Array)\" Takes an array as input and returns the length of the array.");
			}if(!((parameters.get(0) instanceof ArrayList)|(parameters.get(0) instanceof TypeArray))){
				MyProgram.setError("\"length(Array)\" Takes an array as input and returns the length of the array.");
			}	
			
			return(new TypeInteger(0));
		}else
		
		//A math function.
		if(name.equals("getTime")){
			if(parameters.size()!=0)
				MyProgram.setError("\"getTime()\" Take no input and returns a formated string representing the time.");
				
			return(new TypeString(""));
		}else
		
		//A math function.
		if(name.equals("getDate")){
			if(parameters.size()!=0)
				MyProgram.setError("\"getDate()\" Take no input and returns a formated string representing the date.");
				
			return(new TypeString(""));
		}
		
		}catch(Exception e){
			e.printStackTrace();
		}
        
        return(null);
    }
}
