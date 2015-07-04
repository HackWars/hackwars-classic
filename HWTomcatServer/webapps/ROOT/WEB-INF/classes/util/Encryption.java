package util;
/**
Encryption.java

An encryption library for more secure packet transfers. 
*/

import java.security.*;
import javax.crypto.*;
import java.io.*;
import java.security.spec.*;
import com.sun.crypto.provider.SunJCE;
import javax.crypto.spec.*;
import java.util.*;
import javax.crypto.interfaces.*;
public class Encryption{
	private static Encryption MyEncryption=new Encryption();
	
	private KeyPair MyKeyPair=null;
	private byte encodedPublicKey[]=null;
	private HashMap MyKeyStore=new HashMap();
	private HashMap IPs=new HashMap();
	private KeyAgreement MyKeyAgreement=null;
	private SecretKey DESKey=null;
	
	public static Encryption getInstance(){
			return(MyEncryption);
	}
	
	public static void breakSingleton(){
		MyEncryption=new Encryption();
	}
	
	public Encryption(){
	}
	
	
	/**
	Return bytes representing an encoded public key.
	*/
	public byte[] getEncodedKey(){
		return(encodedPublicKey);
	}
	
	/**
	Finalize a key agreement.
	*/
	public void finalize(byte[] encodedExternalPublicKey){
		try{
			KeyFactory MyKeyFactory = KeyFactory.getInstance("DH");
			X509EncodedKeySpec x509KeySpec =new X509EncodedKeySpec(encodedExternalPublicKey);
			PublicKey ExternalPublicKey = MyKeyFactory.generatePublic(x509KeySpec);
			MyKeyAgreement.doPhase(ExternalPublicKey,true);
			DESKey=MyKeyAgreement.generateSecret("DES");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	Initialize with the encoded key and an IP for adding into the encryption store.
	*/
	public byte[] init(byte[] encodedExternalPublicKey,String hash,String ip){
		byte[] returnMe=null;
		try{
			KeyFactory MyKeyFactory = KeyFactory.getInstance("DH");
			X509EncodedKeySpec x509KeySpec =new X509EncodedKeySpec(encodedExternalPublicKey);
			PublicKey ExternalPublicKey = MyKeyFactory.generatePublic(x509KeySpec);
			DHParameterSpec DHPS=((DHPublicKey)ExternalPublicKey).getParams();

			//Generate the first key.
			KeyPairGenerator MyKeyPairGenerator= KeyPairGenerator.getInstance("DH");
			MyKeyPairGenerator.initialize(DHPS);
			MyKeyPair = MyKeyPairGenerator.generateKeyPair();
			KeyAgreement MyKeyAgreement = KeyAgreement.getInstance("DH");
			MyKeyAgreement.init(MyKeyPair.getPrivate());
			MyKeyAgreement.doPhase(ExternalPublicKey,true);
			
			SecretKey DESKey=MyKeyAgreement.generateSecret("DES");
			MyKeyStore.put(hash,DESKey);
			IPs.put(ip,hash);
			returnMe=(new X509EncodedKeySpec(MyKeyPair.getPublic().getEncoded())).getEncoded();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return(returnMe);
	}
	
	/**
	Initialize()
	Generates the first set of keys, and
	*/
	public void init(){
		try{
			//Initialize the algorithm.
			DHParameterSpec DHPS;
			AlgorithmParameterGenerator paramGen = AlgorithmParameterGenerator.getInstance("DH");
			paramGen.init(512);
			AlgorithmParameters params = paramGen.generateParameters();
			DHPS = (DHParameterSpec)params.getParameterSpec(DHParameterSpec.class);
			
			//Generate the first key.
			KeyPairGenerator MyKeyPairGenerator= KeyPairGenerator.getInstance("DH");
			MyKeyPairGenerator.initialize(DHPS);
			MyKeyPair = MyKeyPairGenerator.generateKeyPair();
			MyKeyAgreement = KeyAgreement.getInstance("DH");
			MyKeyAgreement.init(MyKeyPair.getPrivate());
			
			this.encodedPublicKey=(new X509EncodedKeySpec(MyKeyPair.getPublic().getEncoded())).getEncoded();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/*
	Encrypt the data provided.
	*/
	public byte[] encrypt(byte[] data){
		try{
			Cipher MyCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
			MyCipher.init(Cipher.ENCRYPT_MODE,DESKey);
			return(MyCipher.doFinal(data));
		}catch(Exception e){
			e.printStackTrace();
		}
		return(null);
	}
	
	/**
	Decrypt the bytes provided.
	*/
	public byte[] decrypt(byte[] data,String ip){
		try{
			Cipher MyCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
			SecretKey DESKey=(SecretKey)MyKeyStore.get(ip);
			MyCipher.init(Cipher.DECRYPT_MODE,DESKey);
			return(MyCipher.doFinal(data));
		}catch(Exception e){
	//		e.printStackTrace();
		}
		return(null);
	}
	
	/**
	Remove an ip from the encryption handler.
	*/
	public void remove(String ip){
		MyKeyStore.remove((String)IPs.get(ip));
		IPs.remove(ip);
	}
		
	/*public static void main(String args[]){
	
	}*/
}