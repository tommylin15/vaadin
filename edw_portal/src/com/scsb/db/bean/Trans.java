package com.scsb.db.bean; 

import java.io.Serializable;

public class Trans implements Serializable {
	private String beanKey="";
	private String groupid= "" ;
	private String groupname= "" ;
	private String grouptype= "" ;
	private String groupmode= "" ;

	public Trans() {
	}

	//BeanKey===========================================
	public String getBeanKey() { 
		return beanKey;
	}

	public void setBeanKey(String beanKey) { 
  		this.beanKey = beanKey; 
	}

	//Field Data=========================================
	public String getGroupid() { 
		return groupid;
	}

	public void setGroupid(String groupid) { 
  		this.groupid = groupid; 
		this.beanKey=this.groupid+"";
	}

	public String getGroupname() { 
		return groupname;
	}

	public void setGroupname(String groupname) { 
  		this.groupname = groupname; 
	}

	public String getGrouptype() { 
		return grouptype;
	}

	public void setGrouptype(String grouptype) { 
  		this.grouptype = grouptype; 
	}

	public String getGroupmode() { 
		return groupmode;
	}

	public void setGroupmode(String groupmode) { 
  		this.groupmode = groupmode;
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
