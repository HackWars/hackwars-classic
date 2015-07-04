/*
Programmer: Benjamin E. Coe 2008

Description: A statement in the scriptting language.
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

public class WhileStatement extends Statement{
	private Expression Comparison=null;
	private ArrayList Statements=new ArrayList();
	private Model MyModel=null;

	public WhileStatement(CommonTree MyTree,Model MyModel){
		setLine(MyTree.getLine());
		this.MyModel=MyModel;
	
		Comparison=ExpressionFactory.getExpression((CommonTree)MyTree.getChild(0),MyModel);
		if(MyTree.getChildCount()>1){
			for(int i=1;i<MyTree.getChildCount();i++)
				Statements.add(StatementFactory.getStatement((CommonTree)MyTree.getChild(i),MyModel));
		}
	}

	public Variable execute(ArrayList Parameters,ArrayList ParentTable,Linker MyLinker,boolean function,boolean loop) throws Exception{
		try{
			MyModel.incrementIterationCounter(getLine());
			if(MyModel.getExceptionRaised()!=null)
				throw(MyModel.getExceptionRaised());

			HashMap MyTable=new HashMap();
			ParentTable.add(MyTable);
			
			if(!MyModel.getRunAllCode()){
				if(Statements.size()>0)
				while(Comparison.evaluate(ParentTable,MyLinker,function,loop).truthValue()){
					for(int i=0;i<Statements.size();i++){
							Statement TempStatement=(Statement)Statements.get(i);
							TempStatement.execute(Parameters,ParentTable,MyLinker,function,true);
					}
				}
			}else{//We must be attempting to run all code.
				Comparison.evaluate(ParentTable,MyLinker,function,loop);
				for(int i=0;i<Statements.size();i++){
						Statement TempStatement=(Statement)Statements.get(i);
						TempStatement.execute(Parameters,ParentTable,MyLinker,function,loop);
				}
			}
			
			ParentTable.remove(ParentTable.size()-1);

		}catch(Exception e){
			if(e instanceof ModelReturn)//Pop from the stack if we're here.
				ParentTable.remove(ParentTable.size()-1);
		
			if(e instanceof ModelBreak)
				return(null);
		
			if(!(e instanceof ModelError))
				throw(new ModelError("Runtime error on line "+getLine()+" when running while loop"));
			else
				throw(e);
		}
		return(null);
	}

}//END.
