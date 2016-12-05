package com.scsb.crpro;

import java.util.Hashtable;
import java.util.Iterator;

import javax.servlet.ServletContext;

import org.vaadin.teemu.jsoncontainer.JsonContainer;

import com.scsb.crpro.CrproLayout;
import com.scsb.db.bean.EtlCheckfile;
import com.scsb.etl.etl_checkFile_property;
import com.scsb.util.IO;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItem;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Notification.Type;

@SuppressWarnings("serial")
public class LoginForm extends CrproLayout {

	JsonContainer jsonData; 
	TextField userId =new TextField();
	PasswordField password =new PasswordField();
	
	public LoginForm() {
		create();
	}
	public void create(){
		this.removeAllComponents();
		this.setId("explorer");
    	ServletContext servletContext = VaadinServlet.getCurrent().getServletContext();
		String jsonString =IO.read(servletContext.getRealPath("/")+systemProps.getProperty("userJson"));
		 jsonData = JsonContainer.Factory.newInstance(jsonString);		
		
		VerticalLayout loginBox = new VerticalLayout();
		
    	Panel panel = new Panel("Wellcome..");
    	panel.setContent(loginBox);
    	panel.setWidth("500px");		
		
    	
    	userId.setCaption("使用者ID");
    	userId.setImmediate(true);
    	userId.setIcon(FontAwesome.USER); 
    	userId.setInputPrompt("請輸入"+userId.getCaption());
    	loginBox.addComponent(userId);
    	
    	password.setCaption("使用者密碼");
    	password.setImmediate(true);
    	password.setIcon(FontAwesome.KEY);
    	
    	password.setInputPrompt("請輸入"+password.getCaption());
    	loginBox.addComponent(password);
    	
    	HorizontalLayout group = new HorizontalLayout();
    	group.setCaption("　");
    	//group.setIcon(FontAwesome.SIGN_IN);
    	group.setSpacing(true);
    	
        Button loginBu = new Button("登入");
        loginBu.setStyleName("button");
    	loginBu.addClickListener(new ClickListener() {
    		    public void buttonClick(ClickEvent event) {
    		    	checkPwd();
    		    }
    		}); 
    	loginBu.setClickShortcut(KeyCode.ENTER);
    	this.addComponent(panel);
    	this.setComponentAlignment(panel, Alignment.TOP_CENTER);
    	this.addComponent(loginBu);
    	this.setComponentAlignment(loginBu, Alignment.TOP_CENTER);    	
    	this.setSpacing(false);
    	this.setMargin(new MarginInfo(false,false,false,false));    	
	}
	
	private void checkPwd(){
    	for(Iterator iter=jsonData.getItemIds().iterator() ;iter.hasNext();){
    		Item item=jsonData.getItem(iter.next());
    		String id=(String)item.getItemProperty("id").getValue();
    		String pwd=(String)item.getItemProperty("password").getValue();
    		
    		String haveCrpro=(String)item.getItemProperty("haveCrpro").getValue();
    		
    		if (userId.getValue().equals(id) && password.getValue().equals(pwd)){
    			Hashtable<String,String> hashUserAction =new Hashtable<String,String>();
    			for (Iterator propIter =item.getItemPropertyIds().iterator();propIter.hasNext();){
    				String propKey =(String)propIter.next();
    				String propValue =(String)item.getItemProperty(propKey).getValue();
    				hashUserAction.put(propKey, propValue);
    			}
	    		session.setAttribute("User",userId.getValue());
				session.setAttribute("_hashUserAction",hashUserAction);
		    	UI.getCurrent().getPage().reload();        					
				return;
    		}
    	}
    	session.setAttribute("User","");
    	session.setAttribute("_hashUserAction",null);
		Notification.show("["+"作業訊息通知"+"]"
				,"帳號密碼輸入有誤"
				,Type.WARNING_MESSAGE);
		password.setValue("");		
	}
}
