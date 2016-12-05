package com.scsb.vaadin.ui.gc;

import java.util.Collection;
import java.util.HashSet;

import com.scsb.db.DBAction;
import com.scsb.db.bean.Roles;
import com.scsb.db.bean.Rolesd;
import com.scsb.db.bean.Trans;
import com.scsb.db.bean.Transd;
import com.scsb.db.service.RolesdService;
import com.scsb.domain.HashTrans;
import com.scsb.util.DateUtil;
import com.scsb.vaadin.composite.MessageBox;
import com.scsb.vaadin.composite.ScsbProperty;
import com.scsb.vaadin.composite.MessageBox.ButtonType;
import com.scsb.vaadin.composite.MessageBox.EventListener;
import com.scsb.vaadin.include.SelectAndCancelButton;
import com.scsb.vaadin.include.UtilOptionGroup;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.TabSheet.Tab;

/**
 * 角色明細設定
 * @author 3471
 *
 */
@SuppressWarnings("serial")
public class gc6010_rolesD extends ScsbProperty {
	TabSheet     mainTab    =new TabSheet();	
	Tab  tabProperty; 		
	String sid;
	
	Roles srcBean =new Roles();
	Trans transBean=new Trans();
	HashSet<OptionGroup> deatilData =new HashSet<OptionGroup>();
	String propertyMemo="角色明細";
	/**
	 * 角色明細設定-主畫面
	 */
	public gc6010_rolesD(String sid ,Roles orgBean ,Trans orgTrans ,TabSheet mainTab) {
		this.srcBean=orgBean;
		this.transBean=orgTrans;
		this.mainTab=mainTab;
		this.sid=sid;
		this.getToolBar().setSid(sid);			
		create();
	}
	public void create(){
		//舊記錄
		RolesdService rolesdSrv =new RolesdService();
		Rolesd rolesdbean =new Rolesd();
		rolesdbean.setRoleid(srcBean.getRoleid());
		BeanContainer<String,Rolesd> rolesdContainer =rolesdSrv.getRolesd_id(rolesdbean);
		
		OptionGroup itemGroup =new UtilOptionGroup().actionOptionGroup();
		
		//加一個全部清除和全部選取按鈕 for OptionGroup
		SelectAndCancelButton scButton =new SelectAndCancelButton();
		scButton.setOptionGroup(deatilData);
		getFormLayout().addComponent(scButton.hButtonGroup);	
		
		//明細功能
		BeanContainer<String,Transd> transdContainer = hashTrans.getAllTransD(transBean.getGroupid());
		transdContainer.sort(new Object[]{"programid"}, new boolean[]{true});
		for(int i=0;i<transdContainer.size();i++){
			Transd transdBean=transdContainer.getItem(transdContainer.getIdByIndex(i)).getBean();
			Collection collitem = transdBean.getActionItemList();
			if (collitem.size() > 0){
				OptionGroup detail =new OptionGroup();
				detail.setMultiSelect(true);
				detail.addStyleName("horizontal");
				for(java.util.Iterator iter=collitem.iterator();iter.hasNext();){
					String id =(String)iter.next();
					detail.addItem(id);
					detail.setItemCaption(id, itemGroup.getItemCaption(id));
				}						
				detail.setImmediate(true);
				detail.setCaption(transdBean.getProgramname());
				detail.setId(transdBean.getProgramid());
				getFormLayout().addComponent(detail);
				//舊記錄
	    		for(java.util.Iterator<String> iter=rolesdContainer.getItemIds().iterator();iter.hasNext();){
	    			Rolesd bean =rolesdContainer.getItem(iter.next()).getBean();
	    			String id=bean.getProgramid();
	    			if (detail.getId().equals(bean.getProgramid())){
	    				Collection actionCodeList =bean.getActionItemList();		    				
	    				detail.setValue(actionCodeList);
	    				break;
	    			}
	    		}	
	    		detail.addValueChangeListener(new ValueChangeListener() {
					@Override
					public void valueChange(ValueChangeEvent event) {
						getToolBar().setSaveButtonVisible(true);
						
					}
	        	});			    		
	    		deatilData.add(detail);
			}//if
		}//for
		//儲存動作
		getToolBar().saveBu.addClickListener(saveBulistener);
		
		String tabCaption =srcBean.getRolename()+i18nProps.getProperty("propertySet","-可用功能設定");
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
		String errMsg="";
   		//啟用transaction模式
		DBAction dbAction =new DBAction();
	    dbAction.getConnect(); 		
		try {
		 	//有明細時
			HashSet<Rolesd> beanList =new HashSet<Rolesd>();
			for(java.util.Iterator<OptionGroup> iter=deatilData.iterator();iter.hasNext();){
				OptionGroup field =iter.next();
				Rolesd rolesd =new Rolesd();
				rolesd.setProgramid(field.getId());
				rolesd.setRoleid(srcBean.getRoleid());
				rolesd.setActionItemList((Collection)field.getValue());
				rolesd.setUpdateDatetime(DateUtil.getDTS());
				rolesd.setUpdateUser(users.getUserid()); 					        	
				
				if (rolesd.getActionItemList().size() > 0)	beanList.add(rolesd);
			}
       		//啟用transaction模式
			dbAction.connection.setAutoCommit(false);
			RolesdService rolesdSrv =new RolesdService();
	      	if (!rolesdSrv.updateRolesdByRoleidGroupId(dbAction.connection ,srcBean.getRoleid() ,transBean.getGroupid() ,beanList)){
			//if (!rolesdSrv.updateRolesdByRoleidGroupId(srcBean.getRoleid() ,transBean.getGroupid() ,beanList)){
				errMsg=rolesdSrv.ErrMsg;
				dbAction.connection.rollback();
	        }else{
	        	dbAction.connection.commit();		 
	        }			
		} catch (Exception e1) {
			e1.printStackTrace();
			errMsg=e1.getMessage();
		} finally {
			dbAction.disConnect();
		}				
		if (!errMsg.equals("")){
			showMsgError(errMsg);
		}else{
			showMsgSuccess();
			mainTab.removeTab(this.tabProperty);
		}
    }//	onSaveButton	
}