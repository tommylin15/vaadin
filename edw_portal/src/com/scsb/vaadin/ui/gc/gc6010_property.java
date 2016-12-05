package com.scsb.vaadin.ui.gc;

import com.scsb.db.DBAction;
import com.scsb.db.bean.Roles;
import com.scsb.db.bean.Trans;
import com.scsb.db.service.RolesService;
import com.scsb.util.DateUtil;
import com.scsb.vaadin.composite.MessageBox;
import com.scsb.vaadin.composite.ScsbPage;
import com.scsb.vaadin.composite.ScsbProperty;
import com.scsb.vaadin.composite.MessageBox.ButtonType;
import com.scsb.vaadin.composite.MessageBox.EventListener;
import com.scsb.vaadin.composite.ScsbPropertyButton;
import com.scsb.vaadin.include.TitleLayout;
import com.scsb.vaadin.include.UtilOptionGroup;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TabSheet.Tab;

/**
 * 角色-屬性設定
 * @author 3471
 *
 */
public class gc6010_property extends ScsbProperty {
	TabSheet     mainTab    =new TabSheet();	
	Tab  tabProperty; 
	ScsbPage scsbPage;
	String sid;
	
	Roles           	dataBean ;
	BeanItem<Roles> 	dataItem ;	
	FieldGroup          dataBinder;
	
	TextField rolesId =new TextField();
	TextField rolesName =new TextField();
	
	boolean IsInsert=false;
	Roles srcBean =new Roles();
	/**
	 * 角色-屬性設定-主畫面
	 */
	public gc6010_property(String sidx ,Roles orgBean ,boolean IsInsert ,TabSheet mainTabx ,ScsbPage scsbPage ) {
		this.srcBean=orgBean;
		this.mainTab=mainTabx;
		this.sid=sidx;
		this.scsbPage=scsbPage;
		this.getToolBar().setSid(sid);	
		
		DataBinderFactory();
		this.IsInsert=IsInsert;
		
		//欄位編輯====================================================================
		rolesId.setId("rolesId");
		rolesId.setCaption(i18nProps.getProperty("rolesId","角色代碼"));
		if (IsInsert){
			rolesId.setReadOnly(false);
			rolesId.setIcon(FontAwesome.PENCIL_SQUARE);
		}else{
			rolesId.setReadOnly(true);
			rolesId.setIcon(FontAwesome.LOCK);
		}
		rolesId.setImmediate(true);
		rolesId.setInputPrompt(messageProps.getProperty("PleaseKeyin","請輸入")+rolesId.getCaption());
		rolesId.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				getToolBar().setSaveButtonVisible(true);
			}
    	});			
		getFormLayout().addComponent(rolesId);
		
		rolesName.setId("rolesName");
		rolesName.setCaption(i18nProps.getProperty("rolesName","角色名稱"));
		rolesName.setInputPrompt(messageProps.getProperty("PleaseKeyin","請輸入")+rolesName.getCaption());
		rolesName.setImmediate(true);
		rolesName.setIcon(FontAwesome.PENCIL_SQUARE);
		rolesName.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				getToolBar().setSaveButtonVisible(true);
			}
    	});		
		getFormLayout().addComponent(rolesName);

		//儲存動作
		getToolBar().saveBu.addClickListener(saveBulistener);
		//可刪除
		if (!IsInsert){
			getToolBar().setDelButtonVisible(true);
			getToolBar().delBu.addClickListener(delBulistener);
			//角色的敏感性資料授權=========================================================================
			final ScsbPropertyButton menuMask =new ScsbPropertyButton(i18nProps.getProperty("allowBrh","敏感性資料授權"));        	
			menuMask.setDescription(messageProps.getProperty("Button_edit_view","編輯/檢視"));
			menuMask.setId("MASK");
			menuMask.addClickListener(new ClickListener() {
	    		@Override
				public void buttonClick(ClickEvent event) {
	    			//細項功能列表
	    			OptionGroup optionGroup =new UtilOptionGroup().getTransOptionGroup();
					new gc6010_PropertyView(sid ,srcBean.getRolename()+"-"+menuMask.getCaption() ,srcBean.getRoleid() ,menuMask.getId() ,optionGroup ,mainTab);
				}			    		
	    	});
	    	addComponent(menuMask);		
	    	//細項選單====================================================================
	        //主選單
	        BeanContainer<String,Trans> transContainer = hashTrans.getAllTrans();
	        transContainer.sort(new Object[]{"groupid"},new boolean[]{true});
	        
	        int iFlag=0;
			for(int i=0;i<transContainer.size();i++){
				final Trans bean=transContainer.getItem(transContainer.getIdByIndex(i)).getBean();
		    	//導航選單
				if (bean.getGroupid().indexOf("_") == -1){
					TitleLayout title =new TitleLayout(messageProps.getProperty("label_TransList","權限設定")+"－［"+bean.getGroupname()+"］");
					addComponent(title.vLayout);
					iFlag=0;
				}else{
					iFlag++;
					final ScsbPropertyButton menuBu =new ScsbPropertyButton(i18nProps.getProperty(bean.getGroupid(),bean.getGroupname()));        	
					menuBu.setDescription(messageProps.getProperty("Button_edit_view","編輯/檢視"));
					menuBu.setId(bean.getGroupid());
					if (iFlag % 2 ==1) menuBu.addStyleName("scsbBg2");
					else menuBu.addStyleName("scsbBg3");
					
			    	menuBu.addClickListener(new ClickListener() {
			    		@Override
						public void buttonClick(ClickEvent event) {
			    			new gc6010_rolesD(sid,srcBean ,bean,mainTab);
						}			    		
			    	});
			    	addComponent(menuBu);
				}//if
			}//for
		}//if
		String tabCaption =i18nProps.getProperty("propertySet","-屬性設定");
		if (srcBean != null)  tabCaption =srcBean.getRolename()+tabCaption;
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
				if (rolesId.getValue().equals("")){
					Notification.show("["+messageProps.getProperty("msg_title","作業訊息通知")+"]"
							,rolesId.getCaption()+messageProps.getProperty("msg_Not_Null","不可為空值!!")
							,Type.WARNING_MESSAGE);
				}else if (rolesName.getValue().equals("")){
						Notification.show("["+messageProps.getProperty("msg_title","作業訊息通知")+"]"
							,rolesName.getCaption()+messageProps.getProperty("msg_Not_Null","不可為空值!!")
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
	      	RolesService rolesSrv =new RolesService();
	      	if (IsInsert){
		      	if (!rolesSrv.insertRoles(dataBean)){
		      		errMsg=rolesSrv.ErrMsg; 
		      	}
	      	}else{
		      	if (!rolesSrv.updateRoles(dataBean )){
					errMsg=rolesSrv.ErrMsg;
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
   		//啟用transaction模式
		DBAction dbAction =new DBAction();
	    dbAction.getConnect(); 				
		try {
	      	RolesService rolesSrv =new RolesService();
	      	if (!rolesSrv.deleteRoles(dbAction.connection ,dataBean)){
				errMsg=rolesSrv.ErrMsg;
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
			scsbPage.getToolBar().refreshBu.click();
		}
    }//	onDelButton	
    
	 /**
	  * Data的Field Factory
	  * @author 3471
	  *
	  */
    private void DataBinderFactory(){
		RolesService rolesSrv =new RolesService();
		dataBean =rolesSrv.getRoles_PK(srcBean);
		
		dataItem = new BeanItem<Roles>(dataBean);
		dataBinder = new FieldGroup(dataItem);
		dataBinder.setBuffered(true);    	    		
		dataBinder.bind(rolesId, "roleid");
		dataBinder.bind(rolesName, "rolename");
	 }	
}
