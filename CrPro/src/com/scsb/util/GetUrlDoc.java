package com.scsb.util;

import java.io.*;
import java.net.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.vaadin.teemu.jsoncontainer.JsonContainer;
 
public class GetUrlDoc {
 
   URL url;
   public GetUrlDoc(){
	   
   }
   public static void main (String[] args) {
	   GetUrlDoc geturldoc=new GetUrlDoc();
	   String jsonString =geturldoc.getSasJson("http://10.10.2.120/cgi-bin/broker.exe?_service=default4&_program=ibspgm.json_test.sas&xpgn=noCheck");
	   System.out.println("jsonString:"+jsonString);
		try{
			JsonContainer jsonData = JsonContainer.Factory.newInstance(jsonString);
		 } catch (Exception msg) {
			 msg.printStackTrace();
		 }
	   
   }
   /**
    * 
    * @param urlString
    * @return
    */
   public String getSasJson(String urlString){
	   return getSasJson(urlString , "UTF-8");
   }  
   /**
    * 
    * @param urlString
    * @param charsetName
    * @return
    */
   public String getSasJson(String urlString ,String charsetName){
	   String jsonString =getUrl(urlString ,charsetName);
	   jsonString =jsonString.substring(jsonString.indexOf("["),jsonString.lastIndexOf("]")+1);
	   return jsonString;
   }
   /**
    * 
    * @param urlString
    * @return
    */
   public String getUrl(String urlString){
	   return getUrl(urlString , "UTF-8");
   }
   /**
    * 
    * @param urlString
    * @param charsetName
    * @return
    */
   public String getUrl(String urlString ,String charsetName){ 
      String rtnStr ="";
      try {
   	    url = new URL(urlString);
   	    URLConnection conn = url.openConnection();
   	    //DataInputStream in = new DataInputStream (conn.getInputStream()) ;
   	    BufferedReader input = new BufferedReader(
             new InputStreamReader(conn.getInputStream(),charsetName)); 
   	    //BufferedReader d = new BufferedReader(new InputStreamReader(in),"UTF-8"));
   	    while(input.ready()){
   	    	rtnStr+=input.readLine();
   	    }
      } catch (MalformedURLException mue) {
         System.out.println("Ouch - a MalformedURLException happened.");
         mue.printStackTrace();
         System.exit(1);
      } catch (IOException ioe) {
         System.out.println("Oops- an IOException happened.");
         ioe.printStackTrace();
         System.exit(1);
      } finally {
      } // end of 'finally' clause
      return rtnStr;
   }  // end of getUrl
} // end of class definition