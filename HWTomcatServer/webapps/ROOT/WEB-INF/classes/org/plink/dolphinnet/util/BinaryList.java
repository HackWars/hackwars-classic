package com.plink.dolphinnet.util;
import java.util.*;
import java.io.*;

/**
<b>DolphinNet<br />
Benjamin E. Coe (2006)</b><br /><br />

An abstract basis for a binary searchable list. You must implement getKey() which is used to extract
the primary key from the list of objects you are storing and compare() which is used to compare
two objects.
*/
abstract public class BinaryList implements Serializable{
	private ArrayList data=null;

	///////////////////////////
	// Constructor.
	public BinaryList(){
		data=new ArrayList();
	}

	/** Add a new object to the list.*/
	public synchronized boolean add(Object o){
		if(data.size()==0){
			data.add(o);
			return(true);
		}

		int insert=findIndex(getKey(o));
		if(insert==-1)
			return(false);
		data.add(insert,o);
		return(true);
	}
	//Getters.
	public ArrayList getData(){
		return(data);
	}

	/** Fetch an object based on a key. */
	public Object get(Object key){
		if(data.size()==0)
			return(null);

		Object returnMe=null;
		int min=0;
		int max=data.size()-1;
		int center=0;
		while(min<=max){
			center=(max-min)/2+min;
			returnMe=(Object)data.get(center);
			Object data=getKey(returnMe);

			int c;
			if((c=compare(data,key))<0)
				min=center+1;
			else if(c>0)
				max=center-1;
			else
				break;
		}
		if(compare(key,getKey(returnMe))==0)
			return(returnMe);
		return(null);
	}

	//Find insertion point.
	private int findIndex(Object key){
		if(data.size()==0)
			return(-1);

		Object temp;
		int min=0;
		int max=data.size()-1;
		int center=0;
		while(min<=max){
			center=(max-min)/2+min;
			temp=(Object)data.get(center);
			Object data=getKey(temp);
			if(compare(data,key)<0)
				min=center+1;
			else
				max=center-1;
		}

		return(min);
	}

	/** Remove an Object from the list.*/
	public synchronized void remove(Object o){
		if(get(o)==null)
			return;
		data.remove(findIndex(o));
	}

	/** Clear the list of objects.*/
	public synchronized void clear(){
		while(data.size()>0)
			data.remove(0);
	}

	/** Get the Key used in comparison.*/
	public abstract Object getKey(Object o);

	/** Compare one object to another.*/
	public abstract int compare(Object o1,Object o2);

	public abstract String toString();
}