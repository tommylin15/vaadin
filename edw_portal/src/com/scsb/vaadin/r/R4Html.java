package com.scsb.vaadin.r;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;

import com.vaadin.annotations.JavaScript;
import com.vaadin.ui.AbstractJavaScriptComponent;
import com.vaadin.ui.JavaScriptFunction;

import elemental.json.JsonArray;

@JavaScript({ "r4html-connector.js"})
public class R4Html extends AbstractJavaScriptComponent {
	private Hashtable<String,String> hashRequest =new Hashtable<String,String>();
	private String printData="";
	
	public R4Html(String value) {
		    getState().value=value;
		    getState().ynPrint="N";
		    
		    addFunction("onClick", new JavaScriptFunction() {
				@Override
				public void call(JsonArray arguments) {
					JsonArray arr_name=arguments.getArray(0).getArray(0);
					JsonArray arr_value=arguments.getArray(0).getArray(1);
					for(int i=0;i<arr_name.length();i++){
						hashRequest.put(arr_name.getString(i), arr_value.getString(i));
					}
		            for (ValueChangeListener listener: listeners)
		                listener.valueChange();
				}
		    });   
		    
		    this.setWidth("100%");
	}
	
    public interface ValueChangeListener extends Serializable {
        void valueChange();
    }
     ArrayList<ValueChangeListener> listeners =   new ArrayList<ValueChangeListener>();
    
   public void addValueChangeListener(
    	ValueChangeListener listener) {
        		listeners.add(listener);
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
    
    public String getWaterMark() {
        return getState().WaterMarkName;
    }
    
    public void runPrintData(){
		getState().ynPrint="Y";
    }    
    
    public  Hashtable<String,String> getHashRequest() {
        return hashRequest;
    }  
  
    @Override
    protected R4HtmlState getState() {
        return (R4HtmlState) super.getState();
    }
}