package com.scsb.scsbEncrypt;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;  
import java.security.NoSuchAlgorithmException;  
import java.security.Security;  

import javax.crypto.BadPaddingException;  
import javax.crypto.Cipher;  
import javax.crypto.IllegalBlockSizeException;  
import javax.crypto.KeyGenerator;  
import javax.crypto.NoSuchPaddingException;  
import javax.crypto.SecretKey;  
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

    /** 
     * DESede Coder<br/> 
     * secret key length:   112/168 bit, default:   168 bit<br/> 
     * mode:    ECB/CBC/PCBC/CTR/CTS/CFB/CFB8 to CFB128/OFB/OBF8 to OFB128<br/> 
     *          ECB解密,不需要IV
     * padding: Nopadding/PKCS5Padding/ISO10126Padding/ 
     * @author Tommy Lin 
     *  
     */    
    public class EncrypDES3 {  
        /** 
         * 金鑰演算法 
        */  
        //private static final String KEY_ALGORITHM = "DESede";  
        //private static final String DEFAULT_CIPHER_ALGORITHM = "DESede/ECB/PKCS5Padding";  
        //private static final String DEFAULT_CIPHER_ALGORITHM = "DESede/CBC/PKCS5Padding";    	
        //private static final String DEFAULT_CIPHER_ALGORITHM = "DESede/ECB/ISO10126Padding";  
    	
        // KeyGenerator 提供對稱密鑰生成器的功能，支持各種算法  
        private KeyGenerator keygen;  
        // SecretKey 負責保存對稱密鑰  
        private SecretKey deskey;  
        // Cipher負責完成加密或解密工作  
        private Cipher cipher;  
        // 該字節數組負責保存加密的結果  
        private byte[] cipherByte;  
        
        public EncrypDES3(){
        	
        }
        
        /**
         * Grenerate Key
         * @param KEY_ALGORITHM :DESede
         * @param DEFAULT_CIPHER_ALGORITHM : DESede/ECB/PKCS5Padding ,DESede/CBC/PKCS5Padding
         * @throws NoSuchAlgorithmException 
         * @throws NoSuchPaddingException
         */
        public void generateKey(String KEY_ALGORITHM ,String DEFAULT_CIPHER_ALGORITHM)
        	throws NoSuchAlgorithmException, NoSuchPaddingException  {
        	
            Security.addProvider(new com.sun.crypto.provider.SunJCE());  
            // 實例化支持DES算法的密鑰生成器(算法名稱命名需按規定，否則拋出異常)  
            keygen = KeyGenerator.getInstance(KEY_ALGORITHM);  
            // 生成密鑰  
            deskey = keygen.generateKey();  
            // 生成Cipher對像,指定其支持的DES算法  
            cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
        }  
      
        public SecretKey  getSecretKey(){
       	 	return deskey;
        }     
        
        /**
         * ReGenerate SecretKey
         * @param key
         * @param KEY_ALGORITHM :DESede
         * @param DEFAULT_CIPHER_ALGORITHM :DESede/ECB/PKCS5Padding ,DESede/CBC/PKCS5Padding
         * @throws Exception
         */
        public void toKey(byte[] key ,String KEY_ALGORITHM ,String DEFAULT_CIPHER_ALGORITHM) throws Exception{
        	keygen = KeyGenerator.getInstance(KEY_ALGORITHM);
        	cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            DESedeKeySpec dks = new DESedeKeySpec(key);
            SecretKeyFactory skf = SecretKeyFactory.getInstance(KEY_ALGORITHM);  
            deskey = skf.generateSecret(dks);
        }         
        
    	/**
    	 * Save the des3key into files.
    	 * @param keyFile
    	 * @throws IOException
    	 */
         public void SaveKey(String keyFile) throws IOException {
        	 	FileOutputStream fos = new FileOutputStream(keyFile);
    			fos.write(deskey.getEncoded());
    			fos.close();
    	}     
         
     	/**
     	 * Load the des3key from files.
     	 * @param keyFile
     	 * @throws Exception 
     	 */
          public void LoadKey(String keyFile ,String KEY_ALGORITHM ,String DEFAULT_CIPHER_ALGORITHM) throws Exception {
      		File file = new File(keyFile);
    		FileInputStream fis = new FileInputStream(file);
    		byte[] des3Key = new byte[(int) file.length()];
    		fis.read(des3Key);
    		fis.close();
    		toKey(des3Key ,KEY_ALGORITHM ,DEFAULT_CIPHER_ALGORITHM);
     	}           
        
        /** 
         * 對字符串加密 
         *  
         * @param str 
         * @return 
         * @throws InvalidKeyException 
         * @throws IllegalBlockSizeException 
         * @throws BadPaddingException 
         */  
        public byte[] EncrytoECB(byte[] src) throws InvalidKeyException,  
                IllegalBlockSizeException, BadPaddingException {  
            // 根據密鑰，對Cipher對像進行初始化，ENCRYPT_MODE表示加密模式
        	cipher.init(Cipher.ENCRYPT_MODE, deskey);
            //byte[] src = str.getBytes();  
            // 加密，結果保存進cipherByte  
            cipherByte = cipher.doFinal(src);  
            return cipherByte;  
        }  
        
        /** 
         * 對字符串加密 
         *  
         * @param str 
         * @param keyiv
         * @return 
         * @throws InvalidKeyException 
         * @throws IllegalBlockSizeException 
         * @throws BadPaddingException 
         * @throws InvalidAlgorithmParameterException 
         */  
        public byte[] EncrytoCBC(byte[] src ,byte[] keyiv) throws InvalidKeyException,  
                IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {  
            // 根據密鑰，對Cipher對像進行初始化，ENCRYPT_MODE表示加密模式
        	IvParameterSpec ips = new IvParameterSpec(keyiv);
        	cipher.init(Cipher.ENCRYPT_MODE, deskey ,ips);
            //byte[] src = str.getBytes();  
            // 加密，結果保存進cipherByte
            cipherByte = cipher.doFinal(src);  
            return cipherByte;  
        }        
      
        /** 
         * 對字符串解密 
         *  
         * @param buff 
         * @return 
         * @throws InvalidKeyException 
         * @throws IllegalBlockSizeException 
         * @throws BadPaddingException 
         */  
        public byte[] DecrytoECB(byte[] buff) throws InvalidKeyException,  
                IllegalBlockSizeException, BadPaddingException {  
            // 根據密鑰，對Cipher對像進行初始化，DECRYPT_MODE表示解密模式  
        	cipher.init(Cipher.DECRYPT_MODE, deskey);  
            cipherByte = cipher.doFinal(buff);  
            return cipherByte;  
        }  
        
        /** 
         * 對字符串解密 
         *  
         * @param buff 
         * @return 
         * @throws InvalidKeyException 
         * @throws IllegalBlockSizeException 
         * @throws BadPaddingException 
         * @throws InvalidAlgorithmParameterException 
         */  
        public byte[] DecrytoCBC(byte[] buff ,byte[] keyiv) throws InvalidKeyException,  
                IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {  
            // 根據密鑰，對Cipher對像進行初始化，DECRYPT_MODE表示解密模式
        	//IvParameterSpec ips = new IvParameterSpec(keyiv);
        	//cipher.init(Cipher.DECRYPT_MODE, deskey ,ips);
            //cipherByte = cipher.doFinal(buff);
            try {
            	IvParameterSpec ips = new IvParameterSpec(keyiv);
                cipher.init(Cipher.DECRYPT_MODE, deskey ,ips);   
                byte[] resultBytes = cipher.doFinal(buff);
                return resultBytes;
            	
            	/*
            	IvParameterSpec ips = new IvParameterSpec(keyiv);
            	cipher.init(Cipher.DECRYPT_MODE, deskey ,ips);   
                int blockSize = cipher.getBlockSize();   
                ByteArrayOutputStream bout = new ByteArrayOutputStream(64);   
                int j = 0;   
                while (buff.length - j * blockSize > 0) {   
                    bout.write(cipher.doFinal(buff, j * blockSize, blockSize));   
                    j++;   
                } 
                return bout.toByteArray();
                */   
            } catch (Exception e) {   
                try {
					throw new Exception(e.getMessage());
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}   
            }           	
            return null;  
        }        
      
        /** 
         * @param args 
         * @throws NoSuchPaddingException  
         * @throws NoSuchAlgorithmException  
         * @throws BadPaddingException  
         * @throws IllegalBlockSizeException  
         * @throws InvalidKeyException  
         */  
        public static void main(String[] args) throws Exception {  
            String msg ="上海商業儲蓄銀行";
            
            EncrypDES3 desECB = new EncrypDES3();
            desECB.generateKey("DESede", "DESede/ECB/PKCS5Padding");            
            byte[] encontentECB = desECB.EncrytoECB(msg.getBytes());  
            byte[] decontentECB = desECB.DecrytoECB(encontentECB);  
            System.out.println("EBC明文是:" + msg);  
            System.out.println("EBC加密後:" + new String(encontentECB));  
            System.out.println("EBC解密後:" + new String(decontentECB));  

            EncrypDES3 desCBC = new EncrypDES3();
            desCBC.generateKey("DESede", "DESede/CBC/PKCS5Padding");  
            byte[] encontentCBC = desCBC.EncrytoCBC(msg.getBytes(),new byte[8]);  
              
            System.out.println("CBC明文是:" + msg);  
            System.out.println("CBC加密後:" + new String(encontentCBC));
            desCBC.SaveKey("D:\\encrypt\\fisc\\testdes3.key");
            EncrypDES3 desCBC2 = new EncrypDES3();
            desCBC2.LoadKey("D:\\encrypt\\fisc\\testdes3.key","DESede" ,"DESede/CBC/PKCS5Padding");
            byte[] decontentCBC = desCBC2.DecrytoCBC(encontentCBC,new byte[8]);
            System.out.println("CBC解密後:" + new String(decontentCBC));  
            
        }  
      
    }  