package com.scsb.scsbCode;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteOrder;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Hashtable;
import java.util.Vector;

import com.scsb.scsbEncrypt.EncrypRSA;
import com.scsb.scsbSign.GenSignVerify;

/**
 * 上海銀行重組財金公鑰與驗章作業
 * @author 3471
 */
public class FiscKey2Scsb {
	//財金提供的RSA公鑰
	private RSAPublicKey  fiscPublicKey;
	//財金的HashData資料
	private Hashtable<String ,byte[]> hashData =new Hashtable<String ,byte[]>();
	//財金的Public Key FingerPrint =Hash (NewPubKeyLen | NewKeyVersion | ModulusLen | Modulus | ExponentLen | Exponent )
	private String fingerPrint=""; 
	public FiscKey2Scsb(){
	}
	
	public RSAPublicKey getPublicKey(){
		return fiscPublicKey;
	}
	/**
	 * 載入FISC'Public Key 的Hash Data
	 * @param srcFilePath
	 */
	public boolean LoadKeyHashData(String hashDataFilePath ){
		boolean IsSign =false;
       	try{
       		//step.1.載入Hash Data
    		hashData=readHashDataFile(hashDataFilePath);  
    		
    		//step.2.重組財金提供的RSA公鑰
	         KeyFactory keyFactory = KeyFactory.getInstance("RSA");
	         // using known public key's modulus & exponent to re-build public key
	         BigInteger modulus= new BigInteger(1,hashData.get("Modulus"));
	         BigInteger exponent= new BigInteger(1,hashData.get("Exponent"));
	         
	         RSAPublicKeySpec rsaPublicKeySpec = new RSAPublicKeySpec(modulus, exponent);
	         try {
	 		 	fiscPublicKey = (RSAPublicKey) keyFactory.generatePublic(rsaPublicKeySpec);
	 		 } catch (InvalidKeySpecException e) {
	 		 	e.printStackTrace();
	 		 }
	 		 //step.3.組驗章要用的hash data
    		 Vector<byte[]> vHashData =new Vector<byte[]>();
    		 vHashData.add(hashData.get("SenderID"));
    		 vHashData.add(hashData.get("KeyVersion"));
    		 vHashData.add(hashData.get("SignAlg"));
    		 vHashData.add(hashData.get("NewPubKeyInfoLen"));
    		 vHashData.add(hashData.get("NewKeyVersion"));
    		 vHashData.add(hashData.get("ModulusLen"));
    		 vHashData.add(hashData.get("Modulus"));
    		 vHashData.add(hashData.get("ExponentLen"));
    		 vHashData.add(hashData.get("Exponent"));
     		 byte[] bHashData =ByteUtil.streamCopy(vHashData);
     		 //step.4.驗FingerPrint
     		Vector<byte[]> vFingerPrint =new Vector<byte[]>();
     		vFingerPrint.add(hashData.get("NewPubKeyInfoLen"));
     		vFingerPrint.add(hashData.get("NewKeyVersion"));
     		vFingerPrint.add(hashData.get("ModulusLen"));
     		vFingerPrint.add(hashData.get("Modulus"));
     		vFingerPrint.add(hashData.get("ExponentLen"));
     		vFingerPrint.add(hashData.get("Exponent"));
     		byte[] bFingerPrint =ByteUtil.streamCopy(vFingerPrint);
     		MessageDigest md = MessageDigest.getInstance("SHA-256");
     		md.update(bFingerPrint);
     		this.fingerPrint=ByteUtil.BinaryToHexString(md.digest());
	 		 //step.5.驗章
    		 byte[] binSignature=hashData.get("Signature");
    		 GenSignVerify genSignVerify =new GenSignVerify();
    		 IsSign =genSignVerify.Verify(bHashData ,binSignature , fiscPublicKey, "SHA256withRSA");
       	} catch (Exception e) {
       		e.printStackTrace();
        }		
		return IsSign;
	}
	
	private static Hashtable<String ,byte[]> readHashDataFile(String loadFilePath){
		Hashtable<String ,byte[]> hashData =new Hashtable<String ,byte[]>();
		try {
			File file = new File(loadFilePath);
			FileInputStream fis = new FileInputStream(file);
			byte[] bData = new byte[(int) file.length()];
			fis.read(bData);
			fis.close();	
			int iLen=0;
			
			hashData.put("Header"			, ByteUtil.getByteArrayData(bData,iLen,iLen+=8));
			hashData.put("SenderID"			, ByteUtil.getByteArrayData(bData,iLen,iLen+=8));
			hashData.put("KeyVersion"		, ByteUtil.getByteArrayData(bData,iLen,iLen+=3));
			hashData.put("SignAlg"			, ByteUtil.getByteArrayData(bData,iLen,iLen+=1));
			int iSignAlg = Integer.parseInt(ByteUtil.BinstrToStr(hashData.get("SignAlg")),2);
			//System.out.println("iSignAlg:"+iSignAlg);
			hashData.put("SignatureLen"		, ByteUtil.getByteArrayData(bData,iLen,iLen+=2));
			int iSignatureLen = Integer.parseInt(ByteUtil.BinstrToStr(hashData.get("SignatureLen")),2);
			//System.out.println("iSignatureLen:"+iSignatureLen);			
			hashData.put("Signature"		, ByteUtil.getByteArrayData(bData,iLen,iLen+=iSignatureLen));
			hashData.put("NewPubKeyInfoLen"	, ByteUtil.getByteArrayData(bData,iLen,iLen+=4));
			int iNewPubKeyInfoLen = Integer.parseInt(ByteUtil.BinstrToStr(hashData.get("NewPubKeyInfoLen")),2);
			//System.out.println("iNewPubKeyInfoLen:"+iNewPubKeyInfoLen);			
			hashData.put("NewKeyVersion"	, ByteUtil.getByteArrayData(bData,iLen,iLen+=3));
			hashData.put("ModulusLen"		, ByteUtil.getByteArrayData(bData,iLen,iLen+=2));
			int iModulusLen = Integer.parseInt(ByteUtil.BinstrToStr(hashData.get("ModulusLen")),2);
			//System.out.println("iModulusLen:"+iModulusLen);			
			hashData.put("Modulus"			, ByteUtil.getByteArrayData(bData,iLen,iLen+=iModulusLen));
			hashData.put("ExponentLen"		, ByteUtil.getByteArrayData(bData,iLen,iLen+=2));
			int iExponentLen = Integer.parseInt(ByteUtil.BinstrToStr(hashData.get("ExponentLen")),2);
			//System.out.println("iExponentLen:"+iExponentLen);			
			hashData.put("Exponent"			, ByteUtil.getByteArrayData(bData,iLen,iLen+=iExponentLen));
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return hashData;
	}
    
	public static void main(String[] args){
		 if (args.length != 2) {
	            System.out.println("輸入參數為[input:fiscPublicKeyHashData ,output:fiscPublicKey]");
		 }else{
	       	try{
	       		//step.1 載入安全保護文檔,input:fiscPublicKeyHashData
	       		String hashDataFilePath=args[0];
	       		String fiscPublicKeyPath=args[1];
	       		//用scsb的key模擬財金收到
	       		//String hashDataFilePath="D:\\encrypt\\fisc\\true\\95000000.KEY";
	       		//String fiscPublicKeyPath="D:\\encrypt\\fisc\\true\\fiscDecKey.test";
	       		FiscKey2Scsb fiscKey2scsb =new FiscKey2Scsb();
	       		boolean IsSign =fiscKey2scsb.LoadKeyHashData(hashDataFilePath );
	       		
	       		if (IsSign){
	       			try {
	       				EncrypRSA rsa = new EncrypRSA();
	       				rsa.SavePublicKey(fiscKey2scsb.getPublicKey(), fiscPublicKeyPath);
	       			} catch (FileNotFoundException e) {
	       				// TODO Auto-generated catch block
	       				e.printStackTrace();
	       			} catch (IOException e) {
	       				// TODO Auto-generated catch block
	       				e.printStackTrace();
	       			}
	       		}
	       		System.out.println("IsSign:"+IsSign);
	       		System.out.println("fingerPrint:"+fiscKey2scsb.fingerPrint);
	       	} catch (Exception e) {
	       		e.printStackTrace();
	        }
	     }
		 System.out.println( "scsb2fisc Successfully.");
	}	
}
