package com.scsb.scsbCode;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.scsb.scsbEncrypt.EncrypAES;
import com.scsb.scsbEncrypt.EncrypRSA;
import com.scsb.scsbSign.Ca;
import com.scsb.scsbSign.XmlSignVerify_Enveloping;
import com.scsb.util.INIReader;
import com.scsb.util.IO;
import com.scsb.util.LogWriter;
import com.scsb.util.ZipUtils;

/**
 * 上海銀行資料傳遞至IRS,加密及簽章作業
 * 1.簽章資訊(payload.xml)格式:SHA-256(2048) ,以scsb私鑰做rsa(PKCS#1 v1.5)加密
 * 2.payload.xml用session key 做 AES-256(ECB/PKCS#7) 加密
 * 3.session key 以IRS公鑰做 rsa(PKCS#1 v1.5) 加密
 * @author 3471
 */
public class Scsb2IRS{
	
	//原始文檔(PayLoad.xml)
	private byte[] srcData;
	//保存對稱密鑰  (session key)
	private EncrypAES scsbEncrypAES =new EncrypAES();
	//上海銀行申請憑證RSA私鑰
	private RSAPrivateKey scsbPrivateKey;
	private RSAPublicKey  scsbPublicKey;
	//IRS提供的RSA公鑰
	private RSAPublicKey  irsPublicKey;

	public Scsb2IRS(){
		
	}
	
	/**
	 * 參數載入
	 * @param srcFilePath
	 * @param privateKey
	 * @param publicKey
	 * @param irsPublicKey
	 */
	private void init(String srcFilePath ,PrivateKey privateKey ,PublicKey publicKey ,PublicKey irsPublicKey){
       	try{
       		//載入文檔
    		File file = new File(srcFilePath);
    		FileInputStream fis = new FileInputStream(file);
    		this.srcData = new byte[(int) file.length()];
    		fis.read(this.srcData);
    		fis.close();
    		
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
	
	public void runEncryp(String[] args){
		LogWriter log;
		try {
			String nowPath=args[0];
			log = new LogWriter(nowPath+"/log/Scsb2IRS"+LogWriter.GetDate()+".log");
			log.append("SCSB2IRS.............................................................BEGIN",true);
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
			
			String SCSB_GIIN =iniReader.Read("SCSB" ,"SCSB_GIIN");
			log.append("SCSB_GIIN:"+SCSB_GIIN,true);
			String SCSB_DATA_PATH =iniReader.Read("SCSB" ,"SCSB_DATA_PATH");
			log.append("SCSB_DATA_PATH:"+SCSB_DATA_PATH,true);
			String SCSB_ZIP_PATH =iniReader.Read("SCSB" ,"SCSB_ZIP_PATH");
			log.append("SCSB_ZIP_PATH:"+SCSB_ZIP_PATH,true);
			String SCSB_OUT_PATH =iniReader.Read("SCSB" ,"SCSB_OUT_PATH");
			log.append("SCSB_OUT_PATH:"+SCSB_OUT_PATH,true);
			String SCSB_TEMP_PATH =iniReader.Read("SCSB" ,"SCSB_TEMP_PATH");
			log.append("SCSB_TEMP_PATH:"+SCSB_TEMP_PATH,true);
			
			
			String SCSB_XML =iniReader.Read("SCSB" ,"SCSB_XML");
			log.append("SCSB_XML:"+SCSB_XML,true);
			String RECEIVER_GIIN =iniReader.Read("SCSB" ,"RECEIVER_GIIN");
			log.append("RECEIVER_GIIN:"+RECEIVER_GIIN,true);
	
	   		String scsbPrivateKeyPath=CA_PATH+"/"+SCSB_PRIVATE_KEY;
	   		String scsbCerPath=CA_PATH+"/"+SCSB_CA_CER;
	   		String irsCerPath=CA_PATH+"/"+IRS_CA_CER;
	
	   		String srcFilePath=SCSB_DATA_PATH+"/"+SCSB_XML;
	   		String metadataFilePath_in=SCSB_DATA_PATH+"/"+SCSB_GIIN+"_Metadata.xml";
	   		
	   		String safeFilePath=SCSB_TEMP_PATH+"/"+SCSB_GIIN+"_Payload.xml";
	   		String zipFilePath=SCSB_TEMP_PATH+"/"+SCSB_GIIN+"_Payload.zip";
	   		
	   		String aesFilePath=SCSB_ZIP_PATH+"/"+SCSB_GIIN+"_Payload";
	   		String aesKeyFilePath=SCSB_ZIP_PATH+"/"+RECEIVER_GIIN+"_Key";
	   		String metadataFilePath_out=SCSB_ZIP_PATH+"/"+SCSB_GIIN+"_Metadata.xml";
			
	   		//清空資料
	   		IO.deleteSubDir(new File(SCSB_ZIP_PATH));
	   		IO.deleteSubDir(new File(SCSB_TEMP_PATH));
	   		
	   		ZipUtils zipUtils=new ZipUtils();	   		
	   		//由憑證檔來,私鑰取得時必須輸入key pass
			try {
		   		log.append("[STEP.1]取得憑證資料....",true);
		   		Ca ca =new Ca();
		   		log.append("...[Scsb Cer]...."+scsbCerPath,true);
		   		PublicKey publickey;				
				publickey = ca.createPublicKey(scsbCerPath);
		   		log.append("...[Scsb Private Key]...."+scsbPrivateKeyPath,true);
		   		PrivateKey privatekey =ca.createPemRsaPrivateKey(scsbPrivateKeyPath ,SCSB_PRIVATE_PASS);
		   		log.append("...[Irs Cer]...."+irsCerPath,true);
		   		PublicKey  irsPublickey =ca.createPublicKey(irsCerPath);
		   		log.append("...[載入文檔]...."+srcFilePath,true);
		   		init(srcFilePath ,privatekey ,publickey ,irsPublickey);				
			} catch (Exception e1) {
				log.append("[STEP.1][EXCEPTION]...."+e1.getMessage(),true);
				e1.printStackTrace();
				return;
			}
	   		
	   		//step.2 xml簽章(Enveloping模式+RSA+SHA256) [SenderGIIN]_Payload.xml
			boolean isSign=false;
	   		try {
		   		log.append("[STEP.2]xml簽章開始(Enveloping模式+RSA+SHA256) [SenderGIIN]_Payload.xml....",true);
		   		XmlSignVerify_Enveloping xmlSign =new XmlSignVerify_Enveloping();
				//xmlSign.XMLSign(srcFilePath,scsbPublicKey,scsbPrivateKey ,safeFilePath);
		   		//改用X509
		   		xmlSign.XMLSignX509(srcFilePath,CA_PATH+"/"+SCSB_CA_CER,scsbPrivateKey ,safeFilePath);
		   		log.append("...xml簽章完畢....",true);
		   		//step.2.1自己先驗章
		   		isSign=xmlSign.ValidateX509(safeFilePath);
		   		//isSign=true;
		   	} catch (Exception e1) {
		   		log.append("[STEP.2][EXCEPTION]...."+e1.getMessage(),true);
				e1.printStackTrace();
				return;
			}

	   		if (isSign){
	   			log.append("...驗章成功!!",true);
		   		//step.3   壓縮(zip), 產出[SenderGIIN]_Payload.zip
	   			log.append("[STEP.3]壓縮檔案...",true);
	   			File safeFile = new File(safeFilePath);
	   			File zipFile  = new File(zipFilePath);
	   			zipUtils.makeZip(safeFile,zipFile,log);
		   		//step.4   將文檔以AES-256加密, 產出[SenderGIIN]_Payload
	   			log.append("[STEP.4]將ZIP檔以AES-256加密...",true);
	   			
	    		FileInputStream fis = new FileInputStream(zipFile);
	    		byte[] zipData = new byte[(int) zipFile.length()];
	    		fis.read(zipData);
	    		fis.close();
	   			try {
	   	    		//產生SESSION KEY for AES-256
	   		   		scsbEncrypAES.generateKey("AES", "AES/ECB/PKCS7Padding");
	   		   		//scsbEncrypAES.generateKey("AES", "AES/CBC/PKCS7Padding");
	   		   		String hexs =ByteUtil.BinaryToHexString(scsbEncrypAES.getSecretKey().getEncoded());
	   		   		log.append("[STEP.4][aes-256]"+hexs,true);
	   				
	   				byte[] bEncryData = scsbEncrypAES.EncrytoECB(zipData);
	   				//byte[] bEncryData =scsbEncrypAES.EncrytoCBC(zipData,new byte[16]);
		   			FileOutputStream fos = new FileOutputStream(aesFilePath);
		   			fos.write(bEncryData);
		   			fos.close();		
	   			} catch (Exception e1) {
	   				log.append("[STEP.4][EXCEPTION]"+e1.getMessage(),true);
					e1.printStackTrace();
					return;	   				
	   			}	   			
		   		//step.5 AES KEY以ISR PublicKey 加密, 產出[ReceiverGIIN]_Key
	   			log.append("[STEP.5]AES KEY以ISR PublicKey 加密, 產出[ReceiverGIIN]_Key...",true);
	   			try {
	   				EncrypRSA rsa = new EncrypRSA();   				
	   				byte[] bSessionKey =rsa.encrypt(irsPublicKey, scsbEncrypAES.getSecretKey().getEncoded());
	   				FileOutputStream fos = new FileOutputStream(aesKeyFilePath);
	   				fos.write(bSessionKey);
	   				fos.close();		
	   			} catch (FileNotFoundException e) {
	   				log.append("[STEP.5][EXCEPTION]"+e.getMessage(),true);
	   			} catch (Exception e) {
	   				log.append("[STEP.5][EXCEPTION]"+e.getMessage(),true);
	   			}
		   		//step.6 [SenderGIIN]_Payload + [ReceiverGIIN]_Key + [SenderGIIN]_Metadata.xml 產出[UTC]_[SenderGIIN].zip
	   			log.append("[STEP.6][SenderGIIN]_Payload + [ReceiverGIIN]_Key + [SenderGIIN]_Metadata.xml 產出[UTC]_[SenderGIIN].zip..",true);
	   			IO.copyFile(metadataFilePath_in, metadataFilePath_out);
	   			//本地時間
	   			java.util.Calendar cal = java.util.Calendar.getInstance();
	   			log.append("...本地時間:"+new Date(cal.getTimeInMillis()),true);
	   			//時間差
	   			int zoneOffset = cal.get(java.util.Calendar.ZONE_OFFSET);
	   			//夏令時差
	   			int dstOffset = cal.get(java.util.Calendar.DST_OFFSET);
	   			//取得UTC時間
	   			cal.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));
	   			SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
	   			SimpleDateFormat tf = new SimpleDateFormat("HHmmssSSS");
	   			String utcDate = df.format(new java.sql.Timestamp(cal.getTimeInMillis()));
	   			log.append("...UTC日期:"+utcDate,true);
	   			String utcTime = tf.format(new java.sql.Timestamp(cal.getTimeInMillis()));
	   			log.append("...UTC時間:"+utcTime,true);
	   			String outFilePath =SCSB_OUT_PATH+"/"+utcDate+"T"+utcTime+"Z_"+SCSB_GIIN+".zip";
	   			File zipFolder= new File(SCSB_ZIP_PATH);
	   			File outFile  = new File(outFilePath);
	   			zipUtils.makeZip(zipFolder,outFile,log);
	   			log.append("檔案產出完畢:"+outFilePath,true);
	   		}
	   		else log.append("[STEP.2]驗章失敗...",true);		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}	   		
	}
	
	public static void main(String[] args) throws Exception{
		String nowPath="./";
		/*test..begin*/
		//nowPath="D:/encrypt/fatca";
		//args=new String[1];
		//args[0]=nowPath;
		/*test..end*/
   		Scsb2IRS scsb2irs =new Scsb2IRS();
   		scsb2irs.runEncryp(args);

	}	
}