package com.scsb.vaadin.ui.gc;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;

import com.scsb.db.DBAction;
import com.scsb.db.bean.Dept;
import com.scsb.db.bean.Fnbct0;
import com.scsb.db.service.DeptService;
import com.scsb.db.service.Fnbct0Service;
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
 * 單位的允視分行
 * @author 3471
 *
 */
public class gc6013_PropertyView extends ScsbProperty {
	TabSheet     mainTab    =new TabSheet();	
	Tab  tabProperty; 
	String sid;			
	
	Collection<String> valueCollection=new HashSet<String>();
	String ou;
	String propertyKey;
	String propertyMemo="允視分行";
	public gc6013_PropertyView(String sidx ,String title ,String ou ,String  propertyKey ,TabSheet mainTabx) {
		this.ou=ou;
		this.propertyKey=propertyKey;
		this.mainTab=mainTabx;
		this.sid=sidx;
		this.getToolBar().setSid(sid);
		//全部部門
		Fnbct0Service fnbct0Srv =new Fnbct0Service();
		BeanContainer<String,Fnbct0> fnbct0Container =fnbct0Srv.getSasFnbct0_All();
		fnbct0Container.sort(new Object[]{"brhCod"},new boolean[]{true});
		OptionGroup optionGroup = new OptionGroup();
        for (int i=0;i<fnbct0Container.size();i++) {
    		BeanItem<Fnbct0> beanitem = fnbct0Container.getItem(fnbct0Container.getIdByIndex(i));
    		Fnbct0 sasFnbct0 =beanitem.getBean();
    		String CODE_ID =sasFnbct0.getBrhCod();
    		String CODE_NAME =sasFnbct0.getChinAl1();
    		optionGroup.addItem(CODE_ID);
    		optionGroup.setItemCaption(CODE_ID, ""+CODE_ID+" "+CODE_NAME);
        }//for
        //加一個ZZ.全行
		optionGroup.addItem("ZZ");
		optionGroup.setItemCaption("ZZ", "ZZ 全行");
        
		optionGroup.setMultiSelect(true);
		optionGroup.setImmediate(true);
		optionGroup.setHtmlContentAllowed(true);

		//加一個全部清除和全部選取按鈕 for OptionGroup
		SelectAndCancelButton scButton =new SelectAndCancelButton();
		scButton.setOptionGroup(optionGroup);
		getFormLayout().addComponent(scButton.hButtonGroup);	
		
		//載入該部門的允視分行
		DeptService brhSrv = new DeptService();
		BeanContainer<String,Dept> brhContainer =brhSrv.getDept_OuProperty(this.ou ,this.propertyKey);
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
		getFormLayout().addComponent(optionGroup);
		//儲存動作
		getToolBar().saveBu.addClickListener(saveBulistener);
		
		String tabCaption =i18nProps.getProperty("propertySet","-允視分行設定");
		tabCaption =title+tabCaption;
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
		      	deptSrv.deleteDeptByOuKey(dbAction.connection, ou ,propertyKey);
		        while (itr.hasNext()) {
		        	String provertyValue=(String) itr.next();
					//儲存分行屬性=====begin====================================================
					Dept deptBean =new Dept();
					deptBean.setOu(ou);
					deptBean.setPropertyKey(propertyKey);
					deptBean.setPropertyValue(provertyValue);
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
				mainTab.removeTab(this.tabProperty);
			}
		//}//if
    	
    }//	onSaveButton	
}