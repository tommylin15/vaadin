package com.scsb.db.bean; 

import java.io.Serializable;

public class PropertyBean implements Serializable {
	private String propertyKey= "" ;
	private String propertyValue= "" ;
	private String propertyMemo= "" ;

	public PropertyBean() {
	}

	//Field Data=========================================
	public String getPropertyKey() { 
		return propertyKey;
	}

	public void setPropertyKey(String propertyKey) { 
  		this.propertyKey = propertyKey; 
	}

	public String getPropertyValue() { 
		return propertyValue;
	}

	public void setPropertyValue(String propertyValue) { 
  		this.propertyValue = propertyValue; 
	}

	public String getPropertyMemo() { 
		return propertyMemo;
	}

	public void setPropertyMemo(String propertyMemo) { 
  		this.propertyMemo = propertyMemo; 
	}


}
