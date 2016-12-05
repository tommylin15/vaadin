package com.scsb.vaadin.ui.gc;

import com.scsb.db.bean.Trans;
import com.scsb.db.bean.Transd;
import com.scsb.db.service.TransService;
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
public class gc1020_property extends ScsbProperty {
	Trans           	dataBean ;
	BeanItem<Trans> 	dataItem ;	
	FieldGroup          dataBinder;
	
	TextField groupId =new TextField();
	TextField groupName =new TextField();
	NativeSelect groupMode =new NativeSelect();
	
	Trans srcBean=new Trans();
	boolean IsInsert=false;
	
	TabSheet     mainTab ;	
	Tab  tabProperty; 	
	String sid;
	ScsbPage scsbPage;	
	/**
	 * 交易-屬性設定-主畫面
	 */
	public gc1020_property(String sidx ,Trans orgBean ,boolean IsInsert , TabSheet mainTabx ,ScsbPage scsbPage) {
		this.srcBean =orgBean;
		this.IsInsert=IsInsert;
		this.mainTab=mainTabx;
		this.sid=sidx;
		this.scsbPage =scsbPage;
		this.getToolBar().setSid(sid);		

		this.groupMode =new UtilSelect().YNSelect("groupMode" ,i18nProps.getProperty("groupMode","是否啟用") ,false);
		
    	//載入資料
    	DataBinderFactory();		
		//欄位編輯====================================================================
		groupId.setId("groupId");
		groupId.setCaption(i18nProps.getProperty("groupId","選單代碼"));
		if (IsInsert){
			groupId.setReadOnly(false);
			groupId.setIcon(FontAwesome.PENCIL_SQUARE);
		}else{
			groupId.setReadOnly(true);
			groupId.setIcon(FontAwesome.LOCK);
		}
		groupId.setImmediate(true);
		groupId.setInputPrompt(messageProps.getProperty("PleaseKeyin","請輸入")+groupId.getCaption());
		getFormLayout().addComponent(groupId);
		
		groupName.setId("groupName");
		groupName.setCaption(i18nProps.getProperty("groupName","選單名稱"));
		groupName.setInputPrompt(messageProps.getProperty("PleaseKeyin","請輸入")+groupName.getCaption());
		groupName.setImmediate(true);
		groupName.setIcon(FontAwesome.PENCIL_SQUARE);
		groupName.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				getToolBar().setSaveButtonVisible(true);
			}
    	});
		getFormLayout().addComponent(groupName);			
		groupMode.setIcon(FontAwesome.PENCIL_SQUARE);
    	groupMode.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				getToolBar().setSaveButtonVisible(true);
			}
    	});
    	getFormLayout().addComponent(groupMode);
		//儲存動作
    	getToolBar().saveBu.addClickListener(saveBulistener);
		//可刪除
		if (!IsInsert){
			getToolBar().setDelButtonVisible(true);
			getToolBar().delBu.addClickListener(delBulistener);
		}    	
		//細項選單====================================================================
		final ScsbPropertyButton menuBu =new ScsbPropertyButton(i18nProps.getProperty("menuBu","細項選單"));        	
    	menuBu.setDescription(messageProps.getProperty("Button_edit_view","編輯/檢視"));
    	menuBu.setId("transD");
    	menuBu.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				Transd transdBean =new Transd();
				transdBean.setGroupid(srcBean.getGroupid());
    			new gc1020_transD(sid ,transdBean ,mainTab);
			}
    	});
    	//新增時不可編輯明細
    	if (!IsInsert){
    		addComponent(menuBu);
    	}
    	
		String tabCaption =i18nProps.getProperty("propertySet","-屬性設定");
		if (srcBean != null)  tabCaption =srcBean.getGroupname()+tabCaption;
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
				if (groupId.getValue().equals("")){
					Notification.show("["+messageProps.getProperty("msg_title","作業訊息通知")+"]"
							,groupId.getCaption()+messageProps.getProperty("msg_Not_Null","不可為空值!!")
							,Type.WARNING_MESSAGE);
				}else if (groupName.getValue().equals("")){
						Notification.show("["+messageProps.getProperty("msg_title","作業訊息通知")+"]"
							,groupName.getCaption()+messageProps.getProperty("msg_Not_Null","不可為空值!!")
							,Type.WARNING_MESSAGE);
				}else if (groupMode.getValue().equals(null)){
					Notification.show("["+messageProps.getProperty("msg_title","作業訊息通知")+"]"
							,groupMode.getCaption()+messageProps.getProperty("msg_Not_Null","不可為空值!!")
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
	      	TransService transSrv =new TransService();
	      	if (IsInsert){
		      	if (!transSrv.insertTrans(dataBean)){
		      		errMsg=transSrv.ErrMsg; 
		      	}
	      	}else{
		      	if (!transSrv.updateTrans(dataBean)){
					errMsg=transSrv.ErrMsg;
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
	      	TransService transSrv =new TransService();
	      	if (!transSrv.deleteTrans(dataBean)){
				errMsg=transSrv.ErrMsg;
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
		TransService transSrv =new TransService();
		dataBean =transSrv.getTrans_PKs(srcBean.getGroupid());
		dataItem = new BeanItem<Trans>(dataBean);
		dataBinder = new FieldGroup(dataItem);
		dataBinder.setBuffered(true);    	    		
		dataBinder.bind(groupId, "groupid");
		dataBinder.bind(groupName, "groupname");
		dataBinder.bind(groupMode, "groupmode");
	 }	
}
