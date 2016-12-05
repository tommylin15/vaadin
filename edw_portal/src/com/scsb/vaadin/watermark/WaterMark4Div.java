package com.scsb.vaadin.watermark;

import com.vaadin.annotations.JavaScript;
import com.vaadin.ui.AbstractJavaScriptComponent;

@SuppressWarnings("serial")
@JavaScript({"watermark.js", "watermark-connector.js"})
public class WaterMark4Div extends AbstractJavaScriptComponent {
	private String printData="";
	public WaterMark4Div(String value) {
		    getState().value=value;
		    getState().ynPrint="N";
		    /*
	        addFunction("onReturn", new JavaScriptFunction() {
				@Override
				public void call(org.json.JSONArray arguments)
						throws JSONException {
						printData=arguments.getString(0);						
				}
	        });
	        */
	        
	}	
    public void setValue(String value) {
        getState().value=value;
        getState().ynPrint="N";
    }
    public String getValue() {
        return getState().value;
    }  
    public void setWaterMark(String value) {
        getState().WaterMarkName=value;
        getState().ynPrint="N";
    } 
    public String getPrintData() {
        return printData;
    }
    public String getWaterMark() {
        return getState().WaterMarkName;
    }
    
    public void runPrintData(){
		getState().ynPrint="Y";
    }
 
    @Override
    protected WaterMarkState getState() {
        return (WaterMarkState) super.getState();
    }
}