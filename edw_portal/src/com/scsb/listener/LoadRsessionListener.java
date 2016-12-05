package com.scsb.listener;

import java.util.Enumeration;
import java.util.Properties;

import javax.servlet.*;

import org.apache.log4j.PropertyConfigurator;

import com.scsb.domain.HashRsession;
import com.scsb.domain.HashSystem;
import com.scsb.util.PropertiesUtil;

public class LoadRsessionListener implements ServletContextListener {
	
	static HashRsession hashRsession= HashRsession.getInstance();
	
	public LoadRsessionListener() {
	}
	
	public void contextInitialized(ServletContextEvent sce) {
		System.out.println("rsession service Loading....begin");
		String path = sce.getServletContext().getRealPath("/");
		String rsessionPropertyFile = sce.getServletContext().getInitParameter("rsession-file")+"";
		Properties systemFileProp = PropertiesUtil.loadProperties(path+rsessionPropertyFile);
		if(systemFileProp != null){
			hashRsession.putProperties(systemFileProp);
			hashRsession.init();
		}
	}

	public void contextDestroyed(ServletContextEvent servletcontextevent) {
		ServletContext servletcontext = servletcontextevent.getServletContext();
		servletcontext.log("rsession service shutdown.");
	}
}
