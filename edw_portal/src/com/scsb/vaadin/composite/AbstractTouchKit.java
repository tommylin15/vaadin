package com.scsb.vaadin.composite;

import org.junit.Ignore;

import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

@Ignore
public class AbstractTouchKit extends  Panel  {
	   
    public AbstractTouchKit() {  	
        //this.setSizeFull();
    }
   
    public static void makeSmallTabletSize(Component c) {
        c.setWidth("450px");
        c.setHeight("640px");
    }

}