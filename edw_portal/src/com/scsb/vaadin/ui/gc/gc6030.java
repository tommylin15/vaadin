package com.scsb.vaadin.ui.gc;

import com.scsb.util.UserAction;
import com.scsb.vaadin.composite.ScsbGlobView;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Notification.Type;

@SuppressWarnings("serial")
public class gc6030 extends ScsbGlobView {

    public gc6030() {
		String className =this.getClass().getName().substring(this.getClass().getName().lastIndexOf(".")+1);
		if (className.indexOf("_") > -1) className=className.substring(0,className.indexOf("_"));
		this.lang =session.getAttribute("Language")+"";
		loadI18N(lang,className);
		
    	FormLayout loginBox = new FormLayout();
    	//panel.setContent(loginBox);
    	
    	final TextField userId =new TextField();
    	userId.setCaption(i18nProps.getProperty("UserId","使用者帳號"));
    	userId.setImmediate(true);
    	userId.setIcon(FontAwesome.USER);
    	userId.setValue(users.getUserid());
    	userId.setReadOnly(true);
    	loginBox.addComponent(userId);
    	
    	final PasswordField password =new PasswordField();
    	password.setCaption(i18nProps.getProperty("UserPassWord","使用者密碼"));
    	password.setImmediate(true);
    	password.setIcon(FontAwesome.KEY);
    	loginBox.addComponent(password);

    	final PasswordField newpwd =new PasswordField();
    	newpwd.setCaption(i18nProps.getProperty("NewPassWord","使用者新密碼"));
    	newpwd.setImmediate(true);
    	
    	newpwd.setIcon(FontAwesome.KEY);
    	loginBox.addComponent(newpwd);

    	final PasswordField newpwd2 =new PasswordField();
    	newpwd2.setCaption(i18nProps.getProperty("NewPassWordAgain","新密碼再次確認"));
    	newpwd2.setImmediate(true);
    	newpwd2.setIcon(FontAwesome.KEY);
    	loginBox.addComponent(newpwd2);
    	
    	HorizontalLayout group = new HorizontalLayout();
    	group.setCaption("　");
    	group.setIcon(FontAwesome.SIGN_IN);
    	group.setSpacing(true);
    	
        Button loginBu = new Button(messageProps.getProperty("Button_saveBu","存檔"));
        loginBu.setStyleName("button");
    	loginBu.addClickListener(
    		    new ClickListener() {
    		    @Override
    		    public void buttonClick(ClickEvent event) {
    		    	String msg =UserAction.checkLoginDB( users.getUserid(), password.getValue());
    		    	//首次登入者,變更密碼無需驗舊密碼
    		    	if (msg.length() > 0){
    		    		if (msg.substring(0,1).equals("F")) msg="";
    		    	}
    		    	if (msg.equals("")){
    		    		if (!newpwd.getValue().equals(newpwd2.getValue())){
    		        		Notification.show("["+messageProps.getProperty("msg_title","作業訊息通知")+"]"
    		    					,messageProps.getProperty("msg_error","新密碼輸入錯誤!!")+":"+i18nProps.getProperty("msg"+msg.substring(0,1),msg)
    		    					,Type.WARNING_MESSAGE);    		    			
    		    		}else{
    		    			msg=UserAction.updatePassWord(users.getUserid(),newpwd.getValue());
    		    			if (!msg.equals("")){
        		        		Notification.show("["+messageProps.getProperty("msg_title","作業訊息通知")+"]"
        		    					,messageProps.getProperty("msg_error","密碼修改失敗!")+":"+i18nProps.getProperty("msg"+msg.substring(0,1),msg)
        		    					,Type.WARNING_MESSAGE);    		    			
    		    			}
    		    			Notification.show("["+messageProps.getProperty("msg_title","作業訊息通知")+"]"
    		    					,messageProps.getProperty("msg_completed","作業完成")
    		    					,Type.TRAY_NOTIFICATION);
    		    			//清空驗證,重新登入
    		    			session.removeAttribute("PassOK");
    		    			UI.getCurrent().getPage().reload();
    		    		}
    		    	}else{
		        		Notification.show("["+messageProps.getProperty("msg_title","作業訊息通知")+"]"
		    					,messageProps.getProperty("msg_error","舊密碼輸入錯誤!!")+":"+i18nProps.getProperty("msg"+msg.substring(0,1),msg)
		    					,Type.WARNING_MESSAGE);     		    		
    		    	}
    		    }
    		});        
        group.addComponent(loginBu);
        
        Button cancelBu = new Button(messageProps.getProperty("msg_cancel","取消"));
        cancelBu.setStyleName("buttonX");
        group.addComponent(cancelBu);
        
    	loginBox.addComponent(group);    	
    	
    	Panel panel = new Panel(i18nProps.getProperty("ReSetPassWord","使用者變更密碼作業"));
    	panel.setContent(loginBox);
    	panel.setSizeFull();

    	getView().setContent(panel);
    	
    }
   
}



