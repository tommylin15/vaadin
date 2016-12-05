package com.scsb.etl;

import java.io.File;
import java.util.Set;

import javax.servlet.ServletContext;

import org.tepi.filtertable.FilterTable;
import org.tepi.filtertable.FilterTreeTable;
import org.vaadin.teemu.jsoncontainer.JsonContainer;

import com.scsb.crpro.AceWindow;
import com.scsb.crpro.CrproLayout;
import com.scsb.db.bean.EtlCheckfile;
import com.scsb.db.bean.EtlLogChannel;
import com.scsb.db.bean.EtlLogJob;
import com.scsb.db.bean.EtlLogTrans;
import com.scsb.db.service.EtlCheckfileService;
import com.scsb.db.service.EtlLogChannelService;
import com.scsb.db.service.EtlLogJobService;
import com.scsb.db.service.EtlLogTransService;
import com.scsb.util.IO;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Container.Filterable;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomTable.RowHeaderMode;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.themes.ValoTheme;

public class EtlLogLayout extends CrproLayout {
    private static final long serialVersionUID = 1L;
    EtlLogChannelService etlLogChannelSrv =new EtlLogChannelService();
    EtlLogJobService etlLogJobSrv =new EtlLogJobService();
    EtlLogTransService etlLogTransSrv =new EtlLogTransService();
    EtlCheckfileService etlCheckfileSrv =new EtlCheckfileService();
    
    JsonContainer jsonData=null;
    BeanContainer<String,EtlLogChannel> etlLogChannelContainer ;
    
    TabSheet    EtlTab    =new TabSheet(); 
    Tab tabRoot;
    Tab tabCheckFile;
    Tab tabDetil;
    Tab tabJob;
    
    private ComboBox dbSelect = new ComboBox("ETL群組");
    private CheckBox isRunningOnly = new CheckBox("限有執行者(key : Table exists)");
    private Button queryBu =new Button("查詢"); 
	
    public EtlLogLayout() {
    	
    	//0.1建立資料庫連結 for etl log(kettle的部份)
    	ServletContext servletContext = VaadinServlet.getCurrent().getServletContext();
		String jsonString =IO.read(servletContext.getRealPath("/")+systemProps.getProperty("ConnectionPoolJson"));
		jsonData= JsonContainer.Factory.newInstance(jsonString);		
    	
		dbSelect.setId("etlDB");		
		dbSelect.setImmediate(true);
		dbSelect.setNullSelectionAllowed(false);		
		//dbSelect.addValueChangeListener(dbSelectlistener);
		
		isRunningOnly.setValue(false);
		//isRunningOnly.addValueChangeListener(dbSelectlistener);
		
	   	queryBu.setId("squery");
	   	queryBu.setDescription("查詢");
	   	queryBu.addClickListener(
		    new ClickListener() {
				@Override
				public void buttonClick(ClickEvent event) {
					create();
				}
    		}
    	);    		
		
		int idb=0;
		for(int i=0;i<jsonData.size();i++){
        	Item item=jsonData.getItem(jsonData.getIdByIndex(i));
        	String running=(String)item.getItemProperty("Running").getValue();
        	String etllog=(String)item.getItemProperty("EtlLog").getValue();
        	if (running.toUpperCase().equals("TRUE") & etllog.equals("Y") ){
	        	String name=(String)item.getItemProperty("DBPool").getValue();
				String itemId =name;
				dbSelect.addItem(itemId);
				dbSelect.setItemCaption(itemId ,name );
				//一進去,先連第一個位置
				//if (idb ==0){
		        	//dbSelect.select(itemId);
				//}
				idb++;
        	}
		}//for
		//第一次先create()
		create();
    }
    
    /*
    ValueChangeListener dbSelectlistener = new ValueChangeListener() {
		@Override
		public void valueChange(ValueChangeEvent event) {
			create();
		}
	};  
	*/
	
    public void create(){
    	this.removeAllComponents();
    	AbsoluteLayout listLayout = new AbsoluteLayout();
    	listLayout.setWidth("99%");
    	listLayout.setHeight("40px");
    	
    	
    	listLayout.addComponent(dbSelect,"left: 10px; top: 2px;");
    	listLayout.addComponent(isRunningOnly,"left:200px; top: 2px;");
    	listLayout.addComponent(queryBu,"left: 460px; top: 2px;");
    	
    	this.addComponent(listLayout);
    	
    	if (dbSelect.getValue() !=null){
	    	etlLogChannelSrv.setDbPool(dbSelect.getValue()+"");
	    	etlLogJobSrv.setDbPool(dbSelect.getValue()+"");
	    	etlLogTransSrv.setDbPool(dbSelect.getValue()+"");
	    	etlCheckfileSrv.setDbPool(dbSelect.getValue()+"");
	    	EtlTab    =new TabSheet();
	    	EtlTab.setWidth(_screenWidth+"px");
	    	// screenHeight =_screenHeight - tabHeader
	    	EtlTab.setHeight((_screenHeight-150)+"px");
	    	
	    	
	    	/*****建立etl check file list begin*********************************************************************************/
			final BeanContainer<String,EtlCheckfile> etlCheckfileContainer =etlCheckfileSrv.getEtlCheckfile_All();
			etlCheckfileContainer.sort(new Object[]{"filename"}, new boolean[]{false});
	    	
			final FilterTable checkFileTable = new FilterTable();
			checkFileTable.setSizeFull();;
			checkFileTable.setFilterBarVisible(true);
			checkFileTable.setId("checkFileTable");
			checkFileTable.setStyleName("filtertable");
			checkFileTable.setSelectable(true);
			checkFileTable.setImmediate(true); 
			checkFileTable.setSortEnabled(true);
	
			checkFileTable.setContainerDataSource(etlCheckfileContainer);
			checkFileTable.setVisibleColumns((Object[]) new String[] {
					"filename" ,"shortFilename"  ,"type" ,"busDate","nowDate" ,"lastDate","status","size","endDate","updateMode"});
			//選取Table資料時
		    Property.ValueChangeListener listener = new Property.ValueChangeListener() {
		       	public void valueChange(Property.ValueChangeEvent event) {
		       		BeanItem<EtlCheckfile> beanitem=(BeanItem<EtlCheckfile>) etlCheckfileContainer.getItem(checkFileTable.getValue());
					if (beanitem != null){
						EtlCheckfile bean=beanitem.getBean();
						new etl_checkFile_property(dbSelect.getValue()+"" ,beanitem.getBean() ,true ,EtlTab ,checkFileTable);
		       		}
		        }
		    };
		    //有修改權限者
		    if (this._hashUserAction.get("etlEdit").equals("Y")){
		    	checkFileTable.addValueChangeListener(listener);
		    }
		    /**********建立etl check file list end****************************************************************************/
		    
	        tabCheckFile =EtlTab.addTab(checkFileTable);
	        tabCheckFile.setCaption("ETL_CheckFile");
	    	
	        //1.先取得全部的root channel 		
			if (isRunningOnly.getValue()) etlLogChannelContainer =etlLogChannelSrv.getEtlLogChannelByRunRoot();
			else etlLogChannelContainer =etlLogChannelSrv.getEtlLogChannelByAllRoot();
			etlLogChannelContainer.sort(new Object[]{"logDate"}, new boolean[]{false});
	    	
			final FilterTable rootTable = new FilterTable();
			rootTable.setSizeFull();;
			rootTable.setFilterBarVisible(true);
			rootTable.setId("rootTable");
			rootTable.setStyleName("filtertable");
			rootTable.setSelectable(true);
			rootTable.setImmediate(true); 
			rootTable.setSortEnabled(false);
	
			rootTable.setContainerDataSource(etlLogChannelContainer);
			rootTable.setVisibleColumns((Object[]) new String[] {
					"logDate" ,"loggingObjectType" ,"objectName" ,"status" ,"idBatch","channelId" });
			//選取Table資料時
		    Property.ValueChangeListener listenerTable = new Property.ValueChangeListener() {
		       	public void valueChange(Property.ValueChangeEvent event) {
		       		BeanItem<EtlLogChannel> beanitem=(BeanItem<EtlLogChannel>) etlLogChannelContainer.getItem(rootTable.getValue());
					if (beanitem != null){
						buildDetailTab(beanitem.getBean());
		       		}
		        }
		    };		
		    rootTable.addValueChangeListener(listenerTable);   
	        tabRoot =EtlTab.addTab(rootTable);
	        tabRoot.setCaption("主頁面");
        
	        this.addComponent(EtlTab);
    	}//dbSelect.getValue()
    }
    
    private void buildDetailTab(EtlLogChannel beanitem){
    	
    	final FilterTreeTable filterTable = new FilterTreeTable();
		filterTable.setSizeFull();
        filterTable.setFilterBarVisible(true);
        filterTable.setSelectable(true);
        filterTable.setImmediate(true);
        filterTable.setMultiSelect(true);
        filterTable.setStyleName("filtertable");
        filterTable.setRowHeaderMode(RowHeaderMode.INDEX);
        filterTable.setColumnCollapsingAllowed(true);
        filterTable.setColumnReorderingAllowed(true);  
        filterTable.setContainerDataSource(buildHierarchicalContainer(beanitem.getChannelId()));
        filterTable.sort(new Object[]{"logDate"}, new boolean[]{true});
        filterTable.setVisibleColumns((Object[]) new String[] {
        		"logDate" ,"loggingObjectType" ,"objectName" ,"status" ,"idBatch","channelId" });
		//選取Table資料時
	    Property.ValueChangeListener listenerDetail = new Property.ValueChangeListener() {
	       	public void valueChange(Property.ValueChangeEvent event) {
				Set<String> sValue =(Set<String>) event.getProperty().getValue();
				if (!sValue.isEmpty()){
					Object[] oid =sValue.toArray();
	       			Item item= filterTable.getItem(oid[0]);
	       			if (item != null){
						String loggingObjectType =item.getItemProperty("loggingObjectType").getValue().toString();
						if (loggingObjectType.equals("JOB")) buildJobTab(item);
						if (loggingObjectType.equals("TRANS")) buildTransTab(item);
	       			}
				}
	        }
	    };		
	    filterTable.addValueChangeListener(listenerDetail);        
        
        tabDetil =EtlTab.addTab(filterTable);
        tabDetil.setCaption("Channel-"+beanitem.getObjectName()+"-"+beanitem.getLogDate());
        tabDetil.setClosable(true);
        EtlTab.setSelectedTab(tabDetil);
    }
    
    private void buildJobTab(Item item){
    	String channelId =item.getItemProperty("channelId").getValue().toString();
    	String objectName =item.getItemProperty("objectName").getValue().toString();
    	String logDate =item.getItemProperty("logDate").getValue().toString();
    	final String jobName="Job-"+objectName+"-"+logDate;
		final BeanContainer<String,EtlLogJob> etlLogJobContainer =etlLogJobSrv.getEtlLogJob_PKs(channelId);
    	
		final FilterTable jobTable = new FilterTable();
		jobTable.setSizeFull();
		jobTable.setFilterBarVisible(true);
		jobTable.setId("jobTable");
		jobTable.setStyleName("filtertable");
		jobTable.setSelectable(true);
		jobTable.setImmediate(true); 
		jobTable.setSortEnabled(false);

		jobTable.setContainerDataSource(etlLogJobContainer);
		jobTable.setVisibleColumns((Object[]) new String[] {
				"idJob","jobname","status","errors","startdate","enddate","logdate" });
		//選取Table資料時
	    Property.ValueChangeListener listener = new Property.ValueChangeListener() {
	       	public void valueChange(Property.ValueChangeEvent event) {
	       		BeanItem<EtlLogJob> beanitem=(BeanItem<EtlLogJob>) etlLogJobContainer.getItem(jobTable.getValue());
				if (beanitem != null){
					String FileCode="utf-8";
					String FilePath="";
					AceWindow browse =new AceWindow(themePath ,FileCode ,FilePath ,jobName+".log" ,beanitem.getBean().getLogField());				
	       		}
	        }
	    };		
	    jobTable.addValueChangeListener(listener);    	
    	
        tabJob =EtlTab.addTab(jobTable);
        tabJob.setCaption(jobName);
        tabJob.setClosable(true);
        EtlTab.setSelectedTab(tabJob);
	    
    }
    
    
    private void buildTransTab(Item item){
    	String channelId =item.getItemProperty("channelId").getValue().toString();
    	String objectName =item.getItemProperty("objectName").getValue().toString();
    	String logDate =item.getItemProperty("logDate").getValue().toString();
    	final String transName="Trans-"+objectName+"-"+logDate;
		final BeanContainer<String,EtlLogTrans> etlLogTransContainer =etlLogTransSrv.getEtlLogTrans_PKs(channelId);
    	
		final FilterTable transTable = new FilterTable();
		transTable.setSizeFull();
		transTable.setFilterBarVisible(true);
		transTable.setId("transTable");
		transTable.setStyleName("filtertable");
		transTable.setSelectable(true);
		transTable.setImmediate(true); 
		transTable.setSortEnabled(false);

		transTable.setContainerDataSource(etlLogTransContainer);
		transTable.setVisibleColumns((Object[]) new String[] {
				"idBatch","transname","status","errors","startdate","enddate","logdate" });
		//選取Table資料時
	    Property.ValueChangeListener listener = new Property.ValueChangeListener() {
	       	public void valueChange(Property.ValueChangeEvent event) {
	       		BeanItem<EtlLogTrans> beanitem=(BeanItem<EtlLogTrans>) etlLogTransContainer.getItem(transTable.getValue());
				if (beanitem != null){
					String FileCode="utf-8";
					String FilePath="";
					AceWindow browse =new AceWindow(themePath ,FileCode ,FilePath ,transName+".log" ,beanitem.getBean().getLogField());				
	       		}
	        }
	    };		
	    transTable.addValueChangeListener(listener);    	
    	
        tabJob =EtlTab.addTab(transTable);
        tabJob.setCaption(transName);
        tabJob.setClosable(true);
        EtlTab.setSelectedTab(tabJob);
	    
    }    
    
    /**
     * 建立Tree Container by Root
     * @param rootChannelId
     * @return
     */
    private Container buildHierarchicalContainer(String rootChannelId) {
    	 HierarchicalContainer cont = new HierarchicalContainer();
    	 cont.addContainerProperty("channelId", String.class, null);
    	 cont.addContainerProperty("loggingObjectType", String.class, null);
    	 cont.addContainerProperty("logDate", String.class, null);
    	 cont.addContainerProperty("objectName", String.class, null);
    	 cont.addContainerProperty("idBatch", String.class, null);
    	 cont.addContainerProperty("status", String.class, null);
    	 
		BeanContainer<String,EtlLogChannel> etlLogChannelContainer =etlLogChannelSrv.getEtlLogChannelByRoot(rootChannelId);
		
		FilterTable filterTemp = new FilterTable();
		filterTemp.setContainerDataSource(etlLogChannelContainer);
		
		cont =builderTree(cont ,filterTemp ,rootChannelId);
    	return cont;
    }
    
    /**
     * 建立Tree Container by parent
     * @param cont
     * @param filterTemp
     * @param parentChannelId
     * @return
     */
    @SuppressWarnings("unchecked")
	private HierarchicalContainer builderTree(HierarchicalContainer cont
    					,FilterTable filterTemp
    					,String parentChannelId) {
    	
    	filterTemp.setFilterFieldValue("parentChannelId", parentChannelId);
		Filterable filterable =filterTemp.getFilterable();

    	if (filterable.size() > 0){
			for(java.util.Iterator iter= filterable.getItemIds().iterator();iter.hasNext();){
				Object itemId =iter.next();
				Item item =filterable.getItem(itemId);
				String channelId =item.getItemProperty("channelId").getValue().toString();
				String loggingObjectType =item.getItemProperty("loggingObjectType").getValue().toString();
				String logDate =item.getItemProperty("logDate").getValue().toString();
				String objectName =item.getItemProperty("objectName").getValue().toString();
				String idBatch =item.getItemProperty("idBatch").getValue().toString();
				String status =item.getItemProperty("status").getValue().toString();
				
				cont.addItem(channelId);
				cont.getContainerProperty(channelId, "channelId").setValue(channelId);
				cont.getContainerProperty(channelId, "loggingObjectType").setValue(loggingObjectType);
				cont.getContainerProperty(channelId, "logDate").setValue(logDate);
				cont.getContainerProperty(channelId, "objectName").setValue(objectName);				
				cont.getContainerProperty(channelId, "idBatch").setValue(idBatch);
				cont.getContainerProperty(channelId, "status").setValue(status);
				cont.setParent(channelId, parentChannelId);
				
				cont =builderTree(cont ,filterTemp ,channelId);				
			}
    	}else{
    		cont.setChildrenAllowed(parentChannelId , false);
    	}
    	return cont;
    }
}

