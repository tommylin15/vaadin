package com.scsb.etl;

import java.util.ArrayList;
import java.util.List;

import com.scsb.util.ThreadUtil;
import com.scsb.util.WindowsOsUtils;
public  class  ETL_Process_Thread extends Thread{
	
	String cmdexeStr =null;
	//long iCmdPid=-1;
	private volatile boolean finished = false;
    private List<String> stdoutList = new ArrayList<String>();
    private List<String> erroroutList = new ArrayList<String>();	
	
    public ETL_Process_Thread(String cmdexeStr){  
    	this.cmdexeStr=cmdexeStr;
    }
    
    public void stopMe() {
        finished = true;
        WindowsOsUtils.kill(cmdexeStr);
        this.interrupt();
    }    
    
    public void run() {
    	startRunTime(cmdexeStr);
    }
	
    public String startRunTime(String cmdexeStr){  
        try { 
        	 String encoding = System.getProperty("file.encoding");
             // get now ping.exe's process list
             //Hashtable<Long, Long> oldPids = WindowsOsUtils.getTaskList("cmd.exe");
             //iCmdPid =WindowsOsUtils.getPidForWmic("cmd.exe",cmdexeStr);
             // call OS command
        	 //String[] cmd={"cmd","/C" ,"start "+cmdexeStr}; //加start 開dos視窗             
        	 String[] cmd={"cmd","/C" ,cmdexeStr};
             Process proc = Runtime.getRuntime().exec(cmd);
         	//Process proc = Runtime.getRuntime().exec(cmdexeStr);
            //System.out.println("cmd Pid is " + iCmdPid);             

            stdoutList.clear();
            erroroutList.clear();
            
            ThreadUtil stdoutUtil = new ThreadUtil(proc.getInputStream(), stdoutList);
            ThreadUtil erroroutUtil = new ThreadUtil(proc.getErrorStream(), erroroutList);
            stdoutUtil.start();
            erroroutUtil.start();
            
            int num = proc.waitFor(); 
            System.out.println("------num:"+num);
             proc.destroy();   
        } catch (Exception e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
        return null;  
    }     
    
}