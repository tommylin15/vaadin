package com.scsb.crpro.build.tab;

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

	public Button button_Master  = new Button();
	public Button button_Detail  = new Button();
	public Button button_Service = new Button();
	public Button button_OneFile = new Button();
	public Button button_Properties = new Button();
	
	public String tableName="";
	public String titleName="";
	
	public String beanPackage="";
	public String beanName="";
	
	public String servicePackage="";
	public String serviceName="";

	public String filePackage="";
	public String fileName="";
	public String fileType="";
	
	
	public BuildCode(){
	    // Main top/expanded-bottom layout
	    //this.setSizeFull();
	    this.setSpacing(true);
	    addComponent(new Label(""));
	    
	    button_Master= new Button();
	    button_Master = new Button("產出 Master Table Bean");
	    //button_Master.setStyleName("small default");
	    addComponent(button_Master); 
	    
	    button_Detail = new Button("產出 Detail Table Bean");
	    //button_Detail.setStyleName("small default");
	    addComponent(button_Detail);
	    
	    button_Service = new Button("產出DB I/O Service");
	    //button_Service.setStyleName("small default");
	    addComponent(button_Service);
	    
	    button_OneFile = new Button("產出程式碼");
	    //button_OneFile.setStyleName("small default");
	    addComponent(button_OneFile); 	    
	    
	    button_Properties = new Button("產出Properties for tw");
	    //button_Properties.setStyleName("small default");
	    addComponent(button_Properties); 	    
	}
	public void setTableName(String tableName){
		this.tableName=tableName;
	}
	public void setTitleName(String titleName){
		this.titleName=titleName;
	}	
	public void setBeanPackage(String beanPackage){
		this.beanPackage=beanPackage;
	}
	public void setBeanName(String beanName){
		this.beanName=replaceFormatName(beanName);
	}
	public void setFilePackage(String filePackage){
		this.filePackage=filePackage;
	}
	public void setFileName(String fileName){
		this.fileName=fileName;
	}	
	public void setFileType(String fileType){
		this.fileType=fileType;
	}	
	public void setServicePackage(String ServicePackage){
		this.servicePackage=ServicePackage;
	}
	public void setServiceName(String serviceName){
		this.serviceName=replaceFormatName(serviceName)+"Service";
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
	public String buildBeanCode(IndexedContainer container){
		BeanCode beanCode =new BeanCode(beanName ,beanPackage ,container);
		return beanCode.Code;
	}
	/**
	 * 產出Table Service 單檔
	 * @param container
	 * @return
	 */
	public String buildServiceCode(IndexedContainer container){
		ServiceCode serviceCode =new ServiceCode(serviceName ,servicePackage ,beanName ,beanPackage ,tableName ,fileType ,container);
		return serviceCode.Code;
	}
	
	/**
	 * 產出Java Code 單檔
	 * @param container
	 * @return
	 */
	public String buildOneFileCode(IndexedContainer container){
		OneFileCode oneFileCode =new OneFileCode(serviceName ,servicePackage ,beanName ,beanPackage
												 ,fileName ,filePackage ,titleName ,tableName ,container);
		return oneFileCode.Code;
	}	
	
	/**
	 * 產出Java Code 單檔ByCase
	 * @param container
	 * @return
	 */
	public String buildOneCaseFileCode(IndexedContainer container){
		OneCaseFileCode oneCaseFileCode =new OneCaseFileCode(serviceName ,servicePackage ,beanName ,beanPackage
												 ,fileName ,filePackage ,titleName ,tableName ,container);
		return oneCaseFileCode.Code;
	}
	
	/**
	 * 產出Properties for tw
	 * @param container
	 * @return
	 */
	public String buildProperties(IndexedContainer container){
		PropertiesCode propertiesCode =new PropertiesCode(titleName ,tableName ,container);
		return propertiesCode.Code;
	}	
	/**
	 * 產出Table Service Master-Detail
	 * @param Mastercontainer
	 * @param Detailcontainer
	 * @return
	 */
	/*
	public String buildServiceCode(IndexedContainer Mastercontainer ,IndexedContainer Detailcontainer){
		BeanCode beanCode =new BeanCode(beanName ,beanPackage ,container);
		return beanCode.BeanCode;
	}	
	*/
}
