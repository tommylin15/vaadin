package com.scsb.scsbSign;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.security.cert.CertificateException;

import org.apache.xml.security.utils.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMReader;
import org.bouncycastle.openssl.PasswordFinder;

import sun.misc.BASE64Decoder;

public class Ca {
	
	public Ca(){
	}
	
	/**
	 * 檢視Cer檔內容
	 * @param cerFile
	 * @return
	 * @throws Exception
	 */
	public X509Certificate showCerInfo(String cerFile) throws Exception{
		/*open ssl
		# 查看KEY信息
		> openssl rsa -noout -text -in myserver.key

		# 查看CSR信息
		> openssl req -noout -text -in myserver.csr

		# 查看證書信息
		> openssl x509 -noout -text -in ca.crt

		# 驗證證書
		# 會提示self signed
		> openssl verify selfsign.crt
			
		- 將憑證 PEM 格式轉成 DER 格式
		openssl x509 -inform PEM -outform DER -in ClientCA.crt -out ClientCA.cer
		
		- 將憑證 DER 格式轉成 PEM 格式
		openssl x509 -inform DER -in GCA.cer -out PEM GCA.crt
		
		- 將 CRL 檔由 PEM 格式轉成 DER 格式
		openssl crl -in trysoft.crl -outform DER -out trysoft_der.crl
		
		- 檢驗 CRL 檔並將 DER 格式轉成文字格式
		wget http://gca.nat.gov.tw/repository/GCA4/CRL/complete.crl
		wget http://gca.nat.gov.tw/repository/Certs/GCA.cer		 
		openssl crl -inform DER -in complete.crl -text -CAfile GCA.crt -out gca_crl.txt
		*/
		File file = new File(cerFile);
		FileInputStream fis = new FileInputStream(file);
		byte[] srcData = new byte[(int) file.length()];
		fis.read(srcData);
		fis.close();		
		
		ByteArrayInputStream bs = new ByteArrayInputStream(new String(srcData).getBytes());
 
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        X509Certificate cert = (X509Certificate) cf.generateCertificate(bs);
        return cert;
	}
	/**
	 * 從憑證中取得Publickey(PEM)
	 * @param cerFilePath
	 * @return
	 * @throws CertificateException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 * @throws SignatureException
	 * @throws java.security.cert.CertificateException
	 * @throws FileNotFoundException
	 */
	public PublicKey createPublicKey(String cerFilePath) throws CertificateException,
		NoSuchAlgorithmException, InvalidKeyException, SignatureException, java.security.cert.CertificateException, FileNotFoundException {
		FileInputStream fis = new FileInputStream(cerFilePath);
		CertificateFactory cf = CertificateFactory.getInstance("X.509");
		Certificate c = cf.generateCertificate(fis);
		PublicKey pk = c.getPublicKey();
		return pk;
	}	
	
	/**
	 * 從憑證中取得Publickey(PEM)
	 * @param cert
	 * @return
	 * @throws CertificateException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 * @throws SignatureException
	 * @throws java.security.cert.CertificateException
	 */
	public PublicKey createPublicKey(byte[] cert) throws CertificateException,
		NoSuchAlgorithmException, InvalidKeyException, SignatureException, java.security.cert.CertificateException {
		
		CertificateFactory cf = CertificateFactory.getInstance("X.509");
		Certificate c = (Certificate) cf.generateCertificate(new ByteArrayInputStream(cert));
		PublicKey pk = c.getPublicKey();
		return pk;
	}	
	
	/**
	 * 取得privateKey(PEM)(RSA)
	 * @param privateKeyPath
	 * @return
	 * @throws Exception
	 */
	public PrivateKey createPemRsaPrivateKey(String privateKeyPath ,final String pass) throws Exception {
        File privateKeyFile = new File(privateKeyPath);
        byte[] encodedKey = new byte[(int) privateKeyFile.length()];
        new FileInputStream(privateKeyFile).read(encodedKey);
        
        Security.addProvider(new BouncyCastleProvider());  
        ByteArrayInputStream bais = new ByteArrayInputStream(encodedKey);  
        PEMReader reader = new PEMReader(new InputStreamReader(bais), new PasswordFinder() {  
	        public char[] getPassword() {  
		        return pass.toCharArray();  
		    }  
        });
        KeyPair keyPair = (KeyPair) reader.readObject();  
        reader.close();  
        PrivateKey prik = keyPair.getPrivate();
        //System.out.println(prik);
        // KeySpec keySpec2 = new PKCS8EncodedKeySpec(prik.getEncoded());
        //KeyFactory kf = KeyFactory.getInstance("RSA");
        //System.out.println(kf.generatePrivate(keySpec2));
        //RSAPrivateKey pk = (RSAPrivateKey) kf.generatePrivate(keySpec2);
        //System.out.println(pk);
        return prik;
	} 	
	
	/**
	 * 取得privateKey(RSA)(DER)(PCKS#8)
	 * @param privateKeyPath
	 * @return
	 * @throws Exception
	 */
	public RSAPrivateKey getRsaPrivateKey(String privateKeyPath) throws Exception {
	        File privateKeyFile = new File(privateKeyPath);
	        byte[] encodedKey = new byte[(int) privateKeyFile.length()];
	        
	        new FileInputStream(privateKeyFile).read(encodedKey);
	        ByteBuffer keyBytes = new BASE64Decoder().decodeBufferToByteBuffer(encodedKey.toString());
	        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(keyBytes.array());
	        KeyFactory kf = KeyFactory.getInstance("RSA");
	        RSAPrivateKey pk = (RSAPrivateKey) kf.generatePrivate(privateKeySpec);
	        return pk;
	}
	
	
	/**
	 * 使用憑證的驗章
	 * @param data
	 * @param sigToVerify
	 * @param cert
	 * @param algorithm
	 * @return
	 * @throws CertificateException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 * @throws SignatureException
	 * @throws java.security.cert.CertificateException
	 */
	public boolean checkSignedFile(byte[] data, byte[] sigToVerify ,PublicKey pk, String algorithm) throws CertificateException,
			NoSuchAlgorithmException, InvalidKeyException, SignatureException, java.security.cert.CertificateException {
		
		Signature sig;
		boolean verifies = false;
		sig = Signature.getInstance(algorithm);
		sig.initVerify(pk);
		sig.update(data);
		verifies = sig.verify(sigToVerify);
		return verifies;
	}	
	
	public static void main(String[] args) throws Exception{
		//公鑰憑證(DER)
		System.out.println("=====以下為公鑰憑證(DER)=========================================================");
		String cerFile ="d:\\encrypt\\fatca\\fatca_versign_cert.cer";
		Ca ca =new Ca();
		X509Certificate cert =ca.showCerInfo(cerFile);
		System.out.println(cert);
		
		//私鑰(PEM)
		System.out.println("=====以下為私鑰(PEM)=========================================================");
		String privateFile ="d:\\encrypt\\fatca\\scsbkey.key";
		Ca ca2 =new Ca();
		PrivateKey prik=ca2.createPemRsaPrivateKey(privateFile ,"Scsb911adm");
        KeySpec keySpec2 = new PKCS8EncodedKeySpec(prik.getEncoded());
        KeyFactory kf = KeyFactory.getInstance("RSA");
        System.out.println(kf.generatePrivate(keySpec2));

	}
		
}