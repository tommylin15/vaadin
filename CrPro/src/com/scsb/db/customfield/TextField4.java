package com.scsb.db.customfield;

import java.util.Hashtable;

import com.vaadin.data.Property;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class TextField4 extends CustomField<Hashtable>{
	
	protected Hashtable<String,String> hashText  = new Hashtable<String,String>();
	protected CheckBox  ChkYN = new CheckBox();
	protected TextField Text1 = new TextField();
	protected TextField Text2 = new TextField();
	protected TextField Text3 = new TextField();
	protected TextField Text4 = new TextField();	
	VerticalLayout vlayout = new VerticalLayout();
	
	public TextField4() {
	
	    Property.ValueChangeListener listener = new Property.ValueChangeListener() {
	    	public void valueChange(com.vaadin.data.Property.ValueChangeEvent event) {
	        	setFieldVale();
	        }
	    };		
		this.setWidth("200px");
	    this.ChkYN.setValue(false);
	    this.ChkYN.setImmediate(true);
	    this.ChkYN.addValueChangeListener(listener);
	    
	    this.Text1.setId("TEXT1");
	    this.Text1.setInputPrompt("X1");
	    this.Text1.setWidth("30px");
	    this.Text1.addValueChangeListener(listener);
	    this.Text1.setImmediate(true);
		
	    this.Text2.setId("TEXT2");
	    this.Text2.setInputPrompt("X2");
	    this.Text2.setWidth("30px");
	    this.Text2.addValueChangeListener(listener);
	    this.Text2.setImmediate(true);
	    
	    this.Text3.setId("TEXT3");
	    this.Text3.setInputPrompt("Y1");
	    this.Text3.setWidth("30px");
	    this.Text3.addValueChangeListener(listener);
	    this.Text3.setImmediate(true);
	    
	    this.Text4.setId("TEXT4");
	    this.Text4.setInputPrompt("Y2");
	    this.Text4.setWidth("30px");
	    this.Text4.addValueChangeListener(listener);
	    this.Text4.setImmediate(true);	    
		
		
		vlayout.setSizeUndefined();

		HorizontalLayout hlayout = new HorizontalLayout();
		hlayout.setSpacing(true);
		hlayout.setSizeUndefined();
		hlayout.addComponent(this.ChkYN);
		hlayout.addComponent(this.Text1);
        hlayout.addComponent(this.Text2);
        hlayout.addComponent(this.Text3);
        hlayout.addComponent(this.Text4);
        vlayout.addComponent(hlayout);
        
        setFieldVale();
	}
    public void setFieldVale(){
    	hashText=new Hashtable<String,String>();
    	hashText.put("CHECK",this.ChkYN.getValue()+"");
    	hashText.put("TEXT1",this.Text1.getValue()+"");
    	hashText.put("TEXT2",this.Text2.getValue()+"");
    	hashText.put("TEXT3",this.Text3.getValue()+"");
    	hashText.put("TEXT4",this.Text4.getValue()+"");
    	setValue(hashText);     	
    }
    public void setDefaultValue(Hashtable<String, String> defHashTable){
    	if (defHashTable.get("CHECK") != null){
    		if (defHashTable.get("CHECK").equals("true")) this.ChkYN.setValue(true);
    		else  this.ChkYN.setValue(false);
    	}
    	if (defHashTable.get("TEXT1") != null)	this.Text1.setValue(defHashTable.get("TEXT1"));
    	if (defHashTable.get("TEXT2") != null)	this.Text2.setValue(defHashTable.get("TEXT2"));
    	if (defHashTable.get("TEXT3") != null)	this.Text3.setValue(defHashTable.get("TEXT3"));
    	if (defHashTable.get("TEXT4") != null)	this.Text4.setValue(defHashTable.get("TEXT4"));
    }    
	@Override
	public Class<Hashtable> getType() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	protected Component initContent() {		
		return vlayout;
	}     
}