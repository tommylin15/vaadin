package com.scsb.vaadin.ui.gc;

import com.scsb.db.bean.Trans;
import com.scsb.db.bean.Users;
import com.scsb.db.bean.Usersa;
import com.scsb.db.service.UsersService;
import com.scsb.db.service.UsersaService;
import com.scsb.util.DateUtil;
import com.scsb.vaadin.composite.MessageBox;
import com.scsb.vaadin.composite.ScsbPage;
import com.scsb.vaadin.composite.ScsbProperty;
import com.scsb.vaadin.composite.MessageBox.ButtonType;
import com.scsb.vaadin.composite.MessageBox.EventListener;
import com.scsb.vaadin.composite.ScsbPropertyButton;
import com.scsb.vaadin.include.TitleLayout;
import com.scsb.vaadin.include.UtilOptionGroup;
import com.scsb.vaadin.include.UtilSelect;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.TabSheet.Tab;

/**
 * 使用者-屬性設定
 * @author 3471
 *
 */
@SuppressWarnings("serial")
public class gc6020_property extends ScsbProperty {
	
	Users           	dataBean ;
	BeanItem<Users> 	dataItem ;	
	FieldGroup          dataBinder;
	
	TextField userId =new TextField();
	TextField userName =new TextField();
	TextField userLevel =new TextField();
	NativeSelect accountMode =new NativeSelect();
	
	boolean IsInsert=false;
	Users srcBean =new Users();
	Usersa usersaBean =new Usersa();
	
	TabSheet     mainTab    =new TabSheet();	
	Tab  tabProperty; 
	ScsbPage scsbPage;
	String sid;			
	/**
	 * 使用者-屬性設定-主畫面
	 */
	public gc6020_property(String sidx ,Users orgBean ,boolean IsInsert ,TabSheet mainTabx ,ScsbPage scsbPage) {
		this.srcBean=orgBean;
		DataBinderFactory();
		this.IsInsert=IsInsert;
		this.mainTab=mainTabx;
		this.sid=sidx;
		this.scsbPage=scsbPage;
		this.getToolBar().setSid(sid);		
		
		UsersaService usersaSrv =new UsersaService();
		usersaBean.setUserid(dataBean.getUserid());
		usersaBean =usersaSrv.getUsersa_PK(usersaBean);
		
		//欄位編輯====================================================================
		userId.setId("userId");
		userId.setCaption(i18nProps.getProperty("userId","使用者行編"));
		userId.setReadOnly(true);
		userId.setImmediate(true);
		userId.setIcon(FontAwesome.LOCK);
		getFormLayout().addComponent(userId);
		
		userName.setId("userName");
		userName.setCaption(i18nProps.getProperty("userName","使用者姓名"));
		userName.setReadOnly(true);
		userName.setImmediate(true);
		userName.setIcon(FontAwesome.LOCK);
		getFormLayout().addComponent(userName);			
		
		userLevel.setId("userLevel");
		userLevel.setCaption(i18nProps.getProperty("userLevel","使用者職位"));
		userLevel.setReadOnly(true);
		userLevel.setImmediate(true);
		userLevel.setIcon(FontAwesome.LOCK);
		getFormLayout().addComponent(userLevel);		
		
		accountMode =new UtilSelect().accountModeSelect("accountMode" ,"使用者狀態" ,false);
		accountMode.setValue(usersaBean.getAccountmode());
		accountMode.setIcon(FontAwesome.PENCIL_SQUARE);
		accountMode.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				getToolBar().setSaveButtonVisible(true);
			}
    	});
		getFormLayout().addComponent(accountMode);				
		
		//個人所屬角色=========================================================================
		final ScsbPropertyButton menuRoles =new ScsbPropertyButton(i18nProps.getProperty("roles","所屬角色"));        	
		menuRoles.setDescription(messageProps.getProperty("Button_edit_view","編輯/檢視"));
		menuRoles.setId("ROLES");
    	menuRoles.addClickListener(new ClickListener() {
    		@Override
			public void buttonClick(ClickEvent event) {
    			//全部角色
    			OptionGroup optionGroup =new UtilOptionGroup().getRolesOptionGroup();
				new gc6020_PropertyView(sid ,srcBean.getUserName()+"-"+menuRoles.getCaption() ,srcBean.getUserid() ,menuRoles.getId() ,optionGroup ,mainTab);
			}			    		
    	});
    	addComponent(menuRoles);
		//個人的允視分行=========================================================================
		final ScsbPropertyButton menuBrh =new ScsbPropertyButton(i18nProps.getProperty("allowBrh","允視分行"));        	
		menuBrh.setDescription(messageProps.getProperty("Button_edit_view","編輯/檢視"));
		menuBrh.setId("ALLOW_BRH");
    	menuBrh.addClickListener(new ClickListener() {
    		@Override
			public void buttonClick(ClickEvent event) {
    			//全行列表
    			OptionGroup optionGroup =new UtilOptionGroup().getBrhOptionGroup(true);
				new gc6020_PropertyView(sid ,srcBean.getUserName()+"-"+menuBrh.getCaption() ,srcBean.getUserid() ,menuBrh.getId() ,optionGroup ,mainTab);
			}			    		
    	});
    	addComponent(menuBrh);
		//個人的允視區域=========================================================================
		final ScsbPropertyButton menuArea =new ScsbPropertyButton(i18nProps.getProperty("allowArea","允視區域"));        	
		menuArea.setDescription(messageProps.getProperty("Button_edit_view","編輯/檢視"));
		menuArea.setId("ALLOW_AREA");
		menuArea.addClickListener(new ClickListener() {
    		@Override
			public void buttonClick(ClickEvent event) {
    			//全行區域列表
    			OptionGroup optionGroup =new UtilOptionGroup().getAreaOptionGroup(true);
				new gc6020_PropertyView(sid ,srcBean.getUserName()+"-"+menuArea.getCaption() ,srcBean.getUserid() ,menuArea.getId() ,optionGroup,mainTab);
			}			    		
    	});
		addComponent(menuArea);
    	
		//個人的敏感性資料授權=========================================================================
		final ScsbPropertyButton menuMask =new ScsbPropertyButton(i18nProps.getProperty("allowBrh","敏感性資料授權"));        	
		menuMask.setDescription(messageProps.getProperty("Button_edit_view","編輯/檢視"));
		menuMask.setId("MASK");
    	menuMask.addClickListener(new ClickListener() {
    		@Override
			public void buttonClick(ClickEvent event) {
    			//細項功能列表
    			OptionGroup optionGroup =new UtilOptionGroup().getTransOptionGroup();
				new gc6020_PropertyView(sid ,srcBean.getUserName()+"-"+menuMask.getCaption() ,srcBean.getUserid() ,menuMask.getId() ,optionGroup,mainTab);
			}			    		
    	});
    	addComponent(menuMask);		
    	
		//細項選單====================================================================
    	//新增時不可編輯明細
    	if (!IsInsert){
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
			    			new gc6020_usersD(sid ,srcBean ,bean ,mainTab);
						}			    		
			    	});
			    	addComponent(menuBu);
				}//if
			}//for
		}//if
    	//動作=================================================================================================
    	//儲存動作
    	getToolBar().saveBu.addClickListener(saveBulistener);
    	
		String tabCaption =i18nProps.getProperty("propertySet","-屬性設定");
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
		                    	accountMode.commit();
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
		dataBean.setUpdateDatetime(DateUtil.getDTS());
		dataBean.setUpdateUser(users.getUserid()); 					        	
		try {
			UsersaService usersaSrv =new UsersaService();
			usersaBean.setAccountmode(accountMode.getValue()+"");
	      	if (!usersaSrv.updateUsersa(usersaBean)){
				errMsg=usersaSrv.ErrMsg;
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
		UsersService usersSrv =new UsersService();
		dataBean =usersSrv.getUsers_PK(srcBean);
		dataItem = new BeanItem<Users>(dataBean);
		dataBinder = new FieldGroup(dataItem);
		dataBinder.setBuffered(true);    	    		
		dataBinder.bind(userId, "userid");
		dataBinder.bind(userName, "userName");
		dataBinder.bind(userLevel, "userlevel");
	 }	
}
