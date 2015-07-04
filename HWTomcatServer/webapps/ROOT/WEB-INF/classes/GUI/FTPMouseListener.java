package GUI;



import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import Assignments.*;
import View.*;

public class FTPMouseListener implements MouseListener{
	public final static int FROM=0;
	public final static int TO=1;
	private JList list;
	private FTP MyFTP;
	private Object directory[];
	private int type;
	
	public FTPMouseListener(JList list,FTP MyFTP,int type){
		this.list=list;
		this.MyFTP=MyFTP;
		this.type=type;
	}
	
	public void setDirectory(Object[] directory){
		this.directory=directory;
	}
	
	public void mouseEntered(MouseEvent e){

	}

	public void mouseExited(MouseEvent e){

	}

	public void mousePressed(MouseEvent e){

	}

	public void mouseReleased(MouseEvent e){

	}

	public void mouseClicked(MouseEvent e){
		int clickCount=e.getClickCount();
		if(clickCount==2){
			Object selected = directory[list.getSelectedIndex()];
			if(selected instanceof String){
				//System.out.println(selected);
				MyFTP.changeDirectory((String)selected,type);
			}
			else{
				if(type==FROM){
					MyFTP.setTransferType(FTP.PUT);
					//System.out.println("Put "+o[0]);
				}
				else if(type==TO){
					MyFTP.setTransferType(FTP.GET);
					//System.out.println("Get "+o[0]);
				}
				Object o[] = (Object[])selected;
				FTPQuantityDialog FQD = new FTPQuantityDialog(MyFTP,(int)(Integer)o[2],(String)o[0]);
				//MyFTP.getPanel().add(FQD);
				
			}
		}
			

	}


}
