package GUI;
/**
* Base class for all in game applications.
* @author Cameron McGuinness
*/
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
public class Application extends JInternalFrame implements ActionListener,MouseListener,InternalFrameListener{

	//public abstract void populate();
	public static Hacker MyHacker;
	
	public static void setHacker(Hacker hacker){
		MyHacker = hacker;
	}
	
	
	public void actionPerformed(ActionEvent e){

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

	}

	public void internalFrameClosing(InternalFrameEvent e) {
	}

	public void internalFrameClosed(InternalFrameEvent e) {
	}

	public void internalFrameOpened(InternalFrameEvent e) {
	}

	public void internalFrameIconified(InternalFrameEvent e) {
		JDesktopIcon DI = getDesktopIcon();
		DI.setBackground(new Color(41,42,41));
		
		JButton b = (JButton)DI.getUI().getAccessibleChild(DI,0);
		JLabel l = (JLabel)DI.getUI().getAccessibleChild(DI,1);
		//b.setFocusPainted(false);
		b.setContentAreaFilled(false);
		b.setForeground(Color.WHITE);
		//b.setBorderPainted(false);
		
		DI.getUI().update(DI.getGraphics(),DI);
		DI.setLocation(2,2);
		//System.out.println(getTitle()+"  "+DI.getPreferredSize().width);
		DI.setPreferredSize(new Dimension(120,25));
		//mainPanel.setComponentZOrder(DI,0);
		//mainPanel.remove(DI);
		MyHacker.addMinimizedFrame(DI);
	    
	}
	
	public void internalFrameDeiconified(InternalFrameEvent e) {
		MyHacker.removeMinimizedFrame(getDesktopIcon());
		try{
			   setIcon(false);
		    }catch(Exception ex){}
		MyHacker.getPanel().add(this);
		moveToFront();
	}

	public void internalFrameActivated(InternalFrameEvent e) {

	}

	public void internalFrameDeactivated(InternalFrameEvent e) {

	}
    
    public int showYesCancelDialog(String title, String message) {
        Object[] options = {"Yes", "Cancel"};
        return(showOptionsDialog(title, message, options));
    }
    
    public int showYesNoDialog(String title, String message) {
        Object[] options = {"Yes", "No"};
        return (showOptionsDialog(title, message, options));
    }
    
    public int showOptionsDialog(String title, String message, Object[] options) {
        int n = JOptionPane.showOptionDialog(this, message, title,
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[1]);
        return(n);
    }
    
    public int showDeleteDialog(String message) {
        return (showYesNoDialog("Delete?", "Are you sure you want to permanently delete " + message + "?"));
	}
    
    public int showSellDialog(String message) {
        return (showYesNoDialog("Sell all files?", "Are you sure you want to sell these " + message + " files?  All quantity of each file will be sold."));
    }
    
}
