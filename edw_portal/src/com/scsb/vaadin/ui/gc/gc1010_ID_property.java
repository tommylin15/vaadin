package com.scsb.vaadin.ui.gc;

import com.scsb.db.bean.CcCode;
import com.scsb.db.service.CcCodeService;
import com.scsb.util.DateUtil;
import com.scsb.vaadin.composite.MessageBox;
import com.scsb.vaadin.composite.ScsbPage;
import com.scsb.vaadin.composite.MessageBox.ButtonType;
import com.scsb.vaadin.composite.MessageBox.EventListener;
import com.scsb.vaadin.composite.ScsbProperty;
import com.scsb.vaadin.include.UtilSelect;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Notification.Type;

/**
 * 公用參數明細-屬性設定
 * @author 3471
 *
 */
@SuppressWarnings("serial")
public class gc1010_ID_property extends ScsbProperty {
	CcCode           	dataBean ;
	BeanItem<CcCode> 	dataItem ;	
	FieldGroup          dataBinder;
	
	TextField codeKind =new TextField();
	TextField codeMemo =new TextField();
	TextField codeId =new TextField();
	TextField codeName =new TextField();
	NativeSelect delFlg =new NativeSelect();
	TextField sortOrder =new TextField();
	
	boolean IsInsert=false;
	CcCode srcBean;
	
	TabSheet     mainTab    =new TabSheet();	
	Tab  tabProperty; 
	ScsbPage scsbPage;	
	/**
	 * 公用參數-屬性設定
	 */
	public gc1010_ID_property(String sid ,CcCode orgBean ,boolean IsInsert ,TabSheet mainTab ,	ScsbPage scsbPage) {
		this.srcBean=orgBean;
		this.IsInsert=IsInsert;
		this.mainTab=mainTab;
		this.scsbPage=scsbPage;
		this.getToolBar().setSid(sid);		

		this.delFlg =new UtilSelect().YNSelect("delFlg" ,"刪除註記" ,false);
		DataBinderFactory();
		//欄位編輯====================================================================
		codeKind.setCaption(i18nProps.getProperty("codeKind","代碼種類"));
		codeKind.setId("codeKind");
		if (srcBean.getCodeKind().equals("")){
			codeKind.setReadOnly(false);
			codeKind.setIcon(FontAwesome.PENCIL_SQUARE);
		}else{
			codeKind.setReadOnly(true);
			codeKind.setIcon(FontAwesome.LOCK);
		}
		
		codeKind.setImmediate(true);
		getFormLayout().addComponent(codeKind);
		
		codeMemo.setCaption(i18nProps.getProperty("codeMemo","代碼說明"));
		codeMemo.setId("codeMemo");
		if (srcBean.getCodeKind().equals("")){
			codeMemo.setReadOnly(false);
			codeMemo.setIcon(FontAwesome.PENCIL_SQUARE);
		}else{
			codeMemo.setReadOnly(true);
			codeMemo.setIcon(FontAwesome.LOCK);
		}
		codeMemo.setInputPrompt(messageProps.getProperty("PleaseKeyin","請輸入")+codeMemo.getCaption());
		codeMemo.setImmediate(true);
		getFormLayout().addComponent(codeMemo);		
		
		codeId.setCaption(i18nProps.getProperty("codeId","代碼編號"));
		codeId.setId("codeId");
		codeId.setInputPrompt(messageProps.getProperty("PleaseKeyin","請輸入")+codeId.getCaption());
		codeId.setImmediate(true);
		if (IsInsert){
			codeId.setReadOnly(false);
			codeId.setIcon(FontAwesome.PENCIL_SQUARE);
		}else{
			codeId.setReadOnly(true);
			codeId.setIcon(FontAwesome.LOCK);
		}		
		codeId.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				getToolBar().setSaveButtonVisible(true);
			}
    	});		
		getFormLayout().addComponent(codeId);

		codeName.setCaption(i18nProps.getProperty("codeName","代碼名稱"));
		codeName.setId("codeName");
		codeName.setInputPrompt(messageProps.getProperty("PleaseKeyin","請輸入")+codeName.getCaption());
		codeName.setImmediate(true);
		codeName.setIcon(FontAwesome.PENCIL_SQUARE);
		codeName.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				getToolBar().setSaveButtonVisible(true);
			}
    	});		
		getFormLayout().addComponent(codeName);		
		delFlg.setIcon(FontAwesome.PENCIL_SQUARE);
    	delFlg.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				getToolBar().setSaveButtonVisible(true);
			}
    	});
    	getFormLayout().addComponent(delFlg);

    	//動作=================================================================================================
		//儲存動作
    	getToolBar().saveBu.addClickListener(saveBulistener);
		//可刪除
		if (!IsInsert){
			getToolBar().setDelButtonVisible(true);
			getToolBar().delBu.addClickListener(delBulistener);
		}
		
		String tabCaption =i18nProps.getProperty("propertySet","-屬性設定");
		if (srcBean != null){
			String codeName="";
			if (!srcBean.getCodeName().equals("null")) codeName=srcBean.getCodeName();
			tabCaption =codeName+tabCaption;
		}
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
				if (codeKind.getValue().equals("")){
					Notification.show("["+messageProps.getProperty("msg_title","作業訊息通知")+"]"
							,codeKind.getCaption()+messageProps.getProperty("msg_Not_Null","不可為空值!!")
							,Type.WARNING_MESSAGE);
				}else if (codeMemo.getValue().equals("")){
						Notification.show("["+messageProps.getProperty("msg_title","作業訊息通知")+"]"
							,codeMemo.getCaption()+messageProps.getProperty("msg_Not_Null","不可為空值!!")
							,Type.WARNING_MESSAGE);
				}else if (codeId.getValue().equals("")){
					Notification.show("["+messageProps.getProperty("msg_title","作業訊息通知")+"]"
						,codeId.getCaption()+messageProps.getProperty("msg_Not_Null","不可為空值!!")
						,Type.WARNING_MESSAGE);
				}else if (codeName.getValue().equals("")){
					Notification.show("["+messageProps.getProperty("msg_title","作業訊息通知")+"]"
							,codeName.getCaption()+messageProps.getProperty("msg_Not_Null","不可為空值!!")
							,Type.WARNING_MESSAGE);
				}else{
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
				}
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
	      	CcCodeService ccCodeSrv =new CcCodeService();
	      	if (IsInsert){
		      	if (!ccCodeSrv.insertCcCode(dataBean)){
		      		errMsg=ccCodeSrv.ErrMsg; 
		      	}
	      	}else{
		      	if (!ccCodeSrv.updateCcCode(dataBean)){
					errMsg=ccCodeSrv.ErrMsg;
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
			mainTab.removeComponent(this.tabProperty.getComponent());
			System.out.println("after insert ..mainTab.getComponentCount():"+mainTab.getComponentCount());
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
	      	CcCodeService ccCodeSrv =new CcCodeService();
	      	if (!ccCodeSrv.deleteCcCode(dataBean)){
				errMsg=ccCodeSrv.ErrMsg;
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
		CcCodeService ccCodeSrv =new CcCodeService();
		CcCode queryBean =new CcCode();
		queryBean.setCodeKind(srcBean.getCodeKind());
		queryBean.setCodeId(srcBean.getCodeId());
		dataBean =ccCodeSrv.getCcCode_PK(queryBean);	
		if (IsInsert){
			dataBean.setCodeKind(srcBean.getCodeKind());			
			dataBean.setCodeMemo(srcBean.getCodeMemo());
		}	
		dataItem = new BeanItem<CcCode>(dataBean);
		dataBinder = new FieldGroup(dataItem);
		dataBinder.setBuffered(true);
		dataBinder.bind(codeKind, "codeKind");
		dataBinder.bind(codeMemo, "codeMemo");
		dataBinder.bind(codeId  , "codeId");
		dataBinder.bind(codeName, "codeName");
		dataBinder.bind(delFlg  , "delFlg");		
	 }	
}

