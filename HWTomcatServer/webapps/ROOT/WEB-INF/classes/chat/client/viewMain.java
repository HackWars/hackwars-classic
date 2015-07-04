

package chat.client;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.TreeMap;

import chat.util.StringCompare;

public class viewMain extends JPanel implements absViewChannelList,ChangeListener,MouseListener,MouseMotionListener{
    

    private static Dimension preferedSize = new Dimension(600,300);
    private ChatController controller = null;
    private JTabbedPane channelTabs = null;
    private TreeMap<String, absViewChannel> channelListing = new TreeMap<String, absViewChannel>(new StringCompare());
    private int x,y;
    
    
    /** Creates a new instance of viewMain */
    public viewMain(ChatController controller,Dimension preferredSize) throws Exception{
        this.controller = controller;
        this.preferedSize=preferredSize;
        this.setPreferredSize(preferedSize);
        this.setLayout(null);
        this.setSize(preferedSize);
        setBackground(new Color(41,42,41));
        guiTabs();
        setVisible(true);
        validate();
    }
    
    public void redoSize(Dimension newSize){
    	setSize(newSize);
    	setPreferredSize(newSize);
    	preferedSize=newSize;
    	channelTabs.setBounds(0,0,preferedSize.width-8,preferedSize.height-1);
    	validate();
    }
    
    private JTabbedPane guiTabs(){
        channelTabs = new JTabbedPane();
        
        GridBagConstraints c = new GridBagConstraints();
         //x,y postion
        c.gridx =0;
        c.gridy =0;
         //size on grid
        c.gridheight = 1;
        c.gridwidth = 1;
         //paddinging, ignored with expand I think,
        c.ipadx = 50;
        c.ipady = 50;
         //Expand in the x and y directions? 1 = full, 0 = no, inbetween is wierd
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.fill = c.BOTH;
        
        this.add(channelTabs);
    	channelTabs.setBounds(0,0,preferedSize.width-8,preferedSize.height-1);
    	channelTabs.addChangeListener(this);
    	channelTabs.addMouseListener(this);
    	channelTabs.addMouseMotionListener(this);
        return channelTabs;
    }        
    
    /**
     * The VIEW creates a new channel
     *  -- The view should create a new window/tab to display incoming chat messages 
     */
    public absViewChannel addChannel(String channelName) throws Exception{
        viewChatWindow view;
        try{
            view = new viewChatWindow(controller, channelName);
        }catch(Exception e){
            throw e;
        }
        channelListing.put(channelName, view);
        channelTabs.addTab(channelName, view);
		if(channelName.equals("General-0")){
				channelTabs.setSelectedIndex(channelTabs.indexOfTab(channelName));
		}
        return view;
    }
    
    /**
     * The VIEW stops displaying a channel
     *  -- The view should remove the window/tab that is displaying messages
     */
    public void removeChannel(String channelName) throws Exception{
        viewChatWindow view = (viewChatWindow) channelListing.get(channelName);
        if(view == null){
            throw new Exception("viewMain.removeChannel("+channelName+") Gui ERROR! no such view in tree");
        }
        
        int index = getTabIndex(channelName);  
        if(index < 0){
            throw new Exception("viewMain.removeChannel("+channelName+") Gui ERROR! no such tab");
        }
        
        channelTabs.removeTabAt(index);
        try{
            channelListing.remove(channelName);
        }catch(Exception e){
            throw new Exception("viewMain.removeChannel("+channelName+") Gui ERROR! no such postion in hash");
        }
    }
    
    /**
     * The view should return the abstract channel
     */
    public absViewChannel getChannel(String channelName){
        return channelListing.get(channelName);
    }
    
    private int getTabIndex(String s){
        int loopc = 0;
        while(loopc < channelTabs.getTabCount()){
            String cur =channelTabs.getTitleAt(loopc);
            if(cur.compareToIgnoreCase(s) == 0){
                return loopc;
            }
            loopc++;
        }
        return -1;
    }
    
    public TreeMap getChannelListing(){
	    return(channelListing);
    }
    
    public void messageReceived(String channelName){
	    //System.out.println("Channels: "+channelListing.size());
	    if(getTabIndex(channelName)!=channelTabs.getSelectedIndex()){
		    channelTabs.setForegroundAt(getTabIndex(channelName),Color.red);
	    }
    }
    
    public void stateChanged(ChangeEvent e){
	    //System.out.println("State Changed");
	    channelTabs.setForegroundAt(channelTabs.getSelectedIndex(),null);
        viewChatWindow x = (viewChatWindow)channelTabs.getComponentAt(channelTabs.getSelectedIndex());
        x.autoScroll();
    }
    
    	public void mouseClicked(MouseEvent e){
	}
	
	public void mouseEntered(MouseEvent e){
	}
	
	public void mouseExited(MouseEvent e){
	}
	
	public void mousePressed(MouseEvent e){
		this.x=e.getX();
		this.y=e.getY();
		//System.out.println("Clicked Y: "+e.getY());
	}
	public void mouseReleased(MouseEvent e){
	}

	public void mouseDragged(MouseEvent e){
		int mx = e.getX();
		int my = e.getY();
		//System.out.println("Dragged Y: "+my);
		if(Math.abs(my-y)>5){
			//System.out.println("Resize");
			//setBounds(0,getBounds().y+(my-y),getBounds().width,5);
			if(getBounds().height-(my-y)>53){
				redoSize(new Dimension(getBounds().width,getBounds().height-(my-y)));
				setBounds(0,getBounds().y+(my-y),getBounds().width,getBounds().height);
				validate();
			}
		}
	}
	
	public void mouseMoved(MouseEvent e){
		
	}
}
