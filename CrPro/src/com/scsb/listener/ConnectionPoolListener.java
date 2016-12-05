package com.scsb.listener;

import java.sql.SQLException;
import java.util.Properties;

import javax.servlet.*;

import org.apache.log4j.Logger;
import org.vaadin.teemu.jsoncontainer.JsonContainer;

import com.scsb.util.IO;
import com.scsb.util.NamingService;
import com.scsb.util.StrUtil;
import com.scsb.db.ConnectionPool;
import com.scsb.domain.HashSystem;
import com.vaadin.data.Item;

public class ConnectionPoolListener implements ServletContextListener {
	
	static Logger logger = Logger.getLogger(ConnectionPoolListener.class.getName());
	static HashSystem hashSystem = HashSystem.getInstance();
	
	public ConnectionPoolListener() {
		logger.debug("初始化資料庫連線");
		System.out.println("初始化資料庫連線.....");
	}
	
	public void contextInitialized(ServletContextEvent sce) {
		NamingService namingservice = NamingService.getInstance();
		try {
			String path = sce.getServletContext().getRealPath("/");
			String systemFile = sce.getServletContext().getInitParameter("system-file")+"";
			//如果系統參數檔未載入,則重新載入
			if (hashSystem.getProperties() == null){
				hashSystem.resetHashSystem(path+systemFile);
			}
			Properties systemProps =hashSystem.getProperties();
			String jsonProps =IO.read(path+systemProps.getProperty("ConnectionPoolJson"));
			JsonContainer jsonData= JsonContainer.Factory.newInstance(jsonProps);		
			for(int i=0;i<jsonData.size();i++){
				
	        	Item item=jsonData.getItem(jsonData.getIdByIndex(i));
	        	String dbpool=(String)item.getItemProperty("DBPool").getValue();
	        	String dbtype=(String)item.getItemProperty("DBType").getValue();
	        	String running=(String)item.getItemProperty("Running").getValue();
	        	
				System.out.println(dbtype+"/"+dbpool+"...init...."+running);
				if (running.toUpperCase().equals("TRUE")){
					ConnectionPool connectinoPool = new ConnectionPool(dbtype+"/"+dbpool);
					namingservice.setAttribute(dbpool, connectinoPool);
				}
			}
		} catch (ClassNotFoundException e) {
			logger.fatal(StrUtil.convException(e));
		} catch (SQLException e) {
			logger.fatal(StrUtil.convException(e));
		}
	}

	public void contextDestroyed(ServletContextEvent servletcontextevent) {
		ServletContext servletcontext = servletcontextevent.getServletContext();
		NamingService namingservice = NamingService.getInstance();
		ConnectionPool connectionpool = (ConnectionPool) namingservice.getAttribute("connectionPool");
		connectionpool.shutdown();
		servletcontext.log("Connection pool shutdown.");
	}
}
