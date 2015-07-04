/*
Programmer: Benjamin E. Coe 2008

Description: Used to provide compatibility with Alex's code.
*/
package Hackscript.Model;

//import Hacktendo.*;

import org.antlr.runtime.*;
import org.antlr.runtime.debug.*;
import java.util.Stack;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.io.*;
import org.antlr.runtime.tree.*;
import org.antlr.runtime.Token;

public class RunFactory{
	/**
	Invokes the compiler and runs the code ignoring loop logic, etc.
	*/
	public static void runAllCode(String Script,Linker MyLinker) throws Exception{
		try{
			Script=Script+"\nend();";//A fix for the context free grammar.
			Model MyModel=ParserFactory.getModel(Script);
			MyModel.setRunAllCode(true);
			MyModel.execute(MyLinker);
		}catch(Exception e){
			if(e instanceof ModelError)
				throw(e);
			else
				throw(new ModelError("Runtime error."));
		}
	}
	
	/**
	Invokes the compiler and returns a list of code.
	*/
	public static ArrayList getCodeList(String Script) throws Exception{
		try{
			Script=Script+"\nend();";//A fix for the context free grammar.
			Model MyModel=ParserFactory.getModel(Script);
			return(MyModel.getCustomFunctions());
		}catch(Exception e){
			if(e instanceof ModelError)
				throw(e);
			else
				throw(new ModelError("Runtime error."));
		}
	}

	/**
	Invokes the compiler and executes the code.
	*/
	public static void runCode(String Script,Linker MyLinker,int maximumIterations) throws Exception{
		try{
			Script=Script+"\nend();";//A fix for the context free grammar.
			Model MyModel=ParserFactory.getModel(Script);
			MyModel.setMaximumIterations(maximumIterations);
			MyModel.execute(MyLinker);
		}catch(Exception e){
			if(e instanceof ModelError)
				throw(e);
			else
				throw(new ModelError("Runtime error."));
		}
	}
	
	/**
	Invokes the compiler and returns the model.
	*/
	public static Model compileCode(String Script) throws Exception{
		try{
			Script=Script+"\nend();";//A fix for the context free grammar.
			Model MyModel=ParserFactory.getModel(Script);
			return(MyModel);
		}catch(Exception e){
			if(e instanceof ModelError)
				throw(e);
			else
				throw(new ModelError("Runtime error."));
		}
	}
	
	/**
	Invokes the compiler and returns the model.
	*/
	public static void runCode(Model MyModel,Linker MyLinker) throws Exception{
		try{
			MyModel.execute(MyLinker);
		}catch(Exception e){
			if(e instanceof ModelError)
				throw(e);
			else
				throw(new ModelError("Runtime error."));
		}
	}
	
	
	/**
	Invokes the compiler and returns a list of the functions found within it.
	*/
	public static ArrayList runAllCode(String Script) throws Exception{
		try{
			Script=Script+"\nend();";//A fix for the context free grammar.
			Model MyModel=ParserFactory.getModel(Script);
			return(MyModel.getCustomFunctions());
		}catch(Exception e){
			if(e instanceof ModelError)
				throw(e);
			else
				throw(new ModelError("Runtime error."));
		}
	}
}//END.
