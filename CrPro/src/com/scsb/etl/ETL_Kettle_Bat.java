package com.scsb.etl;

import  java.io.*;
import java.util.Date;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.quartz.InterruptableJob;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;
import org.quartz.UnableToInterruptJobException;
@SuppressWarnings("deprecation")
public  class  ETL_Kettle_Bat implements StatefulJob , InterruptableJob{
	  AtomicReference<Thread> runningThread = new AtomicReference<Thread>();
      AtomicBoolean stopFlag = new AtomicBoolean(false);	
      ETL_Process_Thread workerThread=null;
      
	  public void execute(JobExecutionContext context) throws JobExecutionException{
		     System.out.println("currentThread:"+Thread.currentThread().getName());

	    	 //1.config file
	      	 JobDataMap dataMap = context.getJobDetail().getJobDataMap();
	      	 String batName = dataMap.getString("batName");
	      	 //2.啟動bat
			 workerThread=new ETL_Process_Thread(batName);
	  		 workerThread.start();   	 
		  	 this.runningThread.set(workerThread);
	      	 
	    	 //3.當jvm關閉前要執行的Thread...
		  	 Runtime.getRuntime().addShutdownHook(new Thread(){  
		  		 @Override  
		  		 public void run() {  
		  			 System.out.println("jvn is close...");  
		  		 }  
		     }); 
		  	  
		       try {
		             while(runningThread.get() != null) {
		            	 if (stopFlag.get()) break;
		            	 if (!workerThread.isAlive()){
		            		 break;
		            	 }
		            	 try {
							Thread.sleep(3000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} 
		             }//while
		       } finally {
		    	   runningThread.set(null); 
		       }		  	    
			   //workerThread=new ETL_Process_Thread(batName);
	  		   //workerThread.start();
		  	  //startRunTime(batName);
	  }  
	  
	@Override
	public void interrupt() throws UnableToInterruptJobException {
	    stopFlag.set(true);
	    try {
	    	workerThread.stopMe();
	        Thread.sleep(1000);
	    } catch (InterruptedException e) {
	    	e.printStackTrace();
	    }
	    Thread thread = runningThread.getAndSet(null);
	    if (thread != null)  thread.interrupt();
	}
	
}