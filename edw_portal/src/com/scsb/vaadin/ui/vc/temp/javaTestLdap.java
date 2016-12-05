package com.scsb.vaadin.ui.vc.temp;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.endpoint.dynamic.DynamicClientFactory;

 
public class javaTestLdap {
	 public javaTestLdap() {
 		 
	 }
	 static public void main(String[] argv) throws Exception{
		 DynamicClientFactory dcf = DynamicClientFactory.newInstance();
		 Client client = dcf.createClient("http://10.10.2.131/ldapService.do?wsdl");
		 Object[] res = client.invoke("authUser", new String[]{"34711","scsb34711"});
		 System.out.println("result: " + res[0]);
				
     }
	 
}