package com.scsb.listener;

import java.util.Enumeration;
import java.util.Properties;

import javax.servlet.*;

import com.scsb.domain.HashSystem;
import com.scsb.util.PropertiesUtil;

public class LoadHashTableListener implements ServletContextListener {
	
	static HashSystem hashTable = HashSystem.getInstance();
	
	public LoadHashTableListener() {
	}
	
	public void contextInitialized(ServletContextEvent sce) {
		System.out.println("hashtable Loading....begin");
		String path = sce.getServletContext().getRealPath("/");
		String hashTablePropertyFile = sce.getServletContext().getInitParameter("hashtable-file")+"";
		Properties hashTableProp = PropertiesUtil.loadProperties(path+hashTablePropertyFile);
		Enumeration enumeration = hashTableProp.propertyNames();
		do{
			if(!enumeration.hasMoreElements())	break;
			String hashProp = (String)enumeration.nextElement()+"";
			if (hashProp.indexOf("TABLE") > -1){
				try {
					System.out.println(hashProp+":"+hashTableProp.getProperty(hashProp));
					Class classTable = Class.forName(hashTableProp.getProperty(hashProp));
					classTable.getDeclaredMethod("getInstance", new Class[0]).invoke(null, new Object[0]);				
				}catch(Exception exception1){
					System.out.println(exception1);
				}
			}
		} while(true);
		System.out.println("hashtable Loading....end");
	}

	public void contextDestroyed(ServletContextEvent servletcontextevent) {
		ServletContext servletcontext = servletcontextevent.getServletContext();
		servletcontext.log("hashSystem shutdown.");
	}
}
