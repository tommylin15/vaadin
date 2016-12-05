package com.scsb.scsbCode;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteOrder;
import java.util.Vector;

public abstract class ByteUtil {
	private static String hexStr = "0123456789ABCDEF";
	/**
	 * 位元組陣列的合併 :借助位元組輸出流ByteArrayOutputStream來實現
	 * @param srcArrays
	 * @return
	 */
	public static byte[] streamCopy(Vector<byte[]> srcArrays) {
		byte[] destAray = null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			for (byte[] srcArray:srcArrays) {
				bos.write(srcArray);
			}
			bos.flush();
			destAray = bos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bos.close();
			} catch (IOException e) {
			}
		}
		return destAray;
	}	
	
	/**
	 * 將Byte[] 轉成 String
	 * @param binByte
	 * @return
	 */
    public static String BinstrToStr(byte[] binByte) {
		StringBuilder binary = new StringBuilder();
	    for (byte b : binByte){
	    	int val = b;
	        for (int i = 0; i < 8; i++){
	           binary.append((val & 128) == 0 ? 0 : 1);
	        val <<= 1;
	           }
	    }
	    return binary.toString();
	}	
    
    /**
     * 切Byte[]
     * @param srcByte
     * @param iBeningIndex
     * @param iEndIndex
     * @return
     */
    public static byte[] getByteArrayData(byte[] srcByte ,int iBeningIndex ,int iEndIndex) {
    	byte[] newByte=new byte[iEndIndex-iBeningIndex];
    	int iLen=0;
    	for (int i=iBeningIndex ;i<iEndIndex ;i++) {
    		newByte[iLen]=srcByte[i];
    		iLen++;
    	}    	
    	return newByte;
    }
    /**
     * Binary 轉16進位
     * @param bytes
     * @return
     */
    public static String BinaryToHexString(byte[] bytes){
    	String result = "";
    	String hex = "";
    	for(int i=0;i<bytes.length;i++){
	    	hex = String.valueOf(hexStr.charAt((bytes[i]&0xF0)>>4));
	    	hex += String.valueOf(hexStr.charAt(bytes[i]&0x0F));
	    	result +=hex;
    	}
    	return result;
    }
    /**
     * 16進位 to Binary
     * @param hexString
     * @return
     */
    public static byte[] HexStringToBinary(String hexString){
    	//hexString的長度對2取整，作為bytes的長度
    	int len = hexString.length()/2;
    	byte[] bytes = new byte[len];
    	byte high = 0;//位元組高四位
    	byte low = 0;//位元組低四位
    	for(int i=0;i<len;i++){
    		//右移四位得到高位
    		high = (byte)((hexStr.indexOf(hexString.charAt(2*i)))<<4);
    		low = (byte)hexStr.indexOf(hexString.charAt(2*i+1));
    		bytes[i] = (byte) (high|low);//高地位做或運算
    	}
    	return bytes;
    }    
    
    /**
     * 將一般字串轉成Binary字串
     * @param s
     * @return
     */
    public static String StrToBinstr(String s){   	
        byte[] bytes = s.getBytes();
        StringBuffer binary = new StringBuffer();
        for (byte b : bytes){
             int val = b;               
	             for (int i = 0; i < 8; i++){
	                binary.append((val & 128) == 0 ? 0 : 1);
	                val <<= 1;
	             }
          }
        return binary.toString();
    }
    
    /**
     * 將Btye 轉成 bits String
     * @param inByte
     * @return
     */
	public static String getBits( byte inByte ){
        // Go through each bit with a mask
        StringBuilder builder = new StringBuilder();
        for ( int j = 0; j < 8; j++ )
        {
            // Shift each bit by 1 starting at zero shift
            byte tmp =  (byte) ( inByte >> j );

            // Check byte with mask 00000001 for LSB
            int expect1 = tmp & 0x01; 

            builder.append(expect1);
        }
        return ( builder.reverse().toString() );
    }    
    
	/**
	 * Integer to Byte Array
	 * @param value
	 * @return
	 */
	public static byte[] convertToBytes(int value) {
		return convertToBytes(value ,4 ,null);
	}
    public static byte[] convertToBytes(int value ,int iByteLen, ByteOrder order) {                    
        byte[] byteArray = new byte[iByteLen];
        int shift = 0;
        for (int i = 0; i < byteArray.length; i++) {
 
            if (order == ByteOrder.BIG_ENDIAN)
                shift = (byteArray.length - 1 - i) * 8; // 24, 16, 8, 0
            else
                shift = i * 8; // 0,8,16,24
 
            byteArray[i] = (byte) (value >>> shift);
        }
        return byteArray;
        
    }	
    
	public static void main(String[] args){
		String xxx="a";
		System.out.println(xxx);
		byte[] bbb=xxx.getBytes();
		String ccc=ByteUtil.getBits(bbb[0]);
		System.out.println(ccc);
	}    
}    