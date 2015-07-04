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

public class ForStatement extends Statement{
	private Statement InitialDeclaration=null;
	private Expression Comparison=null;
	private Statement LoopDeclaration=null;
	private ArrayList Statements=new ArrayList();
	private Model MyModel=null;

	public ForStatement(CommonTree MyTree,Model MyModel){
		setLine(MyTree.getLine());
		this.MyModel=MyModel;
		
		InitialDeclaration=StatementFactory.getStatement((CommonTree)MyTree.getChild(0),MyModel);
		Comparison=ExpressionFactory.getExpression((CommonTree)MyTree.getChild(1),MyModel);
		LoopDeclaration=StatementFactory.getStatement((CommonTree)MyTree.getChild(2),MyModel);
		if(MyTree.getChildCount()>3){
			for(int i=3;i<MyTree.getChildCount();i++)
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
			Variable Start=InitialDeclaration.execute(Parameters,ParentTable,MyLinker,function,loop);
			
			if(!MyModel.getRunAllCode()){
				if(Statements.size()>0)
				while(Comparison.evaluate(ParentTable,MyLinker,function,loop).truthValue()){
					for(int i=0;i<Statements.size();i++){
							Statement TempStatement=(Statement)Statements.get(i);
							TempStatement.execute(Parameters,ParentTable,MyLinker,function,true);
					}
					LoopDeclaration.execute(Parameters,ParentTable,MyLinker,function,loop);
				}
			}else{//We are running the special case where we evaluate all code.
				Comparison.evaluate(ParentTable,MyLinker,function,loop).truthValue();
				for(int i=0;i<Statements.size();i++){
						Statement TempStatement=(Statement)Statements.get(i);
						TempStatement.execute(Parameters,ParentTable,MyLinker,function,loop);
				}
				LoopDeclaration.execute(Parameters,ParentTable,MyLinker,function,loop);
			}
			
			ParentTable.remove(ParentTable.size()-1);

		}catch(Exception e){
			if((e instanceof ModelReturn))
				ParentTable.remove(ParentTable.size()-1);
		
			if(e instanceof ModelBreak)
				return(null);
						
			if(!(e instanceof ModelError))
				throw(new ModelError("Runtime error on line "+getLine()+" when running for loop"));
			else
				throw(e);
		}
		return(null);
	}

}//END.
