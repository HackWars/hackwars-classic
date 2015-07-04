package Assignments;
import com.plink.dolphinnet.*;
import java.util.*;
import java.io.*;
import java.math.*;
import Game.*;

/**
An implementation of the abstract base assignment...This is where the
processing to be distributed onto other computers should be placed.
Every instance of an assignment is distributed to a specific Reporter
(client). So the more Assignments the more division of work.
*/

public class PacketAssignment extends Assignment implements Serializable{

	private float bankMoney=0.0f;
	private float pettyCash=0.0f;
	private float commodities[]=null;
	private float maxCPU=0.0f;
	private int cpuType=0;
	private int memoryType=0;
	private Object messages[]=null;
	private PacketPort PacketPorts[]=null;
	private PacketPort ScannedPorts[]=null;
	private ArrayList Damage=null;
	private Object Directory[]=null;
	private Object SecondaryDirectory[]=null;
	private HackerFile HF=null;
	private PacketWatch PacketWatches[]=null;
	private String title=null;
	private String body=null;
	private boolean requestPrimary=false;
	private boolean requestSecondary=false;
	private boolean requestHardware=false;
	private int defaultBank=0;
	private int defaultAttack=0;
	private int defaultFTP=0;
	private int defaultHTTP=0;
	private int defaultShipping=0;
	private int hdType=0;
	private int hdQuantity=0;
	private Object Choices[]=null;
	private Object HealthUpdates[]=null;
	private HashMap PeakCode=null;
	private String LogMessages[]=null;
	private int hackCount=0;
	private int voteCount=0;
	private int countDown=-1;
	private int serverLoad=0;
	private int maxHDSpace=0;
	private int requestPrimaryID=0;
	private int requestSecondaryID=0;
	private Object CAPTCHA=null;
	private float cpuCost=0.0f;
	private PacketNetwork MyPacketNetwork=null;
	private HashMap LoadFile=null;
	private float healDiscount=1.0f;
    private HashMap preferences = null;
	private int votesLeft = 0;
	private boolean allowedDir = true;

	/////////////////////////
	// Constructor.
	public PacketAssignment(int id){
		super(id);
		messages=null;
		PacketPorts=null;
	}
	/////////////////////////
	// Getters/Setters
	public void setRequestPrimary(boolean requestPrimary,int id){
		this.requestPrimaryID=id;
		this.requestPrimary=requestPrimary;
	}
	
	/**
	This is only used for Hacktendo Games. It is the save data loaded off a player's HD.
	*/
	public HashMap getLoadFile(){
		return(LoadFile);
	}
	
	public void setLoadFile(HashMap LoadFile){
		this.LoadFile=LoadFile;
	}	
	
    public HashMap getPreferences() {
        return (preferences);
    }
    
    public void setPreferences(HashMap prefs) {
        this.preferences = prefs;
    }
    
	public void setRequestHardware(boolean requestHardware){
		this.requestHardware=requestHardware;
	}
	
	public boolean getRequestHardware(){
		return(requestHardware);
	}

	public void setRequestSecondary(boolean requestSecondary,int id){
		this.requestSecondaryID=id;
		this.requestSecondary=requestSecondary;
	}
	
	public void setBankMoney(float bankMoney){
		this.bankMoney=bankMoney;
	}
	public void setPettyCash(float pettyCash){
		this.pettyCash=pettyCash;
	}
	public float getBankMoney(){
		return(bankMoney);
	}
	public float getPettyCash(){
		return(pettyCash);
	}
	
	public void setMessages(Object messages[]){
		this.messages=messages;
	}
	
	public Object[] getMessages(){
		return(messages);
	}
	
	public void addDamage(int port,int damage){
		if(Damage==null)
			Damage=new ArrayList();
		Object o[]=new Object[]{new Integer(port),new Integer(damage)};
		Damage.add(o);
	}
	
	public void addDamage(int port,int damage,String zombie){
		if(Damage==null)
			Damage=new ArrayList();
		Object o[]=new Object[]{new Integer(port),new Integer(damage),zombie};
		Damage.add(o);
	}
	
	public ArrayList getDamage(){
		return(Damage);
	}
	
	public void setPacketPorts(PacketPort PacketPorts[]){
		this.PacketPorts=PacketPorts;
	}
	
	public PacketPort[] getPacketPorts(){
		return(PacketPorts);
	}
	
	public void setScannedPorts(PacketPort ScannedPorts[]){
		this.ScannedPorts=ScannedPorts;
	}
	
	public PacketPort[] getScannedPorts(){
		return(ScannedPorts);
	}
	
	public void setPacketWatches(PacketWatch PacketWatches[]){
		this.PacketWatches=PacketWatches;
	}
	
	public PacketWatch[] getPacketWatches(){
		return(PacketWatches);
	}
	
	public Object[] getDirectory(){
		return(Directory);
	}
	
	public void setDirectory(Object Directory[]){
		this.Directory=Directory;
	}
	
	public void setSecondaryDirectory(Object SecondaryDirectory[]){
		this.SecondaryDirectory=SecondaryDirectory;
	}
	
	public Object[] getSecondaryDirectory(){
		return(SecondaryDirectory);
	}
	
	public void setAllowedDir(boolean b) {
		this.allowedDir = b;
	}
	
	public boolean getAllowedDir() {
		return this.allowedDir;
	}
	
	public HackerFile getFile(){
		return(HF);
	}
	
	public void setFile(HackerFile HF){
		this.HF=HF;
	}
	
	public void setCPUType(int cpuType){
		this.cpuType=cpuType;
	}
	
	public int getCPUType(){
		return(cpuType);
	}
	
	public void setMemoryType(int memoryType){
		this.memoryType=memoryType;
	}
	
	public int getMemoryType(){
		return(memoryType);
	}
	
	public void setCPUMax(float maxCPU){
		this.maxCPU=maxCPU;
	}
	
	public float getCPUMax(){
		return(maxCPU);
	}
	
	public void setTitle(String title){
		this.title=title;
	}
	
	public String getTitle(){
		return(title);
	}
	
	public void setBody(String body){
		this.body=body;
	}
	
	public String getBody(){
		return(body);
	}
	
	public boolean requestPrimary(){
		return(requestPrimary);
	}
	
	public boolean requestSecondary(){
		return(requestSecondary);
	}
	
	public int getRequestSecondaryID(){
		return(requestSecondaryID);
	}

	public int getRequestPrimaryID(){
		return(requestPrimaryID);
	}
	
	/**
	Set the default bank associated with this computer.
	*/
	public void setDefaultBank(int defaultBank){
		this.defaultBank=defaultBank;
	}
	
	/**
	Set the default shipping application associated with this computer.
	*/
	public void setDefaultShipping(int defaultShipping){
		this.defaultShipping=defaultShipping;
	}
	
	/**
	Set the default attack port associated with this computer.
	*/
	public void setDefaultAttack(int defaultAttack){
		this.defaultAttack=defaultAttack;
	}
	
	/**
	Set the default HTTP port associated with this computer.
	*/
	public void setDefaultHTTP(int defaultHTTP){
		this.defaultHTTP=defaultHTTP;
	}
	
	/**
	Set the default FTP port associated with this computer.
	*/
	public void setDefaultFTP(int defaultFTP){
		this.defaultFTP=defaultFTP;
	}
	
	/**
	Get the default bank associated with this computer.
	*/
	public int getDefaultBank(){
		return(defaultBank);
	}
	
	/**
	Get the default attack port associated with this computer.
	*/
	public int getDefaultAttack(){
		return(defaultAttack);
	}
	
	/**
	Get the default HTTP port associated with this computer.
	*/
	public int getDefaultHTTP(){
		return(defaultHTTP);
	}
	
	/**
	Get the default FTP port associated with this computer.
	*/
	public int getDefaultFTP(){
		return(defaultFTP);
	}
	
	public int getDefaultShipping(){
		return(defaultShipping);
	}

	/** Run the assignments implemented task. */
	public Object execute(DataHandler DH){
		DH.addData(this);
		finish();
		return(null);
	}
	
	/**
	Set the type of HD installed on the computer.
	*/
	public void setHDType(int hdType){
		this.hdType=hdType;
	}
	
	/**
	Return the type of HD installed on this computer.
	*/
	public int getHDType(){
		return(hdType);
	}
	
	/**
	Set the quantity of files currently in the file system.
	*/
	public void  setHDQuantity(int hdQuantity){
		this.hdQuantity=hdQuantity;
	}
	
	/**
	Get the quantity of files currently in the file system.
	*/
	public int getHDQuantity(){
		return(hdQuantity);
	}
	
	/**
	Insert a piece of data into the choices array.
	*/
	public void setChoices(Object Choices[]){
		this.Choices=Choices;
	}
	
	/**
	Return the array list of choices.
	*/
	public Object[] getChoices(){
		return(Choices);
	}
	
	/**
	Set the code that has been returned as the result of a peak command.
	*/
	public void setPeakCode(HashMap PeakCode){
		this.PeakCode=PeakCode;
	}
	
	/**
	Get the code that has been returned as the result of a peak finalize.
	*/
	public HashMap getPeakCode(){
		return(PeakCode);
	}
	
	/**
	Set the countdown to being booted off the server.
	*/
	public void setCountDown(int countDown){
		this.countDown=countDown;
	}
	
	/**
	Get the countdown to being booted off the server.
	*/
	public int getCountDown(){
		return(countDown);
	}
	
	/**
	Set the number of successful hacks that this player has performed.
	*/
	public void setHackCount(int hackCount){
		this.hackCount=hackCount;
	}
	
	/**
	Get the number of successful hacks that this player has performed.
	*/
	public int getHackCount(){
		return(hackCount);
	}
	
	/**
	Set the number of successful hacks that this player has performed.
	*/
	public void setVoteCount(int voteCount){
		this.voteCount=voteCount;
	}
	
	/**
	Get the number of successful hacks that this player has performed.
	*/
	public int getVoteCount(){
		return(voteCount);
	}
	
	/**
	Add a health update for a specific port.
	*/
	public void addHealthUpdate(ArrayList UpdatesArray){
		HealthUpdates=new Object[UpdatesArray.size()];
		for(int i=0;i<UpdatesArray.size();i++){
			Object[] data=(Object[])UpdatesArray.get(i);
			HealthUpdates[i]=data;
		}
	}
	
	/**
	Get health updates.
	*/
	public Object[] getHealthUpdates(){
		return(HealthUpdates);
	}
	
	/**
	Set the current server load of the server.
	*/
	public void setServerLoad(int serverLoad){
		this.serverLoad=serverLoad;
	}
	
	/**
	Get the current server load of the computer.
	*/
	public int getServerLoad(){
		return(serverLoad);
	}
	
	/**
	Add the log information to the packet.
	*/
	public void addLogUpdate(ArrayList LogMessages){
		this.LogMessages=new String[LogMessages.size()];
		for(int i=0;i<LogMessages.size();i++){
			this.LogMessages[i]=((String[])LogMessages.get(i))[0];
		}
	}
	
	/**
	Get the current log information.
	*/
	public String[] getLogUpdate(){
		return(LogMessages);
	}
	
	/**
	Set CPU cost.
	*/
	public void setCPUCost(float cpuCost){
		this.cpuCost=cpuCost;
	}
	
	public float getCPUCost(){
		return(cpuCost);
	}
	
	/**
	CAPTCHA Image.
	*/
	public Object getCAPTCHA(){
		return(CAPTCHA);
	}
	
	public void setCAPTCHA(Object CAPTCHA){
		this.CAPTCHA=CAPTCHA;
	}
	
	/**
	HD space.
	*/
	public void setHDMaximum(int maxHDSpace){
		this.maxHDSpace=maxHDSpace;
	}
	
	public int getHDMaximum(){
		return(maxHDSpace);
	}
	
	/**
	Get the network packet that provides information about the current network node.
	*/
	public void setPacketNetwork(PacketNetwork MyPacketNetwork){
		this.MyPacketNetwork=MyPacketNetwork;
	}
	
	public PacketNetwork getPacketNetwork(){
		return(MyPacketNetwork);
	}
	
	/**
	The commodities on the player's computer.
	*/
	public void setCommodities(float commodities[]){
		this.commodities=commodities;
	}
	
	public float[] getCommodities(){
		return(commodities);
	}
	
	public void setHealDiscount(float healDiscount){
		this.healDiscount=healDiscount;
	}
	
	public float getHealDiscount(){
		return(healDiscount);
	}
	
	public int getVotesLeft(){
		return(votesLeft);
	}
	
	public void setVotesLeft(int votesLeft){
		this.votesLeft = votesLeft;
	}
}
