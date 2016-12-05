package com.scsb.servlet;

import java.util.Properties;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import com.scsb.domain.HashSystem;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.BootstrapFragmentResponse;
import com.vaadin.server.BootstrapListener;
import com.vaadin.server.BootstrapPageResponse;
import com.vaadin.server.CustomizedSystemMessages;
import com.vaadin.server.SessionInitEvent;
import com.vaadin.server.SessionInitListener;
import com.vaadin.server.SystemMessages;
import com.vaadin.server.SystemMessagesInfo;
import com.vaadin.server.SystemMessagesProvider;
import com.vaadin.server.VaadinServlet;
@WebServlet ( urlPatterns = {"/main/*","/VAADIN/*"} , name = "ScsbEdwpWebApplication" , asyncSupported = true )
@VaadinServletConfiguration ( ui = com.scsb.vaadin.ScsbUI.class , productionMode = false )
public final class ScsbCustomeServlet extends VaadinServlet{
	private static final long serialVersionUID = 1L;
	protected static HashSystem    hashSystem    	= HashSystem.getInstance();
	protected static Properties    systemProps 		= hashSystem.getProperties();

	protected void servletInitialized() throws ServletException {
    	super.servletInitialized();
    	getService().addSessionInitListener(new SessionInitListener() {
            /**
			 * 
			 */
			private static final long serialVersionUID = -1186239434967727745L;
			public void sessionInit(SessionInitEvent event) {
                event.getSession().addBootstrapListener( new BootstrapListener() {
					/**
					 * 
					 */
					private static final long serialVersionUID = -6179126385545984419L;
					@Override
					public void modifyBootstrapFragment(BootstrapFragmentResponse response) {
						// TODO Auto-generated method stub
						
					}
					@Override
					public void modifyBootstrapPage(BootstrapPageResponse response) {
						// TODO Auto-generated method stub
						org.jsoup.nodes.Element head = response.getDocument().head();
						// 配合smart query 啟用IE相容性
						org.jsoup.nodes.Element compatTag = head.getElementsByAttributeValue("http-equiv", "X-UA-Compatible").get(0);
		                compatTag.attr("content", "IE=EmulateIE8");						
					}}
                );
            }
        });    	
    	
    	/**
    	 * 
    	 */
	    getService().setSystemMessagesProvider(
            new SystemMessagesProvider() {
            	private static final long serialVersionUID = -6839999172109490028L;
				@Override
				public SystemMessages getSystemMessages(SystemMessagesInfo systemMessagesInfo) {
					
					String loginUrl =systemProps.getProperty("SYSTEM_PATH")+"";
					
                    CustomizedSystemMessages messages = new CustomizedSystemMessages();
                    messages.setCommunicationErrorCaption("連線錯誤");
                    messages.setCommunicationErrorMessage("系統已停止或重新啟動<br>請聯絡資訊研發處#4241");
                    messages.setCommunicationErrorNotificationEnabled(true);
                    messages.setCommunicationErrorURL(loginUrl);
                    messages.setSessionExpiredCaption("操作時間逾時");
                    messages.setSessionExpiredMessage("請重新<a href='"+loginUrl+"'>登入</a>!!");
                    return messages;
				}
            }
	    );
    }
}
