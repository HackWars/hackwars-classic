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

public  class TypeArray extends Variable implements Serializable{
	private ArrayList value;
	
	public boolean truthValue(){
		return(false);
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
	
	public int intValue(){
		return(0);
	}
	
	public TypeArray(ArrayList value){
		this.value=value;
	}

	public void setValue(Variable Value){
	}

	public Object getRawValue(){
		return((Object)value);
	}
	
	public Variable increment(){
		return(this);
	}
	
	public Variable decrement(){
		return(this);
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
		return(new TypeInteger(0));
	}
	
	public Variable notEqual(Variable Value){
		return(new TypeInteger(0));
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
	
	public Variable evaluate(ArrayList ParentTable,Linker MyLinker,boolean function,boolean loop) throws Exception{
		return(new TypeArray(value));
	}

	
}//END.
