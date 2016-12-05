package com.scsb.vaadin.ui.gc;

import com.scsb.db.bean.CcCode;
import com.scsb.vaadin.composite.ScsbPage;
import com.scsb.vaadin.composite.ScsbProperty;
import com.scsb.vaadin.composite.ScsbPropertyButton;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.Tab;

/**
 * 區域中心屬性設定
 * @author 3471
 *
 */
@SuppressWarnings("serial")
public class gc1030_property extends ScsbProperty {
	TabSheet     mainTab    =new TabSheet();	
	Tab  tabProperty;
	ScsbPage scsbPage;	
	String sid;
	/**
	 * 區域中心屬性設定-主畫面
	 */
	CcCode srcBean;
	public gc1030_property(String sidx,CcCode orgBean ,TabSheet mainTabx ,ScsbPage scsbPage) {
		this.srcBean=orgBean;
		this.mainTab=mainTabx;
		this.sid=sidx;
		this.scsbPage=scsbPage;
		this.getToolBar().setSid(sid);	
		//所屬分行別設定
		final ScsbPropertyButton menuBu =new ScsbPropertyButton(i18nProps.getProperty("SubBranch","所屬分行"));        	
    	menuBu.setDescription(messageProps.getProperty("Button_edit_view","編輯/檢視"));
    	menuBu.setId("AN_AREA");

    	menuBu.addClickListener(new ClickListener() {
    		@Override
			public void buttonClick(ClickEvent event) {
				new gc1030_AnBrhView(sid ,srcBean.getCodeName()+"-"+menuBu.getCaption() ,srcBean.getCodeId() ,menuBu.getId() , mainTab);
			}			    		
    	});
    	addComponent(menuBu);
    	
		String tabCaption =i18nProps.getProperty("propertySet","-屬性設定");
		if (srcBean != null)  tabCaption =srcBean.getCodeName()+tabCaption;
	    tabProperty =mainTab.addTab(getContent() ,tabCaption);
	    tabProperty.setClosable(true);
	    tabProperty.setIcon(FontAwesome.EDIT);
	    mainTab.setSelectedTab(tabProperty);       	
	}
}
