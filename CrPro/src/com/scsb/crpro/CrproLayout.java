package com.scsb.crpro;


import java.util.Hashtable;
import java.util.Properties;

import com.scsb.domain.HashSystem;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.server.WrappedSession;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class CrproLayout extends VerticalLayout{
	protected WrappedSession session =UI.getCurrent().getSession().getSession();
	private static final long serialVersionUID = 3325233253592861773L;
	protected static HashSystem hashSystem = HashSystem.getInstance();
	protected static Properties systemProps     =hashSystem.getProperties();
	
	// _screenWidth = BrowserWindow - 25
	 protected static	int _screenWidth = UI.getCurrent().getPage().getCurrent().getBrowserWindowWidth() - 25;
	// _screenHeight = BrowserWindow - header -tab -1
	 protected static	int _screenHeight =(UI.getCurrent().getPage().getCurrent().getBrowserWindowHeight()) -40 -1;
	
	
	protected String themePath;
	protected String _User;
	protected Hashtable<String,String> _hashUserAction =new Hashtable<String,String>();
	protected String _Schedule;
	protected String _EtlLog;
	protected String _Jython;
	
	public CrproLayout(){
	    this.setMargin(true);
	    this.setSpacing(true);
	    
	    this._User =session.getAttribute("User")+"";
	    this._hashUserAction =(Hashtable<String,String>)session.getAttribute("_hashUserAction");
	    
	    this._Schedule =systemProps.getProperty("isSchedule");
	    this._EtlLog =systemProps.getProperty("isEtlLog");
	    this._Jython =systemProps.getProperty("isJython");
	    
	    this.themePath="/"+systemProps.getProperty("SYSTEM_NAME")+"/static/ace";
	    
		
	}	
}