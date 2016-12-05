package com.scsb.vaadin.ui.gc;

import org.tepi.filtertable.paged.PagedFilterTable;

import com.scsb.db.bean.Job;
import com.scsb.db.service.JobService;
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
 * 工作內容設定
 * @author 3471
 *
 */
@SuppressWarnings("serial")
public class gc6012 extends ScsbGlobView {
	TabSheet     mainTab    =new TabSheet();		
	ScsbPage    pagePanel     = new ScsbPage();
	
	JobService jobSrv =new JobService();
	BeanContainer<String,Job> jobContainer;
	PagedFilterTable    pageTable     = pagePanel.getPagedFilterTable();		
	/**
	 * 工作內容設定-主畫面
	 */
	public gc6012() {
		loadData();
		buListener();
	    mainTab.setImmediate(true);
	    String tabCaption =i18nProps.getProperty("main","首頁");
		Tab tabQuery =mainTab.addTab(pagePanel.getContent(),tabCaption);
		tabQuery.setIcon(FontAwesome.HOME);
		getContent().addComponent(mainTab);	  		
	}
	public void loadData(){
		
		jobContainer =jobSrv.getJob_All();
		jobContainer.sort(new Object[]{"jobName"}, new boolean[]{true});

		pagePanel.getToolBar().setSid(className);
        pageTable.setId("ccCodeTable");
        pageTable.setContainerDataSource(jobContainer);
		if (jobContainer.size() >=10)	pageTable.setPageLength(10);
		else pageTable.setPageLength(pageTable.size());		
		//定義要顯示的欄位與名稱
		pageTable.setColumnHeader("jobName"  ,i18nProps.getProperty("jobName","工作內容") );
		pageTable.setVisibleColumns(new Object[]{"jobName"});
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
	       		BeanItem<Job> beanitem=(BeanItem<Job>) jobContainer.getItem(pageTable.getValue());
				if (beanitem != null){
					Job bean =beanitem.getBean();
					new gc6012_property(className ,bean ,false ,mainTab ,pagePanel);
					pageTable.select(null);
	       		}
	        }
	    };
	    pageTable.addValueChangeListener(listener);	

	}
}
