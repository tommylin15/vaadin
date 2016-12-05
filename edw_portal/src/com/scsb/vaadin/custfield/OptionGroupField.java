package com.scsb.vaadin.custfield;

import java.util.Collection;

import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.OptionGroup;

@SuppressWarnings("serial")
public class OptionGroupField extends CustomField<Collection>{
	protected OptionGroup group = new OptionGroup();
	protected HorizontalLayout hlayout = new HorizontalLayout();
	protected Label label = new Label();
	
	public OptionGroupField(Collection valueList ,OptionGroup itemList) {
		group.setMultiSelect(true);
		group.addStyleName("horizontal");
		group.setImmediate(true);
		
		hlayout.setWidth("100%");
		label.setHeight("100%");
		label.setContentMode(ContentMode.HTML);
		hlayout.addComponent(label);
		hlayout.addComponent(group);
		
		group.addValueChangeListener(
    		    new ValueChangeListener() {
					@Override
					public void valueChange(
							com.vaadin.data.Property.ValueChangeEvent event) {
						setValue((Collection) group.getValue());
						
					}
        		}
	        );			
		
		for(java.util.Iterator iter=valueList.iterator();iter.hasNext();){
			String id =(String)iter.next();
			group.addItem(id);
			group.setItemCaption(id, itemList.getItemCaption(id));
		}		
	}
	
	public void setCaption(String caption){
		label.setIcon(FontAwesome.PENCIL_SQUARE);
		label.setCaption(caption);
	}
	
	public void setValue(Collection fieldValue){
		group.setValue(fieldValue);
		setValue(fieldValue, false);
	}	
	
	public Collection getValue(){
		return (Collection) group.getValue();
	}
	
	@Override
	public Class<Collection> getType() {
		return Collection.class;
	}     
	
	public OptionGroup getOptionGroup(){
		return group;
	}

	@Override
	protected Component initContent() {
		return hlayout;
	}

}