package com.scsb.scsbCode;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Hashtable;
import java.util.Vector;

import com.scsb.scsbEncrypt.EncrypDES3;
import com.scsb.scsbEncrypt.EncrypRSA;
import com.scsb.scsbSign.GenSignVerify;

/**
 * 上海銀行解密資料與驗章作業
 * 1.加密的本文用session key 做 DES3 (CBC/PKCS#7) 解密
 *   ps:C#的PKCS7 Padding相當於JAVA的PKCS5Padding Padding
 * 2.session key 以dec私鑰做 rsa(PKCS#1 v1.5) 解密
 * 3.簽章資訊(HASH DATA)格式:SHA-256 ,以enc公鑰做rsa(PKCS#1 v1.5)解密還原Hash Data
 * 4.將本文Hash Data後和簽章還原後的Hash Data做比對
 * @author 3471
 */
public class DecAction {
	
	//原始文檔
	private byte[] srcData;
	//保存對稱密鑰  (session key)
	private EncrypDES3 encEncrypDES3 =new EncrypDES3();
	//dec's自行產生的RSA私鑰
	private RSAPrivateKey decPrivateKey;
	//enc's提供的RSA公鑰
	private RSAPublicKey  encPublicKey;
	//安全保護檔的資料
	private Hashtable<String ,byte[]> hashData =new Hashtable<String ,byte[]>();
	
	public DecAction(){
		
	}
	/**
	 * 載入安全保護文檔 ,DEC'Public Key ,ENC'Private Key ,Session Key
	 * @param srcFilePath
	 */
	private boolean LoadSafeData(String safeFilePath ,String decPrivateKeyPath ,String encPublicKeyPath){
		boolean IsSign =false;
       	try{
    		EncrypRSA rsa1 = new EncrypRSA();
    		//step.1.載入dec's自行產生的的RSA私鑰
    		rsa1.LoadPrivateKey(decPrivateKeyPath);
    		decPrivateKey =rsa1.getRSAPrivateKey();
    		//step.2.載入enc's提供的RSA公鑰
    		EncrypRSA rsa2 = new EncrypRSA();
    		rsa2.LoadPublicKey(encPublicKeyPath);
    		encPublicKey	=rsa2.getRSAPublicKey();
       		//step.3.載入安全文檔
    		hashData=readSafeFile(safeFilePath);    		
    		//step.4.取得SESSION KEY 用dec私鑰做 rsa(PKCS#1 v1.5) 解密
    		byte[] binEncSessionkey=hashData.get("EncryptedKey");    		
    		EncrypRSA rsa3 = new EncrypRSA();
    		byte[] binSessionkey =rsa3.decrypt(decPrivateKey, binEncSessionkey);
    		encEncrypDES3.toKey(binSessionkey, "DESede" ,"DESede/CBC/PKCS5Padding");
    		//step.5.將文檔用 session key 做DES3(CEC),解密 
    		srcData=encEncrypDES3.DecrytoCBC(hashData.get("Content"), new byte[8]);
    		//step.6.取得簽章 ,用enc公鑰做驗證
    		Vector<byte[]> vHashData =new Vector<byte[]>();
    		vHashData.add(hashData.get("DateTime"));
    		vHashData.add(hashData.get("SenderID"));
    		vHashData.add(hashData.get("KeyVersion"));
    		vHashData.add(hashData.get("SignAlg"));
    		vHashData.add(srcData);
    		byte[] bHashData =ByteUtil.streamCopy(vHashData);
    		byte[] binSignature=hashData.get("Signature");
    		GenSignVerify genSignVerify =new GenSignVerify();
    		IsSign =genSignVerify.Verify(bHashData ,binSignature , encPublicKey, "SHA256withRSA");
       	} catch (Exception e) {
       		e.printStackTrace();
        }		
		return IsSign;
	}
	
	private static Hashtable<String ,byte[]> readSafeFile(String loadFilePath){
		Hashtable<String ,byte[]> hashData =new Hashtable<String ,byte[]>();
		try {
			File file = new File(loadFilePath);
			FileInputStream fis = new FileInputStream(file);
			byte[] bData = new byte[(int) file.length()];
			fis.read(bData);   			
			fis.close();
			int iLen=0;
			
			hashData.put("Header"			, ByteUtil.getByteArrayData(bData,iLen,iLen+=8));
			hashData.put("DateTime"			, ByteUtil.getByteArrayData(bData,iLen,iLen+=14));
			hashData.put("SignerInfoLen"	, ByteUtil.getByteArrayData(bData,iLen,iLen+=2));
			hashData.put("SenderID"			, ByteUtil.getByteArrayData(bData,iLen,iLen+=8));
			hashData.put("KeyVersion"		, ByteUtil.getByteArrayData(bData,iLen,iLen+=3));
			hashData.put("SignAlg"			, ByteUtil.getByteArrayData(bData,iLen,iLen+=1));
			hashData.put("SignatureLen"		, ByteUtil.getByteArrayData(bData,iLen,iLen+=2));
			int iSignatureLen = Integer.parseInt(ByteUtil.BinstrToStr(hashData.get("SignatureLen")),2);
			hashData.put("Signature"		, ByteUtil.getByteArrayData(bData,iLen,iLen+=iSignatureLen));
			hashData.put("RecipientnfoLen"	, ByteUtil.getByteArrayData(bData,iLen,iLen+=2));
			hashData.put("RecipientID"		, ByteUtil.getByteArrayData(bData,iLen,iLen+=8));
			hashData.put("KeyVersion"		, ByteUtil.getByteArrayData(bData,iLen,iLen+=3));
			hashData.put("EncAlg"			, ByteUtil.getByteArrayData(bData,iLen,iLen+=1));
			hashData.put("EncryptedKeyLen"	, ByteUtil.getByteArrayData(bData,iLen,iLen+=2));
			int iEncryptedKeyLen = Integer.parseInt(ByteUtil.BinstrToStr(hashData.get("EncryptedKeyLen")),2);		
			hashData.put("EncryptedKey"		, ByteUtil.getByteArrayData(bData,iLen,iLen+=iEncryptedKeyLen));
			hashData.put("ContentLen"		, ByteUtil.getByteArrayData(bData,iLen,iLen+=4));
			int iContentLen = Integer.parseInt(ByteUtil.BinstrToStr(hashData.get("ContentLen")),2);
			hashData.put("Content"			, ByteUtil.getByteArrayData(bData,iLen,iLen+=iContentLen));
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return hashData;
	}
    
	public boolean decrypt(String safeFilePath ,String decPrivateKeyPath ,String encPublicKeyPath ,String decDocFile){
		boolean isOk=false;
       	try{
       		boolean IsSign =this.LoadSafeData(safeFilePath ,decPrivateKeyPath ,encPublicKeyPath );
       		//step.2 產出解密後文檔
       		if (IsSign){
				FileOutputStream fos = new FileOutputStream(decDocFile);
				fos.write(this.srcData);
				fos.close();		
       		}else{
       			System.out.println(safeFilePath+",檔案解密驗章失敗!!");
       			return isOk;
       		}
       		isOk=true;
       	} catch (Exception e) {
       		e.printStackTrace();
        }		
		return isOk;
	}
	
	public static void main(String[] args){
		 if (args.length != 4 && args.length != 5) {
	            System.out.println("輸入參數為[safeFile ,decPrivateKey ,encPublicKey ,decDocFile ");
		 }else{
	       		//step.1 載入安全保護文檔,DEC'Public Key ,ENC'Private Key,session key(DES3)
	       		String safeFilePath=args[0];
	       		String decPrivateKeyPath=args[1];
	       		String encPublicKeyPath=args[2];
	       		String decDocFile=args[3];
	       		
	       		//String safeFilePath="D:\\TommyData\\pchome\\TEST.201412290001.zip.ENC";
	       		//String decPrivateKeyPath="D:\\TommyData\\pchome\\scsbKey.pri";
	       		//String encPublicKeyPath="D:\\TommyData\\pchome\\pchomeKey.pub";
	       		//String decDocFile="D:\\TommyData\\pchome\\TEST.201412290001.zip.DEC";
	       		DecAction decAction =new DecAction();
	       		boolean isOK=decAction.decrypt(safeFilePath ,decPrivateKeyPath ,encPublicKeyPath ,decDocFile);
	       		if (isOK){
	       		 System.out.println( "檔案解密驗章成功!");
	       		}
	       		
	     }
	}	
}