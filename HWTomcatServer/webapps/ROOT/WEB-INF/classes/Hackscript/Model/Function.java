/*
Programmer: Benjamin E. Coe 2008

Description: A statement;
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

public class Function extends Statement{
	private String FunctionName="";
	private String TypeString="";
	private ArrayList ProcedureParameters=new ArrayList();
	private ArrayList Statements=new ArrayList();
	private ArrayList VariableNames=new ArrayList();
	private Model MyModel=null;
	
	//Constructor.
	public Function(CommonTree MyTree,Model MyModel){
		setLine(MyTree.getLine());
		this.MyModel=MyModel;
	
		TypeString=MyTree.getChild(0).toString();
		FunctionName=MyTree.getChild(1).toString();
		MyModel.registerFunction(FunctionName,this);
		
		if(MyTree.getChildCount()>2){
			CommonTree ParameterTree=(CommonTree)MyTree.getChild(2);
			
			if(ParameterTree.getChildCount()>0)
			for (int i=ParameterTree.getChildCount()/2;i<ParameterTree.getChildCount();i+=1) {
				String VariableName=ParameterTree.getChild(i).toString();
				if(VariableName.equals("ARRAY_IDENTIFIER"))
					VariableName=ParameterTree.getChild(i).getChild(0).toString();
				VariableNames.add(VariableName);
			}
		}
		
		if(MyTree.getChildCount()>3){
			for(int i=3;i<MyTree.getChildCount();i++){
				Statements.add(StatementFactory.getStatement((CommonTree)MyTree.getChild(i),MyModel));
			}
		}
	}

	public Variable execute(ArrayList Parameters,ArrayList ParentTable,Linker MyLinker,boolean function,boolean loop) throws Exception{
		try{		
			//Interact with underlying model.
			MyModel.incrementIterationCounter(getLine());
			if(MyModel.getExceptionRaised()!=null)
				throw(MyModel.getExceptionRaised());
		
			HashMap SymbolTable=new HashMap();
			if(Parameters!=null){//Add the function input to our Hash Table.
				for(int i=0;i<Parameters.size();i++){
					if(!(Parameters.get(i) instanceof TypeArray)){
						SymbolTable.put(VariableNames.get(i),Parameters.get(i));
					}else{
						ArrayList TempArrayList=(ArrayList)((TypeArray)Parameters.get(i)).getRawValue();
						SymbolTable.put(VariableNames.get(i),TempArrayList);
					}
				}
			}
									
			ParentTable.add(SymbolTable);//Add the symbol table for this function.
			for(int i=0;i<Statements.size();i++){//Execute all the statements in this function.
				((Statement)Statements.get(i)).execute(null,ParentTable,MyLinker,function,loop);
			}
			
			
			//Print testing string.
		/*	if(FunctionName.equals("main")){
				for(int i=0;i<ParentTable.size();i++)
					System.out.println(ParentTable.get(i));
			}*/
			ParentTable.remove(ParentTable.size()-1);
			
			//If we are in run all code mode, return a default variable of the proper type.
			if(MyModel.getRunAllCode()){
				if(TypeString.equals("int")){
					return(new TypeInteger(0));
				}else if(TypeString.equals("float")){
					return(new TypeFloat(0.0f));
				}else if(TypeString.equals("boolean")||TypeString.equals("bool")){
					return(new TypeBoolean(true));
				}else if(TypeString.equals("string")||TypeString.equals("String")){
					return(new TypeString(""));
				}
				return(null);
			}
		
		}catch(Exception e){
			if(e instanceof ModelBreak)
				ParentTable.remove(ParentTable.size()-1);
		
			if(e instanceof ModelReturn){//Not actually an exception, a return statement.
				//If we are in run all code mode, return a default variable of the proper type.
				if(MyModel.getRunAllCode()){
					if(TypeString.equals("int")){
						return(new TypeInteger(0));
					}else if(TypeString.equals("float")){
						return(new TypeFloat(0.0f));
					}else if(TypeString.equals("boolean")||TypeString.equals("bool")){
						return(new TypeBoolean(true));
					}else if(TypeString.equals("string")||TypeString.equals("String")){
						return(new TypeString(""));
					}
					return(null);
				}
			
				return(((ModelReturn)e).getVariable());
			}
								
			if(!(e instanceof ModelError))
				throw(new ModelError("Runtime error on line "+getLine()+" when running function '"+FunctionName+"'"));
			else
				throw(e);
		}
		return(null);
	}
}//END.
