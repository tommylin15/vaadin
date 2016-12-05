package com.scsb.vaadin.ui.gc;

import org.tepi.filtertable.paged.PagedFilterTable;

import com.scsb.db.bean.Transd;
import com.scsb.db.service.TransdService;
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
 * 交易明細設定
 * @author 3471
 *
 */
@SuppressWarnings("serial")
public class gc1020_transD extends ScsbGlobView {
	TabSheet     mainTab ;	
	Tab  tabProperty; 		
	String sid ;
	ScsbPage    pagePanel     = new ScsbPage();
	PagedFilterTable    pageTable     = pagePanel.getPagedFilterTable();
	TransdService transSrv =new TransdService();
	BeanContainer<String,Transd> transdContainer;
	
	Transd srcBean;	
	/**
	 * 交易明細設定-主畫面
	 */
	public gc1020_transD(String sid ,Transd orgBean ,TabSheet mainTab) {
		this.srcBean=orgBean;
		this.mainTab=mainTab;
		this.sid=sid;		
		loadData();
		buListener();
		String tabCaption =i18nProps.getProperty("detail","-明細");
		if (srcBean != null)  tabCaption =srcBean.getGroupid()+tabCaption;
	    tabProperty =mainTab.addTab(pagePanel.getContent() ,tabCaption);
	    tabProperty.setClosable(true);
	    tabProperty.setIcon(FontAwesome.SEARCH);
	    mainTab.setSelectedTab(tabProperty);  		
	}
	public void loadData(){
		
		transdContainer =transSrv.getTransd_GroupId(srcBean.getGroupid());
		transdContainer.sort(new Object[]{"groupid","programid"}, new boolean[]{true,true});
		
        pageTable.setId("transd");
        pageTable.setContainerDataSource(transdContainer);
		//定義要顯示的欄位與名稱
		pageTable.setColumnHeader("programid"    ,i18nProps.getProperty("programId","程式代碼"));
		pageTable.setColumnHeader("programname"  ,i18nProps.getProperty("programName","程式名稱") );
		pageTable.setColumnHeader("programmode"  ,i18nProps.getProperty("programMode","是否啟用") );
		pageTable.setVisibleColumns(new Object[]{"programid","programname","programmode"});
	}
	
	public void buListener(){		
		pagePanel.getToolBar().setSid(sid);		
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
		if (srcBean.getGroupid().indexOf("_")>-1){
			pagePanel.getToolBar().setAddButtonVisible(true);
		}
		pagePanel.getToolBar().addBu.addClickListener(
    		    new ClickListener() {
    				@Override
    				public void buttonClick(ClickEvent event) {
    					Transd bean =new Transd();
    					bean.setProgramname(messageProps.getProperty("InsertMode","新增作業"));
    					bean.setGroupid(srcBean.getGroupid());
    					gc1020_transD_property propView =new gc1020_transD_property(sid ,bean ,true ,mainTab ,pagePanel);
    				}
        		}
        	);		
		//選取Table資料時
	    Property.ValueChangeListener listener = new Property.ValueChangeListener() {
	       	public void valueChange(Property.ValueChangeEvent event) {
	       		BeanItem<Transd> beanitem=(BeanItem<Transd>) transdContainer.getItem(pageTable.getValue());
				if (beanitem != null){
					Transd bean =beanitem.getBean();
					gc1020_transD_property propView =new gc1020_transD_property(sid ,bean ,false ,mainTab ,pagePanel);
					pageTable.select(null);					
	       		}
	        }
	    };
	    pageTable.addValueChangeListener(listener);		
	}
}
