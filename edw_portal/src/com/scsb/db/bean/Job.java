package com.scsb.db.bean; 

import java.io.Serializable;

public class Job implements Serializable {
	private String beanKey="";
	private String jobName= "" ;

	public Job() {
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
		this.beanKey=this.jobName+"";
	}


}
