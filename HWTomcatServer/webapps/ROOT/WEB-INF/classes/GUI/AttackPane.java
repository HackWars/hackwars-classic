package GUI;
/**
* The attack panel that is shown when a player is attacking.
* @author Cameron McGuinness
*/

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.*;
import Assignments.*;
import View.*;
import java.lang.*;
import Game.*;
import java.text.*;
import javax.swing.Timer;
import java.util.ArrayList;
import java.util.Vector;
import net.miginfocom.swing.*;

public class AttackPane extends Application implements ChangeListener, FocusListener {
	public static int MAX_MESSAGES = 100;
	public static int WINDOW_HANDLE = 0;
	private JDesktopPane mainPanel=null;
	private Hacker MyHacker=null;
	private JSpinner portSpinner;
	private String ipValue;
	private int portValue;
	private JButton button,attackButton = new JButton("Attack");
	private JTextPane textPane;
	private JTabbedPane tabbedPane;
	private int port;
	private JTextField bankScript,attackScript,FTPScript,httpScript;
	private JComboBox secondaryPorts,fromPorts,fromIP;
	private String bankFolder,attackFolder,FTPFolder,httpFolder;
	private String bankMaliciousIP,FTPMaliciousIP,attackMaliciousIP;
	private float bankPettyCashTarget,pettyCashTarget;
	private JSpinner pettyCashSpinner;
	private JTextField addPort,scriptField;
	private JLabel damageDone,healCount;
	private HealthLabel health;
	private float damage=0.0f;
	private JCheckBox details;
	private IPPanel ipPanel;
	private int counter = 0;
	private Timer timer;
	private AttackAnimator attackAnimator= new AttackAnimator();
	private ArrayList messages = new ArrayList();
	private JScrollPane terminalScrollPane;
	private Vector attackPorts;
	private Integer[] secondaryPortsArray = new Integer[0];
    
    private int type = 0;
    public static final int ATTACK = 0;
    public static final int REDIRECT = 1;
	
	private int windowHandle = 0;
    
	/**
	 * constructor
	 * @param name the name of the window.
	 * @param resize whether or not the window is resizable.
	 * @param max whether or not the window is maximizable.
	 * @param close whether or not the window is closable.
	 * @param iconify whether or not the window is iconifiable.
	 * @param mainPanel the desktop panel of the main game.
	 * @param MyHacker the main GUI controller.
	 * @param ipValue the ip to attack.
	 * @param portValue the port to attack.
	 * @param port the port to use to attack from.
	 * */
	public AttackPane(String name, boolean resize, boolean max, boolean close, boolean iconify, JDesktopPane mainPanel, Hacker MyHacker, String ipValue, int portValue, int port, int type) {
        windowHandle = ++WINDOW_HANDLE;
        this.type = type;
        //this.setTitle(getTypeString());
        this.setTitle(name);
		this.setResizable(true);
		this.setMaximizable(max);
		this.setClosable(close);
		this.setIconifiable(iconify);
		this.addInternalFrameListener(this);
		setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
		this.mainPanel=mainPanel;
		this.MyHacker=MyHacker;
		this.ipValue=ipValue;
		this.portValue=portValue;
		this.port=port;
		setMinimumSize(new Dimension(210,125));
		timer = new Timer(100,this);
		timer.start();
		//this.setFrameIcon(ImageLoader.getImageIcon("images/calc.png"));
	}

	/**
	 * create and add the GUI elements to the panel
	 **/
	public void populate(){
		setLayout(new MigLayout("fill,wrap 1,align leading","","[shp 100][shp 110,shrink 110][shp 130][shp 110,shrink 115,gp 150]"));
		JPanel targetPanel = new JPanel();
		targetPanel.setBorder(new FunctionBorder("Attack"));
		targetPanel.setLayout(new MigLayout("fill,wrap 4","","10[][]"));
		targetPanel.add(new JLabel("IP:"));
		ipPanel = new IPPanel(MyHacker);
		targetPanel.add(ipPanel);
		targetPanel.add(new JLabel("Port: "),"align right");
		SpinnerModel model = new SpinnerNumberModel(portValue,0,31,1);
		portSpinner = new JSpinner(model);
		portSpinner.addChangeListener(this);
		targetPanel.add(portSpinner,"align right");
		add(targetPanel,"growx,hmin 12,wmin 50");
		
		JPanel fromPanel = new JPanel();
		fromPanel.setBorder(new FunctionBorder("From"));
		fromPanel.setLayout(new MigLayout("fill,wrap 4","[]10[n:n:140]40[]10[]","10[][]"));
		fromPanel.add(new JLabel("IP:"),"hmin 0");
		fromIP = new JComboBox(new String[]{MyHacker.getIP()});
		fromIP.setActionCommand("title");
		fromIP.addActionListener(this);
		fromPanel.add(fromIP,"growx,hmin 0");
		JLabel healthLabel = new JLabel("Port Health: ");
		fromPanel.add(healthLabel);
		health = new HealthLabel("100.0",MyHacker,port,this);
		health.setHealth(100.0f);
		health.setForeground(Color.blue);
		fromPanel.add(health,"align center");
		fromPanel.add(new JLabel("Port:"),"align left,hmin 0");
		if(type == ATTACK){
			attackPorts = MyHacker.getAttackPorts();
		}
		else if(type == REDIRECT){
			attackPorts = MyHacker.getRedirectPorts();
		}
		fromPorts = new JComboBox();
		setPorts(attackPorts);
		fromPorts.setActionCommand("fromport");
		fromPorts.addActionListener(this);
		setSelectedFromPort();
		fromPanel.add(fromPorts,"growx,align right,hmin 0,wmax 140");
		fromPanel.add(new JLabel("Heals Remaining: "),"align right");
		healCount = new JLabel("10");
		fromPanel.add(healCount,"align center");
		add(fromPanel,"growx,hmin 12,wmin 50");
		
		JPanel advancedPanel = new JPanel();
		advancedPanel.setBorder(new FunctionBorder("Advanced"));
		advancedPanel.setLayout(new MigLayout("fill,wrap 3","","10[][]"));
		advancedPanel.add(new JLabel("installScript()"));
		scriptField = new JTextField(12);
		advancedPanel.add(scriptField,"growx");
		JButton browse = new JButton("Browse");
		browse.addActionListener(this);
		advancedPanel.add(browse,"wmin 0");
		advancedPanel.add(new JLabel("switchAttack()"));
		secondaryPorts = new JComboBox();
		advancedPanel.add(secondaryPorts,"growx,hmin 0");
		JButton setSecondaryPortsButton = new JButton(ImageLoader.getImageIcon("images/edit.png"));
		setSecondaryPortsButton.setContentAreaFilled(false);
		setSecondaryPortsButton.setBorderPainted(false);
		setSecondaryPortsButton.setActionCommand("secondaryPorts");
		setSecondaryPortsButton.addActionListener(this);
		setSecondaryPortsButton.setMargin(new Insets(0,0,0,0));
		advancedPanel.add(setSecondaryPortsButton,"align left");
		advancedPanel.add(new JLabel("checkPettyCashTarget()"));
		model = new SpinnerNumberModel(0,0,500000000.0f,1);
		pettyCashSpinner = new JSpinner(model);
		advancedPanel.add(pettyCashSpinner,"wmin 0");
		if(type==ATTACK){
			add(advancedPanel,"growx,shpy 150, hmin 12,wmin 50");
		}
		
		
		JPanel battlePanel = new JPanel();
		battlePanel.setBorder(new FunctionBorder("Battle"));
		battlePanel.setLayout(new MigLayout("fill,wrap 2","[shp 50][shp 120]"));
		damageDone = new JLabel("Damage Done: 0.0");
		battlePanel.add(damageDone);
		attackButton.addActionListener(this);
		attackButton.setMargin(new Insets(1,1,1,1));
		battlePanel.add(attackButton);
		if(type == REDIRECT){
			attackButton.setText("Redirect");
		}
		textPane = new JTextPane();
		terminalScrollPane = new JScrollPane(textPane);
		battlePanel.add(terminalScrollPane,"span,grow,h 150");
		
		add(battlePanel,"grow,hmin 35,wmin 50");
		
		StyledDocument doc = textPane.getStyledDocument();
		
		Style def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);

        Style regular = doc.addStyle("regular", def);
        //StyleConstants.setFontFamily(def, "SansSerif");

        Style s = doc.addStyle("bold", regular);
        StyleConstants.setBold(s, true);

		s = doc.addStyle("firewall",regular);
		StyleConstants.setForeground(s,Color.red);
		
		pack();
		//setSize(410,450);
	}
	
	public void setPorts(Vector ports){
		attackPorts = ports;
		if(fromPorts!=null){
			ActionListener[] listeners = fromPorts.getActionListeners();
			for(int i=0; i < listeners.length; i++){
				fromPorts.removeActionListener(listeners[i]);
			}
			
			fromPorts.removeAllItems();
			for(int i=0; i < ports.size(); i++) {
				String portInfo = (String)ports.get(i);
				fromPorts.addItem(portInfo);
			}
			if(ports.isEmpty()){
				attackButton.setEnabled(false);
			}else{
				attackButton.setEnabled(true);
			}
			setSelectedFromPort();
			fromPorts.addActionListener(this);
		}
	}
	
	public void setSelectedFromPort(){
		//int defaultPort = MyHacker.getDefaultAttackPort();
		for(int i=0; i < attackPorts.size(); i++) {
			int p = Integer.parseInt((((String)attackPorts.get(i)).split(":")[0]));
			if (p == port){
				fromPorts.setSelectedIndex(i);
				break;
			}
		}
	}
	
	public int getWindowHandle(){
		return(windowHandle);
	}
	
	public int getType(){
		return(type);
	}
	
	private void repopulateMessagePane(){
		Object[] messageArray = this.messages.toArray();
		DecimalFormat format = new DecimalFormat("#.##");
		int start = 0;
		if(messageArray.length > MAX_MESSAGES){
			start = messageArray.length-100;
		}
		String newLines = "";
		textPane.setText("");
		StyledDocument doc = textPane.getStyledDocument();
		for( int i = start; i < messageArray.length; i++ ){
			if( messageArray[i] instanceof Object[] ) {
				Object[] damageArray = (Object[])messageArray[i];
				float damage = (Float)damageArray[0];
				boolean firewall = (Boolean)damageArray[1];
				String m = "";
				String style = "regular";
				if( !firewall ) {
					m = "You have hit the port for "+format.format(damage)+" damage!\n";
				}
				else{
					m = "Your firewall has hit for "+format.format(damage)+" damage!\n";
					style = "firewall";
				}
				try{
					doc.insertString(doc.getLength(), m,doc.getStyle(style));
				}catch(Exception e){
					e.printStackTrace();
				}

			}
			else if ( messageArray[i] instanceof String ) {
				String m = (String)messageArray[i];
				try{
					doc.insertString(doc.getLength(), m+"\n",doc.getStyle("bold"));
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		SwingUtilities.invokeLater(new Runnable()
		{
		public void run()
		{
			terminalScrollPane.getVerticalScrollBar().setValue(terminalScrollPane.getVerticalScrollBar().getMaximum());
		}
		}); 
	}
	
	
	public void setHealth(float health,int healCount){
		this.health.setHealth(health);
		this.healCount.setText(""+(10-healCount));
	}
	
	/**
	 * Adds a damage message ("You have hit the port for xx damage!") to the console.
	 * @param damage the amount of damage done.
	 * @param firewall whether the damage was caused by a firewall or not.
	 **/
	public void addMessage(float damage,boolean firewall){
		this.messages.add(new Object[]{damage,firewall});
		repopulateMessagePane();
		DecimalFormat format = new DecimalFormat("#.##");
		this.damage+=damage;
		damageDone.setText("Damage Done: "+format.format(this.damage));
		/*textPane.setText(newLines);
		//String text=textPane.getText();
		textPane.setCaretPosition(newLines.length());
		attackAnimator.damage(damage,firewall);*/
	}
	
	public void addMessage(String message){
		messages.add(message);
		repopulateMessagePane();
	}
	
	public void internalFrameClosed(InternalFrameEvent e) {
		MyHacker.setAttackOpen(false);
	}
	
	/**
	 * Opening a file that will be installed onto target after the attack is done
	 * @param title the name of the file
	 * @param folder the folder where the file is located on the players in-game HD.
	 * @param type the type of file that was selected. Value coincide with {@link HackerFile} types.
	 * @param ip the malicious ip to set on the port after it has been weakened.
	 * @param pettyCash the target petty cash to set on the port after it has been weakened.
	 * */
	public void openFile(String title,String folder,int type,String ip,float pettyCash){
			bankFolder=folder;
			scriptField.setText(title);
			bankMaliciousIP=ip;
			bankPettyCashTarget=pettyCash;
	}
	
	/**
	 * Secondary ports that can be switched to in an attack.
	 * @param add the array of ports.
	 * */
	public void setSecondaryPorts(Integer[] add){
		secondaryPortsArray = add;
		portSpinner.setValue(add[0]);
		populateSecondaryPorts();
	}
	/**
	 * Sets the port to be attacked.
	 * @param port the port to be attacked.
	 * */
	public void setTargetPort(int port){
		portSpinner.setValue(port);
	}
	
	/**
	 * Sets the ip to attack.
	 * @param ip the ip to attack.
	 * */
	public void setIP(String ip){
		ipPanel.setIP(ip);
		
	}
	
	/**
	 * sets the ip fields after being parsed into the 4 sections.
	 * @param ip1 the first part of the ip.
	 * @param ip2 the second part of the ip.
	 * @param ip3 the third part of the ip.
	 * @param ip4 the fourth part of the ip.
	 * */
	public void setIP(String ip1,String ip2,String ip3,String ip4){
		ipPanel.setIP(ip1+"."+ip2+"."+ip3+"."+ip4);
	}
    
    private String getTypeString() {
        String retVal = "";
        if (this.type == ATTACK) {
            retVal = "Attack";
        } else if (this.type == REDIRECT) {
            retVal = "Redirect";
        }
        
        return retVal;
    }
	
	public void setAttacking(boolean attacking){
		if(attacking){
			attackButton.setText("Cancel");
		}
		else{
			attackButton.setText(getTypeString());
		}
		fromIP.setEnabled(!attacking);
		fromPorts.setEnabled(!attacking);
		ipPanel.setEnabled(!attacking);
		portSpinner.setEnabled(!attacking);
	}
    
	/**
	 * Starts the attack.
	 * */
	public void attack(){
		redoTitle();
		this.damage=0.0f;
        damageDone.setText("Damage Done: "+this.damage);
		
        String targetIP = ipPanel.getIP();
        int targetPort = (int)(Integer)portSpinner.getValue();
        messages.clear();
        messages.add(getTypeString() + " commencing on "+targetIP+" at port "+targetPort+"...\n");
        repopulateMessagePane();

        View MyView = MyHacker.getView();
        //get ip from main class.
        String ip=MyHacker.getEncryptedIP();
        port = (new Integer((((String)fromPorts.getSelectedItem()).split(":")[0]))).intValue();
		health.setPort(port);
        if (type == ATTACK) {
            Integer secondaryPorts[] = new Integer[this.secondaryPorts.getItemCount()];
            for(int i=0;i<this.secondaryPorts.getItemCount();i++){
                secondaryPorts[i]=(Integer)this.secondaryPorts.getItemAt(i);
            }
            String scripts[][] = new String[4][2];
            scripts[0] = new String[]{bankFolder,scriptField.getText()};
            scripts[1] = new String[]{FTPFolder,""};
            scripts[2] = new String[]{attackFolder,""};
            scripts[3] = new String[]{httpFolder,""};
            
            Object otherInfo[] = new Object[5];
            otherInfo[0]=bankMaliciousIP;
            otherInfo[1]=(Float)bankPettyCashTarget;
            otherInfo[2]=FTPMaliciousIP;
            otherInfo[3]=((Double)pettyCashSpinner.getValue()).floatValue();
            otherInfo[4]=attackMaliciousIP;
            
            Object objects[] = {targetIP,targetPort,ip,new Integer(port),secondaryPorts,scripts,otherInfo,windowHandle};
            MyView.setFunction("requestattack");
            MyView.addFunctionCall(new RemoteFunctionCall(0,"requestattack",objects));
        } else if (type == REDIRECT) {
            Integer secondaryPorts[] = new Integer[0];
            String scripts[][] = new String[4][2];
            Object otherInfo[] = new Object[5];
            Object objects[] = {targetIP,targetPort,ip,new Integer(port),secondaryPorts,scripts,otherInfo,windowHandle};
            MyView.setFunction("requestattack");
            MyView.addFunctionCall(new RemoteFunctionCall(0,"requestattack",objects));
        }
        
	}
	
	private void populateSecondaryPorts(){
		secondaryPorts.removeAllItems();
		
		for(int i=0;i<secondaryPortsArray.length;i++){
			secondaryPorts.addItem(secondaryPortsArray[i]);
		}
	
	}
	
	private void redoTitle(){
		String fromPort = (String)fromPorts.getSelectedItem();
		int fromPortInt = 0;
		if(fromPort != null){
			fromPortInt = Integer.parseInt(fromPort.split(":")[0]);
		}
		setTitle(getTypeString()+" -- "+ipPanel.getIP()+":"+portSpinner.getValue()+" from "+fromIP.getSelectedItem()+":"+fromPortInt);
	}
    
	public void actionPerformed(ActionEvent e){
		
        if (e.getActionCommand()==null) {
			attackAnimator.increaseCounter();
			return;
		}
        
		String typeString = getTypeString();
		
        if (e.getActionCommand().equals(typeString)) {
            if ( (type == ATTACK && MyHacker.getShowAttack()) || (type == REDIRECT && MyHacker.getShowRedirect()) ) {
				NumberFormat nf= NumberFormat.getCurrencyInstance();
				Object[] options = {"Yes","Cancel"};
                Object[] check = ConfirmationPanel.showYesNoDialog(this, typeString, "Are You Sure?\n\nIt will cost you " + nf.format(10.0f) + " to " + typeString.toLowerCase() + " this port.", "Always " + typeString.toLowerCase() + ", don't ask again.");
                if ((int)(Integer)check[0] == OptionDialog.OPTION_YES) {
                    attack();
                }
                // always attack, don't ask me again.
                if ((boolean)(Boolean)check[1] == true) {
                    if (type == ATTACK) {
                        MyHacker.setShowAttack(false);
                    } else if (type == REDIRECT) {
                        MyHacker.setShowRedirect(false);
                    }
                }
            } else {
                attack();
            }
		}
		else if(e.getActionCommand().equals("Cancel")){
			//textPane.setText(textPane.getText()+"Attack Canceled by you.\n--------------------------------------\n");
			String ip=MyHacker.getEncryptedIP();
			int port = (new Integer((((String)fromPorts.getSelectedItem()).split(":")[0]))).intValue();
			View MyView = MyHacker.getView();
			Object objects[] = {ip,new Integer(port)};
			MyView.setFunction("requestcancelattack");
			MyView.addFunctionCall(new RemoteFunctionCall(0,"requestcancelattack",objects));
			//tabbedPane.setSelectedIndex(0);
		}
		/*else if(e.getActionCommand().equals("add")){
			try{
				int newport = Integer.parseInt(addPort.getText());
				if(newport<0||newport>31)
					throw new NumberFormatException();
				ports.addItem(newport);
			}catch(NumberFormatException ex){
				MyHacker.showMessage("Secondary ports must be a number between 0 and 31");
			}
		}
		else if(e.getActionCommand().equals("remove")){
			int index = ports.getSelectedIndex();
			ports.removeItemAt(index);
			
		}*/
		else if(e.getActionCommand().equals("Browse")){
			AttackFileChooser AFC = new AttackFileChooser(MyHacker,HackerFile.BANKING_COMPILED,MyHacker.getCurrentFolder(),MyHacker.getCurrentDirectory(),this,HackerFile.BANKING_COMPILED);
			mainPanel.add(AFC);
			AFC.moveToFront();
		}
		else if(e.getActionCommand().equals("Attack Browse")){
			AttackFileChooser AFC = new AttackFileChooser(MyHacker,HackerFile.ATTACKING_COMPILED,MyHacker.getCurrentFolder(),MyHacker.getCurrentDirectory(),this,HackerFile.ATTACKING_COMPILED);
			mainPanel.add(AFC);
			AFC.moveToFront();
		}
		else if(e.getActionCommand().equals("FTP Browse")){
			AttackFileChooser AFC = new AttackFileChooser(MyHacker,HackerFile.FTP_COMPILED,MyHacker.getCurrentFolder(),MyHacker.getCurrentDirectory(),this,HackerFile.FTP_COMPILED);
			mainPanel.add(AFC);
			AFC.moveToFront();
		}
		else if(e.getActionCommand().equals("Details")){
			if(!details.isSelected())
				setBounds(getBounds().x,getBounds().y,getBounds().width,125);
			else
				setBounds(getBounds().x,getBounds().y,getBounds().width,400);
			MyHacker.setShowAttackDetails(details.isSelected());
		}
		else if(e.getActionCommand().equals("title")){
			redoTitle();
		}
		else if(e.getActionCommand().equals("fromport")){
			redoTitle();
			String portInfo = (String)fromPorts.getSelectedItem();
			if(portInfo != null){
				port = Integer.parseInt(portInfo.split(":")[0]);
			}
		}
		else if(e.getActionCommand().equals("secondaryPorts")){
			SecondaryPortsDialog SPD = new SecondaryPortsDialog();
			secondaryPortsArray = SPD.getSecondaryPorts(this,secondaryPortsArray);
			populateSecondaryPorts();
		}
		
		
	}
	public void stateChanged(ChangeEvent e) {
			//pettyCashTarget=(float)((Double)pettyCashSpinner.getValue()).floatValue();
			redoTitle();
	}
	
	public void focusGained(FocusEvent e){
		((JTextField)e.getSource()).selectAll();
	}
	
	public void focusLost(FocusEvent e){
	}
}

