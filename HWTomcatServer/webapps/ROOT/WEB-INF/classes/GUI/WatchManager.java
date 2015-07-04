package GUI;
/**

Withdraw.java
this is the deposit window.
*/

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import Assignments.*;
import View.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.Semaphore;

public class WatchManager extends Application implements FocusListener {
	public final int ROW_HEIGHT = 30;
	public final String[] WATCH_TYPES = Constants.watchTypes;
	private JDesktopPane mainPanel=null;
	private Hacker MyHacker=null;
	private View MyView = null;
	private JPanel panel;
	private PacketWatch[] watches, oldWatches;
    private String oldNotes[] = new String[21];	
	private JTextField noteField[] = new JTextField[21];
	private ImageIcon removeIcon = ImageLoader.getImageIcon("images/dummyoff.png");
    private static Font f = new Font("Dialog",Font.PLAIN,12);
	private Semaphore semaphore = new Semaphore(1, true);
	private int deleteCount=0;
    // number of columns in the watch manager
    static final int numColumns = 11;
    JComponent[][] components;
    EventListener eventListeners[][];
    
    // watch types
    private static int HEALTH = 0;
    private static int PETTY_CASH = 1;
    private static int SCAN = 2; 
    
    //components
    public static final int NUMBER = 0;
    public static final int ON_OFF = 1;
    public static final int PORT = 2;
    public static final int TYPE = 3;
    public static final int CPU_COST = 4;
    public static final int NOTE = 5;
    public static final int OBSERVED_PORTS = 6;
    public static final int EDIT_OBSERVED_PORTS_BUTTON = 10;
    public static final int SEARCH_FIREWALL = 7;
    public static final int VALUE = 8;
    public static final int DELETE = 9;
    
    
	public WatchManager(String name,boolean resize,boolean max,boolean close,boolean iconify,JDesktopPane mainPanel,Hacker MyHacker){
		this.setTitle(name);
		this.setResizable(resize);
		this.setMaximizable(max);
		this.setClosable(close);
		this.setIconifiable(iconify);
		this.addInternalFrameListener(this);
		this.mainPanel=mainPanel;
		this.MyHacker=MyHacker;
		this.setFrameIcon(ImageLoader.getImageIcon("images/watch.png"));
		MyView = MyHacker.getView();
		String ip=MyHacker.getEncryptedIP();
		Object objects[] = {ip};
		MyView.setFunction("fetchwatches");
		MyView.addFunctionCall(new RemoteFunctionCall(0,"fetchwatches",objects));
	}

	public void populate(){
		/*setType();
		setOnOff();
		setCPUCost();
		setFireWall();
		setNote();*/
		panel= new JPanel();
		createMenu();
		Insets insets = this.getInsets();
		panel = new JPanel();
		JScrollPane scrollPane = new JScrollPane(panel);
		this.add(scrollPane);
		
		scrollPane.setBounds(insets.left+5,insets.top+5,this.getBounds().width-22,this.getHeight()-65);
		panel.setLayout(null);
		Insets panelInsets = panel.getInsets();
		Dimension d,labelSize;
		JLabel label;
		
		panel.setPreferredSize(new Dimension(getBounds().width-50,100));
		
	}
	
	public void createMenu(){
		JMenuBar menuBar = new JMenuBar();
		JMenu menu;
		JMenuItem menuItem;
		
		menu = new JMenu("File");
		
		menuItem = new JMenuItem("Install New Watch",ImageLoader.getImageIcon("images/watch.png"));
		menu.add(menuItem);
		menuItem.addActionListener(this);
		
		menuItem = new JMenuItem("Exit",ImageLoader.getImageIcon("images/exit.png"));
		menu.add(menuItem);
		menuItem.addActionListener(this);
		
		menuBar.add(menu);
		this.setJMenuBar(menuBar);
	}
    
	public void receivedWatches(PacketWatch watches[]){
        // this will be null iff it's the first time this is called, so we need to cache all the values locally to compare at a later date
		if (this.watches == null) {
            //panel.removeAll();
            //panel.repaint();
            setupWatchManager();
            components = new JComponent[numColumns][watches.length];
            eventListeners = new EventListener[numColumns][watches.length];
        }
		//System.out.println("Watches.length: "+watches.length);
		//System.out.println("Components Length: "+components[NUMBER].length);
        //This is to take care of timing related issues. If i delete two watches before i receive the watches from the first delete it would cause arraysize problems.
        if(watches.length==components[NUMBER].length){
            this.watches = watches;
            populateWatches();
        }

       
	}
	
	public void populateWatches(){
    
        Insets insets = this.getInsets();
		Insets panelInsets = panel.getInsets();
		Dimension labelSize;
        JLabel label;
        
		int height = panelInsets.top+30;
		JButton button,saveButton;
		JTextField textField;
		JComboBox cb;
		JComboBox portComboBox;
		JComboBox typeComboBox;
		JComboBox firewallComboBox;
		JSpinner valueSpinner;
        
        // the max width of the Observed Ports (N/A) is 47 pixels
        int maxWidth = 47;
            
        //get available ports for the memory type
		int memType = MyHacker.getMemoryType();
		String[] availablePorts = new String[Constants.memoryTypeMaxPorts[memType]];
		for (int i=0; i < Constants.memoryTypeMaxPorts[memType]; i++) {
			availablePorts[i] = ""+ i;
		}
		
		int ports[];
		String[] portValues;
		String portDisplay="";
        
        // loop through and set up the watches, one at a time
		for(int i=0;i<watches.length;i++){
        
            //  WATCH NUMBER
            if (components[NUMBER][i] == null) {
                components[NUMBER][i] = new JLabel(""+i);
    			panel.add(components[NUMBER][i]);
    			portDisplay="";
            }
            ((JLabel)components[NUMBER][i]).setText(""+i);
            labelSize = components[NUMBER][i].getPreferredSize();
            components[NUMBER][i].setBounds(panelInsets.left+2,height+5,labelSize.width,labelSize.height);
            
            // ON/OFF button
            if (components[ON_OFF][i] == null) {
                components[ON_OFF][i] = new JButton();
    			((JButton)components[ON_OFF][i]).setBorderPainted(false);
    			((JButton)components[ON_OFF][i]).setContentAreaFilled(false);
    			((JButton)components[ON_OFF][i]).setOpaque(false);
                WatchOnOffActionListener al = new WatchOnOffActionListener(i,this,((JButton)components[ON_OFF][i]));
                ((JButton)components[ON_OFF][i]).addActionListener(al);
                eventListeners[ON_OFF][i] = al;
    			panel.add(components[ON_OFF][i]);
            }
            
            if(watches[i].getOn()){
                ((JButton)components[ON_OFF][i]).setIcon(ImageLoader.getImageIcon("images/on.png"));
            } else {
                ((JButton)components[ON_OFF][i]).setIcon(ImageLoader.getImageIcon("images/off.png"));
            }
            
			labelSize = components[ON_OFF][i].getPreferredSize();
			components[ON_OFF][i].setBounds(panelInsets.left+12,height,labelSize.width,labelSize.height);
            
            // PORT
            if (components[PORT][i] == null) {
    			if(watches[i].getType() != SCAN){
    				components[PORT][i] = new JComboBox(availablePorts);
    				int port = watches[i].getPort();
    				if(port > Constants.memoryTypeMaxPorts[memType]-1)
    					port = Constants.memoryTypeMaxPorts[memType]-1;
    				if(port < 0)
    					port=0;
                    ((JComboBox)components[PORT][i]).setSelectedIndex(port);
                    
    			}
    			else{
    				components[PORT][i] = new JComboBox(new String[]{"N/A"});
    				components[PORT][i].setEnabled(false);
    			}
                watchPortComboBoxActionListener al = new watchPortComboBoxActionListener(i, MyHacker);
                ((JComboBox)components[PORT][i]).addActionListener(al);
                eventListeners[PORT][i] = al;
                panel.add(components[PORT][i]);
            } 
            
            labelSize = components[PORT][i].getPreferredSize();
            components[PORT][i].setBounds(panelInsets.left+70,height+5,maxWidth,labelSize.height);
            components[PORT][i].repaint();
			
            // OBSERVED PORTS for this watch
            ports = watches[i].getObservedPorts();
			portValues = new String[ports.length];
			if(ports.length>0){
				for(int j=0;j<ports.length;j++){
					portValues[j]=String.valueOf(ports[j]);
				}
			}

            // TYPES
            if (components[TYPE][i] == null) {
    			components[TYPE][i] = new JComboBox(WATCH_TYPES);
    			
    			((JComboBox)components[TYPE][i]).setSelectedIndex(watches[i].getType());
                watchTypeComboBoxActionListener al = new watchTypeComboBoxActionListener(i, MyHacker);
                ((JComboBox)components[TYPE][i]).addActionListener(al);
                eventListeners[TYPE][i] = al;
    			panel.add(components[TYPE][i]);
            } else {
                // change the PORT & OBSERVED PORTS column, if type == scan
                if (watches[i].getType() != oldWatches[i].getType()) {
                    if (watches[i].getType() == SCAN) {
                        populateComboBoxStrings((JComboBox)components[PORT][i], new String[] {"N/A"});
                        populateComboBoxStrings((JComboBox)components[OBSERVED_PORTS][i], new String[] {"N/A"});
                        ((JComboBox)components[PORT][i]).setEnabled(false);
                        ((JComboBox)components[OBSERVED_PORTS][i]).setEnabled(false);
                        ((JButton)components[EDIT_OBSERVED_PORTS_BUTTON][i]).setEnabled(false);
                        
                    } else {
                        populateComboBoxStrings((JComboBox)components[PORT][i], availablePorts);
                        populateComboBoxStrings((JComboBox)components[OBSERVED_PORTS][i], portValues);
                        ((JComboBox)components[PORT][i]).setEnabled(true);
                        ((JComboBox)components[OBSERVED_PORTS][i]).setEnabled(true);
                        ((JButton)components[EDIT_OBSERVED_PORTS_BUTTON][i]).setEnabled(true);
                    }
                    panel.remove(components[VALUE][i]);
                    panel.repaint();
                    components[VALUE][i] = createValueComponent(watches[i].getQuantity(), watches[i].getType(), i);
                    panel.add(components[VALUE][i]);
                }
             
            }
            labelSize = components[TYPE][i].getPreferredSize();
            components[TYPE][i].setBounds(panelInsets.left+125,height+5,labelSize.width,labelSize.height);
            components[TYPE][i].repaint();
			
            // CPU cost
            if (components[CPU_COST][i] == null) {
    			components[CPU_COST][i] = new JLabel(String.valueOf(watches[i].getCPUCost()));
    			components[CPU_COST][i].setFont(f);
    			panel.add(components[CPU_COST][i]);
            }
            labelSize = components[CPU_COST][i].getPreferredSize();
            components[CPU_COST][i].setBounds(panelInsets.left+225,height+5,labelSize.width,labelSize.height);

            // NOTE
            String note = watches[i].getNote();
            if (components[NOTE][i] == null) {
                /* watch notes are now autosaving using a focuslistener */
    			components[NOTE][i] = new JTextField(note,8);
    			panel.add(components[NOTE][i]);
    			components[NOTE][i].addFocusListener(this);
            }
            labelSize = components[NOTE][i].getPreferredSize();
            components[NOTE][i].setBounds(panelInsets.left+275,height+5,110,labelSize.height);
            oldNotes[i] = note;
            noteField[i] = (JTextField)components[NOTE][i];
			
            // OBSERVED PORTS
            if (components[OBSERVED_PORTS][i] == null) {
    			if(watches[i].getType()!=SCAN){
    				components[OBSERVED_PORTS][i] = new JComboBox(portValues);
    			}
    			else{
    				components[OBSERVED_PORTS][i] = new JComboBox(new String[]{"N/A"});
    				components[OBSERVED_PORTS][i].setEnabled(false);
    			}
    			((JComboBox)components[OBSERVED_PORTS][i]).setEditable(false);
    			((JComboBox)components[OBSERVED_PORTS][i]).setMaximumRowCount(5);
    			panel.add(components[OBSERVED_PORTS][i]);
                
                // EDIT OBSERVED PORTS button
    			components[EDIT_OBSERVED_PORTS_BUTTON][i] = new JButton(ImageLoader.getImageIcon("images/edit.png"));
    			((JButton)components[EDIT_OBSERVED_PORTS_BUTTON][i]).setBorderPainted(false);
    			((JButton)components[EDIT_OBSERVED_PORTS_BUTTON][i]).setContentAreaFilled(false);
    			panel.add(components[EDIT_OBSERVED_PORTS_BUTTON][i]);
    			watchObservedPortsActionListener al = new watchObservedPortsActionListener(i,this);
                ((JButton)components[EDIT_OBSERVED_PORTS_BUTTON][i]).addActionListener(al);
                eventListeners[EDIT_OBSERVED_PORTS_BUTTON][i] = al;
    			if(watches[i].getType()==SCAN){
    				((JButton)components[EDIT_OBSERVED_PORTS_BUTTON][i]).setEnabled(false);
    			}
            }
            populateComboBoxStrings((JComboBox)components[OBSERVED_PORTS][i],portValues);
			labelSize = components[OBSERVED_PORTS][i].getPreferredSize();
			components[OBSERVED_PORTS][i].setBounds(panelInsets.left+400,height+5,maxWidth,labelSize.height);
			components[EDIT_OBSERVED_PORTS_BUTTON][i].setBounds(panelInsets.left+400+maxWidth+2,height+8,16,16);
            // FIREWALLS 
            if (components[SEARCH_FIREWALL][i] == null) {
    			components[SEARCH_FIREWALL][i] = new JComboBox(Constants.watchFirewalls);
    			labelSize = components[SEARCH_FIREWALL][i].getPreferredSize();
    			((JComboBox)components[SEARCH_FIREWALL][i]).setSelectedIndex(watches[i].getSearchFireWall());
                watchFirewallComboBoxActionListener al = new watchFirewallComboBoxActionListener(i, MyHacker);
    			((JComboBox)components[SEARCH_FIREWALL][i]).addActionListener(al);
    			eventListeners[SEARCH_FIREWALL][i] = al;
                
    			panel.add(components[SEARCH_FIREWALL][i]);
            }
            components[SEARCH_FIREWALL][i].setBounds(panelInsets.left+485,height+5,142,labelSize.height);
            // VALUE 
            if (components[VALUE][i] == null) {
                components[VALUE][i] = createValueComponent(watches[i].getQuantity(), watches[i].getType(), i);
    			panel.add(components[VALUE][i]);
            }
            
			labelSize = components[VALUE][i].getPreferredSize();
			components[VALUE][i].setBounds(panelInsets.left+655,height+5,100,labelSize.height);
			
            //DELETE button
            if (components[DELETE][i] == null) {
    			components[DELETE][i] = new JButton(removeIcon);
    			((JButton)components[DELETE][i]).setBorderPainted(false);
    			((JButton)components[DELETE][i]).setContentAreaFilled(false);
    			panel.add(components[DELETE][i]);
                watchRemoveActionListener al = new watchRemoveActionListener(i,this);
                ((JButton)components[DELETE][i]).addActionListener(al);
                eventListeners[DELETE][i] = al;
            }
            components[DELETE][i].setBounds(panelInsets.left+730+maxWidth+2,height+8,16,16);
			height+=ROW_HEIGHT;
            
		}
		panel.setPreferredSize(new Dimension(this.getBounds().width-52,height+20));
		revalidate();
        
        this.oldWatches = this.watches;
        
	}
	
    private void populateComboBoxStrings(JComboBox cb, String[] values) {
        cb.removeAllItems();
        for (int i = 0; i < values.length; i++) {
            cb.addItem(values[i]);
        }
    }

    private JComponent createValueComponent(float q, int type, int i) {
        JComponent component=null;
        watchQuantityFocusListener al = new watchQuantityFocusListener(i,MyHacker,q);
        eventListeners[VALUE][i] = al;
        if(type==HEALTH){
            if(q>100.0f)
                q=100.0f;
            if(q<0.0f)
                q=0.0f;
            SpinnerModel model = new SpinnerNumberModel(q,0,100.0,1.0);
            JSpinner spinner = new JSpinner(model); 
            JFormattedTextField c = (JFormattedTextField)spinner.getEditor().getComponents()[0];
            c.addFocusListener(al);
            component = spinner;
        } else if(type==PETTY_CASH) {
            JTextField tf = new JTextField(""+watches[i].getQuantity());
            
            tf.addFocusListener(al);
            tf.setHorizontalAlignment(JTextField.RIGHT);
            component = tf;
            
        }
        else if(type==SCAN){
            component = new JLabel("When Scanned");
            component.setFont(f);
        }
        
        return component;
    }

            
    public void setupWatchManager() {
		Insets insets = this.getInsets();
		Insets panelInsets = panel.getInsets();
		Dimension labelSize;
		JLabel label;
        
		label =new JLabel("#");
		panel.add(label);
		labelSize = label.getPreferredSize();
		label.setBounds(panelInsets.left+2,panelInsets.top+5,labelSize.width,labelSize.height);
		
		label =new JLabel("On/Off");
		panel.add(label);
		labelSize = label.getPreferredSize();
		label.setBounds(panelInsets.left+12,panelInsets.top+5,labelSize.width,labelSize.height);
		
		label = new JLabel("Port");
		panel.add(label);
		labelSize = label.getPreferredSize();
		label.setBounds(panelInsets.left+70,panelInsets.top+5,labelSize.width,labelSize.height);
		
		label =new JLabel("Type");
		panel.add(label);
		labelSize = label.getPreferredSize();
		label.setBounds(panelInsets.left+125,panelInsets.top+5,labelSize.width,labelSize.height);
		
		label =new JLabel("CPU Cost");
		panel.add(label);
		labelSize = label.getPreferredSize();
		label.setBounds(panelInsets.left+200,panelInsets.top+5,labelSize.width,labelSize.height);
		
		label =new JLabel("Note");
		panel.add(label);
		labelSize = label.getPreferredSize();
		label.setBounds(panelInsets.left+320,panelInsets.top+5,labelSize.width,labelSize.height);
		
		label = new JLabel("Observed Ports");
		panel.add(label);
		labelSize = label.getPreferredSize();
		label.setBounds(panelInsets.left+380,panelInsets.top+5,labelSize.width,labelSize.height);
		
		label = new JLabel("Search FireWall");
		panel.add(label);
		labelSize = label.getPreferredSize();
		label.setBounds(panelInsets.left+480,panelInsets.top+5,labelSize.width,labelSize.height);
		
		label = new JLabel("Value");
		panel.add(label);
		labelSize = label.getPreferredSize();
		label.setBounds(panelInsets.left+650,panelInsets.top+5,labelSize.width,labelSize.height);
		
		label = new JLabel("Delete");
		panel.add(label);
		labelSize = label.getPreferredSize();
		label.setBounds(panelInsets.left+760,panelInsets.top+5,labelSize.width,labelSize.height);
		
    }
    
	public JPanel getPanel(){
		return(panel);
	}
	
	public void setOnOff(int index){
		boolean set = !watches[index].getOn();
		View MyView = MyHacker.getView();
		Object objects[] = {MyHacker.getEncryptedIP(),new Integer(index),new Boolean(set)};
		MyView.setFunction("setwatchonoff");
		MyView.addFunctionCall(new RemoteFunctionCall(0,"setwatchonoff",objects));
	}
	
	public void setQuantity(int watchID,float quantity){
		Object objects[] = {MyHacker.getEncryptedIP(),new Integer(watchID),new Float(quantity)};
		View MyView = MyHacker.getView();
		MyView.setFunction("setwatchquantity");
		MyView.addFunctionCall(new RemoteFunctionCall(0,"setwatchquantity",objects));
	}
	
	public void setFireWall(int watchID,int firewall){
		Object objects[] = {MyHacker.getEncryptedIP(),new Integer(watchID),new Integer(firewall)};
		//System.out.println(firewall);
		View MyView = MyHacker.getView();
		MyView.setFunction("setwatchsearchfirewall");
		MyView.addFunctionCall(new RemoteFunctionCall(0,"setwatchsearchfirewall",objects));
	}
	
	public void setObservedPorts(int watchID,Integer[] ports){
		Object objects[] = {MyHacker.getEncryptedIP(),new Integer(watchID),ports};
		View MyView = MyHacker.getView();
		MyView.setFunction("setwatchobservedports");
		MyView.addFunctionCall(new RemoteFunctionCall(0,"setwatchobservedports",objects));
	}
		
	public void install(String folder,String fileName,String type,int port){
		View MyView = MyHacker.getView();
		Integer sendType =  new Integer(0);
		if(type.equals("Health")){
				sendType=new Integer(0);
		}
		if(type.equals("Petty Cash")){
				sendType=new Integer(1);
		}
		if(type.equals("Scan")){
			sendType=new Integer(2);
		}
		Object objects[] = {MyHacker.getEncryptedIP(),folder,fileName,sendType,new Integer(port)};
		MyView.setFunction("installwatch");
		MyView.addFunctionCall(new RemoteFunctionCall(0,"installwatch",objects));
        
        //add row to components array
        addRow();
        
	}
	
	
    // 2 methods required to implement FocusListener
    public void focusGained(FocusEvent e) {
    }
    
    public void focusLost(FocusEvent e) {
        int index = -1;
        for (int i = 0; i < watches.length; i++) {
            if (noteField[i] == (JTextField)(e.getSource()) ) {
                index = i;
                break;
            }
        }
        
        // if the note has changed, save it
        if (index != -1 && noteField[index] != null && oldNotes[index] != null) {
            if (!oldNotes[index].equals(noteField[index].getText())) {
                saveNote(noteField[index].getText(), index);
            }
            
            oldNotes[index] = noteField[index].getText();
        }
    }
	
	public void saveNote(String note,int index){
		//System.out.println("SAVING THE NOTE: " + note + " at index = " + index);
		Object objects[] = {MyHacker.getEncryptedIP(),index,note};
		MyView.setFunction("setwatchnote");
		MyView.addFunctionCall(new RemoteFunctionCall(0,"setwatchnote",objects));
	}
	
	public void internalFrameClosed(InternalFrameEvent e) {
		MyHacker.setWatchOpen(false);
	}

	public void actionPerformed(ActionEvent e){
		if(e.getActionCommand().equals("Install New Watch")){
			WatchInstallFileChooser WIFC = new WatchInstallFileChooser(MyHacker,MyHacker.getCurrentFolder(),MyHacker.getCurrentDirectory(),this);
			mainPanel.add(WIFC);
			WIFC.moveToFront();
		}
	}
    
    public void deleteWatch(int index) {
		if(MyHacker.getStatsPanel().getCPULoadIcon().getPercent()<=100.0f) {
			deleteCount++;
			Object objects[] = {MyHacker.getEncryptedIP(),new Integer(index)};
			View MyView = MyHacker.getView();
			MyView.setFunction("deletewatch");
			MyView.addFunctionCall(new RemoteFunctionCall(0,"deletewatch",objects));
			for(int i=0;i<numColumns;i++){
				panel.remove(components[i][index]);
			}
			deleteRow(index);
			panel.repaint();
        }
    }
    
    public void deleteRow(int index){
		try{
			semaphore.acquire();
		}catch(Exception e){
		}
        JComponent temp[][] = new JComponent[numColumns][components[NUMBER].length-1];
        EventListener tempEvents[][] = new EventListener[numColumns][eventListeners[NUMBER].length-1];
        int in=0;
        for(int i=0;i<components[NUMBER].length;i++){
            if(i!=index){
                temp[NUMBER][in] = components[NUMBER][i];
                temp[ON_OFF][in] = components[ON_OFF][i];
                temp[PORT][in] = components[PORT][i];
                temp[TYPE][in] = components[TYPE][i];
                temp[CPU_COST][in] = components[CPU_COST][i];
                temp[NOTE][in] = components[NOTE][i];
                temp[OBSERVED_PORTS][in] = components[OBSERVED_PORTS][i];
                temp[EDIT_OBSERVED_PORTS_BUTTON][in] = components[EDIT_OBSERVED_PORTS_BUTTON][i];
                temp[SEARCH_FIREWALL][in] = components[SEARCH_FIREWALL][i];
                temp[VALUE][in] = components[VALUE][i];
                temp[DELETE][in] = components[DELETE][i];
				
                
                if(i>index){
					for(int j=0;j<numColumns;j++){
						Component c = temp[j][in];
						c.setBounds(c.getX(),c.getY()-ROW_HEIGHT,c.getWidth(),c.getHeight());
					}
					((JLabel)temp[NUMBER][in]).setText(""+in);
					((WatchOnOffActionListener)eventListeners[ON_OFF][i]).setIndex(in);
					((watchPortComboBoxActionListener)eventListeners[PORT][i]).setIndex(in);
					((watchTypeComboBoxActionListener)eventListeners[TYPE][i]).setIndex(in);
					((watchFirewallComboBoxActionListener)eventListeners[SEARCH_FIREWALL][i]).setIndex(in);
					((watchObservedPortsActionListener)eventListeners[EDIT_OBSERVED_PORTS_BUTTON][i]).setIndex(in);
					((watchRemoveActionListener)eventListeners[DELETE][i]).setIndex(in);
					((watchQuantityFocusListener)eventListeners[VALUE][i]).setIndex(in);
                    
                }
				tempEvents[ON_OFF][in] = eventListeners[ON_OFF][i];
                tempEvents[PORT][in] = eventListeners[PORT][i];
                tempEvents[TYPE][in] = eventListeners[TYPE][i];
                tempEvents[SEARCH_FIREWALL][in] = eventListeners[SEARCH_FIREWALL][i];
                tempEvents[EDIT_OBSERVED_PORTS_BUTTON][in] = eventListeners[EDIT_OBSERVED_PORTS_BUTTON][i];
                tempEvents[DELETE][in] = eventListeners[DELETE][i];
                tempEvents[VALUE][in] = eventListeners[VALUE][i];
                
                in++;
            }      
        
        }
        components = temp;
        eventListeners = tempEvents;
		try{
			semaphore.release();
		}catch(Exception e){}
		deleteCount--;
    }
    
    // This is called when a new watch is installed -- it increases the size of the JComponent and EventListener arrays
    public void addRow(){
        JComponent temp[][] = new JComponent[numColumns][components[NUMBER].length+1];
        EventListener tempEvents[][] = new EventListener[numColumns][eventListeners[NUMBER].length+1];
        for(int i=0;i<watches.length;i++){
            for(int j=0;j<numColumns;j++){
               temp[j][i] = components[j][i];
               tempEvents[j][i] = eventListeners[j][i];
            }
    
        }
        components = temp;
        eventListeners = tempEvents;
    }
	
	public class watchPortComboBoxActionListener implements ActionListener {		
		int index;
		Hacker MyHacker;
		
		public watchPortComboBoxActionListener(int index, Hacker h) {
			this.index = index;
			this.MyHacker = h;
		}
		
        public void setIndex(int index){
            this.index=index;
        }
        
		// IMPLEMENTS ActionListener
		public void actionPerformed(ActionEvent e) {
			JComboBox cb = (JComboBox)e.getSource();
			View MyView = MyHacker.getView();
			Object objects[] = {MyHacker.getEncryptedIP(),index,cb.getSelectedIndex()};
			MyView.setFunction("changewatchport");
			MyView.addFunctionCall(new RemoteFunctionCall(0,"changewatchport",objects));
		}
	}
	
	public class watchTypeComboBoxActionListener implements ActionListener {
		int index;
		Hacker MyHacker;
		
		public watchTypeComboBoxActionListener(int index, Hacker h) {
			this.index = index;
			this.MyHacker = h;
		}
		
		public void setIndex(int index){
            this.index=index;
        }
        
        // IMPLEMENTS ActionListener
		public void actionPerformed(ActionEvent e) {
			JComboBox cb = (JComboBox)e.getSource();
			View MyView = MyHacker.getView();
			Object objects[] = {MyHacker.getEncryptedIP(),index,cb.getSelectedIndex()};
			MyView.setFunction("changewatchtype");
			MyView.addFunctionCall(new RemoteFunctionCall(0,"changewatchtype",objects));
		}
	
	}
	
	
	public class watchFirewallComboBoxActionListener implements ActionListener {
		int index;
		Hacker MyHacker;
		
		public watchFirewallComboBoxActionListener(int index, Hacker h) {
			this.index = index;
			this.MyHacker = h;
		}
		
		public void setIndex(int index){
            this.index=index;
        }
        
        // IMPLEMENTS ActionListener
		public void actionPerformed(ActionEvent e) {
			JComboBox cb = (JComboBox)e.getSource();
			View MyView = MyHacker.getView();
			Object objects[] = {MyHacker.getEncryptedIP(),index,cb.getSelectedIndex()};
			MyView.setFunction("setwatchsearchfirewall");
			MyView.addFunctionCall(new RemoteFunctionCall(0,"setwatchsearchfirewall",objects));
		}
	
	}
	
	public class watchObservedPortsActionListener implements ActionListener {
		int index;
		WatchManager MyWatchManager;
		
		public watchObservedPortsActionListener(int index, WatchManager w) {
			this.index = index;
			this.MyWatchManager = w;
		}
		
		public void setIndex(int index){
            this.index=index;
        }
        
        // IMPLEMENTS ActionListener
		public void actionPerformed(ActionEvent e) {
            int currentObservedPorts[] = watches[index].getObservedPorts();
            ObservedPortsDialog WPD = new ObservedPortsDialog(MyWatchManager, index, Constants.memoryTypeMaxPorts[MyHacker.getMemoryType()], currentObservedPorts);
		}
	
	}
	
	
	public class watchQuantityFocusListener implements FocusListener {
		int index;
		Hacker MyHacker;
		float currentValue=0.0f;
		
		public watchQuantityFocusListener(int index, Hacker h,float current) {
			this.index = index;
			this.MyHacker = h;
			this.currentValue=current;
		}
		
		
		public void setIndex(int index){
            this.index=index;
        }
        
        // IMPLEMENTS FocusListener
		
		public void focusGained(FocusEvent e){
			
		}
		public void focusLost(FocusEvent e) {
			float quantity=0.0f;
			if(e.getSource() instanceof JFormattedTextField){
				quantity = Float.parseFloat(((JFormattedTextField)e.getSource()).getText());
				if(quantity>100.0f){
					quantity=100.0f;
				}
				if(quantity<0.0f){
					quantity=0.0f;
				}
                ((JFormattedTextField)e.getSource()).setText(""+quantity);
			}
			else if(e.getSource() instanceof JTextField){
				try{
					quantity = Float.parseFloat(((JTextField)e.getSource()).getText());
                    ((JTextField)e.getSource()).setText(""+quantity);
				}catch(NumberFormatException ex){
                    ((JTextField)e.getSource()).setText(""+quantity);
				}
			}
			//System.out.println("Setting value for "+index+" at "+quantity);
			
			if(quantity!=currentValue){
				Object objects[] = {MyHacker.getEncryptedIP(),index,new Float(quantity)};
				View MyView = MyHacker.getView();
				MyView.setFunction("setwatchquantity");
				MyView.addFunctionCall(new RemoteFunctionCall(0,"setwatchquantity",objects));
			}
		}
	
	}
	
	public class watchRemoveActionListener implements ActionListener {
		int index;
		//Hacker MyHacker;
        WatchManager wm;
		
		public watchRemoveActionListener(int index, WatchManager myWatchManager) {
			this.index = index;
			//this.MyHacker = h;
            wm = myWatchManager;
		}
		
		public void setIndex(int index){
            this.index=index;
        }
        
        // IMPLEMENTS ActionListener
		public void actionPerformed(ActionEvent e) {
            String message = "Are You Sure?\n This will permanently remove this watch from your computer.";
            String title = "Delete Watch";
    		int n = showYesCancelDialog(title, message);
    		if(n==0){
                wm.deleteWatch(index);
    		}
		}
	
	}
	
	
}

