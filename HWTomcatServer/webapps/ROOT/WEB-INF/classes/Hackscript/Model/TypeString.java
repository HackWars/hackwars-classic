/*
Programmer: Benjamin E. Coe 2008

Description: A variable, e.g., float, string, etc.
*/
package Hackscript.Model;

import org.antlr.runtime.*;
import org.antlr.runtime.debug.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.io.*;
import org.antlr.runtime.tree.*;
import org.antlr.runtime.Token;

public  class TypeString extends Variable implements Serializable{
	private String value;
	
	public boolean truthValue(){
		if(value.length()>0)
			return(true);
		return(false);
	}
	
	public Variable castFloat(){
		return(new TypeFloat(0.0f));
	}
		
	public Variable castString(){
		return(new TypeString(value));
	}
		
	public Variable castInt(){
		return(new TypeInteger(0));
	}
	
	public Variable not(){
		if(value==null)
			return(new TypeBoolean(true));
		else
			return(new TypeBoolean(false));
	}
		
	public Variable castBool(){
		return(new TypeBoolean(false));
	}
	
	public int intValue(){
		return(value.length());
	}
	
	public TypeString(String value){
		this.value=value;
	}

	public void setValue(Variable Value){
		if(Value!=null)
			value=Value.toString();
		else
			value="";
	}

	public Object getRawValue(){
		return((Object)value);
	}
	
	public String getStringValue(){
		return(value);
	}
	
	public Variable increment(){
		return(new TypeString(value));
	}
	
	public Variable decrement(){
		return(new TypeString(value));
	}
	
	public Variable positive(){
		return(new TypeInteger(0));
	}
	
	public Variable negative(){
		return(new TypeInteger(0));
	}

	public Variable add(Variable Value){
		return(new TypeString(value+Value.toString()));
	}
	
	public Variable subtract(Variable Value){
		return(new TypeInteger(0));
	}
	
	public Variable multiply(Variable Value){
		return(new TypeInteger(0));
	}
	
	public Variable divide(Variable Value){
		return(new TypeInteger(0));
	}
	
	public Variable mod(Variable Value){
		return(new TypeInteger(0));
	}
	
	public Variable equal(Variable Value){
		String CompareTo=""+Value;
		return(new TypeBoolean(value.equals(CompareTo)));
	}
	
	public Variable notEqual(Variable Value){
		String CompareTo=""+Value;
		if(value.equals(CompareTo))
			return(new TypeBoolean(false));
		else
			return(new TypeBoolean(true));
	}
	
	public Variable lessThan(Variable Value){
		return(new TypeInteger(0));
	}
	
	public Variable greaterThan(Variable Value){
		return(new TypeInteger(0));
	}
	
	public Variable greaterThanEqual(Variable Value){
		return(new TypeInteger(0));
	}
	
	public Variable lessThanEqual(Variable Value){
		return(new TypeInteger(0));
	}
	
	public Variable and(Variable Value){
		return(new TypeInteger(0));
	}
	
	public Variable or(Variable Value){
		return(new TypeInteger(0));
	}
	
	public Variable power(Variable Value){
		return(new TypeInteger(0));
	}
	
	public String toString(){
		return(""+value);
	}
	
	public Variable evaluate(ArrayList ParentTable,Linker MyLinker,boolean function,boolean loop){
		return(new TypeString(value));
	}
	
}//END.
