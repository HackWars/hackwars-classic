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

public class IfStatement extends Statement{
	private Expression IfExpression=null;
	private ArrayList IfStatements=new ArrayList();
	private ArrayList ElseStatements=new ArrayList();
	private Model MyModel=null;

	public IfStatement(CommonTree MyTree,Model MyModel){
		setLine(MyTree.getLine());
		this.MyModel=MyModel;
		
		IfExpression=ExpressionFactory.getExpression((CommonTree)MyTree.getChild(0),MyModel);
		int i=1;
		while(i<MyTree.getChildCount()&&MyTree.getChild(i).getType()!=Constants.ELSEPART){
			IfStatements.add(StatementFactory.getStatement((CommonTree)MyTree.getChild(i),MyModel));
			i++;
		}
	
		if(i<MyTree.getChildCount())
			if(MyTree.getChild(i).getChildCount()>0){
				MyTree=(CommonTree)MyTree.getChild(i);
				for(i=0;i<MyTree.getChildCount();i++){
					ElseStatements.add(StatementFactory.getStatement((CommonTree)MyTree.getChild(i),MyModel));
				}
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
				if(IfExpression.evaluate(ParentTable,MyLinker,function,loop).truthValue()){
					for(int i=0;i<IfStatements.size();i++){
						Statement TempStatement=(Statement)IfStatements.get(i);
						TempStatement.execute(Parameters,ParentTable,MyLinker,function,loop);
					}
				}else{
					for(int i=0;i<ElseStatements.size();i++){
						Statement TempStatement=(Statement)ElseStatements.get(i);
						TempStatement.execute(Parameters,ParentTable,MyLinker,function,loop);
					}
				}
			}else{//We must be running all code.
				IfExpression.evaluate(ParentTable,MyLinker,function,loop).truthValue();
				for(int i=0;i<IfStatements.size();i++){
					Statement TempStatement=(Statement)IfStatements.get(i);
					TempStatement.execute(Parameters,ParentTable,MyLinker,function,loop);
				}
				for(int i=0;i<ElseStatements.size();i++){
					Statement TempStatement=(Statement)ElseStatements.get(i);
					TempStatement.execute(Parameters,ParentTable,MyLinker,function,loop);
				}
			}
			
			ParentTable.remove(ParentTable.size()-1);

		}catch(Exception e){
			if((e instanceof ModelReturn)|(e instanceof ModelBreak))
				ParentTable.remove(ParentTable.size()-1);
		
			if(!(e instanceof ModelError))
				throw(new ModelError("Runtime error on line "+getLine()+" when running if statement"));
			else
				throw(e);
		}
		return(null);
	}

}//END.
