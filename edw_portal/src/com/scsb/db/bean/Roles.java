package com.scsb.db.bean; 

import java.io.Serializable;

public class Roles implements Serializable {
	private String beanKey="";
	private String roleid= "" ;
	private String rolename= "" ;
	private String loginprogramid= "" ;
	private String logindefaultlanguage= "" ;
	private String createteller= "" ;
	private String createdate= "" ;
	private String createtime= "" ;

	public Roles() {
	}

	//BeanKey===========================================
	public String getBeanKey() { 
		return beanKey;
	}

	public void setBeanKey(String beanKey) { 
  		this.beanKey = beanKey; 
	}

	//Field Data=========================================
	public String getRoleid() { 
		return roleid;
	}

	public void setRoleid(String roleid) { 
  		this.roleid = roleid; 
		this.beanKey=this.roleid+"";
	}

	public String getRolename() { 
		return rolename;
	}

	public void setRolename(String rolename) { 
  		this.rolename = rolename; 
	}

	public String getLoginprogramid() { 
		return loginprogramid;
	}

	public void setLoginprogramid(String loginprogramid) { 
  		this.loginprogramid = loginprogramid; 
	}

	public String getLogindefaultlanguage() { 
		return logindefaultlanguage;
	}

	public void setLogindefaultlanguage(String logindefaultlanguage) { 
  		this.logindefaultlanguage = logindefaultlanguage; 
	}

	public String getCreateteller() { 
		return createteller;
	}

	public void setCreateteller(String createteller) { 
  		this.createteller = createteller; 
	}

	public String getCreatedate() { 
		return createdate;
	}

	public void setCreatedate(String createdate) { 
  		this.createdate = createdate; 
	}

	public String getCreatetime() { 
		return createtime;
	}

	public void setCreatetime(String createtime) { 
  		this.createtime = createtime; 
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
