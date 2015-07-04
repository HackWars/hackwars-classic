package Game;
/**
HackerFile.java

A file in the Hacker game.
*/
import java.util.*;
import java.io.*;
public class HackerFile implements Serializable{

	//File types.
	public static final int BANKING_COMPILED=0;
	public static final int BANKING_SCRIPT=1;
	public static final int ATTACKING_COMPILED=2;
	public static final int ATTACKING_SCRIPT=3;
	public static final int WATCH_COMPILED=4;
	public static final int WATCH_SCRIPT=5;
	public static final int FTP_COMPILED=6;
	public static final int FTP_SCRIPT=7;
	public static final int FIREWALL=8;
	public static final int TEXT=9;
	public static final int CPU=10;
	public static final int HD=11;
	public static final int HTTP=12;
	public static final int MEMORY=13;
	public static final int IMAGE=14;
	public static final int HTTP_SCRIPT=15;
	public static final int CLUE=16;
	public static final int BOUNTY=17;
	public static final int AGP=18;
	public static final int PCI=19;
	public static final int GAME_PROJECT=20;
	public static final int GAME=21;
	public static final int QUEST_GAME=22;
	public static final int SHIPPING_COMPILED=23;
	public static final int SHIPPING_SCRIPT=24;
	public static final int QUEST_ITEM=25;
	public static final int COMMODITY_SLIP=26;
	public static final int CHALLENGE=27;
    
    // new firewall type
    public static final int NEW_FIREWALL = 28;
	
    // trash file
	public static final int TRASH=29;

    // old firewalls can still stack, because they'll all have the same traits, and can be sold for the old values. Any type not in here will just be overwritten.
	public static final int STACKING[]={0,2,4,6,8,10,11,12,13,14,16,17,18,19,21,22,23,25,26,27,28,29};
	
	//Data.
	private String location="";//Location in file system of file.
	private String name="";//Name of file.
	private HashMap Content=null;//Contents of file.
	private int type=0;//Type of file (from constants).
	private float cpuCost=0.0f;//CPU cost associated with file.
	private String description="";//Description of file.
	private String maker="";//Maker of file.
	private float price=0.0f;//Price of file (for store).
	private int quantity=0;//Quanity associated with file (for stacking files).
	
	//Constructor.
	public HackerFile(int type) {
		this.type=type;
	}
	
	/**
	Return a string representing the type of file that this is.
	*/
	public String getTypeString(){
		if(type==BANKING_COMPILED||type==ATTACKING_COMPILED||type==WATCH_COMPILED||type==FTP_COMPILED||type==HTTP||type==SHIPPING_COMPILED)
			return("compiled");
		if(type==BANKING_SCRIPT||type==ATTACKING_SCRIPT||type==WATCH_SCRIPT||type==FTP_SCRIPT||type==HTTP_SCRIPT||type==SHIPPING_SCRIPT)
			return("script");
		if(type==TEXT)
			return("text");
		if(type==FIREWALL)
			return("firewall");
		if(type==IMAGE)
			return("image");
		if(type==AGP||type==PCI)
			return("hardware");
		if(type==TRASH)
			return("trash");
		return("");
	}
	
	/**
	Set content in file. Varies depending on file type.
	*/
	public void setContent(HashMap Content){
		this.Content=Content;
	}
	
	/**
	Get file contents.
	*/
	public HashMap getContent(){
		return(Content);
	}
    
    /**
            * Get special keys contents for Firewalls()
            */
    public HashMap getSpecial(int num) {
		if(Content==null){
			return null;
		}
        return((HashMap)Content.get("specialAttribute" + num));
    }
    
            

	/**
	Set the CPU cost associated with this file once installed.
	*/
	public void setCPUCost(float cpuCost){
		this.cpuCost=cpuCost;
	}
	
	/**
	Get the CPU cost associated with this file once installed.
	*/
	public float getCPUCost(){
		return(cpuCost);
	}
	
	/**
	Set the location in the file system.
	*/
	public void setLocation(String location){
		this.location=location;
	}
	
	/**
	Get the file system location
	*/
	public String getLocation(){
		return(location);
	}
	
	/**
	Set the file name.
	*/
	public void setName(String name){
		this.name=name;
	}
	
	/**
	Get the file name.
	*/
	public String getName(){
		return(name);
	}
	
	/**
	Set the description associated with this file (important for files in stores).
	*/
	public void setDescription(String description){
		this.description=description;
	}
	
	/**
	Get the file description.
	*/
	public String getDescription(){
		if(type==BOUNTY){
			return(description+"Iterations Left: "+(String)Content.get("count"));
		}
			
		return(description);
	}
	
	/**
	Set the player who created this script (applies to compiled applications).
	*/
	public void setMaker(String maker){
		this.maker=maker;
	}
	
	/**
	Get the maker.
	*/
	public String getMaker(){
		return(maker);
	}
	
	/**
	Get the type of file.
	*/
	public int getType(){
		return(type);
	}
	
	/**
	Set the type of file (from constants list).
	*/
	public void setType(int type){
		this.type=type;
	}
	
	/**
	Get the dollar value of this program.
	*/
	public float getPrice(){
		return(price);
	}
	
	/**
	Set the dollar value of this program.
	*/
	public void setPrice(float price){
		this.price=price;
	}
	
	/**
	Get the quantity (used for stacking file types).
	*/
	public int getQuantity(){
		return(quantity);
	}
	
	/**
	Set the quqnity.
	*/
	public void setQuantity(int quantity){
		this.quantity=quantity;
	}
	
	/**
	Check whether this file is stacking.
	*/
	public boolean isStacking(){
		for(int i=0;i<STACKING.length;i++){
			if(STACKING[i]==type)
				return(true);
		}
		return(false);
	}
	
	/**
	Get the keys associated with the file type used to parse the XML structure of this file.
	*/
	public String[] getTypeKeys(){
		if(type==BANKING_COMPILED||type==BANKING_SCRIPT){
			String returnMe[]=new String[]{"deposit","withdraw","transfer"};
			return(returnMe);
		}else
		if(type==ATTACKING_COMPILED||type==ATTACKING_SCRIPT||type==SHIPPING_COMPILED||type==SHIPPING_SCRIPT){
			String returnMe[]=new String[]{"initialize","finalize","continue"};
			return(returnMe);
		}else
		if(type==WATCH_COMPILED||type==WATCH_SCRIPT){
			String returnMe[]=new String[]{"fire"};
			return(returnMe);
		}else
		if(type==FTP_COMPILED||type==FTP_SCRIPT){
			String returnMe[]=new String[]{"put","get"};
			return(returnMe);
		}else if(type==HTTP||type==HTTP_SCRIPT){
			String returnMe[]={"enter","exit","submit"};
			return(returnMe);
		}else if(type==CLUE){
			String returnMe[]={"currentstep","cluelevel","step0","step1","step2","step3","step4","step5"};
			return(returnMe);
		}else if(type==BOUNTY){
			String returnMe[]={"count","script","maker","type","reward","target","bountyip","timeout"};
			return(returnMe);
		}else if(type==PCI||type==AGP){
			String returnMe[]={"attribute0","attribute1","attribute2","quality0","quality1","quality2","timeout","maxquality","currentquality","lastdegrade"};
			return(returnMe);
		}else if(type==QUEST_ITEM){
			String returnMe[]={"questid","itemname","imageid"};
			return(returnMe);
		}else if(type==CHALLENGE){
			String returnMe[]={"input","output","inputtype","outputtype","task","questid","identifier"};
			return(returnMe);
		}else if(type==QUEST_GAME){
			String returnMe[]=new String[]{"data","level","questid","task"};
			return(returnMe);
		}else if(type==TRASH){
			String returnMe[]={"itemname","imageid"};
			return(returnMe);
		} else if (type==NEW_FIREWALL) {
            // old firewalls only had "data" (index of the static final in in Firewall.java of the given firewall type) and "level" (purchase level)
            // new firewalls:
            //            "name" (the name of the firewall), 
            //             "baseSellPrice" (the base price used to calculate the 'sell to store' price),
            //            "useLevel" is the total level required to use this FW
            //            "cpu" is the CPU points this firewall uses
            //            "baseDamageModifer" is ( 1 - absorption/100)  -- how much damage gets through -- used to calculate the % for each firewall
            //            "maxDamage" (the maximum damage a firewall of this level can do)
            //String returnMe[] = {"name", "baseSellPrice", "useLevel", "cpu", "baseDamageModifier", "maxDamage"};
            
            // ON SECOND THOUGHT: I lied.  We'll just use the hashmap in NewFireWall.java to be the only place with the information about the firewalls.  Much better.
            String returnMe[] = {"bank_damage_modifier", "attack_damage_modifier", "redirect_damage_modifier", "ftp_damage_modifier", "http_damage_modifier", "specialAttribute1", "specialAttribute2", "attack_damage", "equip_level","name","store_price"};
            return(returnMe);
        }
		return(new String[]{"data","level"});
	}
    
    // this is only called in the case that it's a new firewall
    public String[] getSpecialKeys() {
        return (new String[] {"name", "long_desc", "short_desc", "value"});
    }
	
	/**
	Returns the translated type of this file to a port type (necessary since different constants are used).
	*/
	public int getPortType(){
		if(type==BANKING_COMPILED)
			return(Port.BANKING);
		if(type==ATTACKING_COMPILED)
			return(Port.ATTACK);
		if(type==FTP_COMPILED)
			return(Port.FTP);
		if(type==HTTP)
			return(Port.HTTP);
		if(type==SHIPPING_COMPILED)
			return(Port.REDIRECT);
			
		return(-1);
	}
	
	/**
	Simple string representation of file for testing.
	*/
	public String toString(){
		String returnMe="\nFile{\n";
		returnMe+="    Name: "+name+"\n";
		returnMe+="    Quantity: "+quantity+"\n";
		returnMe+="}\n";
		return(returnMe);
	}
	
	/**
	Output the file system in XML format.
	*/
	public String outputXML(){
		String returnMe="<file>\n";
		
		if(name!=null)
			returnMe+="<name><![CDATA["+name.replaceAll("]]>","]]&gt;")+"]]></name>\n";
		else
			returnMe+="<name><![CDATA["+name+"]]></name>\n";
		
		returnMe+="<maker><![CDATA["+maker+"]]></maker>\n";
		
		if(location!=null)
			returnMe+="<location><![CDATA["+location.replaceAll("]]>","]]&gt;")+"]]></location>\n";
		else
			returnMe+="<location><![CDATA["+location+"]]></location>\n";
		
		if(description!=null)
			returnMe+="<description><![CDATA["+description.replaceAll("]]>","]]&gt;")+"]]></description>\n";
		else
			returnMe+="<description><![CDATA["+description+"]]></description>\n";
		
		returnMe+="<quantity>"+quantity+"</quantity>\n";
		returnMe+="<type>"+type+"</type>\n";
		returnMe+="<price>"+price+"</price>\n";
		returnMe+="<cpu>"+cpuCost+"</cpu>\n";
		returnMe+="<content>\n";
		String Keys[] = getTypeKeys();
		for(int i=0; i<Keys.length; i++){
			String key=Keys[i];
            if (key.equals("specialAttribute1") || key.equals("specialAttribute2")) {
                returnMe+="<" + key + ">\n";
                HashMap specials = null;
                if (key.equals("specialAttribute1")) {
                    specials = getSpecial(1);
                } else {
                    specials = getSpecial(2);
                }
                
                String[] specialKeys = getSpecialKeys();
                for (int j=0; j < specialKeys.length; j++) {
                    String specialKey = specialKeys[j];
        			returnMe+="<"+specialKey+"><![CDATA[";
                    
        			if(((String)specials.get(specialKey))!=null)
        				returnMe+=((String)specials.get(specialKey)).replaceAll("]]>","]]&gt;");
        			else
        				returnMe+=((String)specials.get(specialKey));
        				
        			returnMe+="]]></"+specialKey+">\n";
                }
                returnMe+="</" + key + ">\n";
            } else {
                returnMe+="<"+key+"><![CDATA[";
    			if((Content.get(key))!=null)
    				returnMe+=(""+Content.get(key)).replaceAll("]]>","]]&gt;");
    			else
    				returnMe+=(""+Content.get(key));
                returnMe+="]]></"+key+">\n";
            }
		}
		returnMe+="</content>\n";
		returnMe+="</file>\n";
		return(returnMe);
	}
	
	/**
	Clone this hacker file.
	*/
	public HackerFile clone(){
		HackerFile returnMe=new HackerFile(type);
		returnMe.setLocation(location);
		returnMe.setName(name);
		returnMe.setContent((HashMap)Content.clone());
		returnMe.setCPUCost(cpuCost);
		returnMe.setDescription(description);
		returnMe.setMaker(maker);
		returnMe.setPrice(price);
		returnMe.setQuantity(quantity);
		return(returnMe);
	}
	
	/**
	Checksum two hacker files.
	*/
	public boolean checkSumFailed(HackerFile HFCheck){
		
		/*
		if(type==NEW_FIREWALL){
			return true;
		}
		*/
		
		HackerFile HF=this;
		String checkSum1="";
		String checkSum2="";
		
		checkSum1 = getCheckSumString(HF);
		checkSum2 = getCheckSumString(HFCheck);
		
		/*
		Keys=HFCheck.getTypeKeys();
		for(int i=0;i<Keys.length;i++){
			checkSum2+=HFCheck.getContent().get(Keys[i]);
		}
		*/
		
		//if(HF.getType()==HFCheck.getType())
		if(!(checkSum1.equals(checkSum2))||!(HF.getCPUCost()==HFCheck.getCPUCost())||!(HF.getName().equals(HFCheck.getName()))){
			return(true);
		}
		return(false);
	}
	
	private String getCheckSumString(HackerFile HF) {
		String checkSum="";
		String Keys[]=HF.getTypeKeys();

		for(int i=0;i<Keys.length;i++){
			if (Keys[i].equals("specialAttribute1") || Keys[i].equals("specialAttribute2")) {
                HashMap specials = null;
                if (Keys[i].equals("specialAttribute1")) {
                    specials = getSpecial(1);
                } else {
                    specials = getSpecial(2);
                }
                
                String[] specialKeys = getSpecialKeys();
                for (int j=0; j < specialKeys.length; j++) {
                    String specialKey = specialKeys[j];
        			if(((String)specials.get(specialKey))!=null)
        				checkSum+=(String)specials.get(specialKey);
        			else
        				checkSum+=(String)specials.get(specialKey);
				}
			} else {
				checkSum+=HF.getContent().get(Keys[i]);
			}
		}
		
		return checkSum;

	}
	
}
