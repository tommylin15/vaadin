package com.scsb.crpro.build.tab;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

import com.scsb.crpro.AceWindow;
import com.scsb.crpro.CrproLayout;
import com.scsb.db.customfield.FieldConstraint;
import com.scsb.db.customfield.PropertyField;
import com.scsb.crpro.fileio.INIReader;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.TabSheet.Tab;

public class CreatePrg extends CrproLayout{

	private static final long serialVersionUID = 3325233253592861773L;

	ConfigSet   		Config      =new ConfigSet();
	ChooseTable 		masterTable;
	ChooseTable 		detailTable;
	ONE_QueryForm   	oneQForm   =new ONE_QueryForm();
	BuildCode   		buildCode   =new BuildCode();

	INIReader 			reader =new INIReader();

	TabSheet     MainTab    =new TabSheet(); 
	Tab tabConfig;
	Tab masterTab;
	Tab detailTab;
	Tab oneQFormTab;
	Tab buildCodeTab;
	
	String themePath="/"+systemProps.getProperty("SYSTEM_NAME")+"/static/ace";
	
	String tableType="ONE";
	
	public CreatePrg(){
		this.setId("CreatePrg");
        setSpacing(true);
        masterTable =new ChooseTable("MasterTable列表");
        detailTable =new ChooseTable("DetailTable列表");
	    
	    MainTab.setSizeFull();
        this.addComponent(MainTab); 
        this.setComponentAlignment(MainTab, Alignment.TOP_LEFT);
        this.setExpandRatio(MainTab, 1.0f);
        
        onSetConfigSet();
        onSetMasterTable();
        onSetDetailTable();
        onBuildCode();
        onSetoneQForm();
        
        //on last
        onSelectType();        
	}
	
	void onSelectType(){
		if (tableType.equals("ONE")){
			masterTab.setVisible(true);
			oneQForm.setVisible(true);
			buildCode.button_OneFile.setVisible(true);
			
			detailTab.setVisible(false);
			buildCode.button_Detail.setVisible(false);
			
		}
		if (tableType.equals("MD")){
			masterTab.setVisible(true);
			detailTab.setVisible(true);
			buildCode.button_Detail.setVisible(true);
			
			oneQForm.setVisible(false);	
			buildCode.button_OneFile.setVisible(false);
		}		
	}
	void onSetConfigSet(){
        //環境設定
        Config.TypeCombo.addValueChangeListener(new Property.ValueChangeListener() {
        	public void valueChange(com.vaadin.data.Property.ValueChangeEvent event) {
        		tableType =(String) Config.TypeCombo.getValue();
        		onSelectType();
        	}
        });
        tabConfig =MainTab.addTab(Config);
        tabConfig.setCaption("環境設定");
        
        //儲存設定
	    Button button_Save = Config.SaveBu;
	    button_Save.addClickListener(new  Button.ClickListener(){
			public void buttonClick(ClickEvent event) {
				if (!Config.FileText.getValue().equals("")){
					onSave();
				}else{
					
				}
			}
		});
	    //載入設定
	    final ByteArrayOutputStream os = new ByteArrayOutputStream(10240);
	    Config.LoadBu = new Upload("",null);
	    
	    Config.LoadBu.addSucceededListener(new SucceededListener() {
            public void uploadSucceeded(SucceededEvent event) {
                StreamSource source = new StreamSource() {
                    public InputStream getStream() {
                        return new ByteArrayInputStream(os.toByteArray());
                    }
                };
                
                reader.setINIFile(source.getStream());
                Config.setObjValue(reader,"Config");
			    masterTable.setObjValue(reader,"ChooseMasterTable");
			    detailTable.setObjValue(reader,"ChooseDetailTable");	                
                
            }
        });	    
	    
	    class FileUploader implements Receiver{
	       public String filename;
	       
           public OutputStream receiveUpload(String filename, String mimeType) {
                this.filename = filename;
                os.reset(); // If re-uploading
                return os;
            }	        
	    };
	    final FileUploader receiver = new FileUploader(); 
	    Config.LoadBu.setReceiver(receiver);
        Config.LoadBu.setButtonCaption("載入");
        Config.LoadBu.setImmediate(true);
        Config.hlayout.addComponent(Config.LoadBu);

	}
	void onSetMasterTable(){
		masterTab =MainTab.addTab(masterTable);
		masterTab.setCaption("選擇MasterTable");  
		masterTable.table.setId("masterTable");
	}
	void onSetDetailTable(){
		detailTab =MainTab.addTab(detailTable);
		detailTab.setCaption("選擇DetailTable");
		detailTable.table.setId("detailTable");
	}
	void onSetoneQForm(){
		oneQFormTab =MainTab.addTab(oneQForm);
		oneQFormTab.setCaption("SAS-查詢畫面");
		/*
	    Button button_Reload = oneQForm.button_Reload;
	    button_Reload.addClickListener(new  Button.ClickListener(){
			public void buttonClick(ClickEvent event) {
				oneQForm.onReLoad();
			}
		});
		*/		
	}	
	void onBuildCode(){
		buildCodeTab =MainTab.addTab(buildCode);
		buildCodeTab.setCaption("產出JavaCode");
		
		//MasterBean =================================================================
		Button crMasterBean =buildCode.button_Master;
		crMasterBean.addClickListener(new  Button.ClickListener(){
			public void buttonClick(ClickEvent event) {
				buildCode.setBeanPackage(Config.BeanText.getValue()+"");
				buildCode.setBeanName(masterTable.dataCombo.getValue()+"");
				String beanCode =buildCode.buildBeanCode(masterTable.container);
				String FileCode="utf-8";
				String FilePath="";
				AceWindow browse =new AceWindow(themePath ,FileCode ,FilePath ,buildCode.beanName+".java" ,beanCode);				
			}
		});	
		
		//DetailBean =================================================================
		Button crDetailBean =buildCode.button_Detail;
		crDetailBean.addClickListener(new  Button.ClickListener(){
			public void buttonClick(ClickEvent event) {
				buildCode.setBeanPackage(Config.BeanText.getValue()+"");
				buildCode.setBeanName(detailTable.dataCombo.getValue()+"");
				String beanCode =buildCode.buildBeanCode(detailTable.container);
				String FileCode="utf-8";
				String FilePath="";
				AceWindow browse =new AceWindow(themePath  ,FileCode ,FilePath ,buildCode.beanName+".java" ,beanCode);
			}
		});	
		
		//Srvice  ===================================================================
		Button crService =buildCode.button_Service;
		crService.addClickListener(new  Button.ClickListener(){
			public void buttonClick(ClickEvent event) {
				buildCode.setServicePackage(Config.ServiceText.getValue()+"");
				buildCode.setServiceName(masterTable.dataCombo.getValue()+"");
				buildCode.setBeanPackage(Config.BeanText.getValue()+"");
				buildCode.setBeanName(masterTable.dataCombo.getValue()+"");
				buildCode.setTableName(masterTable.dataCombo.getValue()+"");
				
				String serviceCode ="";
				if (tableType.equals("ONE")){
					serviceCode=buildCode.buildServiceCode(masterTable.container);
				}else{
					//serviceCode=buildCode.buildServiceCode(MasterContainer ,DetailContainer);
				}
				String FileCode="utf-8";
				String FilePath="";
				AceWindow browse =new AceWindow(themePath  ,FileCode ,FilePath ,serviceCode+".java" ,serviceCode);
			}
		});		
		//產出程式碼  ===================================================================
		Button crOneFile =buildCode.button_OneFile;
		crOneFile.addClickListener(new  Button.ClickListener(){
			public void buttonClick(ClickEvent event) {
				buildCode.setServicePackage(Config.ServiceText.getValue()+"");
				buildCode.setServiceName(masterTable.dataCombo.getValue()+"");
				buildCode.setBeanPackage(Config.BeanText.getValue()+"");
				buildCode.setBeanName(masterTable.dataCombo.getValue()+"");
				buildCode.setFilePackage(Config.FilePackage.getValue()+"");
				buildCode.setFileName(Config.FileText.getValue()+"");
				buildCode.setFileType(Config.TypeCombo.getValue()+"");
				buildCode.setTableName(masterTable.dataCombo.getValue()+"");
				buildCode.setTitleName(Config.TitleText.getValue()+"");
				
				String OneFileCode ="";
				if (tableType.equals("ONE")){
					OneFileCode=buildCode.buildOneFileCode(masterTable.container);
				}else if (tableType.equals("ONE_CASE")){
					OneFileCode=buildCode.buildOneCaseFileCode(masterTable.container);
				}else{
					//serviceCode=buildCode.buildServiceCode(MasterContainer ,DetailContainer);
				}
				String FileCode="utf-8";
				String FilePath="";
				AceWindow browse =new AceWindow(themePath  ,FileCode ,FilePath ,buildCode.fileName+".java" ,OneFileCode);
			}
		});	
		//Properties ===================================================================
		Button crProperties =buildCode.button_Properties;
		crProperties.addClickListener(new  Button.ClickListener(){
			public void buttonClick(ClickEvent event) {
				buildCode.setTableName(masterTable.dataCombo.getValue()+"");
				buildCode.setTitleName(Config.TitleText.getValue()+"");
				String propertiesCode ="";
				if (tableType.equals("ONE")){
					propertiesCode=buildCode.buildProperties(masterTable.container);
				}else{
					//serviceCode=buildCode.buildServiceCode(MasterContainer ,DetailContainer);
				}
				String FileCode="utf-8";
				String FilePath="";
				AceWindow browse =new AceWindow(themePath  ,FileCode ,FilePath ,buildCode.fileName+".properties" ,propertiesCode);
			}
		});		
	}
	
	/**
	 * 存檔
	 */
	void onSave(){
		String sALLset="";
		
		//=====config===========================================================================================
		sALLset+=writeString(Config ,"Config");
		//=====MasterTable======================================================================================
		sALLset+=writeString(masterTable ,"ChooseMasterTable");
		//=====DetailTable======================================================================================
		sALLset+=writeString(detailTable ,"ChooseDetailTable");
		String FileCode="utf-8";
		String FilePath="";
		AceWindow browse =new AceWindow(themePath  ,FileCode ,FilePath ,Config.FileText.getValue()+".properties" ,sALLset);
	}
	
	/**
	 * 解析Layout for VerticalLayout
	 * @param rootComp
	 * @param sName
	 * @return
	 */
	String writeString(VerticalLayout rootComp ,String sName){
		String BR="\r\n";
		String sConfig="";	
		sConfig+=BR+"["+sName+"]"+BR;
		for (Iterator<Component> iterator =rootComp.getComponentIterator();iterator.hasNext();){
			Component comp =(Component)iterator.next();
			sConfig+=getFieldInfo(comp);
		}
		return sConfig;
	}
	/**
	 * 解析Table
	 * @param rootTable
	 * @param tableName
	 * @return
	 */
	String writeString(Table rootTable ,String tableName){
		String BR="\r\n";
		String sConfig="";	
		sConfig+=BR+"["+tableName+"]"+BR;
		sConfig+="TableRows="+rootTable.size()+BR;
		Collection colltable =rootTable.getItemIds();
		int irow=0;
		for (Iterator<String> iterator =colltable.iterator();iterator.hasNext();){
			String itemId =(String)iterator.next();
			irow++;
			Item item =rootTable.getItem(itemId);
			Collection collitem =item.getItemPropertyIds();
			for (Iterator<String> iter =collitem.iterator();iter.hasNext();){
				String PropertyId =iter.next()+"";
				Property prop =item.getItemProperty(PropertyId);
				//欄位屬性另外處理
				if (PropertyId.equals("DataField") || PropertyId.equals("QueryField")){
					PropertyField propField =(PropertyField)prop.getValue();
					sConfig+=writeString(PropertyId+"."+irow ,propField.getBean());
				}else{
					sConfig+=PropertyId+"."+irow+"="+prop.getValue()+BR;					
				}
			}
		}
		return sConfig;
	}
	/**
	 * 解析FieldConstraint for table
	 * @param rootTable
	 * @param irow
	 * @param fieldCons
	 * @return
	 */
	String writeString(String PropId ,FieldConstraint fieldCons){
		String BR="\r\n";
		String sConfig="";	
		Class type = fieldCons.getClass();
		try {
			Map map = BeanUtils.describe(fieldCons);			
			for(Iterator<String> iter=map.keySet().iterator();iter.hasNext();){
				String key =iter.next();
				if (!key.equals("class")){
					Object value =map.get(key);
					sConfig+=PropId+"."+key+"="+value+BR;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sConfig;
	}	
	/**
	 * 取得Component的值
	 * @param comp
	 * @return
	 */
	String getFieldInfo(Component comp){
		String BR="\r\n";
		String sInfo="";
		if (comp.getId() != null){
			String className=comp.getClass().getSimpleName();			
			sInfo=comp.getId().trim()+"=";
			if (className.equals("TextField")){
				TextField tObj =(TextField)comp;
				if (tObj.getValue()==null ) sInfo+=BR;
				else	sInfo+=tObj.getValue()+BR;
			}
			if (className.equals("ComboBox")){
				ComboBox tObj =(ComboBox)comp;
				if (tObj.getValue()==null ) sInfo+=BR;
				else	sInfo+=tObj.getValue()+BR;
			}
			//解析Table
			if (className.equals("Table")){
				Table tObj =(Table)comp;
				if (tObj.size()==0 ) sInfo="Table="+BR;
				else{
					sInfo ="Table="+tObj.getId()+BR;
					sInfo+=writeString(tObj ,tObj.getId());
				}
			}			
		}
		return sInfo;
	}
}
