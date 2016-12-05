package com.scsb.vaadin.ui.gc;

import java.util.Hashtable;

import com.scsb.db.bean.Fnbct0;
import com.scsb.db.bean.Trans;
import com.scsb.db.bean.Users;
import com.scsb.db.service.Fnbct0Service;
import com.scsb.db.service.UsersService;
import com.scsb.vaadin.composite.ScsbProperty;
import com.scsb.vaadin.composite.ScsbPropertyButton;
import com.scsb.vaadin.include.TitleLayout;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.TabSheet.Tab;

/**
 * 使用者-屬性設定
 * @author 3471
 *
 */
@SuppressWarnings("serial")
public class gc6120_property extends ScsbProperty {
	Users           	dataBean ;
	BeanItem<Users> 	dataItem ;	
	FieldGroup          dataBinder;
	
	TextField userId =new TextField();
	TextField userName =new TextField();
	TextField userLevel =new TextField();
	
	boolean IsInsert=false;
	Users srcBean =new Users();
	
	TabSheet     mainTab    =new TabSheet();	
	Tab  tabProperty; 
	String sid;			
	/**
	 * 使用者-屬性設定-主畫面
	 */
	public gc6120_property(String sidx ,Users orgBean ,boolean IsInsert ,TabSheet mainTabx) {
		this.srcBean=orgBean;
		DataBinderFactory();
		this.IsInsert=IsInsert;
		this.mainTab=mainTabx;
		this.sid=sidx;
		this.getToolBar().setSid(sid);		
		
		//欄位編輯====================================================================
		userId.setId("userId");
		userId.setCaption(i18nProps.getProperty("userId","使用者行編"));
		userId.setReadOnly(true);
		userId.setImmediate(true);
		userId.setIcon(FontAwesome.LOCK);
		getFormLayout().addComponent(userId);
		
		userName.setId("userName");
		userName.setCaption(i18nProps.getProperty("userName","使用者姓名"));
		userName.setReadOnly(true);
		userName.setImmediate(true);
		userName.setIcon(FontAwesome.LOCK);
		getFormLayout().addComponent(userName);			
		
		userLevel.setId("userLevel");
		userLevel.setCaption(i18nProps.getProperty("userLevel","使用者職位"));
		userLevel.setReadOnly(true);
		userLevel.setImmediate(true);
		userLevel.setIcon(FontAwesome.LOCK);
		getFormLayout().addComponent(userLevel);		
		
		//先判斷被授權人員是否為營業單位人員(暫時只能用部門前二碼去FNBCT0判斷)
		Hashtable<String,Trans> deptTable =hashTrans.getDeptTrans("L");
		Fnbct0Service sasFnbct0Srv = new Fnbct0Service();
		Fnbct0 sasFnbct0 =new Fnbct0();
		sasFnbct0.setBrhCod(orgBean.getDeptid().substring(0,2));
		BeanContainer<String,Fnbct0> sasFnbct0Container =sasFnbct0Srv.getSasFnbct0_PKs(sasFnbct0);
		if (sasFnbct0Container.size() > 0){		
			Fnbct0 beanSasFnbct0 =sasFnbct0Container.getItem(sasFnbct0Container.getIdByIndex(0)).getBean();
			//營業單位
			if (beanSasFnbct0.getBrhTyp().equals("2"))  deptTable = hashTrans.getDeptTrans("P");
		}
		//細項選單====================================================================
    	//新增時不可編輯明細
    	if (!IsInsert){
	        //主選單
    		BeanContainer<String,Trans> transContainer = hashTrans.getAllTrans();
	        transContainer.sort(new Object[]{"groupid"},new boolean[]{true});
	        
	        int iFlag=0;
			for(int i=0;i<transContainer.size();i++){
				final Trans bean=transContainer.getItem(transContainer.getIdByIndex(i)).getBean();
				if (deptTable.get(bean.getGroupid()) != null)	{
			    	//導航選單
					if (bean.getGroupid().indexOf("_") == -1){
						TitleLayout title =new TitleLayout(messageProps.getProperty("label_TransList","權限設定")+"－［"+bean.getGroupname()+"］");
						addComponent(title.vLayout);
						iFlag=0;
					}else{
						iFlag++;
						final ScsbPropertyButton menuBu =new ScsbPropertyButton(i18nProps.getProperty(bean.getGroupid(),bean.getGroupname()));        	
						menuBu.setDescription(messageProps.getProperty("Button_edit_view","編輯/檢視"));
						menuBu.setId(bean.getGroupid());
						if (iFlag % 2 ==1) menuBu.addStyleName("scsbBg2");
						else menuBu.addStyleName("scsbBg3");							
				    	menuBu.addClickListener(new ClickListener() {
				    		@Override
							public void buttonClick(ClickEvent event) {
				    			new gc6120_usersD(sid ,srcBean ,bean ,mainTab);
							}			    		
				    	});
				    	addComponent(menuBu);
					}//if					
				}			
			}//for
		}//if
    	
		String tabCaption =i18nProps.getProperty("propertySet","-屬性設定");
		if (srcBean != null)  tabCaption =srcBean.getUserName()+tabCaption;
	    tabProperty =mainTab.addTab(getContent() ,tabCaption);
	    tabProperty.setClosable(true);
	    tabProperty.setIcon(FontAwesome.EDIT);
	    mainTab.setSelectedTab(tabProperty);      	
	}
	
	 /**
	  * Data的Field Factory
	  * @author 3471
	  *
	  */
    private void DataBinderFactory(){
		UsersService usersSrv =new UsersService();
		dataBean =usersSrv.getUsers_PK(srcBean);
		
		dataItem = new BeanItem<Users>(dataBean);
		dataBinder = new FieldGroup(dataItem);
		dataBinder.setBuffered(true);    	    		
		dataBinder.bind(userId, "userid");
		dataBinder.bind(userName, "username");
		dataBinder.bind(userLevel, "userlevel");
	 }	
}
