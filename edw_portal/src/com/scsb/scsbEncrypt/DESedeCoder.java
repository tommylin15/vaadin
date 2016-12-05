package com.scsb.scsbEncrypt;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.Key;  
import java.security.MessageDigest;
      
import javax.crypto.Cipher;  
import javax.crypto.KeyGenerator;  
import javax.crypto.SecretKey;  
import javax.crypto.SecretKeyFactory;  
import javax.crypto.spec.DESedeKeySpec;  

import org.bouncycastle.util.encoders.Hex;

/** 
 * DESede Coder<br/> 
 * secret key length:   112/168 bit, default:   168 bit<br/> 
 * mode:    ECB/CBC/PCBC/CTR/CTS/CFB/CFB8 to CFB128/OFB/OBF8 to OFB128<br/> 
 * padding: Nopadding/PKCS5Padding/ISO10126Padding/ 
 * @author Tommy Lin 
 *  
 */  
public class DESedeCoder {  
      
    /** 
     * 金鑰演算法 
    */  
    private static final String KEY_ALGORITHM = "DESede";  
    private static final String DEFAULT_CIPHER_ALGORITHM = "DESede/CBC/PKCS7Padding";  
    //private static final String DEFAULT_CIPHER_ALGORITHM = "DESede/ECB/ISO10126Padding";  
      
    /** 
     * 初始化金鑰 
     *  
     * @return byte[] 金鑰  
     * @throws Exception 
     */  
    public static byte[] initSecretKey() throws Exception{  
        //返回生成指定演算法的金鑰的 KeyGenerator   
        KeyGenerator kg = KeyGenerator.getInstance(KEY_ALGORITHM);  
        //初始化此金鑰生成器，使其具有確定的金鑰大小  
        kg.init(168);  
        //生成一個金鑰  
        SecretKey  secretKey = kg.generateKey();  
        return secretKey.getEncoded();  
    }  
     
    /** 
     * 轉換金鑰 
     *  
     * @param key   二進制金鑰 
     * @return Key  金鑰 
     * @throws Exception 
     */  
    private static Key toKey(byte[] key) throws Exception{  
        //實例化DES金鑰規則  
        DESedeKeySpec dks = new DESedeKeySpec(key);  
        //實例化金鑰工廠  
        SecretKeyFactory skf = SecretKeyFactory.getInstance(KEY_ALGORITHM);  
        //生成金鑰  
        SecretKey  secretKey = skf.generateSecret(dks);  
        return secretKey;  
    }  
      
    /** 
     * 加密 
     *  
     * @param data  待加密資料 
     * @param key   金鑰 
     * @return byte[]   加密資料 
     * @throws Exception 
     */  
    public static byte[] encrypt(byte[] data,Key key) throws Exception{  
        return encrypt(data, key,DEFAULT_CIPHER_ALGORITHM);  
    }  
      
    /** 
     * 加密 
     *  
     * @param data  待加密資料 
     * @param key   二進制金鑰 
     * @return byte[]   加密資料
     * @throws Exception 
     */  
    public static byte[] encrypt(byte[] data,byte[] key) throws Exception{  
        return encrypt(data, key,DEFAULT_CIPHER_ALGORITHM);  
    }  
      
      
    /** 
     * 加密 
     *  
     * @param data  待加密資料 
     * @param key   二進制金鑰 
     * @param cipherAlgorithm   加密算法/工作模式/填充方式 
     * @return byte[]   加密資料 
     * @throws Exception 
     */  
    public static byte[] encrypt(byte[] data,byte[] key,String cipherAlgorithm) throws Exception{  
        //還原金鑰  
        Key k = toKey(key);  
        return encrypt(data, k, cipherAlgorithm);  
    }  
      
    /** 
     * 加密 
     *  
     * @param data  待加密資料 
     * @param key   金鑰 
     * @param cipherAlgorithm   加密算法/工作模式/填充方式 
     * @return byte[]   加密資料 
     * @throws Exception 
     */  
    public static byte[] encrypt(byte[] data,Key key,String cipherAlgorithm) throws Exception{  
        //實例化  
        Cipher cipher = Cipher.getInstance(cipherAlgorithm);  
        //使用金鑰初始化，設置為加密模式  
        cipher.init(Cipher.ENCRYPT_MODE, key);  
        //執行操作  
        return cipher.doFinal(data);  
    }  
      
      
      
    /** 
     * 解密 
     *  
     * @param data  待解密資料 
     * @param key   二進制金鑰 
     * @return byte[]   解密資料 
     * @throws Exception 
     */  
    public static byte[] decrypt(byte[] data,byte[] key) throws Exception{  
        return decrypt(data, key,DEFAULT_CIPHER_ALGORITHM);  
    }  
      
    /** 
     * 解密 
     *  
     * @param data  待解密資料 
     * @param key   金鑰 
     * @return byte[]   解密資料 
     * @throws Exception 
     */  
    public static byte[] decrypt(byte[] data,Key key) throws Exception{  
        return decrypt(data, key,DEFAULT_CIPHER_ALGORITHM);  
    }  
      
    /** 
     * 解密 
     *  
     * @param data  待解密資料 
     * @param key   二進制金鑰 
     * @param cipherAlgorithm   加密算法/工作模式/填充方式 
     * @return byte[]   解密資料 
     * @throws Exception 
     */  
    public static byte[] decrypt(byte[] data,byte[] key,String cipherAlgorithm) throws Exception{  
        //還原金鑰  
        Key k = toKey(key);  
        return decrypt(data, k, cipherAlgorithm);  
    }  
  
    /** 
     * 解密 
     *  
     * @param data  待解密資料 
     * @param key   金鑰 
     * @param cipherAlgorithm   加密算法/工作模式/填充方式 
     * @return byte[]   解密資料 
     * @throws Exception 
     */  
    public static byte[] decrypt(byte[] data,Key key,String cipherAlgorithm) throws Exception{  
        //實例化  
        Cipher cipher = Cipher.getInstance(cipherAlgorithm);  
        //使用金鑰初始化，設置為解密模式  
        cipher.init(Cipher.DECRYPT_MODE, key);  
        //執行操作  
        return cipher.doFinal(data);  
    }  
      
    private static String  showByteArray(byte[] data){  
        if(null == data){  
            return null;  
        }  
        StringBuilder sb = new StringBuilder("{");  
        for(byte b:data){  
            sb.append(b).append(",");  
        }  
        sb.deleteCharAt(sb.length()-1);  
        sb.append("}");  
        return sb.toString();  
    }  
    
    /**
     * 
     * @param args [key ,input file ,output file ,mode:'enc'/'dec']
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
/*    	
        byte[] key = initSecretKey();
    	Key k = toKey(key);
    	System.out.println("key："+ showByteArray(key));
    	String data ="123456789";
    	
        System.out.println("加密前資料: string:"+data);  
        System.out.println("加密前資料: byte[]:"+showByteArray(data.getBytes()));  
        System.out.println();  
        byte[] encryptData = encrypt(data.getBytes(), k);  
        System.out.println("加密后資料: byte[]:"+showByteArray(encryptData));  
        System.out.println("加密后資料: hexStr:"+Hex.encode(encryptData));  
        System.out.println();  
        byte[] decryptData = decrypt(encryptData, k);  
        System.out.println("解密后資料: byte[]:"+showByteArray(decryptData));  
        System.out.println("解密后資料: string:"+new String(decryptData));
        
              	
*/    	  	

        File file1 = new File( args[0] );
        FileInputStream fis = new FileInputStream( new File( args[0] ) );
        int fileLength = (int) file1.length();
        byte[] keyBytes = new byte[ fileLength ];      
        fis.read(keyBytes);
        fis.close();
        Key k = toKey(keyBytes);
        
        File file2 = new File( args[1] );
        FileInputStream fis2 = new FileInputStream( new File( args[1] ) );
        int file2Length = (int) file2.length();
        byte[] dataBytes = new byte[ file2Length ];      
        fis2.read(dataBytes);
        fis2.close();
        
        String mode =args[3];
        if (mode.equals("enc")){
            //System.out.println("加密前資料: string:"+dataBytes.toString());  
            //System.out.println("加密前資料: byte[]:"+showByteArray(dataBytes));  
            //System.out.println();  
            byte[] encryptData = encrypt(dataBytes, k);  
            //System.out.println("加密后資料: byte[]:"+showByteArray(encryptData));  
            //System.out.println("加密后資料: hexStr:"+Hex.encode(encryptData));  
            FileOutputStream fos = new FileOutputStream( new File( args[2] ) );
            fos.write(encryptData);
            fos.close();                      	
        }
        if (mode.equals("dec")){
            //System.out.println("解密前資料: byte[]:"+showByteArray(dataBytes));  
            //System.out.println("解密前資料: hexStr:"+Hex.encode(dataBytes));  
            //System.out.println();          	
            byte[] decryptData = decrypt(dataBytes, k);  
            //System.out.println("解密后資料: byte[]:"+showByteArray(decryptData));  
            //System.out.println("解密后資料: string:"+new String(decryptData));
            FileOutputStream fos = new FileOutputStream( new File( args[2] ) );
            fos.write(decryptData);
            fos.close();            
        }
        
    }  
}  