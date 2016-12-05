package com.scsb.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;
import java.util.ResourceBundle;

import com.vaadin.data.Property;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.ui.Field;

/**
 * 檢核欄位 
 * 目前僅檢查有*註記,且為String ,Integer ,Long ,Hashtable<String,String> ,Collection 的資料與 CommitException
 * @author :Tommy Lin
 * @version : 1.0
 */
public class FieldCheck {
	private  FieldGroup 	FieldBinder; 
	private  ResourceBundle loanMessage;
	private  Properties     screenProps;
	private  String         field_validator	;
	
	public FieldCheck(){
		
	}
	/**
	 * 檢核欄位
	 * @param FieldBinder
	 * @param loanMessage
	 * @param screenProps
	 * @return
	 */
	public String  checkBinderField(FieldGroup FieldBinder 
			,ResourceBundle loanMessage
			,Properties     screenProps
			){
		return checkBinderField(FieldBinder ,loanMessage ,screenProps ,"field" ,new HashSet<String>());
	}
	/**
	 * 檢核欄位
	 * @param FieldBinder
	 * @param loanMessage
	 * @param screenProps
	 * @param field_validator
	 * @return
	 */
	public String  checkBinderField(FieldGroup FieldBinder 
			,ResourceBundle loanMessage
			,Properties     screenProps
			,String         field_validator     
			){
		return checkBinderField(FieldBinder ,loanMessage ,screenProps ,field_validator ,new HashSet<String>());
	}
	/**
	 * 檢核欄位
	 * @param FieldBinder
	 * @param loanMessage
	 * @param screenProps
	 * @param field_validator
	 * @param noCheckField
	 * @return
	 */
	public String  checkBinderField(FieldGroup FieldBinder 
			,ResourceBundle loanMessage
			,Properties     screenProps
			,String         field_validator 
			,Collection<String>     noCheckField
			){		
		this.FieldBinder =FieldBinder;
		this.loanMessage =loanMessage;
		this.screenProps =screenProps;
		this.field_validator =field_validator;    
		
	   String message="";	     	   
    	//檢核欄位資料
	   try {
		   FieldBinder.commit();
		} catch (CommitException e) {
			message+=loanMessage.getString("message_binderCommit_error")+"<br>";
		}
        //檢查必填欄位
		for(Iterator<Field<?>> iter =FieldBinder.getFields().iterator();iter.hasNext();){
			Field field =iter.next();
			String sId =field.getId();
			if (field.isRequired()){
				boolean haveCheck=true;
				for(Iterator<String> iterNoCheck =noCheckField.iterator() ;iterNoCheck.hasNext();){
					if (sId.equals(iterNoCheck.next())) haveCheck=false;
				}				
				if (haveCheck) message+=checkValue(sId);
			}
		}
    	return message;
	}
	
	private String checkValue(String sId){
		String message="";
		Property property =FieldBinder.getItemDataSource().getItemProperty(sId);
		String checkType =property.getType()+"";
		if (checkType.indexOf("Object") > -1){
			if (property.getValue().equals(null)){
				message+=screenProps.getProperty("field_validator")+screenProps.getProperty(field_validator+"_"+sId)+"<br>";
			}								
		}
		if (checkType.indexOf("String") > -1){
			if (property.getValue().equals("")){
				message+=screenProps.getProperty("field_validator")+screenProps.getProperty(field_validator+"_"+sId)+"<br>";
			}								
		}
		if (checkType.indexOf("Integer") > -1){			
			if (property.getValue().equals(0)){
				message+=screenProps.getProperty("field_validator")+screenProps.getProperty(field_validator+"_"+sId)+"<br>";
			}								
		}
		if (checkType.indexOf("Long") > -1){
			if (property.getValue().equals(new Long(0))){
				message+=screenProps.getProperty("field_validator")+screenProps.getProperty(field_validator+"_"+sId)+"<br>";
			}				
		}
		if (checkType.indexOf("Collection") > -1){
			Collection collData =(Collection)property.getValue();
			if (collData.size()==0){
				message+=screenProps.getProperty("field_validator")+screenProps.getProperty(field_validator+"_"+sId)+"<br>";
			}				
		}
		if (checkType.indexOf("Hashtable") > -1){
			Hashtable hData =(Hashtable)property.getValue();
			for(Iterator iter=hData.keySet().iterator();iter.hasNext();){
				String id =(String)iter.next();
				if (FieldBinder.getItemDataSource().getItemProperty(id).equals("")){
					message+=screenProps.getProperty("field_validator")+screenProps.getProperty(field_validator+"_"+sId)+"<br>";
					break;
				}
			}				
		}		
		return message;
	}
	
	public static HashMap getValue(FieldGroup FieldBinder){	
		HashMap fieldMap =new HashMap();
		for(Iterator<Field<?>> iter =FieldBinder.getFields().iterator();iter.hasNext();){
			Field field =iter.next();
			fieldMap.put(field.getId(), field.getValue());
		}
		return fieldMap;
	}
	
	public static FieldGroup copyBinder(FieldGroup binBinder ,FieldGroup srcBinder){
		for(Iterator<Field<?>> iter =srcBinder.getFields().iterator();iter.hasNext();){
			Field field =iter.next();
			binBinder.bind(field, field.getId());
		}
		return binBinder;
	}
}