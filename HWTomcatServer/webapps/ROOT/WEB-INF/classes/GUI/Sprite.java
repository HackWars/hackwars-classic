package GUI;

import java.awt.image.*;
import java.awt.*;
import java.util.zip.*;
import org.apache.axis.encoding.*;
import java.io.*;

public class Sprite{
	
	private byte[][] data = new byte[8][];
	private int trans=0;
	private int id=0;
	private BufferedImage[] images = new BufferedImage[8];
	private byte r[]=null;
	private byte g[]=null;
	private byte b[]=null;
	
	/**
	Set the unique RGB palette used for this sprite.
	*/
	public void setRGB(byte r[],byte g[],byte b[]){
		this.r=r;
		this.g=g;
		this.b=b;
	}
	
	/**
	Create the sprite with this ID.
	*/
	public Sprite(int id){
		this.id=id;
	}
	
	/**
	Create a new sprite from a buferred image.
	*/
	public Sprite(BufferedImage image,int trans,int id){
		this.trans=trans;
		this.id=id;
		
		int maxi = Math.min(2,image.getWidth()/64);
		int maxii = Math.min(4,image.getHeight()/64);
		for(int i=0;i<maxi;i++){
			for(int ii=0;ii<maxii;ii++){
				int index=i+ii*2;
				
				IndexColorModel MyIndexColorModel=(IndexColorModel)image.getColorModel();
				
				byte ri[]=new byte[256];
				byte gi[]=new byte[256];
				byte bi[]=new byte[256];
				for(int p=0;p<256;p++){
					int c[]=MyIndexColorModel.getComponents(p,null,0);
					ri[p]=(byte)c[0];
					gi[p]=(byte)c[1];
					bi[p]=(byte)c[2];
				}
				
				BufferedImage img = new BufferedImage(64,64,BufferedImage.TYPE_BYTE_INDEXED,new IndexColorModel(8,256,ri,gi,bi));
				
				Graphics g = img.createGraphics();
				BufferedImage piece = image.getSubimage(i*64,ii*64,64,64);
				byte[] b = ((DataBufferByte)(img).getRaster().getDataBuffer()).getData();
				byte[] B =  ((DataBufferByte)(piece).getRaster().getDataBuffer()).getData();
				int ind=0;
				for(int y=ii*64;y<ii*64+64;y++){
					for(int x=i*64;x<i*64+64;x++){
						int in = x+y*image.getWidth();
						b[ind]=B[in];
						ind++;
					}
				}
				images[index]=img;
				
				
				ByteArrayOutputStream bout=new ByteArrayOutputStream();
				bout.write(trans);
				bout.write(b,0,b.length);
				
				data[index] = bout.toByteArray();
			}
		}
		
	}
	
	public void setFrame(byte[] data,int frame){
		//System.out.println("original data: "+data.length);
		this.data[frame]=data;
		ByteArrayInputStream bin = new ByteArrayInputStream(data);
		trans = bin.read();
		int size=0;
		byte buffer[]=new byte[512];
		try{
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			while((size=bin.read(buffer))!=-1){
				bout.write(buffer,0,size);
			}
			data = bout.toByteArray();
			//this.data[frame]=data;
		}catch(Exception e){
			e.printStackTrace();
		}
		//System.out.println("data: "+data.length);
		BufferedImage img=null;
		if(trans!=255)
			img = new BufferedImage(64,64,BufferedImage.TYPE_BYTE_INDEXED,new IndexColorModel(8,256,r,g,b,trans));
		else
			img = new BufferedImage(64,64,BufferedImage.TYPE_BYTE_INDEXED,new IndexColorModel(8,256,r,g,b));
		
		byte[] imageData = ((DataBufferByte)(img).getRaster().getDataBuffer()).getData();
		//System.out.println("imagedata: "+imageData.length);
		try{
		for(int i=0;i<imageData.length;i++)
			imageData[i]=data[i];
		}catch(Exception e){}
		images[frame]=img;
		
	}
	
	public BufferedImage getFrame(int frame){
		return images[frame];
		
	}
	
	public byte[][] getData(){
		return data;
	}
	
	public String getXML(){
		
		String xml="<sprite><id>"+id+"</id><frames>";
		
		
		
		try{
		
			IndexColorModel MyIndexColorModel=(IndexColorModel)images[0].getColorModel();
			
			byte ri[]=new byte[256];
			byte gi[]=new byte[256];
			byte bi[]=new byte[256];
			for(int p=0;p<256;p++){
				int c[]=MyIndexColorModel.getComponents(p,null,0);
				ri[p]=(byte)c[0];
				gi[p]=(byte)c[1];
				bi[p]=(byte)c[2];
			}
			
			String pal="";
			{//Output the palette in zipped byte encoded form.
				ByteArrayOutputStream bout=new ByteArrayOutputStream();
				ZipOutputStream zout=new ZipOutputStream(bout);
			
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
		
			for(int ii=0;ii<data.length;ii++){
				byte[] b = data[ii];
				ByteArrayOutputStream bout=new ByteArrayOutputStream();
				ZipOutputStream zout=new ZipOutputStream(bout);
			
				zout.setLevel(9);
				zout.putNextEntry(new ZipEntry("map"));
				//zout.write((byte)trans);
				if(b!=null)
					zout.write(b,0,b.length);
				zout.closeEntry();
				zout.close();
				String info=Base64.encode(bout.toByteArray());
				//System.out.println(bout.toByteArray().length);
				xml+="<frame><data><![CDATA["+info+"]]></data></frame>";
			}
			xml+="</frames></sprite>";
			
			//for(int ii=0;ii<B.length;ii++)
			//	xml+=B[ii];
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return xml;
	}

}
