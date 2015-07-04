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
public class RssSource  extends PrimeSource implements Serializable{
	////////////////////////
	// Constructor.
	public RssSource(String url){
		super(url);
	}

	/** Fetch the news items in the form of an ArrayList.*/
	public ArrayList fetchItems(String url){
		ArrayList returnMe=new ArrayList();
		try{
			LoadXML data=new LoadXML();
			data.loadURL(url);
			//Now extract the data.
			Node n;
			int i=0;
			while((n=data.findNodeRecursive("item",i))!=null){
				ItemData item=new ItemData();

				//Find title.
				Node tn=data.findNodeRecursive(n,"title",0);
				if(tn==null)
					item.setComplete(false);
				else{
					tn=data.findNodeRecursive(tn,"#text",0);
					if(tn==null)
						item.setComplete(false);
					else
						item.setTitle(tn.getNodeValue());
				}

				//Find link.
				tn=data.findNodeRecursive(n,"link",0);
				if(tn==null)
					item.setComplete(false);
				else{
					tn=data.findNodeRecursive(tn,"#text",0);
					if(tn==null)
						item.setComplete(false);
					else
						item.setLink(tn.getNodeValue());
				}

				//Find Description.
				tn=data.findNodeRecursive(n,"description",0);
				if(tn==null)
					item.setComplete(false);
				else{
					tn=data.findNodeRecursive(tn,"#text",0);
					if(tn==null)
						item.setComplete(false);
					else{
						String temp=tn.getNodeValue();
						String description="";

						boolean on=true;
						for(int index=0;index<temp.length();index++){
							if(on)
								description+=temp.charAt(index);
							if(temp.charAt(index)=='<')
								on=false;
							if(temp.charAt(index)=='>')
								on=true;
						}
						item.setDescription(description);
					}
				}

				//Find Date.
				tn=data.findNodeRecursive(n,"pubDate",0);
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
				i++;
			}

		}catch(Exception e){
			e.printStackTrace();
			returnMe=null;
		}
		return(returnMe);
	}
}