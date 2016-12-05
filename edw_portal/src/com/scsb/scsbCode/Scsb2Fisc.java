package com.scsb.scsbCode;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteOrder;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Hashtable;
import java.util.Vector;

import com.scsb.scsbEncrypt.EncrypDES3;
import com.scsb.scsbEncrypt.EncrypRSA;
import com.scsb.scsbSign.GenSignVerify;
import com.scsb.util.DateUtil;

/**
 * 上海銀行資料傳遞至財金,加密及簽章作業
 * 1.本文用session key 做 DES3 (CBC/PKCS#7) 加密
 *   ps:C#的PKCS7 Padding相當於JAVA的PKCS5Padding Padding
 * 2.session key 以fisc公鑰做 rsa(PKCS#1 v1.5) 加密
 * 3.簽章資訊(HASH DATA)格式:SHA-256 ,以scsb私鑰做rsa(PKCS#1 v1.5)加密
 * @author 3471
 */
public class Scsb2Fisc {
	
	//原始文檔
	private byte[] srcData;
	//保存對稱密鑰  (session key)
	private EncrypDES3 scsbEncrypDES3 =new EncrypDES3();
	//上海銀行自行產生的的RSA私鑰
	private RSAPrivateKey scsbPrivateKey;
	//財金提供的RSA公鑰
	private RSAPublicKey  fiscPublicKey;
	//最後要產出安全保護檔的資料
	private Hashtable<String ,byte[]> hashData =new Hashtable<String ,byte[]>();
	
	public Scsb2Fisc(){
		
	}
	/**
	 * 載入文檔 ,SCSB'Private Key ,FISC'Public Key
	 * @param srcFilePath
	 */
	private void LoadData(String srcFilePath ,String scsbPrivateKeyPath ,String fiscPublicKeyPath ,String fiscPublicKeyMode){
       	try{
       		//載入文檔
    		File file = new File(srcFilePath);
    		FileInputStream fis = new FileInputStream(file);
    		srcData = new byte[(int) file.length()];
    		fis.read(srcData);
    		fis.close();
    		
    		EncrypRSA rsa1 = new EncrypRSA();
    		//載入上海銀行自行產生的的RSA私鑰
    		rsa1.LoadPrivateKey(scsbPrivateKeyPath);
    		scsbPrivateKey =rsa1.getRSAPrivateKey();
    		//載入財金提供的RSA公鑰
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
    		
    		//產生SESSION KEY for 3DES
       		scsbEncrypDES3.generateKey("DESede", "DESede/CBC/PKCS5Padding");    		
       	} catch (Exception e) {
       		e.printStackTrace();
        }		
	}
	
	private byte[] getSrcData(){
		return srcData;
	}
	
	/**
	 * 在傳入字串的左邊補滿指定字元
	 * @param s
	 * @param padstr
	 * @param pad
	 * @return
	 */
	public static String lpad(String s,String padstr,int pad) {
		if (s.length()>pad) return s;
		StringBuffer a = new StringBuffer(pad);
		for (int i = 0; i < pad; i++) 
			a = a.append(padstr);
		return a.substring(s.length())+ s;
	}
	
	private static void writeSafeFile(String safeFilePath ,Hashtable<String ,byte[]> hashData){
		byte[] bData=new byte[0];
		Vector<byte[]> vData=new Vector<byte[]>();
		vData.add(hashData.get("Header"));
		vData.add(hashData.get("DateTime"));
		vData.add(hashData.get("SignerInfoLen"));
		vData.add(hashData.get("SenderID"));
		vData.add(hashData.get("KeyVersion"));
		vData.add(hashData.get("SignAlg"));
		vData.add(hashData.get("SignatureLen"));
		vData.add(hashData.get("Signature"));
		vData.add(hashData.get("RecipientnfoLen"));
		vData.add(hashData.get("RecipientID"));
		vData.add(hashData.get("KeyVersion"));
		vData.add(hashData.get("EncAlg"));
		vData.add(hashData.get("EncryptedKeyLen"));
		vData.add(hashData.get("EncryptedKey"));
		vData.add(hashData.get("ContentLen"));
		vData.add(hashData.get("Content"));
		
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
		 if (args.length != 4 && args.length != 5) {
	            System.out.println("輸入參數為[srcFile ,scsbPrivateKey ,fiscPublicKey ,SafeFile,[S]:fiscPublicKeySafeMode]");
		 }else{
	       	try{
	       		//step.1 載入文檔,scsb private key ,fisc public key ,session key(DES3)
	       		String srcFilePath=args[0];
	       		String scsbPrivateKeyPath=args[1];
	       		String fiscPublicKeyPath=args[2];
	       		String safeFilePath=args[3];
	       		//財金公鑰是否有加密(S:加密,O:沒有加密)
	       		String fiscPublicKeyMode="O";
	       		if (args.length == 5){
	       			fiscPublicKeyMode=args[4];
	       		}
	       		
	       		//String srcFilePath="D:\\encrypt\\fisc\\cupdata.txt.zip";
	       		//String scsbPrivateKeyPath="D:\\encrypt\\fisc\\scsbKey.pri";
	       		//String fiscPublicKeyPath="D:\\encrypt\\fisc\\fiscDecKey.pub";
	       		//String safeFilePath="D:\\encrypt\\fisc\\cupdata.txt.zip.ENC";
	       		//String fiscPublicKeyMode="O";
	       		
	       		Scsb2Fisc scsb2fisc =new Scsb2Fisc();
	       		scsb2fisc.LoadData(srcFilePath ,scsbPrivateKeyPath ,fiscPublicKeyPath ,fiscPublicKeyMode);
	       		//step.2建立Hash Data
	       		//	DateTime[CCYYMMDDHHmmss] | SenderID[C8] | KeyVersion[C3] | SignAlg[2:RSAwithSha-256] | Content[文檔])
	       		String sHeader="FiScSeCf";
	       		//時間
	       		String sDT=DateUtil.getDT();
	       		//傳送者id
	       		String sScsb="01100000";
	       		//接收者id
	       		String sFisc="95000000";
	       		//傳送者key var
	       		String sScsbKeyVar="001";
	       		//接收者key var
	       		String sFiscKeyVar="001";
	       		//簽章加密模式
	       		byte[] bSignAlg =new byte[]{0x02};
	       		//文檔的加密模式
	       		byte[] bEncAlg =new byte[]{0x11};
	       		//step.3以Hash Data +SCSB的PRIVATE KEY產生簽章
	       		GenSignVerify genSignVerify =new GenSignVerify();
	    		Vector<byte[]> vHashData =new Vector<byte[]>();
	    		vHashData.add(sDT.getBytes());
	    		vHashData.add(sScsb.getBytes());
	    		vHashData.add(sScsbKeyVar.getBytes());
	    		vHashData.add(bSignAlg);
	    		vHashData.add(scsb2fisc.getSrcData());
	    		byte[] bHashData =ByteUtil.streamCopy(vHashData);	       		
	       		byte[] bSignature   =genSignVerify.Sign(bHashData, scsb2fisc.scsbPrivateKey, "SHA256withRSA");
	       		//step.4以SESSION KEY加密本文
	       		byte[] bEncryData =scsb2fisc.scsbEncrypDES3.EncrytoCBC(scsb2fisc.getSrcData(),new byte[8]);
	       		//step.5以fisc public key 加密 session key
	       		EncrypRSA rsa = new EncrypRSA();
	       		byte[] bSessionKey =rsa.encrypt(scsb2fisc.fiscPublicKey, scsb2fisc.scsbEncrypDES3.getSecretKey().getEncoded());
	       		//step.6產生安全保護檔案
	       		int    iSignLen = bSignature.length;
	       		byte[] bSignLen =ByteUtil.convertToBytes(iSignLen ,2 ,ByteOrder.BIG_ENDIAN);
	       		
	       		int    iSignerInfoLen = sScsb.length()+sScsbKeyVar.length()+1+2+iSignLen;
	       		byte[] bSignerInfoLen =ByteUtil.convertToBytes(iSignerInfoLen ,2 ,ByteOrder.BIG_ENDIAN);
	       		
	       		int    iSessionKeyLen = bSessionKey.length;
	       		byte[] bSessionKeyLen =ByteUtil.convertToBytes(iSessionKeyLen ,2 ,ByteOrder.BIG_ENDIAN);
	       		
	       		int    iEncryDataLen = bEncryData.length;
	       		byte[] bEncryDataLen =ByteUtil.convertToBytes(iEncryDataLen ,4 ,ByteOrder.BIG_ENDIAN);
	       		
	       		int    iRecipientnfoLen=sFisc.length()+sFiscKeyVar.length()+1+2+iSessionKeyLen+4+iEncryDataLen;
	       		byte[] bRecipientnfoLen =ByteUtil.convertToBytes(iRecipientnfoLen ,2 ,ByteOrder.BIG_ENDIAN);
	       		
	       		
	       		scsb2fisc.hashData.put("Header"			, sHeader.getBytes());
	       		scsb2fisc.hashData.put("DateTime"		, sDT.getBytes());
	       		scsb2fisc.hashData.put("SignerInfoLen"	, bSignerInfoLen);
	       		scsb2fisc.hashData.put("SenderID"		, sScsb.getBytes());
	       		scsb2fisc.hashData.put("KeyVersion"		, sScsbKeyVar.getBytes());
	       		scsb2fisc.hashData.put("SignAlg"		, bSignAlg);
	       		scsb2fisc.hashData.put("SignatureLen"	, bSignLen);
	       		scsb2fisc.hashData.put("Signature"		, bSignature);
	       		scsb2fisc.hashData.put("RecipientnfoLen", bRecipientnfoLen);
	       		scsb2fisc.hashData.put("RecipientID"	, sFisc.getBytes());
	       		scsb2fisc.hashData.put("KeyVersion"		, sFiscKeyVar.getBytes());
	       		scsb2fisc.hashData.put("EncAlg"			, bEncAlg);
	       		scsb2fisc.hashData.put("EncryptedKeyLen", bSessionKeyLen);
	       		scsb2fisc.hashData.put("EncryptedKey"	, bSessionKey);
	       		scsb2fisc.hashData.put("ContentLen"		, bEncryDataLen);
	       		scsb2fisc.hashData.put("Content"		, bEncryData);
	       		writeSafeFile(safeFilePath ,scsb2fisc.hashData);      		
	       	} catch (Exception e) {
	       		e.printStackTrace();
	        }
	     }
		 System.out.println( "scsb2fisc Successfully.");
	}	
}