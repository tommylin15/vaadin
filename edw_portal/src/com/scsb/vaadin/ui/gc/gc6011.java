package com.scsb.vaadin.ui.gc;

import org.tepi.filtertable.paged.PagedFilterTable;

import com.scsb.db.bean.ScsbTitle;
import com.scsb.db.service.ScsbTitleService;
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
 * 職稱設定
 * @author 3471
 *
 */
@SuppressWarnings("serial")
public class gc6011 extends ScsbGlobView {
	TabSheet     mainTab    =new TabSheet();
	ScsbPage    pagePanel     = new ScsbPage();
	
	ScsbTitleService titleSrv =new ScsbTitleService();
	BeanContainer<String,ScsbTitle> titleContainer;
	 PagedFilterTable    pageTable     = pagePanel.getPagedFilterTable();
	/**
	 * 職稱設定-主畫面
	 */
	public gc6011() {
		loadData();
		buListener();
	    mainTab.setImmediate(true);
	    String tabCaption =i18nProps.getProperty("main","首頁");
		Tab tabQuery =mainTab.addTab(pagePanel.getContent(),tabCaption);
		tabQuery.setIcon(FontAwesome.HOME);
		getContent().addComponent(mainTab);	  		
	}
	public void loadData(){
		titleContainer =titleSrv.getTitle_All();
		titleContainer.sort(new Object[]{"titleName"}, new boolean[]{true});
		
		pagePanel.getToolBar().setSid(className);
        pageTable.setId("ccCodeTable");
        pageTable.setContainerDataSource(titleContainer);
		if (titleContainer.size() >=10)	pageTable.setPageLength(10);
		else pageTable.setPageLength(pageTable.size());		
		//定義要顯示的欄位與名稱
		pageTable.setColumnHeader("titleName"  ,i18nProps.getProperty("titleName","職稱名稱") );
		pageTable.setVisibleColumns(new Object[]{"titleName"});
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
		//選取Table資料時
	    Property.ValueChangeListener listener = new Property.ValueChangeListener() {
	       	public void valueChange(Property.ValueChangeEvent event) {
	       		BeanItem<ScsbTitle> beanitem=(BeanItem<ScsbTitle>) titleContainer.getItem(pageTable.getValue());
				if (beanitem != null){
					ScsbTitle bean =beanitem.getBean();
					new gc6011_property(className ,bean ,false ,mainTab ,pagePanel);
					pageTable.select(null);
	       		}
	        }
	    };
	    pageTable.addValueChangeListener(listener);
	    
		    
	}
}
