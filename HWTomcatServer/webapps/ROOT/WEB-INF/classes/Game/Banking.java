package Game;

/**
Class which contains the banking application.
*/
import Hackscript.Model.*;
import java.io.*;
import java.util.HashMap;
public class Banking extends Program{

	private String depositScript=null;//Script which runs when a deposit is requested.
	private String withdrawScript=null;//Script which runs when withdraw is requested.
	private String transferScript=null;//Script which runs when a transfer is requested.
	
	private NetworkSwitch MyComputerHandler=null;//Used to dispatch ApplicationData to other computers.
	private Computer MyComputer=null;//The computer executing this program.
	
	/* The amount which has been provided for this transaction.
	   In the case of withdraws the script running can't exceed this amount.*/
	private float amount=0.0f;
	private float initialAmount=0.0f;
	private float pettyCashTarget=0.0f;
	private boolean withdraw=false;
	     
	private Port ParentPort=null;//NOT CURRENTLY USED. The port with this program installed. 
	private String targetIP="";//In the case of a transfer the IP that is being targeted.
	
	//constructor
	public Banking(NetworkSwitch MyComputerHandler,Computer MyComputer,Port ParentPort){
		super.setComputerHandler(MyComputerHandler);
		super.setComputer(MyComputer);
		this.MyComputerHandler=MyComputerHandler;
		this.MyComputer=MyComputer;
		this.ParentPort=ParentPort;
	}
	
	/**
	Deposit money in the the players bank.
	*/
	public void deposit(float amount){
		MyComputer.setBank(MyComputer.getBank()+amount);
	}
	
	/**
	Get the IP address of the computer that this program is installed on.
	*/
	public String getIP(){
		return(MyComputer.getIP());
	}
	
	/**
	Get the malicious IP that should be delivered money.
	*/
	public String getMaliciousTarget(){
		return(ParentPort.getMaliciousTarget());
	}
	
	/**
	Return the target IP of a money transfer.
	*/
	public String getTargetIP(){
		return(targetIP);
	}
	
	/**
	Return the amount that has been requested for the transaction.
	*/
	public float getAmount(){
		return(amount);
	}
	
	/**
	Set the amount still available from the transaction request.
	*/
	public void setAmount(float amount){
		this.amount=amount;
	}
	
	/**
	Return the initial amount that has been requested for the transaction.
	*/
	public float getInitialAmount(){
		return(initialAmount);
	}
	
	/**
	Get whether the requested operation was a withdraw.
	*/
	public boolean isWithdraw(){
		return(withdraw);
	}
	
	/**
	Get whether the requested operation was a withdraw.
	*/
	boolean deposit=false;
	public boolean isDeposit(){
		return(deposit);
	}
	
	/*
	Get whether the requested operation was a withdraw.
	*/
	boolean transfer=false;
	public boolean isTransfer(){
		return(transfer);
	}
	
	
	/**
	Return the amount in the pettycash of the computer this is attached to.
	*/
	public float getPettyCash(){
		return(MyComputer.getPettyCash());
	}
	
	/**
	Set the amount of money in the computer's petty cash.
	*/
	public void setPettyCash(float pettyCash){
		MyComputer.setPettyCash(pettyCash);
	}
	
	/**
	Return the amount in the pettycash of the computer this is attached to.
	*/
	public float getBank(){
		return(MyComputer.getBank());
	}
	
	/**
	Set the amount of money in the computer's petty cash.
	*/
	public void setBank(float bankMoney){
		MyComputer.setBank(bankMoney);
	}
	
	/**
	Return the amount in the bank of the computer this is attached to.
	*/
	public float getBankMoney(){
		return(MyComputer.getBankMoney());
	}
	
	/**
	Set the script to run when a deposit is performed.
	*/
	public void setDepositScript(String depositScript){
		this.depositScript=depositScript;
	}
	
	/**
	Get the script that is to run when a deposit is performed.
	*/
	public String getDepositScript(){
		return(depositScript);	
	}
	
	/**
	Set the script that is to run when a withdraw is performed.
	*/
	public void setWithdrawScript(String withdrawScript){
		this.withdrawScript=withdrawScript;
	}
	
	/**
	Get the script that is to run when a withdraw is performed.
	*/
	public String getWithdrawScript(){
		return(withdrawScript);
	}
	
	/**
	Set the script that is to run when a transfer is requested.
	*/
	public void setTransferScript(String transferScript){
		this.transferScript=transferScript;
	}
	
	/**
	Provides an ApplicationData packet and executes scripts accordingly.
	*/
	public void execute(ApplicationData MyApplicationData){
		
		String data=null;
		
		//A 'deposit' function call.
		if(MyApplicationData.getFunction().equals("deposit")){
			amount=(Float)MyApplicationData.getParameters();
			initialAmount=amount;
			if(amount>MyComputer.getPettyCash())
				amount=MyComputer.getPettyCash();
			data=depositScript;
			withdraw=false;
			deposit=true;
			transfer=false;
		}else
		
		//A 'withdraw' function call.
		if(MyApplicationData.getFunction().equals("withdraw")){
			amount=(Float)MyApplicationData.getParameters();
			initialAmount=amount;
			if(amount>MyComputer.getBankMoney()){
				amount=MyComputer.getBankMoney();
			}
			data=withdrawScript;
			withdraw=true;
			deposit=false;
			transfer=false;
		}else 
		
		//A 'transfer' function call.
		if(MyApplicationData.getFunction().equals("transfer")){
			targetIP=(String)((Object[])MyApplicationData.getParameters())[0];

			//We make an exception for Alexi.
			if(MyComputer.getTotalLevel()<15&&!targetIP.equals("900.800.7.012")){//Noob check.
				MyComputer.addMessage(MessageHandler.TRANSFER_FAIL_NOOB);
				MyComputer.sendPacket();
				return;
			}
		
			amount=(Float)((Object[])MyApplicationData.getParameters())[1];
			initialAmount=amount;
			if(amount>MyComputer.getPettyCash())
				amount=MyComputer.getPettyCash();
			data=transferScript;
			withdraw=false;
			transfer=true;
			deposit=false;
		}else
			return;
					
		try{
			HackerLinker HL=new HackerLinker(this,MyComputerHandler);

			RunFactory.runCode(data,HL,MyComputer.MAX_OPS);

		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	installScript(HashMap Script);
	Installs a script on the various entrance points on this program.
	*/
	public void installScript(HashMap Script){
		depositScript=(String)Script.get("deposit");
		withdrawScript=(String)Script.get("withdraw");
		transferScript=(String)Script.get("transfer");
	}
	
	/**
	Returns the keys associated with this program type.
	*/
	public String[] getTypeKeys(){
		String returnMe[]=new String[]{"deposit","withdraw","transfer"};
		return(returnMe);
	}
	
	/**
	Return a hash map representation of the program currently installed on this port.
	*/
	public HashMap getContent(){
		HashMap returnMe=new HashMap();
		returnMe.put("deposit",depositScript);
		returnMe.put("withdraw",withdrawScript);
		returnMe.put("transfer",transferScript);
		return(returnMe);
	}
	
	/**
	Set the petty cash amount used to decide when stealing should take place.
	*/
	public void setPettyCashTarget(float pettyCashTarget){
		this.pettyCashTarget=pettyCashTarget;
	}
	
	/**
	Get the petty cash amount used to decide when stealing should take place.
	*/
	public float getPettyCashTarget(){
		return(pettyCashTarget);
	}
	
	
	/**
	Output the class data in XML format.
	*/
	public String outputXML(){
		String returnMe="<code>\n";
		returnMe+="<withdraw><![CDATA[";
		if(withdrawScript!=null)
			returnMe+=withdrawScript.replaceAll("]]>","]]&gt;");
		else
			returnMe+=withdrawScript;
		returnMe+="]]></withdraw>\n";
		returnMe+="<deposit><![CDATA[";
		if(depositScript!=null)
			returnMe+=depositScript.replaceAll("]]>","]]&gt;");
		else
			returnMe+=depositScript;
		returnMe+="]]></deposit>\n";
		returnMe+="<transfer><![CDATA[";
		if(transferScript!=null)
			returnMe+=transferScript.replaceAll("]]>","]]&gt;");
		else
			returnMe+=transferScript;
		returnMe+="]]></transfer>\n";
		returnMe+="</code>\n";
		return(returnMe);
	}
}

