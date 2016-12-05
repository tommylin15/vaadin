package com.scsb.vaadin.ui.gc;

import org.tepi.filtertable.paged.PagedFilterTable;

import com.scsb.db.bean.CcCode;
import com.scsb.db.bean.Trans;
import com.scsb.db.service.TransService;
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
 * 交易設定
 * @author 3471
 *
 */
@SuppressWarnings("serial")
public class gc1020 extends ScsbGlobView {
	TabSheet     mainTab    =new TabSheet();
	ScsbPage    pagePanel     = new ScsbPage();
	
	TransService transSrv =new TransService();
	BeanContainer<String,Trans> transContainer ;
	PagedFilterTable    pageTable     = pagePanel.getPagedFilterTable();
	/**
	 * 交易設定-主畫面
	 */
	public gc1020() {
		loadData();
		buListener();
	    mainTab.setImmediate(true);
	    String tabCaption =i18nProps.getProperty("main","首頁");
		Tab tabQuery =mainTab.addTab(pagePanel.getContent(),tabCaption);
		tabQuery.setIcon(FontAwesome.HOME);
		getContent().addComponent(mainTab);	    		
	}
	public void loadData(){
		
		transContainer =transSrv.getTrans();
		transContainer.sort(new Object[]{"groupid"}, new boolean[]{true});
		
		pagePanel.getToolBar().setSid(className);
        pageTable.setId("transdTable");
        pageTable.setContainerDataSource(transContainer);
		//定義要顯示的欄位與名稱
		pageTable.setColumnHeader("groupid"    ,i18nProps.getProperty("groupId","選單代碼"));
		pageTable.setColumnHeader("groupname"  ,i18nProps.getProperty("groupName","選單名稱") );
		pageTable.setColumnHeader("groupmode"  ,i18nProps.getProperty("groupMode","是否啟用") );
		pageTable.setVisibleColumns(new Object[]{"groupid","groupname","groupmode"});
		
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
    					Trans bean =new Trans();
    					gc1020_property propView =new gc1020_property(className ,bean ,true ,mainTab ,pagePanel);
    				}
        		}
        	);		
		//選取Table資料時
	    Property.ValueChangeListener listener = new Property.ValueChangeListener() {
	       	public void valueChange(Property.ValueChangeEvent event) {
	       		BeanItem<Trans> beanitem=(BeanItem<Trans>) transContainer.getItem(pageTable.getValue());
				if (beanitem != null){
					Trans bean =beanitem.getBean();
					gc1020_property propView =new gc1020_property(className ,bean ,false ,mainTab  ,pagePanel);
					pageTable.select(null);
	       		}
	        }
	    };
	    pageTable.addValueChangeListener(listener);
	}		
}
