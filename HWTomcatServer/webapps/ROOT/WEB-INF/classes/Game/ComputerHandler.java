package Game;
/**
ComputerHandler.java<br />
(c) Hacker 2007<br />

A central location for distributing messages to other computers.
*/

import java.util.*;
import com.plink.dolphinnet.*;
import Server.*;
import Assignments.*;
import util.*;
import Server.*;
import java.util.concurrent.Semaphore;

public class ComputerHandler implements Runnable{
	//Data.
	private Time MyTime=null;//Central time handling.
	private ComputerBinaryList Computers=new ComputerBinaryList();//An array of the computers currently attached.
	private ArrayList Tasks=new ArrayList();//An array of tasks to execute.
	private static final long sleepTime=50;//Sleep time for each iteration of the main handling thread.
	private Thread MyThread=null;//The thread.
	private final Semaphore available = new Semaphore(1, true);//Make sure this is thread safe.

	private HackerServer MyHackerServer=null;//The server for dispatching packets.
	private boolean on=true;
	private int playerCount=0;

	//Constructor.
	public ComputerHandler(Time MyTime,HackerServer MyHackerServer){
		this.MyTime=MyTime;
		MyThread=new Thread(this,"Computer Handler");
		MyThread.start();
		this.MyHackerServer=MyHackerServer;
	}
	
	/**
	Application Data Task.
	Used to distribute messages to other computers in a thread safe environment.
	*/
	private class ApplicationDataTask{
		private Object MyApplicationData=null;
		private String ip=null;
		public ApplicationDataTask(Object MyApplicationData,String ip){
			this.MyApplicationData=MyApplicationData;
			this.ip=ip;
		}
		public Object getApplicationData(){
			return(MyApplicationData);
		}
		public String getIP(){
			return(ip);
		}
	}
	
	/**
	Broadcast a message to all the attached computers.
	*/
	public void broadcast(ApplicationData AD){
		try{
			available.acquire();
			Iterator MyIterator=Computers.getData().iterator();
			while(MyIterator.hasNext()){
				Computer MyComputer=(Computer)MyIterator.next();
				MyComputer.addData(AD);
			}
			available.release();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	Tell all the computers attached to the computer handler that it is time to shutdown.
	*/
	public void startCountDown(){
		try{
			available.acquire();
			on=false;
			Iterator MyIterator=Computers.getData().iterator();
			while(MyIterator.hasNext()){
				Computer MyComputer=(Computer)MyIterator.next();
				MyComputer.startCountDown();
			}
			available.release();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	Add Application data.
	Used to distribute messages to other computers in a thread safe environment.
	*/
	public void addData(Object MyApplicationData,String ip,int source){
		((ApplicationData)MyApplicationData).setSource(source);
		addData(MyApplicationData,ip);
	}

	public void addData(Object MyApplicationData,String ip){
		try{
			available.acquire();
			Tasks.add(new ApplicationDataTask(MyApplicationData,ip));
			available.release();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	Add a Computer to the list of computers.
	*/
	public void addComputer(Computer MyComputer){
		try{
			available.acquire();
			Computers.add(MyComputer);
			available.release();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	Increase the number of players connected.
	*/
	public void incrementPlayers(){
		playerCount++;
	}
	
	/**
	Get the number of players currently connected.
	*/
	public int getPlayers(){
		return(playerCount);
	}
	
	/**
	Decrease the number of players connected.
	*/
	public void decrementPlayers(){
		playerCount--;
	}
	
	/**
	Return a computer from the binary list.
	*/
	public Computer getComputer(String ip){
		try{
			available.acquire();
			return((Computer)Computers.get(ip));
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			available.release();
		}
		return(null);
	}
	
    /**
    Fetch execution tasks from the stack.
	Where tasks are distrubuted to other computers.
    */
	int counter=0;
    public synchronized void run(){
		long startTime;
        while(true){
			startTime=MyTime.getCurrentTime();
			
			try{
			
				available.acquire();
				
				//Iterator MyIterator=Tasks.iterator();
				
				//Check for users to load.
				
				
				
				
			//	while(MyIterator.hasNext()){
				while(Tasks.size()>0){
				//	Object o=MyIterator.next();
					Object o=Tasks.remove(0);
				
					if(o instanceof ApplicationDataTask){
						ApplicationDataTask ADT=(ApplicationDataTask)o;
						if(ADT.getApplicationData()!=null){
							Computer C=null;
							if(ADT.getIP()!=null)
								C=(Computer)Computers.get(ADT.getIP());
							if(C!=null){
                                if(C.getLoaded()){
                                    C.addData(ADT.getApplicationData());
                                }else{
                                    available.release();
                                    addData(ADT.getApplicationData(),ADT.getIP());
                                    available.acquire();
                                    
                                }
							//	MyIterator.remove();
							}else if(C==null&&ADT.getIP()!=null){
								if(on){
									//If the file doesn't exist a flag will be set in the Computer's load step.
									Computer MyComputer=new Computer(ADT.getIP(),this,MyTime,-1,MyHackerServer);
									if(ADT.getApplicationData() instanceof ApplicationData){
										MyComputer.setLoadRequester(((ApplicationData)ADT.getApplicationData()).getSourceIP());
										MyComputer.addData(ADT.getApplicationData());
									}
									//MyIterator.remove();
									Computers.add(MyComputer);
									MyComputer.loadSave();
								}
							}else if(ADT.getIP()==null){
							//	MyIterator.remove();
							}
						}else{//Remove a computer from the list.
							System.out.println("Unloading player "+ADT.getIP());
							Computer C=(Computer)Computers.get(ADT.getIP());
							Computers.remove(ADT.getIP());
							MyHackerServer.removeRandomKey(ADT.getIP());
							if(C!=null)
								C.setRun(false);
							//MyIterator.remove();
						}
					}
				}
				
				available.release();
			}catch(Exception e){
				available.release();
				e.printStackTrace();
			}
            
            try{
             long endTime=MyTime.getCurrentTime();
             if(sleepTime-(endTime-startTime)>0)
                MyThread.sleep(sleepTime-(endTime-startTime));
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
