package GUI;


import java.util.*;
import java.util.concurrent.*;
import View.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class StatsList implements Runnable{
	private ArrayList tasks = new ArrayList();
	private Hacker MyHacker;
	private Thread MyThread = null;
	private final Semaphore available = new Semaphore(1, true);
	public StatsList(Hacker MyHacker){
		this.MyHacker=MyHacker;
		MyThread = new Thread(this);
		MyThread.start();
		
	}
	
	public void add(String message){
		tasks.add(new StatTask(message));
	}
	
	class StatTask implements Task,Runnable{
		private String message;
		private Thread MyThread=null;
		public StatTask(String message){
			this.message=message;
		}
		
		public void execute(){
			
			if(MyHacker.getStatsPanel().getStatus()==StatsPanel.DOWN){
				MyThread = new Thread(this);
				MyThread.start();
			}
			
				
				//System.out.println(message);
			
		}
		
		public void run(){
			JDesktopPane panel = MyHacker.getPanel();
			
			JLabel label = new JLabel(message);
			int x = panel.getWidth()-label.getPreferredSize().width-30;
			int ystart=20;
			int y = ystart;
			int width = label.getPreferredSize().width;
			int height = label.getPreferredSize().height;
			Color start = Color.white;
			Color end = new Color(41,42,41);
			panel.add(label);
			label.setForeground(start);
			int yend = 150;
			//System.out.println(end.getRed()+"  "+end.getGreen()+"  "+end.getBlue());
			//System.out.println(start.getRed()+"  "+start.getGreen()+"  "+start.getBlue());
			while(y<yend){
				int red = end.getRed()+(int)(((float)start.getRed()-(float)end.getRed())*(((float)yend-(float)(y-ystart))/(float)yend));
				int blue = end.getBlue()+(int)(((float)start.getBlue()-(float)end.getBlue())*(((float)yend-(float)(y-ystart))/(float)yend));
				int green = end.getGreen()+(int)(((float)start.getGreen()-(float)end.getGreen())*(((float)yend-(float)(y-ystart))/(float)yend));
				//System.out.println(red+"  "+green+"  "+blue);
				label.setBounds(x,y,width,height);
				label.setForeground(new Color(red,green,blue));
				label.repaint();
				y+=10;
				try{
					MyThread.sleep(100);
				}catch(Exception e){}
			}
			label.setVisible(false);
		}
	}	
	
	public void run(){
		while(true){
			try{
				available.acquire();
				Iterator MyIterator=tasks.iterator();
				StatTask st=null;
				if(MyIterator.hasNext()){
					st=(StatTask)MyIterator.next();
					MyIterator.remove();
					st.execute();
				}
				
				
				MyThread.sleep(500);
			}catch(Exception e){}
			finally{
				available.release();
			}
		}
		
	}





}
