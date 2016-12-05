package com.scsb.vaadin.ui.vc;

import java.util.Properties;

import org.tepi.filtertable.FilterTable;
import org.tepi.filtertable.paged.PagedFilterTable;
import org.vaadin.teemu.jsoncontainer.JsonContainer;

import com.scsb.vaadin.composite.ScsbGlobView;
import com.scsb.vaadin.composite.ScsbPage;
import com.scsb.vaadin.server.SasJsonService;
import com.vaadin.data.Container.Filterable;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.TabSheet.Tab;

/**
 * sas 查詢UI
 * @author 3471
 *
 */
@SuppressWarnings("serial")
public class vc0106 extends ScsbGlobView {
	/**
	 * sas 查詢UI-查詢頁面
	 */
	VerticalLayout vchart =new VerticalLayout();
	TabSheet     mainTab    =new TabSheet();	
	ScsbPage    pagePanel     = new ScsbPage();		
	
	public vc0106() {
		create();
	    mainTab.setImmediate(true);
	    String tabCaption =i18nProps.getProperty("main","首頁");
		Tab tabQuery =mainTab.addTab(pagePanel.getContent(),tabCaption);
		tabQuery.setIcon(FontAwesome.HOME);
		getContent().addComponent(mainTab);			
	}
	public void create(){
		SasJsonService sasJson =new SasJsonService();
		sasJson.setSasCgiBroker(systemProps.getProperty("_SasCgi"));
		   Properties pp =new Properties();
		   pp.put("_service", "default4");
		   pp.put("_program", "ibspgm.json_test.sas");
		   pp.put("xpgn"    , "noCheck");
		String jsonString =sasJson.getSasJson(pp);
	
		JsonContainer jsonData = JsonContainer.Factory.newInstance(jsonString);

		pagePanel.getToolBar().setSid(className);
		final PagedFilterTable    pageTable     = pagePanel.getPagedFilterTable();
		pageTable.setId("transdTable");
		pageTable.setContainerDataSource(jsonData);
		//定義要顯示的欄位與名稱
		
		pageTable.setColumnHeader("aobrh"    ,"分行別");
		pageTable.setColumnHeader("resp_no"    ,"AO code");
		pageTable.setColumnHeader("aonam"     ,"AO 姓名" );
		pageTable.setColumnHeader("amt01_92"     ,"累積淨利息收入");
		pageTable.setColumnHeader("qota_int"     ,"累積淨利息收入預算數");
		pageTable.setVisibleColumns(new Object[]{"aobrh" ,"resp_no","aonam","amt01_92","qota_int"});
		getContent().addComponent(vchart);
		//圖表按鈕
		this.pagePanel.getToolBar().setChartButtonVisible(true);
		this.pagePanel.getToolBar().chartBu.addClickListener(
    		    new ClickListener() {
    				@Override
    				public void buttonClick(ClickEvent event) {
    					Filterable filterable =pageTable.getFilterable();
						 new vc0101Popover(filterable);
    				}
        		}
        	);
	}
	
}
