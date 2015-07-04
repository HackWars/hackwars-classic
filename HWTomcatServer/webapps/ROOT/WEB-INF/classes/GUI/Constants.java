package GUI;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.*;

public class Constants {
	
	public final static int MAX_PORTS = 32;
	public final static float[] cpuTypeMaxPorts = new float[]{50.0f,100.0f,150.0f,200.0f,250.0f,300.0f,75.0f};//Maximum Loads of various CPUs.
	public final static int[] memoryTypeMaxPorts = new int[] {8,16,24,32,8};
    public final static String applicationType[] = {"Bank", "Attack", "Redirect", "FTP", "HTTP"};
  	public final static String firewalls[] = {"None","Basic","Medium","Greater","Basic Attacking","Medium Attacking","Greater Attacking","Ultimate Attacking"};
    public final static String watchFirewalls[] = {"None","PortProtector","PwnPreventer","DataShield","PacketBuster","TrafficTender","DigitalFortress","ForceField","RubyGuardian","DiamondDefender","ADNArmour"};
    public final static String watchTypes[] = {"Health","Petty Cash","Scan"};

	public static final Color YELLOW = new Color(255, 255, 153);
	public static final Color GREEN = new Color(153, 255, 153);
	public static final Color WHITE = new Color(255, 255, 255);
	public static final Color BLUE = new Color(153, 204, 255);
	public static final Color GREY = new Color(125, 125, 125);
	public static final Color BLACK = new Color(0, 0, 0);
	public static final Font BIGFONT=new Font("monospace",Font.BOLD,18);
	//individual NPCs and counts
	public static final Font SMALLFONT = new Font("monospace",Font.BOLD,12);
	//NPC List 
	public static final Font MEDIUMFONT = new Font("monospace",Font.BOLD,14);
	
	public static final DecimalFormat ONE_DIGIT = new DecimalFormat("#0.0");
	public static final DecimalFormat TWO_DIGITS = new DecimalFormat("#0.00");

}
