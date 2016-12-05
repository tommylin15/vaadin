package com.scsb.vaadin.composite;

import com.vaadin.ui.Label;

@SuppressWarnings("serial")
public class EmptyLabel extends Label {
	private String  sHeight ="5px";
	public EmptyLabel(){
		this.setWidth("100%");
		this.setId("EmptyLabel");
		this.setHeight(sHeight);
	}
	public String getH(){
		return sHeight;
	}
}