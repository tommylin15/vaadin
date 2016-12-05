package com.scsb.vaadin.ui.popover;

import com.scsb.vaadin.composite.ScsbPopover;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
/**
 * 操作說明
 * @author 3471
 *
 */
@SuppressWarnings("serial")
public class HelpPopover extends ScsbPopover {
	public Window helpWin =new Window();
	public HelpPopover(String fileId) {
		
		helpWin.setWidth("90%");
		helpWin.setHeight("90%");
		this.setTitle("<font color='red'><b>"+i18nProps.getProperty("HelpSet","操作說明")+"</b>&nbsp;</font>");
		String sUrl =systemProps.getProperty("_HelpPath")+"/"+fileId+".html";
		BrowserFrame browser =new BrowserFrame(null,new ExternalResource(sUrl));
        browser.setWidth("100%");
        int iHeight=(int) (UI.getCurrent().getPage().getBrowserWindowHeight()*0.9 -80);
        browser.setHeight(iHeight,Unit.PIXELS);
        helpWin.setContent(browser);
    }
}