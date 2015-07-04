/*
 * MakeBountry.java
 *
 * Created on March 10, 2007, 10:40 AM
 *
 * The main linker for Hack Wars.
 *
 */

package Game;
import java.util.ArrayList;
import Hackscript.Model.*;
import java.util.HashMap;

/**
 * By Alexander Morrison
 */
public class MakeBounty{
	//Events that can trigger a clue.
	public static final int SCAN=0;
	public static final int KILL=1;
	public static final int INSTALL=2;
	public static final int VOTE=3;
	public static final int CHANGE=4;
	public static final int DESTROY_WATCH=5;
	private static String TypeNames[]=new String[]{"Scan","Kill","Install","Vote","Change HTTP","Destroy Watch"};

	FileSystem MyFileSystem=null;//The file system used to check for clues.
	
	public MakeBounty(FileSystem MyFileSystem){
		this.MyFileSystem=MyFileSystem;
	}
	
	/**
	Get the name associated with the bounty type.
	*/
	public static String getTypeName(int type){
		return(TypeNames[type]);
	}
	
	/**
	Check to see whether a condition has been met for a given clue,
	*/
	public void checkBounty(Computer MyComputer,HackerFile InstallFile,int BountyType,String target,boolean npc,String setIP){
		if(!npc){
			Object RootFiles[]=MyFileSystem.getWebDirectory("");
			for(int i=0;i<RootFiles.length;i++){
				if(RootFiles[i] instanceof HackerFile){
					HackerFile HF=(HackerFile)RootFiles[i];
					if(HF.getType()==HF.BOUNTY){
						if(checkFile(HF,MyComputer,InstallFile,BountyType,target,setIP))
							return;
					}
				}
			}
		}
	}
	
	/**
	Check a specific file for the given result.
	*/
	public boolean checkFile(HackerFile HF,Computer MyComputer,HackerFile InstallFile,int BountyType,String target,String setIP){
		HashMap Content=null;
		if(HF!=null)
			Content=HF.getContent();
			
		if(Content!=null){
			int count=new Integer((String)Content.get("count"));
			int CheckBountyType=new Integer((String)Content.get("type"));
			float reward=new Float((String)Content.get("reward"));
			String checkTarget=(String)Content.get("target");
			String scriptName=(String)Content.get("script");
			String maker=(String)Content.get("maker");
			String bountyip=(String)Content.get("bountyip");
			boolean success=true;
			
			try{
				if(HF.getType()==HackerFile.BOUNTY){		
				
					if(BountyType==SCAN&&BountyType==CheckBountyType){
						if(!checkTarget.equals(target)&&!checkTarget.equals("*"))
							success=false;
					}else
					
					if(BountyType==KILL&&BountyType==CheckBountyType){
						if(!checkTarget.equals(target)&&!checkTarget.equals("*"))
							success=false;
					}else
					
					if(BountyType==INSTALL&&BountyType==CheckBountyType){
						if(!checkTarget.equals(target)&&!checkTarget.equals("*"))
							success=false;
						if(InstallFile!=null){
							if(!scriptName.equals(InstallFile.getName())||!maker.equals(InstallFile.getMaker()))
								success=false;
						}
					}else
					
					if(BountyType==VOTE&&BountyType==CheckBountyType){
						if(!checkTarget.equals(target)&&!checkTarget.equals("*"))
							success=false;
					}else
					
					if(BountyType==CHANGE&&BountyType==CheckBountyType&&bountyip.equals(setIP)){
						if(!checkTarget.equals(target)&&!checkTarget.equals("*"))
							success=false;
						else
							MyComputer.getComputerHandler().addData(new ApplicationData("bountyhttp",MyComputer.getIP(),0,MyComputer.getIP()),target);
					}else

					if(BountyType==DESTROY_WATCH&&BountyType==CheckBountyType){
						if(!checkTarget.equals(target)&&!checkTarget.equals("*"))
							success=false;
					}else{
						success=false;
					}
				}
				
				if(success){
					MyComputer.addMessage(MessageHandler.BOUNTY_STEP_COMPLETED);

					count-=1;
					Content.put("count",""+count);
					if(count<=0){
						Object O[]=new Object[]{"Store/",HF.getName()};
						MyFileSystem.deleteFile(HF.getLocation(),HF.getName());
						MyComputer.getComputerHandler().addData(new ApplicationData("checkbounty",HF.getName(),0,MyComputer.getIP()),MyComputer.getStoreIP());
						MyComputer.getComputerHandler().addData(new ApplicationData("deletefile",O,0,MyComputer.getIP()),MyComputer.getStoreIP());
					}
					
					return(true);
				}
				
				
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}
		
		return(false);
	}
	
}
