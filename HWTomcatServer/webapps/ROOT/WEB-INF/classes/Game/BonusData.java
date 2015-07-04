/*
BonusData.java

Description: This file keeps track of the bonus offered to a specific skill by a specific piece of hardware.

 */

package Game;
import java.util.ArrayList;
import Hackscript.Model.*;
import java.util.HashMap;

public class BonusData{
	private HackerFile EquipmentFile=null;//The underlying equipment file that provides this bonus.
	
	/**
	The current bonuses.
	*/
	private int HEAL_MOD=0;//Determines how often healing should take place.
	private float damageBonus=0;//Should extra damage be added onto an attack.
	private float miningBonus=0;//Should extra damage be added onto an attack.
	private boolean freezeImmune=false;//Is this player currently immune to freeze.
	private int freezeImmuneCount=0;//How many items are causing freeze immunity?
	private boolean destroyWatchImmune=false;//Is this player immune to destroy watch.
	private int destroyWatchImmuneCount=0;//How many items are causing destroy watch immunity.
	private float bankingBonus=0.0f;//Percentage bonus for banking transactions.
	private float healCostBonus=0.0f;//The percentage decrease in the price of healing... default no decrease.
	private float cpuBonus=0.0f;//How much lower is the cpu cost on average.
	private int watchBonus=0;//How many extra watches are allowed.
	private int hdBonus=0;//How much extra hard-drive space does the player have.
	
	/**
	Constructor.
	*/
	public BonusData(HackerFile EquipmentFile){
		this.EquipmentFile=EquipmentFile;
	}
	
	/**
	Return the equipment file associated with this bonus.
	*/
	public HackerFile getEquipmentFile(){
		return(EquipmentFile);
	}
	
	/**
	Calculates the current degree of degradation in equipment.
	*/
	public float calculateDegradation(){
		if(EquipmentFile.getContent().get("maxquality")==null||((String)EquipmentFile.getContent().get("maxquality")).equals(""))
			return(1.0f);
	
		float max=50.0f;
		float current=50.0f;
		try{
			max=new Float((String)EquipmentFile.getContent().get("maxquality"));
			current=new Float((String)EquipmentFile.getContent().get("currentquality"));
		}catch(Exception e){}
		return(current/max);
	}
	
	/**
	Get how frequently the port should heal, this is based on a modules and a counter.
	It is checked during an attack iteration, which takes place every 2 seconds.
	*/
	public int getHealMod(){
		if(HEAL_MOD<0){
			int mult=(int)((float)HEAL_MOD*(1.0f-calculateDegradation()));
			return(mult);
		}else{
			int mult=(int)((float)HEAL_MOD*(1.0f+(1.0f-calculateDegradation())));
			return(mult);
		}
	}
	
	public void setHealMod(int HEAL_MOD){
		this.HEAL_MOD=HEAL_MOD;
	}
	
	/**
	Returns the damage bonus that should be added to a hit.
	*/
	public float getDamageBonus(){
		if(damageBonus>0.0)
			return(damageBonus*calculateDegradation());
		else{
			return(damageBonus*(1.0f+(1.0f-calculateDegradation())));
		}
	}
	
	public void setDamageBonus(float damageBonus){
		this.damageBonus=damageBonus;
	}
	
	/**
	Returns the damage bonus that should be added to a hit.
	*/
	public float getMiningBonus(){
		if(miningBonus>0.0)
			return(miningBonus*calculateDegradation());
		else{
			return(miningBonus*(1.0f+(1.0f-calculateDegradation())));
		}
	}
	
	public void setMiningBonus(float miningBonus){
		this.miningBonus=miningBonus;
	}
	
	/**
	Get whether the computer is freeze immune.
	*/
	public boolean getFreezeImmune(){
		if(freezeImmune){
			float roll=(float)Math.random();
			if(roll<=calculateDegradation())
				return(true);
			else
				return(false);
		}else
			return(false);
	}
		
	public void setFreezeImmune(boolean freezeImmune){
		this.freezeImmune=freezeImmune;
	}
	
	/**
	Get destroy watches.
	*/
	public boolean getDestroyWatchesImmune(){
		if(destroyWatchImmune){
			float roll=(float)Math.random();
			if(roll<=calculateDegradation())
				return(true);
			else
				return(false);
		}else
			return(false);
	}
	
	public void setDestroyWatchesImmune(boolean destroyWatchImmune){
		this.destroyWatchImmune=destroyWatchImmune;
	}
	
	/**
	Get the bonus that should be applied to banking.
	*/
	public float getBankingBonus(){
		if(bankingBonus>0.0)
			return(bankingBonus*calculateDegradation());
		else{
			return(bankingBonus*(1.0f+(1.0f-calculateDegradation())));
		}
	}
	
	public void setBankingBonus(float bankingBonus){
		this.bankingBonus=bankingBonus;
	}
	
	/**
	Get the bonus for heal cost.
	*/
	public float getHealBonus(){
		if(healCostBonus<0.0)
			return(healCostBonus*calculateDegradation());
		else{
			return(healCostBonus*(1.0f+(1.0f-calculateDegradation())));
		}
	}
	
	public void setHealBonus(float healCostBonus){
		this.healCostBonus=healCostBonus;
	}
	
	/**
	Return the CPU bonus currently being applied.
	*/
	public float[] getCPUBonus(){
		if(cpuBonus>0.0)
			return(new float[]{cpuBonus,(cpuBonus*calculateDegradation())});
		else{
			return(new float[]{cpuBonus,(cpuBonus*(1.0f+(1.0f-calculateDegradation())))});
		}	
	}
	
	public void setCPUBonus(float cpuBonus){
		this.cpuBonus=cpuBonus;
	}
	
	/**
	Return the Watch bonus currently being applied.
	*/
	public int getWatchBonus(){
		if(watchBonus>0.0)
			return((int)(watchBonus*calculateDegradation()));
		else{
			return((int)(watchBonus*(1.0f+(1.0f-calculateDegradation()))));
		}
	}
	
	public void setWatchBonus(int watchBonus){
		this.watchBonus=watchBonus;
	}
	
	/**
	Return the HD bonus currently being applied.
	*/
	public int getHDBonus(){
		if(hdBonus>0.0)
			return((int)(hdBonus*calculateDegradation()));
		else{
			return((int)(hdBonus*(1.0f+(1.0f-calculateDegradation()))));
		}
	}
	
	public void setHDBonus(int hdBonus){
		this.hdBonus=hdBonus;
	}
}
