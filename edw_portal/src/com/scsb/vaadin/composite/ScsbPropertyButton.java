package com.scsb.vaadin.composite;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;

public class ScsbPropertyButton extends Button {
   
    public ScsbPropertyButton(String caption) {
    	this();
        setCaption(caption);
    }
    public ScsbPropertyButton(){
    	setIcon(FontAwesome.ARROW_RIGHT);
    	addStyleName("textLeft");
    	addStyleName("scsbBg2");
    	addStyleName("icon-align-right");
    	setWidth("100%");    	
    }
}
