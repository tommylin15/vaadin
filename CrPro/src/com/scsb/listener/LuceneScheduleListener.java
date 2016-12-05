package com.scsb.listener;

import java.util.Calendar;
import java.util.Properties;
import java.util.Timer;

import javax.servlet.*;

import org.apache.log4j.PropertyConfigurator;

import com.scsb.domain.HashLucene;
import com.scsb.domain.HashSystem;
import com.scsb.schedule.LuceneIndex;
import com.scsb.util.PropertiesUtil;

public class LuceneScheduleListener implements ServletContextListener {
	
	static HashLucene hashLucene = HashLucene.getInstance();	
    private final Timer timer = new Timer();
	
	public LuceneScheduleListener() {
	}
	
	public void contextInitialized(ServletContextEvent sce) {
		  String path = sce.getServletContext().getRealPath("/");
		  System.setProperty("schedule.root", path);
		  String luceneIni = sce.getServletContext().getInitParameter("lucene-file")+"";
		  Properties luceneFileProp = PropertiesUtil.loadProperties(path+luceneIni);
			if(luceneFileProp != null){
				hashLucene.putProperties(luceneFileProp);
			}   
		      //lucene index 啟動時間
			
			  String lucene_hh_mm_dd =luceneFileProp.getProperty("index_run_time").trim();
		      Calendar c = Calendar.getInstance(); 
		      c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(lucene_hh_mm_dd.substring(0,2)));  
		      c.set(Calendar.MINUTE, Integer.parseInt(lucene_hh_mm_dd.substring(3,5)));  
		      c.set(Calendar.SECOND, Integer.parseInt(lucene_hh_mm_dd.substring(6,8)));  
		      //啟動建立索引..
		      System.out.println("Lucene排程啟動....begin:"+c.getTime());      
		      timer.schedule(new LuceneIndex(), c.getTime(), 24*60*60*1000);
		      //24*60*60*1000表示為多久的區間(以毫秒表示之)			
	}

	public void contextDestroyed(ServletContextEvent servletcontextevent) {
		ServletContext servletcontext = servletcontextevent.getServletContext();
		servletcontext.log("lucene schedule shutdown.");
	}
}
