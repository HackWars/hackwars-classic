/*
Programmer: Benjamin E. Coe 2008

Description: An identifier variable type. Used when exressions reference symbols indirectly.
*/
package Hackscript.Model;

import org.antlr.runtime.*;
import org.antlr.runtime.debug.*;
import java.util.Stack;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.io.*;
import org.antlr.runtime.tree.*;
import org.antlr.runtime.Token;

public  class TypeIdentifier extends Variable implements Serializable{
	private String value;
	private Expression ArrayExpression=null;
	private boolean isArray=false;
	
	public TypeIdentifier(CommonTree MyTree,Model MyModel){
		setLine(MyTree.getLine());
		value=MyTree.getChild(0).toString();
		if(MyTree.getChildCount()>1)
			ArrayExpression=ExpressionFactory.getExpression((CommonTree)MyTree.getChild(1),MyModel);
		isArray=true;
	}

	public Variable castFloat(){
		return(new TypeFloat(0.0f));
	}
	
	
	public Variable not(){
		return(new TypeBoolean(false));
	}
		
	public Variable castString(){
		return(new TypeString(""));
	}
		
	public Variable castInt(){
		return(new TypeInteger(0));
	}
		
	public Variable castBool(){
		return(new TypeBoolean(false));
	}
	
	public TypeIdentifier(String value){
		this.value=value;
	}
	
	public int intValue(){
		return(0);
	}
	
	public boolean truthValue(){
		return(false);
	}
	
	public void setValue(Variable Value){
	}

	public Object getRawValue(){
		return((Object)value);
	}
	
	public Variable increment(){
		return(null);
	}
	
	public Variable decrement(){
		return(null);
	}

	public Variable positive(){
		return(null);
	}
	
	public Variable negative(){
		return(null);
	}

	public Variable add(Variable Value){
		return(null);
	}
	
	public Variable subtract(Variable Value){
		return(null);
	}
	
	public Variable multiply(Variable Value){
		return(null);
	}
	
	public Variable divide(Variable Value){
		return(null);
	}
	
	public Variable mod(Variable Value){
		return(null);
	}
	
	public Variable equal(Variable Value){
		return(null);
	}
	
	public Variable notEqual(Variable Value){
		return(null);
	}
	
	public Variable lessThan(Variable Value){
		return(null);
	}
	
	public Variable greaterThan(Variable Value){
		return(null);
	}
	
	public Variable greaterThanEqual(Variable Value){
		return(null);
	}
	
	public Variable lessThanEqual(Variable Value){
		return(null);
	}
	
	public Variable and(Variable Value){
		return(null);
	}
	
	public Variable or(Variable Value){
		return(null);
	}
	
	public Variable power(Variable Value){
		return(null);
	}
	
	public String toString(){
		return(""+value);
	}
	
	public Variable evaluate(ArrayList ParentTable,Linker MyLinker,boolean function,boolean loop) throws Exception{
		Object MyVariable=null;
		try{
	
			//Find the variable in the stack of Hash Maps.
			//HashMap MyTable=(HashMap)ParentTable.get(ParentTable.size()-1);
			if(MyVariable==null){
				int i=ParentTable.size()-1;
				while(i>=0){
					HashMap TempTable=(HashMap)ParentTable.get(i);
					MyVariable=TempTable.get(value);
					if(MyVariable!=null)
						break;
					i--;
				}
			}
			
			//De-reference an array type.
			if(MyVariable instanceof ArrayList){
			
				if(ArrayExpression!=null){
					int index=ArrayExpression.evaluate(ParentTable,MyLinker,function,loop).intValue();
					if(index>=0&&index<((ArrayList)MyVariable).size())
						return(((Variable)((ArrayList)MyVariable).get(index)).evaluate(ParentTable,MyLinker,function,loop));
					else
						return(new TypeInteger(0));
				}else{
					return(new TypeArray((ArrayList)MyVariable));
				}
			}
		
		}catch(Exception e){
			if(!(e instanceof ModelError))
				throw(new ModelError("Runtime error on line "+getLine()+" when accessing identifier '"+value+"'"));
			else
				throw(e);
		}
		
		if(MyVariable==null)
					return(new TypeInteger(0));
		return(((Variable)MyVariable).evaluate(ParentTable,MyLinker,function,loop));
	}
}//END.
