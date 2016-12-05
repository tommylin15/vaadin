package com.scsb.vaadin.ui.vc.temp;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

 
public class javaTest {
	 public javaTest() {
 		 
	 }
	 static public void main(String[] argv){
		 try {
	    		HttpClient httpClient = new HttpClient();
	    		PostMethod post = new PostMethod("http://10.10.2.196/Smart-Query/URLLogin.aspx");
	    		post.addParameter("Path", "EDW_MARKING");
	    		post.addParameter("UserName", "0034711");
	    		post.addParameter("password", "12345678");
	    		httpClient.executeMethod(post);
	    		byte[] responseBody = post.getResponseBody();
	    		String result =new String(responseBody,"utf-8");
	    		if (result.indexOf("索引鍵不能為 null") > -1){
	    			System.out.println("驗證失敗!!");
	    		}else{
	    			System.out.println("驗證成功");
		    		post = new PostMethod("http://10.10.2.196/Smart-Query/squery.aspx");
		    		post.addParameter("filename", "Q_NB_0001");
		    		post.addParameter("sys", "PMART_MARKETING");
		    		httpClient.executeMethod(post);
		    		byte[] responseBody2 = post.getResponseBody();
		    		String result2 =new String(responseBody2,"utf-8");		 
		    		System.out.println(result2);
	    		}
	    		
	    		//http://#ServerName/Smart-Query/URLLogin.aspx?Path=#ProjectName&UserName=#uid&password=pwd
	    		//GetMethod get = new GetMethod("http://localhost/");
	    		//httpClient.executeMethod(get);
	    		//InputStream inStream=get.getResponseBodyAsStream();
		        //byte[] responseBody = get.getResponseBody();
		        //String result =new String(responseBody,"big5"); 	 			 
		} catch (Exception e) {
			e.printStackTrace();
		}
       }
	 
}