/*
 Copyright (c) 2007 Benjamin Coe/Cameron McGuinness
 Copyright (c) 2009 Jobe Microsystems
 
 This file is part of HackWars.
 
 HackWars is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.
 HackWars is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.
 
 You should have received a copy of the GNU General Public License
 along with HackWars.  If not, see <http://www.gnu.org/licenses/>.
 */
package Game;
/**
 Description: This class looks for data to be saved and makes a connection to the Tomcat server/MySQL and
 saves a profile.
 */

import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import java.util.*;
import java.io.*;
import util.*;
import java.net.URL;
import Server.*;

public class CheckOutHandler implements Runnable{
	//MYSQL INFO.
	public static int SAVE_COUNTER = 0;
	private String Connection="127.0.0.1";
	private String DB="hackwars";
	private String Username="root";
	private String Password="";
	private static final long maxTimeout = 350000;
	private static final long maxZeroTimeout = 20000;
	private long lastSave = 0;
	private static final long sleepTime = 500;//How long should it sleep after saving a profile.
	private Thread MyThread=null;//The thread.
	private boolean running=false;//The variable that keeps track of whether it is running.
	
	public CheckOutHandler(){
		this.start();
	}
	
	private void start(){
		MyThread = new Thread(this,"CheckOutHandler");
		running=true;
		MyThread.start();
	}
	
	//Fetches a profile from the DB based on IP.
	public String fetchProfile(String ip,boolean checkActive){
		sql C=new sql(Connection,DB,Username,Password);
		ArrayList result=null;
		try{
			
			if(checkActive){
				String query = "SELECT npc,TO_DAYS(NOW())-TO_DAYS(last_logged_in) FROM hackerforum.users WHERE ip=\""+ip+"\"";
				result = C.process(query);
				if(result==null)
					return null;
				String npc = (String)result.get(0);
				String active = (String)result.get(1);
				if(npc.equals("N")){
					if((int)Integer.parseInt(active)>14)
						return "inactive";
				}
			}
			String Q="select stats from user where ip=\""+ip+"\"";
			//System.out.println(Q);
			
			result=C.process(Q);	
			
			
			C.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		if(result==null)
			return(null);
		else{
			return((String)result.get(0));
		}
	}
	
	
	//Thread processes current save tasks.
	public synchronized void run(){
		while(running){
			try{
				
				Object O[]=MysqlHandler.getWork();
				if(O!=null){
					
					String ip=(String)O[0];
					//String content=(String)O[1];
					Computer computer = (Computer)O[1];
					String content = computer.outputXML();
					boolean pageChanged=(Boolean)O[3];
					String pageTitle=(String)O[4];
					String pageBody=(String)O[5];
					
					System.out.println("Starting Saving "+ip);
					
					try{
						if(pageChanged){//Save over XML-RPC so that we can update the website easily.
							System.out.println("Page Changed!");
							
							XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
							config.setServerURL(new URL("http://localhost:8081/xmlrpc"));
							XmlRpcClient client = new XmlRpcClient();
							client.setConfig(config);
							Object[] params=null;
							System.out.println("xmlrpc started");
							
							params = new Object[]{"asdbas0d98a0sd9fa8sasdlbo",ip,content,new Boolean(pageChanged),pageTitle,pageBody};
							client.execute("hackerRPC.saveProfile",params);
						}else {//Save Directly to DB
							
							
							if(fetchProfile(ip,false)==null){
								
								sql C=new sql(Connection,DB,Username,Password);
								ArrayList result=null;
								String Q="insert into user values(\""+ip+"\",\""+content.replaceAll("\"","\\\\\"")+"\",NULL);";
								C.process(Q);	
								C.close();
							}else{

								sql C=new sql(Connection,DB,Username,Password);
								ArrayList result=null;
								content = content.replaceAll("\\\\","\\\\\\\\");
								content = content.replaceAll("\"","\\\\\"");				
								
								String Q="update user set stats=\""+content+"\" where ip=\""+ip+"\";";
								
								System.out.println("Just got to the process step.");
								
								C.process(Q);	
								C.close();
							}
						}
						
					}catch(Exception e){
						e.printStackTrace();
					}
					SAVE_COUNTER --;
					System.out.println("Finished Saving " + ip + ", accounts left to save = "+SAVE_COUNTER);
					System.gc();
					lastSave = HackerServer.MyTime.getCurrentTime();
			}
			
			//Force the entire application to exit.
			if( (!HackerServer.on && SAVE_COUNTER == 0 && (HackerServer.MyTime.getCurrentTime() - lastSave) > maxZeroTimeout) || (!HackerServer.on && (HackerServer.MyTime.getCurrentTime() - HackerServer.SHUTDOWN_AT) > maxTimeout) ){
				System.exit(0);
			}
			
			MyThread.sleep(sleepTime);
			}catch(Exception e){}
		}
		
	}
}
