package GUI;

import java.util.*;
import java.util.zip.*;
import org.apache.axis.encoding.*;
import java.io.*;

public class Script{
	public static int MAP=0;
	public static int SPRITE=1;
	
	private String name="";
	private HashMap scripts=null;
	private int type=0;
	private int id=0;
	
	public Script(String name,HashMap scripts,int type,int id){
		this.name=name;
		this.scripts=scripts;
		this.type=type;
		this.id=id;
	}
	
	public String getName(){
		return name;
	}
	
	public HashMap getScripts(){
		return scripts;
	}
	
	public int getType(){
		return type;
	}
	
	public void setScripts(HashMap scripts){
		this.scripts=scripts;
	}
	
	public void setName(String name){
		this.name=name;
	}
	
	public Script clone(){
		return new Script(name,scripts,type,id);
	}
	
	public String getXML(){
		
		String xml="<script><id>"+id+"</id><name><![CDATA["+name+"]]></name>";
		xml+="<type>"+type+"</type><data>";
		try{
			//HashMap HM = script.getScripts();
			if(type==MAP){
				ByteArrayOutputStream bout=new ByteArrayOutputStream();
				ZipOutputStream zout=new ZipOutputStream(bout);
				byte b[] = ((String)scripts.get("initialize")).getBytes();
				zout.setLevel(9);
				zout.putNextEntry(new ZipEntry("script"));
				zout.write(b,0,b.length);
				zout.closeEntry();
				zout.close();
				String info=Base64.encode(bout.toByteArray());
				xml+="<initialize><![CDATA["+info+"]]></initialize>";
				
				bout=new ByteArrayOutputStream();
				zout=new ZipOutputStream(bout);
				b = ((String)scripts.get("continue")).getBytes();
				zout.setLevel(9);
				zout.putNextEntry(new ZipEntry("script"));
				zout.write(b,0,b.length);
				zout.closeEntry();
				zout.close();
				info=Base64.encode(bout.toByteArray());
				xml+="<continue><![CDATA["+info+"]]></continue>";
				
				bout=new ByteArrayOutputStream();
				zout=new ZipOutputStream(bout);
				b = ((String)scripts.get("finalize")).getBytes();
				zout.setLevel(9);
				zout.putNextEntry(new ZipEntry("script"));
				zout.write(b,0,b.length);
				zout.closeEntry();
				zout.close();
				info=Base64.encode(bout.toByteArray());
				xml+="<finalize><![CDATA["+info+"]]></finalize>";
			}
			else if(type==SPRITE){
				ByteArrayOutputStream bout=new ByteArrayOutputStream();
				ZipOutputStream zout=new ZipOutputStream(bout);
				byte b[] = ((String)scripts.get("fire")).getBytes();
				zout.setLevel(9);
				zout.putNextEntry(new ZipEntry("script"));
				zout.write(b,0,b.length);
				zout.closeEntry();
				zout.close();
				String info=Base64.encode(bout.toByteArray());
				xml+="<fire><![CDATA["+info+"]]></fire>";
			}
			xml+="</data></script>";
			
			//for(int ii=0;ii<B.length;ii++)
			//	xml+=B[ii];
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return xml;
	}
}
