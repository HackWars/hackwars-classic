package com.plink.dolphinstem;
import com.plink.dolphinstem.util.*;
import java.util.*;
import java.io.*;
import org.w3c.dom.Node;
/**
<b>
DolphinNet (2006)<br />
Benjamin E. Coe<br /><br />
</b>
Loads an RSS news source.
*/
public class TextSource  extends PrimeSource implements Serializable{
	////////////////////////
	// Constructor.
	public TextSource(String source,ArrayList Data){
		super(source,Data);
	}

	/** Fetch the news items in the form of an ArrayList.*/
	public ArrayList fetchItems(String url){
		ArrayList Data=getData();
		ArrayList returnMe=new ArrayList();
		try{
			for(int i=0;i<Data.size();i++){
				ItemData item=new ItemData();
				item.setDescription((String)Data.get(i));
				item.setTitle(" ");
				returnMe.add(item);
			}
			
		}catch(Exception e){
			e.printStackTrace();
			returnMe=null;
		}
		return(returnMe);
	}
}