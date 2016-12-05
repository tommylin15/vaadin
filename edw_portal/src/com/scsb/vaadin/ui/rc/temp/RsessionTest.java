package com.scsb.vaadin.ui.rc.temp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Properties;
import java.util.Random;

import org.apache.commons.math3.util.MultidimensionalCounter.Iterator;
import org.math.R.RLogPanel;
import org.math.R.RserverConf;
import org.math.R.Rsession;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPLogical;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;

import com.vaadin.ui.Notification;

public class RsessionTest {  
    public static void main(String[] args) throws REXPMismatchException, FileNotFoundException {
    	
    	
    	/*
		try {
System.out.println("running...");			
			RConnection rc = new RConnection("127.0.0.1", 6311);
System.out.println("RConnection...");
			REXP myREXP = rc.parseAndEval("R.version.string");
			//REXPLogical resLog = (REXPLogical) myREXP;
			
			String rv = rc.eval("R.version.string").asString();  			
			System.out.println(rv);
		} catch (RserveException e1) {
			e1.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	*/
    	/*
		Properties prop =new Properties();
		prop.put("workdir", "d:\\rworkspace");
		prop.put("remote", "enable");
		prop.put("fileio", "enable");
		prop.put("interactive", "yes");
		//prop.put("port", "6311");
		prop.put("encoding", "utf8");
		prop.put("remote", "enable");
    	//prop.put("R_HOME", "c:/Program Files/R/R-3.1.3");    	
    	RserverConf rconf = new RserverConf("127.0.0.1", 6311, "", "", prop);
    	if (!rconf.isPortAvailable(6311)){
    		rconf.connect();
    	}
    	//RserverConf rconf2 = new RserverConf("127.0.0.1", 6312, "", "", prop);
    	//OutputStream os=new OutputStream();
    	//PrintStream ps=new PrintStream(os);

    	//Rsession rSession = new Rsession(System.out , rconf , true);
    	
    	Rsession rSession = Rsession.newInstanceTry(System.out, rconf);
    	//Rsession rSession2 = Rsession.newInstanceTry(System.out, rconf2);
		rSession.eval("a<-10");
		rSession.end();
    	//rSession2.end();
    	//Rsession rSession2 = Rsession.newLocalInstance(System.out, prop);

    	//Rsession rSession = Rsession.newInstanceTry(System.out, rconf);
		/*
		double[] rand = rSession.eval("rnorm(5)").asDoubles();
    	for(double ran:rand){
    	    System.out.println(ran+",");
    	}
   		*/
    	//Rsession rSession = Rsession.newLocalInstance(System.out, null);

	    	 //System.out.println(rSession.installPackage("sensitivity", true));
    	/*
    		   rSession.eval("rm(list=ls())");
				String aa=rSession.asString("setwd(\"d:/rworkspace\")");
				String workspace = rSession.asString("getwd()");
				System.out.println(workspace);
				
				System.out.println("getRServeOS:"+rSession.getRServeOS());
				System.out.println("getStatus:"+rSession.getStatus());
				//RLogPanel logPanel =new RLogPanel();
				String list=rSession.asString("ls()");
				System.out.println(list);				
				rSession.end();
				*/
		//} catch (REXPMismatchException e) {
		//	e.printStackTrace();
		//}
    }  
    
    
  
    
}