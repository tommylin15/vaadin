package com.scsb.vaadin.ui.gc;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;

import com.scsb.db.DBAction;
import com.scsb.db.bean.Ldapou;
import com.scsb.db.bean.Transp;
import com.scsb.db.service.LdapouService;
import com.scsb.db.service.TranspService;
import com.scsb.util.DateUtil;
import com.scsb.vaadin.composite.MessageBox;
import com.scsb.vaadin.composite.ScsbProperty;
import com.scsb.vaadin.composite.ScsbGlobView;
import com.scsb.vaadin.composite.MessageBox.ButtonType;
import com.scsb.vaadin.composite.MessageBox.EventListener;
import com.scsb.vaadin.include.SelectAndCancelButton;
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
 * 交易功能-限定部門
 * @author 3471
 *
 */
public class gc1020_transD_maskdept extends ScsbProperty {
	
	Collection<String> valueCollection=new HashSet<String>();
	String programid;
	String propertyKey;
	String propertyMemo="限定部門";
	
	TabSheet     mainTab ;	
	Tab  tabProperty; 	
	String sid;		
	
	public gc1020_transD_maskdept(String sidx  ,String programid ,String propertyKey ,TabSheet mainTabx ) {
		this.programid=programid;
		this.propertyKey=propertyKey;
		this.mainTab=mainTabx;
		this.sid=sidx;
		this.getToolBar().setSid(sid);		
		//全部部門
		LdapouService ldapouSrv =new LdapouService();
		BeanContainer<String,Ldapou> ldapouContainer =ldapouSrv.getSasLdapou_All();
		ldapouContainer.sort(new Object[]{"ou"},new boolean[]{true});
		
		final OptionGroup optionGroup = new OptionGroup(i18nProps.getProperty("ou","限定部門"));
        for (int i=0;i<ldapouContainer.size();i++) {
    		BeanItem<Ldapou> beanitem = ldapouContainer.getItem(ldapouContainer.getIdByIndex(i));
    		Ldapou sasLdapou =beanitem.getBean();
    		String CODE_ID =sasLdapou.getOu();
    		String CODE_NAME =sasLdapou.getDescription();
    		optionGroup.addItem(CODE_ID);
    		optionGroup.setItemCaption(CODE_ID, ""+CODE_ID+" "+CODE_NAME);
        }//for
		optionGroup.setMultiSelect(true);
		optionGroup.setImmediate(true);
		optionGroup.setHtmlContentAllowed(true);

		//載入該交易的限定分行
		TranspService transpSrv = new TranspService();
		BeanContainer<String,Transp> progContainer =transpSrv.getTransp_PKs(this.programid,this.propertyKey);
		HashSet<String> progValue =new HashSet<String>();
		for(int i=0;i<progContainer.size();i++){
			String keyValue =progContainer.getItem(progContainer.getIdByIndex(i)).getBean().getPropertyValue();
			progValue.add(keyValue);
		}
		optionGroup.setValue(progValue);
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
		setCaption(tabCaption);
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
		      	//先刪除,同一屬性的
		      	TranspService transpSrv =new TranspService();
		      	transpSrv.deleteByKeyValue(dbAction.connection, propertyKey ,programid);
		        while (itr.hasNext()) {	        	
		        	String keyValue=(String) itr.next();	        	
					//儲存交易屬性=====begin====================================================
		        	Transp transpBean =new Transp();
		        	transpBean.setProgramid(programid);
		        	transpBean.setPropertyKey(propertyKey);
		        	transpBean.setPropertyValue(keyValue);
		        	transpBean.setPropertyMemo("限定部門");
		        	transpBean.setUpdateDatetime(DateUtil.getDTS());
		        	transpBean.setUpdateUser(users.getUserid()); 					        	
					if (!transpSrv.updateProperty(dbAction.connection ,transpBean)){
						errMsg=transpSrv.ErrMsg;
					}
					//儲存交易屬性=====end====================================================		        	
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
			}
		//}//if
    	
    }//	onSaveButton	
}