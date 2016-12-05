package com.scsb.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class WindowsOsUtils {
	
	private static String WMIC_RUNTIME="C:\\Windows\\system32\\wbem\\wmic.exe";
	
    static class ReturnValue {
        public ReturnValue(int exitCode, String stdout) {
            this.exitCode = exitCode;
            this.stdout = stdout;
        }

        public int exitCode;
        public String stdout;

        @Override
        public String toString() {
            return "exitCode : " + exitCode + "; stdout " + stdout;
        }
    }

    /**
     * call an OS command.
     * 
     * @param cmd
     * @throws IOException
     * @throws InterruptedException
     */
    public static ReturnValue exec(String cmdexeStr) throws IOException,
            InterruptedException {

        // call OS command
    	String[] cmd={"cmd","/C" ,cmdexeStr};    	
        Process process = Runtime.getRuntime().exec(cmd);

        // get input stream for STDOUT
        String character = "ms950";
        /*
        List<String> stdoutList = new ArrayList<String>();
        List<String> erroroutList = new ArrayList<String>();	
        ThreadUtil stdoutUtil = new ThreadUtil(process.getInputStream(), stdoutList);
        ThreadUtil erroroutUtil = new ThreadUtil(process.getErrorStream(), erroroutList);
        stdoutUtil.start();
        erroroutUtil.start();
        */
        InputStream is = process.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is,character));

        String line;

        String stdout = "";

        while ((line = reader.readLine()) != null) {
            stdout += line + "\n";
        }

        // wait to complete
        int retValue = process.waitFor();

        // close I/Os
        process.destroy();
        is.close();
        reader.close();

        ReturnValue rv = new ReturnValue(retValue, stdout);
        return rv;
    }

    /**
     * kill an OS command
     * 
     * @param pid
     */
    public static void kill(String cmdexeStr) {
        System.out.println("kill process : " + cmdexeStr);
        while(true){
	        long pid=getPidForWmic("cmd.exe",cmdexeStr);
	        if (pid==-1) break;
	        String cmd = WMIC_RUNTIME+" process "+pid+" delete";
	        ReturnValue rv;
	        try {
	            rv = WindowsOsUtils.exec(cmd);
	            System.out.println("Kill result " + rv.toString());
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
        }//while
    }
    
    public static synchronized long getPidForWmic(String cmdImageName ,String commandlineKey) {

    	long pid=-1;
        String cmd = WMIC_RUNTIME+" process where caption=\""+cmdImageName+"\" get caption,commandline,handle /format:csv ";

        try {
            ReturnValue rv = WindowsOsUtils.exec(cmd);
            String[] ss = rv.stdout.split("\n");
            for (int i = 0; i < ss.length; i++) {
                String line = ss[i];
                // we use csv format ,so split it with ,
                String[] cols = line.split(",");
                if (cols.length == 4) {
                	if (cols[2].indexOf(commandlineKey) > -1){
                		pid=Long.parseLong(cols[3]);
                	}
                }
            }//for
        } catch (Exception e) {
            e.printStackTrace();
            return pid;
        }
        return pid;
    }        
}
