package Game;
/**
FireWall.java

Contains basic information about a fire wall installed on a port.
*/
import java.util.*;
import Assignments.*;

public class NewFireWall{
	public final static String FireWallNames[] = {"None","PortProtector","PwnPreventer","DataShield","PacketBuster","TrafficTender","DigitalFortress","ForceField","RubyGuardian","DiamondDefender","ADNArmour"};
	public static final float ABSORPTION_PRICE_MULTIPLIER = 100.0f;
    
    public static final int NoFireWall=0;
    public static final int BasicFireWall=1;
    public static final int MediumFireWall=2;
    public static final int GreaterFireWall=3;
    public static final int BasicAttackingFireWall=4;
    public static final int MediumAttackingFireWall=5;
    public static final int GreaterAttackingFireWall=6;
    public static final int UltimateAttackingFireWall=7;

    private NetworkSwitch MyComputerHandler = null;
    private HackerFile hackerFile = null;       // the HackerFile used to setup this specific firewall
    
    private String name;                        // firewall name
    private int equipLevel;
    private int maxAttack;
    private float baseDamageModifier;          // damage modifier is the value to multiple the original damage by to get the actual damage (= 1 - absorption/100)
    private float cpuCost;
    private String description;
    private String maker;
    private float maxSpecialAttributeValue;
    
    // values for this specific firewall
    private float bankDamageModifier;
    private float redirectDamageModifier;
    private float attackDamageModifier;
    private float ftpDamageModifier;
    private float httpDamageModifier;
    private Port parentPort=null;                   // port it's installed on
    private float attackDamage = 0.0f;              // how much attack damage does this firewall do back
    private float price = 0.0f;                     // the price of this specific firewall
    private float pettyCashReducePct = 1.0f;
    private float pettyCashFailPct = 1.0f;
    private float dailyPayChangeFailPct = 1.0f;
    private float dailyPayChangeReducePct = 1.0f;
    private float stealFileFailPct = 1.0f;
    private float installScriptFailPct = 1.0f;
    
    private static final int maxDamageVariationPercent = 5;  // each attack is blocked by the baseDamageModifier +/- some variation which is based on this modifier
    private static final int maxFWDamageVariationPercent = 5;
    private static final double pctChanceOfNoSpecialAttribute = 0.8; 
    private static final double COMPLETE_MISS_PCT = 0.2; // damage gets through completely 5% of the time

    
    // the weighting table for variation in the per-port absorption when FW is generated
    // divided into ten 0.5 chunks, plus the mid value 0... these determine what to add to the mean FW absorption from -5 to +5
    private static int[] weights = {1,2,2,3,3,5,5,7,8,9,10,9,8,7,5,5,3,3,2,2,1}; // 100 total
    // totalweight = 1, 3, 5, 8, 11, 16, 21, 28, 36, 45, 55, 64,72,79,84,89,92,95,97,99,100

    // indexes into the Object[] associated with each firewall
    public static final int EQUIP_LEVEL = 0;
    public static final int DAMAGE_MODIFIER = 1;
    public static final int ATTACK_RANGE = 2;
	public static final int MIN_ATTACK = 3;
    public static final int CPU = 4;
    public static final int DESCRIPTION = 5;
    public static final int MAKER = 6;
    public static final int MAX_SPECIAL_VALUE = 7;
	public static final int BASE_PRICE = 8;
	public static final int PRICE_DAMAGE_MULTIPLIER = 9;
	public static final int ATTRIBUTE_PERCENTAGE_MULTIPLIER = 10;
	
    
    public static final String[] firewallCompanies = new String[] {"Moonstar Ltd.", "Salamander Ltd.", "DataMan Inc.", "SafeGuard Ltd.", "SecureLink Inc."};
    
    // each firewall has a Name (key) and values (Equip Level, Damage Modifier (1.0 - Absorption%/100), Max Attack Damage, CPU,description,maker,max special,base price)
    public final static HashMap firewalls = new HashMap();
    static {
        firewalls.put("None", new Object[]             {0,  1.0f,  0,  0,   0, "", "",                   0.0f,    0.0f,    0.0f,  0.0f});
        firewalls.put("PortProtector", new Object[]    {0,  0.85f, 2,  0,   2, "", firewallCompanies[0], 0.2f,   15.0f,    5.0f,  1.0f});
        firewalls.put("PwnPreventer", new Object[]     {10, 0.80f, 3,  0,   3, "", firewallCompanies[0], 0.2f,   30.0f,   10.0f,  3.0f});
        firewalls.put("DataShield", new Object[]       {20, 0.75f, 4,  0,   4, "", firewallCompanies[1], 0.2f,   50.0f,   45.0f, 10.0f});
        firewalls.put("PacketBuster", new Object[]     {30, 0.70f, 4,  2,   5, "", firewallCompanies[1], 0.3f,  100.0f,   50.0f, 20.0f});
        firewalls.put("TrafficTender", new Object[]    {40, 0.60f, 5,  3,   6, "", firewallCompanies[2], 0.4f,  200.0f,   75.0f, 30.0f});
        firewalls.put("DigitalFortress", new Object[]  {50, 0.50f, 5,  5,   7, "", firewallCompanies[2], 0.5f,  400.0f,  125.0f, 40.0f});
        firewalls.put("ForceField", new Object[]       {60, 0.40f, 6,  6,   8, "", firewallCompanies[3], 0.6f,  600.0f,  250.0f, 45.0f});
        firewalls.put("RubyGuardian", new Object[]     {70, 0.30f, 7,  7,  10, "", firewallCompanies[3], 0.7f, 2000.0f,  500.0f, 50.0f});
        firewalls.put("DiamondDefender", new Object[]  {80, 0.20f, 8,  8,  15, "", firewallCompanies[4], 0.8f, 5000.0f, 1000.0f, 60.0f});
        firewalls.put("ADNArmour", new Object[]        {90, 0.15f, 9,  9,  20, "", firewallCompanies[4], 0.8f, 2000.0f,   50.0f, 80.0f});
    }
    
    
    // indexes of the Object[] in the specialAttributes hash
    private static final int LONG_DESCRIPTION = 0;
    private static final int SHORT_DESCRIPTION = 1;
    private static final int BASE_ATTRIBUTE_PRICE = 2;
    
    private final static String[] specialKeys = new String[] {"emptyPettyCash()fail", "emptyPettyCash()reduce", "stealFile()fail", "changeDailyPay()fail", "changeDailyPay()reduce", "installScript()fail"};
    private final static HashMap specialAttributes = new HashMap();
    static {
        specialAttributes.put(specialKeys[0], new Object[] {"% chance that an opponent's emptyPettyCash() fails, if this FW is protecting a banking application.", "% emptyPettyCash() fails", 50.0f});
        specialAttributes.put(specialKeys[1], new Object[] {"% reduction in an opponent's emptyPettyCash(), if this FW is protecting a banking application.", "% emptyPettyCash() reduced", 50.0f});
        specialAttributes.put(specialKeys[2], new Object[] {"% chance an opponent's stealFile() fails, if this FW is protecting an FTP application.", "% stealFile() fails", 50.0f});
        specialAttributes.put(specialKeys[3], new Object[] {"% chance an opponent's changeDailyPay() fails, if this FW is protecting an HTTP application", "% changeDailyPay() fails", 50.0f});
        specialAttributes.put(specialKeys[4], new Object[] {"% reduction in the amount of daily pay that is lost when an opponent changes your daily pay, if this FW is protecting an HTTP application.", "% changeDailyPay() reduced", 50.0f});
        specialAttributes.put(specialKeys[5], new Object[] {"% chance that opponent's installScript() fails", "% installScript() fails", 50.0f});
    }
    
	//constructor.
	public NewFireWall(NetworkSwitch MyComputerHandler){
		this.MyComputerHandler = MyComputerHandler;
	}
    
    // for testing.
    public NewFireWall() {
    }
    
    // GETTERS & SETTERS for special attributes
    /**
            * Called from Port.java when determining what happens on finalize.
            */
    public float getPettyCashReduction(String opponentIP) {
        return(pettyCashReducePct);
    }
    
    /**
            * 
            * @return true if emptyPettyCash() fails
            */
    public boolean getPettyCashFail(String opponentIP) {
        if (pettyCashFailPct < 1.0f) {
            float x = new Float(Math.random()).floatValue();
            if (x < pettyCashFailPct) {
                return(true);
            }
        }
        return (false);
    }
    
    public boolean getStealFileFail(String opponentIP) {
        if (stealFileFailPct < 1.0f) {
            float x = new Float(Math.random()).floatValue();
            if (x < stealFileFailPct) {
                return(true);
            }
        }
        return (false);
    }
    
    public boolean getChangeDailyPayFail(String opponentIP) {
        if (dailyPayChangeFailPct < 1.0f) {
            float x = new Float(Math.random()).floatValue();
            if (x < dailyPayChangeFailPct) {
                return(true);
            }
        }
        return (false);
    }
    
    public float getChangeDailyPayReduction(String opponentIP) {
        return(dailyPayChangeReducePct);
    }
    
    public boolean getInstallScriptFail(String opponentIP) {
		//System.out.println("Install Script Fail PCT: "+installScriptFailPct);
        if (installScriptFailPct < 1.0f) {
            float x = new Float(Math.random()).floatValue();
			//System.out.println("Rolled a "+x);
            if (x < installScriptFailPct) {
                return(true);
            }
        }
        return (false);
    }
    
    // GETTERS & SETTERS for ABSORPTION
    
    /*
    public void setBaseDamageModifier(float damageModifer) {
        this.baseDamageModifier = damageModifier;
    }
    
    public float getBaseDamageModifier() {
        return baseDamageModifier;
    }
    */
    
    public void setBankDamageModifier(float damageModifier) {
        this.bankDamageModifier = damageModifier;
    }
    
    public float getBankDamageModifier() {
        return bankDamageModifier;
    }
    
    public void setAttackDamageModifier(float damageModifier) {
        this.attackDamageModifier = damageModifier;
    }
    
    public float getAttackDamageModifier() {
        return attackDamageModifier;
    }
    
    public void setRedirectDamageModifier(float damageModifier) {
        this.redirectDamageModifier = damageModifier;
    }
    
    public float getRedirectDamageModifier() {
        return redirectDamageModifier;
    }
    
    public void setFTPDamageModifier(float damageModifier) {
        this.ftpDamageModifier = damageModifier;
    }
    
    public float getFTPDamageModifier() {
        return ftpDamageModifier;
    }

    public void setHTTPDamageModifier(float damageModifier) {
        this.httpDamageModifier = damageModifier;
    }
    
    public float getHTTPDamageModifier() {
        return httpDamageModifier;
    }

	public void setParentPort(Port parentPort){
		this.parentPort=parentPort;
	}
    
    // OTHER GETTERS AND SETTERS
    
    public float getCPUCost() {
        return this.cpuCost;
    }
    
    public void setCPUCost(float cpuCost) {
        this.cpuCost = cpuCost;
    }
    
    
    
	/**
	Called when an attack attempts to deal damage to a port.
	*/
	public float modifyDamage(float damage, String ip, int port, boolean damageFromFireWall){
        // if the port is overheated or weakened, full damage
		if(parentPort.getOverHeated() || parentPort.getWeakened()) //If port is overheated don't apply damage modification.
			return(damage);
        Computer myComputer = parentPort.getMyComputer();
		Computer targetComputer = MyComputerHandler.getMyComputerHandler().getComputer(ip);
		int myFirewallLevel = myComputer.getLevel((float)(Float)myComputer.getStats().get("FireWall"));
		int targetFirewallLevel = targetComputer.getLevel((float)(Float)targetComputer.getStats().get("FireWall"));
		double missPct = ( targetFirewallLevel - myFirewallLevel) * COMPLETE_MISS_PCT / 100.0;
		if(missPct < 0.0){
			missPct = 0.0;
		}
        // check if the firewall malfunctions (5% of the time), if so, deal full damage
		double roll = Math.random();
        if (roll < missPct) {
            return(damage);
        }
		//float mod_damage = DamageModifiers[type]*damage;//Modify damage.
        // calculate the damage that gets through the firewall
        
        // this should really only be done when the fireall is installed / switched to another port, and a local setter should be used to store the damageModifier
        float damageModifier = 0.0f;
        int portType = parentPort.getType();
        switch (portType) {
            case Port.BANKING: damageModifier = getBankDamageModifier(); break;
            case Port.FTP: damageModifier = getFTPDamageModifier(); break;
            case Port.ATTACK: damageModifier = getAttackDamageModifier(); break;
            case Port.HTTP: damageModifier = getHTTPDamageModifier(); break;
            case Port.REDIRECT: damageModifier = getRedirectDamageModifier(); break;
        }   
        
        // only calculate variance in absorbed damage for non-None firewalls
        if (damageModifier < 1.0f) {
            // when calculating the damage done to this port, we use the damage modifier +- a dynamic percentage to add variation
            float variance = (float)Math.random() * maxDamageVariationPercent / 100.0f;
            float plusMinus = (float)Math.random();
            if (plusMinus < 0.5f) {
                damageModifier += variance;
            } else {
                damageModifier -= variance;
            }
        }
        
//System.out.println("Damage modifier (including variation): " + damageModifier);
        
        float mod_damage = damage * damageModifier;
        
        // if the port health is less than the modified damage, the port is going to be weakened, so return the remaining health of the port as the damage done
		if(parentPort.getHealth() - mod_damage < 0.0f)
			mod_damage = parentPort.getHealth();
		
        // **** Should we attack back if they are weakened?  Right now we are.  If we shouldn't, return in the previous if (but calculate the FW XP first).
        // If this firewall is an attacking firewall, calculate and deal the damage it returns
        if (this.attackDamage > 0.0f && !damageFromFireWall) {
            float fwAttackBack = this.attackDamage;
            float fwVariance = (float)Math.random() * maxFWDamageVariationPercent / 100.0f;
            float fwPlusMinus = (float)(Math.random());
            if (fwPlusMinus < 0.5f) {
                fwAttackBack += fwVariance;
            } else {
                fwAttackBack -= fwVariance;
            }
			Object O[]=new Object[]{new Float(fwAttackBack),parentPort.getIP(),new Integer(parentPort.getNumber()),new Boolean(true),null,parentPort.getWindowHandle(),-1};
			ApplicationData AD=new ApplicationData("damage",O,port,parentPort.getIP());
			AD.setSourcePort(parentPort.getNumber());
			MyComputerHandler.addData(AD,ip);
        }
		
        // calculate the XP gained:  XP = absorbed damage
        float xp = damage - mod_damage;
		if(xp>0){
			MyComputerHandler.addData(new ApplicationData("firewallxp",new Float(xp),0,""),parentPort.getIP());
		}

		return(mod_damage);
	}
    
    
    /** 
            * Used to generate a concrete firewall based on the firewall passed in.  Called when generating a drop for an NPC.
            */
    public HackerFile generateFirewall(String name) {
        
        // set the name of this firewall
        this.name = name;
        float price = 0.0f;
        
        // get the firewall from the hashmap that we are creating a concrete instance of
        Object[] firewall = (Object[])firewalls.get(name);
        
        // this should never happen -- what should we do if it does?
        if (firewall == null) {
            return null;
        }

        this.maxAttack = (int)(Integer)firewall[ATTACK_RANGE]+(int)(Integer)firewall[MIN_ATTACK];
        this.baseDamageModifier = (new Float("" + firewall[DAMAGE_MODIFIER])).floatValue();   
        this.cpuCost = (float)(new Float("" + firewall[CPU]));
        this.description = "" + firewall[DESCRIPTION];
        this.maker = "" + firewall[MAKER];
        this.maxSpecialAttributeValue = (new Float("" + firewall[MAX_SPECIAL_VALUE])).floatValue();
        
        HackerFile HF = new HackerFile(HackerFile.NEW_FIREWALL);
        HF.setName(this.name);
		HF.setDescription(this.description);
        HF.setMaker(this.maker);
        HF.setQuantity(1);
        HF.setCPUCost(this.cpuCost);
		HashMap content = new HashMap();

        // normal distrobution about the mean which is ((this FW base absorption - last FW base absorption) / 2)
        // anything >= TrafficTendor will be a normal distrobution around (base + 
        // fuck it: easier: +/- 5.  That means that sometimes at low levels, it's possible to get a FW that is worse than the one above it, but highly unlikely, and you'd just delete/sell it.
        
        // normal distrobution using Random
        //Random r = new Random();
        // nextGaussian() --> return ;the next pseudorandom, Gaussian ("normally") distributed double value with mean 0.0 and standard deviation 1.0 from this random number generator's sequence.
        //float normal = Float.floatValue(new Float(r.nextGaussian()));
        // fuck it again.  I'll just make the weights myself.

//System.out.println("Base: " + this.baseDamageModifier);
        
        price += setBankAbsorption(content);
//System.out.println("Bank Absorption Price: "+price);
        price += setFTPAbsorption(content);
//System.out.println("FTP Absorption Price: "+price);
        price += setAttackAbsorption(content);
//System.out.println("Attack Absorption Price: "+price);
        price += setHTTPAbsorption(content);
//System.out.println("HTTP Absorption Price: "+price);
        price += setRedirectAbsorption(content);
//System.out.println("Redirect Absorption Price: "+price);
        
//System.out.println("Set all the absorptions.");
        
        // set  the special attributes
        price +=generateSpecialAttributes(content);
//System.out.println("Special Attribute Price: "+price);
//System.out.println("Generated the special attributes.");
        
        // set the attack back value
        price +=generateAttackValue(content);
//System.out.println("Attack Value Price: "+price);
//System.out.println("Generated attack value");
        
        content.put("equip_level", "" + firewall[EQUIP_LEVEL]);
		price +=calculatePrice();
//System.out.println("Base Price: "+price);
		content.put("store_price",""+price);
        content.put("name",name);
		
        HF.setContent(content);
        
        return(HF);
    }
    
    /** 
            * Set the attack value of this firewall on FW generation.
            */
    private float generateAttackValue(HashMap content) {
		Object[] firewall = (Object[])firewalls.get(name);
        float maxValue = (float)((int)(Integer)firewall[ATTACK_RANGE]);
        float valuePerBucket = maxValue / weights.length;
        int i = getWeightedBucket(); // get the actual bucket to fall into in our "normal" distribution
        float actualValue = i * valuePerBucket + (int)(Integer)firewall[MIN_ATTACK]; // multiply the bucket by the value of each bucket
        int value = Math.round(actualValue); // round the number to the nearest integer
		float price = (value-(int)(Integer)firewall[MIN_ATTACK])*(float)(Float)(((Object[])firewalls.get(name))[PRICE_DAMAGE_MULTIPLIER]);
//System.out.println("Attack value: " + value);
        content.put("attack_damage", "" + value);
		return price;
    }
    
    /**
            * Create 0 - 2 special attributes for this card.  For now, the % chance of getting any special attribute is equal, and all firewall levels can get all attribute qualities.
            * The chance of getting an attribute each roll is 50%.
            * This is because it's going to be hard to get any firewall, let alone a good one,  let alone a good one at your level with a matching special attribute.
            */
    private float generateSpecialAttributes(HashMap content) {
        // the XML for special attributes will be in the content, and look like <specialAttribute><name></name><long_desc></long_desc><short_desc></short_desc><value></value></specialAttribute>
        Object[] attribute1 = generateSpecialAttribute(content, "", 1);
		
        Object[] attribute2 = generateSpecialAttribute(content, (String)attribute1[0], 2);
		
		return (float)(Float)attribute1[1]+(float)(Float)attribute2[1];
    }
    
    private Object[] generateSpecialAttribute(HashMap content, String otherAttribute, int num) {
        HashMap special = new HashMap();
        String name = "";
        String value = "";
        String longDescription = "";
        String shortDescription = "";
        float price = 0.0f;
        double val = Math.random();
        if (val > pctChanceOfNoSpecialAttribute) {
            // if the roll has succeeded, find out what attribute it is
            do {
                val = Math.random() * specialAttributes.size();
                // could replace these with "keys[]"
                if (val <= 1) {
                    name = "emptyPettyCash()fail";
                } else if (val <= 2) {
                    name = "emptyPettyCash()reduce";
                } else if (val <= 3) {
                    name = "stealFile()fail";
                } else if (val <= 4) {
                    name = "changeDailyPay()fail";
                } else if (val <= 5) {
                    name = "changeDailyPay()reduce";
                } else if (val <= 6) {
                    name = "installScript()fail";
                }
            } while (name.equals(otherAttribute));
            
            Object[] attributeValues = (Object[])specialAttributes.get(name);
			Object[] firewall = (Object[])firewalls.get(this.name);
            longDescription = "" + attributeValues[LONG_DESCRIPTION];
            shortDescription = "" + attributeValues[SHORT_DESCRIPTION];
			price = (float)(Float)attributeValues[BASE_ATTRIBUTE_PRICE];
			float percentageMultiplier  = (Float)firewall[ATTRIBUTE_PERCENTAGE_MULTIPLIER];
            // ***** maybe instead of an even distrobution, we should use a normalized distro here too?  == more "average" attribute percentages
            // find out what %
            float min = 0.2f;
            float range = this.maxSpecialAttributeValue - min;
            float percentage = (min * 100) + (int)(Math.round((float)Math.random() * range * 100.0f));
			price +=percentage*percentageMultiplier;
            value = "" + percentage / 100.0f;
        }

        // put the entries into the <special> hash
        special.put("name", name);
        special.put("long_desc", longDescription);
        special.put("short_desc", shortDescription);
        special.put("value", value);
        content.put("specialAttribute" + num, special);
        return new Object[]{name,price};
    }
    
    private float calculatePrice() {
        float price = (float)new Float("" + ((Object[])(firewalls.get(name)))[BASE_PRICE]);
        // price will be based on how good it could possibly be.  It's possible that the best firewall of one type is worth more than the worst firewall of the next type.
        return price;
    }
    
    private float setBankAbsorption(HashMap content) {
        // get the base damage modifier for this firewall (ie/ 1.0f == 0% absorbed)
        //float baseDamageModifier = firewall[DAMAGE_MODIFIER];
		float change = calculateAbsorption();
        this.bankDamageModifier = baseDamageModifier + change;
//System.out.println("bank: " + this.bankDamageModifier);
        content.put("bank_damage_modifier", "" + this.bankDamageModifier);
		return change*ABSORPTION_PRICE_MULTIPLIER;
    }
    
    private float setAttackAbsorption(HashMap content) {
        //float baseDamageModifier = firewall[DAMAGE_MODIFIER];
		float change = calculateAbsorption();
        this.attackDamageModifier = baseDamageModifier + change;
//System.out.println("attack: " + this.attackDamageModifier);
        content.put("attack_damage_modifier", "" + this.attackDamageModifier);
		return change*ABSORPTION_PRICE_MULTIPLIER;
    }
    
    private float setRedirectAbsorption(HashMap content) {
        //float baseDamageModifier = firewall[DAMAGE_MODIFIER];
		float change = calculateAbsorption();
        this.redirectDamageModifier = baseDamageModifier + change;
//System.out.println("redirect: " + this.redirectDamageModifier);
        content.put("redirect_damage_modifier", "" + this.redirectDamageModifier);
		return change*ABSORPTION_PRICE_MULTIPLIER;
    }

    private float setFTPAbsorption(HashMap content) {
        // get the base damage modifier for this firewall (ie/ 1.0f == 0% absorbed)
        //float baseDamageModifier = firewall[DAMAGE_MODIFIER];
		float change = calculateAbsorption();
        this.ftpDamageModifier = baseDamageModifier + change;
//System.out.println("ftp: " + this.ftpDamageModifier);
        content.put("ftp_damage_modifier", "" + this.ftpDamageModifier);
		return change*ABSORPTION_PRICE_MULTIPLIER;
    }
    
    private float setHTTPAbsorption(HashMap content) {
        // get the base damage modifier for this firewall (ie/ 1.0f == 0% absorbed)
        //float baseDamageModifier = firewall[DAMAGE_MODIFIER];
		float change = calculateAbsorption();
        this.httpDamageModifier = baseDamageModifier + change;
//System.out.println("http: " + this.httpDamageModifier);
        content.put("http_damage_modifier", "" + this.httpDamageModifier);
		return change*ABSORPTION_PRICE_MULTIPLIER;
    }

    
    private float calculateAbsorption() {
        // find what bucket a random number is in
        int i = getWeightedBucket();
        
//System.out.println("   bucket: " + i);

        // the max modification is +/-5 % , broken into 0.5 % chunks
        float modifier = -0.05f;
        modifier += (float)(i * 0.005);
        
        return(modifier);
    }
    
    /**
            * Generate a random number and calculate what weighted bucket it falls into (based on the weights[] at the top of the file
            */
    public int getWeightedBucket() {
        float val = (float)(new Float((Math.random() * 100)));
        int totalWeight = 0;
        int i = 0;
        for (i = 0; i < weights.length; i++) {
            totalWeight += weights[i];
            if (val <= totalWeight)
                break;
        }
        return(i);
    }
    /**
            Resets all the variables to their base values.
            */
    public void resetVariables() {
        this.name = "";
        this.equipLevel = 0;
        this.cpuCost = 0;
        this.bankDamageModifier = 1.0f;
        this.redirectDamageModifier = 1.0f;
        this.attackDamageModifier = 1.0f;
        this.ftpDamageModifier = 1.0f;
        this.httpDamageModifier = 1.0f;
        this.attackDamage = 0.0f;
        this.pettyCashReducePct = 1.0f;
        this.pettyCashFailPct = 1.0f;
        this.dailyPayChangeFailPct = 1.0f;
        this.dailyPayChangeReducePct = 1.0f;
        this.stealFileFailPct = 1.0f;
        this.installScriptFailPct = 1.0f;
    }
    
    
    /**
            * Take in a HackerFile for a firewall and set all the local variables to correspond.
            */
    public void loadHackerFile(HackerFile hf) {
        
        resetVariables();
        
        // should I load the description, etc from the HF?  Should I even save it in the HF?  I don't see any reason.
        this.name = hf.getName();

        HashMap content = hf.getContent();
        
        // <specialAttribute1><name><value><short_desc><long_desc>
        HashMap special1 = hf.getSpecial(1);
        HashMap special2 = hf.getSpecial(2);
        
        String a1 = (String)special1.get("name");
		String v1 = (String)special1.get("value");
		if(v1.equals("")){
			v1 = "0";
		}
        float value = (new Float("" + v1)).floatValue();
        if (a1.equals("emptyPettyCash()fail")) {
            this.pettyCashFailPct = value;
        } else if (a1.equals("emptyPettyCash()reduce")) {
            this.pettyCashReducePct = 1.0f-value;
        } else if (a1.equals("stealFile()fail")) {
            this.stealFileFailPct = value;
        } else if (a1.equals("changeDailyPay()fail")) {
            this.dailyPayChangeFailPct = value;
        } else if (a1.equals("changeDailyPay()reduce")) {
            this.dailyPayChangeReducePct = 1.0f-value;
        } else if (a1.equals("installScript()fail")) {
            this.installScriptFailPct = value;
        }
        
        String a2 = (String)special2.get("name");
		String v2 = (String)special2.get("value");
		if(v2.equals("")){
			v2 = "0";
		}
        value = (new Float("" + v2)).floatValue();
        if (a2.equals("emptyPettyCash()fail")) {
            this.pettyCashFailPct = value;
        } else if (a2.equals("emptyPettyCash()reduce")) {
            this.pettyCashReducePct = value;
        } else if (a2.equals("stealFile()fail")) {
            this.stealFileFailPct = value;
        } else if (a2.equals("changeDailyPay()fail")) {
            this.dailyPayChangeFailPct = value;
        } else if (a2.equals("changeDailyPay()reduce")) {
            this.dailyPayChangeReducePct = value;
        } else if (a2.equals("installScript()fail")) {
            this.installScriptFailPct = value;
        }
        
        // damage modifiers for this specific firewall
        this.bankDamageModifier = (new Float("" + content.get("bank_damage_modifier")).floatValue());
        this.redirectDamageModifier = (new Float("" + content.get("redirect_damage_modifier")).floatValue());
        this.attackDamageModifier = (new Float("" + content.get("attack_damage_modifier")).floatValue());
        this.ftpDamageModifier = (new Float("" + content.get("ftp_damage_modifier")).floatValue());
        this.httpDamageModifier = (new Float("" + content.get("http_damage_modifier")).floatValue());
        
        // attack back value for this firewall
		if(content.get("attack_damage") == null){
			content.put("attack_damage","0");
		}
        this.attackDamage = (new Float("" + content.get("attack_damage"))).floatValue();   // how much attack damage does this firewall do back
		
        this.equipLevel = (int)(float)(new Float("" + content.get("equip_level"))).floatValue();
        this.cpuCost = hf.getCPUCost();
		if(name.equals("")){
			name = (String)content.get("name");
		}
        this.hackerFile = hf;
    }
    
    /**
           Helper function for returning the name of a fire wall.
           */
    public String getName(){
        return(hackerFile.getName());
    }
    
    /**
            Helper function for getting the Hacker File attached to this port.
            */
    public HackerFile getHackerFile(){
        return(hackerFile);
    }
    
    /**
            Return the HackerFile that has been used to create this instance of the class, this represents the fire walls type.
            */
     public HashMap getType(){
        HashMap returnMe=hackerFile.getContent();
        //returnMe.put("name",name);
        return(returnMe);
     }
    
    
            /** 
            * This method is called whenever an old firewall type is found.
            */
    public HackerFile updateFirewall(int oldFirewall) {
        HackerFile HF = new HackerFile(HackerFile.NEW_FIREWALL);
        HashMap content = new HashMap();
		float price = 0.0f;
        // map the old firewall onto it's equivalent
        if (oldFirewall == NoFireWall) {
            HF.setName("None");
			content.put("name","None");
            content.put("attack_damage", "0");
        } else if (oldFirewall == BasicFireWall) {
            HF.setName("DataShield");
			content.put("name","DataShield");
            content.put("attack_damage", "0");
        } else if (oldFirewall == MediumFireWall) {
            HF.setName("DigitalFortress");
			content.put("name","DigitalFortress");
            content.put("attack_damage", "0");
        } else if (oldFirewall == GreaterFireWall) {
            HF.setName("RubyGuardian");
			content.put("name","RubyGuardian");
            content.put("attack_damage", "0");
        } else if (oldFirewall == BasicAttackingFireWall) {
            HF.setName("DataShield");
			content.put("name","DataShield");
            //content.put("attack_damage", 4.0f);
			this.name = HF.getName();
			price += generateAttackValue(content);
        } else if (oldFirewall == MediumAttackingFireWall) {
            HF.setName("DigitalFortress");
			content.put("name","DigitalFortress");
            //content.put("attack_damage", 8.0f);
			this.name = HF.getName();
			price+=generateAttackValue(content);
        } else if (oldFirewall == GreaterAttackingFireWall) {
            HF.setName("RubyGuardian");
			content.put("name","RubyGuardian");
            //content.put("attack_damage", 12.0f);
			this.name = HF.getName();
			price +=generateAttackValue(content);
        } else if (oldFirewall == UltimateAttackingFireWall) {
            HF.setName("DiamondDefender");
			content.put("name","DiamondDefender");
            //content.put("attack_damage", 16.0f);
			this.name = HF.getName();
			price += generateAttackValue(content);
        }
		
		
        Object[] firewall = (Object[])firewalls.get(HF.getName());
		content.put("equip_level", ""+firewall[EQUIP_LEVEL]);
        float baseDamageModifier = (new Float("" + firewall[DAMAGE_MODIFIER])).floatValue();   
        float cpuCost = (float)(new Float("" + firewall[CPU]));
        String description = "" + firewall[DESCRIPTION];
        String maker = "" + firewall[MAKER];
        float maxSpecialAttributeValue = (new Float("" + firewall[MAX_SPECIAL_VALUE])).floatValue();
        
		HF.setDescription(description);
        HF.setMaker(maker);
        HF.setQuantity(1);
        HF.setCPUCost(cpuCost);
        content.put("bank_damage_modifier", "" + baseDamageModifier);
        content.put("attack_damage_modifier", "" + baseDamageModifier);
        content.put("redirect_damage_modifier", "" + baseDamageModifier);
        content.put("ftp_damage_modifier", "" + baseDamageModifier);
        content.put("http_damage_modifier", "" + baseDamageModifier);
        HashMap special = new HashMap();
        for (int i = 0; i < 2; i++) {
            special.put("name", "");
            special.put("long_desc", "");
            special.put("short_desc", "");
            special.put("value", "");
            content.put("specialAttribute" + (i+1), special);
        }
		price += (Float)firewall[BASE_PRICE];
        content.put("store_price",price);
        HF.setContent(content);
        
        // the price algorithm needs to be used to calculate the actual price
        HF.setPrice(0.0f);
		return(HF);
    }
	
	public static HackerFile createNoneFirewall(){
		HackerFile HF = new HackerFile(HackerFile.NEW_FIREWALL);
        HashMap content = new HashMap();
		
        // map the old firewall onto it's equivalent
        
		//this.name = HF.getName();
		HF.setName("None");
		
        Object[] firewall = (Object[])firewalls.get(HF.getName());
		content.put("equip_level", firewall[EQUIP_LEVEL]);
        float baseDamageModifier = (new Float("" + firewall[DAMAGE_MODIFIER])).floatValue();   
        float cpuCost = (float)(new Float("" + firewall[CPU]));
        String description = "" + firewall[DESCRIPTION];
        String maker = "" + firewall[MAKER];
        float maxSpecialAttributeValue = (new Float("" + firewall[MAX_SPECIAL_VALUE])).floatValue();
        
		HF.setDescription(description);
        HF.setMaker(maker);
        HF.setQuantity(1);
        HF.setCPUCost(cpuCost);
        
        content.put("bank_damage_modifier", "" + baseDamageModifier);
        content.put("attack_damage_modifier", "" + baseDamageModifier);
        content.put("redirect_damage_modifier", "" + baseDamageModifier);
        content.put("ftp_damage_modifier", "" + baseDamageModifier);
        content.put("http_damage_modifier", "" + baseDamageModifier);
		content.put("name","None");
        HashMap special = new HashMap();
        for (int i = 0; i < 2; i++) {
            special.put("name", "");
            special.put("long_desc", "");
            special.put("short_desc", "");
            special.put("value", "");
            content.put("specialAttribute" + (i+1), special);
        }
        
        HF.setContent(content);
        
        // the price algorithm needs to be used to calculate the actual price
        HF.setPrice(0.0f);
        
        return(HF);
	
	}
    
}
