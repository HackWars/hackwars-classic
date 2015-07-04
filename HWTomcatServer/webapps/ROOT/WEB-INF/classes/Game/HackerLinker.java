/*
HackerLinker.java

Contains all the functions available to a player using the Hack Wars scriptting language.
 */

package Game;
import java.util.ArrayList;
import Hackscript.Model.*;
import util.*;
import java.util.*;

public class HackerLinker extends Linker{
    
    //the global variables
    public Object globalVars[];
    
    //the functions
	
    public int codeCounter = 0;
    private NetworkSwitch MyComputerHandler=null;
	private Program MyProgram=null;
    private Object theLinker = null;
	private HashMap SpamTracker=new HashMap();
    
    /** Creates a new instance of structInfo */
    public HackerLinker(Program MyProgram,NetworkSwitch MyComputerHandler) {
		this.MyProgram=MyProgram;
		this.MyComputerHandler=MyComputerHandler;
        this.globalVars = globalVars;
        this.theLinker = theLinker;
        codeCounter = 0;
    }
	
	/**
	Escape regex.
	*/
	public static String regexEscape(String data){
		data=data.replaceAll("\\\\", "\\\\\\\\");
		data=data.replaceAll("\\.", "\\\\.");
		data=data.replaceAll("\\$", "\\\\\\$");
		data=data.replaceAll("\\^", "\\\\^");
		data=data.replaceAll("\\{", "\\\\{");
		data=data.replaceAll("\\[", "\\\\[");
		data=data.replaceAll("\\(", "\\\\(");
		data=data.replaceAll("\\|", "\\\\|");
		data=data.replaceAll("\\)", "\\\\)");
		data=data.replaceAll("\\*", "\\\\*");
		data=data.replaceAll("\\+", "\\\\+");
		data=data.replaceAll("\\?", "\\\\?"); 
		return(data);
	}
	
	//incrementValue the spamming counter.
	public int incrementValue(String key){
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
		return(value);
	}
    
    public Variable runFunction(String name, ArrayList parameters){

		Object result=null;
		try{
			
			/**
			Our Banking API.
			*/
			if(MyProgram instanceof Banking){
				Banking B=(Banking)MyProgram;
				float pettyCash=B.getPettyCash();
				float amount=B.getAmount();
				
				//Deposit money into your account.
				boolean isDeposit=false;
				float depositXP=50.0f;
				float depositCut=0.8f;
				if(name.equals("lowerDeposit")){
					isDeposit=true;
				}
				if(name.equals("mediumDeposit")){
					isDeposit=true;
					depositXP=33.0f;
					depositCut=0.83f;
				}
				if(name.equals("higherDeposit")){
					isDeposit=true;
					depositXP=25.0f;
					depositCut=0.86f;
				}
				
				if(name.equals("greaterDeposit")){
					isDeposit=true;
					depositXP=20.0f;
					depositCut=0.89f;
				}
				
				depositCut+=B.getComputer().getEquipmentSheet().getBankingBonus();
				
				//The scripter is using a function that deposits into a user's bank account.
				if(isDeposit&&B.isDeposit()){
					try{
						if(parameters!=null)
						if(parameters.size()==2){
							String ip=null;
							float deposit=0;
							if(parameters.get(0) instanceof TypeString)
								ip=((TypeString)parameters.get(0)).getStringValue();
							if(parameters.get(1) instanceof TypeFloat)
								deposit=(Float)((TypeFloat)parameters.get(1)).getRawValue();

							if(ip!=null&&deposit>0.0f){
								if(deposit>pettyCash){
									deposit=pettyCash;
								}
								
								B.setAmount(B.getAmount()-deposit);
								B.setPettyCash(B.getPettyCash()-deposit);
								
								String banking_ip=B.getIP();
								if(!ip.equals(B.getIP())){
									ApplicationData MyBank=new ApplicationData("bank",new Float(deposit*depositCut),0,ip);
									MyComputerHandler.addData(MyBank,ip);
								}else{
									CentralLogging.getInstance().addOutput(ip+"\t"+ip+"\t"+"0\t"+deposit+"\n");
									B.deposit(deposit*depositCut);
									ApplicationData MyBank=new ApplicationData("bank",new Float(0.0f),0,ip);
									MyComputerHandler.addData(MyBank,ip);
								}
								
								MyComputerHandler.addData(new ApplicationData("bankxp",new Float(deposit/depositXP),0,ip),ip);

								Computer C=B.getComputer();
								if(!C.getIP().equals(ip))//Send a packet if something malicious happens.
									C.sendPacket();
							}
						}
					}catch(Exception e){
						MyProgram.getComputer().addMessage("An exception occurred in deposit().");
					}
				}
				
				//Withdraw money from your account.
				if(name.equals("withdraw")){
					try{
					if(B.isWithdraw()){
						if(parameters!=null)
						if(parameters.size()==2){
							String ip=null;
							float deposit=0;
							if(parameters.get(0) instanceof TypeString)
								ip=((TypeString)parameters.get(0)).getStringValue();
							if(parameters.get(1) instanceof TypeFloat)
								deposit=(Float)((TypeFloat)parameters.get(1)).getRawValue();
								
							if(deposit>=0.0f){
							
								if(deposit>amount){
									deposit=amount;
								}
								if(deposit>B.getBankMoney()){
									deposit=B.getBankMoney();
								}
										
								if(ip!=null&&deposit<=amount){
									String banking_ip=B.getIP();
									
									ApplicationData MyPettyCash=new ApplicationData("pettycash",new Float((deposit)),0,B.getComputer().getIP());
									
									if(!ip.equals(B.getComputer().getIP())){
										CentralLogging.getInstance().addOutput(B.getComputer().getIP()+"\t"+ip+"\t"+"1\t"+deposit+"\n");
									}
									
									B.setAmount(amount-deposit);
									B.setBank(B.getBank()-deposit);

									MyComputerHandler.addData(MyPettyCash,ip);
									
									Computer C=B.getComputer();
									if(!C.getIP().equals(ip))//Send a packet if something malicious happens.
										C.sendPacket();
								}
							}
						}
					}
					}catch(Exception e){
						MyProgram.getComputer().addMessage("An exception occurred in withdraw().");
					}
				}
				
				//Transfer function.
				boolean isTransfer=false;
				float transferXP=50.0f;
				float transferCut=0.8f;
				if(name.equals("lowerTransfer")){
					isTransfer=true;
				}
				if(name.equals("mediumTransfer")){
					isTransfer=true;
					transferXP=33.0f;
					transferCut=0.83f;
				}
				if(name.equals("higherTransfer")){
					isTransfer=true;
					transferXP=25.0f;
					transferCut=0.86f;
				}
				if(name.equals("greaterTransfer")){
					isTransfer=true;
					transferXP=20.0f;
					transferCut=0.89f;
				}

				transferCut+=B.getComputer().getEquipmentSheet().getBankingBonus();
				
				//User is using a function that transfers money to another user.
				if(isTransfer&&B.isTransfer()){
					try{
						if(parameters!=null)
						if(parameters.size()==2){
                                  
							String ip=null;
							float deposit=0;
							if(parameters.get(0) instanceof TypeString)
								ip=((TypeString)parameters.get(0)).getStringValue();
							if(parameters.get(1) instanceof TypeFloat)
								deposit=(Float)((TypeFloat)parameters.get(1)).getRawValue();
                            
							if(ip!=null&&deposit>0.0f){
								if(deposit>pettyCash){
									deposit=pettyCash;
								}
                                String banking_ip=B.getIP();

                                // make sure a non-dummy bank port is on the person being transferred to
								B.setAmount(B.getAmount()-deposit);
								B.setPettyCash(B.getPettyCash()-deposit);
								if(!ip.equals(B.getComputer().getIP())){
									CentralLogging.getInstance().addOutput(B.getComputer().getIP()+"\t"+ip+"\t"+"1\t"+deposit+"\n");
								}
								
								
								ApplicationData MyBank=new ApplicationData("pettycash",new Object[]{new Float(deposit*transferCut),deposit},0,B.getComputer().getIP());

								MyComputerHandler.addData(MyBank,ip);
								MyComputerHandler.addData(new ApplicationData("bankxp",new Float(deposit/transferXP),0,banking_ip),banking_ip);
								
								Computer C=B.getComputer();
								if(!C.getIP().equals(ip))//Send a packet if something malicious happens.
									C.sendPacket();
							}
						}
					}catch(Exception e){
						MyProgram.getComputer().addMessage("An exception occurred in transfer().");
					}
				}
				
				//Return the amount of money that has been requested to transfer.
				if(name.equals("getAmount")){
					try{
					return(new TypeFloat(B.getInitialAmount()));
					}catch(Exception e){
						MyProgram.getComputer().addMessage("An exception occurred in getAmount().");
					}
				}
				
				//Return the IP address of the individual running this script.
				if(name.equals("getSourceIP")){
					try{
					return(new TypeString(B.getIP()));
					}catch(Exception e){
						MyProgram.getComputer().addMessage("An exception occurred in getSourceIP().");
					}
				}
				
				//Return the IP address of the person you're transferring too.
				if(name.equals("getTargetIP")){
					try{
					return(new TypeString(B.getTargetIP()));
					}catch(Exception e){
						MyProgram.getComputer().addMessage("An exception occurred in getTargetIP().");
					}
				}
				
				//Get the target amount for stealing from petty cash.
				if(name.equals("checkPettyCashTarget")){
					try{
					return(new TypeFloat(B.getPettyCashTarget()));
					}catch(Exception e){
						MyProgram.getComputer().addMessage("An exception occurred in checkPettyCashTarget().");
					}
				}

				//Return the amount currently in this player's petty cash.
				if(name.equals("checkPettyCash")){
					try{
					return(new TypeFloat(B.getPettyCash()));
					}catch(Exception e){
						MyProgram.getComputer().addMessage("An exception occurred in checkPettyCash().");
					}
				}
				
				//Return the malicious IP.
				if(name.equals("getMaliciousIP")){
					try{
					return(new TypeString(B.getMaliciousTarget()));
					}catch(Exception e){
						MyProgram.getComputer().addMessage("An exception occurred in getMaliciousIP().");
					}
				}
				
		/**
		Our Watch API.
		*/
		}else if(MyProgram instanceof WatchProgram){
				WatchProgram W=(WatchProgram)MyProgram;
				
				if(name.equals("sendEmail")){
					try{
					String message="";
					if(parameters.get(0) instanceof TypeString)
						message=((TypeString)parameters.get(0)).getStringValue();
					MyComputerHandler.addData(new ApplicationData("sendemail",message,0,W.getIP()),W.getIP());
					}catch(Exception e){
						MyProgram.getComputer().addMessage("An exception occurred in sendEmail().");
					}
				}else
				
				if(name.equals("sendFacebookMessage")){
					try{
					String message="";
					if(parameters.get(0) instanceof TypeString)
						message=((TypeString)parameters.get(0)).getStringValue();
					Object O[]=new Object[]{message,W.getTargetIP()};
					MyComputerHandler.addData(new ApplicationData("sendfacebook",O,0,W.getIP()),W.getIP());
					}catch(Exception e){
						MyProgram.getComputer().addMessage("An exception occurred in sendFacebookMessage().");
					}
				}else
				
				if(name.equals("logMessage")){
					try{
					String Content="";
					if(parameters.get(0) instanceof TypeString)
						Content=((TypeString)parameters.get(0)).getStringValue();
					MyProgram.getComputer().logMessage(Content,W.getTargetIP(),-1L);
					}catch(Exception e){
						MyProgram.getComputer().addMessage("An exception occurred in logMessage().");
					}
				}else
				
				//Return the default attack port associated with this computer.
				if(name.equals("getDefaultAttack")){
					try{
					return(new TypeInteger(W.getDefaultAttack()));
					}catch(Exception e){
						MyProgram.getComputer().addMessage("An exception occurred in getDefaultAttack().");
					}
				}else
				
				//Return the default attack port associated with this computer.
				if(name.equals("getDefaultHTTP")){
					try{
					return(new TypeInteger(W.getDefaultHTTP()));
					}catch(Exception e){
						MyProgram.getComputer().addMessage("An exception occurred in getDefaultHTTP().");
					}
				}else
				
				//Return the default attack port associated with this computer.
				if(name.equals("getDefaultFTP")){
					try{
					return(new TypeInteger(W.getDefaultFTP()));
					}catch(Exception e){
						MyProgram.getComputer().addMessage("An exception occurred in getDefaultFTP().");
					}
				}else
				
				//Cancel an attack currently taking place from your computer.
				if(name.equals("cancelAttack")){
					try{
					int port=-1;
					if(parameters.size()>0)
						port=((TypeInteger)parameters.get(0)).getIntValue();
					if(port>=0){
						MyComputerHandler.addData(new ApplicationData("requestcancelattack",null,port,W.getIP()),W.getIP());
					}
					}catch(Exception e){
						MyProgram.getComputer().addMessage("An exception occurred in cancelAttack().");
					}
				}else
				
				
				//Return the port that the parent watch is associated with.
				if(name.equals("getPort")){
					try{
					return(new TypeInteger(W.getNumber()));
					}catch(Exception e){
						MyProgram.getComputer().addMessage("An exception occurred in getPort().");
					}
				}else
				
				//Return the port that the parent watch is associated with.
				if(name.equals("scan")){
					if(W.getParentWatch().getExternal()){//Only allow this function if it came from triggerWatch.
						try{
							String targetIP=((TypeString)parameters.get(0)).getStringValue();
							ApplicationData Test=new ApplicationData("requestscan",W.getIP(),0,W.getIP());
							MyComputerHandler.addData(Test,targetIP);
						}catch(Exception e){
							MyProgram.getComputer().addMessage("An exception occurred in scan().");
						}
					}else
						W.getComputer().addMessage(MessageHandler.FUNCTION_ONLY_WITH_TRIGGERWATCH);
				}else
				
				//Initialize an attack on the default attack port.
				if(name.equals("counterattack")){
					try{
					int attackport=W.getDefaultAttack();
					if(parameters.size()>0)
						attackport=((TypeInteger)parameters.get(0)).getIntValue();
					
					int targetPort=W.getTargetPort();
					String targetIP=W.getTargetIP();
					Integer I[]=new Integer[0];
					String S[][]={null,null,null};
					Object o=new Object[]{targetIP,new Integer(targetPort),I,S,null,0};
					ApplicationData Test=new ApplicationData("requestattack",o,attackport,W.getIP());
					MyComputerHandler.addData(Test,W.getIP());
					}catch(Exception e){
						MyProgram.getComputer().addMessage("An exception occurred in counterattack().");
					}
				}else
				
				//Initialize an attack on the default attack port.
				if(name.equals("attack")){
					try{
						//if(W.getParentWatch().getExternal()){//Only allow this function if it came from triggerWatch.
							int attackport=W.getDefaultAttack();
							attackport=((TypeInteger)parameters.get(0)).getIntValue();
							
							int targetPort=((TypeInteger)parameters.get(2)).getIntValue();
							String targetIP=((TypeString)parameters.get(1)).getStringValue();
							Integer I[]=new Integer[0];
							String S[][]={null,null,null};
							Object o=new Object[]{targetIP,new Integer(targetPort),I,S,null,0};

							if(!targetIP.equals(W.getIP())&&targetIP.indexOf("store")==-1){
								ApplicationData Test=new ApplicationData("requestattack",o,attackport,W.getIP());
								MyComputerHandler.addData(Test,W.getIP());
							}
						/*}else
							W.getComputer().addMessage(MessageHandler.FUNCTION_ONLY_WITH_TRIGGERWATCH);*/

					}catch(Exception e){
						MyProgram.getComputer().addMessage("An exception occurred in attack().");
					}
				}else
				
				//Initialize an attack on the default attack port.
				if(name.equals("zombieAttack")){
					if(W.getParentWatch().getExternal()){//Only allow this function if it came from triggerWatch.
						try{
						
						String sourceIP=((TypeString)parameters.get(0)).getStringValue();
						int sourcePort=((TypeInteger)parameters.get(1)).getIntValue();
						int targetPort=((TypeInteger)parameters.get(3)).getIntValue();;
						String targetIP=((TypeString)parameters.get(2)).getStringValue();
						Integer I[]=new Integer[0];
						String S[][]=null;
						
						if(targetIP.indexOf("store")==-1){
							Object Parameters[]=new Object[]{targetIP,new Integer(targetPort),I,S,new Object[]{"",new Float(0),"",new Float(0),""},sourceIP};
							ApplicationData AD=new ApplicationData("requestzombieattack",Parameters,sourcePort,W.getIP());					
							MyComputerHandler.addData(AD,W.getIP());
						}
						
						}catch(Exception e){
							MyProgram.getComputer().addMessage("An exception occurred in zombieAttack().");
						}
					}else
							W.getComputer().addMessage(MessageHandler.FUNCTION_ONLY_WITH_TRIGGERWATCH);
				}else
				
				//Initialize an attack on the default bank port.
				if(name.equals("counterattackBank")){
					try{
					int	counterport=((TypeInteger)parameters.get(0)).getIntValue();
					String targetIP=W.getTargetIP();
					int defaultAttack=W.getDefaultAttack();
					Integer I[]=new Integer[0];
					String S[][]={null,null,null};
					Object o=new Object[]{targetIP,"Bank",I,S};
					ApplicationData Test=new ApplicationData("requestattackdefault",o,counterport,W.getIP());
					MyComputerHandler.addData(Test,targetIP);
					}catch(Exception e){
						MyProgram.getComputer().addMessage("An exception occurred in counterattackBank().");
					}
				}else
				
				//Initialize an attack on the default attack port.
				if(name.equals("counterattackAttack")){
					try{
					int	counterport=((TypeInteger)parameters.get(0)).getIntValue();
					String targetIP=W.getTargetIP();
					int defaultAttack=W.getDefaultAttack();
					Integer I[]=new Integer[0];
					String S[][]={null,null,null};
					Object o=new Object[]{targetIP,"Attack",I,S};
					ApplicationData Test=new ApplicationData("requestattackdefault",o,counterport,W.getIP());
					MyComputerHandler.addData(Test,targetIP);
					}catch(Exception e){
						MyProgram.getComputer().addMessage("An exception occurred in counterattackAttack.");
					}
				}else
				
				//Check whether or not a fire wall is installed on the port being watched.
				if(name.equals("checkForFireWall")){
					try{
					String FireWallName="";
					if(parameters.get(0) instanceof TypeString)
						FireWallName=((TypeString)parameters.get(0)).getStringValue();
					
					return(new TypeInteger(W.checkForFireWall(FireWallName)));
					}catch(Exception e){
						MyProgram.getComputer().addMessage("An exception occurred in checkForFireWall().");
					}
				}else
				
				//Check how much money is currently in the petty cash being watched.
				if(name.equals("checkPettyCash")){
					try{
					return(new TypeFloat(W.getPettyCash()));
					}catch(Exception e){
						MyProgram.getComputer().addMessage("An exception occurred in checkPettyCash().");
					}
				}else
				
				//Switch a fire wall from port x to the current port.
				if(name.equals("switchFireWall")){
					try{
					int port=-1;
					if(parameters.get(0) instanceof TypeInteger)
						port=((TypeInteger)parameters.get(0)).getIntValue();
					W.switchFireWall(port);
					}catch(Exception e){
						MyProgram.getComputer().addMessage("An exception occurred in switchFireWall().");
					}
				}else
				
				//Search for any fire wall and place it on this port.
				if(name.equals("switchAnyFireWall")){
					try{
					W.switchAnyFireWall();
					}catch(Exception e){
						MyProgram.getComputer().addMessage("An exception occurred in switchAnyFireWall().");
					}
				}else
				
				//Deposit petty cash into bank as defense.
				if(name.equals("depositPettyCash")){
					try{
					float amount=W.getPettyCash();
					if(parameters.size()!=0)
					if(parameters.get(0) instanceof TypeFloat)
						amount=(Float)((TypeFloat)parameters.get(0)).getRawValue();
					int port = W.getParentWatch().getNumber();
					MyComputerHandler.addData(new ApplicationData("deposit",amount,port,W.getIP()),W.getIP());
					}catch(Exception e){
						MyProgram.getComputer().addMessage("An exception occurred in depositPettyCash().");
					}
				}else
				
				//Deposit petty cash into bank as defense.
				if(name.equals("transferMoney")){
					try{
					String  target_ip=(String)((TypeString)parameters.get(0)).getStringValue();
					float amount=(Float)((TypeFloat)parameters.get(1)).getRawValue();
					Object tO[]=new Object[]{target_ip,new Float(amount)};
					MyComputerHandler.addData(new ApplicationData("transfer",tO,W.getDefaultBank(),W.getIP()),W.getIP());
					}catch(Exception e){
						MyProgram.getComputer().addMessage("An exception occurred in transferMoney.");
					}
				}else
				
				//Check for a fire wall of the given name.
				if(name.equals("checkFireWall")){
					try{
					String FireWallName="";
					if(parameters.get(0) instanceof TypeString)
						FireWallName=((TypeString)parameters.get(0)).getStringValue();
					return(new TypeBoolean(W.checkFireWall(FireWallName)));
					}catch(Exception e){
						MyProgram.getComputer().addMessage("An exception occurred in checkFireWall().");
					}
				}else
				
				//Shut down all observed ports.
				if(name.equals("shutDownPorts")){
					try{
					W.shutDownPorts();
					}catch(Exception e){
						MyProgram.getComputer().addMessage("An exception occurred in shutDownPorts().");
					}
				}else
				
				//Shut down the port at the number provided.
				if(name.equals("shutDownPort")){
					try{
					int port=-1;
					if(parameters.get(0) instanceof TypeInteger)
						port=((TypeInteger)parameters.get(0)).getIntValue();
					W.shutDownPort(port);
					}catch(Exception e){
						MyProgram.getComputer().addMessage("An exception occurred in shutDownPort().");
					}
				}else
				
				//Shut down all observed ports.
				if(name.equals("turnOnPorts")){
					try{
						W.turnOnPorts();
					}catch(Exception e){
						MyProgram.getComputer().addMessage("An exception occurred in turnOnPorts().");
					}
				}else
				
				//Shut down the port at the number provided.
				if(name.equals("shutDownWatch")){
					try{
						int port=-1;
						if(parameters.get(0) instanceof TypeInteger)
							port=((TypeInteger)parameters.get(0)).getIntValue();
						Object O=new Object[]{new Integer(port),new Boolean(false)};
						MyComputerHandler.addData(new ApplicationData("setwatchonoff",O,0,MyProgram.getComputer().getIP()),MyProgram.getComputer().getIP());
					}catch(Exception e){
						MyProgram.getComputer().addMessage("An exception occurred in shutDownWatch().");
					}
				}else
				
				//Shut down all observed ports.
				if(name.equals("turnOnWatch")){
					try{
						int port=-1;
						if(parameters.get(0) instanceof TypeInteger)
							port=((TypeInteger)parameters.get(0)).getIntValue();
						Object O=new Object[]{new Integer(port),new Boolean(true)};
						MyComputerHandler.addData(new ApplicationData("setwatchonoff",O,0,MyProgram.getComputer().getIP()),MyProgram.getComputer().getIP());
					}catch(Exception e){
						MyProgram.getComputer().addMessage("An exception occurred in turnOnWatch().");
					}
				}else
				
				//Shut down the port at the number provided.
				if(name.equals("turnOnPort")){
					try{
					int port=-1;
					if(parameters.get(0) instanceof TypeInteger)
						port=((TypeInteger)parameters.get(0)).getIntValue();
					W.turnOnPort(port);
					}catch(Exception e){
						MyProgram.getComputer().addMessage("An exception occurred in turnOnPort().");
					}
				}else
				
				//Heal this port.
				if(name.equals("heal")){
					try{
					MyComputerHandler.addData(new ApplicationData("heal",null,W.getNumber(),W.getIP()),W.getIP());
					}catch(Exception e){
						MyProgram.getComputer().addMessage("An exception occurred in heal().");
					}
				}else 
				
				//Get the port number that fired this watch.
				if(name.equals("getTargetPort")){
					try{
					return(new TypeInteger(W.getTargetPort()));
					}catch(Exception e){
						MyProgram.getComputer().addMessage("An exception occurred in getTargetPort().");
					}
				}else 
				
				//Get the IP address that triggered this watch.
				if(name.equals("getTargetIP")){
					try{
					return(new TypeString(W.getTargetIP()));
					}catch(Exception e){
						MyProgram.getComputer().addMessage("An exception occurred in getTargetIP().");
					}
				}else
				
				//Get the IP address that triggered this watch.
				if(name.equals("isTriggered")){
					try{
					return(new TypeBoolean(W.getTriggered()));
					}catch(Exception e){
						MyProgram.getComputer().addMessage("An exception occurred in isTriggered().");
					}
				}else
				
				
				//Return the type of fire-wall that should be searched for in 
				if(name.equals("getSearchFireWall")){
					try{
					return(new TypeString(W.getSearchFireWall()));
					}catch(Exception e){
						MyProgram.getComputer().addMessage("An exception occurred in getSearchFireWall().");
					}
				}else
				
				//Return the type of fire-wall that should be searched for in 
				if(name.equals("isTriggerParameterSet")){
					try{
					String key=((TypeString)parameters.get(0)).getStringValue();
					Object O=W.getParentWatch().getTriggerParameter(key);
					if(O==null||O=="")
						return(new TypeBoolean(false));
					else
						return(new TypeBoolean(true));
					}catch(Exception e){
						MyProgram.getComputer().addMessage("An exception occurred in isTriggerParameterSet().");
					}
				}else
				
				//Return the type of fire-wall that should be searched for in 
				if(name.equals("getTriggerParameter")){
					try{
					String key=((TypeString)parameters.get(0)).getStringValue();
					return(W.getParentWatch().getTriggerParameter(key));
					}catch(Exception e){
						MyProgram.getComputer().addMessage("An exception occurred in getTriggerParameter().");
					}
				}else
				
				//Return the ammount deposited that caused this petty cash watch to fire.
				if(name.equals("getTransactionAmount")){
					try{
						return(new TypeFloat(W.getParentWatch().getDepositAmount()));
					}catch(Exception e){
						MyProgram.getComputer().addMessage("An exception occurred in getDespositAmount().");
					}
				}else
				
				//Return the IP address of the computer that triggered this watch.
				if(name.equals("getSourceIP")){
					try{
					return(new TypeString(W.getIP()));
					}catch(Exception e){
						MyProgram.getComputer().addMessage("An exception occurred in getSourceIP().");
					}
				}else
				
				//Return the IP address of the computer that triggered this watch.
				if(name.equals("getDefaultBank")){
					try{
					int defaultBank=MyProgram.getComputer().getDefaultBank();
					return(new TypeInteger(defaultBank));
					}catch(Exception e){
						MyProgram.getComputer().addMessage("An exception occurred in getDefaultBank().");
					}
				}
				
				//Get the CPU load of the computer that this attack program is running on.
				if(name.equals("getCPULoad")){
					try{
						return(new TypeFloat(MyProgram.getComputer().getCPULoad()));
					}catch(Exception e){
						MyProgram.getComputer().addMessage("An exception occurred in getCPULoad().");
					}
				}else
				
				//Get maximum CPU load of the computer performing this attack.
				if(name.equals("getMaximumCPULoad")){
					try{
					return(new TypeFloat(MyProgram.getComputer().getMaximumCPULoad()));
					}catch(Exception e){
						MyProgram.getComputer().addMessage("An exception occurred in getMaximumCPULoad().");
					}
				}
		/**
		Our Attack API.
		*/
		}else if(MyProgram instanceof AttackProgram){
			AttackProgram A=(AttackProgram)MyProgram;
		
			//Returns whoe is allowed to initialize a zombie attack via this port.
			if(name.equals("zombie")){
				try{
				if(parameters.size()==1){
					String zombieIP="";
					if(parameters.get(0) instanceof TypeString)
						zombieIP=((TypeString)parameters.get(0)).getStringValue();
					A.zombie(zombieIP);
				}
				}catch(Exception e){
					MyProgram.getComputer().addMessage("An exception occurred in zombie().");
				}
			}else

			//Message handler.
			if(name.equals("message")){
				try{
					if(incrementValue("message")<=1){
						if(parameters.size()==2){
							String message="";
							String ip="";
							if(parameters.get(0) instanceof TypeString)
								ip=((TypeString)parameters.get(0)).getStringValue();
							if(parameters.get(1) instanceof TypeString)
								message=((TypeString)parameters.get(1)).getStringValue();
							
							if(!(A.getTargetIP().equals(ip)||A.getComputer().getIP().equals(ip))){
								MyProgram.getComputer().addMessage(MessageHandler.MESSAGE_FAIL_INVALID_TARGET);
							}else if(message.length()<256){
								CentralLogging.getInstance().addOutput(MyProgram.getComputer().getIP()+" sent message \""+message+"\" to "+ip+"\n");
								MyComputerHandler.addData(new ApplicationData("message",message,0,MyProgram.getComputer().getIP()),ip);
							}else
								MyProgram.getComputer().addMessage(MessageHandler.MESSAGE_FAIL_TOO_LONG);
						}
					}
					
				}catch(Exception e){
					MyProgram.getComputer().addMessage("An exception occurred in message().");
				}
			}else
			
			//Beserk allows you to do extra damage for one turn, at a cost to yourself.
			if(name.equals("berserk")){
				try{
				if(incrementValue("berserk")==1)
					A.berserk();
				}catch(Exception e){
					MyProgram.getComputer().addMessage("An exception occurred in berserk().");
				}
			}else
			
			//Freeze an opponent's port.
			if(name.equals("freeze")){
				try{
				ApplicationData AD=null;
				if(!A.isZombie()){
					AD=new ApplicationData("freeze",null,A.getTargetPort(),A.getComputer().getIP());
	
				}else{
					AD=new ApplicationData("freeze",null,A.getTargetPort(),A.getMaliciousIP());
				}
				AD.setSourcePort(A.getPort());
				MyComputerHandler.addData(AD,A.getTargetIP());

				A.setDamage(false);
				}catch(Exception e){
					MyProgram.getComputer().addMessage("An exception occurred in freeze().");
				}
			}else
		
			
			//Switch the attack to one of the alternate targets.
			if(name.equals("switchAttack")){
				try{
				int newPort=A.nextTarget();
				if(newPort>=0){//Prevent a rolling attack from the finalize function.
				
					A.setSwitching(true);
					A.setTargetPort(newPort);
					ApplicationData AD=null;
					
					if(!A.isZombie()){
						AD=new ApplicationData("attack",A.getComputer().getNetwork(),newPort,A.getComputer().getIP());
					}else{
						AD=new ApplicationData("attack",new String[]{A.getComputer().getIP(),A.getComputer().getNetwork()},newPort,A.getMaliciousIP());
					}
					
					AD.setSourcePort(A.getPort());
					MyComputerHandler.addData(AD,A.getTargetIP());

				}
				}catch(Exception e){
					MyProgram.getComputer().addMessage("An exception occurred in switchAttack().");
				}
			}else
			
			//Return the malicious IP of the port.
			if(name.equals("getMaliciousIP")){
				try{
				return(new TypeString(A.getSavedMaliciousIP()));
				}catch(Exception e){
					MyProgram.getComputer().addMessage("An exception occurred in getMaliciousIP().");
				}
			}else
			
			//Return the source port that is causing this attack to take place.
			if(name.equals("getSourcePort")){
				try{
				return(new TypeInteger(A.getSourcePort()));
				}catch(Exception e){
					MyProgram.getComputer().addMessage("An exception occurred in getSourcePort().");
				}
			}else
			
			//Return the target port that this attack is being held against.
			if(name.equals("getTargetPort")){
				try{
				return(new TypeInteger(A.getTargetPort()));
				}catch(Exception e){
					MyProgram.getComputer().addMessage("An exception occurred in getTargetPort().");
				}
			}else
			
			//Install a script as a finalization step in an attack.
			if(name.equals("installScript")){
				try{
					if(!A.isZombie()){
						A.setAttacking(false);
						Object o[]=new Object[]{A.getMaliciousCode(),A.getMaliciousParameters()};
						
						//Check for bounty.
						A.checkBounty(A.getLastFile(),MakeBounty.INSTALL);
						ApplicationData AD = new ApplicationData("installScript",o,A.getTargetPort(),A.getIP());
						AD.setSourcePort(A.getPort());
						MyComputerHandler.addData(AD,A.getTargetIP());
					}
				}catch(Exception e){
					MyProgram.getComputer().addMessage("An exception occurred in installScript().");
				}
			}else
			
			//Install a script as a finalization step in an attack.
			if(name.equals("editLogs")){
				try{
				Object o[]=new Object[]{((TypeString)parameters.get(0)).getStringValue(),((TypeString)parameters.get(1)).getStringValue()};
				if(!A.isZombie())
					MyComputerHandler.addData(new ApplicationData("editLogs",o,A.getTargetPort(),A.getIP()),A.getTargetIP());
				else
					MyComputerHandler.addData(new ApplicationData("editLogs",o,A.getTargetPort(),A.getMaliciousIP()),A.getTargetIP());

				
				A.setAttacking(false);
				}catch(Exception e){
					MyProgram.getComputer().addMessage("An exception occurred in editLogs().");
				}
			}else
			
			//Get the number of times that an attack has taken place.
			if(name.equals("getIterations")){
				try{
				return(new TypeInteger(A.getIterations()));
				}catch(Exception e){
					MyProgram.getComputer().addMessage("An exception occurred in getIterations().");
				}
			}else
			
			//Get the CPU load of the computer that this attack program is running on.
			if(name.equals("getCPULoad")){
				try{
					return(new TypeFloat(A.getCPULoad()));
				}catch(Exception e){
					MyProgram.getComputer().addMessage("An exception occurred in getCPULoad().");
				}
			}else
			
			//Return whether or not this is a zombie attack.
			if(name.equals("isZombie")){
				return(new TypeBoolean(A.isZombie()));
			}else
			
			//Finzlize with delete logs.
			if(name.equals("deleteLogs")){
				try{
				String ip="";
				if(parameters.get(0) instanceof TypeString)
					ip=((TypeString)parameters.get(0)).getStringValue();
					
				if(!A.isZombie())
					MyComputerHandler.addData(new ApplicationData("deletelog",ip,A.getTargetPort(),A.getIP()),A.getTargetIP());
				else
					MyComputerHandler.addData(new ApplicationData("deletelog",ip,A.getTargetPort(),A.getMaliciousIP()),A.getTargetIP());
				
				A.setAttacking(false);
				}catch(Exception e){
					MyProgram.getComputer().addMessage("An exception occurred in deleteLogs().");
				}
			}else
									
			//End the attack by emptying the player's petty cash.
			if(name.equals("emptyPettyCash")){
				try{
				if(!A.isZombie())
					MyComputerHandler.addData(new ApplicationData("emptyPettyCash",A.getWindowHandle(),A.getTargetPort(),A.getIP()),A.getTargetIP());
				else
					MyComputerHandler.addData(new ApplicationData("emptyPettyCash",A.getWindowHandle(),A.getTargetPort(),A.getMaliciousIP()),A.getTargetIP());
					
				A.setAttacking(false);
				}catch(Exception e){
					MyProgram.getComputer().addMessage("An exception occurred in emptyPettyCash().");
				}
			}else
			
			//End the attack by emptying the player's petty cash.
			if(name.equals("stealFile")){
				try{
				if(!A.isZombie()){
					String targetIP=A.getIP();
					String fileName=null;
					String fetch_path="Public/";
					String path="";
					String password="";
					Object O[]=new Object[]{targetIP,fileName,fetch_path,path,password,A.getSourcePort()};
					MyComputerHandler.addData(new ApplicationData("malget",O,A.getTargetPort(),A.getIP()),A.getTargetIP());
				}else{
					String targetIP=A.getMaliciousIP();
					String fileName=null;
					String fetch_path="Public/";
					String path="";
					String password="";
					Object O[]=new Object[]{targetIP,fileName,fetch_path,path,password,A.getSourcePort()};
					MyComputerHandler.addData(new ApplicationData("malget",O,A.getTargetPort(),A.getMaliciousIP()),A.getTargetIP());
				}

				A.setAttacking(false);
				}catch(Exception e){
					e.printStackTrace();
					MyProgram.getComputer().addMessage("An exception occurred in stealFile().");
				}
			}else
				
			
			//End the attack by emptying the player's petty cash.
			if(name.equals("changeDailyPay")){
				try{
				String user=((TypeString)parameters.get(0)).getStringValue();
				if(!A.isZombie())
					// This is the fix for changeDailyPay messages not showing up in the attack window ... it was sending the port and not the window handle
					// changed from
					// MyComputerHandler.addData(new ApplicationData("changedailypay",new Object[]{user,A.getPort()},A.getTargetPort(),A.getIP()),A.getTargetIP());
					MyComputerHandler.addData(new ApplicationData("changedailypay",new Object[]{user,A.getWindowHandle()},A.getTargetPort(),A.getIP()),A.getTargetIP());
				else
					MyComputerHandler.addData(new ApplicationData("changedailypay",new Object[]{user,A.getWindowHandle()},A.getTargetPort(),A.getMaliciousIP()),A.getTargetIP());

				A.setAttacking(false);
				}catch(Exception e){
					MyProgram.getComputer().addMessage("An exception occurred in changeDailyPay().");
				}
			}else
			
			//End an attack by requesting that choices are shown on the front-end.
			if(name.equals("showChoices")){
				try{
					A.showChoices();
				}catch(Exception e){
					MyProgram.getComputer().addMessage("An exception occurred in showChoices().");
				}
			}else
			
			//Cancel the current attack taking place.
			if(name.equals("cancelAttack")){
				try{
				if(!A.isZombie())
					MyComputerHandler.addData(new ApplicationData("cancelattack",null,A.getTargetPort(),A.getIP()),A.getTargetIP());
				else
					MyComputerHandler.addData(new ApplicationData("cancelattack",null,A.getTargetPort(),A.getMaliciousIP()),A.getTargetIP());
				A.setAttacking(false);
				}catch(Exception e){
					MyProgram.getComputer().addMessage("An exception occurred in cancelAttack().");
				}
			}else
			
			if(name.equals("underAttack")){
				try{
					boolean underAttack = MyProgram.getComputer().isUnderAttack();
					return(new TypeBoolean(underAttack));
				}catch(Exception e){
					MyProgram.getComputer().addMessage("An exception occurred in underAttack().");
				}
			}else
			
			//Get the HP of the port being targeted with this attack script.
			if(name.equals("getTargetHP")){
				try{
				return(new TypeFloat(A.getTargetHP()));
				}catch(Exception e){
					MyProgram.getComputer().addMessage("An exception occurred in getTargetHP().");
				}
			}else
			
			//Get the amount in the target's petty cash.
			if(name.equals("checkPettyCash")){
				try{
				return(new TypeFloat(A.getTargetPettyCash()));
				}catch(Exception e){
					MyProgram.getComputer().addMessage("An exception occurred in checkPettyCash().");
				}
			}else
			
			//Get the target amount for stealing from petty cash.
			if(name.equals("checkPettyCashTarget")){
				try{
				return(new TypeFloat(A.getPettyCashTarget()));
				}catch(Exception e){
					MyProgram.getComputer().addMessage("An exception occurred in checkPettyCashTarget().");
				}
			}else
			
			//Get the IP address of the individual being targeted with this attack.
			if(name.equals("getTargetIP")){
				try{
				return(new TypeString(A.getTargetIP()));
				}catch(Exception e){
					MyProgram.getComputer().addMessage("An exception occurred in getTargetIP().");
				}
			}else
			
			//Return the hit points of the port doing the attacking.
			if(name.equals("getHP")){
				try{
				return(new TypeFloat(A.getHP()));
				}catch(Exception e){
					MyProgram.getComputer().addMessage("An exception occurred in getHP().");
				}
			}else
			
			//Get maximum CPU load of the computer performing this attack.
			if(name.equals("getMaximumCPULoad")){
				try{
				return(new TypeFloat(A.getMaximumCPULoad()));
				}catch(Exception e){
					MyProgram.getComputer().addMessage("An exception occurred in getMaximumCPULoad().");
				}
			}else
			
			//Finalize the attack by destroying the ports attached to target.
			if(name.equals("destroyWatches")){
				try{
				if(A.getComputer()!=null){
				
					if(!A.isZombie())
						MyComputerHandler.addData(new ApplicationData("destroyWatch",null,A.getTargetPort(),A.getIP()),A.getTargetIP());
					else{
						MyComputerHandler.addData(new ApplicationData("destroyWatch",null,A.getTargetPort(),A.getMaliciousIP()),A.getTargetIP());
					}
				}
				A.setAttacking(false);

				}catch(Exception e){
					MyProgram.getComputer().addMessage("An exception occurred in destroyWatches().");
				}
			}else
			
			//Get the CPU cost of the port being attacked.
			if(name.equals("getTargetCPUCost")){
				try{
				return(new TypeFloat(A.getTargetCPUCost()));
				}catch(Exception e){
					MyProgram.getComputer().addMessage("An exception occurred in getTargetCPUCost().");
				}
			}else
			
			//Check whether port being attacked has a watch installed.
			if(name.equals("checkForWatch")){
				try{
				return(new TypeBoolean(A.getTargetWatch()));
				}catch(Exception e){
					MyProgram.getComputer().addMessage("An exception occurred in checkForWatch().");
				}
			}else
			
			//Get the source IP performing this attack.
			if(name.equals("getSourceIP")){
				try{
				return(new TypeString(A.getIP()));
				}catch(Exception e){
					MyProgram.getComputer().addMessage("An exception occurred in getSourceIP().");
				}
			}
			
		//The Redirecting/Shipping API
		}else if(MyProgram instanceof ShippingProgram){
			ShippingProgram S=(ShippingProgram)MyProgram;
			if(name.equals("redirectDuctTape")){
				S.setCurrentCommodity(0);
			}else
			
			if(name.equals("redirectGermanium")){
				S.setCurrentCommodity(1);
			}else
			
			if(name.equals("redirectSilicon")){
				S.setCurrentCommodity(2);
			}else
			
			if(name.equals("redirectYBCO")){
				S.setCurrentCommodity(3);
			}else
			
			if(name.equals("redirectPlutonium")){
				S.setCurrentCommodity(4);
			}
		//THE HTTP API.
		}else if(MyProgram instanceof HTTPProgram){//THE HTTP API.			
				HTTPProgram H=(HTTPProgram)MyProgram;
				
				if(name.equals("getVisitorIP")){
					try{
					//check for domain here.
					String returnValue = H.getTargetIP();
					if(!H.getComputer().isNPC()){
						sql conn = new sql("localhost","hackwars","root","");
						String query = "SELECT domain FROM domains WHERE ip=\""+returnValue+"\"";
						ArrayList domainResult = conn.process(query);
						if(domainResult!=null){
							returnValue = (String)domainResult.get(0);
						}
					}
					return(new TypeString(returnValue));
					}catch(Exception e){
						MyProgram.getComputer().addMessage("An exception occurred in getVisitorIP().");
					}
				}else
				
				//Shut down all observed ports.
				if(name.equals("turnOnWatch")){
					try{
						int port=-1;
						if(parameters.get(0) instanceof TypeInteger)
							port=((TypeInteger)parameters.get(0)).getIntValue();
						Object O=new Object[]{new Integer(port),new Boolean(true)};
						MyComputerHandler.addData(new ApplicationData("setwatchonoff",O,0,MyProgram.getComputer().getIP()),MyProgram.getComputer().getIP());
					}catch(Exception e){
						MyProgram.getComputer().addMessage("An exception occurred in turnOnWatch().");
					}
				}else
				
				if(name.equals("getHostIP")){
					try{
					return(new TypeString(H.getComputer().getIP()));
					}catch(Exception e){
						MyProgram.getComputer().addMessage("An exception occurred in getHostIP().");
					}
				}else
				
				if(name.equals("getParameter")){
					try{
					String message="";
					if(parameters.get(0) instanceof TypeString)
						message=((TypeString)parameters.get(0)).getStringValue();
										
						
					return(new TypeString(H.getParameter(message)));
					}catch(Exception e){
						MyProgram.getComputer().addMessage("An exception occurred in getParameter().");
					}
				}else
				
				if(name.equals("isParameterSet")){
					try{
					String message="";
					if(parameters.get(0) instanceof TypeString)
						message=((TypeString)parameters.get(0)).getStringValue();
					String O=H.getParameter(message);
					if(O==null||O=="")
						return(new TypeBoolean(false));
					return(new TypeBoolean(true));
					}catch(Exception e){
						MyProgram.getComputer().addMessage("An exception occurred in isParameterSet().");
					}
				}else
				
				if(name.equals("logMessage")){
					try{
					String Content="";
					if(parameters.get(0) instanceof TypeString)
						Content=((TypeString)parameters.get(0)).getStringValue();
					MyProgram.getComputer().logMessage(Content,H.getTargetIP(),-1l);
					}catch(Exception e){
						MyProgram.getComputer().addMessage("An exception occurred in logMessage().");
					}
				}else
				
				//Update page content.
				if(name.equals("replaceContent")){
					try{
					//if(MyProgram.getComputer().getFileIO()){
						String key=((TypeString)parameters.get(0)).getStringValue();
						String content=((TypeString)parameters.get(1)).getStringValue();
						H.replaceContent(key,content);
					//}
					}catch(Exception e){
						MyProgram.getComputer().addMessage("An exception occurred in replaceContent().");
					}
				}else
				
				if(name.equals("hideStore")){
					try{
					H.hideStore();
					}catch(Exception e){
						MyProgram.getComputer().addMessage("An exception occurred in hideStore().");
					}
				}else
				
				if(name.equals("triggerWatch")){
					try{
					int watchNumber=0;
					if(parameters.get(0) instanceof TypeInteger)
						watchNumber=((TypeInteger)parameters.get(0)).getIntValue();
					HashMap TriggerParam=new HashMap();
					for(int i=1;i<parameters.size();i+=2){
						String key=((TypeString)parameters.get(i)).getStringValue();
						TriggerParam.put(key,parameters.get(i+1));
					}
					
					Object O=new Object[]{new Integer(watchNumber),TriggerParam,H.getTargetIP()};
					
					ApplicationData AD=new ApplicationData("requesttrigger",O,0,MyProgram.getComputer().getIP());
					AD.setSource(ApplicationData.OUTSIDE);
					MyComputerHandler.addData(AD,MyProgram.getComputer().getIP());

					}catch(Exception e){
						MyProgram.getComputer().addMessage("An exception occurred in triggerWatch().");
					}
				}else
					
				if(name.equals("triggerWatchRemote")){
					if(MyProgram.getComputer().isNPC()){
						try{
							int watchNumber=0;
							if(parameters.get(0) instanceof TypeInteger)
								watchNumber=((TypeInteger)parameters.get(0)).getIntValue();
							
							String IP=((TypeString)parameters.get(1)).getStringValue();

							HashMap TriggerParam=new HashMap();
							for(int i=2;i<parameters.size();i+=2){
								String key=((TypeString)parameters.get(i)).getStringValue();
								TriggerParam.put(key,parameters.get(i+1));
							}
							
							Object O=new Object[]{new Integer(watchNumber),TriggerParam,IP};
							
							ApplicationData AD=new ApplicationData("requesttrigger",O,0,MyProgram.getComputer().getIP());
							AD.setSource(ApplicationData.OUTSIDE);
							MyComputerHandler.addData(AD,IP);
							
						}catch(Exception e){
							MyProgram.getComputer().addMessage("An exception occurred in triggerWatch().");
						}
					}
				}else
				
				if(name.equals("isGetVariableSet")){
					try{
					if(MyProgram.getComputer().getFileIO()){
						String key=((TypeString)parameters.get(0)).getStringValue();
						String O=H.fetchGetVariable(key);
						if(O==null||O=="")
							return(new TypeBoolean(false));
						return(new TypeBoolean(true));
					}
					}catch(Exception e){
						MyProgram.getComputer().addMessage("An exception occurred in isGetVariableSet().");
					}
				}else
				
				if(name.equals("fetchGetVariable")){
					try{
					if(MyProgram.getComputer().getFileIO()){
						String key=((TypeString)parameters.get(0)).getStringValue();
						return(new TypeString(H.fetchGetVariable(key)));
					}
					}catch(Exception e){
						MyProgram.getComputer().addMessage("An exception occurred in fetchGetVariable().");
					}
				}else
				
				if(name.equals("popUp")){
					try{
					if(incrementValue("popUp")<5){
						String message="";
						if(parameters.get(0) instanceof TypeString)
							message=((TypeString)parameters.get(0)).getStringValue();
						MyComputerHandler.addData(new ApplicationData("message","pop;"+message,0,H.getComputer().getIP()),H.getTargetIP());
					}
					}catch(Exception e){
						MyProgram.getComputer().addMessage("An exception occurred in popUP().");
					}
				}else
					
				if(name.equals("giveTask")){
					Computer C=H.getComputer();
					if(C.isNPC()){
						String TaskName=(String)((TypeString)parameters.get(0)).getRawValue();
						String TaskLabel=(String)((TypeString)parameters.get(1)).getRawValue();
						Integer QuestID=(Integer)((TypeInteger)parameters.get(2)).getRawValue();
						MyComputerHandler.addData(new ApplicationData("givetask",new Object[]{TaskName,TaskLabel,QuestID},0,H.getComputer().getIP()),H.getTargetIP());
					}
				}else
				
				if(name.equals("setTask")){
					Computer C=H.getComputer();
					if(C.isNPC()){
						String TaskName=(String)((TypeString)parameters.get(0)).getRawValue();
						Integer QuestID=(Integer)((TypeInteger)parameters.get(1)).getRawValue();
						Boolean SetTo=(Boolean)((TypeBoolean)parameters.get(2)).getRawValue();
						MyComputerHandler.addData(new ApplicationData("settask",new Object[]{TaskName,QuestID,SetTo},0,H.getComputer().getIP()),H.getTargetIP());

					}
				}else
				
				if(name.equals("completeTask")){
					Computer C=H.getComputer();
					if(C.isNPC()){
						String TaskName=(String)((TypeString)parameters.get(0)).getRawValue();
						Integer QuestID=(Integer)((TypeInteger)parameters.get(1)).getRawValue();
						MyComputerHandler.addData(new ApplicationData("completetask",new Object[]{TaskName,QuestID},0,H.getComputer().getIP()),H.getTargetIP());

					}
				}else
				
				if(name.equals("giveQuest")){
					Computer C=H.getComputer();
					if(C.isNPC()){
						Integer QuestID=(Integer)((TypeInteger)parameters.get(0)).getRawValue();
						String description=(String)((TypeString)parameters.get(1)).getRawValue();

						MyComputerHandler.addData(new ApplicationData("givequest",new Object[]{QuestID,description},0,H.getComputer().getIP()),H.getTargetIP());
					}
				}else
								
				if(name.equals("finishQuest")){
					Computer C=H.getComputer();
					if(C.isNPC()){
						if(!C.checkRecentQuestFinisher(H.getTargetIP())){//Make sure you can't finish the same quest more than once.
							C.addRecentQuestFinisher(H.getTargetIP());
					
							Integer QuestID=(Integer)((TypeInteger)parameters.get(0)).getRawValue();
							MyComputerHandler.addData(new ApplicationData("finishquest",new Object[]{QuestID},0,H.getComputer().getIP()),H.getTargetIP());
						
						}
					}
				}else
					
				if(name.equals("takeMoney")){
					Computer C=H.getComputer();
					if(C.isNPC()){
						Float amount=(Float)((TypeFloat)parameters.get(0)).getRawValue();
						//String TaskName=(String)((TypeString)parameters.get(1)).getRawValue();
						//Integer QuestID=(Integer)((TypeInteger)parameters.get(2)).getRawValue();
						MyComputerHandler.addData(new ApplicationData("takemoney",new Object[]{amount/*,TaskName,QuestID*/},0,H.getComputer().getIP()),H.getTargetIP());
					}
				}else
					
				if(name.equals("takeCommodity")){
					Computer C=H.getComputer();
					if(C.isNPC()){
						Float amount=(Float)((TypeFloat)parameters.get(0)).getRawValue();
						Integer CommodityType=(Integer)((TypeInteger)parameters.get(1)).getRawValue();
						//String TaskName=(String)((TypeString)parameters.get(2)).getRawValue();
						//Integer QuestID=(Integer)((TypeInteger)parameters.get(3)).getRawValue();
						MyComputerHandler.addData(new ApplicationData("takecommodity",new Object[]{amount,CommodityType/*,TaskName,QuestID*/},0,H.getComputer().getIP()),H.getTargetIP());
					}
				}else
				
				if(name.equals("exchangeCommodity")){
					Computer C=H.getComputer();
					if(C.isNPC()){
						Integer ExchangeAmount=(Integer)((TypeInteger)parameters.get(0)).getRawValue();
						Integer CommodityType=(Integer)((TypeInteger)parameters.get(1)).getRawValue();
						Float ExchangeCost=(Float)((TypeFloat)parameters.get(2)).getRawValue();
						MyComputerHandler.addData(new ApplicationData("exchangecommodity",new Object[]{ExchangeAmount,CommodityType,ExchangeCost},0,H.getComputer().getIP()),H.getTargetIP());
					}
				}else
				
				if(name.equals("exchangeFile")){
					Computer C=H.getComputer();
					if(C.isNPC()){
						Integer ExchangeAmount=(Integer)((TypeInteger)parameters.get(0)).getRawValue();
						Integer CommodityType=(Integer)((TypeInteger)parameters.get(1)).getRawValue();
						Float ExchangeCost=(Float)((TypeFloat)parameters.get(2)).getRawValue();
						MyComputerHandler.addData(new ApplicationData("exchangefile",new Object[]{ExchangeAmount,CommodityType,ExchangeCost},0,H.getComputer().getIP()),H.getTargetIP());
					}
				}else
				
				if(name.equals("giveXP")){
					Computer C=H.getComputer();
					if(C.isNPC()){
						String Stat=(String)((TypeString)parameters.get(0)).getRawValue();
						Float Amount=(Float)((TypeFloat)parameters.get(1)).getRawValue();
						MyComputerHandler.addData(new ApplicationData("giveexperience",new Object[]{Stat,Amount},0,H.getComputer().getIP()),H.getTargetIP());
					}
				}else
				
				if(name.equals("giveMoney")){
					Computer C=H.getComputer();
					if(C.isNPC()){
						float amount=(Float)((TypeFloat)parameters.get(0)).getRawValue();
						ApplicationData MyBank=new ApplicationData("bank",new Float(amount),0,H.getComputer().getIP());
						MyComputerHandler.addData(MyBank,H.getTargetIP());
					}
				}else
				
				if(name.equals("giveCommodity")){
					Computer C=H.getComputer();
					if(C.isNPC()){
						Integer commodityType=(Integer)((TypeInteger)parameters.get(0)).getRawValue();
						Float amount=(Float)((TypeFloat)parameters.get(1)).getRawValue();
						ApplicationData MyBank=new ApplicationData("givecommodity",new Object[]{commodityType,amount},0,H.getComputer().getIP());
						MyComputerHandler.addData(MyBank,H.getTargetIP());
					}
				}else
				
				if(name.equals("giveFile")){
					Computer C=H.getComputer();
					if(C.isNPC()){
						String fileID=(String)((TypeString)parameters.get(0)).getRawValue();
						Integer quan=(Integer)((TypeInteger)parameters.get(1)).getRawValue();
						ApplicationData MyBank=new ApplicationData("givefile",new Object[]{fileID,quan},0,H.getTargetIP());
						MyComputerHandler.addData(MyBank,H.getComputer().getIP());
					}
				}else
				
				if(name.equals("takeFile")){
					Computer C=H.getComputer();
					if(C.isNPC()){
						String fileID=(String)((TypeString)parameters.get(0)).getRawValue();
						Integer quan=(Integer)((TypeInteger)parameters.get(1)).getRawValue();
						ApplicationData MyBank=new ApplicationData("takefile2",new Object[]{fileID,quan},0,H.getComputer().getIP());
						MyComputerHandler.addData(MyBank,H.getTargetIP());
					}
				}else
				
				if(name.equals("giveAccess")){
					Computer C=H.getComputer();
					if(C.isNPC()){
						String networkName=(String)((TypeString)parameters.get(0)).getStringValue();
						ApplicationData MyApplicationData=new ApplicationData("giveaccess",networkName,0,H.getComputer().getIP());
						MyComputerHandler.addData(MyApplicationData,H.getTargetIP());
					}
				}
				/*
				else
				
				if(name.equals("fileCount")){
					Computer C=H.getComputer();
					String tIP = H.getTargetIP();
					if(C.isNPC()){
						String fileID=(String)((TypeString)parameters.get(0)).getRawValue();
						ApplicationData MyBank=new ApplicationData("takefile",fileID,0,H.getComputer().getIP());
						MyComputerHandler.addData(MyBank,H.getTargetIP());
					}
				}
				*/

		/**
		The FTP API
		*/
		}else if(MyProgram instanceof FTPProgram){		
		
			FTPProgram F=(FTPProgram)MyProgram;
			
			//Put method has been called.
			if(name.equals("put")){
				try{
					if(incrementValue("ftp")<=1){
						String ip="";
						if(parameters.get(0) instanceof TypeString)
							ip=((TypeString)parameters.get(0)).getStringValue();				
						String path=F.getPath();
						HackerFile file=F.getFile();
						Object Parameter[]=new Object[]{path,file};
						String malIP = F.getMaliciousIP();
						String putIP = F.getIP();
						String sourceIP = F.getComputer().getIP();
						//System.out.println("putting to "+ip+" from "+sourceIP+" with mal ip "+malIP);
						if (sourceIP.equals(ip) || malIP.equals(ip) || putIP.equals(ip)) {
							MyComputerHandler.addData(new ApplicationData("savefile",Parameter,0,ip),ip);
						} else {
							MyProgram.getComputer().addMessage(MessageHandler.FTP_FAIL_WRONG_TARGET);
						}
					} else {
						MyProgram.getComputer().addMessage(MessageHandler.FTP_FAIL_TOO_MANY_PUT_GET);
					}
				}catch(Exception e){
					MyProgram.getComputer().addMessage("An exception occurred in put().");
				}
			}else
			
			if(name.equals("get")){
				try{
				if(incrementValue("ftp")<=1){
						String ip="";
						if(parameters.get(0) instanceof TypeString)
							ip=((TypeString)parameters.get(0)).getStringValue();				
						String path=F.getPath();
						HackerFile file=F.getFile();
						Object Parameter[]=new Object[]{F.getFetchPath(),file};
						String malIP = F.getMaliciousIP();
						String getIP = F.getIP();
						String sourceIP = F.getComputer().getIP();
						if (sourceIP.equals(ip) || malIP.equals(ip) || getIP.equals(ip)) {
							MyComputerHandler.addData(new ApplicationData("savefile",Parameter,0,ip),ip);
						} else {
							MyProgram.getComputer().addMessage(MessageHandler.FTP_FAIL_WRONG_TARGET);
						}
					} else {
						MyProgram.getComputer().addMessage(MessageHandler.FTP_FAIL_TOO_MANY_PUT_GET);
					}
				}catch(Exception e){
					MyProgram.getComputer().addMessage("An exception occurred in get().");
				}
			}else
			
			if(name.equals("getTargetIP")){
				try{
				return(new TypeString(F.getIP()));
				}catch(Exception e){
					MyProgram.getComputer().addMessage("An exception occurred in getTargetIP().");
				}
			}else
			
			if(name.equals("getSourceIP")){
				try{
				return(new TypeString(F.getIP()));
				}catch(Exception e){
					MyProgram.getComputer().addMessage("An exception occurred in getSourceIP().");
				}
			}else
			
			if(name.equals("getMaliciousIP")){
				try{
				return(new TypeString(F.getMaliciousIP()));
				}catch(Exception e){
					MyProgram.getComputer().addMessage("An exception occurred in getMaliciousIP().");
				}
			}else
			
			if(name.equals("getFileName")){
				try{
				HackerFile HF=F.getFile();
				if(HF==null){
					return(new TypeString(""));
				}else
					return(new TypeString(HF.getName()));
				}catch(Exception e){
					MyProgram.getComputer().addMessage("An exception occurred in getFileName().");
				}
			}else
			
			if(name.equals("getFileType")){
				try{
				HackerFile HF=F.getFile();
				if(HF==null){
					return(new TypeString(""));
				}else
					return(new TypeString(HF.getTypeString()));
				}catch(Exception e){
					MyProgram.getComputer().addMessage("An exception occurred in getFileType().");
				}
			}else
			
			if(name.equals("getFilePrice")){
				try{
				HackerFile HF=F.getFile();
				if(HF==null){
					return(new TypeFloat(0.0f));
				}else
					return(new TypeFloat(HF.getPrice()));
				}catch(Exception e){
					MyProgram.getComputer().addMessage("An exception occurred in getFilePrice().");
				}
			}
		}
		
		if(name.equals("rand")){
			try{
			return(new TypeFloat((float)Math.random()));
			}catch(Exception e){
				MyProgram.getComputer().addMessage("An exception occurred in rand().");
			}
		}
		
		//Beserk allows you to do extra damage for one turn, at a cost to yourself.
		if(name.equals("playSound")){
			try{
			if(incrementValue("playSound")<=1){
				String ip=((TypeString)parameters.get(0)).getStringValue();
				int sound=((TypeInteger)parameters.get(1)).getIntValue();
				AttackProgram A=null;
				if(MyProgram instanceof AttackProgram)
					A=(AttackProgram)MyProgram;
				
				if(A==null||(A.getTargetIP().equals(ip)||A.getComputer().getIP().equals(ip))){
					MyComputerHandler.addData(new ApplicationData("message","sound;"+sound,0,MyProgram.getComputer().getIP()),MyProgram.getComputer().getIP());
				}else{
					MyProgram.getComputer().addMessage(MessageHandler.MESSAGE_FAIL_INVALID_TARGET);
				}
			}
			}catch(Exception e){
				MyProgram.getComputer().addMessage("An exception occurred in playSound().");
			}
		}
		
		if(name.equals("printf")){
			try{
			String Content="";
			if(parameters.get(0) instanceof TypeString)
				Content=((TypeString)parameters.get(0)).getStringValue();
			
			Content=" "+Content;
			
			String data[]=Content.split("\\%s");
			String returnMe="";
			for(int i=0;i<data.length;i++){
				returnMe+=data[i];
				if(i+1<parameters.size())
					returnMe+=parameters.get(i+1).toString();
			}
			
			returnMe=returnMe.substring(1,returnMe.length());
			
			return(new TypeString(returnMe));
			}catch(Exception e){
				MyProgram.getComputer().addMessage("An exception occurred in printf().");
			}
		}
		
		
		//Compare two strings which are passed in.
		if(name.equals("equal")){
			try{
			if(parameters.size()!=2)
				MyProgram.setError("\"equal\" takes two strings and returns whether they are equal.");
			else if(!(parameters.get(0) instanceof TypeString)||!(parameters.get(1) instanceof TypeString))
				MyProgram.setError("\"equal\" takes two strings and returns whether they are equal.");
			
			//Compare two strings which are passed in.
			String s1="";
			String s2="";
			if(parameters.get(0) instanceof TypeString)
				s1=((TypeString)parameters.get(0)).getStringValue();
			if(parameters.get(1) instanceof TypeString)
				s2=((TypeString)parameters.get(1)).getStringValue();
			return(new TypeBoolean(s1.equals(s2)));
			}catch(Exception e){
				MyProgram.getComputer().addMessage("An exception occurred in equal().");
			}
		}
		
		//Compare two strings which are passed in.
		if(name.equals("strcmp")){
			try{
			if(parameters.size()!=2)
				MyProgram.setError("\"strcmp(string,string)\" Returns an integer less than, equal to or greater than zero depending on whether the first string is less than, equal to or greater than the second string.");
			else if(!(parameters.get(0) instanceof TypeString)||!(parameters.get(1) instanceof TypeString))
				MyProgram.setError("\"strcmp(string,string)\" Returns an integer less than, equal to or greater than zero depending on whether the first string is less than, equal to or greater than the second string.");
			
			//Compare two strings which are passed in.
			String s1="";
			String s2="";
			if(parameters.get(0) instanceof TypeString)
				s1=((TypeString)parameters.get(0)).getStringValue();
			if(parameters.get(1) instanceof TypeString)
				s2=((TypeString)parameters.get(1)).getStringValue();
			return(new TypeInteger(s1.compareTo(s2)));
			}catch(Exception e){
				MyProgram.getComputer().addMessage("An exception occurred in strcmp().");
			}
		}
		
		//Return the length of a string that is passed in.
		if(name.equals("strlen")){
			try{
			if(parameters.size()!=1)
				MyProgram.setError("\"strlen(string)\" Returns an integer representing the length of a string.");
			else if(!(parameters.get(0) instanceof TypeString))
				MyProgram.setError("\"strlen(string)\" Returns an integer representing the length of a string.");
			
			//Compare two strings which are passed in.
			String s1="";
			if(parameters.get(0) instanceof TypeString)
				s1=((TypeString)parameters.get(0)).getStringValue();
			return(new TypeInteger(s1.length()));
			}catch(Exception e){
				MyProgram.getComputer().addMessage("An exception occurred in strlen().");
			}
		}
		
		//A math function.
		if(name.equals("sqrt")){
			try{
			if(parameters.size()!=1)
				MyProgram.setError("\"sqrt(float)\" Returns the square root of the float provided.");
			else if(!(parameters.get(0) instanceof TypeFloat))
				MyProgram.setError("\"sqrt(float)\" Returns the square root of the float provided.");
			
			//Compare two strings which are passed in.
			float f1=0.0f;
			if(parameters.get(0) instanceof TypeFloat)
				f1=(Float)((TypeFloat)parameters.get(0)).getRawValue();
			return(new TypeFloat((float)Math.sqrt(f1)));
			}catch(Exception e){
				MyProgram.getComputer().addMessage("An exception occurred in sqrt().");
			}
		}
		
		//A math function.
		if(name.equals("abs")){
			try{
			if(parameters.size()!=1)
				MyProgram.setError("\"abs(float)\" Returns the absolute value of the float provided.");
			else if(!(parameters.get(0) instanceof TypeFloat))
				MyProgram.setError("\"abs(float)\" Returns the absolute value of the float provided.");
			
			float f1=0.0f;
			if(parameters.get(0) instanceof TypeFloat)
				f1=(Float)((TypeFloat)parameters.get(0)).getRawValue();
			return(new TypeFloat((float)Math.abs(f1)));
			}catch(Exception e){
				MyProgram.getComputer().addMessage("An exception occurred in abs().");
			}
		}
		
		//A math function.
		if(name.equals("ln")){
			try{
			if(parameters.size()!=1)
				MyProgram.setError("\"ln(float)\" Returns the natural logarithm of the float provided.");
			else if(!(parameters.get(0) instanceof TypeFloat))
				MyProgram.setError("\"ln(float)\" Returns the natural logarithm of the float provided.");
			
			float f1=0.0f;
			if(parameters.get(0) instanceof TypeFloat)
				f1=(Float)((TypeFloat)parameters.get(0)).getRawValue();
			return(new TypeFloat((float)Math.log(f1)));
			}catch(Exception e){
				MyProgram.getComputer().addMessage("An exception occurred in ln().");
			}
		}
		
		//A math function.
		if(name.equals("atan")){
			try{
			if(parameters.size()!=1)
				MyProgram.setError("\"atan(float)\" Returns the arc-tangent of the floating point number provided.");
			else if(!(parameters.get(0) instanceof TypeFloat))
				MyProgram.setError("\"atan(float)\" Returns the arc-tangent of the floating point number provided.");
			
			float f1=0.0f;
			if(parameters.get(0) instanceof TypeFloat)
				f1=(Float)((TypeFloat)parameters.get(0)).getRawValue();
			f1=f1*(float)Math.PI/180.0f;
			return(new TypeFloat((float)Math.atan(f1)));
			}catch(Exception e){
				MyProgram.getComputer().addMessage("An exception occurred in atan().");
			}
		}
		
		//A math function.
		if(name.equals("acos")){
			try{
			if(parameters.size()!=1)
				MyProgram.setError("\"acos(float)\" Returns the arc-cosine of the floating point number provided.");
			else if(!(parameters.get(0) instanceof TypeFloat))
				MyProgram.setError("\"acos(float)\" Returns the arc-cosine of the floating point number provided.");
			
			float f1=0.0f;
			if(parameters.get(0) instanceof TypeFloat)
				f1=(Float)((TypeFloat)parameters.get(0)).getRawValue();
			f1=f1*(float)Math.PI/180.0f;
			return(new TypeFloat((float)Math.acos(f1)));
			}catch(Exception e){
				MyProgram.getComputer().addMessage("An exception occurred in acos().");
			}
		}
		
		//A math function.
		if(name.equals("asin")){
			try{
			if(parameters.size()!=1)
				MyProgram.setError("\"asin(float)\" Returns the arc-sine of the floating point number provided.");
			else if(!(parameters.get(0) instanceof TypeFloat))
				MyProgram.setError("\"asin(float)\" Returns the arc-sine of the floating point number provided.");
			
			float f1=0.0f;
			if(parameters.get(0) instanceof TypeFloat)
				f1=(Float)((TypeFloat)parameters.get(0)).getRawValue();
			f1=f1*(float)Math.PI/180.0f;
			return(new TypeFloat((float)Math.asin(f1)));
			}catch(Exception e){
				MyProgram.getComputer().addMessage("An exception occurred in asin().");
			}
		}
		
		//A math function.
		if(name.equals("tan")){
			try{
			if(parameters.size()!=1)
				MyProgram.setError("\"tan(float)\" Returns the tangent of the floating point number provided.");
			else if(!(parameters.get(0) instanceof TypeFloat))
				MyProgram.setError("\"tan(float)\" Returns the tangent of the floating point number provided.");
			
			float f1=0.0f;
			if(parameters.get(0) instanceof TypeFloat)
				f1=(Float)((TypeFloat)parameters.get(0)).getRawValue();
			f1=f1*(float)Math.PI/180.0f;
			return(new TypeFloat((float)Math.tan(f1)));
			}catch(Exception e){
				MyProgram.getComputer().addMessage("An exception occurred in getE().");
			}
		}
		
		//A math function.
		if(name.equals("cos")){
			try{
			if(parameters.size()!=1)
				MyProgram.setError("\"cos(float)\" Returns the cosine of the floating point number provided.");
			else if(!(parameters.get(0) instanceof TypeFloat))
				MyProgram.setError("\"cos(float)\" Returns the cosine of the floating point number provided.");
			
			float f1=0.0f;
			if(parameters.get(0) instanceof TypeFloat)
				f1=(Float)((TypeFloat)parameters.get(0)).getRawValue();
			f1=f1*(float)Math.PI/180.0f;
			return(new TypeFloat((float)Math.cos(f1)));
			}catch(Exception e){
				MyProgram.getComputer().addMessage("An exception occurred in cos().");
			}
		}
		
		//A math function.
		if(name.equals("sin")){
			try{
			if(parameters.size()!=1)
				MyProgram.setError("\"sin(float)\" Returns the sine of the floating point number provided.");
			else if(!(parameters.get(0) instanceof TypeFloat))
				MyProgram.setError("\"sin(float)\" Returns the sine of the floating point number provided.");
			
			float f1=0.0f;
			if(parameters.get(0) instanceof TypeFloat)
				f1=(Float)((TypeFloat)parameters.get(0)).getRawValue();
			f1=f1*(float)Math.PI/180.0f;
			return(new TypeFloat((float)Math.sin(f1)));
			}catch(Exception e){
				MyProgram.getComputer().addMessage("An exception occurred in sin().");
			}
		}else
		
		//Get a global parameter.
		if(name.equals("setGlobal")){
			try{
			int index=((TypeInteger)parameters.get(0)).getIntValue();
			Object O=parameters.get(1);
			if((O instanceof TypeBoolean)||(O instanceof TypeInteger)||(O instanceof TypeString)||(O instanceof TypeFloat))
				MyProgram.getComputer().setGlobal(index,O);
			}catch(Exception e){
				MyProgram.getComputer().addMessage("An exception occurred in setGlobal().");
			}
		}else
		
		//Is a global variable set.
		if(name.equals("isGlobalSet")){
			try{
			int index=((TypeInteger)parameters.get(0)).getIntValue();
			Object O=MyProgram.getComputer().getGlobal(index);
			if(O==null)
				return(new TypeBoolean(false));
			return(new TypeBoolean(true));
			}catch(Exception e){
				MyProgram.getComputer().addMessage("An exception occurred in isGlobalSet().");
			}
		}else
		
		//Get a global parameter.
		if(name.equals("getGlobal")){
			try{
			int index=((TypeInteger)parameters.get(0)).getIntValue();
			return(MyProgram.getComputer().getGlobal(index));
			}catch(Exception e){
				MyProgram.getComputer().addMessage("An exception occurred in getGlobal().");
			}
		}else
		
		
		//A math function.
		if(name.equals("getE")){
			try{
			if(parameters.size()!=0)
				MyProgram.setError("\"getE()\" Returns the mathematical constant e.");
			
			return(new TypeFloat((float)Math.E));
			}catch(Exception e){
				MyProgram.getComputer().addMessage("An exception occurred in getE().");
			}
		}
		
		//A math function.
		if(name.equals("getPI")){
			try{
			if(parameters.size()!=0)
				MyProgram.setError("\"getPI()\" Returns the mathematical constant PI.");
				
			return(new TypeFloat((float)Math.PI));
			}catch(Exception e){
				MyProgram.getComputer().addMessage("An exception occurred in getPI().");
			}
		}
		
		//A math function.
		if(name.equals("clearFile")){
			try{
	
				String file=((TypeString)parameters.get(0)).getStringValue();
				String data[]=file.split("/");
				String fileName;
				String path="";
				for(int i=0;i<data.length-1;i++)
					path+=data[i]+"/";
				fileName=data[data.length-1];
				HackerFile HF=MyProgram.getComputer().getFileSystem().getFile(path,fileName);
				
				if(HF==null){
					MyProgram.getComputer().addMessage(MessageHandler.FILE_NOT_FOUND);
				}else if(HF.getType()==HF.TEXT){
					HashMap HM=HF.getContent();
					HM.put("data","");
				}else{
					MyProgram.getComputer().addMessage(MessageHandler.FILE_IO_FAIL_INVALID_TYPE);
				}
			
			
			}catch(Exception e){
				MyProgram.getComputer().addMessage("An exception occurred in clearFile().");
			}
		}else
		
		//A math function.
		if(name.equals("readFile")){
			try{			
				String file=((TypeString)parameters.get(0)).getStringValue();
				String data[]=file.split("/");
				String fileName;
				String path="";
				for(int i=0;i<data.length-1;i++)
					path+=data[i]+"/";

				fileName=data[data.length-1];
				HackerFile HF=MyProgram.getComputer().getFileSystem().getFile(path,fileName);
				
				if(HF==null){
					MyProgram.getComputer().addMessage(MessageHandler.FILE_NOT_FOUND);
				}else if(HF.getType()==HF.TEXT){
					HashMap HM=HF.getContent();
					return(new TypeString((String)HM.get("data")));
				}else{
					MyProgram.getComputer().addMessage(MessageHandler.FILE_IO_FAIL_INVALID_TYPE);
				}
			}catch(Exception e){
				MyProgram.getComputer().addMessage("An exception occurred in readFile().");
			}
		}else
		
		//A math function.
		if(name.equals("readLine")){
			try{
				String file=((TypeString)parameters.get(0)).getStringValue();
				String data[]=file.split("/");
				String fileName;
				String path="";
				for(int i=0;i<data.length-1;i++)
					path+=data[i]+"/";

				fileName=data[data.length-1];
				HackerFile HF=MyProgram.getComputer().getFileSystem().getFile(path,fileName);
				
				if(HF==null){
					MyProgram.getComputer().addMessage(MessageHandler.FILE_NOT_FOUND);
				}else if(HF.getType()==HF.TEXT){
					HashMap HM=HF.getContent();
					int line=((TypeInteger)parameters.get(1)).getIntValue();
					String Lines[]=((String)HM.get("data")).split("\\n");
					if(line>=Lines.length){
						MyProgram.getComputer().addMessage(MessageHandler.FILE_IO_FAIL_LINE_NUMBER);
					}else{
						return(new TypeString(Lines[line]));
					}
				}else{
					MyProgram.getComputer().addMessage(MessageHandler.FILE_IO_FAIL_INVALID_TYPE);
				}
			
			}catch(Exception e){
				MyProgram.getComputer().addMessage("An exception occurred in readLine().");
			}
			
		}else
		
		//A math function.
		if(name.equals("countLines")){
			try{
				String file=((TypeString)parameters.get(0)).getStringValue();
				String data[]=file.split("/");
				String fileName;
				String path="";
				for(int i=0;i<data.length-1;i++)
					path+=data[i]+"/";

				fileName=data[data.length-1];
				HackerFile HF=MyProgram.getComputer().getFileSystem().getFile(path,fileName);
				
				if(HF==null){
					MyProgram.getComputer().addMessage(MessageHandler.FILE_NOT_FOUND);
				}else if(HF.getType()==HF.TEXT){
					HashMap HM=HF.getContent();
					String Lines[]=((String)HM.get("data")).split("\\n");
					return(new TypeInteger(Lines.length));
				}else{
					MyProgram.getComputer().addMessage(MessageHandler.FILE_IO_FAIL_INVALID_TYPE);
				}
				
			}catch(Exception e){
				MyProgram.getComputer().addMessage("An exception occurred in countLines().");
			}
		}else
		
		//A math function.
		if(name.equals("writeLine")){
			try{
		
				
				String file=((TypeString)parameters.get(0)).getStringValue();
				String data[]=file.split("/");
				String fileName;
				String path="";
				for(int i=0;i<data.length-1;i++)
					path+=data[i]+"/";

				fileName=data[data.length-1];
				HackerFile HF=MyProgram.getComputer().getFileSystem().getFile(path,fileName);
				
				if(HF==null){
					MyProgram.getComputer().getFileSystem().addDirectory(path);
				
					HF=new HackerFile(HF.TEXT);
					HF.setName(fileName);
					HF.setLocation(path);
					HF.setDescription("Auto-generated text file.");
					HF.setMaker(MyProgram.getComputer().getIP());
					HashMap Content=new HashMap();
					Content.put("data","");
					Content.put("level","0");
					HF.setContent(Content);
					MyProgram.getComputer().getFileSystem().addFile(HF,true);
					HF=MyProgram.getComputer().getFileSystem().getFile(path,fileName);
				}
				
				if(HF==null){
					MyProgram.getComputer().addMessage(MessageHandler.FILE_NOT_FOUND);
				}else if(HF.getType()==HF.TEXT){
					HashMap HM=HF.getContent();
					String content=(String)HM.get("data");
					String newData=((TypeString)parameters.get(1)).getStringValue();
					content=content+newData+"\n";
					if(content.length()<=MyProgram.getComputer().FILE_SIZE_LIMIT){
						HM.put("data",content);
					}else{
						MyProgram.getComputer().addMessage(MessageHandler.FILE_IO_FAIL_FILE_SIZE,new Object[]{fileName});
						if(MyProgram.getComputer().FILE_SIZE_LIMIT == Computer.FREE_FILE_SIZE_LIMIT){
							MyProgram.getComputer().addMessage(MessageHandler.UPGRADE_FILE_SIZE);
						
						}
					}

				}else{
					MyProgram.getComputer().addMessage(MessageHandler.FILE_IO_FAIL_INVALID_TYPE);
				}
			
				
			}catch(Exception e){
				MyProgram.getComputer().addMessage("An exception occurred in writeLine().");
			}
		}else
		
		//A math function.
		if(name.equals("writeFile")){
			try{
					
					String file=((TypeString)parameters.get(0)).getStringValue();
					String data[]=file.split("/");
					String fileName;
					String path="";
					for(int i=0;i<data.length-1;i++)
						path+=data[i]+"/";

					fileName=data[data.length-1];
					HackerFile HF=MyProgram.getComputer().getFileSystem().getFile(path,fileName);
					
					if(HF==null){
						MyProgram.getComputer().getFileSystem().addDirectory(path);

						HF=new HackerFile(HF.TEXT);
						HF.setName(fileName);
						HF.setLocation(path);
						HF.setDescription("Auto-generated text file.");
						HF.setMaker(MyProgram.getComputer().getIP());
						HashMap Content=new HashMap();
						Content.put("data","");
						Content.put("level","0");
						HF.setContent(Content);
						MyProgram.getComputer().getFileSystem().addFile(HF,true);
						HF=MyProgram.getComputer().getFileSystem().getFile(path,fileName);
					}
					
					if(HF==null){
						MyProgram.getComputer().addMessage(MessageHandler.FILE_NOT_FOUND);
					}else if(HF.getType()==HF.TEXT){
						HashMap HM=HF.getContent();
						String newData=((TypeString)parameters.get(1)).getStringValue();
System.out.println("File Length: "+newData.length());
System.out.println("File Size Limit: "+MyProgram.getComputer().FILE_SIZE_LIMIT);
						if(newData.length()<=MyProgram.getComputer().FILE_SIZE_LIMIT){
							HM.put("data",newData);
						}else{
System.out.println("Sending file size fail message");
							MyProgram.getComputer().addMessage(MessageHandler.FILE_IO_FAIL_FILE_SIZE,new Object[]{fileName});
							if(MyProgram.getComputer().FILE_SIZE_LIMIT == Computer.FREE_FILE_SIZE_LIMIT){
								MyProgram.getComputer().addMessage(MessageHandler.UPGRADE_FILE_SIZE);
							
							}
						}

					}else{
						MyProgram.getComputer().addMessage(MessageHandler.FILE_IO_FAIL_INVALID_TYPE);
					}
			}catch(Exception e2){
				MyProgram.getComputer().addMessage("An exception occurred in writeFile().");
			}
		}else
		
		//A math function.
		if(name.equals("fileExists")){
			try{	
					String file=((TypeString)parameters.get(0)).getStringValue();
					String data[]=file.split("/");
					String fileName;
					String path="";
					for(int i=0;i<data.length-1;i++)
						path+=data[i]+"/";

					fileName=data[data.length-1];
					HackerFile HF=MyProgram.getComputer().getFileSystem().getFile(path,fileName);
					
					if(HF==null){
						return(new TypeBoolean(false));
					}else{
						return(new TypeBoolean(true));
					}
			}catch(Exception e2){
				MyProgram.getComputer().addMessage("An exception occurred in fileExists().");
			}
		}else
		
		//A math function.
		if(name.equals("indexOf")){
			try{
				String dataString=((TypeString)parameters.get(0)).getStringValue();
				String searchString=((TypeString)parameters.get(1)).getStringValue();
				int index=((TypeInteger)parameters.get(2)).getIntValue();
				return(new TypeInteger(dataString.indexOf(searchString,index)));
			}catch(Exception e){
				MyProgram.getComputer().addMessage("An exception occurred in indexOf().");
			}
		}else
				
		//A math function.
		if(name.equals("intValue")){
			try{
				float f=(Float)((TypeFloat)parameters.get(0)).getRawValue();
				return(new TypeInteger((int)f));
			}catch(Exception e2){
				MyProgram.getComputer().addMessage("An exception occurred in intValue().");
			}
		}else
		
		//A math function.
		if(name.equals("floatValue")){
			try{
				int i=((TypeInteger)parameters.get(0)).getIntValue();
				return(new TypeFloat((float)i));
			}catch(Exception e2){
				MyProgram.getComputer().addMessage("An exception occurred in floatValue().");
			}
		}else
		
		//A math function.
		if(name.equals("parseFloat")){
			if(MyProgram.getComputer().getFileIO()){
				try{
					String s1=((TypeString)parameters.get(0)).getStringValue();
					return(new TypeFloat((float)(new Float(s1))));
				}catch(Exception e2){
					MyProgram.getComputer().addMessage("An exception occurred in parseFloat().");
				}
			}
		}else
		
		if(name.equals("replaceAll")){
			try{
				String s1=((TypeString)parameters.get(0)).getStringValue();
				String s2=((TypeString)parameters.get(1)).getStringValue();
				String s3=((TypeString)parameters.get(2)).getStringValue();
				return(new TypeString(s1.replaceAll(regexEscape(s2),regexEscape(s3))));
			}catch(Exception e){
				MyProgram.getComputer().addMessage("An exception occurred in replaceAll().");
			}
		}else
		
		//A math function.
		if(name.equals("parseInt")){
			if(MyProgram.getComputer().getFileIO()){
				try{
					String s1=((TypeString)parameters.get(0)).getStringValue();
					return(new TypeInteger((int)(new Integer(s1))));
				}catch(Exception e2){
					MyProgram.getComputer().addMessage("An exception occurred in parseInt().");
				}
			}
		}else
		
		//A math function.
		if(name.equals("char")){
			try{
		
				int i1=((TypeInteger)parameters.get(0)).getIntValue();
				if(i1==10||(i1>=32&&i1<=126)){
					return(new TypeString(""+(char)i1));
				}else{
					MyProgram.getComputer().addMessage("Attempted to use illegal character.");

				}
			
			}catch(Exception e){
				MyProgram.getComputer().addMessage("An exception occurred in char().");
			}
		}else
		
		//Message handler.
		if(name.equals("trim")){
			try{
				String data=((TypeString)parameters.get(0)).getStringValue();
				return(new TypeString(data.trim()));
			}catch(Exception e){
				MyProgram.getComputer().addMessage("An exception occurred in trim().");
			}
		}else
		
		//Message handler.
		if(name.equals("toUpper")){
			try{
				String data=((TypeString)parameters.get(0)).getStringValue();
				return(new TypeString(data.toUpperCase()));
			}catch(Exception e){
				MyProgram.getComputer().addMessage("An exception occurred in upperCase().");
			}
		}else
		
		//Message handler.
		if(name.equals("toLower")){
			try{
				String data=((TypeString)parameters.get(0)).getStringValue();
				return(new TypeString(data.toLowerCase()));
			}catch(Exception e){
				MyProgram.getComputer().addMessage("An exception occurred in lowerCase().");
			}
		}else
		
		//Message handler.
		if(name.equals("message")){
			try{
				if(incrementValue("message")<=1||MyProgram.getComputer().isNPC()){
					if(parameters.size()==2){
						String message="";
						String ip="";
						if(parameters.get(0) instanceof TypeString)
							ip=((TypeString)parameters.get(0)).getStringValue();
						if(parameters.get(1) instanceof TypeString)
							message=((TypeString)parameters.get(1)).getStringValue();
						if(message.length()<256){
							CentralLogging.getInstance().addOutput(MyProgram.getComputer().getIP()+" sent message \""+message+"\" to "+ip);
							MyComputerHandler.addData(new ApplicationData("message",message,0,MyProgram.getComputer().getIP()),ip);
						}else
							MyProgram.getComputer().addMessage(MessageHandler.MESSAGE_FAIL_TOO_LONG);
					}
				}
				
			}catch(Exception e){
				MyProgram.getComputer().addMessage("An exception occurred in message().");
			}
		}else
		
		//Compare two strings which are passed in.
		if(name.equals("substr")){
			try{
		
				if(parameters.size()!=3)
					MyProgram.setError("\"substr(string,int,int)\" returns a substring starting and ending with the two indexes provided.");
				else if(!(parameters.get(0) instanceof TypeString)||!(parameters.get(1) instanceof TypeInteger)||!(parameters.get(2) instanceof TypeInteger))
					MyProgram.setError("\"substr(string,int,int)\" returns a substring starting and ending with the two indexes provided.");
				
				//Compare two strings which are passed in.
				String s1="";
				int start=0;
				int finish=0;
				if(parameters.get(0) instanceof TypeString)
					s1=((TypeString)parameters.get(0)).getStringValue();
				if(parameters.get(1) instanceof TypeInteger)
					start=((TypeInteger)parameters.get(1)).getIntValue();
				if(parameters.get(2) instanceof TypeInteger)
					finish=((TypeInteger)parameters.get(2)).getIntValue();
				return(new TypeString(s1.substring(start,finish)));
				
			}catch(Exception e){
				MyProgram.getComputer().addMessage("An exception occurred in substr().");
			}
		}else 
		
		//Split a string.
		if(name.equals("split")){	
			String SplitData[]=((TypeString)parameters.get(0)).getStringValue().split(((TypeString)parameters.get(1)).getStringValue());
			ArrayList AL=new ArrayList();
			for(int i=0;i<SplitData.length;i++)
				AL.add(new TypeString(SplitData[i]));
			TypeArray TA=new TypeArray(AL);
			return(TA);
		}else
		
		if(name.equals("getDate")){
			Calendar c = Calendar.getInstance();
			
			int month=c.get(c.MONTH);
			String monthString="Dec";
			if(month==c.JANUARY)
				monthString="Jan";
			if(month==c.FEBRUARY)
				monthString="Feb";
			if(month==c.MARCH)
				monthString="Mar";
			if(month==c.APRIL)
				monthString="Apr";
			if(month==c.MAY)
				monthString="May";
			if(month==c.JUNE)
				monthString="Jun";
			if(month==c.JULY)
				monthString="Jul";
			if(month==c.AUGUST)
				monthString="Aug";
			if(month==c.SEPTEMBER)
				monthString="Sep";
			if(month==c.OCTOBER)
				monthString="Oct";
			if(month==c.NOVEMBER)
				monthString="Nov";
			if(month==c.DECEMBER)
				monthString="Dec";
				
			int dayOfMonth=c.get(c.DAY_OF_MONTH);
			int year=c.get(Calendar.YEAR);
				
			String stamp=dayOfMonth+"-"+monthString+"-"+year;
			return(new TypeString(stamp));
		}else
		
		if(name.equals("getTime")){
			Calendar c = Calendar.getInstance();
			
			int hour=c.get(Calendar.HOUR);
			int minute=c.get(Calendar.MINUTE);
			int seconds=c.get(Calendar.SECOND);
			int ampm=c.get(Calendar.AM_PM);
			String pm="PM";
			String second;
			String minutes;
			if(seconds<10)
				second="0"+seconds;
			else
				second=""+seconds;
			if(minute<10)
				minutes="0"+minute;
			else
				minutes=""+minute;
			if(ampm==0)
				pm="AM";
				
			if(hour==0)
				hour=12;
				
			String stamp=""+hour+":"+minutes+":"+second+" "+pm;
			return(new TypeString(stamp));
		}else
		
		if(name.equals("join")){
			try{
				ArrayList Array=null;
				if(parameters.get(0) instanceof TypeArray)
					Array=(ArrayList)((TypeArray)parameters.get(0)).getRawValue();
				else if(parameters.get(0) instanceof ArrayList)
					Array=(ArrayList)parameters.get(0);
					
				String joinString=(String)((TypeString)parameters.get(1)).getRawValue();
				String Result="";
				
				for(int i=0;i<Array.size();i++){
					Result+=""+((Variable)Array.get(i)).getRawValue();
					if(i!=Array.size()-1)
						Result+=joinString;
				}
				
				return(new TypeString(Result));
			}catch(Exception e){
				MyProgram.getComputer().addMessage("An exception occurred in the function join().");
			}
		}else
		
		if(name.equals("length")){
			if(parameters.get(0)!=null){
				if(parameters.get(0) instanceof TypeArray)
					return( new TypeInteger(( (ArrayList) ( (TypeArray) parameters.get(0) ).getRawValue()).size()));
				ArrayList Temp=(ArrayList)parameters.get(0);
				return(new TypeInteger(Temp.size()));
			}else
				return(new TypeInteger(0));
		}
			
		if(name.equals("giveTask")){
			Computer C=MyProgram.getComputer();
			if(C.isNPC()){
				String TaskName=(String)((TypeString)parameters.get(0)).getRawValue();
				String TaskLabel=(String)((TypeString)parameters.get(1)).getRawValue();
				Integer QuestID=(Integer)((TypeInteger)parameters.get(2)).getRawValue();
				String IP=(String)((TypeString)parameters.get(3)).getRawValue();
				MyComputerHandler.addData(new ApplicationData("givetask",new Object[]{TaskName,TaskLabel,QuestID},0,MyProgram.getComputer().getIP()),IP);
			}
		}
				
		if(name.equals("setTask")){
			Computer C=MyProgram.getComputer();
			if(C.isNPC()){
				String TaskName=(String)((TypeString)parameters.get(0)).getRawValue();
				Integer QuestID=(Integer)((TypeInteger)parameters.get(1)).getRawValue();
				Boolean SetTo=(Boolean)((TypeBoolean)parameters.get(2)).getRawValue();
				String IP=(String)((TypeString)parameters.get(3)).getRawValue();
				MyComputerHandler.addData(new ApplicationData("settask",new Object[]{TaskName,QuestID,SetTo},0,MyProgram.getComputer().getIP()),IP);
				
			}
		}else
			
		if(name.equals("giveQuest")){
			Computer C=MyProgram.getComputer();
			if(C.isNPC()){
				Integer QuestID=(Integer)((TypeInteger)parameters.get(0)).getRawValue();
				String description=(String)((TypeString)parameters.get(1)).getRawValue();
				String IP=(String)((TypeString)parameters.get(2)).getRawValue();				
				MyComputerHandler.addData(new ApplicationData("givequest",new Object[]{QuestID,description},0,MyProgram.getComputer().getIP()),IP);
			}
		}else
		
		if(name.equals("finishQuest")){
			Computer C=MyProgram.getComputer();
			if(C.isNPC()){
				String IP=(String)((TypeString)parameters.get(1)).getRawValue();
				if(!C.checkRecentQuestFinisher(IP)){//Make sure you can't finish the same quest more than once.
					C.addRecentQuestFinisher(IP);
					
					Integer QuestID=(Integer)((TypeInteger)parameters.get(0)).getRawValue();

					MyComputerHandler.addData(new ApplicationData("finishquest",new Object[]{QuestID},0,MyProgram.getComputer().getIP()),IP);
					
				}
			}
		}else
							
		if(name.equals("takeMoney")){
			Computer C=MyProgram.getComputer();
			if(C.isNPC()){
				Float amount=(Float)((TypeFloat)parameters.get(0)).getRawValue();
				String TaskName=(String)((TypeString)parameters.get(1)).getRawValue();
				Integer QuestID=(Integer)((TypeInteger)parameters.get(2)).getRawValue();
				String IP=(String)((TypeString)parameters.get(3)).getRawValue();

				MyComputerHandler.addData(new ApplicationData("takemoney",new Object[]{amount,TaskName,QuestID},0,MyProgram.getComputer().getIP()),IP);
			}
		}else
								
		if(name.equals("takeCommodity")){
			Computer C=MyProgram.getComputer();
			if(C.isNPC()){
				Float amount=(Float)((TypeFloat)parameters.get(0)).getRawValue();
				Integer CommodityType=(Integer)((TypeInteger)parameters.get(1)).getRawValue();
				String TaskName=(String)((TypeString)parameters.get(2)).getRawValue();
				Integer QuestID=(Integer)((TypeInteger)parameters.get(3)).getRawValue();
				String IP=(String)((TypeString)parameters.get(4)).getRawValue();
				MyComputerHandler.addData(new ApplicationData("takecommodity",new Object[]{amount,CommodityType,TaskName,QuestID},0,MyProgram.getComputer().getIP()),IP);
			}
		}else
									
		if(name.equals("exchangeCommodity")){
			Computer C=MyProgram.getComputer();
			if(C.isNPC()){
				Integer ExchangeAmount=(Integer)((TypeInteger)parameters.get(0)).getRawValue();
				Integer CommodityType=(Integer)((TypeInteger)parameters.get(1)).getRawValue();
				Float ExchangeCost=(Float)((TypeFloat)parameters.get(2)).getRawValue();
				String IP=(String)((TypeString)parameters.get(3)).getRawValue();
				MyComputerHandler.addData(new ApplicationData("exchangecommodity",new Object[]{ExchangeAmount,CommodityType,ExchangeCost},0,MyProgram.getComputer().getIP()),IP);
			}
		}else
			
		if(name.equals("exchangeFile")){
			Computer C=MyProgram.getComputer();
			if(C.isNPC()){
				Integer ExchangeAmount=(Integer)((TypeInteger)parameters.get(0)).getRawValue();
				Integer CommodityType=(Integer)((TypeInteger)parameters.get(1)).getRawValue();
				Float ExchangeCost=(Float)((TypeFloat)parameters.get(2)).getRawValue();
				String IP=(String)((TypeString)parameters.get(3)).getRawValue();
				MyComputerHandler.addData(new ApplicationData("exchangefile",new Object[]{ExchangeAmount,CommodityType,ExchangeCost},0,MyProgram.getComputer().getIP()),IP);
			}
		}else
											
		if(name.equals("giveXP")){
			Computer C=MyProgram.getComputer();
			if(C.isNPC()){
				String Stat=(String)((TypeString)parameters.get(0)).getRawValue();
				Float Amount=(Float)((TypeFloat)parameters.get(1)).getRawValue();
				String IP=(String)((TypeString)parameters.get(2)).getRawValue();
				MyComputerHandler.addData(new ApplicationData("giveexperience",new Object[]{Stat,Amount},0,MyProgram.getComputer().getIP()),IP);
			}
		}else
		
		if(name.equals("giveMoney")){
			Computer C=MyProgram.getComputer();
			if(C.isNPC()){
				float amount=(Float)((TypeFloat)parameters.get(0)).getRawValue();
				String IP=(String)((TypeString)parameters.get(1)).getRawValue();
				ApplicationData MyBank=new ApplicationData("bank",new Float(amount),0,MyProgram.getComputer().getIP());
				MyComputerHandler.addData(MyBank,IP);
			}
		}else
													
		if(name.equals("giveCommodity")){
			Computer C=MyProgram.getComputer();
			if(C.isNPC()){
				Integer commodityType=(Integer)((TypeInteger)parameters.get(0)).getRawValue();
				Float amount=(Float)((TypeFloat)parameters.get(1)).getRawValue();
				String IP=(String)((TypeString)parameters.get(2)).getRawValue();
				ApplicationData MyBank=new ApplicationData("givecommodity",new Object[]{commodityType,amount},0,MyProgram.getComputer().getIP());
				MyComputerHandler.addData(MyBank,IP);
			}
		}else
														
		if(name.equals("giveFile2")){
			Computer C=MyProgram.getComputer();
			if(C.isNPC()){
				String fileID=(String)((TypeString)parameters.get(0)).getRawValue();
				String IP=(String)((TypeString)parameters.get(1)).getRawValue();
				ApplicationData MyBank=new ApplicationData("givefile",fileID,0,IP);
				MyComputerHandler.addData(MyBank,MyProgram.getComputer().getIP());
			}
		}else
		
		if(name.equals("takeFile2")){
			Computer C=MyProgram.getComputer();
			if(C.isNPC()){
				String fileID=(String)((TypeString)parameters.get(0)).getRawValue();
				String IP=(String)((TypeString)parameters.get(1)).getRawValue();
				ApplicationData MyBank=new ApplicationData("takefile",fileID,0,MyProgram.getComputer().getIP());
				MyComputerHandler.addData(MyBank,IP);
			}
		}else
																	
		if(name.equals("giveAccess")){
			Computer C=MyProgram.getComputer();
			if(C.isNPC()){
				String networkName=(String)((TypeString)parameters.get(0)).getStringValue();
				String IP=(String)((TypeString)parameters.get(1)).getRawValue();
				ApplicationData MyApplicationData=new ApplicationData("giveaccess",networkName,0,MyProgram.getComputer().getIP());
				MyComputerHandler.addData(MyApplicationData,IP);
			}
		}else
																	
		if(name.equals("changeNetwork")){
			Computer C=MyProgram.getComputer();
			if(C.isNPC()){
				String networkName=(String)((TypeString)parameters.get(0)).getStringValue();
				String IP=(String)((TypeString)parameters.get(1)).getRawValue();
				ApplicationData MyApplicationData=new ApplicationData("changenetwork2",networkName,0,MyProgram.getComputer().getIP());
				MyComputerHandler.addData(MyApplicationData,IP);
			}
		}
			
		
		}catch(Exception e){
			e.printStackTrace();
		}
        
        return(null);
    }
	
	//Testing main.
	public static void main(String args[]){
		HackerLinker HL=new HackerLinker(null,null);
		System.out.println(HL.regexEscape("// . $ ^ { [ ( | ) * + ? \\"));

	}
}
