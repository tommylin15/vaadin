package com.scsb.vaadin.include;

import com.scsb.vaadin.composite.ScsbGlob;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;

/**
 * 功能標題
 */
public class TitleLayout extends ScsbGlob { 
  	public VerticalLayout vLayout =new VerticalLayout();
  	Label label = new Label();
	public TitleLayout(String name){
        //名稱列		
		VerticalLayout dataLayout = new VerticalLayout();
		dataLayout.setHeight("30px");
		
        label.setContentMode(ContentMode.HTML);
        label.setImmediate(true);
        label.setSizeFull();
        setLabel(name);

        dataLayout.setStyleName("topbg1");
        dataLayout.addComponent(label);
        dataLayout.setComponentAlignment(label, Alignment.MIDDLE_CENTER);
        dataLayout.setWidth("100%");

        vLayout.addComponent(dataLayout); 
        vLayout.setComponentAlignment(dataLayout, Alignment.TOP_CENTER);
	}
	
	public void setLabel(String name){
        label.setValue("<div style=\"background-color:#2D6BA5;border-bottom: 2px solid #555;\" align=center><font color=#FFFFFF><B>"+name+"</B></font></div>");
	}

}
