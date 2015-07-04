package com.plink.dolphinstem;
import java.io.*;
import java.util.*;
/**
<b>DolphinNet<br />
Benjamin E. Coe (2006)</b><br /><br />

Represents a single news item.
*/
public class ItemData implements Serializable{
	private String fname;
	private String zname;
	private String title;
	private String link;
	private String description;
	private String date;
	private WordBinaryList VectorData=null;
	private boolean complete=false;
	///////////////////////////
	//Constructor.
	public ItemData(){
		VectorData=new WordBinaryList();
	}
	///////////////////////////
	//Getters.
	public String getTitle(){
		return(title);
	}
	public String getLink(){
		return(link);
	}
	public String getDescription(){
		return(description);
	}
	public String getDate(){
		return(date);
	}
	public WordBinaryList getVectorData(){
		return(VectorData);
	}
	public boolean getComplete(){
		return(complete);
	}

	public String getFileName(){
		return(fname);
	}

	public String getZipName(){
		return(zname);
	}
	///////////////////////////
	//Setters.
	public void setFileName(String fname){
		this.fname=fname;
	}

	public void setTitle(String title){
		this.title=title;
	}
	public void setLink(String link){
		this.link=link;
	}
	public void setDescription(String description){
		this.description=description;
	}
	public void setDate(String date){
		this.date=date;
	}
	public void setVectorData(WordBinaryList VectorData){
		this.VectorData=VectorData;
	}
	public void setComplete(boolean complete){
		this.complete=complete;
	}
	public void setZipName(String zname){
		this.zname=zname;
	}

	/////////////////////////////
	// Methods.
	public String toString(){
		String returnMe="";
		returnMe+="Title: "+getTitle()+"\n";
		returnMe+="Link: "+getLink()+"\n";
		returnMe+="Description: "+getDescription()+"\n";
		returnMe+="Date: "+getDate()+"\n";
		returnMe+=VectorData.toString();
		return(returnMe);
	}
}