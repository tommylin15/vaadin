package com.scsb.crpro.build.tab;

import java.util.HashMap;

import com.scsb.crpro.fileio.INIReader;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;

public class ConfigSet extends VerticalLayout{

	private static final long serialVersionUID = 3325233253592861773L;

	public ComboBox TypeCombo = new ComboBox("請選擇作業模式");
	public TextField TitleText =new TextField("Title Name");
	public TextField FileText =new TextField("File Name");
	public TextField BeanText =new TextField("BeanPackage");
	public TextField ServiceText =new TextField("ServicePackage");
	public TextField FilePackage =new TextField("FilePackage");
	
	public HorizontalLayout hlayout = new HorizontalLayout();
	public Button SaveBu =new Button("儲存");
	//public MultiFileUpload LoadBu =  null;
	public Upload LoadBu = null;
	
	@SuppressWarnings("serial")
	public ConfigSet(){
	    // Main top/expanded-bottom layout
	    //this.setSizeFull();
	    this.setSpacing(true);
	    FileText.setId("FileText");
	    addComponent(FileText); 
	    
	    TitleText.setId("TitleText");
	    addComponent(TitleText);	    
	    
	    BeanText.setValue("com.scsb.db.bean");
	    BeanText.setId("BeanText");
	    addComponent(BeanText); 

	    ServiceText.setValue("com.scsb.db.service");
	    ServiceText.setId("ServiceText");
	    addComponent(ServiceText); 
	    
	    FilePackage.setValue("com.scsb.ui.trans");
	    FilePackage.setId("FilePackage");
	    addComponent(FilePackage);	    
	    
	    TypeCombo.setImmediate(true);
	    TypeCombo.addItem("ONE");
	    TypeCombo.setItemCaption("ONE", "單檔");
	    TypeCombo.addItem("MD");
	    TypeCombo.setItemCaption("MD", "MasterDetail");
	    TypeCombo.select("ONE");
	    TypeCombo.setNullSelectionAllowed(false);
	    TypeCombo.setId("TypeCombo");
	    addComponent(TypeCombo); 
	    
	    
		hlayout.setSpacing(true);
		hlayout.setSizeUndefined();
		hlayout.addComponent(SaveBu);
		
		addComponent(hlayout);
	}
	public void setObjValue(INIReader iniReader ,String sConfig){
		HashMap<String, String> HashData =iniReader.Read(sConfig);
		TypeCombo.setValue(HashData.get("TypeCombo")+"");
		FileText.setValue(HashData.get("FileText")+"");
		BeanText.setValue(HashData.get("BeanText")+"");
		ServiceText.setValue(HashData.get("ServiceText")+"");
		TitleText.setValue(HashData.get("TitleText")+"");
		FilePackage.setValue(HashData.get("FilePackage")+"");
	}
}
