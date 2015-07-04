package Game;

/**
FileSystem.java<br />
(c) Vulgate 2007

The file system for the hacker game.
*/
import java.util.*;
import java.io.*;
public class FileSystem{
	private HashMap root=new HashMap();
	private ArrayList FlatDirectory=new ArrayList();
	public static final int HD_CHART[]=new int[]{20,40,80,100,150,30,1000};
	private int HDType=0;
	private int quota=0;
	private Computer MyComputer=null;
	
	/**
	Set the type of HD installed.
	*/ 
	public void setHDType(int HDType){
		this.HDType=HDType;
	}
	
	/**
	Check whether the current type is better than the current type.
	*/
	public boolean checkType(int type){
		if(HD_CHART[type]>HD_CHART[HDType])
			return(true);
		return(false);
	}
	
	/**
	Get the type of HD installed.
	*/
	public int getHDType(){
		return(HDType);
	}
	
	/**
	Get the amount of files currently in the File System.
	*/
	public int getQuantity(){
		return(quota);
	}
	
	/**
	Return the maximum hard-drive space of this hard-drive.
	*/
	public int getMaximumSpace(){
		return(HD_CHART[HDType]+(int)MyComputer.getEquipmentSheet().getHDBonus());
	}	
	
	/**
	Returns the amount of freespace without bonuses.
	*/
	public int getSpaceMinusBonus(){
		return(HD_CHART[HDType]-quota+2);
	}
	
	/**
	Get the space left on the HD.
	*/
	public int getSpaceLeft(){
        // the +2 is for your Public and Shop FTP directories, which are hidden and don't count against your quota
        return (getMaximumSpace() - quota +2);
		//return(HD_CHART[HDType]-quota+2+(int)MyComputer.getEquipmentSheet().getHDBonus());
	}
	
	//Constructor.
	public FileSystem(Computer MyComputer){
		this.MyComputer=MyComputer;
	}
	
	/**
	Check whether the directory provided exists.
	*/
	public void updateFlatDirectory(String directory){	
			
		boolean add=true;
		Iterator I=FlatDirectory.iterator();
		while(I.hasNext()){
			String s=(String)I.next();
			if(s.indexOf(directory)>=0){
				add=false;
				break;
			}else if(directory.indexOf(s)>=0)
				I.remove();
		}
		if(add)
			FlatDirectory.add(directory);
	}

	/**
	Remove from flat.
	*/
	public void removeFlatDirectory(String directory){
		String fixedDirectory="";
		String data[]=directory.split("/");
		for(int i=0;i<data.length;i++){
			if(!data[i].equals("")){
				fixedDirectory+=data[i]+"/";
			}

		}
		directory=fixedDirectory;
	
		Iterator I=FlatDirectory.iterator();
		String addDirectory="";
		while(I.hasNext()){
			String s=(String)I.next();
			if(s.equals(directory)){
				I.remove();
				data=s.split("/");
				for(int i=0;i<data.length-1;i++){
					if(!data[i].equals("")){
						addDirectory+=data[i]+"/";
					}
				}
			}
		}
		if(!addDirectory.equals("")){
			updateFlatDirectory(addDirectory);
		}
	}
	
	/**
	Add a directory to the file system.
	*/
	public boolean addDirectory(String directory){
		if(directory==null||directory=="")
			return(false);
		
		if(directory!=null)
			if(directory.charAt(0)=='/'&&directory.length()>1)
				directory=directory.substring(1,directory.length());
	
		//Remove the directory from the flat directory listing.
		for(int i=0;i<FlatDirectory.size();i++){
			if(((String)FlatDirectory.get(i)).equals(directory)){
				return(false);
			}
		}
	
		if(!(quota-2<HD_CHART[HDType]+(int)MyComputer.getEquipmentSheet().getHDBonus()))
			return(false);
		quota++;
        
        // each part of the locationPath is just a name of a directory (not the entire path)
        // when a directory is passed in, it's passed with the entire path
		String locationPath[]=directory.split("/");
        
        // currentHashMap is the current directory listing
		HashMap currentHashMap=root;
		
		String directoryString="";
        
        // go through the entire path, traversing from directory to directory
		for(int i=0;i<locationPath.length;i++){
			if(!locationPath[i].equals("")){
				directoryString+=locationPath[i];
				directoryString+="/";
			}
			
			if(!locationPath[i].equals("")) {
                // if the current directory doesn't contain the next directory...
                if(currentHashMap.get(locationPath[i])==null) {	
                    // no idea what the fuck is going on here
                    if(i!=locationPath.length-1){
                        if(!(quota-2<HD_CHART[HDType]+(int)MyComputer.getEquipmentSheet().getHDBonus()))
                            return(false);
                        quota++;
                    }
                    
                    // add in the new directory, with an empty hashmap into this directory
                    HashMap NewMap=new HashMap();
                    currentHashMap.put(locationPath[i],NewMap);
                    currentHashMap=NewMap;
                } else {
                    // this directory contains the next? great.  switch directories.
                    if(currentHashMap.get(locationPath[i]) instanceof HashMap) {
                        currentHashMap=(HashMap)currentHashMap.get(locationPath[i]);
                    }
                }
            }
		}
		
		updateFlatDirectory(directoryString);
		
		return(true);
	}

	/**
	Delete a directory from the file system.
	*/
	public boolean deleteDirectory(String directory) {
        // try and delete everything in the folder you're deleting
        if (!deleteDirectoryRecursive(directory)) {
            return false;
        }
        removeFlatDirectory(directory);

        // remove this directory from it's root
		String locationPath[] = directory.split("/");
		HashMap currentDirectory = root;
		for(int i = 0; i < locationPath.length - 1; i++){
			if(!locationPath[i].equals(""))
				currentDirectory = (HashMap)currentDirectory.get(locationPath[i]);
		}
		if(locationPath.length > 0) {
            currentDirectory.remove(locationPath[locationPath.length-1]);
        }
        
        // add one to the total available space on the HD
        quota--;
        if(quota<0)//Make sure quota can't go below 0.
            quota=0;

        return true;
	}
    
    private boolean deleteDirectoryRecursive(String directory) {
		String locationPath[] = directory.split("/");
		HashMap currentDirectory = root;
		for(int i = 0; i < locationPath.length - 1; i++){
			if(!locationPath[i].equals(""))
				currentDirectory = (HashMap)currentDirectory.get(locationPath[i]);
		}
		if(locationPath.length > 0) {
            // get the final directory in the path that's passed in
			Object O = currentDirectory.get(locationPath[locationPath.length - 1]);
			if(O instanceof HashMap){
                HashMap directoryToDelete = (HashMap)O;
                // remove everything in the directory recursively
                Iterator it = (directoryToDelete.entrySet()).iterator();
                while (it.hasNext()) {
                    Map.Entry mapEntry =(Map.Entry)it.next();
                    Object o = mapEntry.getKey();
                    if(mapEntry.getValue() instanceof HashMap) {
                        // if it's a directory, delete it recursively
                        deleteDirectoryRecursive(directory + (String)o);
                        //Remove the directory from the flat directory listing.
                        removeFlatDirectory(directory + (String)o);
                    } else {
                        //deleted directory
                    }
                    
                    it.remove();
                    quota--;
                    if(quota<0)//Make sure quota can't go below 0.
                        quota=0;
                }
			}
		}
		return(true);
    
    }
    
	
	/**
	Add a file to the file system.
	*/
	public boolean addFile(HackerFile HF,boolean checkSpace){
		//Check to see whether the file has timed out.
		if(HF.getType()==HF.BOUNTY||HF.getType()==HF.PCI||HF.getType()==HF.AGP){
			String STimeOut=(String)HF.getContent().get("timeout");
			long timeOut=MyComputer.getCurrentTime();
			
			if(STimeOut==null||STimeOut.equals("")){
				HF.getContent().put("timeout",""+timeOut);
			}else{
				timeOut=new Long(STimeOut);
			}
			
			if(MyComputer.getCurrentTime()-timeOut>86400000&&(HF.getType()==HF.AGP||HF.getType()==HF.PCI)){
				if(MyComputer.getIP().equals("900.800.7.006")){
					return(true);
				}
			}
			
			if(MyComputer.getCurrentTime()-timeOut>604800000){
				if(MyComputer.getIP().equals("900.800.7.006")){
					return(true);
				}
			}
		}
		/////////////////////////////////////////////
	
		String locationPath[]=HF.getLocation().split("/");
		HashMap currentHashMap=root;
		for(int i=0;i<locationPath.length;i++){
			if(!locationPath[i].equals(""))
				if(currentHashMap.get(locationPath[i])!=null)
					currentHashMap=(HashMap)currentHashMap.get(locationPath[i]);
				else{
					return(false);
				}
		}
		if(currentHashMap!=null){		
			boolean Exists=false;
			if(currentHashMap.get(HF.getName())!=null)
				Exists=true;
			
			if(!Exists&&checkSpace){
				if(!(quota-2<HD_CHART[HDType]+(int)MyComputer.getEquipmentSheet().getHDBonus())){
					return(false);
				}
			}
		
			currentHashMap.put(HF.getName(),HF);
			if(!Exists)
				quota++;
				
			return(true);
		}
		return(false);
	}
	
	/**
	Get a file from the file system.
	*/
	public HackerFile getFile(String path,String name){	
	
		String locationPath[]=path.split("/");
		HashMap currentHashMap=root;
		for(int i=0;i<locationPath.length;i++){
			if(!locationPath[i].equals("")){
				currentHashMap=(HashMap)currentHashMap.get(locationPath[i]);
				if(currentHashMap==null)
					return(null);
			}
		}
		
		return((HackerFile)currentHashMap.get(name));
	}
	
	/**
	Delete a file from the file system.
	*/
	public HackerFile deleteFile(String path,String name) {
		String locationPath[]=path.split("/");
		HashMap currentHashMap=root;
		for(int i=0;i<locationPath.length;i++){
			if(!locationPath[i].equals("")){
				currentHashMap=(HashMap)currentHashMap.get(locationPath[i]);
				if(currentHashMap==null)
					return(null);
			}
		}
		
		if(currentHashMap.get(name)==null)//Make sure we found the file.
			return(null);
			
		quota--;
		if(quota<0)//Make sure quota can't go below 0.
			quota=0;
		return((HackerFile)currentHashMap.remove(name));
	}
	
	/**
	Return an array representing a single directory in the file system.
	*/
	public Object[] getDirectory(String path){
		String locationPath[]=path.split("/");
		HashMap currentHashMap=root;
		for(int i=0;i<locationPath.length;i++){
			if(!locationPath[i].equals("")){
				currentHashMap=(HashMap)currentHashMap.get(locationPath[i]);
				if(currentHashMap==null)
					return(null);
			}
		}
		
		//Place the data into an array.
		Object ReturnMe[]=new Object[currentHashMap.size()];
		Iterator DirectoryIterator=currentHashMap.entrySet().iterator();
		int i=0;
		while(DirectoryIterator.hasNext()){
			Map.Entry ME=(Map.Entry)DirectoryIterator.next();
			Object o=null;
			if(ME.getValue() instanceof HashMap)
				o=ME.getKey();
			else{
				HackerFile HF=(HackerFile)ME.getValue();
				Object O[]=new Object[9];
				O[0]=HF.getName();
				O[1]=new Integer(HF.getType());
				O[2]=new Integer(HF.getQuantity());
				O[3]=new Float(HF.getPrice());
				O[4]=HF.getMaker();
				O[5]=HF.getCPUCost();
				O[6]=HF.getDescription();
				if(HF.getType()==HackerFile.NEW_FIREWALL){
					HashMap content = HF.getContent();
					Object priceObject = content.get("store_price");
					if(priceObject == null){
						O[7] = 0.0f;
					}else{
						if((""+priceObject).equals("")||(""+priceObject).equals("null")){
							priceObject = "0.0";
						}
						float price = new Float(""+priceObject);
						O[7] = price;
					}
					O[8] = HF.getContent();
				}
				else if(Computer.makers.containsKey(HF.getMaker())){
					float price = (Float)Computer.makers.get(HF.getMaker());
					O[7]=price;
					O[8] = null;
				}
				else{
					O[7] = 0.0f;
					O[8] = null;
				}
				o=O;
			}
			ReturnMe[i]=o;
			i++;
		}
		return(ReturnMe);
	}
	
	/**
	Return an array representing a single directory in the file system.
	*/
	public Object[] getWebDirectory(String path){
		String locationPath[]=path.split("/");
		HashMap currentHashMap=root;
		for(int i=0;i<locationPath.length;i++){
			if(!locationPath[i].equals("")){
				currentHashMap=(HashMap)currentHashMap.get(locationPath[i]);
				if(currentHashMap==null)
					return(null);
			}
		}
		
		//Place the data into an array.
		Object ReturnMe[]=new Object[currentHashMap.size()];
		Iterator DirectoryIterator=currentHashMap.entrySet().iterator();
		int i=0;
		while(DirectoryIterator.hasNext()){
			Map.Entry ME=(Map.Entry)DirectoryIterator.next();
			Object o=null;
			if(ME.getValue() instanceof HashMap)
				o=ME.getKey();
			else{
				HackerFile HF=(HackerFile)ME.getValue();
				if(HF!=null){
					if(HF.getType()!=HackerFile.BOUNTY&&HF.getType()!=HackerFile.AGP&&HF.getType()!=HackerFile.PCI&&HF.getType()!=HackerFile.HD&&HF.getType()!=HackerFile.MEMORY&&HF.getType()!=HackerFile.CPU&&HF.getType()!=HackerFile.NEW_FIREWALL){
						HF=HF.clone();
						HF.setContent(null);
					}
				}
				o=HF;
			}
			ReturnMe[i]=o;
			i++;
		}
		return(ReturnMe);
	}
	
	/**
	Return an array representing a single directory in the file system.
	*/
	public Object[] getScanDirectory(String path){
		String locationPath[]=path.split("/");
		HashMap currentHashMap=root;
		for(int i=0;i<locationPath.length;i++){
			if(!locationPath[i].equals("")){
				currentHashMap=(HashMap)currentHashMap.get(locationPath[i]);
				if(currentHashMap==null)
					return(null);
			}
		}
		
		//Place the data into an array.
		Object ReturnMe[]=new Object[currentHashMap.size()];
		Iterator DirectoryIterator=currentHashMap.entrySet().iterator();
		int i=0;
		while(DirectoryIterator.hasNext()){
			Map.Entry ME=(Map.Entry)DirectoryIterator.next();
			Object o=null;
			if(ME.getValue() instanceof HashMap)
				o=ME.getKey();
			else{
				HackerFile HF=(HackerFile)ME.getValue();
				o=HF;
			}
			ReturnMe[i]=o;
			i++;
		}
		return(ReturnMe);
	}
	
	
	/**
	Returns an array of equipment files.
	*/
	public Object[] getEquipment(String path){
		String locationPath[]=path.split("/");
		HashMap currentHashMap=root;
		for(int i=0;i<locationPath.length;i++){
			if(!locationPath[i].equals("")){
				currentHashMap=(HashMap)currentHashMap.get(locationPath[i]);
				if(currentHashMap==null)
					return(null);
			}
		}
		
		//Place the data into an array.
		Object ReturnMe[]=new Object[currentHashMap.size()];
		Iterator DirectoryIterator=currentHashMap.entrySet().iterator();
		int i=0;
		while(DirectoryIterator.hasNext()){
			Map.Entry ME=(Map.Entry)DirectoryIterator.next();
			Object o=null;
			if(ME.getValue() instanceof HashMap)
				o=ME.getKey();
			else{
				HackerFile HF=(HackerFile)ME.getValue();
				o=HF;
			}
			if(o instanceof HackerFile)
			if(((HackerFile)o).getType()==HackerFile.AGP||((HackerFile)o).getType()==HackerFile.PCI)
				ReturnMe[i]=o;
			else
				ReturnMe[i]=null;
			i++;
		}
		return(ReturnMe);
	}
		
	/**
	Output the file system in XML format.
	*/
	public String outputXML(){
		String returnMe="<files>\n";
		ArrayList TempArrayList=new ArrayList();
		HashMap WorkingHashMap=null;
		TempArrayList.add(root);
		
		//Output directories.
		for(int i=0;i<FlatDirectory.size();i++){
			if(((String)FlatDirectory.get(i))!=null)
				returnMe+="<directory><![CDATA["+((String)FlatDirectory.get(i)).replaceAll("]]>","]]&gt;")+"]]></directory>\n";
			else
				returnMe+="<directory><![CDATA["+((String)FlatDirectory.get(i))+"]]></directory>\n";
		}
		
		do{
			WorkingHashMap=(HashMap)TempArrayList.get(0);
			TempArrayList.remove(0);
		
			Iterator DirectoryIterator=WorkingHashMap.entrySet().iterator();
			while(DirectoryIterator.hasNext()){
				Object o=((Map.Entry)DirectoryIterator.next()).getValue();
				if(o instanceof HashMap){
					TempArrayList.add(o);
				}else{
					returnMe+=((HackerFile)o).outputXML();
				}
			}
			
		}while(TempArrayList.size()>0);
		
		returnMe+="</files>\n";
		return(returnMe);
	}
	
	/**
	Returns all the files in the root directory of a given type.
	*/
	public ArrayList getFilesOfType(int FileType){
		ArrayList returnMe=new ArrayList();
		Object rootFiles[]=this.getScanDirectory("");
		for(int i=0;i<rootFiles.length;i++){
			if(rootFiles[i] instanceof HackerFile){
				HackerFile HF=(HackerFile)rootFiles[i];
				if(HF.getType()==FileType){
					returnMe.add(HF);
				}
			}
		}
		return(returnMe);
	}
	
	/**
	Returns all the files in the root directory of a given type.
	*/
	public ArrayList getFiles(){ 
		ArrayList returnMe=new ArrayList();
		Object rootFiles[]=this.getScanDirectory("");
		for(int i=0;i<rootFiles.length;i++){
			if(rootFiles[i] instanceof HackerFile){
				HackerFile HF=(HackerFile)rootFiles[i];
				returnMe.add(HF);
			}
		}
		return(returnMe);
	}
}
