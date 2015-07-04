

package chat.client;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.Component;
import javax.swing.JTree;
import javax.swing.JLabel;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.Font;
import java.awt.Color;

public class RendererJTree extends DefaultTreeCellRenderer {
    
    public Component getTreeCellRendererComponent(JTree tree, Object value, 
                            boolean selected, boolean expanded, boolean leaf, 
                            int row, boolean hasFocus) {
        
        
        if(value instanceof DefaultMutableTreeNode){
            DefaultMutableTreeNode v2 = (DefaultMutableTreeNode)value;
            Object obj = v2.getUserObject();
            
            //First row header, comment
              //leveing as the default
            if(obj instanceof String){
                return super.getTreeCellRendererComponent(tree,value,selected,expanded,leaf,row,hasFocus);

            //Second row, user names
              //Custom!
            }else if(obj instanceof StructRelation){
                StructRelation relation = (StructRelation) obj;
                String labelText = relation.getName();
		JLabel label = new JLabel();
                if(relation.isIgnore() == true){
                    labelText += " Ignore" ;
		    label.setForeground(Color.red);
                    //Technically, they can have both at once,
                    //I'm pretty sure I disabled it though,
                }else if( relation.isOnline() == true){
                    label.setForeground(Color.green);
                }
		else if(relation.isOnline()==false){
			label.setForeground(Color.red);
		}
                label.setText(labelText);
                

                
                Font f = new Font("monospaced",Font.PLAIN,14);
                label.setFont(f);
                label.setOpaque(true);
                 //label.setForeground(Color.white);
                 if(selected == true){
                     label.setBackground( new Color(41,42,41));
                     //tPane.setForeground(Color.white);
                 }else{
                     label.setBackground( Color.black);
                 }

                return label;
            }else{
                //No idea WTH this is print an error messge
                return new JLabel("Unknown type in RendererJTree" + value.getClass().getName() );
            }
        }
        return super.getTreeCellRendererComponent(tree,value,selected,expanded,leaf,row,hasFocus);

    }
    
}
