/*
Programmer: Benjamin E. Coe 2008

Description: Parses an expression from the AST tree.
*/
package Hackscript.Model;
import Hackscript.*;

import org.antlr.runtime.*;
import org.antlr.runtime.debug.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.io.*;
import org.antlr.runtime.tree.*;
import org.antlr.runtime.Token;

public class ExpressionFactory{

	public static Expression getExpression(CommonTree Child,Model MyModel){
		Expression returnMe=null;
		int Type=Child.getType();
		if(Type==Constants.NOT||Type==Constants.CAST_EXPRESSION||Type==Constants.MINUS||Type==Constants.PLUS||Type==Constants.POWER_EXPRESSION||Type==Constants.TIMES||Type==Constants.MOD||
			Type==Constants.DIV||Type==Constants.AND||Type==Constants.OR||Type==Constants.NOT_EQUALS||Type==Constants.GTE||
			Type==Constants.LT||Type==Constants.GT||Type==Constants.EQUALS||Type==Constants.LTE||Type==Constants.INCREMENT||Type==Constants.DECREMENT){
			if(Type!=Constants.CAST_EXPRESSION)
				returnMe=(Expression)(new ArithmeticExpression(Child,MyModel,Type));
			else{//We must be attempting to cast move one step farther into tree.
				if(Child.getChildCount()>0){
					Type=((CommonTree)Child.getChild(0)).getType();
					returnMe=(Expression)(new ArithmeticExpression(Child,MyModel,Type));
				}
			}
		}else if(Type==Constants.DOUBLE){
			returnMe=new TypeFloat(new Float(Child.toString()));
		}else if(Type==Constants.STRING_LITERAL){
			returnMe=new TypeString(Child.toString().substring(1,Child.toString().length()-1));
		}else if(Type==Constants.INTEGER_LITERAL){
			returnMe=new TypeInteger(new Integer(Child.toString()));
		}else if(Type==Constants.BOOLEAN_LITERAL){
			returnMe=new TypeBoolean(new Boolean(Child.toString()));
		}else if(Type==Constants.IDENTIFIER){
			returnMe=new TypeIdentifier(Child.toString());
		}else if(Type==Constants.ARRAY_IDENTIFIER){
			returnMe=new TypeIdentifier(Child,MyModel);
		}else if(Type==Constants.ARRAY_DECLARE){
			returnMe=new ArrayDeclareExpression(Child,MyModel);
		}else if(Type==Constants.PROCEDURE){
			returnMe=new ProcedureExpression(Child,MyModel);
		}
		return(returnMe);
	}

}//END.
