package com.scsb.crpro.maskcode;

import com.scsb.crpro.build.code.BeanCode;
import com.scsb.crpro.build.code.OneCaseFileCode;
import com.scsb.crpro.build.code.OneFileCode;
import com.scsb.crpro.build.code.PropertiesCode;
import com.scsb.crpro.build.code.ServiceCode;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class BuildCode extends VerticalLayout{

	private static final long serialVersionUID = 3325233253592861773L;

	public Button button_Kettle  = new Button();
	
	public String tableName="";
	public String titleName="";
	
	
	public BuildCode(){
	    this.setSpacing(true);
	    addComponent(new Label(""));
	    
	    button_Kettle= new Button();
	    button_Kettle = new Button("產出 Kettle Mask Java Code");
	    addComponent(button_Kettle); 	    
	}
	public void setTableName(String tableName){
		this.tableName=tableName;
	}
	public void setTitleName(String titleName){
		this.titleName=titleName;
	}	

	public String replaceFormatName(String TableName){
		String FormatName=TableName;
		FormatName =FormatName.toLowerCase();
		FormatName =FormatName.substring(0,1).toUpperCase()+FormatName.substring(1);
		while(true){
			if (FormatName.indexOf("_") >= 0){
				FormatName = FormatName.substring(0,FormatName.indexOf("_"))
				          +FormatName.substring(FormatName.indexOf("_")+1,FormatName.indexOf("_")+2).toUpperCase()
				          +FormatName.substring(FormatName.indexOf("_")+2);
			}else{
				break;
			}
		}
		return FormatName;
	}
	
	/**
	 * 產出Table Java Bean
	 * @param container
	 * @return
	 */
	/*
	public String buildBeanCode(IndexedContainer container){
		KettleMaskCode beanCode =new KettleMaskCode(beanName ,beanPackage ,container);
		return beanCode.Code;
	}
	*/
}
