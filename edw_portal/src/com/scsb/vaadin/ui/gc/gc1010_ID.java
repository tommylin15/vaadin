package com.scsb.vaadin.ui.gc;

import org.tepi.filtertable.paged.PagedFilterTable;

import com.scsb.db.bean.CcCode;
import com.scsb.db.service.CcCodeService;
import com.scsb.vaadin.composite.ScsbGlobView;
import com.scsb.vaadin.composite.ScsbPage;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.TabSheet.Tab;

/**
 * 公用參數明細
 * @author 3471
 *
 */
@SuppressWarnings("serial")
public class gc1010_ID extends ScsbGlobView {
	/**
	 * 交易明細設定-主畫面
	 */
	TabSheet     mainTab    =new TabSheet();
	String sid;
	ScsbPage pagePanel = new ScsbPage();
	CcCode srcBean;
	
	CcCodeService ccCodeSrv =new CcCodeService();
	BeanContainer<String,CcCode> ccCodeContainer ;
	PagedFilterTable    pageTable     = pagePanel.getPagedFilterTable();
	
	public gc1010_ID(String sid ,CcCode orgBean ,TabSheet mainTab) {
		this.srcBean=orgBean;
		this.mainTab=mainTab;
		this.sid=sid;
		this.getView().setBarVisible(false);
		loadData();
		buListener();
	    getContent().addComponent(pagePanel.getContent());	    		
	    String tabCaption =srcBean.getCodeMemo()+i18nProps.getProperty("detailQuery","--明細查詢");
	    Tab  tabProperty =mainTab.addTab( getContent(),tabCaption);
	    tabProperty.setClosable(true);
	    tabProperty.setIcon(FontAwesome.SEARCH);
	    mainTab.setSelectedTab(tabProperty);		
	}
	public void loadData(){
		ccCodeContainer =ccCodeSrv.getCcCode_Kind_All(srcBean.getCodeKind());
		ccCodeContainer.sort(new Object[]{"codeId"}, new boolean[]{true});
		
		pagePanel.getToolBar().setSid(sid);
        pageTable.setId("ccCode");
        pageTable.setContainerDataSource(ccCodeContainer);
		//定義要顯示的欄位與名稱
		pageTable.setColumnHeader("codeId"    ,i18nProps.getProperty("codeId","代碼類型"));
		pageTable.setColumnHeader("codeName"  ,i18nProps.getProperty("codeName","代碼名稱") );
		pageTable.setVisibleColumns(new Object[]{"codeId","codeName"});
	    
	}
	
	public void buListener(){
		//重新整理按鈕
		pagePanel.getToolBar().setRefreshButtonVisible(true);
		pagePanel.getToolBar().refreshBu.addClickListener(
    		    new ClickListener() {
    				@Override
    				public void buttonClick(ClickEvent event) {
    					loadData();
    				}
        		}
        	);			
		//新增按鈕
		pagePanel.getToolBar().setAddButtonVisible(true);
		pagePanel.getToolBar().addBu.addClickListener(
    		    new ClickListener() {
    				@Override
    				public void buttonClick(ClickEvent event) {
    					gc1010_ID_property propView =new gc1010_ID_property(sid ,srcBean ,true ,mainTab ,pagePanel);
    					pageTable.select(null);    					
    				}
        		}
        	);		
		//選取Table資料時
	    Property.ValueChangeListener listener = new Property.ValueChangeListener() {
	       	public void valueChange(Property.ValueChangeEvent event) {
	       		BeanItem<CcCode> beanitem=(BeanItem<CcCode>) ccCodeContainer.getItem(pageTable.getValue());
				if (beanitem != null){
					CcCode bean =beanitem.getBean();
					gc1010_ID_property propView =new gc1010_ID_property(sid ,bean ,false ,mainTab ,pagePanel);
					pageTable.select(null); 
	       		}
	        }
	    };
	    pageTable.addValueChangeListener(listener);		
	}
}
