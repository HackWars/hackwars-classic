package Hacktendo;
import java.util.*;
import java.io.*;

/**
<b>DolphinNet<br />
Benjamin E. Coe (2006)</b><br /><br />

An abstract basis for a binary searchable list. You must implement getKey() which is used to extract
the primary key from the list of objects you are storing and compare() which is used to compare
two objects.
*/
public class SpriteBinaryList implements Serializable{

	private SpriteByID SpriteByID=null;
	
	public SpriteBinaryList(){
		SpriteByID=new SpriteByID();
	}	

	public void add(Sprite S){
		SpriteByID.add(S);
	}

	public Sprite removeByID(int id){
	
		Sprite S=(Sprite)SpriteByID.get(new Integer(id));
		if(S==null)
			return(null);

		SpriteByID.remove(new Integer(id));

		return(S);
	}
	
	public Sprite getByID(int id){
		return((Sprite)SpriteByID.get(new Integer(id)));
	}

	public ArrayList getIDArray(){
		return(SpriteByID.getData());
	}
	
	public void clear(){
		SpriteByID.getData().clear();
	}

	public String toString(){
		return("");
	}
}