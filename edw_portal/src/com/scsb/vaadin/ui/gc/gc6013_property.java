package com.scsb.vaadin.ui.gc;

import java.sql.SQLException;
import java.util.Properties;

import com.scsb.db.DBAction;
import com.scsb.db.bean.Dept;
import com.scsb.db.bean.Ldapou;
import com.scsb.db.bean.Trans;
import com.scsb.db.service.DeptService;
import com.scsb.util.DateUtil;
import com.scsb.vaadin.composite.MessageBox;
import com.scsb.vaadin.composite.ScsbProperty;
import com.scsb.vaadin.composite.MessageBox.ButtonType;
import com.scsb.vaadin.composite.MessageBox.EventListener;
import com.scsb.vaadin.composite.ScsbPropertyButton;
import com.scsb.vaadin.include.CcCodeSelect;
import com.scsb.vaadin.include.TitleLayout;
import com.scsb.vaadin.property.GoogleMapView;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TabSheet.Tab;

/**
 * 部門屬性屬性設定
 * @author 3471
 *
 */
@SuppressWarnings("serial")
public class gc6013_property extends ScsbProperty {
	NativeSelect areaBox;
	Ldapou srcBean =new Ldapou();
	TabSheet     mainTab    =new TabSheet();	
	Tab  tabProperty; 
	String sid;		
	/**
	 * 部門屬性-主畫面
	 * @param brhCod
	 * @param propertyTitle
	 * @param pp
	 */
	public gc6013_property(String sidx ,final Ldapou srcBean ,final Properties pp ,TabSheet mainTabx) {
		this.srcBean =srcBean;
		this.mainTab=mainTabx;
		this.sid=sidx;
		this.getToolBar().setSid(sid);
		//部門的允視分行=========================================================================
		final ScsbPropertyButton menuBu =new ScsbPropertyButton(i18nProps.getProperty("allowBrh","允視分行"));        	
    	menuBu.setDescription(messageProps.getProperty("Button_edit_view","編輯/檢視"));
    	menuBu.setId("ALLOW_BRH");
    	menuBu.addClickListener(new ClickListener() {
    		@Override
			public void buttonClick(ClickEvent event) {
				new gc6013_PropertyView(sid ,srcBean.getDescription()+"-"+menuBu.getCaption() ,srcBean.getOu() ,menuBu.getId() ,mainTab);
			}
    	});
    	addComponent(menuBu);

		//細項選單====================================================================
        //主選單
        BeanContainer<String,Trans> transContainer = hashTrans.getAllTrans();
        transContainer.sort(new Object[]{"groupid"},new boolean[]{true});
        
        int iFlag=0;
		for(int i=0;i<transContainer.size();i++){
			final Trans bean=transContainer.getItem(transContainer.getIdByIndex(i)).getBean();
	    	//導航選單
			if (bean.getGroupid().indexOf("_") == -1){
				TitleLayout title =new TitleLayout(messageProps.getProperty("label_TransList","權限設定")+"－［"+bean.getGroupname()+"］");
				addComponent(title.vLayout);
				iFlag=0;
			}else{
				iFlag++;
				final ScsbPropertyButton menuBu2 =new ScsbPropertyButton(i18nProps.getProperty(bean.getGroupid(),bean.getGroupname()));        	
				menuBu2.setDescription(messageProps.getProperty("Button_edit_view","編輯/檢視"));
				menuBu2.setId(bean.getGroupid());
				if (iFlag % 2 ==1) menuBu.addStyleName("scsbBg2");
				else menuBu.addStyleName("scsbBg3");				
				menuBu2.addClickListener(new ClickListener() {
		    		@Override
					public void buttonClick(ClickEvent event) {
		    			new gc6013_deptD(sid ,srcBean ,bean ,mainTab);
					}			    		
		    	});
				addComponent(menuBu2);
			}//if
		}//for  
		
		String tabCaption =i18nProps.getProperty("propertySet","-屬性設定");
		if (srcBean != null)  tabCaption =srcBean.getDescription()+tabCaption;
	    tabProperty =mainTab.addTab(getContent() ,tabCaption);
	    tabProperty.setClosable(true);
	    tabProperty.setIcon(FontAwesome.EDIT);
	    mainTab.setSelectedTab(tabProperty); 	
	}
}
