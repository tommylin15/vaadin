package com.scsb.vaadin.ui.gc;

import com.scsb.db.bean.ScsbTitle;
import com.scsb.db.bean.Trans;
import com.scsb.db.service.ScsbTitleService;
import com.scsb.vaadin.composite.ScsbPage;
import com.scsb.vaadin.composite.ScsbProperty;
import com.scsb.vaadin.composite.ScsbPropertyButton;
import com.scsb.vaadin.include.TitleLayout;
import com.scsb.vaadin.include.UtilOptionGroup;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.TabSheet.Tab;

/**
 * 職稱-屬性設定
 * @author 3471
 *
 */
@SuppressWarnings("serial")
public class gc6011_property extends ScsbProperty {
	
	ScsbTitle           	dataBean ;
	BeanItem<ScsbTitle> 	dataItem ;	
	FieldGroup          dataBinder;
	
	TextField titleName =new TextField();
	
	boolean IsInsert=false;
	ScsbTitle srcBean =new ScsbTitle();
	
	TabSheet     mainTab    =new TabSheet();	
	Tab  tabProperty; 
	ScsbPage scsbPage;	
	String sid;		
	/**
	 * 職稱-屬性設定-主畫面
	 */
	public gc6011_property(String sidx ,final ScsbTitle srcBean ,boolean IsInsert ,TabSheet mainTabx,	ScsbPage scsbPage) {
		this.srcBean=srcBean;
		this.mainTab=mainTabx;
		this.sid=sidx;
		this.scsbPage=scsbPage;
		this.getToolBar().setSid(sid);
		
		DataBinderFactory();
		this.IsInsert=IsInsert;
		
		//欄位編輯(職稱不可編輯)====================================================================
		titleName.setId("titleName");
		titleName.setCaption(i18nProps.getProperty("titleName","職稱"));
		titleName.setImmediate(true);
		titleName.setIcon(FontAwesome.LOCK);
		getFormLayout().addComponent(titleName);

		//職稱的敏感性資料授權=========================================================================
		final ScsbPropertyButton menuMask =new ScsbPropertyButton(i18nProps.getProperty("allowBrh","敏感性資料授權"));        	
		menuMask.setDescription(messageProps.getProperty("Button_edit_view","編輯/檢視"));
		menuMask.setId("MASK");
    	menuMask.addClickListener(new ClickListener() {
    		@Override
			public void buttonClick(ClickEvent event) {
    			//細項功能列表
    			OptionGroup optionGroup =new UtilOptionGroup().getTransOptionGroup();
				new gc6011_PropertyView(sid ,srcBean.getTitleName()+"-"+menuMask.getCaption() ,srcBean.getTitleName() ,menuMask.getId() ,optionGroup ,mainTab);
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
			    			new gc6011_titleD(sid ,srcBean ,bean ,mainTab);
						}			    		
			    	});
			    	addComponent(menuBu);
				}//if
			}//for
		}//if
    	
		String tabCaption =i18nProps.getProperty("propertySet","-屬性設定");
		if (srcBean != null)  tabCaption =srcBean.getTitleName()+tabCaption;
	    tabProperty =mainTab.addTab(getContent() ,tabCaption);
	    tabProperty.setClosable(true);
	    tabProperty.setIcon(FontAwesome.EDIT);
	    mainTab.setSelectedTab(tabProperty);      	
	}
    
	 /**
	  * Data的Field Factory
	  * @author 3471
	  *
	  */
    private void DataBinderFactory(){
		ScsbTitleService titleSrv =new ScsbTitleService();
		dataBean =titleSrv.getTitle_PK(srcBean);
		
		dataItem = new BeanItem<ScsbTitle>(dataBean);
		dataBinder = new FieldGroup(dataItem);
		dataBinder.setBuffered(true);    	    		
		dataBinder.bind(titleName, "titleName");
	 }	
}
