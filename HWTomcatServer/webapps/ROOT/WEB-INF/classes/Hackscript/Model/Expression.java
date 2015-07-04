/*
Programmer: Benjamin E. Coe 2008

Description: An expression, e.g., arithmetic, procedure.
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

public abstract class Expression implements Serializable{
	private int line=0;
	public abstract Variable evaluate(ArrayList ParentTable,Linker MyLinker,boolean function,boolean loop) throws Exception;
	public int getLine(){
		return(line);
	}
	public void setLine(int line){
		this.line=line;
	}

}//END.
