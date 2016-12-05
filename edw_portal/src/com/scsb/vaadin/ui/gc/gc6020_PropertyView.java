package com.scsb.vaadin.ui.gc;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;

import com.scsb.db.DBAction;
import com.scsb.db.bean.Dept;
import com.scsb.db.bean.Fnbct0;
import com.scsb.db.bean.Usersp;
import com.scsb.db.service.DeptService;
import com.scsb.db.service.Fnbct0Service;
import com.scsb.db.service.UserspService;
import com.scsb.util.DateUtil;
import com.scsb.vaadin.composite.MessageBox;
import com.scsb.vaadin.composite.ScsbGlobView;
import com.scsb.vaadin.composite.MessageBox.ButtonType;
import com.scsb.vaadin.composite.MessageBox.EventListener;
import com.scsb.vaadin.composite.ScsbProperty;
import com.scsb.vaadin.include.SelectAndCancelButton;
import com.scsb.vaadin.include.UtilOptionGroup;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.TabSheet.Tab;

/**
 * 個人的屬性設定
 * @author 3471
 *
 */
public class gc6020_PropertyView extends ScsbProperty {
	TabSheet     mainTab    =new TabSheet();	
	Tab  tabProperty; 
	String sid;		
	
	Collection<String> valueCollection=new HashSet<String>();
	String userId;
	String propertyKey;
	String propertyMemo="";
	
	public gc6020_PropertyView(String sidx ,String propertyMemo ,String userId ,String  propertyKey ,OptionGroup optionGroup ,TabSheet mainTabx) {
		this.userId=userId;
		this.propertyKey=propertyKey;
		this.propertyMemo=propertyMemo;
		this.mainTab=mainTabx;
		this.sid=sidx;
		this.getToolBar().setSid(sid);				

		//載入該人員的屬性
		UserspService userspSrv = new UserspService();
		BeanContainer<String,Usersp> brhContainer =userspSrv.getUsersp_UserIdProperty(this.userId ,this.propertyKey);
		HashSet<String> brhValue =new HashSet<String>();
		for(int i=0;i<brhContainer.size();i++){
			String provertyValue =brhContainer.getItem(brhContainer.getIdByIndex(i)).getBean().getPropertyValue();
			brhValue.add(provertyValue);
		}
		optionGroup.setValue(brhValue);
		//加上觸發事件
		optionGroup.addValueChangeListener(new ValueChangeListener(){
			@Override
			public void valueChange(ValueChangeEvent event) {
				if (event.getProperty().getValue() instanceof Collection) {
					valueCollection = (Collection<String>)event.getProperty().getValue();
				}
				getToolBar().setSaveButtonVisible(true);
			}
		});
		//加一個全部清除和全部選取按鈕 for OptionGroup
		SelectAndCancelButton scButton =new SelectAndCancelButton();
		scButton.setOptionGroup(optionGroup);
		getFormLayout().addComponent(scButton.hButtonGroup);	
		getFormLayout().addComponent(optionGroup);	
		//儲存動作
		getToolBar().saveBu.addClickListener(saveBulistener);		
		
		String tabCaption =i18nProps.getProperty("propertySet","-屬性設定");
		tabCaption =propertyMemo+tabCaption;
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
	};
	
	 /**
     *  執行儲存動作
	 * @param codeKind 
     */
    public void onSaveButton(){
 		//if (valueCollection.size()>0){
 			String errMsg="";
 			
       		//啟用transaction模式
 			DBAction dbAction =new DBAction();
 		    dbAction.getConnect(); 			
       		try {
				dbAction.connection.setAutoCommit(false);
		      	java.util.Iterator<String> itr = valueCollection.iterator();
		      	//先刪除
		      	UserspService userspSrv =new UserspService();
		      	Usersp bean =new Usersp();
		      	bean.setUserid(userId);
		      	bean.setPropertyKey(propertyKey);
		      	userspSrv.deleteUserspkey(dbAction.connection, bean);
		      	
		        while (itr.hasNext()) {
		        	String provertyValue=(String) itr.next();
					//儲存分行屬性=====begin====================================================
					Usersp userspBean =new Usersp();
					userspBean.setUserid(userId);
					userspBean.setPropertyKey(propertyKey);
					userspBean.setPropertyValue(provertyValue);
					userspBean.setPropertyMemo(propertyMemo);
					userspBean.setUpdateDatetime(DateUtil.getDTS());
					userspBean.setUpdateUser(users.getUserid()); 					        	
					
					if (!userspSrv.updateUserspPropertys(dbAction.connection ,userspBean)){
						errMsg=userspSrv.ErrMsg;
					}
					//儲存分行屬性=====end====================================================		        	
		        }				
				dbAction.connection.commit();
			} catch (Exception e1) {
				try {
					dbAction.connection.rollback();
					e1.printStackTrace();
					errMsg=e1.getMessage();
				} catch (SQLException e) {
					e.printStackTrace();
					errMsg=e.getMessage();
				}
			} finally {
				dbAction.disConnect();
			}				
			if (!errMsg.equals("")){
				showMsgError(errMsg);
			}else{
				showMsgSuccess();
				mainTab.removeTab(this.tabProperty);
			}
		//}//if
    	
    }//	onSaveButton	
}