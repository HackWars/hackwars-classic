package GUI;



public class SyntaxError{


	public static String getMessage(int code){
		String errorType="";
		switch(code){
		case 22:
			errorType="';' Expected";
			break;
		case 28:
			errorType="'(' Expected";
			break;
		case 27:
			errorType="')' Expected";
			break;
		case 40:
			errorType="Unknown Identifier";
			break;
		default:
			errorType="Unknown Code "+code;
		}
	
		return(errorType);
	}



}
