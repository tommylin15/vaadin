package com.scsb.db.bean; 

import java.io.Serializable;

public class PropertyTable implements Serializable {
	private String beanKey="";
	private PropertyBean propertyBean= new PropertyBean() ;

	public PropertyTable() {
	}

	//BeanKey===========================================
	public String getBeanKey() { 
		return beanKey;
	}

	public void setBeanKey(String beanKey) { 
  		this.beanKey = beanKey; 
	}

	//Field Data=========================================
	public PropertyBean getPropertyBean() { 
		return propertyBean;
	}

	public void setPropertyBean(PropertyBean propertyBean) { 
  		this.propertyBean = propertyBean; 
	}
}
