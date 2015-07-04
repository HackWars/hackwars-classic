

package chat.client;
import java.awt.Component;
import java.awt.Color;
import javax.swing.JList;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.ListCellRenderer;

import javax.swing.text.DefaultStyledDocument;
import java.awt.Font;


public class HtmlCellRender implements ListCellRenderer{
    public static JLabel regularLabel=null;
    public static JLabel adminLabel = null;
    public static JLabel modLabel = null;
    public Component getListCellRendererComponent(JList list,
                                                   Object value,
                                                   int index,
                                                   boolean isSelected,
                                                   boolean cellHasFocus) {
         String name = (String)value;
         if(regularLabel==null){
            adminLabel = new JLabel(name);
            modLabel = new JLabel(name);
             regularLabel = new JLabel(name);
             Font f = new Font("monospaced",Font.PLAIN,14);
    	     regularLabel.setFont(f);
    	     regularLabel.setOpaque(true);
             regularLabel.setForeground(Color.white);
             regularLabel.setBackground( Color.black);
             
    	     adminLabel.setFont(f);
    	     adminLabel.setOpaque(true);
             adminLabel.setForeground(viewChatWindow.ADMIN_COLOR);
             adminLabel.setBackground(Color.black);

    	     modLabel.setFont(f);
    	     modLabel.setOpaque(true);
             modLabel.setForeground(viewChatWindow.MOD_COLOR);
             modLabel.setBackground(Color.black);
         }
         
           if (name.equals("geckotoss") || name.equals("johnny_heart") || name.equals("mecha_cephalon") || 
					name.equals("sixteen_faces") || name.equals("terribletriojoe") || name.equals("draconisravenix")) {
                 adminLabel.setText(name);
                 if(isSelected == true){
                     adminLabel.setBackground(new Color(41,42,41));
                 }
                 else{
                    adminLabel.setBackground(Color.black);
                }
                 return adminLabel;

           }
           
//(name.equals("terribletriojoe") || name.equals("jamjardavies") || name.equals("ltlwinters") || name.equals("surfpup") ||name.equals("selamat_daatang") || name.equals("draconisravenix") || name.equals("silverlight")
           else if(ChatController.Admins.get(name)!=null){
				 modLabel.setText(name);
                 if(isSelected == true){
                     modLabel.setBackground(new Color(41,42,41));
                 }
                 else{
                    modLabel.setBackground(Color.black);
                }
                 return modLabel;

           }
           else {
                regularLabel.setText(name);
                if(isSelected == true){
                    regularLabel.setBackground(new Color(41,42,41));
                }
                else{
                    regularLabel.setBackground(Color.black);
                }
                return regularLabel;
           }


     }
}
