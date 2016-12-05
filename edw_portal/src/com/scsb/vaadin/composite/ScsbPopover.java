package com.scsb.vaadin.composite;

import java.util.Properties;

import com.scsb.addons.ui.Popover;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.BaseTheme;
/**
 * SCSB-彈出視窗
 * @author 3471
 *
 */
@SuppressWarnings("serial")
public class ScsbPopover extends ScsbGlob {
	public Popover popover =new Popover();
	protected VerticalLayout content =new VerticalLayout();
	protected Button editBu =new Button(""); 
	protected Button closeBu =new Button(""); 
	public ScsbPopover() {
		this("");
	}
	public ScsbPopover(String titleValue) {
		this.lang =session.getAttribute("Language")+"";
		this.setTitle(titleValue);
		popover.setWidth("250px");
		popover.setHeight("50%");
		popover.setResizable(false);
		popover.setClosable(false);
        this.systemProps = hashSystem.getProperties();
        
		String className =this.getClass().getName().substring(this.getClass().getName().lastIndexOf(".")+1);
		if (className.indexOf("_") > -1) className=className.substring(0,className.indexOf("_"));
		loadI18N(lang,className);    	
        
      	//修改按鈕
    	editBu.setIcon(new ThemeResource("./images/BuEdit.png"));
    	editBu.setId("edit");
    	editBu.setDisableOnClick(true);
    	editBu.setVisible(false);
    	editBu.setStyleName("edit");
    	editBu.addStyleName(BaseTheme.BUTTON_LINK);
    	editBu.addClickListener(
		    new ClickListener() {
				@Override
				public void buttonClick(ClickEvent event) {
					editBu.setVisible(false);
				}
    		}
    	);         
        
      	//關閉按鈕
    	closeBu.setIcon(new ThemeResource("./images/BuClose.png"));
    	closeBu.setId("close");
    	closeBu.setStyleName("close");
    	closeBu.addStyleName(BaseTheme.BUTTON_LINK);
    	closeBu.addClickListener(
		    new ClickListener() {
				@Override
				public void buttonClick(ClickEvent event) {
					popover.close();
				}
    		}
    	);    	

    	AbsoluteLayout layoutTop = new AbsoluteLayout();
    	layoutTop.setWidth("100%");
    	layoutTop.setHeight("45px");
    	
	    title.setSizeUndefined();
	    title.setDescription("");
    	layoutTop.addComponent(title,"left: 5px; top: 4px;");
    	layoutTop.addComponent(editBu,"right: 45px; top: 4px;");
    	layoutTop.addComponent(closeBu,"right: 5px; top: 4px;");
    	
        content.addComponent(layoutTop);
        content.setComponentAlignment(layoutTop, Alignment.TOP_CENTER);
        content.setMargin(new MarginInfo(false,true,false,true));
        content.setSpacing(false);
        popover.setContent(content);
     }

	 /**
	  * 
	  * @param component
	  */
	 public void addContentComponent(Component component){
		 this.content.addComponent(component);
		 this.content.setComponentAlignment(component, Alignment.TOP_CENTER);
		 
	 }	
	
	 /**
	  * 是否使用修改按鈕
	  * @param isVisible
	  */
	 public void setEditButtonVisible(boolean isVisible){
		 this.editBu.setVisible(isVisible);	
		 this.editBu.setEnabled(true);
	 }
	 /**
	  * 選單的標題
	  * @param htmlString
	  */
	 public void setTitle(String htmlString){
		 this.title.setValue("<table width='100%' height='100%' border=0>" +
	    			"<tr valign=center ><td align=right>" +htmlString+
	    			"</td></tr></table>");
	 }
	 /**
	  * 載入多語系選單
	  * @param lang
	  * @param fileName
	  */
	public void loadI18N(String lang ,String fileName){
		i18nProps = hashI18N.getProperties(lang,fileName);
		if (i18nProps == null) i18nProps=new Properties(); 
	}	 
}