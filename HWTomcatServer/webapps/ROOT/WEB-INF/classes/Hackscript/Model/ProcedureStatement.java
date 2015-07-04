/*
Programmer: Benjamin E. Coe 2008

Description: A statement in our scriptting language.
*/
package Hackscript.Model;

import org.antlr.runtime.*;
import org.antlr.runtime.debug.*;
import java.util.Stack;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.io.*;
import org.antlr.runtime.tree.*;
import org.antlr.runtime.Token;

public class ProcedureStatement extends Statement{
	private String FunctionName=null;
	private ArrayList Expressions=new ArrayList();
	private Model MyModel=null;
	
	public String getFunctionName(){
		return(FunctionName);
	}

	public ProcedureStatement(CommonTree MyTree,Model MyModel){
		setLine(MyTree.getLine());
		this.MyModel=MyModel;
		FunctionName=MyTree.getChild(0).toString();
		MyModel.registerProcedure(FunctionName);
		
		CommonTree ParameterTree=(CommonTree)MyTree.getChild(1);
		for(int i=0;i<ParameterTree.getChildCount();i++){
			Expressions.add(ExpressionFactory.getExpression((CommonTree)ParameterTree.getChild(i),MyModel));
		}
	}
	
	public Variable executeExpression(ArrayList ParentTable,Linker MyLinker,boolean function,boolean loop) throws Exception{
		try{
			if(Expressions.size()<=0)
				return(new TypeInteger(0));
		
			Expression MyExpression=(Expression)Expressions.get(0);
			return(MyExpression.evaluate(ParentTable,MyLinker,function,loop));
		}catch(Exception e){
			if(e instanceof ModelError)
				throw(e);
			else
				throw(new ModelError("Runtime error executing return statement"));
		}
	}

	public Variable execute(ArrayList IgnoredParameters,ArrayList ParentTable,Linker MyLinker,boolean function,boolean loop) throws Exception{
		try{
			HashMap Symbols=(HashMap)ParentTable.get(0);
			//Check whether we're using recursion but shouldn't be, this is when we're running all code.
			if(MyModel.getRunAllCode()){
				Variable ReturnMe=null;
				if((ReturnMe=(Variable)Symbols.get("1212function1212"+FunctionName))!=null){
					return(ReturnMe);
				}else{
					Symbols.put("1212function1212"+FunctionName,new TypeString(""));
				}
			}
		
			if(loop&&FunctionName.equals("break")){
				ParentTable.remove(ParentTable.size()-1);
				throw(new ModelBreak());
			}
			
			MyModel.incrementIterationCounter(getLine());
			if(MyModel.getExceptionRaised()!=null)
				throw(MyModel.getExceptionRaised());
						
			ArrayList Parameters=new ArrayList();
			for(int i=0;i<Expressions.size();i++){
					Parameters.add(((Expression)Expressions.get(i)).evaluate(ParentTable,MyLinker,function,loop));
			}
			
			//Perform the return operation.
			if(FunctionName.equals("return")){
				Variable ReturnMe=((Expression)Expressions.get(0)).evaluate(ParentTable,MyLinker,function,loop);
				ParentTable.remove(ParentTable.size()-1);
				if(Expressions.size()>0)
					throw(new ModelReturn(ReturnMe));
				else
					throw(new ModelReturn(null));
			}
			
			Statement MyStatement=MyModel.getFunction(FunctionName);

			Variable ReturnMe=null;	
			if(MyStatement!=null){
				ReturnMe=MyStatement.execute(Parameters,ParentTable,MyLinker,function,loop);
			}else{
				if(MyLinker!=null){
					ReturnMe=MyLinker.runFunction(FunctionName,Parameters);
				}
			}
			
			if(MyModel.getRunAllCode())
				Symbols.put("1212function1212"+FunctionName,ReturnMe);
			
			return(ReturnMe);
		}catch(Exception e){
						
			if(!(e instanceof ModelError))
				throw(new ModelError("Runtime error on line "+getLine()+" when running procedure "+FunctionName+"()"));
			else
				throw(e);
		}

	}

}//END.
