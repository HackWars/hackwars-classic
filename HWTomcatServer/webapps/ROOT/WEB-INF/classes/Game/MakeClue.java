/*
 * MakeClue.java
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
public class MakeClue{
	//Events that can trigger a clue.
	public static final int SCAN=0;
	public static final int KILL=1;
	public static final int READ=2;
	public static final int CHALLENGE=3;
	public static final int FINISH=4;
	public static final int DROP_COUNT[]={3,4,5};
	public static final String DROP_NAME[]={"Secret Document (Medium)","Secret Document (High)","Secret Document (Rare)"};
	public static final float XP_TABLE[]={500.0f,2000.0f,10000.0f};
	public static final float MONEY_TABLE[]={5000.0f,10000.0f,25000.0f};

	private FileSystem MyFileSystem=null;//The file system used to check for clues.
	private Computer MyComputer=null;//The computer.
	
	public MakeClue(FileSystem MyFileSystem,Computer MyComputer,int clueLevel){
		this.MyFileSystem=MyFileSystem;
		this.MyComputer=MyComputer;
		
		//GENERATE THE ACTUAL CLUE VALUES.
		AttackClue.add(""+KILL+"+Attack this source of human knowledge.+192.168.2.300");
		AttackClue.add(""+KILL+"+They can put a man on the moon, but they can't protect their website from attack.+192.168.2.747");

		ScanClue.add(""+SCAN+"+They have a shiny cubic building, and nearly half the world's computing power -- scan them.+171.016.5.036");
		ScanClue.add(""+SCAN+"+Scan the team, if you know what I mean.+192.168.2.400");
		ScanClue.add(""+SCAN+"+What they lack in offensive power, they make up for in cold weather -- scan them.+192.168.3.666");
		ScanClue.add(""+SCAN+"+Scan the first of the great detective's.+478.254.8.001");
		ScanClue.add(""+SCAN+"+Scan this impressive source of Star Wars information.+192.168.2.300");
		ScanClue.add(""+SCAN+"+Scan this source of scientific doodads.+192.168.2.747");

		ChallengeClue.add(""+CHALLENGE+"+Do the 'Draw Triangle' challenge.+7");
		ChallengeClue.add(""+CHALLENGE+"+Do the 'Reverse Sentence' challenge.+8");
		ChallengeClue.add(""+CHALLENGE+"+Do the 'Combine Strings' challenge.+9");
		ChallengeClue.add(""+CHALLENGE+"+Do the 'Prime Number' challenge.+10");
		ChallengeClue.add(""+CHALLENGE+"+Do the 'Search' challenge.+11");
		ChallengeClue.add(""+CHALLENGE+"+Do the 'Volume of a Cube' challenge.+12");

		DataClue.add(""+READ+"+Find where dorothy met the Scarecrow.+222.345.7.003");
		DataClue.add(""+READ+"+Find the conclusion to this victorian mystery.+478.254.8.055");
		DataClue.add(""+READ+"+Find the ghost of Marley (not Bob).+569.234.1.002");
		DataClue.add(""+READ+"+Where the maid visits Patty.+765.432.1.022");
		DataClue.add(""+READ+"+Find the deadly poppy field.+222.345.7.008");
		DataClue.add(""+READ+"+Find where dorothy met the Tin Woodman.+222.345.7.005");
		
		if(clueLevel==1){
			DataClue.add(""+READ+"+Read a natural conclusion.+221.445.3.070");
			AttackClue.add(""+KILL+"+Kill the origin of an origin.+221.445.3.001");
			AttackClue.add(""+KILL+"+Reduce the first health of nations.+324.564.5.001");
			DataClue.add(""+READ+"+Find the start of this slice of Americana.+453.211.5.001");
			DataClue.add(""+READ+"+Find something more beautiful than an example of geometrical symmetry.+458.663.4.014");
			ChallengeClue.add(""+CHALLENGE+"+Do the equation of a line challenge.+15");
		}
		
		if(clueLevel==2){
			AttackClue.add(""+KILL+"+Beat the beginning of the book by the bard.+577.341.8.001");
			DataClue.add(""+READ+"+Read this section regarding heat transfer.+652.876.9.009");
			DataClue.add(""+READ+"+Where Aufidius loses his rage.+577.341.8.026");
			ChallengeClue.add(""+CHALLENGE+"+Do the projectile challenge.+16");
		}
	}

	//Used to keep track of the differnt possible clue tasks.
	private ArrayList DataClue=new ArrayList();
	private ArrayList AttackClue=new ArrayList();
	private ArrayList ChallengeClue=new ArrayList();
	private ArrayList ScanClue=new ArrayList();
	
	public String generateClue(){	
		float drop=(float)Math.random();
		if(drop<0.25f){
			return(getData());
		}else if(drop<0.50f){
			return(getAttack());
		}else if(drop<0.75f){
			return(getChallenge());
		}else if(drop<1.0f){
			return(getScan());
		}
		return("");
	}
	
	public String getData(){
		float drop=(float)Math.random();
		float classSize=1.0f/DataClue.size();
		int idrop=(int)(drop/classSize);
		return((String)DataClue.get(idrop));
	}
	
	public String getAttack(){
		float drop=(float)Math.random();
		float classSize=1.0f/AttackClue.size();
		int idrop=(int)(drop/classSize);
		return((String)AttackClue.get(idrop));
	}
	
	public String getChallenge(){
		float drop=(float)Math.random();
		float classSize=1.0f/ChallengeClue.size();
		int idrop=(int)(drop/classSize);
		return((String)ChallengeClue.get(idrop));
	}
	
	public String getScan(){
		float drop=(float)Math.random();
		float classSize=1.0f/ScanClue.size();
		int idrop=(int)(drop/classSize);
		return((String)ScanClue.get(idrop));
	}
	
	/**
	Check to see whether a condition has been met for a given clue,
	*/
	public void checkClue(Computer MyComputer,String Result,int ClueType){
		Object RootFiles[]=MyFileSystem.getScanDirectory("");
		for(int i=0;i<RootFiles.length;i++){
			if(RootFiles[i] instanceof HackerFile){
				HackerFile HF=(HackerFile)RootFiles[i];
				checkFile(HF,MyComputer,Result,ClueType);
			}
		}
	}
	
	/**
	Check a specific file for the given result.
	*/
	public void checkFile(HackerFile HF,Computer MyComputer,String Result,int ClueType){
		try{
			if(HF.getType()==HackerFile.CLUE){
				HashMap Content=HF.getContent();
				int currentStep=new Integer((String)Content.get("currentstep"));
				String data=(String)Content.get("step"+currentStep);
				String ClueData[]=data.split("\\+");
				//Actually Use The Data.
				int DataType=new Integer(ClueData[0]);
				String Output=ClueData[2];
				HF.setDescription(ClueData[1]);
				
				if(DataType==ClueType&&Output.equals(Result)){
					MyComputer.addMessage(MessageHandler.SECRET_DOCUMENT_TASK_COMPLETED);
					currentStep+=1;
					Content.put("currentstep",""+currentStep);
					data=(String)Content.get("step"+currentStep);
					ClueData=data.split("\\+");
					HF.setDescription(ClueData[1]);
					//Actually Use The Data.
					DataType=new Integer(ClueData[0]);
					if(DataType==FINISH){
						if(HF.getName().indexOf("Gateway Document")==-1){
						
							DropTable MyDropTable=new DropTable(0,MyComputer);
							int clueLevel=new Integer((String)Content.get("cluelevel"));
						
							MyComputer.getFileSystem().deleteFile("",HF.getName());
							MyComputer.addMessage("Congratulations! You have completed all the tasks assigned in a secret document.");
							MyComputer.addMessage("");
							MyComputer.addMessage("Rewards:");
							MyComputer.addMessage(XP_TABLE[clueLevel]+" XP in all skills.");
							MyComputer.addMessage("$"+MONEY_TABLE[clueLevel]+" rewarded.");
							Object O[]=new Object[]{MONEY_TABLE[clueLevel],XP_TABLE[clueLevel]};
							MyComputer.getComputerHandler().addData(new ApplicationData("challengeresults",O,0,MyComputer.getIP()),MyComputer.getIP());
							HackerFile H1=null;
							HackerFile H2=null;
							if(clueLevel==0){
								H1=MyDropTable.generateDrop();
								H2=MyDropTable.generateDrop();
							}else if(clueLevel==1){
								H1=MyDropTable.generateDrop();
								H2=MyDropTable.generateDrop();
							}else{
								H1=MyDropTable.generateDrop();
								H2=MyDropTable.generateDrop();
							}
							MyComputer.addMessage("Rewarded File "+H1.getName());
							MyComputer.addMessage("Rewarded File "+H2.getName());
							
							Object Parameter[]=new Object[]{"",H1};
							MyComputer.getComputerHandler().addData(new ApplicationData("savefile",Parameter,0,MyComputer.getIP()),MyComputer.getIP());
							Parameter=new Object[]{"",H2};
							MyComputer.getComputerHandler().addData(new ApplicationData("savefile",Parameter,0,MyComputer.getIP()),MyComputer.getIP());
						}else{
							MyComputer.getFileSystem().deleteFile("",HF.getName());
							MyComputer.getComputerHandler().addData(new ApplicationData("requestnetworkhop",MyComputer.getIP(),0,MyComputer.getIP()),HF.getMaker());
						}
					}
					MyComputer.sendPacket();
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	Return the data parsed from a packet as a Hacker File. 
	*/                                                              
	public HackerFile generateClue(int clueLevel){
		HackerFile HF=new HackerFile(HackerFile.CLUE);
		
		if(!MyComputer.isGateway())
			HF.setName(DROP_NAME[clueLevel]);
		else
			HF.setName("Gateway Document");
		
		HF.setQuantity(1);
		if(!MyComputer.isGateway())
			HF.setMaker("Johnny Heart");
		else
			HF.setMaker(MyComputer.getIP());
			
		HashMap Data=new HashMap();
		Data.put("currentstep","0");
		Data.put("cluelevel",""+clueLevel);
		for(int i=0;i<6;i++){
			Data.put("step"+i,"");
		}
		int ii=0;
		for(ii=0;ii<DROP_COUNT[clueLevel];ii++){
			Data.put("step"+ii,generateClue());
		}
		Data.put("step"+ii,""+FINISH+"+N/A+N/A");
		String data=(String)Data.get("step0");
		String ClueData[]=data.split("\\+");
		HF.setDescription(ClueData[1]);

		HF.setContent(Data);
		return(HF);
	}
}
