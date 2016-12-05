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
public  class  ETL_Run_Mask_Data implements StatefulJob , InterruptableJob{
	  AtomicReference<Thread> runningThread = new AtomicReference<Thread>();
      AtomicBoolean stopFlag = new AtomicBoolean(false);	
      ETL_Process_Thread workerThread=null;
      
	  public void execute(JobExecutionContext context) throws JobExecutionException{
		     System.out.println("start mask batch job:"+Thread.currentThread().getName());
	    	 //1.config file
	      	 JobDataMap dataMap = context.getJobDetail().getJobDataMap();
	      	 String configPath = dataMap.getString("configPath");
		     
		     //D:/application/GitHome/git/CrPro/WebContent/properites/makecode
		     EtlMaskCode runCode =new EtlMaskCode(configPath);
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