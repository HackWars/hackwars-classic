package Game;

/**
  Description: This is the Network singleton.  It loads all the networks into existence.  Sometimes it blows boiling hot lava all over your keyboard, forcing upgrades.
  */

import java.util.*;
import util.*;
import Assignments.*;
  
 public class Network implements Runnable {
	//The root network.
	public static final String ROOT_NETWORK="UGOPNet";
	public static final String JAIL_NETWORK="JuniperPenetentiary";
	public static final long ATTACK_SLEEP = 180000;
	//private Thread 
	private Thread MyThread = null;
	 
    //MYSQL INFO.
	private String Connection="localhost";
	private String DB="hackwars";
	private String Username="root";
	private String Password="";

    private static Network myNetworkSingleton;
	private NetworkSwitch MyComputerHandler = null;
    private HashMap networkNodes = new HashMap();
	
	/**
	Add a player to a certain network. (Used primarily when logging on to set a player to root.)
	*/
	public synchronized void addToNetwork(String networkName,String playerIP){
		HashMap Node=(HashMap)networkNodes.get(networkName);
		if(Node!=null){
			HashMap Players=(HashMap)Node.get("players");
			if(Players!=null){
				Players.put(playerIP,playerIP);
			}else{
				Players=new HashMap();
				Players.put(playerIP,playerIP);
				Node.put("players",Players);
			}
		}
	}
	
	/**
	Add a player to a certain network. (Used primarily when logging on to set a player to root.)
	*/
	public synchronized void removeFromNetwork(String networkName,String playerIP){
		HashMap Node=(HashMap)networkNodes.get(networkName);
		if(Node!=null){
			HashMap Players=(HashMap)Node.get("players");
			if(Players!=null){
				Players.remove(playerIP);
			}
		}
	}
	
	/**
	Fetch the network information for this network.
	*/
	public synchronized PacketNetwork getNetworkInformation(String networkName){
		HashMap Node=(HashMap)networkNodes.get(networkName);
		if(Node!=null){
			PacketNetwork MyNetworkPacket=new PacketNetwork();
			MyNetworkPacket.setName(networkName);
			
			ArrayList AttackNPCs=(ArrayList)Node.get("attackNPCs");
			ArrayList QuestNPCs=(ArrayList)Node.get("questNPCs");
			ArrayList MineNPCs=(ArrayList)Node.get("miningNPCs");
            ArrayList StoreNPCs=(ArrayList)Node.get("storeNPCs");
			String storeIP=(String)Node.get("storeNPC");
			
			MyNetworkPacket.setAttackNPCs(AttackNPCs);
			MyNetworkPacket.setQuestNPCs(QuestNPCs);
			MyNetworkPacket.setMiningNPCs(MineNPCs);
            MyNetworkPacket.setStoreNPCs(StoreNPCs);
			MyNetworkPacket.setStoreIP(storeIP);
			return(MyNetworkPacket);
		}
		return(null);
	}
	
	/**
	Request that a player be switched to another network.
	*/
	public synchronized String switchNetwork(String startNetwork,String endNetwork,String playerIP){
        // this should never happen as the check now happens in Computer.java
		if(startNetwork.equals(endNetwork))//Check whether you are trying to switch to the same network.
			return("You are already on " + startNetwork + ".");
		
		String message="There is no connection between " + startNetwork + " and " + endNetwork + ".";
		HashMap StartNetwork=(HashMap)networkNodes.get(startNetwork);
		HashMap AttachedNetworks=(HashMap)StartNetwork.get("attachedNetworks");
		if(AttachedNetworks.get(endNetwork)!=null){
			message=(String)AttachedNetworks.get(endNetwork);
		}
		return(message);
	}
    
    private Network(NetworkSwitch MyComputerHandler) {
		this.MyComputerHandler = MyComputerHandler;
		loadNetworks();
		
		MyThread = new Thread(this);
		MyThread.start();
    }
	
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException(); 
    }
    
    public static synchronized Network getInstance(NetworkSwitch MyComputerHandler) {
        if (myNetworkSingleton == null) {
            myNetworkSingleton = new Network(MyComputerHandler);
        }
        return myNetworkSingleton;
    }
    
    public HashMap loadNetworks() {
        
        try {
            // load all the network nodes from the database
            sql C = new sql(Connection, DB, Username, Password);

            ArrayList result = null;
            ArrayList result1 = null;
            
            String Q = "SELECT id, name, attack_probability FROM network";
            result = C.process(Q);
            if (result != null && result.size() > 0) {
                for (int i = 0; i < result.size(); i+=3) {
                    // networkInfo is the value of the networks hashmap, for a given network
                    // "storeNPC" returns a string ip
                    // "attackNPCs" returns an arraList of attack NPCs
                    // "miningNPCs" returns an arrayList (p, resource)
                    // "questNPCs" returns an arrayList of quest NPCs
                    // "attachedNetworks" returns an HashMap ( attached network name, entrance message)
                    
                    HashMap networkInfo = new HashMap();
                    
                    String networkId = (String)result.get(i);
                    String networkName = (String)result.get(i+1);
                    float attackProbability = (float)(new Float((String)result.get(i+2)));

                    //String networkStoreIP = (String)result.get(i+2);
                    //networkInfo.put("storeNPC", networkStoreIP);
                    
                    //get the attached networks and their entrance criteria
                    String Q1 = "SELECT n.name,an.entranceMessage FROM network n INNER JOIN attached_networks an ON n.id = an.attached_network_id WHERE an.network_id = " + networkId;
                    result1 = C.process(Q1);
                    HashMap attachedNetworksArray = new HashMap();
                    if (result1 != null && result1.size() > 0) {
                        for (int j = 0; j < result1.size(); j+=2) {
                            //  j = attachedNetworkName
                            //  j+1 = entranceMessage
							attachedNetworksArray.put((String)result1.get(j),(String)result1.get(j+1));
                        }
                        networkInfo.put("attachedNetworks", attachedNetworksArray);
                    }

                    // variables used in creating the arrayLists & HashMaps for the network NPCs
                    String npcIP = "";
                    String resource = "";
                    String npcName = "";
                    String npcTitle = "";
                    
                    // get the Store NPCs
                    String Qstore = "SELECT npc_ip, name, title FROM network_npc WHERE npc_type = 'store' AND network_id = " + networkId;
                    result1 = C.process(Qstore);
                    ArrayList storeHashArray = new ArrayList();
                    if (result1 != null && result1.size() > 0) {
                        for (int j = 0; j < result1.size(); j+=3) {
							HashMap storeNPCs = new HashMap();
                            npcIP = (String)result1.get(j);
                            npcName = (String)result1.get(j+1);
                            npcTitle = (String)result1.get(j+2);
                            storeNPCs.put("ip",npcIP);
							storeNPCs.put("name",npcName);
							storeNPCs.put("title",npcTitle);
							storeHashArray.add(storeNPCs);
						}
                    }
					networkInfo.put("storeNPCs",storeHashArray);
                    // because we have a link to the "Store" from the web browser, we need to set the storeIP
                    String networkStoreIP = "";
                    if (storeHashArray.size() > 0) {
                        networkStoreIP = (String)((HashMap)(storeHashArray.get(0))).get("ip");
                    }
System.out.println("network = " + networkName + ", storeNPC = " + networkStoreIP);
                    networkInfo.put("storeNPC", networkStoreIP);
                    
                    // get all the mining NPCs for this network
                    String Qmining = "SELECT npc_ip, resource, name, title FROM network_npc WHERE npc_type = 'mining' AND network_id = " + networkId;
                    result1 = C.process(Qmining);
                    ArrayList miningHashArray = new ArrayList();
                    if (result1 != null && result1.size() > 0) {
                        for (int j = 0; j < result1.size(); j+=4) {
							HashMap miningNPCs=new HashMap();
                            npcIP = (String)result1.get(j);
                            resource = (String)result1.get(j+1);
                            npcName = (String)result1.get(j+2);
                            npcTitle = (String)result1.get(j+3);
                            miningNPCs.put("ip",npcIP);
                            miningNPCs.put("commodity",resource);
							miningNPCs.put("name",npcName);
							miningNPCs.put("title",npcTitle);
							miningHashArray.add(miningNPCs);
                        }
                    }
					networkInfo.put("miningNPCs", miningHashArray);
					
                    // get all the attack NPCs for this network
                    String Qattack = "SELECT npc_ip, name, title FROM network_npc WHERE npc_type = 'attack' AND network_id = " + networkId;
                    result1 = C.process(Qattack);
					ArrayList attackHashArray=new ArrayList();
                    if (result1 != null && result1.size() > 0) {
                        for (int j = 0; j < result1.size(); j+=3) {
							HashMap attackNPCs = new HashMap();
                            npcIP = (String)result1.get(j);
                            npcName = (String)result1.get(j+1);
                            npcTitle = (String)result1.get(j+2);
                            attackNPCs.put("ip",npcIP);
							attackNPCs.put("name",npcName);
							attackNPCs.put("title",npcTitle);
							attackHashArray.add(attackNPCs);
						}
                    }
					networkInfo.put("attackNPCs",attackHashArray);
                    
                    // get all the quest NPCs for this network
                    String Qquest = "SELECT npc_ip, name, title FROM network_npc WHERE npc_type = 'quest' AND network_id = " + networkId;
                    result1 = C.process(Qquest);
					ArrayList questHashArray=new ArrayList();
                    if (result1 != null && result1.size() > 0) {
                        for (int j = 0; j < result1.size(); j+=3) {
							HashMap questNPCs = new HashMap();
                            npcIP = (String)result1.get(j);
                            npcName = (String)result1.get(j+1);
                            npcTitle = (String)result1.get(j+2);
                            questNPCs.put("ip",npcIP);
							questNPCs.put("name",npcName);
							questNPCs.put("title",npcTitle);
							questHashArray.add(questNPCs);
                        }
                    }
					networkInfo.put("questNPCs", questHashArray);
					networkInfo.put("attackProbability", attackProbability);
                    networkNodes.put(networkName, networkInfo);
                } //for each network
                
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return networkNodes;
        // create a class for each
        // stuff them in the hashmap, name is the key, values are the arrayLists of the NPCs
    }
 
	//MAIN FOR TESTING.
	public static void main(String[] args) {
		//Jon's testing stuff.
        /*
        Network myNetwork = Network.getInstance(MyComputerHandler);
        HashMap allNetworks = myNetwork.loadNetworks();
        HashSet keys = new HashSet(allNetworks.keySet());
        Iterator it = keys.iterator();
        while (it.hasNext()) {
            String key = (String)it.next();
            System.out.println("Network: " + key);
            HashMap currentNetwork = (HashMap)allNetworks.get(key);
            HashSet keys1 = new HashSet(currentNetwork.keySet());
            Iterator it1 = keys1.iterator();
            while (it1.hasNext()) {
                String newKey = (String)it1.next();
                System.out.println("**** " + newKey + " ****\n");
                Object x = currentNetwork.get(newKey);
                if (x instanceof String) {
                    System.out.println("      " + (String)x);
                }
                else if (x instanceof ArrayList) {
                    ArrayList npcs = (ArrayList)x;
                    for (int i = 0; i < npcs.size(); i++) {
                        HashMap npc = (HashMap)npcs.get(i);
                        Iterator npcIterator = npc.keySet().iterator();
                        while (npcIterator.hasNext()) {
                            String npcKey = (String)npcIterator.next();
                            System.out.println(npcKey + ": " + npc.get(npcKey)); 
                        }
                        System.out.println("-----------");
                    }
                }
            }
        }
        */
	/*	System.out.println(Network.getInstance(MyComputerHandler).switchNetwork("Root","The Subway","900.800.7.002"));
		Network.getInstance(MyComputerHandler).addToNetwork("Root","192.168.2.002");
		Network.getInstance(MyComputerHandler).addToNetwork("Root","192.168.2.012");
		Network.getInstance(MyComputerHandler).removeFromNetwork("Root","192.168.2.012");*/
	}

	//The thread for the network.
	 public void run(){
		while(true) {
		
			try {
				Collection c = networkNodes.values();
				Iterator itr = c.iterator();
				float attackRandomize = (float)Math.random();
				while(itr.hasNext()) {
					HashMap H = (HashMap)itr.next();
					float attackProbability = (Float)H.get("attackProbability");
					if (attackRandomize < attackProbability) {
				
						Object P[] = null;
						if(((HashMap)H.get("players")) != null) {
							P = ((HashMap)H.get("players")).values().toArray();
						}
				
						Object NPC[] = null;
						if(((ArrayList)H.get("attackNPCs")) != null) {
							NPC = ((ArrayList)H.get("attackNPCs")).toArray();
						}
				
						if (P != null) {
							int attackMe = (int)(Math.random() * P.length);
							int attackWithMe = (int)(Math.random() * NPC.length);
					
							Object Parameter[]=new Object[]{(String)((HashMap)NPC[attackWithMe]).get("ip")};
					
							try {
								MyComputerHandler.addData(new ApplicationData("launchNetworkAttack",Parameter,0, (String)P[attackMe]), (String)P[attackMe]);
							} catch (Exception e) {
								//No reason to print this.
							}
						}
					
					} 
				}
			
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			try {
				Thread.sleep(ATTACK_SLEEP);
			} catch(Exception e) {
				
			}
		}
	 }
 }
