package com.scsb.vaadin.ui.rc;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.scsb.vaadin.composite.ScsbGlobView;
import com.scsb.vaadin.composite.ScsbProperty;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.VerticalLayout;

/**
 * 公用參數
 * @author 3471
 *
 */
@SuppressWarnings("serial")
public class rc1001 extends ScsbGlobView {
	TabSheet     mainTab    =new TabSheet();	
	VerticalLayout mainLayout =new VerticalLayout();
	public rc1001() {
		create();
	    mainTab.setImmediate(true);
	    String tabCaption =i18nProps.getProperty("main","首頁");
    
		Tab tabQuery =mainTab.addTab(mainLayout,tabCaption);
		tabQuery.setIcon(FontAwesome.HOME);
		getContent().addComponent(mainTab);
	}

	public void create(){	
		Button queryBu =new Button("查詢");
		queryBu.addClickListener(queryBulistener);
		mainLayout.addComponent(queryBu);		
	}
	ClickListener queryBulistener = new ClickListener() {
		@Override
		public void buttonClick(ClickEvent event) {
			new rcQuery("rcQuery" ,mainTab );
		}
	};
}
