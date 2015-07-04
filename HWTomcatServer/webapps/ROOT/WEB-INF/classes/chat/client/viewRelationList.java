
package chat.client;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.tree.DefaultTreeModel;


//JTree stuff
import javax.swing.tree.DefaultMutableTreeNode;
import java.util.concurrent.Semaphore;

import java.util.TreeMap;
import chat.util.StringCompare;


public class viewRelationList extends JFrame implements absViewRelationList,ActionListener {
    
    private JTree treeRelationList;
    private JScrollPane treeScrollPane;
    private DefaultMutableTreeNode treeRoot;
    private DefaultTreeModel treeModel;
    private Semaphore semiTree = new Semaphore(1, true);
    
    
    private ChatController controller = null;
    
    public viewRelationList(ChatController controller){
        this.controller = controller;
        this.setLayout(new GridBagLayout());
        setTitle("Friends List");
	setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        createMenu();
	guiJTreeRelation();
	setPreferredSize(new Dimension(200,600));
        setVisible(true);
        pack(); 
    }
    
     /*
     * --------------------------- GUI SETUP ---------------------------------------
     */   
     
     private void createMenu(){
	JMenuBar menuBar = new JMenuBar();
	JMenu menu;
	JMenuItem menuItem;
	
	menu=new JMenu("Friends");
	menuItem=new JMenuItem("Add Friend");
	menuItem.addActionListener(this);
	menu.add(menuItem);
	menuBar.add(menu);
	
	
	setJMenuBar(menuBar);
	
     }
    
    private JScrollPane guiJTreeRelation(){
        treeRoot = new DefaultMutableTreeNode("Root");
	treeRelationList = new JTree(treeRoot);
	treeRelationList.setRootVisible(false);
        treeRelationList = new JTree(treeModel);
        treeRelationList.setCellRenderer( new RendererJTree());
        
        treeModel = new DefaultTreeModel(treeRoot);
        treeRelationList.setModel(treeModel);
        //textMsgList.setCellRenderer(textMsgRenderer);
        
	treeRelationList.setBackground(Color.black);
        treeRelationList.setFont(new Font("monospaced",Font.PLAIN,14));
	treeRelationList.setForeground(Color.white);
        
        //One selected at a time
       // textMsgList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); 
        
        GridBagConstraints c = new GridBagConstraints();
         //x,y postion
        c.gridx =0;
        c.gridy =0;
         //size on grid
        c.gridheight = 1;
        c.gridwidth = 1;
         //paddinging, ignored with expand I think,
        c.ipadx = 2;
        c.ipady = 2;

         //Expand in the x and y directions? 1 = full, 0 = no, inbetween is wierd
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.fill = c.BOTH;
        
        //Scrollbar
        treeScrollPane = new JScrollPane(treeRelationList);
        this.add(treeScrollPane, c);
        return treeScrollPane;
    }     
    
    /*
     * ------------------- ABSTRACT VIEW RELATION LIST -----------------------------
     */
    
    
    //Tree Node is NOT thread safe, so if two things access it at the same time without a semiphore, 
                    //bad shit happens,
    public void addRelation(StructRelation relation){
        if(relation == null){
		System.out.println("Relation is null");
            return;
        }
        //System.out.println(relation.getName()+"   |"+relation.getComment()+"|");
        //Dought very much that tree model is thread safe
        try{
            semiTree.acquire();
            
            //Get the folder/directory
	    String comment = relation.getComment();
	    if(comment.equals(""))
		    comment="None";
            DefaultMutableTreeNode parent = getComment(comment);
	    //DefaultMutableTreeNode parent = getComment("Guild");
	    
            
            //dosent have the directory/folder, so add it
            if(parent == null){
                parent = new DefaultMutableTreeNode(comment);
		//parent = new DefaultMutableTreeNode("Guild");
		//System.out.println("Couldn't Find "+comment);
                treeModel.insertNodeInto(parent,treeRoot, treeRoot.getChildCount());
            }
            
            //add the file to the directory
            DefaultMutableTreeNode child = new DefaultMutableTreeNode(relation);
            
            parent.add(child);
            
            treeModel.insertNodeInto(child, parent, 0);
            //treeRoot.remove(parent);
            
            //treeRelationList.scrollPathToVisible(new TreePath( parent.getPath() ));
            treeRelationList.scrollPathToVisible(new TreePath( child.getPath() ));
            treeRelationList.validate();
            //System.out.println("Added to relation list" + relation.getName());
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            semiTree.release();
        }
    }
    
    public void removeRelation(String userName) {
        if(userName == null){
            return;
        }
        
        //Dought very much that tree model is thread safe
        try{
            semiTree.acquire();
            int size = treeRoot.getLeafCount();
            int loopc = 0;
            //Go through all first level branches
            while(loopc < size){
                DefaultMutableTreeNode cNode = (DefaultMutableTreeNode)treeRoot.getChildAt(loopc);
                
                //Ok now we need to check this path
                int sizex = cNode.getChildCount();
                int loopx = 0;
                while(loopx < sizex){
                    DefaultMutableTreeNode rNode = (DefaultMutableTreeNode)cNode.getChildAt(loopx);
                    StructRelation relation = (StructRelation)rNode.getUserObject();
                    
                    //find the user to remove!
                    if(relation.getName().compareToIgnoreCase(userName) == 0){
                        //remove the user
                        cNode.remove(rNode);
                        //Is this comment branch empty? kill it! With fire!
                        if(cNode.getChildCount() == 0){
                            treeRoot.remove(cNode);
                        }
                        return;
                    }
                    
                    loopx++;
                }

                loopc++;
            }
            
        }catch(Exception e){
        }finally{
            semiTree.release();
        }
    }  
    
    //Dont need semiphores since its allways called within things that allready do,
    //WILL thread lock if you add them
    private DefaultMutableTreeNode getComment(String s){
        int size = treeRoot.getChildCount();
        int loopc = 0;
        while(loopc < size){
            DefaultMutableTreeNode cNode = (DefaultMutableTreeNode)treeRoot.getChildAt(loopc);
            String comment = (String) cNode.getUserObject();
            if(comment.compareToIgnoreCase(s) == 0){
                return cNode;
            }
            loopc++;
        }
        return null;
    }
    
    public void actionPerformed(ActionEvent e){
	    if(e.getActionCommand().equals("Add Friend")){
		    AddFriendDialog AFD = new AddFriendDialog("",controller);
	    }
    }
    
}
