package com.plink.dolphinstem.util;
/**
<b>
Programmer: Ben Coe (2006)<br /><br />
</b>
Load an XML document from local or remote source into a DOM Tree.
*/

import java.util.*;
//JAXP lib.
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
//File IO.
import java.io.*;
import java.net.URL;
//DOM lib (xml to object.)
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NamedNodeMap;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
//Exceptions.
import java.lang.Exception.*;

public class LoadXML{
	private Document domTree=null;//The tree.
	private Node root=null;//Root node of document tree.
	private String host=null;
	private String protocol=null;
	
	/**
	Set the current document.
	*/
	public void setDocument(Document MyDocument){
		this.domTree=MyDocument;
		root=(Node)domTree;
	}

	///////////////////////////////////////////////////
	// getDocument()
	// Description: Get the DOM tree variable.
	///////////////////////////////////////////////////
	public Document getDocument(){
		return(domTree);
	}

	///////////////////////////////////////////////////
	// getRoot()
	// Description: Return the document's root node.
	///////////////////////////////////////////////////
	public Node getRoot(){
		return(root);
	}

	ArrayList PreviousNodes=null;
	public ArrayList getPreviousNodes(){
		return(PreviousNodes);
	}

	///////////////////////////////////////////////////
	// getHost()
	// Description: Return the document's host.
	///////////////////////////////////////////////////
	public String getHost(){
		return(host);
	}

	///////////////////////////////////////////////////
	// getHost()
	// Description: Return the document's protocol.
	///////////////////////////////////////////////////
	public String getProtocol(){
		return(protocol);
	}

	///////////////////////////////////////////////////
	// loadUrl(String) : void
	// Description: Generate a DOM tree based on the URL
	// provided.
	///////////////////////////////////////////////////
	public void loadURL(String path) throws Exception{
		URL url=null;
		//Generate an URL based on the path provided.
		try{
			url=new URL(path);
		}catch(Exception e){
			e.printStackTrace();
			throw(new Exception("CDoc.loadURL() : Badly formed URL."));
		}
		protocol=url.getProtocol();
		host=url.getHost();

		//Connect to URL provided.
		Object content=null;
		try{
			content=url.getContent();
		}catch(Exception e){
			throw(new Exception("CDoc.loadURL() : Unable to connect to URL."));
		}

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setCoalescing(true);
		factory.setValidating(false);
		try {
 			DocumentBuilder builder = factory.newDocumentBuilder();
			domTree = builder.parse((InputStream)content);
		} catch (Exception e) {
			throw(new Exception("CDoc.loadURL() : Error parsing URL."));
		}
		root=(Node)domTree;
	}

	///////////////////////////////////////////////////
	// loadFile(String) : void
	// Description: Construct a DOM tree based on the
	// path to a file provided.
	///////////////////////////////////////////////////
	public void loadFile(String path) throws Exception{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setCoalescing(true);
		factory.setValidating(false);
		try{
 			DocumentBuilder builder = factory.newDocumentBuilder();
			domTree = builder.parse(new File(path));
 			root=(Node)domTree;
		}catch(Exception e){
			throw(new Exception("CDoc.loadFile() : Error parsing file."+e));
    	}
	}

	///////////////////////////////////////////////////
	// findNode(String,int)
	// Description: Find node in document tree based on
	// node name. In this case root of tree is assumed.
	//
	///////////////////////////////////////////////////
	public Node findNode(String name,int skip){
		return(findNode(root,name,skip));
	}

	///////////////////////////////////////////////////
	// findNode(int,int)
	// Description: Find node in document tree based on
	// node type. In this case root of tree is assumed.
	//
	///////////////////////////////////////////////////
	public Node findNode(int type,int skip){
		return(findNode(root,type,skip));
	}

	///////////////////////////////////////////////////
	// findNode(Node,String,int)
	// Description: Find node in document tree based on
	// node's name. In this case we specify parent node.
	//
	///////////////////////////////////////////////////
	public Node findNode(Node parent,String name,int skip){

		if(parent==null)//Tree not initialized.
				return(null);

		int cmatch=0;

		for(int i=0;i<parent.getChildNodes().getLength();i++){
			Node temp=parent.getChildNodes().item(i);
			if(temp.getNodeName().toUpperCase().equals(name)&&cmatch>=skip)
				return(temp);
			else if(temp.getNodeName().toUpperCase().equals(name))
				cmatch++;
		}

		return(null);
	}

	///////////////////////////////////////////////////
	// findNode(Node,int,int)
	// Description: Find node in document tree based on
	// node's type. In this case we specify parent node.
	//
	///////////////////////////////////////////////////
	public Node findNode(Node parent,int type,int skip){

		if(root==null)//Tree not initialized.
				return(null);

		int cmatch=0;

		for(int i=0;i<parent.getChildNodes().getLength();i++){
			Node temp=parent.getChildNodes().item(i);
			if(temp.getNodeType()==type&&cmatch>=skip)
				return(temp);
			else if(temp.getNodeType()==type)
				cmatch++;
		}

		return(null);
	}


	///////////////////////////////////////////////////
	// findNodeRecursive(String,int)
	// Description: Find node n document tree based on
	// node name. In this case root of tree is assumed.
	// ignores tree's structure.
	///////////////////////////////////////////////////
	public Node findNodeRecursive(String name,int skip){
		name=name.toUpperCase();
		return(findNodeRecursive(root,name,skip));
	}

	///////////////////////////////////////////////////
	// findNodeRecursive(Node,String,int)
	// Description: Find node in document tree based on
	// node's name. In this case we specify parent node.
	// ignores tree's structure.
	///////////////////////////////////////////////////
	public Node findNodeRecursive(Node parent,String name,int skip){
		PreviousNodes=new ArrayList();
		name=name.toUpperCase();
		if(root==null)//Tree not initialized.
			return(null);

		skip++;
		RMatch m=findNodeRAN(parent,name,skip);
		if(m.getData()!=null)
			return(m.getData());
		else
			return(null);
	}

	///////////////////////////////////////////////////
	// findNode(String,int)
	// Description: Find node in document tree based on
	// node type. In this case root of tree is assumed.
	//
	///////////////////////////////////////////////////
	public Node findNodeRecursive(int type,int skip){
		return(findNodeRecursive(root,type,skip));
	}

	///////////////////////////////////////////////////
	// findNodeRecursive(Node,String,int)
	// Description: Find node in document tree based on
	// node type. In this case we specify parent node.
	//
	///////////////////////////////////////////////////
	public Node findNodeRecursive(Node parent,int type,int skip){
		if(root==null)//Tree not initialized.
			return(null);

		skip++;
		RMatch m=findNodeRAT(parent,type,skip);
		if(m.getData()!=null)
			return(m.getData());
		else
			return(null);
	}

	///////////////////////////////////////////////////
	// findNodeRAN(String,int) //Recursive Algorithim Name.
	// Description: The recursive searching algorithim.
	// Used indirectly due to the match object it returns.
	// In this catch we search for node name.
	///////////////////////////////////////////////////
	private RMatch findNodeRAN(Node parent,String name,int skip){
	
		if(parent.getNodeName().toUpperCase().equals(name)&&skip<=1){
			PreviousNodes.add(parent);
			return(new RMatch(parent,0));
		}else if(parent.getNodeName().toUpperCase().equals(name)){
			PreviousNodes.add(parent);
			skip--;
		}

		RMatch data=null;

		for(int i=0;i<parent.getChildNodes().getLength();i++){
			if((data=findNodeRAN(parent.getChildNodes().item(i),name,skip))!=null){
				if(data.getSkip()>0)
					skip=data.getSkip();
				else
					return(data);
			}
		}

		return(new RMatch(null,skip));
	}

	///////////////////////////////////////////////////
	// findNodeRAT(String,int) //Recursive Algorithim Type.
	// Description: The recursive searching algorithim.
	// Used indirectly due to the match object it returns.
	// In this catch we search for node type.
	///////////////////////////////////////////////////
	private RMatch findNodeRAT(Node parent,int type,int skip){

		if(parent.getNodeType()==type&&skip<=1)
			return(new RMatch(parent,0));
		else if(parent.getNodeType()==type)
			skip--;

		RMatch data=null;
		for(int i=0;i<parent.getChildNodes().getLength();i++){
			if((data=findNodeRAT(parent.getChildNodes().item(i),type,skip))!=null){
				if(data.getSkip()>0)
					skip=data.getSkip();
				else
					return(data);
			}
		}

		return(new RMatch(null,skip));
	}

	///////////////////////////////////////////////////
	// printNode()
	// Description: Print a single node of our
	// document tree..
	///////////////////////////////////////////////////
	private String printNode(Node parent,int depth,String data){
		for(int i=0;i<depth;i++)
			data+="  ";

		//Get name of current node.
		data+="<"+parent.getNodeName();

		//Node has no children so we get its value.
		if(parent.getChildNodes().getLength()==0){//See if it has children.
			data+=">\n";

			//Make sure data exits.
			if(parent.getNodeValue()!=null){
				for(int i=0;i<depth;i++)
					data+="  ";
				data+=parent.getNodeValue()+"\n";
			}

			for(int i=0;i<depth;i++)
				data+="  ";

			data+="</"+parent.getNodeName()+">\n";
			return(data);
		}else
			data+=">\n";

		//Recursivly print all the children
		for(int i=0;i<parent.getChildNodes().getLength();i++)
			data+=printNode(parent.getChildNodes().item(i),depth+1,"");

		//Close the tag.
		for(int i=0;i<depth;i++)
				data+="  ";
		data+="</"+parent.getNodeName()+">\n";

		return(data);
	}

	///////////////////////////////////////////////////
	// findAttribute(Node,String)
	// Description: Get a list of the attributes
	// contained in a node
	///////////////////////////////////////////////////
	public String findAttribute(Node parent,String name){
		NamedNodeMap map=parent.getAttributes();
		if(map==null)
			return(null);
		Node attrib;
		for(int i=0;i<map.getLength();i++){
			attrib=map.item(i);
			if(name.equals(attrib.getNodeName())){
				return(attrib.getNodeValue());
			}
		}
		return(null);
	}

	///////////////////////////////////////////////////
	// setAttribute(Node,String)
	// Description: Change the value of a given attribute.
	///////////////////////////////////////////////////
	public Node setAttribute(Node parent,String name,String value){
		NamedNodeMap map=parent.getAttributes();
		if(map==null)
			return(null);
		Node attrib;
		for(int i=0;i<map.getLength();i++){
			attrib=map.item(i);
			if(name.equals(attrib.getNodeName())){
				attrib.setNodeValue(value);
				return(attrib);
			}
		}
		return(null);
	}

	///////////////////////////////////////////////////
	// outputXML(String)
	// Description: Output our Document as an XML file.
	//
	///////////////////////////////////////////////////
	public void outputXML(String path) throws Exception{
		try{
			File out = new File(path);
			FileOutputStream fos;
			Transformer transformer;

			fos = new FileOutputStream(out);

			//Use a Transformer for output
			TransformerFactory transformerFactory=TransformerFactory.newInstance();
			transformer = transformerFactory.newTransformer();

			DOMSource source = new DOMSource(domTree);
			StreamResult result = new StreamResult(fos);

			//Output it.
			transformer.transform(source,result);
		}catch(Exception e){
			System.out.println(e);
			throw(new Exception("CDoc.outputXML : Error outputting XML file."));
		}
	}

	///////////////////////////////////////////////////
	// countTag(Node,String)
	// Description: Count the number of occurences of a
	// specific tag under a specified parent.
	///////////////////////////////////////////////////
	public int countTag(Node parent,String name){
		int count=0;
		while(findNode(parent,name,count)!=null)
			count++;
		return(count);
	}

	///////////////////////////////////////////////////
	// countTagRecursive(Node,String)
	// Description: Count the number of occurences of a
	// specific tag under a specified parent.
	///////////////////////////////////////////////////
	public int countTagRecursive(Node parent,String name){
		int count=0;
		while(findNodeRecursive(parent,name,count)!=null)
			count++;
		return(count);
	}

	///////////////////////////////////////////////////
	// addNode(Node,Node,Node)
	// Insert the first node in-front of the one
	// specified. Parent must also be provided.
	///////////////////////////////////////////////////
	public Node addNode(Node nParent,Node nNew,Node nIpoint){
		if(nParent==null||nNew==null)
			return(null);
		Node temp=domTree.importNode(nNew,true);
		nParent.insertBefore(temp,nIpoint);
		return(temp);
	}


	///////////////////////////////////////////////////
	// toString()
	// Description: Get a string representation of our
	// DOM tree.
	///////////////////////////////////////////////////
	public String toString(){
		if(root==null)
			return("");
		return(printNode(root,0,""));
	}


}//Tested.