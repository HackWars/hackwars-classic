package GUI;
/**

ManageBookmarks.java
this is where the user manages their bookmarks.
*/

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.undo.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.table.TableModel;

import View.*;


public class ManageBookmarks extends Application implements TableModelListener{
	
	private Hacker MyHacker;
	private Object[] bookmarks;
	private JTable table;
	private GridBagConstraints c = new GridBagConstraints();
	private BookmarkTableModel model;
	private WebBrowser webBrowser;
	public ManageBookmarks(Hacker MyHacker,WebBrowser webBrowser){
		this.MyHacker=MyHacker;
		this.webBrowser=webBrowser;
		setTitle("Manage Bookmarks");
		setResizable(true);
		setMaximizable(true);
		setClosable(true);
		setIconifiable(true);
		setLayout(new GridBagLayout());
		populate();
	}
	
	public synchronized void populate(){
		JButton button;
		//toolbar
		JToolBar toolBar = new JToolBar("Tools");
		
		button = new JButton(ImageLoader.getImageIcon("images/close.png"));
		button.setActionCommand("delete");
		button.setToolTipText("Delete Bookmark.");
		button.addActionListener(this);
		toolBar.add(button);
		c.gridx=0;
		c.gridy=0;
		c.fill=GridBagConstraints.HORIZONTAL;
		add(toolBar,c);
		
		String[] headers = new String[]{"Name","Location"};
		ArrayList marks = MyHacker.getBookmarks();
		bookmarks = marks.toArray();
		String[][] data = new String[bookmarks.length][2];
		for(int i=0;i<bookmarks.length;i++){
			Object[] mark = (Object[])bookmarks[i];
			String domain = (String)mark[0];
			String name = (String)mark[1];
			String folder = (String)mark[2];
			data[i] = new String[]{name,domain};
		}
			
		//System.out.println("Creating Table Model");
		model = new BookmarkTableModel(data,headers);
		table = new JTable(model);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane sp = new JScrollPane(table);
		c.gridy=1;
		c.fill=GridBagConstraints.BOTH;
		c.weighty=1.0;
		c.weightx=1.0;
		add(sp,c);
		model.addTableModelListener(this);
		
	}
	
	//private void redoTable(){
		//table.getModel().resetData();
	
	
	public void tableChanged(TableModelEvent e) {
        int row = e.getFirstRow();
        int column = e.getColumn();
        //TableModel model = (TableModel)e.getSource();
        String columnName = model.getColumnName(column);
        String data = (String)model.getValueAt(row, column); 
		Object[] selected = (Object[])bookmarks[row];
		int id = Integer.parseInt((String)selected[3]);
		Object[] params = new Object[]{id,data};
		XMLRPCCall.execute("http://www.hackwars.net/xmlrpc/bookmarks.php","editBookmark",params);
		//System.out.println(data);
		webBrowser.editBookmark(row,data);
    }
	
	public void actionPerformed(ActionEvent e){
		if(e.getActionCommand().equals("delete")){
			int row = table.getSelectedRow();
			Object[] selected = (Object[])bookmarks[row];
			int id = Integer.parseInt((String)selected[3]);
			//System.out.println("DELETE ID: "+id);
			
			Object[] params = new Object[]{id};
			XMLRPCCall.execute("http://www.hackwars.net/xmlrpc/bookmarks.php","deleteBookmark",params);
			model.removeRow(row);
			webBrowser.removeBookmark(row);
			
		}
	}

	
}
