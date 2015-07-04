package Hackscript.Model;
/*
Programmer: Ben Coe.(2008)<br />

ArrayDeclareExpression.java

Description: This is an expression for defining a new array, it has the form {VAR,VAR,VAR,...}. It fetches the
array from the heap and assigns the values.
*/

import org.antlr.runtime.*;
import java.util.*;
import java.io.*;
import org.antlr.runtime.tree.*;
import org.antlr.runtime.Token;

public class ArrayDeclareExpression extends Expression{
	private ArrayList Expressions=new ArrayList();//The Expressions.
	private String value;//The name of the ArrayList.
	
	/**
	Set the name of the variable to fetch from the heap.
	*/
	public void setValue(String value){
		this.value=value;
	}

	//Constructor.
	public ArrayDeclareExpression(CommonTree MyTree,Model MyModel){
		for(int i=0;i<MyTree.getChildCount();i++){//Parse all the expressions making up this ArrayList declare from the tree.
			Expressions.add(ExpressionFactory.getExpression((CommonTree)MyTree.getChild(i),MyModel));
		}
	}
	
	public Variable evaluate(ArrayList ParentTable,Linker MyLinker,boolean function,boolean loop) throws Exception{
		//Find the variable in the stack of Hash Maps.
		HashMap MyTable=(HashMap)ParentTable.get(ParentTable.size()-1);
		
		
		Object MyVariable=null;
		MyVariable=MyTable.get(value);//Fetch the object from the lookup table.
		
		if(MyVariable==null){//If we didn't find the variable look it up in th elower HashMaps.
			int i=ParentTable.size()-1;
			while(i>=0){
				HashMap TempTable=(HashMap)ParentTable.get(i);
				MyVariable=TempTable.get(value);
				if(MyVariable!=null)
					break;
				i--;
			}
		}
		
		if(MyVariable instanceof ArrayList){//Assuming MyVariable is an ArrayList (which it should be).
			((ArrayList)MyVariable).clear();
			for(int i=0;i<Expressions.size();i++){
				((ArrayList)MyVariable).add(((Expression)Expressions.get(i)).evaluate(ParentTable,MyLinker,function,loop));
			}
		}
		
		return(null);
	}
	
}//END.
