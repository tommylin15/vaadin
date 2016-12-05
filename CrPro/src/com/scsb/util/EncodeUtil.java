package com.scsb.util;

public class EncodeUtil {
	/**
	 * SAS加密Key
	 * @return
	 */
    public static String getKeyString(){
  	  String vKey="0AA1BB2CC3DD4EE5";
	  String binarykey="";
	  for(int i=0;i<vKey.length();i++){
		 String bin4= Integer.toBinaryString(Integer.parseInt( vKey.substring(i,i+1), 16 ));
   		  int x=bin4.length();
		  for(int j=0;j<(4-x);j++){
			  bin4="0"+bin4;
		  }    		 
		 binarykey+=bin4;
	  }
	  return binarykey; 
    }
    /**
     * 將字串編成SAS加密 回傳
     * @param s
     * @return
     */
    public static String getEncodeSASString(String sValue) {
      if (sValue == null) return null;
	  byte[] encoded = sValue.getBytes();
	  String sBinary="";
	  for(int i=0;i<encoded.length;i++){
		  String bin2=Integer.toBinaryString(encoded[i]);
		  int x=bin2.length();
		  for(int j=0;j<(8-x);j++){
			  bin2="0"+bin2;
		  }
		  sBinary+=bin2; 
	  }
	  String binaryKey=getKeyString();
	  String rsString="";
	  int len=binaryKey.length();
	  if (sBinary.length()<binaryKey.length()) len=sBinary.length();
	  for(int i=0 ;i<len;i++){
		  if (sBinary.substring(i,i+1).equals(binaryKey.substring(i,i+1))){
			  rsString+="0";
		  }else{
			  rsString+="1";
		  }
	  }
	  String baseValue="";
	  for(int i=0;i<(rsString.length()/4);i++){
 		 String bin4= rsString.substring(i*4,(i+1)*4);
		 baseValue+=(Integer.toHexString(Integer.parseInt(bin4, 2))+"").toUpperCase();
 	  }
	  return baseValue;
    }
    /**
     * 將SAS加密字串解碼回傳
     * @param s
     * @return
     */
    public static String getDecodeSASString(String sValue) {
  	  String sBinary="";
	  for(int i=0;i<sValue.length();i++){
		 String bin4= Integer.toBinaryString(Integer.parseInt( sValue.substring(i,i+1), 16 ));
   		  int x=bin4.length();
		  for(int j=0;j<(4-x);j++){
			  bin4="0"+bin4;
		  }    		 
		  sBinary+=bin4;
	  }
	  String binaryKey=getKeyString();
	  String rsString="";
	  
	  int len=binaryKey.length();
	  if (sBinary.length()<binaryKey.length()) len=sBinary.length();
	  for(int i=0 ;i<len;i++){
		  if (sBinary.substring(i,i+1).equals(binaryKey.substring(i,i+1))){
			  rsString+="0";
		  }else{
			  rsString+="1";
		  }
	  }
	  String baseValue="";
	  byte[] bytecar =new byte[8];
	  for(int i=0;i<(rsString.length()/8);i++){
 		 String bin8= rsString.substring(i*8,(i+1)*8);
		 baseValue+=(char)Integer.parseInt(bin8,2);
 	  }
	  return baseValue;
    }    
    public static void main(String[] args) {
    	  //System.out.println(fString);
    	  String str="tommy083";
    	  String str64 = EncodeUtil.getEncodeSASString(str);
    	  System.out.println("str="+str);
    	  System.out.println("Base64 Encode str="+EncodeUtil.getEncodeSASString(str));
    	  System.out.println("Base64 Decode str="+EncodeUtil.getDecodeSASString(str64));

    	 }	
}

