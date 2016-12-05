package com.scsb.vaadin.ui.gc;

import com.scsb.db.bean.Transd;
import com.scsb.db.service.TransdService;
import com.scsb.util.DateUtil;
import com.scsb.vaadin.composite.MessageBox;
import com.scsb.vaadin.composite.ScsbPage;
import com.scsb.vaadin.composite.ScsbProperty;
import com.scsb.vaadin.composite.MessageBox.ButtonType;
import com.scsb.vaadin.composite.MessageBox.EventListener;
import com.scsb.vaadin.composite.ScsbPropertyButton;
import com.scsb.vaadin.include.CcCodeSelect;
import com.scsb.vaadin.include.UtilOptionGroup;
import com.scsb.vaadin.include.UtilSelect;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.TabSheet.Tab;

/**
 * 交易-屬性設定
 * @author 3471
 *
 */
@SuppressWarnings("serial")
public class gc1020_transD_property extends ScsbProperty {
	Transd           	dataBean ;
	BeanItem<Transd> 	dataItem ;	
	FieldGroup          dataBinder;
	
	TextField groupId =new TextField();
	TextField programId =new TextField();
	TextField programName =new TextField();
	TextField sortKey =new TextField();
	NativeSelect programMode =new NativeSelect();
	NativeSelect confirmMode =new NativeSelect();
	NativeSelect setMode =new NativeSelect();
	NativeSelect authorizeMode =new NativeSelect();
	NativeSelect browseMode =new NativeSelect();
	NativeSelect programType =new NativeSelect();
	NativeSelect deptactionMode =new NativeSelect();
	OptionGroup actionItemList =new OptionGroup();
	
	boolean IsInsert=false;
	Transd srcBean= new Transd();
	
	TabSheet     mainTab ;	
	Tab  tabProperty; 	
	String sid;	
	ScsbPage scsbPage;		
	/**
	 * 交易-屬性設定-主畫面
	 */
	public gc1020_transD_property(String sidx ,Transd orgBean ,boolean IsInsert , TabSheet mainTabx ,	ScsbPage scsbPage) {
		this.srcBean=orgBean;
		this.IsInsert=IsInsert;
		this.mainTab=mainTabx;
		this.sid=sidx;
		this.scsbPage=scsbPage;
		this.getToolBar().setSid(sid);
		
		this.programMode =new UtilSelect().YNSelect("programMode" ,"是否啟用" ,false);
		this.confirmMode =new UtilSelect().doclevelSelect("confirmMode" ,"文件等級" ,false);
		this.setMode =new UtilSelect().YNSelect("setMode" ,"是否有敏感性資料" ,false);
		this.authorizeMode =new UtilSelect().unitSelect("authorizeMode" ,"權責單位" ,false);
		this.browseMode =new UtilSelect().serviceSelect("browseMode" ,"SmartQuery模式" ,false);
		this.programType =new UtilSelect().progSelect("programMode" ,"程式類型" ,false);
		this.deptactionMode =new UtilSelect().deptactionSelect("deptactionMode" ,"單位主管可否授權" ,false);
		this.actionItemList=new UtilOptionGroup().actionOptionGroup();
		//載入資料與欄位對應===================================================================================
		DataBinderFactory();
		groupId.setReadOnly(true);
		if (IsInsert){
			programId.setReadOnly(false);
			programId.setIcon(FontAwesome.PENCIL_SQUARE);
		}else{
			programId.setReadOnly(true);
			programId.setIcon(FontAwesome.LOCK);
		}		
		//欄位編輯====================================================================
		groupId.setCaption(i18nProps.getProperty("groupId","選單代碼"));
		groupId.setId("groupId");
		groupId.setIcon(FontAwesome.PENCIL_SQUARE);
		groupId.setImmediate(true);
		getFormLayout().addComponent(groupId);
		
		programId.setCaption(i18nProps.getProperty("programId","程式代碼"));
		programId.setId("programId");
		programId.setInputPrompt(messageProps.getProperty("PleaseKeyin","請輸入")+programId.getCaption());
		programId.setImmediate(true);
		getFormLayout().addComponent(programId);		
		
		programName.setCaption(i18nProps.getProperty("programName","程式名稱"));
		programName.setId("programName");
		programName.setInputPrompt(messageProps.getProperty("PleaseKeyin","請輸入")+programName.getCaption());
		programName.setImmediate(true);
		programName.setIcon(FontAwesome.PENCIL_SQUARE);
		programName.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				getToolBar().setSaveButtonVisible(true);
			}
    	});		
		getFormLayout().addComponent(programName);
		
		sortKey.setCaption(i18nProps.getProperty("sortKey","排序"));
		sortKey.setId("sortKey");
		sortKey.setInputPrompt(messageProps.getProperty("PleaseKeyin","請輸入")+sortKey.getCaption());
		sortKey.setImmediate(true);
		sortKey.setIcon(FontAwesome.PENCIL_SQUARE);
		sortKey.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				getToolBar().setSaveButtonVisible(true);
			}
    	});		
		getFormLayout().addComponent(sortKey);		
		
		programMode.setIcon(FontAwesome.PENCIL_SQUARE);
    	programMode.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				getToolBar().setSaveButtonVisible(true);
			}
    	});
    	getFormLayout().addComponent(programMode);
    	
    	confirmMode.setIcon(FontAwesome.PENCIL_SQUARE);
    	confirmMode.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				getToolBar().setSaveButtonVisible(true);
			}
    	});
    	getFormLayout().addComponent(confirmMode); 
    	
    	setMode.setIcon(FontAwesome.PENCIL_SQUARE);
    	setMode.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				getToolBar().setSaveButtonVisible(true);
			}
    	});
    	getFormLayout().addComponent(setMode); 
    	
    	authorizeMode.setIcon(FontAwesome.PENCIL_SQUARE);
    	authorizeMode.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				getToolBar().setSaveButtonVisible(true);
			}
    	});
    	getFormLayout().addComponent(authorizeMode); 
    	
    	browseMode.setIcon(FontAwesome.PENCIL_SQUARE);
    	browseMode.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				getToolBar().setSaveButtonVisible(true);
			}
    	});
    	getFormLayout().addComponent(browseMode); 
    	
    	programType.setIcon(FontAwesome.PENCIL_SQUARE);
       	programType.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				getToolBar().setSaveButtonVisible(true);
			}
    	});
       	getFormLayout().addComponent(programType);     	
    	
    	deptactionMode.setIcon(FontAwesome.PENCIL_SQUARE);
       	deptactionMode.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				getToolBar().setSaveButtonVisible(true);
			}
    	});
       	getFormLayout().addComponent(deptactionMode); 
    	
    	//可執行按鈕
    	actionItemList.setIcon(FontAwesome.PENCIL_SQUARE);
    	actionItemList.addStyleName("horizontal");
       	actionItemList.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				getToolBar().setSaveButtonVisible(true);
			}
    	});
       	getFormLayout().addComponent(actionItemList);
    	
		//細項選單1====================================================================
		final ScsbPropertyButton menuBu =new ScsbPropertyButton(i18nProps.getProperty("maskDept","限定使用部門"));        	
    	menuBu.setDescription(messageProps.getProperty("Button_edit_view","編輯/檢視"));
    	menuBu.setId("maskDept");
    	menuBu.addClickListener(new ClickListener() {
			public void buttonClick(ClickEvent event) {
				Transd transdBean =new Transd();
				transdBean.setGroupid(srcBean.getGroupid());
    			gc1020_transD_maskdept maskdept =new gc1020_transD_maskdept(sid ,srcBean.getProgramid() ,"MASKDEPT" ,mainTab);
    			
			}			    		
    	});
		//細項選單2====================================================================
		final ScsbPropertyButton menuBu2 =new ScsbPropertyButton(i18nProps.getProperty("smartQueryReport","SmartQuery報表"));        	
		menuBu2.setDescription(messageProps.getProperty("Button_edit_view","編輯/檢視"));
		menuBu2.setId("smartQueryReport");
		menuBu2.addClickListener(new ClickListener() {
			public void buttonClick(ClickEvent event) {
				gc1020_transD_smartQueryReport smartQueryReport =new gc1020_transD_smartQueryReport(sid ,srcBean.getProgramid() ,"smartQueryReport" ,mainTab);
			}			    		
    	});    	
    	//新增時不可編輯明細
    	if (!IsInsert) 	addComponent(menuBu2);		
    	
    	//動作=================================================================================================
		//儲存動作
    	getToolBar().saveBu.addClickListener(saveBulistener);
		//可刪除
		if (!IsInsert){
			getToolBar().setDelButtonVisible(true);
			getToolBar().delBu.addClickListener(delBulistener);
		} 
		
		String tabCaption =i18nProps.getProperty("propertySet","-屬性設定");
		if (srcBean != null)  tabCaption =srcBean.getProgramname()+tabCaption;
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
					showMsgError(groupId.getCaption()+messageProps.getProperty("msg_Not_Null","不可為空值!!"));
				}else if (programId.getValue().equals("")){
					showMsgError(programId.getCaption()+messageProps.getProperty("msg_Not_Null","不可為空值!!"));
				}else if (programName.getValue().equals("")){
					showMsgError(programName.getCaption()+messageProps.getProperty("msg_Not_Null","不可為空值!!"));
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
				showMsgError(messageProps.getProperty("msg_note","注意")+":"+e.getMessage());
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
	      	TransdService transdSrv =new TransdService();
	      	if (IsInsert){
		      	if (!transdSrv.insertTransd(dataBean)){
		      		errMsg=transdSrv.ErrMsg; 
		      	}
	      	}else{
		      	if (!transdSrv.updateTransd(dataBean)){
					errMsg=transdSrv.ErrMsg;
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
	      	TransdService transdSrv =new TransdService();
	      	if (!transdSrv.deleteTransd(dataBean)){
				errMsg=transdSrv.ErrMsg;
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
    }//	onDelButton	
    
	 /**
	  * Data的Field Factory
	  * @author 3471
	  *
	  */
    private void DataBinderFactory(){
		TransdService transdSrv =new TransdService();
		dataBean =transdSrv.getTransd_PK(srcBean);
		if (IsInsert){
			dataBean.setGroupid(srcBean.getGroupid());
			String pid ="web"+transdSrv.getTransd_MaxSortKey();
			dataBean.setProgramid(pid);
		}
		dataItem = new BeanItem<Transd>(dataBean);
		dataBinder = new FieldGroup(dataItem);
		dataBinder.setBuffered(true);
		dataBinder.bind(groupId, "groupid");
		dataBinder.bind(programId, "programid");
		dataBinder.bind(programName, "programname");
		dataBinder.bind(sortKey, "sortKey");		
		dataBinder.bind(programMode, "programmode");
		dataBinder.bind(confirmMode, "confirmmode");
		dataBinder.bind(setMode, "setmode");
		dataBinder.bind(authorizeMode, "authorizemode");
		dataBinder.bind(browseMode, "browsemode");
		dataBinder.bind(programType, "programtype");
		dataBinder.bind(deptactionMode, "deptactionmode");
		dataBinder.bind(actionItemList, "actionItemList");
		
	 }	
}
