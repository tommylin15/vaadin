package com.scsb.vaadin.composite;

import org.tepi.filtertable.paged.PagedFilterControlConfig;
import org.tepi.filtertable.paged.PagedFilterTable;
import org.tepi.filtertable.paged.PagedTableChangeEvent;
import org.tepi.filtertable.paged.PagedFilterTable.PageChangeListener;

import com.vaadin.data.Container;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class ScsbPage extends ScsbGlob {
	
	PagedFilterTable    pageTable     = new PagedFilterTable();
	ScsbToolbar		  toolBar		=new ScsbToolbar();
	
	Component breakComp;
	
	Panel content = new Panel();
	VerticalLayout vlayout = new VerticalLayout();
	
	public PagedFilterTable getPagedFilterTable(){
		return pageTable;
	}
	
    public ScsbPage() {
    	this(0);
    }
    public ScsbPage(int totCount) {
    	vlayout.setMargin(new MarginInfo(false,false,false,false));
    	vlayout.setImmediate(true);
    	content.setContent(vlayout);
	
		content.setWidth("100%");
		// screenHeight = _screenHeight - tag head
		content.setHeight((_screenHeight-40)+"px");
    	
    	toolBar.setVisible(true);
    	addComponent(toolBar.getContent());
		
    	PagedFilterControlConfig conftrol =new PagedFilterControlConfig();
    	if (totCount > 0){
	    	conftrol.setItemsPerPage(
	    			   messageProps.getProperty("totalCount","總筆數：")
	    			+totCount+"，"
	    			+messageProps.getProperty("onePage","每頁筆數："));
    	}else{
	    	conftrol.setItemsPerPage(messageProps.getProperty("onePage","每頁筆數："));
    	}
    	conftrol.setPage(messageProps.getProperty("nowPage","目前頁碼："));
    	
    	HorizontalLayout hControl =pageTable.createControls(conftrol);
    	//HorizontalLayout hControl =pageTable.createControls(new PagedFilterControlConfig());
		addComponent(hControl);
		
        //pageTable.setWidth("100%");
		Panel panel =new Panel();
		panel.setWidth("100%");
		// screenHeight = _screenHeight - tag head -toolBar -hControl
		panel.setHeight((_screenHeight-40 -40 -30)+"px");
		panel.setContent(pageTable);
		
		//pageTable.setSizeFull();//使用sizefull會造成換頁筆數不對
		pageTable.setWidth("100%");
        pageTable.setFilterBarVisible(true);
        pageTable.setSelectable(true);
        pageTable.setImmediate(true); 
        pageTable.setSortEnabled(true);
        //pageTable.setPageLength(25);
        pageTable.addStyleName("filtertable");
        pageTable.addStyleName("scsbBg1");
        
		addComponent(panel);
    }
    
    public void addComponent(Component c){
    	vlayout.addComponent(c);
    	vlayout.setComponentAlignment(c, Alignment.TOP_CENTER);
    }
    public Panel getContent(){
    	return content;
    }
    
    public ScsbToolbar getToolBar(){
    	return toolBar;
    }
}
