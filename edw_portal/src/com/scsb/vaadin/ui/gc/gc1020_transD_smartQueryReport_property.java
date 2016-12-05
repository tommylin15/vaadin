package com.scsb.vaadin.ui.gc;

import com.scsb.db.bean.Trans;
import com.scsb.db.bean.Transd;
import com.scsb.db.bean.Transp;
import com.scsb.db.service.TransService;
import com.scsb.db.service.TranspService;
import com.scsb.util.DateUtil;
import com.scsb.vaadin.composite.MessageBox;
import com.scsb.vaadin.composite.ScsbPage;
import com.scsb.vaadin.composite.ScsbProperty;
import com.scsb.vaadin.composite.MessageBox.ButtonType;
import com.scsb.vaadin.composite.MessageBox.EventListener;
import com.scsb.vaadin.composite.ScsbPropertyButton;
import com.scsb.vaadin.include.UtilSelect;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TabSheet.Tab;

/**
 * 交易-屬性設定
 * @author 3471
 *
 */
@SuppressWarnings("serial")
public class gc1020_transD_smartQueryReport_property extends ScsbProperty {
	Transp           	dataBean ;
	BeanItem<Transp> 	dataItem ;	
	FieldGroup          dataBinder;
	
	TextField programid =new TextField();
	TextField propertyKey =new TextField();
	TextField propertyValue =new TextField();
	TextField propertyMemo =new TextField();
	
	Transp srcBean=new Transp();
	boolean IsInsert=false;
	
	TabSheet     mainTab ;	
	Tab  tabProperty; 	
	String sid;
	ScsbPage scsbPage;	
	/**
	 * 交易-屬性設定-主畫面
	 */
	public gc1020_transD_smartQueryReport_property(String sidx ,Transp orgBean ,boolean IsInsert , TabSheet mainTabx ,ScsbPage scsbPage) {
		this.srcBean =orgBean;
		this.IsInsert=IsInsert;
		this.mainTab=mainTabx;
		this.sid=sidx;
		this.scsbPage =scsbPage;
		this.getToolBar().setSid(sid);		

    	//載入資料
    	DataBinderFactory();		
		//欄位編輯====================================================================
    	programid.setId("programid");
    	programid.setCaption(i18nProps.getProperty("programid","程式代碼"));
		if (IsInsert){
			programid.setValue(srcBean.getProgramid());
		}
    	programid.setReadOnly(true);
    	programid.setIcon(FontAwesome.LOCK);
		getFormLayout().addComponent(programid);
		
		propertyKey.setId("propertyKey");
		propertyKey.setCaption(i18nProps.getProperty("propertyKey","屬性類別"));
		if (IsInsert){
			propertyKey.setValue(srcBean.getPropertyKey());
		}
		propertyKey.setReadOnly(true);
		propertyKey.setIcon(FontAwesome.LOCK);
		getFormLayout().addComponent(propertyKey);		
		
		propertyValue.setId("propertyValue");
		propertyValue.setCaption(i18nProps.getProperty("propertyValue","SmartQueryReport代碼"));
		propertyValue.setInputPrompt(messageProps.getProperty("PleaseKeyin","請輸入")+propertyValue.getCaption());
		propertyValue.setImmediate(true);
		propertyValue.setIcon(FontAwesome.PENCIL_SQUARE);
		propertyValue.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				getToolBar().setSaveButtonVisible(true);
			}
    	});
		getFormLayout().addComponent(propertyValue);			
		//儲存動作
    	getToolBar().saveBu.addClickListener(saveBulistener);
		//可刪除
		if (!IsInsert){
			getToolBar().setDelButtonVisible(true);
			getToolBar().delBu.addClickListener(delBulistener);
		}    	
    	
		propertyMemo.setId("propertyMemo");
		propertyMemo.setCaption(i18nProps.getProperty("propertyMemo","屬性備註"));
		if (IsInsert){
			propertyMemo.setValue(srcBean.getPropertyMemo());
		}
		propertyMemo.setReadOnly(true);
		propertyKey.setIcon(FontAwesome.LOCK);
		getFormLayout().addComponent(propertyMemo);			
		
		String tabCaption =i18nProps.getProperty("propertySet","-屬性設定");
		if (srcBean != null)  tabCaption =srcBean.getProgramid()+tabCaption;
	    tabProperty =mainTab.addTab(getContent() ,tabCaption);
	    tabProperty.setClosable(true);
	    tabProperty.setIcon(FontAwesome.EDIT);
	    mainTab.setSelectedTab(tabProperty);    	
	}
	
	/**
	 * Listener for 儲存動作
	 */
	ClickListener saveBulistener = new ClickListener() {
		@Override
		public void buttonClick(ClickEvent event) {
			//檢核欄位資料
			try {
				dataBinder.commit();
				new MessageBox(
						messageProps.getProperty("msg_info","作業資訊"),
		                MessageBox.Icon.QUESTION,
		                messageProps.getProperty("msg_issave","是否存檔")+"?",
		                new MessageBox.ButtonConfig(MessageBox.ButtonType.YES, messageProps.getProperty("msg_confirm","確定")),
		                new MessageBox.ButtonConfig(MessageBox.ButtonType.NO, messageProps.getProperty("msg_cancel","取消"))
					).show(	
		                new EventListener() {
		                	@Override
			                public void buttonClicked(ButtonType buttonType) {
			                    if (buttonType == MessageBox.ButtonType.YES) {
			                    	onSaveButton();
			               		}
			               	}
			            }
		            );
			} catch (CommitException e) {
				e.printStackTrace();
				Notification.show("["+messageProps.getProperty("msg_title","作業訊息通知")+"]"
						,messageProps.getProperty("msg_note","注意")+":"+e.getMessage()
						,Type.ERROR_MESSAGE);
			}
		}		
	};
	
	 /**
     *  執行儲存動作
	 * @param codeKind 
     */
    public void onSaveButton(){
		String errMsg="";
		dataBean.setUpdateDatetime(DateUtil.getDTS());
		dataBean.setUpdateUser(users.getUserid());		
		try {
	      	TranspService transpSrv =new TranspService();
	      	if (IsInsert){
		      	if (!transpSrv.insertTransp(dataBean)){
		      		errMsg=transpSrv.ErrMsg; 
		      	}
	      	}else{
		      	if (!transpSrv.updateTransp(dataBean)){
					errMsg=transpSrv.ErrMsg;
		        }
	      	}
		} catch (Exception e1) {
			e1.printStackTrace();
			errMsg=e1.getMessage();
		}				
		if (!errMsg.equals("")){
			showMsgError(errMsg);
		}else{
			showMsgSuccess();
			mainTab.removeTab(this.tabProperty);
			scsbPage.getToolBar().refreshBu.click();
		}
    }//	onSaveButton		
	
	/**
	 * Listener for 刪除動作
	 */
	ClickListener delBulistener = new ClickListener() {
		@Override
		public void buttonClick(ClickEvent event) {
			new MessageBox(
					messageProps.getProperty("msg_info","作業資訊"),
	                MessageBox.Icon.QUESTION,
	                messageProps.getProperty("msg_isdel","是否刪除")+"?",
	                new MessageBox.ButtonConfig(MessageBox.ButtonType.YES, messageProps.getProperty("msg_confirm","確定")),
	                new MessageBox.ButtonConfig(MessageBox.ButtonType.NO, messageProps.getProperty("msg_cancel","取消"))
				).show(	
	                new EventListener() {
	                	@Override
		                public void buttonClicked(ButtonType buttonType) {
		                    if (buttonType == MessageBox.ButtonType.YES) {
		                    	onDelButton();
		               		}
		               	}
		            }
	            );
		}		
	};
	
	 /**
     *  執行刪除動作
	 * @param codeKind 
     */
    public void onDelButton(){
		String errMsg="";
		
		try {
	      	TranspService transpSrv =new TranspService();
	      	if (!transpSrv.deleteTransp(dataBean)){
				errMsg=transpSrv.ErrMsg;
	        }
		} catch (Exception e1) {
			e1.printStackTrace();
			errMsg=e1.getMessage();
		}				
		if (!errMsg.equals("")){
			showMsgError(errMsg);
		}else{
			showMsgSuccess();
			mainTab.removeTab(this.tabProperty);
			scsbPage.getToolBar().refreshBu.click();
		}
    }//	onSaveButton	
    
	 /**
	  * Data的Field Factory
	  * @author 3471
	  *
	  */
    private void DataBinderFactory(){
		TranspService transpSrv =new TranspService();
		
		dataBean =transpSrv.getTransp_PKb(srcBean);
		dataItem = new BeanItem<Transp>(dataBean);
		dataBinder = new FieldGroup(dataItem);
		dataBinder.setBuffered(true);    	    		
		dataBinder.bind(programid, "programid");
		dataBinder.bind(propertyKey, "propertyKey");
		dataBinder.bind(propertyValue, "propertyValue");
		dataBinder.bind(propertyMemo, "propertyMemo");
	 }	
}
