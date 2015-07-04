package GUI;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import java.util.Vector;
/**
 * @version 1.0 02/25/99
 */
public class SortableTableModel extends DefaultTableModel {
  public static final int HOME = 0;
  public static final int EQUIPMENT = 1;
   public static final int FIREWALL = 2;
  int[] indexes;
  TableSorter sorter;
  int sortColumn=-1;
  boolean ascent;
  private int type;
  public SortableTableModel(int type) {   
	this.type = type;
  }
    
  public Object getValueAt(int row, int col) {
    int rowIndex = row;
    if (indexes != null) {
	  if(row<indexes.length){
		rowIndex = indexes[row];
	  }
    }
	Object value;
	if(getColumnClass(col) == String.class){
		value = "";
	}
	else{
		value = new Integer(0);
	}
	try{
		value = super.getValueAt(rowIndex,col);
	}catch(ArrayIndexOutOfBoundsException e){System.out.println("getting value at ("+row+","+col+")");}
    return value;
  }
    
  public void setValueAt(Object value, int row, int col) {    
    int rowIndex = row;
    if (indexes != null) {
      rowIndex = indexes[row];
    }
    super.setValueAt(value, rowIndex, col);
  }
  
 
  public void sortByColumn(int column, boolean isAscent) {
    if (sorter == null) {
      sorter = new TableSorter(this);
    }   
    sorter.sort(column, isAscent);   
    fireTableDataChanged();
	sortColumn=column;
	ascent=isAscent;
  }
  
  public int[] getIndexes() {
    int n = getRowCount();
    if (indexes != null) {
      if (indexes.length == n) {
        return indexes;
      }
    }
    indexes = new int[n];
    for (int i=0; i<n; i++) {
      indexes[i] = i;
    }
    return indexes;
  }
  
  public Class getColumnClass(int col){
	if(type==HOME){
		switch(col){
			case Home.NAME:	return String.class; 
			case Home.TYPE: return String.class;
			case Home.QUANTITY: return Integer.class;
			case Home.MAKER: return String.class;
			case Home.SELL_TO_STORE_PRICE: return Float.class;
			case Home.YOUR_STORE_PRICE: return Float.class;
			case Home.CPU_COST: return Float.class;
			case Home.DESCRIPTION: return String.class;
		}
	}
	else if(type==EQUIPMENT){
		switch(col){
			case Equipment.FILE_NAME:	return String.class; 
			case Equipment.ATTRIBUTE1: return AttributeCellObject.class;
			case Equipment.ATTRIBUTE2: return AttributeCellObject.class;
			case Equipment.DUCT_TAPE: return Integer.class;
			case Equipment.GERMANIUM: return Integer.class;
			case Equipment.SILICON: return Integer.class;
			case Equipment.YBCO: return Integer.class;
			case Equipment.PLUTONIUM: return Integer.class;
			case Equipment.CURRENT_DURABILITY: return Integer.class;
			case Equipment.NAME: return String.class;
			case Equipment.MAX_DURABILITY: return Integer.class;
			case Equipment.STORE_PRICE: return Float.class;
			case Equipment.YOUR_STORE_PRICE: return Float.class;
			
		}
	
	}
	else if(type==FIREWALL){
		switch(col){
			case FirewallBrowser.NAME: return String.class;
			case FirewallBrowser.MAKER: return String.class;
			case FirewallBrowser.SPECIAL1: return AttributeCellObject.class;
			case FirewallBrowser.SPECIAL2: return AttributeCellObject.class;
			case FirewallBrowser.SELL_TO_STORE_PRICE: return Float.class;
			case FirewallBrowser.YOUR_STORE_PRICE: return Float.class;
			case FirewallBrowser.BANK_ABSORPTION: return String.class;
			case FirewallBrowser.ATTACK_ABSORPTION: return String.class;
			case FirewallBrowser.REDIRECT_ABSORPTION: return String.class;
			case FirewallBrowser.HTTP_ABSORPTION: return String.class;
			case FirewallBrowser.FTP_ABSORPTION: return String.class;
			case FirewallBrowser.CPU_COST: return Float.class;
			case FirewallBrowser.QUANTITY: return Integer.class;
			case FirewallBrowser.EQUIP_LEVEL: return Integer.class;
			case FirewallBrowser.ATTACK_DAMAGE: return Integer.class;
		}
	
	}
	return null;
  
  }
  
  public boolean isCellEditable(int row, int col){
	if(type==HOME){
		if( col == Home.YOUR_STORE_PRICE || col == Home.DESCRIPTION ) {
			return true;
		}
	}
	else if(type==EQUIPMENT){
		if( col == Equipment.YOUR_STORE_PRICE ) {
			return true;
		}
	}
	return false;
  }
  
  public void resetData(){
	Object columns[] = new Object[]{};
	if(type==HOME){
		columns = Home.columns;
	}
	else if(type==EQUIPMENT){
		columns = Equipment.columns;
	}
	else if(type==FIREWALL){
		columns = FirewallBrowser.columns;
	}
	setDataVector(new Object[][]{},columns);
  }
  
  public void removeRow(int row){
	getIndexes();
	int rowIndex = row;
    if (indexes != null) {
      rowIndex = indexes[row];
    }
	super.removeRow(rowIndex);
	if(sortColumn!=-1){
		sortByColumn(sortColumn,ascent);
	}else{
		fireTableDataChanged();
	}
  }
  
    public void removeRows(int[] rows) {
        getIndexes();
        Object columns[] = new Object[]{};
        if(type==HOME){
            columns = Home.columns;
        }
        else if(type==EQUIPMENT){
            columns = Equipment.columns;
        }
		else if(type==FIREWALL){
			columns = FirewallBrowser.columns;
		}
        Object[] data = getDataVector().toArray();
        Object[][] newData = new Object[getRowCount()-rows.length][getColumnCount()];
        int index = 0;
        for (int j = 0; j < data.length; j++) {
            boolean add = true;
            for (int i = rows.length; i > 0; i--) {
                int rowIndex = i;
                if (indexes != null) {
                  rowIndex = indexes[rows[i-1]];
                }
                if(rowIndex == j) {
                    add = false;
                    break;
                }
                //super.removeRow(rowIndex);
            }
            if(add){
                newData[index] = ((Vector)data[j]).toArray();
                index++;
            }
        }
        setDataVector(newData,columns);
        if(sortColumn!=-1){
            sortByColumn(sortColumn,ascent);
        }else{
            fireTableDataChanged();
        }    
    }
  
}




