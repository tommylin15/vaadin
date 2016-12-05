package com.scsb.vaadin.ui;


import com.scsb.db.bean.Transd;
import com.scsb.vaadin.composite.ScsbGlobView;
import com.vaadin.server.ExternalResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;

public class SmartQueryBrowser extends ScsbGlobView {
	/**
	 *主畫面
	 */
	
	//CssLayout layout = new CssLayout();
	Panel panel =new Panel();
	BrowserFrame smartQueryFrame = new BrowserFrame();

	public SmartQueryBrowser(Transd bean) {
		String cProjcet=bean.getGroupid().substring(0,2);
		String cSys=bean.getGroupid();
		String cQuery =bean.getProgramid();
		String cActionCode=bean.getActionCode();
		//String cUserID=users.getUserid();
		//String cPassword="12345678";
		
		String cType=bean.getBrowsemode();
		String queryUrl =systemProps.getProperty("_SmartQuery_Squery");
		if (cType.equals("D")){
			queryUrl =systemProps.getProperty("_SmartQuery_Dquery");
		}
		String smartQueryGUID = session.getAttribute("_SmartQueryGUID")+"";
        if (smartQueryGUID.length() ==0){
	        Label label =new Label("SmartQuery驗證失敗,請重新連線!!<br>"+smartQueryGUID,ContentMode.HTML);
	        label.setSizeFull();
	        panel.setContent(label);
        }else{
	        String cURL = queryUrl+"?GUID="+smartQueryGUID
	        		+ "&Path="+cProjcet 
	        		+"&sys="+cSys 
	        		+"&filename=" + cQuery 
	        		+"&actioncode=" + cActionCode
	        		+ "&SQ_AutoLogout=false";
	        BrowserFrame bf =new BrowserFrame("" ,new ExternalResource(cURL));
	        bf.setSizeFull();
			panel.setContent(bf);
	        //layout.addComponent(panel);
	    }
		panel.setWidth("100%");
		panel.setHeight(_screenHeight+"px");		        
		//layout.setWidth("100%");
		//layout.setHeight(_screenHeight+"px");
	    getView().setContent(panel);	    	

	}
}
