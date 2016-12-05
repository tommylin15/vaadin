package com.scsb.vaadin.ui.sc;

import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;

import org.tepi.filtertable.FilterTable;
import org.tepi.filtertable.paged.PagedFilterTable;
import org.vaadin.aceeditor.AceEditor;
import org.vaadin.dialogs.ConfirmDialog;

import com.scsb.db.DBAction;
import com.scsb.db.bean.Users;
import com.scsb.db.bean.WorkContactListSa;
import com.scsb.db.service.WorkContactListSaService;
import com.scsb.util.DateUtil;
import com.scsb.util.UserAction;
import com.scsb.vaadin.composite.ProgressWindow;
import com.scsb.vaadin.composite.ScsbGlobView;
import com.scsb.vaadin.composite.ScsbPage;
import com.scsb.vaadin.composite.ScsbProperty;
import com.scsb.vaadin.include.TwoSelect;
import com.scsb.vaadin.include.UtilSelect;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TabSheet.Tab;

/**
 * 存款價值戶服務專員異動作業
 * @author 3471
 *
 */
public class sc1000 extends ScsbGlobView {
	/**
	 * 存款價值戶服務專員異動作業-主畫面
	 */
	UtilSelect utilSelect =new UtilSelect();
	
	TabSheet     mainTab    =new TabSheet();
	
	WorkContactListSaService saSrv =new WorkContactListSaService();
	
	String sWorkMode ="change";
	public sc1000() {
		int immdd =Integer.parseInt(DateUtil.getYYYYMMDD().substring(4,8));
		if (immdd >=701 && immdd <=715) sWorkMode="new";
		if (immdd >=101 && immdd <=115) sWorkMode="new";
		
		if (immdd >=616 && immdd <=630) sWorkMode="reset";
		if (immdd >=1216 && immdd <=1231) sWorkMode="reset";
		create();
	    mainTab.setImmediate(true);
		getContent().addComponent(mainTab);				
	}
	public void create(){
		if (tabQuery  != null)	mainTab.removeTab(tabQuery);
		selBrhCod.removeAllItems();
		selDataArea.removeAllItems();
		selIsCheck.removeAllItems();
		queryTab();
	}
	
	/*******Query Tab*************begin********************************************************************************/
	//分行別
	NativeSelect selBrhCod =new NativeSelect();
	//資料區間
	NativeSelect selDataArea =new NativeSelect();
	//狀態
	NativeSelect selIsCheck =new NativeSelect();
	Tab tabQuery ;
	
	public void queryTab(){
		ScsbProperty  queryPanel = new ScsbProperty(messageProps.getProperty("label_Query","查詢條件"));
		queryPanel.getToolBar().setSid(className);
		
		String tabCaption =messageProps.getProperty("home","首頁");
		tabQuery =mainTab.addTab(queryPanel.getContent() ,tabCaption);
		tabQuery.setIcon(FontAwesome.HOME);
		mainTab.setSelectedTab(tabQuery); 
		
		queryPanel.getToolBar().setRunButtonVisible(true);
		queryPanel.getToolBar().runBu.setDescription(messageProps.getProperty("runQuery","執行查詢"));
		queryPanel.getToolBar().runBu.addClickListener(runQueryBulistener);
		
		queryPanel.getToolBar().setEditButtonVisible(true);
		if (sWorkMode.equals("change")){
			queryPanel.getToolBar().editBu.setDescription(i18nProps.getProperty("changeAoCode","服務專員異動作業"));
			queryPanel.getToolBar().editBu.addClickListener(runEditBulistener);
		}
		if (sWorkMode.equals("reset")){
			queryPanel.getToolBar().editBu.setDescription(i18nProps.getProperty("resetAoCode","服務專員撤消作業"));
			queryPanel.getToolBar().editBu.setIcon(FontAwesome.MAGIC);
			queryPanel.getToolBar().editBu.addClickListener(runResetBulistener);
		}
		if (sWorkMode.equals("new")){
			queryPanel.getToolBar().editBu.setDescription(i18nProps.getProperty("newAoCode","服務專員新承接作業"));
			queryPanel.getToolBar().editBu.setIcon(FontAwesome.BUILDING);
			queryPanel.getToolBar().editBu.addClickListener(runEditBulistener);
		}
		
		queryPanel.getToolBar().setCheckButtonVisible(true);
		queryPanel.getToolBar().checkBu.setDescription(i18nProps.getProperty("checkAoCode","審核作業"));
		queryPanel.getToolBar().checkBu.addClickListener(runCheckBulistener);
		//欄位編輯=========================================================
		selBrhCod.setImmediate(true);
		selBrhCod =utilSelect.brhCodSelect("brhCod", messageProps.getProperty("brhCod","分行別"), false, false);
		queryPanel.getFormLayout().addComponent(selBrhCod);
		
		Hashtable<String ,String> inv=saSrv.getWorkContactListSa_invdate();
		
		selDataArea =utilSelect.createSelect("dataArea", i18nProps.getProperty("dataArea","資料區間"), inv, false ,true ,"~");
		
		queryPanel.getFormLayout().addComponent(selDataArea);
		
		selIsCheck.setCaption(i18nProps.getProperty("ischeck","狀態"));
		selIsCheck.setId("dataArea");		
		selIsCheck.setImmediate(true);
		selIsCheck.setNullSelectionAllowed(false);		
		if (sWorkMode.equals("change")){
			selIsCheck.addItem("1");
			selIsCheck.setItemCaption("1","全部");
			selIsCheck.addItem("2");
			selIsCheck.setItemCaption("2","已核可");
			selIsCheck.addItem("3");
			selIsCheck.setItemCaption("3","審核中");
			selIsCheck.select("1");
		}
		if (sWorkMode.equals("reset")){
			selIsCheck.addItem("4");
			selIsCheck.setItemCaption("4","未撤消");		
			selIsCheck.addItem("5");
			selIsCheck.setItemCaption("5","已撤消");
			selIsCheck.addItem("6");
			selIsCheck.setItemCaption("6","審核中");
			selIsCheck.select("4");
		}
		if (sWorkMode.equals("new")){
			selIsCheck.addItem("1");
			selIsCheck.setItemCaption("1","全部");			
			selIsCheck.addItem("7");
			selIsCheck.setItemCaption("7","未認列");		
			selIsCheck.addItem("8");
			selIsCheck.setItemCaption("8","已認列");
			selIsCheck.addItem("3");
			selIsCheck.setItemCaption("3","審核中");
			selIsCheck.select("1");
		}		
		queryPanel.getFormLayout().addComponent(selIsCheck);

		String sMemo="每年的01/01~01/15 ,07/01~07/15為新客戶認列期間 \n"
				+ "每年的06/16~06/30 ,12/16~12/31為服務專員撤消期間";
		AceEditor memo = new AceEditor();
		memo.setCaption("備註");
		memo.setValue(sMemo);
		memo.setWidth("90%");
		memo.setReadOnly(true);
		queryPanel.getFormLayout().addComponent(memo);
		
		//以下為測試時使用
		/*
		NativeSelect test =new NativeSelect();
		test.setCaption("測試用.......");
		test.setNullSelectionAllowed(false);
		test.addItem("change");
		test.setItemCaption("change","日常作業模式");		
		test.addItem("new");
		test.setItemCaption("new","新承作作業模式");		
		test.addItem("reset");
		test.setItemCaption("reset","撤消作業模式");
		test.setValue(sWorkMode);
		
		test.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				sWorkMode =event.getProperty().getValue().toString();
				create();
			}		
		});			
		queryPanel.getFormLayout().addComponent(test);
		*/
		
	}	
	
	/**
	 * Listener for 執行查詢動作
	 */
	ClickListener runQueryBulistener = new ClickListener() {
		@Override
		public void buttonClick(ClickEvent event) {
			if (tabList  != null)	mainTab.removeTab(tabList);
			if (tabEdit  != null)	mainTab.removeTab(tabEdit);
			if (tabReset  != null)	mainTab.removeTab(tabReset);
			if (tabCheck  != null)	mainTab.removeTab(tabCheck);
			listTab();
		}		
	};	
	/**
	 * 執行異動作業
	 */
	ClickListener runEditBulistener = new ClickListener() {
		@Override
		public void buttonClick(ClickEvent event) {
			if (tabList  != null)	mainTab.removeTab(tabList);
			if (tabEdit  != null)	mainTab.removeTab(tabEdit);
			if (tabReset  != null)	mainTab.removeTab(tabReset);
			if (tabCheck  != null)	mainTab.removeTab(tabCheck);
			editTab();   		    							
		}		
	};	
	/**
	 * 執行撤件作業
	 */
	ClickListener runResetBulistener = new ClickListener() {
		@Override
		public void buttonClick(ClickEvent event) {
			if (tabList  != null)	mainTab.removeTab(tabList);
			if (tabEdit  != null)	mainTab.removeTab(tabEdit);
			if (tabReset  != null)	mainTab.removeTab(tabReset);
			if (tabCheck  != null)	mainTab.removeTab(tabCheck);
			resetTab();   		    							
		}		
	};		
		
	/**
	 * 執行審核作業
	 */
	ClickListener runCheckBulistener = new ClickListener() {
		@Override
		public void buttonClick(ClickEvent event) {
			if (tabList  != null)	mainTab.removeTab(tabList);
			if (tabEdit  != null)	mainTab.removeTab(tabEdit);
			if (tabReset  != null)	mainTab.removeTab(tabReset);
			if (tabCheck  != null)	mainTab.removeTab(tabCheck);
			checkTab();   		    							
		}		
	};	
   /*******Query Tab*************end**********************************************************************************/
   
   /*******Data List Tab*************begin******************************************************************************/
	Tab tabList ;
	
	public void listTab(){
		String sBrh =selBrhCod.getValue().toString();
		String sArea =selDataArea.getValue().toString();
		String sIsCheck =selIsCheck.getValue().toString();
		BeanContainer<String,WorkContactListSa>saContainer =saSrv.getWorkContactListSa_brh_area(sBrh ,sArea ,sIsCheck);
		if (saContainer.size() > 0){
			ScsbPage  listPanel = new ScsbPage(saContainer.size());
			String tabCaption =messageProps.getProperty("queryPage","查詢結果");
			tabList =mainTab.addTab(listPanel.getContent() ,tabCaption);
			tabList.setClosable(true);
			tabList.setIcon(FontAwesome.LIST);
			mainTab.setSelectedTab(tabList); 
			
			listPanel.getToolBar().setSid(className);
			//加按鈕
			listPanel.getToolBar().setRefreshButtonVisible(true);
			listPanel.getToolBar().refreshBu.addClickListener(runQueryBulistener);
			
			PagedFilterTable    pageTable     = listPanel.getPagedFilterTable();

			//定義要顯示的欄位與名稱,及資料源
			pageTable.setId("saTable");
			pageTable.setContainerDataSource(saContainer);
			pageTable.setColumnHeader("brhCod"    ,i18nProps.getProperty("brhCod","分行別"));
			pageTable.setColumnHeader("aoCode"  ,i18nProps.getProperty("aoCode","AO行編") );
			pageTable.setColumnHeader("custIdn"    ,i18nProps.getProperty("custIdn","客戶統編"));
			pageTable.setColumnHeader("chinAct"    ,i18nProps.getProperty("chinAct","客戶名稱"));
			pageTable.setColumnHeader("invdateStr"  ,i18nProps.getProperty("invdateStr","應訪談起月") );
			pageTable.setColumnHeader("invdateEnd"  ,i18nProps.getProperty("invdateEnd","應訪談迄月") );
			pageTable.setColumnHeader("ischeckName"  ,i18nProps.getProperty("ischeckName","狀態") );
			pageTable.setColumnHeader("resetName"  ,i18nProps.getProperty("resetName","撤消") );
			
			pageTable.setVisibleColumns(new Object[]{
					"brhCod", "aoCode", "custIdn", "chinAct", "invdateStr", "invdateEnd","ischeckName","resetName"});
		}else{
			Notification.show("["+messageProps.getProperty("msg_title","作業訊息通知")+"]"
					,    messageProps.getProperty("msg_error","作業失敗")+":"
					 +messageProps.getProperty("noData","查無資料")
					,Type.WARNING_MESSAGE);			
		}
	}
	
   /*******Data List Tab*************end********************************************************************************/
	
  /*******Data Edit Tab*************begin******************************************************************************/
	Tab tabEdit ;
	public void editTab(){
		final FilterTable    filterTable     = new FilterTable();
		final TwoSelect usersSelect =utilSelect.usersSelect("brhCod", messageProps.getProperty("brhCod","分行別"), true,  false ,false);
		final BeanContainer<String,WorkContactListSa>saContainer;
		final ScsbProperty  editPanel = new ScsbProperty(messageProps.getProperty("label_Edit","異動作業"));
		
		String sBrh =selBrhCod.getValue().toString();
		String sArea =selDataArea.getValue().toString();
		saContainer =saSrv.getWorkContactListSa_brh_area(sBrh ,sArea ,"2");
		
		editPanel.getToolBar().setSid(className);
		String tabCaption =messageProps.getProperty("editPage","異動作業");
		tabEdit =mainTab.addTab(editPanel.getContent() ,tabCaption);
		tabEdit.setIcon(FontAwesome.EDIT);
		tabEdit.setClosable(true);
		mainTab.setSelectedTab(tabEdit); 
		
		editPanel.getToolBar().saveBu.setDescription("執行異動");
		editPanel.getToolBar().saveBu.addClickListener(
			new ClickListener() {
					@Override
					public void buttonClick(ClickEvent event) {
						Collection collId =(Collection) filterTable.getValue();
						
						final HashSet<WorkContactListSa> hashBean =new HashSet<WorkContactListSa>();
						String sMessage="";
						
						sMessage+="新承接分行別:"+usersSelect.getMasterSelect().getValue()+"\n";
						sMessage+="新承接服務專員:"+usersSelect.getDetailSelect().getValue()+"\n";
						
						for(Iterator iter=collId.iterator();iter.hasNext();){
							WorkContactListSa bean =saContainer.getItem(iter.next()).getBean();
							String id=bean.getCustIdn();
							String name=bean.getChinAct();
							hashBean.add(bean);
							sMessage+=id+"."+name+"\n";
						}
						ConfirmDialog d =ConfirmDialog.show(UI.getCurrent()
									,"是否異動服務專員"+"?"
									, "總共選取"+hashBean.size()+"筆客戶資料"+"\n"+sMessage
									,messageProps.getProperty("msg_confirm","確定")
									,messageProps.getProperty("msg_cancel","取消")
						        	, new ConfirmDialog.Listener() {
						            	public void onClose(ConfirmDialog dialog) {
						            		if (dialog.isConfirmed()) {
						            			onSaveButton(usersSelect.getMasterSelect().getValue().toString()
						            					,usersSelect.getDetailSelect().getValue().toString() ,hashBean);
						            		}
						            	}
						 });
					}		
				});
		
		//step1.請選擇承接服務專員
		Label step1 =new Label(i18nProps.getProperty("step1","STEP1.請選擇承接服務專員"));
		editPanel.addComponent(step1);
		usersSelect.setDefaultMaster(selBrhCod.getValue());
		usersSelect.hSelectGroup.setWidth("95%");
		editPanel.addComponent(usersSelect.hSelectGroup);
		//step2.請選取指定客戶[可複選Ctrl +左鍵 或 Shit +左鍵 ]
		Label step2 =new Label(i18nProps.getProperty("step2","STEP2.請選取指定客戶[可複選Ctrl +左鍵 或 Shit +左鍵 ]"));
		editPanel.addComponent(step2);
		filterTable.setHeight("360px");
		filterTable.setWidth("95%");
		filterTable.setFilterBarVisible(true);
		filterTable.setId("editTable");
		filterTable.setSelectable(true);
		filterTable.setMultiSelect(true);
		filterTable.addStyleName("filtertable");
		filterTable.addStyleName("scsbBg1");
		filterTable.setContainerDataSource(saContainer);
		//定義要顯示的欄位與名稱,及資料源
		filterTable.setColumnHeader("brhCod"    ,i18nProps.getProperty("brhCod","分行別"));
		filterTable.setColumnHeader("aoCode"  ,i18nProps.getProperty("aoCode","AO行編") );
		filterTable.setColumnHeader("custIdn"    ,i18nProps.getProperty("custIdn","客戶統編"));
		filterTable.setColumnHeader("chinAct"    ,i18nProps.getProperty("chinAct","客戶名稱"));
		filterTable.setColumnHeader("invdateStr"  ,i18nProps.getProperty("invdateStr","應訪談起月") );
		filterTable.setColumnHeader("invdateEnd"  ,i18nProps.getProperty("invdateEnd","應訪談迄月") );
		filterTable.setColumnHeader("mgrEmp"  ,i18nProps.getProperty("mgrEmp","理財專員") );
		filterTable.setColumnHeader("respNo"  ,i18nProps.getProperty("respNo","企金AO") );
		filterTable.setColumnHeader("paoId"  ,i18nProps.getProperty("paoId","個金AO") );
		
		filterTable.setVisibleColumns(new Object[]{
				"brhCod", "aoCode", "custIdn", "chinAct", "invdateStr", "invdateEnd","mgrEmp","respNo","paoId"});		
		editPanel.addComponent(filterTable);
		filterTable.addValueChangeListener(new Property.ValueChangeListener() {
	       	public void valueChange(Property.ValueChangeEvent event) {
	       		editPanel.getToolBar().setSaveButtonVisible(true);
	        }
	    });			
	}
	
	 /**
     *  執行儲存動作
	 * @param codeKind 
     */
    public void onSaveButton(String brhCod ,String aoCode ,HashSet<WorkContactListSa> hashBean){
		String errMsg="";
		
   		//啟用transaction模式,要記得自己disConnect
		DBAction dbAction =new DBAction();
	    dbAction.getConnect();
    	ProgressWindow progress = new ProgressWindow("資料處理中");
    	progress.setMaxProgress(hashBean.size());	    
		try {
			Users aoBean =UserAction.getUsersBean(aoCode);
			String sType="EBAO";
			if (aoBean.getTitleName().equals("分行作業主管")
				 || aoBean.getTitleName().equals("存款業務主管")
				 || aoBean.getTitleName().equals("台幣客服資深專員")
				 || aoBean.getTitleName().equals("台幣存款業務資深專員")
				 || aoBean.getTitleName().equals("台幣客服專員")
			)	sType="EBAO";

			if (aoBean.getTitleName().equals("外幣客服資深專員")
					 || aoBean.getTitleName().equals("外幣存款業務資深專員")
					 || aoBean.getTitleName().equals("外幣客服專員")
					 || aoBean.getTitleName().equals("OBU作業主管")
					 || aoBean.getTitleName().equals("外匯客服專員")
					 || aoBean.getTitleName().equals("外匯開戶專員")
					 || aoBean.getTitleName().equals("外匯資深客服專員")
					 || aoBean.getTitleName().equals("外匯資深專員")
				)	sType="FBAO";
			
			if (aoBean.getTitleName().equals("分行作業主管")
					 && aoBean.getDeptid().substring(0,2).equals("05")
				)	sType="FBAO";			
			
			if ( (aoBean.getTitleName().equals("信託開戶資深專員")
					 || aoBean.getTitleName().equals("放款信託作業主管")
					 || aoBean.getTitleName().equals("放款資深專員")
					 || aoBean.getTitleName().equals("會計專員")
					 || aoBean.getTitleName().equals("放款專員"))
					 && aoBean.getDeptid().substring(0,2).equals("27")
				)	sType="FBAO";
			
			if (aoBean.getTitleName().indexOf("個金AO") >=0 )	sType="PBAO";			
			if (aoBean.getTitleName().indexOf("企金AO") >=0 )	sType="CBAO";
			if (aoBean.getTitleName().indexOf("理財") >=0 )	sType="WMAO";
			
			String sDB="EBRH";
			if (sType.equals("CBAO") || sType.equals("PBAO")) sDB="ELOAN";
			if (sType.equals("EBAO") ) sDB="EBRH";
			if (sType.equals("FBAO") ) sDB="FBRH";
			if (sType.equals("WMAO") ) sDB="VIP";
			/*02只用ebrh ,05只用fbrh*/
			if (sDB.equals("FBRH") && brhCod.equals("02")) brhCod="05";
			if (sDB.equals("EBRH") && brhCod.equals("05")) brhCod="02";
			
			dbAction.connection.setAutoCommit(false);
			int imaxid=saSrv.getWorkContactListSa_maxid();
			
	    	int iflag=0;
			for(Iterator<WorkContactListSa> iter=hashBean.iterator();iter.hasNext();){
		    	progress.setNowProgress(iflag++);		
				
				WorkContactListSa bean =iter.next();
				imaxid++;
				WorkContactListSa oldBean =bean;
				bean.setItemid(imaxid);
				bean.setBrhCod(brhCod);
				if (brhCod.equals("05"))	bean.setShowBrh("02");
				else bean.setShowBrh(brhCod);
				bean.setAoCode(aoCode);
				bean.setDb(sDB);
				if (this.sWorkMode.equals("new")){
					bean.setListType("存款訪談"+DateUtil.getYYYYMMDD().substring(0,6)+" 新增");
				}else{
					bean.setListType("存款訪談"+DateUtil.getYYYYMMDD().substring(0,6)+" 轉移");
				}
				bean.setIscheck("N");
				bean.setTitle(aoBean.getTitleName());
				bean.setCrdUser(users.getUserid());
				bean.setDatadate(DateUtil.getYYYYMMDD());
				//bean.setCrdDatetime(DateUtil.getDTS()); //使用 db default
				if (this.sWorkMode.equals("new")){
					if (!saSrv.deleteWorkContactListSa(dbAction.connection, oldBean)){
						dbAction.connection.rollback();
						dbAction.disConnect();
						showMsgError(saSrv.ErrMsg);
						return;
					};
				}
				if (!saSrv.insertWorkContactListSa( dbAction.connection, bean)){
					dbAction.connection.rollback();
					dbAction.disConnect();
					showMsgError(saSrv.ErrMsg);
					return;
				};					
			}//for
			progress.close();
			dbAction.connection.commit();
			showMsgSuccess();
			mainTab.removeTab(tabEdit);
			editTab();
		} catch (Exception e1) {
			progress.close();
			e1.printStackTrace();
			errMsg=e1.getMessage();
			showMsgError(errMsg);
		} finally {
			dbAction.disConnect();
		}			
    }//	onSaveButton			
 /*******Data Edit Tab*************end********************************************************************************/
    
 /*******Data Reset Tab*************begin******************************************************************************/
	Tab tabReset ;
	public void resetTab(){
		final FilterTable    filterTable     = new FilterTable();
		final BeanContainer<String,WorkContactListSa>saContainer;
		final ScsbProperty  resetPanel = new ScsbProperty(messageProps.getProperty("label_Rest","撤消作業"));
		
		String sBrh =selBrhCod.getValue().toString();
		String sArea =selDataArea.getValue().toString();		
		saContainer =saSrv.getWorkContactListSa_brh_area(sBrh ,sArea ,"4");		
		
		resetPanel.getToolBar().setSid(className);
		String tabCaption =messageProps.getProperty("resetPage","撤消作業");
		tabReset =mainTab.addTab(resetPanel.getContent() ,tabCaption);
		tabReset.setIcon(FontAwesome.MAGIC);
		tabReset.setClosable(true);
		mainTab.setSelectedTab(tabReset); 
		
		resetPanel.getToolBar().saveBu.setDescription("執行撤消");
		resetPanel.getToolBar().saveBu.addClickListener(
			new ClickListener() {
					@Override
					public void buttonClick(ClickEvent event) {
						Collection collId =(Collection) filterTable.getValue();
						
						final HashSet<WorkContactListSa> hashBean =new HashSet<WorkContactListSa>();
						String sMessage="";
						
						for(Iterator iter=collId.iterator();iter.hasNext();){
							WorkContactListSa bean =saContainer.getItem(iter.next()).getBean();
							String id=bean.getCustIdn();
							String name=bean.getChinAct();
							hashBean.add(bean);
							sMessage+=id+"."+name+"\n";
						}
						ConfirmDialog d =ConfirmDialog.show(UI.getCurrent()
									,"是否撤消"+"?"
									, "總共選取"+hashBean.size()+"筆客戶資料"+"\n"+sMessage
									,messageProps.getProperty("msg_confirm","確定")
									,messageProps.getProperty("msg_cancel","取消")
						        	, new ConfirmDialog.Listener() {
						            	public void onClose(ConfirmDialog dialog) {
						            		if (dialog.isConfirmed()) {
						            			onResetButton(hashBean);
						            		}
						            	}
						 });
					}		
				});
		
		//step.請選取指定客戶[可複選Ctrl +左鍵 或 Shit +左鍵 ]
		Label step2 =new Label(i18nProps.getProperty("step","STEP.請選取指定客戶[可複選Ctrl +左鍵 或 Shit +左鍵 ]"));
		resetPanel.addComponent(step2);
		filterTable.setHeight("360px");
		filterTable.setWidth("95%");
		filterTable.setFilterBarVisible(true);
		filterTable.setId("resetTable");
		filterTable.setSelectable(true);
		filterTable.setMultiSelect(true);
		filterTable.addStyleName("filtertable");
		filterTable.addStyleName("scsbBg1");
		filterTable.setContainerDataSource(saContainer);
		//定義要顯示的欄位與名稱,及資料源
		filterTable.setColumnHeader("brhCod"    ,i18nProps.getProperty("brhCod","分行別"));
		filterTable.setColumnHeader("aoCode"  ,i18nProps.getProperty("aoCode","AO行編") );
		filterTable.setColumnHeader("custIdn"    ,i18nProps.getProperty("custIdn","客戶統編"));
		filterTable.setColumnHeader("chinAct"    ,i18nProps.getProperty("chinAct","客戶名稱"));
		filterTable.setColumnHeader("invdateStr"  ,i18nProps.getProperty("invdateStr","應訪談起月") );
		filterTable.setColumnHeader("invdateEnd"  ,i18nProps.getProperty("invdateEnd","應訪談迄月") );
		filterTable.setColumnHeader("mgrEmp"  ,i18nProps.getProperty("mgrEmp","理財專員") );
		filterTable.setColumnHeader("respNo"  ,i18nProps.getProperty("respNo","企金AO") );
		filterTable.setColumnHeader("paoId"  ,i18nProps.getProperty("paoId","個金AO") );
		
		filterTable.setVisibleColumns(new Object[]{
				"brhCod", "aoCode", "custIdn", "chinAct", "invdateStr", "invdateEnd","mgrEmp","respNo","paoId"});		
		resetPanel.addComponent(filterTable);
		filterTable.addValueChangeListener(new Property.ValueChangeListener() {
	       	public void valueChange(Property.ValueChangeEvent event) {
	       		resetPanel.getToolBar().setSaveButtonVisible(true);
	        }
	    });			
	}	
	 /**
     *  執行撤件動作
	 * @param codeKind 
     */
    public void onResetButton(HashSet<WorkContactListSa> hashBean){
		String errMsg="";
   		//啟用transaction模式,要記得自己disConnect
		DBAction dbAction =new DBAction();
	    dbAction.getConnect();
    	ProgressWindow progress = new ProgressWindow("資料處理中");
    	progress.setMaxProgress(hashBean.size());	    
	    
		try {
			dbAction.connection.setAutoCommit(false);
			int iflag=0;
			for(Iterator<WorkContactListSa> iter=hashBean.iterator();iter.hasNext();){
				progress.setNowProgress(iflag++);	    
				
				WorkContactListSa bean =iter.next();
				bean.setListType("存款訪談"+DateUtil.getYYYYMMDD().substring(0,6)+" 撤件");
				bean.setReset("N");
				bean.setCrdUser(users.getUserid());
				bean.setDatadate(DateUtil.getYYYYMMDD());
				//bean.setCrdDatetime(DateUtil.getDTS()); //使用 db default
				if (!saSrv.updateWorkContactListSa( dbAction.connection, bean)){
					dbAction.connection.rollback();
					dbAction.disConnect();
					showMsgError(saSrv.ErrMsg);
					return;
				};
			}//for
			progress.close();
			dbAction.connection.commit();
			dbAction.disConnect();
			showMsgSuccess();
			mainTab.removeTab(tabReset);
			resetTab();
		} catch (Exception e1) {
			progress.close();
			dbAction.disConnect();
			e1.printStackTrace();
			errMsg=e1.getMessage();
			showMsgError(errMsg);
		}				
    }//onResetButton			
	
	 /*******Data Reset Tab*************end********************************************************************************/
    
    /*******Data Check Tab*************begin******************************************************************************/
  	Tab tabCheck ;
  	public void checkTab(){
  		final FilterTable    filterTable     = new FilterTable();
  		final BeanContainer<String,WorkContactListSa>saContainer;
  		final ScsbProperty  checkPanel = new ScsbProperty();
  		String tabCaption ="異動審核作業";
  		if (sWorkMode.equals("change")){
  			checkPanel.setCaption("異動審核作業");
  			tabCaption ="異動審核作業";
  		}
  		if (sWorkMode.equals("reset")){
  			checkPanel.setCaption("撤消審核作業");
  			tabCaption ="撤消審核作業";
  		}
  		String sBrh =selBrhCod.getValue().toString();
  		String sArea =selDataArea.getValue().toString();		
  		if (sWorkMode.equals("reset")){
  			saContainer =saSrv.getWorkContactListSa_brh_area(sBrh ,sArea ,"6");
  		}else{
  			saContainer =saSrv.getWorkContactListSa_brh_area(sBrh ,sArea ,"3");
  		}
  		
  		checkPanel.getToolBar().setSid(className);
  		
  		tabCheck =mainTab.addTab(checkPanel.getContent() ,tabCaption);
  		tabCheck.setIcon(FontAwesome.CHECK);
  		tabCheck.setClosable(true);
  		mainTab.setSelectedTab(tabCheck); 
  		
  		checkPanel.getToolBar().saveBu.setDescription("執行審核");
  		checkPanel.getToolBar().saveBu.addClickListener(
  			new ClickListener() {
  					@Override
  					public void buttonClick(ClickEvent event) {
  						Collection collId =(Collection) filterTable.getValue();
  						
  						final HashSet<WorkContactListSa> hashBean =new HashSet<WorkContactListSa>();
  						String sMessage="";
  						for(Iterator iter=collId.iterator();iter.hasNext();){
  							WorkContactListSa bean =saContainer.getItem(iter.next()).getBean();
  							String id=bean.getCustIdn();
  							String name=bean.getChinAct();
  							String newao=bean.getAoCode();
  							hashBean.add(bean);
  							if (sWorkMode.equals("change")){
  								sMessage+=id+"."+name+"("+bean.getItemid()+") :承接服務專員:"+newao+"\n";
  							}
  						}
  						ConfirmDialog d =ConfirmDialog.show(UI.getCurrent()
  									,messageProps.getProperty("msg_ischeck_ordel","核可/刪除")+"?"
  									, "總共選取"+hashBean.size()+"筆資料"+"\n"+sMessage
  									,messageProps.getProperty("msg_confirm","核可")
  									,messageProps.getProperty("msg_del","刪除")
  						        	, new ConfirmDialog.Listener() {
  						            	public void onClose(ConfirmDialog dialog) {
  						            		if (dialog.isConfirmed()) {
  						            			onCheckButton(hashBean ,true);
  						            		}else if(dialog.isCanceled()){
  						            			onCheckButton(hashBean ,false);
  						            		}
  						            	}
  						 });
  					}		
  				});
  		
  		//step1.請選取指定客戶[可複選Ctrl +左鍵 或 Shit +左鍵 ]
  		Label step2 =new Label(i18nProps.getProperty("step_check","請選取要覆核的資料[可複選Ctrl +左鍵 或 Shit +左鍵 ]"));
  		checkPanel.addComponent(step2);
  		filterTable.setHeight("360px");
  		filterTable.setWidth("95%");
  		filterTable.setFilterBarVisible(true);
  		filterTable.setId("checkTable");
  		filterTable.setSelectable(true);
  		filterTable.setMultiSelect(true);
  		filterTable.addStyleName("filtertable");
  		filterTable.addStyleName("scsbBg1");
  		filterTable.setContainerDataSource(saContainer);
  		//定義要顯示的欄位與名稱,及資料源
  		filterTable.setColumnHeader("brhCod"    ,i18nProps.getProperty("brhCod","分行別"));
  		filterTable.setColumnHeader("aoCode"  ,i18nProps.getProperty("aoCode","新承接服務專員行編") );
  		filterTable.setColumnHeader("custIdn"    ,i18nProps.getProperty("custIdn","客戶統編"));
  		filterTable.setColumnHeader("chinAct"    ,i18nProps.getProperty("chinAct","客戶名稱"));
  		filterTable.setColumnHeader("invdateStr"  ,i18nProps.getProperty("invdateStr","應訪談起月") );
  		filterTable.setColumnHeader("invdateEnd"  ,i18nProps.getProperty("invdateEnd","應訪談迄月") );
  		filterTable.setColumnHeader("mgrEmp"  ,i18nProps.getProperty("mgrEmp","理財專員") );
  		filterTable.setColumnHeader("respNo"  ,i18nProps.getProperty("respNo","企金AO") );
  		filterTable.setColumnHeader("paoId"  ,i18nProps.getProperty("paoId","個金AO") );
  		
  		filterTable.setVisibleColumns(new Object[]{
  				"brhCod", "aoCode", "custIdn", "chinAct", "invdateStr", "invdateEnd","mgrEmp","respNo","paoId"});		
  		checkPanel.addComponent(filterTable);
  		filterTable.addValueChangeListener(new Property.ValueChangeListener() {
  	       	public void valueChange(Property.ValueChangeEvent event) {
  	       		checkPanel.getToolBar().setSaveButtonVisible(true);
  	        }
  	    });			
  	}
  	 /**
       *  執行核可動作
  	 * @param codeKind 
       */
      public void onCheckButton(HashSet<WorkContactListSa> hashBean ,boolean bCheck){
  		String errMsg="";
  		
     	//啟用transaction模式,要記得自己disConnect
  		DBAction dbAction =new DBAction();
  	   dbAction.getConnect(); 	
  	   ProgressWindow progress = new ProgressWindow("資料處理中");
  	   progress.setMaxProgress(hashBean.size());	    
  	   
  		try {
  			dbAction.connection.setAutoCommit(false);
  			int iflag=0;
  			for(Iterator<WorkContactListSa> iter=hashBean.iterator();iter.hasNext();){
  				progress.setNowProgress(iflag++);
  				WorkContactListSa bean =iter.next();
  				if (sWorkMode.equals("change")){
	  				if (bCheck) bean.setIscheck("Y");
	  				else bean.setIscheck("D");
  				}else{
	  				if (bCheck) bean.setReset("Y");
	  				else bean.setReset("");
  				}
  				bean.setCrdUser(users.getUserid());
  				//bean.setCrdDatetime(DateUtil.getDTS()); //使用 db default
  				if (!saSrv.updateWorkContactListSa(bean)){
  					dbAction.connection.rollback();
  					dbAction.disConnect();
  					showMsgError(saSrv.ErrMsg);
  					return;
  				};
  			}//for
  			progress.close();
  			dbAction.connection.commit();
  			dbAction.disConnect();
  			showMsgSuccess();
  			mainTab.removeTab(tabCheck);
  			checkTab();
  		} catch (Exception e1) {
  			progress.close();
  			dbAction.disConnect();
  			e1.printStackTrace();
  			errMsg=e1.getMessage();
  			showMsgError(errMsg);
  		}				
      }//	onCheckButton			
   /*******Data Check Tab*************end********************************************************************************/    
}
