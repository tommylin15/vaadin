package com.scsb.vaadin.ui.gc;

import org.tepi.filtertable.paged.PagedFilterTable;

import com.scsb.db.bean.Roles;
import com.scsb.db.service.RolesService;
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
 * 角色設定
 * @author 3471
 *
 */
@SuppressWarnings("serial")
public class gc6010 extends ScsbGlobView {
	TabSheet     mainTab    =new TabSheet();	
	ScsbPage    pagePanel     = new ScsbPage();
	
	RolesService rolesSrv =new RolesService();
	PagedFilterTable    pageTable     = pagePanel.getPagedFilterTable();
	BeanContainer<String,Roles> rolesContainer;
	/**
	 * 角色設定-主畫面
	 */
	public gc6010() {
		loadData();
		buListener();
	    mainTab.setImmediate(true);
	    String tabCaption =i18nProps.getProperty("main","首頁");
		Tab tabQuery =mainTab.addTab(pagePanel.getContent(),tabCaption);
		tabQuery.setIcon(FontAwesome.HOME);
		getContent().addComponent(mainTab);			
	}

	public void loadData(){
		rolesContainer =rolesSrv.getRoles_All();
		rolesContainer.sort(new Object[]{"roleid"}, new boolean[]{true});
		
		pagePanel.getToolBar().setSid(className);
        pageTable.setId("ccCodeTable");
        pageTable.setContainerDataSource(rolesContainer);
		//定義要顯示的欄位與名稱
		pageTable.setColumnHeader("roleid"    ,i18nProps.getProperty("roleId","角色代碼"));
		pageTable.setColumnHeader("rolename"  ,i18nProps.getProperty("roleName","角色名稱") );
		pageTable.setVisibleColumns(new Object[]{"roleid","rolename"});
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
    					Roles bean =new Roles();
    					new gc6010_property(className ,bean ,true ,mainTab ,pagePanel);
    				}
        		}
        	);		
		//選取Table資料時
	    Property.ValueChangeListener listener = new Property.ValueChangeListener() {
	       	public void valueChange(Property.ValueChangeEvent event) {
	       		BeanItem<Roles> beanitem=(BeanItem<Roles>) rolesContainer.getItem(pageTable.getValue());
				if (beanitem != null){
					Roles bean =beanitem.getBean();
					new gc6010_property(className ,bean ,false ,mainTab ,pagePanel);
					pageTable.select(null);
	       		}
	        }
	    };
	    pageTable.addValueChangeListener(listener);
	}
}
