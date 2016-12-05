package com.scsb.vaadin.ui;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Hashtable;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.jsoup.Jsoup;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.scsb.db.bean.Transd;
import com.scsb.util.AsianFontProvider;
import com.scsb.vaadin.composite.ScsbGlobView;
import com.scsb.vaadin.include.FavoriteLayout;
import com.scsb.vaadin.include.QueryLayout;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

@SuppressWarnings("serial")
public class TransDView extends ScsbGlobView {
    public TransDView(String groupId) {
    	int iflag=0;
        //主選單
    	Hashtable<String ,String> userAction =(Hashtable<String ,String>)session.getAttribute("UserAction");
        BeanContainer<String,Transd> transdContainer = hashTrans.getAllTransD(groupId);
        transdContainer.sort(new Object[]{"groupid","sortKey"}, new boolean[]{true,true});
		for(int i=0;i<transdContainer.size();i++){
			final Transd bean=transdContainer.getItem(transdContainer.getIdByIndex(i)).getBean();
	    	//明細功能
			if (bean.getGroupid().equals(groupId)
				&& userAction.get(bean.getProgramid()) != null //使用者有權限者
				){
		    	final Button menuBu =new Button(i18nProps.getProperty(bean.getProgramid(),bean.getProgramname()));
		    	menuBu.setId(bean.getProgramid());
		    	iflag++;
		    	if (iflag % 2 == 1){
		    		menuBu.addStyleName("scsbBg2");
		    		menuBu.addStyleName("textLeft");
		    	}else{
		    		menuBu.addStyleName("scsbBg3");
		    		menuBu.addStyleName("textLeft");
		    	}
				//debug mode
				if (systemProps.getProperty("DEBUG_MODE").equals("true"))
					menuBu.setCaption("("+menuBu.getId()+")"+menuBu.getCaption());
		    		menuBu.setWidth("100%");
		    		menuBu.setIcon(FontAwesome.DESKTOP);
		    		menuBu.addClickListener(new ClickListener() {
					@Override
					public void buttonClick(ClickEvent event) {
							try {
								//java code
								if (bean.getProgramtype().equals("1")){
									ScsbGlobView scsbMenu = (ScsbGlobView)Class.forName("com.scsb.vaadin.ui."+bean.getGroupid().substring(0,2).toLowerCase()+"."+bean.getProgramid()).newInstance();
									scsbMenu.getView().setCaption(menuBu.getCaption());
									scsbMenu.getView().setRightComponent(new FavoriteLayout(users ,"JAVA" ,bean.getProgramid()).absLayout);
									getView().getScsbManager().navigateTo(scsbMenu.getView());
								}
								//smart query
								if (bean.getProgramtype().equals("2")){
							        SmartQueryBrowser scsbMenu =new SmartQueryBrowser(bean);
									scsbMenu.getView().setCaption(menuBu.getCaption());
									scsbMenu.getView().setRightComponent(new FavoriteLayout(users ,"SmartQuery" ,bean.getProgramid()).absLayout);
									getView().getScsbManager().navigateTo(scsbMenu.getView());
									
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}

		    	});
		    	getContent().addComponent(menuBu);
				QueryLayout quyerLayout =new QueryLayout(getContent(),"menuRed");
				getView().setRightComponent(quyerLayout.absLayout);		    	
			}//if
		}//for
		//if (group.getComponentCount()>0) g.addComponent(group);
    }   
       
}