package HackerSearch.util;
import com.plink.dolphinnet.util.*;
import java.util.*;
import java.io.Serializable;

/**
<b>DolphinNet<br />
Benjamin E. Coe (2006)</b><br /><br />

An implementation of a binary list used for storing an Editor's (server's) client data.
*/
public class WordIndex implements Serializable{
	private String Word;
	private String Category;
	private long lastAccessed;
	private SiteBinaryList SBL;
	private RankBinaryList RBL;
	
	public WordIndex(String Word,String Category){
		this.Word=Word;
		this.Category=Category;
		this.SBL=new SiteBinaryList();
		this.RBL=new RankBinaryList();
	}
	
	public void setLastAccessed(long lastAccessed){
		this.lastAccessed=lastAccessed;
	}
	
	public long getLastAccessed(){
		return(lastAccessed);
	}
	
	public String getWord(){
		return(Word);
	}
	
	public String getCategory(){
		return(Category);
	}
	
	public void addSite(SiteIndex SI){
		SBL.add(SI);
		RBL.add(SI);
	}
	
	public void removeSite(SiteIndex SI){
		SBL.remove(SI.getAddress());
		RBL.remove(new Double(SI.getRank()));
	}
	
	public SiteBinaryList getSiteBinaryList(){
		return(SBL);
	}
	
	public RankBinaryList getRankBinaryList(){
		return(RBL);
	}
}