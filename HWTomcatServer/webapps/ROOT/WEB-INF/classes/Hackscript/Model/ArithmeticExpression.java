package Hackscript.Model;
/*
Porgrammer: Ben Coe.(2008)<br />

Holds an expression statement (functions, math, variables).
*/

import org.antlr.runtime.*;
import java.util.*;
import java.io.*;
import org.antlr.runtime.tree.*;
import org.antlr.runtime.Token;

public class ArithmeticExpression extends Expression{
	private int Operation=0;
	private Model MyModel=null;
	private Expression Variable[]=new Expression[]{null,null};
	private Object Parent=null;

	//Constructor.
	public ArithmeticExpression(CommonTree MyTree,Model MyModel,int Operation){
	
		setLine(MyTree.getLine());
		this.Parent=Parent;
		this.Operation=Operation;
		this.MyModel=MyModel;

		int index=0;
		if ( MyTree != null ) {
			for ( int i = 0; i < MyTree.getChildCount(); i++ ) {
				if(i>1)
					break;
			
				CommonTree Child=(CommonTree)MyTree.getChild(i);
				
				if(Child!=null&&!(Child.getType()==Constants.INTEGER_TYPE||Child.getType()==Constants.FLOAT_TYPE||Child.getType()==Constants.BOOLEAN_TYPE||Child.getType()==Constants.STRING_TYPE))
					Variable[index]=ExpressionFactory.getExpression(Child,MyModel);
				
				index++;
				
				if(Child.getType()==Constants.INTEGER_TYPE||Child.getType()==Constants.FLOAT_TYPE||Child.getType()==Constants.BOOLEAN_TYPE||Child.getType()==Constants.STRING_TYPE){
					index--;
				}
			}
		}
		
	}
	
	public Variable evaluate(ArrayList ParentTable,Linker MyLinker,boolean function,boolean loop) throws Exception{
		try{

			if(Variable[1]==null){

				switch(Operation){				
					case Constants.MINUS: return(Variable[0].evaluate(ParentTable,MyLinker,function,loop).negative());
					case Constants.PLUS:  return(Variable[0].evaluate(ParentTable,MyLinker,function,loop).positive());
					case Constants.INCREMENT:  return(Variable[0].evaluate(ParentTable,MyLinker,function,loop).increment());
					case Constants.DECREMENT:  return(Variable[0].evaluate(ParentTable,MyLinker,function,loop).decrement());
					case Constants.NOT:  return(Variable[0].evaluate(ParentTable,MyLinker,function,loop).not());
					case Constants.INTEGER_TYPE:  return(Variable[0].evaluate(ParentTable,MyLinker,function,loop).castInt());
					case Constants.FLOAT_TYPE:  return(Variable[0].evaluate(ParentTable,MyLinker,function,loop).castFloat());
					case Constants.BOOLEAN_TYPE:  return(Variable[0].evaluate(ParentTable,MyLinker,function,loop).castBool());
					case Constants.STRING_TYPE:  return(Variable[0].evaluate(ParentTable,MyLinker,function,loop).castString());

					default: break;
				}
			}

			switch(Operation){
				case Constants.MINUS: return(Variable[0].evaluate(ParentTable,MyLinker,function,loop).subtract(Variable[1].evaluate(ParentTable,MyLinker,function,loop)));
				case Constants.PLUS:  return(Variable[0].evaluate(ParentTable,MyLinker,function,loop).add(Variable[1].evaluate(ParentTable,MyLinker,function,loop))); 
				case Constants.POWER_EXPRESSION:  return(Variable[0].evaluate(ParentTable,MyLinker,function,loop).power(Variable[1].evaluate(ParentTable,MyLinker,function,loop)));
				case Constants.TIMES:  return(Variable[0].evaluate(ParentTable,MyLinker,function,loop).multiply(Variable[1].evaluate(ParentTable,MyLinker,function,loop)));
				case Constants.MOD: return(Variable[0].evaluate(ParentTable,MyLinker,function,loop).mod(Variable[1].evaluate(ParentTable,MyLinker,function,loop)));
				case Constants.DIV:  return(Variable[0].evaluate(ParentTable,MyLinker,function,loop).divide(Variable[1].evaluate(ParentTable,MyLinker,function,loop)));
				case Constants.AND:  return(Variable[0].evaluate(ParentTable,MyLinker,function,loop).and(Variable[1].evaluate(ParentTable,MyLinker,function,loop)));
				case Constants.OR:  return(Variable[0].evaluate(ParentTable,MyLinker,function,loop).or(Variable[1].evaluate(ParentTable,MyLinker,function,loop)));
				case Constants.NOT_EQUALS:  return(Variable[0].evaluate(ParentTable,MyLinker,function,loop).notEqual(Variable[1].evaluate(ParentTable,MyLinker,function,loop)));
				case Constants.GTE:  return(Variable[0].evaluate(ParentTable,MyLinker,function,loop).greaterThanEqual(Variable[1].evaluate(ParentTable,MyLinker,function,loop)));
				case Constants.LT: return(Variable[0].evaluate(ParentTable,MyLinker,function,loop).lessThan(Variable[1].evaluate(ParentTable,MyLinker,function,loop)));
				case Constants.GT:  return(Variable[0].evaluate(ParentTable,MyLinker,function,loop).greaterThan(Variable[1].evaluate(ParentTable,MyLinker,function,loop)));
				case Constants.EQUALS:  return(Variable[0].evaluate(ParentTable,MyLinker,function,loop).equal(Variable[1].evaluate(ParentTable,MyLinker,function,loop)));
				case Constants.LTE:  return(Variable[0].evaluate(ParentTable,MyLinker,function,loop).lessThanEqual(Variable[1].evaluate(ParentTable,MyLinker,function,loop)));
				
				default:break;
			}
			
		}catch(Exception e){			
			if(!(e instanceof ModelError))
				throw(new ModelError("Runtime error on line "+getLine()+" in arithmetic expression."));
			else
				throw(e);
		}
		return(null);
	}
	
}//END.
