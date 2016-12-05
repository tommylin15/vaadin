package com.scsb.vaadin.r;

import com.vaadin.shared.ui.JavaScriptComponentState;

import elemental.json.JsonArray;

public class R4HtmlState extends JavaScriptComponentState {
	public String value="";
	public String tagName="a";
	public String WaterMarkName;
	public String ynPrint ="N"; 
	
	public void setValue(String string) {
		this.value=string;
	}
	public void setTagName(String string) {
		this.tagName=string;
	}
}