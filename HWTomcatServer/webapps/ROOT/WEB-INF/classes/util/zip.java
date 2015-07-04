package util;
import com.plink.dolphinnet.*;
import java.util.*;
import java.util.zip.*;
import java.io.*;
import org.apache.axis.encoding.*;

/**
This tool is used to zip and unzip stuff.
*/

public class zip{

	/**
	Takes a string an dreturns a ziped base64 encoded version.
	*/
	public static String zipString(String data){
		String returnMe="";
		try{
			ByteArrayOutputStream bout=new ByteArrayOutputStream();
			ByteArrayOutputStream bout2=new ByteArrayOutputStream();
			ZipOutputStream zout=new ZipOutputStream(bout);
			zout.setLevel(9);
			ObjectOutputStream oout=new ObjectOutputStream(bout2);
			oout.writeObject(data);
			ByteArrayInputStream bin=new ByteArrayInputStream(bout2.toByteArray());

			zout.putNextEntry(new ZipEntry("Assignment"));

			byte buffer[]=new byte[512];
			int index;
			while((index=bin.read(buffer))>0)
				zout.write(buffer,0,index);

			zout.closeEntry();
			zout.close();
			
			returnMe=Base64.encode(bout.toByteArray());

		}catch(Exception e){
		
		}
		return(returnMe);
	}

	/**
	Takes a zipeed base 64 encoded string and returns the original string.
	*/
	public static String unzipString(String zipstring){
		try{
			byte[] zipbyte=Base64.decode(zipstring);
			ByteArrayOutputStream Buffer=new ByteArrayOutputStream();
			ZipInputStream in=new ZipInputStream(new ByteArrayInputStream(zipbyte));
			ZipEntry data=data=in.getNextEntry();
			if(data==null)
				return(null);
			int i=0;
			byte buffer[]=new byte[512];
			while((i=in.read(buffer))>0)
				Buffer.write(buffer,0,i);

			ObjectInputStream oin=new ObjectInputStream(new ByteArrayInputStream(Buffer.toByteArray()));
			return((String)oin.readObject());
		}catch(Exception e){
			e.printStackTrace();
		}
		return("");
	}

	//Testing main.
	/*public static void main(String args[]){";
		System.out.println(original.length());
		String after=zip.zipString(original);
		System.out.println(after.length());
		String finalString=zip.unzipString(after);
		System.out.println(finalString.length());
		System.out.println(finalString);
		
	}*/
}
