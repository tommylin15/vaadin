package com.scsb.scsbEncrypt;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;  
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;  
import java.security.KeyPairGenerator;  
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;  
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;  
import java.security.interfaces.RSAPublicKey;  
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;  
import javax.crypto.Cipher;  
import javax.crypto.IllegalBlockSizeException;  
import javax.crypto.NoSuchPaddingException;  

import com.scsb.scsbCode.ByteUtil;
  
/*RSA非對稱式加解密*/
public class EncrypRSA {
	 private KeyPair keyPair =null;
	 private RSAPrivateKey privateKey = null;
	 private RSAPublicKey  publicKey  = null;
	 
     public EncrypRSA(){
     }
     /**
      * generateKey
      * @param keysize 1024 /2048
      * @param keyword 
      */
     public void generateKey(int keysize ,String keyword ){
         //KeyPairGenerator類用於生成公鑰和私鑰對，基於RSA算法生成對像  
         KeyPairGenerator keyPairGen;
		try {
			 keyPairGen = KeyPairGenerator.getInstance("RSA");
			 //用指定的字串去產生金鑰只要指定的字串是同一個，產生的金鑰就會是同一個
			 SecureRandom random = new SecureRandom();
			 random.setSeed(keyword.getBytes());
	         //初始化密鑰對生成器，密鑰大小為2048位
	         keyPairGen.initialize(keysize ,random);  
	         //生成一個密鑰對，保存在keyPair中  
	         keyPair = keyPairGen.generateKeyPair();  
	         //得到私鑰  
	         privateKey = (RSAPrivateKey)keyPair.getPrivate();               
	         //得到公鑰  
	         publicKey = (RSAPublicKey)keyPair.getPublic();
	         //KeyFactory keyFactory = KeyFactory.getInstance("RSA");
	         // using known public key's modulus & exponent to re-build public key
	         //BigInteger modulus =publicKey.getModulus();
	         //BigInteger exponent =publicKey.getPublicExponent();
	         //RSAPublicKeySpec rsaPublicKeySpec = new RSAPublicKeySpec(modulus, exponent);
	         //try {
	 		 //	PublicKey rePublicKey = keyFactory.generatePublic(rsaPublicKeySpec);
	 		 //} catch (InvalidKeySpecException e) {
	 		 //	e.printStackTrace();
	 		 //}
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}      	 
     }

     
     public RSAPublicKey  getRSAPublicKey(){
    	 return publicKey;
     }
     public RSAPrivateKey  getRSAPrivateKey(){
    	 return privateKey;
     }
     public BigInteger  getRSAPublicExponent(){
    	 BigInteger exponent =publicKey.getPublicExponent();
    	 return exponent;
     }   
     public BigInteger  getRSAPublicModulus(){
    	 BigInteger modulus =publicKey.getModulus();
    	 return modulus;
     }     

     public String bytesToString(byte[] encrytpByte) {
         String result = "";
         for (Byte bytes : encrytpByte) {
             result += bytes.toString() + " ";
         }
         return result;
     }     
    /** 
     * 加密 
     * @param Key 
     * @param srcBytes 
     * @return 
     * @throws NoSuchAlgorithmException 
     * @throws NoSuchPaddingException 
     * @throws InvalidKeyException 
     * @throws IllegalBlockSizeException 
     * @throws BadPaddingException 
     */
     public byte[] encrypt(Key encKey,byte[] srcBytes) {  
        if(encKey!=null){  
            //Cipher負責完成加密或解密工作，基於RSA  
            Cipher cipher;
			try {
				cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
				cipher.init(Cipher.ENCRYPT_MODE, encKey);
	            byte[] resultBytes = cipher.doFinal(srcBytes);  
	            return resultBytes;				
			} catch (NoSuchAlgorithmException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (NoSuchPaddingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (InvalidKeyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalBlockSizeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (BadPaddingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  

        }  
        return null;  
    }     
      
    /** 
     * 解密  
     * @param Key 
     * @param srcBytes 
     * @return 
     * @throws Exception 
     */  
     public byte[] decrypt(Key decKey,byte[] srcBytes){  
        if(decKey!=null){ 
            try {
            	Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
                cipher.init(cipher.DECRYPT_MODE, decKey);
                byte[] resultBytes = cipher.doFinal(srcBytes);
                return resultBytes;
            } catch (Exception e) {   
                try {
					throw new Exception(e.getMessage());
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}   
            }           	
        }  
        return null;  
    }  
    
	/**
	 * Save the keyPair into files.
	 * @param publicKeyFile
	 * @param privateKeyFile
	 * @throws IOException
	 */
     public void SaveKeyPair(String publicKeyFile ,String privateKeyFile ) throws IOException {
			PrivateKey privateKey = keyPair.getPrivate();
			PublicKey publicKey = keyPair.getPublic();
				 
			// Store Public Key.
			X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(
			publicKey.getEncoded());
			FileOutputStream fos = new FileOutputStream(publicKeyFile);
			fos.write(x509EncodedKeySpec.getEncoded());
			fos.close();
				 
			// Store Private Key.
			PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(
			privateKey.getEncoded());
			fos = new FileOutputStream(privateKeyFile);
			fos.write(pkcs8EncodedKeySpec.getEncoded());
			fos.close();
	}
     
 	/**
 	 * Save the PublicKey into files.
 	 * @param publicKey
 	 * @param publicKeyFile
 	 * @throws IOException
 	 */
      public void SavePublicKey(RSAPublicKey publicKey ,String publicKeyFile) throws IOException {
 			// Store Public Key.
 			X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(
 			publicKey.getEncoded());
 			FileOutputStream fos = new FileOutputStream(publicKeyFile);
 			fos.write(x509EncodedKeySpec.getEncoded());
 			fos.close();
 	} 
      
   	/**
   	 * Save the PrivateKey into files.
   	 * @param publicKey
   	 * @param publicKeyFile
   	 * @throws IOException
   	 */
        public void SavePrivateKey(RSAPrivateKey privateKey ,String privateKeyFile) throws IOException {
			// Store Private Key.
			PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(
			privateKey.getEncoded());
			FileOutputStream fos = new FileOutputStream(privateKeyFile);
			fos.write(pkcs8EncodedKeySpec.getEncoded());
			fos.close();
   	}      
	
	/**
	 * Load the keys from files.
	 * @param publicKeyFile
	 * @param privateKeyFile
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
     public void LoadKeyPair(String publicKeyFile ,String privateKeyFile)
		throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		// Read Public Key.
		try {
			LoadPublicKey(publicKeyFile);
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Read Private Key.
		LoadPrivateKey(privateKeyFile);
		keyPair =new KeyPair(publicKey, privateKey);
		
	}
     
	/**
	 * LoadPublicKey
	 * @param keyFile
	 * @return void
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @throws NoSuchProviderException 
	 */
     public void LoadPublicKey(String keyFile)
		throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException {
		// Read Public Key.
		File file = new File(keyFile);
		FileInputStream fis = new FileInputStream(file);
		byte[] encodedPublicKey = new byte[(int) file.length()];
		fis.read(encodedPublicKey);
		fis.close();
		
		// Generate Key.
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(encodedPublicKey);
		
		publicKey = (RSAPublicKey)keyFactory.generatePublic(publicKeySpec);
		
	}
	/**
	 * LoadPrivateKey
	 * @param keyFile
	 * @return void
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
     public void LoadPrivateKey(String keyFile)
		throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		// Read Public Key.
		File file = new File(keyFile);
		FileInputStream fis = new FileInputStream(file);
		byte[] encodedPrivateKey = new byte[(int) file.length()];
		fis.read(encodedPrivateKey);
		fis.close();

		// Generate Key.
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(encodedPrivateKey);
		privateKey = (RSAPrivateKey)keyFactory.generatePrivate(privateKeySpec);
	}	
     /**
      *  modulus是257個字節,需要去掉首字節"0",用剩下的256個字節實例化BigInteger (java to c#)
      * @param data
      * @return
      */
     public static byte[] removeMSZero(byte[] data) {  
         byte[] data1;  
         int len = data.length;  
         if (data[0] == 0) {  
             data1 = new byte[data.length - 1];  
             System.arraycopy(data, 1, data1, 0, len - 1);  
         } else  
             data1 = data;  
         return data1;  
     }   
     
     /**
      * 取得rsa key的FingerPrint by SHA-256
      * @param data [rsa.publicKey.getEncoded()]
      * @return FingerPrint
     * @throws NoSuchAlgorithmException 
      */
     public static String getFingerPrint(byte[] data) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(data);
		String str1=ByteUtil.BinaryToHexString(md.digest());		
		return str1;
     }      
    /** 
     * @param args 
     * @throws NoSuchAlgorithmException  
     * @throws BadPaddingException  
     * @throws IllegalBlockSizeException  
     * @throws NoSuchPaddingException  
     * @throws InvalidKeyException  
     */  
    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {  
        EncrypRSA rsa = new EncrypRSA();
        /*
        rsa.generateKey(2048, "SCSB");
        String msg = "上海商業儲蓄銀行";  
        
        //用公鑰加密  
        byte[] srcBytes = msg.getBytes();  
        byte[] resultBytes = rsa.encrypt(rsa.publicKey, srcBytes);  
          
        //用私鑰解密  
        byte[] decBytes = rsa.decrypt(rsa.privateKey, resultBytes);  
          
        System.out.println("明文是:" + msg);  
        System.out.println("公鑰加密後是:" + new String(resultBytes));  
        System.out.println("私鑰解密後是:" + new String(decBytes));
        
        
        //用公鑰加密  
        byte[] srcBytes2 = msg.getBytes();  
        byte[] resultBytes2 = rsa.encrypt(rsa.publicKey, srcBytes2);  
          
        //用私鑰解密  
        byte[] decBytes2 = rsa.decrypt(rsa.privateKey, resultBytes2);  
          
        System.out.println("明文是:" + msg);  
        System.out.println("私鑰加密後是:" + new String(resultBytes2));  
        System.out.println("公鑰解密後是:" + new String(decBytes2));  
        */
        //LoadPublicKey
        try {
        	//RSAPublicKey key =rsa.publicKey;
        	
        	//產出FingerPrint
			rsa.LoadPublicKey("d:/encrypt/fisc/true/fiscDecKey.pub");
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			//md.update(rsa.publicKey.getEncoded());
			//String str1=ByteUtil.BinaryToHexString(md.digest());
			//System.out.println(str1);
			String str2=ByteUtil.BinaryToHexString(md.digest(rsa.publicKey.getEncoded()));
			System.out.println(str2);
			//SCSB
			System.out.println(rsa.publicKey.getAlgorithm());
			System.out.println(rsa.publicKey.getFormat());
	BigInteger modulus=rsa.getRSAPublicModulus();
	BigInteger publicExponent=rsa.getRSAPublicExponent();
	byte[] bModulus =modulus.toByteArray();
	bModulus =EncrypRSA.removeMSZero(bModulus);
	System.out.println(ByteUtil.BinaryToHexString(bModulus)+"");
	String str3=ByteUtil.BinaryToHexString(md.digest(bModulus));
	System.out.println(str3);
	
	//iaik.security.rsa.RSAPublicKey rsaKey =;
	//iaik.security.rsa.RSAPublicKey rsaKey =new iaik.security.rsa.RSAPublicKey(modulus,publicExponent);
	//byte[] bFr =rsaKey.getFingerprint();
	//System.out.println(ByteUtil.BinaryToHexString(bFr)+"");		
	
	
	
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }  
} 