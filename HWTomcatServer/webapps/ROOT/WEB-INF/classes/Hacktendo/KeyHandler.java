package Hacktendo;

import java.awt.*;
import java.awt.event.*;


public class KeyHandler extends KeyAdapter implements AWTEventListener {
	
	private RenderEngine RE=null;
	
	public KeyHandler(RenderEngine RE){
		this.RE=RE;
	}
   	/**
    	 * Notification of a key press
    	 *	 
    	 * @param e The event details
    	 */
	 
	public void keyPressed(KeyEvent e) {
    		if (e.isConsumed()) {
    			return;
     		}
            RE.keyPressed(e);
 	}
        
      /**
       * Notification of a key release
       * 
       * @param e The event details
       */
      public void keyReleased(KeyEvent e) {
      	if (e.isConsumed()) {
        		return;
        	}
        	
        	KeyEvent nextPress = (KeyEvent) Toolkit.getDefaultToolkit().getSystemEventQueue().peekEvent(KeyEvent.KEY_PRESSED);
        	
        	if ((nextPress == null) || (nextPress.getWhen() != e.getWhen()) || (nextPress.getKeyCode() != e.getKeyCode())) {
        		RE.keyReleased(e);
        	}  	
      }
        
      /**
       * Notification that an event has occured in the AWT event
       * system
       * 
       * @param e The event details
       */
      public void eventDispatched(AWTEvent e) {
          	if (e.getID() == KeyEvent.KEY_PRESSED) {
              	keyPressed((KeyEvent) e);
          	}
          	if (e.getID() == KeyEvent.KEY_RELEASED) {
              	keyReleased((KeyEvent) e);
            }
     }   
}
