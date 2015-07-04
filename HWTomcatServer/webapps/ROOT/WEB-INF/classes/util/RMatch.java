package util;
//////////////////////////////////////
// RMatch.java
// Description: Used during our recusrive
// search rutine to return data.
//////////////////////////////////////
import org.w3c.dom.Node;

public class RMatch{
	private Node data;
	private int skip;

	/////////////////////////////////
	// CONSTRUCTOR
	//
	/////////////////////////////////
	public RMatch(Node data,int skip){
		this.data=data;
		this.skip=skip;
	}

	/////////////////////////////////
	// getData()
	// Description: Return the data
	// variable.
	/////////////////////////////////
	public Node getData(){
		return(data);
	}

	/////////////////////////////////
	// getSkip()
	// Description: Return the skip
	// variable...suggests how many
	// matches to ignore.
	/////////////////////////////////
	public int getSkip(){
		return(skip);
	}
}