package com.scsb.vaadin.ui.rc.temp;

import com.sas.net.connect.*;

import java.nio.charset.Charset;
import java.util.*;
 
public class JConnect {
	 public JConnect() {
	 }
	 static public void main(String[] argv){
		 String sValue =" data z01;"
		 		+ " label"
		 		+ "  date='徵'"
		 		+ "run;"
		 		+ "";
		 
		 Charset charset = Charset.forName("UTF-8");
		 
		 String sTemp=sValue;
		 String sflag1="/*";
		 String sflag2="*/";
		 int iflag1 =sTemp.indexOf(sflag1);
		 int iflag2 =sTemp.indexOf(sflag2);
		 while (iflag1 > -1 & iflag2 > -1 & iflag1 < iflag2){
			 sTemp =sTemp.substring(0 ,iflag1)+sTemp.substring(iflag2+sflag2.length());
			 
			 iflag1 =sTemp.indexOf(sflag1);
			 iflag2 =sTemp.indexOf(sflag2);
		 }
		 sValue=new String(sTemp.getBytes() ,charset) ;
//System.out.println(sValue);		 
		 		 
		 
		 
		Properties info = new Properties();
		info.setProperty("prompt1", "Username:");
		info.setProperty("response1", "ccdbadmin");
		info.setProperty("prompt2", "Password:");
		info.setProperty("response2", "Thankgive11");
		info.setProperty("prompt3", "Hello>");
		info.setProperty("response3", "sas -nosyntaxcheck  -config c:\\PROGRA~1\\SASHome\\SASFoundation\\9.4\\nls\\zt\\sasv9_web.cfg ");
		
		//info.setProperty("response3", "sas");
		info.setProperty("sasPortTag", "PORT=");
		
                try {
                        TelnetConnectClient client=new TelnetConnectClient(info);
                        System.out.println("getTextTransportFormat1="+client.getTextTransportFormat());
                        client.connect("10.10.2.120",23); 
                        System.out.println("getTextTransportFormat2="+client.getTextTransportFormat());
                        int ver1=client.getMajorSasVersion();
                        int ver2=client.getMinorSasVersion();
                        String driver=client.getDriverName();
                        System.out.println(driver);
                        client.rsubmit(sValue);
                        String lines=client.getEditLines();
                        String logs=client.getLogLines();
                        System.out.println(client.isConnected()+driver+lines+ver1+ver2+logs);
                        client.disconnect();
                } catch (ConnectException e) {
                        e.printStackTrace();
                }
                return;
         
       }
}