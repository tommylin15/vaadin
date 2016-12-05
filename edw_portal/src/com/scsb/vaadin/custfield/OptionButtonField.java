package com.scsb.vaadin.custfield;

import java.util.Collection;
import java.util.HashSet;

import com.vaadin.data.Property;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

@SuppressWarnings("serial")
public class OptionButtonField extends CustomField<Collection<String>>{
	protected HorizontalLayout hlayout = new HorizontalLayout();
	protected Label label = new Label();
	protected HorizontalLayout hgroup = new HorizontalLayout();
	
	public OptionButtonField(Collection<String> valueList ,OptionGroup itemList) {
		hlayout.setWidth("100%");
		label.setHeight("100%");
		label.setContentMode(ContentMode.HTML);
		hlayout.addComponent(label);
		hlayout.addComponent(hgroup);
		hgroup.setImmediate(true);
		for(java.util.Iterator<String> iter=valueList.iterator();iter.hasNext();){
			String id =(String)iter.next();
			Button button =new Button();
			button.setId(id);
			button.setCaption(itemList.getItemCaption(id));
			//button.setStyleName(StyleNames.BUTTON_BLUE);
			button.setImmediate(true);
			button.setData("");
			button.setIcon(new ThemeResource("./images/stop.png"));
			button.addClickListener(
    		    new ClickListener() {
    				@Override
    				public void buttonClick(ClickEvent event) {
    					if (event.getButton().getData().equals("")){
	    					event.getButton().setIcon(new ThemeResource("./images/check.png"));
	    					event.getButton().setData(event.getButton().getId());
    					}else{
	    					event.getButton().setIcon(new ThemeResource("./images/stop.png"));
	    					event.getButton().setData("");	    						
    					}
    					setValue(getCollValues());
    				}
        		}
	        );			
			addButton(button);
		}		
	}
	
	public void setCaption(String caption){
		label.setIcon(FontAwesome.PENCIL_SQUARE);
		label.setCaption(caption);
	}
	
	public void setValue(Collection fieldValue){
    	for(java.util.Iterator<Component> iterButton=hgroup.getComponentIterator();iterButton.hasNext();){
    		Button comp =(Button)iterButton.next();
    		comp.setIcon(new ThemeResource("./images/stop.png"));
    		comp.setData("");
        	for(java.util.Iterator<String> iter=fieldValue.iterator();iter.hasNext();){
            	String id =(String)iter.next();
        		if (comp.getId().equals(id)){
        			comp.setIcon(new ThemeResource("./images/check.png"));
        			comp.setData(comp.getId());
        		}
        	}    		
    	}		
		setValue(fieldValue, false);
	}
	
	private Collection<String> getCollValues(){
		Collection<String> hashSet =new HashSet<String>();
    	for(java.util.Iterator<Component> iter=hgroup.getComponentIterator();iter.hasNext();){
    		Button comp =(Button)iter.next();
    		if (!comp.getData().equals("")) hashSet.add(comp.getId());
    	}
		return hashSet;
	}	
	
	public Collection<String> getItemIds(){
		Collection<String> hashSet =new HashSet<String>();
    	for(java.util.Iterator<Component> iter=hgroup.getComponentIterator();iter.hasNext();){
    		Button comp =(Button)iter.next();
    		hashSet.add(comp.getId());
    	}
		return hashSet;
	}
	
	@Override
	public Class<? extends Collection<String>> getType() {
		return (Class<? extends Collection<String>>) Collection.class;
	}     

	public void addButton(Button button){
		hgroup.addComponent(button);
	}
	/**
     * 由Collection取得值時
     */
    @Override
    public void setPropertyDataSource(Property newDataSource) {
    	Collection<String> hashValue =(Collection<String>)newDataSource.getValue();
    	for(java.util.Iterator<Component> iterButton=hgroup.getComponentIterator();iterButton.hasNext();){
    		Button comp =(Button)iterButton.next();
    		comp.setIcon(new ThemeResource("./images/stop.png"));
    		comp.setData("");
        	for(java.util.Iterator<String> iter=hashValue.iterator();iter.hasNext();){
            	String id =(String)iter.next();
        		if (comp.getId().equals(id)){
        			comp.setIcon(new ThemeResource("./images/check.png"));
        			comp.setData(comp.getId());
        		}
        	}    		
    	}
        super.setPropertyDataSource(newDataSource);       
    }
    /**
     * 傳值回Collection
     */
    @Override
    public Property getPropertyDataSource(){
    	Collection<String> hashSet=new HashSet<String>();
    	for(java.util.Iterator<Component> iter=hgroup.getComponentIterator();iter.hasNext();){
    		Button comp =(Button)iter.next();
    		if (!comp.getData().equals("")) hashSet.add(comp.getData()+"");
    	}
    	Property<Collection<String>> newDataSource = super.getPropertyDataSource();
    	newDataSource.setValue(hashSet);
        return newDataSource;
    }	
	
	@Override
	protected Component initContent() {
		return hlayout;
	}

}