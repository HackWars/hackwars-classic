package com.plink.dolphinstem;
import java.io.*;
/**
<b>DolphinNet<br />
Benjamin E. Coe (2006)</b><br /><br />

Represents a Word type variable. The object used in the WordBinaryList.
*/
public class WordData implements Serializable{
	private String data;
	private double frequency;
	private boolean titleBonus=false;
	private boolean linkBonus=false;
	private boolean headerBonus=false;
	private boolean boldBonus=false;
	private boolean italicBonus=false;
	///////////////////////////
	//Constructor.
	public WordData(String data,double frequency){
		this.data=data;
		this.frequency=frequency;
	}
	///////////////////////////
	//Getters.
	public String getData(){
		return(data);
	}

	public double getFrequency(){
		return(frequency);
	}
	///////////////////////////
	//Setters.
	public void setData(String data){
		this.data=data;
	}
	
	public void setBonus(String tag){
		if(tag.equals("TITLE"))
			this.titleBonus=true;
		if(tag.equals("A"))
			this.linkBonus=true;
		if(tag.equals("B"))
			this.boldBonus=true;
		if(tag.equals("I"))
			this.italicBonus=true;
		if(tag.equals("H1")||tag.equals("H2")||tag.equals("H3")||tag.equals("H4")||tag.equals("H5")||tag.equals("H6"))
			this.headerBonus=true;
	}
	
	public boolean getBonus(String tag){
		if(tag.equals("TITLE"))
			return(titleBonus);
		if(tag.equals("A"))
			return(linkBonus);
		if(tag.equals("B"))
			return(boldBonus);
		if(tag.equals("I"))
			return(italicBonus);
		if(tag.equals("H1")||tag.equals("H2")||tag.equals("H3")||tag.equals("H4")||tag.equals("H5")||tag.equals("H6"))
			return(headerBonus);
		return(false);
	}
	
	public void setFrequency(double frequency){
		this.frequency=frequency;
	}

	public WordData clone(){
		return(new WordData(data,frequency));
	}
}