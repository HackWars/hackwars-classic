/*
 * EquipmentSheet.java
 *
 * Created on March 10, 2007, 10:40 AM
 *
 * Keeps track of hardware installed on computers.
 *
 */

package Game;
import java.util.ArrayList;
import Hackscript.Model.*;
import java.util.HashMap;
import java.util.Iterator;
import java.text.*;
/**
 * By Alexander Morrison
 */
public class EquipmentSheet{
	public static final int DEGRADE_RATE=1400000;

	//The commodity amounts required.
	public static int commodityAmounts[][]=new int[][]{{1,2,3,4,5,6,7, 8, 9,10},
													   {0,0,0,2,4,6,8,10,12,14},
													   {0,0,0,0,0,4,8,12,16,20},
													   {0,0,0,0,0,0,0, 6,12,18},
													   {0,0,0,0,0,0,0, 0, 8,16}};

	//Identifiers.
	public static final int LOW=0;
	public static final int MEDIUM=1;
	public static final int HIGH=2;
	public static final int RARE=3;
	
	public static final int AGP=0;
	public static final int PCI0=1;
	public static final int PCI1=2;
	
	public static final int HEAL_RATE=0;
	public static final int DAMAGE_BONUS=1;
	public static final int WATCH_BONUS=2;
	public static final int HD_BONUS=3;
	public static final int BANKING_BONUS=4;
	public static final int HEAL_COST_BONUS=5;
	public static final int CPU_BONUS=6;
	public static final int MINING_BONUS=7;
	public static final int FREEZE_IMMUNE=8;
	public static final int DESTROY_WATCH_IMMUNE=9;
	
	
	private int bonusCount=9;
	
	private String CardType[]=new String[]{"AGP Card","PCI Card","PCI Card"};
	private int hardwareCount=2;
	
	private String hardwareClassification[]=new String[]{"Value Priced","Consumer's","Premium","Experimental","Alien"};
	
	private HashMap HardwareDescriptions=new HashMap();
	
	//The equipped files.
	private HackerFile AGPEquipped=null;
	private HackerFile PCI0Equipped=null;
	private HackerFile PCI1Equipped=null;
	private Computer MyComputer=null;
	
	//The bonuses.
	ArrayList Bonuses=new ArrayList();

	public EquipmentSheet(Computer MyComputer){
		this.MyComputer=MyComputer;
	
		EquipmentData ED=new EquipmentData(HEAL_RATE,EquipmentData.INT);
		ED.setBonusChart(new int[]{-1,-1,-1,-1,-2,-2,-2,-2,-3,-3});
		ED.setBonusNames(new String[]{"Self-Healing","Self-Healing"});
		HardwareDescriptions.put(new Integer(HEAL_RATE),ED);
		
		ED=new EquipmentData(DAMAGE_BONUS,EquipmentData.FLOAT);
		ED.setBonusChart(new float[]{1.0f,1.0f,2.0f,2.0f,3.0f,4.0f,5.0f,6.0f,7.0f,8.0f});
		ED.setBonusNames(new String[]{"Segmenting","Segmentation"});
		HardwareDescriptions.put(new Integer(DAMAGE_BONUS),ED);
		
		ED=new EquipmentData(BANKING_BONUS,EquipmentData.FLOAT);
		ED.setBonusChart(new float[]{0.01f,0.01f,0.015f,0.02f,0.02f,0.025f,0.03f,0.03f,0.035f,0.04f});
		ED.setBonusNames(new String[]{"Reimbursing","Reimbursement"});
		HardwareDescriptions.put(new Integer(BANKING_BONUS),ED);
		
		ED=new EquipmentData(HEAL_COST_BONUS,EquipmentData.FLOAT);
		ED.setBonusChart(new float[]{-0.05f,-0.10f,-0.15f,-0.20f,-0.25f,-0.30f,-0.35f,-0.40f,-0.45f,-0.50f});
		ED.setBonusNames(new String[]{"System Monitoring","System Monitoring"});
		HardwareDescriptions.put(new Integer(HEAL_COST_BONUS),ED);
		
		ED=new EquipmentData(CPU_BONUS,EquipmentData.FLOAT);
		ED.setBonusChart(new float[]{5.0f,10.0f,15.0f,20.0f,25.0f,30.0f,35.0f,40.0f,45.0f,50.0f});
		ED.setBonusNames(new String[]{"Hyper-Threading","Hyper-Threading"});
		HardwareDescriptions.put(new Integer(CPU_BONUS),ED);
		
		ED=new EquipmentData(WATCH_BONUS,EquipmentData.INT);
		ED.setBonusChart(new int[]{1,1,2,2,3,3,4,4,5,5});
		ED.setBonusNames(new String[]{"RAM Optimizing","RAM Optimization"});
		HardwareDescriptions.put(new Integer(WATCH_BONUS),ED);
		
		ED=new EquipmentData(HD_BONUS,EquipmentData.INT);
		ED.setBonusChart(new int[]{-10,-5,5,5,10,15,15,20,20,30});
		ED.setBonusNames(new String[]{"RAID Controlling","RAID Controlling"});
		HardwareDescriptions.put(new Integer(HD_BONUS),ED);
		
		ED=new EquipmentData(MINING_BONUS,EquipmentData.FLOAT);
		ED.setBonusChart(new float[]{1.0f,1.0f,2.0f,2.0f,3.0f,4.0f,5.0f,6.0f,7.0f,8.0f});
		ED.setBonusNames(new String[]{"Redirecting","Redirection"});
		HardwareDescriptions.put(new Integer(MINING_BONUS),ED);
		
		ED=new EquipmentData(FREEZE_IMMUNE,EquipmentData.BOOLEAN);
		ED.setBonusNames(new String[]{"Non-Blocking","Non-Blocking Operations"});
		HardwareDescriptions.put(new Integer(FREEZE_IMMUNE),ED);

		ED=new EquipmentData(DESTROY_WATCH_IMMUNE,EquipmentData.BOOLEAN);
		ED.setBonusNames(new String[]{"Parity Checking","Parity Checking"});
		HardwareDescriptions.put(new Integer(DESTROY_WATCH_IMMUNE),ED);
	};
	
	/**
	Get how frequently the port should heal, this is based on a modules and a counter.
	It is checked during an attack iteration, which takes place every 2 seconds.
	*/
	public int getHealMod(){
		int HEAL_MOD=4;
		for(int i=0;i<Bonuses.size();i++){
			BonusData BD=(BonusData)Bonuses.get(i);
			HEAL_MOD+=BD.getHealMod();
		}
	
		if(HEAL_MOD<1)
			return(1);
		return(HEAL_MOD);
	}
	
	/**
	Returns the damage bonus that should be added to a hit.
	*/
	public float getDamageBonus(){
		float damageBonus=0;
		for(int i=0;i<Bonuses.size();i++){
			BonusData BD=(BonusData)Bonuses.get(i);
			damageBonus+=BD.getDamageBonus();
		}
	
		return(damageBonus);
	}
	
	/**
	Returns the damage bonus that should be added to a hit.
	*/
	public float getMiningBonus(){
		float miningBonus=0;
		for(int i=0;i<Bonuses.size();i++){
			BonusData BD=(BonusData)Bonuses.get(i);
			miningBonus+=BD.getMiningBonus();
		}
	
		return(miningBonus);
	}
	
	/**
	Get whether the computer is freeze immune.
	*/
	public boolean getFreezeImmune(){
		boolean freezeImmune=false;
		for(int i=0;i<Bonuses.size();i++){
			BonusData BD=(BonusData)Bonuses.get(i);
			if(BD.getFreezeImmune()){
				freezeImmune=true;
			}
		}
	
		return(freezeImmune);
	}
	
	/**
	Get destroy watches.
	*/
	public boolean getDestroyWatchesImmune(){
		boolean destroyWatchImmune=false;
		for(int i=0;i<Bonuses.size();i++){
			BonusData BD=(BonusData)Bonuses.get(i);
			if(BD.getDestroyWatchesImmune()){
				destroyWatchImmune=true;
			}
		}
	
		return(destroyWatchImmune);
	}
	
	/**
	Get the bonus that should be applied to banking.
	*/
	public float getBankingBonus(){
		float bankingBonus=0;
		for(int i=0;i<Bonuses.size();i++){
			BonusData BD=(BonusData)Bonuses.get(i);
			bankingBonus+=BD.getBankingBonus();
		}
	
		if(bankingBonus>0.07f)
			return(0.07f);
			
		return(bankingBonus);
	}
	
	/**
	Get the bonus for heal cost.
	*/
	public float getHealBonus(){
		float healCostBonus=1.0f;
		for(int i=0;i<Bonuses.size();i++){
			BonusData BD=(BonusData)Bonuses.get(i);
			//float bonus = BD.getHealBonus();
			//System.out.println("Heal Cost Bonus: "+bonus);
			healCostBonus+=BD.getHealBonus();
		}
		
		if(healCostBonus<=0.25)
			healCostBonus=0.25f;
		
		return(healCostBonus);
	}
	
	/**
	Return the CPU bonus currently being applied.
	*/
    public float getCPUBonus() {
        return getCPUBonus(null);
    }
    
	public float getCPUBonus(HackerFile card){
		float CPUBonus=0;
		float CPUMaxBonus=0;
		for(int i=0;i<Bonuses.size();i++){
			BonusData BD=(BonusData)Bonuses.get(i);
            if (card == null || BD.getEquipmentFile() == card) {
                CPUBonus+=BD.getCPUBonus()[1];
                CPUMaxBonus+=BD.getCPUBonus()[0];
            }
		}
		float spaceLeft=MyComputer.getMaximumCPUNoBonus()-MyComputer.getBaseCPULoad();
		
		if(!MyComputer.getAttacking())
		if(spaceLeft+CPUBonus<0){
			float required=Math.abs(spaceLeft+CPUBonus);
			if(required+CPUBonus>CPUMaxBonus){
				required=CPUMaxBonus;
			}
			CPUBonus=CPUBonus+required;
		}
	
		return(CPUBonus);
	}
	
	/**
	Return the Watch bonus currently being applied.
	*/
    public int getWatchBonus() {
        return getWatchBonus(null);
    }
    
	public int getWatchBonus(HackerFile card){
		int WatchBonus=0;
		for(int i=0;i<Bonuses.size();i++){
			BonusData BD=(BonusData)Bonuses.get(i);
            if (card == null || BD.getEquipmentFile() == card) {
                WatchBonus+=BD.getWatchBonus();
            }
		}
		int spaceLeft=MyComputer.getMaximumWatchesNoBonus()-MyComputer.getWatchHandler().getWatchCount();
		if(spaceLeft+WatchBonus<0){
			WatchBonus+=(int)Math.abs(spaceLeft+WatchBonus);
		}
	
		return(WatchBonus);
	}
	
	/**
	Return the HD bonus currently being applied.
	*/
    public int getHDBonus() {
        return getHDBonus(null);
    }
    
	public int getHDBonus(HackerFile card) {
		int HDBonus=0;
		for(int i=0;i<Bonuses.size();i++){
			BonusData BD=(BonusData)Bonuses.get(i);
            if (card == null || BD.getEquipmentFile() == card) {
                HDBonus+=BD.getHDBonus();
            } 
		}
        // checking to see if the (space remaining w/o bonus' + bonus') < 0
        // if it's not, return the true HD bonus
        // if it IS (ie/ the bonus puts the HD over capacity), return the max bonus that keeps us at full capacity
        /*
		int HDMinusBonus=MyComputer.getFileSystem().getSpaceMinusBonus();	
		if(HDMinusBonus+HDBonus<0){
			HDBonus+=(int)Math.abs(HDMinusBonus+HDBonus);
		}
                    */
		return(HDBonus);
	}
	
	/**
	Generates a single piece of hardware based on the rarity passed in.
	*/
	public HackerFile generateHardware(float rarity){
        // all attributes are available at all levels - not at the moment
        //int maxAttribute = 10;
        int maxQuality=5;
        
        
		int maxAttribute=8;
		if(rarity==HIGH)
			maxAttribute=9;
		if(rarity==RARE)
			maxAttribute=10;

        
		if(rarity==RARE)
			maxQuality=10;
		if(rarity==HIGH)
			maxQuality=8;
		if(rarity==MEDIUM)
			maxQuality=6;
            
		int attribute1=(int)(Math.random()*maxAttribute);
		int attribute2=0;
        
        // the attributes can't be the same
		do{
			attribute2=(int)(Math.random()*maxAttribute);
		}while(attribute2==attribute1);
        
		int quality1=(int)(Math.random()*maxQuality);
		int quality2=0;
		if(quality1<2){
			do{
				quality2 = (int)(Math.random()*maxQuality);
			}while(quality2<2);
		}
		else{
			quality2 = (int)(Math.random()*maxQuality);
		}
		
		if(attribute1==8||attribute1==9)
			quality1=9;
		if(attribute2==8||attribute2==9)
			quality2=9;
        
		int hardwareType=(int)(Math.random()*hardwareCount);
		String itemName="";
		EquipmentData ED=(EquipmentData)HardwareDescriptions.get(new Integer(attribute1));
		String BonusNames[]=ED.getBonusNames();
		itemName+=BonusNames[0]+" ";
		itemName+=CardType[hardwareType]+" ";
		ED=(EquipmentData)HardwareDescriptions.get(new Integer(attribute2));
		BonusNames=ED.getBonusNames();
		itemName+="of "+BonusNames[1];
		
		HackerFile HF=new HackerFile(hardwareType+18);
		HF.setName(CardType[hardwareType]+".license");
		HF.setDescription(itemName);
		HashMap Keys=new HashMap();
		Keys.put("attribute0",""+attribute1);
		Keys.put("attribute1",""+attribute2);
		Keys.put("attribute2",""+0);
		Keys.put("quality0",""+quality1);
		Keys.put("quality1",""+quality2);
		Keys.put("quality2",""+0);
		
		/*System.out.println("------------------------------------------------");
		System.out.println(itemName);
		System.out.println("Attribute 1: "+attribute1);
		System.out.println("Quality 1: "+quality1);
		System.out.println("Attribute 2: "+attribute2);
		System.out.println("Quality 2: "+quality2);*/
		

		//Our chart for durablity.
		float durability[]=new float[]{50.0f,50.0f,50.0f,50.0f,50.0f,50.0f,50.0f,50.0f,50.0f,50.0f,100.0f,100.0f,100.0f,100.0f,100.0f,150.0f,150.0f,150.0f,200.0f,200.0f,250.0f};
		int choice=(int)(Math.random()*(double)durability.length);
		Keys.put("maxquality",""+durability[choice]);
		Keys.put("currentquality",""+durability[choice]);

	
		HF.setContent(Keys);
		HF.setQuantity(1);
		
		HF.setMaker("Low");
		if(quality1+quality2>5)
			HF.setMaker("Medium");
		if(quality1+quality2>=14)
			HF.setMaker("High");
		if(quality1+quality2>=18)
			HF.setMaker("Rare");
			
		return(HF);
	}
	
    
    
	/**
	Check if a card can be unequipped.  Failure would be due to removing bonus' putting the system into an impossible state.
	*/
	public boolean removeAllowed(HackerFile card){

        //CPU BONUS
        float cpuRemaining=MyComputer.getMaximumCPULoad()-MyComputer.getBaseCPULoad();
        float cpuBonus = getCPUBonus(card);
        if(cpuRemaining - cpuBonus<0) {
            MyComputer.addMessage(MessageHandler.UNEQUIP_FAIL_CPU_RESTRICTIONS);
            return false;
        }
        
        //HD BONUS
        int spaceLeft = MyComputer.getFileSystem().getSpaceLeft(); // can be -ve now
        float hdBonus = getHDBonus(card);

        if(spaceLeft - hdBonus<0) {
            MyComputer.addMessage(MessageHandler.UNEQUIP_FAIL_HD_FULL);
            return false;
        }

        // WATCH BONUS
        int watchSpaceLeft = MyComputer.getMaximumWatches()-MyComputer.getWatchHandler().getWatchCount();
        int watchBonus = getWatchBonus(card);
        if(watchSpaceLeft - watchBonus<0) {
            MyComputer.addMessage(MessageHandler.UNEQUIP_FAIL_WATCH_RESTRICTIONS);
            return false;
        }
		
		return(true);
	}
	
    public boolean addCard(HackerFile cardToEquip, boolean doChecks, boolean isEquipped) {

        // create new BonusDatas for this card
        BonusData bd1 = null;
        BonusData bd2 = null;
        BonusData bd3 = null;
        
        // get a BonusData for each attribute on the card
        int check[]=fetchQualities(cardToEquip);
        bd1 = getBonusData(cardToEquip,check[0],check[1]);
        bd2 = getBonusData(cardToEquip,check[2],check[3]);
        //A third attribute is only possible for special unique hardware.
        if(!(check[4]==0&&check[5]==0))
            bd3 = getBonusData(cardToEquip,check[4],check[5]);

        if (doChecks) {
            //CPU BONUS
            float cpuRemaining = MyComputer.getMaximumCPULoad()-MyComputer.getBaseCPULoad();
            float cpuBonus = 0.0f;
            cpuBonus += bd1.getCPUBonus()[1];
            cpuBonus += bd2.getCPUBonus()[1];
            if (bd3 != null) {
                cpuBonus += bd3.getCPUBonus()[1];
            }
            
            if(cpuRemaining + cpuBonus < 0) {
                MyComputer.addMessage(MessageHandler.EQUIP_FAIL_CPU_RESTRICTIONS);
                return false;
            }

            //HD BONUS
            int spaceLeft = MyComputer.getFileSystem().getSpaceLeft(); // can be -ve now
            if (!isEquipped) {
                // because there's a card already equipped, we have to allow for one extra space on the HD for the switch
                spaceLeft++;
            }
            int hdBonus = 0;
            hdBonus += bd1.getHDBonus();
            hdBonus += bd2.getHDBonus();
            if (bd3 != null) {
                hdBonus += bd3.getHDBonus();
            }
            
            if(spaceLeft + hdBonus < 0) {
                MyComputer.addMessage(MessageHandler.EQUIP_FAIL_HD_FULL);
                return false;
            }
            
            // WATCH BONUS
            int watchSpaceLeft = MyComputer.getMaximumWatches()-MyComputer.getWatchHandler().getWatchCount();
            int watchBonus = 0;
            watchBonus += bd1.getWatchBonus();
            watchBonus += bd2.getWatchBonus();
            if (bd3 != null) {
                watchBonus += bd3.getWatchBonus();
            }
            
            if(watchSpaceLeft + watchBonus < 0) {
                MyComputer.addMessage(MessageHandler.EQUIP_FAIL_WATCH_RESTRICTIONS);
                return false;
            }
        }
        
        addCardToBonuses(bd1, bd2, bd3);

        return true;
    }
	
	/**
	Remove an card's attributes from this EquipmentSheet's Bonus'
	*/
	public void removeCardFromBonuses(HackerFile ParentFile){
		Iterator BonusIterator=Bonuses.iterator();
		while(BonusIterator.hasNext()){
			BonusData BD=(BonusData)BonusIterator.next();
			if(BD.getEquipmentFile() == ParentFile){
				BonusIterator.remove();
			}
		}
	}
	
    /**
            * Add the attributes of a card to the Arraylist of Bonuses for thie EquipmentSheet
            */
    public void addCardToBonuses(BonusData bd1, BonusData bd2, BonusData bd3) {
        Bonuses.add(bd1);
        Bonuses.add(bd2);
        if (bd3 != null) {
            Bonuses.add(bd3);
        }
    }
    
    // get a bonus data for each attribute on the given card
    private BonusData getBonusData(HackerFile ParentFile, int attribute, int quality) {
        // You can add attributes like this to the BonusData of an EquipmentSheet because when it's read through the getters, the degredation is applied.
        EquipmentData ED = (EquipmentData)HardwareDescriptions.get(new Integer(attribute));
        BonusData BD = new BonusData(ParentFile);
		
		if(attribute==HEAL_RATE){
			int BonusChart[]=(int[])ED.getBonusChart();
			int bonus=(int)(BonusChart[quality]);
			BD.setHealMod(bonus);
		}else if(attribute==DAMAGE_BONUS){
			float BonusChart[]=(float[])ED.getBonusChart();
			float bonus=(float)(BonusChart[quality]);
			BD.setDamageBonus(bonus);
		}else if(attribute==MINING_BONUS){
			float BonusChart[]=(float[])ED.getBonusChart();
			float bonus=(float)(BonusChart[quality]);
			BD.setMiningBonus(bonus);
		}else if(attribute==BANKING_BONUS){
			float BonusChart[]=(float[])ED.getBonusChart();
			float bonus=(float)(BonusChart[quality]);
			BD.setBankingBonus(bonus);
		}else if(attribute==HEAL_COST_BONUS){
			float BonusChart[]=(float[])ED.getBonusChart();
			float bonus=(float)(BonusChart[quality]);
			BD.setHealBonus(bonus);
		}else if(attribute==CPU_BONUS){
			float BonusChart[]=(float[])ED.getBonusChart();
			float bonus=(float)(BonusChart[quality]);
			BD.setCPUBonus(bonus);
		}else if(attribute==WATCH_BONUS){
			int BonusChart[]=(int[])ED.getBonusChart();
			int bonus=(int)(BonusChart[quality]);
			BD.setWatchBonus(bonus);
		}else if(attribute==HD_BONUS){
			int BonusChart[]=(int[])ED.getBonusChart();
			int bonus=(int)(BonusChart[quality]);
			BD.setHDBonus(bonus);
		}else if(attribute==FREEZE_IMMUNE){
			BD.setFreezeImmune(true);
		}else if(attribute==DESTROY_WATCH_IMMUNE){
			BD.setDestroyWatchesImmune(true);
		}
        
        return BD;
    }
    
    
	/**
	Fetch an array of integers representing a attribute quality pair from the hacker file
	provided.
	*/
	public int[] fetchQualities(HackerFile HF){
		int returnMe[]=new int[6];
		HashMap Content=HF.getContent();
		returnMe[0]=new Integer((String)Content.get("attribute0"));
		returnMe[1]=new Integer((String)Content.get("quality0"));
		returnMe[2]=new Integer((String)Content.get("attribute1"));
		returnMe[3]=new Integer((String)Content.get("quality1"));
		try{
			returnMe[4]=new Integer((String)Content.get("attribute2"));
			returnMe[5]=new Integer((String)Content.get("quality2"));
		}catch(Exception e){
			returnMe[4]=0;
			returnMe[5]=0;
		}
		return(returnMe);
	}
	
	
    private HackerFile getHackerFileFromName(int position, String name) {
        HackerFile EquipFile=null;
		
		if(name!=null){
			EquipFile=MyComputer.getFileSystem().getFile("",name);
			
			if(position==AGP&&EquipFile.getType()!=HackerFile.AGP){
				MyComputer.addMessage(MessageHandler.EQUIP_FAIL_WRONG_TYPE);
				return null;
			}
			if((position==PCI0||position==PCI1)&&EquipFile.getType()!=HackerFile.PCI){
				MyComputer.addMessage(MessageHandler.EQUIP_FAIL_WRONG_TYPE);
				return null;
			}
			
			//MyComputer.getFileSystem().deleteFile("",name);
		}
        
        return EquipFile;

    }
    
    
    /**
            * Remove the currently equipped card.
            */
    private boolean unequipCard(HackerFile equippedCard, HackerFile cardToEquip, int position) {

        // check if we can remove the card
        if (!removeAllowed(equippedCard)) {
            // what the hell are we doing here? did we somehow remove the card we're going to equip from the HD before we'd unequipped the other one?  LSD?
            /*
            if(cardToEquip!=null){
                Object Parameter[]=new Object[]{"",cardToEquip};
                MyComputer.getComputerHandler().addData(new ApplicationData("savefile",Parameter,0,MyComputer.getIP()),MyComputer.getIP());
            }
            */
            return false;
        }
        
        // remove the card and save it to the HD
        removeCardFromBonuses(equippedCard);
        Object Parameter[]=new Object[]{"",equippedCard};
        MyComputer.getComputerHandler().addData(new ApplicationData("savefile",Parameter,0,MyComputer.getIP()),MyComputer.getIP());
        
        // remove it from our Computer
        if (position == AGP) {
            AGPEquipped=null;
        } else if (position == PCI0) {
            PCI0Equipped=null;
        } else if (position == PCI1) {
            PCI1Equipped=null;
        }
        
        return true;
    }
    
    /**
            * Equip the cardToEquip.
            */
    private boolean equipCard(HackerFile cardToEquip, int position, boolean doChecks, boolean cardEquipped) {
    
        // check if we can add the card
        if (!addCard(cardToEquip, doChecks,cardEquipped)) {
            // what the hell are we doing here? did we somehow remove the card we're going to equip from the HD before we'd unequipped the other one?  LSD?
            /*
            if(cardToEquip!=null) {
                Object Parameter[]=new Object[]{"",cardToEquip};
                MyComputer.getComputerHandler().addData(new ApplicationData("savefile",Parameter,0,MyComputer.getIP()),MyComputer.getIP());
            }
            */
            return false;
        }
        
        MyComputer.getFileSystem().deleteFile("", cardToEquip.getName());
        
        if(position==AGP)
            AGPEquipped=cardToEquip;
        if(position==PCI0)
            PCI0Equipped=cardToEquip;
        if(position==PCI1)
            PCI1Equipped=cardToEquip;
            
        return true;
    }
    
    /**
            * The logic for equipping / unequipping cards.
            *if EquipFile is null, then a card is just being unequipped
            * if EquipFile is not null, then a card is attempting to be equipped
            */
    private void equipLogic(int position, HackerFile cardToEquip, boolean sendMessage) {
        // what we really should do if it were a perfect world:
        //  1) see if we can unequip the card, and what the results would be
        // 2) see if we can equip the new card, given the results of unequipping the other one
        // 3) if it would leave us in a legal state, then unequip the first card and equip the second

        // 1) removeAllowed(cardToUnequip)
        //         * make sure it's possible
        //         * remove the card from the Bonuses of this EquipmentSheet
        // 2) addAllowed(cardToEquip)
        //        * make sure it's possible
        // 3) if (!addAllowed(...))
        //           * add in the BonusData from cardToUnequip again
        //      else
        //           * unequip(cardToUnequip), equip(cardToEquip)
        
        // Attempt to uninstall any card installed on the slot in question
        
        /*
        // simple unequip
        if (cardInstalled && cardToEquip == null) {
        }
        // simple equip
        else if (!cardInstalled & cardToEquip != null) {
        }
        // unequip one, equip another
        else if (cardInstalled & cardToEquip != null) {
        }
        else {
            // nothing to do!
        }
        // functions required
        // removeAllowed(), addAllowed(), unequipCard(), equipCard(), 
        */
        
        boolean unequipped = true;
        HackerFile equippedCard = null;
        boolean isEquipped = false;
		if(position==AGP && AGPEquipped!=null){
            equippedCard = AGPEquipped;
        }else if(position==PCI0&&PCI0Equipped!=null){
            equippedCard = PCI0Equipped;
		}else if(position==PCI1&&PCI1Equipped!=null){
            equippedCard = PCI1Equipped;
		}
        if (equippedCard != null) {
            isEquipped = true;
            unequipped = unequipCard(equippedCard, cardToEquip, position);
        }
        
        // if we couldn't unequip the card, quit
        if (!unequipped) {
            return;
        }
        
        //MyComputer.sendPacket();
        
        
        
        // Attempt to equip the new card (if there is one)
		if(cardToEquip != null){
            if (equipCard(cardToEquip, position, true, isEquipped)) {
                // only send a message when you're not loading the account
                if (sendMessage && equippedCard != null) {
                    MyComputer.addMessage(MessageHandler.EQUIP_SUCCESS,new Object[]{cardToEquip.getName()});
                }
            } else {
                // if the card couldn't be equipped and it was a replacement, re-install the old one
                if (equippedCard != null) {
                    equipCard(equippedCard, position, false, true);
                }
            }
		}
    }
    
	/**
	Equip a piece of equipment (when loading an account -- don't send a message)
	*/
	public void equip(int position,HackerFile EquipFile){
        equipLogic(position, EquipFile, false);
	}
	
	/**
	Equip a piece of equipment.
	*/
	public void equip(int position,String name){
		HackerFile cardToEquip = getHackerFileFromName(position, name);
        equipLogic(position, cardToEquip, true);
	}
	
	/**
	Return the equipment that is currently equipped.
	*/
	public Object[] getEquipment(){
		Object returnMe[]=new Object[3];
		returnMe[0]=AGPEquipped;
		returnMe[1]=PCI0Equipped;
		returnMe[2]=PCI1Equipped;
		return(returnMe);
	}
	
	/**
	Degrades the currently equipped hardware by one point.
	*/
	public void degradeEquipment(){	
		if(MyComputer.getType()!=MyComputer.NPC){
			if(AGPEquipped!=null)
				degradeEquipment(AGPEquipped);
			if(PCI0Equipped!=null)
				degradeEquipment(PCI0Equipped);
			if(PCI1Equipped!=null)
				degradeEquipment(PCI1Equipped);
		}
	}
	
	/**
	The various repair entry points.
	*/
	public void repair(int EquipmentID){
		HackerFile Equipment=null;
		if(EquipmentID==0)
			Equipment=AGPEquipped;
		if(EquipmentID==1)
			Equipment=PCI0Equipped;
		if(EquipmentID==2)
			Equipment=PCI1Equipped;
		
		if(Equipment==null)
			return;
		
		repair(Equipment);
	}
	
	/**
	Fix a piece of equipment.
	*/
	public void repair(HackerFile Equipment){
		int commodityUsed[]=new int[]{0,0,0,0,0};
			
        // get the qualities of this card
		int quality=new Integer((String)Equipment.getContent().get("quality0"));
		int quality1=(int)(new Integer((String)Equipment.getContent().get("quality1")));
				
        // get how much of each commodity is required to repair this card (based on both attributes)
		commodityUsed[4]=commodityAmounts[4][quality]+commodityAmounts[4][quality1];
		commodityUsed[3]=commodityAmounts[3][quality]+commodityAmounts[3][quality1];
		commodityUsed[2]=commodityAmounts[2][quality]+commodityAmounts[2][quality1];
		commodityUsed[1]=commodityAmounts[1][quality]+commodityAmounts[1][quality1];
		commodityUsed[0]=commodityAmounts[0][quality]+commodityAmounts[0][quality1];
		
		boolean commodityFailed=false;
		
        // make sure they have enough of the commodities required to repair the card
		for(int i=0;i<=4;i++){
			int check=(int)MyComputer.getCommodity(i);
			if(check<commodityUsed[i]){
				commodityFailed=true;
			}
		}
        
        int repairLevel=(int)MyComputer.getRepairLevel();
		if(!commodityFailed){
			boolean repairFailed=false;
			String repairFailString="";
			int repairFailedCommodity=0;
            
            // check that they have the repair level required to fix all the commodities on the card
			for(int i=4;i>=0;i--){
				if(commodityUsed[i]>0)
					if(repairLevel<MyComputer.requiredRepairLevel[i]){
						repairFailed=true;
						repairFailString=MyComputer.commodityString[i];
						repairFailedCommodity=i;
						break;
					}
			}
			
			if(!repairFailed){
				float xp=0.0f;
                
                // reduce the commodities used, and increase the repair experience
				for(int i=0;i<=4;i++){
					int check=(int)MyComputer.getCommodity(i);
					MyComputer.setCommodityAmount(i,(float)(check-commodityUsed[i]));
					xp+=commodityUsed[i]*MyComputer.repairXP[i];
				}
				
				MyComputer.getComputerHandler().addData(new ApplicationData("repairxp",new Float(xp),0,MyComputer.getIP()),MyComputer.getIP());
				
                // reset the current quality to the max quality
				HashMap Content=Equipment.getContent();
				float max=new Float((String)Content.get("maxquality"));
				Content.put("currentquality",""+max);
				
				MyComputer.setRepaired(true);
                String message = "";
                for(int i=0;i<4;i++){
                    if(commodityUsed[i]>0){
                        message+="["+((int)commodityUsed[i])+"x"+MyComputer.commodityString[i]+"] ";
                    }
                }
				MyComputer.addMessage(MessageHandler.REPAIR_SUCCESS,new Object[]{Equipment.getName(),message});
			}else{
				MyComputer.addMessage(MessageHandler.REPAIR_FAIL_LEVEL,new Object[]{repairFailString,MyComputer.requiredRepairLevel[repairFailedCommodity]});
			}
		}else{	
			String message="";//"You don't have enough commodities to repair " + Equipment.getName() + ".  Required: ";
			for(int i=0;i<4;i++){
				if(commodityUsed[i]>0){
					message+="["+((int)commodityUsed[i])+"x"+MyComputer.commodityString[i]+"] ";
				}
			}
		
			MyComputer.addMessage(MessageHandler.REPAIR_FAIL_NOT_ENOUGH_COMMODITIES,new Object[]{Equipment.getName(),message});
		}


		//For testing print out some data about the card.
		//System.out.println("Heal Mod: "+getHealMod());
		//System.out.println("Heal Cost Bonus: "+getHealBonus());
	}
	
	public void degradeEquipment(HackerFile HF){
		HashMap Content=HF.getContent();
		if(Content.get("maxquality")==null||Content.get("maxquality").equals("")||Content.get("maxquality").equals("null")){
			Content.put("maxquality",""+50.0);
			Content.put("currentquality",""+50.0);
			Content.put("lastdegrade",""+MyComputer.getCurrentTime());
		}else{
			try{
				float maxQuality = Float.parseFloat((String)Content.get("maxquality"));
				float currentQuality=new Float((String)Content.get("currentquality"));
				long lastDegrade=new Long((String)Content.get("lastdegrade"));
				if(MyComputer.getCurrentTime()-lastDegrade>=DEGRADE_RATE){
					Content.put("lastdegrade",""+MyComputer.getCurrentTime());
					
					currentQuality--;
					//System.out.println("currentQuality"+currentQuality);
					if(currentQuality>=0)
						Content.put("currentquality",""+currentQuality);
				}
			}catch(Exception e){
				Content.put("maxquality",""+50.0);
				Content.put("currentquality",""+50.0);
				Content.put("lastdegrade",""+MyComputer.getCurrentTime());
			}
		}
	}
	
	/**
	Given a HackerFile of a card. Describe's the card's various attributes.
	*/
	public void describeCard(HackerFile TheCard){
		BonusData BonusCheck=new BonusData(TheCard);

		int attribute0=new Integer((String)TheCard.getContent().get("attribute0"));
		int quality0=new Integer((String)TheCard.getContent().get("quality0"));
		String a1=describeAttribute(attribute0,quality0,BonusCheck);
		
		int attribute1=new Integer((String)TheCard.getContent().get("attribute1"));
		int quality1=new Integer((String)TheCard.getContent().get("quality1"));
		String a2=describeAttribute(attribute1,quality1,BonusCheck);

		String a3="";
		try{
			int attribute2=new Integer((String)TheCard.getContent().get("attribute2"));
			int quality2=new Integer((String)TheCard.getContent().get("quality2"));
			if(!a3.equals("0"))
				a3=describeAttribute(attribute2,quality2,BonusCheck);
		}catch(Exception e){}
	
		String bonusdata=a1+"|"+a2;
		if(!a3.equals(""))
			bonusdata+="|"+a3;
			
		TheCard.getContent().put("bonusdata",bonusdata);
	}
	
	public String describeAttribute(int attribute,int quality,BonusData BD){
	
		String bonusString="";
		EquipmentData ED=(EquipmentData)HardwareDescriptions.get(new Integer(attribute));
		NumberFormat decimalFormat = new DecimalFormat("0.00%");
		NumberFormat nf = new DecimalFormat("0.0");
		if(attribute==HEAL_RATE){
			int BonusChart[]=(int[])ED.getBonusChart();
			int bonus=(int)(BonusChart[quality]);
			BD.setHealBonus(bonus);
			
			if(BD.getHealBonus()>0){
				double value=BD.getHealBonus();
				value=value/4.0;
				int intvalue=(int)(value*100.0);
				float max = (float)bonus/4.0f;
				String maxS = decimalFormat.format(max);
				bonusString=""+intvalue+"%/"+maxS+" Slower Heal Rate";
			}else{
				double value=(-1.0)*BD.getHealBonus();
				value=value/4.0;
				int intvalue=(int)(value*100.0);
				float max = (float)bonus/4.0f;
				String maxS = decimalFormat.format(-max);
				bonusString=""+intvalue+"%/"+maxS+" Faster Heal Rate";
			}
		
		}else if(attribute==DAMAGE_BONUS){
			float BonusChart[]=(float[])ED.getBonusChart();
			float bonus=(float)(BonusChart[quality]);
			BD.setDamageBonus(bonus);
			
			if(BD.getDamageBonus()<0)
				bonusString=""+nf.format(BD.getDamageBonus())+"/"+nf.format(bonus)+" to Attack Damage";
			else
				bonusString="+"+nf.format(BD.getDamageBonus())+"/"+nf.format(bonus)+" to Attack Damage";
			
		}else if(attribute==MINING_BONUS){
			float BonusChart[]=(float[])ED.getBonusChart();
			float bonus=(float)(BonusChart[quality]);
			BD.setMiningBonus(bonus);
		
			if(BD.getMiningBonus()<0)
				bonusString=""+nf.format(BD.getMiningBonus())+"/"+nf.format(bonus)+" to Redirecting Damage";
			else
				bonusString="+"+nf.format(BD.getMiningBonus())+"/"+nf.format(bonus)+" to Redirecting Damage";
			
		}else if(attribute==BANKING_BONUS){
			float BonusChart[]=(float[])ED.getBonusChart();
			float bonus=(float)(BonusChart[quality]);
			BD.setBankingBonus(bonus);
			
			if(BD.getBankingBonus()>0){
				double value=BD.getBankingBonus();
				int intvalue=(int)(value*1000.0);
				
				bonusString=""+((double)intvalue/10.0)+"%/"+decimalFormat.format(bonus)+" Lower Banking Costs";
			}else{
				double value=(-1.0)*BD.getBankingBonus();
				int intvalue=(int)(value*1000.0);
				bonusString=""+((double)intvalue/10.0)+"%/"+decimalFormat.format(-bonus)+" Higher Banking Costs";
			}
			
		}else if(attribute==HEAL_COST_BONUS){
			float BonusChart[]=(float[])ED.getBonusChart();
			float bonus=(float)(BonusChart[quality]);
			BD.setHealBonus(bonus);
			
			if(BD.getHealBonus()>0){
				double value=BD.getHealBonus();
				int intvalue=(int)(value*100.0);
				bonusString=""+intvalue+"%/"+decimalFormat.format(bonus)+" Higher Healing Costs";
			}else{
				double value=(-1.0)*BD.getHealBonus();
				int intvalue=(int)(value*100.0);
				bonusString=""+intvalue+"%/"+decimalFormat.format(-bonus)+" Lower Healing Costs";
			}
									
		}else if(attribute==CPU_BONUS){
			float BonusChart[]=(float[])ED.getBonusChart();
			float bonus=(float)(BonusChart[quality]);
			BD.setCPUBonus(bonus);
			
			if(BD.getCPUBonus()[1]>=0){
				bonusString="+"+nf.format(BD.getCPUBonus()[1])+"/"+nf.format(bonus)+" CPU Points";
			}else{
				bonusString=""+nf.format(BD.getCPUBonus()[1])+"/"+nf.format(bonus)+" CPU Points";
			}
		}else if(attribute==WATCH_BONUS){
			int BonusChart[]=(int[])ED.getBonusChart();
			int bonus=(int)(BonusChart[quality]);
			BD.setWatchBonus(bonus);
			
			if(BD.getWatchBonus()>=0){
				bonusString="+"+BD.getWatchBonus()+"/"+bonus+" Watch";
			}else{
				bonusString=""+BD.getWatchBonus()+"/"+bonus+" Watch";
			}
		}else if(attribute==HD_BONUS){
			int BonusChart[]=(int[])ED.getBonusChart();
			int bonus=(int)(BonusChart[quality]);
			BD.setHDBonus(bonus);
			
			if(BD.getHDBonus()>=0){
				bonusString="+"+BD.getHDBonus()+"/"+bonus+" HD Space";
			}else{
				bonusString=""+BD.getHDBonus()+"/"+bonus+" HD Space";
			}
		}else if(attribute==FREEZE_IMMUNE){
			double value=BD.calculateDegradation();
			int intvalue=(int)(value*100.0);
			bonusString=""+intvalue+"% Freeze Immune";
		}else if(attribute==DESTROY_WATCH_IMMUNE){
			double value=BD.calculateDegradation();
			int intvalue=(int)(value*100.0);
			bonusString=""+intvalue+"% Destroy Watch Immune";
		}
		
		return(bonusString);
	}
	
	/**
	Return an xml representation of the data.
	*/
	public String outputXML(){
		String returnMe="<equipment>\n";
		
		if(AGPEquipped!=null)
			returnMe+=AGPEquipped.outputXML();
		returnMe+="</equipment>\n";

		returnMe+="<equipment>\n";
		if(PCI0Equipped!=null)
			returnMe+=PCI0Equipped.outputXML();
		returnMe+="</equipment>\n";

		returnMe+="<equipment>\n";
		if(PCI1Equipped!=null)
			returnMe+=PCI1Equipped.outputXML();
		returnMe+="</equipment>\n";
		
		return(returnMe);
	}
	
	
}
