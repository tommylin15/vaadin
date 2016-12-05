package com.scsb.scsbEncrypt;

import java.security.MessageDigest;  
import java.security.NoSuchAlgorithmException;  
import java.util.Random;
import java.util.UUID;

import org.apache.commons.math3.random.RandomData;

import com.linkway.isecurity.Base64;

public class EncrypSHA {  
    public byte[] eccrypt(String info) throws NoSuchAlgorithmException{  
        MessageDigest md5 = MessageDigest.getInstance("SHA-256");  
        byte[] srcBytes = info.getBytes();  
        //使用srcBytes更新摘要  
        md5.update(srcBytes);  
        //完成哈希計算，得到result  
        byte[] resultBytes = md5.digest();
        return resultBytes;  
    }  
    public byte[] eccrypt(byte[] srcBytes) throws NoSuchAlgorithmException{  
        MessageDigest md5 = MessageDigest.getInstance("SHA-256");   
        //使用srcBytes更新摘要  
        md5.update(srcBytes);  
        //完成哈希計算，得到result  
        byte[] resultBytes = md5.digest();
        return resultBytes;  
    }  
    
  
    /** 
     * @param args 
     * @throws NoSuchAlgorithmException  
     */  
    public static void main(String[] args) throws NoSuchAlgorithmException {  
        String msg = "1234567890123456";
        UUID uuid = UUID.randomUUID();
        msg=uuid.toString();
        System.out.println("明文是：" + msg);
        
        EncrypSHA sha = new EncrypSHA();  
        byte[] resultBytes = sha.eccrypt(msg);
        
        String newstr =new String(resultBytes);
        System.out.println("SHA-256是：" +newstr );  
        
        Base64 base64 = new Base64();
		byte[] textByte =resultBytes;
		String encodedText = base64.encode(textByte);
		System.out.println("SHA-256->Base64是："+encodedText);
		
		
		
    }  
} 