package com.scsb.db.bean; 

import java.io.Serializable;

public class Jobp implements Serializable {
	private String beanKey="";
	private String jobName= "" ;
	private String propertyKey= "" ;
	private String propertyValue= "" ;
	private String propertyMemo= "" ;
	private String updateDatetime= "" ;
	private String updateUser= "" ;

	public Jobp() {
	}

	//BeanKey===========================================
	public String getBeanKey() { 
		return beanKey;
	}

	public void setBeanKey(String beanKey) { 
  		this.beanKey = beanKey; 
	}

	//Field Data=========================================
	public String getJobName() { 
		return jobName;
	}

	public void setJobName(String jobName) { 
  		this.jobName = jobName; 
		this.beanKey=this.jobName+"_"+this.propertyKey+"_"+this.propertyValue+"";
	}

	public String getPropertyKey() { 
		return propertyKey;
	}

	public void setPropertyKey(String propertyKey) { 
  		this.propertyKey = propertyKey; 
		this.beanKey=this.jobName+"_"+this.propertyKey+"_"+this.propertyValue+"";
	}

	public String getPropertyValue() { 
		return propertyValue;
	}

	public void setPropertyValue(String propertyValue) { 
  		this.propertyValue = propertyValue; 
		this.beanKey=this.jobName+"_"+this.propertyKey+"_"+this.propertyValue+"";
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
