package com.scsb.vaadin.composite;

import java.util.Hashtable;

import com.scsb.db.bean.Users;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class ScsbToolbar extends ScsbGlob {
	
	private AbsoluteLayout content =new AbsoluteLayout();
	//private HorizontalButtonGroup hbGroup =new HorizontalButtonGroup();
	//private HorizontalButtonGroup hbleftGroup =new HorizontalButtonGroup();
	private HorizontalLayout hbGroup =new HorizontalLayout();
	private HorizontalLayout hbleftGroup =new HorizontalLayout();
	
	public Button saveBu =new Button(""); 
	public Button addBu =new Button(""); 
	public Button delBu =new Button("");
	public Button editBu =new Button("");
	public Button checkBu =new Button("");
	
	public Button chartBu =new Button("");
	
	public Button runBu =new Button("");
	public Button connectBu =new Button("");
	public Button printBu =new Button("");
	public Button logBu =new Button("");
	
	public Button refreshBu =new Button("");
	
	protected int iButton;	
	
    public ScsbToolbar() {
    	
    	content.setWidth("100%");
    	content.setHeight("34px");
    	content.addComponent(hbleftGroup, " left: 10px; top :4px");
    	content.addComponent(hbGroup, " right: 10px; top :4px");
    	content.addStyleName("scsbBg6");
    	
    	hbGroup.setSpacing(true);
    	hbleftGroup.setSpacing(true);
    	//reFresh=====================================================================
    	refreshBu.setId("reFresh");
    	refreshBu.setDescription(messageProps.getProperty("Button_refreshBu", "重新整理"));
    	refreshBu.setVisible(false);
    	refreshBu.setIcon(FontAwesome.REFRESH);
    	refreshBu.addStyleName("icon-only");
    	refreshBu.addStyleName("friendly");    	
    	//refreshBu.addStyleName("small");
    	refreshBu.addClickListener(
    		    new ClickListener() {
    				@Override
    				public void buttonClick(ClickEvent event) {
    				}
        		}
        	);
    	addLeftComponent(refreshBu);    	
    	
    	//connect=====================================================================
    	connectBu.setId("connect");
    	connectBu.setDescription(messageProps.getProperty("Button_connectBu", "連線"));
    	connectBu.setVisible(false);
    	connectBu.addStyleName("icon-only");
    	connectBu.addStyleName("friendly");    	
    	connectBu.addClickListener(
    		    new ClickListener() {
    				@Override
    				public void buttonClick(ClickEvent event) {
    				}
        		}
        	);
    	addLeftComponent(connectBu);      	

    	
    	//run=====================================================================
    	runBu.setId("run");
    	runBu.setDescription(messageProps.getProperty("Button_runBu", "執行"));
    	runBu.setVisible(false);
    	runBu.setIcon(FontAwesome.SEARCH);
    	runBu.addStyleName("icon-only");
    	runBu.addStyleName("friendly");    	
    	runBu.addClickListener(
    		    new ClickListener() {
    				@Override
    				public void buttonClick(ClickEvent event) {
    				}
        		}
        	);
    	addLeftComponent(runBu);       
    	
    	
    	//add=====================================================================
    	addBu.setId("add");
    	addBu.setDescription(messageProps.getProperty("Button_addBu", "新增"));
    	addBu.setVisible(false);
    	addBu.setIcon(FontAwesome.FILE);
    	addBu.addStyleName("friendly");
    	addBu.addStyleName("icon-only");
    	//addBu.addStyleName("small");
    	addBu.addClickListener(
    		    new ClickListener() {
    				@Override
    				public void buttonClick(ClickEvent event) {
    					//addBu.setVisible(false);
    				}
        		}
        	);
    	addLeftComponent(addBu);
    	//edit=====================================================================
    	editBu.setId("edit");
    	editBu.setDescription(messageProps.getProperty("Button_editBu", "修改"));
    	editBu.setVisible(false);
    	editBu.addStyleName("friendly");    	
    	editBu.addStyleName("icon-only");
    	editBu.setIcon(FontAwesome.EDIT);
    	editBu.addClickListener(
    		    new ClickListener() {
    				@Override
    				public void buttonClick(ClickEvent event) {
    					//delBu.setVisible(false);
    				}
        		}
        	);		
    	addLeftComponent(editBu);   
    	//sasve=====================================================================    	
    	saveBu.setId("save");
    	saveBu.setDescription(messageProps.getProperty("Button_saveBu", "存檔"));
    	saveBu.setDisableOnClick(true);
    	saveBu.setVisible(false);
    	saveBu.addStyleName("friendly");    	
    	//saveBu.addStyleName("small");
    	saveBu.addStyleName("icon-only");
    	saveBu.setIcon(FontAwesome.SAVE);
    	saveBu.addClickListener(
    	    new ClickListener() {
    			@Override
    			public void buttonClick(ClickEvent event) {
    				saveBu.setVisible(false);
    			}
    		}
    	);
    	addLeftComponent(saveBu);
    	//del=====================================================================
    	delBu.setId("del");
    	delBu.setDescription(messageProps.getProperty("Button_delBu", "刪除"));
    	delBu.setVisible(false);
    	delBu.addStyleName("danger");    	
    	delBu.addStyleName("icon-only");
    	//delBu.addStyleName("small");
    	delBu.setIcon(FontAwesome.TRASH_O);
    	delBu.addClickListener(
    		    new ClickListener() {
    				@Override
    				public void buttonClick(ClickEvent event) {
    					//delBu.setVisible(false);
    				}
        		}
        	);		
    	addLeftComponent(delBu);    	 
    	//check=====================================================================
    	checkBu.setId("check");
    	checkBu.setDescription(messageProps.getProperty("Button_checkBu", "核可"));
    	checkBu.setVisible(false);
    	checkBu.addStyleName("friendly");    	
    	checkBu.addStyleName("icon-only");
    	checkBu.setIcon(FontAwesome.CHECK_SQUARE);
    	checkBu.addClickListener(
    		    new ClickListener() {
    				@Override
    				public void buttonClick(ClickEvent event) {
    					//delBu.setVisible(false);
    				}
        		}
        	);		
    	addLeftComponent(checkBu);    	
    	//print====================================================
    	printBu.setId("print");
    	printBu.setDescription(messageProps.getProperty("Button_printBu", "列印"));
    	printBu.setVisible(false);
    	printBu.addStyleName("icon-only");
    	printBu.addStyleName("friendly");    	
    	printBu.setIcon(FontAwesome.PRINT);
    	printBu.addClickListener(
    		    new ClickListener() {
    				@Override
    				public void buttonClick(ClickEvent event) {
    				}
        		}
        	);
    	addLeftComponent(printBu);      
    	//chart=====================================================================
    	chartBu.setId("chart");
    	chartBu.setDescription(messageProps.getProperty("Button_chartBu", "圖表"));
    	chartBu.setVisible(false);
    	//chartBu.addStyleName("small");
    	chartBu.addStyleName("icon-only");
    	chartBu.setIcon(FontAwesome.BAR_CHART_O);
    	chartBu.addClickListener(
    		    new ClickListener() {
    				@Override
    				public void buttonClick(ClickEvent event) {
    					//chartBu.setVisible(false);
    				}
        		}
        	);    	
    	addLeftComponent(chartBu);    	    	
    	//log====================================================
    	logBu.setId("log");
    	logBu.setDescription(messageProps.getProperty("Button_logBu", "訊息"));
    	logBu.setVisible(false);
    	logBu.addStyleName("icon-only");
    	logBu.addStyleName("friendly");    	
    	logBu.setIcon(FontAwesome.LIST);
    	logBu.addClickListener(
    		    new ClickListener() {
    				@Override
    				public void buttonClick(ClickEvent event) {
    				}
        		}
        	);
    	addLeftComponent(logBu);         	
    }

/**
 * 是否使用重新整理按鈕
 * @param isVisible
 */
public void setRefreshButtonVisible(boolean isVisible){
	 this.refreshBu.setVisible(isVisible);	
	 this.refreshBu.setEnabled(true);
	 if (this.refreshBu.isVisible())iButton++;
	 else iButton--;
	 if (iButton > 0)    	setVisible(true);
	 else setVisible(false);			 
}

/**
 * 是否使用連線按鈕
 * @param isVisible
 */
public void setConnectButtonVisible(boolean isVisible){
	 this.connectBu.setVisible(isVisible);	
	 this.connectBu.setEnabled(true);
	 if (this.connectBu.isVisible())iButton++;
	 else iButton--;
	 if (iButton > 0)    	setVisible(true);
	 else setVisible(false);			 
}

/**
 * 是否使用執行按鈕
 * @param isVisible
 */
public void setRunButtonVisible(boolean isVisible){
	 this.runBu.setVisible(isVisible);	
	 this.runBu.setEnabled(true);
	 if (this.runBu.isVisible())iButton++;
	 else iButton--;
	 if (iButton > 0)    	setVisible(true);
	 else setVisible(false);			 
}

    
 /**
  * 是否使用儲存按鈕
  * @param isVisible
  */
 public void setSaveButtonVisible(boolean isVisible){	 
	 if (this.userActionCode.indexOf("U")>-1 || this.userActionCode.indexOf("A")>-1){
		 this.saveBu.setVisible(isVisible);	
		 this.saveBu.setEnabled(true);
		 if (this.saveBu.isVisible())iButton++;
		 else iButton--;
		 if (iButton > 0)    	setVisible(true);
		 else setVisible(false);
	 }
 }	
 /**
  * 是否使用新增按鈕
  * @param isVisible
  */
 public void setAddButtonVisible(boolean isVisible){
	 if (this.userActionCode.indexOf("A")>-1){
		 this.addBu.setVisible(isVisible);	
		 this.addBu.setEnabled(true);
		 if (this.addBu.isVisible())iButton++;
		 else iButton--;
		 if (iButton > 0)    	setVisible(true);
		 else setVisible(false);			 
	 }
 }
 /**
  * 是否使用修改按鈕
  * @param isVisible
  */
 public void setEditButtonVisible(boolean isVisible){
	 if (this.userActionCode.indexOf("U")>-1){
		 this.editBu.setVisible(isVisible);	
		 this.editBu.setEnabled(true);
		 if (this.editBu.isVisible())iButton++;
		 else iButton--;
		 if (iButton > 0)    	setVisible(true);
		 else setVisible(false);			 
	 }
 }
 /**
  * 是否使用刪除按鈕
  * @param isVisible
  */	 
 public void setDelButtonVisible(boolean isVisible){
	 if (this.userActionCode.indexOf("D")>-1){		 
		 this.delBu.setVisible(isVisible);	
		 this.delBu.setEnabled(true);
		 if (this.delBu.isVisible())iButton++;
		 else iButton--;
		 if (iButton > 0)    	setVisible(true);
		 else setVisible(false);			 
	 }
 }
 /**
  * 是否使用核可按鈕
  * @param isVisible
  */	 
 public void setCheckButtonVisible(boolean isVisible){
	 if (this.userActionCode.indexOf("K")>-1){		 
		 this.checkBu.setVisible(isVisible);	
		 this.checkBu.setEnabled(true);
		 if (this.checkBu.isVisible())iButton++;
		 else iButton--;
		 if (iButton > 0)    	setVisible(true);
		 else setVisible(false);			 
	 }
 }
 /**
  * 是否使用圖表按鈕
  * @param isVisible
  */	 
 public void setChartButtonVisible(boolean isVisible){
	 if (this.userActionCode.indexOf("C")>-1){		 
		 this.chartBu.setVisible(isVisible);	
		 this.chartBu.setEnabled(true);
		 if (this.chartBu.isVisible())iButton++;
		 else iButton--;
		 if (iButton > 0)    	setVisible(true);
		 else setVisible(false);			 
	 }
 }	   
 
 /**
  * 是否使用列印按鈕
  * @param isVisible
  */	 
 public void setPrintButtonVisible(boolean isVisible){
		 this.printBu.setVisible(isVisible);	
		 this.printBu.setEnabled(true);
		 if (this.printBu.isVisible())iButton++;
		 else iButton--;
		 if (iButton > 0)    	setVisible(true);
		 else setVisible(false);			 
 }	 
 /**
  * 是否使用訊息按鈕
  * @param isVisible
  */	 
 public void setLogButtonVisible(boolean isVisible){
		 this.logBu.setVisible(isVisible);	
		 this.logBu.setEnabled(true);
		 if (this.logBu.isVisible())iButton++;
		 else iButton--;
		 if (iButton > 0)    	setVisible(true);
		 else setVisible(false);			 
 }	 
 
 public AbsoluteLayout getContent(){
 	return content;
 }
 public void addComponent(Component c){
 	hbGroup.addComponent(c);
 }
 public void addLeftComponent(Component c){
	 	hbleftGroup.addComponent(c);
 }
 
 public void setVisible(boolean visible){
	 content.setVisible(visible);
 }
 public void setSid(String sid){
	 	//不正常簽入
	 	if (session.getAttribute("UsersBean") == null){
	 		session.invalidate();
	 		UI.getCurrent().getPage().reload();
	 	}else{
	 		this.users =(Users)session.getAttribute("UsersBean");
	 		this.userAction =(Hashtable<String,String>)session.getAttribute("UserAction");
	 	}	 
		//取得程式安控
		if (this.userAction != null)	this.userActionCode =this.userAction.get(sid);
		if (this.userActionCode == null) this.userActionCode="";
 }
}

