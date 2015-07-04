package Game.RunChallenge;
/**
A singleton to run a challenge.
*/
import java.util.ArrayList;
import java.util.HashMap;
import Hackscript.Model.*;
import Server.*;
import java.net.URL;
import HackerLogin.*;

public class ChallengeRunner{
	private static ChallengeRunner instance=null;
	
	/**
	Get an instance of this singleton.
	*/
	public static ChallengeRunner getInstance(){
		if(instance==null){
			instance=new ChallengeRunner();
		}
		return(instance);
	}

	public static final String Challenge[]={"strlen","sqrt","abs","ln","atan","acos","asin","tan","cos","sin","getE","getPI","substr","getInputString","getInputStringCount","setOutputString","getInputFloat","getInputFloatCount","setOutputFloat","getInputInt","getInputIntCount","setOutputInt","equal","printf","rand","intValue","floatValue","indexOf","parseFloat","parseInt","replaceAll","split","length"};

	/**
	Runs a challenge script application.
	*/
	public synchronized HashMap runToyProblem(String Source,Integer Iterations,Object OInputFloat[],Object OInputString[],Object OInputInteger[],Object OTargetFloat[],Object OTargetString[],Object OTargetInteger[]){
		HashMap returnMe=new HashMap();
		String error="";
		
		Double InputFloat[]=null;
		try{
			InputFloat=new Double[OInputFloat.length];
			for(int i=0;i<OInputFloat.length;i++)
				InputFloat[i]=(Double)OInputFloat[i];
		}catch(Exception e){}
		
		String InputString[]=null;
		try{
		InputString=new String[OInputString.length];
		for(int i=0;i<OInputString.length;i++)
			InputString[i]=(String)OInputString[i];
		}catch(Exception e){}

		Integer InputInteger[]=null;
		try{
		InputInteger=new Integer[OInputInteger.length];
		for(int i=0;i<OInputInteger.length;i++)
			InputInteger[i]=(Integer)OInputInteger[i];
		}catch(Exception e){}

		Double TargetFloat[]=null;
		try{
		TargetFloat=new Double[OTargetFloat.length];
		for(int i=0;i<OTargetFloat.length;i++)
			TargetFloat[i]=(Double)OTargetFloat[i];
		}catch(Exception e){}

		String TargetString[]=null;
		try{
		TargetString=new String[OTargetString.length];
		for(int i=0;i<OTargetString.length;i++)
			TargetString[i]=(String)OTargetString[i];
		}catch(Exception e){}

		Integer TargetInteger[]=null;
		try{
		TargetInteger=new Integer[OTargetInteger.length];
		for(int i=0;i<OTargetInteger.length;i++)
			TargetInteger[i]=(Integer)OTargetInteger[i];
		}catch(Exception e){}

		
		ToyProgram TP=new ToyProgram(Source,64,(int)Iterations);
		ToyLinker HL=new ToyLinker(TP,null);
		
		//Add the inputs.
		if(InputFloat!=null)
			for(int i=0;i<InputFloat.length;i++)
				TP.addInFloat((float)((double)InputFloat[i]));
		if(InputString!=null)
			for(int i=0;i<InputString.length;i++){
				TP.addInString(InputString[i]);
			}
		if(InputInteger!=null)
			for(int i=0;i<InputInteger.length;i++){
				TP.addInInt((int)InputInteger[i]);
			}
				
		//Add the targets.
		if(TargetFloat!=null)
			for(int i=0;i<TargetFloat.length;i++)
				TP.addTargetFloat((float)((double)TargetFloat[i]));
		if(TargetString!=null)
			for(int i=0;i<TargetString.length;i++){
				TP.addTargetString(TargetString[i]);
			}
		if(TargetInteger!=null)
			for(int i=0;i<TargetInteger.length;i++)
				TP.addTargetInt((int)TargetInteger[i]);
				
		boolean found=false;
		try{
			ArrayList Functions=RunFactory.getCodeList(Source);
			for(int i=0;i<Functions.size();i++){
				String name=(String)Functions.get(i);
				found=false;
				for(int ii=0;ii<Challenge.length;ii++){
					if(Challenge[ii].equals(name))
						found=true;
				}
				if(found==false){
					TP.setError("Function "+name+" not found.");
					break;
				}
			}
		}catch(Exception e){
			TP.setError("Syntax error in challenge code compiler returned ["+e.getMessage()+"]");
		}
		if(found){//Actually run stuff.
			try{
				TP.execute(null);
			}catch(Exception e){
				System.out.println("this happened?");
				TP.setError("Syntax error in challenge code compiler returned ["+e.getMessage()+"]");
			}
		}
					
		returnMe.put("outint",TP.getOutInt());
		returnMe.put("outstring",TP.getOutString());
		returnMe.put("outdouble",TP.getOutDouble());
		returnMe.put("success",new Boolean(TP.getSuccess()));
		returnMe.put("error",TP.getError());
		
		return(returnMe);
	}
}

