package com.scsb.db.bean; 

import java.io.Serializable;

public class Users implements Serializable {
	private String beanKey="";
	private String userid= "" ;
	private String user_name= "" ;
	private String userlevel= "" ;
	private String title_name= "" ;
	private String cardtitle= "" ;
	private String branchid= "" ;
	private String email= "" ;
	private String deptid= "" ;

	public Users() {
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
		this.beanKey=this.userid+"";
	}

	public String getUserName() { 
		return user_name;
	}

	public void setUserName(String username) { 
  		this.user_name = username; 
	}

	public String getUserlevel() { 
		return userlevel;
	}

	public void setUserlevel(String userlevel) { 
  		this.userlevel = userlevel; 
	}

	public String getTitleName() { 
		return title_name;
	}

	public void setTitleName(String titleName) { 
  		this.title_name = titleName; 
	}

	public String getCardtitle() { 
		return cardtitle;
	}

	public void setCardtitle(String cardtitle) { 
  		this.cardtitle = cardtitle; 
	}

	public String getBranchid() { 
		return branchid;
	}

	public void setBranchid(String branchid) { 
  		this.branchid = branchid; 
	}

	public String getEmail() { 
		return email;
	}

	public void setEmail(String email) { 
  		this.email = email; 
	}

	public String getDeptid() { 
		return deptid;
	}

	public void setDeptid(String deptid) { 
  		this.deptid = deptid; 
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
