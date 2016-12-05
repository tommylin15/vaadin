package com.scsb.vaadin.ui.gc;

import org.tepi.filtertable.paged.PagedFilterTable;

import com.scsb.db.bean.Users;
import com.scsb.db.service.UsersService;
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
 * 使用者設定
 * @author 3471
 *
 */
@SuppressWarnings("serial")
public class gc6020 extends ScsbGlobView {
	TabSheet     mainTab    =new TabSheet();
	ScsbPage    pagePanel     = new ScsbPage();
	
	UsersService usersSrv =new UsersService();
	BeanContainer<String,Users> usersContainer;
	PagedFilterTable    pageTable     = pagePanel.getPagedFilterTable();
	/**
	 * 使用者設定-主畫面
	 */
	public gc6020() {
		loadData();
		buListener();
	    mainTab.setImmediate(true);
	    String tabCaption =i18nProps.getProperty("main","首頁");
		Tab tabQuery =mainTab.addTab(pagePanel.getContent(),tabCaption);
		tabQuery.setIcon(FontAwesome.HOME);
		getContent().addComponent(mainTab);	  		
	}
	public void loadData(){
		usersContainer =usersSrv.getUsers_All();
		usersContainer.sort(new Object[]{"userid"},new boolean[]{true});
		
		pagePanel.getToolBar().setSid(className);
        pageTable.setId("ccCodeTable");
        pageTable.setContainerDataSource(usersContainer);
		if (usersContainer.size() >=10)	pageTable.setPageLength(10);
		else pageTable.setPageLength(pageTable.size());		
		//定義要顯示的欄位與名稱
		pageTable.setColumnHeader("userid"    ,i18nProps.getProperty("userId","使用者行編"));
		pageTable.setColumnHeader("username"  ,i18nProps.getProperty("userName","使用者姓名") );
		pageTable.setVisibleColumns(new Object[]{"userid","userName"});
		
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
	       		BeanItem<Users> beanitem=(BeanItem<Users>) usersContainer.getItem(pageTable.getValue());
				if (beanitem != null){
					Users bean =beanitem.getBean();
					new gc6020_property(className ,bean ,false ,mainTab,pagePanel);
					pageTable.select(null);
	       		}
	        }
	    };
	    pageTable.addValueChangeListener(listener);
	    
	}
}
