/*
Programmer: Benjamin E. Coe 2008

Description: Parses a statement from the AST tree.
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

public class StatementFactory{

	public static Statement getStatement(CommonTree Child,Model MyModel){
		Statement returnMe=null;
		int Type=Child.getType();
		if(Type==Constants.FUNCTION){
			returnMe=new Function(Child,MyModel);
		}else
		
		if(Type==Constants.DECLARATION){
			returnMe=new DeclarationStatement(Child,MyModel);
		}else
		
		if(Type==Constants.FOR_STATEMENT){
			returnMe=new ForStatement(Child,MyModel);
		}else
		
		if(Type==Constants.WHILE_STATEMENT){
			returnMe=new WhileStatement(Child,MyModel);
		}else
		
		if(Type==Constants.PROCEDURE){
			returnMe=new ProcedureStatement(Child,MyModel);
		}else
		
		if(Type==Constants.IFPART){
			returnMe=new IfStatement(Child,MyModel);
		}
		return(returnMe);
	}

}//END.
