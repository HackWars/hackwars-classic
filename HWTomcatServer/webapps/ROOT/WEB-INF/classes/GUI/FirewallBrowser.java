package GUI;
/**

Home.java
Represents the file browser.
*/
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import Game.*;
import View.*;
import Assignments.*;
import java.text.*;
import java.util.*;
import java.net.URL;
import net.miginfocom.swing.*;
//import java.awt.font.FontMetrics;

public class FirewallBrowser extends Application implements ComponentListener, KeyListener {
	
    //indexes in array
	public static final int NAME = 0;
	public static final int SELL_TO_STORE_PRICE = 1;
	public static final int EQUIP_LEVEL = 2;
	public static final int ATTACK_DAMAGE = 3;
    public static final int BANK_ABSORPTION = 4;
	public static final int ATTACK_ABSORPTION = 5;
	public static final int REDIRECT_ABSORPTION = 6;
	public static final int HTTP_ABSORPTION = 7;
	public static final int FTP_ABSORPTION = 8;
	public static final int SPECIAL1 = 9;
	public static final int SPECIAL2 = 10;
	public static final int CPU_COST = 11;
	public static final int QUANTITY = 12;
	public static final int YOUR_STORE_PRICE = 13;
	public static final int MAKER = 14;
	
	//table columns
	public static final String[] columns = new String[15];
    static {
        columns[NAME] = "Name";
        columns[SELL_TO_STORE_PRICE] = "Store Buy Price";
        columns[EQUIP_LEVEL] = "Equip Level";
        columns[ATTACK_DAMAGE] = "Attack Damage";
        columns[BANK_ABSORPTION] = "Bank Damage Allowed";
        columns[ATTACK_ABSORPTION] = "Attack Damage Allowed";
        columns[REDIRECT_ABSORPTION] = "Redirect Damage Allowed";
        columns[HTTP_ABSORPTION] = "HTTP Damage Allowed";
        columns[FTP_ABSORPTION] = "FTP Damage Allowed";
        columns[SPECIAL1] = "Attribute 1";
        columns[SPECIAL2] = "Attribute 2";
        columns[CPU_COST] = "CPU Cost";
        columns[YOUR_STORE_PRICE] = "Your Price";
        columns[MAKER] = "Maker";
        columns[QUANTITY] = "Quantity";
    };

	private Hacker MyHacker;
	private Object[] directory,shownDirectory;
	private JToolBar toolBar;
	private JPanel buttonPanel;
	private JTable table;
	private SortableTableModel tableModel;
	private String[] buttonList = new String[0];
	private JScrollPane mainScroll;
	private String folder;
    private static int SORT_IMAGE_WIDTH = BevelArrowIcon.DEFAULT_SIZE + 15; // the +5 is for the padding on the columns, can't be assed to figure  out how to get it.
	
	public FirewallBrowser(Hacker MyHacker){
		this.setTitle("Firewall Browser");
		this.setResizable(true);
		this.setMaximizable(true);
		this.setClosable(true);
		this.setIconifiable(true);
		this.addInternalFrameListener(this);
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		this.MyHacker=MyHacker;
		directory = MyHacker.getCurrentDirectory();
		folder = MyHacker.getCurrentFolder();
		this.setFrameIcon(ImageLoader.getImageIcon("images/firewall.png"));
		setLayout(new MigLayout("fill"));
		populate();
	}
	
	public void populate(){
		createToolbar();
		buttonPanel = new JPanel();
		JButton button = new JButton("Home");
		buttonPanel.setLayout(new FlowLayout(FlowLayout.LEADING,2,0));
		buttonPanel.setMinimumSize(new Dimension(0,button.getPreferredSize().height+5));
		Object[][] data = {};
		tableModel = new SortableTableModel(SortableTableModel.FIREWALL);
		tableModel.setDataVector(data,columns);
		table = new JTable(tableModel);
		table.addMouseListener(this);
		table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		table.setAutoCreateColumnsFromModel(false);
		
		SortButtonRenderer renderer = new SortButtonRenderer();
		TableColumnModel model = table.getColumnModel();
		JTableHeader header = table.getTableHeader();
        Font f = header.getDefaultRenderer().getTableCellRendererComponent(table, "", false, false, 0, 0).getFont();
        FontMetrics fm = header.getFontMetrics(f);
        
		int n = columns.length;
		int totalWidth = 0;
		int[] columnWidth = new int[columns.length];
		columnWidth[NAME] = 120;
		columnWidth[SELL_TO_STORE_PRICE] = 50;//SwingUtilities.computeStringWidth(fm, columns[SELL_TO_STORE_PRICE]) + SORT_IMAGE_WIDTH; //getStringWidthPixels(g, columns[SELL_TO_STORE_PRICE]);
		columnWidth[EQUIP_LEVEL] = 50;//SwingUtilities.computeStringWidth(fm, columns[EQUIP_LEVEL]) + SORT_IMAGE_WIDTH;
		columnWidth[ATTACK_DAMAGE] = 50;//SwingUtilities.computeStringWidth(fm, columns[ATTACK_DAMAGE]) + SORT_IMAGE_WIDTH;
		columnWidth[QUANTITY] = SwingUtilities.computeStringWidth(fm, columns[QUANTITY]) + SORT_IMAGE_WIDTH;
		columnWidth[YOUR_STORE_PRICE] = SwingUtilities.computeStringWidth(fm, columns[YOUR_STORE_PRICE]) + SORT_IMAGE_WIDTH;;
		columnWidth[CPU_COST] = SwingUtilities.computeStringWidth(fm, columns[CPU_COST]) + SORT_IMAGE_WIDTH;;
		columnWidth[BANK_ABSORPTION] = columnWidth[ATTACK_ABSORPTION] = columnWidth[REDIRECT_ABSORPTION] = columnWidth[HTTP_ABSORPTION] = columnWidth[FTP_ABSORPTION] = 70;
		columnWidth[SPECIAL1] = 150;
		columnWidth[SPECIAL2] = 150;
		columnWidth[MAKER] = 1000;
		for (int i=0;i<n;i++) {
		  model.getColumn(i).setHeaderRenderer(renderer);
		  model.getColumn(i).setPreferredWidth(columnWidth[i]);
		}
		header.addMouseListener(new HeaderListener(header,renderer,MyHacker));
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.addKeyListener(this);
		mainScroll = new JScrollPane(table);
		mainScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		mainScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		add(mainScroll,"grow");
	}
    
    private int getStringWidthPixels(Graphics g, String text) {
		FontMetrics metric = g.getFontMetrics();
		return metric.stringWidth(text);
    }
	
	private void createToolbar(){
		toolBar = new JToolBar("Tools");
		JButton button;
		
		button = new JButton(ImageLoader.getImageIcon("images/refresh.png"));
		button.addActionListener(this);
		button.setActionCommand("Refresh");
		button.setToolTipText("Refresh.");
		toolBar.add(button);
		
		/*button = new JButton(ImageLoader.getImageIcon("images/up.png"));
		button.addActionListener(this);
		button.setActionCommand("Up");
		button.setToolTipText("Up One Directory.");
		toolBar.add(button);*/
		
		toolBar.addSeparator();
		
		button = new JButton(ImageLoader.getImageIcon("images/close.png"));
		button.addActionListener(this);
		button.setActionCommand("Delete");
		button.setToolTipText("Delete selected file(s) or folder(s).");
		toolBar.add(button);
		
		toolBar.addSeparator();
		
		button = new JButton(ImageLoader.getImageIcon("images/calc.png"));
		button.addActionListener(this);
		button.setActionCommand("Sell");
		button.setToolTipText("Sell file(s) to Store.");
		toolBar.add(button);
		
		this.add(toolBar,"wrap,growx");
	}
	
	public void startUp(){
		View MyView = MyHacker.getView();
		Object objects[] = {MyHacker.getEncryptedIP(),""};
		MyHacker.setCurrentFolder("");
		folder="";
		MyView.setFunction("requestdirectory");
		MyView.addFunctionCall(new RemoteFunctionCall(Hacker.FIREWALL_BROWSER,"requestdirectory",objects));
		MyHacker.setRequestedDirectory(Hacker.FIREWALL_BROWSER);
	}
	
	public void setDirectory(Object[] directory){
		int j=0;
		if(this.directory==null||this.directory.length!=directory.length){
			tableModel.resetData();
			try{
				if(directory!=null){
					//System.out.println("Setting shownDirectory");
					shownDirectory = new Object[directory.length];
					Object[] rowData=new Object[columns.length];
					for(int i=0;i<directory.length;i++){
						if(directory[i] instanceof String){
							if(!((String)(directory[i])).equals("Store")&&!((String)(directory[i])).equals("Public")){
							}
						}
						else{
							Object dir[] = (Object[])directory[i];
							int type=(Integer)dir[1];
							if(type==HackerFile.NEW_FIREWALL){
								HashMap content = (HashMap)dir[8];
								shownDirectory[j]=dir;
								rowData[NAME] = (String)dir[0];
								rowData[QUANTITY] = (Integer)dir[2];						
								rowData[YOUR_STORE_PRICE] = (Float)dir[3];
								rowData[SELL_TO_STORE_PRICE] = (Float)dir[7];
								rowData[MAKER] = (String)dir[4];
								rowData[CPU_COST] = (Float)dir[5];
								rowData[ATTACK_DAMAGE] = Integer.parseInt((String)content.get("attack_damage"));
								rowData[EQUIP_LEVEL] = Integer.parseInt((String)content.get("equip_level"));
								float percent = 0.0f;
								percent = ((float)Float.parseFloat((String)content.get("bank_damage_modifier")))*100;
								rowData[BANK_ABSORPTION] = Constants.TWO_DIGITS.format(percent)+"%";
								percent = ((float)Float.parseFloat((String)content.get("attack_damage_modifier")))*100;
								rowData[ATTACK_ABSORPTION] = Constants.TWO_DIGITS.format(percent)+"%";
								percent = ((float)Float.parseFloat((String)content.get("redirect_damage_modifier")))*100;
								rowData[REDIRECT_ABSORPTION] = Constants.TWO_DIGITS.format(percent)+"%";
								percent = ((float)Float.parseFloat((String)content.get("http_damage_modifier")))*100;
								rowData[HTTP_ABSORPTION] =Constants.TWO_DIGITS.format(percent)+"%";
								percent = ((float)Float.parseFloat((String)content.get("ftp_damage_modifier")))*100;
								rowData[FTP_ABSORPTION] = Constants.TWO_DIGITS.format(percent)+"%";
								
								rowData[SPECIAL1] = "";
								rowData[SPECIAL2] = "";
								HashMap specials1 = (HashMap)content.get("specialAttribute1");
								String name= (String)specials1.get("name");
								String sa1 = (String)specials1.get("short_desc");
								String v1 = (String)specials1.get("value");
								if(!sa1.equals("")){
									int stype = (Integer)AttributeCellObject.firewallAttributes.get(name);
									float value = Float.parseFloat(v1);
									percent = (value)*100;
									rowData[SPECIAL1] = new AttributeCellObject(percent,stype,Constants.TWO_DIGITS.format(percent)+sa1);
								}
								else{
									rowData[SPECIAL1] = new AttributeCellObject(0.0f,-1,"");
								}
								
								HashMap specials2 = (HashMap)content.get("specialAttribute2");
								name= (String)specials2.get("name");
								String sa2 = (String)specials2.get("short_desc");
								String v2 = (String)specials2.get("value");
								
								if(!sa2.equals("")){
									int stype = (Integer)AttributeCellObject.firewallAttributes.get(name);
									float value = Float.parseFloat(v2);
									percent = (value)*100;
									rowData[SPECIAL2] = new AttributeCellObject(percent,stype,Constants.TWO_DIGITS.format(percent)+sa2);
								}
								else{
									rowData[SPECIAL2] = new AttributeCellObject(0.0f,-1,"");;
								}
								
								//rowData[DESCRIPTION] = (String)dir[6];
								tableModel.addRow(rowData);
								j++;
								
							}
						}
						
					}
					table.revalidate();
					table.repaint();
					//populateButtons();
				}
			}catch(NullPointerException e){
				e.printStackTrace();
			}
			this.directory=directory;
		}
	}
			
	public void changeDirectory(String name){
		//System.out.println("Change Directory to "+name);
		if(!name.equals("..")){
			//sourceDir.setText(sourceDir.getText()+name+"/");
			//requests[requestedFolders]=name;
			//requestedFolders++;
			//String folders[] = folder.split("/");
			String newFolder="";
			String[] newList = new String[buttonList.length+1];
			for(int i=0;i<buttonList.length;i++){
				 newFolder+=buttonList[i]+"/";
				 newList[i]=buttonList[i];
			}
			newList[newList.length-1]=name;
			//System.out.println(newList.length);
			buttonList=newList;
			MyHacker.setCurrentFolder(newFolder+name);
			folder = newFolder+name;
			//System.out.println("Changing To "+folder);
			//System.out.println("Requesting "+newFolder+name+"/");
			Object objects[] = new Object[]{MyHacker.getEncryptedIP(),newFolder+name+"/"};
			View MyView=MyHacker.getView();
			MyHacker.setRequestedDirectory(Hacker.HOME);
			MyView.setFunction("requestdirectory");
			MyView.addFunctionCall(new RemoteFunctionCall(Hacker.HOME,"requestdirectory",objects));
		}
		else{
			if(!folder.equals("")){
				
				String folders[] = folder.split("/");
				String newFolder="";
				String[] newList = new String[buttonList.length-1];
				for(int i=0;i<folders.length-1;i++){
					 newFolder+=buttonList[i]+"/";
					 newList[i]=buttonList[i];
				}
				buttonList=newList;
				folder=newFolder;
				Object objects[] = new Object[]{MyHacker.getEncryptedIP(),newFolder};
				MyHacker.setCurrentFolder(newFolder);
				MyHacker.setRequestedDirectory(Hacker.HOME);
				View MyView = MyHacker.getView();
				MyView.setFunction("requestdirectory");
				MyView.addFunctionCall(new RemoteFunctionCall(Hacker.HOME,"requestdirectory",objects));
			}
		}
	}
	
	public void callDirectory(String location){
		String folders[] = location.split("/");
		String newFolder="";
		String[] newList;
		if(!location.equals("")){
			newList = new String[folders.length];
			for(int i=0;i<folders.length;i++){
				if(!folders[i].equals("")){
						newFolder+=folders[i]+"/";
						newList[i]=folders[i];
				}
			}
		}
		else{
			newList = new String[0];
		}
		buttonList=newList;
		folder=newFolder;
		Object objects[] = new Object[]{MyHacker.getEncryptedIP(),location};
		MyHacker.setCurrentFolder(location);
		MyHacker.setRequestedDirectory(Hacker.HOME);
		View MyView = MyHacker.getView();
		MyView.setFunction("requestdirectory");
		MyView.addFunctionCall(new RemoteFunctionCall(Hacker.HOME,"requestdirectory",objects));
	}
	
	public void populateButtons(){
		buttonPanel.removeAll();
		buttonPanel.repaint();
		JButton button = new JButton("Home");
		buttonPanel.add(button);
		if(buttonList.length==0)
			button.setEnabled(false);
		//button.addActionListener(new HomeActionListener("",this));
		button.setBounds(2,2,button.getPreferredSize().width,button.getPreferredSize().height);
		String folder="";
		int x=button.getPreferredSize().width+5;
		for(int i=0;i<buttonList.length;i++){
			
			folder+=buttonList[i]+"/";
			if(!folder.equals("/")){
				button = new JButton(buttonList[i]);
				buttonPanel.add(button);
				button.setBounds(x,2,button.getPreferredSize().width,button.getPreferredSize().height);
				if(i==buttonList.length-1)
					button.setEnabled(false);
				x+=button.getPreferredSize().width+5;
			}
		}
		buttonPanel.doLayout();
	}
	
	public void changePrice(String fileName,float price){
		View MyView = MyHacker.getView();
		Object objects[] = {MyHacker.getEncryptedIP(),folder,fileName,new Float(price)};
		MyView.setFunction("setfileprice");
		MyView.addFunctionCall(new RemoteFunctionCall(0,"setfileprice",objects));
	}
	
	public void changeDescription(String fileName,String description){
		View MyView = MyHacker.getView();
		Object objects[] = {MyHacker.getEncryptedIP(),folder,fileName,description};
		MyView.setFunction("setfiledescription");
		MyView.addFunctionCall(new RemoteFunctionCall(0,"setfiledescription",objects));
	}
	
	public String getFolder(){
		return folder;
	}
	
	public void sellFile(int index,int quantity){
    
        int[] selRows = table.getSelectedRows();
        Object[] allFiles = new Object[1];
        if (selRows.length == 0) {
                return;
        }
        String fileName = (String)tableModel.getValueAt(index,NAME);
		String maker = (String)tableModel.getValueAt(index,MAKER);
		float storeBuyPrice = (Float)tableModel.getValueAt(index,SELL_TO_STORE_PRICE);
        if (storeBuyPrice!=0.0f) {
            float price = storeBuyPrice;
			float compilingcost=0.0f;
            allFiles[0] = new Object[]{folder,fileName,maker,quantity};
            // check to see that there's a bank port on
            if ( (MyHacker.getBankPorts()).size() > 0) {
				Object objects[] = new Object[]{MyHacker.getEncryptedIP(),allFiles};
				View MyView=MyHacker.getView();
				MyHacker.setRequestedDirectory(Hacker.HOME);
				MyView.setFunction("sellfilemulti");
                MyView.addFunctionCall(new RemoteFunctionCall(Hacker.FIREWALL_BROWSER,"sellfilemulti",objects));
        		int maxQuantity = (Integer)tableModel.getValueAt(index,QUANTITY);
        		if(quantity == maxQuantity){
        			removeRow(index);
        		} else {
        			tableModel.setValueAt((maxQuantity-quantity),index,QUANTITY);
        		}
			} else {
                // send a message saying that shit is busticated.
                String message = "You can't sell files without a non-dummy bank port turned on.";
                MyHacker.showMessage(message);
            }
		}
	}
	
    public void removeRow(int row){
		tableModel.removeRow(row);
		Object[] temp = new Object[directory.length-1];
		int in=0;
		for(int i=0;i<temp.length;i++){
			if(i!=row){
				temp[in] = directory[i];
				in++;
			}
		}
		directory = temp;
	}
    
    public void removeRows(int[] rows) {
        int numRows = rows.length;
        // create a convenience hashmap of the rows
        HashMap tempHash = new HashMap();
        for (int i = 0; i < numRows; i++) {
            tempHash.put(rows[i], i);
        }
        tableModel.removeRows(rows);
		Object[] temp = new Object[directory.length - numRows];
		int in=0;
        
		for(int i=0; i < temp.length; i++){
            if (tempHash.get(i) == null) {
				temp[in] = directory[i];
				in++;
			}
		}
		directory = temp;
    }
	
    
	public void internalFrameClosed(InternalFrameEvent e) {
		MyHacker.setHomeOpen(false);
	}
    
    private boolean isFolder(String type) {
        if (type.equals("Directory")) {
            return true;
        }
        return false;
    }
    
    private void deleteFile() {
        int[] selRows = table.getSelectedRows();
        if (selRows.length == 0) {
                return;
        }
        
        int n = -1;
        if (selRows.length == 1) {
            int selRow = selRows[0];
            String selectedFile = (String)tableModel.getValueAt(selRow,NAME);
            n = showDeleteDialog("'" + selectedFile + "'");
        } else {
            n = showDeleteDialog("these " + selRows.length + " items");
        }
        
        if (n == 0) {
            Object[] allFiles = new Object[selRows.length];
            for (int i = 0; i < selRows.length; i++) {
                String[] details = new String[3];
                int selRow = selRows[i];
                String selectedFile = (String)tableModel.getValueAt(selRow,NAME);
                details[0] = folder;
                details[1] = selectedFile;
				details[2] = "file";
                allFiles[i] = details;
            }
            Object objects[] = {MyHacker.getEncryptedIP(), allFiles};
            View MyView = MyHacker.getView();
            MyView.setFunction("deletemulti");
            MyView.addFunctionCall(new RemoteFunctionCall(Hacker.FIREWALL_BROWSER,"deletemulti",objects));
            MyHacker.setCurrentFolder(folder);
            MyHacker.setRequestedDirectory(Hacker.FIREWALL_BROWSER);
            removeRows(selRows);
        }
    }
    
	public void actionPerformed(ActionEvent e){
		if(e.getActionCommand().equals("Exit")){
			this.doDefaultCloseAction();
			//mainPanel.repaint();
		}
		if(e.getActionCommand().equals("Delete")){
            deleteFile();
		}
		if(e.getActionCommand().equals("Up")){
			changeDirectory("..");
		}
		if(e.getActionCommand().equals("Refresh")){
			Object objects[] = new Object[]{MyHacker.getEncryptedIP(),folder};
			View MyView=MyHacker.getView();
			MyHacker.setRequestedDirectory(Hacker.FIREWALL_BROWSER);
			MyView.setFunction("requestdirectory");
			MyView.addFunctionCall(new RemoteFunctionCall(Hacker.FIREWALL_BROWSER,"requestdirectory",objects));
		}
		if(e.getActionCommand().equals("Sell")){
            int[] selRows = table.getSelectedRows();
            if (selRows.length == 0) {
                    return;
            }
            int n = -1;
            if (selRows.length == 1) {
                int selRow = selRows[0];
                //String type = (String)tableModel.getValueAt(selRow,TYPE);
				String maker = (String)tableModel.getValueAt(selRow,MAKER);
				int quantity = (Integer)tableModel.getValueAt(selRow,QUANTITY);
				String fileName = (String)tableModel.getValueAt(selRow,NAME);
				float storeBuyPrice = (Float)tableModel.getValueAt(selRow,SELL_TO_STORE_PRICE);
				float price=0.0f;
                // make sure they can sell the file; can only sell files made by entries in the 'makers' hashmap
				if(storeBuyPrice!=0.0f) {
					FirewallQuantityDialog FQD = new FirewallQuantityDialog(this,MyHacker,quantity,fileName,storeBuyPrice,selRow);
				} else {
                    MyHacker.showMessage("You can only sell Firewalls, Cards, and non-free Scripts.");
                }
            } else {
                n = showSellDialog("" + selRows.length);
                if (n == 0) {
                    Object[] allFiles = new Object[selRows.length];
                    boolean okay = true;
                    for (int i = 0; i < selRows.length; i++) {
                        Object[] details = new Object[4];
                        //build up the object[] to send to the server
                        int selRow = selRows[i];
                        //String type = (String)tableModel.getValueAt(selRow,TYPE);
                        String maker = (String)tableModel.getValueAt(selRow,MAKER);
                        Integer quantity = (Integer)tableModel.getValueAt(selRow,QUANTITY);
                        String selectedFile = (String)tableModel.getValueAt(selRow,NAME);
						float storeBuyPrice = (Float)tableModel.getValueAt(selRow,SELL_TO_STORE_PRICE);
                        if ( storeBuyPrice == 0.0f ) {
                            MyHacker.showMessage("You can not sell the following file: " + selectedFile);
                            okay = false;
                            break;
                        }
                        details[0] = folder;
                        details[1] = selectedFile;
                        details[2] = maker;
                        details[3] = quantity;
                        allFiles[i] = details;
                    }
                    if (okay) {
                        if ((MyHacker.getBankPorts()).size() > 0) {
                            Object objects[] = {MyHacker.getEncryptedIP(), allFiles};
                            View MyView = MyHacker.getView();
                            MyView.setFunction("sellmulti");
                            MyView.addFunctionCall(new RemoteFunctionCall(Hacker.FIREWALL_BROWSER,"sellfilemulti",objects));
                            MyHacker.setCurrentFolder(folder);
                            MyHacker.setRequestedDirectory(Hacker.FIREWALL_BROWSER);
                            removeRows(selRows);
                        } else {
                            String message = "You can't sell files without a non-dummy bank port turned on.";
                            MyHacker.showMessage(message);
                        }
                    }
                }
            }
		}
	}
	
	public void mouseClicked(MouseEvent e){
		
		if(e.isPopupTrigger()){
			showPopUpMenu(e);
		}
	}
	
	public void mousePressed(MouseEvent e){
		if(e.isPopupTrigger()){
			showPopUpMenu(e);
		}
	}
	
	public void mouseReleased(MouseEvent e){
		if(e.isPopupTrigger()){
			showPopUpMenu(e);
		}
	}
	
	public void showPopUpMenu(MouseEvent e){
		Point p = e.getPoint();

		// get the row index that contains that coordinate
		int rowNumber = table.rowAtPoint( p );

		// Get the ListSelectionModel of the JTable
		ListSelectionModel model = table.getSelectionModel();

		// set the selected interval of rows. Using the "rowNumber"
		// variable for the beginning and end selects only that one row.
		model.setSelectionInterval( rowNumber, rowNumber );
		JPopupMenu menu = new JPopupMenu();
		JMenuItem menuItem;
		
		menuItem = new JMenuItem("Properties");
		menuItem.addActionListener(this);
		menu.add(menuItem);
		
		menu.show(table,e.getX(),e.getY());
	}
	
	public void componentHidden(ComponentEvent e) {
    }

    public void componentMoved(ComponentEvent e) {
        
    }

    public void componentResized(ComponentEvent e){
		//table.sizeColumnsToFit(false);
		if(table!=null){
			table.doLayout();
		}
    }

    public void componentShown(ComponentEvent e) {

    }
	
	class HeaderListener extends MouseAdapter {
    
        JTableHeader   header;
        SortButtonRenderer renderer;
		ToolTip toolTip;
		Hacker hacker;
        HeaderListener(JTableHeader header,SortButtonRenderer renderer,Hacker hacker) {
          this.header   = header;
          this.renderer = renderer;
		  this.hacker = hacker;
		  toolTip = new ToolTip("",hacker);
        }
		
		public void mouseEntered(MouseEvent e){
			int col = header.columnAtPoint(e.getPoint());
			String text = FirewallBrowser.columns[col];
			toolTip.setText(text);
			toolTip.show(e.getX()+header.getLocationOnScreen().x-hacker.getPanel().getLocationOnScreen().x,e.getY()+header.getLocationOnScreen().y-hacker.getPanel().getLocationOnScreen().y);
		}
		
		public void mouseExited(MouseEvent e){
			toolTip.setVisible(false);
		}
      
        public void mousePressed(MouseEvent e) {
            if ( (header.getCursor().getType() != Cursor.E_RESIZE_CURSOR) && (header.getCursor().getType() != Cursor.W_RESIZE_CURSOR) ) {
              int col = header.columnAtPoint(e.getPoint());
              int sortCol = header.getTable().convertColumnIndexToModel(col);
			  if(sortCol!=-1){
				  TableColumn column = table.getColumnModel().getColumn(sortCol);
				  //int width = 
				  renderer.setPressedColumn(col);
				  renderer.setSelectedColumn(col);
				  header.repaint();
				  
				  if (header.getTable().isEditing()) {
					header.getTable().getCellEditor().stopCellEditing();
				  }
				  
				  boolean isAscent;
				  if (SortButtonRenderer.DOWN == renderer.getState(col)) {
					isAscent = true;
				  } else {
					isAscent = false;
				  }
				  ((SortableTableModel)header.getTable().getModel())
					.sortByColumn(sortCol, isAscent);    
				}
			}
        }
      
        public void mouseReleased(MouseEvent e) {
            if ( (header.getCursor().getType() != Cursor.E_RESIZE_CURSOR) && (header.getCursor().getType() != Cursor.W_RESIZE_CURSOR) ) {
              int col = header.columnAtPoint(e.getPoint());
              renderer.setPressedColumn(-1);                // clear
              header.repaint();
            }
        }
  }
  
  /* Implements KeyListener */
  public void keyPressed(KeyEvent e) {
  }
  
  public void keyReleased(KeyEvent e) {
  }
  
  
  public void keyTyped(KeyEvent e) {
    // if the delete key is pressed, delete the file.
    if (e.getKeyChar() == KeyEvent.VK_DELETE) {
        deleteFile();
    }
    
  }
  
	
}
