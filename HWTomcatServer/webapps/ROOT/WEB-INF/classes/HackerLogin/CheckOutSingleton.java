package HackerLogin;
/**
CheckOutSingleton.java

Ensure that only one instance of the check out handler is floating around.
*/
import java.util.*;
import java.io.*;

public class CheckOutSingleton{
	private static CheckOutHandler instance=null;
	
	public static CheckOutHandler getInstance(){
		if(instance==null)
			instance=new CheckOutHandler();
		return instance;
	}
}

