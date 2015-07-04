package Game;

/**
Port.java

A port which can be installed on the computer. Has an application which
can be overwritten by attacks, and performs a various array of functions.
*/

import java.util.*;
import Assignments.*;
import java.text.*;

public class Port {

	//Types of Ports.
	public static final int BANKING=0;
	public static final int FTP=1;
	public static final int ATTACK=2;
	public static final int HTTP=3;
	public static final int REDIRECT=4;
    public static final int SHIPPING=REDIRECT;
	public static final float MAX_REDIRECT_XP=2000.0f;//The max redirect XP that can be gained in a single round of redirecting.
	private float currentRedirectXP=0.0f;//The amount of redirecting XP that has been gained from this single target.
	private String currentRedirectIP="";//The IP of the individual currently redirecting off this port.
	
	//Settings.
	private int type=0;//What type of port.
	private int number;//What port number.
	private String note="";//Notes about port.
	private String maliciousTarget="";//Some malicious programs need a target to deliver perks to.
	
	//Port modes.
	private boolean on=false;//Is the port on or off.
	private boolean attacking=false;//Is the port currently attacking.	
	private boolean overHeated=false;//Is the port overheated.
	private boolean freeze=false;//Is this port currently frozen.
	private long freezeStart=0;//When did the freeze start?
	private static final long FREEZE_TIME=10000;//How long does a port get frozen for?
	private boolean weakened=false;//Is the port in a weakened state (can we do something malicious?)
	private boolean dummy=false;//Is this port a dummy.

	private Program MyProgram=null;//The program which will be activated when ApplicationData is place on this port.
	private NewFireWall MyFireWall=null;//The FireWall that damage will be filtered through.
	private NetworkSwitch MyComputerHandler=null;//Used to dispatch messages between computers.
	private Computer MyComputer=null;//The computer this port is attached to.

	//Port Health.
	private float health=100.0f;
	private float maximumHealth=100.0f;
	
	//Port CPU cost.
	private float cpuCost=0.0f;

	//Under Attack specific.
	public static final long timeOut=30000;//How long before connection to port resets.
	public static final long fullTimeOut=45000;//10000;//How long before connection to port resets.
	private String accessing="";//What IP is currently accessing the port.
	private int accessingPort=0;//What port is accessing the port.
	private long lastAccessed=0;//When was the port last accessed.

	//Attacking specific.
	private float targetHP=100.0f;//Hit points of port being attacked.
	private float targetPortPettyCash=0.0f;//Amount in petty cash of port being attacked.
	private float targetCPUCost=0.0f;//CPU cost of port being attacked.
	private boolean targetWatch=false;//Is there a watch on the port being targeted.
	private PacketAssignment PA=null;
	private int healCount=0;
	
	private int lastDamageWindowHandle = 0;
	private String lastDamageIP = "";
	
	/**
	Constructor.
	*/
	public Port(Computer MyComputer,NetworkSwitch MyComputerHandler){
		this.MyComputer=MyComputer;
		this.MyComputerHandler=MyComputerHandler;
	}
	
	/**
	Set whether or not the port being targeted with an attack has a watch installed.
	*/
	public void setTargetWatch(boolean targetWatch){
		this.targetWatch=targetWatch;
	}
	
	/**
	Get whether or not the port being targeted with an attack has a watch installed.
	*/
	public boolean getTargetWatch(){
		return(targetWatch);
	}
	
	/**
	Get the IP address of the computer this port is attached to.
	*/
	public String getIP(){
		return(MyComputer.getIP());
	}
	
	public Computer getMyComputer(){
		return(MyComputer);
	}
	
	/**
	Return the hit points of the port that is being attacked with this port.
	*/
	public float getTargetHP(){
		return(targetHP);
	}
	
	/**
	Set the current value for target HP the hit points of the computer being attacked.
	*/
	public void setTargetHP(float targetHP){
		this.targetHP=targetHP;
	}
	
	/**
	Set the note associated with this port.
	*/
	public void setNote(String note){
		this.note=note;
	}
	
	/**
	Get the note associated with this port.
	*/
	public String getNote(){
		return(note);
	}
	
	/**
	Set the port number associated with this port.
	*/
	public void setNumber(int number){
		this.number=number;
	}
	
	/**
	Get the port number associated with this port.
	*/
	public int getNumber(){
		return(number);
	}	
	
	/**
	Return the value of the petty cash in the target port.
	*/
	public float getTargetPettyCash(){
		return(targetPortPettyCash);
	}
	
	/**
	Set the petty cash value of the port being attacked.
	*/
	public void setTargetPettyCash(float targetPortPettyCash){
		this.targetPortPettyCash=targetPortPettyCash;
	}
	
	/**
	Set the current CPU cost of the port being attacked.
	*/
	public void setTargetCPUCost(float targetCPUCost){
		this.targetCPUCost=targetCPUCost;
	}
	
	/**
	Get the current CPU cost of the port being attacked.
	*/
	public float getTargetCPUCost(){
		return(targetCPUCost);
	}
	
	/**
	Get whether this port is in a weakened state.
	*/
	public boolean getWeakened(){
		return(weakened);
	}
	
	/**
	Set whether this port is a dummy port (used as a dummy).
	*/
	public void setDummy(boolean dummy){
		this.dummy=dummy;
	}
	
	/**
	Get whether this port is a dummy port (used as a dummy).
	*/
	public boolean getDummy(){
		return(dummy);
	}
	
	/**
	Set whether the port is overheated (makes it  not functional).
	*/
	public void setOverHeated(boolean overHeated){
		if(this.overHeated==false&&overHeated&&attacking){
			cancelAttack(false);
		}
		else if(this.overHeated==true&&overHeated==false){
			
			resetPort(true,false);
		}
		this.overHeated=overHeated;
	}
	
	/**
	Get whether this port has overheated.
	*/
	public boolean getOverHeated(){
		/*if(health>=maximumHealth){
			if(MyComputer.getOverheatStart()==-1)
				overHeated=false;
		}*/
		return(overHeated);
	}
	
	/**
	Get the amount of heals done to the port already.
	*/
	public int getHealCount(){
		return(healCount);
	}
	
	/**
	getProgram()
	returns the program installed on the port.
	*/
	public Program getProgram(){
		return(this.MyProgram);
	}
	
	/**
	setProgram(Program MyProgram)
	installs the program on the port.
	*/
	public void setProgram(Program MyProgram){
		this.MyProgram=MyProgram;
	}
	
	/**
	getFireWall()
	returns the firewall installed on the port.
	*/
	public NewFireWall getFireWall(){
		return(this.MyFireWall);
	}
	
	/**
	setFireWall(FireWall MyFireWall)
	installs the firewall on the port.
	*/
	public void setFireWall(HackerFile NewFireWall){
		MyFireWall.loadHackerFile(NewFireWall);
	}
    
    public void setFireWall(NewFireWall MyFireWall){
        this.MyFireWall=MyFireWall;
    }
	
	/**
	getHealth()
	returns the health of the port.
	*/
	public float getHealth(){
		return(this.health);
	}
	
	/**
	setHealth(int health)
	sets the current health of the port.
	*/
	private boolean healthSet=false;
	public void setHealth(float health){
		this.health = health;
		healthSet=true;
	}
	
	/**
	setMaximumHealth(int health)
	Sets the maximum health of this port.
	*/
	public void setMaximumHealth(float maximumHealth){
		this.maximumHealth=maximumHealth;
	}
	
	/**
	damagePort(int damage)
	damages the port by given amount.
	*/
	public boolean damagePort(float damage){
		boolean returnMe=false;
		if(this.health<100.0f)
			returnMe=true;
		this.health-=damage;
		if(health<0.0f)
			health=0.0f;
		if(health>=maximumHealth){
			health=maximumHealth;
		}
		
		if(MyComputer.getCurrentTime()-freezeStart>FREEZE_TIME){
			freeze=false;
		}
		
		if(this.healthSet){
			healthSet=false;
			returnMe=true;
		}
		
		return(returnMe);
	}
	
	/**
	getType()
	gets the type for this port.
	*/
	public int getType(){
		return(this.type);
	}
	
	/**
	setType(int type)
	sets the type of the port.
	*/
	public void setType(int type){
		this.type=type;
	}
	
	/**
	getOn()
	Gets whether this port is currently on.
	*/
	public boolean getOn(){
		return(on);
	}
	
	/**
	setOn()
	Set whether this port is currently on.
	*/
	public void setOn(boolean on){
		this.on=on;
	}
	
	/**
	Set the packet that is currently getting filled in.
	*/
	public void setCurrentPacket(PacketAssignment PA){
		this.PA=PA;
	}
	
	/**
	Get the packet that is currently being filled.
	*/
	public PacketAssignment getCurrentPacket(){
		return(PA);
	}
	
	/**
	getAttacking()
	Gets whether this port is currently attacking.
	*/
	public boolean getAttacking() {
		return(attacking);
	}
	
	/**
	Set whether the port is currently attacking.
	*/
	public void setAttacking(boolean attacking){
		this.attacking=attacking;
		MyComputer.getDamage().add(new Object[]{getWindowHandle(),attacking});
		MyComputer.sendDamagePacket();
	}
	
	/**
	Get the base CPU cost of this port sans firewall.
	*/
	public float getBaseCPUCost(){
		return(cpuCost);
	}
	
	/**
	Get the CPU cost associated with this port.
	*/
	public float getCPUCost(){
		if(!on)
			return(0.0f);
	
		float returnMe=cpuCost;
		float mult=0.0f;
		if((type==ATTACK||type==REDIRECT)&&(attacking||overHeated)){
			mult+=(maximumHealth-health)*(MyComputer.getMaximumCPULoad()/100.0f);
		}
		
		if(dummy)//If it's a dummy port it costs half as much to run.
			return((cpuCost+mult+MyFireWall.getCPUCost())/2.0f);
		else//Otherwise it has the full CPU cost.
			return(cpuCost+mult+MyFireWall.getCPUCost());
	}
	
	/**
	Get the true CPU cost of this port.
	*/
	public float getBaseCPUCostTotal(){
		if(!on)
			return(0.0f);
	
		if(dummy)//If it's a dummy port it costs half as much to run.
			return((cpuCost+MyFireWall.getCPUCost())/2.0f);
		else//Otherwise it has the full CPU cost.
			return(cpuCost+MyFireWall.getCPUCost());
	}
	
	public float getBaseCPUCostAndFirewall(){
	
		if(dummy)//If it's a dummy port it costs half as much to run.
			return((cpuCost+MyFireWall.getCPUCost())/2.0f);
		else//Otherwise it has the full CPU cost.
			return(cpuCost+MyFireWall.getCPUCost());
	}
	
	/**
	Get the actual CPU cost regardless of whether this port is on.
	*/
	public float getActualCPUCost(){
		float returnMe=cpuCost;
		float mult=0.0f;
		if((type==ATTACK||type==REDIRECT)&&(attacking||overHeated)){
			mult+=(maximumHealth-health)*(MyComputer.getMaximumCPULoad()/100.0f);
		}
		
		if(dummy)//If it's a dummy port it costs half as much to run.
			return((cpuCost+mult+MyFireWall.getCPUCost())/2.0f);
		else//Otherwise it has the full CPU cost.
			return(cpuCost+mult+MyFireWall.getCPUCost());
	}

	/**
	Set the CPU cost associated with this port.
	*/
	public void setCPUCost(float cpuCost){
		this.cpuCost=cpuCost;
	}
	
	/**
	Get whether someone is currently accessing this port.
	*/
	public String getAccessing(){
		return(accessing);
	}
	
	/**
	Get the port of the program currently accessing this port.
	*/
	public int getAccessingPort(){
		return(accessingPort);
	}
	
	/**
	Set the malicious target associated with this port used in various APIs.
	*/
	public void setMaliciousTarget(String maliciousTarget){
		this.maliciousTarget=maliciousTarget;
	}
	
	/**
	Get the malicious target associated with this port.
	*/
	public String getMaliciousTarget(){
		return(maliciousTarget);
	}
	
	/**
	Cancel's an attack on an opponent by this port (if it is an attack port).
	*/
	public void cancelAttack(boolean overHeated){
		if(MyProgram!=null){
			if(MyProgram instanceof AttackProgram){
				((AttackProgram)MyProgram).cancelAttack(overHeated);
			}else
			
			if(MyProgram instanceof ShippingProgram){
				((ShippingProgram)MyProgram).cancelAttack(overHeated);
			}
		}
	}
	
	/**
	Make sure an attack on a port times out in the appropriate amount of time.
	*/
	public void checkTimeOut(long currentTime){
		//THE ATTACK HAS TIMED OUT ALLOW ANOTHER ATTACKER.
		
		//if(weakened||!accessing.equals("")||health<maximumHealth)
		if(currentTime-lastAccessed>timeOut&&weakened){
			resetPort(true,true);
		}
		else if(currentTime-lastAccessed>fullTimeOut){
			resetPort(true,true);
		}
	}
	
	/**
	Set the last access time of this application.
	*/
	public void setLastAccessed(long lastAccessed){
		this.lastAccessed=lastAccessed;
	}
	
	/**
	Check whether a finalize is currently allowed and dispatch a packet otherwise.
	*/
	public boolean finalizeAllowed(ApplicationData MyApplicationData){
		boolean allowed=true;
		if(weakened){
			if(!getDummy()){
		
			}else{
				allowed=false;
				MyComputerHandler.addData(new ApplicationData("message",new Object[]{MessageHandler.PORT_WAS_DUMMY,new Object[]{number,MyComputer.getIP()},new Object[]{MyApplicationData.getSourcePort(),MyApplicationData.getSourceIP()}},0,MyComputer.getIP()),MyApplicationData.getSourceIP());
			}
		}else{
			allowed=false;
			MyComputerHandler.addData(new ApplicationData("message",new Object[]{MessageHandler.PORT_WAS_NOT_WEAKENED,new Object[]{number,MyComputer.getIP()},new Object[]{MyApplicationData.getSourcePort(),MyApplicationData.getSourceIP()}},0,MyComputer.getIP()),MyApplicationData.getSourceIP());
		}
		return(allowed);
	}
	
	/**
	Reset the port to an unweakened state.
	*/
	public void resetPort(boolean heal,boolean setAccessed){
		if(heal){
			this.setHealth(maximumHealth);
		}
		lastAccessed=MyComputer.getCurrentTime();
		weakened=false;
		if(setAccessed){
			accessing="";
		}
		healCount=0;
	}
	
	/**
	Reset the heal counter on the attack.
	*/
	public void resetHealCounter(){
		healCount=0;
	}
	
	public int getLastDamageWindowHandle(){
		return(lastDamageWindowHandle);
	}
	
	public String getLastDamageIP(){
		return(lastDamageIP);
	}
	
	public int getWindowHandle(){
		int windowHandle = 0;
		if(MyProgram instanceof AttackProgram){
			windowHandle = ((AttackProgram)MyProgram).getWindowHandle();
		}
		else if(MyProgram instanceof ShippingProgram){
			windowHandle = ((ShippingProgram)MyProgram).getWindowHandle();
		}
		return(windowHandle);
	}
	
	/**
	Execute a remote function call.
	*/
	public void addApplicationData(ApplicationData MyApplicationData,long currentTime){	
		currentTime = MyComputer.getCurrentTime();
		if(on){//ONLY PERFORM OPERATIONS ON PORT IF IT IS ON.
		//Heal this port.
		if(MyApplicationData.getFunction().equals("heal")){
			
			if(!MyComputer.checkBank()){//Check whether a banking port is installed.
				MyComputerHandler.addData(new ApplicationData("message",MessageHandler.ACTIVE_BANK_NOT_FOUND,0,MyComputer.getIP()),MyApplicationData.getSourceIP());
			}else if(overHeated){
				MyComputerHandler.addData(new ApplicationData("message",MessageHandler.HEAL_FAIL_OVERHEATED,0,MyComputer.getIP()),MyApplicationData.getSourceIP());
			}else if(healCount>MyComputer.HEAL_LIMIT){
				MyComputerHandler.addData(new ApplicationData("message",new Object[]{MessageHandler.HEAL_FAIL_LIMIT,new Object[]{MyComputer.HEAL_LIMIT+1}},0,MyComputer.getIP()),MyApplicationData.getSourceIP());
			}else if(!weakened){
				float cost=(maximumHealth-getHealth())*2.0f*MyComputer.getEquipmentSheet().getHealBonus();//Costs $2 per HP healed.
				
				if(MyComputer.getPettyCash()>=cost){
					
					healCount++;//Increment the heal counter.
				
					MyComputerHandler.addData(new ApplicationData("pettycash",new Float(cost*-1.0f),0,MyComputer.getIP()),MyComputer.getIP());
					this.setHealth(maximumHealth);
					MyComputer.sendDamagePacket();
					MyComputer.getWatchHandler().updateInitialHealthQuanity(number,health);
					MyComputerHandler.addData(new ApplicationData("message",new Object[]{MessageHandler.HEAL_SUCCESS,new Object[]{number,NumberFormat.getCurrencyInstance().format(cost)}},0,MyComputer.getIP()),MyComputer.getIP());
				}
			}else{
				MyComputerHandler.addData(new ApplicationData("message",new Object[]{MessageHandler.HEAL_FAIL_WEAKENED,new Object[]{number}},0,MyComputer.getIP()),MyApplicationData.getSourceIP());
			}
			
			return;
		}
		//EMPTYING PETTY CASH IS ONE OPTION UPON COMPLETING AN ATTACK.
		//if(type==BANKING){
		if(MyApplicationData.getFunction().equals("emptyPettyCash")&&accessing.equals(MyApplicationData.getSourceIP())){
			NumberFormat nf = NumberFormat.getCurrencyInstance();
			int windowHandle = (Integer)MyApplicationData.getParameters();
			if(type!=BANKING){
				MyComputerHandler.addData(new ApplicationData("message", new Object[] {MessageHandler.EMPTY_PETTY_FAIL_WRONG_TYPE, new Object[] {}, new Object[] {windowHandle, MyComputer.getIP()}},0,MyComputer.getIP()),MyApplicationData.getSourceIP());
			} else if (!MyFireWall.getPettyCashFail(MyApplicationData.getSourceIP())){//Has the firewall caused the redirect of money to fail.
				if(finalizeAllowed(MyApplicationData)){
					float amount=MyComputer.getPettyCash()*MyFireWall.getPettyCashReduction(MyApplicationData.getSourceIP());
                    // check to make sure they have an active non-dummy bank port
                    if (MyComputerHandler.getMyComputerHandler().getComputer(MyApplicationData.getSourceIP()).checkBank()) {
						
    					MyComputerHandler.addData(new ApplicationData("pettycash",new Float(-1.0*amount),0,MyComputer.getIP()),MyComputer.getIP());
    					MyComputerHandler.addData(new ApplicationData("pettycash",new Float(amount),0,MyApplicationData.getSourceIP()),MyApplicationData.getSourceIP());//Changed to originate from the attacker.
    					MyComputerHandler.addData(new ApplicationData("message",new Object[]{MessageHandler.EMPTY_PETTY_SUCCESS_GAME,new Object[]{nf.format(amount),MyComputer.getIP()}},0,MyComputer.getIP()),MyApplicationData.getSourceIP());
						MyComputerHandler.addData(new ApplicationData("message",new Object[]{MessageHandler.EMPTY_PETTY_SUCCESS,new Object[]{nf.format(amount)},new Object[]{windowHandle,MyApplicationData.getSourceIP()}},0,MyComputer.getIP()),MyApplicationData.getSourceIP());
                    } else {
                        MyComputerHandler.addData(new ApplicationData("message",new Object[]{MessageHandler.EMPTY_PETTY_FAIL_NO_ACTIVE_BANK,new Object[]{MyComputer.getIP()}},0,MyComputer.getIP()),MyApplicationData.getSourceIP());
                    }
				}
			}
			else{
				MyComputerHandler.addData(new ApplicationData("message",new Object[]{MessageHandler.EMPTY_PETTY_SUCCESS,new Object[]{nf.format(0)}},0,MyComputer.getIP()),MyApplicationData.getSourceIP());
				MyComputerHandler.addData(new ApplicationData("message",new Object[]{MessageHandler.EMPTY_PETTY_SUCCESS_GAME,new Object[]{nf.format(0),MyComputer.getIP()},new Object[]{windowHandle,MyApplicationData.getSourceIP()}},0,MyComputer.getIP()),MyApplicationData.getSourceIP());
			}
			resetPort(true,true);
			return;

		}
        
        else if (MyApplicationData.getFunction().equals("finalizecancelled")&&accessing.equals(MyApplicationData.getSourceIP())) {
            if (finalizeAllowed(MyApplicationData)) {
                resetPort(true,true);
                return;
            }
        }
			
		//}
		//Allow for the malicious stealing of files.
//		if(type==FTP){
		if(MyApplicationData.getFunction().equals("malget")&&accessing.equals(MyApplicationData.getSourceIP())){
		
			if(type!=FTP){
				MyComputerHandler.addData(new ApplicationData("message", new Object[] {MessageHandler.STEAL_FILE_FAIL_WRONG_TYPE, new Object[] {}, new Object [] {lastDamageWindowHandle, MyComputer.getIP()}},0,MyComputer.getIP()),MyApplicationData.getSourceIP());
			} else if(!MyFireWall.getStealFileFail(MyApplicationData.getSourceIP())){//Can we steal a file based on firewall.
				if(finalizeAllowed(MyApplicationData)){
					MyProgram.execute(MyApplicationData);
				}
			}
			resetPort(true,true);
			return;
		}
			
		//}
		//Delete an opponent's logs as a finalize step.
		if(MyApplicationData.getFunction().equals("deletelog")&&accessing.equals(MyApplicationData.getSourceIP())){
			if(finalizeAllowed(MyApplicationData)){
				//DELETE A SINGLE PLAYER'S ENTRY FROM THE LOG.
				if(MyApplicationData.getParameters()!=null){
					MyComputer.deleteLogs((String)MyApplicationData.getParameters());
					MyComputerHandler.addData(new ApplicationData("message",MessageHandler.DELETE_LOGS_SUCCESS,0,MyComputer.getIP()),MyApplicationData.getSourceIP());
					MyComputer.sendPacket();
				}
			}
			resetPort(true,true);
			return;
		}
		//Look at the opponent's code on the port attacked, upon finalizing an attack.
		if(MyApplicationData.getFunction().equals("peekcode")&&accessing.equals(MyApplicationData.getSourceIP())){
			if(finalizeAllowed(MyApplicationData)){
					if(MyProgram!=null){
						if(MyComputer.getType()!=MyComputer.NPC)
							MyComputerHandler.addData(new ApplicationData("code",MyProgram.getContent(),0,MyComputer.getIP()),MyApplicationData.getSourceIP());
						else
							MyComputerHandler.addData(new ApplicationData("code","[Encrypted Data.]",0,MyComputer.getIP()),MyApplicationData.getSourceIP());

					}
			}
			resetPort(true,true);
			return;
		}
		//Look at the opponent's code on the port attacked, upon finalizing an attack.
		if(MyApplicationData.getFunction().equals("peeklogs")&&accessing.equals(MyApplicationData.getSourceIP())){
			if(finalizeAllowed(MyApplicationData)){
					if(MyProgram!=null){
						HashMap Logs=new HashMap();
						Logs.put("logs",MyComputer.getLogs());
						MyComputerHandler.addData(new ApplicationData("code",Logs,0,MyComputer.getIP()),MyApplicationData.getSourceIP());
					}
			}
			resetPort(true,true);
			return;
		}
		//Look at the opponent's code on the port attacked, upon finalizing an attack.
		if(MyApplicationData.getFunction().equals("editLogs")&&accessing.equals(MyApplicationData.getSourceIP())){
			if(finalizeAllowed(MyApplicationData)){
					if(MyProgram!=null){
						String data=(String)((Object[])MyApplicationData.getParameters())[0];
						String replace=(String)((Object[])MyApplicationData.getParameters())[1];
						MyComputer.editLogs(data,replace);
						MyComputerHandler.addData(new ApplicationData("message",MessageHandler.EDIT_LOGS_SUCCESS,0,MyComputer.getIP()),MyApplicationData.getSourceIP());
					}
			}
			resetPort(true,true);
			return;
		}
		
		//Change the IP address that daily pay should be transferred to.
		//if(type==HTTP)
		if(MyApplicationData.getFunction().equals("changedailypay")&&accessing.equals(MyApplicationData.getSourceIP())){
			Object[] parameters = (Object[])MyApplicationData.getParameters();
			String targetIP=(String)parameters[0];
			int port = (Integer)parameters[1];
			if(!MyFireWall.getChangeDailyPayFail(targetIP)){
				MyComputer.setDailyPayReduction(MyFireWall.getChangeDailyPayReduction(MyApplicationData.getSourceIP()));
				if(type!=HTTP){
					MyComputerHandler.addData(new ApplicationData("message",new Object[] {MessageHandler.CHANGE_DAILY_PAY_FAIL_WRONG_TYPE, new Object[] {}, new Object[] {lastDamageWindowHandle, MyComputer.getIP()}},0,MyComputer.getIP()),MyApplicationData.getSourceIP());
				}
				else if(finalizeAllowed(MyApplicationData)){
					if(MyComputer.getLastBountyHTTPIP().equals(MyApplicationData.getSourceIP())){
						MyComputerHandler.addData(new ApplicationData("message",MessageHandler.CHANGE_DAILY_PAY_FAIL_BOUNTY,0,MyComputer.getIP()),MyApplicationData.getSourceIP());
						return;
					}else if(!MyComputer.getAdRevenueTarget().equals(targetIP)){//give http xp.
						if(MyComputer.getType()==MyComputer.NPC)
							MyComputerHandler.addData(new ApplicationData("httpxp",new Float(10.0f),MyApplicationData.getSourcePort(),MyComputer.getIP()),MyApplicationData.getSourceIP());
						else//We give extra XP based on the level of the player.
							MyComputerHandler.addData(new ApplicationData("httpxp",new Float(10.0f)+MyComputer.getHTTPLevel()*10.0f,MyApplicationData.getSourcePort(),MyComputer.getIP()),MyApplicationData.getSourceIP());

						
						if(MyComputer.getType()!=MyComputer.NPC){
							MyComputerHandler.addData(new ApplicationData("dailypayset",targetIP,0,MyComputer.getIP()),MyApplicationData.getSourceIP());
						}
						MyComputerHandler.addData(new ApplicationData("message",new Object[]{MessageHandler.CHANGE_DAILY_PAY_SUCCESS,new Object[]{},new Object[]{port,MyComputer.getIP()}},0,MyComputer.getIP()),MyApplicationData.getSourceIP());
						MyComputerHandler.addData(new ApplicationData("message",MessageHandler.CHANGE_DAILY_PAY_SUCCESS_GAME,0,MyComputer.getIP()),MyApplicationData.getSourceIP());
					}else
						MyComputerHandler.addData(new ApplicationData("message",new Object[]{MessageHandler.CHANGE_DAILY_PAY_FAIL_ALREADY_CONTROLLED,new Object[]{},new Object[]{port,MyComputer.getIP()}},0,MyComputer.getIP()),MyApplicationData.getSourceIP());
					
					MyComputer.setAdRevenueTarget(targetIP);
				}
			}else{
				MyComputerHandler.addData(new ApplicationData("message",new Object[]{MessageHandler.CHANGE_DAILY_PAY_SUCCESS,new Object[]{},new Object[]{port,MyComputer.getIP()}},0,MyComputer.getIP()),MyApplicationData.getSourceIP());
				MyComputerHandler.addData(new ApplicationData("message",MessageHandler.CHANGE_DAILY_PAY_SUCCESS_GAME,0,MyComputer.getIP()),MyApplicationData.getSourceIP());
			}
			resetPort(true,true);
			return;
		}
		//DESTROY ALL THE WATCHES ASSOCIATD WITH THIS PORT.
		if(MyApplicationData.getFunction().equals("destroyWatch")&&accessing.equals(MyApplicationData.getSourceIP())){
			if(!MyComputer.getEquipmentSheet().getDestroyWatchesImmune()){
			
				if(MyComputer.getType()!=Computer.NPC&&finalizeAllowed(MyApplicationData)){
					MyComputer.destroyWatches(number);
					MyComputerHandler.addData(new ApplicationData("message",MessageHandler.DESTROY_WATCHES_SUCCESS,0,MyComputer.getIP()),MyApplicationData.getSourceIP());
				}
				resetPort(true,true);
			}
			return;
		}
		//INITIALIZE AN ATTACK ON THIS PORT -- ONLY ONE INDIVIDUAL CAN ATTACK IT AT A TIME.
		if(MyApplicationData.getFunction().equals("attack")||MyApplicationData.getFunction().equals("mine")){//BETTER IF YOU'RE BLOCKED FOR A WHILE AFTER AN OVERHEAT.
			if(MyApplicationData.getFunction().equals("mine")&&type!=REDIRECT){
				MyComputerHandler.addData(new ApplicationData("message",MessageHandler.REDIRECT_FAIL_WRONG_TYPE,0,MyComputer.getIP()),MyApplicationData.getSourceIP());
				return;
			}
			String network="";//Make Sure the Player is on the proper network.
			if(MyApplicationData.getParameters() instanceof String)
				network=(String)MyApplicationData.getParameters();
			if(MyApplicationData.getParameters() instanceof String[])
				network=((String[])MyApplicationData.getParameters())[1];
			if(!MyComputer.getNetwork().equals(network)){
				String network2=MyComputer.getNetwork();
				MyComputerHandler.addData(new ApplicationData("message",new Object[]{MessageHandler.ATTACK_FAIL_WRONG_NETWORK,new Object[]{MyComputer.getIP(),network2}},0,MyComputer.getIP()),MyApplicationData.getSourceIP());
				return;
			}
			if(MyComputer.getTotalLevel()<MyComputer.getNoobSafety()){//Noob check.
				MyComputerHandler.addData(new ApplicationData("message",MessageHandler.ATTACK_FAIL_NOOB,0,MyComputer.getIP()),MyApplicationData.getSourceIP());
				return;
			}
			if(accessing.length()==0||((MyApplicationData.getSourceIP().equals(accessing)&&MyApplicationData.getSourcePort()==accessingPort)&&!weakened)){
				accessing=MyApplicationData.getSourceIP();
				accessingPort=MyApplicationData.getSourcePort();
				
				//Deal with the flip-flopping of commodities that can be used to get too much XP.
				if(MyComputer.getType()==MyComputer.NPC||!accessing.equals(currentRedirectIP)){
					currentRedirectXP=0.0f;
					currentRedirectIP=accessing;
				}
				
				//Send some information about this port.
				Float F[]=new Float[]{new Float(0.0f),new Float(health),new Float(MyComputer.getPettyCash()),new Float(getCPUCost())};
				Object O[]=new Object[]{F,new Boolean(MyComputer.getWatchHandler().checkForWatch(number)),new Boolean(MyComputer.getType()==MyComputer.NPC)};
				
				ApplicationData AD=null;
				if(MyApplicationData.getParameters() instanceof String){
					AD=new ApplicationData("attackinitialize",O,MyApplicationData.getSourcePort(),MyComputer.getIP());
					AD.setSourcePort(number);
					MyComputerHandler.addData(AD,MyApplicationData.getSourceIP());
				}else{
					AD=new ApplicationData("attackinitialize",O,MyApplicationData.getSourcePort(),MyComputer.getIP());
					AD.setSourcePort(number);
					MyComputerHandler.addData(AD,((String[])MyApplicationData.getParameters())[0]);
				}
					
				this.lastAccessed=currentTime;
			}else{	
				MyComputerHandler.addData(new ApplicationData("message",new Object[]{MessageHandler.PORT_ALREADY_UNDER_ATTACK,new Object[]{number,MyComputer.getIP(),accessing},new Object[]{MyApplicationData.getSourcePort(),accessing}},0,MyComputer.getIP()),MyApplicationData.getSourceIP());
			}
			return;
		}
		//CANCEL THE ATTACK ON THIS PORT.
		if(MyApplicationData.getFunction().equals("cancelattack")&&accessing.equals(MyApplicationData.getSourceIP())){
			Object parameters = MyApplicationData.getParameters();
			boolean heal = true;
			if(parameters!=null){
				heal = (boolean)(Boolean)parameters;
			}
			resetPort(heal,true);
			return;
		}
		
		//DEAL DAMAGE TO THIS PORT.
		if(MyApplicationData.getFunction().equals("freeze")){
			if(!MyComputer.getEquipmentSheet().getFreezeImmune()){
				if(accessing.equals(MyApplicationData.getSourceIP())){
					freeze=true;
					freezeStart=MyComputer.getCurrentTime();
				}
			}
			return;
		}
		
		//DEAL DAMAGE TO THIS PORT.
		if(MyApplicationData.getFunction().equals("damage")&&!getWeakened()){
			float initialHealth=health;//Check initial health used by commodity NPCs.
			Object[] parameters = (Object[])MyApplicationData.getParameters();
			float damage=(Float)parameters[0];//Get the amount of damage that has been dealt.
			String sourceIP = (String)parameters[1];
			int sourcePort = (Integer)parameters[2];
			boolean damageFromFireWall=(Boolean)parameters[3];//Did this damage originate from an attack back fire-wall?
			boolean zombieDamage=false;//Is the damage being dealt by a port that has been maliciously taken over?
			String zombieSource=(String)parameters[4];
			lastDamageWindowHandle = (Integer)parameters[5];
			lastDamageIP = MyApplicationData.getSourceIP();
			int currentCommodity = (Integer)parameters[6];
			if(zombieSource!=null){
				zombieDamage=true;
				//zombieSource=(String)((Object[])MyApplicationData.getParameters())[5];
			}
			
			//Calculate XP and deal damage.
			float modify = 0.0f;
			if(!zombieDamage){
				modify = MyFireWall.modifyDamage(damage,MyApplicationData.getSourceIP(),MyApplicationData.getSourcePort(),damageFromFireWall);
				damagePort(modify);
			}
			else{
				modify = MyFireWall.modifyDamage(damage,zombieSource,MyApplicationData.getSourcePort(),damageFromFireWall);
				damagePort(modify);
			}			

			float xp=damage;//Get as much XP as damage you deal.

			boolean mining=false;
			if(currentCommodity!=-1){//It must be a mining attack.			
				mining=true;
				//int currentCommodity=(Integer)((Object[])MyApplicationData.getParameters())[3];
				float currentAmount=MyComputer.getCommodity(currentCommodity);
				float sendAmount=1.0f;
				
				if(getHealth()<=0.0f&&MyComputer.getType()==Computer.NPC){//If this is an NPC type fork off the remaning resources after an attack.
					sendAmount=currentAmount;
				}
				
				//Make sure NPCs respawn.
				if(initialHealth==100.0&&MyComputer.getType()==MyComputer.NPC&&currentAmount<=0.0f){
					MyComputer.respawnCommodity(currentCommodity);
				}
				
				if(currentAmount>0.0f){
					if((100.0f-getHealth())>=100.0f/currentAmount){
						MyComputer.setCommodityAmount(currentCommodity,currentAmount-sendAmount);
						MyComputerHandler.addData(new ApplicationData("commodity",new Object[]{new Integer(currentCommodity),new Float(sendAmount),MyApplicationData.getSourcePort(),getIP()},MyApplicationData.getSourcePort(),MyComputer.getIP()),MyApplicationData.getSourceIP());
						//Make sure a user can not get too much XP in one round of redirecting.
						currentRedirectXP+=Computer.commodityXP[currentCommodity];
						if(currentRedirectXP<MAX_REDIRECT_XP){ 
							MyComputerHandler.addData(new ApplicationData("redirectxp",new Float(Computer.commodityXP[currentCommodity]*sendAmount),MyApplicationData.getSourcePort(),MyComputer.getIP()),MyApplicationData.getSourceIP());
						}else{
							MyComputerHandler.addData(new ApplicationData("message",new Object[]{MessageHandler.REDIRECT_XP_MAX,new Object[]{MyComputer.getIP()}},0,MyComputer.getIP()),MyApplicationData.getSourceIP());
						}
					}
				}
			}
			
			if(xp>0){//Send experience to the proper recepient.
				Float F[]=new Float[]{new Float(xp),new Float(health),new Float(MyComputer.getPettyCash()),new Float(getCPUCost()),new Float(modify)};
				Object O[]=null;
				if(!zombieDamage)
					O=new Object[]{F,new Boolean(MyComputer.getWatchHandler().checkForWatch(number)),damageFromFireWall,mining};
				else{
					O=new Object[]{F,new Boolean(MyComputer.getWatchHandler().checkForWatch(number)),zombieSource,damageFromFireWall,mining};
					MyComputerHandler.addData(new ApplicationData("opponentupdate",O,MyApplicationData.getSourcePort(),MyComputer.getIP()),zombieSource);
				}
				if(!mining)
					MyComputerHandler.addData(new ApplicationData("attackxp",O,MyApplicationData.getSourcePort(),MyComputer.getIP()),MyApplicationData.getSourceIP());
				else//Make sure we keep seeing damage.
					MyComputerHandler.addData(new ApplicationData("miningdamageupdate",O,MyApplicationData.getSourcePort(),MyComputer.getIP()),MyApplicationData.getSourceIP());

			}
			
			if(getHealth()<=0.0f&&accessing.equals(MyApplicationData.getSourceIP())){
				ApplicationData AD=new ApplicationData("attackfinalize",new Integer(type),MyApplicationData.getSourcePort(),MyComputer.getIP());
				AD.setSourcePort(getNumber());
				if(!zombieDamage)
					MyComputerHandler.addData(AD,MyApplicationData.getSourceIP());
				else
					MyComputerHandler.addData(AD,zombieSource);

				weakened=true;
				resetHealCounter();
			}else if(getHealth()<=0){
			   weakened=true;
			   resetHealCounter();
			}

			lastAccessed=currentTime;
			MyComputer.sendDamagePacket();
			
			return;
		}
		
		//INSTALL MALICOUS CODE ON THIS PORT. (ONE OPTION UPON COMPLETING ATTACK.
		if(MyApplicationData.getFunction().equals("installScript")&&accessing.equals(MyApplicationData.getSourceIP())){
			if(!MyFireWall.getInstallScriptFail(MyApplicationData.getSourceIP())){//Check whether installing a script failed.
				if(MyComputer.getType()!=Computer.NPC&&finalizeAllowed(MyApplicationData)){
					HashMap Script=(HashMap)((Object[])MyApplicationData.getParameters())[0];
					Object[] MaliciousParameters=(Object[])((Object[])MyApplicationData.getParameters())[1];
					
					//Set the parameters provided as part of this malicious program.
					if(type==BANKING){
						this.maliciousTarget=(String)MaliciousParameters[0];
						Banking B=(Banking)MyProgram;
						B.setPettyCashTarget((Float)MaliciousParameters[1]);
					}else if(type==FTP){
						this.maliciousTarget=(String)MaliciousParameters[0];
					}else if(type==ATTACK){
						AttackProgram A=(AttackProgram)MyProgram;
						A.setPettyCashTarget((Float)MaliciousParameters[1]);
						this.maliciousTarget=((String)MaliciousParameters[0]);
					}
					
					if(Script!=null&&MyProgram!=null){
						MyProgram.installScript(Script);
						MyComputerHandler.addData(new ApplicationData("message",new Object[]{MessageHandler.INSTALL_SCRIPT_SUCCESS,new Object[]{},new Object[]{lastDamageWindowHandle,MyComputer.getIP()}},0,MyComputer.getIP()),MyApplicationData.getSourceIP());
						MyComputerHandler.addData(new ApplicationData("message",new Object[]{MessageHandler.INSTALL_SCRIPT_SUCCESS_GAME,new Object[]{}},0,MyComputer.getIP()),MyApplicationData.getSourceIP());
					}
				}
				
			}
			resetPort(true,true);
			return;
		}
        
		if(!dummy){
			if(MyApplicationData.getFunction().equals("attackfinalize")){
				MyProgram.execute(MyApplicationData);
			}else if((!overHeated&&!freeze)||(MyApplicationData.getFunction().equals("requestsecondarydirectory"))){//Malget is a special case.
				if(!MyApplicationData.getFunction().equals("malget")){
					MyProgram.execute(MyApplicationData);
				}
				
			}else{
				if(MyApplicationData.getFunction().equals("zombieattack"))
					MyComputerHandler.addData(new ApplicationData("message",new Object[]{MessageHandler.PORT_IS_OVERHEATED,new Object[]{number,MyComputer.getIP()}},0,MyComputer.getIP()),MyApplicationData.getSourceIP());
				MyComputerHandler.addData(new ApplicationData("message",new Object[]{MessageHandler.PORT_IS_OVERHEATED,new Object[]{number,MyComputer.getIP()}},0,MyComputer.getIP()),MyComputer.getIP());
			}
		} else
			MyComputerHandler.addData(new ApplicationData("message",new Object[]{MessageHandler.PORT_WAS_DUMMY,new Object[]{number,MyComputer.getIP()},new Object[]{MyApplicationData.getSourcePort(),MyApplicationData.getSourceIP()}},0,MyComputer.getIP()),MyComputer.getIP());
	
		} else if(!MyApplicationData.getFunction().equals("logmessage")){
			setAttacking(false);
			MyComputerHandler.addData(new ApplicationData("message",new Object[]{MessageHandler.COULD_NOT_EXECUTE_APPLICATION,new Object[]{number}},0,MyComputer.getIP()),MyComputer.getIP());
		}
	}
	
	/**
	For idiots trying to share files with other idiots, return a finalize put in this situation.
	*/
	public void friendlyPut(ApplicationData MyApplicationData){
		if(MyApplicationData.getFunction().equals("finalizeput")){
			String name=(String)((Object[])MyApplicationData.getParameters())[1];
			String fetch_path=(String)((Object[])MyApplicationData.getParameters())[2];
			String password=(String)((Object[])MyApplicationData.getParameters())[4];
			HackerFile HF=(HackerFile)((Object[])MyApplicationData.getParameters())[5];
			MyComputerHandler.addData(new ApplicationData("message",MessageHandler.FTP_PUT_FAIL,0,this.getIP()),MyApplicationData.getSourceIP());
			HF.setLocation("");
			Object Parameters[]=new Object[]{fetch_path,HF};
			MyComputerHandler.addData(new ApplicationData("savefile",Parameters,0,this.getIP()),MyApplicationData.getSourceIP());
		}
	}
	
	/**
	Get the packet representation of this port.
	*/
	public PacketPort getPacketPort(){
		PacketPort returnMe=new PacketPort();
		returnMe.setNote(note);
		returnMe.setNumber(number);
		returnMe.setFireWall(MyFireWall.getType());
		returnMe.setType(type);
		returnMe.setOn(on);
		returnMe.setAttacking(attacking);
		returnMe.setCPUCost(getCPUCost());
		returnMe.setMaxCPUCost(getBaseCPUCostAndFirewall());
		returnMe.setHealth(health);
		returnMe.setDummy(dummy);
		returnMe.setDefault(0);
		if(getType()==PacketPort.BANKING&&getNumber()==MyComputer.getDefaultBank())
			returnMe.setDefault(1);
		if(getType()==PacketPort.ATTACK&&getNumber()==MyComputer.getDefaultAttack())
			returnMe.setDefault(1);
		if(getType()==PacketPort.REDIRECT&&getNumber()==MyComputer.getDefaultShipping())
			returnMe.setDefault(1);
		if(getType()==PacketPort.FTP&&getNumber()==MyComputer.getDefaultFTP())
			returnMe.setDefault(1);
		if(getType()==PacketPort.HTTP&&getNumber()==MyComputer.getDefaultHTTP())
			returnMe.setDefault(1);
		return(returnMe);
	}
	
	/**
	Output the contents of this class as an XML string.
	*/
	public String outputXML(){
		String returnMe="<port>\n";
		returnMe+="<number>"+number+"</number>\n";
		returnMe+="<type>"+type+"</type>\n";
		returnMe+="<health>"+health+"</health>";
		if(on)
			returnMe+="<onoff>1</onoff>\n";
		else
			returnMe+="<onoff>0</onoff>\n";
		returnMe+="<cpu>"+cpuCost+"</cpu>\n";
		if(note!=null)
			returnMe+="<note><![CDATA["+note.replaceAll("]]>","]]&gt;")+"]]></note>\n";
		else
			returnMe+="<note><![CDATA["+note+"]]></note>\n";
			
		returnMe+="<firewall>"+MyFireWall.getHackerFile().outputXML()+"</firewall>\n";
		if(dummy)
			returnMe+="<dummy>1</dummy>\n";
		else
			returnMe+="<dummy>0</dummy>\n";
			
		if(maliciousTarget!=null)
			returnMe+="<malicioustarget><![CDATA["+maliciousTarget.replaceAll("]]>","]]&gt;")+"]]></malicioustarget>\n";
		else
			returnMe+="<malicioustarget><![CDATA["+maliciousTarget+"]]></malicioustarget>\n";
		
		if(MyProgram!=null)
			returnMe+=MyProgram.outputXML();
		returnMe+="</port>\n";
		return(returnMe);
	}
}
