package com.scsb.vaadin.ui.gc;

import java.util.Properties;

import org.tepi.filtertable.paged.PagedFilterTable;

import com.scsb.db.bean.Fnbct0;
import com.scsb.db.service.Fnbct0Service;
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
 * 分行設定
 * @author 3471
 *
 */
@SuppressWarnings("serial")
public class gc1060 extends ScsbGlobView {
	TabSheet     mainTab    =new TabSheet();
	ScsbPage    pagePanel     = new ScsbPage();
	
	Fnbct0Service fnbct0Srv =new Fnbct0Service();
	BeanContainer<String,Fnbct0> fnbct0Container;
	PagedFilterTable    pageTable     = pagePanel.getPagedFilterTable();
	/**
	 * 分行設定-主畫面
	 */
	public gc1060() {
		loadData();
		buListener();
	    mainTab.setImmediate(true);
	    String tabCaption =i18nProps.getProperty("main","首頁");
		Tab tabQuery =mainTab.addTab(pagePanel.getContent(),tabCaption);
		tabQuery.setIcon(FontAwesome.HOME);
		getContent().addComponent(mainTab);	 		
	}

	public void loadData(){		
		
		fnbct0Container =fnbct0Srv.getSasFnbct0_All();
		fnbct0Container.sort(new Object[]{"brhCod"}, new boolean[]{true});
		
		pagePanel.getToolBar().setSid(className);
		
        pageTable.setId("fnbct0");
        pageTable.setContainerDataSource(fnbct0Container);
		if (fnbct0Container.size() >=10)	pageTable.setPageLength(10);
		else pageTable.setPageLength(pageTable.size());		
		//定義要顯示的欄位與名稱
		pageTable.setColumnHeader("brhCod"    ,i18nProps.getProperty("brhCod","分行代碼") );
		pageTable.setColumnHeader("chinFul"    ,i18nProps.getProperty("chinFul","分行名稱") );
		pageTable.setVisibleColumns(new Object[]{"brhCod","chinFul"});
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
	       		BeanItem<Fnbct0> beanitem=(BeanItem<Fnbct0>) fnbct0Container.getItem(pageTable.getValue());
				if (beanitem != null){
					Properties pp =new Properties();
					pp.setProperty("GOOGLE_MAP_ADDR",beanitem.getBean().getChinAd1());
					//分行屬性
					new gc1060_property(className
															, beanitem.getBean().getBrhCod()
															,beanitem.getBean().getChinFul()
															,pp
															,mainTab
															,pagePanel
															);
					pageTable.select(null);
	       		}
	        }
	    };
	    pageTable.addValueChangeListener(listener);
	
	}
	
}
