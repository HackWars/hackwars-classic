package Hackscript.Model;
/*
Porgrammer: Ben Coe.(2008)<br />

An Exception used by our model.
*/

import org.antlr.runtime.*;
import java.util.*;
import java.io.*;
import org.antlr.runtime.tree.*;
import org.antlr.runtime.Token;

public class ModelReturn extends ModelError{
	private Variable MyVariable=null;
	public ModelReturn(Variable MyVariable){
		super("");
		this.MyVariable=MyVariable;
	}
	public Variable getVariable(){
		return(MyVariable);
	}
}//END.
