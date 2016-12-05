package com.scsb.scsbCode;

import com.scsb.scsbEncrypt.EncrypRSA;

public class CreateRSAKey {
	public static void main(String[] args) throws Exception {
		 if (args.length != 3) {
	            System.out.println("Usage: publicKey ,privateKey ,password");
		 }else{
	       	try{
	       		String publicKeyPath=args[0];
	       		String privateKeyPath=args[1];
	       		String passWord=args[2];
	       		
	       		
	       		EncrypRSA rsa = new EncrypRSA();
	       		rsa.generateKey(2048 ,passWord);
	       		rsa.SaveKeyPair(publicKeyPath,privateKeyPath);
	       	} catch (Exception e) {
	       		e.printStackTrace();
	        }
	     }
		 System.out.println( "KeyFile Create Successfully.");
	}	
}