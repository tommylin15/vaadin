package com.scsb.vaadin.ui;

import java.util.Hashtable;

import com.scsb.db.bean.Trans;
import com.scsb.vaadin.composite.ScsbGlobView;
import com.scsb.vaadin.include.QueryLayout;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

@SuppressWarnings("serial")
public class TransView extends ScsbGlobView {
    public TransView(String groupId) {
    	int iflag=0;

        //主選單
    	Hashtable<String ,String> userTrans =(Hashtable<String ,String>)session.getAttribute("UserTrans");
    	Trans trans=hashTrans.getTrans(groupId);
    	getView().setCaption(trans.getGroupname());
        BeanContainer<String,Trans> transContainer = hashTrans.getAllTrans();
        transContainer.sort(new Object[]{"groupid"}, new boolean[]{true});
		for(int i=0;i<transContainer.size();i++){
			final Trans bean=transContainer.getItem(transContainer.getIdByIndex(i)).getBean();
	    	//導航選單 
			if (bean.getGroupid().substring(0,2).equals(groupId)
				 && bean.getGroupid().indexOf("_") > -1
				 && userTrans.get(bean.getGroupid()) != null //使用者有權限者
				 ){
				
				iflag++;
		    	final Button menuBu =new Button(i18nProps.getProperty(bean.getGroupid(),bean.getGroupname()));
		    	menuBu.setId(bean.getGroupid());
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
		    		menuBu.setIcon(FontAwesome.COFFEE);
		    		menuBu.addClickListener(new ClickListener() {

					@Override
					public void buttonClick(ClickEvent event) {
							try {							
								TransDView transdView =new TransDView(bean.getGroupid());
								transdView.getView().setCaption(menuBu.getCaption());
								getView().getScsbManager().navigateTo(transdView.getView());
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
		    	});
		    		getContent().addComponent(menuBu);
		    	/*
		    	 * group.addComponent(menuBu);
		    	iflag++;
		    	if (iflag % 2 == 0){
		    		g.addComponent(group);
		    		group =new HorizontalButtonGroup();
		        	group.setWidth("100%");
		    	}
		    	*/
			}//if
		}//for
		//fast query===================================================================
		QueryLayout quyerLayout =new QueryLayout(getContent(),"menuBlue");
		getView().setRightComponent(quyerLayout.absLayout);		
    }      
}



