package com.scsb.vaadin.ui.gc;

import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;

import org.apache.commons.math3.util.MultidimensionalCounter.Iterator;

import com.scsb.db.DBAction;
import com.scsb.db.bean.Fnbct0;
import com.scsb.db.bean.Trans;
import com.scsb.db.bean.Transd;
import com.scsb.db.bean.Users;
import com.scsb.db.bean.Usersd;
import com.scsb.db.service.Fnbct0Service;
import com.scsb.db.service.UsersdService;
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
 * 使用者-可用權限設定
 * @author 3471
 *
 */
@SuppressWarnings("serial")
public class gc6120_usersD extends ScsbProperty {
	TabSheet     mainTab    =new TabSheet();	
	Tab  tabProperty; 
	String sid;
	
	Users srcBean =new Users();
	Trans transBean=new Trans();
	HashSet<OptionGroup> deatilData =new HashSet<OptionGroup>();
	/**
	 * 使用者-可用權限設定-主畫面
	 */
	public gc6120_usersD(String sidx ,Users orgBean ,Trans orgTrans ,TabSheet mainTabx) {
		this.srcBean=orgBean;
		this.transBean=orgTrans;
		this.mainTab=mainTabx;
		this.sid=sidx;
		this.getToolBar().setSid(sid);				
		create();
	}
	public void create(){
		//舊記錄
		UsersdService usersdSrv =new UsersdService();
		Usersd usersdbean =new Usersd();
		usersdbean.setUserid(srcBean.getUserid());
		BeanContainer<String,Usersd> usersdContainer =usersdSrv.getUsersd_ID(usersdbean);
		
		OptionGroup itemGroup =new UtilOptionGroup().actionOptionGroup();
		
		//加一個全部清除和全部選取按鈕 for OptionGroup
		SelectAndCancelButton scButton =new SelectAndCancelButton();
		scButton.setOptionGroup(deatilData);
		getFormLayout().addComponent(scButton.hButtonGroup);	
		
		//先判斷被授權人員是否為營業單位人員(暫時只能用部門前二碼去FNBCT0判斷)
		Hashtable<String,Transd> transdContainer = hashTrans.getAllTransDL(transBean.getGroupid());
		Fnbct0Service sasFnbct0Srv = new Fnbct0Service();
		Fnbct0 sasFnbct0 =new Fnbct0();
		sasFnbct0.setBrhCod(srcBean.getDeptid().substring(0,2));
		BeanContainer<String,Fnbct0> sasFnbct0Container =sasFnbct0Srv.getSasFnbct0_PKs(sasFnbct0);
		if (sasFnbct0Container.size() > 0){
			Fnbct0 beanSasFnbct0 =sasFnbct0Container.getItem(sasFnbct0Container.getIdByIndex(0)).getBean();
			//營業單位
			if (beanSasFnbct0.getBrhTyp().equals("2"))  transdContainer = hashTrans.getAllTransDP(transBean.getGroupid());
		}
		
		//明細功能 for 單位主管
		for(java.util.Iterator<String>  iter=transdContainer.keySet().iterator();iter.hasNext();){
			Transd transdBean =transdContainer.get(iter.next());
			Collection collitem = transdBean.getActionItemList();
			if (collitem.size() > 0){
				OptionGroup detail =new OptionGroup();
				detail.setMultiSelect(true);
				detail.addStyleName("horizontal");
				detail.setImmediate(true);
				for(java.util.Iterator iterx=collitem.iterator();iterx.hasNext();){
				    //單位主管僅可授權A新增U修改D刪除Q查詢C圖表...其它權限由資管授權
					String spId ="AUDQC";
					String id =(String)iterx.next();
				    if (spId.indexOf(id) >=-1){
						detail.addItem(id);
						detail.setItemCaption(id, itemGroup.getItemCaption(id));				    	
				    }//if
				}//for
				detail.setImmediate(true);
				detail.setCaption(transdBean.getProgramname());
				detail.setId(transdBean.getProgramid());
				getFormLayout().addComponent(detail);
				//舊記錄
	    		for(java.util.Iterator<String> iterx=usersdContainer.getItemIds().iterator();iterx.hasNext();){
	    			Usersd bean =usersdContainer.getItem(iterx.next()).getBean();
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
		
		String tabCaption =i18nProps.getProperty("transSet","-可用功能設定");
		if (srcBean != null)  tabCaption =srcBean.getUserName()+tabCaption;
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
			HashSet<Usersd> beanList =new HashSet<Usersd>();
			for(java.util.Iterator<OptionGroup> iter=deatilData.iterator();iter.hasNext();){
				OptionGroup field =iter.next();
				Usersd usersd =new Usersd();
				usersd.setProgramid(field.getId());
				usersd.setUserid(srcBean.getUserid());
				usersd.setActionItemList((Collection)field.getValue());
				usersd.setUpdateDatetime(DateUtil.getDTS());
				usersd.setUpdateUser(users.getUserid()); 					        	
				
				if (usersd.getActionItemList().size() > 0)	beanList.add(usersd);
			}
       		//啟用transaction模式
			dbAction.connection.setAutoCommit(false);
			UsersdService usersdSrv =new UsersdService();
	      	if (!usersdSrv.updateUsersdByUseridGroupId(dbAction.connection ,srcBean.getUserid() ,transBean.getGroupid() ,beanList)){
				errMsg=usersdSrv.ErrMsg;
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