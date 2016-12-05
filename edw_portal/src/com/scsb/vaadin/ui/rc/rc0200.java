package com.scsb.vaadin.ui.rc;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Properties;

import org.math.R.RserverConf;
import org.math.R.Rsession;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.Rserve.RConnection;
import org.vaadin.aceeditor.AceEditor;

import com.scsb.domain.RsessionPool;
import com.scsb.vaadin.composite.ScsbGlobView;
import com.scsb.vaadin.composite.ScsbPage;
import com.scsb.vaadin.composite.ScsbToolbar;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;

/**
 * r client 模組
 * @author 3471
 *
 */
@SuppressWarnings("serial")
public class rc0200 extends ScsbGlobView {
	ScsbToolbar		  toolBar		=new ScsbToolbar();
	Accordion ac = new Accordion();
	TabSheet     mainTab    =new TabSheet();	
	 
	AceEditor editor = new AceEditor();
	AceEditor logList = new AceEditor();
	Properties info = new Properties();
	
	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	PrintStream ps = new PrintStream(baos);	
	
	private String workspaceId = "";
	String xworkPath="";

	ScsbPage    pagePanel     = new ScsbPage();
	/**
	 * 主畫面
	 */
	public rc0200() {
		logList.setValue("");
		create();
	}
	
	public void create(){
		//更新
		/*
		toolBar.setRefreshButtonVisible(true);
		toolBar.refreshBu.addClickListener(refreshBulistener);
		*/
		//執行
		toolBar.setRunButtonVisible(true);
		toolBar.runBu.setImmediate(true);
		toolBar.runBu.addClickListener(runBulistener);
		toolBar.runBu.setIcon(FontAwesome.SEND);
		toolBar.runBu.setDescription("執行");
		toolBar.runBu.addStyleName("friendly");
		toolBar.runBu.setEnabled(true);
		
		HorizontalSplitPanel hSplitLayout =new HorizontalSplitPanel();
		VerticalSplitPanel vSplitLayout =new VerticalSplitPanel();
       
        ac.setCaption("R");
        ac.setSizeFull();
		
        editor.setSizeFull();
        editor.setImmediate(true);
        
        logList.setSizeFull();
        logList.setReadOnly(true);
        logList.setImmediate(true);
        
	    mainTab.setImmediate(true);
	    mainTab.setSizeFull();
		Tab tabEdit =mainTab.addTab(editor);
		tabEdit.setIcon(FontAwesome.EDIT);
		tabEdit.setCaption(i18nProps.getProperty("editor","編輯器"));
		Tab tabLog =mainTab.addTab(logList);
		tabLog.setIcon(FontAwesome.HISTORY);		
		tabLog.setCaption(i18nProps.getProperty("loger","日誌"));
		
		hSplitLayout.setLocked(false);
		hSplitLayout.setSplitPosition(200,Unit.PIXELS);// 100px :UNITS_PIXELS=0
		hSplitLayout.setFirstComponent(ac);	
		hSplitLayout.setSecondComponent(mainTab);
		// screenHeight -toolbar
		int screenHeight =this._screenHeight -34;
		hSplitLayout.setHeight(screenHeight+"px");		
		hSplitLayout.setWidth("100%");		
		
		getContent().addComponent(toolBar.getContent());
		getContent().addComponent(hSplitLayout);
		getContent().setSpacing(false);
	}
	
	/**
	 *  Listener for 更新動作
	 * */	
	ClickListener refreshBulistener = new ClickListener() {
		@Override
		public void buttonClick(ClickEvent event) {	
			//setRworkspace();
		}		
	};		
	
	/**
	 * Listener for 執行動作
	 */
	ClickListener runBulistener = new ClickListener() {
		@Override
		public void buttonClick(ClickEvent event) {
			//將editor存入temp.r再執行
			runR();
			//rSession.eval(editor.getValue());
			//setRworkspace();
		}		
	};	
	
	/**
	 * Listener for 讀檔動作
	 */
	ClickListener getDataListener = new ClickListener() {
		@Override
		public void buttonClick(ClickEvent event) {
			/*
			if (checkConnection()){
				String buId=event.getButton().getId();
				String libname=buId.substring(0,buId.indexOf("."));
				String tableName=buId.substring(buId.indexOf(".")+1);
				String dataTableName=libname+"."+tableName;

				String saveFile=i18nProps.getProperty("savePath","")+"/"+users.getUserid()+"_"+DateUtil.getDTS()+".xls";
				SasTable table =new SasTable(jdbcConnection ,dataTableName ,saveFile);
		        
				Tab tabQuery =mainTab.addTab(table.vSplitLayout);
				tabQuery.setIcon(FontAwesome.SEARCH);
				tabQuery.setCaption("檢視:"+buId);	
				tabQuery.setClosable(true);
				mainTab.setSelectedTab(tabQuery);
			}
			*/
		}
	};
	/**
	 * check connection
	 */
	public boolean runR(){
		RserverConf rconf=new RsessionPool().getRserverConf();
		if (rconf == null){
			Notification.show("R Serve 目前忙祿中,請稍後再試.",Notification.Type.ERROR_MESSAGE);			
			return false;
		}
		Rsession rSession = new Rsession(ps, rconf ,false);		
		
		//workspaceId = getRandomString(rand, chars, 20);
		workspaceId=this.session.getId();
		//Rsession rSession = Rsession.newRemoteInstance(ps, rconf);
        try {
        	xworkPath="D:/rworkspace"+"/"+workspaceId;
			String info =rSession.eval("R.version.string").asString();
			logList.setReadOnly(false);
			logList.setValue(logList.getValue()+info);
			logList.setReadOnly(true);				
			rSession.eval(".System.mainDir<-'D:/rworkspace'");
			rSession.eval(".System.subDir<-'"+workspaceId+"'");
			rSession.eval("dir.create(file.path(.System.mainDir, .System.subDir), showWarnings = FALSE)");
			rSession.eval("setwd(file.path(.System.mainDir, .System.subDir))");
			rSession.eval("rm(list=ls())");
			rSession.eval("load('.RData')");	
			baos.reset();
			rSession.eval(editor.getValue());
	        setRworkspace(rSession);
	        rSession.eval("save.image()");			
		} catch (REXPMismatchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
        rSession.end();
		return true;	
	}
	/**
	 * 建立r workspace Accordion
	 */
	private void setRworkspace(Rsession rSession){
          try {
			ac.removeAllComponents();
			String[] arrString ={"list"};
			for(int i=0 ;i<arrString.length;i++){
            	String xtype=arrString[i].trim();
            	VerticalLayout libContent =setRDataFrame(rSession ,xtype);
            	Tab libTab=ac.addTab(libContent , xtype);
            	libTab.setIcon(FontAwesome.FOLDER);
            	libTab.setStyleName("scsbBg5");
            }//for
          } catch (Exception e) {
        	  e.printStackTrace();
          }			
		logList.setReadOnly(false);
		logList.setValue(logList.getValue()+baos.toString());
		baos.reset();
		logList.setReadOnly(true);
	}
	
	/**
	 * 取得R workSpace的 DataFrame
	 * @param workSpace
	 * @return
	 */
	private VerticalLayout setRDataFrame(Rsession rSession ,String vType){
		VerticalLayout vContent = new VerticalLayout();
		int iFlag=0;
		
		try {
			REXP rexp =rSession.eval("ls()");
			String[] arrString =rexp.asStrings();		
			for(int i=0 ;i<arrString.length;i++){
				String filename=arrString[i];
				if (filename.indexOf(".System.") ==-1){
					Button button =new Button(filename);
					iFlag++;
					button.setWidth("100%");
			    	if (iFlag % 2 == 1){
			    		button.addStyleName("scsbBg2");
			    		button.addStyleName("textLeft");
			    	}else{
			    		button.addStyleName("scsbBg3");
			    		button.addStyleName("textLeft");
			    	}
			    	button.setIcon(FontAwesome.TABLE);
					button.setId(vType.trim()+"."+filename.trim());
					button.addClickListener(getDataListener);
					vContent.addComponent(button);
				}
		     }//for		
	     } catch (Exception e) {
			e.printStackTrace();
		}
		return vContent;
	}
}



