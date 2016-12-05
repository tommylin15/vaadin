package com.scsb.vaadin.include;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import org.apache.commons.math3.util.MultidimensionalCounter.Iterator;

import com.scsb.vaadin.composite.ScsbGlob;
import com.scsb.vaadin.custfield.OptionButtonField;
import com.vaadin.data.Container.ItemSetChangeEvent;
import com.vaadin.data.Container.ItemSetChangeListener;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
/**
 * 取得 公用的雙下拉元件
 */
public class TwoSelect  extends ScsbGlob {
	public HorizontalLayout hSelectGroup =new HorizontalLayout();
	
	private IndexedContainer masterContainer =new IndexedContainer();
	private IndexedContainer detailContainer =new IndexedContainer();
	private IndexedContainer container =new IndexedContainer();
	
	private NativeSelect mSelect =new NativeSelect();
	private NativeSelect dSelect =new NativeSelect();
	
	private String masterKey ="";
	private String masterCaption ="";
	private String detailKey ="";
	private String detailCaption ="";
	
	public TwoSelect(IndexedContainer container){	
		this.container =container;
	}
	
	public void setMasterContainer(String itemValue ,String itemCaption ){
		setMasterContainer(itemValue ,itemCaption , container);
	}
	public void setMasterContainer(String itemValue ,String itemCaption , IndexedContainer xContainer){
		masterContainer.removeAllItems();
		masterContainer.removeContainerProperty(itemValue);
		masterContainer.removeContainerProperty(itemCaption);
		masterContainer.addContainerProperty(itemValue, String.class, null);
		masterContainer.addContainerProperty(itemCaption, String.class, null);
		
		masterCaption=itemCaption;
		masterKey =itemValue;		
		
        for (java.util.Iterator iter=xContainer.getItemIds().iterator();iter.hasNext();) {
        	Item item =xContainer.getItem(iter.next());
        	String value =item.getItemProperty(itemValue).getValue().toString();
        	String caption =item.getItemProperty(itemCaption).getValue().toString();
			if (masterContainer.getItem(value) == null){
	        	Item mItem =masterContainer.addItem(value);
	        	mItem.getItemProperty(itemValue).setValue(value);
	        	mItem.getItemProperty(itemCaption).setValue(value+"."+caption);
			}
        }		
	}	
	
	public void setDetailContainer(String nowKey ,String itemValue ,String itemCaption){
		detailContainer.removeAllItems();
		detailContainer.removeContainerProperty(itemValue);
		detailContainer.removeContainerProperty(itemCaption);
		detailContainer.addContainerProperty(itemValue, String.class, null);
		detailContainer.addContainerProperty(itemCaption, String.class, null);
		detailContainer.sort(new Object[]{itemValue},new boolean[]{true});
		detailCaption=itemCaption;
		detailKey =itemValue;		
		
        for (java.util.Iterator iter=container.getItemIds().iterator();iter.hasNext();) {
        	Item item =container.getItem(iter.next());
        	String value =item.getItemProperty(itemValue).getValue().toString();
        	String caption =item.getItemProperty(itemCaption).getValue().toString();
        	if (item.getItemProperty(masterKey).getValue().toString().equals(nowKey)){
				if (detailContainer.getItem(value) == null){					
			        	Item dItem =detailContainer.addItem(value);
			        	dItem.getItemProperty(itemValue).setValue(value);
			        	dItem.getItemProperty(itemCaption).setValue(value+"."+caption);
				}
        	}
        }		
	}	

	public void setSelect(String masterName  ,String detailName){
		
		HorizontalLayout hLayout=new HorizontalLayout();
		
		mSelect.setCaption(masterName);
		mSelect.setImmediate(true);
		mSelect.setNullSelectionAllowed(false);	
		mSelect.setContainerDataSource(masterContainer);
		mSelect.setItemCaptionPropertyId(masterCaption);
		mSelect.addValueChangeListener(onMasterSelectChange);
		hLayout.addComponent(mSelect);
		
		dSelect.setCaption(masterName);
		dSelect.setImmediate(true);
		dSelect.setNullSelectionAllowed(false);	
		dSelect.setContainerDataSource(detailContainer);
		dSelect.setItemCaptionPropertyId(detailCaption);
		hLayout.addComponent(dSelect);
		
		hSelectGroup.addComponent(hLayout);
	}
	
	public void setDefaultMaster(Object defMasterValue){
		if (masterContainer.size()>0){
			if (mSelect.getItemIds().contains(defMasterValue)){
				mSelect.setValue(defMasterValue);
			}else{
				mSelect.setValue(masterContainer.getIdByIndex(0));
			}
		}
	}
	
	/**
	 * on MasterSelect Value Change
	 */
	ValueChangeListener onMasterSelectChange = new ValueChangeListener() {
		@Override
		public void valueChange(ValueChangeEvent event) {
			setDetailContainer(event.getProperty().getValue().toString(), detailKey ,detailCaption );
			if (detailContainer.size()>0){
				dSelect.select(detailContainer.getIdByIndex(0));
			}
		}		
	};	
	
	public NativeSelect getMasterSelect(){
		return mSelect;
	}	

	public NativeSelect getDetailSelect(){
		return dSelect;
	}	
	
	
}