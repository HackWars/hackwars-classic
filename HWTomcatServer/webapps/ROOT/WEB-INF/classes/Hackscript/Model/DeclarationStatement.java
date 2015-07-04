/*
Programmer: Benjamin E. Coe 2008

DeclarationStatement.java

Description: A statement in the scripting language for declaring new variables, either: x=y or type x=y;
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

public class DeclarationStatement extends Statement{

	private boolean isArray=false;//Is it an array we are assigning?
	private Expression ArrayExpression=null;//If it is an array dereference it with this expression.
	private String TypeString=null;//The string representing type {float,string,int,etc}
	private String VariableName="";//Name of the variable to assign.
	private int DeclarationType=Constants.BECOMES;//The type of declaration, increment, decrement, etc.
	private Expression MyExpression=null;//The expression to assign it to.
	private static final int MAX_ARRAY=128;//The maximum array size.
	
	private Model MyModel=null;//The underlying model overseeing the parse.
	private String extraMessage="";//For more verbose error messages.

	public DeclarationStatement(CommonTree MyTree,Model MyModel){	
		setLine(MyTree.getLine());
		this.MyModel=MyModel;
	
		if(MyTree.getChild(1).getType()==Constants.DECLARATION){//Must be declaring something for the first time.
			TypeString=MyTree.getChild(0).toString();
			CommonTree DeclarationPart=(CommonTree)MyTree.getChild(1);
			if(DeclarationPart.getChild(0).getType()==Constants.ARRAY_IDENTIFIER){
				isArray=true;
				VariableName=DeclarationPart.getChild(0).getChild(0).toString();
			}else
				VariableName=DeclarationPart.getChild(0).toString();
			if(DeclarationPart.getChildCount()>2){
				DeclarationType=DeclarationPart.getChild(1).getType();
				MyExpression=ExpressionFactory.getExpression((CommonTree)DeclarationPart.getChild(2),MyModel);
			}
		}else{
			if(MyTree.getChildCount()==2){//Must be increment or decrement.
				if(MyTree.getChild(0).getType()==Constants.ARRAY_IDENTIFIER){
					isArray=true;
					VariableName=MyTree.getChild(0).getChild(0).toString();
					if(MyTree.getChild(1).getChildCount()>0){//Maybe we're using this array badly?
						ArrayExpression=ExpressionFactory.getExpression((CommonTree)MyTree.getChild(0).getChild(1),MyModel);
					}
				}else
					VariableName=MyTree.getChild(0).toString();
				
				DeclarationType=MyTree.getChild(1).getType();
			}else{
				if(MyTree.getChild(0).getType()==Constants.ARRAY_IDENTIFIER){
					isArray=true;
					VariableName=MyTree.getChild(0).getChild(0).toString();
					if(MyTree.getChild(0).getChildCount()>1){//Maybe we're using this array badly?
						ArrayExpression=ExpressionFactory.getExpression((CommonTree)MyTree.getChild(0).getChild(1),MyModel);
					}
				}else
					VariableName=MyTree.getChild(0).toString();
								
				DeclarationType=MyTree.getChild(1).getType();
				MyExpression=ExpressionFactory.getExpression((CommonTree)MyTree.getChild(2),MyModel);
			}
		}
	}

	public Variable execute(ArrayList Parameters,ArrayList ParentTable,Linker MyLinker,boolean function,boolean loop) throws Exception{
		extraMessage="";
		try{
			//Make sure that we haven't run too many iterations.
			MyModel.incrementIterationCounter(getLine());
			if(MyModel.getExceptionRaised()!=null)
				throw(MyModel.getExceptionRaised());
		
			HashMap MyTable=(HashMap)ParentTable.get(ParentTable.size()-1);
			Object MyObject=null;
			Variable MyVariable=null;
			
			//Put the new variable in the HashTable or fetch an existing one.
			if(TypeString!=null){
				
				if(TypeString.equals("string")||TypeString.equals("String")){
					MyVariable=new TypeString("");
				}else if(TypeString.equals("float")){
					MyVariable=new TypeFloat(0.0f);
				}else if(TypeString.equals("int")){
					MyVariable=new TypeInteger(0);
				}else if(TypeString.equals("boolean")){
					MyVariable=new TypeBoolean(false);
				}else if(TypeString.equals("array")){
					MyVariable=new TypeArray(new ArrayList());
				}
				
				MyObject=MyVariable;
				
				if(!isArray)
					MyTable.put(VariableName,MyVariable);
				else{				
					ArrayList MyArray=new ArrayList();
					MyArray.add(MyVariable);
					MyTable.put(VariableName,MyArray);
					MyObject=MyArray;
				}

			}else{//It must already exist.
						
				MyObject=MyTable.get(VariableName);
				if(MyObject==null){
					int i=ParentTable.size()-1;
					while(i>=0){
						HashMap TempTable=(HashMap)ParentTable.get(i);
						MyObject=TempTable.get(VariableName);
						if(MyObject!=null)
							break;
						i--;
					}
				}
				
				if(MyObject==null){
					extraMessage="variable not yet declared";
					throw(new Exception());
				}
				
				if(MyObject instanceof ArrayList)
					isArray=true;
			}
		
			if(isArray){//Are we dealing with an array?
							
				int ArrayIndex=-1;//Get the dereferenced index of the array.
				if(ArrayExpression!=null){//We are dereferncing the array.
					ArrayIndex=ArrayExpression.evaluate(ParentTable,MyLinker,function,loop).intValue();
				}else{//We must be assigning an entire array.
					if(MyExpression!=null&&MyExpression instanceof ArrayDeclareExpression){
						((ArrayDeclareExpression)MyExpression).setValue(VariableName);
						MyExpression.evaluate(ParentTable,MyLinker,function,loop); 
					}else if(MyExpression!=null){
						Object Result=MyExpression.evaluate(ParentTable,MyLinker,function,loop);
						if(Result instanceof ArrayList){
							MyTable.put(VariableName,Result);
							return(null);
						}else if(Result instanceof TypeArray){
							MyTable.put(VariableName,((Variable)Result).getRawValue());
							return(null);
						}else{						
							extraMessage="Can not assign non-array value to an array";
							throw(new Exception());
						}
					}
					return(null);
				}
					
				if(ArrayIndex<0||ArrayIndex>MAX_ARRAY){
					extraMessage="Array index out of bounds, note that max array size is 128.";
					throw(new Exception());
				}
				
				Variable ArrayCheck=(Variable)((ArrayList)MyObject).get(0);
				while(((ArrayList)MyObject).size()<ArrayIndex+1){
					if(ArrayCheck instanceof TypeFloat){
						((ArrayList)MyObject).add(new TypeFloat(0.0f));
					}else if(ArrayCheck instanceof TypeInteger){
						((ArrayList)MyObject).add(new TypeInteger(0));
					}else if(ArrayCheck instanceof TypeBoolean){
						((ArrayList)MyObject).add(new TypeBoolean(false));
					}else if(ArrayCheck instanceof TypeString){
						((ArrayList)MyObject).add(new TypeString(""));
					}else if(ArrayCheck instanceof TypeArray){
						((ArrayList)MyObject).add(new TypeArray(new ArrayList()));
					}
				}
				
				MyVariable=(Variable)((ArrayList)MyObject).get(ArrayIndex);
					
			}else{
				MyVariable=(Variable)MyObject;
			}
				
			if(DeclarationType==Constants.INCREMENT){//++
				MyVariable.increment();
				return(MyVariable);
			}
			
			if(DeclarationType==Constants.DECREMENT){//--
				MyVariable.decrement();
				return(MyVariable);
			}
			
			if(MyExpression!=null){// =,+=,*=,-=
			
				if(DeclarationType==Constants.BECOMES){
					MyVariable.setValue(MyExpression.evaluate(ParentTable,MyLinker,function,loop));
					return(MyVariable);
				}
			
				if(DeclarationType==Constants.MULTIPLY_ASSIGN){
					MyVariable.setValue(MyVariable.multiply(MyExpression.evaluate(ParentTable,MyLinker,function,loop)));
					return(MyVariable);
				}
			
				if(DeclarationType==Constants.INCREMENT_ASSIGN){
					MyVariable.setValue(MyVariable.add(MyExpression.evaluate(ParentTable,MyLinker,function,loop)));
					return(MyVariable);
				}
				
				if(DeclarationType==Constants.DECREMENT_ASSIGN){
					MyVariable.setValue(MyVariable.subtract(MyExpression.evaluate(ParentTable,MyLinker,function,loop)));
					return(MyVariable);
				}
			
			}
		}catch(Exception e){
								
			if(e instanceof ModelError)
				throw(e);
			else
				throw(new ModelError("Runtime error on line "+getLine()+" in declaration of '"+VariableName+"'. "+extraMessage));
		}
	
		return(null);
	}

}//END.
