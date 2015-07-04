package GUI;

import javax.imageio.*;
import java.awt.image.*;
import java.util.zip.*;
import org.apache.axis.encoding.*;
import java.io.*;

public class Tile{
	
	private byte[] imageData;
	private BufferedImage image;
	private int id=0;
	private byte ri[]=null;
	private byte gi[]=null;
	private byte bi[]=null;
	

	public Tile(byte[] imageData,int id,byte ri[],byte gi[],byte bi[]){
		this.ri=ri;
		this.gi=gi;
		this.bi=bi;
		this.imageData=imageData;
		this.id=id;
		image = new BufferedImage(64,64,BufferedImage.TYPE_BYTE_INDEXED,new IndexColorModel(8,256,ri,gi,bi));
		byte b[] = ((DataBufferByte)(image).getRaster().getDataBuffer()).getData();
		try{
		for(int i=0;i<b.length;i++)
			b[i]=imageData[i];
		}catch(Exception e){}
	}
	
	public Tile(BufferedImage image,int id,byte ri[],byte gi[],byte bi[]){
		this.ri=ri;
		this.gi=gi;
		this.bi=bi;
		this.image=image;
		this.id=id;
		imageData = ((DataBufferByte)(image).getRaster().getDataBuffer()).getData();
	}
	
	public BufferedImage getImage(){
		return image;
	}
	
	public byte[] getImageData(){
		return imageData;
	}
	
	public Tile clone(){
		return new Tile(imageData,id,ri,gi,bi);
	}
	
	public String getXML(){
		String xml="";
		xml+="<tile><id>"+id+"</id>";
		try{
			ByteArrayOutputStream bout=new ByteArrayOutputStream();
			ZipOutputStream zout=new ZipOutputStream(bout);
		
			zout.setLevel(9);
			zout.putNextEntry(new ZipEntry("map"));
			zout.write(imageData,0,imageData.length);
			zout.closeEntry();
			zout.close();
			String info=Base64.encode(bout.toByteArray());
			
			String pal="";
			{//Output the palette in zipped byte encoded form.
				bout=new ByteArrayOutputStream();
				zout=new ZipOutputStream(bout);
			
				zout.setLevel(9);
				zout.putNextEntry(new ZipEntry("map"));
				//zout.write((byte)trans);
				zout.write(ri,0,ri.length);
				zout.write(gi,0,gi.length);
				zout.write(bi,0,bi.length);

				zout.closeEntry();
				zout.close();
				pal=Base64.encode(bout.toByteArray());
			}
			xml+="<palette><![CDATA["+pal+"]]></palette>";
						
			xml+="<data><![CDATA["+info+"]]></data></tile>";
			//for(int ii=0;ii<B.length;ii++)
			//	xml+=B[ii];
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return xml;
	}


}
