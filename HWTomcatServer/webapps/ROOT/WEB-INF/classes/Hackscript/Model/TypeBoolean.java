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

public  class TypeBoolean extends Variable implements Serializable{
	private boolean value;
	
	public TypeBoolean(boolean value){
		this.value=value;
	}
	
	public boolean truthValue(){
		return(value);
	}
	
	public int intValue(){
		if(value)
			return(1);
		return(0);
	}
	
	public Variable not(){
		if(value)
			return(new TypeBoolean(false));
		else
			return(new TypeBoolean(true));
	}
	
	public void setValue(Variable Value){
		boolean tempValue=value;
		if(Value instanceof TypeInteger){
			int temp=((TypeInteger)Value).getIntValue();
			if(temp>0)
				tempValue=true;
		}else if(Value instanceof TypeFloat){
			float temp=((TypeFloat)Value).getFloatValue();
			if(temp>0)
				tempValue=true;
		}else if(Value instanceof TypeBoolean){
			boolean temp=((TypeBoolean)Value).getBooleanValue();
			tempValue=temp;
		}
		value=tempValue;
	}

	public Variable castFloat(){
		if(value)
			return(new TypeFloat(1.0f));
		else
			return(new TypeFloat(0.0f));
	}
		
	public Variable castString(){
		if(value)
			return(new TypeString(""+value));
		else
			return(new TypeString(""+value));
	}
		
	public Variable castInt(){
		if(value)
			return(new TypeInteger(1));
		else
			return(new TypeInteger(0));
	}
		
	public Variable castBool(){
		return(new TypeBoolean(value));
	}

	public Object getRawValue(){
		return((Object)value);
	}
	
	public boolean getBooleanValue(){
		return(value);
	}

	public Variable increment(){
		return(new TypeBoolean(value));
	}
	
	public Variable decrement(){
		return(new TypeBoolean(value));
	}

	public Variable positive(){
		return(new TypeBoolean(true));
	}
	
	public Variable negative(){
		return(new TypeBoolean(false));
	}

	public Variable add(Variable Value){
		boolean val1=value;
		boolean val2=false;
		if(Value instanceof TypeBoolean){
			val2=((TypeBoolean)Value).getBooleanValue();
		}else if(Value instanceof TypeString){
			return(new TypeString(""+value+((TypeString)Value).getStringValue()));
		}
		
		if(val1==true||val2==true)
			return(new TypeBoolean(true));
		return(new TypeBoolean(false));
	}
	
	public Variable subtract(Variable Value){
		boolean val1=value;
		boolean val2=false;
		if(Value instanceof TypeBoolean){
			val2=((TypeBoolean)Value).getBooleanValue();
		}
		
		if(val2==false)
			return(new TypeBoolean(true));
		return(new TypeBoolean(false));
	}
	
	public Variable multiply(Variable Value){
		boolean val1=value;
		boolean val2=false;
		if(Value instanceof TypeBoolean){
			val2=((TypeBoolean)Value).getBooleanValue();
			if(val2==true)
				val2=false;
			if(val2==false)
				val2=true;
		}
		
		if(val1==true&&val2==true)
			return(new TypeBoolean(true));
		return(new TypeBoolean(false));
	}
	
	public Variable divide(Variable Value){
		boolean val1=value;
		boolean val2=false;
		if(Value instanceof TypeBoolean){
			val2=((TypeBoolean)Value).getBooleanValue();
			if(val2==true)
				val2=false;
			if(val2==false)
				val2=true;
		}
		
		if(val1==true||val2==true)
			return(new TypeBoolean(true));
		return(new TypeBoolean(false));
	}
	
	public Variable mod(Variable Value){
		return(new TypeBoolean(false));
	}
	
	public Variable equal(Variable Value){
		boolean val1=value;
		boolean val2=false;
		
		if(Value instanceof TypeBoolean){
			val2=((TypeBoolean)Value).getBooleanValue();
		}
		
		if(val1==val2)
			return(new TypeBoolean(true));
		return(new TypeBoolean(false));
	}
	
	public Variable notEqual(Variable Value){
		boolean val1=value;
		boolean val2=false;
		
		if(Value instanceof TypeBoolean){
			val2=((TypeBoolean)Value).getBooleanValue();
		}
		
		if(val1!=val2)
			return(new TypeBoolean(true));
		return(new TypeBoolean(false));
	}
	
	public Variable lessThan(Variable Value){
		boolean val1=value;
		boolean val2=false;
		
		if(Value instanceof TypeBoolean){
			val2=((TypeBoolean)Value).getBooleanValue();
		}
		
		if(val1==val2)
			return(new TypeBoolean(true));
		return(new TypeBoolean(false));
	}
	
	public Variable greaterThan(Variable Value){
		boolean val1=value;
		boolean val2=false;
		
		if(Value instanceof TypeBoolean){
			val2=((TypeBoolean)Value).getBooleanValue();
		}
		
		if(val1==val2)
			return(new TypeBoolean(true));
		return(new TypeBoolean(false));
	}
	
	public Variable greaterThanEqual(Variable Value){
		boolean val1=value;
		boolean val2=false;
		
		if(Value instanceof TypeBoolean){
			val2=((TypeBoolean)Value).getBooleanValue();
		}
		
		if(val1==val2)
			return(new TypeBoolean(true));
		return(new TypeBoolean(false));
	}
	
	public Variable lessThanEqual(Variable Value){
		boolean val1=value;
		boolean val2=false;
		
		if(Value instanceof TypeBoolean){
			val2=((TypeBoolean)Value).getBooleanValue();
		}
		
		if(val1==val2)
			return(new TypeBoolean(true));
		return(new TypeBoolean(false));
	}
	
	public Variable and(Variable Value){
		boolean val1=value;
		boolean val2=false;
		
		if(Value instanceof TypeBoolean){
			val2=((TypeBoolean)Value).getBooleanValue();
		}
		
		if(val1&&val2)
			return(new TypeBoolean(true));
		return(new TypeBoolean(false));
	}
	
	public Variable or(Variable Value){
		boolean val1=value;
		boolean val2=false;
		
		if(Value instanceof TypeBoolean){
			val2=((TypeBoolean)Value).getBooleanValue();
		}
		
		if(val1||val2)
			return(new TypeBoolean(true));
		return(new TypeBoolean(false));
	}
	
	public Variable power(Variable Value){
		boolean val1=value;
		boolean val2=false;
		if(Value instanceof TypeBoolean){
			val2=((TypeBoolean)Value).getBooleanValue();
			if(val2==true)
				val2=false;
			if(val2==false)
				val2=true;
		}
		
		if(val1==true&&val2==true)
			return(new TypeBoolean(true));
		return(new TypeBoolean(false));
	}
	
	public String toString(){
		return(""+value);
	}
	
	public Variable evaluate(ArrayList ParentTable,Linker MyLinker,boolean function,boolean loop) throws Exception{
		return(new TypeBoolean(value));
	}
}//END.
