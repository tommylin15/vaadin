package com.scsb.listener;

import java.util.Properties;

import javax.servlet.*;

import org.apache.log4j.PropertyConfigurator;

import com.scsb.domain.HashSystem;
import com.scsb.util.PropertiesUtil;

public class LoadPropertyListener implements ServletContextListener {
	
	static HashSystem hashSystem = HashSystem.getInstance();
	
	public LoadPropertyListener() {
	}
	
	public void contextInitialized(ServletContextEvent sce) {
		System.out.println("共通屬性預先載入程式....begin");
		String path = sce.getServletContext().getRealPath("/");
		String systemFile = sce.getServletContext().getInitParameter("system-file")+"";
		hashSystem.resetHashSystem(path+systemFile);
	}

	public void contextDestroyed(ServletContextEvent servletcontextevent) {
		ServletContext servletcontext = servletcontextevent.getServletContext();
		servletcontext.log("hashSystem shutdown.");
	}
}
