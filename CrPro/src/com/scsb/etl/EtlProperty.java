package com.scsb.etl;

import com.scsb.crpro.CrproLayout;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
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
public class EtlProperty extends CrproLayout {
	FormLayout formLayout = new FormLayout();
	Panel content = new Panel();
	VerticalLayout vLayout = new VerticalLayout();
	VerticalLayout dLayout = new VerticalLayout();
	public Button saveBu =new Button("存檔"); 
	/**
	 * 屬性選單
	 */
	public EtlProperty() {
		content.setContent(vLayout);
		
    	formLayout.setMargin(false);
    	formLayout.setSizeFull();
    	formLayout.addStyleName("light");		
    	setCaption("屬性設定");
		
		Panel panel =new Panel();
		panel.setWidth("100%");
		// screenHeight = _screenHeight - tag head -toolBar -title -space
		panel.setHeight((_screenHeight-40 -34 -30 -26 )+"px");
		panel.setContent(dLayout);
		dLayout.addComponent(formLayout);
		
		vLayout.setImmediate(true);
		
		saveBu.setDisableOnClick(true);
		saveBu.setVisible(false);
		saveBu.addStyleName("friendly");    	
    	saveBu.addStyleName("icon-only");
    	saveBu.setIcon(FontAwesome.SAVE);
    	
		vLayout.addComponent(saveBu);
		//vLayout.addComponent(title.vLayout);
		vLayout.addComponent(panel);
		vLayout.setSpacing(true);
	}
	
	public void setCaption(String caption){
		//title.setLabel(caption);
	}
	
	 public void showMsgError(String errMsg){
			Notification.show("[作業訊息通知]"
					,"作業失敗"+":"+errMsg
					,Type.TRAY_NOTIFICATION);
		 
	 }
	 public void showMsgSuccess(){
			Notification.show("["+"作業訊息通知"+"]"
					,"作業完成"
					,Type.TRAY_NOTIFICATION);
	 }	
	
	public FormLayout getFormLayout(){
		return formLayout;
	}
	
	public Panel getContent(){
		return content;
	}	
/*	
    public ScsbToolbar getToolBar(){
    	return toolBar;
    }
*/    
    public void addComponent(Component c) {
    	dLayout.addComponent(c);
    }    
    
}