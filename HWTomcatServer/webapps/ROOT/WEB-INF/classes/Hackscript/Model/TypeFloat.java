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

public  class TypeFloat extends Variable implements Serializable{
	private float value;
	
	public void setValue(Variable Value){
		float tempValue=value;
		if(Value instanceof TypeFloat){
			tempValue=((TypeFloat)Value).getFloatValue();
		}else if(Value instanceof TypeInteger){
			tempValue=(float)((TypeInteger)Value).getIntValue();
		}
		value=tempValue;
	}
	
	public int intValue(){
		return((int)value);
	}
	
	
	public Variable not(){
		if(value>0.0f)
			return(new TypeBoolean(true));
		else
			return(new TypeBoolean(false));
	}
	
	public boolean truthValue(){
		if(value>0)
			return(true);
		return(false);
	}
	
	public Variable castFloat(){
		return(new TypeFloat((float)value));
	}
		
	public Variable castString(){
		return(new TypeString(""+value));
	}
		
	public Variable castInt(){
		return(new TypeInteger((int)value));
	}
		
	public Variable castBool(){
		if(value>0.0)
			return(new TypeBoolean(true));
		else
			return(new TypeBoolean(false));
	}
	
	public TypeFloat(float value){
		this.value=value;
	}

	public Object getRawValue(){
		return((Object)value);
	}
	
	public float getFloatValue(){
		return(value);
	}
	
	public Variable increment(){
		float tempValue=value;
		value++;
		return(new TypeFloat(tempValue));
	}
	
	public Variable decrement(){
		float tempValue=value;
		value--;
		return(new TypeFloat(tempValue));
	}

	public Variable positive(){
		float tempValue=value;
		if(tempValue<0)
			tempValue=tempValue*-1.0f;
		
		return(new TypeFloat(tempValue));
	}
	
	public Variable negative(){
		float tempValue=value;
		tempValue=tempValue*-1.0f;
		return(new TypeFloat(tempValue));
	}

	public Variable add(Variable Value){
		float tempValue=value;
		if(Value instanceof TypeFloat){
			tempValue=tempValue+((TypeFloat)Value).getFloatValue();
		}else if(Value instanceof TypeInteger){
			tempValue=tempValue+(float)((TypeInteger)Value).getIntValue();
		}else if(Value instanceof TypeString){
			return(new TypeString(""+value+((TypeString)Value).getStringValue()));
		}
		return(new TypeFloat(tempValue));
	}
	
	public Variable subtract(Variable Value){
		float tempValue=value;
		if(Value instanceof TypeFloat){
			tempValue=tempValue-((TypeFloat)Value).getFloatValue();
		}else if(Value instanceof TypeInteger){
			tempValue=tempValue-(float)((TypeInteger)Value).getIntValue();
		}
		return(new TypeFloat(tempValue));
	}
	
	public Variable multiply(Variable Value){
		float tempValue=value;
		if(Value instanceof TypeFloat){
			tempValue=tempValue*((TypeFloat)Value).getFloatValue();
		}else if(Value instanceof TypeInteger){
			tempValue=tempValue*(float)((TypeInteger)Value).getIntValue();
		}
		return(new TypeFloat(tempValue));
	}
	
	public Variable divide(Variable Value){
		float tempValue=value;
		if(Value instanceof TypeFloat){
			tempValue=tempValue/((TypeFloat)Value).getFloatValue();
		}else if(Value instanceof TypeInteger){
			tempValue=tempValue/(float)((TypeInteger)Value).getIntValue();
		}
		return(new TypeFloat(tempValue));
	}
	
	public Variable mod(Variable Value){
		float tempValue=value;
		if(Value instanceof TypeFloat){
			tempValue=tempValue%((TypeFloat)Value).getFloatValue();
		}else if(Value instanceof TypeInteger){
			tempValue=tempValue%(float)((TypeInteger)Value).getIntValue();
		}
		return(new TypeFloat(tempValue));
	}
	
	public Variable equal(Variable Value){
		float val1=value;
		float val2=0.0f;
		if(Value instanceof TypeFloat){
			val2=((TypeFloat)Value).getFloatValue();
		}else if(Value instanceof TypeInteger){
			val2=(float)((TypeInteger)Value).getIntValue();
		}
		
		if(val1==val2)
			return(new TypeBoolean(true));
		return(new TypeBoolean(false));
	}
	
	public Variable notEqual(Variable Value){
		float val1=value;
		float val2=0.0f;
		if(Value instanceof TypeFloat){
			val2=((TypeFloat)Value).getFloatValue();
		}else if(Value instanceof TypeInteger){
			val2=(float)((TypeInteger)Value).getIntValue();
		}
		
		if(val1!=val2)
			return(new TypeBoolean(true));
		return(new TypeBoolean(false));
	}
	
	public Variable lessThan(Variable Value){
		float val1=value;
		float val2=0.0f;
		if(Value instanceof TypeFloat){
			val2=((TypeFloat)Value).getFloatValue();
		}else if(Value instanceof TypeInteger){
			val2=(float)((TypeInteger)Value).getIntValue();
		}
		
		if(val1<val2)
			return(new TypeBoolean(true));
		return(new TypeBoolean(false));
	}
	
	public Variable greaterThan(Variable Value){
		float val1=value;
		float val2=0.0f;
		if(Value instanceof TypeFloat){
			val2=((TypeFloat)Value).getFloatValue();
		}else if(Value instanceof TypeInteger){
			val2=(float)((TypeInteger)Value).getIntValue();
		}
		
		if(val1>val2)
			return(new TypeBoolean(true));
		return(new TypeBoolean(false));
	}
	
	public Variable greaterThanEqual(Variable Value){
		float val1=value;
		float val2=0.0f;
		if(Value instanceof TypeFloat){
			val2=((TypeFloat)Value).getFloatValue();
		}else if(Value instanceof TypeInteger){
			val2=(float)((TypeInteger)Value).getIntValue();
		}
		
		if(val1>=val2)
			return(new TypeBoolean(true));
		return(new TypeBoolean(false));
	}
	
	public Variable lessThanEqual(Variable Value){
		float val1=value;
		float val2=0.0f;
		if(Value instanceof TypeFloat){
			val2=((TypeFloat)Value).getFloatValue();
		}else if(Value instanceof TypeInteger){
			val2=(float)((TypeInteger)Value).getIntValue();
		}
		
		if(val1<=val2)
			return(new TypeBoolean(true));
		return(new TypeBoolean(false));
	}
	
	public Variable and(Variable Value){
		float val1=value;
		float val2=0.0f;
		if(Value instanceof TypeFloat){
			val2=((TypeFloat)Value).getFloatValue();
		}else if(Value instanceof TypeInteger){
			val2=(float)((TypeInteger)Value).getIntValue();
		}
		
		if(val1>0&&val2>0)
			return(new TypeBoolean(true));
		return(new TypeBoolean(false));
	}
	
	public Variable or(Variable Value){
		float val1=value;
		float val2=0.0f;
		if(Value instanceof TypeFloat){
			val2=((TypeFloat)Value).getFloatValue();
		}else if(Value instanceof TypeInteger){
			val2=(float)((TypeInteger)Value).getIntValue();
		}
		
		if(val1>0||val2>0)
			return(new TypeBoolean(true));
		return(new TypeBoolean(false));
	}
	
	public Variable power(Variable Value){
		float val1=value;
		float val2=0.0f;
		if(Value instanceof TypeFloat){
			val2=((TypeFloat)Value).getFloatValue();
		}else if(Value instanceof TypeInteger){
			val2=(float)((TypeInteger)Value).getIntValue();
		}

		return(new TypeFloat((float)Math.pow((double)val1,(double)val2)));
	}
	
	public String toString(){
		return(""+value);
	}
	
	public Variable evaluate(ArrayList ParentTable,Linker MyLinker,boolean function,boolean loop){
		return(new TypeFloat(value));
	}
	
}//END.
