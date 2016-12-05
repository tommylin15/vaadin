package com.scsb.etl;

import org.tepi.filtertable.FilterTable;

import com.scsb.db.bean.EtlCheckfile;
import com.scsb.db.service.EtlCheckfileService;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.DateField;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.TabSheet.Tab;

/**
 * etl_checkfile-屬性設定
 * @author 3471
 *
 */
@SuppressWarnings("serial")
public class etl_checkFile_property extends EtlProperty {
	
	EtlCheckfileService etlCheckFileSrv =new EtlCheckfileService();
	EtlCheckfile           		dataBean ;
	BeanItem<EtlCheckfile> 	dataItem ;	
	FieldGroup          dataBinder;
	
	TextField filename =new TextField();
	TextField shortFilename =new TextField();
	TextField path =new TextField();
	TextField type =new TextField();
	TextField busDate =new TextField();
	TextField nowDate =new TextField();
	TextField lastDate =new TextField();
	TextField status =new TextField();
	TextField endDate =new TextField();
	TextField updateMode =new TextField();
	
	NativeSelect groupMode =new NativeSelect();
	
	boolean IsInsert=false;
	EtlCheckfile srcBean =new EtlCheckfile();
	
	TabSheet     mainTab    =new TabSheet();
	FilterTable fromTab =null;
	Tab  tabProperty; 
	String sid;		
	/**
	 * etl_checkfile-屬性設定-主畫面
	 */
	public etl_checkFile_property(String dbPool  ,final EtlCheckfile srcBean ,boolean IsInsert ,TabSheet mainTabx ,FilterTable  fromTab) {
		this.srcBean=srcBean;
		this.etlCheckFileSrv.setDbPool(dbPool);
		DataBinderFactory();
		this.mainTab=mainTabx;
		this.fromTab=fromTab;
		
		//欄位編輯====================================================================
		filename.setId("filename");
		filename.setCaption("filename");
		filename.setImmediate(true);
		filename.setIcon(FontAwesome.LOCK);
		filename.setReadOnly(true);
		getFormLayout().addComponent(filename);
		
		shortFilename.setId("shortFilename");
		shortFilename.setCaption("shortFilename");
		shortFilename.setImmediate(true);
		shortFilename.setIcon(FontAwesome.LOCK);
		shortFilename.setReadOnly(true);
		getFormLayout().addComponent(shortFilename);
		
		path.setId("path");
		path.setCaption("path");
		path.setImmediate(true);
		path.setIcon(FontAwesome.LOCK);
		path.setReadOnly(true);
		getFormLayout().addComponent(path);
		
		type.setId("type");
		type.setCaption("type");
		type.setImmediate(true);
		type.setIcon(FontAwesome.LOCK);
		type.setReadOnly(true);
		getFormLayout().addComponent(type);
		
		busDate.setId("busDate");
		busDate.setCaption("busDate");
		busDate.setImmediate(true);
		busDate.setIcon(FontAwesome.LOCK);
		busDate.setReadOnly(true);
		getFormLayout().addComponent(busDate);

		nowDate.setId("nowDate");
		nowDate.setCaption("nowDate");
		nowDate.setImmediate(true);
		nowDate.setIcon(FontAwesome.LOCK);
		nowDate.setReadOnly(true);
		getFormLayout().addComponent(nowDate);

		lastDate.setId("lastDate");
		lastDate.setCaption("lastDate");
		lastDate.setImmediate(true);
		lastDate.setIcon(FontAwesome.LOCK);
		lastDate.setReadOnly(true);
		getFormLayout().addComponent(lastDate);
		
		status.setId("status");
		status.setCaption("status");
		status.setImmediate(true);
		status.setIcon(FontAwesome.EDIT);
		status.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				saveBu.setVisible(true);
			}
    	});			
		getFormLayout().addComponent(status);

		endDate.setId("endDate");
		endDate.setCaption("endDate");
		endDate.setImmediate(true);
		endDate.setIcon(FontAwesome.LOCK);
		endDate.setReadOnly(true);
		getFormLayout().addComponent(endDate);

		updateMode.setId("updateMode");
		updateMode.setCaption("updateMode");
		updateMode.setImmediate(true);
		updateMode.setIcon(FontAwesome.LOCK);
		updateMode.setReadOnly(true);
		getFormLayout().addComponent(updateMode);
		
    	saveBu.addClickListener(
        	    new ClickListener() {
        			@Override
        			public void buttonClick(ClickEvent event) {
        				onSaveButton();
        			}
        		}
        	);		
		
		String tabCaption ="-屬性設定";
		if (srcBean != null)  tabCaption =srcBean.getBeanKey()+tabCaption;
	    tabProperty =mainTab.addTab(getContent() ,tabCaption);
	    tabProperty.setClosable(true);
	    tabProperty.setIcon(FontAwesome.EDIT);
	    mainTab.setSelectedTab(tabProperty);      	
	}
	
	 /**
     *  執行儲存動作
	 * @param codeKind 
     */
    public void onSaveButton(){
		String errMsg="";
		try {
				dataBinder.commit();
		      	if (!etlCheckFileSrv.updateEtlCheckfileStatus(dataBean)){
					errMsg=etlCheckFileSrv.ErrMsg;
		        }
		} catch (Exception e1) {
			e1.printStackTrace();
			errMsg=e1.getMessage();
		}				
		if (!errMsg.equals("")){
			showMsgError(errMsg);
		}else{
			showMsgSuccess();
			mainTab.removeTab(this.tabProperty);
			mainTab.setSelectedTab(fromTab);
			fromTab.getItem(dataBean.getBeanKey()).getItemProperty("status").setValue(dataBean.getStatus());
		}
    }//	onSaveButton			
    
	 /**
	  * Data的Field Factory
	  * @author 3471
	  *
	  */
    private void DataBinderFactory(){
		dataBean =etlCheckFileSrv.getEtlCheckfile_PK(srcBean);
		
		dataItem = new BeanItem<EtlCheckfile>(dataBean);
		dataBinder = new FieldGroup(dataItem);
		dataBinder.setBuffered(true);    	    		
		dataBinder.bind(filename, "filename");
		dataBinder.bind(shortFilename, "shortFilename");
		dataBinder.bind(path, "path");
		dataBinder.bind(type, "type");
		dataBinder.bind(busDate, "busDate");
		dataBinder.bind(nowDate, "nowDate");
		dataBinder.bind(lastDate, "lastDate");
		dataBinder.bind(status, "status");
		dataBinder.bind(endDate, "endDate");
		dataBinder.bind(updateMode, "updateMode");

	 }	
}
