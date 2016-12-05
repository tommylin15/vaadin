package com.scsb.db.bean; 

import java.io.Serializable;

public class Usersp implements Serializable {
	private String beanKey="";
	private String userid= "" ;
	private String propertyKey= "" ;
	private String propertyValue= "" ;
	private String propertyMemo= "" ;

	public Usersp() {
	}

	//BeanKey===========================================
	public String getBeanKey() { 
		return beanKey;
	}

	public void setBeanKey(String beanKey) { 
  		this.beanKey = beanKey; 
	}

	//Field Data=========================================
	public String getUserid() { 
		return userid;
	}

	public void setUserid(String userid) { 
  		this.userid = userid; 
		this.beanKey=this.userid+"_"+this.propertyKey+"_"+this.propertyValue+"";
	}

	public String getPropertyKey() { 
		return propertyKey;
	}

	public void setPropertyKey(String propertyKey) { 
  		this.propertyKey = propertyKey; 
		this.beanKey=this.userid+"_"+this.propertyKey+"_"+this.propertyValue+"";
	}

	public String getPropertyValue() { 
		return propertyValue;
	}

	public void setPropertyValue(String propertyValue) { 
  		this.propertyValue = propertyValue; 
		this.beanKey=this.userid+"_"+this.propertyKey+"_"+this.propertyValue+"";
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
