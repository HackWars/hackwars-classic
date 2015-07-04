package com.plink.dolphinnet;
import javax.swing.*;
import java.util.*;
import java.util.zip.*;
import java.net.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;

/**
Generate a hash singleton.
*/
public class HashSingleton{
	private static String hash="";
	private static final HashSingleton MyHashSingleton=new HashSingleton();
	
	private HashSingleton(){
		hash=getRandomKey();
	}
	
	public static HashSingleton getInstance(){
		return(MyHashSingleton);
	}
	
	public static String getHash(){
		return(hash);
	}

	//Generate a random key.
	public String getRandomKey(){
		String key="";
		for(int i=0;i<10;i++){
			int ii=(int)(Math.random()*26);
			char c=(char)('a'+ii);
			key+=c;
		}
		return(key);
	}
	
}