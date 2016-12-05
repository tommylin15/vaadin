package com.scsb.vaadin.ui.gc;

import org.tepi.filtertable.FilterTable;
import org.tepi.filtertable.paged.PagedFilterTable;

import com.scsb.db.bean.Roles;
import com.scsb.db.bean.Users;
import com.scsb.db.service.RolesService;
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
 * 單位主管授權設定
 * @author 3471
 *
 */
@SuppressWarnings("serial")
public class gc6120 extends ScsbGlobView {
	TabSheet     mainTab    =new TabSheet();		
	/**
	 * 使用者設定-主畫面
	 */
	public gc6120() {
		create();
	}
	public void create(){
		UsersService usersSrv =new UsersService();
		final BeanContainer<String,Users> usersContainer =usersSrv.getDeptUsers(users);
		usersContainer.sort(new Object[]{"userid"},new boolean[]{true});
		
		ScsbPage    pagePanel     = new ScsbPage();
		pagePanel.getToolBar().setSid(className);
		final PagedFilterTable    pageTable     = pagePanel.getPagedFilterTable();
        pageTable.setId("ccCodeTable");
        pageTable.setContainerDataSource(usersContainer);
		if (usersContainer.size() >=10)	pageTable.setPageLength(10);
		else pageTable.setPageLength(pageTable.size());		
		//定義要顯示的欄位與名稱
		pageTable.setColumnHeader("userid"    ,i18nProps.getProperty("userId","使用者行編"));
		pageTable.setColumnHeader("username"  ,i18nProps.getProperty("userName","使用者姓名") );
		pageTable.setVisibleColumns(new Object[]{"userid","username"});

		//選取Table資料時
	    Property.ValueChangeListener listener = new Property.ValueChangeListener() {
	       	public void valueChange(Property.ValueChangeEvent event) {
	       		BeanItem<Users> beanitem=(BeanItem<Users>) usersContainer.getItem(pageTable.getValue());
				if (beanitem != null){
					Users bean =beanitem.getBean();
					new gc6120_property(className ,bean ,false ,mainTab);
					pageTable.select(null);
	       		}
	        }
	    };
	    pageTable.addValueChangeListener(listener);
	    
	    mainTab.setImmediate(true);
	    String tabCaption =i18nProps.getProperty("main","首頁");
		Tab tabQuery =mainTab.addTab(pagePanel.getContent(),tabCaption);
		tabQuery.setIcon(FontAwesome.HOME);
		getContent().addComponent(mainTab);	  		    
	}
}
