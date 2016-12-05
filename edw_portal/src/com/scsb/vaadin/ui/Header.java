package com.scsb.vaadin.ui;


import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;

import com.scsb.db.bean.SessionDb;
import com.scsb.db.bean.Users;
import com.scsb.db.service.SessionDbService;
import com.scsb.vaadin.composite.ScsbGlob;
import com.scsb.vaadin.composite.manager.ScsbManager;
import com.scsb.vaadin.ui.popover.HelpPopover;
import com.scsb.vaadin.ui.popover.LangPopover;
import com.scsb.vaadin.ui.popover.MenuPopover;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.BaseTheme;
import com.vaadin.ui.themes.ValoTheme;

public class Header extends ScsbGlob{

	private Users users =new Users();
	public VerticalLayout vLayout =new VerticalLayout();
	protected ScsbManager manager ;
	
	AbsoluteLayout mainTop = new AbsoluteLayout();
	Button loginSmartQueryBu =new Button(""); 	
    @SuppressWarnings("serial")
	public Header() {
    	//不正常簽入
    	if (session.getAttribute("UsersBean") == null){
    		session.invalidate();
    		UI.getCurrent().getPage().reload();
    	}else{
    		this.users =(Users)session.getAttribute("UsersBean");
    	}
    	this.systemProps = hashSystem.getProperties();
    	
    	//簽入人員
    	Label labelUser =new Label("User:"+this.users.getUserName());
    	labelUser.setWidth("300px");
    	Label labelIP   =new Label("Addr:"+UI.getCurrent().getPage().getWebBrowser().getAddress());
    	labelIP.setWidth("300px");

    	//多語系選單
    	Button langBu =new Button("");        	
    	langBu.setIcon(new ThemeResource("./images/lang.png"));
    	langBu.setId("lang");
    	langBu.setWidth("32px");
    	langBu.setDescription(i18nProps.getProperty("lang","多語系選單"));
    	langBu.addStyleName(ValoTheme.BUTTON_LINK);
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
    	//導航選單
    	Button menuBu =new Button("");        	
    	menuBu.setIcon(new ThemeResource("./images/menu.png"));
    	menuBu.setDescription(i18nProps.getProperty("menu","導航選單"));
    	menuBu.setWidth("32px");
    	menuBu.addStyleName(ValoTheme.BUTTON_LINK);
    	menuBu.addStyleName("icon-only");
    	menuBu.addClickListener(
		    new ClickListener() {
				@Override
				public void buttonClick(ClickEvent event) {
					MenuPopover scsbPopover = new MenuPopover();
    		    	//彈出視窗的位置
					scsbPopover.popover.showRelativeTo(event.getButton());        		    							
				}
    		}
    	);
    	//首頁
    	Button homeBu =new Button(""); 
    	homeBu.setIcon(new ThemeResource("./images/home.png"));
    	homeBu.setId("home");
    	homeBu.setWidth("32px");
    	homeBu.setDescription(i18nProps.getProperty("home","回首頁"));
    	homeBu.addStyleName(ValoTheme.BUTTON_LINK);
    	homeBu.addStyleName("icon-only");
    	homeBu.addClickListener(
		    new ClickListener() {
				@Override
				public void buttonClick(ClickEvent event) {
					//manager.removeAllComponents();
					//manager.setPreviousComponent(null);
					//manager.getViewStack().removeAllElements();					
					manager.navigateTo(new MainView("DW").getView());
					//manager.setCurrentComponent(new MainView("SS").view);
				}
    		}
    	);
      	//登出
    	Button logoutBu =new Button(""); 
    	logoutBu.setIcon(new ThemeResource("./images/logout.png"));
    	logoutBu.setId("logout");
    	logoutBu.setWidth("32px");
    	logoutBu.setDescription(i18nProps.getProperty("logout","登出"));
    	logoutBu.addStyleName(ValoTheme.BUTTON_LINK);
    	logoutBu.addStyleName("icon-only");
    	logoutBu.addClickListener(
		    new ClickListener() {
				@Override
				public void buttonClick(ClickEvent event) {
					//登出
					SessionDbService sessionDBSrv =new SessionDbService();
					SessionDb sessionBean =new SessionDb();
					sessionBean.setSessionId(session.getId());
					sessionBean.setSessionUserid(users.getUserid());
					sessionBean.setSessionIp(UI.getCurrent().getPage().getWebBrowser().getAddress());
					sessionDBSrv.deleteSessionDb(sessionBean);
					
					manager.removeAllComponents();
					//清空session
					
					session.setAttribute("PassOK",null);
					session.setAttribute("UsersBean",null);
					session.setAttribute("UserAction",null);
					
					//session.invalidate();
					//UI.getCurrent().getSession().close();
					UI.getCurrent().getPage().reload();
					
				}
    		}
    	);    
    	
      	//我的最愛
    	Button favoriteBu =new Button(""); 
    	favoriteBu.setIcon(new ThemeResource("./images/favorites.png"));
    	favoriteBu.setId("favorite");
    	favoriteBu.setWidth("32px");
    	favoriteBu.setDescription(i18nProps.getProperty("favorite","我的最愛"));
    	favoriteBu.addStyleName(ValoTheme.BUTTON_LINK);
    	favoriteBu.addStyleName("icon-only");
    	favoriteBu.addClickListener(
		    new ClickListener() {
				@Override
				public void buttonClick(ClickEvent event) {
					//manager.removeAllComponents();
					//manager.setPreviousComponent(null);
					//manager.getViewStack().removeAllElements();
					manager.navigateTo(new FavoriteView().getView());
					//manager.setCurrentComponent(new FavoriteView().view);
				}
    		}
    	);       	
    	//help選單
    	Button helpBu =new Button("");        	
    	helpBu.setIcon(new ThemeResource("./images/faq32.png"));
    	helpBu.setId("help");
    	helpBu.setWidth("32px");
    	helpBu.setDescription(i18nProps.getProperty("help","線上說明"));
    	helpBu.setStyleName(ValoTheme.BUTTON_LINK);
    	helpBu.addStyleName("icon-only");    	
    	helpBu.addClickListener(
		    new ClickListener() {
				@Override
				public void buttonClick(ClickEvent event) {
					HelpPopover scsbPopover = new HelpPopover("Header");
					UI.getCurrent().addWindow(scsbPopover.popover);					
				}
    		}
    	);    	
    	
    	//smart query login
		loginSmartQueryBu.setIcon(new ThemeResource("./images/sq_up.png"));
		loginSmartQueryBu.setId("help");
		loginSmartQueryBu.setWidth("32px");
		loginSmartQueryBu.setDescription(i18nProps.getProperty("help","線上說明"));
		loginSmartQueryBu.setStyleName(ValoTheme.BUTTON_LINK);
		loginSmartQueryBu.addStyleName("icon-only");    	
		loginSmartQueryBu.addClickListener(
		    new ClickListener() {
				@Override
				public void buttonClick(ClickEvent event) {
					runSmartQueryLogin();
				}
    		}
    	);    	
		runSmartQueryLogin();
		
    	mainTop.setWidth("99%");
    	mainTop.setHeight("100%");
    	
    	mainTop.addComponent(labelUser,"left: 10px; top: 2px;");
    	mainTop.addComponent(labelIP,"left: 10px; top: 20px;");
    	//mainTop.addComponent(labelMarquee,"left: 170px; top: 10px;");
    	
    	mainTop.addComponent(loginSmartQueryBu,"right: 215px; top: 2px;");
    	mainTop.addComponent(homeBu,"right: 180px; top: 2px;");
    	mainTop.addComponent(menuBu,"right: 145px; top: 2px;");
    	mainTop.addComponent(langBu,"right: 110px; top: 2px;");
    	mainTop.addComponent(favoriteBu,"right: 75px; top: 2px;");
    	mainTop.addComponent(helpBu,"right: 40px; top: 2px;");
    	mainTop.addComponent(logoutBu,"right: 5px; top: 4px;");
    	
    	vLayout.addComponent(mainTop);
    	vLayout.setMargin(new MarginInfo(false,false,false,false));
    }
    
    public void setManager(ScsbManager manager){
    	this.manager=manager;
    }
    
    public void runSmartQueryLogin(){
		String cProjcet="DW";
		String cUserID=users.getUserid();
		String cPassword="12345678";    	
		String loginUrl =systemProps.getProperty("_SmartQuery_Login");
       	//SmartQuery GUID 取得
    		try {    		
           		HttpClient httpClient = new HttpClient();
            	PostMethod post = new PostMethod(loginUrl);
            	post.setParameter("path", cProjcet);
            	//post.setParameter("filename", cQuery);
            	post.setParameter("UserName", cUserID);
            	//post.setParameter("password", cPassword);
            	post.setParameter("password", session.getId());
            	//GetMethod get = new GetMethod(sUrl);
    			httpClient.executeMethod(post);
    			//InputStream inStream=get.getResponseBodyAsStream();
    	        byte[] responseBody = post.getResponseBody();
    	        String sGUID =new String(responseBody,"UTF-8");
    	        if (sGUID.length() >50){
    	        	loginSmartQueryBu.setIcon(new ThemeResource("./images/sq_down.png"));
    	        	loginSmartQueryBu.setDescription("SmartQuery驗證失敗,請重新連線!!");
    	        	session.setAttribute("_SmartQueryGUID", "");
    	        }else{
    	        	loginSmartQueryBu.setIcon(new ThemeResource("./images/sq_up.png"));
    	        	loginSmartQueryBu.setDescription("SmartQuery連線成功!!");
    	        	session.setAttribute("_SmartQueryGUID", sGUID);
    	        }    	
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
            	loginSmartQueryBu.setIcon(new ThemeResource("./images/sq_down.png"));
            	loginSmartQueryBu.setDescription("SmartQuery驗證失敗,請重新連線!!");
            	session.setAttribute("_SmartQueryGUID", "");
    		}      	        
    }
}
