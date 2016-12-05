package com.scsb.vaadin.ui;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import com.scsb.db.bean.Transd;
import com.scsb.db.bean.Users;
import com.scsb.util.UserAction;
import com.scsb.vaadin.composite.ScsbGlobView;
import com.scsb.vaadin.composite.manager.ScsbBar;
import com.scsb.vaadin.composite.manager.ScsbButton;
import com.scsb.vaadin.include.FavoriteLayout;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.VerticalLayout;
/**
 * SCSB-我的最愛
 * @author 3471
 *
 */
@SuppressWarnings("serial")
public class FavoriteView extends ScsbGlobView {
	public FavoriteView() {
    	ScsbBar navBar =getView().getScsbBar();
    	navBar.setCaption(i18nProps.getProperty("favorite","我的最愛"));       			
		//我的最愛
		final Users users =(Users)session.getAttribute("UsersBean");
		Hashtable<String,String> userAction =(Hashtable<String,String>)session.getAttribute("UserAction");
		Vector vUsersf =UserAction.getUsersf(users.getUserid());
		
        //主選單
		Set<String> setPid =userAction.keySet();
		int iflag=0;
		for(Iterator<String> iterator = setPid.iterator(); iterator.hasNext(); ){
			String programid = iterator.next();
			final Transd transdBean =hashTrans.getTransD(programid);
			if (vUsersf.indexOf(programid) >= 0 ){   //有設定我的最愛
			    	final ScsbButton menuBu =new ScsbButton(i18nProps.getProperty(transdBean.getProgramid(),transdBean.getProgramname()));
			    	menuBu.setDescription(menuBu.getCaption());
			    	menuBu.setId(transdBean.getProgramid());
			    	//menuBu.setStyleName(StyleNames.NAVBAR_BUTTON_ARROW_LEFT);
					//debug mode
					if (systemProps.getProperty("DEBUG_MODE").equals("true"))
						menuBu.setCaption("("+menuBu.getId()+")"+menuBu.getCaption());
			    	menuBu.setWidth("100%");
					menuBu.setIcon(FontAwesome.STAR);
					iflag++;
			    	if (iflag % 2 == 1){
			    		menuBu.addStyleName("scsbBg2");
			    		menuBu.addStyleName("textLeft");
			    	}else{
			    		menuBu.addStyleName("scsbBg3");
			    		menuBu.addStyleName("textLeft");
			    	}				
					final int iSrv = iflag % 4;
			       	menuBu.addClickListener(new ClickListener() {
						@Override
						public void buttonClick(ClickEvent event) {
							try {
								//java code
								if (transdBean.getProgramtype().equals("1")){
									ScsbGlobView scsbMenu = (ScsbGlobView)Class.forName("com.scsb.vaadin.ui."+transdBean.getGroupid().substring(0,2).toLowerCase()+"."+transdBean.getProgramid()).newInstance();
									scsbMenu.getView().setRightComponent(new FavoriteLayout(users ,"JAVA" ,transdBean.getProgramid() ,true).absLayout);
									scsbMenu.getView().setCaption(menuBu.getCaption());
									getView().getScsbManager().navigateTo(scsbMenu.getView());
								}
								//sas 65 code
								if (transdBean.getProgramtype().equals("2")){
									String cgiService="default"+iSrv;
									if (iSrv==0) cgiService="default";
									String sUrl =systemProps.getProperty("_SasCgi")+"?";
									if (!transdBean.getBrowsemode().equals("")) cgiService="default"+transdBean.getBrowsemode().trim()+"";
									sUrl+="_service="+cgiService;
									sUrl+="&_program="+systemProps.getProperty("_SasPath")+"."+transdBean.getProgramid().trim()+".sas&_debug=0";
									sUrl+="&xpgn="+transdBean.getProgramid().trim();
									sUrl+="&xusr="+users.getUserid();
									sUrl+="&_sid="+session.getId();
									TransBrowser scsbMenu =new TransBrowser(sUrl);
									scsbMenu.getView().setCaption(menuBu.getCaption());
									scsbMenu.getView().setRightComponent(new FavoriteLayout(users ,"SAS" ,transdBean.getProgramid() ,true).absLayout);
									getView().getScsbManager().navigateTo(scsbMenu.getView());									

								}	
								//sas 252 code
								if (transdBean.getProgramtype().equals("3")){
									String sUrl =systemProps.getProperty("_CcdbCgi")+"?";
									String cgiService="default"+iSrv;
									if (iSrv==0) cgiService="default";
									if (!transdBean.getBrowsemode().equals("")) cgiService="default"+transdBean.getBrowsemode().trim()+"";
									sUrl+="_service="+cgiService;
									sUrl+="&_program="+systemProps.getProperty("_CcdbPath")+"."+transdBean.getProgramid().trim()+".sas&_debug=0";
									sUrl+="&xpgn="+transdBean.getProgramid().trim();
									sUrl+="&xusr="+users.getUserid();
									sUrl+="&_sid="+session.getId();
									TransBrowser scsbMenu =new TransBrowser(sUrl);
									scsbMenu.getView().setCaption(menuBu.getCaption());
									scsbMenu.getView().setRightComponent(new FavoriteLayout(users ,"SAS" ,transdBean.getProgramid() ,true).absLayout);
									getView().getScsbManager().navigateTo(scsbMenu.getView());									
								}							
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
			    	});
			        getContent().addComponent(menuBu);
				}//if			
		}
    }
	
}