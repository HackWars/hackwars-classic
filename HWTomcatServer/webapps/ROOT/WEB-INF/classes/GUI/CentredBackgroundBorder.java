package GUI;
import java.awt.*;
import java.awt.image.*;
import javax.swing.border.*;
import javax.swing.*;
 
public class CentredBackgroundBorder implements Border {
    private final BufferedImage image;
    private Object parent;
    public CentredBackgroundBorder(BufferedImage image,Object parent) {
	    this.parent=parent;
        this.image = image;
    }
 
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
	    int x0=0;
	    int y0=0;
	    if(parent instanceof JDesktopPane){
		    x0 = x + (width-image.getWidth())/2-100;
		    y0 = y + (height-image.getHeight())/2-50;
	    }
	    else{
		    x0 = x + (width-image.getWidth())/2;
		    y0 = y + (height-image.getHeight())/2;
	    }
        g. drawImage(image, x0, y0, null);
    }
 
    public Insets getBorderInsets(Component c) {
        return new Insets(0,0,0,0);
    }
 
    public boolean isBorderOpaque() {
        return true;
    }
}

