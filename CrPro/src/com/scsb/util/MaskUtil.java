package com.scsb.util;

public abstract class MaskUtil {
	//															  0123456789
	private static String maskStr1 = "BDEFIKLNQV";
	private static String maskStr2 = "EDUSKNVYP";
	private static String maskStr3 = "ZXCVBNMIR";
	private static String maskStr4 = "QPWOEIRUT";
	
	/**
	 * 位元組陣列的合併 :借助位元組輸出流ByteArrayOutputStream來實現
	 * @param srcArrays
	 * @return
	 */
	public static String maskCode(String srcString) {
		return maskCode(srcString ,1) ;
	}
	public static String maskCode(String srcString ,int iMaskNum) {
		String maskString=srcString;
		String maskKey =maskStr1;
		if (iMaskNum == 2) maskKey =maskStr2;
		if (iMaskNum == 3) maskKey =maskStr3;
		if (iMaskNum == 4) maskKey =maskStr4;
		if (maskString != null){
			if (maskString.length() >=7){
				String sTemp=maskString.substring(7);
				sTemp=sTemp.replace("0", maskKey.substring(0,1));
				sTemp=sTemp.replace("1", maskKey.substring(1,2));
				sTemp=sTemp.replace("2", maskKey.substring(2,3));
				sTemp=sTemp.replace("3", maskKey.substring(3,4));
				sTemp=sTemp.replace("4", maskKey.substring(4,5));
				sTemp=sTemp.replace("5", maskKey.substring(5,6));
				sTemp=sTemp.replace("6", maskKey.substring(6,7));
				sTemp=sTemp.replace("7", maskKey.substring(7,8));
				sTemp=sTemp.replace("8", maskKey.substring(8,9));
				sTemp=sTemp.replace("9", maskKey.substring(9,10));
				maskString=maskString.substring(0,7)+sTemp;
			}
		}
		return maskString;
	}	
	public static String maskChina(String srcString ) {
		String maskString=srcString;
		if (maskString.length() >=2){
			maskString=maskString.substring(0,2)+"XXXXXXXX";
		}
		return maskString;
	}		
	
}    