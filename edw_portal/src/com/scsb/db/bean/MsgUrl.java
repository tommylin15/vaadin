package com.scsb.db.bean; 

import java.io.Serializable;

public class MsgUrl implements Serializable {
	private String beanKey="";
	private String msgUrlx64= "" ;
	private String msgUrl= "" ;
	private String msgSys= "" ;
	private String msgNo= "" ;

	public MsgUrl() {
	}

	//BeanKey===========================================
	public String getBeanKey() { 
		return beanKey;
	}

	public void setBeanKey(String beanKey) { 
  		this.beanKey = beanKey; 
	}

	//Field Data=========================================
	public String getMsgUrlx64() { 
		return msgUrlx64;
	}

	public void setMsgUrlx64(String msgUrlx64) { 
  		this.msgUrlx64 = msgUrlx64; 
		this.beanKey=this.msgUrlx64+"_"+this.msgUrl+"_"+this.msgNo+"";
	}

	public String getMsgUrl() { 
		return msgUrl;
	}

	public void setMsgUrl(String msgUrl) { 
  		this.msgUrl = msgUrl; 
		this.beanKey=this.msgUrlx64+"_"+this.msgUrl+"_"+this.msgNo+"";
	}

	public String getMsgSys() { 
		return msgSys;
	}

	public void setMsgSys(String msgSys) { 
  		this.msgSys = msgSys; 
	}

	public String getMsgNo() { 
		return msgNo;
	}

	public void setMsgNo(String msgNo) { 
  		this.msgNo = msgNo; 
		this.beanKey=this.msgUrlx64+"_"+this.msgUrl+"_"+this.msgNo+"";
	}


}
