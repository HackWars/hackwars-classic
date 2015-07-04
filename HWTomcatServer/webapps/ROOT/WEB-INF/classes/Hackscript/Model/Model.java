/*
Programmer: Benjamin E. Coe 2008

Description: Walks the tree building the model.
*/
package Hackscript.Model;

import org.antlr.runtime.*;
import org.antlr.runtime.debug.*;
import java.util.*;
import Hackscript.*;
import java.io.*;
import org.antlr.runtime.tree.*;
import org.antlr.runtime.Token;

public class Model{
	private ArrayList Declarations=new ArrayList();//Global declarations.
	private HashMap Functions=new HashMap();//The functions that have been defined.
	private HashMap CustomFunctions=new HashMap();//The HashMap of user defined functions.
	private Exception CompilerError=null;//The error messages.
	private static HashMap ErrorReplacement=null;
	private int iterationCounter=0;
	private int maximumIteration=5000;
	private Exception RaisedException=null;
	private boolean runAllCode=false;
	/**
	Increment the counter used to prevent infinite loops.
	*/
	public void incrementIterationCounter(int line) throws Exception{
		iterationCounter++;
		if(iterationCounter>maximumIteration){
			RaisedException=new ModelError("Runtime error on line "+line+" maximum operations reached.");
			throw(RaisedException);
		}
	}
	
	/**
	Set the maximum iterations.
	*/
	public void setMaximumIterations(int maximumIteration){
		this.maximumIteration=maximumIteration;
	}
	
	/**
	Kill the current execution of code.
	*/
	public void killExecution(){
		RaisedException=new ModelError("Runtime error execution terminated");
	}
	
	/**
	Run all code determines whether or not we should evaluate if constructs.
	*/
	public boolean getRunAllCode(){
		return(runAllCode);
	}
	
	/**
	Run all code determines whether or not we should evaluate if constructs.
	*/
	public void setRunAllCode(boolean runAllCode){
		this.runAllCode=runAllCode;
	}
	
	/**
	Get whether or not an exception has been raised.
	*/
	public Exception getExceptionRaised(){
		return(RaisedException);
	}
	
	/**
	Build the replacement table for error messages.
	*/
	public static void buildErrorReplacement(){
		if(ErrorReplacement==null){
			ErrorReplacement=new HashMap();
			ErrorReplacement.put("COMMA",",");
			ErrorReplacement.put("TYPE","int,float,boolean,string");
			ErrorReplacement.put("STRING_TYPE","string");
			ErrorReplacement.put("MINUS","-");
			ErrorReplacement.put("SIGN_EXPRESSION","+,-");
			ErrorReplacement.put("DOUBLE","floating point number");
			ErrorReplacement.put("INCREMENT_ASSIGN","+=");
			ErrorReplacement.put("ELSEPART","else{}");
			ErrorReplacement.put("PROCEDURE","procedure(){}");
			ErrorReplacement.put("BOOLEAN_LITERAL","true,false");
			ErrorReplacement.put("THEN","then{}");
			ErrorReplacement.put("LBRACKET","[");
			ErrorReplacement.put("INCREMENT","++");
			ErrorReplacement.put("INTEGER_TYPE","int");
			ErrorReplacement.put("MOD","%");
			ErrorReplacement.put("BECOMES","=");
			ErrorReplacement.put("VOID_TYPE","void");
			ErrorReplacement.put("OR","||");
			ErrorReplacement.put("PROGRAM","code body");
			ErrorReplacement.put("NEWLINE","newline");
			ErrorReplacement.put("PROCEDURE_PARAMETERS","procedure parameters");
			ErrorReplacement.put("NOT_EQUALS","!=");
			ErrorReplacement.put("AND","&&");
			ErrorReplacement.put("DECLARATION","declaration statement");
			ErrorReplacement.put("FUNCTION","function definition");
			ErrorReplacement.put("GTE",">=");
			ErrorReplacement.put("STRING_LITERAL","string value");
			ErrorReplacement.put("RBRACKET","]");
			ErrorReplacement.put("RPAREN",")");
			ErrorReplacement.put("LPAREN","(");
			ErrorReplacement.put("WHITE_SPACE","white space character");
			ErrorReplacement.put("ML_COMMENT","multi-line comment");
			ErrorReplacement.put("PLUS","+");
			ErrorReplacement.put("SBRIGHT","}");
			ErrorReplacement.put("COMMENT_BRANCH","commet branch");
			ErrorReplacement.put("INTEGER_LITERAL","integer number");
			ErrorReplacement.put("WHILE_STATEMENT","while{}");
			ErrorReplacement.put("DECREMENT","--");
			ErrorReplacement.put("TIMES","*");
			ErrorReplacement.put("ARRAY_DECLARE","array declaration");
			ErrorReplacement.put("DECREMENT_ASSIGN","-=");
			ErrorReplacement.put("WHILE","while(){}");
			ErrorReplacement.put("POWER","^");
			ErrorReplacement.put("BOOLEAN_TYPE","boolean");
			ErrorReplacement.put("LT","<");
			ErrorReplacement.put("GT",">");
			ErrorReplacement.put("COMMENT","comment");
			ErrorReplacement.put("MULTIPLY_ASSIGN","*=");
			ErrorReplacement.put("POWER_EXPRESSION","^");
			ErrorReplacement.put("FLOAT_TYPE","float");
			ErrorReplacement.put("FOR_STATEMENT","for(){}");
			ErrorReplacement.put("SBLEFT","{");
			ErrorReplacement.put("LTE","<=");
			ErrorReplacement.put("EQUALS","==");
			ErrorReplacement.put("ELSE","else{}");
			ErrorReplacement.put("SEMICOLON",";");
			ErrorReplacement.put("IF","if(){}");
			ErrorReplacement.put("EOF","end of file");
			ErrorReplacement.put("FOR","for(){}");
			ErrorReplacement.put("ARRAY_IDENTIFIER","variable[]");
			ErrorReplacement.put("DIV","/");
			ErrorReplacement.put("IFPART","if(){}");
			ErrorReplacement.put("IDENTIFIER","identifier");
		}
	}
	
	/**
	Create the model by walking the tree.
	*/
	public void setTree(CommonTree MyTree) throws Exception{
		buildErrorReplacement();
		
		if(CompilerError!=null){//Check for compiling errors.
		
			if(CompilerError instanceof RecognitionException){

				if(CompilerError instanceof MismatchedTokenException){
					MismatchedTokenException MyRecognitionException=(MismatchedTokenException)CompilerError;
					String ErrorMessage="Compiling error ";
					ErrorMessage+="line "+MyRecognitionException.line+":"+MyRecognitionException.charPositionInLine;
					
					if(!(MyRecognitionException.expecting<0||MyRecognitionException.expecting>=CParser.tokenNames.length)){//Did we find a token?
						ErrorMessage+=" mismatched input "+MyRecognitionException.token.getText()+" expecting '"+ErrorReplacement.get(CParser.tokenNames[MyRecognitionException.expecting])+"'";
						throw((Exception)new ModelError(ErrorMessage));
					}
				}
				RecognitionException MyRecognitionException=(RecognitionException)CompilerError;
				String ErrorMessage="line "+MyRecognitionException.line+":"+MyRecognitionException.charPositionInLine;
				throw(new ModelError("Compiling error "+ErrorMessage));

			}
			throw((Exception)new ModelError("Compiling error"));
		}
	
		if ( MyTree != null ) {
			for ( int i = 0; i < MyTree.getChildCount(); i++ ) {
				CommonTree Child=(CommonTree)MyTree.getChild(i);
				Statement MyStatement=StatementFactory.getStatement(Child,this);
				if(MyStatement instanceof DeclarationStatement)
					Declarations.add(MyStatement);
			}
		}
	}
	
	
	/**
	Execute the model we parsed from the tree.
	*/
	public void execute(Linker MyLinker) throws Exception{
	
		if(MyLinker!=null)
			MyLinker.setParentModel(this);
				
		RaisedException=null;
		iterationCounter=0;
		try{
			HashMap SymbolTable=new HashMap();
			ArrayList ParentTable=new ArrayList();
			ParentTable.add(SymbolTable);
			for(int i=0;i<Declarations.size();i++)
				((Statement)Declarations.get(i)).execute(null,ParentTable,MyLinker,false,false);
				
			Statement Entry=(Statement)Functions.get("main");
			if(Entry!=null)
				Entry.execute(null,ParentTable,MyLinker,false,false);
			else
				((Statement)Functions.get("Main")).execute(null,ParentTable,MyLinker,false,false);
		}catch(Exception e){		
			if(e instanceof ModelError)
				throw(e);
			else throw(new ModelError("Runtime error"));
		}
	}
	
	/**
	Register a function in the lookup table.
	*/
	public void registerFunction(String FunctionName,Statement Function){
		Functions.put(FunctionName,Function);
		CustomFunctions.remove(FunctionName);
	}
	
	/**
	Register a procedure in the lookup table.
	*/
	public void registerProcedure(String FunctionName){
		if(Functions.get(FunctionName)==null&&!FunctionName.equals("return")&&!FunctionName.equals("end")&&!FunctionName.equals("break")){
			int count=1;
			Integer temp;
			if((temp=(Integer)CustomFunctions.get(FunctionName))!=null){
				count=count+(int)temp;
			}
			CustomFunctions.put(FunctionName,new Integer(count));
		}
	}
	
	/**
	Get the array list of user defined functions.
	*/
	public ArrayList getCustomFunctions(){
		Iterator MyIterator=CustomFunctions.entrySet().iterator();
		ArrayList returnMe=new ArrayList();
		while(MyIterator.hasNext()){
			Map.Entry MyEntry=(Map.Entry)MyIterator.next();
			int size=(Integer)MyEntry.getValue();
			String function=(String)MyEntry.getKey();
			for(int i=0;i<size;i++)
				returnMe.add(function);
		}
		return(returnMe);
	}

	/**
	Get a function based on the name.
	*/
	public Statement getFunction(String FunctionName){
		return((Statement)Functions.get(FunctionName));
	}

	/**
	Add compiling error.
	*/
	public void addCompilerError(RecognitionException e){
		CompilerError=e;
	}
}//END.
