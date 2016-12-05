package com.scsb.vaadin.composite;

import com.scsb.vaadin.composite.manager.ScsbView;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Notification.Type;
/**
 * SCSB-SAS公用畫面檔
 * @author 3471
 *
 */
public class ScsbGlobView extends ScsbGlob  {
	ScsbView view =new ScsbView();
	VerticalLayout content = new VerticalLayout();

	public ScsbGlobView() {

		String className =this.getClass().getName().substring(this.getClass().getName().lastIndexOf(".")+1);
		if (className.indexOf("_") > -1) className=className.substring(0,className.indexOf("_"));
		this.className =className;
		loadI18N(lang,className);

		//取得程式安控
		if (this.userAction != null)	this.userActionCode =this.userAction.get(className);
		if (this.userActionCode == null) this.userActionCode="";		
		
		content.setMargin(new MarginInfo(false, true, false, true));
		content.setSpacing(true);
		content.setImmediate(true);
	
		Panel panel =new Panel();
		panel.setWidth("100%");
		panel.setHeight(_screenHeight+"px");		
		panel.setContent(content);
		
		view.setSizeFull();
		view.setContent(panel);
	}
	 
	public ScsbView getView(){
		return view;
	}	 
	
	public VerticalLayout getContent(){
		return content;
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
		
}
