package HackerLogin;
/**
Program.java

An abstract program used to handle common functionality between the various programs.
(Banking,Attacking,FTP,etc.).
*/
import Game.*;
import java.util.*;
import Hackscript.Model.*;
public class ToyProgram extends Program{
	private int MAX_ARRAY=64;
	private int MAX_ITERATIONS=128;
	private ArrayList InFloat=new ArrayList();
	private int countIF=0;
	private ArrayList OutFloat=new ArrayList();
	private int countOF=0;
	private ArrayList InString=new ArrayList();
	private int countIS=0;
	private ArrayList OutString=new ArrayList();
	private int countOS=0;
	private String script="";
	private ArrayList InInt=new ArrayList();
	private int countII=0;
	private ArrayList OutInt=new ArrayList();
	private int countOI=0;
	
	//The target state of the program.
	private ArrayList TargetString=null;
	private ArrayList TargetInt=null;
	private ArrayList TargetFloat=null;
	
	private boolean success=false;
	
	/**
	Constructor.
	*/
	public ToyProgram(String script,int MAX_ARRAY,int MAX_ITERATIONS){
		this.script=script;
		this.MAX_ARRAY=MAX_ARRAY;
		this.MAX_ITERATIONS=MAX_ITERATIONS;
	}
	
	/**
	Return whether or not this program passed the test.
	*/
	public boolean getSuccess(){
		return(success);
	}
	
	/**
	Add the target array of strings to be outputted.
	*/
	public void addTargetString(String s){
		if(TargetString==null)
			TargetString=new ArrayList();
		TargetString.add(s);
	}
	
	/**
	Add the target array of floats to be outputted.
	*/
	public void addTargetFloat(float f){
		if(TargetFloat==null)
			TargetFloat=new ArrayList();
		TargetFloat.add(new Float(f));
	}
	
	/**
	Add the target array of ints to be outputted.
	*/
	public void addTargetInt(int i){
		if(TargetInt==null)
			TargetInt=new ArrayList();
		TargetInt.add(new Integer(i));
	}
	
	/**
	Add a float to the inbound float array.
	*/
	public void addInFloat(float f){
		InFloat.add(new Float(f));
	}
	
	/**
	Add a float to the inbound float array.
	*/
	public void addOutFloat(float f){
		if(OutFloat.size()<MAX_ARRAY)
			OutFloat.add(new Float(f));
	}
	
	/**
	Add a string to the inbound string array.
	*/
	public void addInString(String s){
		InString.add(s);
	}
	
	/**
	Add a string to the inbound string array.
	*/
	public void addOutString(String s){
		if(OutString.size()<MAX_ARRAY)
			OutString.add(s);
	}
	
	/**
	Add an int to the inbound int array.
	*/
	public void addOutInt(int i){
		if(OutInt.size()<MAX_ARRAY)
			OutInt.add(new Integer(i));
	}
	
	/**
	Add an int to the inbound int array.
	*/
	public void addInInt(int i){
		InInt.add(new Integer(i));
	}
	
	/**
	Get a string from the inbound string array.
	*/
	public String getInString(){
		if(countIS<InString.size()){
			countIS++;
			return((String)InString.get(countIS-1));
		}
		return("");
	}
	
	/**
	Get a float from the inbound float array.
	*/
	public float getInFloat(){
		if(countIF<InFloat.size()){
			countIF++;
			return((float)((Float)InFloat.get(countIF-1)));
		}
		return(0.0f);
	}

	/**
	Get an int from the inbound int array.
	*/
	public int getInInt(){
		if(countII<InInt.size()){
			countII++;
			return((int)((Integer)InInt.get(countII-1)));
		}
		return(0);
	}
	
	/**
	Return the size of the array of inbound ints.
	*/
	public int getInputIntSize(){
		return(InInt.size());
	}
	
	/**
	Return the size of the array of inbound floats.
	*/
	public int getInputFloatSize(){
		return(InFloat.size());
	}
	
	/**
	Return the size of the array of inbound Strings.
	*/
	public int getInputStringSize(){
		return(InString.size());
	}

	/**
	installScript(HashMap Script);
	Installs a script on the various entrance points on this program.
	*/
	public void installScript(HashMap Script){}
	
	/**
	Execute the program with the RFC provided.
	*/	
	public void execute(ApplicationData MyApplicationData){
			
			try{
				ToyLinker HL=new ToyLinker(this,null);
				RunFactory.runCode(script,HL,MAX_ITERATIONS);
			}catch(Exception e){
				this.setError("Syntax error in challenge code compiler returned ["+e.getMessage()+"]");
			}			
			
			//Check to see whether this was successful.
			success=true;
			if(TargetFloat!=null)
				for(int i=0;i<TargetFloat.size();i++){
					if(i<OutFloat.size()){
						float f1=(Float)OutFloat.get(i);
						float f2=(Float)TargetFloat.get(i);
						if(Math.abs(f1-f2)>0.01){
							success=false;
							break;
						}
					}else{
						success=false;
					}
				}
			
			if(TargetString!=null)
				for(int i=0;i<TargetString.size();i++){
					if(i<OutString.size()){
						String f1=(String)OutString.get(i);
						String f2=(String)TargetString.get(i);
						if(!f1.equals(f2)){
							success=false;
							break;
						}
					}else{
						success=false;
					}
				}
				
			if(TargetInt!=null)
				for(int i=0;i<TargetInt.size();i++){
					if(i<OutInt.size()){
						int f1=(Integer)OutInt.get(i);
						int f2=(Integer)TargetInt.get(i);
						if(f1!=f2){
							success=false;
							break;
						}
					}else{
						success=false;
					}
				}
	}
	
	/**
	Returns the array of outputted ints.
	*/
	public Object[] getOutInt(){
		return(OutInt.toArray());
	}
	
	/**
	Returns the array of outputted doubles.
	*/
	public Object[] getOutDouble(){
		Object returnMe[]=new Object[OutFloat.size()];
		Iterator MyIterator=OutFloat.iterator();
		int i=0;
		while(MyIterator.hasNext()){
			returnMe[i]=new Double((float)((Float)MyIterator.next()));
			i++;
		}
		return(returnMe);
	}
	
	/**
	Returns the array of outputted strings.
	*/
	public Object[] getOutString(){
		return(OutString.toArray());
	}
	
	/**
	Returns the keys that should be parsed from the save file given this program type.
	*/
	public String[] getTypeKeys(){return(null);}
	
	/**
	Return a hashmap representation of the code.
	*/
	public HashMap getContent(){return(null);}
	
	/**
	Output the class data in XML format.
	*/
	public String outputXML(){return("");}
}
