package com.scsb.vaadin.ui.gc;

import java.util.Properties;

import com.scsb.vaadin.composite.ScsbPage;
import com.scsb.vaadin.composite.ScsbProperty;
import com.scsb.vaadin.composite.ScsbPropertyButton;
import com.scsb.vaadin.property.GoogleMapView;
import com.vaadin.server.FontAwesome;
import com.vaadin.tapio.googlemaps.GoogleMap;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.Tab;

/**
 * 部門屬性屬性設定
 * @author 3471
 *
 */
@SuppressWarnings("serial")
public class gc1060_property extends ScsbProperty {
	NativeSelect areaBox;
	String brhCod;
	
	TabSheet     mainTab    =new TabSheet();	
	Tab  tabProperty;
	ScsbPage scsbPage;	
	String sid;	
	/**
	 * 部門屬性-主畫面
	 * @param brhCod
	 * @param propertyTitle
	 * @param pp
	 */
	public gc1060_property(String sidx ,String brhCodx ,final String propertyTitle  ,final Properties pp ,TabSheet mainTabx,	ScsbPage scsbPag) {
		this.brhCod =brhCodx;
		this.mainTab=mainTabx;
		this.scsbPage=scsbPag;
		this.sid=sidx;
		this.getToolBar().setSid(sid);	
		//分行的允視部門=========================================================================
		final ScsbPropertyButton menuBu =new ScsbPropertyButton(i18nProps.getProperty("allowBrh","允視部門"));        	
    	menuBu.setDescription(messageProps.getProperty("Button_edit_view","編輯/檢視"));
    	menuBu.setId("ALLOW_BRH");
    	menuBu.addClickListener(new ClickListener() {
    		@Override
			public void buttonClick(ClickEvent event) {
				new gc1060_AllowBrhView(sid ,propertyTitle+"-"+menuBu.getCaption() ,brhCod ,menuBu.getId() ,mainTab);
			}			    		
    	});
    	addComponent(menuBu);
		//Google Map=========================================================================
    	/*
		final Button mapBu =new Button(i18nProps.getProperty("googleMap","分行位置"));        	
		mapBu.setDescription(messageProps.getProperty("Button_view","檢視"));
		mapBu.setId("GOOGLE_MAP");
		mapBu.setIcon(FontAwesome.ARROW_RIGHT);
		mapBu.addStyleName("textLeft");
		mapBu.addStyleName("primary");
		mapBu.addStyleName("icon-align-right");
		mapBu.setWidth("100%");
    	mapBu.addClickListener(new ClickListener() {
    		@Override
			public void buttonClick(ClickEvent event) {
    			GoogleMapView popover =new GoogleMapView(propertyTitle+"-"+mapBu.getCaption(),pp);
    			GoogleMap googleMap=popover.getGoogleMap();
    			popover.getContent().addComponent(googleMap);
    			popover.getContent().setSizeFull();
			}			    		
    	});
    	getContent().addComponent(mapBu);
    	*/
		//儲存動作
		//saveBu.addClickListener(saveBulistener);		
    	
		String tabCaption =i18nProps.getProperty("propertySet","屬性設定");
	    tabProperty =mainTab.addTab(getContent() ,tabCaption);
	    tabProperty.setClosable(true);
	    tabProperty.setIcon(FontAwesome.EDIT);
	    mainTab.setSelectedTab(tabProperty);       	
	}
}
