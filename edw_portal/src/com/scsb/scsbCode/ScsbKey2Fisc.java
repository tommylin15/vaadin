package com.scsb.scsbCode;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteOrder;
import java.security.MessageDigest;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Hashtable;
import java.util.Vector;

import com.scsb.scsbEncrypt.EncrypRSA;
import com.scsb.scsbSign.GenSignVerify;

/**
 * 上海銀行公鑰傳遞至財金,加密及簽章作業
 * @author 3471
 */
public class ScsbKey2Fisc {
	//上海銀行的RSA私鑰
	private RSAPrivateKey  scsbPrivateKey;
	//上海銀行的RSA公鑰
	private RSAPublicKey  scsbPublicKey;
	//最後要產出公鑰的HashData
	private Hashtable<String ,byte[]> hashData =new Hashtable<String ,byte[]>();
	
	public ScsbKey2Fisc(){
	}

	private static void writeSafeFile(String safeFilePath ,Hashtable<String ,byte[]> hashData){
		byte[] bData=new byte[0];
		Vector<byte[]> vData=new Vector<byte[]>();
		vData.add(hashData.get("Header"));
		vData.add(hashData.get("SenderID"));
		vData.add(hashData.get("KeyVersion"));
		vData.add(hashData.get("SignAlg"));
		vData.add(hashData.get("SignatureLen"));
		vData.add(hashData.get("Signature"));
		
		vData.add(hashData.get("NewPubKeyInfoLen"));
		vData.add(hashData.get("NewKeyVersion"));
		vData.add(hashData.get("ModulusLen"));
		vData.add(hashData.get("Modulus"));
		vData.add(hashData.get("ExponentLen"));
		vData.add(hashData.get("Exponent"));
		
		bData=ByteUtil.streamCopy(vData);
		try {
			FileOutputStream fos = new FileOutputStream(safeFilePath);
			fos.write(bData);
			fos.close();		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){
		 if (args.length != 3) {
	            System.out.println("輸入參數為[scsbPrivateKey  ,scsbPublicKey ,SafeFile]");
		 }else{
	       	try{
	       		//step.1 載入文檔,scsb private key ,fisc public key ,session key(DES3)
	       		String scsbPrivateKeyPath=args[0];
	       		String scsbPublicKeyPath=args[1];
	       		String saveFilePath=args[2];
	       		//String scsbPrivateKeyPath="D:\\encrypt\\fisc\\scsbKey.pri";
	       		//String scsbPublicKeyPath="D:\\encrypt\\fisc\\scsbKey.pub";
	       		//String saveFilePath="D:\\encrypt\\fisc\\01100000.KEY";
	       		
	       		ScsbKey2Fisc scsbKey2fisc =new ScsbKey2Fisc();
	       		//scsb2fisc.LoadData(srcFilePath ,scsbPrivateKeyPath ,fiscPublicKeyPath);
	       		//step.2建立Hash Data
	       		String sHeader="FiScPuBk";
	       		//傳送者id
	       		String sScsb="01100000";
	       		//傳送者key var
	       		String sScsbKeyVar="001";
	       		//傳送者新key var
	       		String sScsbNewKeyVar="001";	       		
	       		//簽章加密模式
	       		byte[] bSignAlg =new byte[]{0x02};
	       		//step.3載入scsb public key ,scsb private key
				EncrypRSA rsa = new EncrypRSA();
	    		rsa.LoadPublicKey(scsbPublicKeyPath);
	    		rsa.LoadPrivateKey(scsbPrivateKeyPath);
	    		scsbKey2fisc.scsbPublicKey	=rsa.getRSAPublicKey();
	    		scsbKey2fisc.scsbPrivateKey	=rsa.getRSAPrivateKey();
	    		byte[] bPublicKey =scsbKey2fisc.scsbPublicKey.getEncoded();
		        // using known public key's modulus & exponent to re-build public key
		        BigInteger modulus =scsbKey2fisc.scsbPublicKey.getModulus();
		        byte[] bModulus =modulus.toByteArray();
		        bModulus =EncrypRSA.removeMSZero(bModulus);
		        int iModulusLen =bModulus.length;
		        byte[] bModulusLen =ByteUtil.convertToBytes(iModulusLen ,2 ,ByteOrder.BIG_ENDIAN);        
		        //System.out.println("iModulusLen:"+iModulusLen);		        
		        
		        BigInteger exponent =scsbKey2fisc.scsbPublicKey.getPublicExponent();
		        byte[] bExponent =exponent.toByteArray();
		        int iExponentLen =bExponent.length;
		        byte[] bExponentLen =ByteUtil.convertToBytes(iExponentLen ,2 ,ByteOrder.BIG_ENDIAN);
		        //System.out.println("iExponentLen:"+iExponentLen);
		        //NewKeyVersion(3)+ModulusLen(2)+Modulus+ExponentLen(2)+Exponent
	    		int iPublicKeyInfoLen =3+2+iModulusLen+2+iExponentLen;
	    		//String hPublicKeyInfoLen = Integer.toHexString(iPublicKeyInfoLen);
	    		byte[] bPublicKeyInfoLen =ByteUtil.convertToBytes(iPublicKeyInfoLen ,4 ,ByteOrder.BIG_ENDIAN);
	    		//System.out.println("iPublicKeyInfoLen:"+iPublicKeyInfoLen);
		        //step.4以Hash Data +SCSB的PRIVATE KEY產生簽章
	       		GenSignVerify genSignVerify =new GenSignVerify();
	    		Vector<byte[]> vHashData =new Vector<byte[]>();
	    		vHashData.add(sScsb.getBytes());
	    		vHashData.add(sScsbKeyVar.getBytes());
	    		vHashData.add(bSignAlg);
	    		vHashData.add(bPublicKeyInfoLen);
	    		vHashData.add(sScsbNewKeyVar.getBytes());
	    		vHashData.add(bModulusLen);
	    		vHashData.add(bModulus);
	    		vHashData.add(bExponentLen);
	    		vHashData.add(bExponent);
	    		byte[] bHashData =ByteUtil.streamCopy(vHashData);
	       		byte[] bSignature   =genSignVerify.Sign(bHashData, scsbKey2fisc.scsbPrivateKey, "SHA256withRSA");
		        int iSignatureLen =bSignature.length;
		        byte[] bSignatureLen =ByteUtil.convertToBytes(iSignatureLen ,2 ,ByteOrder.BIG_ENDIAN);
	       		
	       		//step.5產生公鑰交換檔案
	       		scsbKey2fisc.hashData.put("Header"			, sHeader.getBytes());
	       		scsbKey2fisc.hashData.put("SenderID"		, sScsb.getBytes());
	       		scsbKey2fisc.hashData.put("KeyVersion"		, sScsbKeyVar.getBytes());
	       		scsbKey2fisc.hashData.put("SignAlg"			, bSignAlg);
	       		scsbKey2fisc.hashData.put("SignatureLen"	, bSignatureLen);
	       		scsbKey2fisc.hashData.put("Signature"		, bSignature);
	       		scsbKey2fisc.hashData.put("NewPubKeyInfoLen", bPublicKeyInfoLen);
	       		scsbKey2fisc.hashData.put("NewKeyVersion"	, sScsbNewKeyVar.getBytes());
	       		scsbKey2fisc.hashData.put("ModulusLen"		, bModulusLen);
	       		scsbKey2fisc.hashData.put("Modulus"			, bModulus);
	       		scsbKey2fisc.hashData.put("ExponentLen"		, bExponentLen);
	       		scsbKey2fisc.hashData.put("Exponent"		, bExponent);
	       		writeSafeFile(saveFilePath ,scsbKey2fisc.hashData);
	       		
	       		//step.6列出scsb的fingerprint
	     		Vector<byte[]> vFingerPrint =new Vector<byte[]>();
	     		vFingerPrint.add(bPublicKeyInfoLen);
	     		vFingerPrint.add(sScsbNewKeyVar.getBytes());
	     		vFingerPrint.add(bModulusLen);
	     		vFingerPrint.add(bModulus);
	     		vFingerPrint.add(bExponentLen);
	     		vFingerPrint.add(bExponent);
	     		byte[] bFingerPrint =ByteUtil.streamCopy(vFingerPrint);
	     		MessageDigest md = MessageDigest.getInstance("SHA-256");
	     		md.update(bFingerPrint);
		     	String fingerPrint=ByteUtil.BinaryToHexString(md.digest());
		     	System.out.println("fingerPrint:"+fingerPrint);
	       		
	       	} catch (Exception e) {
	       		e.printStackTrace();
	        }
	     }
		 System.out.println( "scsb2fisc Successfully.");
	}	
}