package com.scsb.vaadin.ui.gc;

import org.tepi.filtertable.paged.PagedFilterTable;

import com.scsb.db.bean.CcCode;
import com.scsb.db.bean.Transd;
import com.scsb.db.bean.Transp;
import com.scsb.db.service.TransdService;
import com.scsb.db.service.TranspService;
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
 * 交易的SmartQueryReport
 * @author 3471
 *
 */
@SuppressWarnings("serial")
public class gc1020_transD_smartQueryReport extends ScsbGlobView {
	TabSheet     mainTab ;	
	Tab  tabProperty; 		
	String sid ;
	String programid;
	String propertyKey;	
	
	ScsbPage    pagePanel     = new ScsbPage();
	TranspService transpSrv = new TranspService();
	BeanContainer<String,Transp> transpContainer;
	 PagedFilterTable    pageTable     = pagePanel.getPagedFilterTable();
	/**
	 * 主畫面
	 */
	public gc1020_transD_smartQueryReport(String sid ,String programid ,String propertyKey ,TabSheet mainTab) {
		this.programid=programid;
		this.propertyKey=propertyKey;
		this.mainTab=mainTab;
		this.sid=sid;		
		loadData();
		buListener();
		String tabCaption =i18nProps.getProperty("detail","-明細");
		tabCaption=programid;
	    tabProperty =mainTab.addTab(pagePanel.getContent() ,tabCaption);
	    tabProperty.setClosable(true);
	    tabProperty.setIcon(FontAwesome.SEARCH);
	    mainTab.setSelectedTab(tabProperty);  		
	}
	public void loadData(){
		transpContainer =transpSrv.getTransp_PKs(this.programid,this.propertyKey);
		transpContainer.sort(new Object[]{"propertyValue"}, new boolean[]{true,true});
		pagePanel.getToolBar().setSid(sid);
        pageTable.setId("transd");
        pageTable.setContainerDataSource(transpContainer);
		//定義要顯示的欄位與名稱
		pageTable.setColumnHeader("programid"    ,i18nProps.getProperty("programId","程式代碼"));
		pageTable.setColumnHeader("propertyValue"  ,i18nProps.getProperty("programMode","報表代碼") );
		pageTable.setVisibleColumns(new Object[]{"programid","propertyValue"});
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
		//新增按鈕(子項才能新增)
		pagePanel.getToolBar().setAddButtonVisible(true);
		
		pagePanel.getToolBar().addBu.addClickListener(
    		    new ClickListener() {
    				@Override
    				public void buttonClick(ClickEvent event) {
    					Transp bean =new Transp();
    					bean.setProgramid(programid);
    					bean.setPropertyKey(propertyKey);
    					bean.setPropertyMemo(messageProps.getProperty("InsertMode","SmartQueryReport"));
    					gc1020_transD_smartQueryReport_property propView =new gc1020_transD_smartQueryReport_property(sid ,bean ,true ,mainTab ,pagePanel);
    				}
        		}
        	);		
		//選取Table資料時
	    Property.ValueChangeListener listener = new Property.ValueChangeListener() {
	       	public void valueChange(Property.ValueChangeEvent event) {
	       		BeanItem<Transp> beanitem=(BeanItem<Transp>) transpContainer.getItem(pageTable.getValue());
				if (beanitem != null){
					Transp bean =beanitem.getBean();
					gc1020_transD_smartQueryReport_property propView =new gc1020_transD_smartQueryReport_property(sid ,bean ,false ,mainTab ,pagePanel);
					pageTable.select(null);					
	       		}
	        }
	    };
	    pageTable.addValueChangeListener(listener);	
	}	
}
