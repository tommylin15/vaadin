package com.scsb.db.bean; 

import java.io.Serializable;

public class ActionItem implements Serializable {
	private String beanKey="";
	private String actionCode= "" ;
	private String actionName= "" ;

	public ActionItem() {
	}

	//BeanKey===========================================
	public String getBeanKey() { 
		return beanKey;
	}

	public void setBeanKey(String beanKey) { 
  		this.beanKey = beanKey; 
	}

	//Field Data=========================================
	public String getActionCode() { 
		return actionCode;
	}

	public void setActionCode(String actionCode) { 
  		this.actionCode = actionCode; 
		this.beanKey=this.actionCode+"";
	}

	public String getActionName() { 
		return actionName;
	}

	public void setActionName(String actionName) { 
  		this.actionName = actionName; 
	}


}
