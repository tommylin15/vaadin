package com.scsb.vaadin.r;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;

import org.math.R.RserverConf;
import org.math.R.Rsession;
import org.rosuda.REngine.REXPMismatchException;

import com.scsb.domain.RsessionPool;
import com.scsb.util.IO;
import com.scsb.vaadin.composite.ScsbGlob;
import com.vaadin.ui.Notification;

public class RunRcode extends ScsbGlob {
	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	PrintStream ps = new PrintStream(baos);	
	
	private String workspaceId = "";
	private String xworkPath="";	
	private String retHtml="";

	public RunRcode(){
		
	}
	
	public String getHtml(){
		return retHtml;
	}
	public String getMsg(){
		return baos.toString();
	}	
	/**
	 * check connection
	 */
	public boolean runR(String rCode){
		RserverConf rconf=new RsessionPool().getRserverConf();
		if (rconf == null){
			Notification.show("R Serve 目前忙祿中,請稍後再試.",Notification.Type.ERROR_MESSAGE);			
			return false;
		}
		Rsession rSession = new Rsession(ps, rconf ,false);		
		workspaceId=this.session.getId();
        try {
        	xworkPath=rsessionProps.get("WORKSPACE")+"/"+workspaceId;
			//String info =rSession.eval("R.version.string").asString();
			String source ="source(\""+rCode+"\", encoding='UTF-8')";
			//先清空工作區
			File wksp =new File(rsessionProps.get("WORKSPACE")+"/"+workspaceId.trim());
			//IO.deleteAll(wksp);
			
			rSession.eval(".System.isFromWeb<-TRUE");
			rSession.eval(".System.mainDir<-'"+rsessionProps.get("WORKSPACE")+"' ");
			rSession.eval(".System.workspaceId<-'"+workspaceId.trim()+"'");
			rSession.eval("rm(list=ls())");
			rSession.eval(source);
			//rSession.eval("load('.RData')");	
			//rSession.eval(rCode);
			//String _webout = rSession.asString(".Server.webout");
			retHtml =IO.read(xworkPath+"/"+workspaceId+".html");		
	        //rSession.eval("save.image()");
		} catch (Exception e) {
			e.printStackTrace();
		} 
        rSession.end();
		return true;	
	}
	
}