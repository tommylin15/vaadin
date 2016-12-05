package com.scsb.vaadin.ui;

import java.io.File;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Properties;

import com.scsb.db.bean.SessionDb;
import com.scsb.db.bean.Usersa;
import com.scsb.db.service.SessionDbService;
import com.scsb.domain.HashI18N;
import com.scsb.domain.HashSystem;
import com.scsb.domain.HashTrans;
import com.scsb.util.DateUtil;
import com.scsb.util.StrUtil;
import com.scsb.util.UserAction;
import com.scsb.vaadin.composite.ProgressWindow;
import com.scsb.vaadin.composite.ScsbGlob;
import com.scsb.vaadin.ui.popover.HelpPopover;
import com.scsb.vaadin.ui.popover.LangPopover;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.WrappedSession;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.ProgressIndicator;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class LoginView {
	
	protected static HashSystem    hashSystem   = HashSystem.getInstance();
	protected static Properties   systemProps     =hashSystem.getProperties();
	protected static HashI18N    hashI18N   = HashI18N.getInstance();
	protected static Properties   i18nProps   = new Properties();
	protected static Properties messageProps   = new Properties();
	
	protected WrappedSession session =UI.getCurrent().getSession().getSession();

    private static LinkedHashMap<String, String> themeVariants = new LinkedHashMap<String, String>();
    static {
        themeVariants.put("scsb_theme", "Default");
        themeVariants.put("scsb_theme-facebook", "Facebook");
        themeVariants.put("scsb_theme-flat", "Flat");
        themeVariants.put("scsb_theme-light", "Light");
        themeVariants.put("scsb_theme-metro", "Metro");
    }	
	
	protected TextField userId =new TextField();
	protected PasswordField password =new PasswordField();
	
	public VerticalLayout vLayout =new VerticalLayout();
	
    public LoginView() {
    	String lang =session.getAttribute("Language")+"";		
    	i18nProps = hashI18N.getProperties(lang,"LoginView");
		if (i18nProps == null) i18nProps=new Properties();
		messageProps = hashI18N.getProperties(lang,"message");
		if (messageProps == null) messageProps=new Properties();    	
    	
		AbsoluteLayout topLayout= new AbsoluteLayout();
    	//多語系選單
    	Button langBu =new Button("");        	
    	langBu.setIcon(new ThemeResource("./images/language32.png"));
    	langBu.setDescription("多語系選單");
    	langBu.setId("lang");
    	langBu.setWidth("32px");
    	langBu.setStyleName(ValoTheme.BUTTON_LINK);
    	langBu.addStyleName("icon-only");
    	langBu.addClickListener(
		    new ClickListener() {
				@Override
				public void buttonClick(ClickEvent event) {
					LangPopover scsbPopover = new LangPopover();
    		    	//彈出視窗的位置
					scsbPopover.popover.showRelativeTo(event.getButton());        		    							
				}
    		}
    	);
    	//help選單
    	Button helpBu =new Button("");        	
    	helpBu.setIcon(new ThemeResource("./images/help32.png"));
    	helpBu.setId("help");
    	helpBu.setDescription("線上說明");    	
    	helpBu.setWidth("32px");
    	helpBu.setStyleName(ValoTheme.BUTTON_LINK);
    	helpBu.addStyleName("icon-only");
    	
    	helpBu.addClickListener(
		    new ClickListener() {
				@Override
				public void buttonClick(ClickEvent event) {
					HelpPopover scsbPopover = new HelpPopover("LoginView");
					UI.getCurrent().addWindow(scsbPopover.popover);					
				}
    		}
    	);  
    	
    	//theme
        final NativeSelect ns = new NativeSelect();
        ns.setNullSelectionAllowed(false);
        ns.setId("themeSelect");
        ns.addContainerProperty("caption", String.class, "");
        ns.setItemCaptionPropertyId("caption");
        for (final String identifier : themeVariants.keySet()) {
            ns.addItem(identifier).getItemProperty("caption")
                    .setValue(themeVariants.get(identifier));
        }
        ns.setValue("scsb_theme");
        ns.addValueChangeListener(new ValueChangeListener() {
            public void valueChange(final ValueChangeEvent event) {
            	session.setAttribute("myTheme", (String) ns.getValue());
            	UI.getCurrent().setTheme((String) ns.getValue());
            }
        });
    	
    	topLayout.setWidth("500px");
    	topLayout.setHeight("60px");    	
    	topLayout.addComponent(langBu,"right: 160px; top: 4px;");
    	topLayout.addComponent(helpBu,"right: 125px; top: 4px;");
    	topLayout.addComponent(ns,"right: 10px; top: 4px;");
    	
		FormLayout loginBox = new FormLayout();
		loginBox.addStyleName("light");
		loginBox.setWidth("500px");
		
        Label section = new Label(i18nProps.getProperty("Wellcome","歡迎光臨 SCSB EDW Portal  ..."));
        section.addStyleName("h4");
        section.addStyleName("colored");        
        loginBox.addComponent(section);    	
        
    	userId.setCaption(i18nProps.getProperty("UserId","使用者ID"));
    	userId.setImmediate(true);
    	userId.setIcon(FontAwesome.USER); 
    	userId.setInputPrompt(messageProps.getProperty("PleaseKeyin","請輸入")+userId.getCaption());
    	loginBox.addComponent(userId);
    	
    	password.setCaption(i18nProps.getProperty("UserPassword","使用者密碼"));
    	password.setImmediate(true);
    	password.setIcon(FontAwesome.KEY);
    	password.setInputPrompt(messageProps.getProperty("PleaseKeyin","請輸入")+password.getCaption());
    	loginBox.addComponent(password);

    	HorizontalLayout group = new HorizontalLayout();
    	group.setCaption("　");
    	group.setSpacing(true);
    	group.setHeight("60px");
    	
        Button loginBu = new Button(i18nProps.getProperty("Login","登入"));
        loginBu.addStyleName(ValoTheme.BUTTON_PRIMARY);
    	loginBu.addClickListener(
    		    new ClickListener() {
    		    @Override
    		    public void buttonClick(ClickEvent event) {
    		    	userId.setValue(StrUtil.lpad(userId.getValue(),"0",7));
    		    	checklogin(userId.getValue() ,password.getValue());
    		    }
    		});    
    	loginBu.setClickShortcut(KeyCode.ENTER);    	
        group.addComponent(loginBu);
        group.setComponentAlignment(loginBu, Alignment.MIDDLE_RIGHT);
        
        Button cancelBu = new Button(i18nProps.getProperty("Cancel","取消"));
        cancelBu.addStyleName(ValoTheme.BUTTON_DANGER);
        group.addComponent(cancelBu);
        group.setComponentAlignment(cancelBu, Alignment.MIDDLE_RIGHT);
        
    	loginBox.addComponent(group);    	
    	loginBox.setComponentAlignment(group, Alignment.BOTTOM_RIGHT);
    	
    	Resource logo = new ThemeResource("./images/SCSB100Logo.jpg");
    	Image image_logo = new Image(null, logo);
	    
	    vLayout.addComponent(topLayout);
	    vLayout.setComponentAlignment(topLayout, Alignment.TOP_CENTER);
	    vLayout.addComponent(loginBox);
	    vLayout.setComponentAlignment(loginBox, Alignment.TOP_CENTER);	    
	    vLayout.addComponent(image_logo);
	    vLayout.setComponentAlignment(image_logo, Alignment.TOP_CENTER);
    	vLayout.setExpandRatio(image_logo, 1.0f);
    	vLayout.setSpacing(false);
    	vLayout.setMargin(new MarginInfo(false,false,false,false));    	
    }
    
    public void checklogin(String userid ,String userPwd){
    	//改成接LDAP
    	//String msg =UserAction.checkLoginDB(userid , userPwd);
    	String ldapWebService=systemProps.getProperty("_LdapWebService");
    	String msg =UserAction.checkLoginLdap(ldapWebService ,userid , userPwd);
    	if (msg.equals("")){
			session.setAttribute("PassOK","Login");
			session.setAttribute("UsersBean", UserAction.getUsersBean(userid));
			Hashtable<String ,String> userAction =UserAction.getUsersAction(userid);
			session.setAttribute("UserAction",userAction );
			Hashtable<String ,String> userTrans =UserAction.getUsersTrans(userAction);
			session.setAttribute("UserTrans",userTrans );
			//寫入session db
			SessionDbService sessionDBSrv =new SessionDbService();
			SessionDb sessionBean =new SessionDb();
			sessionBean.setSessionId(session.getId());
			sessionBean.setSessionUserid(userid);
			sessionBean.setSessionIp(UI.getCurrent().getPage().getWebBrowser().getAddress());
			//配合sas intrnet 必須要先刪除舊的session_id.xml檔
			//String xmlPath=systemProps.getProperty("_SasXmlPath")+"/"+session.getId()+".xml";		
			//File xmlFile=new File(xmlPath);
			//if (xmlFile.exists()){
			//	xmlFile.delete();
			//}
			//暫時不存xml資料
			//sessionBean.setSessionXml(ui.getSessionXML());
			sessionDBSrv.updateData(sessionBean);
			
			//如果MUSTCHANGE="Y" 登入必須變更密碼強制進入變更密碼
			Usersa usersa=UserAction.getUsersaBean(userid);
			if (usersa.getMustchange().equals("Y")){
				session.setAttribute("PassOK","MustChange");
			}else{
				usersa.setUpdateDatetime(DateUtil.getDTS());
				usersa.setUpdateUser(usersa.getUserid());
				usersa.setRecentlogintime(DateUtil.getDTS());
				UserAction.clearErrorTimes(usersa);
			}				
			UI.getCurrent().getPage().reload();
		//首次登入,必須變更密碼強制進入變更密碼
    	}else if(msg.substring(0,1).equals("F")){
    		session.setAttribute("PassOK","MustChange");
    		session.setAttribute("UsersBean", UserAction.getUsersBean(userid));
    		UI.getCurrent().getPage().reload();
    	}else{
    		Notification.show("["+messageProps.getProperty("msg_title","作業訊息通知")+"]"
					,messageProps.getProperty("msg_error","登入失敗")+":"+i18nProps.getProperty("msg"+msg.substring(0,1),msg)
					,Type.WARNING_MESSAGE);
    	}    	
    }
   
}



