package com.scsb.db.bean; 

import java.io.Serializable;

public class ScsbTitle implements Serializable {
	private String beanKey="";
	private String titleName= "" ;

	public ScsbTitle() {
	}

	//BeanKey===========================================
	public String getBeanKey() { 
		return beanKey;
	}

	public void setBeanKey(String beanKey) { 
  		this.beanKey = beanKey; 
	}

	//Field Data=========================================
	public String getTitleName() { 
		return titleName;
	}

	public void setTitleName(String titleName) { 
  		this.titleName = titleName; 
		this.beanKey=this.titleName+"";
	}


}
