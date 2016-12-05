package com.scsb.db.bean; 

import java.io.Serializable;

public class Usersf implements Serializable {
	private String beanKey="";
	private String programid= "" ;
	private String userid= "" ;

	public Usersf() {
	}

	//BeanKey===========================================
	public String getBeanKey() { 
		return beanKey;
	}

	public void setBeanKey(String beanKey) { 
  		this.beanKey = beanKey; 
	}

	//Field Data=========================================
	public String getProgramid() { 
		return programid;
	}

	public void setProgramid(String programid) { 
  		this.programid = programid; 
		this.beanKey=this.programid+"_"+this.userid+"";
	}

	public String getUserid() { 
		return userid;
	}

	public void setUserid(String userid) { 
  		this.userid = userid; 
		this.beanKey=this.programid+"_"+this.userid+"";
	}


}
