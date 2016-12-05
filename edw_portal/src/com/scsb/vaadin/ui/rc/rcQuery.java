package com.scsb.vaadin.ui.rc;

import org.vaadin.aceeditor.AceEditor;


import com.scsb.vaadin.composite.ScsbGlobView;
import com.scsb.vaadin.composite.ScsbPopover;
import com.scsb.vaadin.composite.ScsbToolbar;
import com.scsb.vaadin.r.R4Html;
import com.scsb.vaadin.r.RunRcode;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.TabSheet.Tab;

/**
 * 查詢結果
 * @author 3471
 *
 */
@SuppressWarnings("serial")
public class rcQuery extends ScsbGlobView {
	ScsbToolbar		  toolBar		=new ScsbToolbar();
	R4Html r4html =new R4Html("");
	
	TabSheet     mainTab    =new TabSheet();
	AceEditor logList = new AceEditor();
	String sid;
	
	public rcQuery(String sid ,TabSheet mainTab) {
		this.mainTab=mainTab;
		this.sid=sid;
		this.getView().setBarVisible(false);
		create();
		
	    String tabCaption =i18nProps.getProperty("detailQuery","--明細查詢");
	    Tab  tabProperty =mainTab.addTab( getContent(),tabCaption);
	    tabProperty.setClosable(true);
	    tabProperty.setIcon(FontAwesome.SEARCH);
	    mainTab.setSelectedTab(tabProperty);
	    		
	}
	public void create(){
		try {
			toolBar.setPrintButtonVisible(true);
			toolBar.printBu.addClickListener(printBulistener);
			toolBar.setLogButtonVisible(true);
			toolBar.logBu.addClickListener(logBulistener);
			getContent().addComponent(toolBar.getContent());		
			
			RunRcode runR =new RunRcode();
			boolean isRun=runR.runR(rsessionProps.get("R_CODE_PATH")+"/test.r");
			logList.setValue(runR.getMsg());
			
			if (isRun){
				r4html.setValue(runR.getHtml());
				getContent().addComponent(r4html);
				
				r4html.addValueChangeListener(
				        new R4Html.ValueChangeListener() {
					    @Override
					    public void valueChange() {
					    	String showMsg="";
							for(java.util.Iterator<String> iter =r4html.getHashRequest().keySet().iterator();iter.hasNext();){
								String key =iter.next();
								String value =r4html.getHashRequest().get(key);
								showMsg+=key+"="+value+"\n";
							}
					        Notification.show(showMsg);
					    }
					});		
				}				
			} catch (Exception e) {
				e.printStackTrace();
			}				
	}
	
	/**
	 *  Listener for 列印動作
	 * */	
	ClickListener printBulistener = new ClickListener() {
		@Override
		public void buttonClick(ClickEvent event) {
			r4html.setWaterMark(users.getUserid()+"："+users.getUserName());
			r4html.runPrintData();
		}		
	};		
	/**
	 *  Listener for log動作
	 * */	
	ClickListener logBulistener = new ClickListener() {
		@Override
		public void buttonClick(ClickEvent event) {
			ScsbPopover logPop =new ScsbPopover();
			logList.setWidth("100%");
			logList.setHeight("300px");
			logPop.addContentComponent(logList);
			logPop.popover.setWidth("90%");
			logPop.popover.setHeight("90%");
			UI.getCurrent().addWindow(logPop.popover);
		}		
	};		
}
