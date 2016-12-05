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
import com.vaadin.ui.TabSheet.Tab;

/**
 * 區域中心設定
 * @author 3471
 *
 */
@SuppressWarnings("serial")
public class gc1030 extends ScsbGlobView {
	TabSheet     mainTab    =new TabSheet();	
	ScsbPage    pagePanel     = new ScsbPage();	
	/**
	 * 區域中心設定-主畫面
	 */	
	public gc1030() {
		create();
	    mainTab.setImmediate(true);
	    String tabCaption =i18nProps.getProperty("main","首頁");
		Tab tabQuery =mainTab.addTab(pagePanel.getContent(),tabCaption);
		tabQuery.setIcon(FontAwesome.HOME);
		getContent().addComponent(mainTab);	  		
	}

	public void create(){		
		CcCodeService ccCodeSrv =new CcCodeService();
		final BeanContainer<String,CcCode> ccCodeContainer =ccCodeSrv.getCcCode_Kind("011");
		ccCodeContainer.sort(new Object[]{"codeId"}, new boolean[]{true});
		
		pagePanel.getToolBar().setSid(className);
		final PagedFilterTable    pageTable     = pagePanel.getPagedFilterTable();
        pageTable.setId("fnbct0");
        pageTable.setContainerDataSource(ccCodeContainer);
		if (ccCodeContainer.size() >=10)	pageTable.setPageLength(10);
		else pageTable.setPageLength(pageTable.size());		
		//定義要顯示的欄位與名稱
		pageTable.setColumnHeader("codeId"    ,i18nProps.getProperty("areaId","區域中心代碼"));
		pageTable.setColumnHeader("codeName"  ,i18nProps.getProperty("areaName","區域中心名稱") );
		pageTable.setVisibleColumns(new Object[]{"codeId","codeName"});
		//選取Table資料時
	    Property.ValueChangeListener listener = new Property.ValueChangeListener() {
	       	public void valueChange(Property.ValueChangeEvent event) {
	       		BeanItem<CcCode> beanitem=(BeanItem<CcCode>) ccCodeContainer.getItem(pageTable.getValue());
				if (beanitem != null){
	    			CcCode bean =beanitem.getBean();
					new gc1030_property(className ,bean ,mainTab ,pagePanel);
					pageTable.select(null);			
	       		}
	        }
	    };
	    pageTable.addValueChangeListener(listener);

	}
	
}
