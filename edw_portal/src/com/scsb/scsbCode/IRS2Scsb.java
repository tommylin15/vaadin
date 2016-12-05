package com.scsb.scsbCode;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import com.scsb.scsbEncrypt.EncrypAES;
import com.scsb.scsbEncrypt.EncrypRSA;
import com.scsb.scsbSign.Ca;
import com.scsb.scsbSign.XmlSignVerify_Enveloping;
import com.scsb.util.INIReader;
import com.scsb.util.LogWriter;
import com.scsb.util.Xml;
import com.scsb.util.ZipUtils;
/**
 * IRS回傳上海銀行資料,加密及簽章作業
 * 1.session key 以scsb私鑰做 rsa(PKCS#1 v1.5) 解密
 * 2.payload.xml用session key 做 AES-256(ECB/PKCS#7) 解密
 * 3.簽章資訊(payload.xml)格式:SHA-256(2048) ,以irs公鑰做rsa(PKCS#1 v1.5)解密
 * @author 3471
 */
public class IRS2Scsb{
	
	//保存對稱密鑰  (session key)
	private EncrypAES scsbEncrypAES =new EncrypAES();
	//上海銀行申請憑證RSA私鑰
	private RSAPrivateKey scsbPrivateKey;
	private RSAPublicKey  scsbPublicKey;
	//IRS提供的RSA公鑰
	private RSAPublicKey  irsPublicKey;

	public IRS2Scsb(){
		
	}

	/**
	 * 載入key檔 ,SCSB'Private Key ,IRS'Public Key
	 * @param srcFilePath
	 */
	private void LoadKey(String scsbPrivateKeyPath ,String scsbPublicKeyPath ,String irsPublicKeyPath ){
		try {
			EncrypRSA rsa1 = new EncrypRSA();
			//載入上海銀行自行產生的的RSA公鑰
			rsa1.LoadPublicKey(scsbPublicKeyPath);
    		//載入上海銀行自行產生的RSA私鑰
    		rsa1.LoadPrivateKey(scsbPrivateKeyPath);
    		//載入IRS產生的RSA公鑰
    		EncrypRSA rsa2 = new EncrypRSA();
    		rsa2.LoadPublicKey(irsPublicKeyPath);
			
    		init(rsa1.getRSAPrivateKey() ,rsa1.getRSAPublicKey() ,rsa2.getRSAPublicKey()); 	
		} catch (Exception e) {
		}
	}
	/**
	 * 參數載入
	 * @param privateKey
	 * @param publicKey
	 * @param irsPublicKey
	 */
	private void init(PrivateKey privateKey ,PublicKey publicKey ,PublicKey irsPublicKey){
       	try{
    		
    		//載入上海銀行自行產生的RSA私鑰
    		this.scsbPrivateKey  =(RSAPrivateKey)privateKey;
    		//載入上海銀行自行產生的RSA公鑰
    		this.scsbPublicKey  =(RSAPublicKey)publicKey;
    		//載入IRS提供的RSA公鑰
    		this.irsPublicKey  =(RSAPublicKey)irsPublicKey;
       	} catch (Exception e) {
       		e.printStackTrace();
        }		
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
	
	/**
	 * 程式起始
	 * @param nowPath
	 * @param args
	 */
	public void runDecrypt(String nowPath ,String[] args){
		try {
			
			LogWriter log = new LogWriter(nowPath+"/log/IRS2Scsb"+LogWriter.GetDate()+".log");
			log.append("IRS2SCSB.............................................................BEGIN",true);
			INIReader iniReader =new INIReader(nowPath+"/config.ini");
			String CA_PATH =iniReader.Read("FATCA" ,"CA_PATH");		
			log.append("CA_PATH:"+CA_PATH,true);
			String SCSB_PRIVATE_KEY =iniReader.Read("FATCA" ,"SCSB_PRIVATE_KEY");
			log.append("SCSB_PRIVATE_KEY:"+SCSB_PRIVATE_KEY,true);
			String SCSB_PRIVATE_PASS =iniReader.Read("FATCA" ,"SCSB_PRIVATE_PASS");
			log.append("SCSB_PRIVATE_PASS:*******",true);
			
			String SCSB_CA_CER =iniReader.Read("FATCA" ,"SCSB_CA_CER");
			log.append("SCSB_CA_CER:"+SCSB_CA_CER,true);
			String IRS_CA_CER =iniReader.Read("FATCA" ,"IRS_CA_CER");
			log.append("IRS_CA_CER:"+IRS_CA_CER,true);
			
			String IRS_GIIN =iniReader.Read("IRS" ,"IRS_GIIN");
			String SCSB_GIIN =iniReader.Read("SCSB" ,"SCSB_GIIN");
			log.append("IRS_GIIN:"+IRS_GIIN,true);
			String IRS_DATA_PATH =iniReader.Read("IRS" ,"IRS_DATA_PATH");
			log.append("IRS_DATA_PATH:"+IRS_DATA_PATH,true);
			String IRS_OUT_PATH =iniReader.Read("IRS" ,"IRS_OUT_PATH");
			log.append("IRS_OUT_PATH:"+IRS_OUT_PATH,true);
			String IRS_TEMP_PATH =iniReader.Read("IRS" ,"IRS_TEMP_PATH");
			log.append("IRS_TEMP_PATH:"+IRS_TEMP_PATH,true);
					
			String RECEIVER_GIIN =iniReader.Read("IRS" ,"RECEIVER_GIIN");
			log.append("RECEIVER_GIIN:"+RECEIVER_GIIN,true);

	   		String scsbPrivateKeyPath=CA_PATH+"/"+SCSB_PRIVATE_KEY;
	   		String scsbCerPath=CA_PATH+"/"+SCSB_CA_CER;
	   		String irsCerPath=CA_PATH+"/"+IRS_CA_CER;

	   		String srcFilePath=IRS_DATA_PATH+"/"+args[0];
	   		
	   		String safeFilePath=IRS_TEMP_PATH+"/"+IRS_GIIN+"_Payload.xml";
	   		String zipFilePath=IRS_TEMP_PATH+"/"+IRS_GIIN+"_Payload.zip";
	   		
	   		String aesFilePath=IRS_TEMP_PATH+"/"+IRS_GIIN+"_Payload";
	   		String aesKeyFilePath=IRS_TEMP_PATH+"/"+SCSB_GIIN+"_Key";
	   		String metadataFilePath=IRS_TEMP_PATH+"/"+IRS_GIIN+"_Metadata.xml";
			
	   		//step.1 由憑證檔來,私鑰取得時必須輸入key pass
			try {
		   		log.append("[STEP.1]取得憑證資料....",true);
		   		Ca ca =new Ca();
		   		log.append("...[Scsb Cer]...."+scsbCerPath,true);
		   		PublicKey publickey = ca.createPublicKey(scsbCerPath);
		   		log.append("...[Scsb Private Key]...."+scsbPrivateKeyPath,true);
		   		PrivateKey privatekey =ca.createPemRsaPrivateKey(scsbPrivateKeyPath ,SCSB_PRIVATE_PASS);
		   		log.append("...[Irs Cer]...."+irsCerPath,true);
		   		PublicKey  irsPublickey =ca.createPublicKey(irsCerPath);
		   		init(privatekey ,publickey ,irsPublickey);			
			} catch (Exception e) {
				log.append("[STEP.1]"+e.getMessage(),true);
				e.printStackTrace();
				return;
			}
	   		ZipUtils zipUtils=new ZipUtils();
	   		//step.2.解壓縮
	   		try {
	   	   		log.append("[STEP.2]解壓縮...."+srcFilePath,true);
	   	   		File srcFile =new File(srcFilePath);
				zipUtils.unZip(srcFile, IRS_TEMP_PATH, log);
			} catch (Exception e) {
				log.append("[STEP.2]"+e.getMessage(),true);
				e.printStackTrace();
				return;
			}
	   		//step.3.解密AES Key
			try {
				log.append("[STEP.3]AES KEY以SCSB PrivateKey 解密, AES Key session...",true);
				File aesKeyfile = new File(aesKeyFilePath);
				FileInputStream fis = new FileInputStream(aesKeyfile);
				byte[] baesKey = new byte[(int) aesKeyfile.length()];
				fis.read(baesKey);
				fis.close();			
				EncrypRSA rsa = new EncrypRSA();
				byte[] bSessionKey =rsa.decrypt(scsbPrivateKey , baesKey);			
   		   		String hexs =ByteUtil.BinaryToHexString(bSessionKey);
   		   		log.append("[STEP.3][aes-256]"+hexs,true);
				//scsbEncrypAES.toKey(bSessionKey, "AES" ,"AES/CBC/PKCS7Padding");
   		   		scsbEncrypAES.toKey(bSessionKey, "AES" ,"AES/ECB/PKCS7Padding");
			} catch (Exception e) {
				log.append("[STEP.3]"+e.getMessage(),true);
				e.printStackTrace();
				return;
			}
			//step.4.以aes key解密zip文檔
			try {
				log.append("[STEP.4]以aes key解密zip文檔....",true);
				File aesFile = new File(aesFilePath);
				FileInputStream fis = new FileInputStream(aesFile);
				byte[] bZipFile = new byte[(int) aesFile.length()];
				fis.read(bZipFile);
				fis.close();
				//byte[] bZipData=scsbEncrypAES.DecrytoCBC(bZipFile, new byte[16]);
				byte[] bZipData=scsbEncrypAES.DecrytoECB(bZipFile);
   				FileOutputStream fos = new FileOutputStream(zipFilePath);
   				fos.write(bZipData);
   				fos.close();				
			} catch (Exception e) {
				log.append("[STEP.4][Exception]"+e.getMessage(),true);
				e.printStackTrace();
				return;
			}
			//step.5.unzip文檔
	   		try {
	   	   		log.append("[STEP.5]解壓縮文檔...."+zipFilePath,true);
	   	   		File srcFile =new File(zipFilePath);
				zipUtils.unZip(srcFile, IRS_TEMP_PATH, log);
			} catch (Exception e) {
				log.append("[STEP.5]"+e.getMessage(),true);
				e.printStackTrace();
				return;
			}
			
			//step.6.驗章
			/* 後面不用做了~解出來即可
	   		try {
		   		log.append("[STEP.6]xml驗章(Enveloping模式+RSA+SHA256)....",true);
		   		if (args.length >=2){
			   		for(int i=1;i<args.length;i++){
				   		XmlSignVerify_Enveloping xmlSign =new XmlSignVerify_Enveloping();
				   		boolean isSign=xmlSign.Validate(IRS_TEMP_PATH+"/"+args[i]);
				   		//boolean isSign=xmlSign.ValidateX509("D:/encrypt/fatca/irs/temp/J6NT1S.00000.LE.158_Parload.xml");
				   		//boolean isSign=xmlSign.ValidateX509("D:/encrypt/fatca/irs/000000.00000.TA.124_Payload.xml");
				   		
				   		if(isSign){
				   			log.append("[STEP.6]....驗章成功:"+args[i],true);
				   			//step.6.1驗章成功者,將文檔解出來
				   			org.w3c.dom.Document doc=xmlSign.getData(IRS_TEMP_PATH+"/"+args[i],"UTF-8");
				   			//org.w3c.dom.Document doc=xmlSign.getData("D:/encrypt/fatca/scsb/temp/J6NT1S.00000.LE.158_Parload.xml","UTF-8");
				   			//org.w3c.dom.Document doc=xmlSign.getData("D:/encrypt/fatca/irs/000000.00000.TA.124_Payload.xml","UTF-8");
				   			Xml xml =new Xml();
				   			xml.writeToFile(doc ,IRS_OUT_PATH+"/"+args[i],"UTF-8");
				   			log.append("[STEP.6]....輸出xml:"+args[i],true);
				   		}else{
				   			log.append("[STEP.6]....驗章失敗:"+args[i],true);
				   		}
					}
			   	}
		   	} catch (Exception e1) {
		   		log.append("[STEP.6][EXCEPTION]...."+e1.getMessage(),true);
				e1.printStackTrace();
				return;
			}
			*/
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){
		String nowPath="./";
		/*test..begin*/
		//nowPath="D:/encrypt/fatca";
		//args =new String[]{"840FCxZMDWeINg635RLOkdwD44HYEq8X.zip"};
		/*test..end*/
		IRS2Scsb irs2scsb=new IRS2Scsb();
		irs2scsb.runDecrypt(nowPath, args);  		
	}	
}