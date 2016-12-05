package com.scsb.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

public class StrUtil {
	private static final String NEWLINE = System.getProperty("line.separator");
	/**
	 * 判斷是否為空值或是NULL
	 * @param inputText
	 * @return
	 */
	public static boolean isNull(String inputText) {
		boolean result = false;
		if (inputText == null || inputText.trim().length() == 0 || inputText.trim().equalsIgnoreCase("null"))
			result = true;
		return result;
	}
	
	/**
	 * 判斷是否為非空值且不是NULL
	 * @param inputText
	 * @return
	 */
	public static boolean isNotNull(String inputText) {
		boolean result = false;
		if (inputText != null && inputText.trim().length() > 0 && !inputText.trim().equalsIgnoreCase("null"))
			result = true;
		return result;
	}	
	
	public static String objToStr(Object obj) {
		String result = "";
		try {
			if (isNull("" + obj))
				result = "";
			else
				result = (String)obj;
		} catch (Exception e) {
			result = "";
		}
		return result;
	}
	
	public static String intToStr(int value) {
		String result = "";
		try {
			result = String.valueOf(value);
		} catch (Exception e) {
			result = "";
		}
		return result;
	}	

	/**
	 * 將String Array轉成以token區分的String
	 * @param values
	 * @param token
	 * @return
	 */	
	public static String convAryToStr(Object[] values, String token){
		String result = null;
		if (values==null) return result;
		for (int i=0; i<values.length; i++){
			if (i==0)
				result = values[i]+"";
			else 
				result = result.concat(token).concat(values[i]+"");
		}
		return result;
	}
	
	/**
	 * 重複字串
	 * @param str 原始字串
	 * @param times 重複次數
	 * @param trimend 是否去除最後一個字元
	 * @return
	 */	
	public static String repeat(String str, int times, boolean trimend){
		String result = "";
		for (int i=0; i<times; i++){
			result = result.concat(str);
		}
		if (trimend) { 
			result = result.substring(0, result.length()-1);
		}
		return result;
	}

	/**
	 * 再傳入字串的右邊補滿指定字元
	 * @param s
	 * @param padstr
	 * @param pad
	 * @return
	 */	
	public static String rpad(String s,String padstr, int pad) {
		StringBuffer a = new StringBuffer(pad);
		String rtn = s;
		if (s.length()<pad){
			for (int i = 0; i < pad; i++) 
				a = a.append(padstr);
			rtn = s + a.substring(0,pad-s.length());
		}
		return rtn;
	}
	

	/**
	 * 再傳入字串的左邊補滿指定字元
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
	
	
    public static String convDateToStr(Date value, String format) {
		String rtnVal = "";
		if (value==null) return rtnVal;
		
		SimpleDateFormat df = new SimpleDateFormat(format);
		//df.setTimeZone(TimeZone.getTimeZone("GMT"));
		try {
			rtnVal = df.format(value);
		} catch (Exception e) {
			e.printStackTrace();
			rtnVal = null;
		}		
		return rtnVal;
	}
    
    public static String toUpperCaseFirstWord(String str){
    	String rtnVal = str;
    	if (isNotNull(str)) {
    		rtnVal = str.substring(0,1).toUpperCase()+str.substring(1);
    	}
    	return rtnVal;    	
    }
    
    /**
     * 比較兩數字字串,是否value1>value2
     * @param value1
     * @param value2
     * @return
     */
    public static boolean compare(String value1, String value2){
    	return compare(value1,">",value2);
    }
    
    public static boolean compare(String value1, String symbol, String value2){
    	boolean result = false;
    	String v1 = value1;
    	String v2 = value2;
    	if (StrUtil.isNull(value1)) v1 = "0";
    	if (StrUtil.isNull(value1)) v2 = "0";    	
    	float fV1 = Float.parseFloat(v1);
    	float fV2 = Float.parseFloat(v2);
    	if (symbol.equals(">")) { 
    		if (fV1>fV2) result = true;
    	} else if (symbol.equals(">=")) { 
    		if (fV1>=fV2) result = true;
    	} else if (symbol.equals("=")) { 
    		if (fV1==fV2) result = true;
    	} else if (symbol.equals("<=")) { 
    		if (fV1<=fV2) result = true;
    	} else if (symbol.equals("<")) { 
    		if (fV1<fV2) result = true;
    	} 
    	return result;
    } 

    public static String getFmtValue(float fValue){
    	String result = "";
    	result = objToStr(Math.round(fValue));
    	return result;
    }
    
	/**
	 * 將字串合併在一起.用token連接
	 * @param str1
	 * @param str2
	 * @param token
	 * @return
	 */
	public static String concat(String str1, String str2, String token){
		String result = "";
		if (isNull(str1)) {
			if (isNull(str2)) {
				result = "";
			} else {
				result = str2;
			}
		} else if (isNotNull(str1)) {
			if (isNull(str2)) {
				result = str1;
			} else {
				result = str1.concat(token).concat(str2);
			}
		}
		return result;
	}
	
	/**
	 * convertStringToInteger 轉換字串為整數值
	 * @param value
	 * @return
	 */
	public static int cSI(String value){
		if (isNotNull(value))
			return Integer.parseInt(value);
		else 
			return 0; 
	}
	
	/**
	 * convertStringToFloat 轉換字串為浮點數
	 * @param value
	 * @return
	 */
	public static float cSF(String value){
		if (isNotNull(value))
			return Float.parseFloat(value);
		else 
			return 0; 
	}
	
	public static String convException(Exception e){		
		return e.toString()+ NEWLINE + convAryToStr(e.getStackTrace(),NEWLINE);
	}
	
	/**
	 * Random character string. 
	 * @param length
	 */
	public static String getRandomString(int length) {
		Random rand = new Random((new GregorianCalendar()).getTimeInMillis());
		String chars = "abcdefghijklmnopqrstuvwxyz1234567890";
		
		char[] text = new char[length];
		for (int i = 0; i < length; i++) {
			text[i] = chars.charAt(rand.nextInt(chars.length()));
		}
		return new String(text);
	}	
    
    public static void main(String[] args){

    }
	
}