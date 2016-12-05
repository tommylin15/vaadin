package com.scsb.vaadin.ui.gc;

import java.util.Properties;

import org.tepi.filtertable.FilterTable;
import org.tepi.filtertable.paged.PagedFilterTable;

import com.scsb.db.bean.Ldapou;
import com.scsb.db.service.LdapouService;
import com.scsb.vaadin.composite.ScsbGlobView;
import com.scsb.vaadin.composite.ScsbPage;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.Tab;

/**
 * 部門設定
 * @author 3471
 *
 */
@SuppressWarnings("serial")
public class gc6013 extends ScsbGlobView {
	TabSheet     mainTab    =new TabSheet();			
	/**
	 * 部門設定-主畫面
	 */
	public gc6013() {
		LdapouService ldapouSrv =new LdapouService();
		final BeanContainer<String,Ldapou> ldapouContainer =ldapouSrv.getSasLdapou_All();
		ldapouContainer.sort(new Object[]{"ou"}, new boolean[]{true});
		
		ScsbPage    pagePanel     = new ScsbPage();
		pagePanel.getToolBar().setSid(className);
		final PagedFilterTable    pageTable     = pagePanel.getPagedFilterTable();
        pageTable.setId("ldapou");
        pageTable.setContainerDataSource(ldapouContainer);
		if (ldapouContainer.size() >=10)	pageTable.setPageLength(10);
		else pageTable.setPageLength(pageTable.size());		
		//定義要顯示的欄位與名稱
		pageTable.setColumnHeader("ou"    ,i18nProps.getProperty("ou","部門代碼") );
		pageTable.setColumnHeader("description"    ,i18nProps.getProperty("description","部門名稱") );
		pageTable.setVisibleColumns(new Object[]{"ou","description"});
		//選取Table資料時
	    Property.ValueChangeListener listener = new Property.ValueChangeListener() {
	       	public void valueChange(Property.ValueChangeEvent event) {
	       		BeanItem<Ldapou> beanitem=(BeanItem<Ldapou>) ldapouContainer.getItem(pageTable.getValue());
				if (beanitem != null){
					Properties pp =new Properties();
					//分行屬性
					new gc6013_property( className ,beanitem.getBean(),pp ,mainTab);
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
