package com.scsb.vaadin.composite;

import java.util.Hashtable;
import java.util.Properties;

import javax.servlet.ServletContext;

import com.scsb.db.bean.Users;
import com.scsb.domain.HashI18N;
import com.scsb.domain.HashRsession;
import com.scsb.domain.HashSystem;
import com.scsb.domain.HashTrans;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinSession;
import com.vaadin.server.WrappedSession;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
/**
 * SCSB-SAS公用參數
 * @author 3471
 *
 */@SuppressWarnings("serial")
public class ScsbGlob  {
	 
	//layout.setHeight("1000px");
	 protected int _screenWidth = UI.getCurrent().getPage().getCurrent().getBrowserWindowWidth();
	// screenHeight = BrowserWindow - header - scsbbar -1
	 protected int _screenHeight =(UI.getCurrent().getPage().getCurrent().getBrowserWindowHeight()) - 45 -40 -1;
	 
	protected static HashSystem    hashSystem   = HashSystem.getInstance();
	protected static Properties   systemProps     =hashSystem.getProperties();
	
	protected static HashTrans    hashTrans    	= HashTrans.getInstance();
	
	protected static HashI18N    hashI18N   = HashI18N.getInstance();
	protected Properties   i18nProps   = new Properties();
	
	protected Properties messageProps   = new Properties();
	
	protected HashRsession    hashRsession   = HashRsession.getInstance();
	protected Properties   rsessionProps     =hashRsession.getProperties();
	
	protected WrappedSession session =UI.getCurrent().getSession().getSession();
	protected String lang="tw";
	protected Label title =new Label("",ContentMode.HTML);
	
	protected Users users =new Users();
	protected Hashtable<String,String> userAction =new Hashtable<String,String>();
	protected String userActionCode="";
	protected String className;
	protected String path;
	
	public ScsbGlob() {
		this("");
	}
	public ScsbGlob(String titleValue) {	
		
		this.lang =session.getAttribute("Language")+"";		
		this.setTitle(titleValue);
    	ServletContext servletContext = VaadinServlet.getCurrent().getServletContext();
    	this.path =servletContext.getRealPath("/");
    	
    	//不正常簽入
    	if (session.getAttribute("UsersBean") == null){
    		session.invalidate();
    		UI.getCurrent().getPage().reload();
    	}else{
    		this.users =(Users)session.getAttribute("UsersBean");
    		this.userAction =(Hashtable<String,String>)session.getAttribute("UserAction");
    	}        
    	
	}

	 /**
	  * 標題
	  * @param htmlString
	  */
	 public void setTitle(String htmlString){
		 this.title.setValue("<table width='100%' height='100%' border=0>" +
	    			"<tr valign=center ><td align=right>" +htmlString+
	    			"</td></tr></table>");
	 }
	 /**
	  * 載入多語系
	  * @param lang
	  * @param fileName
	  */
	public void loadI18N(String lang ,String fileName){
		i18nProps = hashI18N.getProperties(lang,fileName);
		if (i18nProps == null) i18nProps=new Properties();
		messageProps = hashI18N.getProperties(lang,"message");
		if (messageProps == null) messageProps=new Properties();
	}
	
}
