/*
Programmer: Benjamin E. Coe 2008

Description: The linker used to execute external functions.
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

public abstract class Linker{
	private Model ParentModel=null;
    public abstract Variable runFunction(String name, ArrayList parameters);
	public void killExecution(){
		if(ParentModel!=null)
			ParentModel.killExecution();
	}
	public void setParentModel(Model ParentModel){
		this.ParentModel=ParentModel;
	}
}//END.
