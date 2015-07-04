/**
AttackProgram.java

A mining program is almost exactly like an attack with a couple minor differences:

1) The damage is determined based on the mining level.
2) You can't use the zombie play mechanic.
*/

package Game;
import Hackscript.Model.*;
import java.io.*;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;

public class ShippingProgram extends Program{
	//TIMEOUT FOR HOW LONG A SINGLE ATTACK IS ALLOWED TO CONTINUE.
	private static long ATTACK_TIMEOUT=450000;
	private static double PWN_PERCENT=0.005;
	private long attackStart=0;
	
	private int currentCommodity=0;//The current commodity being mined for.

	//Installed Scripts.
	private String continueScript="";//Script that runs during each iteration of an attack.
	private String finalizeScript="";
	private String initializeScript="";
	
	private Port ParentPort=null;//The Port that this program is installed on.
	
	private ArrayList SecondaryTargets=new ArrayList();//An array of secondary targets to attack.
	private int currentTarget=0;
	
	private Computer MyComputer=null;//The computer that the parent port is attached to.
	private NetworkSwitch MyComputerHandler=null;//Computer handler for dispatching messages to other computers.
	private MakeBounty MyMakeBounty=null;//Used for checking bounties as an attack takes place.
	
	//State information.
	private String targetIP="";//IP address being targeted with attack.
	private boolean choicesShown=false;//Has the show choices dialogue been shown yet for this attack?
	private boolean isNPC=false;//Is the target of theh attack an NPC.
	private boolean switching=false;//Is the attack switching?
	private boolean dealDamage=true;//Should damage be dealth in this iteration?
	private int targetPort=0;//Port being targeted with attack.
	private int targetPortType=0;//Type of port being attacked.
	private int iterations=0;//Iterations of attack to date.
	
	private String MaliciousCode[][]=new String[4][2];//Potential malicious code to install.
	private Object MaliciousParameters[]=null;//Parameters to initialize malicious code with.
	//Bank Malicious IP : Bank Petty Cash Target : FTP Malicious IP : Petty Cash Target
	
	private int windowHandle = 0;
	
	//Constructor.
	public ShippingProgram(Computer MyComputer,Port ParentPort,NetworkSwitch MyComputerHandler,ArrayList Choices,MakeBounty MyMakeBounty){
		super.setComputerHandler(MyComputerHandler);
		super.setComputer(MyComputer);
		if(MyComputer!=null)
			this.attackStart=MyComputer.getCurrentTime();
		this.MyComputer=MyComputer;
		this.ParentPort=ParentPort;
		this.MyComputerHandler=MyComputerHandler;
	}
	
	/**
	Cancel an attack.
	*/
	public void cancelAttack(boolean heal){
		if(!getTargetIP().equals("")){
			MyComputerHandler.addData(new ApplicationData("cancelattack",new Boolean(heal),this.getTargetPort(),this.getIP()),this.getTargetIP());	
			this.setAttacking(false);
		}
	}
	
	/**
	Get the computer that this attack program is attached to.
	*/
	public Computer getComputer(){
		return(MyComputer);
	}
	
	/**
	Set whether or not the attack is currently switching.
	*/
	public void setSwitching(boolean switching){
		this.switching=switching;
	}
	
	/**
	Set whether or not this damage should be dealt in this iteration of attck.
	*/
	public void setDamage(boolean dealDamage){
		this.dealDamage=dealDamage;
	}
	
	/**
	Get the IP of the computer associated with this program.
	*/
	public String getIP(){
		return(MyComputer.getIP());
	}
	
	/**
	Get the port number of the port that this program is installed on.
	*/
	public int getPort(){
		return(ParentPort.getNumber());
	}
	
	/**
	Get the malicious IP associated with the parent port.
	*/
	public String getSavedMaliciousIP(){
		return(ParentPort.getMaliciousTarget());
	}
	
	/**
	Get the HP of the port being targeted with this attack.
	*/
	public float getTargetHP(){
		return(ParentPort.getTargetHP());
	}
	
	/**
	Set the target port of this attack.
	*/
	public void setTargetPort(int targetPort){
		this.targetPort=targetPort;
	}
	
	/**
	Get the CPU load on the computer that this attack is associated with.
	*/
	public float getCPULoad(){
		return(MyComputer.getCPULoad());
	}
	
	/**
	Get the maximum CPU load of the current CPU installed
	*/
	public float getMaximumCPULoad(){
		return(MyComputer.getMaximumCPULoad());
	}
	
	/**
	Get whether the parent port is currently in an attacking state.
	*/
	public boolean getAttacking(){
		return(ParentPort.getAttacking());
	}
	
	/**
	Get the port that this attack is targeting.
	*/
	public int getTargetPort(){
		return(targetPort);
	}
	
	/**
	Get the HP of the port that this program is attached to.
	*/
	public float getHP(){
		return(ParentPort.getHealth());
	}
	
	/**
	Get the target IP of the current attack taking place.
	*/
	public String getTargetIP(){
		return(targetIP);
	}
	
	/**
	Get the CPU cost of the port being attacked.
	*/
	public float getTargetCPUCost(){
		return(ParentPort.getTargetCPUCost());
	}
	
	/**
	Get whether the port being targeted with an attack has a watch installed.
	*/
	public boolean getTargetWatch(){
		return(ParentPort.getTargetWatch());
	}
	
	/**
	Set whether or not this attacks' parent port is still attacking.
	*/
	public void setAttacking(boolean attacking){
		ParentPort.setAttacking(attacking);
		if(!attacking){
			windowHandle = 0;
		}
	}
	
	/**
	installScript(HashMap Script);
	Installs a script on the various entrance points on this program.
	*/
	public void installScript(HashMap Script){
		continueScript=(String)Script.get("continue");
		initializeScript=(String)Script.get("initialize");
		finalizeScript=(String)Script.get("finalize");
	}
	
	/**
	Return how many times the current attack has iterated.
	*/
	public int getIterations(){
		return(iterations);
	}
	
	/**
	Sets the current commodity being mined for.
	*/
	public void setCurrentCommodity(int currentCommodity){
	//	if(!getAttacking()){
			this.currentCommodity=currentCommodity;
	//	}
	}
	
	/**
	Returns the current commodity being mined.
	*/
	public int getCurrentCommodity(){
		return(currentCommodity);
	}
	
	public int getWindowHandle(){
		return(windowHandle);
	}
	
	public void execute(ApplicationData MyApplicationData){
	
		if(getAttacking()&&MyComputer.getCurrentTime()-attackStart>ATTACK_TIMEOUT){//Attacks can only take up to 5 minutes.
			
			MyComputerHandler.addData(new ApplicationData("cancelattack",null,this.getTargetPort(),this.getIP()),this.getTargetIP());
			MyComputerHandler.addData(new ApplicationData("message",MessageHandler.REDIRECT_EXCEEDED_MAXIMUM_TIMEOUT,0,this.getIP()),this.getIP());
	
			setAttacking(false);
			return;
		}
		
		String execute=null;
		if(MyApplicationData.getFunction().equals("attackcontinue")){
			iterations++;
			execute=continueScript;
			float damage=MyComputer.getDamage("Redirecting");
			
			try{
				HackerLinker HL=new HackerLinker(this,MyComputerHandler);
				RunFactory.runCode(execute,HL,MyComputer.MAX_OPS);
			}catch(Exception e){
			}
			
			if(dealDamage){//Should the port deal damage this iteration.
				Object O[]=new Object[]{new Float(damage+MyComputer.getEquipmentSheet().getMiningBonus()),ParentPort.getIP(),new Integer(ParentPort.getNumber()),false,null,windowHandle,new Integer(getCurrentCommodity())};
				ApplicationData AD=new ApplicationData("damage",O,targetPort,MyComputer.getIP());
				AD.setSourcePort(ParentPort.getNumber());
				MyComputerHandler.addData(AD,targetIP);
			}
			dealDamage=true;
			
			return;
		}
		
		else if(MyApplicationData.getFunction().equals("attackinitialize")&&!switching&&!ParentPort.getAttacking()){
			MyComputerHandler.addData(new ApplicationData("pettycash",new Float(-10.0f),0,MyComputer.getIP()),MyComputer.getIP());
			choicesShown=false;
			attackStart=MyComputer.getCurrentTime();
			
			iterations=0;
			
			Float F[]=(Float[])(((Object[])MyApplicationData.getParameters())[0]);
			boolean targetWatch=(Boolean)(((Object[])MyApplicationData.getParameters())[1]);
			if(((Object[])MyApplicationData.getParameters()).length>2)
				isNPC=(Boolean)((Object[])MyApplicationData.getParameters())[2];
			
			ParentPort.setTargetHP(F[1]);
			ParentPort.setTargetPettyCash(F[2]);
			ParentPort.setTargetCPUCost(F[3]);
			ParentPort.setTargetWatch(targetWatch);		
			
			targetIP=MyApplicationData.getSourceIP();
			targetPort=MyApplicationData.getSourcePort();
			execute=initializeScript;
			
			setAttacking(true);
			
			try{
				HackerLinker HL=new HackerLinker(this,MyComputerHandler);
				RunFactory.runCode(execute,HL,MyComputer.MAX_OPS);
			}catch(Exception e){
			}
				
			if(!MyComputer.checkBank()){
				MyComputerHandler.addData(new ApplicationData("cancelattack",null,this.getTargetPort(),this.getIP()),this.getTargetIP());
				this.setAttacking(false);
				return;
			}
		}
		
		else if(MyApplicationData.getFunction().equals("attackfinalize")&&ParentPort.getAttacking()){		
			targetPortType=(Integer)MyApplicationData.getParameters();
			targetPort=MyApplicationData.getSourcePort();
		
			Iterator MyIterator=SecondaryTargets.iterator();
			while(MyIterator.hasNext()){//Remove this port from our list of secondary targets.
				int tempPort=(Integer)MyIterator.next();
				if(tempPort==targetPort)
					MyIterator.remove();
			}
			
	
			try{
				HackerLinker HL=new HackerLinker(this,MyComputerHandler);
				RunFactory.runCode(finalizeScript,HL,MyComputer.MAX_OPS);
			}catch(Exception e){
			}
			
			MyComputer.addMessage(MessageHandler.REDIRECT_FINISHED,new Object[]{getPort()},new Object[]{windowHandle,getIP()});
			MyComputer.addMessage(MessageHandler.REDIRECT_FINISHED_GAME,new Object[]{getPort()});
			
			MyComputerHandler.addData(new ApplicationData("cancelattack",null,this.getTargetPort(),this.getIP()),this.getTargetIP());
			MyComputer.incrementSuccessfulHacks();
			setAttacking(false);						
			return;
		}
		
		//It has been requested that an attack be initialized on an opponent's computer (REGULAR).
		else if(MyApplicationData.getFunction().equals("requestattack")){
			Object[] parameters = (Object[])MyApplicationData.getParameters();
			int windowHandle = (Integer)parameters[5];
			if(ParentPort.getAttacking()){
				MyComputerHandler.addData(new ApplicationData("message",MessageHandler.REDIRECT_FAIL_ALREADY_REDIRECTING,0,this.getIP()),this.getIP());
				return;
			}
			if(ParentPort.getOverHeated()){
				MyComputerHandler.addData(new ApplicationData("message",MessageHandler.REDIRECT_FAIL_OVERHEATED,0,this.getIP()),this.getIP());
				return;
			}
			
			switching=false;
			this.windowHandle = windowHandle;
			//Check whether you have enough money in your account to perform an attack.
			if(!MyComputer.checkBank()){
				MyComputer.addMessage(MessageHandler.ACTIVE_BANK_NOT_FOUND);
			}else if(MyComputer.getPettyCash()>=10.0f){
			
				targetIP=(String)((Object[])MyApplicationData.getParameters())[0];
				targetPort=(Integer)((Object[])MyApplicationData.getParameters())[1];
				
				if(((Object[])MyApplicationData.getParameters()).length>2){//Add any secondary targets.
					SecondaryTargets=new ArrayList();
					SecondaryTargets.add(new Integer(targetPort));
					currentTarget=0;

				
					Integer I[]=(Integer[])((Object[])MyApplicationData.getParameters())[2];
					for(int i=0;i<I.length;i++)
						SecondaryTargets.add(I[i]);
				}
				
				if(((Object[])MyApplicationData.getParameters()).length>3){//Add malicious scripts
					MaliciousCode=(String[][])((Object[])MyApplicationData.getParameters())[3];
				}
				
				ApplicationData AD=new ApplicationData("mine",MyComputer.getNetwork(),targetPort,MyApplicationData.getSourceIP());
				AD.setSourcePort(ParentPort.getNumber());
				MyComputerHandler.addData(AD,targetIP);
				
			}else
				MyComputer.addMessage(MessageHandler.REDIRECT_FAIL_NOT_ENOUGH_MONEY);

			return;
		}
		else if(MyApplicationData.getFunction().equals("requestcancelattack")&&ParentPort.getAttacking()&&(MyApplicationData.getSourceIP().equals(ParentPort.getIP()))){
			MyComputerHandler.addData(new ApplicationData("cancelattack",null,this.getTargetPort(),this.getIP()),this.getTargetIP());
			this.setAttacking(false);
			return;
		}
		
		else
			return;
	
	}
	
	/**
	Return the array list of secondary attack targets.
	*/
	public int nextTarget(){
		if(SecondaryTargets.size()==0)
			return(-1);
		currentTarget=(1+currentTarget)%SecondaryTargets.size();
		int returnMe=(Integer)SecondaryTargets.get(currentTarget);
		
		return(returnMe);
	}
	
	/**
	Returns the last file that was accessed via getMaliciousCode()
	*/
	HackerFile LastFile=null;
	public HackerFile getLastFile(){
		return(LastFile);
	}
	
	/**
	Return the program that should be installed based on type and malicious code.
	*/
	public HashMap getMaliciousCode(){
	
		if(MaliciousCode[targetPortType]==null)
			return(null);

		HackerFile HF=MyComputer.getFileSystem().getFile(MaliciousCode[targetPortType][0],MaliciousCode[targetPortType][1]);
		
		if(HF==null)
			return(null);
			
		LastFile=HF;
			
		HF.setQuantity(HF.getQuantity()-1);
		if(HF.getQuantity()<=0){
			MyComputer.getFileSystem().deleteFile(MaliciousCode[targetPortType][0],MaliciousCode[targetPortType][1]);
		}
			
		return(HF.getContent());
	}
	
	/**
	Returns the keys associated with this program type.
	*/
	public String[] getTypeKeys(){
		String returnMe[]=new String[]{"continue","initialize","finalize"};
		return(returnMe);
	}
	
	
	/**
	Return a hash map representation of the program currently installed on this port.
	*/
	public HashMap getContent(){
		HashMap returnMe=new HashMap();
		returnMe.put("continue",continueScript);
		returnMe.put("initialize",initializeScript);
		returnMe.put("finalize",finalizeScript);
		return(returnMe);
	}
	
	/**
	Get the parameters provided for installing malicious scripts.
	*/
	public Object[] getMaliciousParameters(){
		return(MaliciousParameters);
	}
	
	/**
	Output the class data in XML format.
	*/
	public String outputXML(){
		String returnMe="<code>\n";
		returnMe+="<initialize><![CDATA[";
		if(initializeScript!=null)
			returnMe+=initializeScript.replaceAll("]]>","]]&gt;");
		else
			returnMe+=initializeScript;
		returnMe+="]]></initialize>\n";
		returnMe+="<continue><![CDATA[";
		if(continueScript!=null)
			returnMe+=continueScript.replaceAll("]]>","]]&gt;");
		else
			returnMe+=continueScript;
		returnMe+="]]></continue>\n";
		returnMe+="<finalize><![CDATA[";
		if(finalizeScript!=null)
			returnMe+=finalizeScript.replaceAll("]]>","]]&gt;");
		else
			returnMe+=finalizeScript;
		returnMe+="]]></finalize>\n";
		returnMe+="</code>\n";
		return(returnMe);
	}
}
