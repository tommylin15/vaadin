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
 * 上海銀行解密財金資料與驗章作業
 * 1.加密的本文用session key 做 DES3 (CBC/PKCS#7) 解密
 *   ps:C#的PKCS7 Padding相當於JAVA的PKCS5Padding Padding
 * 2.session key 以scsb私鑰做 rsa(PKCS#1 v1.5) 解密
 * 3.簽章資訊(HASH DATA)格式:SHA-256 ,以fisc公鑰做rsa(PKCS#1 v1.5)解密還原Hash Data
 * 4.將本文Hash Data後和簽章還原後的Hash Data做比對
 * @author 3471
 */
public class Fisc2Scsb {
	
	//原始文檔
	private byte[] srcData;
	//保存對稱密鑰  (session key)
	private EncrypDES3 fiscEncrypDES3 =new EncrypDES3();
	//上海銀行自行產生的RSA私鑰
	private RSAPrivateKey scsbPrivateKey;
	//財金提供的RSA公鑰
	private RSAPublicKey  fiscPublicKey;
	//安全保護檔的資料
	private Hashtable<String ,byte[]> hashData =new Hashtable<String ,byte[]>();
	
	public Fisc2Scsb(){
		
	}
	/**
	 * 載入安全保護文檔 ,SCSB'Public Key ,FISC'Private Key ,Session Key
	 * @param srcFilePath
	 */
	private boolean LoadSafeData(String safeFilePath ,String scsbPrivateKeyPath ,String fiscPublicKeyPath ,String fiscPublicKeyMode){
		boolean IsSign =false;
       	try{
    		EncrypRSA rsa1 = new EncrypRSA();
    		//step.1.載入上海銀行自行產生的的RSA私鑰
    		rsa1.LoadPrivateKey(scsbPrivateKeyPath);
    		scsbPrivateKey =rsa1.getRSAPrivateKey();
    		//step.2.載入財金提供的RSA公鑰
    		if (fiscPublicKeyMode.equals("S")){
    			FiscKey2Scsb fiscKey2scsb=new FiscKey2Scsb();
    			boolean isKeySign =fiscKey2scsb.LoadKeyHashData(fiscPublicKeyPath);
   				if (isKeySign) fiscPublicKey	=fiscKey2scsb.getPublicKey();
   				else System.out.println(fiscPublicKeyPath+"公鑰解密驗章失敗!!");
   			}else{
	    		EncrypRSA rsa2 = new EncrypRSA();
	    		rsa2.LoadPublicKey(fiscPublicKeyPath);
	    		fiscPublicKey	=rsa2.getRSAPublicKey();
   			}
       		//step.3.載入安全文檔
    		hashData=readSafeFile(safeFilePath);    		
    		//step.4.取得SESSION KEY 用scsb私鑰做 rsa(PKCS#1 v1.5) 解密
    		byte[] binEncSessionkey=hashData.get("EncryptedKey");    		
    		EncrypRSA rsa3 = new EncrypRSA();
    		byte[] binSessionkey =rsa3.decrypt(scsbPrivateKey, binEncSessionkey);
    		fiscEncrypDES3.toKey(binSessionkey, "DESede" ,"DESede/CBC/PKCS5Padding");
    		//step.5.將文檔用 session key 做DES3(CEC),解密 
    		srcData=fiscEncrypDES3.DecrytoCBC(hashData.get("Content"), new byte[8]);
    		//step.6.取得簽章 ,用fisc公鑰做驗證
    		Vector<byte[]> vHashData =new Vector<byte[]>();
    		vHashData.add(hashData.get("DateTime"));
    		vHashData.add(hashData.get("SenderID"));
    		vHashData.add(hashData.get("KeyVersion"));
    		vHashData.add(hashData.get("SignAlg"));
    		vHashData.add(srcData);
    		byte[] bHashData =ByteUtil.streamCopy(vHashData);
    		byte[] binSignature=hashData.get("Signature");
    		GenSignVerify genSignVerify =new GenSignVerify();
    		IsSign =genSignVerify.Verify(bHashData ,binSignature , fiscPublicKey, "SHA256withRSA");
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
    
	public static void main(String[] args){
		 if (args.length != 4 && args.length != 5) {
	            System.out.println("輸入參數為[safeFile ,scsbPrivateKey ,fiscPublicKey ,decDocFile ,[S]:fiscPublicKeySafeMode]");
		 }else{
	       	try{
	       		//step.1 載入安全保護文檔,SCSB'Public Key ,FISC'Private Key,session key(DES3)
	       		String safeFilePath=args[0];
	       		String scsbPrivateKeyPath=args[1];
	       		String fiscPublicKeyPath=args[2];
	       		String decDocFile=args[3];
	       		//財金公鑰是否有加密(S:加密,O:沒有加密)
	       		String fiscPublicKeyMode="O";
	       		if (args.length == 5){
	       			fiscPublicKeyMode=args[4];
	       		}
	       		//String safeFilePath="D:\\encrypt\\fisc\\F01100000.ICAFEQBD.03041421.zip.ENC";
	       		//String scsbPrivateKeyPath="D:\\encrypt\\fisc\\scsbKey.pri";
	       		//String fiscPublicKeyPath="D:\\encrypt\\fisc\\fiscDecKey.pub";
	       		//String decDocFile="D:\\encrypt\\fisc\\F01100000.ICAFEQBD.03041421.zip";
	       		//模擬我是財金時,要載入財金私鑰,上銀公鑰
	       		//String safeFilePath="D:\\encrypt\\fisc\\CUPDATA.TXT.ENC";
	       		//String scsbPrivateKeyPath="D:\\encrypt\\fisc\\fiscTestbKey.pri";
	       		//String fiscPublicKeyPath="D:\\encrypt\\fisc\\scsbKey.pub";
	       		//String decDocFile="D:\\encrypt\\fisc\\CUPDATA.TXT.DEC";
	       		Fisc2Scsb fisc2scsb =new Fisc2Scsb();
	       		boolean IsSign =fisc2scsb.LoadSafeData(safeFilePath ,scsbPrivateKeyPath ,fiscPublicKeyPath ,fiscPublicKeyMode);
	       		//step.2 產出解密後文檔
	       		if (IsSign){
					FileOutputStream fos = new FileOutputStream(decDocFile);
					fos.write(fisc2scsb.srcData);
					fos.close();		
	       		}else{
	       			System.out.println(safeFilePath+",檔案解密驗章失敗!!");
	       			return;
	       		}
	       	} catch (Exception e) {
	       		e.printStackTrace();
	        }
	     }
		 System.out.println( "檔案解密驗章成功!");
	}	
}