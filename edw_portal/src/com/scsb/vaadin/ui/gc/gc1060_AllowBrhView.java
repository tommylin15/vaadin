package com.scsb.vaadin.ui.gc;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;

import com.scsb.db.DBAction;
import com.scsb.db.bean.Dept;
import com.scsb.db.bean.Ldapou;
import com.scsb.db.service.DeptService;
import com.scsb.db.service.LdapouService;
import com.scsb.util.DateUtil;
import com.scsb.vaadin.composite.MessageBox;
import com.scsb.vaadin.composite.MessageBox.ButtonType;
import com.scsb.vaadin.composite.MessageBox.EventListener;
import com.scsb.vaadin.composite.ScsbProperty;
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
 * 分行的允視部門
 * @author 3471
 *
 */
public class gc1060_AllowBrhView extends ScsbProperty {
	TabSheet     mainTab    =new TabSheet();	
	Tab  tabProperty; 		
	String sid;
	
	Collection<String> valueCollection=new HashSet<String>();
	String propertyValue;
	String propertyKey;
	String propertyMemo="允視部門";
	public gc1060_AllowBrhView(String sid ,String title ,String propertyValue ,String  propertyKey ,TabSheet mainTab) {
		this.propertyValue=propertyValue;
		this.propertyKey=propertyKey;
		this.propertyKey=propertyKey;
		this.mainTab=mainTab;
		this.sid=sid;
		this.getToolBar().setSid(sid);	
		//全部部門
		LdapouService ldapouSrv =new LdapouService();
		BeanContainer<String,Ldapou> ldapouContainer =ldapouSrv.getSasLdapou_All();
		ldapouContainer.sort(new Object[]{"ou"},new boolean[]{true});
		OptionGroup optionGroup = new OptionGroup();
        for (int i=0;i<ldapouContainer.size();i++) {
    		BeanItem<Ldapou> beanitem = ldapouContainer.getItem(ldapouContainer.getIdByIndex(i));
    		Ldapou ldapou =beanitem.getBean();
    		String CODE_ID =ldapou.getOu();
    		String CODE_NAME =ldapou.getDescription();
    		optionGroup.addItem(CODE_ID);
    		optionGroup.setItemCaption(CODE_ID, ""+CODE_ID+" "+CODE_NAME);
        }//for
		optionGroup.setMultiSelect(true);
		optionGroup.setImmediate(true);
		optionGroup.setHtmlContentAllowed(true);

		//載入該分行的允視部門
		DeptService brhSrv = new DeptService();
		BeanContainer<String,Dept> brhContainer =brhSrv.getDept_PropertyValue(this.propertyValue ,this.propertyKey);
		HashSet<String> brhValue =new HashSet<String>();
		for(int i=0;i<brhContainer.size();i++){
			String ou =brhContainer.getItem(brhContainer.getIdByIndex(i)).getBean().getOu();
			brhValue.add(ou);
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
		addComponent(scButton.hButtonGroup);	
		
		addComponent(optionGroup);		
		//儲存動作
		getToolBar().saveBu.addClickListener(saveBulistener);
		
		String tabCaption =propertyMemo+i18nProps.getProperty("propertySet","-屬性設定");
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
		      	DeptService deptSrv =new DeptService();
		      	deptSrv.deleteDeptByValue(dbAction.connection, propertyValue ,propertyKey);
		        while (itr.hasNext()) {
		        	String ou=(String) itr.next();
					//儲存分行屬性=====begin====================================================
					Dept deptBean =new Dept();
					deptBean.setOu(ou);
					deptBean.setPropertyKey(propertyKey);
					deptBean.setPropertyValue(propertyValue);
					deptBean.setPropertyMemo(propertyMemo);
					deptBean.setUpdateDatetime(DateUtil.getDTS());
					deptBean.setUpdateUser(users.getUserid()); 					        	
					
					if (!deptSrv.updateDeptPropertys(dbAction.connection ,deptBean)){
						errMsg=deptSrv.ErrMsg;
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
				mainTab.removeTab(tabProperty);
			}
		//}//if
    	
    }//	onSaveButton	
}