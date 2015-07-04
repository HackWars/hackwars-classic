/*
Programmer: Benjamin E. Coe 2008

Description: A variable, e.g., int, string, etc.
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

public  class TypeInteger extends Variable implements Serializable{
	private int value;
	
	public TypeInteger(int value){
		this.value=value;
	}
	
	public int intValue(){
		return(value);
	}
	
	public Variable not(){
		if(value>0)
			return(new TypeBoolean(true));
		else
			return(new TypeBoolean(false));
	}
	
	public boolean truthValue(){
		if(value>0)
			return(true);
		return(false);
	}
	
	public void setValue(Variable Value){
		int tempValue=value;
		if(Value instanceof TypeFloat){
			tempValue=(int)((TypeFloat)Value).getFloatValue();
		}else if(Value instanceof TypeInteger){
			tempValue=((TypeInteger)Value).getIntValue();
		}else
			tempValue=0;
		value=tempValue;
	}
	
	public Variable castFloat(){
		return(new TypeFloat((float)value));
	}
		
	public Variable castString(){
		return(new TypeString(""+value));
	}
		
	public Variable castInt(){
		return(new TypeInteger(value));
	}
		
	public Variable castBool(){
		if(value>0)
			return(new TypeBoolean(true));
		else
			return(new TypeBoolean(false));
	}

	public Object getRawValue(){
		return((Object)value);
	}
	
	public int getIntValue(){
		return(value);
	}

	public Variable increment(){
		int tempValue=value;
		value++;
		return(new TypeFloat(tempValue));
	}
	
	public Variable decrement(){
		int tempValue=value;
		value--;
		return(new TypeFloat(tempValue));
	}

	public Variable positive(){
		int tempValue=value;
		if(tempValue<0)
			tempValue=tempValue*-1;
		
		return(new TypeInteger(tempValue));
	}
	
	public Variable negative(){
		int tempValue=value;
		tempValue=tempValue*-1;
		return(new TypeInteger(tempValue));
	}

	public Variable add(Variable Value){
		int tempValue=value;
		if(Value instanceof TypeFloat){
			return(new TypeFloat((float)tempValue+((TypeFloat)Value).getFloatValue()));
		}else if(Value instanceof TypeInteger){
			tempValue=tempValue+((TypeInteger)Value).getIntValue();
		}else if(Value instanceof TypeString){
			return(new TypeString(""+value+((TypeString)Value).getStringValue()));
		}
		return(new TypeInteger(tempValue));
	}
	
	public Variable subtract(Variable Value){
		int tempValue=value;
		if(Value instanceof TypeFloat){
			return(new TypeFloat((float)tempValue-((TypeFloat)Value).getFloatValue()));
		}else if(Value instanceof TypeInteger){
			tempValue=tempValue-((TypeInteger)Value).getIntValue();
		}
		return(new TypeInteger(tempValue));
	}
	
	public Variable multiply(Variable Value){
		int tempValue=value;
		if(Value instanceof TypeFloat){
			return(new TypeFloat((float)tempValue*((TypeFloat)Value).getFloatValue()));
		}else if(Value instanceof TypeInteger){
			tempValue=tempValue*((TypeInteger)Value).getIntValue();
		}
		return(new TypeInteger(tempValue));
	}
	
	public Variable divide(Variable Value){
		int tempValue=value;
		if(Value instanceof TypeFloat){
			return(new TypeFloat((float)tempValue/((TypeFloat)Value).getFloatValue()));
		}else if(Value instanceof TypeInteger){
			tempValue=tempValue/((TypeInteger)Value).getIntValue();
		}
		return(new TypeInteger(tempValue));
	}
	
	public Variable mod(Variable Value){
		int tempValue=value;
		if(Value instanceof TypeFloat){
			tempValue=tempValue%(int)((TypeFloat)Value).getFloatValue();
		}else if(Value instanceof TypeInteger){
			tempValue=tempValue%((TypeInteger)Value).getIntValue();
		}
		return(new TypeInteger(tempValue));
	}
	
	public Variable equal(Variable Value){
		int val1=value;
		int val2=0;
		if(Value instanceof TypeFloat){
			val2=(int)((TypeFloat)Value).getFloatValue();
		}else if(Value instanceof TypeInteger){
			val2=((TypeInteger)Value).getIntValue();
		}
		
		if(val1==val2)
			return(new TypeBoolean(true));
		else
			return(new TypeBoolean(false));
	}
	
	public Variable notEqual(Variable Value){
		int val1=value;
		int val2=0;
		if(Value instanceof TypeFloat){
			val2=(int)((TypeFloat)Value).getFloatValue();
		}else if(Value instanceof TypeInteger){
			val2=((TypeInteger)Value).getIntValue();
		}
		
		if(val1!=val2)
			return(new TypeBoolean(true));
		else
			return(new TypeBoolean(false));	
	}
	
	public Variable lessThan(Variable Value){
		int val1=value;
		int val2=0;
		if(Value instanceof TypeFloat){
			val2=(int)((TypeFloat)Value).getFloatValue();
		}else if(Value instanceof TypeInteger){
			val2=((TypeInteger)Value).getIntValue();
		}
		if(val1<val2)
			return(new TypeBoolean(true));
		else
			return(new TypeBoolean(false));
	}
	
	public Variable greaterThan(Variable Value){
		int val1=value;
		int val2=0;
		if(Value instanceof TypeFloat){
			val2=(int)((TypeFloat)Value).getFloatValue();
		}else if(Value instanceof TypeInteger){
			val2=((TypeInteger)Value).getIntValue();
		}
		if(val1>val2)
			return(new TypeBoolean(true));
		else
			return(new TypeBoolean(false));
	}
	
	public Variable greaterThanEqual(Variable Value){
		int val1=value;
		int val2=0;
		if(Value instanceof TypeFloat){
			val2=(int)((TypeFloat)Value).getFloatValue();
		}else if(Value instanceof TypeInteger){
			val2=((TypeInteger)Value).getIntValue();
		}
		if(val1>=val2)
			return(new TypeBoolean(true));
		else
			return(new TypeBoolean(false));
	}
	
	public Variable lessThanEqual(Variable Value){
		int val1=value;
		int val2=0;
		if(Value instanceof TypeFloat){
			val2=(int)((TypeFloat)Value).getFloatValue();
		}else if(Value instanceof TypeInteger){
			val2=((TypeInteger)Value).getIntValue();
		}
		if(val1<=val2)
			return(new TypeBoolean(true));
		else
			return(new TypeBoolean(false));
	}
	
	public Variable and(Variable Value){
		int val1=value;
		int val2=0;
		if(Value instanceof TypeFloat){
			val2=(int)((TypeFloat)Value).getFloatValue();
		}else if(Value instanceof TypeInteger){
			val2=((TypeInteger)Value).getIntValue();
		}
		if(val1>0&&val2>0)
			return(new TypeBoolean(true));
		else
			return(new TypeBoolean(false));
	}
	
	public Variable or(Variable Value){
		int val1=value;
		int val2=0;
		if(Value instanceof TypeFloat){
			val2=(int)((TypeFloat)Value).getFloatValue();
		}else if(Value instanceof TypeInteger){
			val2=((TypeInteger)Value).getIntValue();
		}
		if(val1>0||val2>0)
			return(new TypeBoolean(true));
		else
			return(new TypeBoolean(false));
	}
	
	public Variable power(Variable Value){
		int val1=value;
		int val2=0;
		if(Value instanceof TypeFloat){
			val2=(int)((TypeFloat)Value).getFloatValue();
		}else if(Value instanceof TypeInteger){
			val2=((TypeInteger)Value).getIntValue();
		}
		return(new TypeInteger((int)Math.pow((double)val1,(double)val2)));
	}
	
	public String toString(){
		return(""+value);
	}
	
	public Variable evaluate(ArrayList ParentTable,Linker MyLinker,boolean function,boolean loop) throws Exception{
		return(new TypeInteger(value));
	}
	
}//END.
