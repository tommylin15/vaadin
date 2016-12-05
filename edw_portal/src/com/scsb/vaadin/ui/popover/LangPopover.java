package com.scsb.vaadin.ui.popover;

import com.scsb.vaadin.composite.ScsbPopover;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
/**
 * 多語系選單
 * @author 3471
 *
 */
@SuppressWarnings("serial")
public class LangPopover extends ScsbPopover {
	
	public LangPopover() {
		
		this.setTitle("<font color='red'><b>"+i18nProps.getProperty("LangSet","多語系選單")+"</b>&nbsp;</font>");
    	final Button twBu =new Button("中文");        	
    	twBu.setDescription("中文");
    	twBu.setId("tw");
    	twBu.setIcon(new ThemeResource("flags/tw.gif"));
    	twBu.setWidth("100%");
    	twBu.addStyleName("borderless-colored");
    	twBu.addClickListener( new ClickListener() {
				@Override
				public void buttonClick(ClickEvent event) {
					try {
						session.setAttribute("Language", twBu.getId());
						UI.getCurrent().getPage().reload();
				        popover.close();				
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
    	});
    	this.content.addComponent(twBu);
    	
    	final Button enBu =new Button("English");        	
    	enBu.setDescription("English");
    	enBu.setId("en");
    	enBu.setIcon(new ThemeResource("flags/en.gif"));
    	enBu.setWidth("100%");
    	enBu.addStyleName("borderless-colored");	
    	enBu.addClickListener( new ClickListener() {
				@Override
				public void buttonClick(ClickEvent event) {
					try {
						session.setAttribute("Language", enBu.getId());
						UI.getCurrent().getPage().reload();
						popover.close();				
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
    	});
    	this.content.addComponent(enBu);
    	this.content.setSpacing(true);
    }
}