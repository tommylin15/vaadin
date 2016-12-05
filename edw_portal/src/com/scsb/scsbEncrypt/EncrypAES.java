package com.scsb.scsbEncrypt;

    import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;  
    import java.security.NoSuchAlgorithmException;  
    import java.security.Security;  
import java.security.spec.PKCS8EncodedKeySpec;
      
    import javax.crypto.BadPaddingException;  
    import javax.crypto.Cipher;  
    import javax.crypto.IllegalBlockSizeException;  
    import javax.crypto.KeyGenerator;  
    import javax.crypto.NoSuchPaddingException;  
import javax.crypto.SecretKey;  
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
      
    public class EncrypAES {  
          
        //KeyGenerator 提供對稱密鑰生成器的功能，支持各種算法  
        private KeyGenerator keygen;  
        //SecretKey 負責保存對稱密鑰  
        private SecretKey aeskey;  
        //Cipher負責完成加密或解密工作  
        private Cipher cipher;  
        //該字節數組負責保存加密的結果  
        private byte[] cipherByte;  
          
        public EncrypAES(){
        }
        public void generateKey(String KEY_ALGORITHM ,String DEFAULT_CIPHER_ALGORITHM)
        	throws NoSuchAlgorithmException, NoSuchPaddingException{
        	
            Security.addProvider(new com.sun.crypto.provider.SunJCE());
            //實例化支持DES算法的密鑰生成器(算法名稱命名需按規定，否則拋出異常)
            keygen = KeyGenerator.getInstance(KEY_ALGORITHM);
            //keygen = KeyGenerator.getInstance("AES");
            keygen.init(256);
            //生成密鑰  
            aeskey = keygen.generateKey();
            //生成Cipher對像,指定其支持的DES算法
            cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            //cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        } 
        
        public SecretKey  getSecretKey(){
       	 	return aeskey;
        }         
          
        /**
         * ReGenerate SecretKey
         * @param key
         * @param KEY_ALGORITHM :AES
         * @param DEFAULT_CIPHER_ALGORITHM :"AES/CBC/PKCS7Padding"
         * @throws Exception
         */
        public void toKey(byte[] key ,String KEY_ALGORITHM ,String DEFAULT_CIPHER_ALGORITHM) throws Exception{
        	cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
        	SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
            aeskey = new SecretKeySpec(key, "AES");
        }        
        
        /** 
         * 對字符串加密 
         *  
         * @param arrData 
         * @return 
         * @throws InvalidKeyException 
         * @throws IllegalBlockSizeException 
         * @throws BadPaddingException 
         */  
        public byte[] EncrytoECB(byte[] src ) throws InvalidKeyException,  
        IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {  
		    // 根據密鑰，對Cipher對像進行初始化，ENCRYPT_MODE表示加密模式
			//IvParameterSpec ips = new IvParameterSpec(keyiv);
			cipher.init(Cipher.ENCRYPT_MODE, aeskey );
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
        	cipher.init(Cipher.ENCRYPT_MODE, aeskey ,ips);
            //byte[] src = str.getBytes();  
            // 加密，結果保存進cipherByte
            cipherByte = cipher.doFinal(src);  
            return cipherByte;  
        }             

        /** 
         * 對字符串解密 
         * @param buff 
         * @return 
         * @throws InvalidKeyException 
         * @throws IllegalBlockSizeException 
         * @throws BadPaddingException 
         */       
        public byte[] DecrytoECB(byte[] buff) throws Exception {
        	cipher.init(Cipher.DECRYPT_MODE, aeskey);  
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
            try {
            	IvParameterSpec ips = new IvParameterSpec(keyiv);
                cipher.init(Cipher.DECRYPT_MODE, aeskey ,ips);   
                byte[] resultBytes = cipher.doFinal(buff);
                return resultBytes;
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
            EncrypAES de1 = new EncrypAES();  
            de1.generateKey("AES", "AES/ECB/PKCS7Padding");
            String msg ="上海商業儲蓄銀行";  
            byte[] encontent = de1.EncrytoECB(msg.getBytes());  
            byte[] decontent = de1.DecrytoECB(encontent);  
            System.out.println("明文是:" + msg);  
            System.out.println("加密後:" + new String(encontent));  
            System.out.println("解密後:" + new String(decontent));
        }  
      
    }  