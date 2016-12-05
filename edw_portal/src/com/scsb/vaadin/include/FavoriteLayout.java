package com.scsb.vaadin.include;


import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.servlet.ServletContext;

import com.scsb.db.bean.Users;
import com.scsb.util.UserAction;
import com.scsb.vaadin.composite.ScsbGlob;
import com.scsb.vaadin.ui.popover.HelpPopover;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.BaseTheme;
/**
 * SCSB-單一功能的快捷按鈕
 * @author 3471
 *
 */
@SuppressWarnings("serial")
public class FavoriteLayout extends ScsbGlob {
	
	public AbsoluteLayout absLayout =new AbsoluteLayout();
	
	public FavoriteLayout(Users users ,String fileType ,String programid ) {
		//檢查是否存在我的最愛裡
		Vector vUsersf =UserAction.getUsersf(users.getUserid());
		if (vUsersf.indexOf(programid) >= 0 ){   //有設定我的最愛
			Create(users ,fileType ,programid ,true);
		}else{
			Create(users ,fileType ,programid ,false);
		}
	}

	public FavoriteLayout(Users users ,String fileType ,String programid ,boolean haveStar) {
		Create(users ,fileType ,programid ,haveStar);
	}
	
	public void Create(final Users users ,String fileType ,final String programid ,boolean haveStar) {
		absLayout.setWidth("120px");
		absLayout.setHeight("36px");
    	int iFlag=10;
    	this.systemProps = hashSystem.getProperties();
   	
    	//檢查有無說明文件===============================================================
    	ServletContext servletContext = VaadinServlet.getCurrent().getServletContext();
    	String sUrl =systemProps.getProperty("_HelpPath")+"/"+programid+".html";
		String htmlString = servletContext.getRealPath("/")+sUrl;	
		try {
			File htmlFile = new File(htmlString);
			if(htmlFile.exists()){
		    	//help選單
		    	Button helpBu =new Button("");        	
		    	helpBu.setIcon(new ThemeResource("./images/help16.png"));
		    	helpBu.setId("help");
		    	helpBu.addStyleName(BaseTheme.BUTTON_LINK);
		    	helpBu.addClickListener(
				    new ClickListener() {
						@Override
						public void buttonClick(ClickEvent event) {
							HelpPopover popover = new HelpPopover(programid);
							UI.getCurrent().addWindow(popover.helpWin);					
						}
		    		}
		    	);
		    	absLayout.addComponent(helpBu,"right: "+iFlag+"px; top: 1px;");
		    	iFlag+=25;
			}			
		} catch (Exception e) {
		}
		
      	//我的最愛按鈕(初期供sas程式使用)=======================================================
		
    	Button favoriteBu =new Button(""); 
    	if (haveStar){
    		favoriteBu.setIcon(new ThemeResource("./images/ButtonFavorite1.png"));
    		favoriteBu.setId("ButtonFavorite1");
    	}else{
    		favoriteBu.setIcon(new ThemeResource("./images/ButtonFavorite2.png"));
    		favoriteBu.setId("ButtonFavorite2");
    	}
    	favoriteBu.addStyleName(BaseTheme.BUTTON_LINK);
    	favoriteBu.addClickListener(
		    new ClickListener() {
				@Override
				public void buttonClick(ClickEvent event) {
					if (event.getButton().getId().equals("ButtonFavorite2")){
						event.getButton().setIcon(new ThemeResource("./images/ButtonFavorite1.png"));
						event.getButton().setId("ButtonFavorite1");
						//存到我的最愛
						UserAction.insertUsersf(users.getUserid(), programid);
					}else{
						event.getButton().setIcon(new ThemeResource("./images/ButtonFavorite2.png"));
						event.getButton().setId("ButtonFavorite2");
						//取消我的最愛
						UserAction.deleteUsersf(users.getUserid(), programid);
					}
				}
    		}
    	);
    	absLayout.addComponent(favoriteBu,"right: "+iFlag+"px; top: 1px;");
    	iFlag+=25;		
    }
}