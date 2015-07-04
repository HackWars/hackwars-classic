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

public class ModelBreak extends ModelError{
	public ModelBreak(){
		super("");
	}
}//END.
