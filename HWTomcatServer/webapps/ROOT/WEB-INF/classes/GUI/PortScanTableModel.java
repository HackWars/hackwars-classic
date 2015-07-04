package GUI;
/**

PortScanTableModel.java

*/

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import Assignments.*;
import View.*;

public class PortScanTableModel extends AbstractTableModel{
	private Object[][] rowData;
	private String[] columnNames;
	
	public PortScanTableModel(Object[][] rowData,String[] columnNames){
		this.rowData=rowData;
		this.columnNames=columnNames;
	}
	
	public boolean isCellEditable(int row,int column){
		if(column==4)
			return(true);
		return(false);
	}
	
	public String getColumnName(int col) {
		return columnNames[col];
	}
	public int getRowCount() { return rowData.length; }
	public int getColumnCount() { return columnNames.length; }
	public Object getValueAt(int row, int col) {
		return rowData[row][col];
	}
	
	public void setValueAt(Object value, int row, int col) {
		rowData[row][col] = value;
		fireTableCellUpdated(row, col);
	}
	public void addRow(Object[] data){
		//System.out.println(rowData.length+"   "+columnNames.length);
		Object[][] temp = new Object[rowData.length+1][columnNames.length];
		//System.out.println(temp.length);
		for(int i=0;i<rowData.length;i++){
			for(int j=0;j<columnNames.length;j++){
				temp[i][j]=rowData[i][j];
			}
		}
		for(int i=0;i<columnNames.length;i++){
			temp[rowData.length][i]=data[i];
		}
		rowData = temp;
		fireTableDataChanged();
		
	}
	
	public void resetData(){
		rowData = new Object[0][0];
		fireTableDataChanged();
	}

}
