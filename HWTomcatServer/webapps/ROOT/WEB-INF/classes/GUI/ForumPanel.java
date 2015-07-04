package GUI;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.undo.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.*;
import View.*;
import Game.*;
import java.io.*;
import java.net.*;
import org.w3c.dom.Node;
import org.w3c.dom.NamedNodeMap;
import util.*;
import javax.swing.text.html.HTMLEditorKit;
import Browser.*;
import java.net.*;

public class ForumPanel extends HtmlHandler{
	
	public final static int CATEGORY=0;
	public final static int THREADS=1;
	public final static int THREAD=2;
	public final static int PROFILE=3;
	
	private Rectangle size;
	private WebBrowser MyWebBrowser;
	private Hacker MyHacker;
	private JTable table;
	private String url;
	private String[][] ids;
	private int[] type = new int[10];
	private int index=0;
	private String[] id = new String[10];
	private URL[] page = new URL[10];
	
	public ForumPanel(String url,Rectangle size,WebBrowser MyWebBrowser,Hacker MyHacker){
		this.size=size;
		this.url=url;
		this.MyWebBrowser=MyWebBrowser;
		this.MyHacker=MyHacker;
		//setLayout(null);
		//setBackground(Color.white);
		//createView();
		//showCategories();
		setParent(this);
		try{
			URL conn = new URL(url+"categories.php");
			parseDocument(conn,(JInternalFrame)MyWebBrowser);
		}catch(Exception e){e.printStackTrace();}
	}
	
	public void showCategories(){
		//removeAll();
		//repaint();
		try{
			URL conn = new URL(url+"categories.php");
			parseDocument(conn,MyWebBrowser);
		}catch(Exception e){e.printStackTrace();}
		//System.out.println("DOCUMENT: -----------------------------------"+stuff);
		
		
		/*try{
			LoadXML LX = new LoadXML();
			LX.loadURL(url+"categories.php");
			Node N = null;
			String headers[] = {"Title","Topics"};
			String data[][] = {};
			ForumTableModel tableModel = new ForumTableModel(data,headers);
			table = new JTable(tableModel);
			ForumTableCellRenderer renderer = new ForumTableCellRenderer(this,MyHacker);
			table.getColumnModel().getColumn(0).setCellRenderer(renderer);
			table.getColumnModel().getColumn(1).setCellRenderer(renderer);
			table.getColumnModel().getColumn(0).setPreferredWidth(480);
			table.getColumnModel().getColumn(1).setPreferredWidth(30);
			table.addMouseListener(this);
			table.setDragEnabled(false);
			//table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			JPanel pp;
			JLabel l;
			int i=0,tableHeight=10;
			String rowData[] = new String[2];
			//add(table);
			JScrollPane sp = new JScrollPane(table);
			add(sp);
			ids = new String[20][2];
			while((N=LX.findNodeRecursive("category",i))!=null){
				Node G = N.getAttributes().getNamedItem("id");
				ids[i][0]=G.getNodeValue();
				Node temp = LX.findNodeRecursive(N,"name",0);
				temp = LX.findNodeRecursive(temp,"#text",0);
				rowData[0]=temp.getNodeValue();
				temp = LX.findNodeRecursive(N,"topics",0);
				temp = LX.findNodeRecursive(temp,"#text",0);
				rowData[1]=temp.getNodeValue();
				tableModel.addRow(rowData);
				i++;
			}
			sp.setBounds(5,5,size.width-75,table.getPreferredSize().height+20);
			
		}catch(Exception e){e.printStackTrace();}*/
		
	}
	
	public void showThreads(){
		//removeAll();
		//repaint();
		//type[index]=THREADS;
		//this.id[index]=id;
		String link=url+"viewthread.php?id="+id[index];
		try{
			parseDocument(page[index],MyWebBrowser);
		}catch(Exception e){e.printStackTrace();}
		/*try{
			LoadXML LX = new LoadXML();
			LX.loadURL(url+"threads.php?id="+id[index]);
			Node N = null;
			String headers[] = {"Title","Replies","Creator","Date Created"};
			String data[][] = {};
			ForumTableModel tableModel = new ForumTableModel(data,headers);
			table = new JTable(tableModel);
			ForumTableCellRenderer renderer = new ForumTableCellRenderer(this,MyHacker);
			table.getColumnModel().getColumn(0).setCellRenderer(renderer);
			table.getColumnModel().getColumn(1).setCellRenderer(renderer);
			table.getColumnModel().getColumn(2).setCellRenderer(renderer);
			table.getColumnModel().getColumn(3).setCellRenderer(renderer);
			table.getColumnModel().getColumn(0).setPreferredWidth(275);
			table.getColumnModel().getColumn(1).setPreferredWidth(40);
			table.getColumnModel().getColumn(2).setPreferredWidth(110);
			table.getColumnModel().getColumn(3).setPreferredWidth(110);
			table.addMouseListener(this);
			//table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			table.setDragEnabled(false);
			JPanel pp;
			JLabel l;
			int i=0,tableHeight=10;
			String rowData[] = new String[4];
			//add(table);
			JScrollPane sp = new JScrollPane(table);
			add(sp);
			ids = new String[20][2];
			while((N=LX.findNodeRecursive("thread",i))!=null){
				Node G = N.getAttributes().getNamedItem("id");
				ids[i][0]=G.getNodeValue();
				Node temp = LX.findNodeRecursive(N,"name",0);
				temp = LX.findNodeRecursive(temp,"#text",0);
				rowData[0]=temp.getNodeValue();
				temp = LX.findNodeRecursive(N,"replies",0);
				temp = LX.findNodeRecursive(temp,"#text",0);
				rowData[1]=temp.getNodeValue();
				temp = LX.findNodeRecursive(N,"creator",0);
				G = temp.getAttributes().getNamedItem("id");
				ids[i][1]=G.getNodeValue();
				temp = LX.findNodeRecursive(temp,"#text",0);
				rowData[2]=temp.getNodeValue();
				temp = LX.findNodeRecursive(N,"date",0);
				temp = LX.findNodeRecursive(temp,"#text",0);
				rowData[3]=temp.getNodeValue();
				tableModel.addRow(rowData);
				i++;
			}
			sp.setBounds(5,5,size.width-75,table.getPreferredSize().height+20);
			Dimension d = new Dimension(size.width-75,table.getPreferredSize().height+30);
			setPreferredSize(d);
			ForumLink f = new ForumLink(this,id[index],"Create Thread",ForumLink.CREATE_THREAD);
			add(f);
			f.setBounds(40,getPreferredSize().height,f.getPreferredSize().width,f.getPreferredSize().height);
			d = new Dimension(getPreferredSize().width,getPreferredSize().height+15+f.getPreferredSize().height);
			setPreferredSize(d);
		}catch(Exception e){e.printStackTrace();}*/
	}
	
	public void showThread(){
		//removeAll();
		//repaint();
		/*prevType=this.type;
		this.type=THREAD;
		prevID=this.id;
		this.id=id;*/
		try{
			parseDocument(page[index],MyWebBrowser);
		}catch(Exception e){e.printStackTrace();}
		/*try{
			LoadXML LX = new LoadXML();
			LX.loadURL(link);
			Node N = null;
			JPanel pp;
			JLabel l;
			int i=0,tableHeight=10;
			while((N=LX.findNodeRecursive("post",i))!=null){
				Node idN = N.getAttributes().getNamedItem("id");
				String postID = idN.getNodeValue();
				pp=new JPanel();
				pp.setLayout(null);
				int w=2,h=2;
				Node temp=LX.findNodeRecursive(N,"name",0);
				temp=LX.findNodeRecursive(temp,"#text",0);
				l=new JLabel(temp.getNodeValue());
				JPanel ppp=new JPanel();
				ppp.setLayout(null);
				ppp.add(l);
				l.setBounds(w,h,l.getPreferredSize().width,l.getPreferredSize().height);
				temp = LX.findNodeRecursive(N,"date",0);
				temp=LX.findNodeRecursive(temp,"#text",0);
				JLabel l2=new JLabel("@"+temp.getNodeValue());
				ppp.add(l2);
				l2.setBounds(w,h+3+l.getPreferredSize().height,l2.getPreferredSize().width,l2.getPreferredSize().height);
				ppp.setBackground(MyHacker.getColour());
				//l.setForeground(Color.white);
				temp=LX.findNodeRecursive(N,"author",0);
				Node G = temp.getAttributes().getNamedItem("id");
				temp = LX.findNodeRecursive(temp,"#text",0);
				ForumLink b = new ForumLink(this,G.getNodeValue(),temp.getNodeValue(),ForumLink.PROFILE);
				b.setBounds(size.width-125-b.getPreferredSize().width,h-5,b.getPreferredSize().width,b.getPreferredSize().height);
				ppp.add(b);
				if(temp.getNodeValue().equals(MyHacker.getUsername())){
						b = new ForumLink(this,postID,"",ForumLink.EDIT);
						ppp.add(b);
						b.setBounds(size.width-125-b.getPreferredSize().width,h+l.getPreferredSize().height-3,b.getPreferredSize().width,b.getPreferredSize().height);
				}
				//l.setForeground(Color.white);
				
				pp.add(ppp);
				ppp.setBounds(w,h,size.width-124,l.getPreferredSize().height+6+l2.getPreferredSize().height);
				
				h+=l.getPreferredSize().height+8+l2.getPreferredSize().height;
			
				temp=LX.findNodeRecursive(N,"text",0);
				temp=LX.findNodeRecursive(temp,"#text",0);
				JEditorPane ta = new JEditorPane();
				HTMLEditorKit m_kit = new HTMLEditorKit();
				ta.setEditorKit(m_kit);
				String message = temp.getNodeValue();
				message = message.replaceAll("\\:\\)","<img src=\""+url+"images/smiley.png\">");
				message = message.replaceAll("\\:D","<img src=\""+url+"images/bigsmile.png\">");
				message = message.replaceAll("\\:p","<img src=\""+url+"images/tongue.png\">");
				message = message.replaceAll("\\:\\(","<img src=\""+url+"images/frown.png\">");
				message = message.replaceAll("\\;\\)","<img src=\""+url+"images/wink.png\">");
				ta.setText(message);
			
				ta.setEditable(false);
				int rows = temp.getNodeValue().length()/50+1;
				
				pp.add(ta);
				ta.setPreferredSize(new Dimension(size.width,rows*20));
				ta.setBounds(w,h,size.width-125,rows*20);
				h+=ta.getPreferredSize().height+5;
				
				//pp.setSize(size.width-120,l.getPreferredSize().height);
				pp.setBounds(40,tableHeight,size.width-120,h);
				tableHeight+=15+h;
				pp.setBorder(BorderFactory.createLineBorder(Color.black));
				pp.setBackground(Color.white);
				add(pp);
				Dimension d = new Dimension(size.width-120,tableHeight);
				setPreferredSize(d);
				i++;
			}
			ForumLink f = new ForumLink(this,id[index],"Reply",ForumLink.REPLY);
			add(f);
			f.setBounds(40,getPreferredSize().height,f.getPreferredSize().width,f.getPreferredSize().height);
			Dimension d = new Dimension(getPreferredSize().width,getPreferredSize().height+15+f.getPreferredSize().height);
			setPreferredSize(d);
		}catch(Exception e){e.printStackTrace();}*/
		
	}
	
	public void showProfile(){
		//System.out.println("Showing Profile for id: "+id[index]);
	}
	
	public void goBack(){
		if(index!=0){
			index--;
			try{
				parseDocument(page[index],MyWebBrowser);
			}catch(Exception e){e.printStackTrace();}
		}
	}
	public void goForward(){
		if(index!=10&&id[index+1]!=null){
			index++;
			try{
				parseDocument(page[index],MyWebBrowser);
			}catch(Exception e){e.printStackTrace();}
		}
	}
	public void resize(){
		try{
			if(page[index]!=null)
				parseDocument(page[index],MyWebBrowser);
		}catch(Exception e){e.printStackTrace();}
	}
	
	public void increaseIndex(){
		index++;
	}
	
	public void setType(int type){
		this.type[index]=type;
	}
	
	public void setID(String id){
		this.id[index]=id;
	}
	
	public void edit(String id){
		String link=url+"edit.php?id="+id;
		try{
			LoadXML LX = new LoadXML();
			LX.loadURL(link);
			Node N = null;
			JPanel pp;
			JLabel l;
			int i=0,tableHeight=10;
			N = LX.findNodeRecursive("message",0);
			N = LX.findNodeRecursive(N,"#text",0);
			String message = N.getNodeValue();
			ForumEditDialog FED = new ForumEditDialog(this,message,id);
				
		}catch(Exception e){e.printStackTrace();}
		
	}
	
	public void editMessage(String message,String id){
		message = URLEncoder.encode(message+"\n");
		String link=url+"editmessage.php?id="+id+"&message="+message;
		//System.out.println(link);
		try{
			LoadXML LX = new LoadXML();
			LX.loadURL(link);
			Node N = null;
			JPanel pp;
			JLabel l;
			int i=0,tableHeight=10;
			if(LX.findNodeRecursive("error",0)==null){
				showThread();	
			}
			else{
				System.out.println("ERROR WITH EDITING");
			}
				
		}catch(Exception e){e.printStackTrace();}
	}
	
	public void reply(String message){
		message = URLEncoder.encode(message);
		String username = URLEncoder.encode(MyHacker.getUsername());
		String link=url+"reply.php?id="+id[index]+"&message="+message+"&user="+username;
		try{
			LoadXML LX = new LoadXML();
			LX.loadURL(link);
			Node N = null;
			JPanel pp;
			JLabel l;
			int i=0,tableHeight=10;
			if(LX.findNodeRecursive("error",0)==null){
				showThread();	
			}
			else{
				System.out.println("ERROR WITH REPLYING");
			}
				
		}catch(Exception e){e.printStackTrace();}
	}
	
	public void createNewThread(String message,String subject){
		message = URLEncoder.encode(message);
		subject = URLEncoder.encode(subject);
		String username = URLEncoder.encode(MyHacker.getUsername());
		String link=url+"newthread.php?id="+id[index]+"&message="+message+"&user="+username+"&subject="+subject;
		//System.out.println(link);
		try{
			LoadXML LX = new LoadXML();
			LX.loadURL(link);
			Node N = null;
			JPanel pp;
			JLabel l;
			int i=0,tableHeight=10;
			if(LX.findNodeRecursive("error",0)==null){
				showThreads();	
			}
			else{
				System.out.println("ERROR WITH CREATING NEW THREAD");
			}
				
		}catch(Exception e){e.printStackTrace();}
	}
	public void shiftIndex(){
		for(int i=1;i<10;i++){
			page[i-1]=page[i];
		}
		index=9;
	}
	
	public void setLink(URL link){
		String name = link.getPath();
		String[] name2 = name.split("/");
		name = name2[name2.length-1];
		//System.out.println("FileName: "+name);
		String query="";
		//Systemout.println(
		if(name.equals("profile.php")){
			String user = link.getQuery().split("=")[1];
			PersonalSettings PS = new PersonalSettings(user,MyHacker);
			MyHacker.getPanel().add(PS);
			PS.setVisible(true);
			PS.moveToFront();
			return;
		}
		if(!name.equals("categories.php")){
			query = link.getQuery().split("=")[1];
		}
		//System.out.println("Query: "+query);
		String username = URLEncoder.encode(MyHacker.getUsername());
		try{
			if(!name.equals("categories.php")){
				link = new URL(url+name+"?id="+query+"&user="+username);
			}
			else{
				link = new URL(url+name);
			}
		}catch(MalformedURLException e){}
		if(!name.equals("reply.php")&&!name.equals("newthread.php")){
			index++;
			if(index==10)
				shiftIndex();
			page[index]=link;
			id[index]=query;
			try{
				parseDocument(link,MyWebBrowser);
			}catch(Exception e){e.printStackTrace();}
		}
		else if(name.equals("reply.php")){
			ForumReplyDialog FRD = new ForumReplyDialog(this,MyWebBrowser);
		}
		else if(name.equals("newthread.php")){
			ForumCreateThreadDialog FCTD = new ForumCreateThreadDialog(this,MyWebBrowser);
			
		}
		
	}

}
