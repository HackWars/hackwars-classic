package chat.client;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import java.util.ArrayList;
import chat.client.HtmlCellRender;
import chat.messages.MsgOutError;
import GUI.*;
import java.util.Calendar;


public class viewChatWindow extends JPanel implements absViewChannel, ActionListener,MouseListener{
    public static final Color WHISPER_COLOR = Color.red;
	public static final Color WHISPER_SENT_COLOR = Color.yellow;
    public static final Color ADMIN_COLOR = Color.yellow;
    public static final Color MOD_COLOR = Color.green;
    public static final Color REGULAR_COLOR = Color.white;

    private final int MAX_MESSAGE_COUNT=100;
    private ChatController controller = null;
    private String channelName = null;
    
    private JTextPane textMsgList = null;
    private ListCellRenderer textMsgRenderer = new HtmlCellRender();
    private DefaultListModel textMsgModel = null;
    
    private JTextField textInputField = null;
    private JButton buttonCreateChannel = null;
    private JButton buttonJoinChannel = null;
    private JCheckBox autoscroll = null;
    
    private JList userList = null;
    private DefaultListModel userModelAlphabet = null;
    private DefaultListModel userModelTime = null;
    private JScrollPane listScrollPane = null;
    private SimpleAttributeSet bold = new SimpleAttributeSet();
    private SimpleAttributeSet normal = new SimpleAttributeSet();
    private SimpleAttributeSet whisper_bold = new SimpleAttributeSet();
    private SimpleAttributeSet whisper_normal = new SimpleAttributeSet();
	private SimpleAttributeSet whisper_sent_bold = new SimpleAttributeSet();
    private SimpleAttributeSet whisper_sent_normal = new SimpleAttributeSet();
    private SimpleAttributeSet admin_bold = new SimpleAttributeSet();
    private SimpleAttributeSet admin_normal = new SimpleAttributeSet();
    private SimpleAttributeSet mod_bold = new SimpleAttributeSet();
    private SimpleAttributeSet mod_normal = new SimpleAttributeSet();
    
    private int lineCount=0;
    
    /** Creates a new instance of viewChatWindow */
    public viewChatWindow(ChatController controller, String channelName) throws Exception {

        if(channelName == null || channelName.length() == 0){
            throw new Exception("Invalid Channel Name given! <" + channelName+ ">");
        }

        this.controller = controller;
        this.channelName = channelName;
        
    	StyleConstants.setBold(bold, true);
    	StyleConstants.setBold(normal, false);
    	StyleConstants.setBold(whisper_bold, true);
    	StyleConstants.setBold(whisper_normal, false);
		StyleConstants.setBold(whisper_sent_bold, true);
    	StyleConstants.setBold(whisper_sent_normal, false);
    	StyleConstants.setBold(admin_bold, true);
    	StyleConstants.setBold(admin_normal, false);
    	StyleConstants.setBold(mod_bold, true);
    	StyleConstants.setBold(mod_normal, false);
        
    	StyleConstants.setForeground(whisper_bold,WHISPER_COLOR);
    	StyleConstants.setForeground(whisper_normal,WHISPER_COLOR);
		StyleConstants.setForeground(whisper_sent_bold,WHISPER_SENT_COLOR);
    	StyleConstants.setForeground(whisper_sent_normal,WHISPER_SENT_COLOR);
        StyleConstants.setForeground(admin_bold,ADMIN_COLOR);
        StyleConstants.setForeground(admin_normal,ADMIN_COLOR);
        StyleConstants.setForeground(mod_bold,MOD_COLOR);
        StyleConstants.setForeground(mod_normal,MOD_COLOR);
        
        //setting up the guii
        this.setLayout(new GridBagLayout());
        
        try{
            //  setup the vertical scroll panel that holds the text (either game messages, or user messages)
            guiTextMsgList();
            
            // if the channel isn't the "Game Messages" (GAME_MESSAGES_NAME) or the "Chat Messages" (CHAT_CHANNEL_NAME) , setup the User List
            if((channelName.compareToIgnoreCase( ChatController.CHAT_MESSAGES_NAME ) != 0) && (channelName.compareToIgnoreCase(ChatController.GAME_MESSAGES_NAME)!=0)) {
                //guiTextInputField();
                guiUserList();
            }
            
    	    if(channelName.compareToIgnoreCase(ChatController.GAME_MESSAGES_NAME)!=0){
    		    guiTextInputField();
    		    guiCreateChannel();
    		    guiJoinChannel();
                guiAutoScroll();
    	    }
        }catch(Exception e){
            //System.out.println(e.getMessage());
            e.printStackTrace();
        }
        
        setVisible(true);
    }
    
    private JScrollPane guiTextMsgList(){
    	textMsgList = new JTextPane();
    	textMsgList.setEditable(false);
        textMsgList.setBackground(Color.black);
        textMsgList.setFont(new Font("monospaced",Font.PLAIN,14));
        textMsgList.setForeground(Color.white);
        
        GridBagConstraints c = new GridBagConstraints();
         //x,y postion
        c.gridx = 1;
        c.gridy = 0;
         //size on grid
        c.gridheight = 1;
        c.gridwidth = 4;
         //paddinging, ignored with expand I think,
        c.ipadx = 2;
        c.ipady = 2;

         //Expand in the x and y directions? 1 = full, 0 = no, inbetween is wierd
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.fill = c.BOTH;
        
        //Scrollbar
        listScrollPane = new JScrollPane(textMsgList);
        this.add(listScrollPane, c);
        return listScrollPane;
    }     
    
    private JScrollPane guiUserList(){
        
        userModelAlphabet = new DefaultListModel();
        userModelTime = new DefaultListModel();
        
        userList = new JList(userModelTime);
        userList.setCellRenderer(textMsgRenderer);
        userList.setBackground(Color.black);
        userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); 
        userList.addMouseListener(this);

        GridBagConstraints c = new GridBagConstraints();
         //x,y postion
        c.gridx =0;
        c.gridy =0;
         //size on grid
        c.gridheight = 1;
        c.gridwidth = 1;
         //paddinging, ignored with expand I think,
        c.ipadx = 2;
        c.ipady = 2;
        
         //Expand in the x and y directions? 1 = full, 0 = no, inbetween is wierd
        c.weightx = 0.0;
        c.weighty = 1.0;
        c.fill = c.VERTICAL;
        
        //Scrollbar
        JScrollPane listScrollPane = new JScrollPane(userList);
        listScrollPane.setPreferredSize(new Dimension(120,0));
        listScrollPane.setMinimumSize(new Dimension(120,0));
        
        this.add(listScrollPane, c);
        return listScrollPane;
    }         
    
    private JTextField guiTextInputField(){
        textInputField = new JTextField("");
        textInputField.addActionListener(this);
        //textInputField.setActionCommand(INPUT_TEXT_FIELD_EVENT);
        
        GridBagConstraints c = new GridBagConstraints();
         //x,y postion
        c.gridx =0;
        c.gridy =1;
         //size on grid
        c.gridheight = 1;
        c.gridwidth = 2;
         //paddinging, ignored with expand I think,
        c.ipadx = 2;
        c.ipady = 2;
         //Expand in the x and y directions? 1 = full, 0 = no, inbetween is wierd
        c.weightx = 1.0;
        c.weighty = 0.0;
        c.fill = c.HORIZONTAL;

        this.add(textInputField, c);
        return textInputField;
    }
    
    private JButton guiCreateChannel(){
	buttonCreateChannel = new JButton("Create Channel");
        buttonCreateChannel.addActionListener(this);
        GridBagConstraints c = new GridBagConstraints();
         //x,y postion
        c.gridx =3;
        c.gridy =1;
         //size on grid
        c.gridheight = 1;
        c.gridwidth = 1;
         //paddinging, ignored with expand I think,
        c.ipadx = 2;
        c.ipady = 2;
         //Expand in the x and y directions? 1 = full, 0 = no, inbetween is wierd
        c.weightx = 0.0;
        c.weighty = 0.0;;

        this.add(buttonCreateChannel, c);
        return buttonCreateChannel;
    }
    
    private JButton guiJoinChannel(){
        buttonJoinChannel = new JButton("Join Channel");
        buttonJoinChannel.addActionListener(this);
        GridBagConstraints c = new GridBagConstraints();
         //x,y postion
        c.gridx =4;
        c.gridy =1;
         //size on grid
        c.gridheight = 1;
        c.gridwidth = 1;
         //paddinging, ignored with expand I think,
        c.ipadx = 2;
        c.ipady = 2;
         //Expand in the x and y directions? 1 = full, 0 = no, inbetween is wierd
        c.weightx = 0.0;
        c.weighty = 0.0;

        this.add(buttonJoinChannel, c);
        return buttonJoinChannel;
    }
    
    private JCheckBox guiAutoScroll() {
        autoscroll = new JCheckBox("Autoscroll", true);
        autoscroll.addActionListener(this);
        GridBagConstraints c = new GridBagConstraints();
         //x,y postion
        c.gridx =2;
        c.gridy =1;
         //size on grid
        c.gridheight = 1;
        c.gridwidth = 1;
         //paddinging, ignored with expand I think,
        c.ipadx = 2;
        c.ipady = 2;
         //Expand in the x and y directions? 1 = full, 0 = no, inbetween is wierd
        c.weightx = 0.0;
        c.weighty = 0.0;

        this.add(autoscroll, c);
        return autoscroll;
        
    }
	
	private String getTimeStamp(){
		Calendar c = Calendar.getInstance();
		int hour=c.get(Calendar.HOUR);
		int minute=c.get(Calendar.MINUTE);
		int seconds=c.get(Calendar.SECOND);
		int ampm=c.get(Calendar.AM_PM);
		String pm="PM";
		String second;
		String minutes;
		if(seconds<10)
			second="0"+seconds;
		else
			second=""+seconds;
		if(minute<10)
			minutes="0"+minute;
		else
			minutes=""+minute;
		if(ampm==0)
			pm="AM";
		if(hour==0)
			hour=12;
		//System.out.println(newMessage);
		return("["+hour+":"+minutes+":"+second+" "+pm+"]");
	
	}
    
    /**
     * Adds a text string to the channel
     */
    public void addStringMsg(String s,boolean whisper,boolean isSender, boolean isMe){
        try{
            JScrollBar bar = listScrollPane.getVerticalScrollBar();
            String message = textMsgList.getText();
            int index = message.indexOf("\n");
            lineCount++;
            SimpleAttributeSet bold;
            SimpleAttributeSet normal;
            String[] out = s.split(controller.NAME_DELIMETER);
            String name = out[0];
            
            // if it's a whisper, set it to the whisper attributes
            if(whisper&&!isSender){
                bold=whisper_bold;
                normal=whisper_normal;
            }
			else if(whisper&&isSender){
				bold = whisper_sent_bold;
				normal = whisper_sent_normal;
			}
            else{
                bold=this.bold;
                normal=this.normal;
            }
            Document d = textMsgList.getDocument();
            
            // if the maximum chat size is exceeded, remove the top line
            if(lineCount>MAX_MESSAGE_COUNT){
                d.remove(0,index);
                lineCount--;
            }
            
            // if it's a game message, print out the message.  otherwise, put the colons back in.
            if(channelName!=ChatController.GAME_MESSAGES_NAME){
					if (!isMe){
		                String temp = "";
		                d.insertString(d.getLength(), getTimeStamp()+" " + out[0] + ": ", bold);
						d.insertString(d.getLength(), out[1], normal);
		                for(int i = 2; i < out.length; i++){
		                    temp += ":" + out[i];
		                }
		                d.insertString(d.getLength(), temp + "\n", normal);
		                // if autoscroll, move to bottom
		                autoScroll();
					}
					else {
		                d.insertString(d.getLength(), getTimeStamp() + " " + out[0], bold);
						d.insertString(d.getLength(), "\n", normal);
		                // if autoscroll, move to bottom
		                autoScroll();

					}
            }
            else{
                d.insertString(d.getLength(),s+"\n",normal);
                textMsgList.validate();
                //textMsgList.scrollRectToVisible(new Rectangle(0,textMsgList.getHeight()-2,1,1));
                //listScrollPane.validate();
                SwingUtilities.invokeLater(
                    new Runnable(){
                        public void run() {
                            listScrollPane.getVerticalScrollBar().setValue(listScrollPane.getVerticalScrollBar().getMaximum());
                            listScrollPane.validate();
                        }
                    }
                ); 
            }
        } catch(Exception e) {
        }
    }
    
    public void autoScroll() {
        if (autoscroll == null) {
            return;
        }
        
        if (autoscroll.isSelected()) {
            textMsgList.validate();
            //textMsgList.scrollRectToVisible(new Rectangle(0,textMsgList.getHeight()-2,1,1));
            //listScrollPane.validate();
            SwingUtilities.invokeLater(
                new Runnable(){
                    public void run() {
                        listScrollPane.getVerticalScrollBar().setValue(listScrollPane.getVerticalScrollBar().getMaximum());
                    }
                }
            ); 
        }
    }
    
    /**
     * Returns the channels name
     */
    public String getChannelName(){
        return channelName;
    }
    
    /**
     *  Adds a user to the channel
     */
    public void addUser(String s){
    
    	if(getUserIndex(s) == -1) {
            // insert into the list sorted by logged in time
    	   userModelTime.addElement(s);

            // insert the user into the alphabetically sorted list, in their proper place
            
            // if the list is empty, just add this at the beginning
            if (userModelAlphabet.isEmpty()) {
                userModelAlphabet.add(0, s);
                return;
            }
            
            int size = userModelAlphabet.size();
            int low = 0;
            int high = size - 1;
            int mid = - 1;

            // do a binary search to find out where to insert
            while (low <= high) {
                mid = (low + high) / 2;
                int compare = ((String)userModelAlphabet.getElementAt(mid)).compareTo(s);
                if ( compare < 0) {
                    low = mid + 1;
                } else if (compare > 0) {
                    high = mid - 1;
                } else {
                    // this would impoly identical usernames.  abandon ship.  all hope is lost.  repeat, all hope is lost.
                }
            }

            // at this point, your high > low.  if you're adding to the very end of the array, low > size, so insert at the end.  otherwise, insert at "low".
            if (low > size) {
                userModelAlphabet.add(size, s);
            } else {
                userModelAlphabet.add(low, s);
            }
        }

    }
    
    private int getUserIndex(String toGet){
    	int loopc = 0;
    	int size = userModelTime.size();
    	while(loopc < size){
    	   String s = (String) userModelTime.elementAt(loopc);
    	   if(s.compareTo(toGet) == 0){
    		return loopc;
    	   }
    	   loopc++;
    	}
    	return -1;
    }

    /**
     * Removes a user from the channel
     */
    public void removeUser(String s){
        // use the built-in method for the ListModel
        userModelAlphabet.removeElement(s);
        userModelTime.removeElement(s);
    }
    
    /**
     * Abstract for when the text box is edited
     * 
     */
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == textInputField){
            try{
                TextToMessage.processText(controller, textInputField.getText(), channelName);
                textInputField.setText("");
            }catch(Exception err){
                System.out.println(err.getMessage());
            }
            /*
            } catch (InvalidChatSyntaxException ex) {
                // instead of printing a stacktrace of the error (which we probably threw), we should catch the correct errors, and print them in the Game Messages
                // create a MsgOutError with the doohickey, then call it with the controller
                MsgOutError errMsg = new  MsgOutError("", ex.getMessage());
                try {
                    controller.processMessage(errMsg);
                } catch (Exception exc) {
                    exc.printStackTrace();
                }
            } catch (ChatException err){
                err.printStackTrace();
            }
            */
        }
    	if(e.getSource()==buttonCreateChannel){
    		//create channel
    		CreateChannelDialog CCD = new CreateChannelDialog(controller);
    	}
    	if(e.getSource()==buttonJoinChannel){
    		//join channel
    		JoinChannelDialog CCD = new JoinChannelDialog(controller);
    	}
        if (e.getActionCommand().equals("Alphabetical")) {
            userList.setModel(userModelAlphabet);
            userList.validate();
        }
        if (e.getActionCommand().equals("When Logged In")) {
            userList.setModel(userModelTime);
            userList.validate();
        }
    	if(e.getActionCommand().equals("See Profile")){
    		Hacker MyHacker = controller.getHacker();
    		String username = (String)userList.getSelectedValue();
    		MyHacker.startPersonalSettings(username);
    	}
    	if(e.getActionCommand().equals("Send Whisper")){
    		String username = (String)userList.getSelectedValue();
    		textInputField.setText("/w "+username+" ");
    		textInputField.grabFocus();
    	}
    	
    	if(e.getActionCommand().equals("Add as Friend")){
    		    AddFriendDialog AFD = new AddFriendDialog((String)userList.getSelectedValue(),controller);
    	}
    	if(e.getActionCommand().equals("Ignore")){
    		AddIgnoreDialog AID = new AddIgnoreDialog((String)userList.getSelectedValue(),controller);
    	}
        // assuming Unignore function is added and chosen...
        //if (e.getActionCommand().equals("Unignore")){
        //    AddUnignoreDialog AUID = new AddUnignoreDialog((String)userList.getSelectedValue(),controller);
        //}
    }
    
    public void mouseEntered(MouseEvent e){

	}

	public void mouseExited(MouseEvent e){

	}

	public void mousePressed(MouseEvent e){
		//System.out.println("Pressed");
	}

	public void mouseReleased(MouseEvent e){
	}

	public void mouseClicked(MouseEvent e){
        JPopupMenu menu = new JPopupMenu();
		JMenuItem menuItem;
		
        // add a submenu "Sort By"
        JMenu sortByMenu = new JMenu("Sort By");

        menuItem = new JMenuItem("Alphabetical");
        menuItem.addActionListener(this);
        sortByMenu.add(menuItem);
        
        menuItem = new JMenuItem("When Logged In");
        menuItem.addActionListener(this);
        sortByMenu.add(menuItem);

        menu.add(sortByMenu);
        menu.addSeparator();
        
		menuItem = new JMenuItem("See Profile");
		menuItem.addActionListener(this);
		menu.add(menuItem);
		menuItem = new JMenuItem("Send Whisper");
		menuItem.addActionListener(this);
		menu.add(menuItem);
		//menuItem = new JMenuItem("Add as Friend");
		//menuItem.addActionListener(this);
		//menu.add(menuItem);
		menuItem = new JMenuItem("Ignore");
		menuItem.addActionListener(this);
		menu.add(menuItem);
        // adding Unignore to the menu
        //menuItem = new JMenuItem("Unignore");
        //menuItem.addActionListener(this);
        //menu.add(menuItem);
		menu.show(this,e.getX(),e.getY());
	}
    
    
}
