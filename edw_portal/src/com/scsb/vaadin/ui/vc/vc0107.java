package com.scsb.vaadin.ui.vc;

import org.tepi.filtertable.paged.PagedFilterTable;

import com.scsb.db.bean.OdLvrLandA;
import com.scsb.db.service.OdLvrLandAService;
import com.scsb.vaadin.composite.ScsbGlobView;
import com.scsb.vaadin.composite.ScsbPage;
import com.scsb.vaadin.composite.ScsbProperty;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.TabSheet.Tab;

/**
 * Open data Test
 * @author 3471
 *
 */
@SuppressWarnings("serial")
public class vc0107 extends ScsbGlobView {
	/**
	 * 實價登錄-主畫面
	 */
	TabSheet     MainTab    =new TabSheet(); 
	TabSheet     mainTab    =new TabSheet();	
	ScsbPage    pagePanel     = new ScsbPage();
	ScsbProperty  queryPanel = new ScsbProperty();
	
	public vc0107() {
		create();
	    mainTab.setImmediate(true);
	    String tabCaption =i18nProps.getProperty("main","首頁");
		Tab tabQuery =mainTab.addTab(queryPanel.getContent() ,tabCaption);
		tabQuery.setIcon(FontAwesome.HOME);
		getContent().addComponent(mainTab);				
	}
	public void create(){
		queryPanel.getToolBar().setRunButtonVisible(true);
	}
	public void queryTab(){
		pagePanel.getContent();
		OdLvrLandAService lvrlandaSrv =new OdLvrLandAService();
		lvrlandaSrv.setDbPool("opendata");
		BeanContainer<String, OdLvrLandA> data =lvrlandaSrv.getOdLvrLandA_All();

		pagePanel.getToolBar().setSid(className);
		final PagedFilterTable    pageTable     = pagePanel.getPagedFilterTable();
		pageTable.setId("transdTable");
		pageTable.setContainerDataSource(data);
		
		pageTable.setColumnHeader("city"    ,"縣市別");
		pageTable.setColumnHeader("area"    ,"鄉鎮市區");
		pageTable.setColumnHeader("transSubject"     ,"交易標的" );
		pageTable.setColumnHeader("landBuild"     ,"位置");
		pageTable.setColumnHeader("transYm"     ,"交易年月");
		pageTable.setColumnHeader("totalPrice"     ,"總價");
		pageTable.setColumnHeader("buildType"     ,"建物型態");
		pageTable.setColumnHeader("memo"     ,"備註");
		pageTable.setVisibleColumns(new Object[]{"city" ,"area","transSubject","landBuild","transYm","totalPrice","buildType","memo"});
		//圖表按鈕
		/*
		pagePanel.getToolBar().setChartButtonVisible(true);
		pagePanel.getToolBar().chartBu.addClickListener(
    		    new ClickListener() {
    				@Override
    				public void buttonClick(ClickEvent event) {
    					Filterable filterable =pageTable.getFilterable();
						vc0101ChartView chartView = new vc0101ChartView(filterable);
						chartView.vLayout.setSizeFull();
						Tab chartTab =MainTab.addTab(chartView.vLayout);
						chartTab.setCaption("圖表"); 
						chartTab.setClosable(true);
						
						MainTab.setSelectedTab(chartTab);
    					//chartTab =MainTab.addTab(chartView);
    					//chartTab.setCaption("統計圖");		
						//UI.getCurrent().addWindow(popover);
    				}
        		}
        );
        */
	}
	
}
