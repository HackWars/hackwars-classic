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
Loads a Reuters news source.
*/
public class ReutersSource  extends PrimeSource implements Serializable{
	////////////////////////
	// Constructor.
	public ReutersSource(String url){
		super(url);
	}

	/** Fetch the news items in the form of an ArrayList.*/
	public ArrayList fetchItems(String url){
		ArrayList returnMe=new ArrayList();
		try{
			LoadXML data=new LoadXML();
			data.loadFile(url);
			//Now extract the data.

			ItemData item=new ItemData();

			//Find title.
			Node tn=data.findNodeRecursive("title",0);
			if(tn==null)
				item.setComplete(false);
			else{
				tn=data.findNodeRecursive(tn,"#text",0);
				if(tn==null)
					item.setComplete(false);
				else
					item.setTitle(tn.getNodeValue());
			}

			//Find Description.
			int i=0;
			String description="";
			while((tn=data.findNodeRecursive("p",i))!=null){
				tn=data.findNodeRecursive(tn,"#text",0);
				if(tn!=null)
					description+=" "+tn.getNodeValue();
				i++;
			}
			item.setDescription(description);

			//Find Date.
			tn=data.findNodeRecursive("dateline",0);
			if(tn==null)
				item.setComplete(false);
			else{
				tn=data.findNodeRecursive(tn,"#text",0);
				if(tn==null)
					item.setComplete(false);
				else
					item.setDate(tn.getNodeValue());
			}

			returnMe.add(item);

		}catch(Exception e){
			e.printStackTrace();
			returnMe=null;
		}
		return(returnMe);
	}

	//Testing main.
	public static void main(String args[]){
		PrimeSource P=new ReutersSource("98845newsML.xml");
		ArrayList L=P.getItems();
		for(int i=0;i<L.size();i++){
			ItemData temp=(ItemData)L.get(i);
			System.out.println(temp.getTitle());
			System.out.println(temp.getDescription());
			System.out.println(temp.getDate());
		}
	}
}