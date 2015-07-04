package com.plink.dolphinnet;
/** Used to distribute data-sets on the Reporter end of the system.*/
public interface DataHandler{
	public void addData(Object o);
	public void resetData();
	public Object getData(int id);
	public void addFinishedAssignment(Assignment A);
}