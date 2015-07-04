/*
 * structInfo.java
 *
 * Created on March 10, 2007, 10:40 AM
 *
 * By Alexander T Morrsion
 *
 */

package HackerLogin;
import Game.*;
import java.util.ArrayList;
import Hackscript.Model.*;

/**
 * By Alexander Morrison
 */
public class ToyLinker extends Linker{
    
       
    public int codeCounter = 0;
    private ComputerHandler MyComputerHandler=null;
	private Program MyProgram=null;
    private Object theLinker = null;
    
    /** Creates a new instance of structInfo */
    public ToyLinker(Program MyProgram,ComputerHandler MyComputerHandler) {
		this.MyProgram=MyProgram;
		this.MyComputerHandler=MyComputerHandler;
        this.theLinker = theLinker;
        codeCounter = 0;
    }
	
	/**
	Escape regex.
	*/
	public String regexEscape(String data){
		data=data.replaceAll("\\\\", "\\\\\\\\");
		data=data.replaceAll("\\.", "\\\\.");
		data=data.replaceAll("\\$", "\\\\\\$");
		data=data.replaceAll("\\^", "\\\\^");
		data=data.replaceAll("\\{", "\\\\{");
		data=data.replaceAll("\\[", "\\\\[");
		data=data.replaceAll("\\(", "\\\\(");
		data=data.replaceAll("\\|", "\\\\|");
		data=data.replaceAll("\\)", "\\\\)");
		data=data.replaceAll("\\*", "\\\\*");
		data=data.replaceAll("\\+", "\\\\+");
		data=data.replaceAll("\\?", "\\\\?"); 
		return(data);
	}
    
    public Variable runFunction(String name, ArrayList parameters){
		Object result=null;
		ToyProgram TP=(ToyProgram)MyProgram;

		if(TP.getError().length()==0){
		
		try{
		
		//Compare two strings which are passed in.
		if(name.equals("equal")){
			if(parameters.size()!=2)
				MyProgram.setError("\"equal\" takes two strings and returns whether they are equal.");
			else if(!(parameters.get(0) instanceof TypeString)||!(parameters.get(1) instanceof TypeString))
				MyProgram.setError("\"equal\" takes two strings and returns whether they are equal.");
			
			//Compare two strings which are passed in.
			String s1="";
			String s2="";
			if(parameters.get(0) instanceof TypeString)
				s1=((TypeString)parameters.get(0)).getStringValue();
			if(parameters.get(1) instanceof TypeString)
				s2=((TypeString)parameters.get(1)).getStringValue();
			return(new TypeBoolean(s1.equals(s2)));
		}
		
		//Compare two strings which are passed in.
		if(name.equals("strcmp")){
			if(parameters.size()!=2)
				MyProgram.setError("\"strcmp(string,string)\" Returns an integer less than, equal to or greater than zero depending on whether the first string is less than, equal to or greater than the second string.");
			else if(!(parameters.get(0) instanceof TypeString)||!(parameters.get(1) instanceof TypeString))
				MyProgram.setError("\"strcmp(string,string)\" Returns an integer less than, equal to or greater than zero depending on whether the first string is less than, equal to or greater than the second string.");
			
			//Compare two strings which are passed in.
			String s1="";
			String s2="";
			if(parameters.get(0) instanceof TypeString)
				s1=((TypeString)parameters.get(0)).getStringValue();
			if(parameters.get(1) instanceof TypeString)
				s2=((TypeString)parameters.get(1)).getStringValue();
			return(new TypeInteger(s1.compareTo(s2)));
		}
		
		//Return the length of a string that is passed in.
		if(name.equals("strlen")){
			if(parameters.size()!=1)
				MyProgram.setError("\"strlen(string)\" Returns an integer representing the length of a string.");
			else if(!(parameters.get(0) instanceof TypeString))
				MyProgram.setError("\"strlen(string)\" Returns an integer representing the length of a string.");
			
			//Compare two strings which are passed in.
			String s1="";
			if(parameters.get(0) instanceof TypeString)
				s1=((TypeString)parameters.get(0)).getStringValue();
			return(new TypeInteger(s1.length()));
		}
		
		//A math function.
		if(name.equals("sqrt")){
			if(parameters.size()!=1)
				MyProgram.setError("\"sqrt(float)\" Returns the square root of the float provided.");
			else if(!(parameters.get(0) instanceof TypeFloat))
				MyProgram.setError("\"sqrt(float)\" Returns the square root of the float provided.");
			
			//Compare two strings which are passed in.
			float f1=0.0f;
			if(parameters.get(0) instanceof TypeFloat)
				f1=(Float)((TypeFloat)parameters.get(0)).getRawValue();
			return(new TypeFloat((float)Math.sqrt(f1)));
		}
		
		//A math function.
		if(name.equals("abs")){
			if(parameters.size()!=1)
				MyProgram.setError("\"abs(float)\" Returns the absolute value of the float provided.");
			else if(!(parameters.get(0) instanceof TypeFloat))
				MyProgram.setError("\"abs(float)\" Returns the absolute value of the float provided.");
			
			float f1=0.0f;
			if(parameters.get(0) instanceof TypeFloat)
				f1=(Float)((TypeFloat)parameters.get(0)).getRawValue();
			return(new TypeFloat((float)Math.abs(f1)));
		}
		
		//A math function.
		if(name.equals("ln")){
			if(parameters.size()!=1)
				MyProgram.setError("\"ln(float)\" Returns the natural logarithm of the float provided.");
			else if(!(parameters.get(0) instanceof TypeFloat))
				MyProgram.setError("\"ln(float)\" Returns the natural logarithm of the float provided.");
			
			float f1=0.0f;
			if(parameters.get(0) instanceof TypeFloat)
				f1=(Float)((TypeFloat)parameters.get(0)).getRawValue();
			return(new TypeFloat((float)Math.log(f1)));
		}
		
		//A math function.
		if(name.equals("atan")){
			if(parameters.size()!=1)
				MyProgram.setError("\"atan(float)\" Returns the arc-tangent of the floating point number provided.");
			else if(!(parameters.get(0) instanceof TypeFloat))
				MyProgram.setError("\"atan(float)\" Returns the arc-tangent of the floating point number provided.");
			
			float f1=0.0f;
			if(parameters.get(0) instanceof TypeFloat)
				f1=(Float)((TypeFloat)parameters.get(0)).getRawValue();
			f1=f1*(float)Math.PI/180.0f;
			return(new TypeFloat((float)Math.atan(f1)));
		}
		
		//A math function.
		if(name.equals("acos")){
			if(parameters.size()!=1)
				MyProgram.setError("\"acos(float)\" Returns the arc-cosine of the floating point number provided.");
			else if(!(parameters.get(0) instanceof TypeFloat))
				MyProgram.setError("\"acos(float)\" Returns the arc-cosine of the floating point number provided.");
			
			float f1=0.0f;
			if(parameters.get(0) instanceof TypeFloat)
				f1=(Float)((TypeFloat)parameters.get(0)).getRawValue();
			f1=f1*(float)Math.PI/180.0f;
			return(new TypeFloat((float)Math.acos(f1)));
		}
		
		//A math function.
		if(name.equals("asin")){
			if(parameters.size()!=1)
				MyProgram.setError("\"asin(float)\" Returns the arc-sine of the floating point number provided.");
			else if(!(parameters.get(0) instanceof TypeFloat))
				MyProgram.setError("\"asin(float)\" Returns the arc-sine of the floating point number provided.");
			
			float f1=0.0f;
			if(parameters.get(0) instanceof TypeFloat)
				f1=(Float)((TypeFloat)parameters.get(0)).getRawValue();
			f1=f1*(float)Math.PI/180.0f;
			return(new TypeFloat((float)Math.asin(f1)));
		}
		
		//A math function.
		if(name.equals("tan")){
			if(parameters.size()!=1)
				MyProgram.setError("\"tan(float)\" Returns the tangent of the floating point number provided.");
			else if(!(parameters.get(0) instanceof TypeFloat))
				MyProgram.setError("\"tan(float)\" Returns the tangent of the floating point number provided.");
			
			float f1=0.0f;
			if(parameters.get(0) instanceof TypeFloat)
				f1=(Float)((TypeFloat)parameters.get(0)).getRawValue();
			f1=f1*(float)Math.PI/180.0f;
			return(new TypeFloat((float)Math.tan(f1)));
		}
		
		//A math function.
		if(name.equals("cos")){
			if(parameters.size()!=1)
				MyProgram.setError("\"cos(float)\" Returns the cosine of the floating point number provided.");
			else if(!(parameters.get(0) instanceof TypeFloat))
				MyProgram.setError("\"cos(float)\" Returns the cosine of the floating point number provided.");
			
			float f1=0.0f;
			if(parameters.get(0) instanceof TypeFloat)
				f1=(Float)((TypeFloat)parameters.get(0)).getRawValue();
			f1=f1*(float)Math.PI/180.0f;
			return(new TypeFloat((float)Math.cos(f1)));
		}
		
		//A math function.
		if(name.equals("sin")){
			if(parameters.size()!=1)
				MyProgram.setError("\"sin(float)\" Returns the sine of the floating point number provided.");
			else if(!(parameters.get(0) instanceof TypeFloat))
				MyProgram.setError("\"sin(float)\" Returns the sine of the floating point number provided.");
			
			float f1=0.0f;
			if(parameters.get(0) instanceof TypeFloat)
				f1=(Float)((TypeFloat)parameters.get(0)).getRawValue();
			f1=f1*(float)Math.PI/180.0f;
			return(new TypeFloat((float)Math.sin(f1)));
		}
		
		//A math function.
		if(name.equals("getE")){
			if(parameters.size()!=0)
				MyProgram.setError("\"getE()\" Returns the mathematical constant e.");
			
			return(new TypeFloat((float)Math.E));
		}
		
		//A math function.
		if(name.equals("getPI")){
			if(parameters.size()!=0)
				MyProgram.setError("\"getPI()\" Returns the mathematical constant PI.");
				
			return(new TypeFloat((float)Math.PI));
		}
		
		//Compare two strings which are passed in.
		if(name.equals("substr")){
			if(parameters.size()!=3)
				MyProgram.setError("\"substr(string,int,int)\" returns a substring starting and ending with the two indexes provided.");
			else if(!(parameters.get(0) instanceof TypeString)||!(parameters.get(1) instanceof TypeInteger)||!(parameters.get(2) instanceof TypeInteger))
				MyProgram.setError("\"substr(string,int,int)\" returns a substring starting and ending with the two indexes provided.");
			
			//Compare two strings which are passed in.
			String s1="";
			int start=0;
			int finish=0;
			if(parameters.get(0) instanceof TypeString)
				s1=((TypeString)parameters.get(0)).getStringValue();
			if(parameters.get(1) instanceof TypeInteger)
				start=((TypeInteger)parameters.get(1)).getIntValue();
			if(parameters.get(2) instanceof TypeInteger)
				finish=((TypeInteger)parameters.get(2)).getIntValue();
			return(new TypeString(s1.substring(start,finish)));
		}
		
		//Add and get parameters.
		if(name.equals("getInputString")){
			if(parameters.size()!=0)
				MyProgram.setError("\"getInputString()\" Returns the next string in the array of strings provided as input.");
				
			return(new TypeString(TP.getInString()));
		}
		
		if(name.equals("getInputStringCount")){
			if(parameters.size()!=0)
				MyProgram.setError("\"getInputStringCount()\" Returns the number of string parameters that have been provided as input.");
				
			return(new TypeInteger(TP.getInputStringSize()));
		}
		
		if(name.equals("setOutputString")){		
		
			if(parameters.size()!=1)
				MyProgram.setError("\"setOutputString(string)\" add a string to the output array of strings.");
			else if(!(parameters.get(0) instanceof TypeString))
				MyProgram.setError("\"setOutputString(string)\" add a string to the output array of strings.");
			
			String s1="";
			if(parameters.get(0) instanceof TypeString)
				s1=((TypeString)parameters.get(0)).getStringValue();
			
			TP.addOutString(s1);
		}
		
		if(name.equals("getInputFloat")){
			if(parameters.size()!=0)
				MyProgram.setError("\"getInputFloat()\" Returns the next float in the array of floats provided as input.");
				
			return(new TypeFloat(TP.getInFloat()));
		}
		
		if(name.equals("getInputFloatCount")){
			if(parameters.size()!=0)
				MyProgram.setError("\"getInputFloatCount()\" Returns the number of float parameters that have been provided as input.");
				
			return(new TypeInteger(TP.getInputFloatSize()));
		}
		
		if(name.equals("setOutputFloat")){
			if(parameters.size()!=1)
				MyProgram.setError("\"setOutputFloat(float)\" add a float to the output array of floats.");
			else if(!(parameters.get(0) instanceof TypeFloat))
				MyProgram.setError("\"setOutputFloat(float)\" add a float to the output array of floats.");
			
			float s1=0;
			if(parameters.get(0) instanceof TypeFloat)
				s1=(Float)((TypeFloat)parameters.get(0)).getRawValue();
			
			TP.addOutFloat(s1);
		}
		
		if(name.equals("getInputInt")){
			if(parameters.size()!=0)
				MyProgram.setError("\"getInputInt()\" Returns the next int in the array of ints provided as input.");
				
			return(new TypeInteger(TP.getInInt()));
		}
		
		if(name.equals("getInputIntCount")){
			if(parameters.size()!=0)
				MyProgram.setError("\"getInputIntCount()\" Returns the number of int parameters that have been provided as input.");
				
			return(new TypeInteger(TP.getInputIntSize()));
		}
		
		if(name.equals("setOutputInt")){
			if(parameters.size()!=1)
				MyProgram.setError("\"setOutputInt(int)\" add an int to the output array of ints.");
			else if(!(parameters.get(0) instanceof TypeInteger))
				MyProgram.setError("\"setOutputInt(int)\" add an int to the output array of ints.");
			
			int s1=0;
			if(parameters.get(0) instanceof TypeInteger)
				s1=((TypeInteger)parameters.get(0)).getIntValue();
			
			TP.addOutInt(s1);
		}
		
		//Compare two strings which are passed in.
		if(name.equals("equal")){
			if(parameters.size()==2){
				String s1="";
				String s2="";
				if(parameters.get(0) instanceof TypeString)
					s1=((TypeString)parameters.get(0)).getStringValue();
				if(parameters.get(1) instanceof TypeString)
					s2=((TypeString)parameters.get(1)).getStringValue();
				return(new TypeBoolean(s1.equals(s2)));
			}
		}
		
		if(name.equals("rand")){
			return(new TypeFloat((float)Math.random()));
		}

		//A math function.
		if(name.equals("indexOf")){
			String dataString=((TypeString)parameters.get(0)).getStringValue();
			String searchString=((TypeString)parameters.get(1)).getStringValue();
			int index=((TypeInteger)parameters.get(2)).getIntValue();
			return(new TypeInteger(dataString.indexOf(searchString,index)));
		}else
				
		//A math function.
		if(name.equals("intValue")){
			float f=(Float)((TypeFloat)parameters.get(0)).getRawValue();
			return(new TypeInteger((int)f));
		}else
		
		//A math function.
		if(name.equals("floatValue")){
			int i=((TypeInteger)parameters.get(0)).getIntValue();
			return(new TypeFloat((float)i));
		}else
		
		//A math function.
		if(name.equals("parseFloat")){
			try{
				String s1=((TypeString)parameters.get(0)).getStringValue();
				return(new TypeFloat((float)(new Float(s1))));
			}catch(Exception e2){
				e2.printStackTrace();
			}
		}else
		
		if(name.equals("replaceAll")){
			String s1=((TypeString)parameters.get(0)).getStringValue();
			String s2=((TypeString)parameters.get(1)).getStringValue();
			String s3=((TypeString)parameters.get(2)).getStringValue();
			return(new TypeString(s1.replaceAll(regexEscape(s2),regexEscape(s3))));
		}else
		
		//A math function.
		if(name.equals("parseInt")){
			try{
				String s1=((TypeString)parameters.get(0)).getStringValue();
				return(new TypeInteger((int)(new Integer(s1))));
			}catch(Exception e2){
			}
		}else
		
		if(name.equals("printf")){
			String Content="";
			if(parameters.get(0) instanceof TypeString)
				Content=((TypeString)parameters.get(0)).getStringValue();
			
			Content=" "+Content;
			
			String data[]=Content.split("\\%s");
			String returnMe="";
			for(int i=0;i<data.length;i++){
				returnMe+=data[i];
				if(i+1<parameters.size())
					returnMe+=parameters.get(i+1).toString();
			}
			
			returnMe=returnMe.substring(1,returnMe.length());
			
			return(new TypeString(returnMe));
		}else
		
		//Split a string.
		if(name.equals("split")){	
			String SplitData[]=regexEscape(((TypeString)parameters.get(0)).getStringValue()).split(((TypeString)parameters.get(1)).getStringValue());
			ArrayList AL=new ArrayList();
			for(int i=0;i<SplitData.length;i++)
				AL.add(new TypeString(SplitData[i]));
			TypeArray TA=new TypeArray(AL);
			return(TA);
		}else
		
		if(name.equals("length")){
			if(parameters.get(0)!=null){
				if(parameters.get(0) instanceof TypeArray)
					return( new TypeInteger(( (ArrayList) ( (TypeArray) parameters.get(0) ).getRawValue()).size()));
				ArrayList Temp=(ArrayList)parameters.get(0);
				return(new TypeInteger(Temp.size()));
			}else
				return(new TypeInteger(0));
		}
		
		}catch(Exception e){
			e.printStackTrace();
		}
		
		}else{
			System.out.println(TP.getError()); 
		}
        
        return(null);
    }
}
