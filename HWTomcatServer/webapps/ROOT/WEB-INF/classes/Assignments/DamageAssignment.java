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

public class DamageAssignment extends Assignment implements Serializable{

	private Object Damage[]=null;
	private Object HealthUpdates[]=null;
	private float attackXP=0.0f;
	private float merchantXP=0.0f;
	private float fireWallXP=0.0f;
	private float watchXP=0.0f;
	private float scanningXP=0.0f;
	private float redirectXP=0.0f;
	private float HTTPXP=0.0f;
	private float repairXP=0.0f;
	private float cpuCost=0.0f;
	
	/////////////////////////
	// Constructor.
	public DamageAssignment(int id){
		super(id);
	}
	
	public void addDamage(ArrayList Damage){
		this.Damage=new Object[Damage.size()];
		for(int i=0;i<Damage.size();i++){
			Object[] data=(Object[])Damage.get(i);
			this.Damage[i]=data;
		}
	}
	
	public Object[] getDamage(){
		return(Damage);
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
	
	public void setAttackXP(float attackXP){
		this.attackXP=attackXP;
	}
	public float getAttackXP(){
		return(attackXP);
	}
	public void setMerchantXP(float merchantXP){
		this.merchantXP=merchantXP;
	}
	public float getMerchantXP(){
		return(merchantXP);
	}
	public void setFireWallXP(float fireWallXP){
		this.fireWallXP=fireWallXP;
	}
	public float getFireWallXP(){
		return(fireWallXP);
	}
	public void setWatchXP(float watchXP){
		this.watchXP=watchXP;
	}
	public float getWatchXP(){
		return(watchXP);
	}
	public void setScanningXP(float scanningXP){
		this.scanningXP=scanningXP;
	}
	public float getScanningXP(){
		return(scanningXP);
	}
	public void setHTTPXP(float HTTPXP){
		this.HTTPXP=HTTPXP;
	}
	public float getHTTPXP(){
		return(HTTPXP);
	}
	
	public void setRedirectXP(float redirectXP){
		this.redirectXP=redirectXP;
	}
	
	public float getRedirectXP(){
		return(redirectXP);
	}
	
	public void setRepairXP(float repairXP){
		this.repairXP=repairXP;
	}
	
	public float getRepairXP(){
		return(repairXP);
	}

	/** Run the assignments implemented task. */
	public Object execute(DataHandler DH){
		DH.addData(this);
		finish();
		return(null);
	}

	public void setCPUCost(float cpuCost){
		this.cpuCost=cpuCost;
	}
	
	public float getCPUCost(){
		return(cpuCost);
	}
}
