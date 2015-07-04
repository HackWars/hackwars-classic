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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
This class is a container for OptionDialog so we can pop up a dialog and return a value from it.
*/
public class ConfirmationPanel {
	
	public static Object[] showYesNoDialog(Component frame, String title, String message){
		OptionDialog OD = new OptionDialog();
		return (OD.showYesNoDialog(frame,title,message, "Always perform this action."));
	}
	
	public static Object[] showOKDialog(Component frame, String title, String message){
		OptionDialog OD = new OptionDialog();
		return (OD.showOKDialog(frame,title,message));
	}
    
	public static Object[] showYesNoDialog(Component frame, String title, String message, String checkBoxText){
		OptionDialog OD = new OptionDialog();
		return (OD.showYesNoDialog(frame,title,message, checkBoxText));
	}

}
