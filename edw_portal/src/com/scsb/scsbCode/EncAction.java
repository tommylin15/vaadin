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
 * 上海銀行資料傳遞,加密及簽章作業
 * 1.本文用session key 做 DES3 (CBC/PKCS#7) 加密
 *   ps:C#的PKCS7 Padding相當於JAVA的PKCS5Padding Padding
 * 2.session key 以PCHOME公鑰做 rsa(PKCS#1 v1.5) 加密
 * 3.簽章資訊(HASH DATA)格式:SHA-256 ,以scsb私鑰做rsa(PKCS#1 v1.5)加密
 * @author 3471
 */
public class EncAction {
	
	//原始文檔
	private byte[] srcData;
	//保存對稱密鑰  (session key)
	private EncrypDES3 encEncrypDES3 =new EncrypDES3();
	//enc's 提供的RSA私鑰
	private RSAPrivateKey encPrivateKey;
	//dec's 提供的RSA公鑰
	private RSAPublicKey  decPublicKey;
	//最後要產出安全保護檔的資料
	private Hashtable<String ,byte[]> hashData =new Hashtable<String ,byte[]>();
	
	public EncAction(){
		
	}
	/**
	 * 載入文檔 ,enc's Private Key ,dec's Public Key
	 * @param srcFilePath
	 */
	private void LoadData(String srcFilePath ,String encPrivateKeyPath ,String decPublicKeyPath ){
       	try{
       		//載入文檔
    		File file = new File(srcFilePath);
    		FileInputStream fis = new FileInputStream(file);
    		srcData = new byte[(int) file.length()];
    		fis.read(srcData);
    		fis.close();
    		
    		EncrypRSA rsa1 = new EncrypRSA();
    		//載入enc's產生的的RSA私鑰
    		rsa1.LoadPrivateKey(encPrivateKeyPath);
    		encPrivateKey =rsa1.getRSAPrivateKey();
    		//載入dec's提供的RSA公鑰
    		EncrypRSA rsa2 = new EncrypRSA();
	    	rsa2.LoadPublicKey(decPublicKeyPath);
	    	decPublicKey	=rsa2.getRSAPublicKey();
    		
    		//產生SESSION KEY for 3DES
       		encEncrypDES3.generateKey("DESede", "DESede/CBC/PKCS5Padding");    		
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
	
	public boolean encrypt(String srcFilePath ,String encPrivateKeyPath ,String decPublicKeyPath ,String safeFilePath){
		boolean isOk =false;
		this.LoadData(srcFilePath ,encPrivateKeyPath ,decPublicKeyPath );
       	try{
       		//step.1 載入文檔,ender private key ,decer public key ,session key(DES3)
       		this.LoadData(srcFilePath ,encPrivateKeyPath ,decPublicKeyPath );
       		//step.2建立Hash Data
       		//	DateTime[CCYYMMDDHHmmss] | SenderID[C8] | KeyVersion[C3] | SignAlg[2:RSAwithSha-256] | Content[文檔])
       		String sHeader="FiScSeCf";
       		//時間
       		String sDT=DateUtil.getDT();
       		//傳送者id
       		String sEnc="01100000";
       		//接收者id
       		String sDec="95000000";
       		//傳送者key var
       		String sEncKeyVar="001";
       		//接收者key var
       		String sDecKeyVar="001";
       		//簽章加密模式
       		byte[] bSignAlg =new byte[]{0x02};
       		//文檔的加密模式
       		byte[] bEncAlg =new byte[]{0x11};
       		//step.3以Hash Data +SCSB的PRIVATE KEY產生簽章
       		GenSignVerify genSignVerify =new GenSignVerify();
    		Vector<byte[]> vHashData =new Vector<byte[]>();
    		vHashData.add(sDT.getBytes());
    		vHashData.add(sEnc.getBytes());
    		vHashData.add(sEncKeyVar.getBytes());
    		vHashData.add(bSignAlg);
    		vHashData.add(this.getSrcData());
    		byte[] bHashData =ByteUtil.streamCopy(vHashData);	       		
       		byte[] bSignature   =genSignVerify.Sign(bHashData, this.encPrivateKey, "SHA256withRSA");
       		//step.4以SESSION KEY加密本文
       		byte[] bEncryData =this.encEncrypDES3.EncrytoCBC(this.getSrcData(),new byte[8]);
       		//step.5以接收者 public key 加密 session key
       		EncrypRSA rsa = new EncrypRSA();
       		byte[] bSessionKey =rsa.encrypt(this.decPublicKey, this.encEncrypDES3.getSecretKey().getEncoded());
       		//step.6產生安全保護檔案
       		int    iSignLen = bSignature.length;
       		byte[] bSignLen =ByteUtil.convertToBytes(iSignLen ,2 ,ByteOrder.BIG_ENDIAN);
       		
       		int    iSignerInfoLen = sEnc.length()+sEncKeyVar.length()+1+2+iSignLen;
       		byte[] bSignerInfoLen =ByteUtil.convertToBytes(iSignerInfoLen ,2 ,ByteOrder.BIG_ENDIAN);
       		
       		int    iSessionKeyLen = bSessionKey.length;
       		byte[] bSessionKeyLen =ByteUtil.convertToBytes(iSessionKeyLen ,2 ,ByteOrder.BIG_ENDIAN);
       		
       		int    iEncryDataLen = bEncryData.length;
       		byte[] bEncryDataLen =ByteUtil.convertToBytes(iEncryDataLen ,4 ,ByteOrder.BIG_ENDIAN);
       		
       		int    iRecipientnfoLen=sDec.length()+sDecKeyVar.length()+1+2+iSessionKeyLen+4+iEncryDataLen;
       		byte[] bRecipientnfoLen =ByteUtil.convertToBytes(iRecipientnfoLen ,2 ,ByteOrder.BIG_ENDIAN);
       		
       		
       		this.hashData.put("Header"			, sHeader.getBytes());
       		this.hashData.put("DateTime"		, sDT.getBytes());
       		this.hashData.put("SignerInfoLen"	, bSignerInfoLen);
       		this.hashData.put("SenderID"		, sEnc.getBytes());
       		this.hashData.put("KeyVersion"		, sEncKeyVar.getBytes());
       		this.hashData.put("SignAlg"		, bSignAlg);
       		this.hashData.put("SignatureLen"	, bSignLen);
       		this.hashData.put("Signature"		, bSignature);
       		this.hashData.put("RecipientnfoLen", bRecipientnfoLen);
       		this.hashData.put("RecipientID"	, sDec.getBytes());
       		this.hashData.put("KeyVersion"		, sDecKeyVar.getBytes());
       		this.hashData.put("EncAlg"			, bEncAlg);
       		this.hashData.put("EncryptedKeyLen", bSessionKeyLen);
       		this.hashData.put("EncryptedKey"	, bSessionKey);
       		this.hashData.put("ContentLen"		, bEncryDataLen);
       		this.hashData.put("Content"		, bEncryData);
       		writeSafeFile(safeFilePath ,this.hashData);
       		isOk=true;
       	} catch (Exception e) {
       		isOk=false;
       		e.printStackTrace();
        }		
		return isOk;
	}
	
	
	public static void main(String[] args){
		 if (args.length != 4 ) {
	            System.out.println("輸入參數為[srcFile ,enc'PrivateKey ,dec'PublicKey ,SafeFile");
		 }else{
	       		//step.1 載入文檔,ender private key ,decer public key ,session key(DES3)
	       		String srcFilePath=args[0];
	       		String encPrivateKeyPath=args[1];
	       		String decPublicKeyPath=args[2];
	       		String safeFilePath=args[3];	       		
	       		EncAction encAction =new EncAction();
	       		boolean isOK=encAction.encrypt(srcFilePath ,encPrivateKeyPath ,decPublicKeyPath ,safeFilePath);
	       		if (isOK){
		       		 System.out.println( "檔案加密成功!");
		       		}	       		
	     }
	}	
}