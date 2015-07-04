package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.*;
import javax.swing.border.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Collection;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import net.miginfocom.swing.*;

public class OptionPanel extends Application {
	//Option Panel Content (Titles and Options)
	//Items beginning with a ' ' (Titles) are the titles of the sections and are made into jlabels. 
	//Remaining items are made into checkboxes (preferences)
	//protected JButton applyButton;
    
    private Hacker MyHacker;
    private HashMap preferences;

	public static String[] RADIOBUTTONS = {"Always", "Never", "Ask"};

    public final static String NETWORK_KEY = "network";
    public final static String LOG_WINDOW_KEY = "logwindow";
    public final static String ATTACK_TUTORIAL_KEY = "attacktutorial";
    public final static String APP_NOTE_KEY = "appnote";
    public final static String APP_REPLACED_KEY = "appreplaced";
    public final static String HEALING_KEY = "healing";
    public final static String HEALING_CONFIRM_KEY = "healconfirm";
    public final static String CARD_REPAIRED_KEY = "cardrepaired";
    public final static String CARD_REPLACED_KEY = "cardreplaced";
    public final static String ATTACK_CONFIRM_KEY = "attackconfirm";
    public final static String ATTACK_DETAILS_KEY = "attackdetails";
    public final static String REDIRECT_CONFIRM_KEY = "redirectconfirm";
    public final static String REDIRECT_DETAILS_KEY = "redirectdetails";
    public final static String SCAN_CONFIRM_KEY = "scanconfirm";
    public final static String COMMOD_TO_FILE_KEY = "commodtofile";
    public final static String FILE_TO_COMMOD_KEY = "filetocommod";
    public final static String TRANSFER_TO_KEY = "transferto";
    public final static String TRANSFER_FROM_KEY = "transferfrom";
    public final static String FILE_PURCHASED_KEY = "filepurchased";
    public final static String VOTE_SUCCESSFUL_KEY = "votesuccessful";
	public final static String FIREWALL_REPLACED_KEY = "firewallreplaced";
	public final static String FIREWALL_REMOVED_KEY = "firewallremoved";
	public final static String FTP_PASS_KEY = "ftppass";
	public final static String LABEL = "false";    

    public final static LinkedHashMap options = new LinkedHashMap();
    static {
		options.put(" Startup Options", LABEL);
        options.put("Start network application on startup", NETWORK_KEY);
        options.put("Show log window on startup", LOG_WINDOW_KEY);
        options.put("Show 'First Attack' Tutorial on startup", ATTACK_TUTORIAL_KEY);
        options.put(" Port Management Options", LABEL);
        // there's actually 3 options for a "yes/no" question:  do this always, never do this, always ask.
        options.put("&Replace the port note with the name of the newly installed application when replacing an application?", APP_NOTE_KEY);
        
        options.put("Show popup when you've successfully replaced an application.", APP_REPLACED_KEY);
		options.put("Show popup when you've successfully replaced a firewall.", FIREWALL_REPLACED_KEY);
		options.put("Show popup when you've successfully removed a firewall.", FIREWALL_REMOVED_KEY);
        options.put("Show heal successful message", HEALING_KEY);
        options.put("Always ask for confirmation when healing", HEALING_CONFIRM_KEY);
		options.put(" Equipment Manager Options", LABEL);
        options.put("Show popup when a card is repaired", CARD_REPAIRED_KEY);
        options.put("Show popup when a card is replaced", CARD_REPLACED_KEY);
		options.put(" Scan/Attack/Redirect Options", LABEL);
        options.put("Show attack confirmation", ATTACK_CONFIRM_KEY);
        //options.put("Show attack details", ATTACK_DETAILS_KEY);
        options.put("Show redirect confimation", REDIRECT_CONFIRM_KEY);
        //options.put("Show redirect details", REDIRECT_DETAILS_KEY);
        options.put("Show scan confirmation", SCAN_CONFIRM_KEY);
		options.put(" Commodity Converstion Options", LABEL);
        options.put("Show popup when successfully converted commodities to file.", COMMOD_TO_FILE_KEY);
        options.put("Show popup when successfully converted file to commodities.", FILE_TO_COMMOD_KEY);
		options.put(" Banking Options", LABEL);
        options.put("Show popup when you've successfully transferred money.", TRANSFER_TO_KEY);
        options.put("Show popup when you've received a transfer.", TRANSFER_FROM_KEY);
		options.put(" Web Browser", LABEL);
        options.put("Show popup when files successfully purchased.", FILE_PURCHASED_KEY);
        options.put("Show popup when you've successfully voted.", VOTE_SUCCESSFUL_KEY);
		//options.put(" FTP Options", LABEL);
		//options.put("%FTP Password", FTP_PASS_KEY);

    }
    
   
	//create array of checkboxes
	public ArrayList<JCheckBox> checkBoxes = new ArrayList<JCheckBox>(options.size());
	public ArrayList<XButtonGroup> buttonGroups = new ArrayList<XButtonGroup>(options.size());

    public void setValues() {
/*
System.out.println("---------------Hacker preferences in setValues() ---------------");
        Iterator jb = preferences.keySet().iterator();
        while (jb.hasNext()) {
            String key = (String)jb.next();
            Object o = preferences.get(key);
            String value = (String)o;
System.out.println(key + ": " + value);            
        }
*/
        // set the checkbox options
		Iterator<JCheckBox> itr = checkBoxes.iterator();
        String serverKey = "";
        String guiKey = "";
        boolean isSelected = true;
        String value = "";
		while (itr.hasNext()) {
			JCheckBox checkbox = itr.next();
            guiKey = (String)checkbox.getText();
            serverKey = (String)options.get(guiKey);
            Object o = preferences.get(serverKey);
            if (o != null) {
                value = (String)o;
            } else {
                value = "true";
            }
            checkbox.setSelected(new Boolean(value));
		}

        // set the radio button options
		Iterator<XButtonGroup> itr2 = buttonGroups.iterator();
		serverKey = "";
		guiKey = "";
		value = "";

		while (itr2.hasNext()) {
			XButtonGroup bg = itr2.next();
			guiKey = (String)bg.getName();
			serverKey = (String)options.get(guiKey);
            Object o = preferences.get(serverKey);
            if (o != null) {
                value = (String)o;
            } else {
                value = "ask";
            }
			bg.setSelect(value);
		}
    }
    
	//the width of the widest checkbox
	public OptionPanel(Hacker hacker, Dimension d) {
        MyHacker = hacker;

		double width = 650.00;
		double height = d.getHeight() * 0.75;

		Dimension newDim = new Dimension();
		newDim.setSize(width, height);

        preferences = MyHacker.getPreferences();
        if (preferences == null) {
            preferences = new HashMap();
        }
        

        if (preferences == null) {
            preferences = new HashMap();
        }
        
        addInternalFrameListener(this);
        
		JPanel panel = new JPanel();
		panel.setBackground(Constants.BLACK);
		MigLayout layout = new MigLayout("wrap 1");
		panel.setLayout(layout);

		//string code comparison
		String space = " ";
		String amper = "&";
		//go through list of options

		//Hashmap Iterator Yay
		Iterator itr = options.keySet().iterator();

		while (itr.hasNext()) {
			Object obj = itr.next();
			//coerce into string
			String item = obj.toString();
			//System.out.print(item);
			//if the label indication (leading space) is present...
			if (item.startsWith(space)) {
				addALabel(item, panel);
			}
			//if the label indication (leading &) is present...
			else if (item.startsWith(amper)) {
				addRadioButtons(item, panel);			
			}
            else {
            /*
                String value = "true";
                if (preferences != null) {
                    String networkKey = (String)options.get(item);
                    Object o = preferences.get(networkKey);
                    if (o != null) {
                        value = (String)o;
                    }
                }
				addACheckBox(item, panel, new Boolean(value));
                */
                addACheckBox(item, panel);
			}
		checkBoxes.trimToSize();
		}
        
        setValues();

		//create panel for buttons
		JPanel buttPanel = new JPanel();
		buttPanel.setBackground(Constants.BLACK);
		buttPanel.setLayout(new BoxLayout(buttPanel, BoxLayout.X_AXIS));		

		//create and add apply button w/ event listener
		String applytext = "Apply";
		JButton applyButton = new JButton(applytext);
		applyButton.addActionListener(this);
		buttPanel.add(applyButton);        

        String cancelText = "Cancel";
        JButton cancelButton = new JButton(cancelText);
		cancelButton.addActionListener(this);
		buttPanel.add(cancelButton);


		JScrollPane scrollPane = new JScrollPane(panel);

		setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		add(scrollPane);
		add(buttPanel);
        pack();
        setClosable(false);
        setIconifiable(false);
        setResizable(true);
        setTitle("Preferences");
		setSize(newDim);

	}

	//button event listener
	public void actionPerformed(ActionEvent e){
        if (e.getActionCommand().equals("Apply")) {
            setPreferences();
        } else if (e.getActionCommand().equals("Cancel")) {
        }
        MyHacker.setPreferencesOpen(false);
        dispose();
	}

	//add a label indicating a new section of options
	private void addALabel(String text, Container container) {
		JLabel label = new JLabel(text);
		label.setForeground(Constants.BLUE);	
		label.setAlignmentX(CENTER_ALIGNMENT);
		container.add(new JSeparator(), "growx");
		container.add(label, "growx");
	}

	//add radio boxes and a label for them
	private void addRadioButtons(String text, Container container) {
		JPanel radiopanel = new JPanel();
		radiopanel.setBorder(BorderFactory.createLineBorder(Constants.GREY));
		radiopanel.setBackground(Constants.BLACK);
		radiopanel.setLayout(new BoxLayout(radiopanel, BoxLayout.Y_AXIS));
		JLabel label = new JLabel(text.substring(1));

		label.setForeground(Constants.WHITE);	
		radiopanel.add(label);
		
		XButtonGroup bg = new XButtonGroup(text);

		for (int i = 0; i < RADIOBUTTONS.length; i++) {
			String rbtext = RADIOBUTTONS[i];
			JRadioButton rb = new JRadioButton(rbtext);
			rb.setBackground(Constants.BLACK);
			rb.setForeground(Constants.WHITE);
			bg.add(rb);
			bg.addButton(rb);
			radiopanel.add(rb);
		}
		//add to list of button groups for getting and setting selection
		buttonGroups.add(bg);

		//add to container
		//container.add(label, "");
		container.add(radiopanel);
	}


	//add a checkbox indicating a new sectio of options
	private void addACheckBox(String text, Container container) {
		//create checkbox, add to checkbox list
		JCheckBox checkbox = new JCheckBox(text);
        //JCheckBox checkbox = new JCheckBox(text);
		checkbox.setBorder(BorderFactory.createLineBorder(Color.black));
		checkbox.setForeground(Constants.WHITE);
		container.validate();
		checkbox.setBackground(Constants.BLACK);
		checkBoxes.add(checkbox);
		//set alignment
		//add to container		
		container.add(checkbox, "");	
	}

	private void setPreferences() {
//System.out.println("Setting preferences from OptionPanel");
        // iterate through the checkboxes
		Iterator<JCheckBox> itr1 = checkBoxes.iterator();
        String serverKey = "";
		while (itr1.hasNext()) {
			JCheckBox checkbox = itr1.next();
			boolean isSelected = checkbox.isSelected();
            serverKey = (String)options.get(checkbox.getText());
            preferences.put(serverKey, "" + isSelected);
		}
		
        // iterate through the radio buttons
		Iterator<XButtonGroup> itr2 = buttonGroups.iterator();
		serverKey = "";
		while (itr2.hasNext()) {
			XButtonGroup bg = itr2.next();
			String labelText = bg.getName();
			String value = bg.getSelect();
//System.out.println(labelText + ": " + value);
			serverKey = (String)options.get(labelText);
			preferences.put(serverKey, value);
		}

        // set the  preferences for this user on the GUI & server
        MyHacker.setPreferences(preferences);
        MyHacker.sendPreferences();
        
	}
}

