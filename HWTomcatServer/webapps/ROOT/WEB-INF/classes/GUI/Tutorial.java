package GUI;

public class Tutorial{
	//data
	private TutorialWindow window=null; //window to display the tutorial step.
	private int step=0; //what step of the tutorial they are on.
	private String function="requestwebpage"; //function that needs to be performed for this step.
	private Object[] check=new Object[]{"fjfjdkj"}; //other info that needs to be checked for this step. 
	
	/**
	nextStep()<br />
	move to the next step of the tutorial.
	
	*/
	public void nextStep(){
		step++;
		window.nextStep();
	}
	
	/**
	setStep(int step)<br />
	sets the tutorial step that they are on.
	*/
	public void setStep(int step){
		this.step=step;
	}
	
	/**
	int getStep()<br />
	returns what step of the tutorial they are on.
	*/
	public int getStep(){
		return step;	
	}
	
	/**
	boolean checkStep(String function, String check)<br />
	checks to see if the step they are on was just performed.
	*/
	public boolean checkStep(String function,Object[] check){
		//System.out.println(this.function+"   "+function);
		if(this.function.equals(function)){ //if the function is what is what it needs to be.
			for(int i=0;i<this.check.length;i++){  //go through the objects and make sure they are equal
				try{
					if(this.check[i] instanceof String&&check[i] instanceof String){ //if they are both strings make sure they are equal
						//System.out.println(this.check[i]+"    "+check[i]);
						if(!(((String)this.check[i]).equals((String)check[i])))
							return false;
					}else if(this.check[i] instanceof Boolean&&check[i] instanceof Boolean){ //if they are both booleans make sure they are equal
						if((boolean)(Boolean)this.check[i]!=(boolean)(Boolean)check[i])
							return false;
					}else if(this.check[i] instanceof Integer&&check[i] instanceof Integer){ //if they are both integers make sure they are equal
						if((int)(Integer)this.check[i]!=(int)(Integer)check[i])
							return false;
					}else if(this.check[i] instanceof Float&&check[i] instanceof Float){ //if they are both floats make sure they are equal
						if((float)(Float)this.check[i]!=(float)(Float)check[i])
							return false;
					}else{ //if they aren't equal
						return false;
					}
				}catch(ArrayIndexOutOfBoundsException e){ //if the arrays aren't the same size, the check fails
					return false;
				}catch(NullPointerException e){ //if there is a null value, the check fails
					return false;
				}
			}
		}else{
			return false;  //if the function names don't match
		}
		return true;
	}
	
	public void setFunction(String function){
		this.function=function;
	}
	
	public void setCheck(Object[] check){
		this.check=check;
	}
	
	public void setWindow(TutorialWindow window){
		this.window=window;
	}
	
	public String getFunction(){
		return function;
	}
	
	public Object[] getCheck(){
		return check;
	}
	
	public static void main(String args[]){
		Tutorial T = new Tutorial();
		T.setFunction("requestwebpage");
		T.setCheck(new Object[]{"store1",new Integer(2)});
		System.out.println(T.checkStep("requestwebpage",new Object[]{"store1"}));
		System.out.println(T.checkStep("requestwebpage",new Object[]{"238.099.4.179"}));
		System.out.println(T.checkStep("requestwebpage",new Object[]{"store1",new Integer(2)}));
		
	}
	
}
