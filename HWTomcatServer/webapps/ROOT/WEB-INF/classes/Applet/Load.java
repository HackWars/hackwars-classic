package Applet;
/**
Load.java

Performs the loading step necessary to bootstrap the GUI.
*/
import java.applet.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.*;
import java.io.*;
import java.util.*;
import java.util.zip.*;
import java.security.*;
import View.*;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import javax.imageio.*;
import java.net.URL;
import GUI.*;
public class Load extends JApplet implements ActionListener{
	private final String checksum="dec 19";
	private boolean LoaderSet=false;
	private String ip="hackwars.net";
	private JLabel message=null;
	private JPanel LoginFrame=null;
	private JTextField usernameField,ipField;
	private View MyView=null;
	private JButton button=null;
	private JProgressBar JPB=null;
	private JLabel label = null;
	private JPanel panel=null;
	private String clientDate="Jan 9th, 2010 @ 8:00 PM";

	public Load(){
		//getFiles(JPB);
	}
	
	public void init(){
		getContentPane().setBackground(new Color(255,255,255));
		getContentPane().setLayout(null);
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
	    }catch(Exception e){
			e.printStackTrace();
		}
		createLoginWidget();
		getFiles(JPB);
		reconnect();
	}
	
	public void stop(){
		if(MyView!=null)
			MyView.clean();
	}
	
	public void createLoginWidget(){
		LoginFrame=new JPanel();
		LoginFrame.setSize(250,150);
		LoginFrame.setLocation(5,50);
		SpringLayout layout = new SpringLayout();
		LoginFrame.setLayout(null);
		
		int height=8;
		panel = new JPanel();
		panel.setLayout(layout);
		try{
			//System.out.println("Setting Border");
			panel.setBorder(new CentredBackgroundBorder(ImageIO.read(new URL("http://www.hackwars.net/images/loginback.png")),panel));
		}catch(Exception e){e.printStackTrace();}
		panel.validate();
		message = new JLabel("<html><font color=\"#FF0000\">Invalid Username/Password. <br>Please Try Again.</font></html>");
		message.setVisible(false);
		panel.add(message);
		layout.putConstraint(SpringLayout.NORTH,message,height,SpringLayout.NORTH,panel);
		layout.putConstraint(SpringLayout.WEST,message,22,SpringLayout.WEST,panel);
		height+=message.getPreferredSize().height+5;
		
		
		Object[] response=null;
		try{
			//String visitor_ip = (new Socket(getDocumentBase().getHost(), getDocumentBase().getPort())).getLocalAddress().getHostAddress();

			XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
			config.setServerURL(new URL("http://hackwars.net/xmlrpc/checkdate.php"));
			XmlRpcClient client = new XmlRpcClient();
			client.setConfig(config);
			Object[] params = {clientDate};
			response = (Object[])client.execute("login",params);
		}catch(Exception ex){ex.printStackTrace();}
		boolean correct = (Boolean)response[0];
		if(!correct){
			setMessage((String)response[1]);
			
		}
		else{
			label = new JLabel("Username: ");
			panel.add(label);
			layout.putConstraint(SpringLayout.NORTH,label,height,SpringLayout.NORTH,panel);
			layout.putConstraint(SpringLayout.WEST,label,22,SpringLayout.WEST,panel);
			//int height=label.getPreferredSize().height+10;
			int width=label.getPreferredSize().width+32;
			
			usernameField = new JTextField("",10);
			panel.add(usernameField);
			layout.putConstraint(SpringLayout.NORTH,usernameField,height,SpringLayout.NORTH,panel);
			layout.putConstraint(SpringLayout.WEST,usernameField,width,SpringLayout.WEST,panel);
			height+=usernameField.getPreferredSize().height+5;
			
			label = new JLabel("Password: ");
			panel.add(label);
			layout.putConstraint(SpringLayout.NORTH,label,height,SpringLayout.NORTH,panel);
			layout.putConstraint(SpringLayout.WEST,label,22,SpringLayout.WEST,panel);
			
			
			ipField = new JPasswordField("",10);
			ipField.addActionListener(this);
			ipField.setActionCommand("Login");
			panel.add(ipField);
			layout.putConstraint(SpringLayout.NORTH,ipField,height,SpringLayout.NORTH,panel);
			layout.putConstraint(SpringLayout.WEST,ipField,width,SpringLayout.WEST,panel);
			height+=ipField.getPreferredSize().height+5;
			
			button = new JButton("Login");
			panel.add(button);
			button.addActionListener(this);
			layout.putConstraint(SpringLayout.NORTH,button,height,SpringLayout.NORTH,panel);
			layout.putConstraint(SpringLayout.WEST,button,width,SpringLayout.WEST,panel);
			height+=button.getPreferredSize().height+5;
			
			JLabel date = new JLabel(clientDate);
			Font f = new Font("dialog",Font.BOLD,10);
			date.setFont(f);
			panel.add(date);
			layout.putConstraint(SpringLayout.NORTH,date,145-date.getPreferredSize().height,SpringLayout.NORTH,panel);
			layout.putConstraint(SpringLayout.WEST,date,240-date.getPreferredSize().width,SpringLayout.WEST,panel);
			
		}
		
		
		LoginFrame.add(panel);
		panel.setBounds(0,0,250,150);
		LoginFrame.setVisible(true);

		
		this.add(LoginFrame);
		
		
	}
	
	/**
	Download any files that might currently be needed.
	*/
	private int imageCount=86;
	
	public void getFiles(JProgressBar JPB){
		//System.out.println("Getting Images");
		if(JPB==null)
			JPB=new JProgressBar();
		panel.setVisible(false);
		JPB.setVisible(true);
	
		JPB.setIndeterminate(false);
		JPB.setStringPainted(true);

		JPB.setString("Downloading Images 0%");
		JPB.setMinimum(0);
		JPB.setMaximum(imageCount);
		boolean download=false;
		//Check to see whether we have the images directory.
		String tmpDir = System.getProperty("java.io.tmpdir");
		File CF=new File(tmpDir+"/images/checksum.txt");
		if(CF.exists()){
			//System.out.println("File Exists");
			try{

				BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(CF)));
				String check="";
				try{
					check=in.readLine().toLowerCase();
				}catch(Exception e){
					download=true;
				}
				//System.out.println("|"+check.length()+"|   |"+checksum.length()+"|");
				if(!(check.equals(checksum))){
					//System.out.println("Not Equal");
					download=true;
				}
				in.close();

			}catch(Exception e){e.printStackTrace();}
		}
		else{
			//System.out.println("File Does not Exist");
			download=true;
		}
		if(download){//Download the image pack.
			//System.out.println("Download");
			new File(tmpDir+"/images/").mkdirs();
			try{
				//ip="www.hackwars.net";
				//ByteArrayOutputStream Buffer=new ByteArrayOutputStream();
				ZipInputStream in=new ZipInputStream((new URL("http://www.crackjawpublishing.com/images.zip")).openStream());
				ZipEntry data=null;
				while((data=in.getNextEntry())!=null){
					if(!data.getName().equals(".DS_Store")){
						try{
							FileOutputStream FOS=new FileOutputStream(tmpDir+"/images/"+data.getName());
							
							int i=0;
							byte buffer[]=new byte[512];
							while((i=in.read(buffer))>0)
								FOS.write(buffer,0,i);
							FOS.close();
							
							System.out.println(data.getName()+" Unzipped.");
						}catch(Exception e){
							System.out.println(data.getName()+" Failed To Unzip.");
						}

						
					}
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		JPB.setIndeterminate(true);
		JPB.setString("Initializing Classes");
		JPB.setVisible(false);
		panel.setVisible(true);
	}


	public void linkGo(URL link){
		AppletContext a = getAppletContext();
		a.showDocument(link,"_blank");
		
	}

	//Action Listener.
	public void actionPerformed(ActionEvent e){
		button.setEnabled(false);
		message.setVisible(false);
		String username = usernameField.getText();
		String password = ipField.getText();
		//System.out.println("Attempting Login");
		Object[] response=new Object[]{};
		try{
			//String visitor_ip = (new Socket(getDocumentBase().getHost(), getDocumentBase().getPort())).getLocalAddress().getHostAddress();

			XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
			config.setServerURL(new URL("http://hackwars.net/xmlrpc/loginrpc.php"));
			XmlRpcClient client = new XmlRpcClient();
			client.setConfig(config);
			Object[] params = {username,password,clientDate};
			response = (Object[])client.execute("login",params);
		}catch(Exception ex){ex.printStackTrace();}
		boolean correct = (Boolean)response[0];
		String ip = (String)response[1];
		boolean npc = (Boolean)response[2];
		String message = (String)response[4];
		if(correct){
			//System.out.println("Login Successful");
			MyView.loginToServer(username,password,ip);
		}
		else{
			button.setEnabled(true);
			setMessage(message);
		}
		//MyView.loginToServer(username,password,ip);
	}
	
	public void setMessage(String message){
		this.message.setText(message);
		this.message.setVisible(true);
	}
		
	
	/**
	Exit program tells me that the panel containing the Hack Wars program has been closed.
	*/
	public void exitProgram(){
		if(MyView!=null)
			MyView.clean();
		reconnect();
		panel.setVisible(true);
		button.setEnabled(true);
	}
	
	public void loginFailed(){
		panel.setVisible(true);
		message.setVisible(true);
	}
	
	/**
	Finished loading tells me it's finished loading.
	*/
	public void finishedLoading(){
		panel.setVisible(true);
	}
	
	/**
	Connect to the Hack Wars Server.
	*/
	public void reconnect(){
		MyView=new View(ip,this);
	}
	
	//Testing main.
	public static void main(String args[]){
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
	    }catch(Exception e){
		}
		//System.setErr(null);
		Load MyLoad=new Load();
	}
}
