package com.scsb.vaadin.server;

import java.io.*;
import java.net.*;
import java.util.Properties;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.vaadin.teemu.jsoncontainer.JsonContainer;

public class SasJsonService {
    private String SAS_CGI_BROKER = "http://10.10.2.120/cgi-bin/broker.exe?";
    private HttpClient httpClient = new HttpClient();
 
	
   URL url;
   public SasJsonService(){
	   
   }
   public static void main (String[] args) {
	   SasJsonService geturldoc=new SasJsonService();
       
	   Properties pp =new Properties();
	   pp.put("_service", "default4");
	   pp.put("_program", "ibspgm.json_test.sas");
	   pp.put("xpgn", "noCheck");

	   String jsonString =geturldoc.getSasJson(pp);
		try{
			JsonContainer jsonData = JsonContainer.Factory.newInstance(jsonString);
		 } catch (Exception msg) {
			 msg.printStackTrace();
		 }
   }
   
   public void setSasCgiBroker(String  sasCgiBroker){
	   this.SAS_CGI_BROKER=sasCgiBroker;
   }    
   
   /**
    * 
    * @param urlString
    * @return
    */
   public String getSasJson(Properties pp){
	   return getSasJson(pp , "UTF-8");
   }  
   /**
    * 
    * @param urlString
    * @param charsetName
    * @return
    */
   public String getSasJson(Properties pp ,String charsetName){
	   String jsonString =getUrl(pp ,charsetName);   
	   if (jsonString.length() > 0){
		   jsonString =jsonString.substring(jsonString.indexOf("["),jsonString.lastIndexOf("]")+1);
	   }
	   return jsonString;
   }
   /**
    * 
    * @param Properties
    * @return
    */
   private String getUrl(Properties pp){
	   return getUrl(pp , "UTF-8");
   }
   /**
    * 
    * @param Properties
    * @param charsetName
    * @return
    */
   private String getUrl(Properties PP ,String charsetName){ 
      String result ="";
      try {
    	  StringBuilder urlBuilder = new StringBuilder(SAS_CGI_BROKER);
    	  PostMethod post = new PostMethod(urlBuilder.toString());
    	  for (java.util.Iterator<Object> iter =PP.keySet().iterator();iter.hasNext();){
    		  String key =(String)iter.next();
    		  //urlBuilder.append("&"+key+"=").append(URLEncoder.encode(PP.getProperty(key),charsetName));
    		  post.setParameter(key, PP.getProperty(key));
    	  }
           httpClient.executeMethod(post);
           byte[] responseBody = post.getResponseBody();
           result =new String(responseBody,charsetName);
	   } catch (Exception e) {
	        e.printStackTrace();
	   }	   
      return result;
   }
} // end of class definition