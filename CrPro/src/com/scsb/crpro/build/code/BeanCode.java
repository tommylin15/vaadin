package com.scsb.crpro.build.code;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.TextField;

public class BeanCode {
	
	public String Code ="";
	
	String columnName="";
	String fieldName ="";
	String fieldNameUp ="";
	String fieldType  ="";
	String fieldDefault =" \"\" ";
	String DATA_TYPE ="";
	String DECIMAL_DIGITS ="";
	String UpdateKey="";
	String PrimaryKey="";
	String BeanKey="";
	String LogKey="";
	int    columnSize=0;
	
	public BeanCode(String beanName ,String beanPackage ,IndexedContainer container){
		String BR="\r\n";
		String sBeanCode="";
		String sFieldHead="";
		String sFieldBody="";
		
		if (container.size() > 0){
			String sBeanKeyCode="";
			//set bean key
			for(int i=0;i<container.size();i++){
				Item item=container.getItem(container.getIdByIndex(i));
				setItemValue(item);
				if (BeanKey.indexOf(fieldName) >= 0){
					if (sBeanKeyCode.length() == 0 ) sBeanKeyCode+=" this.beanKey=this."+fieldName+"";
					else sBeanKeyCode+="+\"_\"+this."+fieldName+"";
				}
			}
			if (sBeanKeyCode.length() > 0 ) sBeanKeyCode+="+\"\";";
			//set field
			for(int i=0;i<container.size();i++){				
				Item item=container.getItem(container.getIdByIndex(i));
				setItemValue(item);
				
				sFieldHead+="	private "+fieldType+" "+fieldName+"="+fieldDefault+";"+BR;				
				
				sFieldBody+="	public "+fieldType+" get"+fieldNameUp+"() { "+BR;
				sFieldBody+="		return "+fieldName+";"+BR;
				sFieldBody+="	}"+BR+BR;
				sFieldBody+="	public void set"+fieldNameUp+"("+fieldType+" "+fieldName+") { "+BR;
				sFieldBody+="  		this."+fieldName+" = "+fieldName+"; "+BR;
				if (BeanKey.indexOf(fieldName) >= 0){
					sFieldBody+="		"+sBeanKeyCode.trim()+BR;
				}
				sFieldBody+="	}"+BR+BR;				
			}
			sBeanCode+="package "+beanPackage+"; "+BR+BR;
			sBeanCode+="import java.io.Serializable;"+BR+BR;
			sBeanCode+="public class "+beanName+" implements Serializable {"+BR;
			sBeanCode+="	private String beanKey=\"\";"+BR;
			sBeanCode+=		sFieldHead+BR;
			sBeanCode+="	public "+beanName+"() {"+BR;
			sBeanCode+="	}"+BR+BR;
			sBeanCode+="	//BeanKey==========================================="+BR;
			sBeanCode+="	public String getBeanKey() { "+BR;
			sBeanCode+="		return beanKey;"+BR;
			sBeanCode+="	}"+BR+BR;
			sBeanCode+="	public void setBeanKey(String beanKey) { "+BR;
			sBeanCode+="  		this.beanKey = beanKey; "+BR;
			sBeanCode+="	}"+BR+BR;	
			sBeanCode+="	//Field Data========================================="+BR;
			sBeanCode+=		sFieldBody+BR;
			sBeanCode+="}"+BR;	
		}
		this.Code=sBeanCode;
	}
	@SuppressWarnings("unchecked")
	void setItemValue(Item item){	
		this.columnName =(((TextField)item.getItemProperty("COLUMN_NAME").getValue()).getValue()+"").trim();
		this.fieldName =(((TextField)item.getItemProperty("BEAN_NAME").getValue()).getValue()+"").trim();
		this.fieldNameUp =fieldName.substring(0,1).toUpperCase()+fieldName.substring(1);
		this.fieldType  ="String";
		this.DATA_TYPE =(((TextField)item.getItemProperty("DATA_TYPE").getValue()).getValue()+"").trim();
		this.DECIMAL_DIGITS =(((TextField)item.getItemProperty("DECIMAL_DIGITS").getValue()).getValue()+"").trim();
		this.UpdateKey="";
		this.fieldDefault =" \"\" ";
		this.columnSize =Integer.parseInt((((TextField)item.getItemProperty("COLUMN_SIZE").getValue()).getValue()+"").trim());		
		if (DATA_TYPE.equals("NUMBER") && DECIMAL_DIGITS.equals("0") ){
			fieldType="int";
			this.fieldDefault="0";
		}
		if (DATA_TYPE.equals("NUMBER") && !DECIMAL_DIGITS.equals("0") ){
			fieldType="float";
			this.fieldDefault="0";
		}
		if (DATA_TYPE.equals("NUMBER") && DECIMAL_DIGITS.equals("0") && columnSize >=10 ){
			fieldType="long";
			this.fieldDefault="0";
		}		
		if (DATA_TYPE.equals("NUMBER") && !DECIMAL_DIGITS.equals("0") && columnSize >=10 ){
			fieldType="double";
			this.fieldDefault="0";
		}		
    	//拆key
		Collection<String> itemArrayList =new HashSet<String>();
		OptionGroup keyList =(OptionGroup)item.getItemProperty("KEY").getValue();
		itemArrayList =(Collection<String>)keyList.getValue();				
    	Iterator itr = itemArrayList.iterator();
        while (itr.hasNext()) {
        	String value=(String) itr.next();
        	if (value.equals("PrimaryKey")){
        		PrimaryKey+=columnName+",";
        		BeanKey+=fieldName+",";
        	}
        	if (value.equals("LogKey")) LogKey+=fieldName+",";
        }
	}	
}