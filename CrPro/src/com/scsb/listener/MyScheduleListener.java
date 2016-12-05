package com.scsb.listener;

import java.util.Iterator;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import com.scsb.domain.HashSystem;
import com.scsb.util.IO;

import myschedule.web.MySchedule;

/**
 * A servlet context listener to initialize and destroy MySchedule application instance. We also save the instance
 * into the ServletContext space with SCHEDULE_INSTANCE_KEY.
 *
 * @author Tommy Lin
 */
public class MyScheduleListener implements ServletContextListener {
	static Logger logger = Logger.getLogger(MyScheduleListener.class.getName());
	static HashSystem hashSystem = HashSystem.getInstance();
	
    public static final String SCHEDULE_INSTANCE_KEY = "myschedule.web.MySchedule";

    private boolean isRunning =false;
    public void contextInitialized(ServletContextEvent sce) {
    	//啟始
        ServletContext ctx = sce.getServletContext();
		String path = sce.getServletContext().getRealPath("/");
		String systemFile = sce.getServletContext().getInitParameter("system-file")+"";
		//如果系統參數檔未載入,則重新載入
		if (hashSystem.getProperties() == null){
			hashSystem.resetHashSystem(path+systemFile);
		}
		Properties systemProps =hashSystem.getProperties();
		String isSchedule =systemProps.getProperty("isSchedule");
        if (isSchedule.equals("Y")) isRunning=true;
        
        if (isRunning){
	        MySchedule mySchedule = MySchedule.getInstance();
	        mySchedule.init();
	        ctx.setAttribute(SCHEDULE_INSTANCE_KEY, mySchedule);
	    	getEnv();
        }
    }

    public void contextDestroyed(ServletContextEvent sce) {
    	//停止
    	if (isRunning){
	        MySchedule mySchedule = MySchedule.getInstance();
	        mySchedule.destroy();
	        ServletContext ctx = sce.getServletContext();
	        ctx.removeAttribute(SCHEDULE_INSTANCE_KEY);
    	}
    }

    /**
     * 系統參數
     */
    private void getEnv() {
        for ( Iterator<String> iter = System.getenv().keySet().iterator(); iter.hasNext(); ) {
            String key = iter.next();
            System.out.println( key + " = " + System.getenv().get( key ) );
        }
    }    
    
}
