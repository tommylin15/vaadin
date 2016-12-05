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
 * 公用參數
 * @author 3471
 *
 */
@SuppressWarnings("serial")
public class gc1010 extends ScsbGlobView {
	TabSheet     mainTab    =new TabSheet();	
	ScsbPage    pagePanel     = new ScsbPage();
	
	CcCodeService ccCodeSrv =new CcCodeService();
	PagedFilterTable    pageTable     = pagePanel.getPagedFilterTable();
	BeanContainer<String,CcCode> ccCodeContainer;
	/**
	 * 公用參數設定-主畫面
	 */
	public gc1010() {
		loadData();
		buListener();
	    mainTab.setImmediate(true);
	    String tabCaption =i18nProps.getProperty("main","首頁");
		Tab tabQuery =mainTab.addTab(pagePanel.getContent(),tabCaption);
		tabQuery.setIcon(FontAwesome.HOME);
		getContent().addComponent(mainTab);			
	}

	public void loadData(){		
		ccCodeContainer =ccCodeSrv.getCcCode_AllKind();
		ccCodeContainer.sort(new Object[]{"codeKind"}, new boolean[]{true});
		
		pagePanel.getToolBar().setSid(className);
		//定義要顯示的欄位與名稱,及資料源
		pageTable.setId("ccCodeTable");
		pageTable.setContainerDataSource(ccCodeContainer);
		pageTable.setColumnHeader("codeKind"    ,i18nProps.getProperty("codeKind","代碼種類"));
		pageTable.setColumnHeader("codeMemo"  ,i18nProps.getProperty("codeMemo","代碼說明") );
		pageTable.setVisibleColumns(new Object[]{"codeKind","codeMemo"});
		
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
    					CcCode bean =new CcCode();
    					gc1010_ID_property propView =new gc1010_ID_property(className ,bean ,true ,mainTab ,pagePanel);
    				}
        		}
        	);		
		//選取Table資料時
	    Property.ValueChangeListener listener = new Property.ValueChangeListener() {
	       	public void valueChange(Property.ValueChangeEvent event) {
	       		BeanItem<CcCode> beanitem=(BeanItem<CcCode>) ccCodeContainer.getItem(pageTable.getValue());
				if (beanitem != null){
					CcCode bean =beanitem.getBean();
					new gc1010_ID(className ,bean ,mainTab);
					pageTable.select(null);
	       		}
	        }
	    };	
	    pageTable.addValueChangeListener(listener);
	}	
}
