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

public abstract class Variable extends Expression implements Serializable{
	public abstract boolean truthValue();
	
	public abstract int intValue();

	public abstract void setValue(Variable Value);
	
	public abstract Variable increment();
	
	public abstract Variable decrement();

	public abstract Object getRawValue();
	
	public abstract Variable positive();
	
	public abstract Variable negative();
	
	public abstract Variable castFloat();
	
	public abstract Variable castString();
	
	public abstract Variable castInt();
	
	public abstract Variable castBool();

	public abstract Variable add(Variable Value);
	
	public abstract Variable subtract(Variable Value);
	
	public abstract Variable multiply(Variable Value);
	
	public abstract Variable divide(Variable Value);
	
	public abstract Variable mod(Variable Value);
	
	public abstract Variable equal(Variable Value);
	
	public abstract Variable notEqual(Variable Value);
	
	public abstract Variable lessThan(Variable Value);
	
	public abstract Variable greaterThan(Variable Value);
	
	public abstract Variable greaterThanEqual(Variable Value);
	
	public abstract Variable lessThanEqual(Variable Value);
	
	public abstract Variable and(Variable Value);
	
	public abstract Variable or(Variable Value);
	
	public abstract Variable power(Variable Value);
	
	public abstract Variable not();
	
	public abstract Variable evaluate(ArrayList ParentTable,Linker MyLinker,boolean function,boolean loop) throws Exception;
}//END.
