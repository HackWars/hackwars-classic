package com.plink.dolphinnet.assignments;
import com.plink.dolphinnet.*;
import java.util.*;
import java.util.zip.*;
import java.io.*;

/**
This assignment is passed an assignment as a constructor and zips it.
Used for distributing large assignments more efficiently.
*/

public class ZippedAssignment extends Assignment implements Serializable{
	///////////////////////////////
	// Data.
	private byte file[]=null;

	/////////////////////////
	// Constructor.
	public ZippedAssignment(int id,Assignment ZAssignment){
		super(id);
		loadFile(ZAssignment);
	}
	/////////////////////////
	// Getters.
	public Assignment getAssignment(){
		try{
			ByteArrayOutputStream Buffer=new ByteArrayOutputStream();
			ZipInputStream in=new ZipInputStream(new ByteArrayInputStream(file));
			ZipEntry data=data=in.getNextEntry();
			if(data==null)
				return(null);
			int i=0;
			byte buffer[]=new byte[512];
			while((i=in.read(buffer))>0)
				Buffer.write(buffer,0,i);

			ObjectInputStream oin=new ObjectInputStream(new ByteArrayInputStream(Buffer.toByteArray()));
			return((Assignment)oin.readObject());
		}catch(Exception e){
			e.printStackTrace();
		}
		return(null);
	}

	private void loadFile(Assignment ZAssignment){
		try{
			ByteArrayOutputStream bout=new ByteArrayOutputStream();
			ByteArrayOutputStream bout2=new ByteArrayOutputStream();
			ZipOutputStream zout=new ZipOutputStream(bout);
			zout.setLevel(9);
			ObjectOutputStream oout=new ObjectOutputStream(bout2);
			oout.writeObject(ZAssignment);
			ByteArrayInputStream bin=new ByteArrayInputStream(bout2.toByteArray());

			zout.putNextEntry(new ZipEntry("Assignment"));

			byte buffer[]=new byte[512];
			int index;
			while((index=bin.read(buffer))>0)
				zout.write(buffer,0,index);

			zout.closeEntry();
			zout.close();

			file=bout.toByteArray();
		}catch(Exception e){
			e.printStackTrace();
		}
	}


	/** Run the assignments implemented task. */
	public Object execute(DataHandler DH){
		finish();
		return(null);
	}
}
