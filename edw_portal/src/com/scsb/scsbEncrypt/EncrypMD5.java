package com.scsb.scsbEncrypt;

import java.security.MessageDigest;  
import java.security.NoSuchAlgorithmException;  
  
public class EncrypMD5 {  
      
    public byte[] eccrypt(String info) throws NoSuchAlgorithmException{  
        //根據MD5算法生成MessageDigest對像  
        MessageDigest md5 = MessageDigest.getInstance("MD5");  
        byte[] srcBytes = info.getBytes();  
        //使用srcBytes更新摘要  
        md5.update(srcBytes);  
        //完成哈希計算，得到result  
        byte[] resultBytes = md5.digest();  
        return resultBytes;  
    }  
      
      
    public static void main(String args[]) throws NoSuchAlgorithmException{  
        String msg = "上海商業儲蓄銀行";  
        EncrypMD5 md5 = new EncrypMD5();  
        byte[] resultBytes = md5.eccrypt(msg);  
        System.out.println("密文是：" + new String(resultBytes));  
        System.out.println("明文是：" + msg);  
    }  
  
} 