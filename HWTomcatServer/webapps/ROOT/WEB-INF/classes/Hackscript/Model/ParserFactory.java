/*
Porgrammer: Ben Coe.(2007)<br />

Description: Returns a model parsed from the string provided.
*/
package Hackscript.Model;
import Hackscript.*;

import org.antlr.runtime.*;
import org.antlr.runtime.debug.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.io.*;
import org.antlr.runtime.tree.*;
import org.antlr.runtime.Token;

public class ParserFactory{

	static final TreeAdaptor adaptor = new CommonTreeAdaptor() {
		public Object create(Token payload) {
			return new CommonTree(payload);
		}
	};
	
	public synchronized static final Model getModel(String Data) throws Exception{
		try{
			ByteArrayInputStream F=new ByteArrayInputStream(Data.getBytes());
			ANTLRInputStream CS=new ANTLRInputStream(F);
			CLexer E=new CLexer(CS);
			TokenRewriteStream TS = new TokenRewriteStream(E);
			Model MyModel=new Model();
			CParser P=new CParser(TS,MyModel);
			P.setTreeAdaptor(adaptor);
			CParser.start_return ret=P.start();
			MyModel.setTree((CommonTree)ret.getTree());	
			return(MyModel);
		}catch(Exception e){
			throw(e);
		}
	}

}//END.
