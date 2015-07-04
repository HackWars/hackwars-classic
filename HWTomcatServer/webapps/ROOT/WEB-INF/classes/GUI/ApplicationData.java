package GUI;
/**
*
*data to be sent to the server.
*@author Cameron McGuinness
*@author Ben Coe
*/

public abstract class ApplicationData{

	//data
	String  function;
	Object parameters;

	public ApplicationData(String function,Object parameters){
		this.function=function;
		this.parameters=parameters;
	}

	public String getFunction(){
		return(function);
	}

	public Object getParameters(){
		return(parameters);
	}

}
