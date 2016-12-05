package com.scsb.listener;

import javax.servlet.*;
import org.apache.log4j.PropertyConfigurator;

public class Log4JListener implements ServletContextListener {
	public Log4JListener() {
	}
	
	public void contextInitialized(ServletContextEvent sce) {
		String s =sce.getServletContext().getRealPath("/");
		String s1 =sce.getServletContext().getInitParameter("log4j-init-prop");
		if(s1 != null)
			PropertyConfigurator.configure((new StringBuilder()).append(s).append(s1).toString());
	}

	public void contextDestroyed(ServletContextEvent servletcontextevent) {
		ServletContext servletcontext = servletcontextevent.getServletContext();
		servletcontext.log("log4j shutdown.");
	}
}
