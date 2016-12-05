package com.scsb.vaadin.ui;

import com.scsb.vaadin.composite.ScsbGlobView;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.WebBrowser;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;

import elemental.client.Browser;

@SuppressWarnings("serial")
public class TransBrowser extends ScsbGlobView {
	/**
	 *主畫面
	 */
	public TransBrowser(String sUrl) {
		
		CssLayout layout = new CssLayout();
		layout.setWidth("100%");
		layout.setHeight(_screenHeight+"px");
		
		BrowserFrame browser = new BrowserFrame();
		browser.setSizeFull();
		layout.addComponent(browser);

		browser.setSource(new ExternalResource(sUrl));		
		
        getView().setContent(layout);
	}
}
