package GUI;


public class HelpFile{
	String name;
	String link;
	
	public HelpFile(String name,String link){
		this.name=name;
		this.link=link;
	}
	
	public String toString(){
		return name;
	}
	
	public String getLink(){
		return link;
	}
	
}
