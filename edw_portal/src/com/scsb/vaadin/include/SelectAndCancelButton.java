package com.scsb.vaadin.include;

import java.util.HashSet;
import java.util.Properties;

import com.scsb.vaadin.composite.ScsbGlob;
import com.scsb.vaadin.custfield.OptionButtonField;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

@SuppressWarnings("serial")
/**
 * 取得 公用的群組元件
 */
public class SelectAndCancelButton  extends ScsbGlob {
	public HorizontalLayout hButtonGroup =new HorizontalLayout();
	public SelectAndCancelButton(){	
	}
	
	/**
	 * 設定作用的OptionGroup
	 * @param optiongroup
	 */
	public void setOptionGroup(final HashSet<OptionGroup> hashSet){
	
        Button selButton = new Button(messageProps.getProperty("Button_selectall","全選"));
        selButton.setId("Select");
        selButton.setIcon(new ThemeResource("./images/check.png"));
        //selButton.setStyleName("button");
        selButton.addClickListener(new ClickListener(){
			@Override
			public void buttonClick(ClickEvent event) {
				for (java.util.Iterator<OptionGroup> iter=hashSet.iterator();iter.hasNext();){
					OptionGroup optionGroup =iter.next();
					optionGroup.setValue(optionGroup.getItemIds());
				}
			}});
        hButtonGroup.addComponent(selButton);
        
        Button delButton = new Button(messageProps.getProperty("Button_cancelall","全取消"));
        delButton.setId("Cancel");
        delButton.setIcon(new ThemeResource("./images/stop.png"));
        //delButton.setStyleName("buttonX");
        delButton.addClickListener(new ClickListener(){
			@Override
			public void buttonClick(ClickEvent event) {
				for (java.util.Iterator<OptionGroup> iter=hashSet.iterator();iter.hasNext();){
					OptionGroup optionGroup =iter.next();
					optionGroup.setValue(new HashSet());
				}				
			}});
        hButtonGroup.addComponent(delButton);			
	}
	/**
	 * 設定作用的OptionGroup
	 * @param optiongroup
	 */	
	public void setOptionGroup(OptionGroup optiongroup){
		final OptionGroup srcOptionGroup=optiongroup;
		this.lang =session.getAttribute("Language")+"";
		String className =this.getClass().getName().substring(this.getClass().getName().lastIndexOf(".")+1);
		loadI18N(lang ,className);
		
        Button selButton = new Button(messageProps.getProperty("Button_selectall","全選"));
        selButton.setId("Select");
        selButton.setIcon(new ThemeResource("./images/check.png"));
        //selButton.setStyleName("button");
        selButton.addClickListener(new ClickListener(){
			@Override
			public void buttonClick(ClickEvent event) {
				srcOptionGroup.setValue(srcOptionGroup.getItemIds());
			}});
        hButtonGroup.addComponent(selButton);
        
        Button delButton = new Button(messageProps.getProperty("Button_cancelall","全取消"));
        delButton.setId("Cancel");
        delButton.setIcon(new ThemeResource("./images/stop.png"));
        //delButton.setStyleName("buttonX");
        delButton.addClickListener(new ClickListener(){
			@Override
			public void buttonClick(ClickEvent event) {
				srcOptionGroup.setValue(new HashSet());
			}});
        hButtonGroup.addComponent(delButton);		
	}
	/**
	 * 設定作用的OptionButtonField
	 * @param OptionButtonField
	 */
	public void setOptionButtonField(OptionButtonField optiongroup){
		final OptionButtonField srcOptionGroup=optiongroup;
		this.lang =session.getAttribute("Language")+"";
		String className =this.getClass().getName().substring(this.getClass().getName().lastIndexOf(".")+1);
		loadI18N(lang ,className);
		
        Button selButton = new Button(messageProps.getProperty("Button_selectall","全選"));
        selButton.setId("Select");
        selButton.setIcon(new ThemeResource("./images/check.png"));
        //selButton.setStyleName(StyleNames.BUTTON_BLUE);
        selButton.addClickListener(new ClickListener(){
			@Override
			public void buttonClick(ClickEvent event) {
				srcOptionGroup.setValue(srcOptionGroup.getItemIds());
			}});
        hButtonGroup.addComponent(selButton);
        
        Button delButton = new Button(messageProps.getProperty("Button_cancelall","取消"));
        delButton.setId("Cancel");
        delButton.setIcon(new ThemeResource("./images/stop.png"));
        //delButton.setStyleName(StyleNames.BUTTON_RED);
        delButton.addClickListener(new ClickListener(){
			@Override
			public void buttonClick(ClickEvent event) {
				srcOptionGroup.setValue(new HashSet());
			}});
        hButtonGroup.addComponent(delButton);		
	}	
}