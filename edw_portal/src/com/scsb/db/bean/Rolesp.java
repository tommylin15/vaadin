package com.scsb.db.bean; 

import java.io.Serializable;

public class Rolesp implements Serializable {
	private String beanKey="";
	private String rolesid= "" ;
	private String propertyKey= "" ;
	private String propertyValue= "" ;
	private String propertyMemo= "" ;
	private String updateDatetime= "" ;
	private String updateUser= "" ;

	public Rolesp() {
	}

	//BeanKey===========================================
	public String getBeanKey() { 
		return beanKey;
	}

	public void setBeanKey(String beanKey) { 
  		this.beanKey = beanKey; 
	}

	//Field Data=========================================
	public String getRolesid() { 
		return rolesid;
	}

	public void setRolesid(String rolesid) { 
  		this.rolesid = rolesid; 
		this.beanKey=this.rolesid+"_"+this.propertyKey+"_"+this.propertyValue+"";
	}

	public String getPropertyKey() { 
		return propertyKey;
	}

	public void setPropertyKey(String propertyKey) { 
  		this.propertyKey = propertyKey; 
		this.beanKey=this.rolesid+"_"+this.propertyKey+"_"+this.propertyValue+"";
	}

	public String getPropertyValue() { 
		return propertyValue;
	}

	public void setPropertyValue(String propertyValue) { 
  		this.propertyValue = propertyValue; 
		this.beanKey=this.rolesid+"_"+this.propertyKey+"_"+this.propertyValue+"";
	}

	public String getPropertyMemo() { 
		return propertyMemo;
	}

	public void setPropertyMemo(String propertyMemo) { 
  		this.propertyMemo = propertyMemo; 
	}

	public String getUpdateDatetime() { 
		return updateDatetime;
	}

	public void setUpdateDatetime(String updateDatetime) { 
  		this.updateDatetime = updateDatetime; 
	}

	public String getUpdateUser() { 
		return updateUser;
	}

	public void setUpdateUser(String updateUser) { 
  		this.updateUser = updateUser; 
	}


}
