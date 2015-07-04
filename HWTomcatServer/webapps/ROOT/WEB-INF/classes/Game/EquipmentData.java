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

/**
 * By Alexander Morrison
 */
public class EquipmentData{
	public static final int BOOLEAN=0;
	public static final int FLOAT=1;
	public static final int INT=2;
	private int bonusType=0;
	private int bonusReturn=0;
	private Object BonusChart=null;
	private String BonusNames[]=null;

	//Constructor.
	public EquipmentData(int bonusType,int bonusReturn){
		this.bonusType=bonusType;
		this.bonusReturn=bonusReturn;
	}
	
	//Getters/Setters.
	public void setBonusType(int bonusType){
		this.bonusType=bonusType;
	}
	
	public int getBonusType(){
		return(bonusType);
	}
	
	public void setBonusReturn(int bonusReturn){
		this.bonusReturn=bonusReturn;
	}
	
	public Object getBonusChart(){
		return(BonusChart);
	}
	
	public void setBonusChart(Object BonusChart){
		this.BonusChart=BonusChart;
	}
	
	public void setBonusNames(String BonusNames[]){
		this.BonusNames=BonusNames;
	}
	
	public String[] getBonusNames(){
		return(BonusNames);
	}
}
