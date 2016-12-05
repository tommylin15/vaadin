package com.scsb.vaadin.composite;

import java.util.Properties;

import com.scsb.vaadin.include.TitleLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Notification.Type;

/**
 * 屬性選單
 * @author 3471
 *
 */
public class ScsbProperty extends ScsbGlob {
	FormLayout formLayout = new FormLayout();
	Panel content = new Panel();
	VerticalLayout vLayout = new VerticalLayout();
	VerticalLayout dLayout = new VerticalLayout();
	
	ScsbToolbar		  toolBar		=new ScsbToolbar();
	TitleLayout title =new TitleLayout("");
	/**
	 * 屬性選單
	 */
	public ScsbProperty() {
		create(messageProps.getProperty("label_DataList","屬性設定"));
	}
	public ScsbProperty(String propertyTitle) {
		create(propertyTitle);
	}
	private void create(String propertyTitle){
		content.setWidth("100%");
		// screenHeight = _screenHeight - tag head
		content.setHeight((_screenHeight-40 )+"px");
		content.setContent(vLayout);
		
    	formLayout.setMargin(false);
    	formLayout.setSizeFull();
    	formLayout.addStyleName("light");		
    	setCaption(propertyTitle);
		
		toolBar.setVisible(true);
		
		Panel panel =new Panel();
		panel.setWidth("100%");
		// screenHeight = _screenHeight - tag head -toolBar -title -space
		panel.setHeight((_screenHeight-40 -40 -28 -26 )+"px");
		panel.setContent(dLayout);
		dLayout.addComponent(formLayout);
		
		vLayout.setImmediate(true);
		vLayout.addComponent(toolBar.getContent());
		vLayout.addComponent(title.vLayout);
		vLayout.addComponent(panel);
		//vLayout.addComponent(formLayout);
		vLayout.setSpacing(true);
	}
	
	public void setCaption(String caption){
		title.setLabel(caption);
	}
	
	 public void showMsgError(String errMsg){
			Notification.show("["+messageProps.getProperty("msg_title","作業訊息通知")+"]"
					,messageProps.getProperty("msg_error","作業失敗")+":"+errMsg
					,Type.TRAY_NOTIFICATION);
		 
	 }
	 public void showMsgSuccess(){
			Notification.show("["+messageProps.getProperty("msg_title","作業訊息通知")+"]"
					,messageProps.getProperty("msg_completed","作業完成")
					,Type.TRAY_NOTIFICATION);
	 }	
	
	public FormLayout getFormLayout(){
		return formLayout;
	}
	
	public Panel getContent(){
		return content;
	}	
	
    public ScsbToolbar getToolBar(){
    	return toolBar;
    }
    
    public VerticalLayout getLayout(){
    	return dLayout;
    }
    public void addComponent(Component c) {
    	dLayout.addComponent(c);
    	dLayout.setComponentAlignment(c, Alignment.TOP_CENTER);
    }    
    
}