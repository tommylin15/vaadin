package com.scsb.vaadin.ui.popover;

import java.util.Hashtable;

import com.scsb.db.bean.Trans;
import com.scsb.domain.HashTrans;
import com.scsb.vaadin.ScsbUI;
import com.scsb.vaadin.composite.ScsbPopover;
import com.scsb.vaadin.ui.MainView;
import com.scsb.vaadin.ui.TransView;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;
/**
 * SCSB-SAS主選單
 * @author 3471
 *
 */
@SuppressWarnings("serial")
public class MenuPopover extends ScsbPopover {
	
	public MenuPopover() {
    	loadI18N(lang, "TransView");		
		this.setTitle("<font color='red'><b>"+i18nProps.getProperty("title","導航選單")+"</b>&nbsp;</font>");
        //主選單
		Hashtable<String ,String> userTrans =(Hashtable<String ,String>)session.getAttribute("UserTrans");
        BeanContainer<String,Trans> transContainer = hashTrans.getAllTrans();
        transContainer.sort(new Object[]{"groupid"}, new boolean[]{true});
		for(int i=0;i<transContainer.size();i++){
			final Trans bean=transContainer.getItem(transContainer.getIdByIndex(i)).getBean();
	    	//導航選單
			if (bean.getGroupid().indexOf("_") == -1
				&& userTrans.get(bean.getGroupid()) != null //使用者有權限者
				){
		    	final Button menuBu =new Button();
		    	menuBu.setCaption(i18nProps.getProperty(bean.getGroupid(),bean.getGroupname()));
		    	menuBu.setDescription(menuBu.getCaption());
		    	menuBu.setId(bean.getGroupid());
		    	menuBu.setWidth("100%");
		    	menuBu.addStyleName("primary");
				//debug mode
				if (systemProps.getProperty("DEBUG_MODE").equals("true"))
					menuBu.setCaption(menuBu.getCaption()+"("+menuBu.getId()+")");		    	
		    	menuBu.addClickListener( new ClickListener() {
						@Override
						public void buttonClick(ClickEvent event) {
							try {
								TransView transView =new TransView(bean.getGroupid());
								transView.getView().setCaption(menuBu.getCaption());
								//((ScsbUI)UI.getCurrent()).getScsbManager().removeAllComponents();
								//((ScsbUI)UI.getCurrent()).getScsbManager().setPreviousComponent(null);
								//((ScsbUI)UI.getCurrent()).getScsbManager().getViewStack().removeAllElements();					
								//((ScsbUI)UI.getCurrent()).getScsbManager().setCurrentComponent(transView.view);
								((ScsbUI)UI.getCurrent()).getScsbManager().navigateTo(transView.getView());
						        popover.close();								
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
		    	});
		    	this.content.addComponent(menuBu);
		    	this.content.setSpacing(true);
			}
		}        
    }
}