package com.scsb.vaadin.ui.gc;

import com.scsb.db.bean.Job;
import com.scsb.db.bean.Trans;
import com.scsb.db.service.JobService;
import com.scsb.domain.HashTrans;
import com.scsb.vaadin.composite.ScsbPage;
import com.scsb.vaadin.composite.ScsbProperty;
import com.scsb.vaadin.composite.ScsbPropertyButton;
import com.scsb.vaadin.include.TitleLayout;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.TabSheet.Tab;

/**
 * 工作內容-屬性設定
 * @author 3471
 *
 */
@SuppressWarnings("serial")
public class gc6012_property extends ScsbProperty {
	protected static HashTrans    hashTrans    	= HashTrans.getInstance();
	
	Job           		dataBean ;
	BeanItem<Job> 	dataItem ;	
	FieldGroup          dataBinder;
	
	TextField jobName =new TextField();
	NativeSelect groupMode =new NativeSelect();
	
	boolean IsInsert=false;
	Job srcBean =new Job();
	
	TabSheet     mainTab    =new TabSheet();	
	Tab  tabProperty; 
	ScsbPage scsbPage;	
	String sid;		
	/**
	 * 工作內容-屬性設定-主畫面
	 */
	public gc6012_property(String sidx ,final Job srcBean ,boolean IsInsert ,TabSheet mainTabx ,ScsbPage scsbPage) {
		this.srcBean=srcBean;
		DataBinderFactory();
		this.mainTab=mainTabx;
		this.sid=sidx;
		this.scsbPage=scsbPage;
		this.getToolBar().setSid(sid);
		this.IsInsert=IsInsert;
		
		//欄位編輯====================================================================
		jobName.setId("jobName");
		jobName.setCaption(i18nProps.getProperty("jobName","工作內容"));
		jobName.setImmediate(true);
		jobName.setIcon(FontAwesome.LOCK);
		jobName.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				getToolBar().setSaveButtonVisible(true);
			}
    	});		
		getFormLayout().addComponent(jobName);

		//細項選單====================================================================
    	//新增時不可編輯明細
    	if (!IsInsert){
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
					final ScsbPropertyButton menuBu =new ScsbPropertyButton(i18nProps.getProperty(bean.getGroupid(),bean.getGroupname()));        	
					menuBu.setDescription(messageProps.getProperty("Button_edit_view","編輯/檢視"));
					menuBu.setId(bean.getGroupid());
					if (iFlag % 2 ==1) menuBu.addStyleName("scsbBg2");
					else menuBu.addStyleName("scsbBg3");		
					menuBu.addClickListener(new ClickListener() {
			    		@Override
						public void buttonClick(ClickEvent event) {
			    			new gc6012_jobD(sid ,srcBean ,bean ,mainTab);
						}			    		
			    	});
			    	addComponent(menuBu);
				}//if
			}//for
		}//if
    	
		String tabCaption =i18nProps.getProperty("propertySet","-屬性設定");
		if (srcBean != null)  tabCaption =srcBean.getJobName()+tabCaption;
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
		JobService jobSrv =new JobService();
		dataBean =jobSrv.getJob_PK(srcBean);
		
		dataItem = new BeanItem<Job>(dataBean);
		dataBinder = new FieldGroup(dataItem);
		dataBinder.setBuffered(true);    	    		
		dataBinder.bind(jobName, "jobName");
	 }	
}
