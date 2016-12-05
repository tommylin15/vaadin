package com.scsb.db.bean; 

import java.io.Serializable;

public class Dept implements Serializable {
	private String beanKey="";
	private String ou= "" ;
	private String propertyKey= "" ;
	private String propertyValue= "" ;
	private String propertyMemo= "" ;

	public Dept() {
	}

	//BeanKey===========================================
	public String getBeanKey() { 
		return beanKey;
	}

	public void setBeanKey(String beanKey) { 
  		this.beanKey = beanKey; 
	}

	//Field Data=========================================
	public String getOu() { 
		return ou;
	}

	public void setOu(String ou) { 
  		this.ou = ou; 
		this.beanKey=this.ou+"_"+this.propertyKey+"_"+this.propertyValue+"";
	}

	public String getPropertyKey() { 
		return propertyKey;
	}

	public void setPropertyKey(String propertyKey) { 
  		this.propertyKey = propertyKey; 
		this.beanKey=this.ou+"_"+this.propertyKey+"_"+this.propertyValue+"";
	}

	public String getPropertyValue() { 
		return propertyValue;
	}

	public void setPropertyValue(String propertyValue) { 
  		this.propertyValue = propertyValue; 
		this.beanKey=this.ou+"_"+this.propertyKey+"_"+this.propertyValue+"";
	}

	public String getPropertyMemo() { 
		return propertyMemo;
	}

	public void setPropertyMemo(String propertyMemo) { 
  		this.propertyMemo = propertyMemo; 
	}

	private String updateDatetime= "" ;
	public String getUpdateDatetime() { 
		return updateDatetime;
	}

	public void setUpdateDatetime(String updateDatetime) { 
  		this.updateDatetime = updateDatetime; 
	}	
	
	private String updateUser= "" ;
	public String getUpdateUser() { 
		return updateUser;
	}

	public void setUpdateUser(String updateUser) { 
  		this.updateUser = updateUser; 
	}
}
